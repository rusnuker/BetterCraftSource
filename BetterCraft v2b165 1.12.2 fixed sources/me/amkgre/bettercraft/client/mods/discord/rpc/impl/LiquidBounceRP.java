// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.discord.rpc.impl;

import me.amkgre.bettercraft.client.mods.discord.rpc.DiscordRPRegistry;
import net.minecraft.client.Minecraft;
import me.amkgre.bettercraft.client.mods.discord.rpc.DiscordRPContent;
import me.amkgre.bettercraft.client.mods.discord.rpc.DiscordRPType;
import me.amkgre.bettercraft.client.mods.discord.rpc.DiscordRP;

public class LiquidBounceRP extends DiscordRP
{
    @Override
    public String getApplicationID() {
        return "784828761899270164";
    }
    
    @Override
    public DiscordRPType getType() {
        return DiscordRPType.LIQUIDBOUNCE;
    }
    
    @Override
    public DiscordRPContent getJoinServerContent() {
        return new DiscordRPContent("Server: " + Minecraft.getMinecraft().getCurrentServerData().serverIP, "Enabled " + DiscordRPRegistry.getRandomInt(0, 143) + " / 143 modules", "MC 1.8.9 - LiquidBounce b72", new String());
    }
    
    @Override
    public DiscordRPContent getLeaveServerContent() {
        return new DiscordRPContent(new String(), new String(), "MC 1.8.9 - LiquidBounce b72", new String());
    }
    
    @Override
    public boolean hasSmallImage() {
        return false;
    }
}
