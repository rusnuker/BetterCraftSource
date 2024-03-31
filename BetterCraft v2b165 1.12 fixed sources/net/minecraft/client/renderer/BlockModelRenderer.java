// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.renderer;

import optifine.ListQuadsOverlay;
import optifine.BetterSnow;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.block.Block;
import javax.annotation.Nullable;
import net.minecraft.util.math.Vec3d;
import java.util.BitSet;
import net.minecraft.client.renderer.texture.TextureUtil;
import optifine.CustomColors;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.block.model.BakedQuad;
import java.util.List;
import net.minecraft.util.EnumFacing;
import optifine.RenderEnv;
import net.minecraft.util.ReportedException;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.CrashReport;
import optifine.BlockModelCustomizer;
import shadersmod.client.SVertexBuilder;
import optifine.Config;
import optifine.ReflectorForge;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.world.IBlockAccess;
import optifine.Reflector;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.client.renderer.color.BlockColors;

public class BlockModelRenderer
{
    private final BlockColors blockColors;
    private static float aoLightValueOpaque;
    private static final BlockRenderLayer[] OVERLAY_LAYERS;
    
    static {
        BlockModelRenderer.aoLightValueOpaque = 0.2f;
        OVERLAY_LAYERS = new BlockRenderLayer[] { BlockRenderLayer.CUTOUT, BlockRenderLayer.CUTOUT_MIPPED, BlockRenderLayer.TRANSLUCENT };
    }
    
    public BlockModelRenderer(final BlockColors blockColorsIn) {
        this.blockColors = blockColorsIn;
        if (Reflector.ForgeModContainer_forgeLightPipelineEnabled.exists()) {
            Reflector.setFieldValue(Reflector.ForgeModContainer_forgeLightPipelineEnabled, false);
        }
    }
    
    public boolean renderModel(final IBlockAccess blockAccessIn, final IBakedModel modelIn, final IBlockState blockStateIn, final BlockPos blockPosIn, final BufferBuilder buffer, final boolean checkSides) {
        return this.renderModel(blockAccessIn, modelIn, blockStateIn, blockPosIn, buffer, checkSides, MathHelper.getPositionRandom(blockPosIn));
    }
    
    public boolean renderModel(final IBlockAccess worldIn, IBakedModel modelIn, final IBlockState stateIn, final BlockPos posIn, final BufferBuilder buffer, final boolean checkSides, long rand) {
        final boolean flag = Minecraft.isAmbientOcclusionEnabled() && ReflectorForge.getLightValue(stateIn, worldIn, posIn) == 0 && modelIn.isAmbientOcclusion();
        try {
            if (Config.isShaders()) {
                SVertexBuilder.pushEntity(stateIn, posIn, worldIn, buffer);
            }
            if (!Config.isAlternateBlocks()) {
                rand = 0L;
            }
            final RenderEnv renderenv = buffer.getRenderEnv(worldIn, stateIn, posIn);
            modelIn = BlockModelCustomizer.getRenderModel(modelIn, stateIn, renderenv);
            final boolean flag2 = flag ? this.renderModelSmooth(worldIn, modelIn, stateIn, posIn, buffer, checkSides, rand) : this.renderModelFlat(worldIn, modelIn, stateIn, posIn, buffer, checkSides, rand);
            if (flag2) {
                this.renderOverlayModels(worldIn, modelIn, stateIn, posIn, buffer, checkSides, rand, renderenv, flag);
            }
            if (Config.isShaders()) {
                SVertexBuilder.popEntity(buffer);
            }
            return flag2;
        }
        catch (final Throwable throwable1) {
            final CrashReport crashreport = CrashReport.makeCrashReport(throwable1, "Tesselating block model");
            final CrashReportCategory crashreportcategory = crashreport.makeCategory("Block model being tesselated");
            CrashReportCategory.addBlockInfo(crashreportcategory, posIn, stateIn);
            crashreportcategory.addCrashSection("Using AO", flag);
            throw new ReportedException(crashreport);
        }
    }
    
    public boolean renderModelSmooth(final IBlockAccess worldIn, final IBakedModel modelIn, final IBlockState stateIn, final BlockPos posIn, final BufferBuilder buffer, final boolean checkSides, final long rand) {
        boolean flag = false;
        final RenderEnv renderenv = buffer.getRenderEnv(worldIn, stateIn, posIn);
        EnumFacing[] values;
        for (int length = (values = EnumFacing.VALUES).length, i = 0; i < length; ++i) {
            final EnumFacing enumfacing = values[i];
            List<BakedQuad> list = modelIn.getQuads(stateIn, enumfacing, rand);
            if (!list.isEmpty() && (!checkSides || stateIn.shouldSideBeRendered(worldIn, posIn, enumfacing))) {
                list = BlockModelCustomizer.getRenderQuads(list, worldIn, stateIn, posIn, enumfacing, rand, renderenv);
                this.renderQuadsSmooth(worldIn, stateIn, posIn, buffer, list, renderenv);
                flag = true;
            }
        }
        List<BakedQuad> list2 = modelIn.getQuads(stateIn, null, rand);
        if (!list2.isEmpty()) {
            list2 = BlockModelCustomizer.getRenderQuads(list2, worldIn, stateIn, posIn, null, rand, renderenv);
            this.renderQuadsSmooth(worldIn, stateIn, posIn, buffer, list2, renderenv);
            flag = true;
        }
        return flag;
    }
    
    public boolean renderModelFlat(final IBlockAccess worldIn, final IBakedModel modelIn, final IBlockState stateIn, final BlockPos posIn, final BufferBuilder buffer, final boolean checkSides, final long rand) {
        boolean flag = false;
        final RenderEnv renderenv = buffer.getRenderEnv(worldIn, stateIn, posIn);
        EnumFacing[] values;
        for (int length = (values = EnumFacing.VALUES).length, j = 0; j < length; ++j) {
            final EnumFacing enumfacing = values[j];
            List<BakedQuad> list = modelIn.getQuads(stateIn, enumfacing, rand);
            if (!list.isEmpty() && (!checkSides || stateIn.shouldSideBeRendered(worldIn, posIn, enumfacing))) {
                final int i = stateIn.getPackedLightmapCoords(worldIn, posIn.offset(enumfacing));
                list = BlockModelCustomizer.getRenderQuads(list, worldIn, stateIn, posIn, enumfacing, rand, renderenv);
                this.renderQuadsFlat(worldIn, stateIn, posIn, i, false, buffer, list, renderenv);
                flag = true;
            }
        }
        List<BakedQuad> list2 = modelIn.getQuads(stateIn, null, rand);
        if (!list2.isEmpty()) {
            list2 = BlockModelCustomizer.getRenderQuads(list2, worldIn, stateIn, posIn, null, rand, renderenv);
            this.renderQuadsFlat(worldIn, stateIn, posIn, -1, true, buffer, list2, renderenv);
            flag = true;
        }
        return flag;
    }
    
    private void renderQuadsSmooth(final IBlockAccess p_renderQuadsSmooth_1_, final IBlockState p_renderQuadsSmooth_2_, final BlockPos p_renderQuadsSmooth_3_, final BufferBuilder p_renderQuadsSmooth_4_, final List<BakedQuad> p_renderQuadsSmooth_5_, final RenderEnv p_renderQuadsSmooth_6_) {
        final float[] afloat = p_renderQuadsSmooth_6_.getQuadBounds();
        final BitSet bitset = p_renderQuadsSmooth_6_.getBoundsFlags();
        final AmbientOcclusionFace blockmodelrenderer$ambientocclusionface = p_renderQuadsSmooth_6_.getAoFace();
        final Vec3d vec3d = p_renderQuadsSmooth_2_.func_191059_e(p_renderQuadsSmooth_1_, p_renderQuadsSmooth_3_);
        final double d0 = p_renderQuadsSmooth_3_.getX() + vec3d.xCoord;
        final double d2 = p_renderQuadsSmooth_3_.getY() + vec3d.yCoord;
        final double d3 = p_renderQuadsSmooth_3_.getZ() + vec3d.zCoord;
        for (int i = 0, j = p_renderQuadsSmooth_5_.size(); i < j; ++i) {
            final BakedQuad bakedquad = p_renderQuadsSmooth_5_.get(i);
            this.fillQuadBounds(p_renderQuadsSmooth_2_, bakedquad.getVertexData(), bakedquad.getFace(), afloat, bitset);
            blockmodelrenderer$ambientocclusionface.updateVertexBrightness(p_renderQuadsSmooth_1_, p_renderQuadsSmooth_2_, p_renderQuadsSmooth_3_, bakedquad.getFace(), afloat, bitset);
            if (p_renderQuadsSmooth_4_.isMultiTexture()) {
                p_renderQuadsSmooth_4_.addVertexData(bakedquad.getVertexDataSingle());
                p_renderQuadsSmooth_4_.putSprite(bakedquad.getSprite());
            }
            else {
                p_renderQuadsSmooth_4_.addVertexData(bakedquad.getVertexData());
            }
            p_renderQuadsSmooth_4_.putBrightness4(blockmodelrenderer$ambientocclusionface.vertexBrightness[0], blockmodelrenderer$ambientocclusionface.vertexBrightness[1], blockmodelrenderer$ambientocclusionface.vertexBrightness[2], blockmodelrenderer$ambientocclusionface.vertexBrightness[3]);
            if (bakedquad.shouldApplyDiffuseLighting()) {
                final float f = FaceBakery.getFaceBrightness(bakedquad.getFace());
                final float[] access$1;
                float[] afloat2 = access$1 = blockmodelrenderer$ambientocclusionface.vertexColorMultiplier;
                final int n = 0;
                access$1[n] *= f;
                final float[] access$2;
                afloat2 = (access$2 = blockmodelrenderer$ambientocclusionface.vertexColorMultiplier);
                final int n2 = 1;
                access$2[n2] *= f;
                final float[] access$3;
                afloat2 = (access$3 = blockmodelrenderer$ambientocclusionface.vertexColorMultiplier);
                final int n3 = 2;
                access$3[n3] *= f;
                final float[] access$4;
                afloat2 = (access$4 = blockmodelrenderer$ambientocclusionface.vertexColorMultiplier);
                final int n4 = 3;
                access$4[n4] *= f;
            }
            final int l = CustomColors.getColorMultiplier(bakedquad, p_renderQuadsSmooth_2_, p_renderQuadsSmooth_1_, p_renderQuadsSmooth_3_, p_renderQuadsSmooth_6_);
            if (!bakedquad.hasTintIndex() && l == -1) {
                p_renderQuadsSmooth_4_.putColorMultiplier(blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[0], blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[0], blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[0], 4);
                p_renderQuadsSmooth_4_.putColorMultiplier(blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[1], blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[1], blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[1], 3);
                p_renderQuadsSmooth_4_.putColorMultiplier(blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[2], blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[2], blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[2], 2);
                p_renderQuadsSmooth_4_.putColorMultiplier(blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[3], blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[3], blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[3], 1);
            }
            else {
                int k;
                if ((k = l) == -1) {
                    k = this.blockColors.colorMultiplier(p_renderQuadsSmooth_2_, p_renderQuadsSmooth_1_, p_renderQuadsSmooth_3_, bakedquad.getTintIndex());
                }
                if (EntityRenderer.anaglyphEnable) {
                    k = TextureUtil.anaglyphColor(k);
                }
                final float f2 = (k >> 16 & 0xFF) / 255.0f;
                final float f3 = (k >> 8 & 0xFF) / 255.0f;
                final float f4 = (k & 0xFF) / 255.0f;
                p_renderQuadsSmooth_4_.putColorMultiplier(blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[0] * f2, blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[0] * f3, blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[0] * f4, 4);
                p_renderQuadsSmooth_4_.putColorMultiplier(blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[1] * f2, blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[1] * f3, blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[1] * f4, 3);
                p_renderQuadsSmooth_4_.putColorMultiplier(blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[2] * f2, blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[2] * f3, blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[2] * f4, 2);
                p_renderQuadsSmooth_4_.putColorMultiplier(blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[3] * f2, blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[3] * f3, blockmodelrenderer$ambientocclusionface.vertexColorMultiplier[3] * f4, 1);
            }
            p_renderQuadsSmooth_4_.putPosition(d0, d2, d3);
        }
    }
    
    private void fillQuadBounds(final IBlockState stateIn, final int[] vertexData, final EnumFacing face, @Nullable final float[] quadBounds, final BitSet boundsFlags) {
        float f = 32.0f;
        float f2 = 32.0f;
        float f3 = 32.0f;
        float f4 = -32.0f;
        float f5 = -32.0f;
        float f6 = -32.0f;
        final int i = vertexData.length / 4;
        for (int j = 0; j < 4; ++j) {
            final float f7 = Float.intBitsToFloat(vertexData[j * i]);
            final float f8 = Float.intBitsToFloat(vertexData[j * i + 1]);
            final float f9 = Float.intBitsToFloat(vertexData[j * i + 2]);
            f = Math.min(f, f7);
            f2 = Math.min(f2, f8);
            f3 = Math.min(f3, f9);
            f4 = Math.max(f4, f7);
            f5 = Math.max(f5, f8);
            f6 = Math.max(f6, f9);
        }
        if (quadBounds != null) {
            quadBounds[EnumFacing.WEST.getIndex()] = f;
            quadBounds[EnumFacing.EAST.getIndex()] = f4;
            quadBounds[EnumFacing.DOWN.getIndex()] = f2;
            quadBounds[EnumFacing.UP.getIndex()] = f5;
            quadBounds[EnumFacing.NORTH.getIndex()] = f3;
            quadBounds[EnumFacing.SOUTH.getIndex()] = f6;
            final int k = EnumFacing.VALUES.length;
            quadBounds[EnumFacing.WEST.getIndex() + k] = 1.0f - f;
            quadBounds[EnumFacing.EAST.getIndex() + k] = 1.0f - f4;
            quadBounds[EnumFacing.DOWN.getIndex() + k] = 1.0f - f2;
            quadBounds[EnumFacing.UP.getIndex() + k] = 1.0f - f5;
            quadBounds[EnumFacing.NORTH.getIndex() + k] = 1.0f - f3;
            quadBounds[EnumFacing.SOUTH.getIndex() + k] = 1.0f - f6;
        }
        final float f10 = 1.0E-4f;
        final float f11 = 0.9999f;
        switch (face) {
            case DOWN: {
                boundsFlags.set(1, f >= 1.0E-4f || f3 >= 1.0E-4f || f4 <= 0.9999f || f6 <= 0.9999f);
                boundsFlags.set(0, (f2 < 1.0E-4f || stateIn.isFullCube()) && f2 == f5);
                break;
            }
            case UP: {
                boundsFlags.set(1, f >= 1.0E-4f || f3 >= 1.0E-4f || f4 <= 0.9999f || f6 <= 0.9999f);
                boundsFlags.set(0, (f5 > 0.9999f || stateIn.isFullCube()) && f2 == f5);
                break;
            }
            case NORTH: {
                boundsFlags.set(1, f >= 1.0E-4f || f2 >= 1.0E-4f || f4 <= 0.9999f || f5 <= 0.9999f);
                boundsFlags.set(0, (f3 < 1.0E-4f || stateIn.isFullCube()) && f3 == f6);
                break;
            }
            case SOUTH: {
                boundsFlags.set(1, f >= 1.0E-4f || f2 >= 1.0E-4f || f4 <= 0.9999f || f5 <= 0.9999f);
                boundsFlags.set(0, (f6 > 0.9999f || stateIn.isFullCube()) && f3 == f6);
                break;
            }
            case WEST: {
                boundsFlags.set(1, f2 >= 1.0E-4f || f3 >= 1.0E-4f || f5 <= 0.9999f || f6 <= 0.9999f);
                boundsFlags.set(0, (f < 1.0E-4f || stateIn.isFullCube()) && f == f4);
                break;
            }
            case EAST: {
                boundsFlags.set(1, f2 >= 1.0E-4f || f3 >= 1.0E-4f || f5 <= 0.9999f || f6 <= 0.9999f);
                boundsFlags.set(0, (f4 > 0.9999f || stateIn.isFullCube()) && f == f4);
                break;
            }
        }
    }
    
    private void renderQuadsFlat(final IBlockAccess p_renderQuadsFlat_1_, final IBlockState p_renderQuadsFlat_2_, final BlockPos p_renderQuadsFlat_3_, int p_renderQuadsFlat_4_, final boolean p_renderQuadsFlat_5_, final BufferBuilder p_renderQuadsFlat_6_, final List<BakedQuad> p_renderQuadsFlat_7_, final RenderEnv p_renderQuadsFlat_8_) {
        final BitSet bitset = p_renderQuadsFlat_8_.getBoundsFlags();
        final Vec3d vec3d = p_renderQuadsFlat_2_.func_191059_e(p_renderQuadsFlat_1_, p_renderQuadsFlat_3_);
        final double d0 = p_renderQuadsFlat_3_.getX() + vec3d.xCoord;
        final double d2 = p_renderQuadsFlat_3_.getY() + vec3d.yCoord;
        final double d3 = p_renderQuadsFlat_3_.getZ() + vec3d.zCoord;
        for (int i = 0, j = p_renderQuadsFlat_7_.size(); i < j; ++i) {
            final BakedQuad bakedquad = p_renderQuadsFlat_7_.get(i);
            if (p_renderQuadsFlat_5_) {
                this.fillQuadBounds(p_renderQuadsFlat_2_, bakedquad.getVertexData(), bakedquad.getFace(), null, bitset);
                final BlockPos blockpos = bitset.get(0) ? p_renderQuadsFlat_3_.offset(bakedquad.getFace()) : p_renderQuadsFlat_3_;
                p_renderQuadsFlat_4_ = p_renderQuadsFlat_2_.getPackedLightmapCoords(p_renderQuadsFlat_1_, blockpos);
            }
            if (p_renderQuadsFlat_6_.isMultiTexture()) {
                p_renderQuadsFlat_6_.addVertexData(bakedquad.getVertexDataSingle());
                p_renderQuadsFlat_6_.putSprite(bakedquad.getSprite());
            }
            else {
                p_renderQuadsFlat_6_.addVertexData(bakedquad.getVertexData());
            }
            p_renderQuadsFlat_6_.putBrightness4(p_renderQuadsFlat_4_, p_renderQuadsFlat_4_, p_renderQuadsFlat_4_, p_renderQuadsFlat_4_);
            final int l = CustomColors.getColorMultiplier(bakedquad, p_renderQuadsFlat_2_, p_renderQuadsFlat_1_, p_renderQuadsFlat_3_, p_renderQuadsFlat_8_);
            if (!bakedquad.hasTintIndex() && l == -1) {
                if (bakedquad.shouldApplyDiffuseLighting()) {
                    final float f4 = FaceBakery.getFaceBrightness(bakedquad.getFace());
                    p_renderQuadsFlat_6_.putColorMultiplier(f4, f4, f4, 4);
                    p_renderQuadsFlat_6_.putColorMultiplier(f4, f4, f4, 3);
                    p_renderQuadsFlat_6_.putColorMultiplier(f4, f4, f4, 2);
                    p_renderQuadsFlat_6_.putColorMultiplier(f4, f4, f4, 1);
                }
            }
            else {
                int k;
                if ((k = l) == -1) {
                    k = this.blockColors.colorMultiplier(p_renderQuadsFlat_2_, p_renderQuadsFlat_1_, p_renderQuadsFlat_3_, bakedquad.getTintIndex());
                }
                if (EntityRenderer.anaglyphEnable) {
                    k = TextureUtil.anaglyphColor(k);
                }
                float f5 = (k >> 16 & 0xFF) / 255.0f;
                float f6 = (k >> 8 & 0xFF) / 255.0f;
                float f7 = (k & 0xFF) / 255.0f;
                if (bakedquad.shouldApplyDiffuseLighting()) {
                    final float f8 = FaceBakery.getFaceBrightness(bakedquad.getFace());
                    f5 *= f8;
                    f6 *= f8;
                    f7 *= f8;
                }
                p_renderQuadsFlat_6_.putColorMultiplier(f5, f6, f7, 4);
                p_renderQuadsFlat_6_.putColorMultiplier(f5, f6, f7, 3);
                p_renderQuadsFlat_6_.putColorMultiplier(f5, f6, f7, 2);
                p_renderQuadsFlat_6_.putColorMultiplier(f5, f6, f7, 1);
            }
            p_renderQuadsFlat_6_.putPosition(d0, d2, d3);
        }
    }
    
    public void renderModelBrightnessColor(final IBakedModel bakedModel, final float p_178262_2_, final float red, final float green, final float blue) {
        this.renderModelBrightnessColor(null, bakedModel, p_178262_2_, red, green, blue);
    }
    
    public void renderModelBrightnessColor(final IBlockState state, final IBakedModel p_187495_2_, final float p_187495_3_, final float p_187495_4_, final float p_187495_5_, final float p_187495_6_) {
        EnumFacing[] values;
        for (int length = (values = EnumFacing.VALUES).length, i = 0; i < length; ++i) {
            final EnumFacing enumfacing = values[i];
            this.renderModelBrightnessColorQuads(p_187495_3_, p_187495_4_, p_187495_5_, p_187495_6_, p_187495_2_.getQuads(state, enumfacing, 0L));
        }
        this.renderModelBrightnessColorQuads(p_187495_3_, p_187495_4_, p_187495_5_, p_187495_6_, p_187495_2_.getQuads(state, null, 0L));
    }
    
    public void renderModelBrightness(final IBakedModel model, final IBlockState state, final float brightness, final boolean p_178266_4_) {
        final Block block = state.getBlock();
        GlStateManager.rotate(90.0f, 0.0f, 1.0f, 0.0f);
        int i = this.blockColors.colorMultiplier(state, null, null, 0);
        if (EntityRenderer.anaglyphEnable) {
            i = TextureUtil.anaglyphColor(i);
        }
        final float f = (i >> 16 & 0xFF) / 255.0f;
        final float f2 = (i >> 8 & 0xFF) / 255.0f;
        final float f3 = (i & 0xFF) / 255.0f;
        if (!p_178266_4_) {
            GlStateManager.color(brightness, brightness, brightness, 1.0f);
        }
        this.renderModelBrightnessColor(state, model, brightness, f, f2, f3);
    }
    
    private void renderModelBrightnessColorQuads(final float brightness, final float red, final float green, final float blue, final List<BakedQuad> listQuads) {
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        for (int i = 0, j = listQuads.size(); i < j; ++i) {
            final BakedQuad bakedquad = listQuads.get(i);
            bufferbuilder.begin(7, DefaultVertexFormats.ITEM);
            bufferbuilder.addVertexData(bakedquad.getVertexData());
            if (bakedquad.hasTintIndex()) {
                bufferbuilder.putColorRGB_F4(red * brightness, green * brightness, blue * brightness);
            }
            else {
                bufferbuilder.putColorRGB_F4(brightness, brightness, brightness);
            }
            final Vec3i vec3i = bakedquad.getFace().getDirectionVec();
            bufferbuilder.putNormal((float)vec3i.getX(), (float)vec3i.getY(), (float)vec3i.getZ());
            tessellator.draw();
        }
    }
    
    public static float fixAoLightValue(final float p_fixAoLightValue_0_) {
        return (p_fixAoLightValue_0_ == 0.2f) ? BlockModelRenderer.aoLightValueOpaque : p_fixAoLightValue_0_;
    }
    
    public static void updateAoLightValue() {
        BlockModelRenderer.aoLightValueOpaque = 1.0f - Config.getAmbientOcclusionLevel() * 0.8f;
    }
    
    private void renderOverlayModels(final IBlockAccess p_renderOverlayModels_1_, final IBakedModel p_renderOverlayModels_2_, final IBlockState p_renderOverlayModels_3_, final BlockPos p_renderOverlayModels_4_, final BufferBuilder p_renderOverlayModels_5_, final boolean p_renderOverlayModels_6_, final long p_renderOverlayModels_7_, final RenderEnv p_renderOverlayModels_9_, final boolean p_renderOverlayModels_10_) {
        if (p_renderOverlayModels_9_.isOverlaysRendered()) {
            for (int i = 0; i < BlockModelRenderer.OVERLAY_LAYERS.length; ++i) {
                final BlockRenderLayer blockrenderlayer = BlockModelRenderer.OVERLAY_LAYERS[i];
                final ListQuadsOverlay listquadsoverlay = p_renderOverlayModels_9_.getListQuadsOverlay(blockrenderlayer);
                if (listquadsoverlay.size() > 0) {
                    final RegionRenderCacheBuilder regionrendercachebuilder = p_renderOverlayModels_9_.getRegionRenderCacheBuilder();
                    if (regionrendercachebuilder != null) {
                        final BufferBuilder bufferbuilder = regionrendercachebuilder.getWorldRendererByLayer(blockrenderlayer);
                        if (!bufferbuilder.isDrawing()) {
                            bufferbuilder.begin(7, DefaultVertexFormats.BLOCK);
                            bufferbuilder.setTranslation(p_renderOverlayModels_5_.getXOffset(), p_renderOverlayModels_5_.getYOffset(), p_renderOverlayModels_5_.getZOffset());
                        }
                        for (int j = 0; j < listquadsoverlay.size(); ++j) {
                            final BakedQuad bakedquad = listquadsoverlay.getQuad(j);
                            final List<BakedQuad> list = listquadsoverlay.getListQuadsSingle(bakedquad);
                            final IBlockState iblockstate = listquadsoverlay.getBlockState(j);
                            p_renderOverlayModels_9_.reset(p_renderOverlayModels_1_, iblockstate, p_renderOverlayModels_4_);
                            if (p_renderOverlayModels_10_) {
                                this.renderQuadsSmooth(p_renderOverlayModels_1_, iblockstate, p_renderOverlayModels_4_, bufferbuilder, list, p_renderOverlayModels_9_);
                            }
                            else {
                                final int k = iblockstate.getPackedLightmapCoords(p_renderOverlayModels_1_, p_renderOverlayModels_4_.offset(bakedquad.getFace()));
                                this.renderQuadsFlat(p_renderOverlayModels_1_, iblockstate, p_renderOverlayModels_4_, k, false, bufferbuilder, list, p_renderOverlayModels_9_);
                            }
                        }
                    }
                    listquadsoverlay.clear();
                }
            }
        }
        if (Config.isBetterSnow() && !p_renderOverlayModels_9_.isBreakingAnimation() && BetterSnow.shouldRender(p_renderOverlayModels_1_, p_renderOverlayModels_3_, p_renderOverlayModels_4_)) {
            final IBakedModel ibakedmodel = BetterSnow.getModelSnowLayer();
            final IBlockState iblockstate2 = BetterSnow.getStateSnowLayer();
            this.renderModel(p_renderOverlayModels_1_, ibakedmodel, iblockstate2, p_renderOverlayModels_4_, p_renderOverlayModels_5_, p_renderOverlayModels_6_, p_renderOverlayModels_7_);
        }
    }
    
    public enum EnumNeighborInfo
    {
        DOWN("DOWN", 0, new EnumFacing[] { EnumFacing.WEST, EnumFacing.EAST, EnumFacing.NORTH, EnumFacing.SOUTH }, 0.5f, true, new Orientation[] { Orientation.FLIP_WEST, Orientation.SOUTH, Orientation.FLIP_WEST, Orientation.FLIP_SOUTH, Orientation.WEST, Orientation.FLIP_SOUTH, Orientation.WEST, Orientation.SOUTH }, new Orientation[] { Orientation.FLIP_WEST, Orientation.NORTH, Orientation.FLIP_WEST, Orientation.FLIP_NORTH, Orientation.WEST, Orientation.FLIP_NORTH, Orientation.WEST, Orientation.NORTH }, new Orientation[] { Orientation.FLIP_EAST, Orientation.NORTH, Orientation.FLIP_EAST, Orientation.FLIP_NORTH, Orientation.EAST, Orientation.FLIP_NORTH, Orientation.EAST, Orientation.NORTH }, new Orientation[] { Orientation.FLIP_EAST, Orientation.SOUTH, Orientation.FLIP_EAST, Orientation.FLIP_SOUTH, Orientation.EAST, Orientation.FLIP_SOUTH, Orientation.EAST, Orientation.SOUTH }), 
        UP("UP", 1, new EnumFacing[] { EnumFacing.EAST, EnumFacing.WEST, EnumFacing.NORTH, EnumFacing.SOUTH }, 1.0f, true, new Orientation[] { Orientation.EAST, Orientation.SOUTH, Orientation.EAST, Orientation.FLIP_SOUTH, Orientation.FLIP_EAST, Orientation.FLIP_SOUTH, Orientation.FLIP_EAST, Orientation.SOUTH }, new Orientation[] { Orientation.EAST, Orientation.NORTH, Orientation.EAST, Orientation.FLIP_NORTH, Orientation.FLIP_EAST, Orientation.FLIP_NORTH, Orientation.FLIP_EAST, Orientation.NORTH }, new Orientation[] { Orientation.WEST, Orientation.NORTH, Orientation.WEST, Orientation.FLIP_NORTH, Orientation.FLIP_WEST, Orientation.FLIP_NORTH, Orientation.FLIP_WEST, Orientation.NORTH }, new Orientation[] { Orientation.WEST, Orientation.SOUTH, Orientation.WEST, Orientation.FLIP_SOUTH, Orientation.FLIP_WEST, Orientation.FLIP_SOUTH, Orientation.FLIP_WEST, Orientation.SOUTH }), 
        NORTH("NORTH", 2, new EnumFacing[] { EnumFacing.UP, EnumFacing.DOWN, EnumFacing.EAST, EnumFacing.WEST }, 0.8f, true, new Orientation[] { Orientation.UP, Orientation.FLIP_WEST, Orientation.UP, Orientation.WEST, Orientation.FLIP_UP, Orientation.WEST, Orientation.FLIP_UP, Orientation.FLIP_WEST }, new Orientation[] { Orientation.UP, Orientation.FLIP_EAST, Orientation.UP, Orientation.EAST, Orientation.FLIP_UP, Orientation.EAST, Orientation.FLIP_UP, Orientation.FLIP_EAST }, new Orientation[] { Orientation.DOWN, Orientation.FLIP_EAST, Orientation.DOWN, Orientation.EAST, Orientation.FLIP_DOWN, Orientation.EAST, Orientation.FLIP_DOWN, Orientation.FLIP_EAST }, new Orientation[] { Orientation.DOWN, Orientation.FLIP_WEST, Orientation.DOWN, Orientation.WEST, Orientation.FLIP_DOWN, Orientation.WEST, Orientation.FLIP_DOWN, Orientation.FLIP_WEST }), 
        SOUTH("SOUTH", 3, new EnumFacing[] { EnumFacing.WEST, EnumFacing.EAST, EnumFacing.DOWN, EnumFacing.UP }, 0.8f, true, new Orientation[] { Orientation.UP, Orientation.FLIP_WEST, Orientation.FLIP_UP, Orientation.FLIP_WEST, Orientation.FLIP_UP, Orientation.WEST, Orientation.UP, Orientation.WEST }, new Orientation[] { Orientation.DOWN, Orientation.FLIP_WEST, Orientation.FLIP_DOWN, Orientation.FLIP_WEST, Orientation.FLIP_DOWN, Orientation.WEST, Orientation.DOWN, Orientation.WEST }, new Orientation[] { Orientation.DOWN, Orientation.FLIP_EAST, Orientation.FLIP_DOWN, Orientation.FLIP_EAST, Orientation.FLIP_DOWN, Orientation.EAST, Orientation.DOWN, Orientation.EAST }, new Orientation[] { Orientation.UP, Orientation.FLIP_EAST, Orientation.FLIP_UP, Orientation.FLIP_EAST, Orientation.FLIP_UP, Orientation.EAST, Orientation.UP, Orientation.EAST }), 
        WEST("WEST", 4, new EnumFacing[] { EnumFacing.UP, EnumFacing.DOWN, EnumFacing.NORTH, EnumFacing.SOUTH }, 0.6f, true, new Orientation[] { Orientation.UP, Orientation.SOUTH, Orientation.UP, Orientation.FLIP_SOUTH, Orientation.FLIP_UP, Orientation.FLIP_SOUTH, Orientation.FLIP_UP, Orientation.SOUTH }, new Orientation[] { Orientation.UP, Orientation.NORTH, Orientation.UP, Orientation.FLIP_NORTH, Orientation.FLIP_UP, Orientation.FLIP_NORTH, Orientation.FLIP_UP, Orientation.NORTH }, new Orientation[] { Orientation.DOWN, Orientation.NORTH, Orientation.DOWN, Orientation.FLIP_NORTH, Orientation.FLIP_DOWN, Orientation.FLIP_NORTH, Orientation.FLIP_DOWN, Orientation.NORTH }, new Orientation[] { Orientation.DOWN, Orientation.SOUTH, Orientation.DOWN, Orientation.FLIP_SOUTH, Orientation.FLIP_DOWN, Orientation.FLIP_SOUTH, Orientation.FLIP_DOWN, Orientation.SOUTH }), 
        EAST("EAST", 5, new EnumFacing[] { EnumFacing.DOWN, EnumFacing.UP, EnumFacing.NORTH, EnumFacing.SOUTH }, 0.6f, true, new Orientation[] { Orientation.FLIP_DOWN, Orientation.SOUTH, Orientation.FLIP_DOWN, Orientation.FLIP_SOUTH, Orientation.DOWN, Orientation.FLIP_SOUTH, Orientation.DOWN, Orientation.SOUTH }, new Orientation[] { Orientation.FLIP_DOWN, Orientation.NORTH, Orientation.FLIP_DOWN, Orientation.FLIP_NORTH, Orientation.DOWN, Orientation.FLIP_NORTH, Orientation.DOWN, Orientation.NORTH }, new Orientation[] { Orientation.FLIP_UP, Orientation.NORTH, Orientation.FLIP_UP, Orientation.FLIP_NORTH, Orientation.UP, Orientation.FLIP_NORTH, Orientation.UP, Orientation.NORTH }, new Orientation[] { Orientation.FLIP_UP, Orientation.SOUTH, Orientation.FLIP_UP, Orientation.FLIP_SOUTH, Orientation.UP, Orientation.FLIP_SOUTH, Orientation.UP, Orientation.SOUTH });
        
        private final EnumFacing[] corners;
        private final float shadeWeight;
        private final boolean doNonCubicWeight;
        private final Orientation[] vert0Weights;
        private final Orientation[] vert1Weights;
        private final Orientation[] vert2Weights;
        private final Orientation[] vert3Weights;
        private static final EnumNeighborInfo[] VALUES;
        
        static {
            (VALUES = new EnumNeighborInfo[6])[EnumFacing.DOWN.getIndex()] = EnumNeighborInfo.DOWN;
            EnumNeighborInfo.VALUES[EnumFacing.UP.getIndex()] = EnumNeighborInfo.UP;
            EnumNeighborInfo.VALUES[EnumFacing.NORTH.getIndex()] = EnumNeighborInfo.NORTH;
            EnumNeighborInfo.VALUES[EnumFacing.SOUTH.getIndex()] = EnumNeighborInfo.SOUTH;
            EnumNeighborInfo.VALUES[EnumFacing.WEST.getIndex()] = EnumNeighborInfo.WEST;
            EnumNeighborInfo.VALUES[EnumFacing.EAST.getIndex()] = EnumNeighborInfo.EAST;
        }
        
        private EnumNeighborInfo(final String s, final int n, final EnumFacing[] p_i46236_3_, final float p_i46236_4_, final boolean p_i46236_5_, final Orientation[] p_i46236_6_, final Orientation[] p_i46236_7_, final Orientation[] p_i46236_8_, final Orientation[] p_i46236_9_) {
            this.corners = p_i46236_3_;
            this.shadeWeight = p_i46236_4_;
            this.doNonCubicWeight = p_i46236_5_;
            this.vert0Weights = p_i46236_6_;
            this.vert1Weights = p_i46236_7_;
            this.vert2Weights = p_i46236_8_;
            this.vert3Weights = p_i46236_9_;
        }
        
        public static EnumNeighborInfo getNeighbourInfo(final EnumFacing p_178273_0_) {
            return EnumNeighborInfo.VALUES[p_178273_0_.getIndex()];
        }
    }
    
    public enum Orientation
    {
        DOWN("DOWN", 0, EnumFacing.DOWN, false), 
        UP("UP", 1, EnumFacing.UP, false), 
        NORTH("NORTH", 2, EnumFacing.NORTH, false), 
        SOUTH("SOUTH", 3, EnumFacing.SOUTH, false), 
        WEST("WEST", 4, EnumFacing.WEST, false), 
        EAST("EAST", 5, EnumFacing.EAST, false), 
        FLIP_DOWN("FLIP_DOWN", 6, EnumFacing.DOWN, true), 
        FLIP_UP("FLIP_UP", 7, EnumFacing.UP, true), 
        FLIP_NORTH("FLIP_NORTH", 8, EnumFacing.NORTH, true), 
        FLIP_SOUTH("FLIP_SOUTH", 9, EnumFacing.SOUTH, true), 
        FLIP_WEST("FLIP_WEST", 10, EnumFacing.WEST, true), 
        FLIP_EAST("FLIP_EAST", 11, EnumFacing.EAST, true);
        
        private final int shape;
        
        private Orientation(final String s, final int n, final EnumFacing p_i46233_3_, final boolean p_i46233_4_) {
            this.shape = p_i46233_3_.getIndex() + (p_i46233_4_ ? EnumFacing.values().length : 0);
        }
    }
    
    enum VertexTranslations
    {
        DOWN("DOWN", 0, 0, 1, 2, 3), 
        UP("UP", 1, 2, 3, 0, 1), 
        NORTH("NORTH", 2, 3, 0, 1, 2), 
        SOUTH("SOUTH", 3, 0, 1, 2, 3), 
        WEST("WEST", 4, 3, 0, 1, 2), 
        EAST("EAST", 5, 1, 2, 3, 0);
        
        private final int vert0;
        private final int vert1;
        private final int vert2;
        private final int vert3;
        private static final VertexTranslations[] VALUES;
        
        static {
            (VALUES = new VertexTranslations[6])[EnumFacing.DOWN.getIndex()] = VertexTranslations.DOWN;
            VertexTranslations.VALUES[EnumFacing.UP.getIndex()] = VertexTranslations.UP;
            VertexTranslations.VALUES[EnumFacing.NORTH.getIndex()] = VertexTranslations.NORTH;
            VertexTranslations.VALUES[EnumFacing.SOUTH.getIndex()] = VertexTranslations.SOUTH;
            VertexTranslations.VALUES[EnumFacing.WEST.getIndex()] = VertexTranslations.WEST;
            VertexTranslations.VALUES[EnumFacing.EAST.getIndex()] = VertexTranslations.EAST;
        }
        
        private VertexTranslations(final String s, final int n, final int p_i46234_3_, final int p_i46234_4_, final int p_i46234_5_, final int p_i46234_6_) {
            this.vert0 = p_i46234_3_;
            this.vert1 = p_i46234_4_;
            this.vert2 = p_i46234_5_;
            this.vert3 = p_i46234_6_;
        }
        
        public static VertexTranslations getVertexTranslations(final EnumFacing p_178184_0_) {
            return VertexTranslations.VALUES[p_178184_0_.getIndex()];
        }
    }
    
    public static class AmbientOcclusionFace
    {
        private final float[] vertexColorMultiplier;
        private final int[] vertexBrightness;
        
        public AmbientOcclusionFace() {
            this.vertexColorMultiplier = new float[4];
            this.vertexBrightness = new int[4];
        }
        
        public AmbientOcclusionFace(final BlockModelRenderer p_i46235_1_) {
            this.vertexColorMultiplier = new float[4];
            this.vertexBrightness = new int[4];
        }
        
        public void updateVertexBrightness(final IBlockAccess worldIn, final IBlockState state, final BlockPos centerPos, final EnumFacing direction, final float[] faceShape, final BitSet shapeState) {
            final BlockPos blockpos = shapeState.get(0) ? centerPos.offset(direction) : centerPos;
            final BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();
            final EnumNeighborInfo blockmodelrenderer$enumneighborinfo = EnumNeighborInfo.getNeighbourInfo(direction);
            final BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos2 = BlockPos.PooledMutableBlockPos.retain(blockpos).move(blockmodelrenderer$enumneighborinfo.corners[0]);
            final BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos3 = BlockPos.PooledMutableBlockPos.retain(blockpos).move(blockmodelrenderer$enumneighborinfo.corners[1]);
            final BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos4 = BlockPos.PooledMutableBlockPos.retain(blockpos).move(blockmodelrenderer$enumneighborinfo.corners[2]);
            final BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos5 = BlockPos.PooledMutableBlockPos.retain(blockpos).move(blockmodelrenderer$enumneighborinfo.corners[3]);
            final int i = state.getPackedLightmapCoords(worldIn, blockpos$pooledmutableblockpos2);
            final int j = state.getPackedLightmapCoords(worldIn, blockpos$pooledmutableblockpos3);
            final int k = state.getPackedLightmapCoords(worldIn, blockpos$pooledmutableblockpos4);
            final int l = state.getPackedLightmapCoords(worldIn, blockpos$pooledmutableblockpos5);
            float f = worldIn.getBlockState(blockpos$pooledmutableblockpos2).getAmbientOcclusionLightValue();
            float f2 = worldIn.getBlockState(blockpos$pooledmutableblockpos3).getAmbientOcclusionLightValue();
            float f3 = worldIn.getBlockState(blockpos$pooledmutableblockpos4).getAmbientOcclusionLightValue();
            float f4 = worldIn.getBlockState(blockpos$pooledmutableblockpos5).getAmbientOcclusionLightValue();
            f = BlockModelRenderer.fixAoLightValue(f);
            f2 = BlockModelRenderer.fixAoLightValue(f2);
            f3 = BlockModelRenderer.fixAoLightValue(f3);
            f4 = BlockModelRenderer.fixAoLightValue(f4);
            final boolean flag = worldIn.getBlockState(blockpos$pooledmutableblockpos.setPos(blockpos$pooledmutableblockpos2).move(direction)).isTranslucent();
            final boolean flag2 = worldIn.getBlockState(blockpos$pooledmutableblockpos.setPos(blockpos$pooledmutableblockpos3).move(direction)).isTranslucent();
            final boolean flag3 = worldIn.getBlockState(blockpos$pooledmutableblockpos.setPos(blockpos$pooledmutableblockpos4).move(direction)).isTranslucent();
            final boolean flag4 = worldIn.getBlockState(blockpos$pooledmutableblockpos.setPos(blockpos$pooledmutableblockpos5).move(direction)).isTranslucent();
            float f5;
            int i2;
            if (!flag3 && !flag) {
                f5 = f;
                i2 = i;
            }
            else {
                final BlockPos blockpos2 = blockpos$pooledmutableblockpos.setPos(blockpos$pooledmutableblockpos2).move(blockmodelrenderer$enumneighborinfo.corners[2]);
                f5 = worldIn.getBlockState(blockpos2).getAmbientOcclusionLightValue();
                f5 = BlockModelRenderer.fixAoLightValue(f5);
                i2 = state.getPackedLightmapCoords(worldIn, blockpos2);
            }
            float f6;
            int j2;
            if (!flag4 && !flag) {
                f6 = f;
                j2 = i;
            }
            else {
                final BlockPos blockpos3 = blockpos$pooledmutableblockpos.setPos(blockpos$pooledmutableblockpos2).move(blockmodelrenderer$enumneighborinfo.corners[3]);
                f6 = worldIn.getBlockState(blockpos3).getAmbientOcclusionLightValue();
                f6 = BlockModelRenderer.fixAoLightValue(f6);
                j2 = state.getPackedLightmapCoords(worldIn, blockpos3);
            }
            float f7;
            int k2;
            if (!flag3 && !flag2) {
                f7 = f2;
                k2 = j;
            }
            else {
                final BlockPos blockpos4 = blockpos$pooledmutableblockpos.setPos(blockpos$pooledmutableblockpos3).move(blockmodelrenderer$enumneighborinfo.corners[2]);
                f7 = worldIn.getBlockState(blockpos4).getAmbientOcclusionLightValue();
                f7 = BlockModelRenderer.fixAoLightValue(f7);
                k2 = state.getPackedLightmapCoords(worldIn, blockpos4);
            }
            float f8;
            int l2;
            if (!flag4 && !flag2) {
                f8 = f2;
                l2 = j;
            }
            else {
                final BlockPos blockpos5 = blockpos$pooledmutableblockpos.setPos(blockpos$pooledmutableblockpos3).move(blockmodelrenderer$enumneighborinfo.corners[3]);
                f8 = worldIn.getBlockState(blockpos5).getAmbientOcclusionLightValue();
                f8 = BlockModelRenderer.fixAoLightValue(f8);
                l2 = state.getPackedLightmapCoords(worldIn, blockpos5);
            }
            int i3 = state.getPackedLightmapCoords(worldIn, centerPos);
            if (shapeState.get(0) || !worldIn.getBlockState(centerPos.offset(direction)).isOpaqueCube()) {
                i3 = state.getPackedLightmapCoords(worldIn, centerPos.offset(direction));
            }
            float f9 = shapeState.get(0) ? worldIn.getBlockState(blockpos).getAmbientOcclusionLightValue() : worldIn.getBlockState(centerPos).getAmbientOcclusionLightValue();
            f9 = BlockModelRenderer.fixAoLightValue(f9);
            final VertexTranslations blockmodelrenderer$vertextranslations = VertexTranslations.getVertexTranslations(direction);
            blockpos$pooledmutableblockpos.release();
            blockpos$pooledmutableblockpos2.release();
            blockpos$pooledmutableblockpos3.release();
            blockpos$pooledmutableblockpos4.release();
            blockpos$pooledmutableblockpos5.release();
            if (shapeState.get(1) && blockmodelrenderer$enumneighborinfo.doNonCubicWeight) {
                final float f10 = (f4 + f + f6 + f9) * 0.25f;
                final float f11 = (f3 + f + f5 + f9) * 0.25f;
                final float f12 = (f3 + f2 + f7 + f9) * 0.25f;
                final float f13 = (f4 + f2 + f8 + f9) * 0.25f;
                final float f14 = faceShape[blockmodelrenderer$enumneighborinfo.vert0Weights[0].shape] * faceShape[blockmodelrenderer$enumneighborinfo.vert0Weights[1].shape];
                final float f15 = faceShape[blockmodelrenderer$enumneighborinfo.vert0Weights[2].shape] * faceShape[blockmodelrenderer$enumneighborinfo.vert0Weights[3].shape];
                final float f16 = faceShape[blockmodelrenderer$enumneighborinfo.vert0Weights[4].shape] * faceShape[blockmodelrenderer$enumneighborinfo.vert0Weights[5].shape];
                final float f17 = faceShape[blockmodelrenderer$enumneighborinfo.vert0Weights[6].shape] * faceShape[blockmodelrenderer$enumneighborinfo.vert0Weights[7].shape];
                final float f18 = faceShape[blockmodelrenderer$enumneighborinfo.vert1Weights[0].shape] * faceShape[blockmodelrenderer$enumneighborinfo.vert1Weights[1].shape];
                final float f19 = faceShape[blockmodelrenderer$enumneighborinfo.vert1Weights[2].shape] * faceShape[blockmodelrenderer$enumneighborinfo.vert1Weights[3].shape];
                final float f20 = faceShape[blockmodelrenderer$enumneighborinfo.vert1Weights[4].shape] * faceShape[blockmodelrenderer$enumneighborinfo.vert1Weights[5].shape];
                final float f21 = faceShape[blockmodelrenderer$enumneighborinfo.vert1Weights[6].shape] * faceShape[blockmodelrenderer$enumneighborinfo.vert1Weights[7].shape];
                final float f22 = faceShape[blockmodelrenderer$enumneighborinfo.vert2Weights[0].shape] * faceShape[blockmodelrenderer$enumneighborinfo.vert2Weights[1].shape];
                final float f23 = faceShape[blockmodelrenderer$enumneighborinfo.vert2Weights[2].shape] * faceShape[blockmodelrenderer$enumneighborinfo.vert2Weights[3].shape];
                final float f24 = faceShape[blockmodelrenderer$enumneighborinfo.vert2Weights[4].shape] * faceShape[blockmodelrenderer$enumneighborinfo.vert2Weights[5].shape];
                final float f25 = faceShape[blockmodelrenderer$enumneighborinfo.vert2Weights[6].shape] * faceShape[blockmodelrenderer$enumneighborinfo.vert2Weights[7].shape];
                final float f26 = faceShape[blockmodelrenderer$enumneighborinfo.vert3Weights[0].shape] * faceShape[blockmodelrenderer$enumneighborinfo.vert3Weights[1].shape];
                final float f27 = faceShape[blockmodelrenderer$enumneighborinfo.vert3Weights[2].shape] * faceShape[blockmodelrenderer$enumneighborinfo.vert3Weights[3].shape];
                final float f28 = faceShape[blockmodelrenderer$enumneighborinfo.vert3Weights[4].shape] * faceShape[blockmodelrenderer$enumneighborinfo.vert3Weights[5].shape];
                final float f29 = faceShape[blockmodelrenderer$enumneighborinfo.vert3Weights[6].shape] * faceShape[blockmodelrenderer$enumneighborinfo.vert3Weights[7].shape];
                this.vertexColorMultiplier[blockmodelrenderer$vertextranslations.vert0] = f10 * f14 + f11 * f15 + f12 * f16 + f13 * f17;
                this.vertexColorMultiplier[blockmodelrenderer$vertextranslations.vert1] = f10 * f18 + f11 * f19 + f12 * f20 + f13 * f21;
                this.vertexColorMultiplier[blockmodelrenderer$vertextranslations.vert2] = f10 * f22 + f11 * f23 + f12 * f24 + f13 * f25;
                this.vertexColorMultiplier[blockmodelrenderer$vertextranslations.vert3] = f10 * f26 + f11 * f27 + f12 * f28 + f13 * f29;
                final int i4 = this.getAoBrightness(l, i, j2, i3);
                final int j3 = this.getAoBrightness(k, i, i2, i3);
                final int k3 = this.getAoBrightness(k, j, k2, i3);
                final int l3 = this.getAoBrightness(l, j, l2, i3);
                this.vertexBrightness[blockmodelrenderer$vertextranslations.vert0] = this.getVertexBrightness(i4, j3, k3, l3, f14, f15, f16, f17);
                this.vertexBrightness[blockmodelrenderer$vertextranslations.vert1] = this.getVertexBrightness(i4, j3, k3, l3, f18, f19, f20, f21);
                this.vertexBrightness[blockmodelrenderer$vertextranslations.vert2] = this.getVertexBrightness(i4, j3, k3, l3, f22, f23, f24, f25);
                this.vertexBrightness[blockmodelrenderer$vertextranslations.vert3] = this.getVertexBrightness(i4, j3, k3, l3, f26, f27, f28, f29);
            }
            else {
                final float f30 = (f4 + f + f6 + f9) * 0.25f;
                final float f31 = (f3 + f + f5 + f9) * 0.25f;
                final float f32 = (f3 + f2 + f7 + f9) * 0.25f;
                final float f33 = (f4 + f2 + f8 + f9) * 0.25f;
                this.vertexBrightness[blockmodelrenderer$vertextranslations.vert0] = this.getAoBrightness(l, i, j2, i3);
                this.vertexBrightness[blockmodelrenderer$vertextranslations.vert1] = this.getAoBrightness(k, i, i2, i3);
                this.vertexBrightness[blockmodelrenderer$vertextranslations.vert2] = this.getAoBrightness(k, j, k2, i3);
                this.vertexBrightness[blockmodelrenderer$vertextranslations.vert3] = this.getAoBrightness(l, j, l2, i3);
                this.vertexColorMultiplier[blockmodelrenderer$vertextranslations.vert0] = f30;
                this.vertexColorMultiplier[blockmodelrenderer$vertextranslations.vert1] = f31;
                this.vertexColorMultiplier[blockmodelrenderer$vertextranslations.vert2] = f32;
                this.vertexColorMultiplier[blockmodelrenderer$vertextranslations.vert3] = f33;
            }
        }
        
        private int getAoBrightness(int br1, int br2, int br3, final int br4) {
            if (br1 == 0) {
                br1 = br4;
            }
            if (br2 == 0) {
                br2 = br4;
            }
            if (br3 == 0) {
                br3 = br4;
            }
            return br1 + br2 + br3 + br4 >> 2 & 0xFF00FF;
        }
        
        private int getVertexBrightness(final int p_178203_1_, final int p_178203_2_, final int p_178203_3_, final int p_178203_4_, final float p_178203_5_, final float p_178203_6_, final float p_178203_7_, final float p_178203_8_) {
            final int i = (int)((p_178203_1_ >> 16 & 0xFF) * p_178203_5_ + (p_178203_2_ >> 16 & 0xFF) * p_178203_6_ + (p_178203_3_ >> 16 & 0xFF) * p_178203_7_ + (p_178203_4_ >> 16 & 0xFF) * p_178203_8_) & 0xFF;
            final int j = (int)((p_178203_1_ & 0xFF) * p_178203_5_ + (p_178203_2_ & 0xFF) * p_178203_6_ + (p_178203_3_ & 0xFF) * p_178203_7_ + (p_178203_4_ & 0xFF) * p_178203_8_) & 0xFF;
            return i << 16 | j;
        }
    }
}
