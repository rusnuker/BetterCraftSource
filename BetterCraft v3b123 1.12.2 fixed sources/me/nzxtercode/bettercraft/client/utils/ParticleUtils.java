// 
// Decompiled by Procyon v0.6.0
// 

package me.nzxtercode.bettercraft.client.utils;

import net.minecraft.client.gui.Gui;
import me.nzxtercode.bettercraft.client.Config;
import org.lwjgl.input.Mouse;
import net.minecraft.client.gui.GuiScreen;
import java.util.ArrayList;

public class ParticleUtils
{
    private final ArrayList<Particle> particles;
    private final int count;
    private final float size;
    
    public ParticleUtils(final int count, final float size) {
        this.particles = new ArrayList<Particle>();
        this.count = count;
        this.size = size;
    }
    
    public void setup() {
        this.particles.clear();
        for (int count = 0; count <= this.count; ++count) {
            this.particles.add(new Particle(this.size));
        }
    }
    
    public void render(final int mouseX, final int mouseY) {
        this.particles.forEach(particle -> particle.render(mouseX2, mouseY2));
    }
    
    public float genRandom(final float min, final float max) {
        return (float)(min + Math.random() * (max - min + 1.0f));
    }
    
    public class Particle
    {
        private float x;
        private float y;
        private boolean xDir;
        private boolean yDir;
        private float speed;
        private float sSpeed;
        private float size;
        private boolean hold;
        
        public Particle(final float size) {
            this.x = ParticleUtils.this.genRandom(0.0f, (float)GuiScreen.width);
            this.y = ParticleUtils.this.genRandom(0.0f, (float)GuiScreen.height);
            this.xDir = ((int)ParticleUtils.this.genRandom(0.0f, 1.0f) == 1);
            this.yDir = ((int)ParticleUtils.this.genRandom(0.0f, 1.0f) == 1);
            this.size = size;
            final float genRandom = ParticleUtils.this.genRandom(0.2f, 1.0f);
            this.speed = genRandom;
            this.sSpeed = genRandom;
        }
        
        public void render(final int mouseX, final int mouseY) {
            if (Mouse.isButtonDown(1)) {
                if ((int)this.x != mouseX) {
                    if (this.x < mouseX) {
                        this.x += this.speed;
                    }
                    else {
                        this.x -= this.speed;
                    }
                }
                if ((int)this.y != mouseY) {
                    if (this.y < mouseY && this.y != mouseY) {
                        this.y += this.speed;
                    }
                    else {
                        this.y -= this.speed;
                    }
                }
            }
            else {
                if (this.xDir) {
                    this.x += this.speed;
                }
                else {
                    this.x -= this.speed;
                }
                if (this.yDir) {
                    this.y += this.speed;
                }
                else {
                    this.y -= this.speed;
                }
                if (this.x + this.size >= GuiScreen.width || this.x - this.size <= 0.0f) {
                    this.xDir = !this.xDir;
                    if (this.speed < 5.0f) {
                        this.speed += 2.0f;
                    }
                }
                if (this.y + this.size >= GuiScreen.height || this.y - this.size <= 0.0f) {
                    this.yDir = !this.yDir;
                    if (this.speed < 5.0f) {
                        this.speed += 2.0f;
                    }
                }
                if (this.speed > this.sSpeed) {
                    this.speed -= 0.05f;
                }
            }
            Gui.drawRect((int)this.x, (int)this.y, (int)this.x + (int)this.size, (int)this.y + (int)this.size, Config.getInstance().getColor("Particel").get("color").getAsInt());
        }
    }
}
