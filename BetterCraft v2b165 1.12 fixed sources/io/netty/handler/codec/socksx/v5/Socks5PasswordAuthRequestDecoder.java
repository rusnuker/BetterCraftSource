// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.socksx.v5;

import io.netty.handler.codec.DecoderResult;
import io.netty.util.CharsetUtil;
import io.netty.handler.codec.DecoderException;
import java.util.List;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

public class Socks5PasswordAuthRequestDecoder extends ReplayingDecoder<State>
{
    public Socks5PasswordAuthRequestDecoder() {
        super(State.INIT);
    }
    
    @Override
    protected void decode(final ChannelHandlerContext ctx, final ByteBuf in, final List<Object> out) throws Exception {
        try {
            switch (this.state()) {
                case INIT: {
                    final int startOffset = in.readerIndex();
                    final byte version = in.getByte(startOffset);
                    if (version != 1) {
                        throw new DecoderException("unsupported subnegotiation version: " + version + " (expected: 1)");
                    }
                    final int usernameLength = in.getUnsignedByte(startOffset + 1);
                    final int passwordLength = in.getUnsignedByte(startOffset + 2 + usernameLength);
                    final int totalLength = usernameLength + passwordLength + 3;
                    in.skipBytes(totalLength);
                    out.add(new DefaultSocks5PasswordAuthRequest(in.toString(startOffset + 2, usernameLength, CharsetUtil.US_ASCII), in.toString(startOffset + 3 + usernameLength, passwordLength, CharsetUtil.US_ASCII)));
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
        final Socks5Message m = new DefaultSocks5PasswordAuthRequest("", "");
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
