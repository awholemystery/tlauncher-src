package org.apache.log4j.xml;

import org.apache.log4j.LogManager;
import org.apache.log4j.helpers.FileWatchdog;

/* compiled from: DOMConfigurator.java */
/* loaded from: TLauncher-2.876.jar:org/apache/log4j/xml/XMLWatchdog.class */
class XMLWatchdog extends FileWatchdog {
    /* JADX INFO: Access modifiers changed from: package-private */
    public XMLWatchdog(String filename) {
        super(filename);
    }

    @Override // org.apache.log4j.helpers.FileWatchdog
    public void doOnChange() {
        new DOMConfigurator().doConfigure(this.filename, LogManager.getLoggerRepository());
    }
}
