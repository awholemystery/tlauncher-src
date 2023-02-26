package ch.qos.logback.core.spi;

import java.util.Map;

/* loaded from: TLauncher-2.876.jar:ch/qos/logback/core/spi/PropertyContainer.class */
public interface PropertyContainer {
    String getProperty(String str);

    Map<String, String> getCopyOfPropertyMap();
}
