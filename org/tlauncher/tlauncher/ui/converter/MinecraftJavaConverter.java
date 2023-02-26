package org.tlauncher.tlauncher.ui.converter;

import ch.qos.logback.core.CoreConstants;
import com.google.inject.Inject;
import java.util.Objects;
import org.tlauncher.tlauncher.controller.JavaMinecraftController;
import org.tlauncher.tlauncher.entity.minecraft.MinecraftJava;
import org.tlauncher.tlauncher.ui.loc.Localizable;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/converter/MinecraftJavaConverter.class */
public class MinecraftJavaConverter implements StringConverter<MinecraftJava.CompleteMinecraftJava> {
    @Inject
    private JavaMinecraftController controller;

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.tlauncher.tlauncher.ui.converter.StringConverter
    public MinecraftJava.CompleteMinecraftJava fromString(String from) {
        if (Objects.isNull(from)) {
            return null;
        }
        return this.controller.getById(Long.valueOf(from));
    }

    @Override // org.tlauncher.tlauncher.ui.converter.StringConverter
    public String toString(MinecraftJava.CompleteMinecraftJava from) {
        if (Objects.isNull(from)) {
            return null;
        }
        return Localizable.get(from.getName());
    }

    @Override // org.tlauncher.tlauncher.ui.converter.StringConverter
    public String toValue(MinecraftJava.CompleteMinecraftJava from) {
        if (Objects.isNull(from)) {
            return null;
        }
        return CoreConstants.EMPTY_STRING + from.getId();
    }

    @Override // org.tlauncher.tlauncher.ui.converter.StringConverter
    public Class<MinecraftJava.CompleteMinecraftJava> getObjectClass() {
        return MinecraftJava.CompleteMinecraftJava.class;
    }
}
