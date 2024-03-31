// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.memcache;

import io.netty.buffer.ByteBufHolder;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.MessageAggregator;

public abstract class AbstractMemcacheObjectAggregator<H extends MemcacheMessage> extends MessageAggregator<MemcacheObject, H, MemcacheContent, FullMemcacheMessage>
{
    protected AbstractMemcacheObjectAggregator(final int maxContentLength) {
        super(maxContentLength);
    }
    
    @Override
    protected boolean isContentMessage(final MemcacheObject msg) throws Exception {
        return msg instanceof MemcacheContent;
    }
    
    @Override
    protected boolean isLastContentMessage(final MemcacheContent msg) throws Exception {
        return msg instanceof LastMemcacheContent;
    }
    
    @Override
    protected boolean isAggregated(final MemcacheObject msg) throws Exception {
        return msg instanceof FullMemcacheMessage;
    }
    
    @Override
    protected boolean isContentLengthInvalid(final H start, final int maxContentLength) {
        return false;
    }
    
    @Override
    protected Object newContinueResponse(final H start, final int maxContentLength, final ChannelPipeline pipeline) {
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
}
