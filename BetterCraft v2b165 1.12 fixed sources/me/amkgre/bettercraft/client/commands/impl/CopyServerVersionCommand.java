// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.commands.impl;

import me.amkgre.bettercraft.client.utils.ClipboardUtils;
import net.minecraft.client.Minecraft;
import me.amkgre.bettercraft.client.commands.Command;

public class CopyServerVersionCommand extends Command
{
    @Override
    public void execute(final String[] args) {
        if (args.length == 0) {
            Command.clientMSG("The Server Version was saved in your §dclipboard!", true);
            String toCopy = "vanilla";
            if (!Minecraft.getMinecraft().isIntegratedServerRunning()) {
                toCopy = Minecraft.getMinecraft().player.getServerBrand().toString();
            }
            ClipboardUtils.setClipboard(toCopy);
        }
    }
    
    @Override
    public String getName() {
        return "copyversion";
    }
}
