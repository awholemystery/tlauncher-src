package org.apache.commons.compress.harmony.pack200;

import ch.qos.logback.core.CoreConstants;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.compress.harmony.pack200.Archive;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/pack200/Segment.class */
public class Segment extends ClassVisitor {
    public static int ASM_API = 262144;
    private SegmentHeader segmentHeader;
    private CpBands cpBands;
    private AttributeDefinitionBands attributeDefinitionBands;
    private IcBands icBands;
    private ClassBands classBands;
    private BcBands bcBands;
    private FileBands fileBands;
    private final SegmentFieldVisitor fieldVisitor;
    private final SegmentMethodVisitor methodVisitor;
    private Pack200ClassReader currentClassReader;
    private PackingOptions options;
    private boolean stripDebug;
    private Attribute[] nonStandardAttributePrototypes;

    /* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/pack200/Segment$PassException.class */
    public static class PassException extends RuntimeException {
        private static final long serialVersionUID = 1;
    }

    public Segment() {
        super(ASM_API);
        this.fieldVisitor = new SegmentFieldVisitor();
        this.methodVisitor = new SegmentMethodVisitor();
    }

    public void pack(Archive.SegmentUnit segmentUnit, OutputStream out, PackingOptions options) throws IOException, Pack200Exception {
        this.options = options;
        this.stripDebug = options.isStripDebug();
        int effort = options.getEffort();
        this.nonStandardAttributePrototypes = options.getUnknownAttributePrototypes();
        PackingUtils.log("Start to pack a new segment with " + segmentUnit.fileListSize() + " files including " + segmentUnit.classListSize() + " classes");
        PackingUtils.log("Initialize a header for the segment");
        this.segmentHeader = new SegmentHeader();
        this.segmentHeader.setFile_count(segmentUnit.fileListSize());
        this.segmentHeader.setHave_all_code_flags(!this.stripDebug);
        if (!options.isKeepDeflateHint()) {
            this.segmentHeader.setDeflate_hint("true".equals(options.getDeflateHint()));
        }
        PackingUtils.log("Setup constant pool bands for the segment");
        this.cpBands = new CpBands(this, effort);
        PackingUtils.log("Setup attribute definition bands for the segment");
        this.attributeDefinitionBands = new AttributeDefinitionBands(this, effort, this.nonStandardAttributePrototypes);
        PackingUtils.log("Setup internal class bands for the segment");
        this.icBands = new IcBands(this.segmentHeader, this.cpBands, effort);
        PackingUtils.log("Setup class bands for the segment");
        this.classBands = new ClassBands(this, segmentUnit.classListSize(), effort, this.stripDebug);
        PackingUtils.log("Setup byte code bands for the segment");
        this.bcBands = new BcBands(this.cpBands, this, effort);
        PackingUtils.log("Setup file bands for the segment");
        this.fileBands = new FileBands(this.cpBands, this.segmentHeader, options, segmentUnit, effort);
        processClasses(segmentUnit, this.nonStandardAttributePrototypes);
        this.cpBands.finaliseBands();
        this.attributeDefinitionBands.finaliseBands();
        this.icBands.finaliseBands();
        this.classBands.finaliseBands();
        this.bcBands.finaliseBands();
        this.fileBands.finaliseBands();
        ByteArrayOutputStream bandsOutputStream = new ByteArrayOutputStream();
        PackingUtils.log("Packing...");
        int finalNumberOfClasses = this.classBands.numClassesProcessed();
        this.segmentHeader.setClass_count(finalNumberOfClasses);
        this.cpBands.pack(bandsOutputStream);
        if (finalNumberOfClasses > 0) {
            this.attributeDefinitionBands.pack(bandsOutputStream);
            this.icBands.pack(bandsOutputStream);
            this.classBands.pack(bandsOutputStream);
            this.bcBands.pack(bandsOutputStream);
        }
        this.fileBands.pack(bandsOutputStream);
        ByteArrayOutputStream headerOutputStream = new ByteArrayOutputStream();
        this.segmentHeader.pack(headerOutputStream);
        headerOutputStream.writeTo(out);
        bandsOutputStream.writeTo(out);
        segmentUnit.addPackedByteAmount(headerOutputStream.size());
        segmentUnit.addPackedByteAmount(bandsOutputStream.size());
        PackingUtils.log("Wrote total of " + segmentUnit.getPackedByteAmount() + " bytes");
        PackingUtils.log("Transmitted " + segmentUnit.fileListSize() + " files of " + segmentUnit.getByteAmount() + " input bytes in a segment of " + segmentUnit.getPackedByteAmount() + " bytes");
    }

    private void processClasses(Archive.SegmentUnit segmentUnit, Attribute[] attributes) throws Pack200Exception {
        this.segmentHeader.setClass_count(segmentUnit.classListSize());
        for (Pack200ClassReader classReader : segmentUnit.getClassList()) {
            this.currentClassReader = classReader;
            int flags = 0;
            if (this.stripDebug) {
                flags = 0 | 2;
            }
            try {
                classReader.accept(this, attributes, flags);
            } catch (PassException e) {
                this.classBands.removeCurrentClass();
                String name = classReader.getFileName();
                this.options.addPassFile(name);
                this.cpBands.addCPUtf8(name);
                boolean found = false;
                Iterator<Archive.PackingFile> it = segmentUnit.getFileList().iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    Archive.PackingFile file = it.next();
                    if (file.getName().equals(name)) {
                        found = true;
                        file.setContents(classReader.b);
                        break;
                    }
                }
                if (!found) {
                    throw new Pack200Exception("Error passing file " + name);
                }
            }
        }
    }

    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        this.bcBands.setCurrentClass(name, superName);
        this.segmentHeader.addMajorVersion(version);
        this.classBands.addClass(version, access, name, signature, superName, interfaces);
    }

    public void visitSource(String source, String debug) {
        if (!this.stripDebug) {
            this.classBands.addSourceFile(source);
        }
    }

    public void visitOuterClass(String owner, String name, String desc) {
        this.classBands.addEnclosingMethod(owner, name, desc);
    }

    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        return new SegmentAnnotationVisitor(0, desc, visible);
    }

    public void visitAttribute(Attribute attribute) {
        if (attribute.isUnknown()) {
            String action = this.options.getUnknownAttributeAction();
            if (action.equals("pass")) {
                passCurrentClass();
            } else if (action.equals("error")) {
                throw new Error("Unknown attribute encountered");
            }
        } else if (attribute instanceof NewAttribute) {
            NewAttribute newAttribute = (NewAttribute) attribute;
            if (newAttribute.isUnknown(0)) {
                String action2 = this.options.getUnknownClassAttributeAction(newAttribute.type);
                if (action2.equals("pass")) {
                    passCurrentClass();
                } else if (action2.equals("error")) {
                    throw new Error("Unknown attribute encountered");
                }
            }
            this.classBands.addClassAttribute(newAttribute);
        } else {
            throw new IllegalArgumentException("Unexpected attribute encountered: " + attribute.type);
        }
    }

    public void visitInnerClass(String name, String outerName, String innerName, int flags) {
        this.icBands.addInnerClass(name, outerName, innerName, flags);
    }

    public FieldVisitor visitField(int flags, String name, String desc, String signature, Object value) {
        this.classBands.addField(flags, name, desc, signature, value);
        return this.fieldVisitor;
    }

    public MethodVisitor visitMethod(int flags, String name, String desc, String signature, String[] exceptions) {
        this.classBands.addMethod(flags, name, desc, signature, exceptions);
        return this.methodVisitor;
    }

    public void visitEnd() {
        this.classBands.endOfClass();
    }

    /* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/pack200/Segment$SegmentMethodVisitor.class */
    public class SegmentMethodVisitor extends MethodVisitor {
        public SegmentMethodVisitor() {
            super(Segment.ASM_API);
        }

        public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
            return new SegmentAnnotationVisitor(2, desc, visible);
        }

        public AnnotationVisitor visitAnnotationDefault() {
            return new SegmentAnnotationVisitor(2);
        }

        public void visitAttribute(Attribute attribute) {
            if (attribute.isUnknown()) {
                String action = Segment.this.options.getUnknownAttributeAction();
                if (action.equals("pass")) {
                    Segment.this.passCurrentClass();
                } else if (action.equals("error")) {
                    throw new Error("Unknown attribute encountered");
                }
            } else if (attribute instanceof NewAttribute) {
                NewAttribute newAttribute = (NewAttribute) attribute;
                if (attribute.isCodeAttribute()) {
                    if (newAttribute.isUnknown(3)) {
                        String action2 = Segment.this.options.getUnknownCodeAttributeAction(newAttribute.type);
                        if (action2.equals("pass")) {
                            Segment.this.passCurrentClass();
                        } else if (action2.equals("error")) {
                            throw new Error("Unknown attribute encountered");
                        }
                    }
                    Segment.this.classBands.addCodeAttribute(newAttribute);
                    return;
                }
                if (newAttribute.isUnknown(2)) {
                    String action3 = Segment.this.options.getUnknownMethodAttributeAction(newAttribute.type);
                    if (action3.equals("pass")) {
                        Segment.this.passCurrentClass();
                    } else if (action3.equals("error")) {
                        throw new Error("Unknown attribute encountered");
                    }
                }
                Segment.this.classBands.addMethodAttribute(newAttribute);
            } else {
                throw new IllegalArgumentException("Unexpected attribute encountered: " + attribute.type);
            }
        }

        public void visitCode() {
            Segment.this.classBands.addCode();
        }

        public void visitFrame(int arg0, int arg1, Object[] arg2, int arg3, Object[] arg4) {
        }

        public void visitLabel(Label label) {
            Segment.this.bcBands.visitLabel(label);
        }

        public void visitLineNumber(int line, Label start) {
            if (!Segment.this.stripDebug) {
                Segment.this.classBands.addLineNumber(line, start);
            }
        }

        public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
            if (!Segment.this.stripDebug) {
                Segment.this.classBands.addLocalVariable(name, desc, signature, start, end, index);
            }
        }

        public void visitMaxs(int maxStack, int maxLocals) {
            Segment.this.classBands.addMaxStack(maxStack, maxLocals);
        }

        public AnnotationVisitor visitParameterAnnotation(int parameter, String desc, boolean visible) {
            return new SegmentAnnotationVisitor(2, parameter, desc, visible);
        }

        public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
            Segment.this.classBands.addHandler(start, end, handler, type);
        }

        public void visitEnd() {
            Segment.this.classBands.endOfMethod();
            Segment.this.bcBands.visitEnd();
        }

        public void visitFieldInsn(int opcode, String owner, String name, String desc) {
            Segment.this.bcBands.visitFieldInsn(opcode, owner, name, desc);
        }

        public void visitIincInsn(int var, int increment) {
            Segment.this.bcBands.visitIincInsn(var, increment);
        }

        public void visitInsn(int opcode) {
            Segment.this.bcBands.visitInsn(opcode);
        }

        public void visitIntInsn(int opcode, int operand) {
            Segment.this.bcBands.visitIntInsn(opcode, operand);
        }

        public void visitJumpInsn(int opcode, Label label) {
            Segment.this.bcBands.visitJumpInsn(opcode, label);
        }

        public void visitLdcInsn(Object cst) {
            Segment.this.bcBands.visitLdcInsn(cst);
        }

        public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
            Segment.this.bcBands.visitLookupSwitchInsn(dflt, keys, labels);
        }

        public void visitMethodInsn(int opcode, String owner, String name, String desc) {
            Segment.this.bcBands.visitMethodInsn(opcode, owner, name, desc);
        }

        public void visitMultiANewArrayInsn(String desc, int dimensions) {
            Segment.this.bcBands.visitMultiANewArrayInsn(desc, dimensions);
        }

        public void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels) {
            Segment.this.bcBands.visitTableSwitchInsn(min, max, dflt, labels);
        }

        public void visitTypeInsn(int opcode, String type) {
            Segment.this.bcBands.visitTypeInsn(opcode, type);
        }

        public void visitVarInsn(int opcode, int var) {
            Segment.this.bcBands.visitVarInsn(opcode, var);
        }
    }

    public ClassBands getClassBands() {
        return this.classBands;
    }

    /* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/pack200/Segment$SegmentAnnotationVisitor.class */
    public class SegmentAnnotationVisitor extends AnnotationVisitor {
        private int context;
        private int parameter;
        private String desc;
        private boolean visible;
        private final List<String> nameRU;
        private final List<String> tags;
        private final List<Object> values;
        private final List<Integer> caseArrayN;
        private final List<String> nestTypeRS;
        private final List<String> nestNameRU;
        private final List<Integer> nestPairN;

        public SegmentAnnotationVisitor(int context, String desc, boolean visible) {
            super(Segment.ASM_API);
            this.context = -1;
            this.parameter = -1;
            this.nameRU = new ArrayList();
            this.tags = new ArrayList();
            this.values = new ArrayList();
            this.caseArrayN = new ArrayList();
            this.nestTypeRS = new ArrayList();
            this.nestNameRU = new ArrayList();
            this.nestPairN = new ArrayList();
            this.context = context;
            this.desc = desc;
            this.visible = visible;
        }

        public SegmentAnnotationVisitor(int context) {
            super(Segment.ASM_API);
            this.context = -1;
            this.parameter = -1;
            this.nameRU = new ArrayList();
            this.tags = new ArrayList();
            this.values = new ArrayList();
            this.caseArrayN = new ArrayList();
            this.nestTypeRS = new ArrayList();
            this.nestNameRU = new ArrayList();
            this.nestPairN = new ArrayList();
            this.context = context;
        }

        public SegmentAnnotationVisitor(int context, int parameter, String desc, boolean visible) {
            super(Segment.ASM_API);
            this.context = -1;
            this.parameter = -1;
            this.nameRU = new ArrayList();
            this.tags = new ArrayList();
            this.values = new ArrayList();
            this.caseArrayN = new ArrayList();
            this.nestTypeRS = new ArrayList();
            this.nestNameRU = new ArrayList();
            this.nestPairN = new ArrayList();
            this.context = context;
            this.parameter = parameter;
            this.desc = desc;
            this.visible = visible;
        }

        public void visit(String name, Object value) {
            if (name == null) {
                name = CoreConstants.EMPTY_STRING;
            }
            this.nameRU.add(name);
            Segment.this.addValueAndTag(value, this.tags, this.values);
        }

        public AnnotationVisitor visitAnnotation(String name, String desc) {
            this.tags.add("@");
            if (name == null) {
                name = CoreConstants.EMPTY_STRING;
            }
            this.nameRU.add(name);
            this.nestTypeRS.add(desc);
            this.nestPairN.add(0);
            return new AnnotationVisitor(this.context, this.av) { // from class: org.apache.commons.compress.harmony.pack200.Segment.SegmentAnnotationVisitor.1
                public void visit(String name2, Object value) {
                    Integer numPairs = (Integer) SegmentAnnotationVisitor.this.nestPairN.remove(SegmentAnnotationVisitor.this.nestPairN.size() - 1);
                    SegmentAnnotationVisitor.this.nestPairN.add(Integer.valueOf(numPairs.intValue() + 1));
                    SegmentAnnotationVisitor.this.nestNameRU.add(name2);
                    Segment.this.addValueAndTag(value, SegmentAnnotationVisitor.this.tags, SegmentAnnotationVisitor.this.values);
                }

                public AnnotationVisitor visitAnnotation(String arg0, String arg1) {
                    throw new UnsupportedOperationException("Not yet supported");
                }

                public AnnotationVisitor visitArray(String arg0) {
                    throw new UnsupportedOperationException("Not yet supported");
                }

                public void visitEnd() {
                }

                public void visitEnum(String name2, String desc2, String value) {
                    Integer numPairs = (Integer) SegmentAnnotationVisitor.this.nestPairN.remove(SegmentAnnotationVisitor.this.nestPairN.size() - 1);
                    SegmentAnnotationVisitor.this.nestPairN.add(Integer.valueOf(numPairs.intValue() + 1));
                    SegmentAnnotationVisitor.this.tags.add("e");
                    SegmentAnnotationVisitor.this.nestNameRU.add(name2);
                    SegmentAnnotationVisitor.this.values.add(desc2);
                    SegmentAnnotationVisitor.this.values.add(value);
                }
            };
        }

        public AnnotationVisitor visitArray(String name) {
            this.tags.add("[");
            if (name == null) {
                name = CoreConstants.EMPTY_STRING;
            }
            this.nameRU.add(name);
            this.caseArrayN.add(0);
            return new ArrayVisitor(this.caseArrayN, this.tags, this.nameRU, this.values);
        }

        public void visitEnd() {
            if (this.desc == null) {
                Segment.this.classBands.addAnnotationDefault(this.nameRU, this.tags, this.values, this.caseArrayN, this.nestTypeRS, this.nestNameRU, this.nestPairN);
            } else if (this.parameter != -1) {
                Segment.this.classBands.addParameterAnnotation(this.parameter, this.desc, this.visible, this.nameRU, this.tags, this.values, this.caseArrayN, this.nestTypeRS, this.nestNameRU, this.nestPairN);
            } else {
                Segment.this.classBands.addAnnotation(this.context, this.desc, this.visible, this.nameRU, this.tags, this.values, this.caseArrayN, this.nestTypeRS, this.nestNameRU, this.nestPairN);
            }
        }

        public void visitEnum(String name, String desc, String value) {
            this.tags.add("e");
            if (name == null) {
                name = CoreConstants.EMPTY_STRING;
            }
            this.nameRU.add(name);
            this.values.add(desc);
            this.values.add(value);
        }
    }

    /* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/pack200/Segment$ArrayVisitor.class */
    public class ArrayVisitor extends AnnotationVisitor {
        private final int indexInCaseArrayN;
        private final List<Integer> caseArrayN;
        private final List<Object> values;
        private final List<String> nameRU;
        private final List<String> tags;

        public ArrayVisitor(List<Integer> caseArrayN, List<String> tags, List<String> nameRU, List<Object> values) {
            super(Segment.ASM_API);
            this.caseArrayN = caseArrayN;
            this.tags = tags;
            this.nameRU = nameRU;
            this.values = values;
            this.indexInCaseArrayN = caseArrayN.size() - 1;
        }

        public void visit(String name, Object value) {
            Integer numCases = this.caseArrayN.remove(this.indexInCaseArrayN);
            this.caseArrayN.add(this.indexInCaseArrayN, Integer.valueOf(numCases.intValue() + 1));
            if (name == null) {
            }
            Segment.this.addValueAndTag(value, this.tags, this.values);
        }

        public AnnotationVisitor visitAnnotation(String arg0, String arg1) {
            throw new UnsupportedOperationException("Not yet supported");
        }

        public AnnotationVisitor visitArray(String name) {
            this.tags.add("[");
            if (name == null) {
                name = CoreConstants.EMPTY_STRING;
            }
            this.nameRU.add(name);
            this.caseArrayN.add(0);
            return new ArrayVisitor(this.caseArrayN, this.tags, this.nameRU, this.values);
        }

        public void visitEnd() {
        }

        public void visitEnum(String name, String desc, String value) {
            Integer numCases = this.caseArrayN.remove(this.caseArrayN.size() - 1);
            this.caseArrayN.add(Integer.valueOf(numCases.intValue() + 1));
            this.tags.add("e");
            this.values.add(desc);
            this.values.add(value);
        }
    }

    /* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/pack200/Segment$SegmentFieldVisitor.class */
    public class SegmentFieldVisitor extends FieldVisitor {
        public SegmentFieldVisitor() {
            super(Segment.ASM_API);
        }

        public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
            return new SegmentAnnotationVisitor(1, desc, visible);
        }

        public void visitAttribute(Attribute attribute) {
            if (attribute.isUnknown()) {
                String action = Segment.this.options.getUnknownAttributeAction();
                if (action.equals("pass")) {
                    Segment.this.passCurrentClass();
                } else if (action.equals("error")) {
                    throw new Error("Unknown attribute encountered");
                }
            } else if (attribute instanceof NewAttribute) {
                NewAttribute newAttribute = (NewAttribute) attribute;
                if (newAttribute.isUnknown(1)) {
                    String action2 = Segment.this.options.getUnknownFieldAttributeAction(newAttribute.type);
                    if (action2.equals("pass")) {
                        Segment.this.passCurrentClass();
                    } else if (action2.equals("error")) {
                        throw new Error("Unknown attribute encountered");
                    }
                }
                Segment.this.classBands.addFieldAttribute(newAttribute);
            } else {
                throw new IllegalArgumentException("Unexpected attribute encountered: " + attribute.type);
            }
        }

        public void visitEnd() {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addValueAndTag(Object value, List<String> tags, List<Object> values) {
        if (value instanceof Integer) {
            tags.add("I");
            values.add(value);
        } else if (value instanceof Double) {
            tags.add("D");
            values.add(value);
        } else if (value instanceof Float) {
            tags.add("F");
            values.add(value);
        } else if (value instanceof Long) {
            tags.add("J");
            values.add(value);
        } else if (value instanceof Byte) {
            tags.add("B");
            values.add(Integer.valueOf(((Byte) value).intValue()));
        } else if (value instanceof Character) {
            tags.add("C");
            values.add(Integer.valueOf(((Character) value).charValue()));
        } else if (value instanceof Short) {
            tags.add("S");
            values.add(Integer.valueOf(((Short) value).intValue()));
        } else if (value instanceof Boolean) {
            tags.add("Z");
            values.add(Integer.valueOf(((Boolean) value).booleanValue() ? 1 : 0));
        } else if (value instanceof String) {
            tags.add("s");
            values.add(value);
        } else if (value instanceof Type) {
            tags.add("c");
            values.add(((Type) value).toString());
        }
    }

    public boolean lastConstantHadWideIndex() {
        return this.currentClassReader.lastConstantHadWideIndex();
    }

    public CpBands getCpBands() {
        return this.cpBands;
    }

    public SegmentHeader getSegmentHeader() {
        return this.segmentHeader;
    }

    public AttributeDefinitionBands getAttrBands() {
        return this.attributeDefinitionBands;
    }

    public IcBands getIcBands() {
        return this.icBands;
    }

    public Pack200ClassReader getCurrentClassReader() {
        return this.currentClassReader;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void passCurrentClass() {
        throw new PassException();
    }
}
