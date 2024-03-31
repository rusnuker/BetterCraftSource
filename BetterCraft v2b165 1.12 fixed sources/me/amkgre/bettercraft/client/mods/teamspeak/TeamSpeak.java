// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.teamspeak;

import java.net.URI;
import java.awt.Desktop;
import me.amkgre.bettercraft.client.mods.teamspeak.api.TeamSpeakClient;

public class TeamSpeak
{
    private static final TeamSpeakClient CLIENT;
    private static boolean debugMode;
    
    static {
        CLIENT = new TeamSpeakClientImpl();
        TeamSpeak.debugMode = false;
    }
    
    public static TeamSpeakClient getClient() {
        return TeamSpeak.CLIENT;
    }
    
    public static void setDebugMode(final boolean debugMode) {
        TeamSpeak.debugMode = debugMode;
    }
    
    public static boolean isDebugMode() {
        return TeamSpeak.debugMode;
    }
    
    public static void startClient(final String server, final String nickname) {
        try {
            Desktop.getDesktop().browse(new URI("ts3server://" + server + "?nickname=" + nickname));
        }
        catch (final Throwable throwable) {
            throw new TeamSpeakException(throwable);
        }
    }
}
