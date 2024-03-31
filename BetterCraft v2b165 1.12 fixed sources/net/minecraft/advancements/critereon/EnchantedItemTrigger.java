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
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayerMP;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.common.collect.Maps;
import net.minecraft.advancements.PlayerAdvancements;
import java.util.Map;
import net.minecraft.util.ResourceLocation;
import net.minecraft.advancements.ICriterionTrigger;

public class EnchantedItemTrigger implements ICriterionTrigger<Instance>
{
    private static final ResourceLocation field_192191_a;
    private final Map<PlayerAdvancements, Listeners> field_192192_b;
    
    static {
        field_192191_a = new ResourceLocation("enchanted_item");
    }
    
    public EnchantedItemTrigger() {
        this.field_192192_b = (Map<PlayerAdvancements, Listeners>)Maps.newHashMap();
    }
    
    @Override
    public ResourceLocation func_192163_a() {
        return EnchantedItemTrigger.field_192191_a;
    }
    
    @Override
    public void func_192165_a(final PlayerAdvancements p_192165_1_, final Listener<Instance> p_192165_2_) {
        Listeners enchanteditemtrigger$listeners = this.field_192192_b.get(p_192165_1_);
        if (enchanteditemtrigger$listeners == null) {
            enchanteditemtrigger$listeners = new Listeners(p_192165_1_);
            this.field_192192_b.put(p_192165_1_, enchanteditemtrigger$listeners);
        }
        enchanteditemtrigger$listeners.func_192460_a(p_192165_2_);
    }
    
    @Override
    public void func_192164_b(final PlayerAdvancements p_192164_1_, final Listener<Instance> p_192164_2_) {
        final Listeners enchanteditemtrigger$listeners = this.field_192192_b.get(p_192164_1_);
        if (enchanteditemtrigger$listeners != null) {
            enchanteditemtrigger$listeners.func_192457_b(p_192164_2_);
            if (enchanteditemtrigger$listeners.func_192458_a()) {
                this.field_192192_b.remove(p_192164_1_);
            }
        }
    }
    
    @Override
    public void func_192167_a(final PlayerAdvancements p_192167_1_) {
        this.field_192192_b.remove(p_192167_1_);
    }
    
    @Override
    public Instance func_192166_a(final JsonObject p_192166_1_, final JsonDeserializationContext p_192166_2_) {
        final ItemPredicate itempredicate = ItemPredicate.func_192492_a(p_192166_1_.get("item"));
        final MinMaxBounds minmaxbounds = MinMaxBounds.func_192515_a(p_192166_1_.get("levels"));
        return new Instance(itempredicate, minmaxbounds);
    }
    
    public void func_192190_a(final EntityPlayerMP p_192190_1_, final ItemStack p_192190_2_, final int p_192190_3_) {
        final Listeners enchanteditemtrigger$listeners = this.field_192192_b.get(p_192190_1_.func_192039_O());
        if (enchanteditemtrigger$listeners != null) {
            enchanteditemtrigger$listeners.func_192459_a(p_192190_2_, p_192190_3_);
        }
    }
    
    public static class Instance extends AbstractCriterionInstance
    {
        private final ItemPredicate field_192258_a;
        private final MinMaxBounds field_192259_b;
        
        public Instance(final ItemPredicate p_i47376_1_, final MinMaxBounds p_i47376_2_) {
            super(EnchantedItemTrigger.field_192191_a);
            this.field_192258_a = p_i47376_1_;
            this.field_192259_b = p_i47376_2_;
        }
        
        public boolean func_192257_a(final ItemStack p_192257_1_, final int p_192257_2_) {
            return this.field_192258_a.func_192493_a(p_192257_1_) && this.field_192259_b.func_192514_a((float)p_192257_2_);
        }
    }
    
    static class Listeners
    {
        private final PlayerAdvancements field_192461_a;
        private final Set<Listener<Instance>> field_192462_b;
        
        public Listeners(final PlayerAdvancements p_i47377_1_) {
            this.field_192462_b = (Set<Listener<Instance>>)Sets.newHashSet();
            this.field_192461_a = p_i47377_1_;
        }
        
        public boolean func_192458_a() {
            return this.field_192462_b.isEmpty();
        }
        
        public void func_192460_a(final Listener<Instance> p_192460_1_) {
            this.field_192462_b.add(p_192460_1_);
        }
        
        public void func_192457_b(final Listener<Instance> p_192457_1_) {
            this.field_192462_b.remove(p_192457_1_);
        }
        
        public void func_192459_a(final ItemStack p_192459_1_, final int p_192459_2_) {
            List<Listener<Instance>> list = null;
            for (final Listener<Instance> listener : this.field_192462_b) {
                if (listener.func_192158_a().func_192257_a(p_192459_1_, p_192459_2_)) {
                    if (list == null) {
                        list = (List<Listener<Instance>>)Lists.newArrayList();
                    }
                    list.add(listener);
                }
            }
            if (list != null) {
                for (final Listener<Instance> listener2 : list) {
                    listener2.func_192159_a(this.field_192461_a);
                }
            }
        }
    }
}
