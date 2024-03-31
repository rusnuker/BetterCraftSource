// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.discord.rpc.impl;

import net.minecraft.client.Minecraft;
import me.amkgre.bettercraft.client.mods.discord.rpc.DiscordRPContent;
import me.amkgre.bettercraft.client.mods.discord.rpc.DiscordRPType;
import me.amkgre.bettercraft.client.mods.discord.rpc.DiscordRP;

public class LabyModRP extends DiscordRP
{
    @Override
    public String getApplicationID() {
        return "784815014703267902";
    }
    
    @Override
    public DiscordRPType getType() {
        return DiscordRPType.LABYMOD;
    }
    
    @Override
    public DiscordRPContent getJoinServerContent() {
        return new DiscordRPContent(Minecraft.getMinecraft().getCurrentServerData().serverIP, "Ingame", "MC 1.8.9 - LabyMod 3.7.6", new String());
    }
    
    @Override
    public DiscordRPContent getLeaveServerContent() {
        return new DiscordRPContent("Menu", new String(), "MC 1.8.9 - LabyMod 3.7.6", new String());
    }
    
    @Override
    public boolean hasSmallImage() {
        return false;
    }
}
