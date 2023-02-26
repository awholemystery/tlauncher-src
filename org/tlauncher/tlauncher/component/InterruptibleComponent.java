package org.tlauncher.tlauncher.component;

import java.util.concurrent.Semaphore;
import org.tlauncher.tlauncher.managers.ComponentManager;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/component/InterruptibleComponent.class */
public abstract class InterruptibleComponent extends RefreshableComponent {
    protected final boolean[] refreshList;
    private int lastRefreshID;
    protected final Semaphore semaphore;
    protected boolean lastResult;

    protected abstract boolean refresh(int i);

    /* JADX INFO: Access modifiers changed from: protected */
    public InterruptibleComponent(ComponentManager manager) throws Exception {
        this(manager, 64);
    }

    private InterruptibleComponent(ComponentManager manager, int listSize) throws Exception {
        super(manager);
        this.semaphore = new Semaphore(1);
        if (listSize < 1) {
            throw new IllegalArgumentException("Invalid list size: " + listSize + " < 1");
        }
        this.refreshList = new boolean[listSize];
    }

    @Override // org.tlauncher.tlauncher.component.RefreshableComponent
    public final boolean refresh() {
        try {
            if (this.semaphore.tryAcquire()) {
                try {
                    boolean refresh = refresh(nextID());
                    this.lastResult = refresh;
                    return refresh;
                } finally {
                }
            }
            this.semaphore.acquire();
            return this.lastResult;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        } finally {
        }
    }

    public synchronized void stopRefresh() {
        for (int i = 0; i < this.refreshList.length; i++) {
            this.refreshList[i] = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public synchronized int nextID() {
        int listSize = this.refreshList.length;
        int i = this.lastRefreshID;
        this.lastRefreshID = i + 1;
        int next = i;
        if (next >= listSize) {
            next = 0;
        }
        this.lastRefreshID = next;
        return next;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isCancelled(int refreshID) {
        return !this.refreshList[refreshID];
    }
}
