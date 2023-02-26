package ch.qos.logback.core.boolex;

import ch.qos.logback.core.spi.ContextAwareBase;

/* loaded from: TLauncher-2.876.jar:ch/qos/logback/core/boolex/EventEvaluatorBase.class */
public abstract class EventEvaluatorBase<E> extends ContextAwareBase implements EventEvaluator<E> {
    String name;
    boolean started;

    @Override // ch.qos.logback.core.boolex.EventEvaluator
    public String getName() {
        return this.name;
    }

    @Override // ch.qos.logback.core.boolex.EventEvaluator
    public void setName(String name) {
        if (this.name != null) {
            throw new IllegalStateException("name has been already set");
        }
        this.name = name;
    }

    @Override // ch.qos.logback.core.spi.LifeCycle
    public boolean isStarted() {
        return this.started;
    }

    @Override // ch.qos.logback.core.spi.LifeCycle
    public void start() {
        this.started = true;
    }

    @Override // ch.qos.logback.core.spi.LifeCycle
    public void stop() {
        this.started = false;
    }
}