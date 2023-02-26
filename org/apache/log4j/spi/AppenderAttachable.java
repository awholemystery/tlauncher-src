package org.apache.log4j.spi;

import java.util.Enumeration;
import org.apache.log4j.Appender;

/* loaded from: TLauncher-2.876.jar:org/apache/log4j/spi/AppenderAttachable.class */
public interface AppenderAttachable {
    void addAppender(Appender appender);

    Enumeration getAllAppenders();

    Appender getAppender(String str);

    boolean isAttached(Appender appender);

    void removeAllAppenders();

    void removeAppender(Appender appender);

    void removeAppender(String str);
}
