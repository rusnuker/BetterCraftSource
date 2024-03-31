// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.teamspeak.util;

import me.amkgre.bettercraft.client.mods.teamspeak.request.Request;
import me.amkgre.bettercraft.client.mods.teamspeak.TeamSpeakException;
import me.amkgre.bettercraft.client.mods.teamspeak.response.TeamSpeakCommandResponse;
import me.amkgre.bettercraft.client.mods.teamspeak.request.WhoAmIRequest;
import me.amkgre.bettercraft.client.mods.teamspeak.net.TeamSpeakNetworkManager;

public class KeepAliveThread extends Thread
{
    private static final int SLEEP = 60000;
    private final TeamSpeakNetworkManager networkManager;
    
    public KeepAliveThread(final TeamSpeakNetworkManager networkManager) {
        super("TeamSpeak Keep Alive");
        this.networkManager = networkManager;
    }
    
    @Override
    public void run() {
        while (this.networkManager.isConnected()) {
            try {
                Thread.sleep(60000L);
            }
            catch (final InterruptedException ex) {}
            if (!this.networkManager.isConnected()) {
                continue;
            }
            this.networkManager.sendRequest(new WhoAmIRequest(), new EmptyCallback<TeamSpeakCommandResponse>() {
                @Override
                public void exceptionCaught(final TeamSpeakException exception) {
                }
            });
        }
    }
    
    public void shutdown() {
        this.interrupt();
    }
}
