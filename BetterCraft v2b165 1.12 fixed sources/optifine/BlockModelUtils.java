// 
// Decompiled by Procyon v0.6.0
// 

package optifine;

import net.minecraft.util.math.BlockPos;
import net.minecraft.block.Block;
import net.minecraft.util.math.AxisAlignedBB;
import java.util.Iterator;
import net.minecraft.client.renderer.block.model.BakedQuadRetextured;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.block.model.BlockPartRotation;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.block.model.BlockPartFace;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import org.lwjgl.util.vector.Vector3f;
import java.util.Collection;
import net.minecraft.block.state.IBlockState;
import java.util.Map;
import net.minecraft.client.renderer.block.model.SimpleBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.client.renderer.block.model.BakedQuad;
import java.util.List;
import java.util.HashMap;
import net.minecraft.util.EnumFacing;
import java.util.ArrayList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.block.model.IBakedModel;

public class BlockModelUtils
{
    private static final float VERTEX_COORD_ACCURACY = 1.0E-6f;
    
    public static IBakedModel makeModelCube(final String p_makeModelCube_0_, final int p_makeModelCube_1_) {
        final TextureAtlasSprite textureatlassprite = Config.getMinecraft().getTextureMapBlocks().getAtlasSprite(p_makeModelCube_0_);
        return makeModelCube(textureatlassprite, p_makeModelCube_1_);
    }
    
    public static IBakedModel makeModelCube(final TextureAtlasSprite p_makeModelCube_0_, final int p_makeModelCube_1_) {
        final List list = new ArrayList();
        final EnumFacing[] aenumfacing = EnumFacing.VALUES;
        final Map<EnumFacing, List<BakedQuad>> map = new HashMap<EnumFacing, List<BakedQuad>>();
        for (int i = 0; i < aenumfacing.length; ++i) {
            final EnumFacing enumfacing = aenumfacing[i];
            final List list2 = new ArrayList();
            list2.add(makeBakedQuad(enumfacing, p_makeModelCube_0_, p_makeModelCube_1_));
            map.put(enumfacing, list2);
        }
        final ItemOverrideList itemoverridelist = new ItemOverrideList(new ArrayList<ItemOverride>());
        final IBakedModel ibakedmodel = new SimpleBakedModel(list, map, true, true, p_makeModelCube_0_, ItemCameraTransforms.DEFAULT, itemoverridelist);
        return ibakedmodel;
    }
    
    public static IBakedModel joinModelsCube(final IBakedModel p_joinModelsCube_0_, final IBakedModel p_joinModelsCube_1_) {
        final List<BakedQuad> list = new ArrayList<BakedQuad>();
        list.addAll(p_joinModelsCube_0_.getQuads(null, null, 0L));
        list.addAll(p_joinModelsCube_1_.getQuads(null, null, 0L));
        final EnumFacing[] aenumfacing = EnumFacing.VALUES;
        final Map<EnumFacing, List<BakedQuad>> map = new HashMap<EnumFacing, List<BakedQuad>>();
        for (int i = 0; i < aenumfacing.length; ++i) {
            final EnumFacing enumfacing = aenumfacing[i];
            final List list2 = new ArrayList();
            list2.addAll(p_joinModelsCube_0_.getQuads(null, enumfacing, 0L));
            list2.addAll(p_joinModelsCube_1_.getQuads(null, enumfacing, 0L));
            map.put(enumfacing, list2);
        }
        final boolean flag = p_joinModelsCube_0_.isAmbientOcclusion();
        final boolean flag2 = p_joinModelsCube_0_.isBuiltInRenderer();
        final TextureAtlasSprite textureatlassprite = p_joinModelsCube_0_.getParticleTexture();
        final ItemCameraTransforms itemcameratransforms = p_joinModelsCube_0_.getItemCameraTransforms();
        final ItemOverrideList itemoverridelist = p_joinModelsCube_0_.getOverrides();
        final IBakedModel ibakedmodel = new SimpleBakedModel(list, map, flag, flag2, textureatlassprite, itemcameratransforms, itemoverridelist);
        return ibakedmodel;
    }
    
    public static BakedQuad makeBakedQuad(final EnumFacing p_makeBakedQuad_0_, final TextureAtlasSprite p_makeBakedQuad_1_, final int p_makeBakedQuad_2_) {
        final Vector3f vector3f = new Vector3f(0.0f, 0.0f, 0.0f);
        final Vector3f vector3f2 = new Vector3f(16.0f, 16.0f, 16.0f);
        final BlockFaceUV blockfaceuv = new BlockFaceUV(new float[] { 0.0f, 0.0f, 16.0f, 16.0f }, 0);
        final BlockPartFace blockpartface = new BlockPartFace(p_makeBakedQuad_0_, p_makeBakedQuad_2_, "#" + p_makeBakedQuad_0_.getName(), blockfaceuv);
        final ModelRotation modelrotation = ModelRotation.X0_Y0;
        final BlockPartRotation blockpartrotation = null;
        final boolean flag = false;
        final boolean flag2 = true;
        final FaceBakery facebakery = new FaceBakery();
        final BakedQuad bakedquad = facebakery.makeBakedQuad(vector3f, vector3f2, blockpartface, p_makeBakedQuad_1_, p_makeBakedQuad_0_, modelrotation, blockpartrotation, flag, flag2);
        return bakedquad;
    }
    
    public static IBakedModel makeModel(final String p_makeModel_0_, final String p_makeModel_1_, final String p_makeModel_2_) {
        final TextureMap texturemap = Config.getMinecraft().getTextureMapBlocks();
        final TextureAtlasSprite textureatlassprite = texturemap.getSpriteSafe(p_makeModel_1_);
        final TextureAtlasSprite textureatlassprite2 = texturemap.getSpriteSafe(p_makeModel_2_);
        return makeModel(p_makeModel_0_, textureatlassprite, textureatlassprite2);
    }
    
    public static IBakedModel makeModel(final String p_makeModel_0_, final TextureAtlasSprite p_makeModel_1_, final TextureAtlasSprite p_makeModel_2_) {
        if (p_makeModel_1_ == null || p_makeModel_2_ == null) {
            return null;
        }
        final ModelManager modelmanager = Config.getModelManager();
        if (modelmanager == null) {
            return null;
        }
        final ModelResourceLocation modelresourcelocation = new ModelResourceLocation(p_makeModel_0_, "normal");
        final IBakedModel ibakedmodel = modelmanager.getModel(modelresourcelocation);
        if (ibakedmodel != null && ibakedmodel != modelmanager.getMissingModel()) {
            final IBakedModel ibakedmodel2 = ModelUtils.duplicateModel(ibakedmodel);
            final EnumFacing[] aenumfacing = EnumFacing.VALUES;
            for (int i = 0; i < aenumfacing.length; ++i) {
                final EnumFacing enumfacing = aenumfacing[i];
                final List<BakedQuad> list = ibakedmodel2.getQuads(null, enumfacing, 0L);
                replaceTexture(list, p_makeModel_1_, p_makeModel_2_);
            }
            final List<BakedQuad> list2 = ibakedmodel2.getQuads(null, null, 0L);
            replaceTexture(list2, p_makeModel_1_, p_makeModel_2_);
            return ibakedmodel2;
        }
        return null;
    }
    
    private static void replaceTexture(final List<BakedQuad> p_replaceTexture_0_, final TextureAtlasSprite p_replaceTexture_1_, final TextureAtlasSprite p_replaceTexture_2_) {
        final List<BakedQuad> list = new ArrayList<BakedQuad>();
        for (final BakedQuad bakedquad : p_replaceTexture_0_) {
            if (bakedquad.getSprite() != p_replaceTexture_1_) {
                list.add(bakedquad);
                break;
            }
            final BakedQuad bakedquad2 = new BakedQuadRetextured(bakedquad, p_replaceTexture_2_);
            list.add(bakedquad2);
        }
        p_replaceTexture_0_.clear();
        p_replaceTexture_0_.addAll(list);
    }
    
    public static void snapVertexPosition(final Vector3f p_snapVertexPosition_0_) {
        p_snapVertexPosition_0_.setX(snapVertexCoord(p_snapVertexPosition_0_.getX()));
        p_snapVertexPosition_0_.setY(snapVertexCoord(p_snapVertexPosition_0_.getY()));
        p_snapVertexPosition_0_.setZ(snapVertexCoord(p_snapVertexPosition_0_.getZ()));
    }
    
    private static float snapVertexCoord(final float p_snapVertexCoord_0_) {
        if (p_snapVertexCoord_0_ > -1.0E-6f && p_snapVertexCoord_0_ < 1.0E-6f) {
            return 0.0f;
        }
        return (p_snapVertexCoord_0_ > 0.999999f && p_snapVertexCoord_0_ < 1.000001f) ? 1.0f : p_snapVertexCoord_0_;
    }
    
    public static AxisAlignedBB getOffsetBoundingBox(final AxisAlignedBB p_getOffsetBoundingBox_0_, final Block.EnumOffsetType p_getOffsetBoundingBox_1_, final BlockPos p_getOffsetBoundingBox_2_) {
        final int i = p_getOffsetBoundingBox_2_.getX();
        final int j = p_getOffsetBoundingBox_2_.getZ();
        long k = (long)(i * 3129871) ^ j * 116129781L;
        k = k * k * 42317861L + k * 11L;
        final double d0 = ((k >> 16 & 0xFL) / 15.0f - 0.5) * 0.5;
        final double d2 = ((k >> 24 & 0xFL) / 15.0f - 0.5) * 0.5;
        double d3 = 0.0;
        if (p_getOffsetBoundingBox_1_ == Block.EnumOffsetType.XYZ) {
            d3 = ((k >> 20 & 0xFL) / 15.0f - 1.0) * 0.2;
        }
        return p_getOffsetBoundingBox_0_.offset(d0, d3, d2);
    }
}
