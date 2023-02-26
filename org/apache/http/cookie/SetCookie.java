package org.apache.http.cookie;

import java.util.Date;
import org.apache.http.annotation.Obsolete;

/* loaded from: TLauncher-2.876.jar:org/apache/http/cookie/SetCookie.class */
public interface SetCookie extends Cookie {
    void setValue(String str);

    @Obsolete
    void setComment(String str);

    void setExpiryDate(Date date);

    void setDomain(String str);

    void setPath(String str);

    void setSecure(boolean z);

    @Obsolete
    void setVersion(int i);
}
