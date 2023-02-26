package net.minecraft.common;

import ch.qos.logback.core.CoreConstants;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/* loaded from: TLauncher-2.876.jar:net/minecraft/common/NBTTagShort.class */
public class NBTTagShort extends NBTBase {
    public short data;

    public NBTTagShort(String par1Str) {
        super(par1Str);
    }

    public NBTTagShort(String par1Str, short par2) {
        super(par1Str);
        this.data = par2;
    }

    @Override // net.minecraft.common.NBTBase
    void write(DataOutput par1DataOutput) throws IOException {
        par1DataOutput.writeShort(this.data);
    }

    @Override // net.minecraft.common.NBTBase
    void load(DataInput par1DataInput, int par2) throws IOException {
        this.data = par1DataInput.readShort();
    }

    @Override // net.minecraft.common.NBTBase
    public byte getId() {
        return (byte) 2;
    }

    public String toString() {
        return CoreConstants.EMPTY_STRING + ((int) this.data);
    }

    @Override // net.minecraft.common.NBTBase
    public NBTBase copy() {
        return new NBTTagShort(getName(), this.data);
    }

    @Override // net.minecraft.common.NBTBase
    public boolean equals(Object par1Obj) {
        if (super.equals(par1Obj)) {
            NBTTagShort var2 = (NBTTagShort) par1Obj;
            return this.data == var2.data;
        }
        return false;
    }

    @Override // net.minecraft.common.NBTBase
    public int hashCode() {
        return super.hashCode() ^ this.data;
    }
}
