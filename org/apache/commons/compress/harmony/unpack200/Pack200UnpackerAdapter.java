package org.apache.commons.compress.harmony.unpack200;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.util.jar.JarOutputStream;
import org.apache.commons.compress.harmony.pack200.Pack200Adapter;
import org.apache.commons.compress.harmony.pack200.Pack200Exception;
import org.apache.commons.compress.java.util.jar.Pack200;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/unpack200/Pack200UnpackerAdapter.class */
public class Pack200UnpackerAdapter extends Pack200Adapter implements Pack200.Unpacker {
    @Override // org.apache.commons.compress.java.util.jar.Pack200.Unpacker
    public void unpack(InputStream in, JarOutputStream out) throws IOException {
        if (in == null || out == null) {
            throw new IllegalArgumentException("Must specify both input and output streams");
        }
        completed(0.0d);
        try {
            new Archive(in, out).unpack();
            completed(1.0d);
        } catch (Pack200Exception e) {
            throw new IOException("Failed to unpack Jar:" + e);
        }
    }

    @Override // org.apache.commons.compress.java.util.jar.Pack200.Unpacker
    public void unpack(File file, JarOutputStream out) throws IOException {
        if (file == null || out == null) {
            throw new IllegalArgumentException("Must specify both input and output streams");
        }
        int size = (int) file.length();
        int bufferSize = (size <= 0 || size >= 8192) ? 8192 : size;
        InputStream in = new BufferedInputStream(Files.newInputStream(file.toPath(), new OpenOption[0]), bufferSize);
        Throwable th = null;
        try {
            unpack(in, out);
            if (in != null) {
                if (0 != 0) {
                    try {
                        in.close();
                        return;
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                        return;
                    }
                }
                in.close();
            }
        } catch (Throwable th3) {
            try {
                throw th3;
            } catch (Throwable th4) {
                if (in != null) {
                    if (th3 != null) {
                        try {
                            in.close();
                        } catch (Throwable th5) {
                            th3.addSuppressed(th5);
                        }
                    } else {
                        in.close();
                    }
                }
                throw th4;
            }
        }
    }
}
