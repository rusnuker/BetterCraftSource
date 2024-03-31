// 
// Decompiled by Procyon v0.6.0
// 

package com.mcf.davidee.nbtedit;

import java.io.DataInput;
import net.minecraft.nbt.NBTSizeTracker;
import io.netty.buffer.ByteBufInputStream;
import io.netty.handler.codec.EncoderException;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.ByteBuf;
import java.util.List;
import net.minecraft.nbt.NBTTagList;
import me.amkgre.bettercraft.client.utils.ReflectionHelperUtils;
import net.minecraft.nbt.NBTBase;
import java.util.Map;
import java.io.DataOutput;
import java.io.IOException;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import java.io.DataInputStream;

public class NBTHelper
{
    public static NBTTagCompound nbtRead(final DataInputStream in) throws IOException {
        return CompressedStreamTools.read(in);
    }
    
    public static void nbtWrite(final NBTTagCompound compound, final DataOutput out) throws IOException {
        CompressedStreamTools.write(compound, out);
    }
    
    public static Map<String, NBTBase> getMap(final NBTTagCompound tag) {
        return ReflectionHelperUtils.getPrivateValue(NBTTagCompound.class, tag, 2);
    }
    
    public static NBTBase getTagAt(final NBTTagList tag, final int index) {
        final List<NBTBase> list = ReflectionHelperUtils.getPrivateValue(NBTTagList.class, tag, 1);
        return list.get(index);
    }
    
    public static void writeToBuffer(final NBTTagCompound nbt, final ByteBuf buf) {
        if (nbt == null) {
            buf.writeByte(0);
        }
        else {
            try {
                CompressedStreamTools.write(nbt, new ByteBufOutputStream(buf));
            }
            catch (final IOException e) {
                throw new EncoderException(e);
            }
        }
    }
    
    public static NBTTagCompound readNbtFromBuffer(final ByteBuf buf) {
        final int index = buf.readerIndex();
        final byte isNull = buf.readByte();
        if (isNull == 0) {
            return null;
        }
        buf.readerIndex(index);
        try {
            return CompressedStreamTools.read(new ByteBufInputStream(buf), new NBTSizeTracker(2097152L));
        }
        catch (final IOException ioexception) {
            throw new EncoderException(ioexception);
        }
    }
}
