// 
// Decompiled by Procyon v0.6.0
// 

package net.jpountz.lz4;

import net.jpountz.util.SafeUtils;

enum LZ4SafeUtils
{
    static int hash(final byte[] buf, final int i) {
        return LZ4Utils.hash(SafeUtils.readInt(buf, i));
    }
    
    static int hash64k(final byte[] buf, final int i) {
        return LZ4Utils.hash64k(SafeUtils.readInt(buf, i));
    }
    
    static boolean readIntEquals(final byte[] buf, final int i, final int j) {
        return buf[i] == buf[j] && buf[i + 1] == buf[j + 1] && buf[i + 2] == buf[j + 2] && buf[i + 3] == buf[j + 3];
    }
    
    static void safeIncrementalCopy(final byte[] dest, final int matchOff, final int dOff, final int matchLen) {
        for (int i = 0; i < matchLen; ++i) {
            dest[dOff + i] = dest[matchOff + i];
        }
    }
    
    static void wildIncrementalCopy(final byte[] dest, int matchOff, int dOff, final int matchCopyEnd) {
        do {
            copy8Bytes(dest, matchOff, dest, dOff);
            matchOff += 8;
            dOff += 8;
        } while (dOff < matchCopyEnd);
    }
    
    static void copy8Bytes(final byte[] src, final int sOff, final byte[] dest, final int dOff) {
        for (int i = 0; i < 8; ++i) {
            dest[dOff + i] = src[sOff + i];
        }
    }
    
    static int commonBytes(final byte[] b, int o1, int o2, final int limit) {
        int count = 0;
        while (o2 < limit && b[o1++] == b[o2++]) {
            ++count;
        }
        return count;
    }
    
    static int commonBytesBackward(final byte[] b, int o1, int o2, final int l1, final int l2) {
        int count = 0;
        while (o1 > l1 && o2 > l2 && b[--o1] == b[--o2]) {
            ++count;
        }
        return count;
    }
    
    static void safeArraycopy(final byte[] src, final int sOff, final byte[] dest, final int dOff, final int len) {
        System.arraycopy(src, sOff, dest, dOff, len);
    }
    
    static void wildArraycopy(final byte[] src, final int sOff, final byte[] dest, final int dOff, final int len) {
        try {
            for (int i = 0; i < len; i += 8) {
                copy8Bytes(src, sOff + i, dest, dOff + i);
            }
        }
        catch (final ArrayIndexOutOfBoundsException e) {
            throw new LZ4Exception("Malformed input at offset " + sOff);
        }
    }
    
    static int encodeSequence(final byte[] src, final int anchor, final int matchOff, final int matchRef, int matchLen, final byte[] dest, int dOff, final int destEnd) {
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
    
    static int lastLiterals(final byte[] src, final int sOff, final int srcLen, final byte[] dest, int dOff, final int destEnd) {
        final int runLen = srcLen;
        if (dOff + runLen + 1 + (runLen + 255 - 15) / 255 > destEnd) {
            throw new LZ4Exception();
        }
        if (runLen >= 15) {
            dest[dOff++] = -16;
            dOff = writeLen(runLen - 15, dest, dOff);
        }
        else {
            dest[dOff++] = (byte)(runLen << 4);
        }
        System.arraycopy(src, sOff, dest, dOff, runLen);
        dOff += runLen;
        return dOff;
    }
    
    static int writeLen(int len, final byte[] dest, int dOff) {
        while (len >= 255) {
            dest[dOff++] = -1;
            len -= 255;
        }
        dest[dOff++] = (byte)len;
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
