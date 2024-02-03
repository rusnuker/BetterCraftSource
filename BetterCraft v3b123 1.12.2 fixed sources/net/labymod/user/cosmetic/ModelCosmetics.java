// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.user.cosmetic;

import net.minecraft.client.model.ModelRenderer;
import net.labymod.api.permissions.Permissions;
import net.minecraft.util.ResourceLocation;
import net.labymod.user.User;
import java.util.UUID;
import net.labymod.user.UserManager;
import net.labymod.user.emote.EmoteRenderer;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;
import me.nzxtercode.bettercraft.client.misc.irc.IRC;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.item.Item;
import net.labymod.core.LabyModCore;
import net.labymod.main.LabyMod;
import net.minecraft.entity.Entity;
import net.lenni0451.eventapi.events.EventTarget;
import me.nzxtercode.bettercraft.client.events.PlayerTickEvent;
import java.util.Iterator;
import net.labymod.main.Source;
import java.util.HashMap;
import net.labymod.gui.skin.GuiSkinCustomization;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.labymod.user.cosmetic.util.CosmeticData;
import java.util.Map;
import net.labymod.user.cosmetic.sticker.StickerRenderer;
import net.labymod.user.cosmetic.util.CosmeticClassLoader;
import net.minecraft.client.model.ModelPlayer;

public class ModelCosmetics extends ModelPlayer
{
    private static final Class<?>[] partialTicksBlackList;
    private static final CosmeticClassLoader cosmeticClassLoader;
    private final StickerRenderer stickerRenderer;
    private final boolean mc18;
    protected Map<Integer, CosmeticRenderer<CosmeticData>> cosmeticRenderers;
    
    static {
        partialTicksBlackList = new Class[] { GuiInventory.class, GuiContainerCreative.class, GuiSkinCustomization.class };
        cosmeticClassLoader = new CosmeticClassLoader();
    }
    
    public ModelCosmetics(final float modelSize, final boolean value) {
        super(modelSize, value);
        this.cosmeticRenderers = new HashMap<Integer, CosmeticRenderer<CosmeticData>>();
        this.mc18 = Source.ABOUT_MC_VERSION.startsWith("1.8");
        this.stickerRenderer = new StickerRenderer(this, modelSize);
        try {
            for (final Class<?> loadedClassInfo : ModelCosmetics.cosmeticClassLoader.getCosmeticClasses()) {
                final CosmeticRenderer<CosmeticData> cosmeticRenderer = (CosmeticRenderer<CosmeticData>)loadedClassInfo.newInstance();
                this.cosmeticRenderers.put(cosmeticRenderer.getCosmeticId(), cosmeticRenderer);
                cosmeticRenderer.addModels(this, modelSize);
            }
        }
        catch (final Exception error) {
            error.printStackTrace();
        }
    }
    
    public ModelCosmetics(final float p_i46304_1_, final float f, final int i, final int j) {
        this(p_i46304_1_, false);
    }
    
    @EventTarget
    public void handleEvent(final PlayerTickEvent event) {
        for (final CosmeticRenderer<CosmeticData> cosmeticRenderer : this.cosmeticRenderers.values()) {
            cosmeticRenderer.onTick();
        }
    }
    
    @Override
    public void render(final Entity entityIn, final float limbSwing, final float limbSwingAmount, final float ageInTicks, float yaw, float pitch, final float scale) {
        boolean swap = LabyMod.getSettings().leftHand;
        final ItemStack itemStack = LabyModCore.getMinecraft().getMainHandItem();
        final int itemId = (itemStack != null && itemStack.getItem() != null) ? Item.getIdFromItem(itemStack.getItem()) : 0;
        if (LabyMod.getSettings().swapBow && itemId == 261) {
            swap = !swap;
        }
        if ((swap && LabyModCore.getMinecraft().getItemInUseMaxCount() != 0 && itemId == 261) || (swap && LabyMod.getInstance().isHasLeftHand())) {
            swap = false;
        }
        if (entityIn instanceof AbstractClientPlayer && LabyMod.getSettings().emotes) {
            final AbstractClientPlayer abstractClientPlayer = (AbstractClientPlayer)entityIn;
            final EmoteRenderer emoteRenderer = LabyMod.getInstance().getEmoteRegistry().getEmoteRendererFor(abstractClientPlayer);
            if (emoteRenderer != null && emoteRenderer.isVisible()) {
                emoteRenderer.checkForNextFrame();
                emoteRenderer.animate();
                emoteRenderer.transformEntity(entityIn, false, yaw, pitch);
                yaw = emoteRenderer.getFadedYaw();
                pitch = emoteRenderer.getFadedPitch();
            }
            this.bipedHead.rotateAngleY = yaw / 57.295776f;
            this.bipedHead.rotateAngleX = pitch / 57.295776f;
        }
        if (swap) {
            this.transformForLeftHand(entityIn, limbSwing, limbSwingAmount, ageInTicks, yaw, pitch, scale);
        }
        else {
            super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, yaw, pitch, scale);
        }
        if (LabyModCore.getMinecraft().isElytraFlying(entityIn)) {
            pitch = -45.0f;
        }
        if (entityIn instanceof AbstractClientPlayer && (LabyMod.getSettings().cosmetics || LabyMod.getSettings().stickers)) {
            final AbstractClientPlayer abstractClientPlayer = (AbstractClientPlayer)entityIn;
            final UserManager userManager = LabyMod.getInstance().getUserManager();
            final UUID uuid = abstractClientPlayer.getUniqueID();
            if (userManager.isWhitelisted(uuid) && !entityIn.isInvisible()) {
                final User user = userManager.getUser(uuid);
                if (LabyMod.getSettings().cosmetics) {
                    boolean canAnimate = true;
                    if (Minecraft.getMinecraft().currentScreen != null) {
                        Class<?>[] partialTicksBlackList;
                        for (int length = (partialTicksBlackList = ModelCosmetics.partialTicksBlackList).length, i = 0; i < length; ++i) {
                            final Class<?> clazz = partialTicksBlackList[i];
                            if (clazz.isAssignableFrom(Minecraft.getMinecraft().currentScreen.getClass())) {
                                canAnimate = false;
                            }
                        }
                    }
                    final ResourceLocation tex = abstractClientPlayer.getLocationSkin();
                    user.resetMaxNameTagHeight();
                    GlStateManager.pushMatrix();
                    GlStateManager.enableAlpha();
                    if (entityIn.isSneaking()) {
                        GlStateManager.translate(0.0f, 0.2f, 0.0f);
                    }
                    for (final Map.Entry<Integer, CosmeticData> entry : user.getCosmetics().entrySet()) {
                        final CosmeticData data = entry.getValue();
                        if (data.isEnabled()) {
                            if (IRC.getInstance().isUserConnected(abstractClientPlayer.getGameProfile().getName())) {
                                continue;
                            }
                            GlStateManager.pushMatrix();
                            GlStateManager.disableBlend();
                            GlStateManager.enableLighting();
                            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                            GlStateManager.enableLighting();
                            GlStateManager.enableLight(0);
                            GlStateManager.enableLight(1);
                            GlStateManager.enableColorMaterial();
                            GlStateManager.colorMaterial(1032, 5634);
                            final CosmeticRenderer<CosmeticData> cosmeticRenderer = this.cosmeticRenderers.get(entry.getKey());
                            cosmeticRenderer.render(this, entityIn, data, scale, limbSwing, limbSwingAmount, ageInTicks, yaw, pitch, canAnimate);
                            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                            GlStateManager.enableTexture2D();
                            GlStateManager.shadeModel(7424);
                            GlStateManager.enableAlpha();
                            GlStateManager.disableBlend();
                            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                            GlStateManager.disableBlend();
                            GlStateManager.resetColor();
                            user.applyNameTagHeight(cosmeticRenderer.getNameTagHeight());
                            Minecraft.getMinecraft().getTextureManager().bindTexture(tex);
                            GlStateManager.popMatrix();
                        }
                    }
                    GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                    GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                    GlStateManager.enableAlpha();
                    GlStateManager.popMatrix();
                }
                if (LabyMod.getSettings().stickers && user.isStickerVisible()) {
                    final long timePassed = System.currentTimeMillis() - user.getStickerStartedPlaying();
                    this.stickerRenderer.render(entityIn, user, timePassed, scale, limbSwing, limbSwingAmount, ageInTicks, yaw, pitch);
                }
            }
        }
        if (swap) {
            GlStateManager.scale(-1.0f, 1.0f, 1.0f);
            GlStateManager.disableCull();
        }
    }
    
    @Override
    public void setRotationAngles(final float movementFactor, final float walkingSpeed, final float tickValue, final float var4, final float var5, final float var6, final Entity entityIn) {
        super.setRotationAngles(movementFactor, walkingSpeed, tickValue, var4, var5, var6, entityIn);
        if (LabyModCore.getMinecraft().isRightArmPoseBow(this) && this.mc18 && LabyMod.getSettings().oldSword && Permissions.isAllowed(Permissions.Permission.ANIMATIONS)) {
            this.bipedRightArm.rotateAngleY = 0.0f;
            if (this.swingProgress > -9990.0f) {
                final ModelRenderer bipedRightArm3;
                final ModelRenderer bipedRightArm = bipedRightArm3 = this.bipedRightArm;
                bipedRightArm3.rotateAngleY += this.bipedBody.rotateAngleY;
                final ModelRenderer bipedRightArm4;
                final ModelRenderer bipedRightArm2 = bipedRightArm4 = this.bipedRightArm;
                bipedRightArm4.rotateAngleY += this.bipedBody.rotateAngleY * 2.0f;
            }
            if (LabyModCore.getMinecraft().isAimedBow(this)) {
                this.bipedRightArm.rotateAngleY = -0.1f + this.bipedHead.rotateAngleY;
            }
            this.bipedRightArmwear.rotateAngleY = this.bipedRightArm.rotateAngleY;
        }
        for (final CosmeticRenderer<CosmeticData> cosmeticRenderer : this.cosmeticRenderers.values()) {
            cosmeticRenderer.setRotationAngles(this, movementFactor, walkingSpeed, tickValue, var4, var5, var6, entityIn);
        }
    }
    
    @Override
    public void setInvisible(final boolean invisible) {
        for (final CosmeticRenderer<CosmeticData> cosmeticRenderer : this.cosmeticRenderers.values()) {
            cosmeticRenderer.setInvisible(invisible);
        }
    }
    
    public void setVisible(final boolean visible) {
        for (final CosmeticRenderer<CosmeticData> cosmeticRenderer : this.cosmeticRenderers.values()) {
            cosmeticRenderer.setInvisible(visible);
        }
    }
    
    public void setTextureOffset(final String partName, final int x, final int y) {
        super.setTextureOffset(partName, x, y);
    }
    
    private void transformForLeftHand(final Entity entityIn, final float var1, final float var2, final float var3, final float var4, final float var5, final float scale) {
        this.bipedRightArm.isHidden = true;
        this.bipedRightArmwear.isHidden = true;
        this.bipedLeftArm.isHidden = true;
        this.bipedLeftArmwear.isHidden = true;
        super.render(entityIn, var1, var2, var3, var4, var5, scale);
        GlStateManager.pushMatrix();
        GlStateManager.scale(-1.0f, 1.0f, 1.0f);
        GlStateManager.disableCull();
        this.bipedRightArm.isHidden = false;
        this.bipedRightArmwear.isHidden = false;
        this.bipedLeftArm.isHidden = false;
        this.bipedLeftArmwear.isHidden = false;
        if (entityIn.isSneaking()) {
            GlStateManager.translate(0.0f, 0.2f, 0.0f);
        }
        this.bipedRightArm.render(scale);
        this.bipedRightArmwear.render(scale);
        this.bipedLeftArm.render(scale);
        this.bipedLeftArmwear.render(scale);
        GlStateManager.scale(-1.0f, 1.0f, 1.0f);
        GlStateManager.disableCull();
        GlStateManager.popMatrix();
    }
    
    public Map<Integer, CosmeticRenderer<CosmeticData>> getCosmeticRenderers() {
        return this.cosmeticRenderers;
    }
}
