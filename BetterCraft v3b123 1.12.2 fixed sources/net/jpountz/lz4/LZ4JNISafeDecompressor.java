// 
// Decompiled by Procyon v0.6.0
// 

package net.jpountz.lz4;

import net.jpountz.util.ByteBufferUtils;
import java.nio.ByteBuffer;
import net.jpountz.util.SafeUtils;

final class LZ4JNISafeDecompressor extends LZ4SafeDecompressor
{
    public static final LZ4JNISafeDecompressor INSTANCE;
    private static LZ4SafeDecompressor SAFE_INSTANCE;
    
    @Override
    public final int decompress(final byte[] src, final int srcOff, final int srcLen, final byte[] dest, final int destOff, final int maxDestLen) {
        SafeUtils.checkRange(src, srcOff, srcLen);
        SafeUtils.checkRange(dest, destOff, maxDestLen);
        final int result = LZ4JNI.LZ4_decompress_safe(src, null, srcOff, srcLen, dest, null, destOff, maxDestLen);
        if (result < 0) {
            throw new LZ4Exception("Error decoding offset " + (srcOff - result) + " of input buffer");
        }
        return result;
    }
    
    @Override
    public int decompress(final ByteBuffer src, int srcOff, final int srcLen, final ByteBuffer dest, int destOff, final int maxDestLen) {
        ByteBufferUtils.checkNotReadOnly(dest);
        ByteBufferUtils.checkRange(src, srcOff, srcLen);
        ByteBufferUtils.checkRange(dest, destOff, maxDestLen);
        if ((!src.hasArray() && !src.isDirect()) || (!dest.hasArray() && !dest.isDirect())) {
            LZ4SafeDecompressor safeInstance = LZ4JNISafeDecompressor.SAFE_INSTANCE;
            if (safeInstance == null) {
                safeInstance = (LZ4JNISafeDecompressor.SAFE_INSTANCE = LZ4Factory.safeInstance().safeDecompressor());
            }
            return safeInstance.decompress(src, srcOff, srcLen, dest, destOff, maxDestLen);
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
        final int result = LZ4JNI.LZ4_decompress_safe(srcArr, srcBuf, srcOff, srcLen, destArr, destBuf, destOff, maxDestLen);
        if (result < 0) {
            throw new LZ4Exception("Error decoding offset " + (srcOff - result) + " of input buffer");
        }
        return result;
    }
    
    static {
        INSTANCE = new LZ4JNISafeDecompressor();
    }
}
