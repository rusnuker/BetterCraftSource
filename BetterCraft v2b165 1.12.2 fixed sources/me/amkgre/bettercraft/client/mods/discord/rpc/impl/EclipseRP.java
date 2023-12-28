// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.discord.rpc.impl;

import me.amkgre.bettercraft.client.mods.discord.rpc.DiscordRPContent;
import me.amkgre.bettercraft.client.mods.discord.rpc.DiscordRPType;
import me.amkgre.bettercraft.client.mods.discord.rpc.DiscordRP;

public class EclipseRP extends DiscordRP
{
    @Override
    public String getApplicationID() {
        return "784824709946146816";
    }
    
    @Override
    public DiscordRPType getType() {
        return DiscordRPType.ECLIPSE;
    }
    
    @Override
    public DiscordRPContent getJoinServerContent() {
        return new DiscordRPContent("Editing Main.java", "Working on MCP", "Programming in Java", "Eclipse IDE");
    }
    
    @Override
    public DiscordRPContent getLeaveServerContent() {
        return new DiscordRPContent("Editing Main.java", "Working on MCP", "Programming in Java", "Eclipse IDE");
    }
    
    @Override
    public boolean hasSmallImage() {
        return true;
    }
}
