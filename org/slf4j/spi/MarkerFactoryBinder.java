package org.slf4j.spi;

import org.slf4j.IMarkerFactory;

/* loaded from: TLauncher-2.876.jar:org/slf4j/spi/MarkerFactoryBinder.class */
public interface MarkerFactoryBinder {
    IMarkerFactory getMarkerFactory();

    String getMarkerFactoryClassStr();
}
