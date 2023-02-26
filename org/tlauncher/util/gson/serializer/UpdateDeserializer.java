package org.tlauncher.util.gson.serializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.apache.http.cookie.ClientCookie;
import org.tlauncher.tlauncher.updater.client.Banner;
import org.tlauncher.tlauncher.updater.client.Offer;
import org.tlauncher.tlauncher.updater.client.Update;
import org.tlauncher.util.U;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/util/gson/serializer/UpdateDeserializer.class */
public class UpdateDeserializer implements JsonDeserializer<Update> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.google.gson.JsonDeserializer
    public Update deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        try {
            return deserialize0(json, context);
        } catch (Exception e) {
            U.log("Cannot parse update:", e);
            return new Update();
        }
    }

    private Update deserialize0(JsonElement json, JsonDeserializationContext context) {
        JsonObject object = json.getAsJsonObject();
        Update update = new Update();
        update.setVersion(object.get(ClientCookie.VERSION_ATTR).getAsDouble());
        update.setMandatory(object.get("mandatory").getAsBoolean());
        update.setRequiredAtLeastFor(object.has("requiredAtLeastFor") ? object.get("requiredAtLeastFor").getAsDouble() : 0.0d);
        Map<String, String> description = (Map) context.deserialize(object.get("description"), new TypeToken<Map<String, String>>() { // from class: org.tlauncher.util.gson.serializer.UpdateDeserializer.1
        }.getType());
        if (description != null) {
            update.setDescription(description);
        }
        List<String> jarLinks = (List) context.deserialize(object.get("jarLinks"), new TypeToken<List<String>>() { // from class: org.tlauncher.util.gson.serializer.UpdateDeserializer.2
        }.getType());
        if (jarLinks != null) {
            update.setJarLinks(jarLinks);
        }
        List<String> exeLinks = (List) context.deserialize(object.get("exeLinks"), new TypeToken<List<String>>() { // from class: org.tlauncher.util.gson.serializer.UpdateDeserializer.3
        }.getType());
        if (exeLinks != null) {
            update.setExeLinks(exeLinks);
        }
        update.setUpdaterView(object.get("updaterView").getAsInt());
        update.setOfferDelay(object.get("offerDelay").getAsInt());
        update.setOfferEmptyCheckboxDelay(object.get("offerEmptyCheckboxDelay").getAsInt());
        update.setUpdaterLaterInstall(object.get("updaterLaterInstall").getAsBoolean());
        Map<String, List<Banner>> banners = (Map) context.deserialize(object.get("banners"), new TypeToken<Map<String, List<Banner>>>() { // from class: org.tlauncher.util.gson.serializer.UpdateDeserializer.4
        }.getType());
        banners.values().forEach(Collections::shuffle);
        update.setBanners(banners);
        List<Offer> offers = (List) context.deserialize(object.get("offers"), new TypeToken<List<Offer>>() { // from class: org.tlauncher.util.gson.serializer.UpdateDeserializer.5
        }.getType());
        Collections.shuffle(offers);
        update.setOffers(offers);
        update.setRootAccessExe((List) context.deserialize(object.get("rootAccessExe"), new TypeToken<List<String>>() { // from class: org.tlauncher.util.gson.serializer.UpdateDeserializer.6
        }.getType()));
        if (Objects.nonNull(object.get("aboveMandatoryVersion"))) {
            update.setAboveMandatoryVersion(Double.valueOf(object.get("aboveMandatoryVersion").getAsDouble()));
        }
        if (Objects.nonNull(object.get("mandatoryUpdatedVersions"))) {
            update.setMandatoryUpdatedVersions((Set) context.deserialize(object.get("mandatoryUpdatedVersions"), new TypeToken<Set<Double>>() { // from class: org.tlauncher.util.gson.serializer.UpdateDeserializer.7
            }.getType()));
        }
        return update;
    }
}
