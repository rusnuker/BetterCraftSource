// 
// Decompiled by Procyon v0.6.0
// 

package me.nzxtercode.bettercraft.client.hud;

public interface IRender extends IRenderConfig
{
    int getWidth();
    
    int getHeight();
    
    void render(final ScreenPosition p0);
    
    default void renderDummy(final ScreenPosition pos) {
        this.render(pos);
    }
    
    default boolean isEnabled() {
        return true;
    }
}
