// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http2;

import io.netty.buffer.ByteBuf;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.Promise;

public interface Http2Connection
{
    Future<Void> close(final Promise<Void> p0);
    
    PropertyKey newKey();
    
    void addListener(final Listener p0);
    
    void removeListener(final Listener p0);
    
    Http2Stream stream(final int p0);
    
    boolean streamMayHaveExisted(final int p0);
    
    Http2Stream connectionStream();
    
    int numActiveStreams();
    
    Http2Stream forEachActiveStream(final Http2StreamVisitor p0) throws Http2Exception;
    
    boolean isServer();
    
    Endpoint<Http2LocalFlowController> local();
    
    Endpoint<Http2RemoteFlowController> remote();
    
    boolean goAwayReceived();
    
    void goAwayReceived(final int p0, final long p1, final ByteBuf p2);
    
    boolean goAwaySent();
    
    void goAwaySent(final int p0, final long p1, final ByteBuf p2);
    
    public interface PropertyKey
    {
    }
    
    public interface Endpoint<F extends Http2FlowController>
    {
        int incrementAndGetNextStreamId();
        
        boolean isValidStreamId(final int p0);
        
        boolean mayHaveCreatedStream(final int p0);
        
        boolean created(final Http2Stream p0);
        
        boolean canOpenStream();
        
        Http2Stream createStream(final int p0, final boolean p1) throws Http2Exception;
        
        Http2Stream reservePushStream(final int p0, final Http2Stream p1) throws Http2Exception;
        
        boolean isServer();
        
        void allowPushTo(final boolean p0);
        
        boolean allowPushTo();
        
        int numActiveStreams();
        
        int maxActiveStreams();
        
        void maxActiveStreams(final int p0);
        
        int lastStreamCreated();
        
        int lastStreamKnownByPeer();
        
        F flowController();
        
        void flowController(final F p0);
        
        Endpoint<? extends Http2FlowController> opposite();
    }
    
    public interface Listener
    {
        void onStreamAdded(final Http2Stream p0);
        
        void onStreamActive(final Http2Stream p0);
        
        void onStreamHalfClosed(final Http2Stream p0);
        
        void onStreamClosed(final Http2Stream p0);
        
        void onStreamRemoved(final Http2Stream p0);
        
        void onGoAwaySent(final int p0, final long p1, final ByteBuf p2);
        
        void onGoAwayReceived(final int p0, final long p1, final ByteBuf p2);
    }
}
