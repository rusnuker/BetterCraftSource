// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class SPacketSetSlot implements Packet<INetHandlerPlayClient>
{
    private int windowId;
    private int slot;
    private ItemStack item;
    
    public SPacketSetSlot() {
        this.item = ItemStack.field_190927_a;
    }
    
    public SPacketSetSlot(final int windowIdIn, final int slotIn, final ItemStack itemIn) {
        this.item = ItemStack.field_190927_a;
        this.windowId = windowIdIn;
        this.slot = slotIn;
        this.item = itemIn.copy();
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleSetSlot(this);
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.windowId = buf.readByte();
        this.slot = buf.readShort();
        this.item = buf.readItemStackFromBuffer();
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeByte(this.windowId);
        buf.writeShort(this.slot);
        buf.writeItemStackToBuffer(this.item);
    }
    
    public int getWindowId() {
        return this.windowId;
    }
    
    public int getSlot() {
        return this.slot;
    }
    
    public ItemStack getStack() {
        return this.item;
    }
}
