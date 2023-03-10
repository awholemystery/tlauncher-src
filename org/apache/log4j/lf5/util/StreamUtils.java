package org.apache.log4j.lf5.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/* loaded from: TLauncher-2.876.jar:org/apache/log4j/lf5/util/StreamUtils.class */
public abstract class StreamUtils {
    public static final int DEFAULT_BUFFER_SIZE = 2048;

    public static void copy(InputStream input, OutputStream output) throws IOException {
        copy(input, output, 2048);
    }

    public static void copy(InputStream input, OutputStream output, int bufferSize) throws IOException {
        byte[] buf = new byte[bufferSize];
        int read = input.read(buf);
        while (true) {
            int bytesRead = read;
            if (bytesRead != -1) {
                output.write(buf, 0, bytesRead);
                read = input.read(buf);
            } else {
                output.flush();
                return;
            }
        }
    }

    public static void copyThenClose(InputStream input, OutputStream output) throws IOException {
        copy(input, output);
        input.close();
        output.close();
    }

    public static byte[] getBytes(InputStream input) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        copy(input, result);
        result.close();
        return result.toByteArray();
    }
}
