// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.smtp;

import io.netty.handler.codec.DecoderException;
import java.util.ArrayList;
import java.util.Collections;
import io.netty.util.CharsetUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import java.util.List;
import io.netty.handler.codec.LineBasedFrameDecoder;

public final class SmtpResponseDecoder extends LineBasedFrameDecoder
{
    private List<CharSequence> details;
    
    public SmtpResponseDecoder(final int maxLineLength) {
        super(maxLineLength);
    }
    
    @Override
    protected SmtpResponse decode(final ChannelHandlerContext ctx, final ByteBuf buffer) throws Exception {
        final ByteBuf frame = (ByteBuf)super.decode(ctx, buffer);
        if (frame == null) {
            return null;
        }
        try {
            final int readable = frame.readableBytes();
            final int readerIndex = frame.readerIndex();
            if (readable < 3) {
                throw newDecoderException(buffer, readerIndex, readable);
            }
            final int code = parseCode(frame);
            final int separator = frame.readByte();
            final CharSequence detail = frame.isReadable() ? frame.toString(CharsetUtil.US_ASCII) : null;
            List<CharSequence> details = this.details;
            switch (separator) {
                case 32: {
                    this.details = null;
                    if (details != null) {
                        if (detail != null) {
                            details.add(detail);
                        }
                    }
                    else {
                        details = Collections.singletonList(detail);
                    }
                    return new DefaultSmtpResponse(code, details);
                }
                case 45: {
                    if (detail != null) {
                        if (details == null) {
                            details = (this.details = new ArrayList<CharSequence>(4));
                        }
                        details.add(detail);
                    }
                    break;
                }
                default: {
                    throw newDecoderException(buffer, readerIndex, readable);
                }
            }
        }
        finally {
            frame.release();
        }
        return null;
    }
    
    private static DecoderException newDecoderException(final ByteBuf buffer, final int readerIndex, final int readable) {
        return new DecoderException("Received invalid line: '" + buffer.toString(readerIndex, readable, CharsetUtil.US_ASCII) + '\'');
    }
    
    private static int parseCode(final ByteBuf buffer) {
        final int first = parseNumber(buffer.readByte()) * 100;
        final int second = parseNumber(buffer.readByte()) * 10;
        final int third = parseNumber(buffer.readByte());
        return first + second + third;
    }
    
    private static int parseNumber(final byte b) {
        return Character.digit((char)b, 10);
    }
}
