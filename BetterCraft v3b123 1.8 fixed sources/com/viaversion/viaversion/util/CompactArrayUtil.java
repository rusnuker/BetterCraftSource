// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.util;

import java.util.function.IntToLongFunction;

public final class CompactArrayUtil
{
    private static final long[] RECIPROCAL_MULT_AND_ADD;
    private static final int[] RECIPROCAL_RIGHT_SHIFT;
    
    private CompactArrayUtil() {
        throw new AssertionError();
    }
    
    public static long[] createCompactArrayWithPadding(final int bitsPerEntry, final int entries, final IntToLongFunction valueGetter) {
        final long maxEntryValue = (1L << bitsPerEntry) - 1L;
        final char valuesPerLong = (char)(64 / bitsPerEntry);
        final int magicIndex = valuesPerLong - '\u0001';
        final long divideAdd = CompactArrayUtil.RECIPROCAL_MULT_AND_ADD[magicIndex];
        final long divideMul = (divideAdd != 0L) ? divideAdd : 2147483648L;
        final int divideShift = CompactArrayUtil.RECIPROCAL_RIGHT_SHIFT[magicIndex];
        final int size = (entries + valuesPerLong - 1) / valuesPerLong;
        final long[] data = new long[size];
        for (int i = 0; i < entries; ++i) {
            final long value = valueGetter.applyAsLong(i);
            final int cellIndex = (int)(i * divideMul + divideAdd >> 32 >> divideShift);
            final int bitIndex = (i - cellIndex * valuesPerLong) * bitsPerEntry;
            data[cellIndex] = ((data[cellIndex] & ~(maxEntryValue << bitIndex)) | (value & maxEntryValue) << bitIndex);
        }
        return data;
    }
    
    public static void iterateCompactArrayWithPadding(final int bitsPerEntry, final int entries, final long[] data, final BiIntConsumer consumer) {
        final long maxEntryValue = (1L << bitsPerEntry) - 1L;
        final char valuesPerLong = (char)(64 / bitsPerEntry);
        final int magicIndex = valuesPerLong - '\u0001';
        final long divideAdd = CompactArrayUtil.RECIPROCAL_MULT_AND_ADD[magicIndex];
        final long divideMul = (divideAdd != 0L) ? divideAdd : 2147483648L;
        final int divideShift = CompactArrayUtil.RECIPROCAL_RIGHT_SHIFT[magicIndex];
        for (int i = 0; i < entries; ++i) {
            final int cellIndex = (int)(i * divideMul + divideAdd >> 32 >> divideShift);
            final int bitIndex = (i - cellIndex * valuesPerLong) * bitsPerEntry;
            final int value = (int)(data[cellIndex] >> bitIndex & maxEntryValue);
            consumer.consume(i, value);
        }
    }
    
    public static long[] createCompactArray(final int bitsPerEntry, final int entries, final IntToLongFunction valueGetter) {
        final long maxEntryValue = (1L << bitsPerEntry) - 1L;
        final long[] data = new long[(int)Math.ceil(entries * bitsPerEntry / 64.0)];
        for (int i = 0; i < entries; ++i) {
            final long value = valueGetter.applyAsLong(i);
            final int bitIndex = i * bitsPerEntry;
            final int startIndex = bitIndex / 64;
            final int endIndex = ((i + 1) * bitsPerEntry - 1) / 64;
            final int startBitSubIndex = bitIndex % 64;
            data[startIndex] = ((data[startIndex] & ~(maxEntryValue << startBitSubIndex)) | (value & maxEntryValue) << startBitSubIndex);
            if (startIndex != endIndex) {
                final int endBitSubIndex = 64 - startBitSubIndex;
                data[endIndex] = (data[endIndex] >>> endBitSubIndex << endBitSubIndex | (value & maxEntryValue) >> endBitSubIndex);
            }
        }
        return data;
    }
    
    public static void iterateCompactArray(final int bitsPerEntry, final int entries, final long[] data, final BiIntConsumer consumer) {
        final long maxEntryValue = (1L << bitsPerEntry) - 1L;
        for (int i = 0; i < entries; ++i) {
            final int bitIndex = i * bitsPerEntry;
            final int startIndex = bitIndex / 64;
            final int endIndex = ((i + 1) * bitsPerEntry - 1) / 64;
            final int startBitSubIndex = bitIndex % 64;
            int value;
            if (startIndex == endIndex) {
                value = (int)(data[startIndex] >>> startBitSubIndex & maxEntryValue);
            }
            else {
                final int endBitSubIndex = 64 - startBitSubIndex;
                value = (int)((data[startIndex] >>> startBitSubIndex | data[endIndex] << endBitSubIndex) & maxEntryValue);
            }
            consumer.consume(i, value);
        }
    }
    
    static {
        RECIPROCAL_MULT_AND_ADD = new long[] { 4294967295L, 0L, 1431655765L, 0L, 858993459L, 715827882L, 613566756L, 0L, 477218588L, 429496729L, 390451572L, 357913941L, 330382099L, 306783378L, 286331153L, 0L, 252645135L, 238609294L, 226050910L, 214748364L, 204522252L, 195225786L, 186737708L, 178956970L, 171798691L, 165191049L, 159072862L, 153391689L, 148102320L, 143165576L, 138547332L, 0L, 130150524L, 126322567L, 122713351L, 119304647L, 116080197L, 113025455L, 110127366L, 107374182L, 104755299L, 102261126L, 99882960L, 97612893L, 95443717L, 93368854L, 91382282L, 89478485L, 87652393L, 85899345L, 84215045L, 82595524L, 81037118L, 79536431L, 78090314L, 76695844L, 75350303L, 74051160L, 72796055L, 71582788L, 70409299L, 69273666L, 68174084L, 0L };
        RECIPROCAL_RIGHT_SHIFT = new int[] { 0, 0, 0, 1, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5 };
    }
}
