// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.cosmetics.impl;

import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.client.Minecraft;

public class CosmeticSkinDerp
{
    public static int step;
    
    static {
        CosmeticSkinDerp.step = 0;
    }
    
    public static void tick() {
        switch (++CosmeticSkinDerp.step) {
            case 1: {
                Minecraft.getMinecraft().gameSettings.switchModelPartEnabled(EnumPlayerModelParts.HAT);
                break;
            }
            case 2: {
                Minecraft.getMinecraft().gameSettings.switchModelPartEnabled(EnumPlayerModelParts.JACKET);
                Minecraft.getMinecraft().gameSettings.switchModelPartEnabled(EnumPlayerModelParts.LEFT_SLEEVE);
                Minecraft.getMinecraft().gameSettings.switchModelPartEnabled(EnumPlayerModelParts.RIGHT_SLEEVE);
                break;
            }
            case 3: {
                Minecraft.getMinecraft().gameSettings.switchModelPartEnabled(EnumPlayerModelParts.LEFT_PANTS_LEG);
                Minecraft.getMinecraft().gameSettings.switchModelPartEnabled(EnumPlayerModelParts.RIGHT_PANTS_LEG);
                break;
            }
        }
        if (CosmeticSkinDerp.step == 3) {
            CosmeticSkinDerp.step = 0;
        }
    }
}
