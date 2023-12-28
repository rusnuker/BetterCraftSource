// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.item;

import net.minecraft.util.text.TextFormatting;

public enum EnumRarity
{
    COMMON("COMMON", 0, TextFormatting.WHITE, "Common"), 
    UNCOMMON("UNCOMMON", 1, TextFormatting.YELLOW, "Uncommon"), 
    RARE("RARE", 2, TextFormatting.AQUA, "Rare"), 
    EPIC("EPIC", 3, TextFormatting.LIGHT_PURPLE, "Epic");
    
    public final TextFormatting rarityColor;
    public final String rarityName;
    
    private EnumRarity(final String s, final int n, final TextFormatting color, final String name) {
        this.rarityColor = color;
        this.rarityName = name;
    }
}
