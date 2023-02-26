package org.tlauncher.util.statistics;

import java.util.HashMap;
import java.util.Map;
import org.apache.http.cookie.ClientCookie;
import org.tlauncher.tlauncher.minecraft.launcher.MinecraftLauncher;
import org.tlauncher.tlauncher.minecraft.launcher.MinecraftListenerAdapter;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/util/statistics/GameRunningListener.class */
public class GameRunningListener extends MinecraftListenerAdapter {
    private MinecraftLauncher game;

    public GameRunningListener(MinecraftLauncher game) {
        this.game = game;
    }

    @Override // org.tlauncher.tlauncher.minecraft.launcher.MinecraftListenerAdapter, org.tlauncher.tlauncher.minecraft.launcher.MinecraftListener
    public void onMinecraftLaunch() {
        Map<String, Object> map = new HashMap<>();
        map.put(ClientCookie.VERSION_ATTR, this.game.getVersion().getID());
        StatisticsUtil.startSending("save/run/version", null, map);
    }
}
