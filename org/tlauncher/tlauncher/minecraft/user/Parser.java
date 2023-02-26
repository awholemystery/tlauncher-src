package org.tlauncher.tlauncher.minecraft.user;

import org.apache.log4j.Logger;
import org.tlauncher.tlauncher.minecraft.exceptions.ParseException;
import org.tlauncher.tlauncher.minecraft.user.preq.Validatable;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/minecraft/user/Parser.class */
public interface Parser<V extends Validatable> {
    V parseResponse(Logger logger, String str) throws ParseException;
}
