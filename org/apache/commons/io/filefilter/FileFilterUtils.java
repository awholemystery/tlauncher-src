package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOCase;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/io/filefilter/FileFilterUtils.class */
public class FileFilterUtils {
    private static final IOFileFilter cvsFilter = notFileFilter(and(directoryFileFilter(), nameFileFilter("CVS")));
    private static final IOFileFilter svnFilter = notFileFilter(and(directoryFileFilter(), nameFileFilter(".svn")));

    public static IOFileFilter ageFileFilter(Date cutoffDate) {
        return new AgeFileFilter(cutoffDate);
    }

    public static IOFileFilter ageFileFilter(Date cutoffDate, boolean acceptOlder) {
        return new AgeFileFilter(cutoffDate, acceptOlder);
    }

    public static IOFileFilter ageFileFilter(File cutoffReference) {
        return new AgeFileFilter(cutoffReference);
    }

    public static IOFileFilter ageFileFilter(File cutoffReference, boolean acceptOlder) {
        return new AgeFileFilter(cutoffReference, acceptOlder);
    }

    public static IOFileFilter ageFileFilter(long cutoff) {
        return new AgeFileFilter(cutoff);
    }

    public static IOFileFilter ageFileFilter(long cutoff, boolean acceptOlder) {
        return new AgeFileFilter(cutoff, acceptOlder);
    }

    public static IOFileFilter and(IOFileFilter... filters) {
        return new AndFileFilter(toList(filters));
    }

    @Deprecated
    public static IOFileFilter andFileFilter(IOFileFilter filter1, IOFileFilter filter2) {
        return new AndFileFilter(filter1, filter2);
    }

    public static IOFileFilter asFileFilter(FileFilter filter) {
        return new DelegateFileFilter(filter);
    }

    public static IOFileFilter asFileFilter(FilenameFilter filter) {
        return new DelegateFileFilter(filter);
    }

    public static IOFileFilter directoryFileFilter() {
        return DirectoryFileFilter.DIRECTORY;
    }

    public static IOFileFilter falseFileFilter() {
        return FalseFileFilter.FALSE;
    }

    public static IOFileFilter fileFileFilter() {
        return FileFileFilter.INSTANCE;
    }

    public static File[] filter(IOFileFilter filter, File... files) {
        if (filter == null) {
            throw new IllegalArgumentException("file filter is null");
        }
        if (files == null) {
            return FileUtils.EMPTY_FILE_ARRAY;
        }
        return (File[]) ((List) filterFiles(filter, Stream.of((Object[]) files), Collectors.toList())).toArray(FileUtils.EMPTY_FILE_ARRAY);
    }

    private static <R, A> R filterFiles(IOFileFilter filter, Stream<File> stream, Collector<? super File, A, R> collector) {
        Objects.requireNonNull(collector, "collector");
        if (filter == null) {
            throw new IllegalArgumentException("file filter is null");
        }
        if (stream == null) {
            return (R) Stream.empty().collect(collector);
        }
        filter.getClass();
        return (R) stream.filter(this::accept).collect(collector);
    }

    public static File[] filter(IOFileFilter filter, Iterable<File> files) {
        return (File[]) filterList(filter, files).toArray(FileUtils.EMPTY_FILE_ARRAY);
    }

    public static List<File> filterList(IOFileFilter filter, File... files) {
        return Arrays.asList(filter(filter, files));
    }

    public static List<File> filterList(IOFileFilter filter, Iterable<File> files) {
        if (files == null) {
            return Collections.emptyList();
        }
        return (List) filterFiles(filter, StreamSupport.stream(files.spliterator(), false), Collectors.toList());
    }

    public static Set<File> filterSet(IOFileFilter filter, File... files) {
        return new HashSet(Arrays.asList(filter(filter, files)));
    }

    public static Set<File> filterSet(IOFileFilter filter, Iterable<File> files) {
        if (files == null) {
            return Collections.emptySet();
        }
        return (Set) filterFiles(filter, StreamSupport.stream(files.spliterator(), false), Collectors.toSet());
    }

    public static IOFileFilter magicNumberFileFilter(byte[] magicNumber) {
        return new MagicNumberFileFilter(magicNumber);
    }

    public static IOFileFilter magicNumberFileFilter(byte[] magicNumber, long offset) {
        return new MagicNumberFileFilter(magicNumber, offset);
    }

    public static IOFileFilter magicNumberFileFilter(String magicNumber) {
        return new MagicNumberFileFilter(magicNumber);
    }

    public static IOFileFilter magicNumberFileFilter(String magicNumber, long offset) {
        return new MagicNumberFileFilter(magicNumber, offset);
    }

    public static IOFileFilter makeCVSAware(IOFileFilter filter) {
        return filter == null ? cvsFilter : and(filter, cvsFilter);
    }

    public static IOFileFilter makeDirectoryOnly(IOFileFilter filter) {
        if (filter == null) {
            return DirectoryFileFilter.DIRECTORY;
        }
        return DirectoryFileFilter.DIRECTORY.and(filter);
    }

    public static IOFileFilter makeFileOnly(IOFileFilter filter) {
        if (filter == null) {
            return FileFileFilter.INSTANCE;
        }
        return FileFileFilter.INSTANCE.and(filter);
    }

    public static IOFileFilter makeSVNAware(IOFileFilter filter) {
        return filter == null ? svnFilter : and(filter, svnFilter);
    }

    public static IOFileFilter nameFileFilter(String name) {
        return new NameFileFilter(name);
    }

    public static IOFileFilter nameFileFilter(String name, IOCase caseSensitivity) {
        return new NameFileFilter(name, caseSensitivity);
    }

    public static IOFileFilter notFileFilter(IOFileFilter filter) {
        return filter.negate();
    }

    public static IOFileFilter or(IOFileFilter... filters) {
        return new OrFileFilter(toList(filters));
    }

    @Deprecated
    public static IOFileFilter orFileFilter(IOFileFilter filter1, IOFileFilter filter2) {
        return new OrFileFilter(filter1, filter2);
    }

    public static IOFileFilter prefixFileFilter(String prefix) {
        return new PrefixFileFilter(prefix);
    }

    public static IOFileFilter prefixFileFilter(String prefix, IOCase caseSensitivity) {
        return new PrefixFileFilter(prefix, caseSensitivity);
    }

    public static IOFileFilter sizeFileFilter(long threshold) {
        return new SizeFileFilter(threshold);
    }

    public static IOFileFilter sizeFileFilter(long threshold, boolean acceptLarger) {
        return new SizeFileFilter(threshold, acceptLarger);
    }

    public static IOFileFilter sizeRangeFileFilter(long minSizeInclusive, long maxSizeInclusive) {
        IOFileFilter minimumFilter = new SizeFileFilter(minSizeInclusive, true);
        IOFileFilter maximumFilter = new SizeFileFilter(maxSizeInclusive + 1, false);
        return minimumFilter.and(maximumFilter);
    }

    public static IOFileFilter suffixFileFilter(String suffix) {
        return new SuffixFileFilter(suffix);
    }

    public static IOFileFilter suffixFileFilter(String suffix, IOCase caseSensitivity) {
        return new SuffixFileFilter(suffix, caseSensitivity);
    }

    public static List<IOFileFilter> toList(IOFileFilter... filters) {
        if (filters == null) {
            throw new IllegalArgumentException("The filters must not be null");
        }
        List<IOFileFilter> list = new ArrayList<>(filters.length);
        for (int i = 0; i < filters.length; i++) {
            if (filters[i] == null) {
                throw new IllegalArgumentException("The filter[" + i + "] is null");
            }
            list.add(filters[i]);
        }
        return list;
    }

    public static IOFileFilter trueFileFilter() {
        return TrueFileFilter.TRUE;
    }
}
