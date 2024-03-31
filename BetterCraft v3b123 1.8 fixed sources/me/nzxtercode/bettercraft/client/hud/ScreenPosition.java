// 
// Decompiled by Procyon v0.6.0
// 

package me.nzxtercode.bettercraft.client.hud;

import net.minecraft.client.Minecraft;

public class ScreenPosition
{
    private static final Minecraft mc;
    private int x;
    private int y;
    
    static {
        mc = Minecraft.getMinecraft();
    }
    
    public ScreenPosition(final double x, final double y) {
        this.setRelativ(x, y);
    }
    
    public ScreenPosition(final int x, final int y) {
        this.setAbsolute(x, y);
    }
    
    public static ScreenPosition fromRelativPosition(final double x, final double y) {
        return new ScreenPosition(x, y);
    }
    
    public static ScreenPosition fromAbsolute(final int x, final int y) {
        return new ScreenPosition(x, y);
    }
    
    public int getAbsoluteX() {
        new ScreenResolution(ScreenPosition.mc);
        return this.x;
    }
    
    public int getAbsoluteY() {
        new ScreenResolution(ScreenPosition.mc);
        return this.y;
    }
    
    public double getRelativX() {
        final ScreenResolution screenresolution = new ScreenResolution(ScreenPosition.mc);
        return (int)(this.x / screenresolution.getScaledWidth_double());
    }
    
    public double getRelativY() {
        final ScreenResolution screenresolution = new ScreenResolution(ScreenPosition.mc);
        return (int)(this.y / screenresolution.getScaledHeight_double());
    }
    
    public void setAbsolute(final int x, final int y) {
        this.x = x;
        this.y = y;
    }
    
    public void setRelativ(final double x, final double y) {
        final ScreenResolution screenresolution = new ScreenResolution(ScreenPosition.mc);
        this.x = (int)(x / screenresolution.getScaledWidth());
        this.y = (int)(y / screenresolution.getScaledHeight());
    }
}
