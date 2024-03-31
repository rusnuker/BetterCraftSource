// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.compression;

final class Bzip2MTFAndRLE2StageEncoder
{
    private final int[] bwtBlock;
    private final int bwtLength;
    private final boolean[] bwtValuesPresent;
    private final char[] mtfBlock;
    private int mtfLength;
    private final int[] mtfSymbolFrequencies;
    private int alphabetSize;
    
    Bzip2MTFAndRLE2StageEncoder(final int[] bwtBlock, final int bwtLength, final boolean[] bwtValuesPresent) {
        this.mtfSymbolFrequencies = new int[258];
        this.bwtBlock = bwtBlock;
        this.bwtLength = bwtLength;
        this.bwtValuesPresent = bwtValuesPresent;
        this.mtfBlock = new char[bwtLength + 1];
    }
    
    void encode() {
        final int bwtLength = this.bwtLength;
        final boolean[] bwtValuesPresent = this.bwtValuesPresent;
        final int[] bwtBlock = this.bwtBlock;
        final char[] mtfBlock = this.mtfBlock;
        final int[] mtfSymbolFrequencies = this.mtfSymbolFrequencies;
        final byte[] huffmanSymbolMap = new byte[256];
        final Bzip2MoveToFrontTable symbolMTF = new Bzip2MoveToFrontTable();
        int totalUniqueValues = 0;
        for (int i = 0; i < huffmanSymbolMap.length; ++i) {
            if (bwtValuesPresent[i]) {
                huffmanSymbolMap[i] = (byte)(totalUniqueValues++);
            }
        }
        final int endOfBlockSymbol = totalUniqueValues + 1;
        int mtfIndex = 0;
        int repeatCount = 0;
        int totalRunAs = 0;
        int totalRunBs = 0;
        for (int j = 0; j < bwtLength; ++j) {
            final int mtfPosition = symbolMTF.valueToFront(huffmanSymbolMap[bwtBlock[j] & 0xFF]);
            if (mtfPosition == 0) {
                ++repeatCount;
            }
            else {
                if (repeatCount > 0) {
                    --repeatCount;
                    while (true) {
                        if ((repeatCount & 0x1) == 0x0) {
                            mtfBlock[mtfIndex++] = '\0';
                            ++totalRunAs;
                        }
                        else {
                            mtfBlock[mtfIndex++] = '\u0001';
                            ++totalRunBs;
                        }
                        if (repeatCount <= 1) {
                            break;
                        }
                        repeatCount = repeatCount - 2 >>> 1;
                    }
                    repeatCount = 0;
                }
                mtfBlock[mtfIndex++] = (char)(mtfPosition + 1);
                final int[] array = mtfSymbolFrequencies;
                final int n = mtfPosition + 1;
                ++array[n];
            }
        }
        if (repeatCount > 0) {
            --repeatCount;
            while (true) {
                if ((repeatCount & 0x1) == 0x0) {
                    mtfBlock[mtfIndex++] = '\0';
                    ++totalRunAs;
                }
                else {
                    mtfBlock[mtfIndex++] = '\u0001';
                    ++totalRunBs;
                }
                if (repeatCount <= 1) {
                    break;
                }
                repeatCount = repeatCount - 2 >>> 1;
            }
        }
        mtfBlock[mtfIndex] = (char)endOfBlockSymbol;
        final int[] array2 = mtfSymbolFrequencies;
        final int n2 = endOfBlockSymbol;
        ++array2[n2];
        final int[] array3 = mtfSymbolFrequencies;
        final int n3 = 0;
        array3[n3] += totalRunAs;
        final int[] array4 = mtfSymbolFrequencies;
        final int n4 = 1;
        array4[n4] += totalRunBs;
        this.mtfLength = mtfIndex + 1;
        this.alphabetSize = endOfBlockSymbol + 1;
    }
    
    char[] mtfBlock() {
        return this.mtfBlock;
    }
    
    int mtfLength() {
        return this.mtfLength;
    }
    
    int mtfAlphabetSize() {
        return this.alphabetSize;
    }
    
    int[] mtfSymbolFrequencies() {
        return this.mtfSymbolFrequencies;
    }
}
