// 
// Decompiled by Procyon v0.6.0
// 

package optifine;

import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.client.Minecraft;

public class CloudRenderer
{
    private Minecraft mc;
    private boolean updated;
    private boolean renderFancy;
    int cloudTickCounter;
    private Vec3d cloudColor;
    float partialTicks;
    private boolean updateRenderFancy;
    private int updateCloudTickCounter;
    private Vec3d updateCloudColor;
    private double updatePlayerX;
    private double updatePlayerY;
    private double updatePlayerZ;
    private int glListClouds;
    
    public CloudRenderer(final Minecraft p_i23_1_) {
        this.updated = false;
        this.renderFancy = false;
        this.updateRenderFancy = false;
        this.updateCloudTickCounter = 0;
        this.updateCloudColor = new Vec3d(-1.0, -1.0, -1.0);
        this.updatePlayerX = 0.0;
        this.updatePlayerY = 0.0;
        this.updatePlayerZ = 0.0;
        this.glListClouds = -1;
        this.mc = p_i23_1_;
        this.glListClouds = GLAllocation.generateDisplayLists(1);
    }
    
    public void prepareToRender(final boolean p_prepareToRender_1_, final int p_prepareToRender_2_, final float p_prepareToRender_3_, final Vec3d p_prepareToRender_4_) {
        this.renderFancy = p_prepareToRender_1_;
        this.cloudTickCounter = p_prepareToRender_2_;
        this.partialTicks = p_prepareToRender_3_;
        this.cloudColor = p_prepareToRender_4_;
    }
    
    public boolean shouldUpdateGlList() {
        if (!this.updated) {
            return true;
        }
        if (this.renderFancy != this.updateRenderFancy) {
            return true;
        }
        if (this.cloudTickCounter >= this.updateCloudTickCounter + 20) {
            return true;
        }
        if (Math.abs(this.cloudColor.xCoord - this.updateCloudColor.xCoord) > 0.003) {
            return true;
        }
        if (Math.abs(this.cloudColor.yCoord - this.updateCloudColor.yCoord) > 0.003) {
            return true;
        }
        if (Math.abs(this.cloudColor.zCoord - this.updateCloudColor.zCoord) > 0.003) {
            return true;
        }
        final Entity entity = this.mc.getRenderViewEntity();
        final boolean flag = this.updatePlayerY + entity.getEyeHeight() < 128.0 + this.mc.gameSettings.ofCloudsHeight * 128.0f;
        final boolean flag2 = entity.prevPosY + entity.getEyeHeight() < 128.0 + this.mc.gameSettings.ofCloudsHeight * 128.0f;
        return flag2 ^ flag;
    }
    
    public void startUpdateGlList() {
        GL11.glNewList(this.glListClouds, 4864);
    }
    
    public void endUpdateGlList() {
        GL11.glEndList();
        this.updateRenderFancy = this.renderFancy;
        this.updateCloudTickCounter = this.cloudTickCounter;
        this.updateCloudColor = this.cloudColor;
        this.updatePlayerX = this.mc.getRenderViewEntity().prevPosX;
        this.updatePlayerY = this.mc.getRenderViewEntity().prevPosY;
        this.updatePlayerZ = this.mc.getRenderViewEntity().prevPosZ;
        this.updated = true;
        GlStateManager.resetColor();
    }
    
    public void renderGlList() {
        final Entity entity = this.mc.getRenderViewEntity();
        final double d0 = entity.prevPosX + (entity.posX - entity.prevPosX) * this.partialTicks;
        final double d2 = entity.prevPosY + (entity.posY - entity.prevPosY) * this.partialTicks;
        final double d3 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * this.partialTicks;
        final double d4 = this.cloudTickCounter - this.updateCloudTickCounter + this.partialTicks;
        final float f = (float)(d0 - this.updatePlayerX + d4 * 0.03);
        final float f2 = (float)(d2 - this.updatePlayerY);
        final float f3 = (float)(d3 - this.updatePlayerZ);
        GlStateManager.pushMatrix();
        if (this.renderFancy) {
            GlStateManager.translate(-f / 12.0f, -f2, -f3 / 12.0f);
        }
        else {
            GlStateManager.translate(-f, -f2, -f3);
        }
        GlStateManager.callList(this.glListClouds);
        GlStateManager.popMatrix();
        GlStateManager.resetColor();
    }
    
    public void reset() {
        this.updated = false;
    }
}
