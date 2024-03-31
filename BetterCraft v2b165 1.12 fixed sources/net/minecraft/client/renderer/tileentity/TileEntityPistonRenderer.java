// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.renderer.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockPistonExtension;
import net.minecraft.init.Blocks;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.tileentity.TileEntityPiston;

public class TileEntityPistonRenderer extends TileEntitySpecialRenderer<TileEntityPiston>
{
    private final BlockRendererDispatcher blockRenderer;
    
    public TileEntityPistonRenderer() {
        this.blockRenderer = Minecraft.getMinecraft().getBlockRendererDispatcher();
    }
    
    @Override
    public void func_192841_a(final TileEntityPiston p_192841_1_, final double p_192841_2_, final double p_192841_4_, final double p_192841_6_, final float p_192841_8_, final int p_192841_9_, final float p_192841_10_) {
        final BlockPos blockpos = p_192841_1_.getPos();
        IBlockState iblockstate = p_192841_1_.getPistonState();
        final Block block = iblockstate.getBlock();
        if (iblockstate.getMaterial() != Material.AIR && p_192841_1_.getProgress(p_192841_8_) < 1.0f) {
            final Tessellator tessellator = Tessellator.getInstance();
            final BufferBuilder bufferbuilder = tessellator.getBuffer();
            this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            RenderHelper.disableStandardItemLighting();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            GlStateManager.enableBlend();
            GlStateManager.disableCull();
            if (Minecraft.isAmbientOcclusionEnabled()) {
                GlStateManager.shadeModel(7425);
            }
            else {
                GlStateManager.shadeModel(7424);
            }
            bufferbuilder.begin(7, DefaultVertexFormats.BLOCK);
            bufferbuilder.setTranslation(p_192841_2_ - blockpos.getX() + p_192841_1_.getOffsetX(p_192841_8_), p_192841_4_ - blockpos.getY() + p_192841_1_.getOffsetY(p_192841_8_), p_192841_6_ - blockpos.getZ() + p_192841_1_.getOffsetZ(p_192841_8_));
            final World world = this.getWorld();
            if (block == Blocks.PISTON_HEAD && p_192841_1_.getProgress(p_192841_8_) <= 0.25f) {
                iblockstate = iblockstate.withProperty((IProperty<Comparable>)BlockPistonExtension.SHORT, true);
                this.renderStateModel(blockpos, iblockstate, bufferbuilder, world, true);
            }
            else if (p_192841_1_.shouldPistonHeadBeRendered() && !p_192841_1_.isExtending()) {
                final BlockPistonExtension.EnumPistonType blockpistonextension$enumpistontype = (block == Blocks.STICKY_PISTON) ? BlockPistonExtension.EnumPistonType.STICKY : BlockPistonExtension.EnumPistonType.DEFAULT;
                IBlockState iblockstate2 = Blocks.PISTON_HEAD.getDefaultState().withProperty(BlockPistonExtension.TYPE, blockpistonextension$enumpistontype).withProperty((IProperty<Comparable>)BlockPistonExtension.FACING, (EnumFacing)iblockstate.getValue((IProperty<V>)BlockPistonBase.FACING));
                iblockstate2 = iblockstate2.withProperty((IProperty<Comparable>)BlockPistonExtension.SHORT, p_192841_1_.getProgress(p_192841_8_) >= 0.5f);
                this.renderStateModel(blockpos, iblockstate2, bufferbuilder, world, true);
                bufferbuilder.setTranslation(p_192841_2_ - blockpos.getX(), p_192841_4_ - blockpos.getY(), p_192841_6_ - blockpos.getZ());
                iblockstate = iblockstate.withProperty((IProperty<Comparable>)BlockPistonBase.EXTENDED, true);
                this.renderStateModel(blockpos, iblockstate, bufferbuilder, world, true);
            }
            else {
                this.renderStateModel(blockpos, iblockstate, bufferbuilder, world, false);
            }
            bufferbuilder.setTranslation(0.0, 0.0, 0.0);
            tessellator.draw();
            RenderHelper.enableStandardItemLighting();
        }
    }
    
    private boolean renderStateModel(final BlockPos p_188186_1_, final IBlockState p_188186_2_, final BufferBuilder p_188186_3_, final World p_188186_4_, final boolean p_188186_5_) {
        return this.blockRenderer.getBlockModelRenderer().renderModel(p_188186_4_, this.blockRenderer.getModelForState(p_188186_2_), p_188186_2_, p_188186_1_, p_188186_3_, p_188186_5_);
    }
}
