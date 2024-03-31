// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.teamspeak.response;

import me.amkgre.bettercraft.client.mods.teamspeak.util.EscapeUtil;

public class TeamSpeakCommandResponse extends TeamSpeakResponse
{
    private final int errorId;
    private final String errorMsg;
    
    public TeamSpeakCommandResponse(final String message, final int errorId, final String errorMsg) {
        super(message);
        this.errorId = errorId;
        this.errorMsg = EscapeUtil.unescape(errorMsg);
    }
    
    public int getErrorId() {
        return this.errorId;
    }
    
    public String getErrorMsg() {
        return this.errorMsg;
    }
}
