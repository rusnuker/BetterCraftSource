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
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.crafting.IRecipe;
import com.google.gson.JsonSyntaxException;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.util.JsonUtils;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.common.collect.Maps;
import net.minecraft.advancements.PlayerAdvancements;
import java.util.Map;
import net.minecraft.util.ResourceLocation;
import net.minecraft.advancements.ICriterionTrigger;

public class RecipeUnlockedTrigger implements ICriterionTrigger<Instance>
{
    private static final ResourceLocation field_192227_a;
    private final Map<PlayerAdvancements, Listeners> field_192228_b;
    
    static {
        field_192227_a = new ResourceLocation("recipe_unlocked");
    }
    
    public RecipeUnlockedTrigger() {
        this.field_192228_b = (Map<PlayerAdvancements, Listeners>)Maps.newHashMap();
    }
    
    @Override
    public ResourceLocation func_192163_a() {
        return RecipeUnlockedTrigger.field_192227_a;
    }
    
    @Override
    public void func_192165_a(final PlayerAdvancements p_192165_1_, final Listener<Instance> p_192165_2_) {
        Listeners recipeunlockedtrigger$listeners = this.field_192228_b.get(p_192165_1_);
        if (recipeunlockedtrigger$listeners == null) {
            recipeunlockedtrigger$listeners = new Listeners(p_192165_1_);
            this.field_192228_b.put(p_192165_1_, recipeunlockedtrigger$listeners);
        }
        recipeunlockedtrigger$listeners.func_192528_a(p_192165_2_);
    }
    
    @Override
    public void func_192164_b(final PlayerAdvancements p_192164_1_, final Listener<Instance> p_192164_2_) {
        final Listeners recipeunlockedtrigger$listeners = this.field_192228_b.get(p_192164_1_);
        if (recipeunlockedtrigger$listeners != null) {
            recipeunlockedtrigger$listeners.func_192525_b(p_192164_2_);
            if (recipeunlockedtrigger$listeners.func_192527_a()) {
                this.field_192228_b.remove(p_192164_1_);
            }
        }
    }
    
    @Override
    public void func_192167_a(final PlayerAdvancements p_192167_1_) {
        this.field_192228_b.remove(p_192167_1_);
    }
    
    @Override
    public Instance func_192166_a(final JsonObject p_192166_1_, final JsonDeserializationContext p_192166_2_) {
        final ResourceLocation resourcelocation = new ResourceLocation(JsonUtils.getString(p_192166_1_, "recipe"));
        final IRecipe irecipe = CraftingManager.func_193373_a(resourcelocation);
        if (irecipe == null) {
            throw new JsonSyntaxException("Unknown recipe '" + resourcelocation + "'");
        }
        return new Instance(irecipe);
    }
    
    public void func_192225_a(final EntityPlayerMP p_192225_1_, final IRecipe p_192225_2_) {
        final Listeners recipeunlockedtrigger$listeners = this.field_192228_b.get(p_192225_1_.func_192039_O());
        if (recipeunlockedtrigger$listeners != null) {
            recipeunlockedtrigger$listeners.func_193493_a(p_192225_2_);
        }
    }
    
    public static class Instance extends AbstractCriterionInstance
    {
        private final IRecipe field_192282_a;
        
        public Instance(final IRecipe p_i47526_1_) {
            super(RecipeUnlockedTrigger.field_192227_a);
            this.field_192282_a = p_i47526_1_;
        }
        
        public boolean func_193215_a(final IRecipe p_193215_1_) {
            return this.field_192282_a == p_193215_1_;
        }
    }
    
    static class Listeners
    {
        private final PlayerAdvancements field_192529_a;
        private final Set<Listener<Instance>> field_192530_b;
        
        public Listeners(final PlayerAdvancements p_i47397_1_) {
            this.field_192530_b = (Set<Listener<Instance>>)Sets.newHashSet();
            this.field_192529_a = p_i47397_1_;
        }
        
        public boolean func_192527_a() {
            return this.field_192530_b.isEmpty();
        }
        
        public void func_192528_a(final Listener<Instance> p_192528_1_) {
            this.field_192530_b.add(p_192528_1_);
        }
        
        public void func_192525_b(final Listener<Instance> p_192525_1_) {
            this.field_192530_b.remove(p_192525_1_);
        }
        
        public void func_193493_a(final IRecipe p_193493_1_) {
            List<Listener<Instance>> list = null;
            for (final Listener<Instance> listener : this.field_192530_b) {
                if (listener.func_192158_a().func_193215_a(p_193493_1_)) {
                    if (list == null) {
                        list = (List<Listener<Instance>>)Lists.newArrayList();
                    }
                    list.add(listener);
                }
            }
            if (list != null) {
                for (final Listener<Instance> listener2 : list) {
                    listener2.func_192159_a(this.field_192529_a);
                }
            }
        }
    }
}
