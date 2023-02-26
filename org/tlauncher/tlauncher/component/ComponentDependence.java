package org.tlauncher.tlauncher.component;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/component/ComponentDependence.class */
public @interface ComponentDependence {
    Class<?>[] value();
}
