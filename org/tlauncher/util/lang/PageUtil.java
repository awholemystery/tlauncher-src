package org.tlauncher.util.lang;

import org.tlauncher.tlauncher.rmo.TLauncher;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/util/lang/PageUtil.class */
public class PageUtil {
    public static String buildPagePath() {
        StringBuilder append = new StringBuilder().append(TLauncher.getInstance().getPagePrefix());
        TLauncher.getInstance();
        return append.append(TLauncher.getInnerSettings().get(TLauncher.getInstance().getConfiguration().getLocale().getLanguage())).toString();
    }
}
