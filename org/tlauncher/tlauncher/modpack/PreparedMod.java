package org.tlauncher.tlauncher.modpack;

import java.nio.file.Path;
import java.util.List;
import net.minecraft.launcher.versions.CompleteVersion;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/modpack/PreparedMod.class */
public interface PreparedMod {
    List<Path> prepare(CompleteVersion completeVersion);
}
