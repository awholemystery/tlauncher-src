package org.tlauncher.tlauncher.controller;

import com.google.common.eventbus.EventBus;
import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import org.tlauncher.tlauncher.configuration.Configuration;
import org.tlauncher.tlauncher.entity.minecraft.MinecraftJava;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.util.FileUtil;
import org.tlauncher.util.MinecraftUtil;
import org.tlauncher.util.U;

@Singleton
/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/controller/JavaMinecraftController.class */
public class JavaMinecraftController {
    private final String filename = "minecraft_tlauncher_java_config.json";
    public static final String SELECTED_JAVA_KEY = "minecraft.java.selected";
    @Inject
    private EventBus eventBus;
    @Inject
    private Gson gson;
    private MinecraftJava minecraftJava;
    private Configuration con;

    @Inject
    private void init() {
        this.minecraftJava = read();
        this.con = TLauncher.getInstance().getConfiguration();
    }

    public MinecraftJava.CompleteMinecraftJava getCurrent() {
        return this.minecraftJava.getJvm().get(Long.valueOf(this.con.getLong(SELECTED_JAVA_KEY)));
    }

    public boolean isUserJavaVersion() {
        return Objects.nonNull(getCurrent());
    }

    public void add(MinecraftJava.CompleteMinecraftJava completeMinecraftJava) {
        if (Objects.isNull(completeMinecraftJava.getId())) {
            Optional max = this.minecraftJava.getJvm().values().stream().map(e -> {
                return e.getId();
            }).max(Comparator.comparing((v0) -> {
                return Long.valueOf(v0);
            }));
            completeMinecraftJava.setId(Long.valueOf(max.isPresent() ? ((Long) max.get()).longValue() + 1 : 1L));
        }
        this.minecraftJava.getJvm().put(completeMinecraftJava.getId(), completeMinecraftJava);
        this.eventBus.post(this.minecraftJava);
        write();
    }

    public void remove(MinecraftJava.CompleteMinecraftJava java) {
        this.minecraftJava.getJvm().remove(java.getId());
        this.eventBus.post(this.minecraftJava);
        this.con.set(SELECTED_JAVA_KEY, (Object) 0);
        write();
    }

    private MinecraftJava read() {
        try {
            return (MinecraftJava) this.gson.fromJson(FileUtil.readFile(MinecraftUtil.getTLauncherFile("minecraft_tlauncher_java_config.json")), (Class<Object>) MinecraftJava.class);
        } catch (IOException e) {
            if (!(e instanceof FileNotFoundException)) {
                write();
                U.log(e);
            }
            this.minecraftJava = new MinecraftJava();
            return this.minecraftJava;
        }
    }

    private void write() {
        try {
            FileUtil.writeFile(MinecraftUtil.getTLauncherFile("minecraft_tlauncher_java_config.json"), this.gson.toJson(this.minecraftJava));
        } catch (IOException e) {
            U.log(e);
        }
    }

    public void notifyListeners() {
        this.eventBus.post(this.minecraftJava);
    }

    public MinecraftJava.CompleteMinecraftJava getById(Long value) {
        return this.minecraftJava.getJvm().get(value);
    }
}
