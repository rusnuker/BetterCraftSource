// 
// Decompiled by Procyon v0.6.0
// 

package net.jpountz.lz4;

import java.nio.ByteOrder;
import net.jpountz.util.ByteBufferUtils;
import java.nio.ByteBuffer;

enum LZ4ByteBufferUtils
{
    static int hash(final ByteBuffer buf, final int i) {
        return LZ4Utils.hash(ByteBufferUtils.readInt(buf, i));
    }
    
    static int hash64k(final ByteBuffer buf, final int i) {
        return LZ4Utils.hash64k(ByteBufferUtils.readInt(buf, i));
    }
    
    static boolean readIntEquals(final ByteBuffer buf, final int i, final int j) {
        return buf.getInt(i) == buf.getInt(j);
    }
    
    static void safeIncrementalCopy(final ByteBuffer dest, final int matchOff, final int dOff, final int matchLen) {
        for (int i = 0; i < matchLen; ++i) {
            dest.put(dOff + i, dest.get(matchOff + i));
        }
    }
    
    static void wildIncrementalCopy(final ByteBuffer dest, int matchOff, int dOff, final int matchCopyEnd) {
        if (dOff - matchOff < 4) {
            for (int i = 0; i < 4; ++i) {
                ByteBufferUtils.writeByte(dest, dOff + i, ByteBufferUtils.readByte(dest, matchOff + i));
            }
            dOff += 4;
            matchOff += 4;
            int dec = 0;
            assert dOff >= matchOff && dOff - matchOff < 8;
            switch (dOff - matchOff) {
                case 1: {
                    matchOff -= 3;
                    break;
                }
                case 2: {
                    matchOff -= 2;
                    break;
                }
                case 3: {
                    matchOff -= 3;
                    dec = -1;
                    break;
                }
                case 5: {
                    dec = 1;
                    break;
                }
                case 6: {
                    dec = 2;
                    break;
                }
                case 7: {
                    dec = 3;
                    break;
                }
            }
            ByteBufferUtils.writeInt(dest, dOff, ByteBufferUtils.readInt(dest, matchOff));
            dOff += 4;
            matchOff -= dec;
        }
        else if (dOff - matchOff < 8) {
            ByteBufferUtils.writeLong(dest, dOff, ByteBufferUtils.readLong(dest, matchOff));
            dOff += dOff - matchOff;
        }
        while (dOff < matchCopyEnd) {
            ByteBufferUtils.writeLong(dest, dOff, ByteBufferUtils.readLong(dest, matchOff));
            dOff += 8;
            matchOff += 8;
        }
    }
    
    static int commonBytes(final ByteBuffer src, int ref, int sOff, final int srcLimit) {
        int matchLen = 0;
        while (sOff <= srcLimit - 8) {
            if (ByteBufferUtils.readLong(src, sOff) != ByteBufferUtils.readLong(src, ref)) {
                int zeroBits;
                if (src.order() == ByteOrder.BIG_ENDIAN) {
                    zeroBits = Long.numberOfLeadingZeros(ByteBufferUtils.readLong(src, sOff) ^ ByteBufferUtils.readLong(src, ref));
                }
                else {
                    zeroBits = Long.numberOfTrailingZeros(ByteBufferUtils.readLong(src, sOff) ^ ByteBufferUtils.readLong(src, ref));
                }
                return matchLen + (zeroBits >>> 3);
            }
            matchLen += 8;
            ref += 8;
            sOff += 8;
        }
        while (sOff < srcLimit && ByteBufferUtils.readByte(src, ref++) == ByteBufferUtils.readByte(src, sOff++)) {
            ++matchLen;
        }
        return matchLen;
    }
    
    static int commonBytesBackward(final ByteBuffer b, int o1, int o2, final int l1, final int l2) {
        int count = 0;
        while (o1 > l1 && o2 > l2 && b.get(--o1) == b.get(--o2)) {
            ++count;
        }
        return count;
    }
    
    static void safeArraycopy(final ByteBuffer src, final int sOff, final ByteBuffer dest, final int dOff, final int len) {
        for (int i = 0; i < len; ++i) {
            dest.put(dOff + i, src.get(sOff + i));
        }
    }
    
    static void wildArraycopy(final ByteBuffer src, final int sOff, final ByteBuffer dest, final int dOff, final int len) {
        assert src.order().equals(dest.order());
        try {
            for (int i = 0; i < len; i += 8) {
                dest.putLong(dOff + i, src.getLong(sOff + i));
            }
        }
        catch (final IndexOutOfBoundsException e) {
            throw new LZ4Exception("Malformed input at offset " + sOff);
        }
    }
    
    static int encodeSequence(final ByteBuffer src, final int anchor, final int matchOff, final int matchRef, int matchLen, final ByteBuffer dest, int dOff, final int destEnd) {
        final int runLen = matchOff - anchor;
        final int tokenOff = dOff++;
        if (dOff + runLen + 8 + (runLen >>> 8) > destEnd) {
            throw new LZ4Exception("maxDestLen is too small");
        }
        int token;
        if (runLen >= 15) {
            token = -16;
            dOff = writeLen(runLen - 15, dest, dOff);
        }
        else {
            token = runLen << 4;
        }
        wildArraycopy(src, anchor, dest, dOff, runLen);
        dOff += runLen;
        final int matchDec = matchOff - matchRef;
        dest.put(dOff++, (byte)matchDec);
        dest.put(dOff++, (byte)(matchDec >>> 8));
        matchLen -= 4;
        if (dOff + 6 + (matchLen >>> 8) > destEnd) {
            throw new LZ4Exception("maxDestLen is too small");
        }
        if (matchLen >= 15) {
            token |= 0xF;
            dOff = writeLen(matchLen - 15, dest, dOff);
        }
        else {
            token |= matchLen;
        }
        dest.put(tokenOff, (byte)token);
        return dOff;
    }
    
    static int lastLiterals(final ByteBuffer src, final int sOff, final int srcLen, final ByteBuffer dest, int dOff, final int destEnd) {
        final int runLen = srcLen;
        if (dOff + runLen + 1 + (runLen + 255 - 15) / 255 > destEnd) {
            throw new LZ4Exception();
        }
        if (runLen >= 15) {
            dest.put(dOff++, (byte)(-16));
            dOff = writeLen(runLen - 15, dest, dOff);
        }
        else {
            dest.put(dOff++, (byte)(runLen << 4));
        }
        safeArraycopy(src, sOff, dest, dOff, runLen);
        dOff += runLen;
        return dOff;
    }
    
    static int writeLen(int len, final ByteBuffer dest, int dOff) {
        while (len >= 255) {
            dest.put(dOff++, (byte)(-1));
            len -= 255;
        }
        dest.put(dOff++, (byte)len);
        return dOff;
    }
    
    static void copyTo(final Match m1, final Match m2) {
        m2.len = m1.len;
        m2.start = m1.start;
        m2.ref = m1.ref;
    }
    
    static class Match
    {
        int start;
        int ref;
        int len;
        
        void fix(final int correction) {
            this.start += correction;
            this.ref += correction;
            this.len -= correction;
        }
        
        int end() {
            return this.start + this.len;
        }
    }
}
