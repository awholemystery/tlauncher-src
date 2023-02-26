package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/codec/language/RefinedSoundex.class */
public class RefinedSoundex implements StringEncoder {
    private final char[] soundexMapping;
    public static final String US_ENGLISH_MAPPING_STRING = "01360240043788015936020505";
    private static final char[] US_ENGLISH_MAPPING = US_ENGLISH_MAPPING_STRING.toCharArray();
    public static final RefinedSoundex US_ENGLISH = new RefinedSoundex();

    public RefinedSoundex() {
        this.soundexMapping = US_ENGLISH_MAPPING;
    }

    public RefinedSoundex(char[] mapping) {
        this.soundexMapping = new char[mapping.length];
        System.arraycopy(mapping, 0, this.soundexMapping, 0, mapping.length);
    }

    public RefinedSoundex(String mapping) {
        this.soundexMapping = mapping.toCharArray();
    }

    public int difference(String s1, String s2) throws EncoderException {
        return SoundexUtils.difference(this, s1, s2);
    }

    @Override // org.apache.commons.codec.Encoder
    public Object encode(Object obj) throws EncoderException {
        if (!(obj instanceof String)) {
            throw new EncoderException("Parameter supplied to RefinedSoundex encode is not of type java.lang.String");
        }
        return soundex((String) obj);
    }

    @Override // org.apache.commons.codec.StringEncoder
    public String encode(String str) {
        return soundex(str);
    }

    char getMappingCode(char c) {
        if (!Character.isLetter(c)) {
            return (char) 0;
        }
        return this.soundexMapping[Character.toUpperCase(c) - 'A'];
    }

    public String soundex(String str) {
        if (str == null) {
            return null;
        }
        String str2 = SoundexUtils.clean(str);
        if (str2.length() == 0) {
            return str2;
        }
        StringBuilder sBuf = new StringBuilder();
        sBuf.append(str2.charAt(0));
        char last = '*';
        for (int i = 0; i < str2.length(); i++) {
            char current = getMappingCode(str2.charAt(i));
            if (current != last) {
                if (current != 0) {
                    sBuf.append(current);
                }
                last = current;
            }
        }
        return sBuf.toString();
    }
}
