package net.minecraft.launcher.versions.json;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.File;
import java.io.IOException;

/* loaded from: TLauncher-2.876.jar:net/minecraft/launcher/versions/json/FileTypeAdapter.class */
public class FileTypeAdapter extends TypeAdapter<File> {
    @Override // com.google.gson.TypeAdapter
    public void write(JsonWriter out, File value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            out.value(value.getAbsolutePath());
        }
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.google.gson.TypeAdapter
    public File read(JsonReader in) throws IOException {
        String name;
        if (!in.hasNext() || (name = in.nextString()) == null) {
            return null;
        }
        return new File(name);
    }
}
