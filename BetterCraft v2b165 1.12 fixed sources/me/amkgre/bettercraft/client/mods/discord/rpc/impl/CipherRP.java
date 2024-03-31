// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.discord.rpc.impl;

import net.minecraft.client.Minecraft;
import me.amkgre.bettercraft.client.mods.discord.rpc.DiscordRPContent;
import me.amkgre.bettercraft.client.mods.discord.rpc.DiscordRPType;
import me.amkgre.bettercraft.client.mods.discord.rpc.DiscordRP;

public class CipherRP extends DiscordRP
{
    @Override
    public String getApplicationID() {
        return "785112877949386772";
    }
    
    @Override
    public DiscordRPType getType() {
        return DiscordRPType.CIPHER;
    }
    
    @Override
    public DiscordRPContent getJoinServerContent() {
        return new DiscordRPContent("Playing " + Minecraft.getMinecraft().getCurrentServerData().serverIP, new String(), new String(), new String());
    }
    
    @Override
    public DiscordRPContent getLeaveServerContent() {
        return new DiscordRPContent("Fabric Injection Mod", new String(), new String(), new String());
    }
    
    @Override
    public boolean hasSmallImage() {
        return false;
    }
}
