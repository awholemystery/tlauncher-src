package org.tlauncher.tlauncher.minecraft.user;

import java.io.IOException;
import org.apache.log4j.Logger;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/minecraft/user/Requester.class */
public interface Requester<A> {
    String makeRequest(Logger logger, A a) throws InvalidResponseException, IOException;
}
