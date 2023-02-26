package net.minecraft.common;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/* loaded from: TLauncher-2.876.jar:net/minecraft/common/NBTTagEnd.class */
public class NBTTagEnd extends NBTBase {
    public NBTTagEnd() {
        super(null);
    }

    @Override // net.minecraft.common.NBTBase
    void load(DataInput par1DataInput, int par2) throws IOException {
    }

    @Override // net.minecraft.common.NBTBase
    void write(DataOutput par1DataOutput) throws IOException {
    }

    @Override // net.minecraft.common.NBTBase
    public byte getId() {
        return (byte) 0;
    }

    public String toString() {
        return "END";
    }

    @Override // net.minecraft.common.NBTBase
    public NBTBase copy() {
        return new NBTTagEnd();
    }
}
