// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.teamspeak.api;

public interface OwnClient extends Client
{
    void setNickName(final String p0);
    
    void setAway(final boolean p0);
    
    void setAwayMessage(final String p0);
    
    void setInputMuted(final boolean p0);
    
    void setOutputMuted(final boolean p0);
    
    void setInputDeactivated(final boolean p0);
}
