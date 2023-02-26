package ch.qos.logback.core.joran.spi;

import ch.qos.logback.core.joran.action.Action;
import java.util.List;

/* loaded from: TLauncher-2.876.jar:ch/qos/logback/core/joran/spi/RuleStore.class */
public interface RuleStore {
    void addRule(ElementSelector elementSelector, String str) throws ClassNotFoundException;

    void addRule(ElementSelector elementSelector, Action action);

    List<Action> matchActions(ElementPath elementPath);
}
