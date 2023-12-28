// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.renderer.tileentity;

import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.math.MathHelper;
import net.minecraft.tileentity.TileEntityEndGateway;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.tileentity.TileEntityEndPortal;
import net.minecraft.util.ResourceLocation;

public class TileEntityEndGatewayRenderer extends TileEntityEndPortalRenderer
{
    private static final ResourceLocation END_GATEWAY_BEAM_TEXTURE;
    
    static {
        END_GATEWAY_BEAM_TEXTURE = new ResourceLocation("textures/entity/end_gateway_beam.png");
    }
    
    @Override
    public void func_192841_a(final TileEntityEndPortal p_192841_1_, final double p_192841_2_, final double p_192841_4_, final double p_192841_6_, final float p_192841_8_, final int p_192841_9_, final float p_192841_10_) {
        GlStateManager.disableFog();
        final TileEntityEndGateway tileentityendgateway = (TileEntityEndGateway)p_192841_1_;
        if (tileentityendgateway.isSpawning() || tileentityendgateway.isCoolingDown()) {
            GlStateManager.alphaFunc(516, 0.1f);
            this.bindTexture(TileEntityEndGatewayRenderer.END_GATEWAY_BEAM_TEXTURE);
            float f = tileentityendgateway.isSpawning() ? tileentityendgateway.getSpawnPercent(p_192841_8_) : tileentityendgateway.getCooldownPercent(p_192841_8_);
            final double d0 = tileentityendgateway.isSpawning() ? (256.0 - p_192841_4_) : 50.0;
            f = MathHelper.sin(f * 3.1415927f);
            final int i = MathHelper.floor(f * d0);
            final float[] afloat = tileentityendgateway.isSpawning() ? EnumDyeColor.MAGENTA.func_193349_f() : EnumDyeColor.PURPLE.func_193349_f();
            TileEntityBeaconRenderer.renderBeamSegment(p_192841_2_, p_192841_4_, p_192841_6_, p_192841_8_, f, (double)tileentityendgateway.getWorld().getTotalWorldTime(), 0, i, afloat, 0.15, 0.175);
            TileEntityBeaconRenderer.renderBeamSegment(p_192841_2_, p_192841_4_, p_192841_6_, p_192841_8_, f, (double)tileentityendgateway.getWorld().getTotalWorldTime(), 0, -i, afloat, 0.15, 0.175);
        }
        super.func_192841_a(p_192841_1_, p_192841_2_, p_192841_4_, p_192841_6_, p_192841_8_, p_192841_9_, p_192841_10_);
        GlStateManager.enableFog();
    }
    
    @Override
    protected int func_191286_a(final double p_191286_1_) {
        return super.func_191286_a(p_191286_1_) + 1;
    }
    
    @Override
    protected float func_191287_c() {
        return 1.0f;
    }
}
