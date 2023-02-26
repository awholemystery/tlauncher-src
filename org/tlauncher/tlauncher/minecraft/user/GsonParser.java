package org.tlauncher.tlauncher.minecraft.user;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.lang.reflect.Type;
import java.util.UUID;
import org.apache.log4j.Logger;
import org.tlauncher.tlauncher.minecraft.auth.UUIDTypeAdapter;
import org.tlauncher.tlauncher.minecraft.exceptions.ParseException;
import org.tlauncher.tlauncher.minecraft.user.preq.Validatable;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/minecraft/user/GsonParser.class */
public class GsonParser<V extends Validatable> implements Parser<V> {
    private final Gson gson;
    private final Type type;

    public GsonParser(Gson gson, Type type) {
        this.gson = gson;
        this.type = type;
    }

    public static <V extends Validatable> GsonParser<V> defaultParser(Type type) {
        return new GsonParser<>(new GsonBuilder().create(), type);
    }

    public static <V extends Validatable> GsonParser<V> withDashlessUUIDAdapter(Type type) {
        return new GsonParser<>(new GsonBuilder().registerTypeAdapter(UUID.class, new UUIDTypeAdapter()).create(), type);
    }

    public static <V extends Validatable> GsonParser<V> lowerCaseWithUnderscores(Type type) {
        return new GsonParser<>(new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create(), type);
    }

    public static <V extends Validatable> GsonParser<V> upperCamelCase(Type type) {
        return new GsonParser<>(new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create(), type);
    }

    public static <V extends Validatable> GsonParser<V> withDeserializer(Type type, Object typeAdapter) {
        return new GsonParser<>(new GsonBuilder().registerTypeAdapter(type, typeAdapter).create(), type);
    }

    @Override // org.tlauncher.tlauncher.minecraft.user.Parser
    public V parseResponse(Logger logger, String response) throws ParseException {
        logger.trace("Parsing response");
        try {
            V result = (V) this.gson.fromJson(response, this.type);
            logger.trace("Validating response");
            try {
                result.validate();
                return result;
            } catch (ParseException e) {
                throw e;
            } catch (RuntimeException e2) {
                throw new ParseException(e2);
            }
        } catch (RuntimeException e3) {
            throw new ParseException(e3);
        }
    }
}
