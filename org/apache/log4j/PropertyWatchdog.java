package org.apache.log4j;

import org.apache.log4j.helpers.FileWatchdog;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: PropertyConfigurator.java */
/* loaded from: TLauncher-2.876.jar:org/apache/log4j/PropertyWatchdog.class */
public class PropertyWatchdog extends FileWatchdog {
    /* JADX INFO: Access modifiers changed from: package-private */
    public PropertyWatchdog(String filename) {
        super(filename);
    }

    @Override // org.apache.log4j.helpers.FileWatchdog
    public void doOnChange() {
        new PropertyConfigurator().doConfigure(this.filename, LogManager.getLoggerRepository());
    }
}
