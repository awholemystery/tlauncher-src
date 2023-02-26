package com.google.gson.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Documented
@Retention(RetentionPolicy.RUNTIME)
/* loaded from: TLauncher-2.876.jar:com/google/gson/annotations/Expose.class */
public @interface Expose {
    boolean serialize() default true;

    boolean deserialize() default true;
}
