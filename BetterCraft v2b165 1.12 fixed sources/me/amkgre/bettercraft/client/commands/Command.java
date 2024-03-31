// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.commands;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.client.Minecraft;
import me.amkgre.bettercraft.client.Client;

public abstract class Command
{
    public abstract void execute(final String[] p0);
    
    public abstract String getName();
    
    public static void clientMSG(String s, final boolean prefix) {
        String clientPrefix;
        if (prefix) {
            Client.getInstance();
            clientPrefix = Client.clientPrefix;
        }
        else {
            clientPrefix = "";
        }
        s = String.valueOf(String.valueOf(clientPrefix)) + s;
        Minecraft.getMinecraft().player.addChatMessage(new TextComponentString(s.replace("&", "§")));
    }
    
    public static void labyModMSG(String s, final boolean prefix) {
        String labymodPrefix;
        if (prefix) {
            Client.getInstance();
            labymodPrefix = Client.labymodPrefix;
        }
        else {
            labymodPrefix = "";
        }
        s = String.valueOf(String.valueOf(labymodPrefix)) + s;
        Minecraft.getMinecraft().player.addChatMessage(new TextComponentString(s.replace("&", "§")));
    }
    
    public static void ircMSG(String s, final boolean prefix) {
        s = String.valueOf(String.valueOf(prefix ? Client.getInstance().ircbot.prefix : "")) + s;
        Minecraft.getMinecraft().player.addChatMessage(new TextComponentString(s.replace("&", "§")));
    }
}
