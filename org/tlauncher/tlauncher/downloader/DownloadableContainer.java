package org.tlauncher.tlauncher.downloader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.tlauncher.tlauncher.ui.console.Console;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/downloader/DownloadableContainer.class */
public class DownloadableContainer {
    private Console console;
    private boolean locked;
    private boolean aborted;
    final List<Downloadable> list = Collections.synchronizedList(new ArrayList());
    private final List<DownloadableContainerHandler> handlers = Collections.synchronizedList(new ArrayList());
    private final List<Throwable> errors = Collections.synchronizedList(new ArrayList());
    private boolean requireAllFiles = true;
    private final AtomicInteger sum = new AtomicInteger();

    public List<Downloadable> getList() {
        return Collections.unmodifiableList(this.list);
    }

    public void add(Downloadable d) {
        if (d == null) {
            throw new NullPointerException();
        }
        checkLocked();
        if (this.list.contains(d)) {
            return;
        }
        this.list.add(d);
        d.setContainer(this);
        this.sum.incrementAndGet();
    }

    public void addAll(Downloadable... ds) {
        if (ds == null) {
            throw new NullPointerException();
        }
        for (int i = 0; i < ds.length; i++) {
            if (ds[i] == null) {
                throw new NullPointerException("Downloadable at " + i + " is NULL!");
            }
            if (!this.list.contains(ds[i])) {
                this.list.add(ds[i]);
                ds[i].setContainer(this);
                this.sum.incrementAndGet();
            }
        }
    }

    public void addAll(Collection<Downloadable> coll) {
        if (coll == null) {
            throw new NullPointerException();
        }
        int i = -1;
        for (Downloadable d : coll) {
            i++;
            if (d == null) {
                throw new NullPointerException("Downloadable at" + i + " is NULL!");
            }
            this.list.add(d);
            d.setContainer(this);
            this.sum.incrementAndGet();
        }
    }

    public void addHandler(DownloadableContainerHandler handler) {
        if (handler == null) {
            throw new NullPointerException();
        }
        checkLocked();
        this.handlers.add(handler);
    }

    public List<Throwable> getErrors() {
        return Collections.unmodifiableList(this.errors);
    }

    public Console getConsole() {
        return this.console;
    }

    public void setConsole(Console console) {
        checkLocked();
        this.console = console;
    }

    public boolean isAborted() {
        return this.aborted;
    }

    private static void removeDuplicates(DownloadableContainer a, DownloadableContainer b) {
        if (a.locked) {
            throw new IllegalStateException("First conatiner is already locked!");
        }
        if (b.locked) {
            throw new IllegalStateException("Second container is already locked!");
        }
        a.locked = true;
        b.locked = true;
        try {
            List<Downloadable> bList = b.list;
            List<Downloadable> deleteList = new ArrayList<>();
            for (Downloadable aDownloadable : a.list) {
                for (Downloadable bDownloadable : bList) {
                    if (aDownloadable.equals(bDownloadable)) {
                        deleteList.add(bDownloadable);
                    }
                }
            }
            bList.removeAll(deleteList);
            a.locked = false;
            b.locked = false;
        } catch (Throwable th) {
            a.locked = false;
            b.locked = false;
            throw th;
        }
    }

    public boolean isRequireAllFiles() {
        return this.requireAllFiles;
    }

    public void setRequireAllFiles(boolean requireAllFiles) {
        this.requireAllFiles = requireAllFiles;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onComplete(Downloadable d) throws RetryDownloadException {
        for (DownloadableContainerHandler handler : this.handlers) {
            handler.onComplete(this, d);
        }
        if (this.sum.decrementAndGet() > 0) {
            return;
        }
        for (DownloadableContainerHandler handler2 : this.handlers) {
            handler2.onFullComplete(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onAbort(Downloadable d) {
        this.aborted = true;
        this.errors.add(d.getError());
        if (this.sum.decrementAndGet() > 0) {
            return;
        }
        for (DownloadableContainerHandler handler : this.handlers) {
            handler.onAbort(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onError(Downloadable d, Throwable e) {
        this.errors.add(e);
        for (DownloadableContainerHandler handler : this.handlers) {
            handler.onError(this, d, e);
        }
    }

    private void checkLocked() {
        if (this.locked) {
            throw new IllegalStateException("Downloadable is locked!");
        }
    }

    public static void removeDuplicates(List<? extends DownloadableContainer> list) {
        if (list == null) {
            throw new NullPointerException();
        }
        if (list.size() < 2) {
            return;
        }
        for (int i = 0; i < list.size() - 1; i++) {
            for (int k = i + 1; k < list.size(); k++) {
                removeDuplicates(list.get(i), list.get(k));
            }
        }
    }
}
