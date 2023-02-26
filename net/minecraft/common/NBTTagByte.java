package net.minecraft.common;

import ch.qos.logback.core.CoreConstants;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/* loaded from: TLauncher-2.876.jar:net/minecraft/common/NBTTagByte.class */
public class NBTTagByte extends NBTBase {
    public byte data;

    public NBTTagByte(String par1Str) {
        super(par1Str);
    }

    public NBTTagByte(String par1Str, byte par2) {
        super(par1Str);
        this.data = par2;
    }

    @Override // net.minecraft.common.NBTBase
    void write(DataOutput par1DataOutput) throws IOException {
        par1DataOutput.writeByte(this.data);
    }

    @Override // net.minecraft.common.NBTBase
    void load(DataInput par1DataInput, int par2) throws IOException {
        this.data = par1DataInput.readByte();
    }

    @Override // net.minecraft.common.NBTBase
    public byte getId() {
        return (byte) 1;
    }

    public String toString() {
        return CoreConstants.EMPTY_STRING + ((int) this.data);
    }

    @Override // net.minecraft.common.NBTBase
    public NBTBase copy() {
        return new NBTTagByte(getName(), this.data);
    }

    @Override // net.minecraft.common.NBTBase
    public boolean equals(Object par1Obj) {
        if (super.equals(par1Obj)) {
            NBTTagByte var2 = (NBTTagByte) par1Obj;
            return this.data == var2.data;
        }
        return false;
    }

    @Override // net.minecraft.common.NBTBase
    public int hashCode() {
        return super.hashCode() ^ this.data;
    }
}
