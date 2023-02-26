package org.tlauncher.tlauncher.minecraft.launcher.server;

import java.io.IOException;
import net.minecraft.launcher.versions.Version;
import org.tlauncher.tlauncher.entity.server.Server;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/minecraft/launcher/server/InnerMinecraftServer.class */
public interface InnerMinecraftServer {
    void initInnerServers();

    void prepareInnerServer() throws IOException;

    void searchRemovedServers() throws IOException;

    void addPageServer(Server server) throws IOException;

    void addPageServerToModpack(Server server, Version version) throws IOException;
}
