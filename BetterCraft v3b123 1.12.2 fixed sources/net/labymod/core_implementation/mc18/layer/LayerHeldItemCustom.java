// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.core_implementation.mc18.layer;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.item.ItemCarrotOnAStick;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.labymod.main.LabyMod;
import net.labymod.api.permissions.Permissions;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemSword;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;

public class LayerHeldItemCustom extends LayerHeldItem
{
    private RendererLivingEntity<?> livingEntityRenderer;
    
    public LayerHeldItemCustom(final RendererLivingEntity<?> livingEntityRendererIn) {
        super(livingEntityRendererIn);
        this.livingEntityRenderer = livingEntityRendererIn;
    }
    
    @Override
    public void doRenderLayer(final EntityLivingBase entitylivingbaseIn, final float p_177141_2_, final float p_177141_3_, final float partialTicks, final float p_177141_5_, final float p_177141_6_, final float p_177141_7_, final float scale) {
        ItemStack itemstack = entitylivingbaseIn.getHeldItem();
        if (itemstack != null) {
            GlStateManager.pushMatrix();
            if (this.livingEntityRenderer.getMainModel().isChild) {
                final float f = 0.5f;
                GlStateManager.translate(0.0f, 0.625f, 0.0f);
                GlStateManager.rotate(-20.0f, -1.0f, 0.0f, 0.0f);
                GlStateManager.scale(0.5f, 0.5f, 0.5f);
            }
            final Item item = itemstack.getItem();
            final boolean tool = item instanceof ItemSword || item instanceof ItemHoe || item instanceof ItemAxe || item instanceof ItemPickaxe || item instanceof ItemSpade;
            final boolean allowed = Permissions.isAllowed(Permissions.Permission.ANIMATIONS);
            if (tool && LabyMod.getSettings().oldSword && allowed) {
                if (LabyMod.isBlocking((EntityPlayer)entitylivingbaseIn)) {
                    this.postRenderArm(entitylivingbaseIn, 0.07f);
                    GlStateManager.rotate(70.0f, 0.0f, 0.0f, -1.0f);
                    GlStateManager.rotate(35.0f, 0.0f, -1.0f, 0.0f);
                    GlStateManager.rotate(20.0f, 1.0f, 0.0f, 0.0f);
                    GlStateManager.scale(1.15f, 1.15f, 1.15f);
                    if (entitylivingbaseIn.isSneaking()) {
                        GlStateManager.translate(-0.38, 0.06, 0.36);
                    }
                    else {
                        GlStateManager.translate(-0.32, 0.15, 0.26);
                    }
                }
                else {
                    this.postRenderArm(entitylivingbaseIn, 0.0625f);
                    GlStateManager.scale(1.1f, 1.1f, 1.1f);
                    GlStateManager.translate(-0.1f, 0.445f, 0.169f);
                    GlStateManager.rotate(18.5f, -1.0f, 0.0f, 0.0f);
                }
            }
            else if (LabyMod.getSettings().oldItemHold && allowed && !(item instanceof ItemBlock)) {
                this.postRenderArm(entitylivingbaseIn, 0.0625f);
                GlStateManager.scale(-1.0f, 1.0f, 1.0f);
                GlStateManager.translate(-0.0625f, 0.4375f, 0.0625f);
                if (item instanceof ItemBow) {
                    GlStateManager.translate(-0.1f, 0.0f, -0.05f);
                    GlStateManager.rotate(10.0f, -1.0f, 0.0f, 0.0f);
                    GlStateManager.rotate(15.0f, 0.0f, 1.0f, 0.0f);
                    GlStateManager.rotate(15.0f, 0.0f, 1.0f, 0.0f);
                }
                else if (item instanceof ItemFishingRod || item instanceof ItemCarrotOnAStick) {
                    GlStateManager.translate(0.08f, -0.07f, 0.07f);
                    GlStateManager.scale(1.1, 1.1, 1.1);
                    GlStateManager.rotate(20.0f, 1.0f, 0.0f, 0.0f);
                    GlStateManager.rotate(5.0f, 0.0f, 0.0f, -1.0f);
                }
                else {
                    GlStateManager.translate(0.06f, 0.01f, -0.0f);
                    GlStateManager.rotate(10.0f, -1.0f, 1.0f, -2.0f);
                }
            }
            else {
                this.postRenderArm(entitylivingbaseIn, 0.0625f);
                GlStateManager.translate(-0.0625f, 0.4375f, 0.0625f);
            }
            if (entitylivingbaseIn instanceof EntityPlayer && ((EntityPlayer)entitylivingbaseIn).fishEntity != null) {
                itemstack = new ItemStack(Items.fishing_rod, 0);
            }
            final Minecraft minecraft = Minecraft.getMinecraft();
            if (item instanceof ItemBlock && Block.getBlockFromItem(item).getRenderType() == 2) {
                GlStateManager.translate(0.0f, 0.1875f, -0.3125f);
                GlStateManager.rotate(20.0f, 1.0f, 0.0f, 0.0f);
                GlStateManager.rotate(45.0f, 0.0f, 1.0f, 0.0f);
                final float f2 = 0.375f;
                GlStateManager.scale(-0.375f, -0.375f, 0.375f);
            }
            if (entitylivingbaseIn.isSneaking()) {
                GlStateManager.translate(0.0f, 0.203125f, 0.0f);
            }
            minecraft.getItemRenderer().renderItem(entitylivingbaseIn, itemstack, ItemCameraTransforms.TransformType.THIRD_PERSON);
            GlStateManager.popMatrix();
        }
    }
    
    private void postRenderArm(final EntityLivingBase entityLivingBase, final float scale) {
        ((ModelBiped)this.livingEntityRenderer.getMainModel()).postRenderArm(scale);
    }
}
