// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.teamspeak.net;

import me.amkgre.bettercraft.client.mods.teamspeak.TeamSpeakException;
import me.amkgre.bettercraft.client.mods.teamspeak.response.TeamSpeakCommandResponse;
import me.amkgre.bettercraft.client.mods.teamspeak.util.Callback;
import me.amkgre.bettercraft.client.mods.teamspeak.request.Request;

public class RequestChain
{
    private final TeamSpeakNetworkManager networkManager;
    private TeamSpeakRequest request;
    
    public RequestChain(final TeamSpeakNetworkManager networkManager) {
        this.networkManager = networkManager;
    }
    
    RequestChain sendThen(final Request command, final Callback<TeamSpeakCommandResponse> callback) {
        final RequestChain chain = new RequestChain(this.networkManager);
        this.request = new TeamSpeakRequest(command, callback, chain);
        return chain;
    }
    
    void sendNextRequest() throws TeamSpeakException {
        if (this.request != null) {
            this.networkManager.sendRequest(this.request);
        }
    }
}
