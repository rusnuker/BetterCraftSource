// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.cosmetics;

import net.minecraft.util.ResourceLocation;

public class AnimatedResourceLocation
{
    private final int fpt;
    private int currentTick;
    private int currentFrame;
    private final ResourceLocation[] textures;
    
    public AnimatedResourceLocation(final String folder, final int frames, final int fpt) {
        this.currentTick = 0;
        this.currentFrame = 0;
        this.fpt = fpt;
        this.textures = new ResourceLocation[frames];
        for (int i2 = 0; i2 < frames; ++i2) {
            this.textures[i2] = new ResourceLocation(String.valueOf(folder) + "/" + i2 + ".png");
        }
    }
    
    public ResourceLocation getTexture() {
        return this.textures[this.currentFrame];
    }
    
    public void update() {
        if (this.currentTick > this.fpt) {
            this.currentTick = 0;
            ++this.currentFrame;
            if (this.currentFrame > this.textures.length - 1) {
                this.currentFrame = 0;
            }
        }
        ++this.currentTick;
    }
}
