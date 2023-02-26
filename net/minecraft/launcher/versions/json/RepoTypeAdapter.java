package net.minecraft.launcher.versions.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import org.tlauncher.tlauncher.repository.ClientInstanceRepo;
import org.tlauncher.tlauncher.repository.Repo;

/* loaded from: TLauncher-2.876.jar:net/minecraft/launcher/versions/json/RepoTypeAdapter.class */
public class RepoTypeAdapter implements JsonDeserializer<Repo>, JsonSerializer<Repo> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.google.gson.JsonDeserializer
    public Repo deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return ClientInstanceRepo.find(jsonElement.getAsString());
    }

    @Override // com.google.gson.JsonSerializer
    public JsonElement serialize(Repo repo, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(repo.getName().toLowerCase());
    }
}
