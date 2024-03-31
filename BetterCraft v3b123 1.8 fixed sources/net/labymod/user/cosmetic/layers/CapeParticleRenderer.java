// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.user.cosmetic.layers;

import java.util.Iterator;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.labymod.user.User;
import java.util.LinkedList;
import net.labymod.main.LabyMod;
import java.util.List;
import java.util.Random;

public class CapeParticleRenderer
{
    private Random random;
    private List<ParticleStar> starList;
    private long lastStarSpawned;
    
    public CapeParticleRenderer() {
        this.random = LabyMod.getRandom();
        this.starList = new LinkedList<ParticleStar>();
        this.lastStarSpawned = -1L;
    }
    
    public void render(final User user, final AbstractClientPlayer entitylivingbaseIn, final float partialTicks) {
        if (!LabyMod.getSettings().capeOriginalParticles || user.isMojangCapeModified()) {
            return;
        }
        if (this.lastStarSpawned < System.currentTimeMillis()) {
            this.lastStarSpawned = System.currentTimeMillis() + 200L;
            final double x = (0.5 - this.random.nextDouble()) * 0.5;
            final double y = this.random.nextDouble() - 0.025;
            final ParticleStar particleStar = new ParticleStar(x, y, System.currentTimeMillis());
            this.starList.add(particleStar);
        }
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0, 0.0, -0.07);
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        final Iterator<ParticleStar> iterator = this.starList.iterator();
        while (iterator.hasNext()) {
            final ParticleStar next = iterator.next();
            next.render();
            if (next.isFadedOut()) {
                iterator.remove();
            }
        }
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.popMatrix();
    }
}
