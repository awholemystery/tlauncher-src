package ch.jamiete.mcping;

import com.google.gson.Gson;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.SRVRecord;
import org.xbill.DNS.TextParseException;

/* loaded from: TLauncher-2.876.jar:ch/jamiete/mcping/MinecraftPing.class */
public class MinecraftPing {
    public MinecraftPingReply getPing(String hostname) throws IOException {
        return getPing(new MinecraftPingOptions().setHostname(hostname));
    }

    public MinecraftPingReply getPing(MinecraftPingOptions options) throws IOException {
        MinecraftPingUtil.validate(options.getHostname(), "Hostname cannot be null.");
        MinecraftPingUtil.validate(Integer.valueOf(options.getPort()), "Port cannot be null.");
        Socket socket = new Socket();
        Throwable th = null;
        try {
            socket.connect(new InetSocketAddress(options.getHostname(), options.getPort()), options.getTimeout());
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            ByteArrayOutputStream handshake_bytes = new ByteArrayOutputStream();
            DataOutputStream handshake = new DataOutputStream(handshake_bytes);
            handshake.writeByte(MinecraftPingUtil.PACKET_HANDSHAKE);
            MinecraftPingUtil.writeVarInt(handshake, MinecraftPingUtil.PROTOCOL_VERSION);
            MinecraftPingUtil.writeVarInt(handshake, options.getHostname().length());
            handshake.writeBytes(options.getHostname());
            handshake.writeShort(options.getPort());
            MinecraftPingUtil.writeVarInt(handshake, MinecraftPingUtil.STATUS_HANDSHAKE);
            MinecraftPingUtil.writeVarInt(out, handshake_bytes.size());
            out.write(handshake_bytes.toByteArray());
            out.writeByte(1);
            out.writeByte(MinecraftPingUtil.PACKET_STATUSREQUEST);
            MinecraftPingUtil.readVarInt(in);
            int id = MinecraftPingUtil.readVarInt(in);
            MinecraftPingUtil.io(id == -1, "Server prematurely ended stream.");
            MinecraftPingUtil.io(id != MinecraftPingUtil.PACKET_STATUSREQUEST, "Server returned invalid packet.");
            int length = MinecraftPingUtil.readVarInt(in);
            MinecraftPingUtil.io(length == -1, "Server prematurely ended stream.");
            MinecraftPingUtil.io(length == 0, "Server returned unexpected value.");
            byte[] data = new byte[length];
            in.readFully(data);
            String json = new String(data, options.getCharset());
            out.writeByte(9);
            out.writeByte(MinecraftPingUtil.PACKET_PING);
            out.writeLong(System.currentTimeMillis());
            MinecraftPingUtil.readVarInt(in);
            int id2 = MinecraftPingUtil.readVarInt(in);
            MinecraftPingUtil.io(id2 == -1, "Server prematurely ended stream.");
            MinecraftPingUtil.io(id2 != MinecraftPingUtil.PACKET_PING, "Server returned invalid packet.");
            handshake.close();
            handshake_bytes.close();
            out.close();
            in.close();
            MinecraftPingReply minecraftPingReply = (MinecraftPingReply) new Gson().fromJson(json, (Class<Object>) MinecraftPingReply.class);
            if (socket != null) {
                if (0 != 0) {
                    try {
                        socket.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                } else {
                    socket.close();
                }
            }
            return minecraftPingReply;
        } finally {
        }
    }

    public void resolveDNS(MinecraftPingOptions options) throws TextParseException {
        String service = "_minecraft._tcp." + new InetSocketAddress(options.getHostname(), options.getPort()).getHostName();
        SRVRecord[] run = new Lookup(service, 33).run();
        if (run != null) {
            for (SRVRecord srv : run) {
                String hostname = srv.getTarget().toString();
                int port = srv.getPort();
                options.setHostname(hostname);
                options.setPort(port);
            }
        }
    }
}
