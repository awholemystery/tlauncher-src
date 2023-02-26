package ch.qos.logback.core.util;

import ch.qos.logback.core.CoreConstants;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/* loaded from: TLauncher-2.876.jar:ch/qos/logback/core/util/JNDIUtil.class */
public class JNDIUtil {
    static final String RESTRICTION_MSG = "JNDI name must start with java: but was ";

    public static Context getInitialContext() throws NamingException {
        return new InitialContext();
    }

    public static String lookup(Context ctx, String name) throws NamingException {
        if (ctx == null || OptionHelper.isEmpty(name)) {
            return null;
        }
        if (!name.startsWith(CoreConstants.JNDI_JAVA_NAMESPACE)) {
            throw new NamingException(RESTRICTION_MSG + name);
        }
        Object lookup = ctx.lookup(name);
        if (lookup == null) {
            return null;
        }
        return lookup.toString();
    }
}
