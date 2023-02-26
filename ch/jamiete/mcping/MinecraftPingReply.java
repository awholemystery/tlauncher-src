package ch.jamiete.mcping;

import ch.qos.logback.core.CoreConstants;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import javax.imageio.ImageIO;

/* loaded from: TLauncher-2.876.jar:ch/jamiete/mcping/MinecraftPingReply.class */
public class MinecraftPingReply {
    private Players players;
    private Version version;
    private String favicon;

    public Players getPlayers() {
        return this.players;
    }

    public Version getVersion() {
        return this.version;
    }

    public BufferedImage getFavicon() throws IOException {
        String part = this.favicon.split(",")[1];
        byte[] imageByte = Base64.getDecoder().decode(part.replaceAll("\\n", CoreConstants.EMPTY_STRING).getBytes(StandardCharsets.UTF_8));
        ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
        return ImageIO.read(bis);
    }

    public String getFaviconString() {
        return this.favicon;
    }

    /* loaded from: TLauncher-2.876.jar:ch/jamiete/mcping/MinecraftPingReply$Description.class */
    public class Description {
        private String text;

        public Description() {
        }

        public String getText() {
            return this.text;
        }
    }

    /* loaded from: TLauncher-2.876.jar:ch/jamiete/mcping/MinecraftPingReply$Players.class */
    public class Players {
        private int max;
        private int online;
        private List<Player> sample;

        public Players() {
        }

        public int getMax() {
            return this.max;
        }

        public int getOnline() {
            return this.online;
        }

        public List<Player> getSample() {
            return this.sample;
        }
    }

    /* loaded from: TLauncher-2.876.jar:ch/jamiete/mcping/MinecraftPingReply$Player.class */
    public class Player {
        private String name;
        private String id;

        public Player() {
        }

        public String getName() {
            return this.name;
        }

        public String getId() {
            return this.id;
        }
    }

    /* loaded from: TLauncher-2.876.jar:ch/jamiete/mcping/MinecraftPingReply$Version.class */
    public class Version {
        private String name;
        private int protocol;

        public Version() {
        }

        public String getName() {
            return this.name;
        }

        public int getProtocol() {
            return this.protocol;
        }
    }
}
