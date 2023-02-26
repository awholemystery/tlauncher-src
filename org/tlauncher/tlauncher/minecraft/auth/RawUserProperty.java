package org.tlauncher.tlauncher.minecraft.auth;

import ch.qos.logback.core.joran.action.Action;
import java.util.LinkedHashMap;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/minecraft/auth/RawUserProperty.class */
class RawUserProperty extends LinkedHashMap<String, String> {
    private static final long serialVersionUID = 1281494999913983388L;

    RawUserProperty() {
    }

    public UserProperty toProperty() {
        return new UserProperty(get(Action.NAME_ATTRIBUTE), get("value"));
    }
}
