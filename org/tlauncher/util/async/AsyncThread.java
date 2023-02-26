package org.tlauncher.util.async;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/util/async/AsyncThread.class */
public class AsyncThread {
    private static ExecutorService service = Executors.newCachedThreadPool(new ThreadFactory() { // from class: org.tlauncher.util.async.AsyncThread.1
        @Override // java.util.concurrent.ThreadFactory
        public Thread newThread(Runnable r) {
            return new RunnableThread(r);
        }
    });

    public static void execute(Runnable r) {
        service.execute(r);
    }

    public static ExecutorService getService() {
        return service;
    }
}
