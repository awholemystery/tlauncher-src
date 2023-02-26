package org.apache.log4j.pattern;

/* loaded from: TLauncher-2.876.jar:org/apache/log4j/pattern/PatternConverter.class */
public abstract class PatternConverter {
    private final String name;
    private final String style;

    public abstract void format(Object obj, StringBuffer stringBuffer);

    /* JADX INFO: Access modifiers changed from: protected */
    public PatternConverter(String name, String style) {
        this.name = name;
        this.style = style;
    }

    public final String getName() {
        return this.name;
    }

    public String getStyleClass(Object e) {
        return this.style;
    }
}
