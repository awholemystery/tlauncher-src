package org.apache.http.protocol;

import java.util.HashMap;
import java.util.Map;
import org.apache.http.annotation.GuardedBy;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.util.Args;
import org.slf4j.Marker;

@ThreadSafe
/* loaded from: TLauncher-2.876.jar:org/apache/http/protocol/UriPatternMatcher.class */
public class UriPatternMatcher<T> {
    @GuardedBy("this")
    private final Map<String, T> map = new HashMap();

    public synchronized void register(String pattern, T obj) {
        Args.notNull(pattern, "URI request pattern");
        this.map.put(pattern, obj);
    }

    public synchronized void unregister(String pattern) {
        if (pattern == null) {
            return;
        }
        this.map.remove(pattern);
    }

    @Deprecated
    public synchronized void setHandlers(Map<String, T> map) {
        Args.notNull(map, "Map of handlers");
        this.map.clear();
        this.map.putAll(map);
    }

    @Deprecated
    public synchronized void setObjects(Map<String, T> map) {
        Args.notNull(map, "Map of handlers");
        this.map.clear();
        this.map.putAll(map);
    }

    @Deprecated
    public synchronized Map<String, T> getObjects() {
        return this.map;
    }

    public synchronized T lookup(String path) {
        Args.notNull(path, "Request path");
        T obj = this.map.get(path);
        if (obj == null) {
            String bestMatch = null;
            for (String pattern : this.map.keySet()) {
                if (matchUriRequestPattern(pattern, path) && (bestMatch == null || bestMatch.length() < pattern.length() || (bestMatch.length() == pattern.length() && pattern.endsWith(Marker.ANY_MARKER)))) {
                    obj = this.map.get(pattern);
                    bestMatch = pattern;
                }
            }
        }
        return obj;
    }

    protected boolean matchUriRequestPattern(String pattern, String path) {
        if (pattern.equals(Marker.ANY_MARKER)) {
            return true;
        }
        return (pattern.endsWith(Marker.ANY_MARKER) && path.startsWith(pattern.substring(0, pattern.length() - 1))) || (pattern.startsWith(Marker.ANY_MARKER) && path.endsWith(pattern.substring(1, pattern.length())));
    }

    public String toString() {
        return this.map.toString();
    }
}
