package org.apache.log4j.pattern;

/* loaded from: TLauncher-2.876.jar:org/apache/log4j/pattern/FileDatePatternConverter.class */
public final class FileDatePatternConverter {
    private FileDatePatternConverter() {
    }

    public static PatternConverter newInstance(String[] options) {
        return (options == null || options.length == 0) ? DatePatternConverter.newInstance(new String[]{"yyyy-MM-dd"}) : DatePatternConverter.newInstance(options);
    }
}
