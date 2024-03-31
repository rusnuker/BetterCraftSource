// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.memcache.binary;

import io.netty.handler.codec.PrematureChannelClosureException;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.memcache.LastMemcacheContent;
import java.util.List;
import io.netty.channel.ChannelHandlerContext;
import java.util.concurrent.atomic.AtomicLong;
import io.netty.channel.CombinedChannelDuplexHandler;

public final class BinaryMemcacheClientCodec extends CombinedChannelDuplexHandler<BinaryMemcacheResponseDecoder, BinaryMemcacheRequestEncoder>
{
    private final boolean failOnMissingResponse;
    private final AtomicLong requestResponseCounter;
    
    public BinaryMemcacheClientCodec() {
        this(8192);
    }
    
    public BinaryMemcacheClientCodec(final int decodeChunkSize) {
        this(decodeChunkSize, false);
    }
    
    public BinaryMemcacheClientCodec(final int decodeChunkSize, final boolean failOnMissingResponse) {
        this.requestResponseCounter = new AtomicLong();
        this.failOnMissingResponse = failOnMissingResponse;
        ((CombinedChannelDuplexHandler<Decoder, Encoder>)this).init(new Decoder(decodeChunkSize), new Encoder());
    }
    
    private final class Encoder extends BinaryMemcacheRequestEncoder
    {
        @Override
        protected void encode(final ChannelHandlerContext ctx, final Object msg, final List<Object> out) throws Exception {
            super.encode(ctx, msg, out);
            if (BinaryMemcacheClientCodec.this.failOnMissingResponse && msg instanceof LastMemcacheContent) {
                BinaryMemcacheClientCodec.this.requestResponseCounter.incrementAndGet();
            }
        }
    }
    
    private final class Decoder extends BinaryMemcacheResponseDecoder
    {
        Decoder(final int chunkSize) {
            super(chunkSize);
        }
        
        @Override
        protected void decode(final ChannelHandlerContext ctx, final ByteBuf in, final List<Object> out) throws Exception {
            final int oldSize = out.size();
            super.decode(ctx, in, out);
            if (BinaryMemcacheClientCodec.this.failOnMissingResponse) {
                for (int size = out.size(), i = oldSize; i < size; ++i) {
                    final Object msg = out.get(i);
                    if (msg instanceof LastMemcacheContent) {
                        BinaryMemcacheClientCodec.this.requestResponseCounter.decrementAndGet();
                    }
                }
            }
        }
        
        @Override
        public void channelInactive(final ChannelHandlerContext ctx) throws Exception {
            super.channelInactive(ctx);
            if (BinaryMemcacheClientCodec.this.failOnMissingResponse) {
                final long missingResponses = BinaryMemcacheClientCodec.this.requestResponseCounter.get();
                if (missingResponses > 0L) {
                    ctx.fireExceptionCaught((Throwable)new PrematureChannelClosureException("channel gone inactive with " + missingResponses + " missing response(s)"));
                }
            }
        }
    }
}
