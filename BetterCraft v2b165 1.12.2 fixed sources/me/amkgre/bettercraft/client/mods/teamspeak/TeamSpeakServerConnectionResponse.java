// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.teamspeak;

import me.amkgre.bettercraft.client.mods.teamspeak.response.TeamSpeakResponse;

public class TeamSpeakServerConnectionResponse extends TeamSpeakResponse
{
    private final int serverConnectionHandlerId;
    private final boolean requiresAuth;
    
    public TeamSpeakServerConnectionResponse(final String message, final int serverConnectionHandlerId, final boolean requiresAuth) {
        super(message);
        this.serverConnectionHandlerId = serverConnectionHandlerId;
        this.requiresAuth = requiresAuth;
    }
    
    public int getServerConnectionHandlerId() {
        return this.serverConnectionHandlerId;
    }
    
    public boolean requiresAuth() {
        return this.requiresAuth;
    }
}
