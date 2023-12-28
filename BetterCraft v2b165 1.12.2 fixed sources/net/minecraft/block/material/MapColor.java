// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.block.material;

import net.minecraft.item.EnumDyeColor;

public class MapColor
{
    public static final MapColor[] COLORS;
    public static final MapColor[] field_193575_b;
    public static final MapColor AIR;
    public static final MapColor GRASS;
    public static final MapColor SAND;
    public static final MapColor CLOTH;
    public static final MapColor TNT;
    public static final MapColor ICE;
    public static final MapColor IRON;
    public static final MapColor FOLIAGE;
    public static final MapColor SNOW;
    public static final MapColor CLAY;
    public static final MapColor DIRT;
    public static final MapColor STONE;
    public static final MapColor WATER;
    public static final MapColor WOOD;
    public static final MapColor QUARTZ;
    public static final MapColor ADOBE;
    public static final MapColor MAGENTA;
    public static final MapColor LIGHT_BLUE;
    public static final MapColor YELLOW;
    public static final MapColor LIME;
    public static final MapColor PINK;
    public static final MapColor GRAY;
    public static final MapColor SILVER;
    public static final MapColor CYAN;
    public static final MapColor PURPLE;
    public static final MapColor BLUE;
    public static final MapColor BROWN;
    public static final MapColor GREEN;
    public static final MapColor RED;
    public static final MapColor BLACK;
    public static final MapColor GOLD;
    public static final MapColor DIAMOND;
    public static final MapColor LAPIS;
    public static final MapColor EMERALD;
    public static final MapColor OBSIDIAN;
    public static final MapColor NETHERRACK;
    public static final MapColor field_193561_M;
    public static final MapColor field_193562_N;
    public static final MapColor field_193563_O;
    public static final MapColor field_193564_P;
    public static final MapColor field_193565_Q;
    public static final MapColor field_193566_R;
    public static final MapColor field_193567_S;
    public static final MapColor field_193568_T;
    public static final MapColor field_193569_U;
    public static final MapColor field_193570_V;
    public static final MapColor field_193571_W;
    public static final MapColor field_193572_X;
    public static final MapColor field_193573_Y;
    public static final MapColor field_193574_Z;
    public static final MapColor field_193559_aa;
    public static final MapColor field_193560_ab;
    public int colorValue;
    public final int colorIndex;
    
    static {
        COLORS = new MapColor[64];
        field_193575_b = new MapColor[16];
        AIR = new MapColor(0, 0);
        GRASS = new MapColor(1, 8368696);
        SAND = new MapColor(2, 16247203);
        CLOTH = new MapColor(3, 13092807);
        TNT = new MapColor(4, 16711680);
        ICE = new MapColor(5, 10526975);
        IRON = new MapColor(6, 10987431);
        FOLIAGE = new MapColor(7, 31744);
        SNOW = new MapColor(8, 16777215);
        CLAY = new MapColor(9, 10791096);
        DIRT = new MapColor(10, 9923917);
        STONE = new MapColor(11, 7368816);
        WATER = new MapColor(12, 4210943);
        WOOD = new MapColor(13, 9402184);
        QUARTZ = new MapColor(14, 16776437);
        ADOBE = new MapColor(15, 14188339);
        MAGENTA = new MapColor(16, 11685080);
        LIGHT_BLUE = new MapColor(17, 6724056);
        YELLOW = new MapColor(18, 15066419);
        LIME = new MapColor(19, 8375321);
        PINK = new MapColor(20, 15892389);
        GRAY = new MapColor(21, 5000268);
        SILVER = new MapColor(22, 10066329);
        CYAN = new MapColor(23, 5013401);
        PURPLE = new MapColor(24, 8339378);
        BLUE = new MapColor(25, 3361970);
        BROWN = new MapColor(26, 6704179);
        GREEN = new MapColor(27, 6717235);
        RED = new MapColor(28, 10040115);
        BLACK = new MapColor(29, 1644825);
        GOLD = new MapColor(30, 16445005);
        DIAMOND = new MapColor(31, 6085589);
        LAPIS = new MapColor(32, 4882687);
        EMERALD = new MapColor(33, 55610);
        OBSIDIAN = new MapColor(34, 8476209);
        NETHERRACK = new MapColor(35, 7340544);
        field_193561_M = new MapColor(36, 13742497);
        field_193562_N = new MapColor(37, 10441252);
        field_193563_O = new MapColor(38, 9787244);
        field_193564_P = new MapColor(39, 7367818);
        field_193565_Q = new MapColor(40, 12223780);
        field_193566_R = new MapColor(41, 6780213);
        field_193567_S = new MapColor(42, 10505550);
        field_193568_T = new MapColor(43, 3746083);
        field_193569_U = new MapColor(44, 8874850);
        field_193570_V = new MapColor(45, 5725276);
        field_193571_W = new MapColor(46, 8014168);
        field_193572_X = new MapColor(47, 4996700);
        field_193573_Y = new MapColor(48, 4993571);
        field_193574_Z = new MapColor(49, 5001770);
        field_193559_aa = new MapColor(50, 9321518);
        field_193560_ab = new MapColor(51, 2430480);
        MapColor.field_193575_b[EnumDyeColor.WHITE.getMetadata()] = MapColor.SNOW;
        MapColor.field_193575_b[EnumDyeColor.ORANGE.getMetadata()] = MapColor.ADOBE;
        MapColor.field_193575_b[EnumDyeColor.MAGENTA.getMetadata()] = MapColor.MAGENTA;
        MapColor.field_193575_b[EnumDyeColor.LIGHT_BLUE.getMetadata()] = MapColor.LIGHT_BLUE;
        MapColor.field_193575_b[EnumDyeColor.YELLOW.getMetadata()] = MapColor.YELLOW;
        MapColor.field_193575_b[EnumDyeColor.LIME.getMetadata()] = MapColor.LIME;
        MapColor.field_193575_b[EnumDyeColor.PINK.getMetadata()] = MapColor.PINK;
        MapColor.field_193575_b[EnumDyeColor.GRAY.getMetadata()] = MapColor.GRAY;
        MapColor.field_193575_b[EnumDyeColor.SILVER.getMetadata()] = MapColor.SILVER;
        MapColor.field_193575_b[EnumDyeColor.CYAN.getMetadata()] = MapColor.CYAN;
        MapColor.field_193575_b[EnumDyeColor.PURPLE.getMetadata()] = MapColor.PURPLE;
        MapColor.field_193575_b[EnumDyeColor.BLUE.getMetadata()] = MapColor.BLUE;
        MapColor.field_193575_b[EnumDyeColor.BROWN.getMetadata()] = MapColor.BROWN;
        MapColor.field_193575_b[EnumDyeColor.GREEN.getMetadata()] = MapColor.GREEN;
        MapColor.field_193575_b[EnumDyeColor.RED.getMetadata()] = MapColor.RED;
        MapColor.field_193575_b[EnumDyeColor.BLACK.getMetadata()] = MapColor.BLACK;
    }
    
    private MapColor(final int index, final int color) {
        if (index >= 0 && index <= 63) {
            this.colorIndex = index;
            this.colorValue = color;
            MapColor.COLORS[index] = this;
            return;
        }
        throw new IndexOutOfBoundsException("Map colour ID must be between 0 and 63 (inclusive)");
    }
    
    public int getMapColor(final int p_151643_1_) {
        int i = 220;
        if (p_151643_1_ == 3) {
            i = 135;
        }
        if (p_151643_1_ == 2) {
            i = 255;
        }
        if (p_151643_1_ == 1) {
            i = 220;
        }
        if (p_151643_1_ == 0) {
            i = 180;
        }
        final int j = (this.colorValue >> 16 & 0xFF) * i / 255;
        final int k = (this.colorValue >> 8 & 0xFF) * i / 255;
        final int l = (this.colorValue & 0xFF) * i / 255;
        return 0xFF000000 | j << 16 | k << 8 | l;
    }
    
    public static MapColor func_193558_a(final EnumDyeColor p_193558_0_) {
        return MapColor.field_193575_b[p_193558_0_.getMetadata()];
    }
}
