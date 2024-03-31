// 
// Decompiled by Procyon v0.6.0
// 

package net.jpountz.lz4;

import net.jpountz.util.ByteBufferUtils;
import java.nio.ByteBuffer;
import net.jpountz.util.SafeUtils;

final class LZ4HCJNICompressor extends LZ4Compressor
{
    public static final LZ4HCJNICompressor INSTANCE;
    private static LZ4Compressor SAFE_INSTANCE;
    private final int compressionLevel;
    
    LZ4HCJNICompressor() {
        this(9);
    }
    
    LZ4HCJNICompressor(final int compressionLevel) {
        this.compressionLevel = compressionLevel;
    }
    
    @Override
    public int compress(final byte[] src, final int srcOff, final int srcLen, final byte[] dest, final int destOff, final int maxDestLen) {
        SafeUtils.checkRange(src, srcOff, srcLen);
        SafeUtils.checkRange(dest, destOff, maxDestLen);
        final int result = LZ4JNI.LZ4_compressHC(src, null, srcOff, srcLen, dest, null, destOff, maxDestLen, this.compressionLevel);
        if (result <= 0) {
            throw new LZ4Exception();
        }
        return result;
    }
    
    @Override
    public int compress(final ByteBuffer src, int srcOff, final int srcLen, final ByteBuffer dest, int destOff, final int maxDestLen) {
        ByteBufferUtils.checkNotReadOnly(dest);
        ByteBufferUtils.checkRange(src, srcOff, srcLen);
        ByteBufferUtils.checkRange(dest, destOff, maxDestLen);
        if ((!src.hasArray() && !src.isDirect()) || (!dest.hasArray() && !dest.isDirect())) {
            LZ4Compressor safeInstance = LZ4HCJNICompressor.SAFE_INSTANCE;
            if (safeInstance == null) {
                safeInstance = (LZ4HCJNICompressor.SAFE_INSTANCE = LZ4Factory.safeInstance().highCompressor(this.compressionLevel));
            }
            return safeInstance.compress(src, srcOff, srcLen, dest, destOff, maxDestLen);
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
        final int result = LZ4JNI.LZ4_compressHC(srcArr, srcBuf, srcOff, srcLen, destArr, destBuf, destOff, maxDestLen, this.compressionLevel);
        if (result <= 0) {
            throw new LZ4Exception();
        }
        return result;
    }
    
    static {
        INSTANCE = new LZ4HCJNICompressor();
    }
}
