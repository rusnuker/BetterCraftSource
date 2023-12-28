// 
// Decompiled by Procyon v0.6.0
// 

package com.TominoCZ.FBP.util;

import java.util.HashMap;

public class FBPObfUtil
{
    private static HashMap<String, String> map;
    
    static {
        FBPObfUtil.map = new HashMap<String, String>();
        final String obfNames = "field_151579_a,field_151577_b,field_151578_c,field_151575_d,field_151576_e,field_151573_f,field_151574_g,field_151586_h,field_151587_i,field_151584_j,field_151585_k,field_151582_l,field_151583_m,field_151580_n,field_151581_o,field_151595_p,field_151594_q,field_151593_r,field_151592_s,field_151591_t,field_151590_u,field_151589_v,field_151588_w,field_151598_x,field_151597_y,field_151596_z,field_151570_A,field_151571_B,field_151572_C,field_151566_D,field_151567_E,field_151568_F,field_151569_G,field_76233_E,field_175972_I,field_189963_J";
        final String deobfNames = "AIR,GRASS,GROUND,WOOD,ROCK,IRON,ANVIL,WATER,LAVA,LEAVES,PLANTS,VINE,SPONGE,CLOTH,FIRE,SAND,CIRCUITS,CARPET,GLASS,REDSTONE_LIGHT,TNT,CORAL,ICE,PACKED_ICE,SNOW,CRAFTED_SNOW,CACTUS,CLAY,GOURD,DRAGON_EGG,PORTAL,CAKE,WEB,PISTON,BARRIER,STRUCTURE_VOID";
        final String[] obf = obfNames.split(",");
        final String[] deobf = deobfNames.split(",");
        for (int i = 0; i < obf.length; ++i) {
            FBPObfUtil.map.put(obf[i], deobf[i].toLowerCase());
        }
    }
    
    public static String translateObfMaterialName(final String fieldName) {
        if (!FBPObfUtil.map.containsKey(fieldName)) {
            return fieldName;
        }
        return FBPObfUtil.map.get(fieldName);
    }
}
