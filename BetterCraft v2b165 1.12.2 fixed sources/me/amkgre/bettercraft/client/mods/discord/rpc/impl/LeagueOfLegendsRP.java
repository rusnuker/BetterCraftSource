// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.discord.rpc.impl;

import me.amkgre.bettercraft.client.mods.discord.rpc.DiscordRPContent;
import me.amkgre.bettercraft.client.mods.discord.rpc.DiscordRPType;
import me.amkgre.bettercraft.client.mods.discord.rpc.DiscordRP;

public class LeagueOfLegendsRP extends DiscordRP
{
    @Override
    public String getApplicationID() {
        return "784821241919963146";
    }
    
    @Override
    public DiscordRPType getType() {
        return DiscordRPType.LEAGUEOFLEGENDS;
    }
    
    @Override
    public DiscordRPContent getJoinServerContent() {
        return new DiscordRPContent("Summoner's Rift (Ranked)", "In Lobby", new String(), new String());
    }
    
    @Override
    public DiscordRPContent getLeaveServerContent() {
        return new DiscordRPContent("Summoner's Rift (Ranked)", "In Lobby", new String(), new String());
    }
    
    @Override
    public boolean hasSmallImage() {
        return false;
    }
}
