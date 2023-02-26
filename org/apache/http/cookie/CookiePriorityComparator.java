package org.apache.http.cookie;

import java.util.Comparator;
import java.util.Date;
import org.apache.http.annotation.Immutable;
import org.apache.http.impl.cookie.BasicClientCookie;

@Immutable
/* loaded from: TLauncher-2.876.jar:org/apache/http/cookie/CookiePriorityComparator.class */
public class CookiePriorityComparator implements Comparator<Cookie> {
    public static final CookiePriorityComparator INSTANCE = new CookiePriorityComparator();

    private int getPathLength(Cookie cookie) {
        String path = cookie.getPath();
        if (path != null) {
            return path.length();
        }
        return 1;
    }

    @Override // java.util.Comparator
    public int compare(Cookie c1, Cookie c2) {
        int l1 = getPathLength(c1);
        int l2 = getPathLength(c2);
        int result = l2 - l1;
        if (result == 0 && (c1 instanceof BasicClientCookie) && (c2 instanceof BasicClientCookie)) {
            Date d1 = ((BasicClientCookie) c1).getCreationDate();
            Date d2 = ((BasicClientCookie) c2).getCreationDate();
            if (d1 != null && d2 != null) {
                return (int) (d1.getTime() - d2.getTime());
            }
        }
        return result;
    }
}
