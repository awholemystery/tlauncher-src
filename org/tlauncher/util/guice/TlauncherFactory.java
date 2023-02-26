package org.tlauncher.util.guice;

import org.tlauncher.tlauncher.configuration.Configuration;
import org.tlauncher.tlauncher.rmo.TLauncher;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/util/guice/TlauncherFactory.class */
public interface TlauncherFactory {
    TLauncher create(Configuration configuration);
}
