package org.apache.http.params;

import org.apache.http.util.Args;

@Deprecated
/* loaded from: TLauncher-2.876.jar:org/apache/http/params/HttpAbstractParamBean.class */
public abstract class HttpAbstractParamBean {
    protected final HttpParams params;

    public HttpAbstractParamBean(HttpParams params) {
        this.params = (HttpParams) Args.notNull(params, "HTTP parameters");
    }
}
