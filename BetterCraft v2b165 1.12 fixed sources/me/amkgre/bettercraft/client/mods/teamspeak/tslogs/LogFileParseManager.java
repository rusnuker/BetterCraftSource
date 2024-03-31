// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.teamspeak.tslogs;

import org.apache.commons.codec.binary.Base64;
import com.google.common.base.Charsets;
import java.io.File;
import me.amkgre.bettercraft.client.mods.teamspeak.util.Utils;
import me.amkgre.bettercraft.client.mods.teamspeak.impl.ServerInfoImpl;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.Marker;

public class LogFileParseManager
{
    public static final Marker logMarkerTail;
    private LogFileParser serverLogParser;
    private LogFileParser channelLogParser;
    
    static {
        logMarkerTail = MarkerManager.getMarker("CHAT_TAIL");
    }
    
    public LogFileParseManager(final ServerInfoImpl serverInfo) {
        final File dir = new File(new File(Utils.getTeamspeakDirectory(), "chats"), Base64.encodeBase64String(serverInfo.getUniqueId().getBytes(Charsets.UTF_8)));
        this.serverLogParser = new ServerLogFileParser(serverInfo, dir);
        this.channelLogParser = new ChannelLogFileParser(serverInfo, dir);
    }
    
    public void stop() {
        this.serverLogParser.stop();
        this.channelLogParser.stop();
    }
}
