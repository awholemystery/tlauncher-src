package org.apache.log4j.varia;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ServerSocket;
import java.net.Socket;
import org.apache.log4j.helpers.LogLog;

/* compiled from: ExternallyRolledFileAppender.java */
/* loaded from: TLauncher-2.876.jar:org/apache/log4j/varia/HUP.class */
class HUP extends Thread {
    int port;
    ExternallyRolledFileAppender er;

    /* JADX INFO: Access modifiers changed from: package-private */
    public HUP(ExternallyRolledFileAppender er, int port) {
        this.er = er;
        this.port = port;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        while (!isInterrupted()) {
            try {
                ServerSocket serverSocket = new ServerSocket(this.port);
                while (true) {
                    Socket socket = serverSocket.accept();
                    LogLog.debug(new StringBuffer().append("Connected to client at ").append(socket.getInetAddress()).toString());
                    new Thread(new HUPNode(socket, this.er), "ExternallyRolledFileAppender-HUP").start();
                }
            } catch (InterruptedIOException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            } catch (RuntimeException e3) {
                e3.printStackTrace();
            }
        }
    }
}
