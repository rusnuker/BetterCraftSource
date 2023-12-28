// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.io.IOException;
import java.util.Iterator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import java.util.Map;
import com.google.common.collect.Lists;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.nbt.NBTTagCompound;
import java.util.List;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class SPacketChunkData implements Packet<INetHandlerPlayClient>
{
    private int chunkX;
    private int chunkZ;
    private int availableSections;
    private byte[] buffer;
    private List<NBTTagCompound> tileEntityTags;
    private boolean loadChunk;
    
    public SPacketChunkData() {
    }
    
    public SPacketChunkData(final Chunk p_i47124_1_, final int p_i47124_2_) {
        this.chunkX = p_i47124_1_.xPosition;
        this.chunkZ = p_i47124_1_.zPosition;
        this.loadChunk = (p_i47124_2_ == 65535);
        final boolean flag = p_i47124_1_.getWorld().provider.func_191066_m();
        this.buffer = new byte[this.calculateChunkSize(p_i47124_1_, flag, p_i47124_2_)];
        this.availableSections = this.extractChunkData(new PacketBuffer(this.getWriteBuffer()), p_i47124_1_, flag, p_i47124_2_);
        this.tileEntityTags = (List<NBTTagCompound>)Lists.newArrayList();
        for (final Map.Entry<BlockPos, TileEntity> entry : p_i47124_1_.getTileEntityMap().entrySet()) {
            final BlockPos blockpos = entry.getKey();
            final TileEntity tileentity = entry.getValue();
            final int i = blockpos.getY() >> 4;
            if (this.doChunkLoad() || (p_i47124_2_ & 1 << i) != 0x0) {
                final NBTTagCompound nbttagcompound = tileentity.getUpdateTag();
                this.tileEntityTags.add(nbttagcompound);
            }
        }
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.chunkX = buf.readInt();
        this.chunkZ = buf.readInt();
        this.loadChunk = buf.readBoolean();
        this.availableSections = buf.readVarIntFromBuffer();
        final int i = buf.readVarIntFromBuffer();
        if (i > 2097152) {
            throw new RuntimeException("Chunk Packet trying to allocate too much memory on read.");
        }
        buf.readBytes(this.buffer = new byte[i]);
        final int j = buf.readVarIntFromBuffer();
        this.tileEntityTags = (List<NBTTagCompound>)Lists.newArrayList();
        for (int k = 0; k < j; ++k) {
            this.tileEntityTags.add(buf.readNBTTagCompoundFromBuffer());
        }
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeInt(this.chunkX);
        buf.writeInt(this.chunkZ);
        buf.writeBoolean(this.loadChunk);
        buf.writeVarIntToBuffer(this.availableSections);
        buf.writeVarIntToBuffer(this.buffer.length);
        buf.writeBytes(this.buffer);
        buf.writeVarIntToBuffer(this.tileEntityTags.size());
        for (final NBTTagCompound nbttagcompound : this.tileEntityTags) {
            buf.writeNBTTagCompoundToBuffer(nbttagcompound);
        }
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleChunkData(this);
    }
    
    public PacketBuffer getReadBuffer() {
        return new PacketBuffer(Unpooled.wrappedBuffer(this.buffer));
    }
    
    private ByteBuf getWriteBuffer() {
        final ByteBuf bytebuf = Unpooled.wrappedBuffer(this.buffer);
        bytebuf.writerIndex(0);
        return bytebuf;
    }
    
    public int extractChunkData(final PacketBuffer p_189555_1_, final Chunk p_189555_2_, final boolean p_189555_3_, final int p_189555_4_) {
        int i = 0;
        final ExtendedBlockStorage[] aextendedblockstorage = p_189555_2_.getBlockStorageArray();
        for (int j = 0, k = aextendedblockstorage.length; j < k; ++j) {
            final ExtendedBlockStorage extendedblockstorage = aextendedblockstorage[j];
            if (extendedblockstorage != Chunk.NULL_BLOCK_STORAGE && (!this.doChunkLoad() || !extendedblockstorage.isEmpty()) && (p_189555_4_ & 1 << j) != 0x0) {
                i |= 1 << j;
                extendedblockstorage.getData().write(p_189555_1_);
                p_189555_1_.writeBytes(extendedblockstorage.getBlocklightArray().getData());
                if (p_189555_3_) {
                    p_189555_1_.writeBytes(extendedblockstorage.getSkylightArray().getData());
                }
            }
        }
        if (this.doChunkLoad()) {
            p_189555_1_.writeBytes(p_189555_2_.getBiomeArray());
        }
        return i;
    }
    
    protected int calculateChunkSize(final Chunk chunkIn, final boolean p_189556_2_, final int p_189556_3_) {
        int i = 0;
        final ExtendedBlockStorage[] aextendedblockstorage = chunkIn.getBlockStorageArray();
        for (int j = 0, k = aextendedblockstorage.length; j < k; ++j) {
            final ExtendedBlockStorage extendedblockstorage = aextendedblockstorage[j];
            if (extendedblockstorage != Chunk.NULL_BLOCK_STORAGE && (!this.doChunkLoad() || !extendedblockstorage.isEmpty()) && (p_189556_3_ & 1 << j) != 0x0) {
                i += extendedblockstorage.getData().getSerializedSize();
                i += extendedblockstorage.getBlocklightArray().getData().length;
                if (p_189556_2_) {
                    i += extendedblockstorage.getSkylightArray().getData().length;
                }
            }
        }
        if (this.doChunkLoad()) {
            i += chunkIn.getBiomeArray().length;
        }
        return i;
    }
    
    public int getChunkX() {
        return this.chunkX;
    }
    
    public int getChunkZ() {
        return this.chunkZ;
    }
    
    public int getExtractedSize() {
        return this.availableSections;
    }
    
    public boolean doChunkLoad() {
        return this.loadChunk;
    }
    
    public List<NBTTagCompound> getTileEntityTags() {
        return this.tileEntityTags;
    }
}
