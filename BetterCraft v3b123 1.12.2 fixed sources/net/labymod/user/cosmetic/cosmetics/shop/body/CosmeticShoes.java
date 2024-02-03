// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.user.cosmetic.cosmetics.shop.body;

import java.util.UUID;
import net.labymod.user.User;
import net.labymod.user.cosmetic.custom.UserTextureContainer;
import net.labymod.user.cosmetic.util.CosmeticData;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.labymod.main.LabyMod;
import net.minecraft.entity.Entity;
import net.minecraft.client.model.ModelBase;
import net.labymod.user.cosmetic.ModelCosmetics;
import net.minecraft.client.model.ModelRenderer;
import net.labymod.user.cosmetic.CosmeticRenderer;

public class CosmeticShoes extends CosmeticRenderer<CosmeticShoesData>
{
    public static final int ID = 27;
    private ModelRenderer shoeRight;
    private ModelRenderer shoeLeft;
    
    @Override
    public void addModels(final ModelCosmetics modelCosmetics, final float modelSize) {
        final int width = 27;
        final int height = 18;
        (this.shoeRight = new ModelRenderer(modelCosmetics).setTextureSize(27, 18).setTextureOffset(0, 0)).addBox(-2.6f, 9.0f, -2.5f, 5, 4, 5, modelSize);
        this.shoeRight.setRotationPoint(-1.9f, 12.0f, 0.0f);
        this.shoeRight.isHidden = true;
        ModelRenderer shoeFront = new ModelRenderer(modelCosmetics).setTextureSize(27, 18).setTextureOffset(15, 0);
        shoeFront.addBox(-2.6f, 9.0f, -2.5f, 5, 3, 1, modelSize);
        shoeFront.setRotationPoint(0.0f, 1.0f, -1.0f);
        this.shoeRight.addChild(shoeFront);
        (this.shoeLeft = new ModelRenderer(modelCosmetics).setTextureSize(27, 18).setTextureOffset(0, 9)).addBox(-2.4f, 9.0f, -2.5f, 5, 4, 5, modelSize);
        this.shoeLeft.setRotationPoint(1.9f, 12.001f, 0.001f);
        this.shoeLeft.isHidden = true;
        shoeFront = new ModelRenderer(modelCosmetics).setTextureSize(27, 18).setTextureOffset(15, 9);
        shoeFront.addBox(-2.4f, 9.0f, -2.5f, 5, 3, 1, modelSize);
        shoeFront.setRotationPoint(0.0f, 1.0f, -1.0f);
        this.shoeLeft.addChild(shoeFront);
    }
    
    @Override
    public void setInvisible(final boolean invisible) {
        this.shoeRight.showModel = invisible;
        this.shoeLeft.showModel = invisible;
    }
    
    @Override
    public void render(final ModelCosmetics modelCosmetics, final Entity entityIn, final CosmeticShoesData cosmeticData, final float scale, final float movementFactor, final float walkingSpeed, final float tickValue, final float firstRotationX, final float secondRotationX, final boolean canAnimate) {
        final ResourceLocation location = LabyMod.getInstance().getUserManager().getCosmeticImageManager().getShoesImageHandler().getResourceLocation((AbstractClientPlayer)entityIn);
        if (location == null) {
            return;
        }
        GlStateManager.pushMatrix();
        Minecraft.getMinecraft().getTextureManager().bindTexture(location);
        this.shoeRight.isHidden = false;
        this.shoeLeft.isHidden = false;
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        ModelBase.copyModelAngles(modelCosmetics.bipedRightLeg, this.shoeRight);
        ModelBase.copyModelAngles(modelCosmetics.bipedLeftLeg, this.shoeLeft);
        this.shoeRight.render(scale);
        this.shoeLeft.render(scale);
        this.shoeLeft.isHidden = true;
        this.shoeRight.isHidden = true;
        GlStateManager.popMatrix();
    }
    
    @Override
    public int getCosmeticId() {
        return 27;
    }
    
    @Override
    public String getCosmeticName() {
        return "Shoes";
    }
    
    @Override
    public boolean isOfflineAvailable() {
        return false;
    }
    
    public static class CosmeticShoesData extends CosmeticData
    {
        private UserTextureContainer userTextureContainer;
        
        @Override
        public boolean isEnabled() {
            return true;
        }
        
        @Override
        public void init(final User user) {
            this.userTextureContainer = user.getShoesContainer();
        }
        
        @Override
        public void loadData(final String[] data) throws Exception {
            this.userTextureContainer.setFileName(UUID.fromString(data[0]));
        }
    }
}
