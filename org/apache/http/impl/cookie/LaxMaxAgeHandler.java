package org.apache.http.impl.cookie;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.http.annotation.Immutable;
import org.apache.http.cookie.ClientCookie;
import org.apache.http.cookie.CommonCookieAttributeHandler;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.cookie.SM;
import org.apache.http.cookie.SetCookie;
import org.apache.http.util.Args;
import org.apache.http.util.TextUtils;

@Immutable
/* loaded from: TLauncher-2.876.jar:org/apache/http/impl/cookie/LaxMaxAgeHandler.class */
public class LaxMaxAgeHandler extends AbstractCookieAttributeHandler implements CommonCookieAttributeHandler {
    private static final Pattern MAX_AGE_PATTERN = Pattern.compile("^\\-?[0-9]+$");

    @Override // org.apache.http.cookie.CookieAttributeHandler
    public void parse(SetCookie cookie, String value) throws MalformedCookieException {
        Args.notNull(cookie, SM.COOKIE);
        if (TextUtils.isBlank(value)) {
            return;
        }
        Matcher matcher = MAX_AGE_PATTERN.matcher(value);
        if (matcher.matches()) {
            try {
                int age = Integer.parseInt(value);
                Date expiryDate = age >= 0 ? new Date(System.currentTimeMillis() + (age * 1000)) : new Date(Long.MIN_VALUE);
                cookie.setExpiryDate(expiryDate);
            } catch (NumberFormatException e) {
            }
        }
    }

    @Override // org.apache.http.cookie.CommonCookieAttributeHandler
    public String getAttributeName() {
        return ClientCookie.MAX_AGE_ATTR;
    }
}
