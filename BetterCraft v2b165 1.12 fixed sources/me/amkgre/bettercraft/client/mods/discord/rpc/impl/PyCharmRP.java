// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.discord.rpc.impl;

import me.amkgre.bettercraft.client.mods.discord.rpc.DiscordRPContent;
import me.amkgre.bettercraft.client.mods.discord.rpc.DiscordRPType;
import me.amkgre.bettercraft.client.mods.discord.rpc.DiscordRP;

public class PyCharmRP extends DiscordRP
{
    @Override
    public String getApplicationID() {
        return "784826666353819649";
    }
    
    @Override
    public DiscordRPType getType() {
        return DiscordRPType.PYCHARM;
    }
    
    @Override
    public DiscordRPContent getJoinServerContent() {
        return new DiscordRPContent("MCP", "Editing Main.py", "Python", "2020.3");
    }
    
    @Override
    public DiscordRPContent getLeaveServerContent() {
        return new DiscordRPContent("Idle", "", "Python", "2020.3");
    }
    
    @Override
    public boolean hasSmallImage() {
        return true;
    }
}
