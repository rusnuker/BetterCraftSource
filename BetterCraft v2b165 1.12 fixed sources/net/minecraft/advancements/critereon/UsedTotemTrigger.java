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

public class UsedTotemTrigger implements ICriterionTrigger<Instance>
{
    private static final ResourceLocation field_193188_a;
    private final Map<PlayerAdvancements, Listeners> field_193189_b;
    
    static {
        field_193188_a = new ResourceLocation("used_totem");
    }
    
    public UsedTotemTrigger() {
        this.field_193189_b = (Map<PlayerAdvancements, Listeners>)Maps.newHashMap();
    }
    
    @Override
    public ResourceLocation func_192163_a() {
        return UsedTotemTrigger.field_193188_a;
    }
    
    @Override
    public void func_192165_a(final PlayerAdvancements p_192165_1_, final Listener<Instance> p_192165_2_) {
        Listeners usedtotemtrigger$listeners = this.field_193189_b.get(p_192165_1_);
        if (usedtotemtrigger$listeners == null) {
            usedtotemtrigger$listeners = new Listeners(p_192165_1_);
            this.field_193189_b.put(p_192165_1_, usedtotemtrigger$listeners);
        }
        usedtotemtrigger$listeners.func_193508_a(p_192165_2_);
    }
    
    @Override
    public void func_192164_b(final PlayerAdvancements p_192164_1_, final Listener<Instance> p_192164_2_) {
        final Listeners usedtotemtrigger$listeners = this.field_193189_b.get(p_192164_1_);
        if (usedtotemtrigger$listeners != null) {
            usedtotemtrigger$listeners.func_193506_b(p_192164_2_);
            if (usedtotemtrigger$listeners.func_193507_a()) {
                this.field_193189_b.remove(p_192164_1_);
            }
        }
    }
    
    @Override
    public void func_192167_a(final PlayerAdvancements p_192167_1_) {
        this.field_193189_b.remove(p_192167_1_);
    }
    
    @Override
    public Instance func_192166_a(final JsonObject p_192166_1_, final JsonDeserializationContext p_192166_2_) {
        final ItemPredicate itempredicate = ItemPredicate.func_192492_a(p_192166_1_.get("item"));
        return new Instance(itempredicate);
    }
    
    public void func_193187_a(final EntityPlayerMP p_193187_1_, final ItemStack p_193187_2_) {
        final Listeners usedtotemtrigger$listeners = this.field_193189_b.get(p_193187_1_.func_192039_O());
        if (usedtotemtrigger$listeners != null) {
            usedtotemtrigger$listeners.func_193509_a(p_193187_2_);
        }
    }
    
    public static class Instance extends AbstractCriterionInstance
    {
        private final ItemPredicate field_193219_a;
        
        public Instance(final ItemPredicate p_i47564_1_) {
            super(UsedTotemTrigger.field_193188_a);
            this.field_193219_a = p_i47564_1_;
        }
        
        public boolean func_193218_a(final ItemStack p_193218_1_) {
            return this.field_193219_a.func_192493_a(p_193218_1_);
        }
    }
    
    static class Listeners
    {
        private final PlayerAdvancements field_193510_a;
        private final Set<Listener<Instance>> field_193511_b;
        
        public Listeners(final PlayerAdvancements p_i47565_1_) {
            this.field_193511_b = (Set<Listener<Instance>>)Sets.newHashSet();
            this.field_193510_a = p_i47565_1_;
        }
        
        public boolean func_193507_a() {
            return this.field_193511_b.isEmpty();
        }
        
        public void func_193508_a(final Listener<Instance> p_193508_1_) {
            this.field_193511_b.add(p_193508_1_);
        }
        
        public void func_193506_b(final Listener<Instance> p_193506_1_) {
            this.field_193511_b.remove(p_193506_1_);
        }
        
        public void func_193509_a(final ItemStack p_193509_1_) {
            List<Listener<Instance>> list = null;
            for (final Listener<Instance> listener : this.field_193511_b) {
                if (listener.func_192158_a().func_193218_a(p_193509_1_)) {
                    if (list == null) {
                        list = (List<Listener<Instance>>)Lists.newArrayList();
                    }
                    list.add(listener);
                }
            }
            if (list != null) {
                for (final Listener<Instance> listener2 : list) {
                    listener2.func_192159_a(this.field_193510_a);
                }
            }
        }
    }
}
