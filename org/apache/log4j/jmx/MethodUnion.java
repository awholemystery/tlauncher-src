package org.apache.log4j.jmx;

import java.lang.reflect.Method;

/* loaded from: TLauncher-2.876.jar:org/apache/log4j/jmx/MethodUnion.class */
class MethodUnion {
    Method readMethod;
    Method writeMethod;

    /* JADX INFO: Access modifiers changed from: package-private */
    public MethodUnion(Method readMethod, Method writeMethod) {
        this.readMethod = readMethod;
        this.writeMethod = writeMethod;
    }
}
