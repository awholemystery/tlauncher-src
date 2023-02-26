package org.tlauncher.tlauncher.managers;

import ch.qos.logback.core.CoreConstants;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Key;
import com.google.inject.name.Names;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.tlauncher.tlauncher.component.InterruptibleComponent;
import org.tlauncher.tlauncher.entity.AdditionalAsset;
import org.tlauncher.tlauncher.repository.ClientInstanceRepo;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.util.U;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/managers/AdditionalAssetsComponent.class */
public class AdditionalAssetsComponent extends InterruptibleComponent {
    private List<AdditionalAsset> additionalAssets;

    public List<AdditionalAsset> getAdditionalAssets() {
        return this.additionalAssets;
    }

    public AdditionalAssetsComponent(ComponentManager manager) throws Exception {
        super(manager);
        this.additionalAssets = new ArrayList();
    }

    @Override // org.tlauncher.tlauncher.component.InterruptibleComponent
    protected boolean refresh(int refreshID) {
        String result = CoreConstants.EMPTY_STRING;
        try {
            result = ClientInstanceRepo.EXTRA_VERSION_REPO.getUrl("additional_assets-1.0.json");
            Gson gson = (Gson) TLauncher.getInjector().getInstance(Key.get(Gson.class, Names.named("GsonAdditionalFile")));
            this.additionalAssets = (List) gson.fromJson(result, new TypeToken<List<AdditionalAsset>>() { // from class: org.tlauncher.tlauncher.managers.AdditionalAssetsComponent.1
            }.getType());
            return true;
        } catch (JsonSyntaxException | IOException e) {
            U.log(e);
            U.log(result);
            return false;
        }
    }
}
