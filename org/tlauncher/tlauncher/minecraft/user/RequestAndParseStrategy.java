package org.tlauncher.tlauncher.minecraft.user;

import java.io.IOException;
import org.apache.log4j.Logger;
import org.tlauncher.tlauncher.minecraft.exceptions.ParseException;
import org.tlauncher.tlauncher.minecraft.user.preq.Validatable;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/minecraft/user/RequestAndParseStrategy.class */
public abstract class RequestAndParseStrategy<A, V extends Validatable> {
    private final Logger logger;
    private final Requester<A> requester;
    private final Parser<V> parser;

    /* JADX INFO: Access modifiers changed from: protected */
    public RequestAndParseStrategy(Logger logger, Requester<A> requester, Parser<V> parser) {
        this.logger = logger;
        this.requester = requester;
        this.parser = parser;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public V requestAndParse(A argument) throws IOException, InvalidResponseException {
        String response = this.requester.makeRequest(this.logger, argument);
        try {
            return this.parser.parseResponse(this.logger, response);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new InvalidResponseException(response, e);
        }
    }
}
