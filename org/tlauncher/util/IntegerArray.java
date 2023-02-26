package org.tlauncher.util;

import org.tlauncher.exceptions.ParseException;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/util/IntegerArray.class */
public class IntegerArray {
    public static final char defaultDelimiter = ';';
    private final int[] integers;
    private final char delimiter;
    private final int length;

    private IntegerArray(char del, int... values) {
        this.delimiter = del;
        this.length = values.length;
        this.integers = new int[this.length];
        System.arraycopy(values, 0, this.integers, 0, this.length);
    }

    public IntegerArray(int... values) {
        this(';', values);
    }

    public int get(int pos) {
        if (pos < 0 || pos >= this.length) {
            throw new ArrayIndexOutOfBoundsException("Invalid position (" + pos + " / " + this.length + ")!");
        }
        return this.integers[pos];
    }

    public void set(int pos, int val) {
        if (pos < 0 || pos >= this.length) {
            throw new ArrayIndexOutOfBoundsException("Invalid position (" + pos + " / " + this.length + ")!");
        }
        this.integers[pos] = val;
    }

    public int size() {
        return this.length;
    }

    public int[] toArray() {
        int[] r = new int[this.length];
        System.arraycopy(this.integers, 0, r, 0, this.length);
        return r;
    }

    public String toString() {
        int[] iArr;
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (int i : this.integers) {
            if (!first) {
                sb.append(this.delimiter);
            } else {
                first = false;
            }
            sb.append(i);
        }
        return sb.toString();
    }

    public static IntegerArray parseIntegerArray(String val, char del) throws ParseException {
        if (val == null) {
            throw new ParseException("String cannot be NULL!");
        }
        if (val.length() <= 1) {
            throw new ParseException("String mustn't equal or be less than delimiter!");
        }
        String regexp = "(?<!\\\\)";
        if (del != 'x') {
            regexp = regexp + "\\";
        }
        String[] ints = val.split(regexp + del);
        int l = ints.length;
        int[] arr = new int[l];
        for (int i = 0; i < l; i++) {
            try {
                int cur = Integer.parseInt(ints[i]);
                arr[i] = cur;
            } catch (NumberFormatException e) {
                U.log("Cannot parse integer (iteration: " + i + ")", e);
                throw new ParseException("Cannot parse integer (iteration: " + i + ")", e);
            }
        }
        return new IntegerArray(del, arr);
    }

    public static IntegerArray parseIntegerArray(String val) throws ParseException {
        return parseIntegerArray(val, ';');
    }

    private static int[] toArray(String val, char del) throws ParseException {
        IntegerArray arr = parseIntegerArray(val, del);
        return arr.toArray();
    }

    public static int[] toArray(String val) throws ParseException {
        return toArray(val, ';');
    }
}
