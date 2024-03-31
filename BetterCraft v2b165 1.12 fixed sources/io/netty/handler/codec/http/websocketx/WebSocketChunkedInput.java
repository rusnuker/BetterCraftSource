// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http.websocketx;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.ObjectUtil;
import io.netty.buffer.ByteBuf;
import io.netty.handler.stream.ChunkedInput;

public final class WebSocketChunkedInput implements ChunkedInput<WebSocketFrame>
{
    private final ChunkedInput<ByteBuf> input;
    private final int rsv;
    
    public WebSocketChunkedInput(final ChunkedInput<ByteBuf> input) {
        this(input, 0);
    }
    
    public WebSocketChunkedInput(final ChunkedInput<ByteBuf> input, final int rsv) {
        this.input = ObjectUtil.checkNotNull(input, "input");
        this.rsv = rsv;
    }
    
    @Override
    public boolean isEndOfInput() throws Exception {
        return this.input.isEndOfInput();
    }
    
    @Override
    public void close() throws Exception {
        this.input.close();
    }
    
    @Deprecated
    @Override
    public WebSocketFrame readChunk(final ChannelHandlerContext ctx) throws Exception {
        return this.readChunk(ctx.alloc());
    }
    
    @Override
    public WebSocketFrame readChunk(final ByteBufAllocator allocator) throws Exception {
        final ByteBuf buf = this.input.readChunk(allocator);
        if (buf == null) {
            return null;
        }
        return new ContinuationWebSocketFrame(this.input.isEndOfInput(), this.rsv, buf);
    }
    
    @Override
    public long length() {
        return this.input.length();
    }
    
    @Override
    public long progress() {
        return this.input.progress();
    }
}
