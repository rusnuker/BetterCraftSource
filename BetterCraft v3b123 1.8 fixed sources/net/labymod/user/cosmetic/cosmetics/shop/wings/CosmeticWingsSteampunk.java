// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.user.cosmetic.cosmetics.shop.wings;

import net.labymod.user.cosmetic.util.CosmeticData;
import java.awt.Color;
import net.minecraft.entity.Entity;
import net.labymod.main.ModTextures;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import net.labymod.utils.Consumer;
import net.labymod.user.cosmetic.util.ModelRendererHook;
import net.minecraft.client.model.ModelBase;
import net.labymod.user.cosmetic.ModelCosmetics;
import net.minecraft.client.model.ModelRenderer;
import net.labymod.user.cosmetic.CosmeticRenderer;

public class CosmeticWingsSteampunk extends CosmeticRenderer<CosmeticWingsSteamPunkData>
{
    public static final int ID = 14;
    private ModelRenderer model;
    private float lastAnimationTick;
    
    @Override
    public void addModels(final ModelCosmetics modelCosmetics, final float modelSize) {
        final int width = 26;
        final int height = 22;
        final float layerOffset = 0.5f;
        final ModelRenderer bone3 = new ModelRenderer(modelCosmetics).setTextureSize(26, 22).setTextureOffset(0, 0);
        bone3.addBox(-0.5f, -0.5f, 1.0f, 1, 10, 1);
        bone3.setRotationPoint(1.0f, 2.0f, 0.0f);
        final ModelRenderer bone3Sub = new ModelRenderer(modelCosmetics).setTextureSize(26, 22).setTextureOffset(8, 0);
        bone3Sub.addBox(-0.5f, -1.5f, 1.0f, 1, 5, 1);
        bone3Sub.setRotationPoint(0.0f, 9.0f, 0.0f);
        bone3Sub.addChild(bone3);
        final ModelRendererHook bone3Gear = (ModelRendererHook)new ModelRendererHook(modelCosmetics).setTextureSize(26, 22).setTextureOffset(16, 0);
        bone3Gear.addBox(-2.0f, -2.0f, 1.1f, 4, 4, 1);
        bone3Gear.setRotationPoint(0.0f, 0.0f, 1.0f);
        bone3Gear.setHook(new Consumer<ModelRendererHook>() {
            @Override
            public void accept(final ModelRendererHook model) {
                GlStateManager.enableBlend();
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                bone3Gear.renderSuper();
                GlStateManager.disableBlend();
            }
        });
        bone3Sub.addChild(bone3Gear);
        final ModelRenderer bone4 = new ModelRenderer(modelCosmetics).setTextureSize(26, 22).setTextureOffset(22, 11);
        bone4.addBox(-0.5f, -0.5f, 0.4f, 1, 10, 1);
        bone4.setRotationPoint(0.0f, 3.0f, 0.0f);
        bone4.addChild(bone3Sub);
        final ModelRenderer bone5 = new ModelRenderer(modelCosmetics).setTextureSize(26, 22).setTextureOffset(12, 0);
        bone5.addBox(-0.5f, -0.5f, 0.5f, 1, 4, 1);
        bone5.setRotationPoint(0.0f, 0.0f, 0.0f);
        bone5.addChild(bone4);
        final ModelRendererHook bone1Gear = (ModelRendererHook)new ModelRendererHook(modelCosmetics).setTextureSize(26, 22).setTextureOffset(8, 6);
        bone1Gear.addBox(-1.0f, -1.0f, 2.0f, 2, 2, 1);
        bone1Gear.setRotationPoint(0.0f, 3.0f, 0.0f);
        bone1Gear.setHook(new Consumer<ModelRendererHook>() {
            @Override
            public void accept(final ModelRendererHook model) {
                GlStateManager.enableBlend();
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                bone1Gear.renderSuper();
                GlStateManager.disableBlend();
            }
        });
        bone5.addChild(bone1Gear);
        final ModelRenderer stick1 = new ModelRenderer(modelCosmetics).setTextureSize(26, 22).setTextureOffset(0, 11);
        stick1.addBox(-0.5f, -0.5f, 0.5f, 1, 10, 1);
        stick1.setRotationPoint(0.0f, 9.0f, 0.0f);
        bone4.addChild(stick1);
        final ModelRenderer stick2 = new ModelRenderer(modelCosmetics).setTextureSize(26, 22).setTextureOffset(4, 11);
        stick2.addBox(-0.5f, -0.5f, 0.6f, 1, 9, 1);
        stick2.setRotationPoint(0.0f, 9.0f, 0.0f);
        bone4.addChild(stick2);
        final ModelRenderer stick3 = new ModelRenderer(modelCosmetics).setTextureSize(26, 22).setTextureOffset(8, 11);
        stick3.addBox(-0.5f, -0.5f, 0.7f, 1, 8, 1);
        stick3.setRotationPoint(0.0f, 9.0f, 0.0f);
        bone4.addChild(stick3);
        final ModelRendererHook textile1 = (ModelRendererHook)new ModelRendererHook(modelCosmetics).setTextureSize(26, 22).setTextureOffset(16, 5);
        textile1.addBox(-0.5f, -0.5f, 0.4f, 4, 8, 1);
        textile1.setRotationPoint(-3.8f, 2.0f, 0.0f);
        textile1.setHook(new Consumer<ModelRendererHook>() {
            @Override
            public void accept(final ModelRendererHook model) {
                CosmeticWingsSteampunk.this.renderTextile(model, 1);
            }
        });
        bone3.addChild(textile1);
        final ModelRendererHook textile2 = (ModelRendererHook)new ModelRendererHook(modelCosmetics).setTextureSize(26, 22).setTextureOffset(16, 5);
        textile2.addBox(-0.5f, -0.5f, 0.4f, 4, 8, 1);
        textile2.setRotationPoint(-3.8f, 2.0f, 0.0f);
        textile2.setHook(new Consumer<ModelRendererHook>() {
            @Override
            public void accept(final ModelRendererHook model) {
                CosmeticWingsSteampunk.this.renderTextile(model, 2);
            }
        });
        stick1.addChild(textile2);
        final ModelRendererHook textile3 = (ModelRendererHook)new ModelRendererHook(modelCosmetics).setTextureSize(26, 22).setTextureOffset(16, 5);
        textile3.addBox(-0.5f, -0.5f, 0.4f, 4, 8, 1);
        textile3.setRotationPoint(-3.8f, 2.0f, 0.0f);
        textile3.setHook(new Consumer<ModelRendererHook>() {
            @Override
            public void accept(final ModelRendererHook model) {
                CosmeticWingsSteampunk.this.renderTextile(model, 3);
            }
        });
        stick2.addChild(textile3);
        final ModelRendererHook textile4 = (ModelRendererHook)new ModelRendererHook(modelCosmetics).setTextureSize(26, 22).setTextureOffset(16, 5);
        textile4.addBox(-0.5f, -0.5f, 0.4f, 4, 8, 1);
        textile4.setRotationPoint(-3.8f, 2.0f, 0.0f);
        textile4.setHook(new Consumer<ModelRendererHook>() {
            @Override
            public void accept(final ModelRendererHook model) {
                CosmeticWingsSteampunk.this.renderTextile(model, 4);
            }
        });
        stick3.addChild(textile4);
        final ModelRenderer connection = new ModelRenderer(modelCosmetics).setTextureSize(26, 22).setTextureOffset(12, 11);
        connection.addBox(0.5f, 0.0f, -1.0f, 1, 10, 1);
        connection.setRotationPoint(0.0f, 1.5f, 0.0f);
        bone5.addChild(connection);
        this.model = bone5;
        this.model.isHidden = true;
    }
    
    @Override
    public void setInvisible(final boolean invisible) {
        this.model.showModel = invisible;
    }
    
    private void renderTextile(final ModelRendererHook model, final int index) {
        final boolean fogEnabled = GL11.glGetBoolean(2912);
        GlStateManager.pushMatrix();
        GlStateManager.disableFog();
        GlStateManager.disableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.depthMask(false);
        Minecraft.getMinecraft().getTextureManager().bindTexture(ModTextures.VOID);
        double heightMain = 0.0;
        double heightOff = 0.0;
        double distance = 0.0;
        float zMain = 0.07f;
        final float zOff = 0.07f;
        final float transparency = (1.0f - this.lastAnimationTick) / 2.0f;
        switch (index) {
            case 1: {
                heightMain = 0.6000000238418579;
                heightOff = 0.4000000059604645;
                distance = this.lastAnimationTick / 3.0 - 0.32;
                zMain = 0.09f;
                break;
            }
            case 2: {
                heightMain = 0.6000000238418579;
                heightOff = 0.4300000071525574;
                distance = this.lastAnimationTick / 3.0 - 0.32;
                break;
            }
            case 3: {
                heightMain = 0.5299999713897705;
                heightOff = 0.4099999964237213;
                distance = this.lastAnimationTick / 4.0 - 0.26;
                break;
            }
            case 4: {
                heightMain = 0.4699999988079071;
                heightOff = 0.46000000834465027;
                distance = this.lastAnimationTick / 2.6 - 0.42;
                break;
            }
        }
        GL11.glBegin(4);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, transparency);
        if (index == 1) {
            GL11.glVertex3f(-0.06f, -0.0f, zMain);
        }
        else {
            GL11.glVertex3f(0.0f, 0.0f, zMain);
        }
        GL11.glVertex3d(distance, heightOff, (index == 4) ? 0.05000000074505806 : 0.07000000029802322);
        GL11.glVertex3d(0.0, heightMain, zMain);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glEnd();
        Minecraft.getMinecraft().getTextureManager().bindTexture(ModTextures.COSMETIC_WINGS_STEAMPUNK);
        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.resetColor();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.enableAlpha();
        if (fogEnabled) {
            GlStateManager.enableFog();
        }
        GlStateManager.popMatrix();
    }
    
    @Override
    public void render(final ModelCosmetics modelCosmetics, final Entity entityIn, final CosmeticWingsSteamPunkData cosmeticData, final float scale, final float movementFactor, final float walkingSpeed, final float tickValue, final float firstRotationX, final float secondRotationX, final boolean canAnimate) {
        float animation = 1.0f - cosmeticData.getAnimationTick(walkingSpeed / 1.3f) + (float)Math.cos(movementFactor / 2.0f) / 15.0f * walkingSpeed - 0.1f;
        if (entityIn.isSneaking()) {
            animation -= 0.3f;
        }
        this.lastAnimationTick = animation;
        this.model.rotateAngleX = 0.4f;
        this.model.rotateAngleY = 0.0f;
        this.model.rotateAngleZ = animation - 1.0f;
        final ModelRenderer connection = this.model.childModels.get(2);
        connection.rotateAngleX = 0.6f;
        connection.rotateAngleY = -0.4f;
        connection.rotateAngleZ = -animation * 1.5f - 1.2f;
        final ModelRenderer bone2 = this.model.childModels.get(0);
        bone2.rotateAngleX = 0.5f;
        bone2.rotateAngleY = -0.4f;
        bone2.rotateAngleZ = -animation * 1.4f - 1.5f;
        final ModelRenderer gear1 = this.model.childModels.get(1);
        gear1.rotateAngleZ = animation;
        final ModelRenderer bone3 = bone2.childModels.get(0);
        bone3.rotateAngleZ = animation * 1.8f * 1.2f + 1.8f - 1.0f;
        final ModelRenderer gear2 = bone3.childModels.get(1);
        gear2.rotateAngleZ = animation;
        final ModelRenderer stick1 = bone2.childModels.get(1);
        stick1.rotateAngleZ = (animation * 0.9f + 2.1f) * 2.0f - 3.0f;
        final ModelRenderer stick2 = bone2.childModels.get(2);
        stick2.rotateAngleZ = (animation * 0.6f + 2.4f) * 2.0f - 3.0f;
        final ModelRenderer stick3 = bone2.childModels.get(3);
        stick3.rotateAngleZ = (animation * 0.3f + 2.7f) * 2.0f - 3.0f;
        GlStateManager.pushMatrix();
        if (entityIn.isSneaking()) {
            GlStateManager.translate(0.0, -0.009999999776482582, -0.0);
            GlStateManager.rotate(20.0f, 1.0f, 0.0f, 0.0f);
        }
        Minecraft.getMinecraft().getTextureManager().bindTexture(ModTextures.COSMETIC_WINGS_STEAMPUNK);
        this.model.isHidden = false;
        GlStateManager.color(1.0f, 1.0f, 1.0f, 0.4f);
        GlStateManager.translate(0.0f, 0.1f, 0.1f);
        GlStateManager.scale(0.8f, 0.8f, 0.8f);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.resetColor();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.enableAlpha();
        final Color color = cosmeticData.getColor();
        for (int i = -1; i <= 1; i += 2) {
            GlStateManager.pushMatrix();
            if (i == 1) {
                GlStateManager.scale(-1.0f, 1.0f, 1.0f);
            }
            GlStateManager.translate(0.1, 0.0, 0.0);
            GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 0.5f);
            if (i == 1 && cosmeticData.getSecondColor() != null) {
                final Color secondColor = cosmeticData.getSecondColor();
                GL11.glColor4f(secondColor.getRed() / 255.0f, secondColor.getGreen() / 255.0f, secondColor.getBlue() / 255.0f, 0.5f);
            }
            this.model.render(scale);
            GlStateManager.popMatrix();
        }
        this.model.isHidden = true;
        GlStateManager.popMatrix();
    }
    
    @Override
    public int getCosmeticId() {
        return 14;
    }
    
    @Override
    public String getCosmeticName() {
        return "SteampunkWings";
    }
    
    @Override
    public boolean isOfflineAvailable() {
        return false;
    }
    
    public static class CosmeticWingsSteamPunkData extends CosmeticData
    {
        public long lastWalking;
        private Color color;
        private Color secondColor;
        
        public CosmeticWingsSteamPunkData() {
            this.color = new Color(60, 60, 60);
            this.secondColor = null;
        }
        
        @Override
        public boolean isEnabled() {
            return true;
        }
        
        @Override
        public void loadData(final String[] data) throws Exception {
            this.color = Color.decode("#" + data[0]);
            if (data.length > 1) {
                this.secondColor = Color.decode("#" + data[1]);
            }
        }
        
        public float getAnimationTick(final float walkingSpeed) {
            final long fadeOutAnimationTick = this.lastWalking - System.currentTimeMillis();
            if (walkingSpeed > 0.7) {
                this.lastWalking = System.currentTimeMillis() + 2500L;
            }
            final float fadeOut = 0.00136f * (this.lastWalking - System.currentTimeMillis());
            if (fadeOutAnimationTick < 500L && fadeOutAnimationTick >= 0L) {
                return Math.max(fadeOut, walkingSpeed);
            }
            if (fadeOutAnimationTick <= 0L || walkingSpeed >= 0.7) {
                return walkingSpeed;
            }
            if (fadeOutAnimationTick < 500L) {
                return fadeOut;
            }
            return 0.7f + (float)Math.cos((fadeOutAnimationTick + 900L) / 360.0f) / 20.0f;
        }
        
        public long getLastWalking() {
            return this.lastWalking;
        }
        
        public void setLastWalking(final long lastWalking) {
            this.lastWalking = lastWalking;
        }
        
        public Color getColor() {
            return this.color;
        }
        
        public Color getSecondColor() {
            return this.secondColor;
        }
    }
}
