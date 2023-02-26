package org.apache.log4j.pattern;

/* loaded from: TLauncher-2.876.jar:org/apache/log4j/pattern/NamePatternConverter.class */
public abstract class NamePatternConverter extends LoggingEventPatternConverter {
    private final NameAbbreviator abbreviator;

    /* JADX INFO: Access modifiers changed from: protected */
    public NamePatternConverter(String name, String style, String[] options) {
        super(name, style);
        if (options != null && options.length > 0) {
            this.abbreviator = NameAbbreviator.getAbbreviator(options[0]);
        } else {
            this.abbreviator = NameAbbreviator.getDefaultAbbreviator();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void abbreviate(int nameStart, StringBuffer buf) {
        this.abbreviator.abbreviate(nameStart, buf);
    }
}
