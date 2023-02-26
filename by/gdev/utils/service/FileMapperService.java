package by.gdev.utils.service;

import by.gdev.util.excepiton.NotAllowWriteFileOperation;
import com.google.gson.Gson;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* loaded from: TLauncher-2.876.jar:by/gdev/utils/service/FileMapperService.class */
public class FileMapperService {
    private static final Logger log = LoggerFactory.getLogger(FileMapperService.class);
    private Gson gson;
    private Charset charset;
    private String workingDirectory;

    public FileMapperService(Gson gson, Charset charset, String workingDirectory) {
        this.gson = gson;
        this.charset = charset;
        this.workingDirectory = workingDirectory;
    }

    public void write(Object create, String config) throws IOException, UnsupportedOperationException {
        Path path = Paths.get(this.workingDirectory, config);
        if (Files.notExists(path.getParent(), new LinkOption[0])) {
            Files.createDirectories(path.getParent(), new FileAttribute[0]);
        }
        if (Files.exists(path, new LinkOption[0]) && !path.toFile().canWrite()) {
            throw new NotAllowWriteFileOperation(path.toString());
        }
        OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(path.toFile()), this.charset);
        Throwable th = null;
        try {
            this.gson.toJson(create, out);
            if (out != null) {
                if (0 != 0) {
                    try {
                        out.close();
                        return;
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                        return;
                    }
                }
                out.close();
            }
        } catch (Throwable th3) {
            try {
                throw th3;
            } catch (Throwable th4) {
                if (out != null) {
                    if (th3 != null) {
                        try {
                            out.close();
                        } catch (Throwable th5) {
                            th3.addSuppressed(th5);
                        }
                    } else {
                        out.close();
                    }
                }
                throw th4;
            }
        }
    }

    public <T> T read(String file, Class<T> cl) {
        try {
            InputStreamReader read = new InputStreamReader(new FileInputStream(Paths.get(this.workingDirectory, file).toFile()), this.charset);
            Throwable th = null;
            try {
                T t = (T) this.gson.fromJson((Reader) read, (Class<Object>) cl);
                if (read != null) {
                    if (0 != 0) {
                        try {
                            read.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    } else {
                        read.close();
                    }
                }
                return t;
            } catch (Throwable th3) {
                try {
                    throw th3;
                } catch (Throwable th4) {
                    if (read != null) {
                        if (th3 != null) {
                            try {
                                read.close();
                            } catch (Throwable th5) {
                                th3.addSuppressed(th5);
                            }
                        } else {
                            read.close();
                        }
                    }
                    throw th4;
                }
            }
        } catch (FileNotFoundException e) {
            log.info("file not exist " + file);
            return null;
        } catch (Throwable t2) {
            log.warn("error read json " + file, t2);
            return null;
        }
    }
}
