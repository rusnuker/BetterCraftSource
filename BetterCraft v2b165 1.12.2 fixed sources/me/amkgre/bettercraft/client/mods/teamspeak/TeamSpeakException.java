// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.teamspeak;

import me.amkgre.bettercraft.client.mods.teamspeak.response.TeamSpeakCommandResponse;

public class TeamSpeakException extends RuntimeException
{
    public int errorId;
    
    public TeamSpeakException() {
    }
    
    public TeamSpeakException(final String message) {
        super(message);
    }
    
    public TeamSpeakException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public TeamSpeakException(final Throwable cause) {
        super(cause);
    }
    
    public TeamSpeakException(final TeamSpeakCommandResponse commandResponse) {
        this(String.valueOf(commandResponse.getErrorMsg()) + " (" + commandResponse.getErrorId() + ")");
        this.errorId = commandResponse.getErrorId();
    }
}
