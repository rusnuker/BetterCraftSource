// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.renderer.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.block.Block;
import java.awt.Color;
import me.nzxtercode.bettercraft.client.utils.OutlineUtils;
import me.nzxtercode.bettercraft.client.gui.section.GuiMisc;
import net.minecraft.block.BlockChest;
import net.minecraft.client.renderer.GlStateManager;
import java.util.Calendar;
import net.minecraft.client.model.ModelLargeChest;
import net.minecraft.client.model.ModelChest;
import net.minecraft.util.ResourceLocation;
import net.minecraft.tileentity.TileEntityChest;

public class TileEntityChestRenderer extends TileEntitySpecialRenderer<TileEntityChest>
{
    private static final ResourceLocation textureTrappedDouble;
    private static final ResourceLocation textureChristmasDouble;
    private static final ResourceLocation textureNormalDouble;
    private static final ResourceLocation textureTrapped;
    private static final ResourceLocation textureChristmas;
    private static final ResourceLocation textureNormal;
    private ModelChest simpleChest;
    private ModelChest largeChest;
    private boolean isChristmas;
    
    static {
        textureTrappedDouble = new ResourceLocation("textures/entity/chest/trapped_double.png");
        textureChristmasDouble = new ResourceLocation("textures/entity/chest/christmas_double.png");
        textureNormalDouble = new ResourceLocation("textures/entity/chest/normal_double.png");
        textureTrapped = new ResourceLocation("textures/entity/chest/trapped.png");
        textureChristmas = new ResourceLocation("textures/entity/chest/christmas.png");
        textureNormal = new ResourceLocation("textures/entity/chest/normal.png");
    }
    
    public TileEntityChestRenderer() {
        this.simpleChest = new ModelChest();
        this.largeChest = new ModelLargeChest();
        final Calendar calendar = Calendar.getInstance();
        if (calendar.get(2) + 1 == 12 && calendar.get(5) >= 24 && calendar.get(5) <= 26) {
            this.isChristmas = true;
        }
    }
    
    @Override
    public void renderTileEntityAt(final TileEntityChest te, final double x, final double y, final double z, final float partialTicks, final int destroyStage) {
        GlStateManager.enableDepth();
        GlStateManager.depthFunc(515);
        GlStateManager.depthMask(true);
        int i;
        if (!te.hasWorldObj()) {
            i = 0;
        }
        else {
            final Block block = te.getBlockType();
            i = te.getBlockMetadata();
            if (block instanceof BlockChest && i == 0) {
                ((BlockChest)block).checkForSurroundingChests(te.getWorld(), te.getPos(), te.getWorld().getBlockState(te.getPos()));
                i = te.getBlockMetadata();
            }
            te.checkForAdjacentChests();
        }
        if (te.adjacentChestZNeg == null && te.adjacentChestXNeg == null) {
            ModelChest modelchest;
            if (te.adjacentChestXPos == null && te.adjacentChestZPos == null) {
                modelchest = this.simpleChest;
                if (destroyStage >= 0) {
                    this.bindTexture(TileEntityChestRenderer.DESTROY_STAGES[destroyStage]);
                    GlStateManager.matrixMode(5890);
                    GlStateManager.pushMatrix();
                    GlStateManager.scale(4.0f, 4.0f, 1.0f);
                    GlStateManager.translate(0.0625f, 0.0625f, 0.0625f);
                    GlStateManager.matrixMode(5888);
                }
                else if (this.isChristmas) {
                    this.bindTexture(TileEntityChestRenderer.textureChristmas);
                }
                else if (te.getChestType() == 1) {
                    this.bindTexture(TileEntityChestRenderer.textureTrapped);
                }
                else {
                    this.bindTexture(TileEntityChestRenderer.textureNormal);
                }
            }
            else {
                modelchest = this.largeChest;
                if (destroyStage >= 0) {
                    this.bindTexture(TileEntityChestRenderer.DESTROY_STAGES[destroyStage]);
                    GlStateManager.matrixMode(5890);
                    GlStateManager.pushMatrix();
                    GlStateManager.scale(8.0f, 4.0f, 1.0f);
                    GlStateManager.translate(0.0625f, 0.0625f, 0.0625f);
                    GlStateManager.matrixMode(5888);
                }
                else if (this.isChristmas) {
                    this.bindTexture(TileEntityChestRenderer.textureChristmasDouble);
                }
                else if (te.getChestType() == 1) {
                    this.bindTexture(TileEntityChestRenderer.textureTrappedDouble);
                }
                else {
                    this.bindTexture(TileEntityChestRenderer.textureNormalDouble);
                }
            }
            GlStateManager.pushMatrix();
            GlStateManager.enableRescaleNormal();
            if (destroyStage < 0) {
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            }
            GlStateManager.translate((float)x, (float)y + 1.0f, (float)z + 1.0f);
            GlStateManager.scale(1.0f, -1.0f, -1.0f);
            GlStateManager.translate(0.5f, 0.5f, 0.5f);
            int j = 0;
            if (i == 2) {
                j = 180;
            }
            if (i == 3) {
                j = 0;
            }
            if (i == 4) {
                j = 90;
            }
            if (i == 5) {
                j = -90;
            }
            if (i == 2 && te.adjacentChestXPos != null) {
                GlStateManager.translate(1.0f, 0.0f, 0.0f);
            }
            if (i == 5 && te.adjacentChestZPos != null) {
                GlStateManager.translate(0.0f, 0.0f, -1.0f);
            }
            GlStateManager.rotate((float)j, 0.0f, 1.0f, 0.0f);
            GlStateManager.translate(-0.5f, -0.5f, -0.5f);
            float f = te.prevLidAngle + (te.lidAngle - te.prevLidAngle) * partialTicks;
            if (te.adjacentChestZNeg != null) {
                final float f2 = te.adjacentChestZNeg.prevLidAngle + (te.adjacentChestZNeg.lidAngle - te.adjacentChestZNeg.prevLidAngle) * partialTicks;
                if (f2 > f) {
                    f = f2;
                }
            }
            if (te.adjacentChestXNeg != null) {
                final float f3 = te.adjacentChestXNeg.prevLidAngle + (te.adjacentChestXNeg.lidAngle - te.adjacentChestXNeg.prevLidAngle) * partialTicks;
                if (f3 > f) {
                    f = f3;
                }
            }
            f = 1.0f - f;
            f = 1.0f - f * f * f;
            modelchest.chestLid.rotateAngleX = -(f * 3.1415927f / 2.0f);
            modelchest.renderAll();
            if (GuiMisc.enabledESP[2]) {
                modelchest.renderAll();
                OutlineUtils.renderOne();
                modelchest.renderAll();
                OutlineUtils.renderTwo();
                modelchest.renderAll();
                OutlineUtils.renderThree();
                OutlineUtils.setColor(new Color(0.0f, 0.0f, 0.0f, 0.1f));
                OutlineUtils.renderFour();
                modelchest.renderAll();
                OutlineUtils.renderFive();
            }
            GlStateManager.disableRescaleNormal();
            GlStateManager.popMatrix();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            if (destroyStage >= 0) {
                GlStateManager.matrixMode(5890);
                GlStateManager.popMatrix();
                GlStateManager.matrixMode(5888);
            }
        }
    }
}
