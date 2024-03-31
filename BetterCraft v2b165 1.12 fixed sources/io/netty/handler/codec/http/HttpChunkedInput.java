// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.buffer.ByteBuf;
import io.netty.handler.stream.ChunkedInput;

public class HttpChunkedInput implements ChunkedInput<HttpContent>
{
    private final ChunkedInput<ByteBuf> input;
    private final LastHttpContent lastHttpContent;
    private boolean sentLastChunk;
    
    public HttpChunkedInput(final ChunkedInput<ByteBuf> input) {
        this.input = input;
        this.lastHttpContent = LastHttpContent.EMPTY_LAST_CONTENT;
    }
    
    public HttpChunkedInput(final ChunkedInput<ByteBuf> input, final LastHttpContent lastHttpContent) {
        this.input = input;
        this.lastHttpContent = lastHttpContent;
    }
    
    @Override
    public boolean isEndOfInput() throws Exception {
        return this.input.isEndOfInput() && this.sentLastChunk;
    }
    
    @Override
    public void close() throws Exception {
        this.input.close();
    }
    
    @Deprecated
    @Override
    public HttpContent readChunk(final ChannelHandlerContext ctx) throws Exception {
        return this.readChunk(ctx.alloc());
    }
    
    @Override
    public HttpContent readChunk(final ByteBufAllocator allocator) throws Exception {
        if (this.input.isEndOfInput()) {
            if (this.sentLastChunk) {
                return null;
            }
            this.sentLastChunk = true;
            return this.lastHttpContent;
        }
        else {
            final ByteBuf buf = this.input.readChunk(allocator);
            if (buf == null) {
                return null;
            }
            return new DefaultHttpContent(buf);
        }
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
