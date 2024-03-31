// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.socksx.v5;

import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.socksx.SocksVersion;
import java.util.List;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

public class Socks5InitialRequestDecoder extends ReplayingDecoder<State>
{
    public Socks5InitialRequestDecoder() {
        super(State.INIT);
    }
    
    @Override
    protected void decode(final ChannelHandlerContext ctx, final ByteBuf in, final List<Object> out) throws Exception {
        try {
            switch (this.state()) {
                case INIT: {
                    final byte version = in.readByte();
                    if (version != SocksVersion.SOCKS5.byteValue()) {
                        throw new DecoderException("unsupported version: " + version + " (expected: " + SocksVersion.SOCKS5.byteValue() + ')');
                    }
                    final int authMethodCnt = in.readUnsignedByte();
                    if (this.actualReadableBytes() < authMethodCnt) {
                        break;
                    }
                    final Socks5AuthMethod[] authMethods = new Socks5AuthMethod[authMethodCnt];
                    for (int i = 0; i < authMethodCnt; ++i) {
                        authMethods[i] = Socks5AuthMethod.valueOf(in.readByte());
                    }
                    out.add(new DefaultSocks5InitialRequest(authMethods));
                    this.checkpoint(State.SUCCESS);
                }
                case SUCCESS: {
                    final int readableBytes = this.actualReadableBytes();
                    if (readableBytes > 0) {
                        out.add(in.readRetainedSlice(readableBytes));
                        break;
                    }
                    break;
                }
                case FAILURE: {
                    in.skipBytes(this.actualReadableBytes());
                    break;
                }
            }
        }
        catch (final Exception e) {
            this.fail(out, e);
        }
    }
    
    private void fail(final List<Object> out, Throwable cause) {
        if (!(cause instanceof DecoderException)) {
            cause = new DecoderException(cause);
        }
        this.checkpoint(State.FAILURE);
        final Socks5Message m = new DefaultSocks5InitialRequest(new Socks5AuthMethod[] { Socks5AuthMethod.NO_AUTH });
        m.setDecoderResult(DecoderResult.failure(cause));
        out.add(m);
    }
    
    enum State
    {
        INIT, 
        SUCCESS, 
        FAILURE;
    }
}
