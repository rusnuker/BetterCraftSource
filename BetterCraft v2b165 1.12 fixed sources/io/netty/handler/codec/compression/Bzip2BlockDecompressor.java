// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.compression;

final class Bzip2BlockDecompressor
{
    private final Bzip2BitReader reader;
    private final Crc32 crc;
    private final int blockCRC;
    private final boolean blockRandomised;
    int huffmanEndOfBlockSymbol;
    int huffmanInUse16;
    final byte[] huffmanSymbolMap;
    private final int[] bwtByteCounts;
    private final byte[] bwtBlock;
    private final int bwtStartPointer;
    private int[] bwtMergedPointers;
    private int bwtCurrentMergedPointer;
    private int bwtBlockLength;
    private int bwtBytesDecoded;
    private int rleLastDecodedByte;
    private int rleAccumulator;
    private int rleRepeat;
    private int randomIndex;
    private int randomCount;
    private final Bzip2MoveToFrontTable symbolMTF;
    private int repeatCount;
    private int repeatIncrement;
    private int mtfValue;
    
    Bzip2BlockDecompressor(final int blockSize, final int blockCRC, final boolean blockRandomised, final int bwtStartPointer, final Bzip2BitReader reader) {
        this.crc = new Crc32();
        this.huffmanSymbolMap = new byte[256];
        this.bwtByteCounts = new int[256];
        this.rleLastDecodedByte = -1;
        this.randomCount = Bzip2Rand.rNums(0) - 1;
        this.symbolMTF = new Bzip2MoveToFrontTable();
        this.repeatIncrement = 1;
        this.bwtBlock = new byte[blockSize];
        this.blockCRC = blockCRC;
        this.blockRandomised = blockRandomised;
        this.bwtStartPointer = bwtStartPointer;
        this.reader = reader;
    }
    
    boolean decodeHuffmanData(final Bzip2HuffmanStageDecoder huffmanDecoder) {
        final Bzip2BitReader reader = this.reader;
        final byte[] bwtBlock = this.bwtBlock;
        final byte[] huffmanSymbolMap = this.huffmanSymbolMap;
        final int streamBlockSize = this.bwtBlock.length;
        final int huffmanEndOfBlockSymbol = this.huffmanEndOfBlockSymbol;
        final int[] bwtByteCounts = this.bwtByteCounts;
        final Bzip2MoveToFrontTable symbolMTF = this.symbolMTF;
        int bwtBlockLength = this.bwtBlockLength;
        int repeatCount = this.repeatCount;
        int repeatIncrement = this.repeatIncrement;
        int mtfValue = this.mtfValue;
        while (reader.hasReadableBits(23)) {
            final int nextSymbol = huffmanDecoder.nextSymbol();
            if (nextSymbol == 0) {
                repeatCount += repeatIncrement;
                repeatIncrement <<= 1;
            }
            else if (nextSymbol == 1) {
                repeatCount += repeatIncrement << 1;
                repeatIncrement <<= 1;
            }
            else {
                if (repeatCount > 0) {
                    if (bwtBlockLength + repeatCount > streamBlockSize) {
                        throw new DecompressionException("block exceeds declared block size");
                    }
                    final byte nextByte = huffmanSymbolMap[mtfValue];
                    final int[] array = bwtByteCounts;
                    final int n = nextByte & 0xFF;
                    array[n] += repeatCount;
                    while (--repeatCount >= 0) {
                        bwtBlock[bwtBlockLength++] = nextByte;
                    }
                    repeatCount = 0;
                    repeatIncrement = 1;
                }
                if (nextSymbol == huffmanEndOfBlockSymbol) {
                    this.bwtBlockLength = bwtBlockLength;
                    this.initialiseInverseBWT();
                    return true;
                }
                if (bwtBlockLength >= streamBlockSize) {
                    throw new DecompressionException("block exceeds declared block size");
                }
                mtfValue = (symbolMTF.indexToFront(nextSymbol - 1) & 0xFF);
                final byte nextByte = huffmanSymbolMap[mtfValue];
                final int[] array2 = bwtByteCounts;
                final int n2 = nextByte & 0xFF;
                ++array2[n2];
                bwtBlock[bwtBlockLength++] = nextByte;
            }
        }
        this.bwtBlockLength = bwtBlockLength;
        this.repeatCount = repeatCount;
        this.repeatIncrement = repeatIncrement;
        this.mtfValue = mtfValue;
        return false;
    }
    
    private void initialiseInverseBWT() {
        final int bwtStartPointer = this.bwtStartPointer;
        final byte[] bwtBlock = this.bwtBlock;
        final int[] bwtMergedPointers = new int[this.bwtBlockLength];
        final int[] characterBase = new int[256];
        if (bwtStartPointer < 0 || bwtStartPointer >= this.bwtBlockLength) {
            throw new DecompressionException("start pointer invalid");
        }
        System.arraycopy(this.bwtByteCounts, 0, characterBase, 1, 255);
        for (int i = 2; i <= 255; ++i) {
            final int[] array = characterBase;
            final int n = i;
            array[n] += characterBase[i - 1];
        }
        for (int i = 0; i < this.bwtBlockLength; ++i) {
            final int value = bwtBlock[i] & 0xFF;
            bwtMergedPointers[characterBase[value]++] = (i << 8) + value;
        }
        this.bwtMergedPointers = bwtMergedPointers;
        this.bwtCurrentMergedPointer = bwtMergedPointers[bwtStartPointer];
    }
    
    public int read() {
        while (this.rleRepeat < 1) {
            if (this.bwtBytesDecoded == this.bwtBlockLength) {
                return -1;
            }
            final int nextByte = this.decodeNextBWTByte();
            if (nextByte != this.rleLastDecodedByte) {
                this.rleLastDecodedByte = nextByte;
                this.rleRepeat = 1;
                this.rleAccumulator = 1;
                this.crc.updateCRC(nextByte);
            }
            else if (++this.rleAccumulator == 4) {
                final int rleRepeat = this.decodeNextBWTByte() + 1;
                this.rleRepeat = rleRepeat;
                this.rleAccumulator = 0;
                this.crc.updateCRC(nextByte, rleRepeat);
            }
            else {
                this.rleRepeat = 1;
                this.crc.updateCRC(nextByte);
            }
        }
        --this.rleRepeat;
        return this.rleLastDecodedByte;
    }
    
    private int decodeNextBWTByte() {
        final int mergedPointer = this.bwtCurrentMergedPointer;
        int nextDecodedByte = mergedPointer & 0xFF;
        this.bwtCurrentMergedPointer = this.bwtMergedPointers[mergedPointer >>> 8];
        if (this.blockRandomised && --this.randomCount == 0) {
            nextDecodedByte ^= 0x1;
            this.randomIndex = (this.randomIndex + 1) % 512;
            this.randomCount = Bzip2Rand.rNums(this.randomIndex);
        }
        ++this.bwtBytesDecoded;
        return nextDecodedByte;
    }
    
    public int blockLength() {
        return this.bwtBlockLength;
    }
    
    int checkCRC() {
        final int computedBlockCRC = this.crc.getCRC();
        if (this.blockCRC != computedBlockCRC) {
            throw new DecompressionException("block CRC error");
        }
        return computedBlockCRC;
    }
}
