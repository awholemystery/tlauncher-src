package org.apache.log4j.xml;

import java.util.Properties;
import org.w3c.dom.Element;

/* loaded from: TLauncher-2.876.jar:org/apache/log4j/xml/UnrecognizedElementHandler.class */
public interface UnrecognizedElementHandler {
    boolean parseUnrecognizedElement(Element element, Properties properties) throws Exception;
}
