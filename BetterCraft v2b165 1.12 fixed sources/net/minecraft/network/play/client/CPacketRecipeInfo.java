// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.network.play.client;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.network.PacketBuffer;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.Packet;

public class CPacketRecipeInfo implements Packet<INetHandlerPlayServer>
{
    private Purpose field_194157_a;
    private IRecipe field_193649_d;
    private boolean field_192631_e;
    private boolean field_192632_f;
    
    public CPacketRecipeInfo() {
    }
    
    public CPacketRecipeInfo(final IRecipe p_i47518_1_) {
        this.field_194157_a = Purpose.SHOWN;
        this.field_193649_d = p_i47518_1_;
    }
    
    public CPacketRecipeInfo(final boolean p_i47424_1_, final boolean p_i47424_2_) {
        this.field_194157_a = Purpose.SETTINGS;
        this.field_192631_e = p_i47424_1_;
        this.field_192632_f = p_i47424_2_;
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.field_194157_a = buf.readEnumValue(Purpose.class);
        if (this.field_194157_a == Purpose.SHOWN) {
            this.field_193649_d = CraftingManager.func_193374_a(buf.readInt());
        }
        else if (this.field_194157_a == Purpose.SETTINGS) {
            this.field_192631_e = buf.readBoolean();
            this.field_192632_f = buf.readBoolean();
        }
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeEnumValue(this.field_194157_a);
        if (this.field_194157_a == Purpose.SHOWN) {
            buf.writeInt(CraftingManager.func_193375_a(this.field_193649_d));
        }
        else if (this.field_194157_a == Purpose.SETTINGS) {
            buf.writeBoolean(this.field_192631_e);
            buf.writeBoolean(this.field_192632_f);
        }
    }
    
    @Override
    public void processPacket(final INetHandlerPlayServer handler) {
        handler.func_191984_a(this);
    }
    
    public Purpose func_194156_a() {
        return this.field_194157_a;
    }
    
    public IRecipe func_193648_b() {
        return this.field_193649_d;
    }
    
    public boolean func_192624_c() {
        return this.field_192631_e;
    }
    
    public boolean func_192625_d() {
        return this.field_192632_f;
    }
    
    public enum Purpose
    {
        SHOWN("SHOWN", 0), 
        SETTINGS("SETTINGS", 1);
        
        private Purpose(final String s, final int n) {
        }
    }
}
