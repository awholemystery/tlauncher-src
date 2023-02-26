package ch.qos.logback.core.joran.action;

import ch.qos.logback.core.joran.spi.InterpretationContext;
import ch.qos.logback.core.joran.util.PropertySetter;
import ch.qos.logback.core.joran.util.beans.BeanDescriptionCache;
import org.xml.sax.Attributes;

/* loaded from: TLauncher-2.876.jar:ch/qos/logback/core/joran/action/ParamAction.class */
public class ParamAction extends Action {
    static String NO_NAME = "No name attribute in <param> element";
    static String NO_VALUE = "No value attribute in <param> element";
    boolean inError = false;
    private final BeanDescriptionCache beanDescriptionCache;

    public ParamAction(BeanDescriptionCache beanDescriptionCache) {
        this.beanDescriptionCache = beanDescriptionCache;
    }

    @Override // ch.qos.logback.core.joran.action.Action
    public void begin(InterpretationContext ec, String localName, Attributes attributes) {
        String name = attributes.getValue(Action.NAME_ATTRIBUTE);
        String value = attributes.getValue("value");
        if (name == null) {
            this.inError = true;
            addError(NO_NAME);
        } else if (value == null) {
            this.inError = true;
            addError(NO_VALUE);
        } else {
            String value2 = value.trim();
            Object o = ec.peekObject();
            PropertySetter propSetter = new PropertySetter(this.beanDescriptionCache, o);
            propSetter.setContext(this.context);
            propSetter.setProperty(ec.subst(name), ec.subst(value2));
        }
    }

    @Override // ch.qos.logback.core.joran.action.Action
    public void end(InterpretationContext ec, String localName) {
    }

    public void finish(InterpretationContext ec) {
    }
}
