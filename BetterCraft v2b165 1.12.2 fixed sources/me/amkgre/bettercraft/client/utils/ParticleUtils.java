// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.utils;

import net.minecraft.client.gui.Gui;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public class ParticleUtils
{
    private final List<Particle> particles;
    private int width;
    private int height;
    private int count;
    
    public ParticleUtils(final int width, final int height) {
        this.width = width;
        this.height = height;
        this.count = 150;
        this.particles = new ArrayList<Particle>();
        for (int count = 0; count <= this.count; ++count) {
            this.particles.add(new Particle(new Random().nextInt(width), new Random().nextInt(height)));
        }
    }
    
    public void drawParticles() {
        this.particles.forEach(particle -> particle.drawParticle());
    }
    
    public class Particle
    {
        private int xPos;
        private int yPos;
        
        public Particle(final int xPos, final int yPos) {
            this.xPos = xPos;
            this.yPos = yPos;
        }
        
        public void drawParticle() {
            this.xPos += new Random().nextInt(2);
            this.yPos += new Random().nextInt(2);
            final int particleSize = 1;
            if (this.xPos > ParticleUtils.this.width) {
                this.xPos = -1;
            }
            if (this.yPos > ParticleUtils.this.height) {
                this.yPos = -1;
            }
            Gui.drawRect(this.xPos, this.yPos, this.xPos + 1, this.yPos + 1, ColorUtils.rainbowEffect(0L, 1.0f).getRGB());
        }
    }
}
