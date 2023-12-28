// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.advancements.critereon;

import com.google.common.collect.Sets;
import java.util.Set;
import java.util.Iterator;
import net.minecraft.item.ItemStack;
import java.util.List;
import com.google.common.collect.Lists;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.JsonUtils;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.common.collect.Maps;
import net.minecraft.advancements.PlayerAdvancements;
import java.util.Map;
import net.minecraft.util.ResourceLocation;
import net.minecraft.advancements.ICriterionTrigger;

public class InventoryChangeTrigger implements ICriterionTrigger<Instance>
{
    private static final ResourceLocation field_192209_a;
    private final Map<PlayerAdvancements, Listeners> field_192210_b;
    
    static {
        field_192209_a = new ResourceLocation("inventory_changed");
    }
    
    public InventoryChangeTrigger() {
        this.field_192210_b = (Map<PlayerAdvancements, Listeners>)Maps.newHashMap();
    }
    
    @Override
    public ResourceLocation func_192163_a() {
        return InventoryChangeTrigger.field_192209_a;
    }
    
    @Override
    public void func_192165_a(final PlayerAdvancements p_192165_1_, final Listener<Instance> p_192165_2_) {
        Listeners inventorychangetrigger$listeners = this.field_192210_b.get(p_192165_1_);
        if (inventorychangetrigger$listeners == null) {
            inventorychangetrigger$listeners = new Listeners(p_192165_1_);
            this.field_192210_b.put(p_192165_1_, inventorychangetrigger$listeners);
        }
        inventorychangetrigger$listeners.func_192489_a(p_192165_2_);
    }
    
    @Override
    public void func_192164_b(final PlayerAdvancements p_192164_1_, final Listener<Instance> p_192164_2_) {
        final Listeners inventorychangetrigger$listeners = this.field_192210_b.get(p_192164_1_);
        if (inventorychangetrigger$listeners != null) {
            inventorychangetrigger$listeners.func_192487_b(p_192164_2_);
            if (inventorychangetrigger$listeners.func_192488_a()) {
                this.field_192210_b.remove(p_192164_1_);
            }
        }
    }
    
    @Override
    public void func_192167_a(final PlayerAdvancements p_192167_1_) {
        this.field_192210_b.remove(p_192167_1_);
    }
    
    @Override
    public Instance func_192166_a(final JsonObject p_192166_1_, final JsonDeserializationContext p_192166_2_) {
        final JsonObject jsonobject = JsonUtils.getJsonObject(p_192166_1_, "slots", new JsonObject());
        final MinMaxBounds minmaxbounds = MinMaxBounds.func_192515_a(jsonobject.get("occupied"));
        final MinMaxBounds minmaxbounds2 = MinMaxBounds.func_192515_a(jsonobject.get("full"));
        final MinMaxBounds minmaxbounds3 = MinMaxBounds.func_192515_a(jsonobject.get("empty"));
        final ItemPredicate[] aitempredicate = ItemPredicate.func_192494_b(p_192166_1_.get("items"));
        return new Instance(minmaxbounds, minmaxbounds2, minmaxbounds3, aitempredicate);
    }
    
    public void func_192208_a(final EntityPlayerMP p_192208_1_, final InventoryPlayer p_192208_2_) {
        final Listeners inventorychangetrigger$listeners = this.field_192210_b.get(p_192208_1_.func_192039_O());
        if (inventorychangetrigger$listeners != null) {
            inventorychangetrigger$listeners.func_192486_a(p_192208_2_);
        }
    }
    
    public static class Instance extends AbstractCriterionInstance
    {
        private final MinMaxBounds field_192266_a;
        private final MinMaxBounds field_192267_b;
        private final MinMaxBounds field_192268_c;
        private final ItemPredicate[] field_192269_d;
        
        public Instance(final MinMaxBounds p_i47390_1_, final MinMaxBounds p_i47390_2_, final MinMaxBounds p_i47390_3_, final ItemPredicate[] p_i47390_4_) {
            super(InventoryChangeTrigger.field_192209_a);
            this.field_192266_a = p_i47390_1_;
            this.field_192267_b = p_i47390_2_;
            this.field_192268_c = p_i47390_3_;
            this.field_192269_d = p_i47390_4_;
        }
        
        public boolean func_192265_a(final InventoryPlayer p_192265_1_) {
            int i = 0;
            int j = 0;
            int k = 0;
            final List<ItemPredicate> list = Lists.newArrayList(this.field_192269_d);
            for (int l = 0; l < p_192265_1_.getSizeInventory(); ++l) {
                final ItemStack itemstack = p_192265_1_.getStackInSlot(l);
                if (itemstack.func_190926_b()) {
                    ++j;
                }
                else {
                    ++k;
                    if (itemstack.func_190916_E() >= itemstack.getMaxStackSize()) {
                        ++i;
                    }
                    final Iterator<ItemPredicate> iterator = list.iterator();
                    while (iterator.hasNext()) {
                        final ItemPredicate itempredicate = iterator.next();
                        if (itempredicate.func_192493_a(itemstack)) {
                            iterator.remove();
                        }
                    }
                }
            }
            return this.field_192267_b.func_192514_a((float)i) && this.field_192268_c.func_192514_a((float)j) && this.field_192266_a.func_192514_a((float)k) && list.isEmpty();
        }
    }
    
    static class Listeners
    {
        private final PlayerAdvancements field_192490_a;
        private final Set<Listener<Instance>> field_192491_b;
        
        public Listeners(final PlayerAdvancements p_i47391_1_) {
            this.field_192491_b = (Set<Listener<Instance>>)Sets.newHashSet();
            this.field_192490_a = p_i47391_1_;
        }
        
        public boolean func_192488_a() {
            return this.field_192491_b.isEmpty();
        }
        
        public void func_192489_a(final Listener<Instance> p_192489_1_) {
            this.field_192491_b.add(p_192489_1_);
        }
        
        public void func_192487_b(final Listener<Instance> p_192487_1_) {
            this.field_192491_b.remove(p_192487_1_);
        }
        
        public void func_192486_a(final InventoryPlayer p_192486_1_) {
            List<Listener<Instance>> list = null;
            for (final Listener<Instance> listener : this.field_192491_b) {
                if (listener.func_192158_a().func_192265_a(p_192486_1_)) {
                    if (list == null) {
                        list = (List<Listener<Instance>>)Lists.newArrayList();
                    }
                    list.add(listener);
                }
            }
            if (list != null) {
                for (final Listener<Instance> listener2 : list) {
                    listener2.func_192159_a(this.field_192490_a);
                }
            }
        }
    }
}
