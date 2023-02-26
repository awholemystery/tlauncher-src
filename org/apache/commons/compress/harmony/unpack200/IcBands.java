package org.apache.commons.compress.harmony.unpack200;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.compress.harmony.pack200.Codec;
import org.apache.commons.compress.harmony.pack200.Pack200Exception;
import org.apache.commons.compress.harmony.unpack200.bytecode.CPClass;
import org.apache.commons.compress.harmony.unpack200.bytecode.ClassConstantPool;
import org.apache.commons.compress.harmony.unpack200.bytecode.ClassFileEntry;
import org.apache.commons.compress.harmony.unpack200.bytecode.ConstantPoolEntry;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/unpack200/IcBands.class */
public class IcBands extends BandSet {
    private IcTuple[] icAll;
    private final String[] cpUTF8;
    private final String[] cpClass;
    private Map<String, IcTuple> thisClassToTuple;
    private Map<String, List<IcTuple>> outerClassToTuples;

    public IcBands(Segment segment) {
        super(segment);
        this.cpClass = segment.getCpBands().getCpClass();
        this.cpUTF8 = segment.getCpBands().getCpUTF8();
    }

    @Override // org.apache.commons.compress.harmony.unpack200.BandSet
    public void read(InputStream in) throws IOException, Pack200Exception {
        int innerClassCount = this.header.getInnerClassCount();
        int[] icThisClassInts = decodeBandInt("ic_this_class", in, Codec.UDELTA5, innerClassCount);
        String[] icThisClass = getReferences(icThisClassInts, this.cpClass);
        int[] icFlags = decodeBandInt("ic_flags", in, Codec.UNSIGNED5, innerClassCount);
        int outerClasses = SegmentUtils.countBit16(icFlags);
        int[] icOuterClassInts = decodeBandInt("ic_outer_class", in, Codec.DELTA5, outerClasses);
        String[] icOuterClass = new String[outerClasses];
        for (int i = 0; i < icOuterClass.length; i++) {
            if (icOuterClassInts[i] == 0) {
                icOuterClass[i] = null;
            } else {
                icOuterClass[i] = this.cpClass[icOuterClassInts[i] - 1];
            }
        }
        int[] icNameInts = decodeBandInt("ic_name", in, Codec.DELTA5, outerClasses);
        String[] icName = new String[outerClasses];
        for (int i2 = 0; i2 < icName.length; i2++) {
            if (icNameInts[i2] == 0) {
                icName[i2] = null;
            } else {
                icName[i2] = this.cpUTF8[icNameInts[i2] - 1];
            }
        }
        this.icAll = new IcTuple[icThisClass.length];
        int index = 0;
        for (int i3 = 0; i3 < icThisClass.length; i3++) {
            String icTupleC = icThisClass[i3];
            int icTupleF = icFlags[i3];
            String icTupleC2 = null;
            String icTupleN = null;
            int cIndex = icThisClassInts[i3];
            int c2Index = -1;
            int nIndex = -1;
            if ((icFlags[i3] & IcTuple.NESTED_CLASS_FLAG) != 0) {
                icTupleC2 = icOuterClass[index];
                icTupleN = icName[index];
                c2Index = icOuterClassInts[index] - 1;
                nIndex = icNameInts[index] - 1;
                index++;
            }
            this.icAll[i3] = new IcTuple(icTupleC, icTupleF, icTupleC2, icTupleN, cIndex, c2Index, nIndex, i3);
        }
    }

    @Override // org.apache.commons.compress.harmony.unpack200.BandSet
    public void unpack() throws IOException, Pack200Exception {
        IcTuple[] allTuples = getIcTuples();
        this.thisClassToTuple = new HashMap(allTuples.length);
        this.outerClassToTuples = new HashMap(allTuples.length);
        for (IcTuple tuple : allTuples) {
            Object result = this.thisClassToTuple.put(tuple.thisClassString(), tuple);
            if (result != null) {
                throw new Error("Collision detected in <thisClassString, IcTuple> mapping. There are at least two inner clases with the same name.");
            }
            if ((!tuple.isAnonymous() && !tuple.outerIsAnonymous()) || tuple.nestedExplicitFlagSet()) {
                String key = tuple.outerClassString();
                List<IcTuple> bucket = this.outerClassToTuples.get(key);
                if (bucket == null) {
                    bucket = new ArrayList<>();
                    this.outerClassToTuples.put(key, bucket);
                }
                bucket.add(tuple);
            }
        }
    }

    public IcTuple[] getIcTuples() {
        return this.icAll;
    }

    public IcTuple[] getRelevantIcTuples(String className, ClassConstantPool cp) {
        Set<IcTuple> relevantTuplesContains = new HashSet<>();
        List<IcTuple> relevantTuples = new ArrayList<>();
        List<IcTuple> relevantCandidates = this.outerClassToTuples.get(className);
        if (relevantCandidates != null) {
            for (int index = 0; index < relevantCandidates.size(); index++) {
                IcTuple tuple = relevantCandidates.get(index);
                relevantTuplesContains.add(tuple);
                relevantTuples.add(tuple);
            }
        }
        List<ClassFileEntry> entries = cp.entries();
        for (int eIndex = 0; eIndex < entries.size(); eIndex++) {
            ConstantPoolEntry entry = (ConstantPoolEntry) entries.get(eIndex);
            if (entry instanceof CPClass) {
                CPClass clazz = (CPClass) entry;
                IcTuple relevant = this.thisClassToTuple.get(clazz.name);
                if (relevant != null && relevantTuplesContains.add(relevant)) {
                    relevantTuples.add(relevant);
                }
            }
        }
        List<IcTuple> tuplesToScan = new ArrayList<>(relevantTuples);
        List<IcTuple> tuplesToAdd = new ArrayList<>();
        while (tuplesToScan.size() > 0) {
            tuplesToAdd.clear();
            for (int index2 = 0; index2 < tuplesToScan.size(); index2++) {
                IcTuple aRelevantTuple = tuplesToScan.get(index2);
                IcTuple relevant2 = this.thisClassToTuple.get(aRelevantTuple.outerClassString());
                if (relevant2 != null && !aRelevantTuple.outerIsAnonymous()) {
                    tuplesToAdd.add(relevant2);
                }
            }
            tuplesToScan.clear();
            for (int index3 = 0; index3 < tuplesToAdd.size(); index3++) {
                IcTuple tuple2 = tuplesToAdd.get(index3);
                if (relevantTuplesContains.add(tuple2)) {
                    relevantTuples.add(tuple2);
                    tuplesToScan.add(tuple2);
                }
            }
        }
        relevantTuples.sort(arg0, arg1 -> {
            Integer index1 = Integer.valueOf(arg0.getTupleIndex());
            Integer index22 = Integer.valueOf(arg1.getTupleIndex());
            return index1.compareTo(index22);
        });
        return (IcTuple[]) relevantTuples.toArray(IcTuple.EMPTY_ARRAY);
    }
}
