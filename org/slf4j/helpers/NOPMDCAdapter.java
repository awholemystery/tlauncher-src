package org.slf4j.helpers;

import java.util.Map;
import org.slf4j.spi.MDCAdapter;

/* loaded from: TLauncher-2.876.jar:org/slf4j/helpers/NOPMDCAdapter.class */
public class NOPMDCAdapter implements MDCAdapter {
    @Override // org.slf4j.spi.MDCAdapter
    public void clear() {
    }

    @Override // org.slf4j.spi.MDCAdapter
    public String get(String key) {
        return null;
    }

    @Override // org.slf4j.spi.MDCAdapter
    public void put(String key, String val) {
    }

    @Override // org.slf4j.spi.MDCAdapter
    public void remove(String key) {
    }

    @Override // org.slf4j.spi.MDCAdapter
    public Map getCopyOfContextMap() {
        return null;
    }

    @Override // org.slf4j.spi.MDCAdapter
    public void setContextMap(Map contextMap) {
    }
}
