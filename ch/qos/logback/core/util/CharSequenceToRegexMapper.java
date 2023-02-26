package ch.qos.logback.core.util;

import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.net.SyslogConstants;
import java.text.DateFormatSymbols;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;

/* loaded from: TLauncher-2.876.jar:ch/qos/logback/core/util/CharSequenceToRegexMapper.class */
class CharSequenceToRegexMapper {
    DateFormatSymbols symbols = DateFormatSymbols.getInstance();

    /* JADX INFO: Access modifiers changed from: package-private */
    public String toRegex(CharSequenceState css) {
        int occurrences = css.occurrences;
        char c = css.c;
        switch (css.c) {
            case CoreConstants.SINGLE_QUOTE_CHAR /* 39 */:
                if (occurrences == 1) {
                    return CoreConstants.EMPTY_STRING;
                }
                throw new IllegalStateException("Too many single quotes");
            case '(':
            case CoreConstants.RIGHT_PARENTHESIS_CHAR /* 41 */:
            case '*':
            case '+':
            case CoreConstants.COMMA_CHAR /* 44 */:
            case CoreConstants.DASH_CHAR /* 45 */:
            case IOUtils.DIR_SEPARATOR_UNIX /* 47 */:
            case '0':
            case TarConstants.LF_LINK /* 49 */:
            case '2':
            case TarConstants.LF_CHR /* 51 */:
            case TarConstants.LF_BLK /* 52 */:
            case TarConstants.LF_DIR /* 53 */:
            case TarConstants.LF_FIFO /* 54 */:
            case TarConstants.LF_CONTIG /* 55 */:
            case '8':
            case '9':
            case CoreConstants.COLON_CHAR /* 58 */:
            case ';':
            case '<':
            case '=':
            case '>':
            case '?':
            case '@':
            case 'A':
            case 'B':
            case 'C':
            case 'I':
            case 'J':
            case 'L':
            case 'N':
            case 'O':
            case 'P':
            case 'Q':
            case 'R':
            case 'T':
            case 'U':
            case 'V':
            case 'X':
            case 'Y':
            case '[':
            case ']':
            case '^':
            case '_':
            case '`':
            case 'b':
            case 'c':
            case HttpStatus.SC_SWITCHING_PROTOCOLS /* 101 */:
            case HttpStatus.SC_PROCESSING /* 102 */:
            case TarConstants.LF_PAX_GLOBAL_EXTENDED_HEADER /* 103 */:
            case 'i':
            case 'j':
            case 'l':
            case 'n':
            case 'o':
            case SyslogConstants.LOG_ALERT /* 112 */:
            case 'q':
            case 'r':
            case 't':
            case 'u':
            case 'v':
            case 'x':
            default:
                if (occurrences == 1) {
                    return CoreConstants.EMPTY_STRING + c;
                }
                return c + "{" + occurrences + "}";
            case '.':
                return "\\.";
            case 'D':
            case CoreConstants.OOS_RESET_FREQUENCY /* 70 */:
            case 'H':
            case TarConstants.LF_GNUTYPE_LONGLINK /* 75 */:
            case TarConstants.LF_GNUTYPE_SPARSE /* 83 */:
            case 'W':
            case 'd':
            case SyslogConstants.LOG_AUDIT /* 104 */:
            case 'k':
            case 'm':
            case 's':
            case 'w':
            case 'y':
                return number(occurrences);
            case 'E':
                if (occurrences >= 4) {
                    return getRegexForLongDaysOfTheWeek();
                }
                return getRegexForShortDaysOfTheWeek();
            case 'G':
            case 'z':
                return ".*";
            case TarConstants.LF_MULTIVOLUME /* 77 */:
                if (occurrences <= 2) {
                    return number(occurrences);
                }
                if (occurrences == 3) {
                    return getRegexForShortMonths();
                }
                return getRegexForLongMonths();
            case 'Z':
                return "(\\+|-)\\d{4}";
            case '\\':
                throw new IllegalStateException("Forward slashes are not allowed");
            case 'a':
                return getRegexForAmPms();
        }
    }

    private String number(int occurrences) {
        return "\\d{" + occurrences + "}";
    }

    private String getRegexForAmPms() {
        return symbolArrayToRegex(this.symbols.getAmPmStrings());
    }

    private String getRegexForLongDaysOfTheWeek() {
        return symbolArrayToRegex(this.symbols.getWeekdays());
    }

    private String getRegexForShortDaysOfTheWeek() {
        return symbolArrayToRegex(this.symbols.getShortWeekdays());
    }

    private String getRegexForLongMonths() {
        return symbolArrayToRegex(this.symbols.getMonths());
    }

    String getRegexForShortMonths() {
        return symbolArrayToRegex(this.symbols.getShortMonths());
    }

    private String symbolArrayToRegex(String[] symbolArray) {
        int[] minMax = findMinMaxLengthsInSymbols(symbolArray);
        return ".{" + minMax[0] + "," + minMax[1] + "}";
    }

    static int[] findMinMaxLengthsInSymbols(String[] symbols) {
        int min = Integer.MAX_VALUE;
        int max = 0;
        for (String symbol : symbols) {
            int len = symbol.length();
            if (len != 0) {
                min = Math.min(min, len);
                max = Math.max(max, len);
            }
        }
        return new int[]{min, max};
    }
}
