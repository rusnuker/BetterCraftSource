// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.smtp;

import io.netty.buffer.Unpooled;
import java.util.Iterator;
import io.netty.buffer.ByteBufUtil;
import java.util.RandomAccess;
import java.util.List;
import io.netty.channel.ChannelHandlerContext;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.MessageToMessageEncoder;

public final class SmtpRequestEncoder extends MessageToMessageEncoder<Object>
{
    private static final byte[] CRLF;
    private static final byte[] DOT_CRLF;
    private static final byte SP = 32;
    private static final ByteBuf DOT_CRLF_BUFFER;
    private boolean contentExpected;
    
    @Override
    public boolean acceptOutboundMessage(final Object msg) throws Exception {
        return msg instanceof SmtpRequest || msg instanceof SmtpContent;
    }
    
    @Override
    protected void encode(final ChannelHandlerContext ctx, final Object msg, final List<Object> out) throws Exception {
        if (msg instanceof SmtpRequest) {
            if (this.contentExpected) {
                throw new IllegalStateException("SmtpContent expected");
            }
            boolean release = true;
            final ByteBuf buffer = ctx.alloc().buffer();
            try {
                final SmtpRequest req = (SmtpRequest)msg;
                req.command().encode(buffer);
                writeParameters(req.parameters(), buffer);
                buffer.writeBytes(SmtpRequestEncoder.CRLF);
                out.add(buffer);
                release = false;
                if (req.command().isContentExpected()) {
                    this.contentExpected = true;
                }
            }
            finally {
                if (release) {
                    buffer.release();
                }
            }
        }
        if (msg instanceof SmtpContent) {
            if (!this.contentExpected) {
                throw new IllegalStateException("No SmtpContent expected");
            }
            final ByteBuf content = ((SmtpContent)msg).content();
            out.add(content.retain());
            if (msg instanceof LastSmtpContent) {
                out.add(SmtpRequestEncoder.DOT_CRLF_BUFFER.retainedDuplicate());
                this.contentExpected = false;
            }
        }
    }
    
    private static void writeParameters(final List<CharSequence> parameters, final ByteBuf out) {
        if (parameters.isEmpty()) {
            return;
        }
        out.writeByte(32);
        if (parameters instanceof RandomAccess) {
            final int sizeMinusOne = parameters.size() - 1;
            for (int i = 0; i < sizeMinusOne; ++i) {
                ByteBufUtil.writeAscii(out, parameters.get(i));
                out.writeByte(32);
            }
            ByteBufUtil.writeAscii(out, parameters.get(sizeMinusOne));
        }
        else {
            final Iterator<CharSequence> params = parameters.iterator();
            while (true) {
                ByteBufUtil.writeAscii(out, params.next());
                if (!params.hasNext()) {
                    break;
                }
                out.writeByte(32);
            }
        }
    }
    
    static {
        CRLF = new byte[] { 13, 10 };
        DOT_CRLF = new byte[] { 46, 13, 10 };
        DOT_CRLF_BUFFER = Unpooled.unreleasableBuffer(Unpooled.directBuffer(3).writeBytes(SmtpRequestEncoder.DOT_CRLF));
    }
}
