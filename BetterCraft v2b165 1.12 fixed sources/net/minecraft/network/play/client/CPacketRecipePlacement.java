// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.network.play.client;

import net.minecraft.network.INetHandler;
import java.util.Iterator;
import net.minecraft.item.ItemStack;
import com.google.common.collect.Lists;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import java.util.List;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.Packet;

public class CPacketRecipePlacement implements Packet<INetHandlerPlayServer>
{
    private int field_192616_a;
    private short field_192617_b;
    private List<ItemMove> field_192618_c;
    private List<ItemMove> field_192619_d;
    
    public CPacketRecipePlacement() {
    }
    
    public CPacketRecipePlacement(final int p_i47425_1_, final List<ItemMove> p_i47425_2_, final List<ItemMove> p_i47425_3_, final short p_i47425_4_) {
        this.field_192616_a = p_i47425_1_;
        this.field_192617_b = p_i47425_4_;
        this.field_192618_c = p_i47425_2_;
        this.field_192619_d = p_i47425_3_;
    }
    
    public int func_192613_a() {
        return this.field_192616_a;
    }
    
    public short func_192614_b() {
        return this.field_192617_b;
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.field_192616_a = buf.readByte();
        this.field_192617_b = buf.readShort();
        this.field_192618_c = this.func_192611_c(buf);
        this.field_192619_d = this.func_192611_c(buf);
    }
    
    private List<ItemMove> func_192611_c(final PacketBuffer p_192611_1_) throws IOException {
        final int i = p_192611_1_.readShort();
        final List<ItemMove> list = (List<ItemMove>)Lists.newArrayListWithCapacity(i);
        for (int j = 0; j < i; ++j) {
            final ItemStack itemstack = p_192611_1_.readItemStackFromBuffer();
            final byte b0 = p_192611_1_.readByte();
            final byte b2 = p_192611_1_.readByte();
            list.add(new ItemMove(itemstack, b0, b2));
        }
        return list;
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeByte(this.field_192616_a);
        buf.writeShort(this.field_192617_b);
        this.func_192612_a(buf, this.field_192618_c);
        this.func_192612_a(buf, this.field_192619_d);
    }
    
    private void func_192612_a(final PacketBuffer p_192612_1_, final List<ItemMove> p_192612_2_) {
        p_192612_1_.writeShort(p_192612_2_.size());
        for (final ItemMove cpacketrecipeplacement$itemmove : p_192612_2_) {
            p_192612_1_.writeItemStackToBuffer(cpacketrecipeplacement$itemmove.field_192673_a);
            p_192612_1_.writeByte(cpacketrecipeplacement$itemmove.field_192674_b);
            p_192612_1_.writeByte(cpacketrecipeplacement$itemmove.field_192675_c);
        }
    }
    
    @Override
    public void processPacket(final INetHandlerPlayServer handler) {
        handler.func_191985_a(this);
    }
    
    public List<ItemMove> func_192615_c() {
        return this.field_192619_d;
    }
    
    public List<ItemMove> func_192610_d() {
        return this.field_192618_c;
    }
    
    public static class ItemMove
    {
        public ItemStack field_192673_a;
        public int field_192674_b;
        public int field_192675_c;
        
        public ItemMove(final ItemStack p_i47401_1_, final int p_i47401_2_, final int p_i47401_3_) {
            this.field_192673_a = p_i47401_1_.copy();
            this.field_192674_b = p_i47401_2_;
            this.field_192675_c = p_i47401_3_;
        }
    }
}
