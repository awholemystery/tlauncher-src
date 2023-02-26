package ch.qos.logback.core.pattern.color;

/* loaded from: TLauncher-2.876.jar:ch/qos/logback/core/pattern/color/BlueCompositeConverter.class */
public class BlueCompositeConverter<E> extends ForegroundCompositeConverterBase<E> {
    @Override // ch.qos.logback.core.pattern.color.ForegroundCompositeConverterBase
    protected String getForegroundColorCode(E event) {
        return ANSIConstants.BLUE_FG;
    }
}