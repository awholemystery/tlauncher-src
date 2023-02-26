package ch.qos.logback.core.pattern;

import ch.qos.logback.core.Context;

/* loaded from: TLauncher-2.876.jar:ch/qos/logback/core/pattern/PostCompileProcessor.class */
public interface PostCompileProcessor<E> {
    void process(Context context, Converter<E> converter);
}
