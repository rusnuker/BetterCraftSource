// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec;

import io.netty.util.internal.StringUtil;
import io.netty.buffer.Unpooled;
import io.netty.buffer.ByteBuf;
import java.util.List;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.Signal;

public abstract class ReplayingDecoder<S> extends ByteToMessageDecoder
{
    static final Signal REPLAY;
    private final ReplayingDecoderByteBuf replayable;
    private S state;
    private int checkpoint;
    
    protected ReplayingDecoder() {
        this(null);
    }
    
    protected ReplayingDecoder(final S initialState) {
        this.replayable = new ReplayingDecoderByteBuf();
        this.checkpoint = -1;
        this.state = initialState;
    }
    
    protected void checkpoint() {
        this.checkpoint = this.internalBuffer().readerIndex();
    }
    
    protected void checkpoint(final S state) {
        this.checkpoint();
        this.state(state);
    }
    
    protected S state() {
        return this.state;
    }
    
    protected S state(final S newState) {
        final S oldState = this.state;
        this.state = newState;
        return oldState;
    }
    
    @Override
    final void channelInputClosed(final ChannelHandlerContext ctx, final List<Object> out) throws Exception {
        try {
            this.replayable.terminate();
            if (this.cumulation != null) {
                this.callDecode(ctx, this.internalBuffer(), out);
                this.decodeLast(ctx, this.replayable, out);
            }
            else {
                this.replayable.setCumulation(Unpooled.EMPTY_BUFFER);
                this.decodeLast(ctx, this.replayable, out);
            }
        }
        catch (final Signal replay) {
            replay.expect(ReplayingDecoder.REPLAY);
        }
    }
    
    @Override
    protected void callDecode(final ChannelHandlerContext ctx, final ByteBuf in, final List<Object> out) {
        this.replayable.setCumulation(in);
        try {
            while (in.isReadable()) {
                final int readerIndex = in.readerIndex();
                this.checkpoint = readerIndex;
                final int oldReaderIndex = readerIndex;
                int outSize = out.size();
                if (outSize > 0) {
                    ByteToMessageDecoder.fireChannelRead(ctx, out, outSize);
                    out.clear();
                    if (ctx.isRemoved()) {
                        break;
                    }
                    outSize = 0;
                }
                final S oldState = this.state;
                final int oldInputLength = in.readableBytes();
                try {
                    this.decode(ctx, this.replayable, out);
                    if (ctx.isRemoved()) {
                        break;
                    }
                    if (outSize == out.size()) {
                        if (oldInputLength == in.readableBytes() && oldState == this.state) {
                            throw new DecoderException(StringUtil.simpleClassName(this.getClass()) + ".decode() must consume the inbound data or change its state if it did not decode anything.");
                        }
                        continue;
                    }
                }
                catch (final Signal replay) {
                    replay.expect(ReplayingDecoder.REPLAY);
                    if (ctx.isRemoved()) {
                        break;
                    }
                    final int checkpoint = this.checkpoint;
                    if (checkpoint >= 0) {
                        in.readerIndex(checkpoint);
                    }
                    break;
                }
                if (oldReaderIndex == in.readerIndex() && oldState == this.state) {
                    throw new DecoderException(StringUtil.simpleClassName(this.getClass()) + ".decode() method must consume the inbound data or change its state if it decoded something.");
                }
                if (this.isSingleDecode()) {
                    break;
                }
            }
        }
        catch (final DecoderException e) {
            throw e;
        }
        catch (final Throwable cause) {
            throw new DecoderException(cause);
        }
    }
    
    static {
        REPLAY = Signal.valueOf(ReplayingDecoder.class, "REPLAY");
    }
}
