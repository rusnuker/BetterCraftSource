// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.compression;

import io.netty.buffer.ByteBuf;
import java.util.Arrays;

final class Bzip2HuffmanStageEncoder
{
    private static final int HUFFMAN_HIGH_SYMBOL_COST = 15;
    private final Bzip2BitWriter writer;
    private final char[] mtfBlock;
    private final int mtfLength;
    private final int mtfAlphabetSize;
    private final int[] mtfSymbolFrequencies;
    private final int[][] huffmanCodeLengths;
    private final int[][] huffmanMergedCodeSymbols;
    private final byte[] selectors;
    
    Bzip2HuffmanStageEncoder(final Bzip2BitWriter writer, final char[] mtfBlock, final int mtfLength, final int mtfAlphabetSize, final int[] mtfSymbolFrequencies) {
        this.writer = writer;
        this.mtfBlock = mtfBlock;
        this.mtfLength = mtfLength;
        this.mtfAlphabetSize = mtfAlphabetSize;
        this.mtfSymbolFrequencies = mtfSymbolFrequencies;
        final int totalTables = selectTableCount(mtfLength);
        this.huffmanCodeLengths = new int[totalTables][mtfAlphabetSize];
        this.huffmanMergedCodeSymbols = new int[totalTables][mtfAlphabetSize];
        this.selectors = new byte[(mtfLength + 50 - 1) / 50];
    }
    
    private static int selectTableCount(final int mtfLength) {
        if (mtfLength >= 2400) {
            return 6;
        }
        if (mtfLength >= 1200) {
            return 5;
        }
        if (mtfLength >= 600) {
            return 4;
        }
        if (mtfLength >= 200) {
            return 3;
        }
        return 2;
    }
    
    private static void generateHuffmanCodeLengths(final int alphabetSize, final int[] symbolFrequencies, final int[] codeLengths) {
        final int[] mergedFrequenciesAndIndices = new int[alphabetSize];
        final int[] sortedFrequencies = new int[alphabetSize];
        for (int i = 0; i < alphabetSize; ++i) {
            mergedFrequenciesAndIndices[i] = (symbolFrequencies[i] << 9 | i);
        }
        Arrays.sort(mergedFrequenciesAndIndices);
        for (int i = 0; i < alphabetSize; ++i) {
            sortedFrequencies[i] = mergedFrequenciesAndIndices[i] >>> 9;
        }
        Bzip2HuffmanAllocator.allocateHuffmanCodeLengths(sortedFrequencies, 20);
        for (int i = 0; i < alphabetSize; ++i) {
            codeLengths[mergedFrequenciesAndIndices[i] & 0x1FF] = sortedFrequencies[i];
        }
    }
    
    private void generateHuffmanOptimisationSeeds() {
        final int[][] huffmanCodeLengths = this.huffmanCodeLengths;
        final int[] mtfSymbolFrequencies = this.mtfSymbolFrequencies;
        final int mtfAlphabetSize = this.mtfAlphabetSize;
        final int totalTables = huffmanCodeLengths.length;
        int remainingLength = this.mtfLength;
        int lowCostEnd = -1;
        for (int i = 0; i < totalTables; ++i) {
            final int targetCumulativeFrequency = remainingLength / (totalTables - i);
            final int lowCostStart = lowCostEnd + 1;
            int actualCumulativeFrequency;
            for (actualCumulativeFrequency = 0; actualCumulativeFrequency < targetCumulativeFrequency && lowCostEnd < mtfAlphabetSize - 1; actualCumulativeFrequency += mtfSymbolFrequencies[++lowCostEnd]) {}
            if (lowCostEnd > lowCostStart && i != 0 && i != totalTables - 1 && (totalTables - i & 0x1) == 0x0) {
                actualCumulativeFrequency -= mtfSymbolFrequencies[lowCostEnd--];
            }
            final int[] tableCodeLengths = huffmanCodeLengths[i];
            for (int j = 0; j < mtfAlphabetSize; ++j) {
                if (j < lowCostStart || j > lowCostEnd) {
                    tableCodeLengths[j] = 15;
                }
            }
            remainingLength -= actualCumulativeFrequency;
        }
    }
    
    private void optimiseSelectorsAndHuffmanTables(final boolean storeSelectors) {
        final char[] mtfBlock = this.mtfBlock;
        final byte[] selectors = this.selectors;
        final int[][] huffmanCodeLengths = this.huffmanCodeLengths;
        final int mtfLength = this.mtfLength;
        final int mtfAlphabetSize = this.mtfAlphabetSize;
        final int totalTables = huffmanCodeLengths.length;
        final int[][] tableFrequencies = new int[totalTables][mtfAlphabetSize];
        int selectorIndex = 0;
        int groupEnd;
        for (int groupStart = 0; groupStart < mtfLength; groupStart = groupEnd + 1) {
            groupEnd = Math.min(groupStart + 50, mtfLength) - 1;
            final short[] cost = new short[totalTables];
            for (int i = groupStart; i <= groupEnd; ++i) {
                final int value = mtfBlock[i];
                for (int j = 0; j < totalTables; ++j) {
                    final short[] array = cost;
                    final int n = j;
                    array[n] += (short)huffmanCodeLengths[j][value];
                }
            }
            byte bestTable = 0;
            int bestCost = cost[0];
            for (byte k = 1; k < totalTables; ++k) {
                final int tableCost = cost[k];
                if (tableCost < bestCost) {
                    bestCost = tableCost;
                    bestTable = k;
                }
            }
            final int[] bestGroupFrequencies = tableFrequencies[bestTable];
            for (int l = groupStart; l <= groupEnd; ++l) {
                final int[] array2 = bestGroupFrequencies;
                final char c = mtfBlock[l];
                ++array2[c];
            }
            if (storeSelectors) {
                selectors[selectorIndex++] = bestTable;
            }
        }
        for (int m = 0; m < totalTables; ++m) {
            generateHuffmanCodeLengths(mtfAlphabetSize, tableFrequencies[m], huffmanCodeLengths[m]);
        }
    }
    
    private void assignHuffmanCodeSymbols() {
        final int[][] huffmanMergedCodeSymbols = this.huffmanMergedCodeSymbols;
        final int[][] huffmanCodeLengths = this.huffmanCodeLengths;
        final int mtfAlphabetSize = this.mtfAlphabetSize;
        for (int totalTables = huffmanCodeLengths.length, i = 0; i < totalTables; ++i) {
            final int[] tableLengths = huffmanCodeLengths[i];
            int minimumLength = 32;
            int maximumLength = 0;
            for (final int length : tableLengths) {
                if (length > maximumLength) {
                    maximumLength = length;
                }
                if (length < minimumLength) {
                    minimumLength = length;
                }
            }
            int code = 0;
            for (int k = minimumLength; k <= maximumLength; ++k) {
                for (int l = 0; l < mtfAlphabetSize; ++l) {
                    if ((huffmanCodeLengths[i][l] & 0xFF) == k) {
                        huffmanMergedCodeSymbols[i][l] = (k << 24 | code);
                        ++code;
                    }
                }
                code <<= 1;
            }
        }
    }
    
    private void writeSelectorsAndHuffmanTables(final ByteBuf out) {
        final Bzip2BitWriter writer = this.writer;
        final byte[] selectors = this.selectors;
        final int totalSelectors = selectors.length;
        final int[][] huffmanCodeLengths = this.huffmanCodeLengths;
        final int totalTables = huffmanCodeLengths.length;
        final int mtfAlphabetSize = this.mtfAlphabetSize;
        writer.writeBits(out, 3, totalTables);
        writer.writeBits(out, 15, totalSelectors);
        final Bzip2MoveToFrontTable selectorMTF = new Bzip2MoveToFrontTable();
        for (final byte selector : selectors) {
            writer.writeUnary(out, selectorMTF.valueToFront(selector));
        }
        for (final int[] tableLengths : huffmanCodeLengths) {
            int currentLength = tableLengths[0];
            writer.writeBits(out, 5, currentLength);
            for (final int codeLength : tableLengths) {
                final int value = (currentLength < codeLength) ? 2 : 3;
                int delta = Math.abs(codeLength - currentLength);
                while (delta-- > 0) {
                    writer.writeBits(out, 2, value);
                }
                writer.writeBoolean(out, false);
                currentLength = codeLength;
            }
        }
    }
    
    private void writeBlockData(final ByteBuf out) {
        final Bzip2BitWriter writer = this.writer;
        final int[][] huffmanMergedCodeSymbols = this.huffmanMergedCodeSymbols;
        final byte[] selectors = this.selectors;
        final char[] mtf = this.mtfBlock;
        final int mtfLength = this.mtfLength;
        int selectorIndex = 0;
        int mtfIndex = 0;
        while (mtfIndex < mtfLength) {
            final int groupEnd = Math.min(mtfIndex + 50, mtfLength) - 1;
            final int[] tableMergedCodeSymbols = huffmanMergedCodeSymbols[selectors[selectorIndex++]];
            while (mtfIndex <= groupEnd) {
                final int mergedCodeSymbol = tableMergedCodeSymbols[mtf[mtfIndex++]];
                writer.writeBits(out, mergedCodeSymbol >>> 24, mergedCodeSymbol);
            }
        }
    }
    
    void encode(final ByteBuf out) {
        this.generateHuffmanOptimisationSeeds();
        for (int i = 3; i >= 0; --i) {
            this.optimiseSelectorsAndHuffmanTables(i == 0);
        }
        this.assignHuffmanCodeSymbols();
        this.writeSelectorsAndHuffmanTables(out);
        this.writeBlockData(out);
    }
}
