// 
// Decompiled by Procyon v0.6.0
// 

package me.nzxtercode.bettercraft.client.misc.cosmetics.impl;

import net.minecraft.entity.Entity;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GLAllocation;
import java.nio.FloatBuffer;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.util.ResourceLocation;
import me.nzxtercode.bettercraft.client.misc.cosmetics.CosmeticBase;

public class CosmeticGalaxySkin extends CosmeticBase
{
    private static final ResourceLocation TEXTURE;
    private final RenderPlayer renderPlayer;
    private ModelPlayer playerModel;
    FloatBuffer field_147528_b;
    
    static {
        TEXTURE = new ResourceLocation("client/cosmetic/end_portal.png");
    }
    
    public CosmeticGalaxySkin(final RenderPlayer renderPlayer) {
        super(renderPlayer);
        this.field_147528_b = GLAllocation.createDirectFloatBuffer(16);
        this.renderPlayer = renderPlayer;
        this.playerModel = renderPlayer.getMainModel();
    }
    
    @Override
    public void render(final AbstractClientPlayer player, final float limbSwing, final float limbSwingAmount, final float partialTicks, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(1, 1);
        GlStateManager.texGen(GlStateManager.TexGen.S, 9217);
        GlStateManager.texGen(GlStateManager.TexGen.T, 9217);
        GlStateManager.texGen(GlStateManager.TexGen.R, 9217);
        GlStateManager.texGen(GlStateManager.TexGen.Q, 9216);
        GlStateManager.texGen(GlStateManager.TexGen.S, 9473, this.doFloatBuffer(1.0f, 0.0f, 0.0f, 0.0f));
        GlStateManager.texGen(GlStateManager.TexGen.T, 9473, this.doFloatBuffer(0.0f, 0.0f, 1.0f, 0.0f));
        GlStateManager.texGen(GlStateManager.TexGen.R, 9473, this.doFloatBuffer(0.0f, 0.0f, 0.0f, 1.0f));
        GlStateManager.texGen(GlStateManager.TexGen.Q, 9474, this.doFloatBuffer(0.0f, 1.0f, 0.0f, 0.0f));
        GlStateManager.enableTexGenCoord(GlStateManager.TexGen.S);
        GlStateManager.enableTexGenCoord(GlStateManager.TexGen.T);
        GlStateManager.enableTexGenCoord(GlStateManager.TexGen.R);
        GlStateManager.enableTexGenCoord(GlStateManager.TexGen.Q);
        this.playerModel = new ModelPlayer(0.4f, false);
        this.renderPlayer.bindTexture(CosmeticGalaxySkin.TEXTURE);
        this.playerModel.setModelAttributes(this.renderPlayer.getMainModel());
        this.playerModel.render(player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        GlStateManager.disableBlend();
        GlStateManager.disableTexGenCoord(GlStateManager.TexGen.S);
        GlStateManager.disableTexGenCoord(GlStateManager.TexGen.T);
        GlStateManager.disableTexGenCoord(GlStateManager.TexGen.R);
        GlStateManager.disableTexGenCoord(GlStateManager.TexGen.Q);
        GlStateManager.enableLighting();
    }
    
    private FloatBuffer doFloatBuffer(final float f, final float g, final float h, final float i) {
        this.field_147528_b.clear();
        this.field_147528_b.put(f).put(g).put(h).put(i);
        this.field_147528_b.flip();
        return this.field_147528_b;
    }
    
    @Override
    public int getId() {
        return 12;
    }
}
