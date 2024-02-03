// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.user.cosmetic.cosmetics.event;

import net.labymod.user.cosmetic.util.CosmeticData;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.labymod.user.cosmetic.ModelCosmetics;
import net.labymod.user.cosmetic.CosmeticRenderer;

public class CosmeticHalloween extends CosmeticRenderer<CosmeticHalloweenData>
{
    public static final int ID = 15;
    
    @Override
    public void addModels(final ModelCosmetics modelCosmetics, final float modelSize) {
    }
    
    @Override
    public void setInvisible(final boolean invisible) {
    }
    
    @Override
    public void render(final ModelCosmetics modelCosmetics, final Entity entityIn, final CosmeticHalloweenData cosmeticData, final float scale, final float movementFactor, final float walkingSpeed, final float tickValue, final float firstRotationX, final float secondRotationX, final boolean canAnimate) {
        GlStateManager.pushMatrix();
        GlStateManager.enableAlpha();
        GlStateManager.rotate(firstRotationX, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(secondRotationX, 1.0f, 0.0f, 0.0f);
        GlStateManager.scale(0.8, 0.8, 0.8);
        ItemStack item = null;
        int itemId = 0;
        switch (cosmeticData.getEnumHalloweenType()) {
            case AXE_RIGHT: {
                itemId = 258;
                GlStateManager.rotate(-120.0f, 0.0f, 0.0f, 1.0f);
                GlStateManager.rotate(40.0f, 0.0f, 1.0f, 0.0f);
                GlStateManager.translate(0.4, -0.4, 0.3);
                break;
            }
            case PICKAXE_TOP: {
                itemId = 257;
                GlStateManager.rotate(-30.0f, 0.0f, 0.0f, 1.0f);
                GlStateManager.rotate(-30.0f, 0.0f, 1.0f, 0.0f);
                GlStateManager.translate(0.2, -0.8, -0.22);
                break;
            }
            case AXE_LEFT: {
                itemId = 258;
                GlStateManager.rotate(70.0f, 0.0f, 0.0f, 1.0f);
                GlStateManager.rotate(-30.0f, 0.0f, 1.0f, 0.0f);
                GlStateManager.translate(-0.3, -0.7, -0.02);
                break;
            }
            case AXE_TOP: {
                itemId = 292;
                GlStateManager.rotate(-50.0f, 0.0f, 0.0f, 1.0f);
                GlStateManager.rotate(70.0f, 0.0f, 1.0f, 0.0f);
                GlStateManager.translate(0.2, -0.6, 0.4);
                break;
            }
            case ARROW_UPPER_LEFT_BACK_CORNER: {
                itemId = 262;
                GlStateManager.rotate(-180.0f, 0.0f, 0.0f, 1.0f);
                GlStateManager.rotate(-100.0f, 0.0f, 1.0f, 0.0f);
                GlStateManager.rotate(30.0f, 1.0f, 0.0f, 0.0f);
                GlStateManager.translate(0.1, 0.3, -0.5);
                break;
            }
            case ARROW_DIAGONALLY: {
                itemId = 262;
                GlStateManager.rotate(-50.0f, 0.0f, 0.0f, 1.0f);
                GlStateManager.translate(0.1, -0.1, 0.0);
                GlStateManager.translate(-0.19, -0.19, 0.0);
                item = new ItemStack(Item.getItemById(itemId));
                if (item != null && item.getItem() != null) {
                    Minecraft.getMinecraft().getItemRenderer().renderItem((EntityLivingBase)entityIn, item, ItemCameraTransforms.TransformType.NONE);
                }
                GlStateManager.translate(0.38, 0.38, 0.0);
                break;
            }
        }
        if (itemId != 0) {
            if (item == null) {
                item = new ItemStack(Item.getItemById(itemId));
            }
            if (item != null && item.getItem() != null) {
                Minecraft.getMinecraft().getItemRenderer().renderItem((EntityLivingBase)entityIn, item, ItemCameraTransforms.TransformType.NONE);
            }
        }
        GlStateManager.popMatrix();
    }
    
    @Override
    public int getCosmeticId() {
        return 15;
    }
    
    @Override
    public String getCosmeticName() {
        return "Halloween";
    }
    
    @Override
    public boolean isOfflineAvailable() {
        return false;
    }
    
    public static class CosmeticHalloweenData extends CosmeticData
    {
        private EnumHalloweenType enumHalloweenType;
        
        public CosmeticHalloweenData() {
            this.enumHalloweenType = EnumHalloweenType.AXE_RIGHT;
        }
        
        @Override
        public boolean isEnabled() {
            return true;
        }
        
        @Override
        public void loadData(final String[] data) throws Exception {
            this.enumHalloweenType = EnumHalloweenType.values()[Integer.parseInt(data[0])];
        }
        
        public EnumHalloweenType getEnumHalloweenType() {
            return this.enumHalloweenType;
        }
        
        public enum EnumHalloweenType
        {
            AXE_RIGHT("AXE_RIGHT", 0), 
            PICKAXE_TOP("PICKAXE_TOP", 1), 
            AXE_LEFT("AXE_LEFT", 2), 
            AXE_TOP("AXE_TOP", 3), 
            ARROW_UPPER_LEFT_BACK_CORNER("ARROW_UPPER_LEFT_BACK_CORNER", 4), 
            ARROW_DIAGONALLY("ARROW_DIAGONALLY", 5);
            
            private EnumHalloweenType(final String s, final int n) {
            }
        }
    }
}
