package org.apache.commons.compress.harmony.unpack200;

import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.net.SyslogConstants;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.harmony.unpack200.bytecode.AnnotationDefaultAttribute;
import org.apache.commons.compress.harmony.unpack200.bytecode.AnnotationsAttribute;
import org.apache.commons.compress.harmony.unpack200.bytecode.Attribute;
import org.apache.commons.compress.harmony.unpack200.bytecode.CPDouble;
import org.apache.commons.compress.harmony.unpack200.bytecode.CPFloat;
import org.apache.commons.compress.harmony.unpack200.bytecode.CPInteger;
import org.apache.commons.compress.harmony.unpack200.bytecode.CPLong;
import org.apache.commons.compress.harmony.unpack200.bytecode.CPUTF8;
import org.apache.commons.compress.harmony.unpack200.bytecode.RuntimeVisibleorInvisibleAnnotationsAttribute;
import org.apache.commons.compress.harmony.unpack200.bytecode.RuntimeVisibleorInvisibleParameterAnnotationsAttribute;
import org.apache.http.HttpStatus;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/unpack200/MetadataBandGroup.class */
public class MetadataBandGroup {
    private final String type;
    private final CpBands cpBands;
    private static CPUTF8 rvaUTF8;
    private static CPUTF8 riaUTF8;
    private static CPUTF8 rvpaUTF8;
    private static CPUTF8 ripaUTF8;
    private List<Attribute> attributes;
    public int[] param_NB;
    public int[] anno_N;
    public CPUTF8[][] type_RS;
    public int[][] pair_N;
    public CPUTF8[] name_RU;
    public int[] T;
    public CPInteger[] caseI_KI;
    public CPDouble[] caseD_KD;
    public CPFloat[] caseF_KF;
    public CPLong[] caseJ_KJ;
    public CPUTF8[] casec_RS;
    public String[] caseet_RS;
    public String[] caseec_RU;
    public CPUTF8[] cases_RU;
    public int[] casearray_N;
    public CPUTF8[] nesttype_RS;
    public int[] nestpair_N;
    public CPUTF8[] nestname_RU;
    private int caseI_KI_Index;
    private int caseD_KD_Index;
    private int caseF_KF_Index;
    private int caseJ_KJ_Index;
    private int casec_RS_Index;
    private int caseet_RS_Index;
    private int caseec_RU_Index;
    private int cases_RU_Index;
    private int casearray_N_Index;
    private int T_index;
    private int nesttype_RS_Index;
    private int nestpair_N_Index;
    private Iterator<CPUTF8> nestname_RU_Iterator;
    private int anno_N_Index;
    private int pair_N_Index;

    public static void setRvaAttributeName(CPUTF8 cpUTF8Value) {
        rvaUTF8 = cpUTF8Value;
    }

    public static void setRiaAttributeName(CPUTF8 cpUTF8Value) {
        riaUTF8 = cpUTF8Value;
    }

    public static void setRvpaAttributeName(CPUTF8 cpUTF8Value) {
        rvpaUTF8 = cpUTF8Value;
    }

    public static void setRipaAttributeName(CPUTF8 cpUTF8Value) {
        ripaUTF8 = cpUTF8Value;
    }

    public MetadataBandGroup(String type, CpBands cpBands) {
        this.type = type;
        this.cpBands = cpBands;
    }

    public List<Attribute> getAttributes() {
        int[] iArr;
        if (this.attributes == null) {
            this.attributes = new ArrayList();
            if (this.name_RU != null) {
                Iterator<CPUTF8> name_RU_Iterator = Arrays.asList(this.name_RU).iterator();
                if (!this.type.equals("AD")) {
                    this.T_index = 0;
                }
                this.caseI_KI_Index = 0;
                this.caseD_KD_Index = 0;
                this.caseF_KF_Index = 0;
                this.caseJ_KJ_Index = 0;
                this.casec_RS_Index = 0;
                this.caseet_RS_Index = 0;
                this.caseec_RU_Index = 0;
                this.cases_RU_Index = 0;
                this.casearray_N_Index = 0;
                this.nesttype_RS_Index = 0;
                this.nestpair_N_Index = 0;
                this.nestname_RU_Iterator = Arrays.asList(this.nestname_RU).iterator();
                if (this.type.equals("RVA") || this.type.equals("RIA")) {
                    for (int i = 0; i < this.anno_N.length; i++) {
                        this.attributes.add(getAttribute(this.anno_N[i], this.type_RS[i], this.pair_N[i], name_RU_Iterator));
                    }
                } else if (this.type.equals("RVPA") || this.type.equals("RIPA")) {
                    this.anno_N_Index = 0;
                    this.pair_N_Index = 0;
                    for (int element : this.param_NB) {
                        this.attributes.add(getParameterAttribute(element, name_RU_Iterator));
                    }
                }
            } else if (this.type.equals("AD")) {
                for (int element2 : this.T) {
                    this.attributes.add(new AnnotationDefaultAttribute(new AnnotationsAttribute.ElementValue(element2, getNextValue(element2))));
                }
            }
        }
        return this.attributes;
    }

    private Attribute getAttribute(int numAnnotations, CPUTF8[] types, int[] pairCounts, Iterator<CPUTF8> namesIterator) {
        AnnotationsAttribute.Annotation[] annotations = new AnnotationsAttribute.Annotation[numAnnotations];
        Arrays.setAll(annotations, i -> {
            return getAnnotation(types[namesIterator], types[namesIterator], pairCounts);
        });
        return new RuntimeVisibleorInvisibleAnnotationsAttribute(this.type.equals("RVA") ? rvaUTF8 : riaUTF8, annotations);
    }

    private Attribute getParameterAttribute(int numParameters, Iterator<CPUTF8> namesIterator) {
        RuntimeVisibleorInvisibleParameterAnnotationsAttribute.ParameterAnnotation[] parameter_annotations = new RuntimeVisibleorInvisibleParameterAnnotationsAttribute.ParameterAnnotation[numParameters];
        for (int i = 0; i < numParameters; i++) {
            int[] iArr = this.anno_N;
            int i2 = this.anno_N_Index;
            this.anno_N_Index = i2 + 1;
            int numAnnotations = iArr[i2];
            int[][] iArr2 = this.pair_N;
            int i3 = this.pair_N_Index;
            this.pair_N_Index = i3 + 1;
            int[] pairCounts = iArr2[i3];
            AnnotationsAttribute.Annotation[] annotations = new AnnotationsAttribute.Annotation[numAnnotations];
            Arrays.setAll(annotations, j -> {
                return getAnnotation(this.type_RS[this.anno_N_Index - 1][namesIterator], pairCounts[namesIterator], pairCounts);
            });
            parameter_annotations[i] = new RuntimeVisibleorInvisibleParameterAnnotationsAttribute.ParameterAnnotation(annotations);
        }
        return new RuntimeVisibleorInvisibleParameterAnnotationsAttribute(this.type.equals("RVPA") ? rvpaUTF8 : ripaUTF8, parameter_annotations);
    }

    private AnnotationsAttribute.Annotation getAnnotation(CPUTF8 type, int pairCount, Iterator<CPUTF8> namesIterator) {
        CPUTF8[] elementNames = new CPUTF8[pairCount];
        AnnotationsAttribute.ElementValue[] elementValues = new AnnotationsAttribute.ElementValue[pairCount];
        for (int j = 0; j < elementNames.length; j++) {
            elementNames[j] = namesIterator.next();
            int[] iArr = this.T;
            int i = this.T_index;
            this.T_index = i + 1;
            int t = iArr[i];
            elementValues[j] = new AnnotationsAttribute.ElementValue(t, getNextValue(t));
        }
        return new AnnotationsAttribute.Annotation(pairCount, type, elementNames, elementValues);
    }

    private Object getNextValue(int t) {
        switch (t) {
            case 64:
                CPUTF8[] cputf8Arr = this.nesttype_RS;
                int i = this.nesttype_RS_Index;
                this.nesttype_RS_Index = i + 1;
                CPUTF8 type = cputf8Arr[i];
                int[] iArr = this.nestpair_N;
                int i2 = this.nestpair_N_Index;
                this.nestpair_N_Index = i2 + 1;
                int numPairs = iArr[i2];
                return getAnnotation(type, numPairs, this.nestname_RU_Iterator);
            case 65:
            case 69:
            case 71:
            case 72:
            case TarConstants.LF_GNUTYPE_LONGLINK /* 75 */:
            case 76:
            case TarConstants.LF_MULTIVOLUME /* 77 */:
            case 78:
            case 79:
            case 80:
            case 81:
            case 82:
            case 84:
            case 85:
            case 86:
            case 87:
            case 88:
            case 89:
            case 92:
            case 93:
            case 94:
            case 95:
            case 96:
            case 97:
            case 98:
            case 100:
            case HttpStatus.SC_PROCESSING /* 102 */:
            case TarConstants.LF_PAX_GLOBAL_EXTENDED_HEADER /* 103 */:
            case SyslogConstants.LOG_AUDIT /* 104 */:
            case 105:
            case 106:
            case 107:
            case 108:
            case 109:
            case 110:
            case 111:
            case SyslogConstants.LOG_ALERT /* 112 */:
            case 113:
            case 114:
            default:
                return null;
            case 66:
            case 67:
            case 73:
            case TarConstants.LF_GNUTYPE_SPARSE /* 83 */:
            case 90:
                CPInteger[] cPIntegerArr = this.caseI_KI;
                int i3 = this.caseI_KI_Index;
                this.caseI_KI_Index = i3 + 1;
                return cPIntegerArr[i3];
            case 68:
                CPDouble[] cPDoubleArr = this.caseD_KD;
                int i4 = this.caseD_KD_Index;
                this.caseD_KD_Index = i4 + 1;
                return cPDoubleArr[i4];
            case CoreConstants.OOS_RESET_FREQUENCY /* 70 */:
                CPFloat[] cPFloatArr = this.caseF_KF;
                int i5 = this.caseF_KF_Index;
                this.caseF_KF_Index = i5 + 1;
                return cPFloatArr[i5];
            case 74:
                CPLong[] cPLongArr = this.caseJ_KJ;
                int i6 = this.caseJ_KJ_Index;
                this.caseJ_KJ_Index = i6 + 1;
                return cPLongArr[i6];
            case 91:
                int[] iArr2 = this.casearray_N;
                int i7 = this.casearray_N_Index;
                this.casearray_N_Index = i7 + 1;
                int arraySize = iArr2[i7];
                AnnotationsAttribute.ElementValue[] nestedArray = new AnnotationsAttribute.ElementValue[arraySize];
                for (int i8 = 0; i8 < arraySize; i8++) {
                    int[] iArr3 = this.T;
                    int i9 = this.T_index;
                    this.T_index = i9 + 1;
                    int nextT = iArr3[i9];
                    nestedArray[i8] = new AnnotationsAttribute.ElementValue(nextT, getNextValue(nextT));
                }
                return nestedArray;
            case 99:
                CPUTF8[] cputf8Arr2 = this.casec_RS;
                int i10 = this.casec_RS_Index;
                this.casec_RS_Index = i10 + 1;
                return cputf8Arr2[i10];
            case HttpStatus.SC_SWITCHING_PROTOCOLS /* 101 */:
                StringBuilder sb = new StringBuilder();
                String[] strArr = this.caseet_RS;
                int i11 = this.caseet_RS_Index;
                this.caseet_RS_Index = i11 + 1;
                StringBuilder append = sb.append(strArr[i11]).append(":");
                String[] strArr2 = this.caseec_RU;
                int i12 = this.caseec_RU_Index;
                this.caseec_RU_Index = i12 + 1;
                String enumString = append.append(strArr2[i12]).toString();
                return this.cpBands.cpNameAndTypeValue(enumString);
            case 115:
                CPUTF8[] cputf8Arr3 = this.cases_RU;
                int i13 = this.cases_RU_Index;
                this.cases_RU_Index = i13 + 1;
                return cputf8Arr3[i13];
        }
    }
}
