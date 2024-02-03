// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.user.cosmetic.util;

import net.minecraft.client.Minecraft;

public abstract class AnimatedCosmeticData extends CosmeticData
{
    private float fadeAnimation;
    
    public void updateFadeAnimation(final boolean onGround) {
        final float fps = (float)Math.max(1, Minecraft.getDebugFPS());
        if (onGround && this.fadeAnimation > -1.0f) {
            this.fadeAnimation -= 0.8f / fps;
        }
        else if (!onGround && this.fadeAnimation < 1.0f) {
            this.fadeAnimation += 0.8f / fps;
        }
        this.fadeAnimation = Math.min(this.fadeAnimation, 1.0f);
        this.fadeAnimation = Math.max(this.fadeAnimation, -1.0f);
    }
    
    public float getOnGroundStrength() {
        return (this.fadeAnimation - 1.0f) / -2.0f;
    }
    
    public float getAirStrength() {
        return (this.fadeAnimation + 1.0f) / 2.0f;
    }
    
    public float getFadeAnimation() {
        return this.fadeAnimation;
    }
}
