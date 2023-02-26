package org.tlauncher.tlauncher.minecraft.launcher.server;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.inject.Named;
import net.minecraft.common.CompressedStreamTools;
import net.minecraft.common.NBTTagCompound;
import net.minecraft.common.NBTTagList;
import net.minecraft.launcher.versions.Version;
import org.apache.commons.lang3.time.DateUtils;
import org.tlauncher.exceptions.ParseException;
import org.tlauncher.tlauncher.configuration.Configuration;
import org.tlauncher.tlauncher.entity.server.RemoteServer;
import org.tlauncher.tlauncher.entity.server.ReplacedAddressServer;
import org.tlauncher.tlauncher.entity.server.Server;
import org.tlauncher.tlauncher.managers.ServerList;
import org.tlauncher.tlauncher.managers.ServerListManager;
import org.tlauncher.tlauncher.modpack.ModpackUtil;
import org.tlauncher.tlauncher.rmo.TLauncher;
import org.tlauncher.tlauncher.ui.loc.Localizable;
import org.tlauncher.util.FileUtil;
import org.tlauncher.util.MinecraftUtil;
import org.tlauncher.util.U;

@Singleton
/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/minecraft/launcher/server/InnerMinecraftServersImpl.class */
public class InnerMinecraftServersImpl implements InnerMinecraftServer {
    private static final File INNER_SERVERS_FILE = MinecraftUtil.getTLauncherFile("InnerStateServer-1.3.json");
    private static final File SERVERS_DAT_FILE = FileUtil.getRelative("servers.dat").toFile();
    @Inject
    private TLauncher tlauncher;
    private List<RemoteServer> innerServers;
    private List<Server> localServerList;
    @Inject
    @Named("GsonCompleteVersion")
    private Gson gson;

    @Override // org.tlauncher.tlauncher.minecraft.launcher.server.InnerMinecraftServer
    public void prepareInnerServer() throws IOException {
        U.classNameLog(InnerMinecraftServersImpl.class, "prepare inner servers");
        ServerList serverList = ((ServerListManager) this.tlauncher.getManager().getComponent(ServerListManager.class)).getList();
        Configuration settings = this.tlauncher.getConfiguration();
        if (this.tlauncher.getProfileManager().isNotPremium() || settings.getBoolean("gui.settings.servers.recommendation")) {
            addNewServers(serverList.getNewServers());
            this.innerServers.stream().filter(s -> {
                return s.getState().equals(RemoteServer.ServerState.DEACTIVATED);
            }).filter(s2 -> {
                return s2.getMaxRemovingCountServer().intValue() > s2.getRemovedTime().size();
            }).filter(s3 -> {
                int hours = s3.getRecoveryServerTime().intValue();
                Date addedDate = DateUtils.addHours(new Date(s3.getRemovedTime().get(s3.getRemovedTime().size() - 1).longValue()), hours);
                return new Date().after(addedDate);
            }).forEach(s4 -> {
                s4.setState(RemoteServer.ServerState.ACTIVE);
                if (!this.localServerList.contains(s4)) {
                    this.localServerList.add(0, s4);
                }
            });
        }
        if (this.tlauncher.getProfileManager().isNotPremium() || settings.getBoolean("gui.settings.guard.checkbox")) {
            this.innerServers.removeIf(s5 -> {
                return ((List) serverList.getRemovedServers().stream().map((v0) -> {
                    return v0.toLowerCase();
                }).collect(Collectors.toList())).contains(s5.getAddress().toLowerCase());
            });
            this.localServerList.removeIf(s6 -> {
                return ((List) serverList.getRemovedServers().stream().map((v0) -> {
                    return v0.toLowerCase();
                }).collect(Collectors.toList())).contains(s6.getAddress().toLowerCase());
            });
        }
        for (ReplacedAddressServer s7 : serverList.getClientChangedAddress()) {
            this.localServerList.stream().filter(s1 -> {
                return s7.getOldAddress().equalsIgnoreCase(s1.getAddress());
            }).forEach(s12 -> {
                U.log(String.format("changed address from %s to %s ", s12.getAddress(), s7.getNewAddress()));
                s12.setAddress(s7.getNewAddress());
            });
        }
        try {
            writeServerDatFile(SERVERS_DAT_FILE, this.localServerList);
            FileUtil.writeFile(INNER_SERVERS_FILE, this.gson.toJson(this.innerServers));
        } catch (RuntimeException e) {
            U.log(e);
        }
    }

    @Override // org.tlauncher.tlauncher.minecraft.launcher.server.InnerMinecraftServer
    public void searchRemovedServers() throws IOException {
        U.classNameLog(InnerMinecraftServersImpl.class, "search changers of the servers");
        this.localServerList = readServerDatFile(SERVERS_DAT_FILE);
        this.innerServers.stream().filter(s -> {
            return s.getState().equals(RemoteServer.ServerState.ACTIVE);
        }).filter(s2 -> {
            return !this.localServerList.contains(s2);
        }).forEach(s3 -> {
            s3.setState(RemoteServer.ServerState.DEACTIVATED);
            s3.getRemovedTime().add(Long.valueOf(new Date().getTime()));
        });
        this.innerServers.removeIf(s4 -> {
            return s4.getState().equals(RemoteServer.ServerState.DEACTIVATED) && s4.getRemovedTime().size() >= s4.getMaxRemovingCountServer().intValue() && new Date().after(DateUtils.addDays(s4.getAddedDate(), 5));
        });
    }

    @Override // org.tlauncher.tlauncher.minecraft.launcher.server.InnerMinecraftServer
    public void addPageServer(Server server) throws IOException {
        this.localServerList = readServerDatFile(SERVERS_DAT_FILE);
        this.localServerList.removeIf(s -> {
            return s.getAddress().equals(server.getAddress());
        });
        this.localServerList.add(0, server);
        writeServerDatFile(SERVERS_DAT_FILE, this.localServerList);
    }

    @Override // org.tlauncher.tlauncher.minecraft.launcher.server.InnerMinecraftServer
    public void addPageServerToModpack(Server server, Version c) throws IOException {
        Path path = ModpackUtil.getPathByVersion(c, "servers.dat");
        List<Server> list = new ArrayList<>();
        if (Files.exists(path, new LinkOption[0])) {
            list = readServerDatFile(path.toFile());
        }
        list.remove(server);
        list.add(0, server);
        writeServerDatFile(path.toFile(), list);
    }

    private void addNewServers(List<RemoteServer> remoteServers) {
        String lang = Localizable.get().getSelected().toString();
        remoteServers.stream().filter(s -> {
            return !this.localServerList.contains(s);
        }).filter(s2 -> {
            return s2.getLocales().isEmpty() || s2.getLocales().contains(lang);
        }).filter(s3 -> {
            if (!this.innerServers.contains(s3)) {
                return true;
            }
            return this.innerServers.stream().filter(s1 -> {
                return s1.equals(s3);
            }).anyMatch(s12 -> {
                return s12.getState().equals(RemoteServer.ServerState.ACTIVE);
            });
        }).forEach(s4 -> {
            s4.initRemote();
            if (!this.innerServers.contains(s4)) {
                this.innerServers.add(0, s4);
            }
            this.localServerList.add(0, s4);
        });
    }

    @Override // org.tlauncher.tlauncher.minecraft.launcher.server.InnerMinecraftServer
    public void initInnerServers() {
        if (Files.exists(INNER_SERVERS_FILE.toPath(), new LinkOption[0])) {
            try {
                this.innerServers = (List) this.gson.fromJson(FileUtil.readFile(INNER_SERVERS_FILE), new TypeToken<List<RemoteServer>>() { // from class: org.tlauncher.tlauncher.minecraft.launcher.server.InnerMinecraftServersImpl.1
                }.getType());
                return;
            } catch (JsonParseException | IOException e) {
                U.log(e);
            }
        }
        this.innerServers = new ArrayList();
    }

    private List<Server> readServerDatFile(File file) throws IOException {
        List<Server> list = new ArrayList<>();
        NBTTagCompound compound = CompressedStreamTools.read(file);
        if (Objects.nonNull(compound)) {
            NBTTagList servers = compound.getTagList("servers");
            for (int i = 0; i < servers.tagCount(); i++) {
                try {
                    list.add(Server.loadFromNBT((NBTTagCompound) servers.tagAt(i)));
                } catch (ParseException e) {
                    U.log("found server ", e);
                }
            }
            U.log("read servers from servers.dat", list);
        }
        return list;
    }

    private void writeServerDatFile(File file, List<Server> list) throws IOException {
        U.log("save servers to servers.dat");
        NBTTagList servers = new NBTTagList();
        list.forEach(s -> {
            servers.appendTag(s.getNBT());
        });
        NBTTagCompound compound = new NBTTagCompound();
        compound.setTag("servers", servers);
        CompressedStreamTools.safeWrite(compound, file);
    }
}
