package org.apache.log4j.helpers;

import ch.qos.logback.core.net.SyslogConstants;
import java.io.IOException;
import java.io.Writer;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;

/* loaded from: TLauncher-2.876.jar:org/apache/log4j/helpers/SyslogWriter.class */
public class SyslogWriter extends Writer {
    final int SYSLOG_PORT = SyslogConstants.SYSLOG_PORT;
    static String syslogHost;
    private InetAddress address;
    private final int port;
    private DatagramSocket ds;

    public SyslogWriter(String syslogHost2) {
        syslogHost = syslogHost2;
        if (syslogHost2 == null) {
            throw new NullPointerException("syslogHost");
        }
        String host = syslogHost2;
        int urlPort = -1;
        if (host.indexOf("[") != -1 || host.indexOf(58) == host.lastIndexOf(58)) {
            try {
                URL url = new URL(new StringBuffer().append("http://").append(host).toString());
                if (url.getHost() != null) {
                    host = url.getHost();
                    if (host.startsWith("[") && host.charAt(host.length() - 1) == ']') {
                        host = host.substring(1, host.length() - 1);
                    }
                    urlPort = url.getPort();
                }
            } catch (MalformedURLException e) {
                LogLog.error("Malformed URL: will attempt to interpret as InetAddress.", e);
            }
        }
        this.port = urlPort == -1 ? 514 : urlPort;
        try {
            this.address = InetAddress.getByName(host);
        } catch (UnknownHostException e2) {
            LogLog.error(new StringBuffer().append("Could not find ").append(host).append(". All logging will FAIL.").toString(), e2);
        }
        try {
            this.ds = new DatagramSocket();
        } catch (SocketException e3) {
            e3.printStackTrace();
            LogLog.error(new StringBuffer().append("Could not instantiate DatagramSocket to ").append(host).append(". All logging will FAIL.").toString(), e3);
        }
    }

    @Override // java.io.Writer
    public void write(char[] buf, int off, int len) throws IOException {
        write(new String(buf, off, len));
    }

    @Override // java.io.Writer
    public void write(String string) throws IOException {
        if (this.ds != null && this.address != null) {
            byte[] bytes = string.getBytes();
            int bytesLength = bytes.length;
            if (bytesLength >= 1024) {
                bytesLength = 1024;
            }
            DatagramPacket packet = new DatagramPacket(bytes, bytesLength, this.address, this.port);
            this.ds.send(packet);
        }
    }

    @Override // java.io.Writer, java.io.Flushable
    public void flush() {
    }

    @Override // java.io.Writer, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        if (this.ds != null) {
            this.ds.close();
        }
    }
}
