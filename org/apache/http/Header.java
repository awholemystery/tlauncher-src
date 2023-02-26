package org.apache.http;

/* loaded from: TLauncher-2.876.jar:org/apache/http/Header.class */
public interface Header {
    String getName();

    String getValue();

    HeaderElement[] getElements() throws ParseException;
}
