package org.tlauncher.tlauncher.entity.server;

import ch.qos.logback.core.joran.action.Action;
import net.minecraft.common.NBTTagCompound;
import org.apache.commons.lang3.StringUtils;
import org.tlauncher.exceptions.ParseException;

/* loaded from: TLauncher-2.876.jar:org/tlauncher/tlauncher/entity/server/Server.class */
public class Server {
    private String name;
    private String address;
    private String ip;
    private String port;
    private boolean hideAddress;
    private int acceptTextures;

    public void setName(String name) {
        this.name = name;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void setHideAddress(boolean hideAddress) {
        this.hideAddress = hideAddress;
    }

    public void setAcceptTextures(int acceptTextures) {
        this.acceptTextures = acceptTextures;
    }

    public String toString() {
        return "Server(name=" + getName() + ", address=" + getAddress() + ", ip=" + getIp() + ", port=" + getPort() + ", hideAddress=" + isHideAddress() + ", acceptTextures=" + getAcceptTextures() + ")";
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof Server) {
            Server other = (Server) o;
            if (other.canEqual(this)) {
                Object this$address = getAddress();
                Object other$address = other.getAddress();
                return this$address == null ? other$address == null : this$address.equals(other$address);
            }
            return false;
        }
        return false;
    }

    protected boolean canEqual(Object other) {
        return other instanceof Server;
    }

    public int hashCode() {
        Object $address = getAddress();
        int result = (1 * 59) + ($address == null ? 43 : $address.hashCode());
        return result;
    }

    public String getName() {
        return this.name;
    }

    public String getAddress() {
        return this.address;
    }

    public String getIp() {
        return this.ip;
    }

    public String getPort() {
        return this.port;
    }

    public boolean isHideAddress() {
        return this.hideAddress;
    }

    public int getAcceptTextures() {
        return this.acceptTextures;
    }

    private static String[] splitAddress(String address) {
        String[] array = StringUtils.split(address, ':');
        switch (array.length) {
            case 1:
                return new String[]{address, null};
            case 2:
                return new String[]{array[0], array[1]};
            default:
                throw new ParseException("split incorrectly " + address);
        }
    }

    public NBTTagCompound getNBT() {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setString(Action.NAME_ATTRIBUTE, this.name);
        compound.setString("ip", this.address);
        compound.setBoolean("hideAddress", this.hideAddress);
        if (this.acceptTextures != 0) {
            compound.setBoolean("acceptTextures", this.acceptTextures == 1);
        }
        return compound;
    }

    public static Server loadFromNBT(NBTTagCompound nbt) {
        Server server = new Server();
        server.setName(nbt.getString(Action.NAME_ATTRIBUTE));
        server.setAddress(nbt.getString("ip"));
        server.hideAddress = nbt.getBoolean("hideAddress");
        if (nbt.hasKey("acceptTextures")) {
            server.acceptTextures = nbt.getBoolean("acceptTextures") ? 1 : -1;
        }
        return server;
    }

    public void setAddress(String address) {
        if (address == null) {
            this.ip = null;
            this.port = null;
        } else {
            String[] split = splitAddress(address);
            this.ip = split[0];
            this.port = split[1];
        }
        this.address = address;
    }
}
