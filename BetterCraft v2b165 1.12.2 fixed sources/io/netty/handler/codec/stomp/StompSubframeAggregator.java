// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.stomp;

import io.netty.util.AsciiString;
import io.netty.buffer.ByteBufHolder;
import io.netty.handler.codec.Headers;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.MessageAggregator;

public class StompSubframeAggregator extends MessageAggregator<StompSubframe, StompHeadersSubframe, StompContentSubframe, StompFrame>
{
    public StompSubframeAggregator(final int maxContentLength) {
        super(maxContentLength);
    }
    
    @Override
    protected boolean isStartMessage(final StompSubframe msg) throws Exception {
        return msg instanceof StompHeadersSubframe;
    }
    
    @Override
    protected boolean isContentMessage(final StompSubframe msg) throws Exception {
        return msg instanceof StompContentSubframe;
    }
    
    @Override
    protected boolean isLastContentMessage(final StompContentSubframe msg) throws Exception {
        return msg instanceof LastStompContentSubframe;
    }
    
    @Override
    protected boolean isAggregated(final StompSubframe msg) throws Exception {
        return msg instanceof StompFrame;
    }
    
    @Override
    protected boolean isContentLengthInvalid(final StompHeadersSubframe start, final int maxContentLength) {
        return (int)Math.min(2147483647L, ((Headers<AsciiString, V, T>)start.headers()).getLong(StompHeaders.CONTENT_LENGTH, -1L)) > maxContentLength;
    }
    
    @Override
    protected Object newContinueResponse(final StompHeadersSubframe start, final int maxContentLength, final ChannelPipeline pipeline) {
        return null;
    }
    
    @Override
    protected boolean closeAfterContinueResponse(final Object msg) throws Exception {
        throw new UnsupportedOperationException();
    }
    
    @Override
    protected boolean ignoreContentAfterContinueResponse(final Object msg) throws Exception {
        throw new UnsupportedOperationException();
    }
    
    @Override
    protected StompFrame beginAggregation(final StompHeadersSubframe start, final ByteBuf content) throws Exception {
        final StompFrame ret = new DefaultStompFrame(start.command(), content);
        ret.headers().set(start.headers());
        return ret;
    }
}
