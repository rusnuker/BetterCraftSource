// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.user.cosmetic.layers;

import java.beans.ConstructorProperties;
import net.labymod.utils.DrawUtils;
import org.lwjgl.opengl.GL11;
import net.labymod.main.ModTextures;
import net.minecraft.client.Minecraft;
import net.labymod.main.LabyMod;

public class ParticleStar
{
    private static final long FADE_TIME = 1500L;
    private double x;
    private double y;
    private long timestampSpawned;
    
    public void render() {
        final DrawUtils draw = LabyMod.getInstance().getDrawUtils();
        Minecraft.getMinecraft().getTextureManager().bindTexture(ModTextures.COSMETIC_CAPE_STAR);
        final double starSize = (this.timestampSpawned - System.currentTimeMillis()) / 10000.0;
        final double offset = starSize / 2.0;
        GL11.glColor4f(255.0f, 255.0f, 255.0f, (float)(1.0 + offset * 15.0));
        draw.drawTexture(this.x - offset, this.y - offset, 0.0, 0.0, 255.0, 255.0, starSize, starSize, 1.1f);
    }
    
    public boolean isFadedOut() {
        return System.currentTimeMillis() - this.timestampSpawned > 1500L;
    }
    
    @ConstructorProperties({ "x", "y", "timestampSpawned" })
    public ParticleStar(final double x, final double y, final long timestampSpawned) {
        this.x = x;
        this.y = y;
        this.timestampSpawned = timestampSpawned;
    }
}
