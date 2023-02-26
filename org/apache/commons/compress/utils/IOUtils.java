package org.apache.commons.compress.utils;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/utils/IOUtils.class */
public final class IOUtils {
    private static final int COPY_BUF_SIZE = 8024;
    private static final int SKIP_BUF_SIZE = 4096;
    public static final LinkOption[] EMPTY_LINK_OPTIONS = new LinkOption[0];
    private static final byte[] SKIP_BUF = new byte[4096];

    private IOUtils() {
    }

    public static long copy(InputStream input, OutputStream output) throws IOException {
        return copy(input, output, COPY_BUF_SIZE);
    }

    public static long copy(InputStream input, OutputStream output, int buffersize) throws IOException {
        if (buffersize < 1) {
            throw new IllegalArgumentException("buffersize must be bigger than 0");
        }
        byte[] buffer = new byte[buffersize];
        long j = 0;
        while (true) {
            long count = j;
            int n = input.read(buffer);
            if (-1 != n) {
                if (output != null) {
                    output.write(buffer, 0, n);
                }
                j = count + n;
            } else {
                return count;
            }
        }
    }

    public static long skip(InputStream input, long numToSkip) throws IOException {
        int read;
        while (numToSkip > 0) {
            long skipped = input.skip(numToSkip);
            if (skipped == 0) {
                break;
            }
            numToSkip -= skipped;
        }
        while (numToSkip > 0 && (read = readFully(input, SKIP_BUF, 0, (int) Math.min(numToSkip, 4096L))) >= 1) {
            numToSkip -= read;
        }
        return numToSkip - numToSkip;
    }

    public static int read(File file, byte[] array) throws IOException {
        InputStream inputStream = Files.newInputStream(file.toPath(), new OpenOption[0]);
        Throwable th = null;
        try {
            int readFully = readFully(inputStream, array, 0, array.length);
            if (inputStream != null) {
                if (0 != 0) {
                    try {
                        inputStream.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                } else {
                    inputStream.close();
                }
            }
            return readFully;
        } finally {
        }
    }

    public static int readFully(InputStream input, byte[] array) throws IOException {
        return readFully(input, array, 0, array.length);
    }

    public static int readFully(InputStream input, byte[] array, int offset, int len) throws IOException {
        int x;
        if (len < 0 || offset < 0 || len + offset > array.length || len + offset < 0) {
            throw new IndexOutOfBoundsException();
        }
        int count = 0;
        while (count != len && (x = input.read(array, offset + count, len - count)) != -1) {
            count += x;
        }
        return count;
    }

    public static void readFully(ReadableByteChannel channel, ByteBuffer byteBuffer) throws IOException {
        int read;
        int readNow;
        int expectedLength = byteBuffer.remaining();
        int i = 0;
        while (true) {
            read = i;
            if (read >= expectedLength || (readNow = channel.read(byteBuffer)) <= 0) {
                break;
            }
            i = read + readNow;
        }
        if (read < expectedLength) {
            throw new EOFException();
        }
    }

    public static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        copy(input, output);
        return output.toByteArray();
    }

    public static void closeQuietly(Closeable c) {
        if (c != null) {
            try {
                c.close();
            } catch (IOException e) {
            }
        }
    }

    public static void copy(File sourceFile, OutputStream outputStream) throws IOException {
        Files.copy(sourceFile.toPath(), outputStream);
    }

    public static long copyRange(InputStream input, long len, OutputStream output) throws IOException {
        return copyRange(input, len, output, COPY_BUF_SIZE);
    }

    public static long copyRange(InputStream input, long len, OutputStream output, int buffersize) throws IOException {
        long count;
        int n;
        if (buffersize < 1) {
            throw new IllegalArgumentException("buffersize must be bigger than 0");
        }
        byte[] buffer = new byte[(int) Math.min(buffersize, len)];
        long j = 0;
        while (true) {
            count = j;
            if (count >= len || -1 == (n = input.read(buffer, 0, (int) Math.min(len - count, buffer.length)))) {
                break;
            }
            if (output != null) {
                output.write(buffer, 0, n);
            }
            j = count + n;
        }
        return count;
    }

    public static byte[] readRange(InputStream input, int len) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        copyRange(input, len, output);
        return output.toByteArray();
    }

    public static byte[] readRange(ReadableByteChannel input, int len) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ByteBuffer b = ByteBuffer.allocate(Math.min(len, (int) COPY_BUF_SIZE));
        int i = 0;
        while (true) {
            int read = i;
            if (read >= len) {
                break;
            }
            b.limit(Math.min(len - read, b.capacity()));
            int readNow = input.read(b);
            if (readNow <= 0) {
                break;
            }
            output.write(b.array(), 0, readNow);
            b.rewind();
            i = read + readNow;
        }
        return output.toByteArray();
    }
}
