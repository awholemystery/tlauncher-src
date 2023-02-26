package ch.qos.logback.core.joran.event;

import org.xml.sax.Locator;

/* loaded from: TLauncher-2.876.jar:ch/qos/logback/core/joran/event/EndEvent.class */
public class EndEvent extends SaxEvent {
    /* JADX INFO: Access modifiers changed from: package-private */
    public EndEvent(String namespaceURI, String localName, String qName, Locator locator) {
        super(namespaceURI, localName, qName, locator);
    }

    public String toString() {
        return "  EndEvent(" + getQName() + ")  [" + this.locator.getLineNumber() + "," + this.locator.getColumnNumber() + "]";
    }
}
