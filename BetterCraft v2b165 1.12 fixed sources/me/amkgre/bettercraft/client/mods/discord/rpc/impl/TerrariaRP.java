// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.discord.rpc.impl;

import me.amkgre.bettercraft.client.mods.discord.rpc.DiscordRPContent;
import me.amkgre.bettercraft.client.mods.discord.rpc.DiscordRPType;
import me.amkgre.bettercraft.client.mods.discord.rpc.DiscordRP;

public class TerrariaRP extends DiscordRP
{
    @Override
    public String getApplicationID() {
        return "784808588921143338";
    }
    
    @Override
    public DiscordRPType getType() {
        return DiscordRPType.TERRARIA;
    }
    
    @Override
    public DiscordRPContent getJoinServerContent() {
        return new DiscordRPContent(new String(), new String(), new String(), new String());
    }
    
    @Override
    public DiscordRPContent getLeaveServerContent() {
        return new DiscordRPContent(new String(), new String(), new String(), new String());
    }
    
    @Override
    public boolean hasSmallImage() {
        return false;
    }
}
