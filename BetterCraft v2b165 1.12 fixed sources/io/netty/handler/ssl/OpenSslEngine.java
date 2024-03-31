// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.ssl;

import io.netty.util.ReferenceCounted;
import io.netty.buffer.ByteBufAllocator;

public final class OpenSslEngine extends ReferenceCountedOpenSslEngine
{
    OpenSslEngine(final OpenSslContext context, final ByteBufAllocator alloc, final String peerHost, final int peerPort) {
        super(context, alloc, peerHost, peerPort, false);
    }
    
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        OpenSsl.releaseIfNeeded(this);
    }
}
