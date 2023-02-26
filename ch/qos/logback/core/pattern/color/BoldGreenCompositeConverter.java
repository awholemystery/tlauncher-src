package ch.qos.logback.core.pattern.color;

/* loaded from: TLauncher-2.876.jar:ch/qos/logback/core/pattern/color/BoldGreenCompositeConverter.class */
public class BoldGreenCompositeConverter<E> extends ForegroundCompositeConverterBase<E> {
    @Override // ch.qos.logback.core.pattern.color.ForegroundCompositeConverterBase
    protected String getForegroundColorCode(E event) {
        return "1;32";
    }
}