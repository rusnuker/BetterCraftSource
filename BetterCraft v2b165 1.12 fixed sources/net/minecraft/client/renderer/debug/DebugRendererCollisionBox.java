// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.renderer.debug;

import java.util.Iterator;
import java.util.List;
import net.minecraft.world.World;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.Minecraft;

public class DebugRendererCollisionBox implements DebugRenderer.IDebugRenderer
{
    private final Minecraft field_191312_a;
    private EntityPlayer field_191313_b;
    private double field_191314_c;
    private double field_191315_d;
    private double field_191316_e;
    
    public DebugRendererCollisionBox(final Minecraft p_i47215_1_) {
        this.field_191312_a = p_i47215_1_;
    }
    
    @Override
    public void render(final float p_190060_1_, final long p_190060_2_) {
        this.field_191313_b = this.field_191312_a.player;
        this.field_191314_c = this.field_191313_b.lastTickPosX + (this.field_191313_b.posX - this.field_191313_b.lastTickPosX) * p_190060_1_;
        this.field_191315_d = this.field_191313_b.lastTickPosY + (this.field_191313_b.posY - this.field_191313_b.lastTickPosY) * p_190060_1_;
        this.field_191316_e = this.field_191313_b.lastTickPosZ + (this.field_191313_b.posZ - this.field_191313_b.lastTickPosZ) * p_190060_1_;
        final World world = this.field_191312_a.player.world;
        final List<AxisAlignedBB> list = world.getCollisionBoxes(this.field_191313_b, this.field_191313_b.getEntityBoundingBox().expandXyz(6.0));
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.glLineWidth(2.0f);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        for (final AxisAlignedBB axisalignedbb : list) {
            RenderGlobal.drawSelectionBoundingBox(axisalignedbb.expandXyz(0.002).offset(-this.field_191314_c, -this.field_191315_d, -this.field_191316_e), 1.0f, 1.0f, 1.0f, 1.0f);
        }
        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
}
