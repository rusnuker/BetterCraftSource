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
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayerMP;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.common.collect.Maps;
import net.minecraft.advancements.PlayerAdvancements;
import java.util.Map;
import net.minecraft.util.ResourceLocation;
import net.minecraft.advancements.ICriterionTrigger;

public class TameAnimalTrigger implements ICriterionTrigger<Instance>
{
    private static final ResourceLocation field_193179_a;
    private final Map<PlayerAdvancements, Listeners> field_193180_b;
    
    static {
        field_193179_a = new ResourceLocation("tame_animal");
    }
    
    public TameAnimalTrigger() {
        this.field_193180_b = (Map<PlayerAdvancements, Listeners>)Maps.newHashMap();
    }
    
    @Override
    public ResourceLocation func_192163_a() {
        return TameAnimalTrigger.field_193179_a;
    }
    
    @Override
    public void func_192165_a(final PlayerAdvancements p_192165_1_, final Listener<Instance> p_192165_2_) {
        Listeners tameanimaltrigger$listeners = this.field_193180_b.get(p_192165_1_);
        if (tameanimaltrigger$listeners == null) {
            tameanimaltrigger$listeners = new Listeners(p_192165_1_);
            this.field_193180_b.put(p_192165_1_, tameanimaltrigger$listeners);
        }
        tameanimaltrigger$listeners.func_193496_a(p_192165_2_);
    }
    
    @Override
    public void func_192164_b(final PlayerAdvancements p_192164_1_, final Listener<Instance> p_192164_2_) {
        final Listeners tameanimaltrigger$listeners = this.field_193180_b.get(p_192164_1_);
        if (tameanimaltrigger$listeners != null) {
            tameanimaltrigger$listeners.func_193494_b(p_192164_2_);
            if (tameanimaltrigger$listeners.func_193495_a()) {
                this.field_193180_b.remove(p_192164_1_);
            }
        }
    }
    
    @Override
    public void func_192167_a(final PlayerAdvancements p_192167_1_) {
        this.field_193180_b.remove(p_192167_1_);
    }
    
    @Override
    public Instance func_192166_a(final JsonObject p_192166_1_, final JsonDeserializationContext p_192166_2_) {
        final EntityPredicate entitypredicate = EntityPredicate.func_192481_a(p_192166_1_.get("entity"));
        return new Instance(entitypredicate);
    }
    
    public void func_193178_a(final EntityPlayerMP p_193178_1_, final EntityAnimal p_193178_2_) {
        final Listeners tameanimaltrigger$listeners = this.field_193180_b.get(p_193178_1_.func_192039_O());
        if (tameanimaltrigger$listeners != null) {
            tameanimaltrigger$listeners.func_193497_a(p_193178_1_, p_193178_2_);
        }
    }
    
    public static class Instance extends AbstractCriterionInstance
    {
        private final EntityPredicate field_193217_a;
        
        public Instance(final EntityPredicate p_i47513_1_) {
            super(TameAnimalTrigger.field_193179_a);
            this.field_193217_a = p_i47513_1_;
        }
        
        public boolean func_193216_a(final EntityPlayerMP p_193216_1_, final EntityAnimal p_193216_2_) {
            return this.field_193217_a.func_192482_a(p_193216_1_, p_193216_2_);
        }
    }
    
    static class Listeners
    {
        private final PlayerAdvancements field_193498_a;
        private final Set<Listener<Instance>> field_193499_b;
        
        public Listeners(final PlayerAdvancements p_i47514_1_) {
            this.field_193499_b = (Set<Listener<Instance>>)Sets.newHashSet();
            this.field_193498_a = p_i47514_1_;
        }
        
        public boolean func_193495_a() {
            return this.field_193499_b.isEmpty();
        }
        
        public void func_193496_a(final Listener<Instance> p_193496_1_) {
            this.field_193499_b.add(p_193496_1_);
        }
        
        public void func_193494_b(final Listener<Instance> p_193494_1_) {
            this.field_193499_b.remove(p_193494_1_);
        }
        
        public void func_193497_a(final EntityPlayerMP p_193497_1_, final EntityAnimal p_193497_2_) {
            List<Listener<Instance>> list = null;
            for (final Listener<Instance> listener : this.field_193499_b) {
                if (listener.func_192158_a().func_193216_a(p_193497_1_, p_193497_2_)) {
                    if (list == null) {
                        list = (List<Listener<Instance>>)Lists.newArrayList();
                    }
                    list.add(listener);
                }
            }
            if (list != null) {
                for (final Listener<Instance> listener2 : list) {
                    listener2.func_192159_a(this.field_193498_a);
                }
            }
        }
    }
}
