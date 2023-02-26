package org.tlauncher.util.gson;

import ch.qos.logback.core.CoreConstants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import net.minecraft.launcher.Http;
import net.minecraft.launcher.versions.json.PatternTypeAdapter;
import org.tlauncher.tlauncher.repository.Repo;
import org.tlauncher.tlauncher.rmo.Bootstrapper;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.util.U;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/util/gson/DownloadUtil.class */
public class DownloadUtil {
    private static final Gson gson;
    private static final int CONNECT_TIMEOUT = 5000;
    private static final int READ_TIMEOUT = 15000;

    static {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Pattern.class, new PatternTypeAdapter());
        builder.setPrettyPrinting();
        gson = builder.create();
    }

    public static <T> T loadObjectByKey(String key, Class<T> cl) throws IOException {
        String result = readTextByKey(key, CoreConstants.EMPTY_STRING, true);
        return (T) gson.fromJson(result, (Class<Object>) cl);
    }

    public static <T> T loadByRepository(Repo repo, Class<T> cl) throws IOException {
        return (T) gson.fromJson(repo.getUrl(), (Class<Object>) cl);
    }

    public static <T> T loadByRepository(Repo repository, Type type) throws IOException {
        return (T) gson.fromJson(repository.getUrl(), type);
    }

    public static <T> T loadObjectByKey(String key, Type type, boolean flag) throws IOException {
        return (T) gson.fromJson(readTextByKey(key, CoreConstants.EMPTY_STRING, flag), type);
    }

    public static String readTextByKey(String key, String postfix, boolean innerConfig) throws IOException {
        List<String> urls;
        if (innerConfig) {
            urls = Arrays.asList(TLauncher.getInnerSettings().get(key).split(","));
        } else {
            urls = Arrays.asList(Bootstrapper.innerConfig.get(key).split(","));
        }
        IOException error = null;
        for (String url : urls) {
            log("request to " + url);
            try {
                return Http.performGet(new URL(url + postfix), 5000, READ_TIMEOUT);
            } catch (IOException ex) {
                error = ex;
            }
        }
        throw new IOException("couldn't download ", error);
    }

    private static void log(String line) {
        U.log("[GsonUtil] ", line);
    }
}
