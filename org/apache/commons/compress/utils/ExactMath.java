package org.apache.commons.compress.utils;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/utils/ExactMath.class */
public class ExactMath {
    private ExactMath() {
    }

    public static int add(int x, long y) {
        return Math.addExact(x, Math.toIntExact(y));
    }
}
