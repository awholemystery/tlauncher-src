package org.tlauncher.util;

import ch.qos.logback.core.CoreConstants;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.List;
import java.util.Random;
import org.apache.commons.lang3.StringUtils;
import org.tlauncher.exceptions.ParseException;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/util/StringUtil.class */
public class StringUtil {
    public static final String LINK_PATTERN = "<a style='color:#585858;' href='link'>link</a>";

    public static String addSlashes(String str, EscapeGroup group) {
        char[] chars;
        if (str == null) {
            return CoreConstants.EMPTY_STRING;
        }
        StringBuilder s = new StringBuilder(str);
        int i = 0;
        while (i < s.length()) {
            char curChar = s.charAt(i);
            for (char c : group.getChars()) {
                if (curChar == c) {
                    int i2 = i;
                    i++;
                    s.insert(i2, '\\');
                }
            }
            i++;
        }
        return s.toString();
    }

    public static String[] addSlashes(String[] str, EscapeGroup group) {
        if (str == null) {
            return null;
        }
        int len = str.length;
        String[] ret = new String[len];
        for (int i = 0; i < len; i++) {
            ret[i] = addSlashes(str[i], group);
        }
        return ret;
    }

    public static String iconv(String inChar, String outChar, String str) {
        Charset in = Charset.forName(inChar);
        Charset out = Charset.forName(outChar);
        CharsetDecoder decoder = in.newDecoder();
        CharsetEncoder encoder = out.newEncoder();
        try {
            ByteBuffer bbuf = encoder.encode(CharBuffer.wrap(str));
            CharBuffer cbuf = decoder.decode(bbuf);
            return cbuf.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean parseBoolean(String b) throws ParseException {
        if (b == null) {
            throw new ParseException("String cannot be NULL!");
        }
        if (b.equalsIgnoreCase("true")) {
            return true;
        }
        if (b.equalsIgnoreCase("false")) {
            return false;
        }
        throw new ParseException("Cannot parse value (" + b + ")!");
    }

    public static int countLines(String str) {
        if (str == null || str.length() == 0) {
            return 0;
        }
        int lines = 1;
        int len = str.length();
        int pos = 0;
        while (pos < len) {
            char c = str.charAt(pos);
            if (c == '\r') {
                lines++;
                if (pos + 1 < len && str.charAt(pos + 1) == '\n') {
                    pos++;
                }
            } else if (c == '\n') {
                lines++;
            }
            pos++;
        }
        return lines;
    }

    public static char lastChar(String str) {
        if (str == null) {
            throw new NullPointerException();
        }
        int len = str.length();
        if (len == 0) {
            return (char) 0;
        }
        if (len == 1) {
            return str.charAt(0);
        }
        return str.charAt(len - 1);
    }

    public static String randomizeWord(String str, boolean softly) {
        if (str == null) {
            return null;
        }
        int len = str.length();
        if (len < 4) {
            return str;
        }
        boolean[] reversedFlag = new boolean[len];
        if (softly) {
            reversedFlag[0] = true;
        }
        boolean chosenLastLetter = !softly;
        char[] chars = str.toCharArray();
        for (int i = len - 1; i > -1; i--) {
            char curChar = chars[i];
            int charType = Character.getType(curChar);
            boolean canBeReversed = charType == 1 || charType == 2;
            int i2 = i;
            reversedFlag[i2] = reversedFlag[i2] | (!canBeReversed);
            if (canBeReversed && !chosenLastLetter) {
                reversedFlag[i] = true;
                chosenLastLetter = true;
            }
        }
        for (int i3 = 0; i3 < len; i3++) {
            if (!reversedFlag[i3]) {
                int newPos = i3;
                int tries = 0;
                while (true) {
                    if (tries >= 3) {
                        break;
                    }
                    tries++;
                    newPos = new Random().nextInt(len);
                    if (!reversedFlag[newPos]) {
                        tries = 10;
                        break;
                    }
                }
                if (tries == 10) {
                    char curChar2 = chars[i3];
                    char replaceChar = chars[newPos];
                    chars[i3] = replaceChar;
                    chars[newPos] = curChar2;
                    reversedFlag[i3] = true;
                    reversedFlag[newPos] = true;
                }
            }
        }
        return new String(chars);
    }

    public static String randomizeWord(String str) {
        return randomizeWord(str, true);
    }

    public static String randomize(String str, boolean softly) {
        if (str == null) {
            return null;
        }
        if (str.isEmpty()) {
            return str;
        }
        String[] lines = str.split("\n");
        StringBuilder lineBuilder = new StringBuilder();
        boolean isFirstLine = true;
        for (String line : lines) {
            String[] words = line.split(" ");
            StringBuilder wordBuilder = new StringBuilder();
            boolean isFirstWord = true;
            for (String word : words) {
                if (isFirstWord) {
                    isFirstWord = false;
                } else {
                    wordBuilder.append(' ');
                }
                wordBuilder.append(randomizeWord(word));
            }
            if (isFirstLine) {
                isFirstLine = false;
            } else {
                lineBuilder.append('\n');
            }
            lineBuilder.append((CharSequence) wordBuilder);
        }
        return lineBuilder.toString();
    }

    public static String randomize(String str) {
        return randomize(str, true);
    }

    public static boolean isHTML(char[] s) {
        if (s != null && s.length >= 6 && s[0] == '<' && s[5] == '>') {
            String tag = new String(s, 1, 4);
            return tag.equalsIgnoreCase("html");
        }
        return false;
    }

    public static String wrap(char[] s, int maxChars, boolean rudeBreaking, boolean detectHTML) {
        if (s == null) {
            throw new NullPointerException("sequence");
        }
        if (maxChars < 1) {
            throw new IllegalArgumentException("maxChars < 1");
        }
        boolean detectHTML2 = detectHTML && isHTML(s);
        String lineBreak = detectHTML2 ? "<br />" : "\n";
        StringBuilder builder = new StringBuilder();
        int len = s.length;
        int remaining = maxChars;
        boolean tagDetecting = false;
        boolean ignoreCurrent = false;
        for (int x = 0; x < len; x++) {
            char current = s[x];
            if (current == '<' && detectHTML2) {
                tagDetecting = true;
                ignoreCurrent = true;
            } else if (tagDetecting) {
                if (current == '>') {
                    tagDetecting = false;
                }
                ignoreCurrent = true;
            }
            if (ignoreCurrent) {
                ignoreCurrent = false;
                builder.append(current);
            } else {
                remaining--;
                if (s[x] == '\n' || (remaining < 1 && current == ' ')) {
                    remaining = maxChars;
                    builder.append(lineBreak);
                } else {
                    if (lookForward(s, x, lineBreak)) {
                        remaining = maxChars;
                    }
                    builder.append(current);
                    if (remaining <= 0 && rudeBreaking) {
                        remaining = maxChars;
                        builder.append(lineBreak);
                    }
                }
            }
        }
        return builder.toString();
    }

    private static boolean lookForward(char[] c, int caret, CharSequence search) {
        if (c == null) {
            throw new NullPointerException("char array");
        }
        if (caret < 0) {
            throw new IllegalArgumentException("caret < 0");
        }
        if (caret >= c.length) {
            return false;
        }
        int length = search.length();
        int available = c.length - caret;
        if (length < available) {
            return false;
        }
        for (int i = 0; i < length; i++) {
            if (c[caret + i] != search.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    public static String wrap(String s, int maxChars, boolean rudeBreaking, boolean detectHTML) {
        return wrap(s.toCharArray(), maxChars, rudeBreaking, detectHTML);
    }

    public static String wrap(char[] s, int maxChars, boolean rudeBreaking) {
        return wrap(s, maxChars, rudeBreaking, true);
    }

    public static String wrap(char[] s, int maxChars) {
        return wrap(s, maxChars, false);
    }

    public static String wrap(String s, int maxChars) {
        return wrap(s.toCharArray(), maxChars);
    }

    public static String cut(String string, int max) {
        if (string == null) {
            return null;
        }
        int len = string.length();
        if (len <= max) {
            return string;
        }
        String[] words = string.split(" ");
        String ret = CoreConstants.EMPTY_STRING;
        int remaining = max + 1;
        int x = 0;
        while (true) {
            if (x >= words.length) {
                break;
            }
            String curword = words[x];
            int curlen = curword.length();
            if (curlen < remaining) {
                ret = ret + " " + curword;
                remaining -= curlen + 1;
                x++;
            } else if (x == 0) {
                ret = ret + " " + curword.substring(0, remaining - 1);
            }
        }
        if (ret.length() == 0) {
            return CoreConstants.EMPTY_STRING;
        }
        return ret.substring(1) + "...";
    }

    public static String requireNotBlank(String s, String name) {
        if (s == null) {
            throw new NullPointerException(name);
        }
        if (StringUtils.isBlank(s)) {
            throw new IllegalArgumentException(name);
        }
        return s;
    }

    public static String getLink(String link) {
        return LINK_PATTERN.replaceAll("link", link);
    }

    public static String convertListToString(String c, List<String> l) {
        StringBuilder b = new StringBuilder();
        for (String string : l) {
            b.append(string).append(c);
        }
        return b.toString();
    }

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/util/StringUtil$EscapeGroup.class */
    public enum EscapeGroup {
        DOUBLE_QUOTE('\"'),
        COMMAND(DOUBLE_QUOTE, '\'', ' '),
        REGEXP(COMMAND, '/', '\\', '?', '*', '+', '[', ']', ':', '{', '}', '(', ')');
        
        private final char[] chars;

        EscapeGroup(char... symbols) {
            this.chars = symbols;
        }

        EscapeGroup(EscapeGroup extend, char... symbols) {
            int len = extend.chars.length + symbols.length;
            this.chars = new char[len];
            int x = 0;
            while (x < extend.chars.length) {
                this.chars[x] = extend.chars[x];
                x++;
            }
            System.arraycopy(symbols, 0, this.chars, x, symbols.length);
        }

        public char[] getChars() {
            return this.chars;
        }
    }
}
