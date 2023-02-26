package org.slf4j;

import org.slf4j.helpers.BasicMarkerFactory;
import org.slf4j.helpers.Util;
import org.slf4j.impl.StaticMarkerBinder;

/* loaded from: TLauncher-2.876.jar:org/slf4j/MarkerFactory.class */
public class MarkerFactory {
    static IMarkerFactory markerFactory;

    private MarkerFactory() {
    }

    static {
        try {
            markerFactory = StaticMarkerBinder.SINGLETON.getMarkerFactory();
        } catch (Exception e) {
            Util.report("Unexpected failure while binding MarkerFactory", e);
        } catch (NoClassDefFoundError e2) {
            markerFactory = new BasicMarkerFactory();
        }
    }

    public static Marker getMarker(String name) {
        return markerFactory.getMarker(name);
    }

    public static Marker getDetachedMarker(String name) {
        return markerFactory.getDetachedMarker(name);
    }

    public static IMarkerFactory getIMarkerFactory() {
        return markerFactory;
    }
}
