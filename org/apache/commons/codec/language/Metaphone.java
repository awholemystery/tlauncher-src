package org.apache.commons.codec.language;

import ch.qos.logback.core.CoreConstants;
import java.util.Locale;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.compress.archivers.tar.TarConstants;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/codec/language/Metaphone.class */
public class Metaphone implements StringEncoder {
    private static final String VOWELS = "AEIOU";
    private static final String FRONTV = "EIY";
    private static final String VARSON = "CSPTG";
    private int maxCodeLen = 4;

    public String metaphone(String txt) {
        boolean hard;
        if (txt == null || txt.length() == 0) {
            return CoreConstants.EMPTY_STRING;
        }
        if (txt.length() == 1) {
            return txt.toUpperCase(Locale.ENGLISH);
        }
        char[] inwd = txt.toUpperCase(Locale.ENGLISH).toCharArray();
        StringBuilder local = new StringBuilder(40);
        StringBuilder code = new StringBuilder(10);
        switch (inwd[0]) {
            case 'A':
                if (inwd[1] == 'E') {
                    local.append(inwd, 1, inwd.length - 1);
                    break;
                } else {
                    local.append(inwd);
                    break;
                }
            case 'G':
            case TarConstants.LF_GNUTYPE_LONGLINK /* 75 */:
            case 'P':
                if (inwd[1] == 'N') {
                    local.append(inwd, 1, inwd.length - 1);
                    break;
                } else {
                    local.append(inwd);
                    break;
                }
            case 'W':
                if (inwd[1] == 'R') {
                    local.append(inwd, 1, inwd.length - 1);
                    break;
                } else if (inwd[1] == 'H') {
                    local.append(inwd, 1, inwd.length - 1);
                    local.setCharAt(0, 'W');
                    break;
                } else {
                    local.append(inwd);
                    break;
                }
            case 'X':
                inwd[0] = 'S';
                local.append(inwd);
                break;
            default:
                local.append(inwd);
                break;
        }
        int wdsz = local.length();
        int n = 0;
        while (code.length() < getMaxCodeLen() && n < wdsz) {
            char symb = local.charAt(n);
            if (symb != 'C' && isPreviousChar(local, n, symb)) {
                n++;
            } else {
                switch (symb) {
                    case 'A':
                    case 'E':
                    case 'I':
                    case 'O':
                    case 'U':
                        if (n == 0) {
                            code.append(symb);
                            break;
                        }
                        break;
                    case 'B':
                        if (!isPreviousChar(local, n, 'M') || !isLastChar(wdsz, n)) {
                            code.append(symb);
                            break;
                        }
                        break;
                    case 'C':
                        if (!isPreviousChar(local, n, 'S') || isLastChar(wdsz, n) || FRONTV.indexOf(local.charAt(n + 1)) < 0) {
                            if (regionMatch(local, n, "CIA")) {
                                code.append('X');
                                break;
                            } else if (!isLastChar(wdsz, n) && FRONTV.indexOf(local.charAt(n + 1)) >= 0) {
                                code.append('S');
                                break;
                            } else if (isPreviousChar(local, n, 'S') && isNextChar(local, n, 'H')) {
                                code.append('K');
                                break;
                            } else if (isNextChar(local, n, 'H')) {
                                if (n == 0 && wdsz >= 3 && isVowel(local, 2)) {
                                    code.append('K');
                                    break;
                                } else {
                                    code.append('X');
                                    break;
                                }
                            } else {
                                code.append('K');
                                break;
                            }
                        }
                        break;
                    case 'D':
                        if (!isLastChar(wdsz, n + 1) && isNextChar(local, n, 'G') && FRONTV.indexOf(local.charAt(n + 2)) >= 0) {
                            code.append('J');
                            n += 2;
                            break;
                        } else {
                            code.append('T');
                            break;
                        }
                        break;
                    case CoreConstants.OOS_RESET_FREQUENCY /* 70 */:
                    case 'J':
                    case 'L':
                    case TarConstants.LF_MULTIVOLUME /* 77 */:
                    case 'N':
                    case 'R':
                        code.append(symb);
                        break;
                    case 'G':
                        if ((!isLastChar(wdsz, n + 1) || !isNextChar(local, n, 'H')) && ((isLastChar(wdsz, n + 1) || !isNextChar(local, n, 'H') || isVowel(local, n + 2)) && (n <= 0 || (!regionMatch(local, n, "GN") && !regionMatch(local, n, "GNED"))))) {
                            if (isPreviousChar(local, n, 'G')) {
                                hard = true;
                            } else {
                                hard = false;
                            }
                            if (!isLastChar(wdsz, n) && FRONTV.indexOf(local.charAt(n + 1)) >= 0 && !hard) {
                                code.append('J');
                                break;
                            } else {
                                code.append('K');
                                break;
                            }
                        }
                        break;
                    case 'H':
                        if (!isLastChar(wdsz, n) && ((n <= 0 || VARSON.indexOf(local.charAt(n - 1)) < 0) && isVowel(local, n + 1))) {
                            code.append('H');
                            break;
                        }
                        break;
                    case TarConstants.LF_GNUTYPE_LONGLINK /* 75 */:
                        if (n > 0) {
                            if (!isPreviousChar(local, n, 'C')) {
                                code.append(symb);
                                break;
                            }
                        } else {
                            code.append(symb);
                            break;
                        }
                        break;
                    case 'P':
                        if (isNextChar(local, n, 'H')) {
                            code.append('F');
                            break;
                        } else {
                            code.append(symb);
                            break;
                        }
                    case 'Q':
                        code.append('K');
                        break;
                    case TarConstants.LF_GNUTYPE_SPARSE /* 83 */:
                        if (regionMatch(local, n, "SH") || regionMatch(local, n, "SIO") || regionMatch(local, n, "SIA")) {
                            code.append('X');
                            break;
                        } else {
                            code.append('S');
                            break;
                        }
                    case 'T':
                        if (regionMatch(local, n, "TIA") || regionMatch(local, n, "TIO")) {
                            code.append('X');
                            break;
                        } else if (!regionMatch(local, n, "TCH")) {
                            if (regionMatch(local, n, "TH")) {
                                code.append('0');
                                break;
                            } else {
                                code.append('T');
                                break;
                            }
                        }
                        break;
                    case 'V':
                        code.append('F');
                        break;
                    case 'W':
                    case 'Y':
                        if (!isLastChar(wdsz, n) && isVowel(local, n + 1)) {
                            code.append(symb);
                            break;
                        }
                        break;
                    case 'X':
                        code.append('K');
                        code.append('S');
                        break;
                    case 'Z':
                        code.append('S');
                        break;
                }
                n++;
            }
            if (code.length() > getMaxCodeLen()) {
                code.setLength(getMaxCodeLen());
            }
        }
        return code.toString();
    }

    private boolean isVowel(StringBuilder string, int index) {
        return VOWELS.indexOf(string.charAt(index)) >= 0;
    }

    private boolean isPreviousChar(StringBuilder string, int index, char c) {
        boolean matches = false;
        if (index > 0 && index < string.length()) {
            matches = string.charAt(index - 1) == c;
        }
        return matches;
    }

    private boolean isNextChar(StringBuilder string, int index, char c) {
        boolean matches = false;
        if (index >= 0 && index < string.length() - 1) {
            matches = string.charAt(index + 1) == c;
        }
        return matches;
    }

    private boolean regionMatch(StringBuilder string, int index, String test) {
        boolean matches = false;
        if (index >= 0 && (index + test.length()) - 1 < string.length()) {
            String substring = string.substring(index, index + test.length());
            matches = substring.equals(test);
        }
        return matches;
    }

    private boolean isLastChar(int wdsz, int n) {
        return n + 1 == wdsz;
    }

    @Override // org.apache.commons.codec.Encoder
    public Object encode(Object obj) throws EncoderException {
        if (!(obj instanceof String)) {
            throw new EncoderException("Parameter supplied to Metaphone encode is not of type java.lang.String");
        }
        return metaphone((String) obj);
    }

    @Override // org.apache.commons.codec.StringEncoder
    public String encode(String str) {
        return metaphone(str);
    }

    public boolean isMetaphoneEqual(String str1, String str2) {
        return metaphone(str1).equals(metaphone(str2));
    }

    public int getMaxCodeLen() {
        return this.maxCodeLen;
    }

    public void setMaxCodeLen(int maxCodeLen) {
        this.maxCodeLen = maxCodeLen;
    }
}
