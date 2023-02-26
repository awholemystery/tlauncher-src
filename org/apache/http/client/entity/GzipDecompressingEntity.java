package org.apache.http.client.entity;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import org.apache.http.HttpEntity;

/* loaded from: TLauncher-2.876.jar:org/apache/http/client/entity/GzipDecompressingEntity.class */
public class GzipDecompressingEntity extends DecompressingEntity {
    public GzipDecompressingEntity(HttpEntity entity) {
        super(entity, new InputStreamFactory() { // from class: org.apache.http.client.entity.GzipDecompressingEntity.1
            @Override // org.apache.http.client.entity.InputStreamFactory
            public InputStream create(InputStream instream) throws IOException {
                return new GZIPInputStream(instream);
            }
        });
    }
}
