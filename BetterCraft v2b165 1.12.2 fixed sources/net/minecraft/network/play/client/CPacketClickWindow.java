// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.network.play.client;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.Packet;

public class CPacketClickWindow implements Packet<INetHandlerPlayServer>
{
    private int windowId;
    private int slotId;
    private int usedButton;
    private short actionNumber;
    private ItemStack clickedItem;
    private ClickType mode;
    
    public CPacketClickWindow() {
        this.clickedItem = ItemStack.field_190927_a;
    }
    
    public CPacketClickWindow(final int windowIdIn, final int slotIdIn, final int usedButtonIn, final ClickType modeIn, final ItemStack clickedItemIn, final short actionNumberIn) {
        this.clickedItem = ItemStack.field_190927_a;
        this.windowId = windowIdIn;
        this.slotId = slotIdIn;
        this.usedButton = usedButtonIn;
        this.clickedItem = clickedItemIn.copy();
        this.actionNumber = actionNumberIn;
        this.mode = modeIn;
    }
    
    @Override
    public void processPacket(final INetHandlerPlayServer handler) {
        handler.processClickWindow(this);
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.windowId = buf.readByte();
        this.slotId = buf.readShort();
        this.usedButton = buf.readByte();
        this.actionNumber = buf.readShort();
        this.mode = buf.readEnumValue(ClickType.class);
        this.clickedItem = buf.readItemStackFromBuffer();
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeByte(this.windowId);
        buf.writeShort(this.slotId);
        buf.writeByte(this.usedButton);
        buf.writeShort(this.actionNumber);
        buf.writeEnumValue(this.mode);
        buf.writeItemStackToBuffer(this.clickedItem);
    }
    
    public int getWindowId() {
        return this.windowId;
    }
    
    public int getSlotId() {
        return this.slotId;
    }
    
    public int getUsedButton() {
        return this.usedButton;
    }
    
    public short getActionNumber() {
        return this.actionNumber;
    }
    
    public ItemStack getClickedItem() {
        return this.clickedItem;
    }
    
    public ClickType getClickType() {
        return this.mode;
    }
}
