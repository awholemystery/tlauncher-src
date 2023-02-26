package ch.qos.logback.core.pattern.util;

/* loaded from: TLauncher-2.876.jar:ch/qos/logback/core/pattern/util/RestrictedEscapeUtil.class */
public class RestrictedEscapeUtil implements IEscapeUtil {
    @Override // ch.qos.logback.core.pattern.util.IEscapeUtil
    public void escape(String escapeChars, StringBuffer buf, char next, int pointer) {
        if (escapeChars.indexOf(next) >= 0) {
            buf.append(next);
            return;
        }
        buf.append("\\");
        buf.append(next);
    }
}
