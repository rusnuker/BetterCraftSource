// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.core_implementation.mc18.render;

import net.minecraft.util.Vec3;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;
import net.minecraft.crash.CrashReport;
import net.labymod.main.LabyMod;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import java.util.Iterator;
import java.lang.reflect.Field;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import java.util.Map;
import net.labymod.utils.ReflectionHelper;
import net.labymod.core.LabyModCore;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.entity.RenderManager;

public class RenderManagerCustom extends RenderManager
{
    private double renderPosX;
    private double renderPosY;
    private double renderPosZ;
    private boolean debugBoundingBox;
    
    public RenderManagerCustom(final TextureManager renderEngineIn, final RenderItem itemRendererIn) {
        super(renderEngineIn, itemRendererIn);
        final Field renderManagerRenderField = ReflectionHelper.findField(Render.class, LabyModCore.getMappingAdapter().getRenderManagerRenderMappings());
        try {
            final Map<Class<? extends Entity>, Render<? extends Entity>> map = (Map<Class<? extends Entity>, Render<? extends Entity>>)ReflectionHelper.findField(RenderManager.class, LabyModCore.getMappingAdapter().getEntityRenderMapMappings()).get(this);
            for (final Map.Entry<Class<? extends Entity>, Render<? extends Entity>> entityRenderEntry : map.entrySet()) {
                renderManagerRenderField.set(entityRenderEntry.getValue(), this);
            }
        }
        catch (final IllegalAccessException e) {
            e.printStackTrace();
        }
        try {
            final Map<String, RenderPlayer> map2 = (Map<String, RenderPlayer>)ReflectionHelper.findField(RenderManager.class, LabyModCore.getMappingAdapter().getSkinMapMappings()).get(this);
            for (final Map.Entry<String, RenderPlayer> skinEntry : map2.entrySet()) {
                renderManagerRenderField.set(skinEntry.getValue(), this);
            }
        }
        catch (final IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public boolean isDebugBoundingBox() {
        return this.debugBoundingBox;
    }
    
    @Override
    public void setDebugBoundingBox(final boolean debugBoundingBoxIn) {
        this.debugBoundingBox = debugBoundingBoxIn;
    }
    
    @Override
    public boolean renderEntityWithPosYaw(final Entity entityIn, final double x, final double y, final double z, final float entityYaw, final float partialTicks) {
        return this.doRenderEntity(entityIn, x, y, z, entityYaw, partialTicks, false);
    }
    
    @Override
    public boolean renderEntitySimple(final Entity entityIn, final float partialTicks) {
        return this.renderEntityStatic(entityIn, partialTicks, false);
    }
    
    @Override
    public boolean renderEntityStatic(final Entity entity, final float partialTicks, final boolean p_147936_3_) {
        if (entity.ticksExisted == 0) {
            entity.lastTickPosX = entity.posX;
            entity.lastTickPosY = entity.posY;
            entity.lastTickPosZ = entity.posZ;
        }
        final double d0 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks;
        final double d2 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks;
        final double d3 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks;
        final float f = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks;
        int i = entity.getBrightnessForRender(partialTicks);
        if (entity.isBurning()) {
            i = 15728880;
        }
        final int j = i % 65536;
        final int k = i / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, j / 1.0f, k / 1.0f);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        return this.doRenderEntity(entity, d0 - this.renderPosX, d2 - this.renderPosY, d3 - this.renderPosZ, f, partialTicks, p_147936_3_);
    }
    
    @Override
    public void setRenderPosition(final double renderPosXIn, final double renderPosYIn, final double renderPosZIn) {
        this.renderPosX = renderPosXIn;
        this.renderPosY = renderPosYIn;
        this.renderPosZ = renderPosZIn;
    }
    
    @Override
    public boolean doRenderEntity(final Entity entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks, final boolean p_147939_10_) {
        try {
            super.doRenderEntity(entity, x, y, z, entityYaw, partialTicks, p_147939_10_);
        }
        catch (final ClassCastException e) {
            e.printStackTrace();
        }
        LabyMod.getInstance().getEventManager().callRenderEntity(entity, x, y, z, partialTicks);
        if (this.debugBoundingBox) {
            Render<Entity> render = null;
            try {
                render = this.getEntityRenderObject(entity);
                if (render != null && this.renderEngine != null) {
                    if (entity.isInvisible() || p_147939_10_) {
                        return true;
                    }
                    try {
                        this.renderDebugBoundingBox(entity, x, y, z, entityYaw, partialTicks);
                        return true;
                    }
                    catch (final Throwable throwable) {
                        throw new ReportedException(CrashReport.makeCrashReport(throwable, "Rendering entity hitbox in world"));
                    }
                }
                return this.renderEngine == null;
            }
            catch (final Throwable throwable2) {
                final CrashReport crashreport = CrashReport.makeCrashReport(throwable2, "Rendering entity in world");
                final CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity being rendered");
                entity.addEntityCrashInfo(crashreportcategory);
                final CrashReportCategory crashreportcategory2 = crashreport.makeCategory("Renderer details");
                crashreportcategory2.addCrashSection("Assigned renderer", render);
                crashreportcategory2.addCrashSection("Location", CrashReportCategory.getCoordinateInfo(x, y, z));
                crashreportcategory2.addCrashSection("Rotation", entityYaw);
                crashreportcategory2.addCrashSection("Delta", partialTicks);
                throw new ReportedException(crashreport);
            }
        }
        return true;
    }
    
    private void renderDebugBoundingBox(final Entity entityIn, final double p_85094_2_, final double p_85094_4_, final double p_85094_6_, final float p_85094_8_, final float p_85094_9_) {
        GlStateManager.depthMask(false);
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        GlStateManager.disableBlend();
        final float f = entityIn.width / 2.0f;
        final AxisAlignedBB axisalignedbb = entityIn.getEntityBoundingBox();
        final AxisAlignedBB axisalignedbb2 = new AxisAlignedBB(axisalignedbb.minX - entityIn.posX + p_85094_2_, axisalignedbb.minY - entityIn.posY + p_85094_4_, axisalignedbb.minZ - entityIn.posZ + p_85094_6_, axisalignedbb.maxX - entityIn.posX + p_85094_2_, axisalignedbb.maxY - entityIn.posY + p_85094_4_, axisalignedbb.maxZ - entityIn.posZ + p_85094_6_);
        RenderGlobal.drawOutlinedBoundingBox(axisalignedbb2, 255, 255, 255, 255);
        if (entityIn instanceof EntityLivingBase && !LabyMod.getSettings().oldHitbox) {
            RenderGlobal.drawOutlinedBoundingBox(new AxisAlignedBB(p_85094_2_ - f, p_85094_4_ + entityIn.getEyeHeight() - 0.009999999776482582, p_85094_6_ - f, p_85094_2_ + f, p_85094_4_ + entityIn.getEyeHeight() + 0.009999999776482582, p_85094_6_ + f), 255, 0, 0, 255);
        }
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        if (!LabyMod.getSettings().oldHitbox) {
            final Vec3 vec3 = entityIn.getLook(p_85094_9_);
            worldrenderer.begin(3, DefaultVertexFormats.POSITION_COLOR);
            worldrenderer.pos(p_85094_2_, p_85094_4_ + entityIn.getEyeHeight(), p_85094_6_).color(0, 0, 255, 255).endVertex();
            worldrenderer.pos(p_85094_2_ + vec3.xCoord * 2.0, p_85094_4_ + entityIn.getEyeHeight() + vec3.yCoord * 2.0, p_85094_6_ + vec3.zCoord * 2.0).color(0, 0, 255, 255).endVertex();
            tessellator.draw();
        }
        GlStateManager.enableTexture2D();
        GlStateManager.enableLighting();
        GlStateManager.enableCull();
        GlStateManager.disableBlend();
        GlStateManager.depthMask(true);
    }
}
