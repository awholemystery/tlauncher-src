package org.apache.commons.compress.compressors.lz77support;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/compressors/lz77support/Parameters.class */
public final class Parameters {
    public static final int TRUE_MIN_BACK_REFERENCE_LENGTH = 3;
    private final int windowSize;
    private final int minBackReferenceLength;
    private final int maxBackReferenceLength;
    private final int maxOffset;
    private final int maxLiteralLength;
    private final int niceBackReferenceLength;
    private final int maxCandidates;
    private final int lazyThreshold;
    private final boolean lazyMatching;

    public static Builder builder(int windowSize) {
        return new Builder(windowSize);
    }

    /* loaded from: TLauncher-2.876.jar:org/apache/commons/compress/compressors/lz77support/Parameters$Builder.class */
    public static class Builder {
        private final int windowSize;
        private int minBackReferenceLength;
        private int maxBackReferenceLength;
        private int maxOffset;
        private int maxLiteralLength;
        private Integer niceBackReferenceLength;
        private Integer maxCandidates;
        private Integer lazyThreshold;
        private Boolean lazyMatches;

        private Builder(int windowSize) {
            if (windowSize < 2 || !Parameters.isPowerOfTwo(windowSize)) {
                throw new IllegalArgumentException("windowSize must be a power of two");
            }
            this.windowSize = windowSize;
            this.minBackReferenceLength = 3;
            this.maxBackReferenceLength = windowSize - 1;
            this.maxOffset = windowSize - 1;
            this.maxLiteralLength = windowSize;
        }

        public Builder withMinBackReferenceLength(int minBackReferenceLength) {
            this.minBackReferenceLength = Math.max(3, minBackReferenceLength);
            if (this.windowSize < this.minBackReferenceLength) {
                throw new IllegalArgumentException("minBackReferenceLength can't be bigger than windowSize");
            }
            if (this.maxBackReferenceLength < this.minBackReferenceLength) {
                this.maxBackReferenceLength = this.minBackReferenceLength;
            }
            return this;
        }

        public Builder withMaxBackReferenceLength(int maxBackReferenceLength) {
            this.maxBackReferenceLength = maxBackReferenceLength < this.minBackReferenceLength ? this.minBackReferenceLength : Math.min(maxBackReferenceLength, this.windowSize - 1);
            return this;
        }

        public Builder withMaxOffset(int maxOffset) {
            this.maxOffset = maxOffset < 1 ? this.windowSize - 1 : Math.min(maxOffset, this.windowSize - 1);
            return this;
        }

        public Builder withMaxLiteralLength(int maxLiteralLength) {
            this.maxLiteralLength = maxLiteralLength < 1 ? this.windowSize : Math.min(maxLiteralLength, this.windowSize);
            return this;
        }

        public Builder withNiceBackReferenceLength(int niceLen) {
            this.niceBackReferenceLength = Integer.valueOf(niceLen);
            return this;
        }

        public Builder withMaxNumberOfCandidates(int maxCandidates) {
            this.maxCandidates = Integer.valueOf(maxCandidates);
            return this;
        }

        public Builder withLazyMatching(boolean lazy) {
            this.lazyMatches = Boolean.valueOf(lazy);
            return this;
        }

        public Builder withLazyThreshold(int threshold) {
            this.lazyThreshold = Integer.valueOf(threshold);
            return this;
        }

        public Builder tunedForSpeed() {
            this.niceBackReferenceLength = Integer.valueOf(Math.max(this.minBackReferenceLength, this.maxBackReferenceLength / 8));
            this.maxCandidates = Integer.valueOf(Math.max(32, this.windowSize / 1024));
            this.lazyMatches = false;
            this.lazyThreshold = Integer.valueOf(this.minBackReferenceLength);
            return this;
        }

        public Builder tunedForCompressionRatio() {
            Integer valueOf = Integer.valueOf(this.maxBackReferenceLength);
            this.lazyThreshold = valueOf;
            this.niceBackReferenceLength = valueOf;
            this.maxCandidates = Integer.valueOf(Math.max(32, this.windowSize / 16));
            this.lazyMatches = true;
            return this;
        }

        public Parameters build() {
            int niceLen = this.niceBackReferenceLength != null ? this.niceBackReferenceLength.intValue() : Math.max(this.minBackReferenceLength, this.maxBackReferenceLength / 2);
            int candidates = this.maxCandidates != null ? this.maxCandidates.intValue() : Math.max(256, this.windowSize / 128);
            boolean lazy = this.lazyMatches == null || this.lazyMatches.booleanValue();
            int threshold = lazy ? this.lazyThreshold != null ? this.lazyThreshold.intValue() : niceLen : this.minBackReferenceLength;
            return new Parameters(this.windowSize, this.minBackReferenceLength, this.maxBackReferenceLength, this.maxOffset, this.maxLiteralLength, niceLen, candidates, lazy, threshold);
        }
    }

    private Parameters(int windowSize, int minBackReferenceLength, int maxBackReferenceLength, int maxOffset, int maxLiteralLength, int niceBackReferenceLength, int maxCandidates, boolean lazyMatching, int lazyThreshold) {
        this.windowSize = windowSize;
        this.minBackReferenceLength = minBackReferenceLength;
        this.maxBackReferenceLength = maxBackReferenceLength;
        this.maxOffset = maxOffset;
        this.maxLiteralLength = maxLiteralLength;
        this.niceBackReferenceLength = niceBackReferenceLength;
        this.maxCandidates = maxCandidates;
        this.lazyMatching = lazyMatching;
        this.lazyThreshold = lazyThreshold;
    }

    public int getWindowSize() {
        return this.windowSize;
    }

    public int getMinBackReferenceLength() {
        return this.minBackReferenceLength;
    }

    public int getMaxBackReferenceLength() {
        return this.maxBackReferenceLength;
    }

    public int getMaxOffset() {
        return this.maxOffset;
    }

    public int getMaxLiteralLength() {
        return this.maxLiteralLength;
    }

    public int getNiceBackReferenceLength() {
        return this.niceBackReferenceLength;
    }

    public int getMaxCandidates() {
        return this.maxCandidates;
    }

    public boolean getLazyMatching() {
        return this.lazyMatching;
    }

    public int getLazyMatchingThreshold() {
        return this.lazyThreshold;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isPowerOfTwo(int x) {
        return (x & (x - 1)) == 0;
    }
}
