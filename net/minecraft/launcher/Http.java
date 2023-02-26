package net.minecraft.launcher;

import ch.qos.logback.core.CoreConstants;
import com.google.common.base.Strings;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipInputStream;
import javax.net.ssl.SSLException;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.tlauncher.tlauncher.entity.ServerCommandEntity;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.util.TlauncherUtil;
import org.tlauncher.util.U;

/* loaded from: TLauncher-2.876.jar:net/minecraft/launcher/Http.class */
public class Http {
    public static final String JSON_CONTENT_TYPE = "application/json";
    public static final String TEXT_PLAIN = "text/plain";
    public static final String APPLICATION_X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded";

    public static String get(String url, Map<String, Object> query) {
        String line = buildQuery(query);
        if (Strings.isNullOrEmpty(line)) {
            return url;
        }
        return url + "?" + line;
    }

    public static String get(String url, String key, String value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        String line = buildQuery(map);
        if (Strings.isNullOrEmpty(line)) {
            return url;
        }
        return url + "?" + line;
    }

    public static String performPost(URL url, Map<String, Object> query) throws IOException {
        return performPost(url, buildQuery(query), "application/x-www-form-urlencoded");
    }

    public static String performGet(URL url, int connTimeout, int readTimeout) throws IOException {
        String res;
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection(U.getProxy());
            connection.setConnectTimeout(connTimeout);
            connection.setReadTimeout(readTimeout);
            connection.setRequestMethod(HttpGet.METHOD_NAME);
            connection.setInstanceFollowRedirects(true);
            if (connection.getContentType() != null && connection.getContentType().equalsIgnoreCase("application/zip")) {
                res = readZip(connection);
            } else {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
                StringBuilder response = new StringBuilder();
                while (true) {
                    String line = reader.readLine();
                    if (line == null) {
                        break;
                    }
                    response.append(line);
                }
                reader.close();
                res = response.toString();
            }
            if (TLauncher.DEBUG) {
                String shortRes = res.contains("\r") ? res.replaceAll("\r", CoreConstants.EMPTY_STRING) : res;
                if (shortRes.length() > 400) {
                    shortRes = shortRes.substring(0, HttpStatus.SC_BAD_REQUEST);
                }
                U.debug("request: " + url + ", responce: " + shortRes);
            }
            return res;
        } catch (SSLException e) {
            TlauncherUtil.deactivateSSL();
            throw e;
        }
    }

    public static String performGet(String url) throws IOException {
        return performGet(constantURL(url), U.getConnectionTimeout(), U.getReadTimeout());
    }

    public static String performGet(URL url) throws IOException {
        return performGet(url, U.getConnectionTimeout(), U.getReadTimeout());
    }

    public static String performGet(String url, Map<String, Object> map, int connTimeout, int readTimeout) throws IOException {
        StringBuilder stringBuilder = new StringBuilder(url);
        stringBuilder.append("?");
        for (Map.Entry<String, Object> e : map.entrySet()) {
            stringBuilder.append(e.getKey()).append("=").append(encode(e.getValue().toString())).append("&");
        }
        stringBuilder.setLength(stringBuilder.length() - 1);
        return performGet(new URL(stringBuilder.toString()), connTimeout, readTimeout);
    }

    public static String performPost(URL url, byte[] body, String contentType, boolean gzip) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection(U.getProxy());
        connection.setConnectTimeout(U.getConnectionTimeout());
        connection.setReadTimeout(U.getReadTimeout());
        connection.setRequestMethod(HttpPost.METHOD_NAME);
        connection.setRequestProperty("Content-Type", contentType + "; charset=utf-8");
        connection.setRequestProperty(HttpHeaders.CONTENT_LANGUAGE, "en-US");
        connection.setUseCaches(false);
        connection.setDoInput(true);
        connection.setInstanceFollowRedirects(true);
        connection.setDoOutput(true);
        if (gzip) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            GZIPOutputStream g = new GZIPOutputStream(out);
            g.write(body);
            g.close();
            body = out.toByteArray();
        }
        connection.setRequestProperty("Content-Length", CoreConstants.EMPTY_STRING + body.length);
        OutputStream writer = new DataOutputStream(connection.getOutputStream());
        writer.write(body);
        writer.close();
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        while (true) {
            String line = reader.readLine();
            if (line != null) {
                response.append(line);
                response.append('\r');
            } else {
                reader.close();
                return response.toString();
            }
        }
    }

    public static String performPost(URL url, String body, String contentType) throws IOException {
        return performPost(url, body.getBytes(StandardCharsets.UTF_8), contentType, false);
    }

    public static URL constantURL(String input) {
        try {
            return new URL(input);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public static String encode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8").replaceAll("\\+", "%20").replaceAll("%3A", ":").replaceAll("%2F", "/").replaceAll("%21", "!").replaceAll("%27", "'").replaceAll("%28", "(").replaceAll("%29", ")").replaceAll("%7E", "~");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UTF-8 is not supported.", e);
        }
    }

    public static ServerCommandEntity readRequestInfo(Socket socket) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        ServerCommandEntity res = new ServerCommandEntity();
        String line = in.readLine();
        StringBuilder raw = new StringBuilder();
        raw.append(line);
        String[] array = line.split(" ");
        res.setRequestType(array[0]);
        res.setUrn(array[1]);
        res.setQueries(getQueryMap(res.getUrn()));
        boolean isPost = raw.toString().startsWith(HttpPost.METHOD_NAME);
        int contentLength = 0;
        while (true) {
            String line2 = in.readLine();
            if (line2.equals(CoreConstants.EMPTY_STRING)) {
                break;
            }
            raw.append('\n').append(line2);
            if (isPost && line2.startsWith("Content-Length: ")) {
                contentLength = Integer.parseInt(line2.substring("Content-Length: ".length()));
            }
        }
        StringBuilder body = new StringBuilder();
        if (isPost) {
            for (int i = 0; i < contentLength; i++) {
                int c = in.read();
                body.append((char) c);
            }
        }
        raw.append(body.toString());
        out.write("HTTP/1.1 200 OK\r\n");
        out.write("Content-Type: text/html\r\n");
        out.write("Access-Control-Allow-Origin: *\r\n");
        out.write("\r\n");
        out.close();
        in.close();
        socket.close();
        res.setBody(body.toString());
        return res;
    }

    private static Map<String, String> getQueryMap(String query) {
        String[] params = query.split("\\?");
        if (params.length != 2) {
            return new HashMap();
        }
        String[] params2 = params[1].split("&");
        Map<String, String> map = new HashMap<>();
        for (String param : params2) {
            String name = param.split("=")[0];
            String value = param.split("=")[1];
            map.put(name, value);
        }
        return map;
    }

    private static String buildQuery(Map<String, Object> query) {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, Object> entry : query.entrySet()) {
            if (builder.length() > 0) {
                builder.append('&');
            }
            try {
                builder.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                U.log("Unexpected exception building query", e);
            }
            if (entry.getValue() != null) {
                builder.append('=');
                try {
                    builder.append(URLEncoder.encode(entry.getValue().toString(), "UTF-8"));
                } catch (UnsupportedEncodingException e2) {
                    U.log(e2);
                }
            }
        }
        return builder.toString();
    }

    private static String readZip(HttpURLConnection connection) throws IOException {
        ZipInputStream zip = new ZipInputStream(connection.getInputStream(), StandardCharsets.UTF_8);
        zip.getNextEntry();
        String res = IOUtils.toString(zip);
        zip.closeEntry();
        zip.close();
        return res;
    }
}
