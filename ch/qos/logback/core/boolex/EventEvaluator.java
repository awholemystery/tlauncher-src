package ch.qos.logback.core.boolex;

import ch.qos.logback.core.spi.ContextAware;
import ch.qos.logback.core.spi.LifeCycle;

/* loaded from: TLauncher-2.876.jar:ch/qos/logback/core/boolex/EventEvaluator.class */
public interface EventEvaluator<E> extends ContextAware, LifeCycle {
    boolean evaluate(E e) throws NullPointerException, EvaluationException;

    String getName();

    void setName(String str);
}
