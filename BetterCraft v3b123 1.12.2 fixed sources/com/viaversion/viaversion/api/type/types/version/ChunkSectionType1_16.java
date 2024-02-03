// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.api.type.types.version;

import com.viaversion.viaversion.api.minecraft.chunks.DataPalette;
import com.viaversion.viaversion.util.CompactArrayUtil;
import com.viaversion.viaversion.api.minecraft.chunks.PaletteType;
import com.viaversion.viaversion.api.minecraft.chunks.ChunkSectionImpl;
import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.api.minecraft.chunks.ChunkSection;
import com.viaversion.viaversion.api.type.Type;

public class ChunkSectionType1_16 extends Type<ChunkSection>
{
    private static final int GLOBAL_PALETTE = 15;
    
    public ChunkSectionType1_16() {
        super("Chunk Section Type", ChunkSection.class);
    }
    
    @Override
    public ChunkSection read(final ByteBuf buffer) throws Exception {
        int bitsPerBlock = buffer.readUnsignedByte();
        if (bitsPerBlock > 8) {
            bitsPerBlock = 15;
        }
        else if (bitsPerBlock < 4) {
            bitsPerBlock = 4;
        }
        ChunkSection chunkSection;
        if (bitsPerBlock != 15) {
            final int paletteLength = Type.VAR_INT.readPrimitive(buffer);
            chunkSection = new ChunkSectionImpl(false, paletteLength);
            final DataPalette blockPalette = chunkSection.palette(PaletteType.BLOCKS);
            for (int i = 0; i < paletteLength; ++i) {
                blockPalette.addId(Type.VAR_INT.readPrimitive(buffer));
            }
        }
        else {
            chunkSection = new ChunkSectionImpl(false);
        }
        final long[] blockData = Type.LONG_ARRAY_PRIMITIVE.read(buffer);
        if (blockData.length > 0) {
            final char valuesPerLong = (char)(64 / bitsPerBlock);
            final int expectedLength = ('\u1000' + valuesPerLong - 1) / valuesPerLong;
            if (blockData.length == expectedLength) {
                final DataPalette blockPalette2 = chunkSection.palette(PaletteType.BLOCKS);
                CompactArrayUtil.iterateCompactArrayWithPadding(bitsPerBlock, 4096, blockData, (bitsPerBlock == 15) ? blockPalette2::setIdAt : blockPalette2::setPaletteIndexAt);
            }
        }
        return chunkSection;
    }
    
    @Override
    public void write(final ByteBuf buffer, final ChunkSection chunkSection) throws Exception {
        int bitsPerBlock;
        DataPalette blockPalette;
        for (bitsPerBlock = 4, blockPalette = chunkSection.palette(PaletteType.BLOCKS); blockPalette.size() > 1 << bitsPerBlock; ++bitsPerBlock) {}
        if (bitsPerBlock > 8) {
            bitsPerBlock = 15;
        }
        buffer.writeByte(bitsPerBlock);
        if (bitsPerBlock != 15) {
            Type.VAR_INT.writePrimitive(buffer, blockPalette.size());
            for (int i = 0; i < blockPalette.size(); ++i) {
                Type.VAR_INT.writePrimitive(buffer, blockPalette.idByIndex(i));
            }
        }
        final long[] data = CompactArrayUtil.createCompactArrayWithPadding(bitsPerBlock, 4096, (bitsPerBlock == 15) ? blockPalette::idAt : blockPalette::paletteIndexAt);
        Type.LONG_ARRAY_PRIMITIVE.write(buffer, data);
    }
}
