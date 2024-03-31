// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.world.storage.MapData;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.RenderHelper;
import optifine.ReflectorForge;
import net.minecraft.init.Items;
import optifine.Reflector;
import optifine.Config;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.util.math.BlockPos;
import net.minecraft.item.ItemMap;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.item.EntityItemFrame;

public class RenderItemFrame extends Render<EntityItemFrame>
{
    private static final ResourceLocation MAP_BACKGROUND_TEXTURES;
    private final Minecraft mc;
    private final ModelResourceLocation itemFrameModel;
    private final ModelResourceLocation mapModel;
    private final RenderItem itemRenderer;
    
    static {
        MAP_BACKGROUND_TEXTURES = new ResourceLocation("textures/map/map_background.png");
    }
    
    public RenderItemFrame(final RenderManager renderManagerIn, final RenderItem itemRendererIn) {
        super(renderManagerIn);
        this.mc = Minecraft.getMinecraft();
        this.itemFrameModel = new ModelResourceLocation("item_frame", "normal");
        this.mapModel = new ModelResourceLocation("item_frame", "map");
        this.itemRenderer = itemRendererIn;
    }
    
    @Override
    public void doRender(final EntityItemFrame entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks) {
        GlStateManager.pushMatrix();
        final BlockPos blockpos = entity.getHangingPosition();
        final double d0 = blockpos.getX() - entity.posX + x;
        final double d2 = blockpos.getY() - entity.posY + y;
        final double d3 = blockpos.getZ() - entity.posZ + z;
        GlStateManager.translate(d0 + 0.5, d2 + 0.5, d3 + 0.5);
        GlStateManager.rotate(180.0f - entity.rotationYaw, 0.0f, 1.0f, 0.0f);
        this.renderManager.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        final BlockRendererDispatcher blockrendererdispatcher = this.mc.getBlockRendererDispatcher();
        final ModelManager modelmanager = blockrendererdispatcher.getBlockModelShapes().getModelManager();
        IBakedModel ibakedmodel;
        if (entity.getDisplayedItem().getItem() instanceof ItemMap) {
            ibakedmodel = modelmanager.getModel(this.mapModel);
        }
        else {
            ibakedmodel = modelmanager.getModel(this.itemFrameModel);
        }
        GlStateManager.pushMatrix();
        GlStateManager.translate(-0.5f, -0.5f, -0.5f);
        if (this.renderOutlines) {
            GlStateManager.enableColorMaterial();
            GlStateManager.enableOutlineMode(this.getTeamColor(entity));
        }
        blockrendererdispatcher.getBlockModelRenderer().renderModelBrightnessColor(ibakedmodel, 1.0f, 1.0f, 1.0f, 1.0f);
        if (this.renderOutlines) {
            GlStateManager.disableOutlineMode();
            GlStateManager.disableColorMaterial();
        }
        GlStateManager.popMatrix();
        GlStateManager.translate(0.0f, 0.0f, 0.4375f);
        this.renderItem(entity);
        GlStateManager.popMatrix();
        this.renderName(entity, x + entity.facingDirection.getFrontOffsetX() * 0.3f, y - 0.25, z + entity.facingDirection.getFrontOffsetZ() * 0.3f);
    }
    
    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(final EntityItemFrame entity) {
        return null;
    }
    
    private void renderItem(final EntityItemFrame itemFrame) {
        final ItemStack itemstack = itemFrame.getDisplayedItem();
        if (!itemstack.func_190926_b()) {
            if (!Config.zoomMode) {
                final Entity entity = this.mc.player;
                final double d0 = itemFrame.getDistanceSq(entity.posX, entity.posY, entity.posZ);
                if (d0 > 4096.0) {
                    return;
                }
            }
            GlStateManager.pushMatrix();
            GlStateManager.disableLighting();
            final boolean flag = itemstack.getItem() instanceof ItemMap;
            final int i = flag ? (itemFrame.getRotation() % 4 * 2) : itemFrame.getRotation();
            GlStateManager.rotate(i * 360.0f / 8.0f, 0.0f, 0.0f, 1.0f);
            if (!Reflector.postForgeBusEvent(Reflector.RenderItemInFrameEvent_Constructor, itemFrame, this)) {
                if (flag) {
                    this.renderManager.renderEngine.bindTexture(RenderItemFrame.MAP_BACKGROUND_TEXTURES);
                    GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
                    final float f = 0.0078125f;
                    GlStateManager.scale(0.0078125f, 0.0078125f, 0.0078125f);
                    GlStateManager.translate(-64.0f, -64.0f, 0.0f);
                    final MapData mapdata = ReflectorForge.getMapData(Items.FILLED_MAP, itemstack, itemFrame.world);
                    GlStateManager.translate(0.0f, 0.0f, -1.0f);
                    if (mapdata != null) {
                        this.mc.entityRenderer.getMapItemRenderer().renderMap(mapdata, true);
                    }
                }
                else {
                    GlStateManager.scale(0.5f, 0.5f, 0.5f);
                    GlStateManager.pushAttrib();
                    RenderHelper.enableStandardItemLighting();
                    this.itemRenderer.renderItem(itemstack, ItemCameraTransforms.TransformType.FIXED);
                    RenderHelper.disableStandardItemLighting();
                    GlStateManager.popAttrib();
                }
            }
            GlStateManager.enableLighting();
            GlStateManager.popMatrix();
        }
    }
    
    @Override
    protected void renderName(final EntityItemFrame entity, final double x, final double y, final double z) {
        if (Minecraft.isGuiEnabled() && !entity.getDisplayedItem().func_190926_b() && entity.getDisplayedItem().hasDisplayName() && this.renderManager.pointedEntity == entity) {
            final double d0 = entity.getDistanceSqToEntity(this.renderManager.renderViewEntity);
            final float f = entity.isSneaking() ? 32.0f : 64.0f;
            if (d0 < f * f) {
                final String s = entity.getDisplayedItem().getDisplayName();
                this.renderLivingLabel(entity, s, x, y, z, 64);
            }
        }
    }
}
