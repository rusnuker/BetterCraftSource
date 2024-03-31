// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.commands.impl;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import javax.imageio.ImageIO;
import java.net.URL;
import me.amkgre.bettercraft.client.mods.hologram.impl.PictureHologram;
import me.amkgre.bettercraft.client.Client;
import me.amkgre.bettercraft.client.commands.CommandManager;
import me.amkgre.bettercraft.client.commands.Command;

public class HologramCommand extends Command
{
    @Override
    public void execute(final String[] args) {
        if (args.length == 0) {
            Command.clientMSG("§m§8----------§r §5Hologram §m§8----------", true);
            Command.clientMSG("§d" + CommandManager.syntax + "hologram <x> <y> <z> <text>", true);
            Command.clientMSG("§d" + CommandManager.syntax + "hologram list", true);
            Command.clientMSG("§d" + CommandManager.syntax + "hologram load <url> <height> <width>", true);
            Command.clientMSG("§m§8----------§r §5Hologram §m§8----------", true);
        }
        else if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("list")) {
                Client.getInstance().getHologramManager().listHolograms();
            }
            else if (args[0].equalsIgnoreCase("load")) {
                new PictureHologram().init();
                try {
                    final BufferedImage img = ImageIO.read(new URL(args[1]));
                    PictureHologram.client.setLoc();
                    PictureHologram.client.setup(args[1], Integer.valueOf(args[2].replace("%", String.valueOf(img.getHeight()))), Integer.valueOf(args[3].replace("%", String.valueOf(img.getWidth()))));
                }
                catch (final MalformedURLException ex) {}
                catch (final IOException ex2) {}
            }
            else if (args.length > 0) {
                try {
                    final StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 3; i < args.length; ++i) {
                        stringBuilder.append(" ").append(args[i]);
                    }
                    Client.getInstance().getHologramManager().spawnHologram(stringBuilder.toString().trim(), args[0], args[1], args[2]);
                }
                catch (final Exception ex3) {}
            }
            else {
                Command.clientMSG("§cType hologram", true);
            }
        }
    }
    
    @Override
    public String getName() {
        return "hologram";
    }
}
