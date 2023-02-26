package ch.qos.logback.classic.boolex;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.boolex.EvaluationException;
import ch.qos.logback.core.boolex.EventEvaluatorBase;

/* loaded from: TLauncher-2.876.jar:ch/qos/logback/classic/boolex/OnErrorEvaluator.class */
public class OnErrorEvaluator extends EventEvaluatorBase<ILoggingEvent> {
    @Override // ch.qos.logback.core.boolex.EventEvaluator
    public boolean evaluate(ILoggingEvent event) throws NullPointerException, EvaluationException {
        return event.getLevel().levelInt >= 40000;
    }
}
