// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.teamspeak.tslogs;

import org.apache.logging.log4j.LogManager;
import me.amkgre.bettercraft.client.mods.teamspeak.TeamSpeak;
import me.amkgre.bettercraft.client.mods.teamspeak.impl.MessageImpl;
import javax.swing.text.html.HTMLEditorKit;
import java.io.File;
import me.amkgre.bettercraft.client.mods.teamspeak.impl.ServerInfoImpl;

public class ServerLogFileParser extends LogFileParser
{
    public ServerLogFileParser(final ServerInfoImpl serverInfo, final File logDirectory) {
        super(serverInfo, new File(logDirectory, "server.html"));
    }
    
    @Override
    protected HTMLEditorKit.ParserCallback createParserCallback() {
        return new Parser((Parser)null);
    }
    
    private class Parser extends HTMLEditorKit.ParserCallback
    {
        private StringBuilder builder;
        
        private Parser() {
            this.builder = new StringBuilder();
        }
        
        @Override
        public void handleText(final char[] data, final int pos) {
            this.builder.append(data);
        }
        
        @Override
        public void handleEndOfLineString(final String eol) {
            final String message = this.builder.toString();
            ServerLogFileParser.this.serverInfo.getServerTab().getServerChat().addMessage(new MessageImpl(message, System.currentTimeMillis()));
            if (TeamSpeak.isDebugMode()) {
                LogManager.getLogger().info(LogFileParseManager.logMarkerTail, message);
            }
        }
    }
}
