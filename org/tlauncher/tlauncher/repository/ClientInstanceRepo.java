package org.tlauncher.tlauncher.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.tlauncher.tlauncher.rmo.TLauncher;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/repository/ClientInstanceRepo.class */
public class ClientInstanceRepo {
    public static final Repo LOCAL_VERSION_REPO = new Repo(new String[0], "LOCAL_VERSION_REPO");
    public static final Repo OFFICIAL_VERSION_REPO = new Repo(TLauncher.getInnerSettings().getArray("official.repo"), "OFFICIAL_VERSION_REPO");
    public static final Repo EMPTY_REPO = new Repo(TLauncher.getInnerSettings().getArray("empty.repositories"), "EMPTY");
    public static final Repo EXTRA_VERSION_REPO = new Repo(TLauncher.getInnerSettings().getArray("tlauncher.versions.repo"), "EXTRA_VERSION_REPO");
    public static final Repo ASSETS_REPO = new Repo(TLauncher.getInnerSettings().getArray("assets.repo"), "ASSETS_REPO");
    public static final Repo LIBRARY_REPO = new Repo(TLauncher.getInnerSettings().getArray("library.repo"), "LIBRARY_REPO");
    public static final Repo SKIN_VERSION_REPO = new Repo(TLauncher.getInnerSettings().getArray("skin.extra.repo"), "SKIN_VERSION_REPO");
    public static final Repo HOT_SERVERS_REPO = new Repo(TLauncher.getInnerSettings().getArray("hot.servers"), "HOT_SERVERS_REPO");
    public static final Repo ADD_HOT_SERVERS_REPO = new Repo(TLauncher.getInnerSettings().getArray("add.hot.servers"), "ADD_HOT_SERVERS_REPO");
    public static final Repo SERVER_LIST_REPO = new Repo(new String[]{"http://repo.tlauncher.org/update/downloads/configs/inner_servers.json", "https://tlauncher.org/repo/update/downloads/configs/inner_servers.json", "http://advancedrepository.com/update/downloads/configs/inner_servers.json"}, "SERVER_LIST_REPO");
    private static final List<Repo> LIST = new ArrayList<Repo>() { // from class: org.tlauncher.tlauncher.repository.ClientInstanceRepo.1
        {
            add(ClientInstanceRepo.LOCAL_VERSION_REPO);
            add(ClientInstanceRepo.OFFICIAL_VERSION_REPO);
            add(ClientInstanceRepo.EXTRA_VERSION_REPO);
            add(ClientInstanceRepo.SKIN_VERSION_REPO);
        }
    };

    public static Repo find(String name) {
        if (name.isEmpty()) {
            return EMPTY_REPO;
        }
        Optional<Repo> repo = LIST.stream().filter(r -> {
            return r.getName().equalsIgnoreCase(name);
        }).findFirst();
        if (repo.isPresent()) {
            return repo.get();
        }
        throw new RuntimeException("can't find proper repo " + name);
    }

    public static Repo createModpackRepo() {
        return new Repo(TLauncher.getInnerSettings().getArray("file.server"), "MODPACK_REPO");
    }
}
