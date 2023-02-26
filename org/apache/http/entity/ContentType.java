package org.apache.http.entity;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import net.minecraft.launcher.Http;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.annotation.Immutable;
import org.apache.http.message.BasicHeaderValueFormatter;
import org.apache.http.message.BasicHeaderValueParser;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.message.ParserCursor;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.util.TextUtils;

@Immutable
/* loaded from: TLauncher-2.876.jar:org/apache/http/entity/ContentType.class */
public final class ContentType implements Serializable {
    private static final long serialVersionUID = -7768694718232371896L;
    public static final ContentType APPLICATION_ATOM_XML = create("application/atom+xml", Consts.ISO_8859_1);
    public static final ContentType APPLICATION_FORM_URLENCODED = create("application/x-www-form-urlencoded", Consts.ISO_8859_1);
    public static final ContentType APPLICATION_JSON = create(Http.JSON_CONTENT_TYPE, Consts.UTF_8);
    public static final ContentType APPLICATION_OCTET_STREAM = create("application/octet-stream", (Charset) null);
    public static final ContentType APPLICATION_SVG_XML = create("application/svg+xml", Consts.ISO_8859_1);
    public static final ContentType APPLICATION_XHTML_XML = create("application/xhtml+xml", Consts.ISO_8859_1);
    public static final ContentType APPLICATION_XML = create("application/xml", Consts.ISO_8859_1);
    public static final ContentType MULTIPART_FORM_DATA = create("multipart/form-data", Consts.ISO_8859_1);
    public static final ContentType TEXT_HTML = create("text/html", Consts.ISO_8859_1);
    public static final ContentType TEXT_PLAIN = create("text/plain", Consts.ISO_8859_1);
    public static final ContentType TEXT_XML = create("text/xml", Consts.ISO_8859_1);
    public static final ContentType WILDCARD = create("*/*", (Charset) null);
    public static final ContentType DEFAULT_TEXT = TEXT_PLAIN;
    public static final ContentType DEFAULT_BINARY = APPLICATION_OCTET_STREAM;
    private final String mimeType;
    private final Charset charset;
    private final NameValuePair[] params;

    ContentType(String mimeType, Charset charset) {
        this.mimeType = mimeType;
        this.charset = charset;
        this.params = null;
    }

    ContentType(String mimeType, Charset charset, NameValuePair[] params) {
        this.mimeType = mimeType;
        this.charset = charset;
        this.params = params;
    }

    public String getMimeType() {
        return this.mimeType;
    }

    public Charset getCharset() {
        return this.charset;
    }

    public String getParameter(String name) {
        Args.notEmpty(name, "Parameter name");
        if (this.params == null) {
            return null;
        }
        NameValuePair[] arr$ = this.params;
        for (NameValuePair param : arr$) {
            if (param.getName().equalsIgnoreCase(name)) {
                return param.getValue();
            }
        }
        return null;
    }

    public String toString() {
        CharArrayBuffer buf = new CharArrayBuffer(64);
        buf.append(this.mimeType);
        if (this.params != null) {
            buf.append("; ");
            BasicHeaderValueFormatter.INSTANCE.formatParameters(buf, this.params, false);
        } else if (this.charset != null) {
            buf.append(HTTP.CHARSET_PARAM);
            buf.append(this.charset.name());
        }
        return buf.toString();
    }

    private static boolean valid(String s) {
        for (int i = 0; i < s.length(); i++) {
            char ch2 = s.charAt(i);
            if (ch2 == '\"' || ch2 == ',' || ch2 == ';') {
                return false;
            }
        }
        return true;
    }

    public static ContentType create(String mimeType, Charset charset) {
        String type = ((String) Args.notBlank(mimeType, "MIME type")).toLowerCase(Locale.ROOT);
        Args.check(valid(type), "MIME type may not contain reserved characters");
        return new ContentType(type, charset);
    }

    public static ContentType create(String mimeType) {
        return new ContentType(mimeType, null);
    }

    public static ContentType create(String mimeType, String charset) throws UnsupportedCharsetException {
        return create(mimeType, !TextUtils.isBlank(charset) ? Charset.forName(charset) : null);
    }

    private static ContentType create(HeaderElement helem, boolean strict) {
        return create(helem.getName(), helem.getParameters(), strict);
    }

    /* JADX WARN: Code restructure failed: missing block: B:17:0x0056, code lost:
        r3 = r9;
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x005d, code lost:
        if (r7 == null) goto L18;
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x0062, code lost:
        if (r7.length <= 0) goto L18;
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x0065, code lost:
        r4 = r7;
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x0069, code lost:
        r4 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x006d, code lost:
        return new org.apache.http.entity.ContentType(r6, r3, r4);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static org.apache.http.entity.ContentType create(java.lang.String r6, org.apache.http.NameValuePair[] r7, boolean r8) {
        /*
            r0 = 0
            r9 = r0
            r0 = r7
            r10 = r0
            r0 = r10
            int r0 = r0.length
            r11 = r0
            r0 = 0
            r12 = r0
        Ld:
            r0 = r12
            r1 = r11
            if (r0 >= r1) goto L56
            r0 = r10
            r1 = r12
            r0 = r0[r1]
            r13 = r0
            r0 = r13
            java.lang.String r0 = r0.getName()
            java.lang.String r1 = "charset"
            boolean r0 = r0.equalsIgnoreCase(r1)
            if (r0 == 0) goto L50
            r0 = r13
            java.lang.String r0 = r0.getValue()
            r14 = r0
            r0 = r14
            boolean r0 = org.apache.http.util.TextUtils.isBlank(r0)
            if (r0 != 0) goto L56
            r0 = r14
            java.nio.charset.Charset r0 = java.nio.charset.Charset.forName(r0)     // Catch: java.nio.charset.UnsupportedCharsetException -> L44
            r9 = r0
            goto L56
        L44:
            r15 = move-exception
            r0 = r8
            if (r0 == 0) goto L4d
            r0 = r15
            throw r0
        L4d:
            goto L56
        L50:
            int r12 = r12 + 1
            goto Ld
        L56:
            org.apache.http.entity.ContentType r0 = new org.apache.http.entity.ContentType
            r1 = r0
            r2 = r6
            r3 = r9
            r4 = r7
            if (r4 == 0) goto L69
            r4 = r7
            int r4 = r4.length
            if (r4 <= 0) goto L69
            r4 = r7
            goto L6a
        L69:
            r4 = 0
        L6a:
            r1.<init>(r2, r3, r4)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.http.entity.ContentType.create(java.lang.String, org.apache.http.NameValuePair[], boolean):org.apache.http.entity.ContentType");
    }

    public static ContentType create(String mimeType, NameValuePair... params) throws UnsupportedCharsetException {
        String type = ((String) Args.notBlank(mimeType, "MIME type")).toLowerCase(Locale.ROOT);
        Args.check(valid(type), "MIME type may not contain reserved characters");
        return create(mimeType, params, true);
    }

    public static ContentType parse(String s) throws ParseException, UnsupportedCharsetException {
        Args.notNull(s, "Content type");
        CharArrayBuffer buf = new CharArrayBuffer(s.length());
        buf.append(s);
        ParserCursor cursor = new ParserCursor(0, s.length());
        HeaderElement[] elements = BasicHeaderValueParser.INSTANCE.parseElements(buf, cursor);
        if (elements.length > 0) {
            return create(elements[0], true);
        }
        throw new ParseException("Invalid content type: " + s);
    }

    public static ContentType get(HttpEntity entity) throws ParseException, UnsupportedCharsetException {
        Header header;
        if (entity != null && (header = entity.getContentType()) != null) {
            HeaderElement[] elements = header.getElements();
            if (elements.length > 0) {
                return create(elements[0], true);
            }
            return null;
        }
        return null;
    }

    public static ContentType getLenient(HttpEntity entity) {
        Header header;
        if (entity != null && (header = entity.getContentType()) != null) {
            try {
                HeaderElement[] elements = header.getElements();
                if (elements.length > 0) {
                    return create(elements[0], false);
                }
                return null;
            } catch (ParseException e) {
                return null;
            }
        }
        return null;
    }

    public static ContentType getOrDefault(HttpEntity entity) throws ParseException, UnsupportedCharsetException {
        ContentType contentType = get(entity);
        return contentType != null ? contentType : DEFAULT_TEXT;
    }

    public static ContentType getLenientOrDefault(HttpEntity entity) throws ParseException, UnsupportedCharsetException {
        ContentType contentType = get(entity);
        return contentType != null ? contentType : DEFAULT_TEXT;
    }

    public ContentType withCharset(Charset charset) {
        return create(getMimeType(), charset);
    }

    public ContentType withCharset(String charset) {
        return create(getMimeType(), charset);
    }

    public ContentType withParameters(NameValuePair... params) throws UnsupportedCharsetException {
        if (params.length == 0) {
            return this;
        }
        Map<String, String> paramMap = new LinkedHashMap<>();
        if (this.params != null) {
            NameValuePair[] arr$ = this.params;
            for (NameValuePair param : arr$) {
                paramMap.put(param.getName(), param.getValue());
            }
        }
        for (NameValuePair param2 : params) {
            paramMap.put(param2.getName(), param2.getValue());
        }
        List<NameValuePair> newParams = new ArrayList<>(paramMap.size() + 1);
        if (this.charset != null && !paramMap.containsKey("charset")) {
            newParams.add(new BasicNameValuePair("charset", this.charset.name()));
        }
        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
            newParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        return create(getMimeType(), (NameValuePair[]) newParams.toArray(new NameValuePair[newParams.size()]), true);
    }
}
