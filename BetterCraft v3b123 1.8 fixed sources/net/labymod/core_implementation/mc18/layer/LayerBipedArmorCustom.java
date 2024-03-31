// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.core_implementation.mc18.layer;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.item.ItemStack;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.Item;
import net.labymod.core.LabyModCore;
import net.labymod.main.LabyMod;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;

public class LayerBipedArmorCustom extends LayerBipedArmor
{
    private boolean swapped;
    
    public LayerBipedArmorCustom(final RendererLivingEntity<?> rendererIn) {
        super(rendererIn);
        this.swapped = false;
    }
    
    @Override
    public void doRenderLayer(final EntityLivingBase entity, final float var1, final float var2, final float partialTicks, final float var4, final float var5, final float var6, final float scale) {
        boolean swap = LabyMod.getSettings().leftHand;
        final ItemStack itemStack = LabyModCore.getMinecraft().getMainHandItem();
        final int itemId = (itemStack != null && itemStack.getItem() != null) ? Item.getIdFromItem(itemStack.getItem()) : 0;
        if (LabyMod.getSettings().swapBow && itemId == 261) {
            swap = !swap;
        }
        if (swap && LabyModCore.getMinecraft().getItemInUseMaxCount() != 0 && itemId == 261) {
            swap = false;
        }
        if (swap) {
            GlStateManager.scale(-1.0f, 1.0f, 1.0f);
            GlStateManager.disableCull();
        }
        super.doRenderLayer(entity, var1, var2, partialTicks, var4, var5, var6, scale);
        if (swap) {
            GlStateManager.scale(-1.0f, 1.0f, 1.0f);
            GlStateManager.disableCull();
        }
        if (this.swapped) {
            this.swapped = false;
            GlStateManager.scale(-1.0f, 1.0f, 1.0f);
            GlStateManager.disableCull();
        }
    }
    
    public ModelBiped func_177175_a(final int slot) {
        boolean swap = LabyMod.getSettings().leftHand;
        final ItemStack itemStack = LabyModCore.getMinecraft().getMainHandItem();
        final int itemId = (itemStack != null && itemStack.getItem() != null) ? Item.getIdFromItem(itemStack.getItem()) : 0;
        if (LabyMod.getSettings().swapBow && itemId == 261) {
            swap = !swap;
        }
        if (swap && LabyModCore.getMinecraft().getItemInUseMaxCount() != 0 && itemId == 261) {
            swap = false;
        }
        if (this.swapped) {
            this.swapped = false;
            GlStateManager.scale(-1.0f, 1.0f, 1.0f);
            GlStateManager.disableCull();
        }
        if (slot == 3 && swap) {
            this.swapped = true;
            GlStateManager.scale(-1.0f, 1.0f, 1.0f);
            GlStateManager.disableCull();
        }
        return super.getArmorModel(slot);
    }
    
    @Override
    public boolean shouldCombineTextures() {
        return LabyMod.getSettings().oldDamage;
    }
}
