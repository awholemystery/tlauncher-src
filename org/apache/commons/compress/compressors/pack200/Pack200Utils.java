package org.apache.commons.compress.compressors.pack200;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import org.apache.commons.compress.java.util.jar.Pack200;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/compressors/pack200/Pack200Utils.class */
public class Pack200Utils {
    private Pack200Utils() {
    }

    public static void normalize(File jar) throws IOException {
        normalize(jar, jar, null);
    }

    public static void normalize(File jar, Map<String, String> props) throws IOException {
        normalize(jar, jar, props);
    }

    public static void normalize(File from, File to) throws IOException {
        normalize(from, to, null);
    }

    public static void normalize(File from, File to, Map<String, String> props) throws IOException {
        if (props == null) {
            props = new HashMap();
        }
        props.put(Pack200.Packer.SEGMENT_LIMIT, "-1");
        File tempFile = File.createTempFile("commons-compress", "pack200normalize");
        try {
            OutputStream fos = Files.newOutputStream(tempFile.toPath(), new OpenOption[0]);
            JarFile jarFile = new JarFile(from);
            Throwable th = null;
            try {
                Pack200.Packer packer = Pack200.newPacker();
                packer.properties().putAll(props);
                packer.pack(jarFile, fos);
                if (jarFile != null) {
                    if (0 != 0) {
                        try {
                            jarFile.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    } else {
                        jarFile.close();
                    }
                }
                if (fos != null) {
                    if (0 != 0) {
                        fos.close();
                    } else {
                        fos.close();
                    }
                }
                Pack200.Unpacker unpacker = Pack200.newUnpacker();
                JarOutputStream jos = new JarOutputStream(Files.newOutputStream(to.toPath(), new OpenOption[0]));
                unpacker.unpack(tempFile, jos);
                if (jos != null) {
                    if (0 != 0) {
                        jos.close();
                    } else {
                        jos.close();
                    }
                }
            } catch (Throwable th3) {
                try {
                    throw th3;
                } catch (Throwable th4) {
                    if (jarFile != null) {
                        if (th3 != null) {
                            try {
                                jarFile.close();
                            } catch (Throwable th5) {
                                th3.addSuppressed(th5);
                            }
                        } else {
                            jarFile.close();
                        }
                    }
                    throw th4;
                }
            }
        } finally {
            if (!tempFile.delete()) {
                tempFile.deleteOnExit();
            }
        }
    }
}
