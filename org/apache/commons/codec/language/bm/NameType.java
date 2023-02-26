package org.apache.commons.codec.language.bm;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/codec/language/bm/NameType.class */
public enum NameType {
    ASHKENAZI("ash"),
    GENERIC("gen"),
    SEPHARDIC("sep");
    
    private final String name;

    NameType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
