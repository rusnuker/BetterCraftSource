// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.particle;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import shadersmod.client.Shaders;
import optifine.Config;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;

public class ParticleItemPickup extends Particle
{
    private final Entity item;
    private final Entity target;
    private int age;
    private final int maxAge;
    private final float yOffset;
    private final RenderManager renderManager;
    
    public ParticleItemPickup(final World worldIn, final Entity p_i1233_2_, final Entity p_i1233_3_, final float p_i1233_4_) {
        super(worldIn, p_i1233_2_.posX, p_i1233_2_.posY, p_i1233_2_.posZ, p_i1233_2_.motionX, p_i1233_2_.motionY, p_i1233_2_.motionZ);
        this.renderManager = Minecraft.getMinecraft().getRenderManager();
        this.item = p_i1233_2_;
        this.target = p_i1233_3_;
        this.maxAge = 3;
        this.yOffset = p_i1233_4_;
    }
    
    @Override
    public void renderParticle(final BufferBuilder worldRendererIn, final Entity entityIn, final float partialTicks, final float rotationX, final float rotationZ, final float rotationYZ, final float rotationXY, final float rotationXZ) {
        int i = 0;
        if (Config.isShaders()) {
            i = Shaders.activeProgram;
            Shaders.nextEntity(this.item);
        }
        float f = (this.age + partialTicks) / this.maxAge;
        f *= f;
        final double d0 = this.item.posX;
        final double d2 = this.item.posY;
        final double d3 = this.item.posZ;
        final double d4 = this.target.lastTickPosX + (this.target.posX - this.target.lastTickPosX) * partialTicks;
        final double d5 = this.target.lastTickPosY + (this.target.posY - this.target.lastTickPosY) * partialTicks + this.yOffset;
        final double d6 = this.target.lastTickPosZ + (this.target.posZ - this.target.lastTickPosZ) * partialTicks;
        double d7 = d0 + (d4 - d0) * f;
        double d8 = d2 + (d5 - d2) * f;
        double d9 = d3 + (d6 - d3) * f;
        final int j = this.getBrightnessForRender(partialTicks);
        final int k = j % 65536;
        final int l = j / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)k, (float)l);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        d7 -= ParticleItemPickup.interpPosX;
        d8 -= ParticleItemPickup.interpPosY;
        d9 -= ParticleItemPickup.interpPosZ;
        GlStateManager.enableLighting();
        this.renderManager.doRenderEntity(this.item, d7, d8, d9, this.item.rotationYaw, partialTicks, false);
        if (Config.isShaders()) {
            Shaders.useProgram(i);
        }
    }
    
    @Override
    public void onUpdate() {
        ++this.age;
        if (this.age == this.maxAge) {
            this.setExpired();
        }
    }
    
    @Override
    public int getFXLayer() {
        return 3;
    }
}
