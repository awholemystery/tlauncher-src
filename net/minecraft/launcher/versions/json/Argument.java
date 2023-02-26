package net.minecraft.launcher.versions.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import net.minecraft.launcher.versions.Rule;

/* loaded from: TLauncher-2.876.jar:net/minecraft/launcher/versions/json/Argument.class */
public class Argument {
    private final String[] values;
    private final List<Rule> rules;

    public Argument(String[] values, List<Rule> rules) {
        this.values = values;
        this.rules = rules;
    }

    public boolean appliesToCurrentEnvironment() {
        if (this.rules == null || this.rules.isEmpty()) {
            return true;
        }
        Rule.Action lastAction = Rule.Action.DISALLOW;
        for (Rule compatibilityRule : this.rules) {
            Rule.Action action = compatibilityRule.getAppliedAction();
            if (action != null) {
                lastAction = action;
            }
        }
        return lastAction == Rule.Action.ALLOW;
    }

    public String[] getValues() {
        return this.values;
    }

    public String toString() {
        return "Argument{values=" + Arrays.toString(this.values) + '}';
    }

    /* loaded from: TLauncher-2.876.jar:net/minecraft/launcher/versions/json/Argument$Serializer.class */
    public static class Serializer implements JsonDeserializer<Argument> {
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.google.gson.JsonDeserializer
        public Argument deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            String[] values;
            if (json.isJsonPrimitive()) {
                return new Argument(new String[]{json.getAsString()}, null);
            }
            if (json.isJsonObject()) {
                JsonObject obj = json.getAsJsonObject();
                JsonElement value = obj.get("value");
                if (value == null) {
                    value = obj.get("values");
                }
                if (value.isJsonPrimitive()) {
                    values = new String[]{value.getAsString()};
                } else {
                    JsonArray array = value.getAsJsonArray();
                    values = new String[array.size()];
                    for (int i = 0; i < array.size(); i++) {
                        values[i] = array.get(i).getAsString();
                    }
                }
                ArrayList arrayList = new ArrayList();
                if (obj.has("rules")) {
                    Iterator<JsonElement> it = obj.getAsJsonArray("rules").iterator();
                    while (it.hasNext()) {
                        JsonElement element = it.next();
                        arrayList.add(context.deserialize(element, Rule.class));
                    }
                }
                return new Argument(values, arrayList);
            }
            throw new JsonParseException("Invalid argument, must be object or string");
        }
    }
}
