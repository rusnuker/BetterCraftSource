// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.advancements.critereon;

import java.util.Iterator;
import java.util.List;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Set;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.common.collect.Maps;
import net.minecraft.advancements.PlayerAdvancements;
import java.util.Map;
import net.minecraft.util.ResourceLocation;
import net.minecraft.advancements.ICriterionTrigger;

public class SummonedEntityTrigger implements ICriterionTrigger<Instance>
{
    private static final ResourceLocation field_192232_a;
    private final Map<PlayerAdvancements, Listeners> field_192233_b;
    
    static {
        field_192232_a = new ResourceLocation("summoned_entity");
    }
    
    public SummonedEntityTrigger() {
        this.field_192233_b = (Map<PlayerAdvancements, Listeners>)Maps.newHashMap();
    }
    
    @Override
    public ResourceLocation func_192163_a() {
        return SummonedEntityTrigger.field_192232_a;
    }
    
    @Override
    public void func_192165_a(final PlayerAdvancements p_192165_1_, final Listener<Instance> p_192165_2_) {
        Listeners summonedentitytrigger$listeners = this.field_192233_b.get(p_192165_1_);
        if (summonedentitytrigger$listeners == null) {
            summonedentitytrigger$listeners = new Listeners(p_192165_1_);
            this.field_192233_b.put(p_192165_1_, summonedentitytrigger$listeners);
        }
        summonedentitytrigger$listeners.func_192534_a(p_192165_2_);
    }
    
    @Override
    public void func_192164_b(final PlayerAdvancements p_192164_1_, final Listener<Instance> p_192164_2_) {
        final Listeners summonedentitytrigger$listeners = this.field_192233_b.get(p_192164_1_);
        if (summonedentitytrigger$listeners != null) {
            summonedentitytrigger$listeners.func_192531_b(p_192164_2_);
            if (summonedentitytrigger$listeners.func_192532_a()) {
                this.field_192233_b.remove(p_192164_1_);
            }
        }
    }
    
    @Override
    public void func_192167_a(final PlayerAdvancements p_192167_1_) {
        this.field_192233_b.remove(p_192167_1_);
    }
    
    @Override
    public Instance func_192166_a(final JsonObject p_192166_1_, final JsonDeserializationContext p_192166_2_) {
        final EntityPredicate entitypredicate = EntityPredicate.func_192481_a(p_192166_1_.get("entity"));
        return new Instance(entitypredicate);
    }
    
    public void func_192229_a(final EntityPlayerMP p_192229_1_, final Entity p_192229_2_) {
        final Listeners summonedentitytrigger$listeners = this.field_192233_b.get(p_192229_1_.func_192039_O());
        if (summonedentitytrigger$listeners != null) {
            summonedentitytrigger$listeners.func_192533_a(p_192229_1_, p_192229_2_);
        }
    }
    
    public static class Instance extends AbstractCriterionInstance
    {
        private final EntityPredicate field_192284_a;
        
        public Instance(final EntityPredicate p_i47371_1_) {
            super(SummonedEntityTrigger.field_192232_a);
            this.field_192284_a = p_i47371_1_;
        }
        
        public boolean func_192283_a(final EntityPlayerMP p_192283_1_, final Entity p_192283_2_) {
            return this.field_192284_a.func_192482_a(p_192283_1_, p_192283_2_);
        }
    }
    
    static class Listeners
    {
        private final PlayerAdvancements field_192535_a;
        private final Set<Listener<Instance>> field_192536_b;
        
        public Listeners(final PlayerAdvancements p_i47372_1_) {
            this.field_192536_b = (Set<Listener<Instance>>)Sets.newHashSet();
            this.field_192535_a = p_i47372_1_;
        }
        
        public boolean func_192532_a() {
            return this.field_192536_b.isEmpty();
        }
        
        public void func_192534_a(final Listener<Instance> p_192534_1_) {
            this.field_192536_b.add(p_192534_1_);
        }
        
        public void func_192531_b(final Listener<Instance> p_192531_1_) {
            this.field_192536_b.remove(p_192531_1_);
        }
        
        public void func_192533_a(final EntityPlayerMP p_192533_1_, final Entity p_192533_2_) {
            List<Listener<Instance>> list = null;
            for (final Listener<Instance> listener : this.field_192536_b) {
                if (listener.func_192158_a().func_192283_a(p_192533_1_, p_192533_2_)) {
                    if (list == null) {
                        list = (List<Listener<Instance>>)Lists.newArrayList();
                    }
                    list.add(listener);
                }
            }
            if (list != null) {
                for (final Listener<Instance> listener2 : list) {
                    listener2.func_192159_a(this.field_192535_a);
                }
            }
        }
    }
}
