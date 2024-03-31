// 
// Decompiled by Procyon v0.6.0
// 

package net.jpountz.xxhash;

import net.jpountz.util.ByteBufferUtils;
import java.nio.ByteBuffer;
import net.jpountz.util.SafeUtils;

final class XXHash64JNI extends XXHash64
{
    public static final XXHash64 INSTANCE;
    private static XXHash64 SAFE_INSTANCE;
    
    @Override
    public long hash(final byte[] buf, final int off, final int len, final long seed) {
        SafeUtils.checkRange(buf, off, len);
        return XXHashJNI.XXH64(buf, off, len, seed);
    }
    
    @Override
    public long hash(final ByteBuffer buf, final int off, final int len, final long seed) {
        if (buf.isDirect()) {
            ByteBufferUtils.checkRange(buf, off, len);
            return XXHashJNI.XXH64BB(buf, off, len, seed);
        }
        if (buf.hasArray()) {
            return this.hash(buf.array(), off + buf.arrayOffset(), len, seed);
        }
        XXHash64 safeInstance = XXHash64JNI.SAFE_INSTANCE;
        if (safeInstance == null) {
            safeInstance = (XXHash64JNI.SAFE_INSTANCE = XXHashFactory.safeInstance().hash64());
        }
        return safeInstance.hash(buf, off, len, seed);
    }
    
    static {
        INSTANCE = new XXHash64JNI();
    }
}
