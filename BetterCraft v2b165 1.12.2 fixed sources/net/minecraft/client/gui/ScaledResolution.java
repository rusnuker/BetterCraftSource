// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.gui;

import net.minecraft.util.math.MathHelper;
import net.minecraft.client.Minecraft;

public class ScaledResolution
{
    private final double scaledWidthD;
    private final double scaledHeightD;
    private static int scaledWidth;
    private static int scaledHeight;
    private int scaleFactor;
    
    public ScaledResolution(final Minecraft minecraftClient) {
        ScaledResolution.scaledWidth = minecraftClient.displayWidth;
        ScaledResolution.scaledHeight = minecraftClient.displayHeight;
        this.scaleFactor = 1;
        final boolean flag = minecraftClient.isUnicode();
        int i = minecraftClient.gameSettings.guiScale;
        if (i == 0) {
            i = 1000;
        }
        while (this.scaleFactor < i && ScaledResolution.scaledWidth / (this.scaleFactor + 1) >= 320 && ScaledResolution.scaledHeight / (this.scaleFactor + 1) >= 240) {
            ++this.scaleFactor;
        }
        if (flag && this.scaleFactor % 2 != 0 && this.scaleFactor != 1) {
            --this.scaleFactor;
        }
        this.scaledWidthD = ScaledResolution.scaledWidth / (double)this.scaleFactor;
        this.scaledHeightD = ScaledResolution.scaledHeight / (double)this.scaleFactor;
        ScaledResolution.scaledWidth = MathHelper.ceil(this.scaledWidthD);
        ScaledResolution.scaledHeight = MathHelper.ceil(this.scaledHeightD);
    }
    
    public static int getScaledWidth() {
        return ScaledResolution.scaledWidth;
    }
    
    public static int getScaledHeight() {
        return ScaledResolution.scaledHeight;
    }
    
    public double getScaledWidth_double() {
        return this.scaledWidthD;
    }
    
    public double getScaledHeight_double() {
        return this.scaledHeightD;
    }
    
    public int getScaleFactor() {
        return this.scaleFactor;
    }
}
