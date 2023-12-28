// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.internal.tcnative;

import java.nio.ByteBuffer;

public final class Buffer
{
    private Buffer() {
    }
    
    public static native long address(final ByteBuffer p0);
    
    public static native long size(final ByteBuffer p0);
}
