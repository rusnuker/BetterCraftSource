// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.discord.rpc.impl;

import me.amkgre.bettercraft.client.mods.discord.rpc.DiscordRPRegistry;
import me.amkgre.bettercraft.client.mods.discord.rpc.DiscordRPContent;
import me.amkgre.bettercraft.client.mods.discord.rpc.DiscordRPType;
import me.amkgre.bettercraft.client.mods.discord.rpc.DiscordRP;

public class RainbowSixSiegeRP extends DiscordRP
{
    @Override
    public String getApplicationID() {
        return "784818906242089030";
    }
    
    @Override
    public DiscordRPType getType() {
        return DiscordRPType.RAINBOWSIXSIEGE;
    }
    
    @Override
    public DiscordRPContent getJoinServerContent() {
        return new DiscordRPContent("QUICK MATCH match - Round " + DiscordRPRegistry.getRandomInt(1, 4) + " / 4", new String(), new String(), new String());
    }
    
    @Override
    public DiscordRPContent getLeaveServerContent() {
        return new DiscordRPContent("in MENU", new String(), new String(), new String());
    }
    
    @Override
    public boolean hasSmallImage() {
        return false;
    }
}
