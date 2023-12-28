// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.hologram.impl;

import me.amkgre.bettercraft.client.commands.Command;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketCreativeInventoryAction;
import java.io.IOException;
import java.net.MalformedURLException;
import javax.imageio.ImageIO;
import java.net.URL;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

public class PictureHologram
{
    public static PictureHologram client;
    public double x;
    public double y;
    public double z;
    public ItemStack[] stands;
    public int counter;
    public PictureHologramLoader is;
    
    public PictureHologram() {
        this.x = -1.0;
        this.y = -1.0;
        this.z = -1.0;
        this.counter = 0;
    }
    
    public void init() {
        PictureHologram.client = this;
    }
    
    public void setLoc() {
        this.x = Minecraft.getMinecraft().player.posX;
        this.y = Minecraft.getMinecraft().player.posY;
        this.z = Minecraft.getMinecraft().player.posZ;
    }
    
    public void setup(final String link, final int height, final int widht) {
        try {
            this.is = new PictureHologramLoader(ImageIO.read(new URL(link)), height, '\u25fc');
        }
        catch (final MalformedURLException ex) {}
        catch (final IOException ex2) {}
        if (this.is == null) {
            return;
        }
        this.stands = this.is.getArmorStands(this.x, this.y, this.z);
        this.counter = 0;
        this.giveNextArmorstand();
    }
    
    public void giveNextArmorstand() {
        if (this.stands != null) {
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketCreativeInventoryAction(36, this.stands[this.counter++]));
            Command.clientMSG("You musst be to place " + String.valueOf(this.is.pictures - this.counter + 1), true);
        }
        if (this.counter >= this.stands.length) {
            this.counter = 0;
            this.stands = null;
        }
        Command.clientMSG("Finish!", true);
    }
}
