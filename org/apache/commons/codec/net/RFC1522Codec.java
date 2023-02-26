package org.apache.commons.codec.net;

import ch.qos.logback.core.CoreConstants;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.binary.StringUtils;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/codec/net/RFC1522Codec.class */
abstract class RFC1522Codec {
    protected static final char SEP = '?';
    protected static final String POSTFIX = "?=";
    protected static final String PREFIX = "=?";

    protected abstract String getEncoding();

    protected abstract byte[] doEncoding(byte[] bArr) throws EncoderException;

    protected abstract byte[] doDecoding(byte[] bArr) throws DecoderException;

    /* JADX INFO: Access modifiers changed from: protected */
    public String encodeText(String text, Charset charset) throws EncoderException {
        if (text == null) {
            return null;
        }
        StringBuilder buffer = new StringBuilder();
        buffer.append(PREFIX);
        buffer.append(charset);
        buffer.append('?');
        buffer.append(getEncoding());
        buffer.append('?');
        byte[] rawData = doEncoding(text.getBytes(charset));
        buffer.append(StringUtils.newStringUsAscii(rawData));
        buffer.append(POSTFIX);
        return buffer.toString();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String encodeText(String text, String charsetName) throws EncoderException, UnsupportedEncodingException {
        if (text == null) {
            return null;
        }
        return encodeText(text, Charset.forName(charsetName));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String decodeText(String text) throws DecoderException, UnsupportedEncodingException {
        if (text == null) {
            return null;
        }
        if (!text.startsWith(PREFIX) || !text.endsWith(POSTFIX)) {
            throw new DecoderException("RFC 1522 violation: malformed encoded content");
        }
        int terminator = text.length() - 2;
        int to = text.indexOf(SEP, 2);
        if (to == terminator) {
            throw new DecoderException("RFC 1522 violation: charset token not found");
        }
        String charset = text.substring(2, to);
        if (charset.equals(CoreConstants.EMPTY_STRING)) {
            throw new DecoderException("RFC 1522 violation: charset not specified");
        }
        int from = to + 1;
        int to2 = text.indexOf(SEP, from);
        if (to2 == terminator) {
            throw new DecoderException("RFC 1522 violation: encoding token not found");
        }
        String encoding = text.substring(from, to2);
        if (!getEncoding().equalsIgnoreCase(encoding)) {
            throw new DecoderException("This codec cannot decode " + encoding + " encoded content");
        }
        int from2 = to2 + 1;
        byte[] data = StringUtils.getBytesUsAscii(text.substring(from2, text.indexOf(SEP, from2)));
        return new String(doDecoding(data), charset);
    }
}
