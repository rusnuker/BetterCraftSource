// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.item;

import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.IStringSerializable;

public enum EnumDyeColor implements IStringSerializable
{
    WHITE("WHITE", 0, 0, 15, "white", "white", 16383998, TextFormatting.WHITE), 
    ORANGE("ORANGE", 1, 1, 14, "orange", "orange", 16351261, TextFormatting.GOLD), 
    MAGENTA("MAGENTA", 2, 2, 13, "magenta", "magenta", 13061821, TextFormatting.AQUA), 
    LIGHT_BLUE("LIGHT_BLUE", 3, 3, 12, "light_blue", "lightBlue", 3847130, TextFormatting.BLUE), 
    YELLOW("YELLOW", 4, 4, 11, "yellow", "yellow", 16701501, TextFormatting.YELLOW), 
    LIME("LIME", 5, 5, 10, "lime", "lime", 8439583, TextFormatting.GREEN), 
    PINK("PINK", 6, 6, 9, "pink", "pink", 15961002, TextFormatting.LIGHT_PURPLE), 
    GRAY("GRAY", 7, 7, 8, "gray", "gray", 4673362, TextFormatting.DARK_GRAY), 
    SILVER("SILVER", 8, 8, 7, "silver", "silver", 10329495, TextFormatting.GRAY), 
    CYAN("CYAN", 9, 9, 6, "cyan", "cyan", 1481884, TextFormatting.DARK_AQUA), 
    PURPLE("PURPLE", 10, 10, 5, "purple", "purple", 8991416, TextFormatting.DARK_PURPLE), 
    BLUE("BLUE", 11, 11, 4, "blue", "blue", 3949738, TextFormatting.DARK_BLUE), 
    BROWN("BROWN", 12, 12, 3, "brown", "brown", 8606770, TextFormatting.GOLD), 
    GREEN("GREEN", 13, 13, 2, "green", "green", 6192150, TextFormatting.DARK_GREEN), 
    RED("RED", 14, 14, 1, "red", "red", 11546150, TextFormatting.DARK_RED), 
    BLACK("BLACK", 15, 15, 0, "black", "black", 1908001, TextFormatting.BLACK);
    
    private static final EnumDyeColor[] META_LOOKUP;
    private static final EnumDyeColor[] DYE_DMG_LOOKUP;
    private final int meta;
    private final int dyeDamage;
    private final String name;
    private final String unlocalizedName;
    private final int field_193351_w;
    private final float[] field_193352_x;
    private final TextFormatting chatColor;
    
    static {
        META_LOOKUP = new EnumDyeColor[values().length];
        DYE_DMG_LOOKUP = new EnumDyeColor[values().length];
        EnumDyeColor[] values;
        for (int length = (values = values()).length, i = 0; i < length; ++i) {
            final EnumDyeColor enumdyecolor = values[i];
            EnumDyeColor.META_LOOKUP[enumdyecolor.getMetadata()] = enumdyecolor;
            EnumDyeColor.DYE_DMG_LOOKUP[enumdyecolor.getDyeDamage()] = enumdyecolor;
        }
    }
    
    private EnumDyeColor(final String s, final int n, final int p_i47505_3_, final int p_i47505_4_, final String p_i47505_5_, final String p_i47505_6_, final int p_i47505_7_, final TextFormatting p_i47505_8_) {
        this.meta = p_i47505_3_;
        this.dyeDamage = p_i47505_4_;
        this.name = p_i47505_5_;
        this.unlocalizedName = p_i47505_6_;
        this.field_193351_w = p_i47505_7_;
        this.chatColor = p_i47505_8_;
        final int i = (p_i47505_7_ & 0xFF0000) >> 16;
        final int j = (p_i47505_7_ & 0xFF00) >> 8;
        final int k = (p_i47505_7_ & 0xFF) >> 0;
        this.field_193352_x = new float[] { i / 255.0f, j / 255.0f, k / 255.0f };
    }
    
    public int getMetadata() {
        return this.meta;
    }
    
    public int getDyeDamage() {
        return this.dyeDamage;
    }
    
    public String func_192396_c() {
        return this.name;
    }
    
    public String getUnlocalizedName() {
        return this.unlocalizedName;
    }
    
    public int func_193350_e() {
        return this.field_193351_w;
    }
    
    public float[] func_193349_f() {
        return this.field_193352_x;
    }
    
    public static EnumDyeColor byDyeDamage(int damage) {
        if (damage < 0 || damage >= EnumDyeColor.DYE_DMG_LOOKUP.length) {
            damage = 0;
        }
        return EnumDyeColor.DYE_DMG_LOOKUP[damage];
    }
    
    public static EnumDyeColor byMetadata(int meta) {
        if (meta < 0 || meta >= EnumDyeColor.META_LOOKUP.length) {
            meta = 0;
        }
        return EnumDyeColor.META_LOOKUP[meta];
    }
    
    @Override
    public String toString() {
        return this.unlocalizedName;
    }
    
    @Override
    public String getName() {
        return this.name;
    }
}
