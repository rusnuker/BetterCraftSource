// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.renderer.block.model;

import java.util.Iterator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import javax.annotation.Nullable;
import net.minecraft.world.World;
import net.minecraft.item.ItemStack;
import com.google.common.collect.Lists;
import java.util.List;

public class ItemOverrideList
{
    public static final ItemOverrideList NONE;
    private final List<ItemOverride> overrides;
    
    static {
        NONE = new ItemOverrideList();
    }
    
    private ItemOverrideList() {
        this.overrides = (List<ItemOverride>)Lists.newArrayList();
    }
    
    public ItemOverrideList(final List<ItemOverride> overridesIn) {
        this.overrides = (List<ItemOverride>)Lists.newArrayList();
        for (int i = overridesIn.size() - 1; i >= 0; --i) {
            this.overrides.add(overridesIn.get(i));
        }
    }
    
    @Nullable
    public ResourceLocation applyOverride(final ItemStack stack, @Nullable final World worldIn, @Nullable final EntityLivingBase entityIn) {
        if (!this.overrides.isEmpty()) {
            for (final ItemOverride itemoverride : this.overrides) {
                if (itemoverride.matchesItemStack(stack, worldIn, entityIn)) {
                    return itemoverride.getLocation();
                }
            }
        }
        return null;
    }
}
