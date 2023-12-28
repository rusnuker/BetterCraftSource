// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.advancements.critereon;

import java.util.Iterator;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Set;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.entity.player.EntityPlayerMP;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.common.collect.Maps;
import net.minecraft.advancements.PlayerAdvancements;
import java.util.Map;
import net.minecraft.util.ResourceLocation;
import net.minecraft.advancements.ICriterionTrigger;

public class TickTrigger implements ICriterionTrigger<Instance>
{
    public static final ResourceLocation field_193183_a;
    private final Map<PlayerAdvancements, Listeners> field_193184_b;
    
    static {
        field_193183_a = new ResourceLocation("tick");
    }
    
    public TickTrigger() {
        this.field_193184_b = (Map<PlayerAdvancements, Listeners>)Maps.newHashMap();
    }
    
    @Override
    public ResourceLocation func_192163_a() {
        return TickTrigger.field_193183_a;
    }
    
    @Override
    public void func_192165_a(final PlayerAdvancements p_192165_1_, final Listener<Instance> p_192165_2_) {
        Listeners ticktrigger$listeners = this.field_193184_b.get(p_192165_1_);
        if (ticktrigger$listeners == null) {
            ticktrigger$listeners = new Listeners(p_192165_1_);
            this.field_193184_b.put(p_192165_1_, ticktrigger$listeners);
        }
        ticktrigger$listeners.func_193502_a(p_192165_2_);
    }
    
    @Override
    public void func_192164_b(final PlayerAdvancements p_192164_1_, final Listener<Instance> p_192164_2_) {
        final Listeners ticktrigger$listeners = this.field_193184_b.get(p_192164_1_);
        if (ticktrigger$listeners != null) {
            ticktrigger$listeners.func_193500_b(p_192164_2_);
            if (ticktrigger$listeners.func_193501_a()) {
                this.field_193184_b.remove(p_192164_1_);
            }
        }
    }
    
    @Override
    public void func_192167_a(final PlayerAdvancements p_192167_1_) {
        this.field_193184_b.remove(p_192167_1_);
    }
    
    @Override
    public Instance func_192166_a(final JsonObject p_192166_1_, final JsonDeserializationContext p_192166_2_) {
        return new Instance();
    }
    
    public void func_193182_a(final EntityPlayerMP p_193182_1_) {
        final Listeners ticktrigger$listeners = this.field_193184_b.get(p_193182_1_.func_192039_O());
        if (ticktrigger$listeners != null) {
            ticktrigger$listeners.func_193503_b();
        }
    }
    
    public static class Instance extends AbstractCriterionInstance
    {
        public Instance() {
            super(TickTrigger.field_193183_a);
        }
    }
    
    static class Listeners
    {
        private final PlayerAdvancements field_193504_a;
        private final Set<Listener<Instance>> field_193505_b;
        
        public Listeners(final PlayerAdvancements p_i47496_1_) {
            this.field_193505_b = (Set<Listener<Instance>>)Sets.newHashSet();
            this.field_193504_a = p_i47496_1_;
        }
        
        public boolean func_193501_a() {
            return this.field_193505_b.isEmpty();
        }
        
        public void func_193502_a(final Listener<Instance> p_193502_1_) {
            this.field_193505_b.add(p_193502_1_);
        }
        
        public void func_193500_b(final Listener<Instance> p_193500_1_) {
            this.field_193505_b.remove(p_193500_1_);
        }
        
        public void func_193503_b() {
            for (final Listener<Instance> listener : Lists.newArrayList((Iterable<? extends Listener<Instance>>)this.field_193505_b)) {
                listener.func_192159_a(this.field_193504_a);
            }
        }
    }
}
