// 
// Decompiled by Procyon v0.6.0
// 

package me.nzxtercode.bettercraft.client.utils;

import java.awt.Color;

public class ColorUtils
{
    public static int rainbowEffect() {
        return Color.HSBtoRGB(System.currentTimeMillis() % 3000L / 3000.0f, 0.8f, 1.0f);
    }
}
