// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http2;

import io.netty.channel.ChannelHandlerContext;

public interface Http2RemoteFlowController extends Http2FlowController
{
    ChannelHandlerContext channelHandlerContext();
    
    void addFlowControlled(final Http2Stream p0, final FlowControlled p1);
    
    boolean hasFlowControlled(final Http2Stream p0);
    
    void writePendingBytes() throws Http2Exception;
    
    void listener(final Listener p0);
    
    boolean isWritable(final Http2Stream p0);
    
    void channelWritabilityChanged() throws Http2Exception;
    
    void updateDependencyTree(final int p0, final int p1, final short p2, final boolean p3);
    
    public interface Listener
    {
        void writabilityChanged(final Http2Stream p0);
    }
    
    public interface FlowControlled
    {
        int size();
        
        void error(final ChannelHandlerContext p0, final Throwable p1);
        
        void writeComplete();
        
        void write(final ChannelHandlerContext p0, final int p1);
        
        boolean merge(final ChannelHandlerContext p0, final FlowControlled p1);
    }
}
