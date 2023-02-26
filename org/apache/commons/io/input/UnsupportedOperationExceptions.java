package org.apache.commons.io.input;

/* loaded from: TLauncher-2.876.jar:org/apache/commons/io/input/UnsupportedOperationExceptions.class */
class UnsupportedOperationExceptions {
    private static final String MARK_RESET = "mark/reset";

    UnsupportedOperationExceptions() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static UnsupportedOperationException mark() {
        return method(MARK_RESET);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static UnsupportedOperationException method(String method) {
        return new UnsupportedOperationException(method + " not supported");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static UnsupportedOperationException reset() {
        return method(MARK_RESET);
    }
}
