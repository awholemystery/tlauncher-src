package org.apache.log4j;

import java.util.Vector;

/* loaded from: TLauncher-2.876.jar:org/apache/log4j/ProvisionNode.class */
class ProvisionNode extends Vector {
    private static final long serialVersionUID = -4479121426311014469L;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ProvisionNode(Logger logger) {
        addElement(logger);
    }
}
