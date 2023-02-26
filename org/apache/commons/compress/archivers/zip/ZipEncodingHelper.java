package org.apache.commons.compress.archivers.zip;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/archivers/zip/ZipEncodingHelper.class */
public abstract class ZipEncodingHelper {
    static final String UTF8 = "UTF8";
    static final ZipEncoding UTF8_ZIP_ENCODING = getZipEncoding(UTF8);

    public static ZipEncoding getZipEncoding(String name) {
        Charset cs = Charset.defaultCharset();
        if (name != null) {
            try {
                cs = Charset.forName(name);
            } catch (UnsupportedCharsetException e) {
            }
        }
        boolean useReplacement = isUTF8(cs.name());
        return new NioZipEncoding(cs, useReplacement);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean isUTF8(String charsetName) {
        String actual = charsetName != null ? charsetName : Charset.defaultCharset().name();
        if (StandardCharsets.UTF_8.name().equalsIgnoreCase(actual)) {
            return true;
        }
        return StandardCharsets.UTF_8.aliases().stream().anyMatch(alias -> {
            return alias.equalsIgnoreCase(actual);
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ByteBuffer growBufferBy(ByteBuffer buffer, int increment) {
        buffer.limit(buffer.position());
        buffer.rewind();
        ByteBuffer on = ByteBuffer.allocate(buffer.capacity() + increment);
        on.put(buffer);
        return on;
    }
}
