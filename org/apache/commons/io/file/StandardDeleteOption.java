package org.apache.commons.io.file;

import org.apache.commons.io.IOUtils;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/io/file/StandardDeleteOption.class */
public enum StandardDeleteOption implements DeleteOption {
    OVERRIDE_READ_ONLY;

    public static boolean overrideReadOnly(DeleteOption[] options) {
        if (IOUtils.length(options) == 0) {
            return false;
        }
        for (DeleteOption deleteOption : options) {
            if (deleteOption == OVERRIDE_READ_ONLY) {
                return true;
            }
        }
        return false;
    }
}
