package org.tlauncher.tlauncher.minecraft.user.preq;

import org.apache.commons.lang3.StringUtils;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/minecraft/user/preq/Validatable.class */
public interface Validatable {
    void validate();

    static void notNull(Object o, String name) {
        if (o == null) {
            throw new NullPointerException(name);
        }
    }

    static void notEmpty(String s, String name) {
        if (s == null) {
            throw new NullPointerException(name);
        }
        if (StringUtils.isEmpty(s)) {
            throw new IllegalArgumentException("blank " + name);
        }
    }

    static void notNegative(int i, String name) {
        if (i < 0) {
            throw new IllegalArgumentException("negative " + name);
        }
    }
}
