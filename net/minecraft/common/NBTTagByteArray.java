package net.minecraft.common;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

/* loaded from: TLauncher-2.876.jar:net/minecraft/common/NBTTagByteArray.class */
public class NBTTagByteArray extends NBTBase {
    public byte[] byteArray;

    public NBTTagByteArray(String par1Str) {
        super(par1Str);
    }

    public NBTTagByteArray(String par1Str, byte[] par2ArrayOfByte) {
        super(par1Str);
        this.byteArray = par2ArrayOfByte;
    }

    @Override // net.minecraft.common.NBTBase
    void write(DataOutput par1DataOutput) throws IOException {
        par1DataOutput.writeInt(this.byteArray.length);
        par1DataOutput.write(this.byteArray);
    }

    @Override // net.minecraft.common.NBTBase
    void load(DataInput par1DataInput, int par2) throws IOException {
        int var3 = par1DataInput.readInt();
        this.byteArray = new byte[var3];
        par1DataInput.readFully(this.byteArray);
    }

    @Override // net.minecraft.common.NBTBase
    public byte getId() {
        return (byte) 7;
    }

    public String toString() {
        return "[" + this.byteArray.length + " bytes]";
    }

    @Override // net.minecraft.common.NBTBase
    public NBTBase copy() {
        byte[] var1 = new byte[this.byteArray.length];
        System.arraycopy(this.byteArray, 0, var1, 0, this.byteArray.length);
        return new NBTTagByteArray(getName(), var1);
    }

    @Override // net.minecraft.common.NBTBase
    public boolean equals(Object par1Obj) {
        if (super.equals(par1Obj)) {
            return Arrays.equals(this.byteArray, ((NBTTagByteArray) par1Obj).byteArray);
        }
        return false;
    }

    @Override // net.minecraft.common.NBTBase
    public int hashCode() {
        return super.hashCode() ^ Arrays.hashCode(this.byteArray);
    }
}
