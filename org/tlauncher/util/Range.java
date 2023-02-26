package org.tlauncher.util;

import java.lang.Number;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/util/Range.class */
public class Range<T extends Number> {
    private final T minValue;
    private final T maxValue;
    private final boolean including;
    private final double doubleMin;
    private final double doubleMax;

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/util/Range$RangeDifference.class */
    public enum RangeDifference {
        LESS,
        FITS,
        GREATER
    }

    public Range(T minValue, T maxValue, boolean including) {
        if (minValue == null) {
            throw new NullPointerException("min");
        }
        if (maxValue == null) {
            throw new NullPointerException("max");
        }
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.doubleMin = minValue.doubleValue();
        this.doubleMax = maxValue.doubleValue();
        if (this.doubleMin >= this.doubleMax) {
            throw new IllegalArgumentException("min >= max");
        }
        this.including = including;
    }

    public Range(T minValue, T maxValue) {
        this(minValue, maxValue, true);
    }

    public T getMinValue() {
        return this.minValue;
    }

    public T getMaxValue() {
        return this.maxValue;
    }

    public boolean getIncluding() {
        return this.including;
    }

    public RangeDifference getDifference(T value) {
        if (value == null) {
            throw new NullPointerException("value");
        }
        double doubleValue = value.doubleValue();
        double min = doubleValue - this.doubleMin;
        if (min == 0.0d) {
            return this.including ? RangeDifference.FITS : RangeDifference.LESS;
        } else if (min < 0.0d) {
            return RangeDifference.LESS;
        } else {
            double max = doubleValue - this.doubleMax;
            if (max == 0.0d) {
                return this.including ? RangeDifference.FITS : RangeDifference.GREATER;
            } else if (max > 0.0d) {
                return RangeDifference.GREATER;
            } else {
                return RangeDifference.FITS;
            }
        }
    }

    public boolean fits(T value) {
        return getDifference(value) == RangeDifference.FITS;
    }
}
