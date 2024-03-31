// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http2;

import io.netty.channel.ChannelHandlerContext;

public interface Http2PromisedRequestVerifier
{
    public static final Http2PromisedRequestVerifier ALWAYS_VERIFY = new Http2PromisedRequestVerifier() {
        @Override
        public boolean isAuthoritative(final ChannelHandlerContext ctx, final Http2Headers headers) {
            return true;
        }
        
        @Override
        public boolean isCacheable(final Http2Headers headers) {
            return true;
        }
        
        @Override
        public boolean isSafe(final Http2Headers headers) {
            return true;
        }
    };
    
    boolean isAuthoritative(final ChannelHandlerContext p0, final Http2Headers p1);
    
    boolean isCacheable(final Http2Headers p0);
    
    boolean isSafe(final Http2Headers p0);
}
