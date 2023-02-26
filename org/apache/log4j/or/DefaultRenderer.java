package org.apache.log4j.or;

/* loaded from: TLauncher-2.876.jar:org/apache/log4j/or/DefaultRenderer.class */
class DefaultRenderer implements ObjectRenderer {
    @Override // org.apache.log4j.or.ObjectRenderer
    public String doRender(Object o) {
        try {
            return o.toString();
        } catch (Exception ex) {
            return ex.toString();
        }
    }
}
