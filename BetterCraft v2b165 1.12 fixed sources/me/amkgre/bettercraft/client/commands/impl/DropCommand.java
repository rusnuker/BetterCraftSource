// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.commands.impl;

import net.minecraft.client.Minecraft;
import me.amkgre.bettercraft.client.commands.Command;

public class DropCommand extends Command
{
    @Override
    public void execute(final String[] args) {
        if (args.length == 0) {
            for (int i2 = 0; i2 < 500; ++i2) {
                Minecraft.getMinecraft().player.dropItem(true);
            }
        }
    }
    
    @Override
    public String getName() {
        return "drop";
    }
}
