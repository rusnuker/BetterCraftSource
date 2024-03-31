// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec;

import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.util.internal.StringUtil;
import io.netty.channel.socket.ChannelInputShutdownEvent;
import java.util.List;
import io.netty.channel.ChannelHandlerContext;
import io.netty.buffer.Unpooled;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelInboundHandlerAdapter;

public abstract class ByteToMessageDecoder extends ChannelInboundHandlerAdapter
{
    public static final Cumulator MERGE_CUMULATOR;
    public static final Cumulator COMPOSITE_CUMULATOR;
    ByteBuf cumulation;
    private Cumulator cumulator;
    private boolean singleDecode;
    private boolean decodeWasNull;
    private boolean first;
    private int discardAfterReads;
    private int numReads;
    
    protected ByteToMessageDecoder() {
        this.cumulator = ByteToMessageDecoder.MERGE_CUMULATOR;
        this.discardAfterReads = 16;
        this.ensureNotSharable();
    }
    
    public void setSingleDecode(final boolean singleDecode) {
        this.singleDecode = singleDecode;
    }
    
    public boolean isSingleDecode() {
        return this.singleDecode;
    }
    
    public void setCumulator(final Cumulator cumulator) {
        if (cumulator == null) {
            throw new NullPointerException("cumulator");
        }
        this.cumulator = cumulator;
    }
    
    public void setDiscardAfterReads(final int discardAfterReads) {
        if (discardAfterReads <= 0) {
            throw new IllegalArgumentException("discardAfterReads must be > 0");
        }
        this.discardAfterReads = discardAfterReads;
    }
    
    protected int actualReadableBytes() {
        return this.internalBuffer().readableBytes();
    }
    
    protected ByteBuf internalBuffer() {
        if (this.cumulation != null) {
            return this.cumulation;
        }
        return Unpooled.EMPTY_BUFFER;
    }
    
    @Override
    public final void handlerRemoved(final ChannelHandlerContext ctx) throws Exception {
        final ByteBuf buf = this.cumulation;
        if (buf != null) {
            this.cumulation = null;
            final int readable = buf.readableBytes();
            if (readable > 0) {
                final ByteBuf bytes = buf.readBytes(readable);
                buf.release();
                ctx.fireChannelRead((Object)bytes);
            }
            else {
                buf.release();
            }
            this.numReads = 0;
            ctx.fireChannelReadComplete();
        }
        this.handlerRemoved0(ctx);
    }
    
    protected void handlerRemoved0(final ChannelHandlerContext ctx) throws Exception {
    }
    
    @Override
    public void channelRead(final ChannelHandlerContext ctx, final Object msg) throws Exception {
        if (msg instanceof ByteBuf) {
            final CodecOutputList out = CodecOutputList.newInstance();
            try {
                final ByteBuf data = (ByteBuf)msg;
                this.first = (this.cumulation == null);
                if (this.first) {
                    this.cumulation = data;
                }
                else {
                    this.cumulation = this.cumulator.cumulate(ctx.alloc(), this.cumulation, data);
                }
                this.callDecode(ctx, this.cumulation, out);
            }
            catch (final DecoderException e) {
                throw e;
            }
            catch (final Throwable t) {
                throw new DecoderException(t);
            }
            finally {
                if (this.cumulation != null && !this.cumulation.isReadable()) {
                    this.numReads = 0;
                    this.cumulation.release();
                    this.cumulation = null;
                }
                else if (++this.numReads >= this.discardAfterReads) {
                    this.numReads = 0;
                    this.discardSomeReadBytes();
                }
                final int size = out.size();
                this.decodeWasNull = !out.insertSinceRecycled();
                fireChannelRead(ctx, out, size);
                out.recycle();
            }
        }
        else {
            ctx.fireChannelRead(msg);
        }
    }
    
    static void fireChannelRead(final ChannelHandlerContext ctx, final List<Object> msgs, final int numElements) {
        if (msgs instanceof CodecOutputList) {
            fireChannelRead(ctx, (CodecOutputList)msgs, numElements);
        }
        else {
            for (int i = 0; i < numElements; ++i) {
                ctx.fireChannelRead(msgs.get(i));
            }
        }
    }
    
    static void fireChannelRead(final ChannelHandlerContext ctx, final CodecOutputList msgs, final int numElements) {
        for (int i = 0; i < numElements; ++i) {
            ctx.fireChannelRead(msgs.getUnsafe(i));
        }
    }
    
    @Override
    public void channelReadComplete(final ChannelHandlerContext ctx) throws Exception {
        this.numReads = 0;
        this.discardSomeReadBytes();
        if (this.decodeWasNull) {
            this.decodeWasNull = false;
            if (!ctx.channel().config().isAutoRead()) {
                ctx.read();
            }
        }
        ctx.fireChannelReadComplete();
    }
    
    protected final void discardSomeReadBytes() {
        if (this.cumulation != null && !this.first && this.cumulation.refCnt() == 1) {
            this.cumulation.discardSomeReadBytes();
        }
    }
    
    @Override
    public void channelInactive(final ChannelHandlerContext ctx) throws Exception {
        this.channelInputClosed(ctx, true);
    }
    
    @Override
    public void userEventTriggered(final ChannelHandlerContext ctx, final Object evt) throws Exception {
        if (evt instanceof ChannelInputShutdownEvent) {
            this.channelInputClosed(ctx, false);
        }
        super.userEventTriggered(ctx, evt);
    }
    
    private void channelInputClosed(final ChannelHandlerContext ctx, final boolean callChannelInactive) throws Exception {
        final CodecOutputList out = CodecOutputList.newInstance();
        try {
            this.channelInputClosed(ctx, out);
        }
        catch (final DecoderException e) {
            throw e;
        }
        catch (final Exception e2) {
            throw new DecoderException(e2);
        }
        finally {
            try {
                if (this.cumulation != null) {
                    this.cumulation.release();
                    this.cumulation = null;
                }
                final int size = out.size();
                fireChannelRead(ctx, out, size);
                if (size > 0) {
                    ctx.fireChannelReadComplete();
                }
                if (callChannelInactive) {
                    ctx.fireChannelInactive();
                }
            }
            finally {
                out.recycle();
            }
        }
    }
    
    void channelInputClosed(final ChannelHandlerContext ctx, final List<Object> out) throws Exception {
        if (this.cumulation != null) {
            this.callDecode(ctx, this.cumulation, out);
            this.decodeLast(ctx, this.cumulation, out);
        }
        else {
            this.decodeLast(ctx, Unpooled.EMPTY_BUFFER, out);
        }
    }
    
    protected void callDecode(final ChannelHandlerContext ctx, final ByteBuf in, final List<Object> out) {
        try {
            while (in.isReadable()) {
                int outSize = out.size();
                if (outSize > 0) {
                    fireChannelRead(ctx, out, outSize);
                    out.clear();
                    if (ctx.isRemoved()) {
                        break;
                    }
                    outSize = 0;
                }
                final int oldInputLength = in.readableBytes();
                this.decode(ctx, in, out);
                if (ctx.isRemoved()) {
                    break;
                }
                if (outSize == out.size()) {
                    if (oldInputLength == in.readableBytes()) {
                        break;
                    }
                    continue;
                }
                else {
                    if (oldInputLength == in.readableBytes()) {
                        throw new DecoderException(StringUtil.simpleClassName(this.getClass()) + ".decode() did not read anything but decoded a message.");
                    }
                    if (this.isSingleDecode()) {
                        break;
                    }
                    continue;
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
    
    protected abstract void decode(final ChannelHandlerContext p0, final ByteBuf p1, final List<Object> p2) throws Exception;
    
    protected void decodeLast(final ChannelHandlerContext ctx, final ByteBuf in, final List<Object> out) throws Exception {
        if (in.isReadable()) {
            this.decode(ctx, in, out);
        }
    }
    
    static ByteBuf expandCumulation(final ByteBufAllocator alloc, ByteBuf cumulation, final int readable) {
        final ByteBuf oldCumulation = cumulation;
        cumulation = alloc.buffer(oldCumulation.readableBytes() + readable);
        cumulation.writeBytes(oldCumulation);
        oldCumulation.release();
        return cumulation;
    }
    
    static {
        MERGE_CUMULATOR = new Cumulator() {
            @Override
            public ByteBuf cumulate(final ByteBufAllocator alloc, final ByteBuf cumulation, final ByteBuf in) {
                ByteBuf buffer;
                if (cumulation.writerIndex() > cumulation.maxCapacity() - in.readableBytes() || cumulation.refCnt() > 1) {
                    buffer = ByteToMessageDecoder.expandCumulation(alloc, cumulation, in.readableBytes());
                }
                else {
                    buffer = cumulation;
                }
                buffer.writeBytes(in);
                in.release();
                return buffer;
            }
        };
        COMPOSITE_CUMULATOR = new Cumulator() {
            @Override
            public ByteBuf cumulate(final ByteBufAllocator alloc, final ByteBuf cumulation, final ByteBuf in) {
                ByteBuf buffer;
                if (cumulation.refCnt() > 1) {
                    buffer = ByteToMessageDecoder.expandCumulation(alloc, cumulation, in.readableBytes());
                    buffer.writeBytes(in);
                    in.release();
                }
                else {
                    CompositeByteBuf composite;
                    if (cumulation instanceof CompositeByteBuf) {
                        composite = (CompositeByteBuf)cumulation;
                    }
                    else {
                        composite = alloc.compositeBuffer(Integer.MAX_VALUE);
                        composite.addComponent(true, cumulation);
                    }
                    composite.addComponent(true, in);
                    buffer = composite;
                }
                return buffer;
            }
        };
    }
    
    public interface Cumulator
    {
        ByteBuf cumulate(final ByteBufAllocator p0, final ByteBuf p1, final ByteBuf p2);
    }
}
