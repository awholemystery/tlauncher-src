package ch.qos.logback.core.joran.spi;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: TLauncher-2.876.jar:ch/qos/logback/core/joran/spi/DefaultClass.class */
public @interface DefaultClass {
    Class<?> value();
}
