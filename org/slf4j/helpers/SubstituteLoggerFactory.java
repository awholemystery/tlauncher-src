package org.slf4j.helpers;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

/* loaded from: TLauncher-2.876.jar:org/slf4j/helpers/SubstituteLoggerFactory.class */
public class SubstituteLoggerFactory implements ILoggerFactory {
    final List loggerNameList = new ArrayList();

    @Override // org.slf4j.ILoggerFactory
    public Logger getLogger(String name) {
        synchronized (this.loggerNameList) {
            this.loggerNameList.add(name);
        }
        return NOPLogger.NOP_LOGGER;
    }

    public List getLoggerNameList() {
        List copy = new ArrayList();
        synchronized (this.loggerNameList) {
            copy.addAll(this.loggerNameList);
        }
        return copy;
    }
}
