// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http2;

import io.netty.buffer.ByteBuf;

public class Http2ConnectionAdapter implements Http2Connection.Listener
{
    @Override
    public void onStreamAdded(final Http2Stream stream) {
    }
    
    @Override
    public void onStreamActive(final Http2Stream stream) {
    }
    
    @Override
    public void onStreamHalfClosed(final Http2Stream stream) {
    }
    
    @Override
    public void onStreamClosed(final Http2Stream stream) {
    }
    
    @Override
    public void onStreamRemoved(final Http2Stream stream) {
    }
    
    @Override
    public void onGoAwaySent(final int lastStreamId, final long errorCode, final ByteBuf debugData) {
    }
    
    @Override
    public void onGoAwayReceived(final int lastStreamId, final long errorCode, final ByteBuf debugData) {
    }
}
