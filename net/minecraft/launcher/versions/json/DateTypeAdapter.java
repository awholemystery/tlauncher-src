package net.minecraft.launcher.versions.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.tlauncher.exceptions.ParseException;

/* loaded from: TLauncher-2.876.jar:net/minecraft/launcher/versions/json/DateTypeAdapter.class */
public class DateTypeAdapter implements JsonDeserializer<Date>, JsonSerializer<Date> {
    private final DateFormat enUsFormat = DateFormat.getDateTimeInstance(2, 2, Locale.US);
    private final DateFormat iso8601Format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.google.gson.JsonDeserializer
    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (!(json instanceof JsonPrimitive)) {
            throw new JsonParseException("The date should be a string value");
        }
        Date date = toDate(json.getAsString());
        if (typeOfT == Date.class) {
            return date;
        }
        throw new IllegalArgumentException(getClass() + " cannot deserialize to " + typeOfT);
    }

    @Override // com.google.gson.JsonSerializer
    public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
        JsonPrimitive jsonPrimitive;
        synchronized (this.enUsFormat) {
            jsonPrimitive = new JsonPrimitive(toString(src));
        }
        return jsonPrimitive;
    }

    public String toString(Date date) {
        String str;
        synchronized (this.enUsFormat) {
            String result = this.iso8601Format.format(date);
            str = result.substring(0, 22) + ":" + result.substring(22);
        }
        return str;
    }

    public Date toDate(String string) {
        Date parse;
        synchronized (this.enUsFormat) {
            try {
                parse = this.enUsFormat.parse(string);
            } catch (Exception e) {
                try {
                    return this.iso8601Format.parse(string);
                } catch (Exception e2) {
                    try {
                        String cleaned = string.replace("Z", "+00:00");
                        return this.iso8601Format.parse(cleaned.substring(0, 22) + cleaned.substring(23));
                    } catch (Exception e3) {
                        throw new ParseException("Invalid date: " + string, e3);
                    }
                }
            }
        }
        return parse;
    }
}
