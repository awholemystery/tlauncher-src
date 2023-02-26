package org.tlauncher.tlauncher.minecraft.user;

import java.io.IOException;
import java.util.function.Function;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/minecraft/user/HttpClientRequester.class */
public class HttpClientRequester<A> implements Requester<A> {
    private final Function<A, Request> requestFactory;

    public HttpClientRequester(Function<A, Request> requestFactory) {
        this.requestFactory = requestFactory;
    }

    @Override // org.tlauncher.tlauncher.minecraft.user.Requester
    public String makeRequest(Logger logger, A argument) throws InvalidResponseException, IOException {
        Request request = this.requestFactory.apply(argument);
        logger.trace("Sending request: {}" + request);
        HttpResponse httpResponse = request.execute().returnResponse();
        logger.trace("Reading response");
        String response = EntityUtils.toString(httpResponse.getEntity());
        logger.trace("Response: {}" + response);
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        logger.trace("Status code: {}" + statusCode);
        if (statusCode >= 200 && statusCode <= 299) {
            return response;
        }
        throw new InvalidStatusCodeException(statusCode, response);
    }
}
