// 
// Decompiled by Procyon v0.6.0
// 

package net.jpountz.lz4;

import java.nio.ByteOrder;
import net.jpountz.util.Utils;
import net.jpountz.util.UnsafeUtils;

enum LZ4UnsafeUtils
{
    static void safeArraycopy(final byte[] src, final int srcOff, final byte[] dest, final int destOff, final int len) {
        final int fastLen = len & 0xFFFFFFF8;
        wildArraycopy(src, srcOff, dest, destOff, fastLen);
        for (int i = 0, slowLen = len & 0x7; i < slowLen; ++i) {
            UnsafeUtils.writeByte(dest, destOff + fastLen + i, UnsafeUtils.readByte(src, srcOff + fastLen + i));
        }
    }
    
    static void wildArraycopy(final byte[] src, final int srcOff, final byte[] dest, final int destOff, final int len) {
        for (int i = 0; i < len; i += 8) {
            UnsafeUtils.writeLong(dest, destOff + i, UnsafeUtils.readLong(src, srcOff + i));
        }
    }
    
    static void wildIncrementalCopy(final byte[] dest, int matchOff, int dOff, final int matchCopyEnd) {
        if (dOff - matchOff < 4) {
            for (int i = 0; i < 4; ++i) {
                UnsafeUtils.writeByte(dest, dOff + i, UnsafeUtils.readByte(dest, matchOff + i));
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
            UnsafeUtils.writeInt(dest, dOff, UnsafeUtils.readInt(dest, matchOff));
            dOff += 4;
            matchOff -= dec;
        }
        else if (dOff - matchOff < 8) {
            UnsafeUtils.writeLong(dest, dOff, UnsafeUtils.readLong(dest, matchOff));
            dOff += dOff - matchOff;
        }
        while (dOff < matchCopyEnd) {
            UnsafeUtils.writeLong(dest, dOff, UnsafeUtils.readLong(dest, matchOff));
            dOff += 8;
            matchOff += 8;
        }
    }
    
    static void safeIncrementalCopy(final byte[] dest, final int matchOff, final int dOff, final int matchLen) {
        for (int i = 0; i < matchLen; ++i) {
            dest[dOff + i] = dest[matchOff + i];
            UnsafeUtils.writeByte(dest, dOff + i, UnsafeUtils.readByte(dest, matchOff + i));
        }
    }
    
    static int readShortLittleEndian(final byte[] src, final int srcOff) {
        short s = UnsafeUtils.readShort(src, srcOff);
        if (Utils.NATIVE_BYTE_ORDER == ByteOrder.BIG_ENDIAN) {
            s = Short.reverseBytes(s);
        }
        return s & 0xFFFF;
    }
    
    static void writeShortLittleEndian(final byte[] dest, final int destOff, final int value) {
        short s = (short)value;
        if (Utils.NATIVE_BYTE_ORDER == ByteOrder.BIG_ENDIAN) {
            s = Short.reverseBytes(s);
        }
        UnsafeUtils.writeShort(dest, destOff, s);
    }
    
    static boolean readIntEquals(final byte[] src, final int ref, final int sOff) {
        return UnsafeUtils.readInt(src, ref) == UnsafeUtils.readInt(src, sOff);
    }
    
    static int commonBytes(final byte[] src, int ref, int sOff, final int srcLimit) {
        int matchLen = 0;
        while (sOff <= srcLimit - 8) {
            if (UnsafeUtils.readLong(src, sOff) != UnsafeUtils.readLong(src, ref)) {
                int zeroBits;
                if (Utils.NATIVE_BYTE_ORDER == ByteOrder.BIG_ENDIAN) {
                    zeroBits = Long.numberOfLeadingZeros(UnsafeUtils.readLong(src, sOff) ^ UnsafeUtils.readLong(src, ref));
                }
                else {
                    zeroBits = Long.numberOfTrailingZeros(UnsafeUtils.readLong(src, sOff) ^ UnsafeUtils.readLong(src, ref));
                }
                return matchLen + (zeroBits >>> 3);
            }
            matchLen += 8;
            ref += 8;
            sOff += 8;
        }
        while (sOff < srcLimit && UnsafeUtils.readByte(src, ref++) == UnsafeUtils.readByte(src, sOff++)) {
            ++matchLen;
        }
        return matchLen;
    }
    
    static int writeLen(int len, final byte[] dest, int dOff) {
        while (len >= 255) {
            UnsafeUtils.writeByte(dest, dOff++, 255);
            len -= 255;
        }
        UnsafeUtils.writeByte(dest, dOff++, len);
        return dOff;
    }
    
    static int encodeSequence(final byte[] src, final int anchor, final int matchOff, final int matchRef, int matchLen, final byte[] dest, int dOff, final int destEnd) {
        final int runLen = matchOff - anchor;
        final int tokenOff = dOff++;
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
        dest[dOff++] = (byte)matchDec;
        dest[dOff++] = (byte)(matchDec >>> 8);
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
        dest[tokenOff] = (byte)token;
        return dOff;
    }
    
    static int commonBytesBackward(final byte[] b, int o1, int o2, final int l1, final int l2) {
        int count = 0;
        while (o1 > l1 && o2 > l2 && UnsafeUtils.readByte(b, --o1) == UnsafeUtils.readByte(b, --o2)) {
            ++count;
        }
        return count;
    }
    
    static int lastLiterals(final byte[] src, final int sOff, final int srcLen, final byte[] dest, final int dOff, final int destEnd) {
        return LZ4SafeUtils.lastLiterals(src, sOff, srcLen, dest, dOff, destEnd);
    }
}
