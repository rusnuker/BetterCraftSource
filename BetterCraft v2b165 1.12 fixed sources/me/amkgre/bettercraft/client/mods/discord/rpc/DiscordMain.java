// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.discord.rpc;

import me.amkgre.bettercraft.client.mods.discord.rpc.events.EventRegistry;

public class DiscordMain
{
    private static final DiscordMain INSTANCE;
    public EventRegistry eventRegistry;
    private DiscordRPRegistry discordRP;
    
    static {
        INSTANCE = new DiscordMain();
    }
    
    public DiscordMain() {
        this.eventRegistry = new EventRegistry();
        this.discordRP = new DiscordRPRegistry();
    }
    
    public static final DiscordMain getInstance() {
        return DiscordMain.INSTANCE;
    }
    
    public void init() {
        this.discordRP.startRPC(DiscordRPType.BETTERCRAFT);
    }
    
    public void shutdown() {
        this.discordRP.shutdownRPC();
    }
    
    public DiscordRPRegistry getDiscordRP() {
        return this.discordRP;
    }
    
    public EventRegistry getEventRegistry() {
        return this.eventRegistry;
    }
}
