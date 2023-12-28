// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.renderer.tileentity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;
import net.minecraft.crash.CrashReport;
import net.minecraft.util.math.BlockPos;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import optifine.Reflector;
import net.minecraft.client.renderer.RenderHelper;
import javax.annotation.Nullable;
import java.util.Iterator;
import net.minecraft.tileentity.TileEntityBed;
import net.minecraft.client.model.ModelShulker;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.tileentity.TileEntityStructure;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.tileentity.TileEntityEndGateway;
import net.minecraft.tileentity.TileEntityEndPortal;
import net.minecraft.tileentity.TileEntityEnchantmentTable;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityPiston;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.tileentity.TileEntitySign;
import com.google.common.collect.Maps;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.gui.FontRenderer;
import java.util.Map;

public class TileEntityRendererDispatcher
{
    public final Map<Class, TileEntitySpecialRenderer> mapSpecialRenderers;
    public static TileEntityRendererDispatcher instance;
    public FontRenderer fontRenderer;
    public static double staticPlayerX;
    public static double staticPlayerY;
    public static double staticPlayerZ;
    public TextureManager renderEngine;
    public World worldObj;
    public Entity entity;
    public float entityYaw;
    public float entityPitch;
    public RayTraceResult cameraHitResult;
    public double entityX;
    public double entityY;
    public double entityZ;
    public TileEntity tileEntityRendered;
    private Tessellator batchBuffer;
    private boolean drawingBatch;
    
    static {
        TileEntityRendererDispatcher.instance = new TileEntityRendererDispatcher();
    }
    
    private TileEntityRendererDispatcher() {
        this.mapSpecialRenderers = (Map<Class, TileEntitySpecialRenderer>)Maps.newHashMap();
        this.batchBuffer = new Tessellator(2097152);
        this.drawingBatch = false;
        this.mapSpecialRenderers.put(TileEntitySign.class, new TileEntitySignRenderer());
        this.mapSpecialRenderers.put(TileEntityMobSpawner.class, new TileEntityMobSpawnerRenderer());
        this.mapSpecialRenderers.put(TileEntityPiston.class, new TileEntityPistonRenderer());
        this.mapSpecialRenderers.put(TileEntityChest.class, new TileEntityChestRenderer());
        this.mapSpecialRenderers.put(TileEntityEnderChest.class, new TileEntityEnderChestRenderer());
        this.mapSpecialRenderers.put(TileEntityEnchantmentTable.class, new TileEntityEnchantmentTableRenderer());
        this.mapSpecialRenderers.put(TileEntityEndPortal.class, new TileEntityEndPortalRenderer());
        this.mapSpecialRenderers.put(TileEntityEndGateway.class, new TileEntityEndGatewayRenderer());
        this.mapSpecialRenderers.put(TileEntityBeacon.class, new TileEntityBeaconRenderer());
        this.mapSpecialRenderers.put(TileEntitySkull.class, new TileEntitySkullRenderer());
        this.mapSpecialRenderers.put(TileEntityBanner.class, new TileEntityBannerRenderer());
        this.mapSpecialRenderers.put(TileEntityStructure.class, new TileEntityStructureRenderer());
        this.mapSpecialRenderers.put(TileEntityShulkerBox.class, new TileEntityShulkerBoxRenderer(new ModelShulker()));
        this.mapSpecialRenderers.put(TileEntityBed.class, new TileEntityBedRenderer());
        for (final TileEntitySpecialRenderer<?> tileentityspecialrenderer : this.mapSpecialRenderers.values()) {
            tileentityspecialrenderer.setRendererDispatcher(this);
        }
    }
    
    public <T extends TileEntity> TileEntitySpecialRenderer<T> getSpecialRendererByClass(final Class<? extends TileEntity> teClass) {
        TileEntitySpecialRenderer<T> tileentityspecialrenderer = this.mapSpecialRenderers.get(teClass);
        if (tileentityspecialrenderer == null && teClass != TileEntity.class) {
            tileentityspecialrenderer = (TileEntitySpecialRenderer<T>)this.getSpecialRendererByClass((Class<? extends TileEntity>)teClass.getSuperclass());
            this.mapSpecialRenderers.put(teClass, tileentityspecialrenderer);
        }
        return tileentityspecialrenderer;
    }
    
    @Nullable
    public <T extends TileEntity> TileEntitySpecialRenderer<T> getSpecialRenderer(@Nullable final TileEntity tileEntityIn) {
        return (tileEntityIn == null) ? null : this.getSpecialRendererByClass(tileEntityIn.getClass());
    }
    
    public void prepare(final World p_190056_1_, final TextureManager p_190056_2_, final FontRenderer p_190056_3_, final Entity p_190056_4_, final RayTraceResult p_190056_5_, final float p_190056_6_) {
        if (this.worldObj != p_190056_1_) {
            this.setWorld(p_190056_1_);
        }
        this.renderEngine = p_190056_2_;
        this.entity = p_190056_4_;
        this.fontRenderer = p_190056_3_;
        this.cameraHitResult = p_190056_5_;
        this.entityYaw = p_190056_4_.prevRotationYaw + (p_190056_4_.rotationYaw - p_190056_4_.prevRotationYaw) * p_190056_6_;
        this.entityPitch = p_190056_4_.prevRotationPitch + (p_190056_4_.rotationPitch - p_190056_4_.prevRotationPitch) * p_190056_6_;
        this.entityX = p_190056_4_.lastTickPosX + (p_190056_4_.posX - p_190056_4_.lastTickPosX) * p_190056_6_;
        this.entityY = p_190056_4_.lastTickPosY + (p_190056_4_.posY - p_190056_4_.lastTickPosY) * p_190056_6_;
        this.entityZ = p_190056_4_.lastTickPosZ + (p_190056_4_.posZ - p_190056_4_.lastTickPosZ) * p_190056_6_;
    }
    
    public void renderTileEntity(final TileEntity tileentityIn, final float partialTicks, final int destroyStage) {
        if (tileentityIn.getDistanceSq(this.entityX, this.entityY, this.entityZ) < tileentityIn.getMaxRenderDistanceSquared()) {
            RenderHelper.enableStandardItemLighting();
            boolean flag = true;
            if (Reflector.ForgeTileEntity_hasFastRenderer.exists()) {
                flag = (!this.drawingBatch || !Reflector.callBoolean(tileentityIn, Reflector.ForgeTileEntity_hasFastRenderer, new Object[0]));
            }
            if (flag) {
                final int i = this.worldObj.getCombinedLight(tileentityIn.getPos(), 0);
                final int j = i % 65536;
                final int k = i / 65536;
                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j, (float)k);
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            }
            final BlockPos blockpos = tileentityIn.getPos();
            this.func_192854_a(tileentityIn, blockpos.getX() - TileEntityRendererDispatcher.staticPlayerX, blockpos.getY() - TileEntityRendererDispatcher.staticPlayerY, blockpos.getZ() - TileEntityRendererDispatcher.staticPlayerZ, partialTicks, destroyStage, 1.0f);
        }
    }
    
    public void renderTileEntityAt(final TileEntity tileEntityIn, final double x, final double y, final double z, final float partialTicks) {
        this.func_192855_a(tileEntityIn, x, y, z, partialTicks, 1.0f);
    }
    
    public void func_192855_a(final TileEntity p_192855_1_, final double p_192855_2_, final double p_192855_4_, final double p_192855_6_, final float p_192855_8_, final float p_192855_9_) {
        this.func_192854_a(p_192855_1_, p_192855_2_, p_192855_4_, p_192855_6_, p_192855_8_, -1, p_192855_9_);
    }
    
    public void func_192854_a(final TileEntity p_192854_1_, final double p_192854_2_, final double p_192854_4_, final double p_192854_6_, final float p_192854_8_, final int p_192854_9_, final float p_192854_10_) {
        final TileEntitySpecialRenderer<TileEntity> tileentityspecialrenderer = this.getSpecialRenderer(p_192854_1_);
        if (tileentityspecialrenderer != null) {
            try {
                this.tileEntityRendered = p_192854_1_;
                if (this.drawingBatch && Reflector.callBoolean(p_192854_1_, Reflector.ForgeTileEntity_hasFastRenderer, new Object[0])) {
                    tileentityspecialrenderer.renderTileEntityFast(p_192854_1_, p_192854_2_, p_192854_4_, p_192854_6_, p_192854_8_, p_192854_9_, p_192854_10_, this.batchBuffer.getBuffer());
                }
                else {
                    tileentityspecialrenderer.func_192841_a(p_192854_1_, p_192854_2_, p_192854_4_, p_192854_6_, p_192854_8_, p_192854_9_, p_192854_10_);
                }
                this.tileEntityRendered = null;
            }
            catch (final Throwable throwable) {
                final CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Rendering Block Entity");
                final CrashReportCategory crashreportcategory = crashreport.makeCategory("Block Entity Details");
                p_192854_1_.addInfoToCrashReport(crashreportcategory);
                throw new ReportedException(crashreport);
            }
        }
    }
    
    public void setWorld(@Nullable final World worldIn) {
        this.worldObj = worldIn;
        if (worldIn == null) {
            this.entity = null;
        }
    }
    
    public FontRenderer getFontRenderer() {
        return this.fontRenderer;
    }
    
    public void preDrawBatch() {
        this.batchBuffer.getBuffer().begin(7, DefaultVertexFormats.BLOCK);
        this.drawingBatch = true;
    }
    
    public void drawBatch(final int p_drawBatch_1_) {
        this.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.blendFunc(770, 771);
        GlStateManager.enableBlend();
        GlStateManager.disableCull();
        if (Minecraft.isAmbientOcclusionEnabled()) {
            GlStateManager.shadeModel(7425);
        }
        else {
            GlStateManager.shadeModel(7424);
        }
        if (p_drawBatch_1_ > 0) {
            this.batchBuffer.getBuffer().sortVertexData(0.0f, 0.0f, 0.0f);
        }
        this.batchBuffer.draw();
        RenderHelper.enableStandardItemLighting();
        this.drawingBatch = false;
    }
}
