// 
// Decompiled by Procyon v0.6.0
// 

package me.nzxtercode.bettercraft.client.hud;

import net.minecraft.util.MathHelper;
import net.minecraft.client.Minecraft;

public class ScreenResolution
{
    private final double scaledWidthD;
    private final double scaledHeightD;
    private int scaledWidth;
    private int scaledHeight;
    private int scaleFactor;
    Minecraft minecraft;
    
    public ScreenResolution(final Minecraft p_i46445_1_) {
        this.minecraft = p_i46445_1_;
        this.scaledWidth = p_i46445_1_.displayWidth;
        this.scaledHeight = p_i46445_1_.displayHeight;
        this.scaleFactor = 1;
        final boolean flag = p_i46445_1_.isUnicode();
        int i = 3;
        if (i == 0) {
            i = 1000;
        }
        while (this.scaleFactor < i && this.scaledWidth / (this.scaleFactor + 1) >= 320 && this.scaledHeight / (this.scaleFactor + 1) >= 240) {
            ++this.scaleFactor;
        }
        if (flag && this.scaleFactor % 2 != 0 && this.scaleFactor != 1) {
            --this.scaleFactor;
        }
        this.scaledWidthD = this.scaledWidth / (double)this.scaleFactor;
        this.scaledHeightD = this.scaledHeight / (double)this.scaleFactor;
        this.scaledWidth = MathHelper.ceiling_double_int(this.scaledWidthD);
        this.scaledHeight = MathHelper.ceiling_double_int(this.scaledHeightD);
    }
    
    public int getCenterX() {
        return this.minecraft.displayWidth / (2 * this.scaleFactor);
    }
    
    public int getCenterY() {
        return this.minecraft.displayHeight / (2 * this.scaleFactor);
    }
    
    public ScreenResolution(final Minecraft minecraft, final int displayWidth, final int displayHeight) {
        this.minecraft = minecraft;
        this.scaledWidth = displayWidth;
        this.scaledHeight = displayHeight;
        this.scaleFactor = 1;
        final boolean flag = minecraft.isUnicode();
        int i = 3;
        if (i == 0) {
            i = 1000;
        }
        while (this.scaleFactor < i && this.scaledWidth / (this.scaleFactor + 1) >= 320 && this.scaledHeight / (this.scaleFactor + 1) >= 240) {
            ++this.scaleFactor;
        }
        if (flag && this.scaleFactor % 2 != 0 && this.scaleFactor != 1) {
            --this.scaleFactor;
        }
        this.scaledWidthD = this.scaledWidth / (double)this.scaleFactor;
        this.scaledHeightD = this.scaledHeight / (double)this.scaleFactor;
        this.scaledWidth = MathHelper.ceiling_double_int(this.scaledWidthD);
        this.scaledHeight = MathHelper.ceiling_double_int(this.scaledHeightD);
    }
    
    public int getWidthScale() {
        return this.scaledWidth / 640;
    }
    
    public int getHeightScale() {
        return this.scaledHeight / 360;
    }
    
    public int getScaledWidth() {
        return this.scaledWidth;
    }
    
    public int getScaledHeight() {
        return this.scaledHeight;
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
