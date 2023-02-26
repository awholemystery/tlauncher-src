package ch.qos.logback.core.joran.conditional;

import ch.qos.logback.core.joran.event.SaxEvent;
import java.util.List;

/* loaded from: TLauncher-2.876.jar:ch/qos/logback/core/joran/conditional/ThenAction.class */
public class ThenAction extends ThenOrElseActionBase {
    @Override // ch.qos.logback.core.joran.conditional.ThenOrElseActionBase
    void registerEventList(IfAction ifAction, List<SaxEvent> eventList) {
        ifAction.setThenSaxEventList(eventList);
    }
}
