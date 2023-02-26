package org.apache.http.impl.auth;

import java.io.IOException;

@Deprecated
/* loaded from: TLauncher-2.876.jar:org/apache/http/impl/auth/SpnegoTokenGenerator.class */
public interface SpnegoTokenGenerator {
    byte[] generateSpnegoDERObject(byte[] bArr) throws IOException;
}
