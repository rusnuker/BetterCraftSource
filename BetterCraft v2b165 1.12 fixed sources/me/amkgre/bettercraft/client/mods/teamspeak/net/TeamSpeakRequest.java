// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.teamspeak.net;

import me.amkgre.bettercraft.client.mods.teamspeak.response.TeamSpeakCommandResponse;
import me.amkgre.bettercraft.client.mods.teamspeak.util.Callback;
import me.amkgre.bettercraft.client.mods.teamspeak.request.Request;

public class TeamSpeakRequest
{
    private final Request request;
    private final Callback<TeamSpeakCommandResponse> callback;
    private final RequestChain chain;
    
    TeamSpeakRequest(final Request request, final Callback<TeamSpeakCommandResponse> callback, final RequestChain chain) {
        this.request = request;
        this.callback = callback;
        this.chain = chain;
    }
    
    public Request getRequest() {
        return this.request;
    }
    
    public Callback<TeamSpeakCommandResponse> getCallback() {
        return this.callback;
    }
    
    public RequestChain getChain() {
        return this.chain;
    }
    
    @Override
    public String toString() {
        return "TeamSpeakRequest{request=" + this.request + '}';
    }
}
