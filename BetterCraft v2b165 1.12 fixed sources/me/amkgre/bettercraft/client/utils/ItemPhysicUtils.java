// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.utils;

import net.minecraft.item.Item;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.Minecraft;
import java.util.Random;

public class ItemPhysicUtils
{
    public static Random random;
    public static Minecraft mc;
    public static RenderItem renderItem;
    public static long tick;
    public static double rotation;
    public static final ResourceLocation RES_ITEM_GLINT;
    
    static {
        ItemPhysicUtils.random = new Random();
        ItemPhysicUtils.mc = Minecraft.getMinecraft();
        ItemPhysicUtils.renderItem = ItemPhysicUtils.mc.getRenderItem();
        RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");
    }
    
    public static void doRender(final Entity par1Entity, final double x, final double y, final double z, final float par8, final float par9) {
        ItemPhysicUtils.rotation = (System.nanoTime() - ItemPhysicUtils.tick) / 3000000.0 * 1.0;
        if (!ItemPhysicUtils.mc.inGameHasFocus) {
            ItemPhysicUtils.rotation = 0.0;
        }
        final EntityItem entityitem = (EntityItem)par1Entity;
        final ItemStack itemstack = entityitem.getEntityItem();
        if (itemstack.getItem() != null) {
            ItemPhysicUtils.random.setSeed(187L);
            boolean flag = false;
            if (TextureMap.LOCATION_BLOCKS_TEXTURE != null) {
                ItemPhysicUtils.mc.getRenderManager().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
                ItemPhysicUtils.mc.getRenderManager().renderEngine.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
                flag = true;
            }
            GlStateManager.enableRescaleNormal();
            GlStateManager.alphaFunc(516, 0.1f);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.pushMatrix();
            final IBakedModel ibakedmodel = ItemPhysicUtils.renderItem.getItemModelMesher().getItemModel(itemstack);
            final int i = func_177077_a(entityitem, x, y, z, par9, ibakedmodel);
            final BlockPos blockpos = new BlockPos(entityitem);
            if (entityitem.rotationPitch > 360.0f) {
                entityitem.rotationPitch = 0.0f;
            }
            if (entityitem != null && !Double.isNaN(entityitem.posX) && !Double.isNaN(entityitem.posY) && !Double.isNaN(entityitem.posZ) && entityitem.world != null) {
                if (entityitem.onGround) {
                    if (entityitem.rotationPitch != 0.0f && entityitem.rotationPitch != 90.0f && entityitem.rotationPitch != 180.0f && entityitem.rotationPitch != 270.0f) {
                        final double d0 = formPositiv(entityitem.rotationPitch);
                        final double d2 = formPositiv(entityitem.rotationPitch - 90.0f);
                        final double d3 = formPositiv(entityitem.rotationPitch - 180.0f);
                        final double d4 = formPositiv(entityitem.rotationPitch - 270.0f);
                        if (d0 <= d2 && d0 <= d3 && d0 <= d4) {
                            if (entityitem.rotationPitch < 0.0f) {
                                entityitem.rotationPitch += (float)ItemPhysicUtils.rotation;
                            }
                            else {
                                entityitem.rotationPitch -= (float)ItemPhysicUtils.rotation;
                            }
                        }
                        if (d2 < d0 && d2 <= d3 && d2 <= d4) {
                            if (entityitem.rotationPitch - 90.0f < 0.0f) {
                                entityitem.rotationPitch += (float)ItemPhysicUtils.rotation;
                            }
                            else {
                                entityitem.rotationPitch -= (float)ItemPhysicUtils.rotation;
                            }
                        }
                        if (d3 < d2 && d3 < d0 && d3 <= d4) {
                            if (entityitem.rotationPitch - 180.0f < 0.0f) {
                                entityitem.rotationPitch += (float)ItemPhysicUtils.rotation;
                            }
                            else {
                                entityitem.rotationPitch -= (float)ItemPhysicUtils.rotation;
                            }
                        }
                        if (d4 < d2 && d4 < d3 && d4 < d0) {
                            if (entityitem.rotationPitch - 270.0f < 0.0f) {
                                entityitem.rotationPitch += (float)ItemPhysicUtils.rotation;
                            }
                            else {
                                entityitem.rotationPitch -= (float)ItemPhysicUtils.rotation;
                            }
                        }
                    }
                }
                else {
                    final BlockPos blockpos2 = new BlockPos(entityitem);
                    blockpos2.add(0, 1, 0);
                    final Material material = entityitem.world.getBlockState(blockpos2).getBlock().getMaterial(null);
                    final Material material2 = entityitem.world.getBlockState(blockpos).getBlock().getMaterial(null);
                    final boolean flag2 = entityitem.isInsideOfMaterial(Material.WATER);
                    final boolean flag3 = entityitem.isInWater();
                    if (flag2 | material == Material.WATER | material2 == Material.WATER | flag3) {
                        entityitem.rotationPitch += (float)(ItemPhysicUtils.rotation / 4.0);
                    }
                    else {
                        entityitem.rotationPitch += (float)(ItemPhysicUtils.rotation * 2.0);
                    }
                }
            }
            GL11.glRotatef(entityitem.rotationYaw, 0.0f, 1.0f, 0.0f);
            GL11.glRotatef(entityitem.rotationPitch + 90.0f, 1.0f, 0.0f, 0.0f);
            for (int j = 0; j < i; ++j) {
                if (ibakedmodel.isAmbientOcclusion()) {
                    GlStateManager.pushMatrix();
                    GlStateManager.scale(0.2f, 0.2f, 0.2f);
                    ItemPhysicUtils.renderItem.renderItem(itemstack, ibakedmodel);
                    GlStateManager.popMatrix();
                }
                else {
                    GlStateManager.pushMatrix();
                    GlStateManager.scale(0.4f, 0.4f, 0.4f);
                    if (j > 0 && shouldSpreadItems()) {
                        GlStateManager.translate(0.0f, 0.0f, 0.046875f * j);
                    }
                    ItemPhysicUtils.renderItem.renderItem(itemstack, ibakedmodel);
                    if (!shouldSpreadItems()) {
                        GlStateManager.translate(0.0f, 0.0f, 0.046875f);
                    }
                    GlStateManager.popMatrix();
                }
            }
            GlStateManager.popMatrix();
            GlStateManager.disableRescaleNormal();
            GlStateManager.disableBlend();
            ItemPhysicUtils.mc.getRenderManager().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            if (flag) {
                ItemPhysicUtils.mc.getRenderManager().renderEngine.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
            }
        }
    }
    
    public static int func_177077_a(final EntityItem item, final double x, final double y, final double z, final float p_177077_8_, final IBakedModel p_177077_9_) {
        final ItemStack itemstack = item.getEntityItem();
        final Item item2 = itemstack.getItem();
        if (item2 == null) {
            return 0;
        }
        final boolean flag = p_177077_9_.isAmbientOcclusion();
        final int i = func_177078_a(itemstack);
        final float f = 0.25f;
        final float f2 = 0.0f;
        GlStateManager.translate((float)x, (float)y + f2 + 0.25f, (float)z);
        float f3 = 0.0f;
        if (flag || (ItemPhysicUtils.mc.getRenderManager().options != null && ItemPhysicUtils.mc.getRenderManager().options.fancyGraphics)) {
            GlStateManager.rotate(f3, 0.0f, 1.0f, 0.0f);
        }
        if (!flag) {
            f3 = -0.0f * (i - 1) * 0.5f;
            final float f4 = -0.0f * (i - 1) * 0.5f;
            final float f5 = -0.046875f * (i - 1) * 0.5f;
            GlStateManager.translate(f3, f4, f5);
        }
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        return i;
    }
    
    public static boolean shouldSpreadItems() {
        return true;
    }
    
    public static double formPositiv(final float rotationPitch) {
        return (rotationPitch > 0.0f) ? rotationPitch : ((double)(-rotationPitch));
    }
    
    public static int func_177078_a(final ItemStack stack) {
        byte b0 = 1;
        if (stack.stackSize > 48) {
            b0 = 5;
        }
        else if (stack.stackSize > 32) {
            b0 = 4;
        }
        else if (stack.stackSize > 16) {
            b0 = 3;
        }
        else if (stack.stackSize > 1) {
            b0 = 2;
        }
        return b0;
    }
    
    public static byte getMiniBlockCount(final ItemStack stack, final byte original) {
        return original;
    }
    
    public static byte getMiniItemCount(final ItemStack stack, final byte original) {
        return original;
    }
}
