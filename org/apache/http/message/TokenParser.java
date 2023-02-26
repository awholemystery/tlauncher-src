package org.apache.http.message;

import java.util.BitSet;
import org.apache.http.annotation.Immutable;
import org.apache.http.util.CharArrayBuffer;

@Immutable
/* loaded from: TLauncher-2.876.jar:org/apache/http/message/TokenParser.class */
public class TokenParser {
    public static final char CR = '\r';
    public static final char LF = '\n';
    public static final char SP = ' ';
    public static final char HT = '\t';
    public static final char DQUOTE = '\"';
    public static final char ESCAPE = '\\';
    public static final TokenParser INSTANCE = new TokenParser();

    public static BitSet INIT_BITSET(int... b) {
        BitSet bitset = new BitSet();
        for (int aB : b) {
            bitset.set(aB);
        }
        return bitset;
    }

    public static boolean isWhitespace(char ch2) {
        return ch2 == ' ' || ch2 == '\t' || ch2 == '\r' || ch2 == '\n';
    }

    public String parseToken(CharArrayBuffer buf, ParserCursor cursor, BitSet delimiters) {
        StringBuilder dst = new StringBuilder();
        boolean z = false;
        while (true) {
            boolean whitespace = z;
            if (!cursor.atEnd()) {
                char current = buf.charAt(cursor.getPos());
                if (delimiters != null && delimiters.get(current)) {
                    break;
                } else if (isWhitespace(current)) {
                    skipWhiteSpace(buf, cursor);
                    z = true;
                } else {
                    if (whitespace && dst.length() > 0) {
                        dst.append(' ');
                    }
                    copyContent(buf, cursor, delimiters, dst);
                    z = false;
                }
            } else {
                break;
            }
        }
        return dst.toString();
    }

    public String parseValue(CharArrayBuffer buf, ParserCursor cursor, BitSet delimiters) {
        StringBuilder dst = new StringBuilder();
        boolean z = false;
        while (true) {
            boolean whitespace = z;
            if (!cursor.atEnd()) {
                char current = buf.charAt(cursor.getPos());
                if (delimiters != null && delimiters.get(current)) {
                    break;
                } else if (isWhitespace(current)) {
                    skipWhiteSpace(buf, cursor);
                    z = true;
                } else if (current == '\"') {
                    if (whitespace && dst.length() > 0) {
                        dst.append(' ');
                    }
                    copyQuotedContent(buf, cursor, dst);
                    z = false;
                } else {
                    if (whitespace && dst.length() > 0) {
                        dst.append(' ');
                    }
                    copyUnquotedContent(buf, cursor, delimiters, dst);
                    z = false;
                }
            } else {
                break;
            }
        }
        return dst.toString();
    }

    public void skipWhiteSpace(CharArrayBuffer buf, ParserCursor cursor) {
        int pos = cursor.getPos();
        int indexFrom = cursor.getPos();
        int indexTo = cursor.getUpperBound();
        for (int i = indexFrom; i < indexTo; i++) {
            char current = buf.charAt(i);
            if (!isWhitespace(current)) {
                break;
            }
            pos++;
        }
        cursor.updatePos(pos);
    }

    public void copyContent(CharArrayBuffer buf, ParserCursor cursor, BitSet delimiters, StringBuilder dst) {
        int pos = cursor.getPos();
        int indexFrom = cursor.getPos();
        int indexTo = cursor.getUpperBound();
        for (int i = indexFrom; i < indexTo; i++) {
            char current = buf.charAt(i);
            if ((delimiters != null && delimiters.get(current)) || isWhitespace(current)) {
                break;
            }
            pos++;
            dst.append(current);
        }
        cursor.updatePos(pos);
    }

    public void copyUnquotedContent(CharArrayBuffer buf, ParserCursor cursor, BitSet delimiters, StringBuilder dst) {
        int pos = cursor.getPos();
        int indexFrom = cursor.getPos();
        int indexTo = cursor.getUpperBound();
        for (int i = indexFrom; i < indexTo; i++) {
            char current = buf.charAt(i);
            if ((delimiters != null && delimiters.get(current)) || isWhitespace(current) || current == '\"') {
                break;
            }
            pos++;
            dst.append(current);
        }
        cursor.updatePos(pos);
    }

    public void copyQuotedContent(CharArrayBuffer buf, ParserCursor cursor, StringBuilder dst) {
        if (cursor.atEnd()) {
            return;
        }
        int pos = cursor.getPos();
        int indexFrom = cursor.getPos();
        int indexTo = cursor.getUpperBound();
        if (buf.charAt(pos) != '\"') {
            return;
        }
        int pos2 = pos + 1;
        boolean escaped = false;
        int i = indexFrom + 1;
        while (true) {
            if (i >= indexTo) {
                break;
            }
            char current = buf.charAt(i);
            if (escaped) {
                if (current != '\"' && current != '\\') {
                    dst.append('\\');
                }
                dst.append(current);
                escaped = false;
            } else if (current == '\"') {
                pos2++;
                break;
            } else if (current == '\\') {
                escaped = true;
            } else if (current != '\r' && current != '\n') {
                dst.append(current);
            }
            i++;
            pos2++;
        }
        cursor.updatePos(pos2);
    }
}
