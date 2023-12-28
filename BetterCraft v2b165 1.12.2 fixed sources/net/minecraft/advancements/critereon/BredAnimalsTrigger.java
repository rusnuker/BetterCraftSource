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
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayerMP;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.common.collect.Maps;
import net.minecraft.advancements.PlayerAdvancements;
import java.util.Map;
import net.minecraft.util.ResourceLocation;
import net.minecraft.advancements.ICriterionTrigger;

public class BredAnimalsTrigger implements ICriterionTrigger<Instance>
{
    private static final ResourceLocation field_192171_a;
    private final Map<PlayerAdvancements, Listeners> field_192172_b;
    
    static {
        field_192171_a = new ResourceLocation("bred_animals");
    }
    
    public BredAnimalsTrigger() {
        this.field_192172_b = (Map<PlayerAdvancements, Listeners>)Maps.newHashMap();
    }
    
    @Override
    public ResourceLocation func_192163_a() {
        return BredAnimalsTrigger.field_192171_a;
    }
    
    @Override
    public void func_192165_a(final PlayerAdvancements p_192165_1_, final Listener<Instance> p_192165_2_) {
        Listeners bredanimalstrigger$listeners = this.field_192172_b.get(p_192165_1_);
        if (bredanimalstrigger$listeners == null) {
            bredanimalstrigger$listeners = new Listeners(p_192165_1_);
            this.field_192172_b.put(p_192165_1_, bredanimalstrigger$listeners);
        }
        bredanimalstrigger$listeners.func_192343_a(p_192165_2_);
    }
    
    @Override
    public void func_192164_b(final PlayerAdvancements p_192164_1_, final Listener<Instance> p_192164_2_) {
        final Listeners bredanimalstrigger$listeners = this.field_192172_b.get(p_192164_1_);
        if (bredanimalstrigger$listeners != null) {
            bredanimalstrigger$listeners.func_192340_b(p_192164_2_);
            if (bredanimalstrigger$listeners.func_192341_a()) {
                this.field_192172_b.remove(p_192164_1_);
            }
        }
    }
    
    @Override
    public void func_192167_a(final PlayerAdvancements p_192167_1_) {
        this.field_192172_b.remove(p_192167_1_);
    }
    
    @Override
    public Instance func_192166_a(final JsonObject p_192166_1_, final JsonDeserializationContext p_192166_2_) {
        final EntityPredicate entitypredicate = EntityPredicate.func_192481_a(p_192166_1_.get("parent"));
        final EntityPredicate entitypredicate2 = EntityPredicate.func_192481_a(p_192166_1_.get("partner"));
        final EntityPredicate entitypredicate3 = EntityPredicate.func_192481_a(p_192166_1_.get("child"));
        return new Instance(entitypredicate, entitypredicate2, entitypredicate3);
    }
    
    public void func_192168_a(final EntityPlayerMP p_192168_1_, final EntityAnimal p_192168_2_, final EntityAnimal p_192168_3_, final EntityAgeable p_192168_4_) {
        final Listeners bredanimalstrigger$listeners = this.field_192172_b.get(p_192168_1_.func_192039_O());
        if (bredanimalstrigger$listeners != null) {
            bredanimalstrigger$listeners.func_192342_a(p_192168_1_, p_192168_2_, p_192168_3_, p_192168_4_);
        }
    }
    
    public static class Instance extends AbstractCriterionInstance
    {
        private final EntityPredicate field_192247_a;
        private final EntityPredicate field_192248_b;
        private final EntityPredicate field_192249_c;
        
        public Instance(final EntityPredicate p_i47408_1_, final EntityPredicate p_i47408_2_, final EntityPredicate p_i47408_3_) {
            super(BredAnimalsTrigger.field_192171_a);
            this.field_192247_a = p_i47408_1_;
            this.field_192248_b = p_i47408_2_;
            this.field_192249_c = p_i47408_3_;
        }
        
        public boolean func_192246_a(final EntityPlayerMP p_192246_1_, final EntityAnimal p_192246_2_, final EntityAnimal p_192246_3_, final EntityAgeable p_192246_4_) {
            return this.field_192249_c.func_192482_a(p_192246_1_, p_192246_4_) && ((this.field_192247_a.func_192482_a(p_192246_1_, p_192246_2_) && this.field_192248_b.func_192482_a(p_192246_1_, p_192246_3_)) || (this.field_192247_a.func_192482_a(p_192246_1_, p_192246_3_) && this.field_192248_b.func_192482_a(p_192246_1_, p_192246_2_)));
        }
    }
    
    static class Listeners
    {
        private final PlayerAdvancements field_192344_a;
        private final Set<Listener<Instance>> field_192345_b;
        
        public Listeners(final PlayerAdvancements p_i47409_1_) {
            this.field_192345_b = (Set<Listener<Instance>>)Sets.newHashSet();
            this.field_192344_a = p_i47409_1_;
        }
        
        public boolean func_192341_a() {
            return this.field_192345_b.isEmpty();
        }
        
        public void func_192343_a(final Listener<Instance> p_192343_1_) {
            this.field_192345_b.add(p_192343_1_);
        }
        
        public void func_192340_b(final Listener<Instance> p_192340_1_) {
            this.field_192345_b.remove(p_192340_1_);
        }
        
        public void func_192342_a(final EntityPlayerMP p_192342_1_, final EntityAnimal p_192342_2_, final EntityAnimal p_192342_3_, final EntityAgeable p_192342_4_) {
            List<Listener<Instance>> list = null;
            for (final Listener<Instance> listener : this.field_192345_b) {
                if (listener.func_192158_a().func_192246_a(p_192342_1_, p_192342_2_, p_192342_3_, p_192342_4_)) {
                    if (list == null) {
                        list = (List<Listener<Instance>>)Lists.newArrayList();
                    }
                    list.add(listener);
                }
            }
            if (list != null) {
                for (final Listener<Instance> listener2 : list) {
                    listener2.func_192159_a(this.field_192344_a);
                }
            }
        }
    }
}
