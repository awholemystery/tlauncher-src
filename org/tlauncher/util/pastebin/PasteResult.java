package org.tlauncher.util.pastebin;

import java.net.URL;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/util/pastebin/PasteResult.class */
public abstract class PasteResult {
    private final Paste paste;

    PasteResult(Paste paste) {
        this.paste = paste;
    }

    public final Paste getPaste() {
        return this.paste;
    }

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/util/pastebin/PasteResult$PasteUploaded.class */
    public static class PasteUploaded extends PasteResult {
        private final URL url;

        /* JADX INFO: Access modifiers changed from: package-private */
        public PasteUploaded(Paste paste, URL url) {
            super(paste);
            this.url = url;
        }

        public final URL getURL() {
            return this.url;
        }

        public String toString() {
            return "PasteUploaded{url='" + this.url + "'}";
        }
    }

    /* loaded from: TLauncher-2.876.jar:org/tlauncher/util/pastebin/PasteResult$PasteFailed.class */
    public static class PasteFailed extends PasteResult {
        private final Throwable error;

        /* JADX INFO: Access modifiers changed from: package-private */
        public PasteFailed(Paste paste, Throwable error) {
            super(paste);
            this.error = error;
        }

        public final Throwable getError() {
            return this.error;
        }

        public String toString() {
            return "PasteFailed{error='" + this.error + "'}";
        }
    }
}
