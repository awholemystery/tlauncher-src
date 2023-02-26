package org.tlauncher.util.guice;

import com.google.inject.assistedinject.Assisted;
import org.tlauncher.tlauncher.ui.progress.ProgressFrame;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/util/guice/CustomBarFactory.class */
public interface CustomBarFactory {
    ProgressFrame create(@Assisted("info") String str, @Assisted("bottomIcon") String str2, @Assisted("tlauncher") String str3);
}
