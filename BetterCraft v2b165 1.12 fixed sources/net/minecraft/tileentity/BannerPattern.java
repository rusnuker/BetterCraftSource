// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.tileentity;

import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.init.Items;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public enum BannerPattern
{
    BASE("BASE", 0, "base", "b"), 
    SQUARE_BOTTOM_LEFT("SQUARE_BOTTOM_LEFT", 1, "square_bottom_left", "bl", "   ", "   ", "#  "), 
    SQUARE_BOTTOM_RIGHT("SQUARE_BOTTOM_RIGHT", 2, "square_bottom_right", "br", "   ", "   ", "  #"), 
    SQUARE_TOP_LEFT("SQUARE_TOP_LEFT", 3, "square_top_left", "tl", "#  ", "   ", "   "), 
    SQUARE_TOP_RIGHT("SQUARE_TOP_RIGHT", 4, "square_top_right", "tr", "  #", "   ", "   "), 
    STRIPE_BOTTOM("STRIPE_BOTTOM", 5, "stripe_bottom", "bs", "   ", "   ", "###"), 
    STRIPE_TOP("STRIPE_TOP", 6, "stripe_top", "ts", "###", "   ", "   "), 
    STRIPE_LEFT("STRIPE_LEFT", 7, "stripe_left", "ls", "#  ", "#  ", "#  "), 
    STRIPE_RIGHT("STRIPE_RIGHT", 8, "stripe_right", "rs", "  #", "  #", "  #"), 
    STRIPE_CENTER("STRIPE_CENTER", 9, "stripe_center", "cs", " # ", " # ", " # "), 
    STRIPE_MIDDLE("STRIPE_MIDDLE", 10, "stripe_middle", "ms", "   ", "###", "   "), 
    STRIPE_DOWNRIGHT("STRIPE_DOWNRIGHT", 11, "stripe_downright", "drs", "#  ", " # ", "  #"), 
    STRIPE_DOWNLEFT("STRIPE_DOWNLEFT", 12, "stripe_downleft", "dls", "  #", " # ", "#  "), 
    STRIPE_SMALL("STRIPE_SMALL", 13, "small_stripes", "ss", "# #", "# #", "   "), 
    CROSS("CROSS", 14, "cross", "cr", "# #", " # ", "# #"), 
    STRAIGHT_CROSS("STRAIGHT_CROSS", 15, "straight_cross", "sc", " # ", "###", " # "), 
    TRIANGLE_BOTTOM("TRIANGLE_BOTTOM", 16, "triangle_bottom", "bt", "   ", " # ", "# #"), 
    TRIANGLE_TOP("TRIANGLE_TOP", 17, "triangle_top", "tt", "# #", " # ", "   "), 
    TRIANGLES_BOTTOM("TRIANGLES_BOTTOM", 18, "triangles_bottom", "bts", "   ", "# #", " # "), 
    TRIANGLES_TOP("TRIANGLES_TOP", 19, "triangles_top", "tts", " # ", "# #", "   "), 
    DIAGONAL_LEFT("DIAGONAL_LEFT", 20, "diagonal_left", "ld", "## ", "#  ", "   "), 
    DIAGONAL_RIGHT("DIAGONAL_RIGHT", 21, "diagonal_up_right", "rd", "   ", "  #", " ##"), 
    DIAGONAL_LEFT_MIRROR("DIAGONAL_LEFT_MIRROR", 22, "diagonal_up_left", "lud", "   ", "#  ", "## "), 
    DIAGONAL_RIGHT_MIRROR("DIAGONAL_RIGHT_MIRROR", 23, "diagonal_right", "rud", " ##", "  #", "   "), 
    CIRCLE_MIDDLE("CIRCLE_MIDDLE", 24, "circle", "mc", "   ", " # ", "   "), 
    RHOMBUS_MIDDLE("RHOMBUS_MIDDLE", 25, "rhombus", "mr", " # ", "# #", " # "), 
    HALF_VERTICAL("HALF_VERTICAL", 26, "half_vertical", "vh", "## ", "## ", "## "), 
    HALF_HORIZONTAL("HALF_HORIZONTAL", 27, "half_horizontal", "hh", "###", "###", "   "), 
    HALF_VERTICAL_MIRROR("HALF_VERTICAL_MIRROR", 28, "half_vertical_right", "vhr", " ##", " ##", " ##"), 
    HALF_HORIZONTAL_MIRROR("HALF_HORIZONTAL_MIRROR", 29, "half_horizontal_bottom", "hhb", "   ", "###", "###"), 
    BORDER("BORDER", 30, "border", "bo", "###", "# #", "###"), 
    CURLY_BORDER("CURLY_BORDER", 31, "curly_border", "cbo", new ItemStack(Blocks.VINE)), 
    CREEPER("CREEPER", 32, "creeper", "cre", new ItemStack(Items.SKULL, 1, 4)), 
    GRADIENT("GRADIENT", 33, "gradient", "gra", "# #", " # ", " # "), 
    GRADIENT_UP("GRADIENT_UP", 34, "gradient_up", "gru", " # ", " # ", "# #"), 
    BRICKS("BRICKS", 35, "bricks", "bri", new ItemStack(Blocks.BRICK_BLOCK)), 
    SKULL("SKULL", 36, "skull", "sku", new ItemStack(Items.SKULL, 1, 1)), 
    FLOWER("FLOWER", 37, "flower", "flo", new ItemStack(Blocks.RED_FLOWER, 1, BlockFlower.EnumFlowerType.OXEYE_DAISY.getMeta())), 
    MOJANG("MOJANG", 38, "mojang", "moj", new ItemStack(Items.GOLDEN_APPLE, 1, 1));
    
    private final String field_191014_N;
    private final String field_191015_O;
    private final String[] field_191016_P;
    private ItemStack field_191017_Q;
    
    private BannerPattern(final String s, final int n, final String p_i47245_3_, final String p_i47245_4_) {
        this.field_191016_P = new String[3];
        this.field_191017_Q = ItemStack.field_190927_a;
        this.field_191014_N = p_i47245_3_;
        this.field_191015_O = p_i47245_4_;
    }
    
    private BannerPattern(final String s, final int n, final String p_i47246_3_, final String p_i47246_4_, final ItemStack p_i47246_5_) {
        this(s, n, p_i47246_3_, p_i47246_4_);
        this.field_191017_Q = p_i47246_5_;
    }
    
    private BannerPattern(final String s, final int n, final String p_i47247_3_, final String p_i47247_4_, final String p_i47247_5_, final String p_i47247_6_, final String p_i47247_7_) {
        this(s, n, p_i47247_3_, p_i47247_4_);
        this.field_191016_P[0] = p_i47247_5_;
        this.field_191016_P[1] = p_i47247_6_;
        this.field_191016_P[2] = p_i47247_7_;
    }
    
    public String func_190997_a() {
        return this.field_191014_N;
    }
    
    public String func_190993_b() {
        return this.field_191015_O;
    }
    
    public String[] func_190996_c() {
        return this.field_191016_P;
    }
    
    public boolean func_191000_d() {
        return !this.field_191017_Q.func_190926_b() || this.field_191016_P[0] != null;
    }
    
    public boolean func_190999_e() {
        return !this.field_191017_Q.func_190926_b();
    }
    
    public ItemStack func_190998_f() {
        return this.field_191017_Q;
    }
    
    @Nullable
    public static BannerPattern func_190994_a(final String p_190994_0_) {
        BannerPattern[] values;
        for (int length = (values = values()).length, i = 0; i < length; ++i) {
            final BannerPattern bannerpattern = values[i];
            if (bannerpattern.field_191015_O.equals(p_190994_0_)) {
                return bannerpattern;
            }
        }
        return null;
    }
}
