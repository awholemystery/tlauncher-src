package org.tlauncher.tlauncher.repository;

import ch.qos.logback.core.CoreConstants;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import net.minecraft.launcher.Http;
import org.tlauncher.util.Time;
import org.tlauncher.util.U;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/repository/Repo.class */
public class Repo {
    private static final List<String> notShowLogs = new ArrayList<String>() { // from class: org.tlauncher.tlauncher.repository.Repo.1
        {
            add("inner_servers.json");
        }
    };
    private final String name;
    private final List<String> repos;
    private int primaryTimeout;
    private int selected;
    private boolean isSelected;

    public Repo(String[] urls, String name) {
        if (urls == null) {
            throw new NullPointerException("URL array is NULL!");
        }
        this.name = name;
        this.repos = Collections.synchronizedList(new ArrayList());
        this.primaryTimeout = U.getConnectionTimeout();
        Collections.addAll(this.repos, urls);
    }

    private String getUrl0(String uri) throws IOException {
        boolean canSelect = isSelectable();
        if (!canSelect) {
            return getRawUrl(uri);
        }
        Object lock = new Object();
        IOException e = null;
        int i = 0;
        int attempt = 0;
        while (i < 3) {
            i++;
            int timeout = this.primaryTimeout * i;
            for (int x = 0; x < getCount(); x++) {
                attempt++;
                String url = getRepo(x);
                Stream<String> stream = notShowLogs.stream();
                url.getClass();
                if (stream.noneMatch(this::endsWith)) {
                    log("Attempt #" + attempt + "; timeout: " + timeout + " ms; url: " + url);
                }
                Time.start(lock);
                try {
                    String result = Http.performGet(new URL(url + uri), timeout, timeout);
                    setSelected(x);
                    log("Success: Reached the repo in", Long.valueOf(Time.stop(lock)), "ms.");
                    return result;
                } catch (IOException e0) {
                    Stream<String> stream2 = notShowLogs.stream();
                    url.getClass();
                    if (stream2.noneMatch(this::endsWith)) {
                        log("request to url = " + url + uri);
                    }
                    log("Failed: Repo is not reachable!", e0);
                    e = e0;
                    Time.stop(lock);
                }
            }
        }
        log("Failed: All repos are unreachable.");
        throw ((IOException) Objects.requireNonNull(e));
    }

    public String getUrl(String uri) throws IOException {
        return getUrl0(uri);
    }

    public String getUrl() throws IOException {
        return getUrl0(CoreConstants.EMPTY_STRING);
    }

    private String getRawUrl(String uri) throws IOException {
        String url = getSelectedRepo() + Http.encode(uri);
        try {
            return Http.performGet(new URL(url));
        } catch (IOException e) {
            log("Cannot get raw:", url);
            throw e;
        }
    }

    public int getSelected() {
        return this.selected;
    }

    public synchronized void selectNext() {
        int i = this.selected + 1;
        this.selected = i;
        if (i >= getCount()) {
            this.selected = 0;
        }
    }

    public String getSelectedRepo() {
        return this.repos.get(this.selected);
    }

    public String getRepo(int pos) {
        return this.repos.get(pos);
    }

    public List<String> getList() {
        return this.repos;
    }

    public int getCount() {
        return this.repos.size();
    }

    boolean isSelected() {
        return this.isSelected;
    }

    void setSelected(int pos) {
        if (!isSelectable()) {
            throw new IllegalStateException();
        }
        this.isSelected = true;
        this.selected = pos;
    }

    public boolean isSelectable() {
        return !this.repos.isEmpty();
    }

    public String toString() {
        return this.name;
    }

    private void log(Object... obj) {
        U.log("[REPO][" + this.name + "]", obj);
    }

    public String getName() {
        return this.name;
    }
}
