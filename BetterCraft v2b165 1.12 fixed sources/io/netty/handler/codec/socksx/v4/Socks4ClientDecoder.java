// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.socksx.v4;

import io.netty.handler.codec.DecoderResult;
import io.netty.util.NetUtil;
import io.netty.handler.codec.DecoderException;
import java.util.List;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

public class Socks4ClientDecoder extends ReplayingDecoder<State>
{
    public Socks4ClientDecoder() {
        super(State.START);
        this.setSingleDecode(true);
    }
    
    @Override
    protected void decode(final ChannelHandlerContext ctx, final ByteBuf in, final List<Object> out) throws Exception {
        try {
            switch (this.state()) {
                case START: {
                    final int version = in.readUnsignedByte();
                    if (version != 0) {
                        throw new DecoderException("unsupported reply version: " + version + " (expected: 0)");
                    }
                    final Socks4CommandStatus status = Socks4CommandStatus.valueOf(in.readByte());
                    final int dstPort = in.readUnsignedShort();
                    final String dstAddr = NetUtil.intToIpAddress(in.readInt());
                    out.add(new DefaultSocks4CommandResponse(status, dstAddr, dstPort));
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
        final Socks4CommandResponse m = new DefaultSocks4CommandResponse(Socks4CommandStatus.REJECTED_OR_FAILED);
        m.setDecoderResult(DecoderResult.failure(cause));
        out.add(m);
        this.checkpoint(State.FAILURE);
    }
    
    enum State
    {
        START, 
        SUCCESS, 
        FAILURE;
    }
}
