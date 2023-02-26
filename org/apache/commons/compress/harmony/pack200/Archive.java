package org.apache.commons.compress.harmony.pack200;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.zip.GZIPOutputStream;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/pack200/Archive.class */
public class Archive {
    private final JarInputStream jarInputStream;
    private final OutputStream outputStream;
    private JarFile jarFile;
    private long currentSegmentSize;
    private final PackingOptions options;

    public Archive(JarInputStream inputStream, OutputStream outputStream, PackingOptions options) throws IOException {
        this.jarInputStream = inputStream;
        options = options == null ? new PackingOptions() : options;
        this.options = options;
        this.outputStream = new BufferedOutputStream(options.isGzip() ? new GZIPOutputStream(outputStream) : outputStream);
        PackingUtils.config(options);
    }

    public Archive(JarFile jarFile, OutputStream outputStream, PackingOptions options) throws IOException {
        options = options == null ? new PackingOptions() : options;
        this.options = options;
        this.outputStream = new BufferedOutputStream(options.isGzip() ? new GZIPOutputStream(outputStream) : outputStream);
        this.jarFile = jarFile;
        this.jarInputStream = null;
        PackingUtils.config(options);
    }

    public void pack() throws Pack200Exception, IOException {
        if (0 == this.options.getEffort()) {
            doZeroEffortPack();
        } else {
            doNormalPack();
        }
    }

    private void doZeroEffortPack() throws IOException {
        PackingUtils.log("Start to perform a zero-effort packing");
        if (this.jarInputStream != null) {
            PackingUtils.copyThroughJar(this.jarInputStream, this.outputStream);
        } else {
            PackingUtils.copyThroughJar(this.jarFile, this.outputStream);
        }
    }

    private void doNormalPack() throws IOException, Pack200Exception {
        List<PackingFile> packingFileList;
        PackingUtils.log("Start to perform a normal packing");
        if (this.jarInputStream != null) {
            packingFileList = PackingUtils.getPackingFileListFromJar(this.jarInputStream, this.options.isKeepFileOrder());
        } else {
            packingFileList = PackingUtils.getPackingFileListFromJar(this.jarFile, this.options.isKeepFileOrder());
        }
        List<SegmentUnit> segmentUnitList = splitIntoSegments(packingFileList);
        int previousByteAmount = 0;
        int packedByteAmount = 0;
        int segmentSize = segmentUnitList.size();
        for (int index = 0; index < segmentSize; index++) {
            SegmentUnit segmentUnit = segmentUnitList.get(index);
            new Segment().pack(segmentUnit, this.outputStream, this.options);
            previousByteAmount += segmentUnit.getByteAmount();
            packedByteAmount += segmentUnit.getPackedByteAmount();
        }
        PackingUtils.log("Total: Packed " + previousByteAmount + " input bytes of " + packingFileList.size() + " files into " + packedByteAmount + " bytes in " + segmentSize + " segments");
        this.outputStream.close();
    }

    private List<SegmentUnit> splitIntoSegments(List<PackingFile> packingFileList) {
        List<SegmentUnit> segmentUnitList = new ArrayList<>();
        List<Pack200ClassReader> classes = new ArrayList<>();
        List<PackingFile> files = new ArrayList<>();
        long segmentLimit = this.options.getSegmentLimit();
        int size = packingFileList.size();
        for (int index = 0; index < size; index++) {
            PackingFile packingFile = packingFileList.get(index);
            if (!addJarEntry(packingFile, classes, files)) {
                segmentUnitList.add(new SegmentUnit(classes, files));
                classes = new ArrayList<>();
                files = new ArrayList<>();
                this.currentSegmentSize = 0L;
                addJarEntry(packingFile, classes, files);
                this.currentSegmentSize = 0L;
            } else if (segmentLimit == 0 && estimateSize(packingFile) > 0) {
                segmentUnitList.add(new SegmentUnit(classes, files));
                classes = new ArrayList<>();
                files = new ArrayList<>();
            }
        }
        if (classes.size() > 0 || files.size() > 0) {
            segmentUnitList.add(new SegmentUnit(classes, files));
        }
        return segmentUnitList;
    }

    private boolean addJarEntry(PackingFile packingFile, List<Pack200ClassReader> javaClasses, List<PackingFile> files) {
        long segmentLimit = this.options.getSegmentLimit();
        if (segmentLimit != -1 && segmentLimit != 0) {
            long packedSize = estimateSize(packingFile);
            if (packedSize + this.currentSegmentSize > segmentLimit && this.currentSegmentSize > 0) {
                return false;
            }
            this.currentSegmentSize += packedSize;
        }
        String name = packingFile.getName();
        if (name.endsWith(".class") && !this.options.isPassFile(name)) {
            Pack200ClassReader classParser = new Pack200ClassReader(packingFile.contents);
            classParser.setFileName(name);
            javaClasses.add(classParser);
            packingFile.contents = new byte[0];
        }
        files.add(packingFile);
        return true;
    }

    private long estimateSize(PackingFile packingFile) {
        String name = packingFile.getName();
        if (name.startsWith("META-INF") || name.startsWith("/META-INF")) {
            return 0L;
        }
        long fileSize = packingFile.contents.length;
        if (fileSize < 0) {
            fileSize = 0;
        }
        return name.length() + fileSize + 5;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/pack200/Archive$SegmentUnit.class */
    public static class SegmentUnit {
        private final List<Pack200ClassReader> classList;
        private final List<PackingFile> fileList;
        private int byteAmount = 0;
        private int packedByteAmount;

        public SegmentUnit(List<Pack200ClassReader> classes, List<PackingFile> files) {
            this.classList = classes;
            this.fileList = files;
            this.byteAmount += this.classList.stream().mapToInt(element -> {
                return element.b.length;
            }).sum();
            this.byteAmount += this.fileList.stream().mapToInt(element2 -> {
                return element2.contents.length;
            }).sum();
        }

        public List<Pack200ClassReader> getClassList() {
            return this.classList;
        }

        public int classListSize() {
            return this.classList.size();
        }

        public int fileListSize() {
            return this.fileList.size();
        }

        public List<PackingFile> getFileList() {
            return this.fileList;
        }

        public int getByteAmount() {
            return this.byteAmount;
        }

        public int getPackedByteAmount() {
            return this.packedByteAmount;
        }

        public void addPackedByteAmount(int amount) {
            this.packedByteAmount += amount;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/pack200/Archive$PackingFile.class */
    public static class PackingFile {
        private final String name;
        private byte[] contents;
        private final long modtime;
        private final boolean deflateHint;
        private final boolean isDirectory;

        public PackingFile(String name, byte[] contents, long modtime) {
            this.name = name;
            this.contents = contents;
            this.modtime = modtime;
            this.deflateHint = false;
            this.isDirectory = false;
        }

        public PackingFile(byte[] bytes, JarEntry jarEntry) {
            this.name = jarEntry.getName();
            this.contents = bytes;
            this.modtime = jarEntry.getTime();
            this.deflateHint = jarEntry.getMethod() == 8;
            this.isDirectory = jarEntry.isDirectory();
        }

        public byte[] getContents() {
            return this.contents;
        }

        public String getName() {
            return this.name;
        }

        public long getModtime() {
            return this.modtime;
        }

        public void setContents(byte[] contents) {
            this.contents = contents;
        }

        public boolean isDefalteHint() {
            return this.deflateHint;
        }

        public boolean isDirectory() {
            return this.isDirectory;
        }

        public String toString() {
            return this.name;
        }
    }
}
