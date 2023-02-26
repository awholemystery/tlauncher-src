package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.function.Function;
import java.util.regex.Pattern;
import org.apache.commons.io.IOCase;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/io/filefilter/RegexFileFilter.class */
public class RegexFileFilter extends AbstractFileFilter implements Serializable {
    private static final long serialVersionUID = 4269646126155225062L;
    private final Pattern pattern;
    private final Function<Path, String> pathToString;

    private static Pattern compile(String pattern, int flags) {
        if (pattern == null) {
            throw new IllegalArgumentException("Pattern is missing");
        }
        return Pattern.compile(pattern, flags);
    }

    private static int toFlags(IOCase caseSensitivity) {
        return IOCase.isCaseSensitive(caseSensitivity) ? 2 : 0;
    }

    public RegexFileFilter(Pattern pattern) {
        this(pattern, p -> {
            return p.getFileName().toString();
        });
    }

    public RegexFileFilter(Pattern pattern, Function<Path, String> pathToString) {
        if (pattern == null) {
            throw new IllegalArgumentException("Pattern is missing");
        }
        this.pattern = pattern;
        this.pathToString = pathToString;
    }

    public RegexFileFilter(String pattern) {
        this(pattern, 0);
    }

    public RegexFileFilter(String pattern, int flags) {
        this(compile(pattern, flags));
    }

    public RegexFileFilter(String pattern, IOCase caseSensitivity) {
        this(compile(pattern, toFlags(caseSensitivity)));
    }

    @Override // org.apache.commons.io.filefilter.AbstractFileFilter, org.apache.commons.io.filefilter.IOFileFilter, java.io.FilenameFilter
    public boolean accept(File dir, String name) {
        return this.pattern.matcher(name).matches();
    }

    @Override // org.apache.commons.io.filefilter.IOFileFilter, org.apache.commons.io.file.PathFilter
    public FileVisitResult accept(Path path, BasicFileAttributes attributes) {
        return toFileVisitResult(this.pattern.matcher(this.pathToString.apply(path)).matches(), path);
    }

    @Override // org.apache.commons.io.filefilter.AbstractFileFilter
    public String toString() {
        return "RegexFileFilter [pattern=" + this.pattern + "]";
    }
}
