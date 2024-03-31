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

public class ItemDurabilityTrigger implements ICriterionTrigger<Instance>
{
    private static final ResourceLocation field_193159_a;
    private final Map<PlayerAdvancements, Listeners> field_193160_b;
    
    static {
        field_193159_a = new ResourceLocation("item_durability_changed");
    }
    
    public ItemDurabilityTrigger() {
        this.field_193160_b = (Map<PlayerAdvancements, Listeners>)Maps.newHashMap();
    }
    
    @Override
    public ResourceLocation func_192163_a() {
        return ItemDurabilityTrigger.field_193159_a;
    }
    
    @Override
    public void func_192165_a(final PlayerAdvancements p_192165_1_, final Listener<Instance> p_192165_2_) {
        Listeners itemdurabilitytrigger$listeners = this.field_193160_b.get(p_192165_1_);
        if (itemdurabilitytrigger$listeners == null) {
            itemdurabilitytrigger$listeners = new Listeners(p_192165_1_);
            this.field_193160_b.put(p_192165_1_, itemdurabilitytrigger$listeners);
        }
        itemdurabilitytrigger$listeners.func_193440_a(p_192165_2_);
    }
    
    @Override
    public void func_192164_b(final PlayerAdvancements p_192164_1_, final Listener<Instance> p_192164_2_) {
        final Listeners itemdurabilitytrigger$listeners = this.field_193160_b.get(p_192164_1_);
        if (itemdurabilitytrigger$listeners != null) {
            itemdurabilitytrigger$listeners.func_193438_b(p_192164_2_);
            if (itemdurabilitytrigger$listeners.func_193439_a()) {
                this.field_193160_b.remove(p_192164_1_);
            }
        }
    }
    
    @Override
    public void func_192167_a(final PlayerAdvancements p_192167_1_) {
        this.field_193160_b.remove(p_192167_1_);
    }
    
    @Override
    public Instance func_192166_a(final JsonObject p_192166_1_, final JsonDeserializationContext p_192166_2_) {
        final ItemPredicate itempredicate = ItemPredicate.func_192492_a(p_192166_1_.get("item"));
        final MinMaxBounds minmaxbounds = MinMaxBounds.func_192515_a(p_192166_1_.get("durability"));
        final MinMaxBounds minmaxbounds2 = MinMaxBounds.func_192515_a(p_192166_1_.get("delta"));
        return new Instance(itempredicate, minmaxbounds, minmaxbounds2);
    }
    
    public void func_193158_a(final EntityPlayerMP p_193158_1_, final ItemStack p_193158_2_, final int p_193158_3_) {
        final Listeners itemdurabilitytrigger$listeners = this.field_193160_b.get(p_193158_1_.func_192039_O());
        if (itemdurabilitytrigger$listeners != null) {
            itemdurabilitytrigger$listeners.func_193441_a(p_193158_2_, p_193158_3_);
        }
    }
    
    public static class Instance extends AbstractCriterionInstance
    {
        private final ItemPredicate field_193198_a;
        private final MinMaxBounds field_193199_b;
        private final MinMaxBounds field_193200_c;
        
        public Instance(final ItemPredicate p_i47511_1_, final MinMaxBounds p_i47511_2_, final MinMaxBounds p_i47511_3_) {
            super(ItemDurabilityTrigger.field_193159_a);
            this.field_193198_a = p_i47511_1_;
            this.field_193199_b = p_i47511_2_;
            this.field_193200_c = p_i47511_3_;
        }
        
        public boolean func_193197_a(final ItemStack p_193197_1_, final int p_193197_2_) {
            return this.field_193198_a.func_192493_a(p_193197_1_) && this.field_193199_b.func_192514_a((float)(p_193197_1_.getMaxDamage() - p_193197_2_)) && this.field_193200_c.func_192514_a((float)(p_193197_1_.getItemDamage() - p_193197_2_));
        }
    }
    
    static class Listeners
    {
        private final PlayerAdvancements field_193442_a;
        private final Set<Listener<Instance>> field_193443_b;
        
        public Listeners(final PlayerAdvancements p_i47512_1_) {
            this.field_193443_b = (Set<Listener<Instance>>)Sets.newHashSet();
            this.field_193442_a = p_i47512_1_;
        }
        
        public boolean func_193439_a() {
            return this.field_193443_b.isEmpty();
        }
        
        public void func_193440_a(final Listener<Instance> p_193440_1_) {
            this.field_193443_b.add(p_193440_1_);
        }
        
        public void func_193438_b(final Listener<Instance> p_193438_1_) {
            this.field_193443_b.remove(p_193438_1_);
        }
        
        public void func_193441_a(final ItemStack p_193441_1_, final int p_193441_2_) {
            List<Listener<Instance>> list = null;
            for (final Listener<Instance> listener : this.field_193443_b) {
                if (listener.func_192158_a().func_193197_a(p_193441_1_, p_193441_2_)) {
                    if (list == null) {
                        list = (List<Listener<Instance>>)Lists.newArrayList();
                    }
                    list.add(listener);
                }
            }
            if (list != null) {
                for (final Listener<Instance> listener2 : list) {
                    listener2.func_192159_a(this.field_193442_a);
                }
            }
        }
    }
}
