// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.network.play.server;

import net.minecraft.network.INetHandler;
import java.util.Iterator;
import java.io.IOException;
import net.minecraft.item.crafting.CraftingManager;
import com.google.common.collect.Lists;
import net.minecraft.network.PacketBuffer;
import net.minecraft.item.crafting.IRecipe;
import java.util.List;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.Packet;

public class SPacketRecipeBook implements Packet<INetHandlerPlayClient>
{
    private State field_193646_a;
    private List<IRecipe> field_192596_a;
    private List<IRecipe> field_193647_c;
    private boolean field_192598_c;
    private boolean field_192599_d;
    
    public SPacketRecipeBook() {
    }
    
    public SPacketRecipeBook(final State p_i47597_1_, final List<IRecipe> p_i47597_2_, final List<IRecipe> p_i47597_3_, final boolean p_i47597_4_, final boolean p_i47597_5_) {
        this.field_193646_a = p_i47597_1_;
        this.field_192596_a = p_i47597_2_;
        this.field_193647_c = p_i47597_3_;
        this.field_192598_c = p_i47597_4_;
        this.field_192599_d = p_i47597_5_;
    }
    
    @Override
    public void processPacket(final INetHandlerPlayClient handler) {
        handler.func_191980_a(this);
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.field_193646_a = buf.readEnumValue(State.class);
        this.field_192598_c = buf.readBoolean();
        this.field_192599_d = buf.readBoolean();
        int i = buf.readVarIntFromBuffer();
        this.field_192596_a = (List<IRecipe>)Lists.newArrayList();
        for (int j = 0; j < i; ++j) {
            this.field_192596_a.add(CraftingManager.func_193374_a(buf.readVarIntFromBuffer()));
        }
        if (this.field_193646_a == State.INIT) {
            i = buf.readVarIntFromBuffer();
            this.field_193647_c = (List<IRecipe>)Lists.newArrayList();
            for (int k = 0; k < i; ++k) {
                this.field_193647_c.add(CraftingManager.func_193374_a(buf.readVarIntFromBuffer()));
            }
        }
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeEnumValue(this.field_193646_a);
        buf.writeBoolean(this.field_192598_c);
        buf.writeBoolean(this.field_192599_d);
        buf.writeVarIntToBuffer(this.field_192596_a.size());
        for (final IRecipe irecipe : this.field_192596_a) {
            buf.writeVarIntToBuffer(CraftingManager.func_193375_a(irecipe));
        }
        if (this.field_193646_a == State.INIT) {
            buf.writeVarIntToBuffer(this.field_193647_c.size());
            for (final IRecipe irecipe2 : this.field_193647_c) {
                buf.writeVarIntToBuffer(CraftingManager.func_193375_a(irecipe2));
            }
        }
    }
    
    public List<IRecipe> func_192595_a() {
        return this.field_192596_a;
    }
    
    public List<IRecipe> func_193644_b() {
        return this.field_193647_c;
    }
    
    public boolean func_192593_c() {
        return this.field_192598_c;
    }
    
    public boolean func_192594_d() {
        return this.field_192599_d;
    }
    
    public State func_194151_e() {
        return this.field_193646_a;
    }
    
    public enum State
    {
        INIT("INIT", 0), 
        ADD("ADD", 1), 
        REMOVE("REMOVE", 2);
        
        private State(final String s, final int n) {
        }
    }
}
