// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.socksx.v4;

import io.netty.util.CharsetUtil;
import io.netty.handler.codec.DecoderResult;
import io.netty.util.NetUtil;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.socksx.SocksVersion;
import java.util.List;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

public class Socks4ServerDecoder extends ReplayingDecoder<State>
{
    private static final int MAX_FIELD_LENGTH = 255;
    private Socks4CommandType type;
    private String dstAddr;
    private int dstPort;
    private String userId;
    
    public Socks4ServerDecoder() {
        super(State.START);
        this.setSingleDecode(true);
    }
    
    @Override
    protected void decode(final ChannelHandlerContext ctx, final ByteBuf in, final List<Object> out) throws Exception {
        try {
            switch (this.state()) {
                case START: {
                    final int version = in.readUnsignedByte();
                    if (version != SocksVersion.SOCKS4a.byteValue()) {
                        throw new DecoderException("unsupported protocol version: " + version);
                    }
                    this.type = Socks4CommandType.valueOf(in.readByte());
                    this.dstPort = in.readUnsignedShort();
                    this.dstAddr = NetUtil.intToIpAddress(in.readInt());
                    this.checkpoint(State.READ_USERID);
                }
                case READ_USERID: {
                    this.userId = readString("userid", in);
                    this.checkpoint(State.READ_DOMAIN);
                }
                case READ_DOMAIN: {
                    if (!"0.0.0.0".equals(this.dstAddr) && this.dstAddr.startsWith("0.0.0.")) {
                        this.dstAddr = readString("dstAddr", in);
                    }
                    out.add(new DefaultSocks4CommandRequest(this.type, this.dstAddr, this.dstPort, this.userId));
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
        final Socks4CommandRequest m = new DefaultSocks4CommandRequest((this.type != null) ? this.type : Socks4CommandType.CONNECT, (this.dstAddr != null) ? this.dstAddr : "", (this.dstPort != 0) ? this.dstPort : 65535, (this.userId != null) ? this.userId : "");
        m.setDecoderResult(DecoderResult.failure(cause));
        out.add(m);
        this.checkpoint(State.FAILURE);
    }
    
    private static String readString(final String fieldName, final ByteBuf in) {
        final int length = in.bytesBefore(256, (byte)0);
        if (length < 0) {
            throw new DecoderException("field '" + fieldName + "' longer than " + 255 + " chars");
        }
        final String value = in.readSlice(length).toString(CharsetUtil.US_ASCII);
        in.skipBytes(1);
        return value;
    }
    
    enum State
    {
        START, 
        READ_USERID, 
        READ_DOMAIN, 
        SUCCESS, 
        FAILURE;
    }
}
