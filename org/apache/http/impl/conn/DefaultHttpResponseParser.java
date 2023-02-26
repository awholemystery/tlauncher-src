package org.apache.http.impl.conn;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseFactory;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.config.MessageConstraints;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.io.AbstractMessageParser;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.message.LineParser;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

@NotThreadSafe
/* loaded from: TLauncher-2.876.jar:org/apache/http/impl/conn/DefaultHttpResponseParser.class */
public class DefaultHttpResponseParser extends AbstractMessageParser<HttpResponse> {
    private final Log log;
    private final HttpResponseFactory responseFactory;
    private final CharArrayBuffer lineBuf;

    @Deprecated
    public DefaultHttpResponseParser(SessionInputBuffer buffer, LineParser parser, HttpResponseFactory responseFactory, HttpParams params) {
        super(buffer, parser, params);
        this.log = LogFactory.getLog(getClass());
        Args.notNull(responseFactory, "Response factory");
        this.responseFactory = responseFactory;
        this.lineBuf = new CharArrayBuffer(128);
    }

    public DefaultHttpResponseParser(SessionInputBuffer buffer, LineParser lineParser, HttpResponseFactory responseFactory, MessageConstraints constraints) {
        super(buffer, lineParser, constraints);
        this.log = LogFactory.getLog(getClass());
        this.responseFactory = responseFactory != null ? responseFactory : DefaultHttpResponseFactory.INSTANCE;
        this.lineBuf = new CharArrayBuffer(128);
    }

    public DefaultHttpResponseParser(SessionInputBuffer buffer, MessageConstraints constraints) {
        this(buffer, (LineParser) null, (HttpResponseFactory) null, constraints);
    }

    public DefaultHttpResponseParser(SessionInputBuffer buffer) {
        this(buffer, (LineParser) null, (HttpResponseFactory) null, MessageConstraints.DEFAULT);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't rename method to resolve collision */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x006a, code lost:
        throw new org.apache.http.ProtocolException("The server failed to respond with a valid HTTP response");
     */
    @Override // org.apache.http.impl.io.AbstractMessageParser
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public org.apache.http.HttpResponse parseHead(org.apache.http.io.SessionInputBuffer r6) throws java.io.IOException, org.apache.http.HttpException {
        /*
            r5 = this;
            r0 = 0
            r7 = r0
            r0 = 0
            r8 = r0
        L4:
            r0 = r5
            org.apache.http.util.CharArrayBuffer r0 = r0.lineBuf
            r0.clear()
            r0 = r6
            r1 = r5
            org.apache.http.util.CharArrayBuffer r1 = r1.lineBuf
            int r0 = r0.readLine(r1)
            r9 = r0
            r0 = r9
            r1 = -1
            if (r0 != r1) goto L2b
            r0 = r7
            if (r0 != 0) goto L2b
            org.apache.http.NoHttpResponseException r0 = new org.apache.http.NoHttpResponseException
            r1 = r0
            java.lang.String r2 = "The target server failed to respond"
            r1.<init>(r2)
            throw r0
        L2b:
            org.apache.http.message.ParserCursor r0 = new org.apache.http.message.ParserCursor
            r1 = r0
            r2 = 0
            r3 = r5
            org.apache.http.util.CharArrayBuffer r3 = r3.lineBuf
            int r3 = r3.length()
            r1.<init>(r2, r3)
            r8 = r0
            r0 = r5
            org.apache.http.message.LineParser r0 = r0.lineParser
            r1 = r5
            org.apache.http.util.CharArrayBuffer r1 = r1.lineBuf
            r2 = r8
            boolean r0 = r0.hasProtocolVersion(r1, r2)
            if (r0 == 0) goto L4f
            goto L9f
        L4f:
            r0 = r9
            r1 = -1
            if (r0 == r1) goto L61
            r0 = r5
            r1 = r5
            org.apache.http.util.CharArrayBuffer r1 = r1.lineBuf
            r2 = r7
            boolean r0 = r0.reject(r1, r2)
            if (r0 == 0) goto L6b
        L61:
            org.apache.http.ProtocolException r0 = new org.apache.http.ProtocolException
            r1 = r0
            java.lang.String r2 = "The server failed to respond with a valid HTTP response"
            r1.<init>(r2)
            throw r0
        L6b:
            r0 = r5
            org.apache.commons.logging.Log r0 = r0.log
            boolean r0 = r0.isDebugEnabled()
            if (r0 == 0) goto L99
            r0 = r5
            org.apache.commons.logging.Log r0 = r0.log
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r2 = r1
            r2.<init>()
            java.lang.String r2 = "Garbage in response: "
            java.lang.StringBuilder r1 = r1.append(r2)
            r2 = r5
            org.apache.http.util.CharArrayBuffer r2 = r2.lineBuf
            java.lang.String r2 = r2.toString()
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.String r1 = r1.toString()
            r0.debug(r1)
        L99:
            int r7 = r7 + 1
            goto L4
        L9f:
            r0 = r5
            org.apache.http.message.LineParser r0 = r0.lineParser
            r1 = r5
            org.apache.http.util.CharArrayBuffer r1 = r1.lineBuf
            r2 = r8
            org.apache.http.StatusLine r0 = r0.parseStatusLine(r1, r2)
            r9 = r0
            r0 = r5
            org.apache.http.HttpResponseFactory r0 = r0.responseFactory
            r1 = r9
            r2 = 0
            org.apache.http.HttpResponse r0 = r0.newHttpResponse(r1, r2)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.http.impl.conn.DefaultHttpResponseParser.parseHead(org.apache.http.io.SessionInputBuffer):org.apache.http.HttpResponse");
    }

    protected boolean reject(CharArrayBuffer line, int count) {
        return false;
    }
}
