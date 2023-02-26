package org.apache.commons.compress.utils;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/utils/OsgiUtils.class */
public class OsgiUtils {
    private static final boolean inOsgiEnvironment;

    static {
        Class<?> classloaderClass = OsgiUtils.class.getClassLoader().getClass();
        inOsgiEnvironment = isBundleReference(classloaderClass);
    }

    private static boolean isBundleReference(Class<?> clazz) {
        Class<?>[] interfaces;
        Class<?> cls = clazz;
        while (true) {
            Class<?> c = cls;
            if (c != null) {
                if (c.getName().equals("org.osgi.framework.BundleReference")) {
                    return true;
                }
                for (Class<?> ifc : c.getInterfaces()) {
                    if (isBundleReference(ifc)) {
                        return true;
                    }
                }
                cls = c.getSuperclass();
            } else {
                return false;
            }
        }
    }

    public static boolean isRunningInOsgiEnvironment() {
        return inOsgiEnvironment;
    }
}
