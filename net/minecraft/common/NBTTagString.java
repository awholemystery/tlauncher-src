package net.minecraft.common;

import ch.qos.logback.core.CoreConstants;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/* loaded from: TLauncher-2.876.jar:net/minecraft/common/NBTTagString.class */
public class NBTTagString extends NBTBase {
    public String data;

    public NBTTagString(String par1Str) {
        super(par1Str);
    }

    public NBTTagString(String par1Str, String par2Str) {
        super(par1Str);
        this.data = par2Str;
        if (par2Str == null) {
            throw new IllegalArgumentException("Empty string not allowed");
        }
    }

    @Override // net.minecraft.common.NBTBase
    void write(DataOutput par1DataOutput) throws IOException {
        par1DataOutput.writeUTF(this.data);
    }

    @Override // net.minecraft.common.NBTBase
    void load(DataInput par1DataInput, int par2) throws IOException {
        this.data = par1DataInput.readUTF();
    }

    @Override // net.minecraft.common.NBTBase
    public byte getId() {
        return (byte) 8;
    }

    public String toString() {
        return CoreConstants.EMPTY_STRING + this.data;
    }

    @Override // net.minecraft.common.NBTBase
    public NBTBase copy() {
        return new NBTTagString(getName(), this.data);
    }

    @Override // net.minecraft.common.NBTBase
    public boolean equals(Object par1Obj) {
        if (!super.equals(par1Obj)) {
            return false;
        }
        NBTTagString var2 = (NBTTagString) par1Obj;
        return (this.data == null && var2.data == null) || (this.data != null && this.data.equals(var2.data));
    }

    @Override // net.minecraft.common.NBTBase
    public int hashCode() {
        return super.hashCode() ^ this.data.hashCode();
    }
}
