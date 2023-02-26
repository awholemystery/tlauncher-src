package ch.qos.logback.core.spi;

import java.io.Serializable;

/* loaded from: TLauncher-2.876.jar:ch/qos/logback/core/spi/PreSerializationTransformer.class */
public interface PreSerializationTransformer<E> {
    Serializable transform(E e);
}
