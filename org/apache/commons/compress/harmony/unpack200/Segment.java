package org.apache.commons.compress.harmony.unpack200;

import ch.qos.logback.core.CoreConstants;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.zip.CRC32;
import org.apache.commons.compress.harmony.pack200.Pack200Exception;
import org.apache.commons.compress.harmony.unpack200.bytecode.Attribute;
import org.apache.commons.compress.harmony.unpack200.bytecode.CPClass;
import org.apache.commons.compress.harmony.unpack200.bytecode.CPField;
import org.apache.commons.compress.harmony.unpack200.bytecode.CPMethod;
import org.apache.commons.compress.harmony.unpack200.bytecode.CPUTF8;
import org.apache.commons.compress.harmony.unpack200.bytecode.ClassConstantPool;
import org.apache.commons.compress.harmony.unpack200.bytecode.ClassFile;
import org.apache.commons.compress.harmony.unpack200.bytecode.ClassFileEntry;
import org.apache.commons.compress.harmony.unpack200.bytecode.InnerClassesAttribute;
import org.apache.commons.compress.harmony.unpack200.bytecode.SourceFileAttribute;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/unpack200/Segment.class */
public class Segment {
    public static final int LOG_LEVEL_VERBOSE = 2;
    public static final int LOG_LEVEL_STANDARD = 1;
    public static final int LOG_LEVEL_QUIET = 0;
    private SegmentHeader header;
    private CpBands cpBands;
    private AttrDefinitionBands attrDefinitionBands;
    private IcBands icBands;
    private ClassBands classBands;
    private BcBands bcBands;
    private FileBands fileBands;
    private boolean overrideDeflateHint;
    private boolean deflateHint;
    private boolean doPreRead;
    private int logLevel;
    private PrintWriter logStream;
    private byte[][] classFilesContents;
    private boolean[] fileDeflate;
    private boolean[] fileIsClass;
    private InputStream internalBuffer;

    private ClassFile buildClassFile(int classNum) {
        String fileName;
        ClassFile classFile = new ClassFile();
        int[] major = this.classBands.getClassVersionMajor();
        int[] minor = this.classBands.getClassVersionMinor();
        if (major != null) {
            classFile.major = major[classNum];
            classFile.minor = minor[classNum];
        } else {
            classFile.major = this.header.getDefaultClassMajorVersion();
            classFile.minor = this.header.getDefaultClassMinorVersion();
        }
        ClassConstantPool cp = classFile.pool;
        int fullNameIndexInCpClass = this.classBands.getClassThisInts()[classNum];
        String fullName = this.cpBands.getCpClass()[fullNameIndexInCpClass];
        int i = fullName.lastIndexOf("/") + 1;
        List<Attribute> classAttributes = this.classBands.getClassAttributes()[classNum];
        SourceFileAttribute sourceFileAttribute = null;
        for (Attribute classAttribute : classAttributes) {
            if (classAttribute.isSourceFileAttribute()) {
                sourceFileAttribute = (SourceFileAttribute) classAttribute;
            }
        }
        if (sourceFileAttribute == null) {
            AttributeLayout SOURCE_FILE = this.attrDefinitionBands.getAttributeDefinitionMap().getAttributeLayout(AttributeLayout.ATTRIBUTE_SOURCE_FILE, 0);
            if (SOURCE_FILE.matches(this.classBands.getRawClassFlags()[classNum])) {
                int firstDollar = -1;
                for (int index = 0; index < fullName.length(); index++) {
                    if (fullName.charAt(index) <= '$') {
                        firstDollar = index;
                    }
                }
                if (firstDollar > -1 && i <= firstDollar) {
                    fileName = fullName.substring(i, firstDollar) + ".java";
                } else {
                    fileName = fullName.substring(i) + ".java";
                }
                SourceFileAttribute sourceFileAttribute2 = new SourceFileAttribute(this.cpBands.cpUTF8Value(fileName, false));
                classFile.attributes = new Attribute[]{(Attribute) cp.add(sourceFileAttribute2)};
            } else {
                classFile.attributes = new Attribute[0];
            }
        } else {
            classFile.attributes = new Attribute[]{(Attribute) cp.add(sourceFileAttribute)};
        }
        List<Attribute> classAttributesWithoutSourceFileAttribute = new ArrayList<>(classAttributes.size());
        for (int index2 = 0; index2 < classAttributes.size(); index2++) {
            Attribute attrib = classAttributes.get(index2);
            if (!attrib.isSourceFileAttribute()) {
                classAttributesWithoutSourceFileAttribute.add(attrib);
            }
        }
        Attribute[] originalAttributes = classFile.attributes;
        classFile.attributes = new Attribute[originalAttributes.length + classAttributesWithoutSourceFileAttribute.size()];
        System.arraycopy(originalAttributes, 0, classFile.attributes, 0, originalAttributes.length);
        for (int index3 = 0; index3 < classAttributesWithoutSourceFileAttribute.size(); index3++) {
            Attribute attrib2 = classAttributesWithoutSourceFileAttribute.get(index3);
            cp.add(attrib2);
            classFile.attributes[originalAttributes.length + index3] = attrib2;
        }
        ClassFileEntry cfThis = cp.add(this.cpBands.cpClassValue(fullNameIndexInCpClass));
        ClassFileEntry cfSuper = cp.add(this.cpBands.cpClassValue(this.classBands.getClassSuperInts()[classNum]));
        ClassFileEntry[] cfInterfaces = new ClassFileEntry[this.classBands.getClassInterfacesInts()[classNum].length];
        for (int i2 = 0; i2 < cfInterfaces.length; i2++) {
            cfInterfaces[i2] = cp.add(this.cpBands.cpClassValue(this.classBands.getClassInterfacesInts()[classNum][i2]));
        }
        ClassFileEntry[] cfFields = new ClassFileEntry[this.classBands.getClassFieldCount()[classNum]];
        for (int i3 = 0; i3 < cfFields.length; i3++) {
            int descriptorIndex = this.classBands.getFieldDescrInts()[classNum][i3];
            int nameIndex = this.cpBands.getCpDescriptorNameInts()[descriptorIndex];
            int typeIndex = this.cpBands.getCpDescriptorTypeInts()[descriptorIndex];
            CPUTF8 name = this.cpBands.cpUTF8Value(nameIndex);
            CPUTF8 descriptor = this.cpBands.cpSignatureValue(typeIndex);
            cfFields[i3] = cp.add(new CPField(name, descriptor, this.classBands.getFieldFlags()[classNum][i3], this.classBands.getFieldAttributes()[classNum][i3]));
        }
        ClassFileEntry[] cfMethods = new ClassFileEntry[this.classBands.getClassMethodCount()[classNum]];
        for (int i4 = 0; i4 < cfMethods.length; i4++) {
            int descriptorIndex2 = this.classBands.getMethodDescrInts()[classNum][i4];
            int nameIndex2 = this.cpBands.getCpDescriptorNameInts()[descriptorIndex2];
            int typeIndex2 = this.cpBands.getCpDescriptorTypeInts()[descriptorIndex2];
            CPUTF8 name2 = this.cpBands.cpUTF8Value(nameIndex2);
            CPUTF8 descriptor2 = this.cpBands.cpSignatureValue(typeIndex2);
            cfMethods[i4] = cp.add(new CPMethod(name2, descriptor2, this.classBands.getMethodFlags()[classNum][i4], this.classBands.getMethodAttributes()[classNum][i4]));
        }
        cp.addNestedEntries();
        boolean addInnerClassesAttr = false;
        IcTuple[] ic_local = getClassBands().getIcLocal()[classNum];
        boolean ic_local_sent = ic_local != null;
        InnerClassesAttribute innerClassesAttribute = new InnerClassesAttribute(AttributeLayout.ATTRIBUTE_INNER_CLASSES);
        IcTuple[] ic_relevant = getIcBands().getRelevantIcTuples(fullName, cp);
        List<IcTuple> ic_stored = computeIcStored(ic_local, ic_relevant);
        for (IcTuple icStored : ic_stored) {
            int innerClassIndex = icStored.thisClassIndex();
            int outerClassIndex = icStored.outerClassIndex();
            int simpleClassNameIndex = icStored.simpleClassNameIndex();
            String innerClassString = icStored.thisClassString();
            String outerClassString = icStored.outerClassString();
            String simpleClassName = icStored.simpleClassName();
            CPUTF8 innerName = null;
            CPClass outerClass = null;
            CPClass innerClass = innerClassIndex != -1 ? this.cpBands.cpClassValue(innerClassIndex) : this.cpBands.cpClassValue(innerClassString);
            if (!icStored.isAnonymous()) {
                innerName = simpleClassNameIndex != -1 ? this.cpBands.cpUTF8Value(simpleClassNameIndex) : this.cpBands.cpUTF8Value(simpleClassName);
            }
            if (icStored.isMember()) {
                outerClass = outerClassIndex != -1 ? this.cpBands.cpClassValue(outerClassIndex) : this.cpBands.cpClassValue(outerClassString);
            }
            int flags = icStored.F;
            innerClassesAttribute.addInnerClassesEntry(innerClass, outerClass, innerName, flags);
            addInnerClassesAttr = true;
        }
        if (ic_local_sent && ic_local.length == 0) {
            addInnerClassesAttr = false;
        }
        if (!ic_local_sent && ic_relevant.length == 0) {
            addInnerClassesAttr = false;
        }
        if (addInnerClassesAttr) {
            Attribute[] originalAttrs = classFile.attributes;
            Attribute[] newAttrs = new Attribute[originalAttrs.length + 1];
            System.arraycopy(originalAttrs, 0, newAttrs, 0, originalAttrs.length);
            newAttrs[newAttrs.length - 1] = innerClassesAttribute;
            classFile.attributes = newAttrs;
            cp.addWithNestedEntries(innerClassesAttribute);
        }
        cp.resolve(this);
        classFile.accessFlags = (int) this.classBands.getClassFlags()[classNum];
        classFile.thisClass = cp.indexOf(cfThis);
        classFile.superClass = cp.indexOf(cfSuper);
        classFile.interfaces = new int[cfInterfaces.length];
        for (int i5 = 0; i5 < cfInterfaces.length; i5++) {
            classFile.interfaces[i5] = cp.indexOf(cfInterfaces[i5]);
        }
        classFile.fields = cfFields;
        classFile.methods = cfMethods;
        return classFile;
    }

    private List<IcTuple> computeIcStored(IcTuple[] ic_local, IcTuple[] ic_relevant) {
        List<IcTuple> result = new ArrayList<>(ic_relevant.length);
        List<IcTuple> duplicates = new ArrayList<>(ic_relevant.length);
        Set<IcTuple> isInResult = new HashSet<>(ic_relevant.length);
        if (ic_local != null) {
            for (IcTuple element : ic_local) {
                if (isInResult.add(element)) {
                    result.add(element);
                }
            }
        }
        for (IcTuple element2 : ic_relevant) {
            if (isInResult.add(element2)) {
                result.add(element2);
            } else {
                duplicates.add(element2);
            }
        }
        result.getClass();
        duplicates.forEach((v1) -> {
            r1.remove(v1);
        });
        return result;
    }

    private void readSegment(InputStream in) throws IOException, Pack200Exception {
        log(2, "-------");
        this.cpBands = new CpBands(this);
        this.cpBands.read(in);
        this.attrDefinitionBands = new AttrDefinitionBands(this);
        this.attrDefinitionBands.read(in);
        this.icBands = new IcBands(this);
        this.icBands.read(in);
        this.classBands = new ClassBands(this);
        this.classBands.read(in);
        this.bcBands = new BcBands(this);
        this.bcBands.read(in);
        this.fileBands = new FileBands(this);
        this.fileBands.read(in);
        this.fileBands.processFileBits();
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [byte[], byte[][]] */
    private void parseSegment() throws IOException, Pack200Exception {
        this.header.unpack();
        this.cpBands.unpack();
        this.attrDefinitionBands.unpack();
        this.icBands.unpack();
        this.classBands.unpack();
        this.bcBands.unpack();
        this.fileBands.unpack();
        int classNum = 0;
        int numberOfFiles = this.header.getNumberOfFiles();
        String[] fileName = this.fileBands.getFileName();
        int[] fileOptions = this.fileBands.getFileOptions();
        SegmentOptions options = this.header.getOptions();
        this.classFilesContents = new byte[numberOfFiles];
        this.fileDeflate = new boolean[numberOfFiles];
        this.fileIsClass = new boolean[numberOfFiles];
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);
        for (int i = 0; i < numberOfFiles; i++) {
            String name = fileName[i];
            boolean nameIsEmpty = name == null || name.equals(CoreConstants.EMPTY_STRING);
            boolean isClass = (fileOptions[i] & 2) == 2 || nameIsEmpty;
            if (isClass && nameIsEmpty) {
                fileName[i] = this.cpBands.getCpClass()[this.classBands.getClassThisInts()[classNum]] + ".class";
            }
            if (!this.overrideDeflateHint) {
                this.fileDeflate[i] = (fileOptions[i] & 1) == 1 || options.shouldDeflate();
            } else {
                this.fileDeflate[i] = this.deflateHint;
            }
            this.fileIsClass[i] = isClass;
            if (isClass) {
                ClassFile classFile = buildClassFile(classNum);
                classFile.write(dos);
                dos.flush();
                this.classFilesContents[classNum] = bos.toByteArray();
                bos.reset();
                classNum++;
            }
        }
    }

    public void unpack(InputStream in, JarOutputStream out) throws IOException, Pack200Exception {
        unpackRead(in);
        unpackProcess();
        unpackWrite(out);
    }

    void unpackRead(InputStream in) throws IOException, Pack200Exception {
        if (!in.markSupported()) {
            in = new BufferedInputStream(in);
        }
        this.header = new SegmentHeader(this);
        this.header.read(in);
        int size = ((int) this.header.getArchiveSize()) - this.header.getArchiveSizeOffset();
        if (this.doPreRead && this.header.getArchiveSize() != 0) {
            byte[] data = new byte[size];
            in.read(data);
            this.internalBuffer = new BufferedInputStream(new ByteArrayInputStream(data));
            return;
        }
        readSegment(in);
    }

    void unpackProcess() throws IOException, Pack200Exception {
        if (this.internalBuffer != null) {
            readSegment(this.internalBuffer);
        }
        parseSegment();
    }

    void unpackWrite(JarOutputStream out) throws IOException {
        writeJar(out);
        if (this.logStream != null) {
            this.logStream.close();
        }
    }

    public void writeJar(JarOutputStream out) throws IOException {
        String[] fileName = this.fileBands.getFileName();
        int[] fileModtime = this.fileBands.getFileModtime();
        long[] fileSize = this.fileBands.getFileSize();
        byte[][] fileBits = this.fileBands.getFileBits();
        int classNum = 0;
        int numberOfFiles = this.header.getNumberOfFiles();
        long archiveModtime = this.header.getArchiveModtime();
        for (int i = 0; i < numberOfFiles; i++) {
            String name = fileName[i];
            long modtime = 1000 * (archiveModtime + fileModtime[i]);
            boolean deflate = this.fileDeflate[i];
            JarEntry entry = new JarEntry(name);
            if (deflate) {
                entry.setMethod(8);
            } else {
                entry.setMethod(0);
                CRC32 crc = new CRC32();
                if (this.fileIsClass[i]) {
                    crc.update(this.classFilesContents[classNum]);
                    entry.setSize(this.classFilesContents[classNum].length);
                } else {
                    crc.update(fileBits[i]);
                    entry.setSize(fileSize[i]);
                }
                entry.setCrc(crc.getValue());
            }
            entry.setTime(modtime - TimeZone.getDefault().getRawOffset());
            out.putNextEntry(entry);
            if (this.fileIsClass[i]) {
                entry.setSize(this.classFilesContents[classNum].length);
                out.write(this.classFilesContents[classNum]);
                classNum++;
            } else {
                entry.setSize(fileSize[i]);
                out.write(fileBits[i]);
            }
        }
    }

    public SegmentConstantPool getConstantPool() {
        return this.cpBands.getConstantPool();
    }

    public SegmentHeader getSegmentHeader() {
        return this.header;
    }

    public void setPreRead(boolean value) {
        this.doPreRead = value;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public AttrDefinitionBands getAttrDefinitionBands() {
        return this.attrDefinitionBands;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public ClassBands getClassBands() {
        return this.classBands;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public CpBands getCpBands() {
        return this.cpBands;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public IcBands getIcBands() {
        return this.icBands;
    }

    public void setLogLevel(int logLevel) {
        this.logLevel = logLevel;
    }

    public void setLogStream(OutputStream logStream) {
        this.logStream = new PrintWriter((Writer) new OutputStreamWriter(logStream, Charset.defaultCharset()), false);
    }

    public void log(int logLevel, String message) {
        if (this.logLevel >= logLevel) {
            this.logStream.println(message);
        }
    }

    public void overrideDeflateHint(boolean deflateHint) {
        this.overrideDeflateHint = true;
        this.deflateHint = deflateHint;
    }
}
