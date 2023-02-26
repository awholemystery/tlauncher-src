package ch.qos.logback.classic.spi;

import ch.qos.logback.classic.Logger;
import java.util.Comparator;

/* loaded from: TLauncher-2.876.jar:ch/qos/logback/classic/spi/LoggerComparator.class */
public class LoggerComparator implements Comparator<Logger> {
    @Override // java.util.Comparator
    public int compare(Logger l1, Logger l2) {
        if (l1.getName().equals(l2.getName())) {
            return 0;
        }
        if (l1.getName().equals(org.slf4j.Logger.ROOT_LOGGER_NAME)) {
            return -1;
        }
        if (l2.getName().equals(org.slf4j.Logger.ROOT_LOGGER_NAME)) {
            return 1;
        }
        return l1.getName().compareTo(l2.getName());
    }
}
