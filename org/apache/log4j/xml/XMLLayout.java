package org.apache.log4j.xml;

import java.util.Arrays;
import java.util.Set;
import org.apache.log4j.Layout;
import org.apache.log4j.helpers.Transform;
import org.apache.log4j.spi.LocationInfo;
import org.apache.log4j.spi.LoggingEvent;

/* loaded from: TLauncher-2.876.jar:org/apache/log4j/xml/XMLLayout.class */
public class XMLLayout extends Layout {
    private final int DEFAULT_SIZE = 256;
    private final int UPPER_LIMIT = 2048;
    private StringBuffer buf = new StringBuffer(256);
    private boolean locationInfo = false;
    private boolean properties = false;

    public void setLocationInfo(boolean flag) {
        this.locationInfo = flag;
    }

    public boolean getLocationInfo() {
        return this.locationInfo;
    }

    public void setProperties(boolean flag) {
        this.properties = flag;
    }

    public boolean getProperties() {
        return this.properties;
    }

    @Override // org.apache.log4j.spi.OptionHandler
    public void activateOptions() {
    }

    @Override // org.apache.log4j.Layout
    public String format(LoggingEvent event) {
        if (this.buf.capacity() > 2048) {
            this.buf = new StringBuffer(256);
        } else {
            this.buf.setLength(0);
        }
        this.buf.append("<log4j:event logger=\"");
        this.buf.append(Transform.escapeTags(event.getLoggerName()));
        this.buf.append("\" timestamp=\"");
        this.buf.append(event.timeStamp);
        this.buf.append("\" level=\"");
        this.buf.append(Transform.escapeTags(String.valueOf(event.getLevel())));
        this.buf.append("\" thread=\"");
        this.buf.append(Transform.escapeTags(event.getThreadName()));
        this.buf.append("\">\r\n");
        this.buf.append("<log4j:message><![CDATA[");
        Transform.appendEscapingCDATA(this.buf, event.getRenderedMessage());
        this.buf.append("]]></log4j:message>\r\n");
        String ndc = event.getNDC();
        if (ndc != null) {
            this.buf.append("<log4j:NDC><![CDATA[");
            Transform.appendEscapingCDATA(this.buf, ndc);
            this.buf.append("]]></log4j:NDC>\r\n");
        }
        String[] s = event.getThrowableStrRep();
        if (s != null) {
            this.buf.append("<log4j:throwable><![CDATA[");
            for (String str : s) {
                Transform.appendEscapingCDATA(this.buf, str);
                this.buf.append("\r\n");
            }
            this.buf.append("]]></log4j:throwable>\r\n");
        }
        if (this.locationInfo) {
            LocationInfo locationInfo = event.getLocationInformation();
            this.buf.append("<log4j:locationInfo class=\"");
            this.buf.append(Transform.escapeTags(locationInfo.getClassName()));
            this.buf.append("\" method=\"");
            this.buf.append(Transform.escapeTags(locationInfo.getMethodName()));
            this.buf.append("\" file=\"");
            this.buf.append(Transform.escapeTags(locationInfo.getFileName()));
            this.buf.append("\" line=\"");
            this.buf.append(locationInfo.getLineNumber());
            this.buf.append("\"/>\r\n");
        }
        if (this.properties) {
            Set keySet = event.getPropertyKeySet();
            if (keySet.size() > 0) {
                this.buf.append("<log4j:properties>\r\n");
                Object[] keys = keySet.toArray();
                Arrays.sort(keys);
                for (Object obj : keys) {
                    String key = obj.toString();
                    Object val = event.getMDC(key);
                    if (val != null) {
                        this.buf.append("<log4j:data name=\"");
                        this.buf.append(Transform.escapeTags(key));
                        this.buf.append("\" value=\"");
                        this.buf.append(Transform.escapeTags(String.valueOf(val)));
                        this.buf.append("\"/>\r\n");
                    }
                }
                this.buf.append("</log4j:properties>\r\n");
            }
        }
        this.buf.append("</log4j:event>\r\n\r\n");
        return this.buf.toString();
    }

    @Override // org.apache.log4j.Layout
    public boolean ignoresThrowable() {
        return false;
    }
}
