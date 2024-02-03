// 
// Decompiled by Procyon v0.6.0
// 

package me.nzxtercode.bettercraft.client.commands.impl;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ChatComponentText;
import me.nzxtercode.bettercraft.client.BetterCraft;
import net.minecraft.client.gui.GuiScreen;
import java.net.InetAddress;
import net.minecraft.client.multiplayer.ServerAddress;
import net.minecraft.client.Minecraft;
import me.nzxtercode.bettercraft.client.commands.Command;

public class CopyIPCommand extends Command
{
    private String tocopy;
    
    public CopyIPCommand() {
        super("copyip", new String[] { "ip" });
    }
    
    @Override
    public void run(final String alias, final String[] args) {
        if (!Minecraft.getMinecraft().isIntegratedServerRunning()) {
            try {
                final ServerAddress serveradress = ServerAddress.resolveAddress(Minecraft.getMinecraft().getCurrentServerData().serverIP);
                final String address = String.valueOf(InetAddress.getByName(serveradress.getIP()).getHostAddress()) + " " + serveradress.getPort();
                GuiScreen.setClipboardString(address);
                this.tocopy = address;
            }
            catch (final Exception ex) {}
        }
        final GuiNewChat chatGUI = Minecraft.getMinecraft().ingameGUI.getChatGUI();
        BetterCraft.getInstance();
        chatGUI.printChatMessage(new ChatComponentText(String.valueOf(BetterCraft.clientPrefix) + "IP was copied in clipboard: §f" + this.tocopy));
    }
    
    @Override
    public List<String> autocomplete(final int arg, final String[] args) {
        return new ArrayList<String>();
    }
}
