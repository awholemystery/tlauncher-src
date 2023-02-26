package org.apache.log4j;

/* compiled from: PropertyConfigurator.java */
/* loaded from: TLauncher-2.876.jar:org/apache/log4j/NameValue.class */
class NameValue {
    String key;
    String value;

    public NameValue(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String toString() {
        return new StringBuffer().append(this.key).append("=").append(this.value).toString();
    }
}
