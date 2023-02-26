package org.apache.log4j.rewrite;

import org.apache.log4j.spi.LoggingEvent;

/* loaded from: TLauncher-2.876.jar:org/apache/log4j/rewrite/RewritePolicy.class */
public interface RewritePolicy {
    LoggingEvent rewrite(LoggingEvent loggingEvent);
}
