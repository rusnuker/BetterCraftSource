// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.commands.impl;

import me.amkgre.bettercraft.client.utils.ClipboardUtils;
import java.net.UnknownHostException;
import java.net.InetAddress;
import net.minecraft.client.multiplayer.ServerAddress;
import net.minecraft.client.Minecraft;
import me.amkgre.bettercraft.client.commands.Command;

public class CopyIPCommand extends Command
{
    @Override
    public void execute(final String[] args) {
        if (args.length == 0) {
            Command.clientMSG("The Server IP was saved in your §dclipboard!", true);
            String toCopy = "localhost";
            if (!Minecraft.getMinecraft().isIntegratedServerRunning()) {
                final ServerAddress serveradress = ServerAddress.resolveAddress(Minecraft.getMinecraft().getCurrentServerData().serverIP);
                String address = null;
                try {
                    address = String.valueOf(InetAddress.getByName(serveradress.getIP()).getHostAddress()) + " " + serveradress.getPort();
                }
                catch (final UnknownHostException ex) {}
                toCopy = address;
            }
            ClipboardUtils.setClipboard(toCopy);
        }
    }
    
    @Override
    public String getName() {
        return "copyip";
    }
}
