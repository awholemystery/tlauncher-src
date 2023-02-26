package net.minecraft.common;

import ch.qos.logback.core.CoreConstants;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/* loaded from: TLauncher-2.876.jar:net/minecraft/common/NBTTagFloat.class */
public class NBTTagFloat extends NBTBase {
    public float data;

    public NBTTagFloat(String par1Str) {
        super(par1Str);
    }

    public NBTTagFloat(String par1Str, float par2) {
        super(par1Str);
        this.data = par2;
    }

    @Override // net.minecraft.common.NBTBase
    void write(DataOutput par1DataOutput) throws IOException {
        par1DataOutput.writeFloat(this.data);
    }

    @Override // net.minecraft.common.NBTBase
    void load(DataInput par1DataInput, int par2) throws IOException {
        this.data = par1DataInput.readFloat();
    }

    @Override // net.minecraft.common.NBTBase
    public byte getId() {
        return (byte) 5;
    }

    public String toString() {
        return CoreConstants.EMPTY_STRING + this.data;
    }

    @Override // net.minecraft.common.NBTBase
    public NBTBase copy() {
        return new NBTTagFloat(getName(), this.data);
    }

    @Override // net.minecraft.common.NBTBase
    public boolean equals(Object par1Obj) {
        if (super.equals(par1Obj)) {
            NBTTagFloat var2 = (NBTTagFloat) par1Obj;
            return this.data == var2.data;
        }
        return false;
    }

    @Override // net.minecraft.common.NBTBase
    public int hashCode() {
        return super.hashCode() ^ Float.floatToIntBits(this.data);
    }
}
