package org.tlauncher.tlauncher.ui.loc;

import java.awt.Component;
import java.awt.Container;
import org.tlauncher.tlauncher.configuration.LangConfiguration;
import org.tlauncher.util.Reflect;
import org.tlauncher.util.U;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/loc/Localizable.class */
public class Localizable {
    public static final Object[] EMPTY_VARS = new Object[0];
    private static final LocalizableFilter defaultFilter = comp -> {
        return true;
    };
    private static LangConfiguration lang;

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/loc/Localizable$LocalizableFilter.class */
    public interface LocalizableFilter {
        boolean localize(Component component);
    }

    public static void setLang(LangConfiguration l) {
        lang = l;
    }

    public static LangConfiguration get() {
        return lang;
    }

    public static boolean exists() {
        return lang != null;
    }

    public static String get(String path) {
        return lang != null ? lang.get(path) : path;
    }

    public static String get(String path, Object... vars) {
        return lang != null ? lang.get(path, vars) : path + " {" + U.toLog(vars) + "}";
    }

    public static String getByKeys(String path, Object... keys) {
        return lang != null ? lang.ngetKeys(path, keys) : path + " {" + U.toLog(keys) + "}";
    }

    public static String nget(String path) {
        if (lang != null) {
            return lang.nget(path);
        }
        return null;
    }

    public static String[] checkVariables(Object[] check) {
        if (check == null) {
            throw new NullPointerException();
        }
        String[] string = new String[check.length];
        for (int i = 0; i < check.length; i++) {
            if (check[i] == null) {
                throw new NullPointerException("Variable at index " + i + " is NULL!");
            }
            string[i] = check[i].toString();
        }
        return string;
    }

    public static void updateContainer(Container container, LocalizableFilter filter) {
        Container[] components;
        for (Container container2 : container.getComponents()) {
            LocalizableComponent asLocalizable = (LocalizableComponent) Reflect.cast(container2, LocalizableComponent.class);
            if (asLocalizable != null && filter.localize(container2)) {
                asLocalizable.updateLocale();
            }
            if (container2 instanceof Container) {
                updateContainer(container2, filter);
            }
        }
    }

    public static void updateContainer(Container container) {
        updateContainer(container, defaultFilter);
    }
}
