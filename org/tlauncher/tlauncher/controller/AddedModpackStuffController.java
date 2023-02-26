package org.tlauncher.tlauncher.controller;

import com.google.gson.Gson;
import java.io.IOException;
import java.net.URL;
import javax.inject.Inject;
import javax.inject.Named;
import net.minecraft.launcher.Http;
import org.tlauncher.modpack.domain.client.AddedGameEntityDTO;
import org.tlauncher.tlauncher.managers.ModpackManager;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.ui.alert.Alert;
import org.tlauncher.tlauncher.ui.loc.Localizable;
import org.tlauncher.util.U;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/controller/AddedModpackStuffController.class */
public class AddedModpackStuffController {
    @Inject
    @Named("GsonCompleteVersion")
    private Gson gson;

    public void send(String link) {
        AddedGameEntityDTO en = new AddedGameEntityDTO();
        en.setUrl(link);
        try {
            URL url = new URL(TLauncher.getInnerSettings().get("modpack.operation.url") + ModpackManager.ModpackServerCommand.ADD_NEW_GAME_ENTITY.toString().toLowerCase());
            Http.performPost(url, this.gson.toJson(en), Http.JSON_CONTENT_TYPE);
            Alert.showLocMessage("modpack.send.success");
        } catch (IOException e) {
            Alert.showMonologError(Localizable.get().get("modpack.error.send.unsuccess"), 0);
            U.log(e);
        }
    }
}
