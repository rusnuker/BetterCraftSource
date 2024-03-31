// 
// Decompiled by Procyon v0.6.0
// 

package net.jpountz.lz4;

enum LZ4Utils
{
    private static final int MAX_INPUT_SIZE = 2113929216;
    
    static int maxCompressedLength(final int length) {
        if (length < 0) {
            throw new IllegalArgumentException("length must be >= 0, got " + length);
        }
        if (length >= 2113929216) {
            throw new IllegalArgumentException("length must be < 2113929216");
        }
        return length + length / 255 + 16;
    }
    
    static int hash(final int i) {
        return i * -1640531535 >>> 20;
    }
    
    static int hash64k(final int i) {
        return i * -1640531535 >>> 19;
    }
    
    static int hashHC(final int i) {
        return i * -1640531535 >>> 17;
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
