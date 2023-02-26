package org.tlauncher.util;

import java.util.Hashtable;
import java.util.Map;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/util/Time.class */
public class Time {
    private static Map<Object, Long> timers = new Hashtable();

    public static void start(Object holder) {
        if (timers.containsKey(holder)) {
            throw new IllegalArgumentException("This holder (" + holder.toString() + ") is already in use!");
        }
        timers.put(holder, Long.valueOf(System.currentTimeMillis()));
    }

    public static long stop(Object holder) {
        long current = System.currentTimeMillis();
        Long l = timers.get(holder);
        if (l == null) {
            return 0L;
        }
        timers.remove(holder);
        return current - l.longValue();
    }

    public static void start() {
        start(Thread.currentThread());
    }

    public static long stop() {
        return stop(Thread.currentThread());
    }
}
