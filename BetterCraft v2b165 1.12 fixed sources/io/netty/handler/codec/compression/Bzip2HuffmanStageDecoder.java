// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.compression;

final class Bzip2HuffmanStageDecoder
{
    private final Bzip2BitReader reader;
    byte[] selectors;
    private final int[] minimumLengths;
    private final int[][] codeBases;
    private final int[][] codeLimits;
    private final int[][] codeSymbols;
    private int currentTable;
    private int groupIndex;
    private int groupPosition;
    final int totalTables;
    final int alphabetSize;
    final Bzip2MoveToFrontTable tableMTF;
    int currentSelector;
    final byte[][] tableCodeLengths;
    int currentGroup;
    int currentLength;
    int currentAlpha;
    boolean modifyLength;
    
    Bzip2HuffmanStageDecoder(final Bzip2BitReader reader, final int totalTables, final int alphabetSize) {
        this.groupIndex = -1;
        this.groupPosition = -1;
        this.tableMTF = new Bzip2MoveToFrontTable();
        this.currentLength = -1;
        this.reader = reader;
        this.totalTables = totalTables;
        this.alphabetSize = alphabetSize;
        this.minimumLengths = new int[totalTables];
        this.codeBases = new int[totalTables][25];
        this.codeLimits = new int[totalTables][24];
        this.codeSymbols = new int[totalTables][258];
        this.tableCodeLengths = new byte[totalTables][258];
    }
    
    void createHuffmanDecodingTables() {
        final int alphabetSize = this.alphabetSize;
        for (int table = 0; table < this.tableCodeLengths.length; ++table) {
            final int[] tableBases = this.codeBases[table];
            final int[] tableLimits = this.codeLimits[table];
            final int[] tableSymbols = this.codeSymbols[table];
            final byte[] codeLengths = this.tableCodeLengths[table];
            int minimumLength = 23;
            int maximumLength = 0;
            for (final byte currLength : codeLengths) {
                maximumLength = Math.max(currLength, maximumLength);
                minimumLength = Math.min(currLength, minimumLength);
            }
            this.minimumLengths[table] = minimumLength;
            for (int i = 0; i < alphabetSize; ++i) {
                final int[] array = tableBases;
                final int n = codeLengths[i] + 1;
                ++array[n];
            }
            int i = 1;
            int b = tableBases[0];
            while (i < 25) {
                b += tableBases[i];
                tableBases[i] = b;
                ++i;
            }
            i = minimumLength;
            int code = 0;
            while (i <= maximumLength) {
                final int base = code;
                code += tableBases[i + 1] - tableBases[i];
                tableBases[i] = base - tableBases[i];
                tableLimits[i] = code - 1;
                code <<= 1;
                ++i;
            }
            int bitLength = minimumLength;
            int codeIndex = 0;
            while (bitLength <= maximumLength) {
                for (int symbol = 0; symbol < alphabetSize; ++symbol) {
                    if (codeLengths[symbol] == bitLength) {
                        tableSymbols[codeIndex++] = symbol;
                    }
                }
                ++bitLength;
            }
        }
        this.currentTable = this.selectors[0];
    }
    
    int nextSymbol() {
        if (++this.groupPosition % 50 == 0) {
            ++this.groupIndex;
            if (this.groupIndex == this.selectors.length) {
                throw new DecompressionException("error decoding block");
            }
            this.currentTable = (this.selectors[this.groupIndex] & 0xFF);
        }
        final Bzip2BitReader reader = this.reader;
        final int currentTable = this.currentTable;
        final int[] tableLimits = this.codeLimits[currentTable];
        final int[] tableBases = this.codeBases[currentTable];
        final int[] tableSymbols = this.codeSymbols[currentTable];
        int codeLength = this.minimumLengths[currentTable];
        int codeBits = reader.readBits(codeLength);
        while (codeLength <= 23) {
            if (codeBits <= tableLimits[codeLength]) {
                return tableSymbols[codeBits - tableBases[codeLength]];
            }
            codeBits = (codeBits << 1 | reader.readBits(1));
            ++codeLength;
        }
        throw new DecompressionException("a valid code was not recognised");
    }
}
