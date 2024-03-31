// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.discord.rpc;

public abstract class DiscordRP
{
    private boolean running;
    
    public abstract String getApplicationID();
    
    public abstract DiscordRPType getType();
    
    public abstract DiscordRPContent getJoinServerContent();
    
    public abstract DiscordRPContent getLeaveServerContent();
    
    public abstract boolean hasSmallImage();
    
    public void setRunning(final boolean value) {
        this.running = value;
    }
    
    public boolean isRunning() {
        return this.running;
    }
}
