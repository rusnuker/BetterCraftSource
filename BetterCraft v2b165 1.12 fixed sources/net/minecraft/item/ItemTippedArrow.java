// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.item;

import net.minecraft.util.text.translation.I18n;
import net.minecraft.client.util.ITooltipFlag;
import java.util.List;
import javax.annotation.Nullable;
import java.util.Iterator;
import net.minecraft.potion.PotionType;
import net.minecraft.util.NonNullList;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import net.minecraft.potion.PotionUtils;
import net.minecraft.init.PotionTypes;

public class ItemTippedArrow extends ItemArrow
{
    @Override
    public ItemStack func_190903_i() {
        return PotionUtils.addPotionToItemStack(super.func_190903_i(), PotionTypes.POISON);
    }
    
    @Override
    public EntityArrow createArrow(final World worldIn, final ItemStack stack, final EntityLivingBase shooter) {
        final EntityTippedArrow entitytippedarrow = new EntityTippedArrow(worldIn, shooter);
        entitytippedarrow.setPotionEffect(stack);
        return entitytippedarrow;
    }
    
    @Override
    public void getSubItems(final CreativeTabs itemIn, final NonNullList<ItemStack> tab) {
        if (this.func_194125_a(itemIn)) {
            for (final PotionType potiontype : PotionType.REGISTRY) {
                if (!potiontype.getEffects().isEmpty()) {
                    tab.add(PotionUtils.addPotionToItemStack(new ItemStack(this), potiontype));
                }
            }
        }
    }
    
    @Override
    public void addInformation(final ItemStack stack, @Nullable final World playerIn, final List<String> tooltip, final ITooltipFlag advanced) {
        PotionUtils.addPotionTooltip(stack, tooltip, 0.125f);
    }
    
    @Override
    public String getItemStackDisplayName(final ItemStack stack) {
        return I18n.translateToLocal(PotionUtils.getPotionFromItem(stack).getNamePrefixed("tipped_arrow.effect."));
    }
}
