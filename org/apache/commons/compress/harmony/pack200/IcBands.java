package org.apache.commons.compress.harmony.pack200;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/pack200/IcBands.class */
public class IcBands extends BandSet {
    private final Set<IcTuple> innerClasses;
    private final CpBands cpBands;
    private int bit16Count;
    private final Map<String, List<IcTuple>> outerToInner;

    public IcBands(SegmentHeader segmentHeader, CpBands cpBands, int effort) {
        super(effort, segmentHeader);
        this.innerClasses = new TreeSet();
        this.bit16Count = 0;
        this.outerToInner = new HashMap();
        this.cpBands = cpBands;
    }

    public void finaliseBands() {
        this.segmentHeader.setIc_count(this.innerClasses.size());
    }

    @Override // org.apache.commons.compress.harmony.pack200.BandSet
    public void pack(OutputStream outputStream) throws IOException, Pack200Exception {
        PackingUtils.log("Writing internal class bands...");
        int[] ic_this_class = new int[this.innerClasses.size()];
        int[] ic_flags = new int[this.innerClasses.size()];
        int[] ic_outer_class = new int[this.bit16Count];
        int[] ic_name = new int[this.bit16Count];
        int index2 = 0;
        List<IcTuple> innerClassesList = new ArrayList<>(this.innerClasses);
        for (int i = 0; i < ic_this_class.length; i++) {
            IcTuple icTuple = innerClassesList.get(i);
            ic_this_class[i] = icTuple.C.getIndex();
            ic_flags[i] = icTuple.F;
            if ((icTuple.F & org.apache.commons.compress.harmony.unpack200.IcTuple.NESTED_CLASS_FLAG) != 0) {
                ic_outer_class[index2] = icTuple.C2 == null ? 0 : icTuple.C2.getIndex() + 1;
                ic_name[index2] = icTuple.N == null ? 0 : icTuple.N.getIndex() + 1;
                index2++;
            }
        }
        byte[] encodedBand = encodeBandInt("ic_this_class", ic_this_class, Codec.UDELTA5);
        outputStream.write(encodedBand);
        PackingUtils.log("Wrote " + encodedBand.length + " bytes from ic_this_class[" + ic_this_class.length + "]");
        byte[] encodedBand2 = encodeBandInt("ic_flags", ic_flags, Codec.UNSIGNED5);
        outputStream.write(encodedBand2);
        PackingUtils.log("Wrote " + encodedBand2.length + " bytes from ic_flags[" + ic_flags.length + "]");
        byte[] encodedBand3 = encodeBandInt("ic_outer_class", ic_outer_class, Codec.DELTA5);
        outputStream.write(encodedBand3);
        PackingUtils.log("Wrote " + encodedBand3.length + " bytes from ic_outer_class[" + ic_outer_class.length + "]");
        byte[] encodedBand4 = encodeBandInt("ic_name", ic_name, Codec.DELTA5);
        outputStream.write(encodedBand4);
        PackingUtils.log("Wrote " + encodedBand4.length + " bytes from ic_name[" + ic_name.length + "]");
    }

    public void addInnerClass(String name, String outerName, String innerName, int flags) {
        if (outerName != null || innerName != null) {
            if (namesArePredictable(name, outerName, innerName)) {
                IcTuple innerClass = new IcTuple(this.cpBands.getCPClass(name), flags, null, null);
                addToMap(outerName, innerClass);
                this.innerClasses.add(innerClass);
                return;
            }
            IcTuple icTuple = new IcTuple(this.cpBands.getCPClass(name), flags | org.apache.commons.compress.harmony.unpack200.IcTuple.NESTED_CLASS_FLAG, this.cpBands.getCPClass(outerName), this.cpBands.getCPUtf8(innerName));
            boolean added = this.innerClasses.add(icTuple);
            if (added) {
                this.bit16Count++;
                addToMap(outerName, icTuple);
                return;
            }
            return;
        }
        IcTuple innerClass2 = new IcTuple(this.cpBands.getCPClass(name), flags, null, null);
        addToMap(getOuter(name), innerClass2);
        this.innerClasses.add(innerClass2);
    }

    public List<IcTuple> getInnerClassesForOuter(String outerClassName) {
        return this.outerToInner.get(outerClassName);
    }

    private String getOuter(String name) {
        return name.substring(0, name.lastIndexOf(36));
    }

    private void addToMap(String outerName, IcTuple icTuple) {
        List<IcTuple> tuples = this.outerToInner.get(outerName);
        if (tuples == null) {
            List<IcTuple> tuples2 = new ArrayList<>();
            this.outerToInner.put(outerName, tuples2);
            tuples2.add(icTuple);
            return;
        }
        for (IcTuple tuple : tuples) {
            if (icTuple.equals(tuple)) {
                return;
            }
        }
        tuples.add(icTuple);
    }

    private boolean namesArePredictable(String name, String outerName, String innerName) {
        return name.equals(new StringBuilder().append(outerName).append('$').append(innerName).toString()) && innerName.indexOf(36) == -1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/pack200/IcBands$IcTuple.class */
    public class IcTuple implements Comparable<IcTuple> {
        protected CPClass C;
        protected int F;
        protected CPClass C2;
        protected CPUTF8 N;

        public IcTuple(CPClass C, int F, CPClass C2, CPUTF8 N) {
            this.C = C;
            this.F = F;
            this.C2 = C2;
            this.N = N;
        }

        public boolean equals(Object o) {
            if (o instanceof IcTuple) {
                IcTuple icT = (IcTuple) o;
                return this.C.equals(icT.C) && this.F == icT.F && Objects.equals(this.C2, icT.C2) && Objects.equals(this.N, icT.N);
            }
            return false;
        }

        public String toString() {
            return this.C.toString();
        }

        @Override // java.lang.Comparable
        public int compareTo(IcTuple arg0) {
            return this.C.compareTo(arg0.C);
        }

        public boolean isAnonymous() {
            String className = this.C.toString();
            String innerName = className.substring(className.lastIndexOf(36) + 1);
            return Character.isDigit(innerName.charAt(0));
        }
    }

    public IcTuple getIcTuple(CPClass inner) {
        for (IcTuple icTuple : this.innerClasses) {
            if (icTuple.C.equals(inner)) {
                return icTuple;
            }
        }
        return null;
    }
}
