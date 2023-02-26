package org.apache.commons.compress.harmony.pack200;

import ch.qos.logback.core.CoreConstants;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.compress.harmony.pack200.NewAttribute;
import org.objectweb.asm.Attribute;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/pack200/AttributeDefinitionBands.class */
public class AttributeDefinitionBands extends BandSet {
    public static final int CONTEXT_CLASS = 0;
    public static final int CONTEXT_CODE = 3;
    public static final int CONTEXT_FIELD = 1;
    public static final int CONTEXT_METHOD = 2;
    private final List<AttributeDefinition> classAttributeLayouts;
    private final List<AttributeDefinition> methodAttributeLayouts;
    private final List<AttributeDefinition> fieldAttributeLayouts;
    private final List<AttributeDefinition> codeAttributeLayouts;
    private final List<AttributeDefinition> attributeDefinitions;
    private final CpBands cpBands;
    private final Segment segment;

    public AttributeDefinitionBands(Segment segment, int effort, Attribute[] attributePrototypes) {
        super(effort, segment.getSegmentHeader());
        this.classAttributeLayouts = new ArrayList();
        this.methodAttributeLayouts = new ArrayList();
        this.fieldAttributeLayouts = new ArrayList();
        this.codeAttributeLayouts = new ArrayList();
        this.attributeDefinitions = new ArrayList();
        this.cpBands = segment.getCpBands();
        this.segment = segment;
        Map<String, String> classLayouts = new HashMap<>();
        Map<String, String> methodLayouts = new HashMap<>();
        Map<String, String> fieldLayouts = new HashMap<>();
        Map<String, String> codeLayouts = new HashMap<>();
        for (Attribute attributePrototype : attributePrototypes) {
            NewAttribute newAttribute = (NewAttribute) attributePrototype;
            if (!(newAttribute instanceof NewAttribute.ErrorAttribute) && !(newAttribute instanceof NewAttribute.PassAttribute) && !(newAttribute instanceof NewAttribute.StripAttribute)) {
                if (newAttribute.isContextClass()) {
                    classLayouts.put(newAttribute.type, newAttribute.getLayout());
                }
                if (newAttribute.isContextMethod()) {
                    methodLayouts.put(newAttribute.type, newAttribute.getLayout());
                }
                if (newAttribute.isContextField()) {
                    fieldLayouts.put(newAttribute.type, newAttribute.getLayout());
                }
                if (newAttribute.isContextCode()) {
                    codeLayouts.put(newAttribute.type, newAttribute.getLayout());
                }
            }
        }
        if (classLayouts.size() > 7) {
            this.segmentHeader.setHave_class_flags_hi(true);
        }
        if (methodLayouts.size() > 6) {
            this.segmentHeader.setHave_method_flags_hi(true);
        }
        if (fieldLayouts.size() > 10) {
            this.segmentHeader.setHave_field_flags_hi(true);
        }
        if (codeLayouts.size() > 15) {
            this.segmentHeader.setHave_code_flags_hi(true);
        }
        int[] availableClassIndices = {25, 26, 27, 28, 29, 30, 31};
        addAttributeDefinitions(classLayouts, classLayouts.size() > 7 ? addHighIndices(availableClassIndices) : availableClassIndices, 0);
        int[] availableMethodIndices = {26, 27, 28, 29, 30, 31};
        addAttributeDefinitions(methodLayouts, this.methodAttributeLayouts.size() > 6 ? addHighIndices(availableMethodIndices) : availableMethodIndices, 2);
        int[] availableFieldIndices = {18, 23, 24, 25, 26, 27, 28, 29, 30, 31};
        addAttributeDefinitions(fieldLayouts, this.fieldAttributeLayouts.size() > 10 ? addHighIndices(availableFieldIndices) : availableFieldIndices, 1);
        int[] availableCodeIndices = {17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31};
        addAttributeDefinitions(codeLayouts, this.codeAttributeLayouts.size() > 15 ? addHighIndices(availableCodeIndices) : availableCodeIndices, 3);
    }

    public void finaliseBands() {
        addSyntheticDefinitions();
        this.segmentHeader.setAttribute_definition_count(this.attributeDefinitions.size());
    }

    @Override // org.apache.commons.compress.harmony.pack200.BandSet
    public void pack(OutputStream out) throws IOException, Pack200Exception {
        PackingUtils.log("Writing attribute definition bands...");
        int[] attributeDefinitionHeader = new int[this.attributeDefinitions.size()];
        int[] attributeDefinitionName = new int[this.attributeDefinitions.size()];
        int[] attributeDefinitionLayout = new int[this.attributeDefinitions.size()];
        for (int i = 0; i < attributeDefinitionLayout.length; i++) {
            AttributeDefinition def = this.attributeDefinitions.get(i);
            attributeDefinitionHeader[i] = def.contextType | ((def.index + 1) << 2);
            attributeDefinitionName[i] = def.name.getIndex();
            attributeDefinitionLayout[i] = def.layout.getIndex();
        }
        byte[] encodedBand = encodeBandInt("attributeDefinitionHeader", attributeDefinitionHeader, Codec.BYTE1);
        out.write(encodedBand);
        PackingUtils.log("Wrote " + encodedBand.length + " bytes from attributeDefinitionHeader[" + attributeDefinitionHeader.length + "]");
        byte[] encodedBand2 = encodeBandInt("attributeDefinitionName", attributeDefinitionName, Codec.UNSIGNED5);
        out.write(encodedBand2);
        PackingUtils.log("Wrote " + encodedBand2.length + " bytes from attributeDefinitionName[" + attributeDefinitionName.length + "]");
        byte[] encodedBand3 = encodeBandInt("attributeDefinitionLayout", attributeDefinitionLayout, Codec.UNSIGNED5);
        out.write(encodedBand3);
        PackingUtils.log("Wrote " + encodedBand3.length + " bytes from attributeDefinitionLayout[" + attributeDefinitionLayout.length + "]");
    }

    private void addSyntheticDefinitions() {
        boolean anySytheticClasses = this.segment.getClassBands().isAnySyntheticClasses();
        boolean anySyntheticMethods = this.segment.getClassBands().isAnySyntheticMethods();
        boolean anySyntheticFields = this.segment.getClassBands().isAnySyntheticFields();
        if (anySytheticClasses || anySyntheticMethods || anySyntheticFields) {
            CPUTF8 syntheticUTF = this.cpBands.getCPUtf8("Synthetic");
            CPUTF8 emptyUTF = this.cpBands.getCPUtf8(CoreConstants.EMPTY_STRING);
            if (anySytheticClasses) {
                this.attributeDefinitions.add(new AttributeDefinition(12, 0, syntheticUTF, emptyUTF));
            }
            if (anySyntheticMethods) {
                this.attributeDefinitions.add(new AttributeDefinition(12, 2, syntheticUTF, emptyUTF));
            }
            if (anySyntheticFields) {
                this.attributeDefinitions.add(new AttributeDefinition(12, 1, syntheticUTF, emptyUTF));
            }
        }
    }

    private int[] addHighIndices(int[] availableIndices) {
        int[] temp = new int[availableIndices.length + 32];
        System.arraycopy(availableIndices, 0, temp, 0, availableIndices.length);
        int j = 32;
        for (int i = availableIndices.length; i < temp.length; i++) {
            temp[i] = j;
            j++;
        }
        return temp;
    }

    private void addAttributeDefinitions(Map<String, String> layoutMap, int[] availableIndices, int contextType) {
        layoutMap.forEach(name, layout -> {
            int index = availableIndices[0];
            AttributeDefinition definition = new AttributeDefinition(index, availableIndices, this.cpBands.getCPUtf8(contextType), this.cpBands.getCPUtf8(layout));
            this.attributeDefinitions.add(definition);
            switch (availableIndices) {
                case 0:
                    this.classAttributeLayouts.add(definition);
                    return;
                case 1:
                    this.fieldAttributeLayouts.add(definition);
                    return;
                case 2:
                    this.methodAttributeLayouts.add(definition);
                    return;
                case 3:
                    this.codeAttributeLayouts.add(definition);
                    return;
                default:
                    return;
            }
        });
    }

    public List<AttributeDefinition> getClassAttributeLayouts() {
        return this.classAttributeLayouts;
    }

    public List<AttributeDefinition> getMethodAttributeLayouts() {
        return this.methodAttributeLayouts;
    }

    public List<AttributeDefinition> getFieldAttributeLayouts() {
        return this.fieldAttributeLayouts;
    }

    public List<AttributeDefinition> getCodeAttributeLayouts() {
        return this.codeAttributeLayouts;
    }

    /* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/pack200/AttributeDefinitionBands$AttributeDefinition.class */
    public static class AttributeDefinition {
        public int index;
        public int contextType;
        public CPUTF8 name;
        public CPUTF8 layout;

        public AttributeDefinition(int index, int contextType, CPUTF8 name, CPUTF8 layout) {
            this.index = index;
            this.contextType = contextType;
            this.name = name;
            this.layout = layout;
        }
    }
}
