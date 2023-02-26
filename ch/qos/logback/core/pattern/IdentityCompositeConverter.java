package ch.qos.logback.core.pattern;

/* loaded from: TLauncher-2.876.jar:ch/qos/logback/core/pattern/IdentityCompositeConverter.class */
public class IdentityCompositeConverter<E> extends CompositeConverter<E> {
    @Override // ch.qos.logback.core.pattern.CompositeConverter
    protected String transform(E event, String in) {
        return in;
    }
}
