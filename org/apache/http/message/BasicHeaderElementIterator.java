package org.apache.http.message;

import java.util.NoSuchElementException;
import org.apache.http.FormattedHeader;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HeaderIterator;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

@NotThreadSafe
/* loaded from: TLauncher-2.876.jar:org/apache/http/message/BasicHeaderElementIterator.class */
public class BasicHeaderElementIterator implements HeaderElementIterator {
    private final HeaderIterator headerIt;
    private final HeaderValueParser parser;
    private HeaderElement currentElement;
    private CharArrayBuffer buffer;
    private ParserCursor cursor;

    public BasicHeaderElementIterator(HeaderIterator headerIterator, HeaderValueParser parser) {
        this.currentElement = null;
        this.buffer = null;
        this.cursor = null;
        this.headerIt = (HeaderIterator) Args.notNull(headerIterator, "Header iterator");
        this.parser = (HeaderValueParser) Args.notNull(parser, "Parser");
    }

    public BasicHeaderElementIterator(HeaderIterator headerIterator) {
        this(headerIterator, BasicHeaderValueParser.INSTANCE);
    }

    private void bufferHeaderValue() {
        this.cursor = null;
        this.buffer = null;
        while (this.headerIt.hasNext()) {
            Header h = this.headerIt.nextHeader();
            if (h instanceof FormattedHeader) {
                this.buffer = ((FormattedHeader) h).getBuffer();
                this.cursor = new ParserCursor(0, this.buffer.length());
                this.cursor.updatePos(((FormattedHeader) h).getValuePos());
                return;
            }
            String value = h.getValue();
            if (value != null) {
                this.buffer = new CharArrayBuffer(value.length());
                this.buffer.append(value);
                this.cursor = new ParserCursor(0, this.buffer.length());
                return;
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x0039  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void parseNextElement() {
        /*
            r4 = this;
        L0:
            r0 = r4
            org.apache.http.HeaderIterator r0 = r0.headerIt
            boolean r0 = r0.hasNext()
            if (r0 != 0) goto L13
            r0 = r4
            org.apache.http.message.ParserCursor r0 = r0.cursor
            if (r0 == 0) goto L80
        L13:
            r0 = r4
            org.apache.http.message.ParserCursor r0 = r0.cursor
            if (r0 == 0) goto L24
            r0 = r4
            org.apache.http.message.ParserCursor r0 = r0.cursor
            boolean r0 = r0.atEnd()
            if (r0 == 0) goto L28
        L24:
            r0 = r4
            r0.bufferHeaderValue()
        L28:
            r0 = r4
            org.apache.http.message.ParserCursor r0 = r0.cursor
            if (r0 == 0) goto L0
        L2f:
            r0 = r4
            org.apache.http.message.ParserCursor r0 = r0.cursor
            boolean r0 = r0.atEnd()
            if (r0 != 0) goto L69
            r0 = r4
            org.apache.http.message.HeaderValueParser r0 = r0.parser
            r1 = r4
            org.apache.http.util.CharArrayBuffer r1 = r1.buffer
            r2 = r4
            org.apache.http.message.ParserCursor r2 = r2.cursor
            org.apache.http.HeaderElement r0 = r0.parseHeaderElement(r1, r2)
            r5 = r0
            r0 = r5
            java.lang.String r0 = r0.getName()
            int r0 = r0.length()
            if (r0 != 0) goto L60
            r0 = r5
            java.lang.String r0 = r0.getValue()
            if (r0 == 0) goto L66
        L60:
            r0 = r4
            r1 = r5
            r0.currentElement = r1
            return
        L66:
            goto L2f
        L69:
            r0 = r4
            org.apache.http.message.ParserCursor r0 = r0.cursor
            boolean r0 = r0.atEnd()
            if (r0 == 0) goto L0
            r0 = r4
            r1 = 0
            r0.cursor = r1
            r0 = r4
            r1 = 0
            r0.buffer = r1
            goto L0
        L80:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.http.message.BasicHeaderElementIterator.parseNextElement():void");
    }

    @Override // org.apache.http.HeaderElementIterator, java.util.Iterator
    public boolean hasNext() {
        if (this.currentElement == null) {
            parseNextElement();
        }
        return this.currentElement != null;
    }

    @Override // org.apache.http.HeaderElementIterator
    public HeaderElement nextElement() throws NoSuchElementException {
        if (this.currentElement == null) {
            parseNextElement();
        }
        if (this.currentElement == null) {
            throw new NoSuchElementException("No more header elements available");
        }
        HeaderElement element = this.currentElement;
        this.currentElement = null;
        return element;
    }

    @Override // java.util.Iterator
    public final Object next() throws NoSuchElementException {
        return nextElement();
    }

    @Override // java.util.Iterator
    public void remove() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Remove not supported");
    }
}
