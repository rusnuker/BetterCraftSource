// 
// Decompiled by Procyon v0.6.0
// 

package net.jpountz.lz4;

import net.jpountz.util.ByteBufferUtils;
import java.nio.ByteBuffer;
import net.jpountz.util.SafeUtils;

final class LZ4JNIFastDecompressor extends LZ4FastDecompressor
{
    public static final LZ4JNIFastDecompressor INSTANCE;
    private static LZ4FastDecompressor SAFE_INSTANCE;
    
    @Override
    public final int decompress(final byte[] src, final int srcOff, final byte[] dest, final int destOff, final int destLen) {
        SafeUtils.checkRange(src, srcOff);
        SafeUtils.checkRange(dest, destOff, destLen);
        final int result = LZ4JNI.LZ4_decompress_fast(src, null, srcOff, dest, null, destOff, destLen);
        if (result < 0) {
            throw new LZ4Exception("Error decoding offset " + (srcOff - result) + " of input buffer");
        }
        return result;
    }
    
    @Override
    public int decompress(final ByteBuffer src, int srcOff, final ByteBuffer dest, int destOff, final int destLen) {
        ByteBufferUtils.checkNotReadOnly(dest);
        ByteBufferUtils.checkRange(src, srcOff);
        ByteBufferUtils.checkRange(dest, destOff, destLen);
        if ((!src.hasArray() && !src.isDirect()) || (!dest.hasArray() && !dest.isDirect())) {
            LZ4FastDecompressor safeInstance = LZ4JNIFastDecompressor.SAFE_INSTANCE;
            if (safeInstance == null) {
                safeInstance = (LZ4JNIFastDecompressor.SAFE_INSTANCE = LZ4Factory.safeInstance().fastDecompressor());
            }
            return safeInstance.decompress(src, srcOff, dest, destOff, destLen);
        }
        byte[] srcArr = null;
        byte[] destArr = null;
        ByteBuffer srcBuf = null;
        ByteBuffer destBuf = null;
        if (src.hasArray()) {
            srcArr = src.array();
            srcOff += src.arrayOffset();
        }
        else {
            assert src.isDirect();
            srcBuf = src;
        }
        if (dest.hasArray()) {
            destArr = dest.array();
            destOff += dest.arrayOffset();
        }
        else {
            assert dest.isDirect();
            destBuf = dest;
        }
        final int result = LZ4JNI.LZ4_decompress_fast(srcArr, srcBuf, srcOff, destArr, destBuf, destOff, destLen);
        if (result < 0) {
            throw new LZ4Exception("Error decoding offset " + (srcOff - result) + " of input buffer");
        }
        return result;
    }
    
    static {
        INSTANCE = new LZ4JNIFastDecompressor();
    }
}
