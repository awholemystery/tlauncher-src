package org.apache.http.impl.conn.tsccm;

@Deprecated
/* loaded from: TLauncher-2.876.jar:org/apache/http/impl/conn/tsccm/WaitingThreadAborter.class */
public class WaitingThreadAborter {
    private WaitingThread waitingThread;
    private boolean aborted;

    public void abort() {
        this.aborted = true;
        if (this.waitingThread != null) {
            this.waitingThread.interrupt();
        }
    }

    public void setWaitingThread(WaitingThread waitingThread) {
        this.waitingThread = waitingThread;
        if (this.aborted) {
            waitingThread.interrupt();
        }
    }
}
