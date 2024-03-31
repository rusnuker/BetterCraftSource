// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http2;

import io.netty.buffer.ByteBuf;

public interface Http2LocalFlowController extends Http2FlowController
{
    Http2LocalFlowController frameWriter(final Http2FrameWriter p0);
    
    void receiveFlowControlledFrame(final Http2Stream p0, final ByteBuf p1, final int p2, final boolean p3) throws Http2Exception;
    
    boolean consumeBytes(final Http2Stream p0, final int p1) throws Http2Exception;
    
    int unconsumedBytes(final Http2Stream p0);
    
    int initialWindowSize(final Http2Stream p0);
}
