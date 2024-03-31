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

public class ConsumeItemTrigger implements ICriterionTrigger<Instance>
{
    private static final ResourceLocation field_193149_a;
    private final Map<PlayerAdvancements, Listeners> field_193150_b;
    
    static {
        field_193149_a = new ResourceLocation("consume_item");
    }
    
    public ConsumeItemTrigger() {
        this.field_193150_b = (Map<PlayerAdvancements, Listeners>)Maps.newHashMap();
    }
    
    @Override
    public ResourceLocation func_192163_a() {
        return ConsumeItemTrigger.field_193149_a;
    }
    
    @Override
    public void func_192165_a(final PlayerAdvancements p_192165_1_, final Listener<Instance> p_192165_2_) {
        Listeners consumeitemtrigger$listeners = this.field_193150_b.get(p_192165_1_);
        if (consumeitemtrigger$listeners == null) {
            consumeitemtrigger$listeners = new Listeners(p_192165_1_);
            this.field_193150_b.put(p_192165_1_, consumeitemtrigger$listeners);
        }
        consumeitemtrigger$listeners.func_193239_a(p_192165_2_);
    }
    
    @Override
    public void func_192164_b(final PlayerAdvancements p_192164_1_, final Listener<Instance> p_192164_2_) {
        final Listeners consumeitemtrigger$listeners = this.field_193150_b.get(p_192164_1_);
        if (consumeitemtrigger$listeners != null) {
            consumeitemtrigger$listeners.func_193237_b(p_192164_2_);
            if (consumeitemtrigger$listeners.func_193238_a()) {
                this.field_193150_b.remove(p_192164_1_);
            }
        }
    }
    
    @Override
    public void func_192167_a(final PlayerAdvancements p_192167_1_) {
        this.field_193150_b.remove(p_192167_1_);
    }
    
    @Override
    public Instance func_192166_a(final JsonObject p_192166_1_, final JsonDeserializationContext p_192166_2_) {
        final ItemPredicate itempredicate = ItemPredicate.func_192492_a(p_192166_1_.get("item"));
        return new Instance(itempredicate);
    }
    
    public void func_193148_a(final EntityPlayerMP p_193148_1_, final ItemStack p_193148_2_) {
        final Listeners consumeitemtrigger$listeners = this.field_193150_b.get(p_193148_1_.func_192039_O());
        if (consumeitemtrigger$listeners != null) {
            consumeitemtrigger$listeners.func_193240_a(p_193148_2_);
        }
    }
    
    public static class Instance extends AbstractCriterionInstance
    {
        private final ItemPredicate field_193194_a;
        
        public Instance(final ItemPredicate p_i47562_1_) {
            super(ConsumeItemTrigger.field_193149_a);
            this.field_193194_a = p_i47562_1_;
        }
        
        public boolean func_193193_a(final ItemStack p_193193_1_) {
            return this.field_193194_a.func_192493_a(p_193193_1_);
        }
    }
    
    static class Listeners
    {
        private final PlayerAdvancements field_193241_a;
        private final Set<Listener<Instance>> field_193242_b;
        
        public Listeners(final PlayerAdvancements p_i47563_1_) {
            this.field_193242_b = (Set<Listener<Instance>>)Sets.newHashSet();
            this.field_193241_a = p_i47563_1_;
        }
        
        public boolean func_193238_a() {
            return this.field_193242_b.isEmpty();
        }
        
        public void func_193239_a(final Listener<Instance> p_193239_1_) {
            this.field_193242_b.add(p_193239_1_);
        }
        
        public void func_193237_b(final Listener<Instance> p_193237_1_) {
            this.field_193242_b.remove(p_193237_1_);
        }
        
        public void func_193240_a(final ItemStack p_193240_1_) {
            List<Listener<Instance>> list = null;
            for (final Listener<Instance> listener : this.field_193242_b) {
                if (listener.func_192158_a().func_193193_a(p_193240_1_)) {
                    if (list == null) {
                        list = (List<Listener<Instance>>)Lists.newArrayList();
                    }
                    list.add(listener);
                }
            }
            if (list != null) {
                for (final Listener<Instance> listener2 : list) {
                    listener2.func_192159_a(this.field_193241_a);
                }
            }
        }
    }
}
