package org.tlauncher.tlauncher.configuration;

import java.util.UUID;
import org.tlauncher.exceptions.ParseException;
import org.tlauncher.tlauncher.configuration.enums.ActionOnLaunch;
import org.tlauncher.tlauncher.configuration.enums.ConnectionQuality;
import org.tlauncher.tlauncher.configuration.enums.ConsoleType;
import org.tlauncher.util.IntegerArray;
import org.tlauncher.util.StringUtil;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/configuration/PlainParser.class */
public class PlainParser {
    PlainParser() {
    }

    public static void parse(Object plainValue, Object defaultValue) throws ParseException {
        if (defaultValue == null) {
            return;
        }
        if (plainValue == null) {
            throw new ParseException("Value is NULL");
        }
        String value = plainValue.toString();
        try {
            if (defaultValue instanceof Integer) {
                Integer.parseInt(value);
            } else if (defaultValue instanceof Boolean) {
                StringUtil.parseBoolean(value);
            } else if (defaultValue instanceof Double) {
                Double.parseDouble(value);
            } else if (defaultValue instanceof Long) {
                Long.parseLong(value);
            } else if (defaultValue instanceof IntegerArray) {
                IntegerArray.parseIntegerArray(value);
            } else if (defaultValue instanceof ActionOnLaunch) {
                if (!ActionOnLaunch.parse(value)) {
                    throw new ParseException("Cannot parse ActionOnLaunch");
                }
            } else if (defaultValue instanceof ConsoleType) {
                if (!ConsoleType.parse(value)) {
                    throw new ParseException("Cannot parse ConsoleType");
                }
            } else if (defaultValue instanceof ConnectionQuality) {
                if (!ConnectionQuality.parse(value)) {
                    throw new ParseException("Cannot parse ConnectionQuality");
                }
            } else if (defaultValue instanceof UUID) {
                UUID.fromString(value);
            }
        } catch (Exception e) {
            if (e instanceof ParseException) {
                throw ((ParseException) e);
            }
            throw new ParseException("Cannot parse input value!", e);
        }
    }
}
