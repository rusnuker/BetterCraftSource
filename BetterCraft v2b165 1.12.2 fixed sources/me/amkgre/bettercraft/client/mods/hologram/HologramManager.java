// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.hologram;

import me.amkgre.bettercraft.client.commands.Command;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import java.util.ArrayList;

public class HologramManager
{
    public ArrayList<Hologram> spawnedHolograms;
    
    public HologramManager() {
        this.spawnedHolograms = new ArrayList<Hologram>();
    }
    
    public void spawnHologram(final String text2, String x, String y, String z) {
        x = x.replace("~", new StringBuilder().append(Minecraft.getMinecraft().player.posX).toString());
        y = y.replace("~", new StringBuilder().append(Minecraft.getMinecraft().player.posY).toString());
        z = z.replace("~", new StringBuilder().append(Minecraft.getMinecraft().player.posZ).toString());
        final double xPos = Double.valueOf(x);
        final double yPos = Double.valueOf(y);
        final double zPos = Double.valueOf(z);
        final Hologram hd = new Hologram(text2, xPos, yPos, zPos);
        hd.spawnHologram();
        this.spawnedHolograms.add(hd);
    }
    
    public String getPosFromHologram(final String text2) {
        return String.valueOf(String.valueOf(this.getHologramByText(text2).getX())) + " " + this.getHologramByText(text2).getY() + " " + this.getHologramByText(text2).getZ();
    }
    
    public Hologram getHologramByText(final String text2) {
        for (final Hologram ho : this.spawnedHolograms) {
            if (!ho.getText().equalsIgnoreCase(text2)) {
                continue;
            }
            return ho;
        }
        return null;
    }
    
    public void listHolograms() {
        for (final Hologram hd : this.spawnedHolograms) {
            Command.clientMSG("", true);
            Command.clientMSG("§dName §7" + hd.getText(), true);
            Command.clientMSG("§dX §7" + hd.getX(), true);
            Command.clientMSG("§dY §7" + hd.getY(), true);
            Command.clientMSG("§dZ §7" + hd.getZ(), true);
            Command.clientMSG("§dName §7" + hd.getDate(), true);
        }
    }
}
