// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.string;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import java.nio.CharBuffer;
import java.util.List;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.CharsetUtil;
import java.nio.charset.Charset;
import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.MessageToMessageEncoder;

@ChannelHandler.Sharable
public class LineEncoder extends MessageToMessageEncoder<CharSequence>
{
    private final Charset charset;
    private final byte[] lineSeparator;
    
    public LineEncoder() {
        this(LineSeparator.DEFAULT, CharsetUtil.UTF_8);
    }
    
    public LineEncoder(final LineSeparator lineSeparator) {
        this(lineSeparator, CharsetUtil.UTF_8);
    }
    
    public LineEncoder(final Charset charset) {
        this(LineSeparator.DEFAULT, charset);
    }
    
    public LineEncoder(final LineSeparator lineSeparator, final Charset charset) {
        this.charset = ObjectUtil.checkNotNull(charset, "charset");
        this.lineSeparator = ObjectUtil.checkNotNull(lineSeparator, "lineSeparator").value().getBytes(charset);
    }
    
    @Override
    protected void encode(final ChannelHandlerContext ctx, final CharSequence msg, final List<Object> out) throws Exception {
        final ByteBuf buffer = ByteBufUtil.encodeString(ctx.alloc(), CharBuffer.wrap(msg), this.charset, this.lineSeparator.length);
        buffer.writeBytes(this.lineSeparator);
        out.add(buffer);
    }
}
