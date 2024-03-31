// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.cosmetics;

import java.util.HashMap;
import java.awt.Color;
import me.amkgre.bettercraft.client.utils.ColorUtils;
import net.minecraft.client.entity.AbstractClientPlayer;

public class CosmeticController
{
    public static boolean shouldRenderTopHat(final AbstractClientPlayer player) {
        return true;
    }
    
    public static float[] getTopHatColor(final AbstractClientPlayer player) {
        final Color rainbow = ColorUtils.rainbowEffect(1L, 1.0f);
        return new float[] { (float)rainbow.getRed(), (float)rainbow.getGreen(), (float)rainbow.getBlue() };
    }
    
    public static String getColor() {
        final String s = new HashMap<Object, String>().getOrDefault("color", "#FFFFFF");
        return String.valueOf(ColorUtils.rainbowEffect(0L, 1.0f).getRGB());
    }
}
