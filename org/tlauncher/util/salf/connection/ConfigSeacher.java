package org.tlauncher.util.salf.connection;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.commons.io.IOUtils;
import org.tlauncher.util.FileUtil;
import org.tlauncher.util.MinecraftUtil;
import org.tlauncher.util.U;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/util/salf/connection/ConfigSeacher.class */
public class ConfigSeacher {
    private String[] urlServer;
    private String nameFile;

    public ConfigSeacher(String[] urlServer, String nameFile) {
        this.urlServer = urlServer;
        this.nameFile = nameFile;
    }

    private ServerEntity readConfigFromFile() throws IOException {
        Path p = Paths.get(MinecraftUtil.getWorkingDirectory().getCanonicalPath(), this.nameFile);
        if (!Files.isReadable(p)) {
            log("file doesn't readable or exist is " + p.toString());
            return null;
        }
        String dataJson = FileUtil.readFile(new File(MinecraftUtil.getWorkingDirectory(), this.nameFile));
        return (ServerEntity) new Gson().fromJson(dataJson, (Class<Object>) ServerEntity.class);
    }

    public ServerEntity saveConfigFromServer() throws IOException {
        String[] strArr;
        IOException io = null;
        for (String s : this.urlServer) {
            try {
                String serverText = IOUtils.toString(new URL(s), "utf-8");
                IOUtils.write(serverText, (OutputStream) new FileOutputStream(new File(MinecraftUtil.getWorkingDirectory().getCanonicalPath(), this.nameFile)), "utf-8");
                return (ServerEntity) new Gson().fromJson(serverText, (Class<Object>) ServerEntity.class);
            } catch (IOException e) {
                io = e;
            }
        }
        throw io;
    }

    private ServerEntity readOldServer() throws IOException {
        ServerEntity server = null;
        try {
            server = readConfigFromFile();
        } catch (JsonSyntaxException ex) {
            log(ex.getMessage());
        }
        if (server == null) {
            server = saveConfigFromServer();
        }
        if (server == null) {
            throw new NullPointerException("didn't receive data from filenameFile=" + this.nameFile + " urlServer=" + this.urlServer);
        }
        return server;
    }

    public ServerEntity readServer() throws IOException {
        return readOldServer();
    }

    private void log(String line) {
        U.log("[ConfigSeacher] ", line);
    }
}
