package org.apache.http;

import org.apache.http.util.CharArrayBuffer;

/* loaded from: TLauncher-2.876.jar:org/apache/http/FormattedHeader.class */
public interface FormattedHeader extends Header {
    CharArrayBuffer getBuffer();

    int getValuePos();
}
