// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.entity.passive;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import javax.annotation.Nullable;

public enum HorseArmorType
{
    NONE("NONE", 0, 0), 
    IRON("IRON", 1, 5, "iron", "meo"), 
    GOLD("GOLD", 2, 7, "gold", "goo"), 
    DIAMOND("DIAMOND", 3, 11, "diamond", "dio");
    
    private final String textureName;
    private final String hash;
    private final int protection;
    
    private HorseArmorType(final String s, final int n, final int armorStrengthIn) {
        this.protection = armorStrengthIn;
        this.textureName = null;
        this.hash = "";
    }
    
    private HorseArmorType(final String s, final int n, final int armorStrengthIn, final String p_i46800_4_, final String p_i46800_5_) {
        this.protection = armorStrengthIn;
        this.textureName = "textures/entity/horse/armor/horse_armor_" + p_i46800_4_ + ".png";
        this.hash = p_i46800_5_;
    }
    
    public int getOrdinal() {
        return this.ordinal();
    }
    
    public String getHash() {
        return this.hash;
    }
    
    public int getProtection() {
        return this.protection;
    }
    
    @Nullable
    public String getTextureName() {
        return this.textureName;
    }
    
    public static HorseArmorType getByOrdinal(final int ordinal) {
        return values()[ordinal];
    }
    
    public static HorseArmorType getByItemStack(final ItemStack stack) {
        return stack.func_190926_b() ? HorseArmorType.NONE : getByItem(stack.getItem());
    }
    
    public static HorseArmorType getByItem(final Item itemIn) {
        if (itemIn == Items.IRON_HORSE_ARMOR) {
            return HorseArmorType.IRON;
        }
        if (itemIn == Items.GOLDEN_HORSE_ARMOR) {
            return HorseArmorType.GOLD;
        }
        return (itemIn == Items.DIAMOND_HORSE_ARMOR) ? HorseArmorType.DIAMOND : HorseArmorType.NONE;
    }
    
    public static boolean isHorseArmor(final Item itemIn) {
        return getByItem(itemIn) != HorseArmorType.NONE;
    }
}
