package org.tlauncher.tlauncher.minecraft.crash;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.tlauncher.tlauncher.minecraft.crash.CrashSignatureContainer;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/minecraft/crash/Crash.class */
public class Crash {
    private String file;
    private List<CrashSignatureContainer.CrashSignature> signatures = new ArrayList();

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addSignature(CrashSignatureContainer.CrashSignature sign) {
        this.signatures.add(sign);
    }

    void removeSignature(CrashSignatureContainer.CrashSignature sign) {
        this.signatures.remove(sign);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setFile(String path) {
        this.file = path;
    }

    public String getFile() {
        return this.file;
    }

    public List<CrashSignatureContainer.CrashSignature> getSignatures() {
        return Collections.unmodifiableList(this.signatures);
    }

    public boolean hasSignature(CrashSignatureContainer.CrashSignature s) {
        return this.signatures.contains(s);
    }

    public boolean isRecognized() {
        return !this.signatures.isEmpty();
    }
}
