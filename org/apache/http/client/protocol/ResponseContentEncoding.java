package org.apache.http.client.protocol;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.zip.GZIPInputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.annotation.Immutable;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.DecompressingEntity;
import org.apache.http.client.entity.DeflateInputStream;
import org.apache.http.client.entity.InputStreamFactory;
import org.apache.http.config.Lookup;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

@Immutable
/* loaded from: TLauncher-2.876.jar:org/apache/http/client/protocol/ResponseContentEncoding.class */
public class ResponseContentEncoding implements HttpResponseInterceptor {
    public static final String UNCOMPRESSED = "http.client.response.uncompressed";
    private static final InputStreamFactory GZIP = new InputStreamFactory() { // from class: org.apache.http.client.protocol.ResponseContentEncoding.1
        @Override // org.apache.http.client.entity.InputStreamFactory
        public InputStream create(InputStream instream) throws IOException {
            return new GZIPInputStream(instream);
        }
    };
    private static final InputStreamFactory DEFLATE = new InputStreamFactory() { // from class: org.apache.http.client.protocol.ResponseContentEncoding.2
        @Override // org.apache.http.client.entity.InputStreamFactory
        public InputStream create(InputStream instream) throws IOException {
            return new DeflateInputStream(instream);
        }
    };
    private final Lookup<InputStreamFactory> decoderRegistry;
    private final boolean ignoreUnknown;

    public ResponseContentEncoding(Lookup<InputStreamFactory> decoderRegistry, boolean ignoreUnknown) {
        this.decoderRegistry = decoderRegistry != null ? decoderRegistry : RegistryBuilder.create().register("gzip", GZIP).register("x-gzip", GZIP).register(CompressorStreamFactory.DEFLATE, DEFLATE).build();
        this.ignoreUnknown = ignoreUnknown;
    }

    public ResponseContentEncoding(boolean ignoreUnknown) {
        this(null, ignoreUnknown);
    }

    public ResponseContentEncoding(Lookup<InputStreamFactory> decoderRegistry) {
        this(decoderRegistry, true);
    }

    public ResponseContentEncoding() {
        this((Lookup<InputStreamFactory>) null);
    }

    @Override // org.apache.http.HttpResponseInterceptor
    public void process(HttpResponse response, HttpContext context) throws HttpException, IOException {
        Header ceheader;
        HttpEntity entity = response.getEntity();
        HttpClientContext clientContext = HttpClientContext.adapt(context);
        RequestConfig requestConfig = clientContext.getRequestConfig();
        if (requestConfig.isContentCompressionEnabled() && entity != null && entity.getContentLength() != 0 && (ceheader = entity.getContentEncoding()) != null) {
            HeaderElement[] codecs = ceheader.getElements();
            for (HeaderElement codec : codecs) {
                String codecname = codec.getName().toLowerCase(Locale.ROOT);
                InputStreamFactory decoderFactory = this.decoderRegistry.lookup(codecname);
                if (decoderFactory != null) {
                    response.setEntity(new DecompressingEntity(response.getEntity(), decoderFactory));
                    response.removeHeaders("Content-Length");
                    response.removeHeaders("Content-Encoding");
                    response.removeHeaders(HttpHeaders.CONTENT_MD5);
                } else if (!HTTP.IDENTITY_CODING.equals(codecname) && !this.ignoreUnknown) {
                    throw new HttpException("Unsupported Content-Encoding: " + codec.getName());
                }
            }
        }
    }
}
