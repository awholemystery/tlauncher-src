package org.apache.log4j.helpers;

import java.io.File;

/* loaded from: TLauncher-2.876.jar:org/apache/log4j/helpers/FileWatchdog.class */
public abstract class FileWatchdog extends Thread {
    public static final long DEFAULT_DELAY = 60000;
    protected String filename;
    protected long delay;
    File file;
    long lastModif;
    boolean warnedAlready;
    boolean interrupted;

    protected abstract void doOnChange();

    /* JADX INFO: Access modifiers changed from: protected */
    public FileWatchdog(String filename) {
        super("FileWatchdog");
        this.delay = 60000L;
        this.lastModif = 0L;
        this.warnedAlready = false;
        this.interrupted = false;
        this.filename = filename;
        this.file = new File(filename);
        setDaemon(true);
        checkAndConfigure();
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    protected void checkAndConfigure() {
        try {
            boolean fileExists = this.file.exists();
            if (fileExists) {
                long l = this.file.lastModified();
                if (l > this.lastModif) {
                    this.lastModif = l;
                    doOnChange();
                    this.warnedAlready = false;
                }
            } else if (!this.warnedAlready) {
                LogLog.debug(new StringBuffer().append("[").append(this.filename).append("] does not exist.").toString());
                this.warnedAlready = true;
            }
        } catch (SecurityException e) {
            LogLog.warn(new StringBuffer().append("Was not allowed to read check file existance, file:[").append(this.filename).append("].").toString());
            this.interrupted = true;
        }
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        while (!this.interrupted) {
            try {
                Thread.sleep(this.delay);
            } catch (InterruptedException e) {
            }
            checkAndConfigure();
        }
    }
}
