// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.discord.rpc.impl;

import me.amkgre.bettercraft.client.Client;
import net.minecraft.client.Minecraft;
import me.amkgre.bettercraft.client.mods.discord.rpc.DiscordRPContent;
import me.amkgre.bettercraft.client.mods.discord.rpc.DiscordRPType;
import me.amkgre.bettercraft.client.mods.discord.rpc.DiscordRP;

public class BetterCraftRP extends DiscordRP
{
    @Override
    public String getApplicationID() {
        return "752623035743207544";
    }
    
    @Override
    public DiscordRPType getType() {
        return DiscordRPType.BETTERCRAFT;
    }
    
    @Override
    public DiscordRPContent getJoinServerContent() {
        Minecraft.getMinecraft();
        final String username = Minecraft.session.username;
        final String string = "Playing " + Minecraft.getMinecraft().getCurrentServerData().serverIP;
        Client.getInstance();
        final String clientVersion = Client.clientVersion;
        final StringBuilder sb = new StringBuilder("Made by ");
        Client.getInstance();
        return new DiscordRPContent(username, string, clientVersion, sb.append(Client.clientAuthor).toString());
    }
    
    @Override
    public DiscordRPContent getLeaveServerContent() {
        Minecraft.getMinecraft();
        final String username = Minecraft.session.username;
        final String secondLine = "Menu";
        Client.getInstance();
        final String clientVersion = Client.clientVersion;
        final StringBuilder sb = new StringBuilder("Made by ");
        Client.getInstance();
        return new DiscordRPContent(username, secondLine, clientVersion, sb.append(Client.clientAuthor).toString());
    }
    
    @Override
    public boolean hasSmallImage() {
        return true;
    }
}
