// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.item.Item;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class SPacketCooldown implements Packet<INetHandlerPlayClient>
{
    private Item item;
    private int ticks;
    
    public SPacketCooldown() {
    }
    
    public SPacketCooldown(final Item itemIn, final int ticksIn) {
        this.item = itemIn;
        this.ticks = ticksIn;
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.item = Item.getItemById(buf.readVarIntFromBuffer());
        this.ticks = buf.readVarIntFromBuffer();
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeVarIntToBuffer(Item.getIdFromItem(this.item));
        buf.writeVarIntToBuffer(this.ticks);
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.handleCooldown(this);
    }
    
    public Item getItem() {
        return this.item;
    }
    
    public int getTicks() {
        return this.ticks;
    }
}
