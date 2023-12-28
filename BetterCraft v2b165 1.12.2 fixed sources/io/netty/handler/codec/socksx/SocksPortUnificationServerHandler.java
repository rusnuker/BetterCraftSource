// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.socksx;

import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.socksx.v5.Socks5InitialRequestDecoder;
import io.netty.handler.codec.socksx.v4.Socks4ServerDecoder;
import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.socksx.v4.Socks4ServerEncoder;
import java.util.List;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.socksx.v5.Socks5ServerEncoder;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.handler.codec.ByteToMessageDecoder;

public class SocksPortUnificationServerHandler extends ByteToMessageDecoder
{
    private static final InternalLogger logger;
    private final Socks5ServerEncoder socks5encoder;
    
    public SocksPortUnificationServerHandler() {
        this(Socks5ServerEncoder.DEFAULT);
    }
    
    public SocksPortUnificationServerHandler(final Socks5ServerEncoder socks5encoder) {
        if (socks5encoder == null) {
            throw new NullPointerException("socks5encoder");
        }
        this.socks5encoder = socks5encoder;
    }
    
    @Override
    protected void decode(final ChannelHandlerContext ctx, final ByteBuf in, final List<Object> out) throws Exception {
        final int readerIndex = in.readerIndex();
        if (in.writerIndex() == readerIndex) {
            return;
        }
        final ChannelPipeline p = ctx.pipeline();
        final byte versionVal = in.getByte(readerIndex);
        final SocksVersion version = SocksVersion.valueOf(versionVal);
        switch (version) {
            case SOCKS4a: {
                logKnownVersion(ctx, version);
                p.addAfter(ctx.name(), null, Socks4ServerEncoder.INSTANCE);
                p.addAfter(ctx.name(), null, new Socks4ServerDecoder());
                break;
            }
            case SOCKS5: {
                logKnownVersion(ctx, version);
                p.addAfter(ctx.name(), null, this.socks5encoder);
                p.addAfter(ctx.name(), null, new Socks5InitialRequestDecoder());
                break;
            }
            default: {
                logUnknownVersion(ctx, versionVal);
                in.skipBytes(in.readableBytes());
                ctx.close();
                return;
            }
        }
        p.remove(this);
    }
    
    private static void logKnownVersion(final ChannelHandlerContext ctx, final SocksVersion version) {
        SocksPortUnificationServerHandler.logger.debug("{} Protocol version: {}({})", ctx.channel(), version);
    }
    
    private static void logUnknownVersion(final ChannelHandlerContext ctx, final byte versionVal) {
        if (SocksPortUnificationServerHandler.logger.isDebugEnabled()) {
            SocksPortUnificationServerHandler.logger.debug("{} Unknown protocol version: {}", ctx.channel(), versionVal & 0xFF);
        }
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(SocksPortUnificationServerHandler.class);
    }
}
