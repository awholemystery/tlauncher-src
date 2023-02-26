package org.apache.commons.compress.archivers.sevenz;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/archivers/sevenz/BindPair.class */
class BindPair {
    long inIndex;
    long outIndex;

    public String toString() {
        return "BindPair binding input " + this.inIndex + " to output " + this.outIndex;
    }
}
