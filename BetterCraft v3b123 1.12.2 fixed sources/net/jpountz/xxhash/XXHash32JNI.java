// 
// Decompiled by Procyon v0.6.0
// 

package net.jpountz.xxhash;

import net.jpountz.util.ByteBufferUtils;
import java.nio.ByteBuffer;
import net.jpountz.util.SafeUtils;

final class XXHash32JNI extends XXHash32
{
    public static final XXHash32 INSTANCE;
    private static XXHash32 SAFE_INSTANCE;
    
    @Override
    public int hash(final byte[] buf, final int off, final int len, final int seed) {
        SafeUtils.checkRange(buf, off, len);
        return XXHashJNI.XXH32(buf, off, len, seed);
    }
    
    @Override
    public int hash(final ByteBuffer buf, final int off, final int len, final int seed) {
        if (buf.isDirect()) {
            ByteBufferUtils.checkRange(buf, off, len);
            return XXHashJNI.XXH32BB(buf, off, len, seed);
        }
        if (buf.hasArray()) {
            return this.hash(buf.array(), off + buf.arrayOffset(), len, seed);
        }
        XXHash32 safeInstance = XXHash32JNI.SAFE_INSTANCE;
        if (safeInstance == null) {
            safeInstance = (XXHash32JNI.SAFE_INSTANCE = XXHashFactory.safeInstance().hash32());
        }
        return safeInstance.hash(buf, off, len, seed);
    }
    
    static {
        INSTANCE = new XXHash32JNI();
    }
}
