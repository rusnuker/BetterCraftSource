// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.teamspeak.net;

import me.amkgre.bettercraft.client.mods.teamspeak.response.TeamSpeakEventResponse;
import me.amkgre.bettercraft.client.mods.teamspeak.event.EventType;
import me.amkgre.bettercraft.client.mods.teamspeak.TeamSpeakServerConnectionResponse;
import me.amkgre.bettercraft.client.mods.teamspeak.response.TeamSpeakCommandResponse;
import java.util.List;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import java.nio.charset.Charset;
import io.netty.handler.codec.ByteToMessageDecoder;

public class TeamSpeakDecoder extends ByteToMessageDecoder
{
    private final Charset charset;
    
    public TeamSpeakDecoder() {
        this(Charset.defaultCharset());
    }
    
    public TeamSpeakDecoder(final Charset charset) {
        if (charset == null) {
            throw new NullPointerException("charset");
        }
        this.charset = charset;
    }
    
    @Override
    protected void decode(final ChannelHandlerContext channelHandlerContext, final ByteBuf byteBuf, final List<Object> out) throws Exception {
        final String line = byteBuf.toString(this.charset);
        if (line.contains("\rerror id=")) {
            final int lastErrorId = line.lastIndexOf("error id=");
            final int lastMsgId = line.lastIndexOf("msg=");
            out.add(new TeamSpeakCommandResponse(line.substring(0, line.lastIndexOf("\rerror id=")).replace("\r", "\n"), Integer.parseInt(line.substring(lastErrorId + "error id=".length(), lastMsgId - 1)), line.substring(lastMsgId + "msg=".length())));
            byteBuf.clear();
            return;
        }
        if (line.contains("\rselected schandlerid=") && line.startsWith("TS3 Client")) {
            out.add(new TeamSpeakServerConnectionResponse(line, Integer.parseInt(line.substring(line.lastIndexOf("=") + 1)), line.contains("Use the \"auth\" command to authenticate yourself")));
            byteBuf.clear();
            return;
        }
        final String replace = line.replace("\r", "");
        final EventType event = EventType.byName(replace.split(" ")[0]);
        if (event != null) {
            out.add(new TeamSpeakEventResponse(line, event));
            byteBuf.clear();
        }
    }
}
