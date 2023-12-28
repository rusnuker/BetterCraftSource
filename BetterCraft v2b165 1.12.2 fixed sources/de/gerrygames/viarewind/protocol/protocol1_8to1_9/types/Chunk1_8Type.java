// 
// Decompiled by Procyon v0.6.0
// 

package de.gerrygames.viarewind.protocol.protocol1_8to1_9.types;

import com.viaversion.viaversion.api.minecraft.Environment;
import java.util.List;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import java.util.ArrayList;
import java.util.logging.Level;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.minecraft.chunks.Chunk1_8;
import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.api.minecraft.chunks.ChunkSection;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.protocols.protocol1_9_3to1_9_1_2.storage.ClientWorld;
import com.viaversion.viaversion.api.minecraft.chunks.Chunk;
import com.viaversion.viaversion.api.type.PartialType;

public class Chunk1_8Type extends PartialType<Chunk, ClientWorld>
{
    private static final Type<ChunkSection> CHUNK_SECTION_TYPE;
    
    public Chunk1_8Type(final ClientWorld param) {
        super(param, Chunk.class);
    }
    
    @Override
    public Chunk read(final ByteBuf input, final ClientWorld world) throws Exception {
        final int chunkX = input.readInt();
        final int chunkZ = input.readInt();
        final boolean groundUp = input.readByte() != 0;
        final int bitmask = input.readUnsignedShort();
        final int dataLength = Type.VAR_INT.readPrimitive(input);
        if (bitmask == 0 && groundUp) {
            if (dataLength >= 256) {
                input.readerIndex(input.readerIndex() + 256);
            }
            return new Chunk1_8(chunkX, chunkZ);
        }
        final ChunkSection[] sections = new ChunkSection[16];
        int[] biomeData = null;
        final int startIndex = input.readerIndex();
        for (int i = 0; i < 16; ++i) {
            if ((bitmask & 1 << i) != 0x0) {
                sections[i] = Chunk1_8Type.CHUNK_SECTION_TYPE.read(input);
            }
        }
        for (int i = 0; i < 16; ++i) {
            if ((bitmask & 1 << i) != 0x0) {
                sections[i].getLight().readBlockLight(input);
            }
        }
        int bytesLeft = dataLength - (input.readerIndex() - startIndex);
        if (bytesLeft >= 2048) {
            for (int j = 0; j < 16; ++j) {
                if ((bitmask & 1 << j) != 0x0) {
                    sections[j].getLight().readSkyLight(input);
                    bytesLeft -= 2048;
                }
            }
        }
        if (bytesLeft >= 256) {
            biomeData = new int[256];
            for (int j = 0; j < 256; ++j) {
                biomeData[j] = (input.readByte() & 0xFF);
            }
            bytesLeft -= 256;
        }
        if (bytesLeft > 0) {
            Via.getPlatform().getLogger().log(Level.WARNING, bytesLeft + " Bytes left after reading chunks! (" + groundUp + ")");
        }
        return new Chunk1_8(chunkX, chunkZ, groundUp, bitmask, sections, biomeData, new ArrayList<CompoundTag>());
    }
    
    @Override
    public void write(final ByteBuf output, final ClientWorld world, final Chunk chunk) throws Exception {
        final ByteBuf buf = output.alloc().buffer();
        for (int i = 0; i < chunk.getSections().length; ++i) {
            if ((chunk.getBitmask() & 1 << i) != 0x0) {
                Chunk1_8Type.CHUNK_SECTION_TYPE.write(buf, chunk.getSections()[i]);
            }
        }
        for (int i = 0; i < chunk.getSections().length; ++i) {
            if ((chunk.getBitmask() & 1 << i) != 0x0) {
                chunk.getSections()[i].getLight().writeBlockLight(buf);
            }
        }
        final boolean skyLight = world.getEnvironment() == Environment.NORMAL;
        if (skyLight) {
            for (int j = 0; j < chunk.getSections().length; ++j) {
                if ((chunk.getBitmask() & 1 << j) != 0x0) {
                    chunk.getSections()[j].getLight().writeSkyLight(buf);
                }
            }
        }
        if (chunk.isFullChunk() && chunk.isBiomeData()) {
            for (final int biome : chunk.getBiomeData()) {
                buf.writeByte((byte)biome);
            }
        }
        output.writeInt(chunk.getX());
        output.writeInt(chunk.getZ());
        output.writeBoolean(chunk.isFullChunk());
        output.writeShort(chunk.getBitmask());
        Type.VAR_INT.writePrimitive(output, buf.readableBytes());
        output.writeBytes(buf, buf.readableBytes());
        buf.release();
    }
    
    static {
        CHUNK_SECTION_TYPE = new ChunkSectionType1_8();
    }
}
