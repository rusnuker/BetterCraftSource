// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.discord.rpc.impl;

import me.amkgre.bettercraft.client.mods.discord.rpc.DiscordRPContent;
import me.amkgre.bettercraft.client.mods.discord.rpc.DiscordRPType;
import me.amkgre.bettercraft.client.mods.discord.rpc.DiscordRP;

public class BlueStacksRP extends DiscordRP
{
    @Override
    public String getApplicationID() {
        return "784827659778654268";
    }
    
    @Override
    public DiscordRPType getType() {
        return DiscordRPType.BLUESTACKS;
    }
    
    @Override
    public DiscordRPContent getJoinServerContent() {
        return new DiscordRPContent("Minecraft", "Playing", "BlueStacks", new String());
    }
    
    @Override
    public DiscordRPContent getLeaveServerContent() {
        return new DiscordRPContent("Menu", "", "BlueStacks", new String());
    }
    
    @Override
    public boolean hasSmallImage() {
        return false;
    }
}
