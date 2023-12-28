// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.protocols.protocol1_9to1_8.types;

import com.viaversion.viaversion.api.type.types.version.Types1_9;
import java.util.List;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import java.util.ArrayList;
import java.util.logging.Level;
import com.viaversion.viaversion.api.type.types.version.Types1_8;
import com.viaversion.viaversion.api.minecraft.chunks.Chunk1_8;
import com.viaversion.viaversion.api.minecraft.chunks.ChunkSection;
import java.util.BitSet;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.protocols.protocol1_10to1_9_3.Protocol1_10To1_9_3_4;
import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.api.type.types.minecraft.BaseChunkType;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.protocols.protocol1_9to1_8.storage.ClientChunks;
import com.viaversion.viaversion.api.minecraft.chunks.Chunk;
import com.viaversion.viaversion.api.type.PartialType;

public class Chunk1_9to1_8Type extends PartialType<Chunk, ClientChunks>
{
    public static final int SECTION_COUNT = 16;
    private static final int SECTION_SIZE = 16;
    private static final int BIOME_DATA_LENGTH = 256;
    
    public Chunk1_9to1_8Type(final ClientChunks chunks) {
        super(chunks, Chunk.class);
    }
    
    private static long toLong(final int msw, final int lsw) {
        return ((long)msw << 32) + lsw + 2147483648L;
    }
    
    @Override
    public Class<? extends Type> getBaseClass() {
        return BaseChunkType.class;
    }
    
    @Override
    public Chunk read(final ByteBuf input, final ClientChunks param) throws Exception {
        final boolean replacePistons = param.getUser().getProtocolInfo().getPipeline().contains(Protocol1_10To1_9_3_4.class) && Via.getConfig().isReplacePistons();
        final int replacementId = Via.getConfig().getPistonReplacementId();
        final int chunkX = input.readInt();
        final int chunkZ = input.readInt();
        final long chunkHash = toLong(chunkX, chunkZ);
        final boolean fullChunk = input.readByte() != 0;
        final int bitmask = input.readUnsignedShort();
        final int dataLength = Type.VAR_INT.readPrimitive(input);
        final BitSet usedSections = new BitSet(16);
        final ChunkSection[] sections = new ChunkSection[16];
        int[] biomeData = null;
        for (int i = 0; i < 16; ++i) {
            if ((bitmask & 1 << i) != 0x0) {
                usedSections.set(i);
            }
        }
        final int sectionCount = usedSections.cardinality();
        final boolean isBulkPacket = param.getBulkChunks().remove(chunkHash);
        if (sectionCount == 0 && fullChunk && !isBulkPacket && param.getLoadedChunks().contains(chunkHash)) {
            param.getLoadedChunks().remove(chunkHash);
            return new Chunk1_8(chunkX, chunkZ);
        }
        final int startIndex = input.readerIndex();
        param.getLoadedChunks().add(chunkHash);
        for (int j = 0; j < 16; ++j) {
            if (usedSections.get(j)) {
                final ChunkSection section = Types1_8.CHUNK_SECTION.read(input);
                sections[j] = section;
                if (replacePistons) {
                    section.replacePaletteEntry(36, replacementId);
                }
            }
        }
        for (int j = 0; j < 16; ++j) {
            if (usedSections.get(j)) {
                sections[j].getLight().readBlockLight(input);
            }
        }
        int bytesLeft = dataLength - (input.readerIndex() - startIndex);
        if (bytesLeft >= 2048) {
            for (int k = 0; k < 16; ++k) {
                if (usedSections.get(k)) {
                    sections[k].getLight().readSkyLight(input);
                    bytesLeft -= 2048;
                }
            }
        }
        if (bytesLeft >= 256) {
            biomeData = new int[256];
            for (int k = 0; k < 256; ++k) {
                biomeData[k] = (input.readByte() & 0xFF);
            }
            bytesLeft -= 256;
        }
        if (bytesLeft > 0) {
            Via.getPlatform().getLogger().log(Level.WARNING, bytesLeft + " Bytes left after reading chunks! (" + fullChunk + ")");
        }
        return new Chunk1_8(chunkX, chunkZ, fullChunk, bitmask, sections, biomeData, new ArrayList<CompoundTag>());
    }
    
    @Override
    public void write(final ByteBuf output, final ClientChunks param, final Chunk input) throws Exception {
        if (!(input instanceof Chunk1_8)) {
            throw new Exception("Incompatible chunk, " + input.getClass());
        }
        final Chunk1_8 chunk = (Chunk1_8)input;
        output.writeInt(chunk.getX());
        output.writeInt(chunk.getZ());
        if (chunk.isUnloadPacket()) {
            return;
        }
        output.writeByte(chunk.isFullChunk() ? 1 : 0);
        Type.VAR_INT.writePrimitive(output, chunk.getBitmask());
        final ByteBuf buf = output.alloc().buffer();
        try {
            for (int i = 0; i < 16; ++i) {
                final ChunkSection section = chunk.getSections()[i];
                if (section != null) {
                    Types1_9.CHUNK_SECTION.write(buf, section);
                    section.getLight().writeBlockLight(buf);
                    if (section.getLight().hasSkyLight()) {
                        section.getLight().writeSkyLight(buf);
                    }
                }
            }
            buf.readerIndex(0);
            Type.VAR_INT.writePrimitive(output, buf.readableBytes() + (chunk.hasBiomeData() ? 256 : 0));
            output.writeBytes(buf);
        }
        finally {
            buf.release();
        }
        if (chunk.hasBiomeData()) {
            for (final int biome : chunk.getBiomeData()) {
                output.writeByte((byte)biome);
            }
        }
    }
}
