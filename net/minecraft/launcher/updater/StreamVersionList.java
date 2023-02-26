package net.minecraft.launcher.updater;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.commons.io.Charsets;

/* loaded from: TLauncher-2.876.jar:net/minecraft/launcher/updater/StreamVersionList.class */
public abstract class StreamVersionList extends VersionList {
    protected abstract InputStream getInputStream(String str) throws IOException;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // net.minecraft.launcher.updater.VersionList
    public String getUrl(String uri) throws IOException {
        InputStream inputStream = getInputStream(uri);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charsets.UTF_8));
        StringBuilder result = new StringBuilder();
        while (true) {
            String line = reader.readLine();
            if (line != null) {
                if (result.length() > 0) {
                    result.append('\n');
                }
                result.append(line);
            } else {
                reader.close();
                return result.toString();
            }
        }
    }
}
