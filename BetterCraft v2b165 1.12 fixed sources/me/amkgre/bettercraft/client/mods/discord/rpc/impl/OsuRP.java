// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.discord.rpc.impl;

import net.minecraft.client.Minecraft;
import me.amkgre.bettercraft.client.mods.discord.rpc.DiscordRPContent;
import me.amkgre.bettercraft.client.mods.discord.rpc.DiscordRPType;
import me.amkgre.bettercraft.client.mods.discord.rpc.DiscordRP;

public class OsuRP extends DiscordRP
{
    @Override
    public String getApplicationID() {
        return "784819466289938442";
    }
    
    @Override
    public DiscordRPType getType() {
        return DiscordRPType.OSU;
    }
    
    @Override
    public DiscordRPContent getJoinServerContent() {
        final String firstLine = "Menu...";
        final String secondLine = new String();
        Minecraft.getMinecraft();
        return new DiscordRPContent(firstLine, secondLine, String.valueOf(Minecraft.getSession().getUsername()) + " (rank #41,082)", "osu!");
    }
    
    @Override
    public DiscordRPContent getLeaveServerContent() {
        final String firstLine = "Menu...";
        final String secondLine = new String();
        Minecraft.getMinecraft();
        return new DiscordRPContent(firstLine, secondLine, String.valueOf(Minecraft.getSession().getUsername()) + " (rank #41,082)", "osu!");
    }
    
    @Override
    public boolean hasSmallImage() {
        return true;
    }
}
