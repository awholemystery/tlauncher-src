package org.apache.commons.codec.language.bm;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/codec/language/bm/RuleType.class */
public enum RuleType {
    APPROX("approx"),
    EXACT("exact"),
    RULES("rules");
    
    private final String name;

    RuleType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
