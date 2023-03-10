package org.apache.log4j.pattern;

import java.util.ArrayList;
import java.util.List;

/* loaded from: TLauncher-2.876.jar:org/apache/log4j/pattern/NameAbbreviator.class */
public abstract class NameAbbreviator {
    private static final NameAbbreviator DEFAULT = new NOPAbbreviator();

    public abstract void abbreviate(int i, StringBuffer stringBuffer);

    public static NameAbbreviator getAbbreviator(String pattern) {
        int charCount;
        if (pattern.length() > 0) {
            String trimmed = pattern.trim();
            if (trimmed.length() == 0) {
                return DEFAULT;
            }
            int i = 0;
            if (trimmed.length() > 0) {
                if (trimmed.charAt(0) == '-') {
                    i = 0 + 1;
                }
                while (i < trimmed.length() && trimmed.charAt(i) >= '0' && trimmed.charAt(i) <= '9') {
                    i++;
                }
            }
            if (i == trimmed.length()) {
                int elements = Integer.parseInt(trimmed);
                if (elements >= 0) {
                    return new MaxElementAbbreviator(elements);
                }
                return new DropElementAbbreviator(-elements);
            }
            ArrayList fragments = new ArrayList(5);
            int pos = 0;
            while (pos < trimmed.length() && pos >= 0) {
                int ellipsisPos = pos;
                if (trimmed.charAt(pos) == '*') {
                    charCount = Integer.MAX_VALUE;
                    ellipsisPos++;
                } else if (trimmed.charAt(pos) >= '0' && trimmed.charAt(pos) <= '9') {
                    charCount = trimmed.charAt(pos) - '0';
                    ellipsisPos++;
                } else {
                    charCount = 0;
                }
                char ellipsis = 0;
                if (ellipsisPos < trimmed.length()) {
                    ellipsis = trimmed.charAt(ellipsisPos);
                    if (ellipsis == '.') {
                        ellipsis = 0;
                    }
                }
                fragments.add(new PatternAbbreviatorFragment(charCount, ellipsis));
                int pos2 = trimmed.indexOf(".", pos);
                if (pos2 == -1) {
                    break;
                }
                pos = pos2 + 1;
            }
            return new PatternAbbreviator(fragments);
        }
        return DEFAULT;
    }

    public static NameAbbreviator getDefaultAbbreviator() {
        return DEFAULT;
    }

    /* loaded from: TLauncher-2.876.jar:org/apache/log4j/pattern/NameAbbreviator$NOPAbbreviator.class */
    private static class NOPAbbreviator extends NameAbbreviator {
        @Override // org.apache.log4j.pattern.NameAbbreviator
        public void abbreviate(int nameStart, StringBuffer buf) {
        }
    }

    /* loaded from: TLauncher-2.876.jar:org/apache/log4j/pattern/NameAbbreviator$MaxElementAbbreviator.class */
    private static class MaxElementAbbreviator extends NameAbbreviator {
        private final int count;

        public MaxElementAbbreviator(int count) {
            this.count = count;
        }

        @Override // org.apache.log4j.pattern.NameAbbreviator
        public void abbreviate(int nameStart, StringBuffer buf) {
            int end = buf.length() - 1;
            String bufString = buf.toString();
            for (int i = this.count; i > 0; i--) {
                end = bufString.lastIndexOf(".", end - 1);
                if (end == -1 || end < nameStart) {
                    return;
                }
            }
            buf.delete(nameStart, end + 1);
        }
    }

    /* loaded from: TLauncher-2.876.jar:org/apache/log4j/pattern/NameAbbreviator$DropElementAbbreviator.class */
    private static class DropElementAbbreviator extends NameAbbreviator {
        private final int count;

        public DropElementAbbreviator(int count) {
            this.count = count;
        }

        @Override // org.apache.log4j.pattern.NameAbbreviator
        public void abbreviate(int nameStart, StringBuffer buf) {
            int i = this.count;
            int indexOf = buf.indexOf(".", nameStart);
            while (true) {
                int pos = indexOf;
                if (pos != -1) {
                    i--;
                    if (i != 0) {
                        indexOf = buf.indexOf(".", pos + 1);
                    } else {
                        buf.delete(nameStart, pos + 1);
                        return;
                    }
                } else {
                    return;
                }
            }
        }
    }

    /* loaded from: TLauncher-2.876.jar:org/apache/log4j/pattern/NameAbbreviator$PatternAbbreviatorFragment.class */
    private static class PatternAbbreviatorFragment {
        private final int charCount;
        private final char ellipsis;

        public PatternAbbreviatorFragment(int charCount, char ellipsis) {
            this.charCount = charCount;
            this.ellipsis = ellipsis;
        }

        public int abbreviate(StringBuffer buf, int startPos) {
            int nextDot = buf.toString().indexOf(".", startPos);
            if (nextDot != -1) {
                if (nextDot - startPos > this.charCount) {
                    buf.delete(startPos + this.charCount, nextDot);
                    nextDot = startPos + this.charCount;
                    if (this.ellipsis != 0) {
                        buf.insert(nextDot, this.ellipsis);
                        nextDot++;
                    }
                }
                nextDot++;
            }
            return nextDot;
        }
    }

    /* loaded from: TLauncher-2.876.jar:org/apache/log4j/pattern/NameAbbreviator$PatternAbbreviator.class */
    private static class PatternAbbreviator extends NameAbbreviator {
        private final PatternAbbreviatorFragment[] fragments;

        public PatternAbbreviator(List fragments) {
            if (fragments.size() == 0) {
                throw new IllegalArgumentException("fragments must have at least one element");
            }
            this.fragments = new PatternAbbreviatorFragment[fragments.size()];
            fragments.toArray(this.fragments);
        }

        @Override // org.apache.log4j.pattern.NameAbbreviator
        public void abbreviate(int nameStart, StringBuffer buf) {
            int pos = nameStart;
            for (int i = 0; i < this.fragments.length - 1 && pos < buf.length(); i++) {
                pos = this.fragments[i].abbreviate(buf, pos);
            }
            PatternAbbreviatorFragment terminalFragment = this.fragments[this.fragments.length - 1];
            while (pos < buf.length() && pos >= 0) {
                pos = terminalFragment.abbreviate(buf, pos);
            }
        }
    }
}
