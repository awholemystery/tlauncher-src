package org.apache.http.params;

@Deprecated
/* loaded from: TLauncher-2.876.jar:org/apache/http/params/HttpParams.class */
public interface HttpParams {
    Object getParameter(String str);

    HttpParams setParameter(String str, Object obj);

    HttpParams copy();

    boolean removeParameter(String str);

    long getLongParameter(String str, long j);

    HttpParams setLongParameter(String str, long j);

    int getIntParameter(String str, int i);

    HttpParams setIntParameter(String str, int i);

    double getDoubleParameter(String str, double d);

    HttpParams setDoubleParameter(String str, double d);

    boolean getBooleanParameter(String str, boolean z);

    HttpParams setBooleanParameter(String str, boolean z);

    boolean isParameterTrue(String str);

    boolean isParameterFalse(String str);
}
