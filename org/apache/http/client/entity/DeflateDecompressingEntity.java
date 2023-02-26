package org.apache.http.client.entity;

import java.io.IOException;
import java.io.InputStream;
import org.apache.http.HttpEntity;

/* loaded from: TLauncher-2.876.jar:org/apache/http/client/entity/DeflateDecompressingEntity.class */
public class DeflateDecompressingEntity extends DecompressingEntity {
    public DeflateDecompressingEntity(HttpEntity entity) {
        super(entity, new InputStreamFactory() { // from class: org.apache.http.client.entity.DeflateDecompressingEntity.1
            @Override // org.apache.http.client.entity.InputStreamFactory
            public InputStream create(InputStream instream) throws IOException {
                return new DeflateInputStream(instream);
            }
        });
    }
}
