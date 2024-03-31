// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.teamspeak.net;

import io.netty.buffer.ByteBufUtil;
import java.nio.CharBuffer;
import java.util.List;
import io.netty.channel.ChannelHandlerContext;
import java.nio.charset.Charset;
import me.amkgre.bettercraft.client.mods.teamspeak.request.Request;
import io.netty.handler.codec.MessageToMessageEncoder;

public class TeamSpeakEncoder extends MessageToMessageEncoder<Request>
{
    private final Charset charset;
    
    public TeamSpeakEncoder() {
        this(Charset.defaultCharset());
    }
    
    public TeamSpeakEncoder(final Charset charset) {
        if (charset == null) {
            throw new NullPointerException("charset");
        }
        this.charset = charset;
    }
    
    @Override
    protected void encode(final ChannelHandlerContext ctx, final Request msg, final List<Object> out) throws Exception {
        out.add(ByteBufUtil.encodeString(ctx.alloc(), CharBuffer.wrap(String.valueOf(msg.toString()) + "\r\n"), this.charset));
    }
}
