// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.advancements.critereon;

import java.util.Iterator;
import java.util.List;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Set;
import net.minecraft.entity.Entity;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayerMP;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.common.collect.Maps;
import net.minecraft.advancements.PlayerAdvancements;
import java.util.Map;
import net.minecraft.util.ResourceLocation;
import net.minecraft.advancements.ICriterionTrigger;

public class VillagerTradeTrigger implements ICriterionTrigger<Instance>
{
    private static final ResourceLocation field_192237_a;
    private final Map<PlayerAdvancements, Listeners> field_192238_b;
    
    static {
        field_192237_a = new ResourceLocation("villager_trade");
    }
    
    public VillagerTradeTrigger() {
        this.field_192238_b = (Map<PlayerAdvancements, Listeners>)Maps.newHashMap();
    }
    
    @Override
    public ResourceLocation func_192163_a() {
        return VillagerTradeTrigger.field_192237_a;
    }
    
    @Override
    public void func_192165_a(final PlayerAdvancements p_192165_1_, final Listener<Instance> p_192165_2_) {
        Listeners villagertradetrigger$listeners = this.field_192238_b.get(p_192165_1_);
        if (villagertradetrigger$listeners == null) {
            villagertradetrigger$listeners = new Listeners(p_192165_1_);
            this.field_192238_b.put(p_192165_1_, villagertradetrigger$listeners);
        }
        villagertradetrigger$listeners.func_192540_a(p_192165_2_);
    }
    
    @Override
    public void func_192164_b(final PlayerAdvancements p_192164_1_, final Listener<Instance> p_192164_2_) {
        final Listeners villagertradetrigger$listeners = this.field_192238_b.get(p_192164_1_);
        if (villagertradetrigger$listeners != null) {
            villagertradetrigger$listeners.func_192538_b(p_192164_2_);
            if (villagertradetrigger$listeners.func_192539_a()) {
                this.field_192238_b.remove(p_192164_1_);
            }
        }
    }
    
    @Override
    public void func_192167_a(final PlayerAdvancements p_192167_1_) {
        this.field_192238_b.remove(p_192167_1_);
    }
    
    @Override
    public Instance func_192166_a(final JsonObject p_192166_1_, final JsonDeserializationContext p_192166_2_) {
        final EntityPredicate entitypredicate = EntityPredicate.func_192481_a(p_192166_1_.get("villager"));
        final ItemPredicate itempredicate = ItemPredicate.func_192492_a(p_192166_1_.get("item"));
        return new Instance(entitypredicate, itempredicate);
    }
    
    public void func_192234_a(final EntityPlayerMP p_192234_1_, final EntityVillager p_192234_2_, final ItemStack p_192234_3_) {
        final Listeners villagertradetrigger$listeners = this.field_192238_b.get(p_192234_1_.func_192039_O());
        if (villagertradetrigger$listeners != null) {
            villagertradetrigger$listeners.func_192537_a(p_192234_1_, p_192234_2_, p_192234_3_);
        }
    }
    
    public static class Instance extends AbstractCriterionInstance
    {
        private final EntityPredicate field_192286_a;
        private final ItemPredicate field_192287_b;
        
        public Instance(final EntityPredicate p_i47457_1_, final ItemPredicate p_i47457_2_) {
            super(VillagerTradeTrigger.field_192237_a);
            this.field_192286_a = p_i47457_1_;
            this.field_192287_b = p_i47457_2_;
        }
        
        public boolean func_192285_a(final EntityPlayerMP p_192285_1_, final EntityVillager p_192285_2_, final ItemStack p_192285_3_) {
            return this.field_192286_a.func_192482_a(p_192285_1_, p_192285_2_) && this.field_192287_b.func_192493_a(p_192285_3_);
        }
    }
    
    static class Listeners
    {
        private final PlayerAdvancements field_192541_a;
        private final Set<Listener<Instance>> field_192542_b;
        
        public Listeners(final PlayerAdvancements p_i47458_1_) {
            this.field_192542_b = (Set<Listener<Instance>>)Sets.newHashSet();
            this.field_192541_a = p_i47458_1_;
        }
        
        public boolean func_192539_a() {
            return this.field_192542_b.isEmpty();
        }
        
        public void func_192540_a(final Listener<Instance> p_192540_1_) {
            this.field_192542_b.add(p_192540_1_);
        }
        
        public void func_192538_b(final Listener<Instance> p_192538_1_) {
            this.field_192542_b.remove(p_192538_1_);
        }
        
        public void func_192537_a(final EntityPlayerMP p_192537_1_, final EntityVillager p_192537_2_, final ItemStack p_192537_3_) {
            List<Listener<Instance>> list = null;
            for (final Listener<Instance> listener : this.field_192542_b) {
                if (listener.func_192158_a().func_192285_a(p_192537_1_, p_192537_2_, p_192537_3_)) {
                    if (list == null) {
                        list = (List<Listener<Instance>>)Lists.newArrayList();
                    }
                    list.add(listener);
                }
            }
            if (list != null) {
                for (final Listener<Instance> listener2 : list) {
                    listener2.func_192159_a(this.field_192541_a);
                }
            }
        }
    }
}
