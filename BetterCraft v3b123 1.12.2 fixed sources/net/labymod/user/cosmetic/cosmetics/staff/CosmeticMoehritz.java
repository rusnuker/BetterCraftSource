// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.user.cosmetic.cosmetics.staff;

import net.labymod.user.User;
import java.util.UUID;
import net.labymod.user.cosmetic.util.CosmeticData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.labymod.utils.DrawUtils;
import net.labymod.utils.ModColor;
import net.labymod.main.LabyMod;
import net.labymod.core.LabyModCore;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.labymod.main.Source;
import net.labymod.user.cosmetic.ModelCosmetics;
import net.labymod.user.cosmetic.CosmeticRenderer;

public class CosmeticMoehritz extends CosmeticRenderer<CosmeticMoehritzData>
{
    public static final int ID = 4;
    private boolean mc18;
    
    @Override
    public void addModels(final ModelCosmetics modelCosmetics, final float modelSize) {
        this.mc18 = Source.ABOUT_MC_VERSION.startsWith("1.8");
    }
    
    @Override
    public void setInvisible(final boolean invisible) {
    }
    
    @Override
    public void render(final ModelCosmetics modelCosmetics, final Entity entityIn, final CosmeticMoehritzData cosmeticData, final float scale, final float movementFactor, final float walkingSpeed, final float tickValue, final float firstRotationX, final float secondRotationX, final boolean canAnimate) {
        if (!this.mc18) {
            return;
        }
        GlStateManager.pushMatrix();
        final AbstractClientPlayer player = (AbstractClientPlayer)entityIn;
        LabyModCore.getMinecraft().setSecondLayerBit(player, 6, (byte)0);
        if (entityIn.isSneaking()) {
            GlStateManager.translate(0.0, 0.06, 0.0);
        }
        GlStateManager.rotate(firstRotationX, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(secondRotationX, 1.0f, 0.0f, 0.0f);
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.translate(-0.25, -0.5, -0.26);
        GlStateManager.scale(0.0625, 0.0625, 1.0);
        GlStateManager.rotate(180.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.translate(-8.0, 0.0, 0.004);
        int f = (int)(55.0f - (90.0f + entityIn.rotationPitch));
        if (f < 0) {
            f = 0;
        }
        final DrawUtils draw = LabyMod.getInstance().getDrawUtils();
        GlStateManager.translate(0.0, 0.0, entityIn.isSneaking() ? -0.008 : -0.011);
        DrawUtils.drawRect(1.899999976158142, 5.900000095367432, 6.099999904632568, 8.0, ModColor.toRGB(242 - f, 215 - f, 159 - f, 255));
        final int colorBrow = ModColor.toRGB(73, 33, 2, 255);
        final int colorEye = ModColor.toRGB(150, 0, 0, 255);
        final int colorMouth = ModColor.toRGB(30, 30, 30, 255);
        Gui.drawRect(1, 3, 3, 4, colorBrow);
        Gui.drawRect(5, 3, 7, 4, colorBrow);
        Entity targetEntity = Minecraft.getMinecraft().getRenderViewEntity();
        if (cosmeticData.targetPlayerUUID != null) {
            if (cosmeticData.lastTargetEntityCheck + 5000L < System.currentTimeMillis()) {
                cosmeticData.lastTargetEntityCheck = System.currentTimeMillis();
                cosmeticData.targetEntity = LabyModCore.getMinecraft().getWorld().getPlayerEntityByUUID(cosmeticData.targetPlayerUUID);
            }
            if (cosmeticData.targetEntity != null) {
                targetEntity = cosmeticData.targetEntity;
            }
        }
        double eyeX = 2.0;
        double movement = LabyModCore.getMinecraft().calculateEyeMovement(entityIn, targetEntity);
        if (movement > 0.5) {
            movement = 0.5;
        }
        if (movement < -0.5) {
            movement = -0.5;
        }
        eyeX -= movement;
        final int eyeY = 4;
        for (int i = 0; i < 2; ++i) {
            DrawUtils.drawRect(eyeX - 0.5, 4.0, eyeX + 0.5, 5.0, colorEye);
            eyeX += 4.0;
        }
        double smile = 0.0;
        if (targetEntity != null) {
            final double distanceSq = targetEntity.getDistanceSq(entityIn.posX, entityIn.posY, entityIn.posZ);
            smile += distanceSq / 50.0;
            if (smile > 1.0) {
                smile = 1.0;
            }
        }
        GlStateManager.translate(0.0, 0.0, 0.002);
        DrawUtils.drawRect(2.0, 6.0 + smile, 3.0, 7.0 + smile, colorMouth);
        DrawUtils.drawRect(5.0, 6.0 + smile, 6.0, 7.0 + smile, colorMouth);
        DrawUtils.drawRect(3.0, 7.0 - smile, 5.0, 8.0 - smile, colorMouth);
        GlStateManager.enableLighting();
        GlStateManager.enableAlpha();
        GlStateManager.popMatrix();
    }
    
    @Override
    public int getCosmeticId() {
        return 4;
    }
    
    @Override
    public String getCosmeticName() {
        return "Moehritz";
    }
    
    @Override
    public boolean isOfflineAvailable() {
        return false;
    }
    
    public static class CosmeticMoehritzData extends CosmeticData
    {
        protected UUID targetPlayerUUID;
        protected Entity targetEntity;
        protected long lastTargetEntityCheck;
        
        @Override
        public boolean isEnabled() {
            return true;
        }
        
        @Override
        public void loadData(final String[] data) throws Exception {
            this.targetPlayerUUID = UUID.fromString(data[0]);
        }
        
        @Override
        public void init(final User user) {
        }
    }
}
