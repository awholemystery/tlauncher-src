package org.apache.commons.compress.harmony.pack200;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/pack200/MetadataBandGroup.class */
public class MetadataBandGroup extends BandSet {
    public static final int CONTEXT_CLASS = 0;
    public static final int CONTEXT_FIELD = 1;
    public static final int CONTEXT_METHOD = 2;
    private final String type;
    private int numBackwardsCalls;
    public IntList param_NB;
    public IntList anno_N;
    public List<CPSignature> type_RS;
    public IntList pair_N;
    public List<CPUTF8> name_RU;
    public List<String> T;
    public List<CPConstant<?>> caseI_KI;
    public List<CPConstant<?>> caseD_KD;
    public List<CPConstant<?>> caseF_KF;
    public List<CPConstant<?>> caseJ_KJ;
    public List<CPSignature> casec_RS;
    public List<CPSignature> caseet_RS;
    public List<CPUTF8> caseec_RU;
    public List<CPUTF8> cases_RU;
    public IntList casearray_N;
    public List<CPSignature> nesttype_RS;
    public IntList nestpair_N;
    public List<CPUTF8> nestname_RU;
    private final CpBands cpBands;
    private final int context;

    public MetadataBandGroup(String type, int context, CpBands cpBands, SegmentHeader segmentHeader, int effort) {
        super(effort, segmentHeader);
        this.numBackwardsCalls = 0;
        this.param_NB = new IntList();
        this.anno_N = new IntList();
        this.type_RS = new ArrayList();
        this.pair_N = new IntList();
        this.name_RU = new ArrayList();
        this.T = new ArrayList();
        this.caseI_KI = new ArrayList();
        this.caseD_KD = new ArrayList();
        this.caseF_KF = new ArrayList();
        this.caseJ_KJ = new ArrayList();
        this.casec_RS = new ArrayList();
        this.caseet_RS = new ArrayList();
        this.caseec_RU = new ArrayList();
        this.cases_RU = new ArrayList();
        this.casearray_N = new IntList();
        this.nesttype_RS = new ArrayList();
        this.nestpair_N = new IntList();
        this.nestname_RU = new ArrayList();
        this.type = type;
        this.cpBands = cpBands;
        this.context = context;
    }

    @Override // org.apache.commons.compress.harmony.pack200.BandSet
    public void pack(OutputStream out) throws IOException, Pack200Exception {
        String contextStr;
        PackingUtils.log("Writing metadata band group...");
        if (hasContent()) {
            if (this.context == 0) {
                contextStr = "Class";
            } else if (this.context == 1) {
                contextStr = "Field";
            } else {
                contextStr = "Method";
            }
            if (!this.type.equals("AD")) {
                if (this.type.indexOf(80) != -1) {
                    byte[] encodedBand = encodeBandInt(contextStr + "_" + this.type + " param_NB", this.param_NB.toArray(), Codec.BYTE1);
                    out.write(encodedBand);
                    PackingUtils.log("Wrote " + encodedBand.length + " bytes from " + contextStr + "_" + this.type + " anno_N[" + this.param_NB.size() + "]");
                }
                byte[] encodedBand2 = encodeBandInt(contextStr + "_" + this.type + " anno_N", this.anno_N.toArray(), Codec.UNSIGNED5);
                out.write(encodedBand2);
                PackingUtils.log("Wrote " + encodedBand2.length + " bytes from " + contextStr + "_" + this.type + " anno_N[" + this.anno_N.size() + "]");
                byte[] encodedBand3 = encodeBandInt(contextStr + "_" + this.type + " type_RS", cpEntryListToArray(this.type_RS), Codec.UNSIGNED5);
                out.write(encodedBand3);
                PackingUtils.log("Wrote " + encodedBand3.length + " bytes from " + contextStr + "_" + this.type + " type_RS[" + this.type_RS.size() + "]");
                byte[] encodedBand4 = encodeBandInt(contextStr + "_" + this.type + " pair_N", this.pair_N.toArray(), Codec.UNSIGNED5);
                out.write(encodedBand4);
                PackingUtils.log("Wrote " + encodedBand4.length + " bytes from " + contextStr + "_" + this.type + " pair_N[" + this.pair_N.size() + "]");
                byte[] encodedBand5 = encodeBandInt(contextStr + "_" + this.type + " name_RU", cpEntryListToArray(this.name_RU), Codec.UNSIGNED5);
                out.write(encodedBand5);
                PackingUtils.log("Wrote " + encodedBand5.length + " bytes from " + contextStr + "_" + this.type + " name_RU[" + this.name_RU.size() + "]");
            }
            byte[] encodedBand6 = encodeBandInt(contextStr + "_" + this.type + " T", tagListToArray(this.T), Codec.BYTE1);
            out.write(encodedBand6);
            PackingUtils.log("Wrote " + encodedBand6.length + " bytes from " + contextStr + "_" + this.type + " T[" + this.T.size() + "]");
            byte[] encodedBand7 = encodeBandInt(contextStr + "_" + this.type + " caseI_KI", cpEntryListToArray(this.caseI_KI), Codec.UNSIGNED5);
            out.write(encodedBand7);
            PackingUtils.log("Wrote " + encodedBand7.length + " bytes from " + contextStr + "_" + this.type + " caseI_KI[" + this.caseI_KI.size() + "]");
            byte[] encodedBand8 = encodeBandInt(contextStr + "_" + this.type + " caseD_KD", cpEntryListToArray(this.caseD_KD), Codec.UNSIGNED5);
            out.write(encodedBand8);
            PackingUtils.log("Wrote " + encodedBand8.length + " bytes from " + contextStr + "_" + this.type + " caseD_KD[" + this.caseD_KD.size() + "]");
            byte[] encodedBand9 = encodeBandInt(contextStr + "_" + this.type + " caseF_KF", cpEntryListToArray(this.caseF_KF), Codec.UNSIGNED5);
            out.write(encodedBand9);
            PackingUtils.log("Wrote " + encodedBand9.length + " bytes from " + contextStr + "_" + this.type + " caseF_KF[" + this.caseF_KF.size() + "]");
            byte[] encodedBand10 = encodeBandInt(contextStr + "_" + this.type + " caseJ_KJ", cpEntryListToArray(this.caseJ_KJ), Codec.UNSIGNED5);
            out.write(encodedBand10);
            PackingUtils.log("Wrote " + encodedBand10.length + " bytes from " + contextStr + "_" + this.type + " caseJ_KJ[" + this.caseJ_KJ.size() + "]");
            byte[] encodedBand11 = encodeBandInt(contextStr + "_" + this.type + " casec_RS", cpEntryListToArray(this.casec_RS), Codec.UNSIGNED5);
            out.write(encodedBand11);
            PackingUtils.log("Wrote " + encodedBand11.length + " bytes from " + contextStr + "_" + this.type + " casec_RS[" + this.casec_RS.size() + "]");
            byte[] encodedBand12 = encodeBandInt(contextStr + "_" + this.type + " caseet_RS", cpEntryListToArray(this.caseet_RS), Codec.UNSIGNED5);
            out.write(encodedBand12);
            PackingUtils.log("Wrote " + encodedBand12.length + " bytes from " + contextStr + "_" + this.type + " caseet_RS[" + this.caseet_RS.size() + "]");
            byte[] encodedBand13 = encodeBandInt(contextStr + "_" + this.type + " caseec_RU", cpEntryListToArray(this.caseec_RU), Codec.UNSIGNED5);
            out.write(encodedBand13);
            PackingUtils.log("Wrote " + encodedBand13.length + " bytes from " + contextStr + "_" + this.type + " caseec_RU[" + this.caseec_RU.size() + "]");
            byte[] encodedBand14 = encodeBandInt(contextStr + "_" + this.type + " cases_RU", cpEntryListToArray(this.cases_RU), Codec.UNSIGNED5);
            out.write(encodedBand14);
            PackingUtils.log("Wrote " + encodedBand14.length + " bytes from " + contextStr + "_" + this.type + " cases_RU[" + this.cases_RU.size() + "]");
            byte[] encodedBand15 = encodeBandInt(contextStr + "_" + this.type + " casearray_N", this.casearray_N.toArray(), Codec.UNSIGNED5);
            out.write(encodedBand15);
            PackingUtils.log("Wrote " + encodedBand15.length + " bytes from " + contextStr + "_" + this.type + " casearray_N[" + this.casearray_N.size() + "]");
            byte[] encodedBand16 = encodeBandInt(contextStr + "_" + this.type + " nesttype_RS", cpEntryListToArray(this.nesttype_RS), Codec.UNSIGNED5);
            out.write(encodedBand16);
            PackingUtils.log("Wrote " + encodedBand16.length + " bytes from " + contextStr + "_" + this.type + " nesttype_RS[" + this.nesttype_RS.size() + "]");
            byte[] encodedBand17 = encodeBandInt(contextStr + "_" + this.type + " nestpair_N", this.nestpair_N.toArray(), Codec.UNSIGNED5);
            out.write(encodedBand17);
            PackingUtils.log("Wrote " + encodedBand17.length + " bytes from " + contextStr + "_" + this.type + " nestpair_N[" + this.nestpair_N.size() + "]");
            byte[] encodedBand18 = encodeBandInt(contextStr + "_" + this.type + " nestname_RU", cpEntryListToArray(this.nestname_RU), Codec.UNSIGNED5);
            out.write(encodedBand18);
            PackingUtils.log("Wrote " + encodedBand18.length + " bytes from " + contextStr + "_" + this.type + " nestname_RU[" + this.nestname_RU.size() + "]");
        }
    }

    private int[] tagListToArray(List<String> list) {
        return list.stream().mapToInt(s -> {
            return s.charAt(0);
        }).toArray();
    }

    public void addParameterAnnotation(int numParams, int[] annoN, IntList pairN, List<String> typeRS, List<String> nameRU, List<String> t, List<Object> values, List<Integer> caseArrayN, List<String> nestTypeRS, List<String> nestNameRU, List<Integer> nestPairN) {
        this.param_NB.add(numParams);
        for (int element : annoN) {
            this.anno_N.add(element);
        }
        this.pair_N.addAll(pairN);
        for (String desc : typeRS) {
            this.type_RS.add(this.cpBands.getCPSignature(desc));
        }
        for (String name : nameRU) {
            this.name_RU.add(this.cpBands.getCPUtf8(name));
        }
        Iterator<Object> valuesIterator = values.iterator();
        for (String tag : t) {
            this.T.add(tag);
            if (tag.equals("B") || tag.equals("C") || tag.equals("I") || tag.equals("S") || tag.equals("Z")) {
                Integer value = (Integer) valuesIterator.next();
                this.caseI_KI.add(this.cpBands.getConstant(value));
            } else if (tag.equals("D")) {
                Double value2 = (Double) valuesIterator.next();
                this.caseD_KD.add(this.cpBands.getConstant(value2));
            } else if (tag.equals("F")) {
                Float value3 = (Float) valuesIterator.next();
                this.caseF_KF.add(this.cpBands.getConstant(value3));
            } else if (tag.equals("J")) {
                Long value4 = (Long) valuesIterator.next();
                this.caseJ_KJ.add(this.cpBands.getConstant(value4));
            } else if (tag.equals("c")) {
                String value5 = (String) valuesIterator.next();
                this.casec_RS.add(this.cpBands.getCPSignature(value5));
            } else if (tag.equals("e")) {
                String value6 = (String) valuesIterator.next();
                String value22 = (String) valuesIterator.next();
                this.caseet_RS.add(this.cpBands.getCPSignature(value6));
                this.caseec_RU.add(this.cpBands.getCPUtf8(value22));
            } else if (tag.equals("s")) {
                String value7 = (String) valuesIterator.next();
                this.cases_RU.add(this.cpBands.getCPUtf8(value7));
            }
        }
        for (Integer element2 : caseArrayN) {
            int arraySize = element2.intValue();
            this.casearray_N.add(arraySize);
            this.numBackwardsCalls += arraySize;
        }
        for (String type : nestTypeRS) {
            this.nesttype_RS.add(this.cpBands.getCPSignature(type));
        }
        for (String name2 : nestNameRU) {
            this.nestname_RU.add(this.cpBands.getCPUtf8(name2));
        }
        for (Integer numPairs : nestPairN) {
            this.nestpair_N.add(numPairs.intValue());
            this.numBackwardsCalls += numPairs.intValue();
        }
    }

    public void addAnnotation(String desc, List<String> nameRU, List<String> tags, List<Object> values, List<Integer> caseArrayN, List<String> nestTypeRS, List<String> nestNameRU, List<Integer> nestPairN) {
        this.type_RS.add(this.cpBands.getCPSignature(desc));
        this.pair_N.add(nameRU.size());
        for (String name : nameRU) {
            this.name_RU.add(this.cpBands.getCPUtf8(name));
        }
        Iterator<Object> valuesIterator = values.iterator();
        for (String tag : tags) {
            this.T.add(tag);
            if (tag.equals("B") || tag.equals("C") || tag.equals("I") || tag.equals("S") || tag.equals("Z")) {
                Integer value = (Integer) valuesIterator.next();
                this.caseI_KI.add(this.cpBands.getConstant(value));
            } else if (tag.equals("D")) {
                Double value2 = (Double) valuesIterator.next();
                this.caseD_KD.add(this.cpBands.getConstant(value2));
            } else if (tag.equals("F")) {
                Float value3 = (Float) valuesIterator.next();
                this.caseF_KF.add(this.cpBands.getConstant(value3));
            } else if (tag.equals("J")) {
                Long value4 = (Long) valuesIterator.next();
                this.caseJ_KJ.add(this.cpBands.getConstant(value4));
            } else if (tag.equals("c")) {
                String value5 = (String) valuesIterator.next();
                this.casec_RS.add(this.cpBands.getCPSignature(value5));
            } else if (tag.equals("e")) {
                String value6 = (String) valuesIterator.next();
                String value22 = (String) valuesIterator.next();
                this.caseet_RS.add(this.cpBands.getCPSignature(value6));
                this.caseec_RU.add(this.cpBands.getCPUtf8(value22));
            } else if (tag.equals("s")) {
                String value7 = (String) valuesIterator.next();
                this.cases_RU.add(this.cpBands.getCPUtf8(value7));
            }
        }
        for (Integer element : caseArrayN) {
            int arraySize = element.intValue();
            this.casearray_N.add(arraySize);
            this.numBackwardsCalls += arraySize;
        }
        for (String element2 : nestTypeRS) {
            this.nesttype_RS.add(this.cpBands.getCPSignature(element2));
        }
        for (String element3 : nestNameRU) {
            this.nestname_RU.add(this.cpBands.getCPUtf8(element3));
        }
        for (Integer element4 : nestPairN) {
            this.nestpair_N.add(element4.intValue());
            this.numBackwardsCalls += element4.intValue();
        }
    }

    public boolean hasContent() {
        return this.type_RS.size() > 0;
    }

    public int numBackwardsCalls() {
        return this.numBackwardsCalls;
    }

    public void incrementAnnoN() {
        this.anno_N.increment(this.anno_N.size() - 1);
    }

    public void newEntryInAnnoN() {
        this.anno_N.add(1);
    }

    public void removeLatest() {
        int latest = this.anno_N.remove(this.anno_N.size() - 1);
        for (int i = 0; i < latest; i++) {
            this.type_RS.remove(this.type_RS.size() - 1);
            int pairs = this.pair_N.remove(this.pair_N.size() - 1);
            for (int j = 0; j < pairs; j++) {
                removeOnePair();
            }
        }
    }

    private void removeOnePair() {
        String tag = this.T.remove(this.T.size() - 1);
        if (tag.equals("B") || tag.equals("C") || tag.equals("I") || tag.equals("S") || tag.equals("Z")) {
            this.caseI_KI.remove(this.caseI_KI.size() - 1);
        } else if (tag.equals("D")) {
            this.caseD_KD.remove(this.caseD_KD.size() - 1);
        } else if (tag.equals("F")) {
            this.caseF_KF.remove(this.caseF_KF.size() - 1);
        } else if (tag.equals("J")) {
            this.caseJ_KJ.remove(this.caseJ_KJ.size() - 1);
        } else if (tag.equals("C")) {
            this.casec_RS.remove(this.casec_RS.size() - 1);
        } else if (tag.equals("e")) {
            this.caseet_RS.remove(this.caseet_RS.size() - 1);
            this.caseec_RU.remove(this.caseet_RS.size() - 1);
        } else if (tag.equals("s")) {
            this.cases_RU.remove(this.cases_RU.size() - 1);
        } else if (tag.equals("[")) {
            int arraySize = this.casearray_N.remove(this.casearray_N.size() - 1);
            this.numBackwardsCalls -= arraySize;
            for (int k = 0; k < arraySize; k++) {
                removeOnePair();
            }
        } else if (tag.equals("@")) {
            this.nesttype_RS.remove(this.nesttype_RS.size() - 1);
            int numPairs = this.nestpair_N.remove(this.nestpair_N.size() - 1);
            this.numBackwardsCalls -= numPairs;
            for (int i = 0; i < numPairs; i++) {
                removeOnePair();
            }
        }
    }
}
