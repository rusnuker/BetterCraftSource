// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.enchantment;

import net.minecraft.inventory.EntityEquipmentSlot;

public class EnchantmentArrowDamage extends Enchantment
{
    public EnchantmentArrowDamage(final Rarity rarityIn, final EntityEquipmentSlot... slots) {
        super(rarityIn, EnumEnchantmentType.BOW, slots);
        this.setName("arrowDamage");
    }
    
    @Override
    public int getMinEnchantability(final int enchantmentLevel) {
        return 1 + (enchantmentLevel - 1) * 10;
    }
    
    @Override
    public int getMaxEnchantability(final int enchantmentLevel) {
        return this.getMinEnchantability(enchantmentLevel) + 15;
    }
    
    @Override
    public int getMaxLevel() {
        return 5;
    }
}
