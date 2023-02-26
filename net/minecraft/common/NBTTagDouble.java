package net.minecraft.common;

import ch.qos.logback.core.CoreConstants;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/* loaded from: TLauncher-2.876.jar:net/minecraft/common/NBTTagDouble.class */
public class NBTTagDouble extends NBTBase {
    public double data;

    public NBTTagDouble(String par1Str) {
        super(par1Str);
    }

    public NBTTagDouble(String par1Str, double par2) {
        super(par1Str);
        this.data = par2;
    }

    @Override // net.minecraft.common.NBTBase
    void write(DataOutput par1DataOutput) throws IOException {
        par1DataOutput.writeDouble(this.data);
    }

    @Override // net.minecraft.common.NBTBase
    void load(DataInput par1DataInput, int par2) throws IOException {
        this.data = par1DataInput.readDouble();
    }

    @Override // net.minecraft.common.NBTBase
    public byte getId() {
        return (byte) 6;
    }

    public String toString() {
        return CoreConstants.EMPTY_STRING + this.data;
    }

    @Override // net.minecraft.common.NBTBase
    public NBTBase copy() {
        return new NBTTagDouble(getName(), this.data);
    }

    @Override // net.minecraft.common.NBTBase
    public boolean equals(Object par1Obj) {
        if (super.equals(par1Obj)) {
            NBTTagDouble var2 = (NBTTagDouble) par1Obj;
            return this.data == var2.data;
        }
        return false;
    }

    @Override // net.minecraft.common.NBTBase
    public int hashCode() {
        long var1 = Double.doubleToLongBits(this.data);
        return super.hashCode() ^ ((int) (var1 ^ (var1 >>> 32)));
    }
}
