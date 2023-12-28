// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.teamspeak.response;

import me.amkgre.bettercraft.client.mods.teamspeak.event.EventType;

public class TeamSpeakEventResponse extends TeamSpeakResponse
{
    private final EventType type;
    
    public TeamSpeakEventResponse(final String message, final EventType type) {
        super(message);
        this.type = type;
    }
    
    public EventType getType() {
        return this.type;
    }
}
