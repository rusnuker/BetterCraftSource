// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.compression;

import io.netty.buffer.ByteBuf;
import io.netty.util.ByteProcessor;

final class Bzip2BlockCompressor
{
    private final ByteProcessor writeProcessor;
    private final Bzip2BitWriter writer;
    private final Crc32 crc;
    private final byte[] block;
    private int blockLength;
    private final int blockLengthLimit;
    private final boolean[] blockValuesPresent;
    private final int[] bwtBlock;
    private int rleCurrentValue;
    private int rleLength;
    
    Bzip2BlockCompressor(final Bzip2BitWriter writer, final int blockSize) {
        this.writeProcessor = new ByteProcessor() {
            @Override
            public boolean process(final byte value) throws Exception {
                return Bzip2BlockCompressor.this.write(value);
            }
        };
        this.crc = new Crc32();
        this.blockValuesPresent = new boolean[256];
        this.rleCurrentValue = -1;
        this.writer = writer;
        this.block = new byte[blockSize + 1];
        this.bwtBlock = new int[blockSize + 1];
        this.blockLengthLimit = blockSize - 6;
    }
    
    private void writeSymbolMap(final ByteBuf out) {
        final Bzip2BitWriter writer = this.writer;
        final boolean[] blockValuesPresent = this.blockValuesPresent;
        final boolean[] condensedInUse = new boolean[16];
        for (int i = 0; i < condensedInUse.length; ++i) {
            for (int j = 0, k = i << 4; j < 16; ++j, ++k) {
                if (blockValuesPresent[k]) {
                    condensedInUse[i] = true;
                }
            }
        }
        for (final boolean isCondensedInUse : condensedInUse) {
            writer.writeBoolean(out, isCondensedInUse);
        }
        for (int i = 0; i < condensedInUse.length; ++i) {
            if (condensedInUse[i]) {
                for (int j = 0, k = i << 4; j < 16; ++j, ++k) {
                    writer.writeBoolean(out, blockValuesPresent[k]);
                }
            }
        }
    }
    
    private void writeRun(final int value, int runLength) {
        final int blockLength = this.blockLength;
        final byte[] block = this.block;
        this.blockValuesPresent[value] = true;
        this.crc.updateCRC(value, runLength);
        final byte byteValue = (byte)value;
        switch (runLength) {
            case 1: {
                block[blockLength] = byteValue;
                this.blockLength = blockLength + 1;
                break;
            }
            case 2: {
                block[blockLength + 1] = (block[blockLength] = byteValue);
                this.blockLength = blockLength + 2;
                break;
            }
            case 3: {
                block[blockLength] = byteValue;
                block[blockLength + 2] = (block[blockLength + 1] = byteValue);
                this.blockLength = blockLength + 3;
                break;
            }
            default: {
                runLength -= 4;
                this.blockValuesPresent[runLength] = true;
                block[blockLength + 1] = (block[blockLength] = byteValue);
                block[blockLength + 3] = (block[blockLength + 2] = byteValue);
                block[blockLength + 4] = (byte)runLength;
                this.blockLength = blockLength + 5;
                break;
            }
        }
    }
    
    boolean write(final int value) {
        if (this.blockLength > this.blockLengthLimit) {
            return false;
        }
        final int rleCurrentValue = this.rleCurrentValue;
        final int rleLength = this.rleLength;
        if (rleLength == 0) {
            this.rleCurrentValue = value;
            this.rleLength = 1;
        }
        else if (rleCurrentValue != value) {
            this.writeRun(rleCurrentValue & 0xFF, rleLength);
            this.rleCurrentValue = value;
            this.rleLength = 1;
        }
        else if (rleLength == 254) {
            this.writeRun(rleCurrentValue & 0xFF, 255);
            this.rleLength = 0;
        }
        else {
            this.rleLength = rleLength + 1;
        }
        return true;
    }
    
    int write(final ByteBuf buffer, final int offset, final int length) {
        final int index = buffer.forEachByte(offset, length, this.writeProcessor);
        return (index == -1) ? length : (index - offset);
    }
    
    void close(final ByteBuf out) {
        if (this.rleLength > 0) {
            this.writeRun(this.rleCurrentValue & 0xFF, this.rleLength);
        }
        this.block[this.blockLength] = this.block[0];
        final Bzip2DivSufSort divSufSort = new Bzip2DivSufSort(this.block, this.bwtBlock, this.blockLength);
        final int bwtStartPointer = divSufSort.bwt();
        final Bzip2BitWriter writer = this.writer;
        writer.writeBits(out, 24, 3227993L);
        writer.writeBits(out, 24, 2511705L);
        writer.writeInt(out, this.crc.getCRC());
        writer.writeBoolean(out, false);
        writer.writeBits(out, 24, bwtStartPointer);
        this.writeSymbolMap(out);
        final Bzip2MTFAndRLE2StageEncoder mtfEncoder = new Bzip2MTFAndRLE2StageEncoder(this.bwtBlock, this.blockLength, this.blockValuesPresent);
        mtfEncoder.encode();
        final Bzip2HuffmanStageEncoder huffmanEncoder = new Bzip2HuffmanStageEncoder(writer, mtfEncoder.mtfBlock(), mtfEncoder.mtfLength(), mtfEncoder.mtfAlphabetSize(), mtfEncoder.mtfSymbolFrequencies());
        huffmanEncoder.encode(out);
    }
    
    int availableSize() {
        if (this.blockLength == 0) {
            return this.blockLengthLimit + 2;
        }
        return this.blockLengthLimit - this.blockLength + 1;
    }
    
    boolean isFull() {
        return this.blockLength > this.blockLengthLimit;
    }
    
    boolean isEmpty() {
        return this.blockLength == 0 && this.rleLength == 0;
    }
    
    int crc() {
        return this.crc.getCRC();
    }
}
