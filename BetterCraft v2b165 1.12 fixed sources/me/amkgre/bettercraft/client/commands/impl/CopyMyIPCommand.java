// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.commands.impl;

import java.net.UnknownHostException;
import me.amkgre.bettercraft.client.utils.ClipboardUtils;
import java.net.InetAddress;
import me.amkgre.bettercraft.client.commands.Command;

public class CopyMyIPCommand extends Command
{
    @Override
    public void execute(final String[] args) {
        if (args.length == 0) {
            Command.clientMSG("Your local IP was saved in your §dclipboard!", true);
            try {
                ClipboardUtils.setClipboard(InetAddress.getLocalHost().getHostAddress());
            }
            catch (final UnknownHostException e) {
                e.printStackTrace();
            }
        }
    }
    
    @Override
    public String getName() {
        return "copymyip";
    }
}
