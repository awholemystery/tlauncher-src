package org.apache.commons.compress.harmony.unpack200;

import ch.qos.logback.core.CoreConstants;
import java.util.ArrayList;
import java.util.List;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/harmony/unpack200/IcTuple.class */
public class IcTuple {
    private final int cIndex;
    private final int c2Index;
    private final int nIndex;
    private final int tIndex;
    public static final int NESTED_CLASS_FLAG = 65536;
    protected String C;
    protected int F;
    protected String C2;
    protected String N;
    private boolean predictSimple;
    private boolean predictOuter;
    private String cachedOuterClassString;
    private String cachedSimpleClassName;
    private boolean initialized;
    private boolean anonymous;
    private boolean outerIsAnonymous;
    private boolean member = true;
    private int cachedOuterClassIndex = -1;
    private int cachedSimpleClassNameIndex = -1;
    private boolean hashcodeComputed;
    private int cachedHashCode;
    private static final String[] EMPTY_STRING_ARRAY = new String[0];
    static final IcTuple[] EMPTY_ARRAY = new IcTuple[0];

    public IcTuple(String C, int F, String C2, String N, int cIndex, int c2Index, int nIndex, int tIndex) {
        this.C = C;
        this.F = F;
        this.C2 = C2;
        this.N = N;
        this.cIndex = cIndex;
        this.c2Index = c2Index;
        this.nIndex = nIndex;
        this.tIndex = tIndex;
        if (null == N) {
            this.predictSimple = true;
        }
        if (null == C2) {
            this.predictOuter = true;
        }
        initializeClassStrings();
    }

    public boolean predicted() {
        return this.predictOuter || this.predictSimple;
    }

    public boolean nestedExplicitFlagSet() {
        return (this.F & NESTED_CLASS_FLAG) == 65536;
    }

    public String[] innerBreakAtDollar(String className) {
        List<String> resultList = new ArrayList<>();
        int start = 0;
        int index = 0;
        while (index < className.length()) {
            if (className.charAt(index) <= '$') {
                resultList.add(className.substring(start, index));
                start = index + 1;
            }
            index++;
            if (index >= className.length()) {
                resultList.add(className.substring(start));
            }
        }
        return (String[]) resultList.toArray(EMPTY_STRING_ARRAY);
    }

    public String outerClassString() {
        return this.cachedOuterClassString;
    }

    public String simpleClassName() {
        return this.cachedSimpleClassName;
    }

    public String thisClassString() {
        if (predicted()) {
            return this.C;
        }
        return this.C2 + "$" + this.N;
    }

    public boolean isMember() {
        return this.member;
    }

    public boolean isAnonymous() {
        return this.anonymous;
    }

    public boolean outerIsAnonymous() {
        return this.outerIsAnonymous;
    }

    private boolean computeOuterIsAnonymous() {
        String[] result = innerBreakAtDollar(this.cachedOuterClassString);
        if (result.length == 0) {
            throw new Error("Should have an outer before checking if it's anonymous");
        }
        for (String element : result) {
            if (isAllDigits(element)) {
                return true;
            }
        }
        return false;
    }

    private void initializeClassStrings() {
        if (this.initialized) {
            return;
        }
        this.initialized = true;
        if (!this.predictSimple) {
            this.cachedSimpleClassName = this.N;
        }
        if (!this.predictOuter) {
            this.cachedOuterClassString = this.C2;
        }
        String[] nameComponents = innerBreakAtDollar(this.C);
        if (nameComponents.length == 0) {
        }
        if (nameComponents.length == 1) {
        }
        if (nameComponents.length < 2) {
            return;
        }
        int lastPosition = nameComponents.length - 1;
        this.cachedSimpleClassName = nameComponents[lastPosition];
        this.cachedOuterClassString = CoreConstants.EMPTY_STRING;
        for (int index = 0; index < lastPosition; index++) {
            this.cachedOuterClassString += nameComponents[index];
            if (isAllDigits(nameComponents[index])) {
                this.member = false;
            }
            if (index + 1 != lastPosition) {
                this.cachedOuterClassString += '$';
            }
        }
        if (!this.predictSimple) {
            this.cachedSimpleClassName = this.N;
            this.cachedSimpleClassNameIndex = this.nIndex;
        }
        if (!this.predictOuter) {
            this.cachedOuterClassString = this.C2;
            this.cachedOuterClassIndex = this.c2Index;
        }
        if (isAllDigits(this.cachedSimpleClassName)) {
            this.anonymous = true;
            this.member = false;
            if (nestedExplicitFlagSet()) {
                this.member = true;
            }
        }
        this.outerIsAnonymous = computeOuterIsAnonymous();
    }

    private boolean isAllDigits(String nameString) {
        if (null == nameString) {
            return false;
        }
        for (int index = 0; index < nameString.length(); index++) {
            if (!Character.isDigit(nameString.charAt(index))) {
                return false;
            }
        }
        return true;
    }

    public String toString() {
        return "IcTuple (" + simpleClassName() + " in " + outerClassString() + ')';
    }

    public boolean nullSafeEquals(String stringOne, String stringTwo) {
        if (null == stringOne) {
            return null == stringTwo;
        }
        return stringOne.equals(stringTwo);
    }

    public boolean equals(Object object) {
        if (object == null || object.getClass() != getClass()) {
            return false;
        }
        IcTuple compareTuple = (IcTuple) object;
        if (!nullSafeEquals(this.C, compareTuple.C) || !nullSafeEquals(this.C2, compareTuple.C2) || !nullSafeEquals(this.N, compareTuple.N)) {
            return false;
        }
        return true;
    }

    private void generateHashCode() {
        this.hashcodeComputed = true;
        this.cachedHashCode = 17;
        if (this.C != null) {
            this.cachedHashCode = this.C.hashCode();
        }
        if (this.C2 != null) {
            this.cachedHashCode = this.C2.hashCode();
        }
        if (this.N != null) {
            this.cachedHashCode = this.N.hashCode();
        }
    }

    public int hashCode() {
        if (!this.hashcodeComputed) {
            generateHashCode();
        }
        return this.cachedHashCode;
    }

    public String getC() {
        return this.C;
    }

    public int getF() {
        return this.F;
    }

    public String getC2() {
        return this.C2;
    }

    public String getN() {
        return this.N;
    }

    public int getTupleIndex() {
        return this.tIndex;
    }

    public int thisClassIndex() {
        if (predicted()) {
            return this.cIndex;
        }
        return -1;
    }

    public int outerClassIndex() {
        return this.cachedOuterClassIndex;
    }

    public int simpleClassNameIndex() {
        return this.cachedSimpleClassNameIndex;
    }
}
