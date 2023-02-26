package org.tlauncher.tlauncher.ui.util;

import java.util.regex.Pattern;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/ui/util/ValidateUtil.class */
public class ValidateUtil {
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public static boolean validateEmail(String line) {
        return Pattern.compile(EMAIL_PATTERN).matcher(line).matches();
    }
}
