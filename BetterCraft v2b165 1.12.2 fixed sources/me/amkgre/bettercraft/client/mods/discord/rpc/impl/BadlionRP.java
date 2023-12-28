// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.discord.rpc.impl;

import me.amkgre.bettercraft.client.mods.discord.rpc.DiscordRPContent;
import me.amkgre.bettercraft.client.mods.discord.rpc.DiscordRPType;
import me.amkgre.bettercraft.client.mods.discord.rpc.DiscordRP;

public class BadlionRP extends DiscordRP
{
    @Override
    public String getApplicationID() {
        return "784812906960650274";
    }
    
    @Override
    public DiscordRPType getType() {
        return DiscordRPType.BADLION;
    }
    
    @Override
    public DiscordRPContent getJoinServerContent() {
        return new DiscordRPContent("Playing Minecraft 1.8.9", new String(), "Using Badlion Client Minecraft Launcher", new String());
    }
    
    @Override
    public DiscordRPContent getLeaveServerContent() {
        return new DiscordRPContent("Idle", new String(), "Using Badlion Client Minecraft Launcher", new String());
    }
    
    @Override
    public boolean hasSmallImage() {
        return false;
    }
}
