// 
// Decompiled by Procyon v0.6.0
// 

package optifine;

import java.util.Set;
import java.util.HashSet;
import java.util.Collection;
import java.util.ArrayList;
import java.io.InputStream;
import java.io.FileNotFoundException;
import java.util.Properties;
import java.util.Arrays;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.Minecraft;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.block.BlockGlass;
import net.minecraft.world.biome.Biome;
import net.minecraft.block.BlockQuartz;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.state.BlockStateBase;
import java.util.List;
import java.util.EnumMap;
import java.util.IdentityHashMap;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockStainedGlassPane;
import net.minecraft.block.BlockPane;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.init.Blocks;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.block.state.IBlockState;
import java.util.Map;

public class ConnectedTextures
{
    private static Map[] spriteQuadMaps;
    private static Map[] spriteQuadFullMaps;
    private static Map[][] spriteQuadCompactMaps;
    private static ConnectedProperties[][] blockProperties;
    private static ConnectedProperties[][] tileProperties;
    private static boolean multipass;
    protected static final int UNKNOWN = -1;
    protected static final int Y_NEG_DOWN = 0;
    protected static final int Y_POS_UP = 1;
    protected static final int Z_NEG_NORTH = 2;
    protected static final int Z_POS_SOUTH = 3;
    protected static final int X_NEG_WEST = 4;
    protected static final int X_POS_EAST = 5;
    private static final int Y_AXIS = 0;
    private static final int Z_AXIS = 1;
    private static final int X_AXIS = 2;
    public static final IBlockState AIR_DEFAULT_STATE;
    private static TextureAtlasSprite emptySprite;
    private static final BlockDir[] SIDES_Y_NEG_DOWN;
    private static final BlockDir[] SIDES_Y_POS_UP;
    private static final BlockDir[] SIDES_Z_NEG_NORTH;
    private static final BlockDir[] SIDES_Z_POS_SOUTH;
    private static final BlockDir[] SIDES_X_NEG_WEST;
    private static final BlockDir[] SIDES_X_POS_EAST;
    private static final BlockDir[] SIDES_Z_NEG_NORTH_Z_AXIS;
    private static final BlockDir[] SIDES_X_POS_EAST_X_AXIS;
    private static final BlockDir[] EDGES_Y_NEG_DOWN;
    private static final BlockDir[] EDGES_Y_POS_UP;
    private static final BlockDir[] EDGES_Z_NEG_NORTH;
    private static final BlockDir[] EDGES_Z_POS_SOUTH;
    private static final BlockDir[] EDGES_X_NEG_WEST;
    private static final BlockDir[] EDGES_X_POS_EAST;
    private static final BlockDir[] EDGES_Z_NEG_NORTH_Z_AXIS;
    private static final BlockDir[] EDGES_X_POS_EAST_X_AXIS;
    
    static {
        ConnectedTextures.spriteQuadMaps = null;
        ConnectedTextures.spriteQuadFullMaps = null;
        ConnectedTextures.spriteQuadCompactMaps = null;
        ConnectedTextures.blockProperties = null;
        ConnectedTextures.tileProperties = null;
        ConnectedTextures.multipass = false;
        AIR_DEFAULT_STATE = Blocks.AIR.getDefaultState();
        ConnectedTextures.emptySprite = null;
        SIDES_Y_NEG_DOWN = new BlockDir[] { BlockDir.WEST, BlockDir.EAST, BlockDir.NORTH, BlockDir.SOUTH };
        SIDES_Y_POS_UP = new BlockDir[] { BlockDir.WEST, BlockDir.EAST, BlockDir.SOUTH, BlockDir.NORTH };
        SIDES_Z_NEG_NORTH = new BlockDir[] { BlockDir.EAST, BlockDir.WEST, BlockDir.DOWN, BlockDir.UP };
        SIDES_Z_POS_SOUTH = new BlockDir[] { BlockDir.WEST, BlockDir.EAST, BlockDir.DOWN, BlockDir.UP };
        SIDES_X_NEG_WEST = new BlockDir[] { BlockDir.NORTH, BlockDir.SOUTH, BlockDir.DOWN, BlockDir.UP };
        SIDES_X_POS_EAST = new BlockDir[] { BlockDir.SOUTH, BlockDir.NORTH, BlockDir.DOWN, BlockDir.UP };
        SIDES_Z_NEG_NORTH_Z_AXIS = new BlockDir[] { BlockDir.WEST, BlockDir.EAST, BlockDir.UP, BlockDir.DOWN };
        SIDES_X_POS_EAST_X_AXIS = new BlockDir[] { BlockDir.NORTH, BlockDir.SOUTH, BlockDir.UP, BlockDir.DOWN };
        EDGES_Y_NEG_DOWN = new BlockDir[] { BlockDir.NORTH_EAST, BlockDir.NORTH_WEST, BlockDir.SOUTH_EAST, BlockDir.SOUTH_WEST };
        EDGES_Y_POS_UP = new BlockDir[] { BlockDir.SOUTH_EAST, BlockDir.SOUTH_WEST, BlockDir.NORTH_EAST, BlockDir.NORTH_WEST };
        EDGES_Z_NEG_NORTH = new BlockDir[] { BlockDir.DOWN_WEST, BlockDir.DOWN_EAST, BlockDir.UP_WEST, BlockDir.UP_EAST };
        EDGES_Z_POS_SOUTH = new BlockDir[] { BlockDir.DOWN_EAST, BlockDir.DOWN_WEST, BlockDir.UP_EAST, BlockDir.UP_WEST };
        EDGES_X_NEG_WEST = new BlockDir[] { BlockDir.DOWN_SOUTH, BlockDir.DOWN_NORTH, BlockDir.UP_SOUTH, BlockDir.UP_NORTH };
        EDGES_X_POS_EAST = new BlockDir[] { BlockDir.DOWN_NORTH, BlockDir.DOWN_SOUTH, BlockDir.UP_NORTH, BlockDir.UP_SOUTH };
        EDGES_Z_NEG_NORTH_Z_AXIS = new BlockDir[] { BlockDir.UP_EAST, BlockDir.UP_WEST, BlockDir.DOWN_EAST, BlockDir.DOWN_WEST };
        EDGES_X_POS_EAST_X_AXIS = new BlockDir[] { BlockDir.UP_SOUTH, BlockDir.UP_NORTH, BlockDir.DOWN_SOUTH, BlockDir.DOWN_NORTH };
    }
    
    public static synchronized BakedQuad[] getConnectedTexture(final IBlockAccess p_getConnectedTexture_0_, final IBlockState p_getConnectedTexture_1_, final BlockPos p_getConnectedTexture_2_, BakedQuad p_getConnectedTexture_3_, final RenderEnv p_getConnectedTexture_4_) {
        final TextureAtlasSprite textureatlassprite = p_getConnectedTexture_3_.getSprite();
        if (textureatlassprite == null) {
            return p_getConnectedTexture_4_.getArrayQuadsCtm(p_getConnectedTexture_3_);
        }
        final Block block = p_getConnectedTexture_1_.getBlock();
        if (skipConnectedTexture(p_getConnectedTexture_0_, p_getConnectedTexture_1_, p_getConnectedTexture_2_, p_getConnectedTexture_3_, p_getConnectedTexture_4_)) {
            p_getConnectedTexture_3_ = getQuad(ConnectedTextures.emptySprite, p_getConnectedTexture_3_);
            return p_getConnectedTexture_4_.getArrayQuadsCtm(p_getConnectedTexture_3_);
        }
        final EnumFacing enumfacing = p_getConnectedTexture_3_.getFace();
        final BakedQuad[] abakedquad = getConnectedTextureMultiPass(p_getConnectedTexture_0_, p_getConnectedTexture_1_, p_getConnectedTexture_2_, enumfacing, p_getConnectedTexture_3_, p_getConnectedTexture_4_);
        return abakedquad;
    }
    
    private static boolean skipConnectedTexture(final IBlockAccess p_skipConnectedTexture_0_, final IBlockState p_skipConnectedTexture_1_, final BlockPos p_skipConnectedTexture_2_, final BakedQuad p_skipConnectedTexture_3_, final RenderEnv p_skipConnectedTexture_4_) {
        final Block block = p_skipConnectedTexture_1_.getBlock();
        if (block instanceof BlockPane) {
            final EnumFacing enumfacing = p_skipConnectedTexture_3_.getFace();
            if (enumfacing != EnumFacing.UP && enumfacing != EnumFacing.DOWN) {
                return false;
            }
            if (!p_skipConnectedTexture_3_.isFaceQuad()) {
                return false;
            }
            final BlockPos blockpos = p_skipConnectedTexture_2_.offset(p_skipConnectedTexture_3_.getFace());
            IBlockState iblockstate = p_skipConnectedTexture_0_.getBlockState(blockpos);
            if (iblockstate.getBlock() != block) {
                return false;
            }
            if (block == Blocks.STAINED_GLASS_PANE && iblockstate.getValue(BlockStainedGlassPane.COLOR) != p_skipConnectedTexture_1_.getValue(BlockStainedGlassPane.COLOR)) {
                return false;
            }
            iblockstate = iblockstate.getActualState(p_skipConnectedTexture_0_, blockpos);
            final double d0 = p_skipConnectedTexture_3_.getMidX();
            if (d0 < 0.4) {
                if (iblockstate.getValue((IProperty<Boolean>)BlockPane.WEST)) {
                    return true;
                }
            }
            else if (d0 > 0.6) {
                if (iblockstate.getValue((IProperty<Boolean>)BlockPane.EAST)) {
                    return true;
                }
            }
            else {
                final double d2 = p_skipConnectedTexture_3_.getMidZ();
                if (d2 < 0.4) {
                    if (iblockstate.getValue((IProperty<Boolean>)BlockPane.NORTH)) {
                        return true;
                    }
                }
                else {
                    if (d2 <= 0.6) {
                        return true;
                    }
                    if (iblockstate.getValue((IProperty<Boolean>)BlockPane.SOUTH)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    protected static BakedQuad[] getQuads(final TextureAtlasSprite p_getQuads_0_, final BakedQuad p_getQuads_1_, final RenderEnv p_getQuads_2_) {
        if (p_getQuads_0_ == null) {
            return null;
        }
        final BakedQuad bakedquad = getQuad(p_getQuads_0_, p_getQuads_1_);
        final BakedQuad[] abakedquad = p_getQuads_2_.getArrayQuadsCtm(bakedquad);
        return abakedquad;
    }
    
    private static BakedQuad getQuad(final TextureAtlasSprite p_getQuad_0_, final BakedQuad p_getQuad_1_) {
        if (ConnectedTextures.spriteQuadMaps == null) {
            return p_getQuad_1_;
        }
        final int i = p_getQuad_0_.getIndexInMap();
        if (i >= 0 && i < ConnectedTextures.spriteQuadMaps.length) {
            Map map = ConnectedTextures.spriteQuadMaps[i];
            if (map == null) {
                map = new IdentityHashMap(1);
                ConnectedTextures.spriteQuadMaps[i] = map;
            }
            BakedQuad bakedquad = map.get(p_getQuad_1_);
            if (bakedquad == null) {
                bakedquad = makeSpriteQuad(p_getQuad_1_, p_getQuad_0_);
                map.put(p_getQuad_1_, bakedquad);
            }
            return bakedquad;
        }
        return p_getQuad_1_;
    }
    
    private static BakedQuad getQuadFull(final TextureAtlasSprite p_getQuadFull_0_, final BakedQuad p_getQuadFull_1_, final int p_getQuadFull_2_) {
        if (ConnectedTextures.spriteQuadFullMaps == null) {
            return p_getQuadFull_1_;
        }
        final int i = p_getQuadFull_0_.getIndexInMap();
        if (i >= 0 && i < ConnectedTextures.spriteQuadFullMaps.length) {
            Map map = ConnectedTextures.spriteQuadFullMaps[i];
            if (map == null) {
                map = new EnumMap(EnumFacing.class);
                ConnectedTextures.spriteQuadFullMaps[i] = map;
            }
            final EnumFacing enumfacing = p_getQuadFull_1_.getFace();
            BakedQuad bakedquad = map.get(enumfacing);
            if (bakedquad == null) {
                bakedquad = BlockModelUtils.makeBakedQuad(enumfacing, p_getQuadFull_0_, p_getQuadFull_2_);
                map.put(enumfacing, bakedquad);
            }
            return bakedquad;
        }
        return p_getQuadFull_1_;
    }
    
    private static BakedQuad makeSpriteQuad(final BakedQuad p_makeSpriteQuad_0_, final TextureAtlasSprite p_makeSpriteQuad_1_) {
        final int[] aint = p_makeSpriteQuad_0_.getVertexData().clone();
        final TextureAtlasSprite textureatlassprite = p_makeSpriteQuad_0_.getSprite();
        for (int i = 0; i < 4; ++i) {
            fixVertex(aint, i, textureatlassprite, p_makeSpriteQuad_1_);
        }
        final BakedQuad bakedquad = new BakedQuad(aint, p_makeSpriteQuad_0_.getTintIndex(), p_makeSpriteQuad_0_.getFace(), p_makeSpriteQuad_1_);
        return bakedquad;
    }
    
    private static void fixVertex(final int[] p_fixVertex_0_, final int p_fixVertex_1_, final TextureAtlasSprite p_fixVertex_2_, final TextureAtlasSprite p_fixVertex_3_) {
        final int i = p_fixVertex_0_.length / 4;
        final int j = i * p_fixVertex_1_;
        final float f = Float.intBitsToFloat(p_fixVertex_0_[j + 4]);
        final float f2 = Float.intBitsToFloat(p_fixVertex_0_[j + 4 + 1]);
        final double d0 = p_fixVertex_2_.getSpriteU16(f);
        final double d2 = p_fixVertex_2_.getSpriteV16(f2);
        p_fixVertex_0_[j + 4] = Float.floatToRawIntBits(p_fixVertex_3_.getInterpolatedU(d0));
        p_fixVertex_0_[j + 4 + 1] = Float.floatToRawIntBits(p_fixVertex_3_.getInterpolatedV(d2));
    }
    
    private static BakedQuad[] getConnectedTextureMultiPass(final IBlockAccess p_getConnectedTextureMultiPass_0_, final IBlockState p_getConnectedTextureMultiPass_1_, final BlockPos p_getConnectedTextureMultiPass_2_, final EnumFacing p_getConnectedTextureMultiPass_3_, final BakedQuad p_getConnectedTextureMultiPass_4_, final RenderEnv p_getConnectedTextureMultiPass_5_) {
        final BakedQuad[] abakedquad = getConnectedTextureSingle(p_getConnectedTextureMultiPass_0_, p_getConnectedTextureMultiPass_1_, p_getConnectedTextureMultiPass_2_, p_getConnectedTextureMultiPass_3_, p_getConnectedTextureMultiPass_4_, true, 0, p_getConnectedTextureMultiPass_5_);
        if (!ConnectedTextures.multipass) {
            return abakedquad;
        }
        if (abakedquad.length == 1 && abakedquad[0] == p_getConnectedTextureMultiPass_4_) {
            return abakedquad;
        }
        final List<BakedQuad> list = p_getConnectedTextureMultiPass_5_.getListQuadsCtmMultipass(abakedquad);
        for (int i = 0; i < list.size(); ++i) {
            BakedQuad bakedquad2;
            final BakedQuad bakedquad = bakedquad2 = list.get(i);
            for (int j = 0; j < 3; ++j) {
                final BakedQuad[] abakedquad2 = getConnectedTextureSingle(p_getConnectedTextureMultiPass_0_, p_getConnectedTextureMultiPass_1_, p_getConnectedTextureMultiPass_2_, p_getConnectedTextureMultiPass_3_, bakedquad2, false, j + 1, p_getConnectedTextureMultiPass_5_);
                if (abakedquad2.length != 1) {
                    break;
                }
                if (abakedquad2[0] == bakedquad2) {
                    break;
                }
                bakedquad2 = abakedquad2[0];
            }
            list.set(i, bakedquad2);
        }
        for (int k = 0; k < abakedquad.length; ++k) {
            abakedquad[k] = list.get(k);
        }
        return abakedquad;
    }
    
    public static BakedQuad[] getConnectedTextureSingle(final IBlockAccess p_getConnectedTextureSingle_0_, final IBlockState p_getConnectedTextureSingle_1_, final BlockPos p_getConnectedTextureSingle_2_, final EnumFacing p_getConnectedTextureSingle_3_, final BakedQuad p_getConnectedTextureSingle_4_, final boolean p_getConnectedTextureSingle_5_, final int p_getConnectedTextureSingle_6_, final RenderEnv p_getConnectedTextureSingle_7_) {
        final Block block = p_getConnectedTextureSingle_1_.getBlock();
        if (!(p_getConnectedTextureSingle_1_ instanceof BlockStateBase)) {
            return p_getConnectedTextureSingle_7_.getArrayQuadsCtm(p_getConnectedTextureSingle_4_);
        }
        final BlockStateBase blockstatebase = (BlockStateBase)p_getConnectedTextureSingle_1_;
        final TextureAtlasSprite textureatlassprite = p_getConnectedTextureSingle_4_.getSprite();
        if (ConnectedTextures.tileProperties != null) {
            final int i = textureatlassprite.getIndexInMap();
            if (i >= 0 && i < ConnectedTextures.tileProperties.length) {
                final ConnectedProperties[] aconnectedproperties = ConnectedTextures.tileProperties[i];
                if (aconnectedproperties != null) {
                    final int j = getSide(p_getConnectedTextureSingle_3_);
                    for (int k = 0; k < aconnectedproperties.length; ++k) {
                        final ConnectedProperties connectedproperties = aconnectedproperties[k];
                        if (connectedproperties != null && connectedproperties.matchesBlockId(blockstatebase.getBlockId())) {
                            final BakedQuad[] abakedquad = getConnectedTexture(connectedproperties, p_getConnectedTextureSingle_0_, blockstatebase, p_getConnectedTextureSingle_2_, j, p_getConnectedTextureSingle_4_, p_getConnectedTextureSingle_6_, p_getConnectedTextureSingle_7_);
                            if (abakedquad != null) {
                                return abakedquad;
                            }
                        }
                    }
                }
            }
        }
        if (ConnectedTextures.blockProperties != null && p_getConnectedTextureSingle_5_) {
            final int l = p_getConnectedTextureSingle_7_.getBlockId();
            if (l >= 0 && l < ConnectedTextures.blockProperties.length) {
                final ConnectedProperties[] aconnectedproperties2 = ConnectedTextures.blockProperties[l];
                if (aconnectedproperties2 != null) {
                    final int i2 = getSide(p_getConnectedTextureSingle_3_);
                    for (int j2 = 0; j2 < aconnectedproperties2.length; ++j2) {
                        final ConnectedProperties connectedproperties2 = aconnectedproperties2[j2];
                        if (connectedproperties2 != null && connectedproperties2.matchesIcon(textureatlassprite)) {
                            final BakedQuad[] abakedquad2 = getConnectedTexture(connectedproperties2, p_getConnectedTextureSingle_0_, blockstatebase, p_getConnectedTextureSingle_2_, i2, p_getConnectedTextureSingle_4_, p_getConnectedTextureSingle_6_, p_getConnectedTextureSingle_7_);
                            if (abakedquad2 != null) {
                                return abakedquad2;
                            }
                        }
                    }
                }
            }
        }
        return p_getConnectedTextureSingle_7_.getArrayQuadsCtm(p_getConnectedTextureSingle_4_);
    }
    
    public static int getSide(final EnumFacing p_getSide_0_) {
        if (p_getSide_0_ == null) {
            return -1;
        }
        switch (p_getSide_0_) {
            case DOWN: {
                return 0;
            }
            case UP: {
                return 1;
            }
            case EAST: {
                return 5;
            }
            case WEST: {
                return 4;
            }
            case NORTH: {
                return 2;
            }
            case SOUTH: {
                return 3;
            }
            default: {
                return -1;
            }
        }
    }
    
    private static EnumFacing getFacing(final int p_getFacing_0_) {
        switch (p_getFacing_0_) {
            case 0: {
                return EnumFacing.DOWN;
            }
            case 1: {
                return EnumFacing.UP;
            }
            case 2: {
                return EnumFacing.NORTH;
            }
            case 3: {
                return EnumFacing.SOUTH;
            }
            case 4: {
                return EnumFacing.WEST;
            }
            case 5: {
                return EnumFacing.EAST;
            }
            default: {
                return EnumFacing.UP;
            }
        }
    }
    
    private static BakedQuad[] getConnectedTexture(final ConnectedProperties p_getConnectedTexture_0_, final IBlockAccess p_getConnectedTexture_1_, final BlockStateBase p_getConnectedTexture_2_, final BlockPos p_getConnectedTexture_3_, final int p_getConnectedTexture_4_, final BakedQuad p_getConnectedTexture_5_, final int p_getConnectedTexture_6_, final RenderEnv p_getConnectedTexture_7_) {
        int i = 0;
        int k;
        final int j = k = p_getConnectedTexture_2_.getMetadata();
        final Block block = p_getConnectedTexture_2_.getBlock();
        if (block instanceof BlockRotatedPillar) {
            i = getWoodAxis(p_getConnectedTexture_4_, j);
            if (p_getConnectedTexture_0_.getMetadataMax() <= 3) {
                k = (j & 0x3);
            }
        }
        if (block instanceof BlockQuartz) {
            i = getQuartzAxis(p_getConnectedTexture_4_, j);
            if (p_getConnectedTexture_0_.getMetadataMax() <= 2 && k > 2) {
                k = 2;
            }
        }
        if (!p_getConnectedTexture_0_.matchesBlock(p_getConnectedTexture_2_.getBlockId(), k)) {
            return null;
        }
        if (p_getConnectedTexture_4_ >= 0 && p_getConnectedTexture_0_.faces != 63) {
            int l = p_getConnectedTexture_4_;
            if (i != 0) {
                l = fixSideByAxis(p_getConnectedTexture_4_, i);
            }
            if ((1 << l & p_getConnectedTexture_0_.faces) == 0x0) {
                return null;
            }
        }
        final int i2 = p_getConnectedTexture_3_.getY();
        if (i2 >= p_getConnectedTexture_0_.minHeight && i2 <= p_getConnectedTexture_0_.maxHeight) {
            if (p_getConnectedTexture_0_.biomes != null) {
                final Biome biome = p_getConnectedTexture_1_.getBiome(p_getConnectedTexture_3_);
                if (!p_getConnectedTexture_0_.matchesBiome(biome)) {
                    return null;
                }
            }
            final TextureAtlasSprite textureatlassprite = p_getConnectedTexture_5_.getSprite();
            switch (p_getConnectedTexture_0_.method) {
                case 1: {
                    return getQuads(getConnectedTextureCtm(p_getConnectedTexture_0_, p_getConnectedTexture_1_, p_getConnectedTexture_2_, p_getConnectedTexture_3_, i, p_getConnectedTexture_4_, textureatlassprite, j, p_getConnectedTexture_7_), p_getConnectedTexture_5_, p_getConnectedTexture_7_);
                }
                case 2: {
                    return getQuads(getConnectedTextureHorizontal(p_getConnectedTexture_0_, p_getConnectedTexture_1_, p_getConnectedTexture_2_, p_getConnectedTexture_3_, i, p_getConnectedTexture_4_, textureatlassprite, j), p_getConnectedTexture_5_, p_getConnectedTexture_7_);
                }
                case 3: {
                    return getQuads(getConnectedTextureTop(p_getConnectedTexture_0_, p_getConnectedTexture_1_, p_getConnectedTexture_2_, p_getConnectedTexture_3_, i, p_getConnectedTexture_4_, textureatlassprite, j), p_getConnectedTexture_5_, p_getConnectedTexture_7_);
                }
                case 4: {
                    return getQuads(getConnectedTextureRandom(p_getConnectedTexture_0_, p_getConnectedTexture_3_, p_getConnectedTexture_4_), p_getConnectedTexture_5_, p_getConnectedTexture_7_);
                }
                case 5: {
                    return getQuads(getConnectedTextureRepeat(p_getConnectedTexture_0_, p_getConnectedTexture_3_, p_getConnectedTexture_4_), p_getConnectedTexture_5_, p_getConnectedTexture_7_);
                }
                case 6: {
                    return getQuads(getConnectedTextureVertical(p_getConnectedTexture_0_, p_getConnectedTexture_1_, p_getConnectedTexture_2_, p_getConnectedTexture_3_, i, p_getConnectedTexture_4_, textureatlassprite, j), p_getConnectedTexture_5_, p_getConnectedTexture_7_);
                }
                case 7: {
                    return getQuads(getConnectedTextureFixed(p_getConnectedTexture_0_), p_getConnectedTexture_5_, p_getConnectedTexture_7_);
                }
                case 8: {
                    return getQuads(getConnectedTextureHorizontalVertical(p_getConnectedTexture_0_, p_getConnectedTexture_1_, p_getConnectedTexture_2_, p_getConnectedTexture_3_, i, p_getConnectedTexture_4_, textureatlassprite, j), p_getConnectedTexture_5_, p_getConnectedTexture_7_);
                }
                case 9: {
                    return getQuads(getConnectedTextureVerticalHorizontal(p_getConnectedTexture_0_, p_getConnectedTexture_1_, p_getConnectedTexture_2_, p_getConnectedTexture_3_, i, p_getConnectedTexture_4_, textureatlassprite, j), p_getConnectedTexture_5_, p_getConnectedTexture_7_);
                }
                case 10: {
                    if (p_getConnectedTexture_6_ == 0) {
                        return getConnectedTextureCtmCompact(p_getConnectedTexture_0_, p_getConnectedTexture_1_, p_getConnectedTexture_2_, p_getConnectedTexture_3_, i, p_getConnectedTexture_4_, p_getConnectedTexture_5_, j, p_getConnectedTexture_7_);
                    }
                    break;
                }
                case 11: {
                    return getConnectedTextureOverlay(p_getConnectedTexture_0_, p_getConnectedTexture_1_, p_getConnectedTexture_2_, p_getConnectedTexture_3_, i, p_getConnectedTexture_4_, p_getConnectedTexture_5_, j, p_getConnectedTexture_7_);
                }
            }
            return null;
        }
        return null;
    }
    
    private static int fixSideByAxis(final int p_fixSideByAxis_0_, final int p_fixSideByAxis_1_) {
        switch (p_fixSideByAxis_1_) {
            case 0: {
                return p_fixSideByAxis_0_;
            }
            case 1: {
                switch (p_fixSideByAxis_0_) {
                    case 0: {
                        return 2;
                    }
                    case 1: {
                        return 3;
                    }
                    case 2: {
                        return 1;
                    }
                    case 3: {
                        return 0;
                    }
                    default: {
                        return p_fixSideByAxis_0_;
                    }
                }
                break;
            }
            case 2: {
                switch (p_fixSideByAxis_0_) {
                    case 0: {
                        return 4;
                    }
                    case 1: {
                        return 5;
                    }
                    default: {
                        return p_fixSideByAxis_0_;
                    }
                    case 4: {
                        return 1;
                    }
                    case 5: {
                        return 0;
                    }
                }
                break;
            }
            default: {
                return p_fixSideByAxis_0_;
            }
        }
    }
    
    private static int getWoodAxis(final int p_getWoodAxis_0_, final int p_getWoodAxis_1_) {
        final int i = (p_getWoodAxis_1_ & 0xC) >> 2;
        switch (i) {
            case 1: {
                return 2;
            }
            case 2: {
                return 1;
            }
            default: {
                return 0;
            }
        }
    }
    
    private static int getQuartzAxis(final int p_getQuartzAxis_0_, final int p_getQuartzAxis_1_) {
        switch (p_getQuartzAxis_1_) {
            case 3: {
                return 2;
            }
            case 4: {
                return 1;
            }
            default: {
                return 0;
            }
        }
    }
    
    private static TextureAtlasSprite getConnectedTextureRandom(final ConnectedProperties p_getConnectedTextureRandom_0_, final BlockPos p_getConnectedTextureRandom_1_, final int p_getConnectedTextureRandom_2_) {
        if (p_getConnectedTextureRandom_0_.tileIcons.length == 1) {
            return p_getConnectedTextureRandom_0_.tileIcons[0];
        }
        final int i = p_getConnectedTextureRandom_2_ / p_getConnectedTextureRandom_0_.symmetry * p_getConnectedTextureRandom_0_.symmetry;
        final int j = Config.getRandom(p_getConnectedTextureRandom_1_, i) & Integer.MAX_VALUE;
        int k = 0;
        if (p_getConnectedTextureRandom_0_.weights == null) {
            k = j % p_getConnectedTextureRandom_0_.tileIcons.length;
        }
        else {
            final int l = j % p_getConnectedTextureRandom_0_.sumAllWeights;
            final int[] aint = p_getConnectedTextureRandom_0_.sumWeights;
            for (int i2 = 0; i2 < aint.length; ++i2) {
                if (l < aint[i2]) {
                    k = i2;
                    break;
                }
            }
        }
        return p_getConnectedTextureRandom_0_.tileIcons[k];
    }
    
    private static TextureAtlasSprite getConnectedTextureFixed(final ConnectedProperties p_getConnectedTextureFixed_0_) {
        return p_getConnectedTextureFixed_0_.tileIcons[0];
    }
    
    private static TextureAtlasSprite getConnectedTextureRepeat(final ConnectedProperties p_getConnectedTextureRepeat_0_, final BlockPos p_getConnectedTextureRepeat_1_, final int p_getConnectedTextureRepeat_2_) {
        if (p_getConnectedTextureRepeat_0_.tileIcons.length == 1) {
            return p_getConnectedTextureRepeat_0_.tileIcons[0];
        }
        final int i = p_getConnectedTextureRepeat_1_.getX();
        final int j = p_getConnectedTextureRepeat_1_.getY();
        final int k = p_getConnectedTextureRepeat_1_.getZ();
        int l = 0;
        int i2 = 0;
        switch (p_getConnectedTextureRepeat_2_) {
            case 0: {
                l = i;
                i2 = k;
                break;
            }
            case 1: {
                l = i;
                i2 = k;
                break;
            }
            case 2: {
                l = -i - 1;
                i2 = -j;
                break;
            }
            case 3: {
                l = i;
                i2 = -j;
                break;
            }
            case 4: {
                l = k;
                i2 = -j;
                break;
            }
            case 5: {
                l = -k - 1;
                i2 = -j;
                break;
            }
        }
        l %= p_getConnectedTextureRepeat_0_.width;
        i2 %= p_getConnectedTextureRepeat_0_.height;
        if (l < 0) {
            l += p_getConnectedTextureRepeat_0_.width;
        }
        if (i2 < 0) {
            i2 += p_getConnectedTextureRepeat_0_.height;
        }
        final int j2 = i2 * p_getConnectedTextureRepeat_0_.width + l;
        return p_getConnectedTextureRepeat_0_.tileIcons[j2];
    }
    
    private static TextureAtlasSprite getConnectedTextureCtm(final ConnectedProperties p_getConnectedTextureCtm_0_, final IBlockAccess p_getConnectedTextureCtm_1_, final IBlockState p_getConnectedTextureCtm_2_, final BlockPos p_getConnectedTextureCtm_3_, final int p_getConnectedTextureCtm_4_, final int p_getConnectedTextureCtm_5_, final TextureAtlasSprite p_getConnectedTextureCtm_6_, final int p_getConnectedTextureCtm_7_, final RenderEnv p_getConnectedTextureCtm_8_) {
        final int i = getConnectedTextureCtmIndex(p_getConnectedTextureCtm_0_, p_getConnectedTextureCtm_1_, p_getConnectedTextureCtm_2_, p_getConnectedTextureCtm_3_, p_getConnectedTextureCtm_4_, p_getConnectedTextureCtm_5_, p_getConnectedTextureCtm_6_, p_getConnectedTextureCtm_7_, p_getConnectedTextureCtm_8_);
        return p_getConnectedTextureCtm_0_.tileIcons[i];
    }
    
    private static BakedQuad[] getConnectedTextureCtmCompact(final ConnectedProperties p_getConnectedTextureCtmCompact_0_, final IBlockAccess p_getConnectedTextureCtmCompact_1_, final IBlockState p_getConnectedTextureCtmCompact_2_, final BlockPos p_getConnectedTextureCtmCompact_3_, final int p_getConnectedTextureCtmCompact_4_, final int p_getConnectedTextureCtmCompact_5_, final BakedQuad p_getConnectedTextureCtmCompact_6_, final int p_getConnectedTextureCtmCompact_7_, final RenderEnv p_getConnectedTextureCtmCompact_8_) {
        final TextureAtlasSprite textureatlassprite = p_getConnectedTextureCtmCompact_6_.getSprite();
        final int i = getConnectedTextureCtmIndex(p_getConnectedTextureCtmCompact_0_, p_getConnectedTextureCtmCompact_1_, p_getConnectedTextureCtmCompact_2_, p_getConnectedTextureCtmCompact_3_, p_getConnectedTextureCtmCompact_4_, p_getConnectedTextureCtmCompact_5_, textureatlassprite, p_getConnectedTextureCtmCompact_7_, p_getConnectedTextureCtmCompact_8_);
        return ConnectedTexturesCompact.getConnectedTextureCtmCompact(i, p_getConnectedTextureCtmCompact_0_, p_getConnectedTextureCtmCompact_5_, p_getConnectedTextureCtmCompact_6_, p_getConnectedTextureCtmCompact_8_);
    }
    
    private static BakedQuad[] getConnectedTextureOverlay(final ConnectedProperties p_getConnectedTextureOverlay_0_, final IBlockAccess p_getConnectedTextureOverlay_1_, final IBlockState p_getConnectedTextureOverlay_2_, final BlockPos p_getConnectedTextureOverlay_3_, final int p_getConnectedTextureOverlay_4_, final int p_getConnectedTextureOverlay_5_, final BakedQuad p_getConnectedTextureOverlay_6_, final int p_getConnectedTextureOverlay_7_, final RenderEnv p_getConnectedTextureOverlay_8_) {
        if (!p_getConnectedTextureOverlay_6_.isFullQuad()) {
            return null;
        }
        final TextureAtlasSprite textureatlassprite = p_getConnectedTextureOverlay_6_.getSprite();
        final BlockDir[] ablockdir = getSideDirections(p_getConnectedTextureOverlay_5_, p_getConnectedTextureOverlay_4_);
        final boolean[] aboolean = p_getConnectedTextureOverlay_8_.getBorderFlags();
        for (int i = 0; i < 4; ++i) {
            aboolean[i] = isNeighbourOverlay(p_getConnectedTextureOverlay_0_, p_getConnectedTextureOverlay_1_, p_getConnectedTextureOverlay_2_, ablockdir[i].offset(p_getConnectedTextureOverlay_3_), p_getConnectedTextureOverlay_5_, textureatlassprite, p_getConnectedTextureOverlay_7_);
        }
        final ListQuadsOverlay listquadsoverlay = p_getConnectedTextureOverlay_8_.getListQuadsOverlay(p_getConnectedTextureOverlay_0_.layer);
        Object dirEdges;
        try {
            if (!aboolean[0] || !aboolean[1] || !aboolean[2] || !aboolean[3]) {
                if (aboolean[0] && aboolean[1] && aboolean[2]) {
                    listquadsoverlay.addQuad(getQuadFull(p_getConnectedTextureOverlay_0_.tileIcons[5], p_getConnectedTextureOverlay_6_, p_getConnectedTextureOverlay_0_.tintIndex), p_getConnectedTextureOverlay_0_.tintBlockState);
                    dirEdges = null;
                    return (BakedQuad[])dirEdges;
                }
                if (aboolean[0] && aboolean[2] && aboolean[3]) {
                    listquadsoverlay.addQuad(getQuadFull(p_getConnectedTextureOverlay_0_.tileIcons[6], p_getConnectedTextureOverlay_6_, p_getConnectedTextureOverlay_0_.tintIndex), p_getConnectedTextureOverlay_0_.tintBlockState);
                    dirEdges = null;
                    return (BakedQuad[])dirEdges;
                }
                if (aboolean[1] && aboolean[2] && aboolean[3]) {
                    listquadsoverlay.addQuad(getQuadFull(p_getConnectedTextureOverlay_0_.tileIcons[12], p_getConnectedTextureOverlay_6_, p_getConnectedTextureOverlay_0_.tintIndex), p_getConnectedTextureOverlay_0_.tintBlockState);
                    dirEdges = null;
                    return (BakedQuad[])dirEdges;
                }
                if (aboolean[0] && aboolean[1] && aboolean[3]) {
                    listquadsoverlay.addQuad(getQuadFull(p_getConnectedTextureOverlay_0_.tileIcons[13], p_getConnectedTextureOverlay_6_, p_getConnectedTextureOverlay_0_.tintIndex), p_getConnectedTextureOverlay_0_.tintBlockState);
                    dirEdges = null;
                    return (BakedQuad[])dirEdges;
                }
                final BlockDir[] ablockdir2 = getEdgeDirections(p_getConnectedTextureOverlay_5_, p_getConnectedTextureOverlay_4_);
                final boolean[] aboolean2 = p_getConnectedTextureOverlay_8_.getBorderFlags2();
                for (int j = 0; j < 4; ++j) {
                    aboolean2[j] = isNeighbourOverlay(p_getConnectedTextureOverlay_0_, p_getConnectedTextureOverlay_1_, p_getConnectedTextureOverlay_2_, ablockdir2[j].offset(p_getConnectedTextureOverlay_3_), p_getConnectedTextureOverlay_5_, textureatlassprite, p_getConnectedTextureOverlay_7_);
                }
                if (aboolean[1] && aboolean[2]) {
                    listquadsoverlay.addQuad(getQuadFull(p_getConnectedTextureOverlay_0_.tileIcons[3], p_getConnectedTextureOverlay_6_, p_getConnectedTextureOverlay_0_.tintIndex), p_getConnectedTextureOverlay_0_.tintBlockState);
                    if (aboolean2[3]) {
                        listquadsoverlay.addQuad(getQuadFull(p_getConnectedTextureOverlay_0_.tileIcons[16], p_getConnectedTextureOverlay_6_, p_getConnectedTextureOverlay_0_.tintIndex), p_getConnectedTextureOverlay_0_.tintBlockState);
                    }
                    final Object object4 = null;
                    return (BakedQuad[])object4;
                }
                if (aboolean[0] && aboolean[2]) {
                    listquadsoverlay.addQuad(getQuadFull(p_getConnectedTextureOverlay_0_.tileIcons[4], p_getConnectedTextureOverlay_6_, p_getConnectedTextureOverlay_0_.tintIndex), p_getConnectedTextureOverlay_0_.tintBlockState);
                    if (aboolean2[2]) {
                        listquadsoverlay.addQuad(getQuadFull(p_getConnectedTextureOverlay_0_.tileIcons[14], p_getConnectedTextureOverlay_6_, p_getConnectedTextureOverlay_0_.tintIndex), p_getConnectedTextureOverlay_0_.tintBlockState);
                    }
                    final Object object5 = null;
                    return (BakedQuad[])object5;
                }
                if (aboolean[1] && aboolean[3]) {
                    listquadsoverlay.addQuad(getQuadFull(p_getConnectedTextureOverlay_0_.tileIcons[10], p_getConnectedTextureOverlay_6_, p_getConnectedTextureOverlay_0_.tintIndex), p_getConnectedTextureOverlay_0_.tintBlockState);
                    if (aboolean2[1]) {
                        listquadsoverlay.addQuad(getQuadFull(p_getConnectedTextureOverlay_0_.tileIcons[2], p_getConnectedTextureOverlay_6_, p_getConnectedTextureOverlay_0_.tintIndex), p_getConnectedTextureOverlay_0_.tintBlockState);
                    }
                    final Object object6 = null;
                    return (BakedQuad[])object6;
                }
                if (aboolean[0] && aboolean[3]) {
                    listquadsoverlay.addQuad(getQuadFull(p_getConnectedTextureOverlay_0_.tileIcons[11], p_getConnectedTextureOverlay_6_, p_getConnectedTextureOverlay_0_.tintIndex), p_getConnectedTextureOverlay_0_.tintBlockState);
                    if (aboolean2[0]) {
                        listquadsoverlay.addQuad(getQuadFull(p_getConnectedTextureOverlay_0_.tileIcons[0], p_getConnectedTextureOverlay_6_, p_getConnectedTextureOverlay_0_.tintIndex), p_getConnectedTextureOverlay_0_.tintBlockState);
                    }
                    final Object object7 = null;
                    return (BakedQuad[])object7;
                }
                final boolean[] aboolean3 = p_getConnectedTextureOverlay_8_.getBorderFlags3();
                for (int k = 0; k < 4; ++k) {
                    aboolean3[k] = isNeighbourMatching(p_getConnectedTextureOverlay_0_, p_getConnectedTextureOverlay_1_, p_getConnectedTextureOverlay_2_, ablockdir[k].offset(p_getConnectedTextureOverlay_3_), p_getConnectedTextureOverlay_5_, textureatlassprite, p_getConnectedTextureOverlay_7_);
                }
                if (aboolean[0]) {
                    listquadsoverlay.addQuad(getQuadFull(p_getConnectedTextureOverlay_0_.tileIcons[9], p_getConnectedTextureOverlay_6_, p_getConnectedTextureOverlay_0_.tintIndex), p_getConnectedTextureOverlay_0_.tintBlockState);
                }
                if (aboolean[1]) {
                    listquadsoverlay.addQuad(getQuadFull(p_getConnectedTextureOverlay_0_.tileIcons[7], p_getConnectedTextureOverlay_6_, p_getConnectedTextureOverlay_0_.tintIndex), p_getConnectedTextureOverlay_0_.tintBlockState);
                }
                if (aboolean[2]) {
                    listquadsoverlay.addQuad(getQuadFull(p_getConnectedTextureOverlay_0_.tileIcons[1], p_getConnectedTextureOverlay_6_, p_getConnectedTextureOverlay_0_.tintIndex), p_getConnectedTextureOverlay_0_.tintBlockState);
                }
                if (aboolean[3]) {
                    listquadsoverlay.addQuad(getQuadFull(p_getConnectedTextureOverlay_0_.tileIcons[15], p_getConnectedTextureOverlay_6_, p_getConnectedTextureOverlay_0_.tintIndex), p_getConnectedTextureOverlay_0_.tintBlockState);
                }
                if (aboolean2[0] && (aboolean3[1] || aboolean3[2]) && !aboolean[1] && !aboolean[2]) {
                    listquadsoverlay.addQuad(getQuadFull(p_getConnectedTextureOverlay_0_.tileIcons[0], p_getConnectedTextureOverlay_6_, p_getConnectedTextureOverlay_0_.tintIndex), p_getConnectedTextureOverlay_0_.tintBlockState);
                }
                if (aboolean2[1] && (aboolean3[0] || aboolean3[2]) && !aboolean[0] && !aboolean[2]) {
                    listquadsoverlay.addQuad(getQuadFull(p_getConnectedTextureOverlay_0_.tileIcons[2], p_getConnectedTextureOverlay_6_, p_getConnectedTextureOverlay_0_.tintIndex), p_getConnectedTextureOverlay_0_.tintBlockState);
                }
                if (aboolean2[2] && (aboolean3[1] || aboolean3[3]) && !aboolean[1] && !aboolean[3]) {
                    listquadsoverlay.addQuad(getQuadFull(p_getConnectedTextureOverlay_0_.tileIcons[14], p_getConnectedTextureOverlay_6_, p_getConnectedTextureOverlay_0_.tintIndex), p_getConnectedTextureOverlay_0_.tintBlockState);
                }
                if (aboolean2[3] && (aboolean3[0] || aboolean3[3]) && !aboolean[0] && !aboolean[3]) {
                    listquadsoverlay.addQuad(getQuadFull(p_getConnectedTextureOverlay_0_.tileIcons[16], p_getConnectedTextureOverlay_6_, p_getConnectedTextureOverlay_0_.tintIndex), p_getConnectedTextureOverlay_0_.tintBlockState);
                }
                final Object object8 = null;
                return (BakedQuad[])object8;
            }
            else {
                listquadsoverlay.addQuad(getQuadFull(p_getConnectedTextureOverlay_0_.tileIcons[8], p_getConnectedTextureOverlay_6_, p_getConnectedTextureOverlay_0_.tintIndex), p_getConnectedTextureOverlay_0_.tintBlockState);
                dirEdges = null;
            }
        }
        finally {
            if (listquadsoverlay.size() > 0) {
                p_getConnectedTextureOverlay_8_.setOverlaysRendered(true);
            }
        }
        if (listquadsoverlay.size() > 0) {
            p_getConnectedTextureOverlay_8_.setOverlaysRendered(true);
        }
        return (BakedQuad[])dirEdges;
    }
    
    private static BlockDir[] getSideDirections(final int p_getSideDirections_0_, final int p_getSideDirections_1_) {
        switch (p_getSideDirections_0_) {
            case 0: {
                return ConnectedTextures.SIDES_Y_NEG_DOWN;
            }
            case 1: {
                return ConnectedTextures.SIDES_Y_POS_UP;
            }
            case 2: {
                if (p_getSideDirections_1_ == 1) {
                    return ConnectedTextures.SIDES_Z_NEG_NORTH_Z_AXIS;
                }
                return ConnectedTextures.SIDES_Z_NEG_NORTH;
            }
            case 3: {
                return ConnectedTextures.SIDES_Z_POS_SOUTH;
            }
            case 4: {
                return ConnectedTextures.SIDES_X_NEG_WEST;
            }
            case 5: {
                if (p_getSideDirections_1_ == 2) {
                    return ConnectedTextures.SIDES_X_POS_EAST_X_AXIS;
                }
                return ConnectedTextures.SIDES_X_POS_EAST;
            }
            default: {
                throw new IllegalArgumentException("Unknown side: " + p_getSideDirections_0_);
            }
        }
    }
    
    private static BlockDir[] getEdgeDirections(final int p_getEdgeDirections_0_, final int p_getEdgeDirections_1_) {
        switch (p_getEdgeDirections_0_) {
            case 0: {
                return ConnectedTextures.EDGES_Y_NEG_DOWN;
            }
            case 1: {
                return ConnectedTextures.EDGES_Y_POS_UP;
            }
            case 2: {
                if (p_getEdgeDirections_1_ == 1) {
                    return ConnectedTextures.EDGES_Z_NEG_NORTH_Z_AXIS;
                }
                return ConnectedTextures.EDGES_Z_NEG_NORTH;
            }
            case 3: {
                return ConnectedTextures.EDGES_Z_POS_SOUTH;
            }
            case 4: {
                return ConnectedTextures.EDGES_X_NEG_WEST;
            }
            case 5: {
                if (p_getEdgeDirections_1_ == 2) {
                    return ConnectedTextures.EDGES_X_POS_EAST_X_AXIS;
                }
                return ConnectedTextures.EDGES_X_POS_EAST;
            }
            default: {
                throw new IllegalArgumentException("Unknown side: " + p_getEdgeDirections_0_);
            }
        }
    }
    
    protected static Map[][] getSpriteQuadCompactMaps() {
        return ConnectedTextures.spriteQuadCompactMaps;
    }
    
    private static int getConnectedTextureCtmIndex(final ConnectedProperties p_getConnectedTextureCtmIndex_0_, final IBlockAccess p_getConnectedTextureCtmIndex_1_, final IBlockState p_getConnectedTextureCtmIndex_2_, final BlockPos p_getConnectedTextureCtmIndex_3_, final int p_getConnectedTextureCtmIndex_4_, final int p_getConnectedTextureCtmIndex_5_, final TextureAtlasSprite p_getConnectedTextureCtmIndex_6_, final int p_getConnectedTextureCtmIndex_7_, final RenderEnv p_getConnectedTextureCtmIndex_8_) {
        final boolean[] aboolean = p_getConnectedTextureCtmIndex_8_.getBorderFlags();
        switch (p_getConnectedTextureCtmIndex_5_) {
            case 0: {
                aboolean[0] = isNeighbour(p_getConnectedTextureCtmIndex_0_, p_getConnectedTextureCtmIndex_1_, p_getConnectedTextureCtmIndex_2_, p_getConnectedTextureCtmIndex_3_.west(), p_getConnectedTextureCtmIndex_5_, p_getConnectedTextureCtmIndex_6_, p_getConnectedTextureCtmIndex_7_);
                aboolean[1] = isNeighbour(p_getConnectedTextureCtmIndex_0_, p_getConnectedTextureCtmIndex_1_, p_getConnectedTextureCtmIndex_2_, p_getConnectedTextureCtmIndex_3_.east(), p_getConnectedTextureCtmIndex_5_, p_getConnectedTextureCtmIndex_6_, p_getConnectedTextureCtmIndex_7_);
                aboolean[2] = isNeighbour(p_getConnectedTextureCtmIndex_0_, p_getConnectedTextureCtmIndex_1_, p_getConnectedTextureCtmIndex_2_, p_getConnectedTextureCtmIndex_3_.north(), p_getConnectedTextureCtmIndex_5_, p_getConnectedTextureCtmIndex_6_, p_getConnectedTextureCtmIndex_7_);
                aboolean[3] = isNeighbour(p_getConnectedTextureCtmIndex_0_, p_getConnectedTextureCtmIndex_1_, p_getConnectedTextureCtmIndex_2_, p_getConnectedTextureCtmIndex_3_.south(), p_getConnectedTextureCtmIndex_5_, p_getConnectedTextureCtmIndex_6_, p_getConnectedTextureCtmIndex_7_);
                break;
            }
            case 1: {
                aboolean[0] = isNeighbour(p_getConnectedTextureCtmIndex_0_, p_getConnectedTextureCtmIndex_1_, p_getConnectedTextureCtmIndex_2_, p_getConnectedTextureCtmIndex_3_.west(), p_getConnectedTextureCtmIndex_5_, p_getConnectedTextureCtmIndex_6_, p_getConnectedTextureCtmIndex_7_);
                aboolean[1] = isNeighbour(p_getConnectedTextureCtmIndex_0_, p_getConnectedTextureCtmIndex_1_, p_getConnectedTextureCtmIndex_2_, p_getConnectedTextureCtmIndex_3_.east(), p_getConnectedTextureCtmIndex_5_, p_getConnectedTextureCtmIndex_6_, p_getConnectedTextureCtmIndex_7_);
                aboolean[2] = isNeighbour(p_getConnectedTextureCtmIndex_0_, p_getConnectedTextureCtmIndex_1_, p_getConnectedTextureCtmIndex_2_, p_getConnectedTextureCtmIndex_3_.south(), p_getConnectedTextureCtmIndex_5_, p_getConnectedTextureCtmIndex_6_, p_getConnectedTextureCtmIndex_7_);
                aboolean[3] = isNeighbour(p_getConnectedTextureCtmIndex_0_, p_getConnectedTextureCtmIndex_1_, p_getConnectedTextureCtmIndex_2_, p_getConnectedTextureCtmIndex_3_.north(), p_getConnectedTextureCtmIndex_5_, p_getConnectedTextureCtmIndex_6_, p_getConnectedTextureCtmIndex_7_);
                break;
            }
            case 2: {
                aboolean[0] = isNeighbour(p_getConnectedTextureCtmIndex_0_, p_getConnectedTextureCtmIndex_1_, p_getConnectedTextureCtmIndex_2_, p_getConnectedTextureCtmIndex_3_.east(), p_getConnectedTextureCtmIndex_5_, p_getConnectedTextureCtmIndex_6_, p_getConnectedTextureCtmIndex_7_);
                aboolean[1] = isNeighbour(p_getConnectedTextureCtmIndex_0_, p_getConnectedTextureCtmIndex_1_, p_getConnectedTextureCtmIndex_2_, p_getConnectedTextureCtmIndex_3_.west(), p_getConnectedTextureCtmIndex_5_, p_getConnectedTextureCtmIndex_6_, p_getConnectedTextureCtmIndex_7_);
                aboolean[2] = isNeighbour(p_getConnectedTextureCtmIndex_0_, p_getConnectedTextureCtmIndex_1_, p_getConnectedTextureCtmIndex_2_, p_getConnectedTextureCtmIndex_3_.down(), p_getConnectedTextureCtmIndex_5_, p_getConnectedTextureCtmIndex_6_, p_getConnectedTextureCtmIndex_7_);
                aboolean[3] = isNeighbour(p_getConnectedTextureCtmIndex_0_, p_getConnectedTextureCtmIndex_1_, p_getConnectedTextureCtmIndex_2_, p_getConnectedTextureCtmIndex_3_.up(), p_getConnectedTextureCtmIndex_5_, p_getConnectedTextureCtmIndex_6_, p_getConnectedTextureCtmIndex_7_);
                if (p_getConnectedTextureCtmIndex_4_ == 1) {
                    switchValues(0, 1, aboolean);
                    switchValues(2, 3, aboolean);
                    break;
                }
                break;
            }
            case 3: {
                aboolean[0] = isNeighbour(p_getConnectedTextureCtmIndex_0_, p_getConnectedTextureCtmIndex_1_, p_getConnectedTextureCtmIndex_2_, p_getConnectedTextureCtmIndex_3_.west(), p_getConnectedTextureCtmIndex_5_, p_getConnectedTextureCtmIndex_6_, p_getConnectedTextureCtmIndex_7_);
                aboolean[1] = isNeighbour(p_getConnectedTextureCtmIndex_0_, p_getConnectedTextureCtmIndex_1_, p_getConnectedTextureCtmIndex_2_, p_getConnectedTextureCtmIndex_3_.east(), p_getConnectedTextureCtmIndex_5_, p_getConnectedTextureCtmIndex_6_, p_getConnectedTextureCtmIndex_7_);
                aboolean[2] = isNeighbour(p_getConnectedTextureCtmIndex_0_, p_getConnectedTextureCtmIndex_1_, p_getConnectedTextureCtmIndex_2_, p_getConnectedTextureCtmIndex_3_.down(), p_getConnectedTextureCtmIndex_5_, p_getConnectedTextureCtmIndex_6_, p_getConnectedTextureCtmIndex_7_);
                aboolean[3] = isNeighbour(p_getConnectedTextureCtmIndex_0_, p_getConnectedTextureCtmIndex_1_, p_getConnectedTextureCtmIndex_2_, p_getConnectedTextureCtmIndex_3_.up(), p_getConnectedTextureCtmIndex_5_, p_getConnectedTextureCtmIndex_6_, p_getConnectedTextureCtmIndex_7_);
                break;
            }
            case 4: {
                aboolean[0] = isNeighbour(p_getConnectedTextureCtmIndex_0_, p_getConnectedTextureCtmIndex_1_, p_getConnectedTextureCtmIndex_2_, p_getConnectedTextureCtmIndex_3_.north(), p_getConnectedTextureCtmIndex_5_, p_getConnectedTextureCtmIndex_6_, p_getConnectedTextureCtmIndex_7_);
                aboolean[1] = isNeighbour(p_getConnectedTextureCtmIndex_0_, p_getConnectedTextureCtmIndex_1_, p_getConnectedTextureCtmIndex_2_, p_getConnectedTextureCtmIndex_3_.south(), p_getConnectedTextureCtmIndex_5_, p_getConnectedTextureCtmIndex_6_, p_getConnectedTextureCtmIndex_7_);
                aboolean[2] = isNeighbour(p_getConnectedTextureCtmIndex_0_, p_getConnectedTextureCtmIndex_1_, p_getConnectedTextureCtmIndex_2_, p_getConnectedTextureCtmIndex_3_.down(), p_getConnectedTextureCtmIndex_5_, p_getConnectedTextureCtmIndex_6_, p_getConnectedTextureCtmIndex_7_);
                aboolean[3] = isNeighbour(p_getConnectedTextureCtmIndex_0_, p_getConnectedTextureCtmIndex_1_, p_getConnectedTextureCtmIndex_2_, p_getConnectedTextureCtmIndex_3_.up(), p_getConnectedTextureCtmIndex_5_, p_getConnectedTextureCtmIndex_6_, p_getConnectedTextureCtmIndex_7_);
                break;
            }
            case 5: {
                aboolean[0] = isNeighbour(p_getConnectedTextureCtmIndex_0_, p_getConnectedTextureCtmIndex_1_, p_getConnectedTextureCtmIndex_2_, p_getConnectedTextureCtmIndex_3_.south(), p_getConnectedTextureCtmIndex_5_, p_getConnectedTextureCtmIndex_6_, p_getConnectedTextureCtmIndex_7_);
                aboolean[1] = isNeighbour(p_getConnectedTextureCtmIndex_0_, p_getConnectedTextureCtmIndex_1_, p_getConnectedTextureCtmIndex_2_, p_getConnectedTextureCtmIndex_3_.north(), p_getConnectedTextureCtmIndex_5_, p_getConnectedTextureCtmIndex_6_, p_getConnectedTextureCtmIndex_7_);
                aboolean[2] = isNeighbour(p_getConnectedTextureCtmIndex_0_, p_getConnectedTextureCtmIndex_1_, p_getConnectedTextureCtmIndex_2_, p_getConnectedTextureCtmIndex_3_.down(), p_getConnectedTextureCtmIndex_5_, p_getConnectedTextureCtmIndex_6_, p_getConnectedTextureCtmIndex_7_);
                aboolean[3] = isNeighbour(p_getConnectedTextureCtmIndex_0_, p_getConnectedTextureCtmIndex_1_, p_getConnectedTextureCtmIndex_2_, p_getConnectedTextureCtmIndex_3_.up(), p_getConnectedTextureCtmIndex_5_, p_getConnectedTextureCtmIndex_6_, p_getConnectedTextureCtmIndex_7_);
                if (p_getConnectedTextureCtmIndex_4_ == 2) {
                    switchValues(0, 1, aboolean);
                    switchValues(2, 3, aboolean);
                    break;
                }
                break;
            }
        }
        int i = 0;
        if (aboolean[0] & !aboolean[1] & !aboolean[2] & !aboolean[3]) {
            i = 3;
        }
        else if (!aboolean[0] & aboolean[1] & !aboolean[2] & !aboolean[3]) {
            i = 1;
        }
        else if (!aboolean[0] & !aboolean[1] & aboolean[2] & !aboolean[3]) {
            i = 12;
        }
        else if (!aboolean[0] & !aboolean[1] & !aboolean[2] & aboolean[3]) {
            i = 36;
        }
        else if (aboolean[0] & aboolean[1] & !aboolean[2] & !aboolean[3]) {
            i = 2;
        }
        else if (!aboolean[0] & !aboolean[1] & aboolean[2] & aboolean[3]) {
            i = 24;
        }
        else if (aboolean[0] & !aboolean[1] & aboolean[2] & !aboolean[3]) {
            i = 15;
        }
        else if (aboolean[0] & !aboolean[1] & !aboolean[2] & aboolean[3]) {
            i = 39;
        }
        else if (!aboolean[0] & aboolean[1] & aboolean[2] & !aboolean[3]) {
            i = 13;
        }
        else if (!aboolean[0] & aboolean[1] & !aboolean[2] & aboolean[3]) {
            i = 37;
        }
        else if (!aboolean[0] & aboolean[1] & aboolean[2] & aboolean[3]) {
            i = 25;
        }
        else if (aboolean[0] & !aboolean[1] & aboolean[2] & aboolean[3]) {
            i = 27;
        }
        else if (aboolean[0] & aboolean[1] & !aboolean[2] & aboolean[3]) {
            i = 38;
        }
        else if (aboolean[0] & aboolean[1] & aboolean[2] & !aboolean[3]) {
            i = 14;
        }
        else if (aboolean[0] & aboolean[1] & aboolean[2] & aboolean[3]) {
            i = 26;
        }
        if (i == 0) {
            return i;
        }
        if (!Config.isConnectedTexturesFancy()) {
            return i;
        }
        switch (p_getConnectedTextureCtmIndex_5_) {
            case 0: {
                aboolean[0] = !isNeighbour(p_getConnectedTextureCtmIndex_0_, p_getConnectedTextureCtmIndex_1_, p_getConnectedTextureCtmIndex_2_, p_getConnectedTextureCtmIndex_3_.east().north(), p_getConnectedTextureCtmIndex_5_, p_getConnectedTextureCtmIndex_6_, p_getConnectedTextureCtmIndex_7_);
                aboolean[1] = !isNeighbour(p_getConnectedTextureCtmIndex_0_, p_getConnectedTextureCtmIndex_1_, p_getConnectedTextureCtmIndex_2_, p_getConnectedTextureCtmIndex_3_.west().north(), p_getConnectedTextureCtmIndex_5_, p_getConnectedTextureCtmIndex_6_, p_getConnectedTextureCtmIndex_7_);
                aboolean[2] = !isNeighbour(p_getConnectedTextureCtmIndex_0_, p_getConnectedTextureCtmIndex_1_, p_getConnectedTextureCtmIndex_2_, p_getConnectedTextureCtmIndex_3_.east().south(), p_getConnectedTextureCtmIndex_5_, p_getConnectedTextureCtmIndex_6_, p_getConnectedTextureCtmIndex_7_);
                aboolean[3] = !isNeighbour(p_getConnectedTextureCtmIndex_0_, p_getConnectedTextureCtmIndex_1_, p_getConnectedTextureCtmIndex_2_, p_getConnectedTextureCtmIndex_3_.west().south(), p_getConnectedTextureCtmIndex_5_, p_getConnectedTextureCtmIndex_6_, p_getConnectedTextureCtmIndex_7_);
                break;
            }
            case 1: {
                aboolean[0] = !isNeighbour(p_getConnectedTextureCtmIndex_0_, p_getConnectedTextureCtmIndex_1_, p_getConnectedTextureCtmIndex_2_, p_getConnectedTextureCtmIndex_3_.east().south(), p_getConnectedTextureCtmIndex_5_, p_getConnectedTextureCtmIndex_6_, p_getConnectedTextureCtmIndex_7_);
                aboolean[1] = !isNeighbour(p_getConnectedTextureCtmIndex_0_, p_getConnectedTextureCtmIndex_1_, p_getConnectedTextureCtmIndex_2_, p_getConnectedTextureCtmIndex_3_.west().south(), p_getConnectedTextureCtmIndex_5_, p_getConnectedTextureCtmIndex_6_, p_getConnectedTextureCtmIndex_7_);
                aboolean[2] = !isNeighbour(p_getConnectedTextureCtmIndex_0_, p_getConnectedTextureCtmIndex_1_, p_getConnectedTextureCtmIndex_2_, p_getConnectedTextureCtmIndex_3_.east().north(), p_getConnectedTextureCtmIndex_5_, p_getConnectedTextureCtmIndex_6_, p_getConnectedTextureCtmIndex_7_);
                aboolean[3] = !isNeighbour(p_getConnectedTextureCtmIndex_0_, p_getConnectedTextureCtmIndex_1_, p_getConnectedTextureCtmIndex_2_, p_getConnectedTextureCtmIndex_3_.west().north(), p_getConnectedTextureCtmIndex_5_, p_getConnectedTextureCtmIndex_6_, p_getConnectedTextureCtmIndex_7_);
                break;
            }
            case 2: {
                aboolean[0] = !isNeighbour(p_getConnectedTextureCtmIndex_0_, p_getConnectedTextureCtmIndex_1_, p_getConnectedTextureCtmIndex_2_, p_getConnectedTextureCtmIndex_3_.west().down(), p_getConnectedTextureCtmIndex_5_, p_getConnectedTextureCtmIndex_6_, p_getConnectedTextureCtmIndex_7_);
                aboolean[1] = !isNeighbour(p_getConnectedTextureCtmIndex_0_, p_getConnectedTextureCtmIndex_1_, p_getConnectedTextureCtmIndex_2_, p_getConnectedTextureCtmIndex_3_.east().down(), p_getConnectedTextureCtmIndex_5_, p_getConnectedTextureCtmIndex_6_, p_getConnectedTextureCtmIndex_7_);
                aboolean[2] = !isNeighbour(p_getConnectedTextureCtmIndex_0_, p_getConnectedTextureCtmIndex_1_, p_getConnectedTextureCtmIndex_2_, p_getConnectedTextureCtmIndex_3_.west().up(), p_getConnectedTextureCtmIndex_5_, p_getConnectedTextureCtmIndex_6_, p_getConnectedTextureCtmIndex_7_);
                aboolean[3] = !isNeighbour(p_getConnectedTextureCtmIndex_0_, p_getConnectedTextureCtmIndex_1_, p_getConnectedTextureCtmIndex_2_, p_getConnectedTextureCtmIndex_3_.east().up(), p_getConnectedTextureCtmIndex_5_, p_getConnectedTextureCtmIndex_6_, p_getConnectedTextureCtmIndex_7_);
                if (p_getConnectedTextureCtmIndex_4_ == 1) {
                    switchValues(0, 3, aboolean);
                    switchValues(1, 2, aboolean);
                    break;
                }
                break;
            }
            case 3: {
                aboolean[0] = !isNeighbour(p_getConnectedTextureCtmIndex_0_, p_getConnectedTextureCtmIndex_1_, p_getConnectedTextureCtmIndex_2_, p_getConnectedTextureCtmIndex_3_.east().down(), p_getConnectedTextureCtmIndex_5_, p_getConnectedTextureCtmIndex_6_, p_getConnectedTextureCtmIndex_7_);
                aboolean[1] = !isNeighbour(p_getConnectedTextureCtmIndex_0_, p_getConnectedTextureCtmIndex_1_, p_getConnectedTextureCtmIndex_2_, p_getConnectedTextureCtmIndex_3_.west().down(), p_getConnectedTextureCtmIndex_5_, p_getConnectedTextureCtmIndex_6_, p_getConnectedTextureCtmIndex_7_);
                aboolean[2] = !isNeighbour(p_getConnectedTextureCtmIndex_0_, p_getConnectedTextureCtmIndex_1_, p_getConnectedTextureCtmIndex_2_, p_getConnectedTextureCtmIndex_3_.east().up(), p_getConnectedTextureCtmIndex_5_, p_getConnectedTextureCtmIndex_6_, p_getConnectedTextureCtmIndex_7_);
                aboolean[3] = !isNeighbour(p_getConnectedTextureCtmIndex_0_, p_getConnectedTextureCtmIndex_1_, p_getConnectedTextureCtmIndex_2_, p_getConnectedTextureCtmIndex_3_.west().up(), p_getConnectedTextureCtmIndex_5_, p_getConnectedTextureCtmIndex_6_, p_getConnectedTextureCtmIndex_7_);
                break;
            }
            case 4: {
                aboolean[0] = !isNeighbour(p_getConnectedTextureCtmIndex_0_, p_getConnectedTextureCtmIndex_1_, p_getConnectedTextureCtmIndex_2_, p_getConnectedTextureCtmIndex_3_.down().south(), p_getConnectedTextureCtmIndex_5_, p_getConnectedTextureCtmIndex_6_, p_getConnectedTextureCtmIndex_7_);
                aboolean[1] = !isNeighbour(p_getConnectedTextureCtmIndex_0_, p_getConnectedTextureCtmIndex_1_, p_getConnectedTextureCtmIndex_2_, p_getConnectedTextureCtmIndex_3_.down().north(), p_getConnectedTextureCtmIndex_5_, p_getConnectedTextureCtmIndex_6_, p_getConnectedTextureCtmIndex_7_);
                aboolean[2] = !isNeighbour(p_getConnectedTextureCtmIndex_0_, p_getConnectedTextureCtmIndex_1_, p_getConnectedTextureCtmIndex_2_, p_getConnectedTextureCtmIndex_3_.up().south(), p_getConnectedTextureCtmIndex_5_, p_getConnectedTextureCtmIndex_6_, p_getConnectedTextureCtmIndex_7_);
                aboolean[3] = !isNeighbour(p_getConnectedTextureCtmIndex_0_, p_getConnectedTextureCtmIndex_1_, p_getConnectedTextureCtmIndex_2_, p_getConnectedTextureCtmIndex_3_.up().north(), p_getConnectedTextureCtmIndex_5_, p_getConnectedTextureCtmIndex_6_, p_getConnectedTextureCtmIndex_7_);
                break;
            }
            case 5: {
                aboolean[0] = !isNeighbour(p_getConnectedTextureCtmIndex_0_, p_getConnectedTextureCtmIndex_1_, p_getConnectedTextureCtmIndex_2_, p_getConnectedTextureCtmIndex_3_.down().north(), p_getConnectedTextureCtmIndex_5_, p_getConnectedTextureCtmIndex_6_, p_getConnectedTextureCtmIndex_7_);
                aboolean[1] = !isNeighbour(p_getConnectedTextureCtmIndex_0_, p_getConnectedTextureCtmIndex_1_, p_getConnectedTextureCtmIndex_2_, p_getConnectedTextureCtmIndex_3_.down().south(), p_getConnectedTextureCtmIndex_5_, p_getConnectedTextureCtmIndex_6_, p_getConnectedTextureCtmIndex_7_);
                aboolean[2] = !isNeighbour(p_getConnectedTextureCtmIndex_0_, p_getConnectedTextureCtmIndex_1_, p_getConnectedTextureCtmIndex_2_, p_getConnectedTextureCtmIndex_3_.up().north(), p_getConnectedTextureCtmIndex_5_, p_getConnectedTextureCtmIndex_6_, p_getConnectedTextureCtmIndex_7_);
                aboolean[3] = !isNeighbour(p_getConnectedTextureCtmIndex_0_, p_getConnectedTextureCtmIndex_1_, p_getConnectedTextureCtmIndex_2_, p_getConnectedTextureCtmIndex_3_.up().south(), p_getConnectedTextureCtmIndex_5_, p_getConnectedTextureCtmIndex_6_, p_getConnectedTextureCtmIndex_7_);
                if (p_getConnectedTextureCtmIndex_4_ == 2) {
                    switchValues(0, 3, aboolean);
                    switchValues(1, 2, aboolean);
                    break;
                }
                break;
            }
        }
        if (i == 13 && aboolean[0]) {
            i = 4;
        }
        else if (i == 15 && aboolean[1]) {
            i = 5;
        }
        else if (i == 37 && aboolean[2]) {
            i = 16;
        }
        else if (i == 39 && aboolean[3]) {
            i = 17;
        }
        else if (i == 14 && aboolean[0] && aboolean[1]) {
            i = 7;
        }
        else if (i == 25 && aboolean[0] && aboolean[2]) {
            i = 6;
        }
        else if (i == 27 && aboolean[3] && aboolean[1]) {
            i = 19;
        }
        else if (i == 38 && aboolean[3] && aboolean[2]) {
            i = 18;
        }
        else if (i == 14 && !aboolean[0] && aboolean[1]) {
            i = 31;
        }
        else if (i == 25 && aboolean[0] && !aboolean[2]) {
            i = 30;
        }
        else if (i == 27 && !aboolean[3] && aboolean[1]) {
            i = 41;
        }
        else if (i == 38 && aboolean[3] && !aboolean[2]) {
            i = 40;
        }
        else if (i == 14 && aboolean[0] && !aboolean[1]) {
            i = 29;
        }
        else if (i == 25 && !aboolean[0] && aboolean[2]) {
            i = 28;
        }
        else if (i == 27 && aboolean[3] && !aboolean[1]) {
            i = 43;
        }
        else if (i == 38 && !aboolean[3] && aboolean[2]) {
            i = 42;
        }
        else if (i == 26 && aboolean[0] && aboolean[1] && aboolean[2] && aboolean[3]) {
            i = 46;
        }
        else if (i == 26 && !aboolean[0] && aboolean[1] && aboolean[2] && aboolean[3]) {
            i = 9;
        }
        else if (i == 26 && aboolean[0] && !aboolean[1] && aboolean[2] && aboolean[3]) {
            i = 21;
        }
        else if (i == 26 && aboolean[0] && aboolean[1] && !aboolean[2] && aboolean[3]) {
            i = 8;
        }
        else if (i == 26 && aboolean[0] && aboolean[1] && aboolean[2] && !aboolean[3]) {
            i = 20;
        }
        else if (i == 26 && aboolean[0] && aboolean[1] && !aboolean[2] && !aboolean[3]) {
            i = 11;
        }
        else if (i == 26 && !aboolean[0] && !aboolean[1] && aboolean[2] && aboolean[3]) {
            i = 22;
        }
        else if (i == 26 && !aboolean[0] && aboolean[1] && !aboolean[2] && aboolean[3]) {
            i = 23;
        }
        else if (i == 26 && aboolean[0] && !aboolean[1] && aboolean[2] && !aboolean[3]) {
            i = 10;
        }
        else if (i == 26 && aboolean[0] && !aboolean[1] && !aboolean[2] && aboolean[3]) {
            i = 34;
        }
        else if (i == 26 && !aboolean[0] && aboolean[1] && aboolean[2] && !aboolean[3]) {
            i = 35;
        }
        else if (i == 26 && aboolean[0] && !aboolean[1] && !aboolean[2] && !aboolean[3]) {
            i = 32;
        }
        else if (i == 26 && !aboolean[0] && aboolean[1] && !aboolean[2] && !aboolean[3]) {
            i = 33;
        }
        else if (i == 26 && !aboolean[0] && !aboolean[1] && aboolean[2] && !aboolean[3]) {
            i = 44;
        }
        else if (i == 26 && !aboolean[0] && !aboolean[1] && !aboolean[2] && aboolean[3]) {
            i = 45;
        }
        return i;
    }
    
    private static void switchValues(final int p_switchValues_0_, final int p_switchValues_1_, final boolean[] p_switchValues_2_) {
        final boolean flag = p_switchValues_2_[p_switchValues_0_];
        p_switchValues_2_[p_switchValues_0_] = p_switchValues_2_[p_switchValues_1_];
        p_switchValues_2_[p_switchValues_1_] = flag;
    }
    
    private static boolean isNeighbourOverlay(final ConnectedProperties p_isNeighbourOverlay_0_, final IBlockAccess p_isNeighbourOverlay_1_, final IBlockState p_isNeighbourOverlay_2_, final BlockPos p_isNeighbourOverlay_3_, final int p_isNeighbourOverlay_4_, final TextureAtlasSprite p_isNeighbourOverlay_5_, final int p_isNeighbourOverlay_6_) {
        final IBlockState iblockstate = p_isNeighbourOverlay_1_.getBlockState(p_isNeighbourOverlay_3_);
        if (!isFullCubeModel(iblockstate)) {
            return false;
        }
        if (p_isNeighbourOverlay_0_.connectBlocks != null) {
            final BlockStateBase blockstatebase = (BlockStateBase)iblockstate;
            if (!Matches.block(blockstatebase.getBlockId(), blockstatebase.getMetadata(), p_isNeighbourOverlay_0_.connectBlocks)) {
                return false;
            }
        }
        if (p_isNeighbourOverlay_0_.connectTileIcons != null) {
            final TextureAtlasSprite textureatlassprite = getNeighbourIcon(p_isNeighbourOverlay_1_, p_isNeighbourOverlay_2_, p_isNeighbourOverlay_3_, iblockstate, p_isNeighbourOverlay_4_);
            if (!Config.isSameOne(textureatlassprite, p_isNeighbourOverlay_0_.connectTileIcons)) {
                return false;
            }
        }
        final IBlockState iblockstate2 = p_isNeighbourOverlay_1_.getBlockState(p_isNeighbourOverlay_3_.offset(getFacing(p_isNeighbourOverlay_4_)));
        return !iblockstate2.isOpaqueCube() && (p_isNeighbourOverlay_4_ != 1 || iblockstate2.getBlock() != Blocks.SNOW_LAYER) && !isNeighbour(p_isNeighbourOverlay_0_, p_isNeighbourOverlay_1_, p_isNeighbourOverlay_2_, p_isNeighbourOverlay_3_, iblockstate, p_isNeighbourOverlay_4_, p_isNeighbourOverlay_5_, p_isNeighbourOverlay_6_);
    }
    
    private static boolean isFullCubeModel(final IBlockState p_isFullCubeModel_0_) {
        if (p_isFullCubeModel_0_.isFullCube()) {
            return true;
        }
        final Block block = p_isFullCubeModel_0_.getBlock();
        return block instanceof BlockGlass || block instanceof BlockStainedGlass;
    }
    
    private static boolean isNeighbourMatching(final ConnectedProperties p_isNeighbourMatching_0_, final IBlockAccess p_isNeighbourMatching_1_, final IBlockState p_isNeighbourMatching_2_, final BlockPos p_isNeighbourMatching_3_, final int p_isNeighbourMatching_4_, final TextureAtlasSprite p_isNeighbourMatching_5_, final int p_isNeighbourMatching_6_) {
        final IBlockState iblockstate = p_isNeighbourMatching_1_.getBlockState(p_isNeighbourMatching_3_);
        if (iblockstate == ConnectedTextures.AIR_DEFAULT_STATE) {
            return false;
        }
        if (p_isNeighbourMatching_0_.matchBlocks != null && iblockstate instanceof BlockStateBase) {
            final BlockStateBase blockstatebase = (BlockStateBase)iblockstate;
            if (!p_isNeighbourMatching_0_.matchesBlock(blockstatebase.getBlockId(), blockstatebase.getMetadata())) {
                return false;
            }
        }
        if (p_isNeighbourMatching_0_.matchTileIcons != null) {
            final TextureAtlasSprite textureatlassprite = getNeighbourIcon(p_isNeighbourMatching_1_, p_isNeighbourMatching_2_, p_isNeighbourMatching_3_, iblockstate, p_isNeighbourMatching_4_);
            if (textureatlassprite != p_isNeighbourMatching_5_) {
                return false;
            }
        }
        final IBlockState iblockstate2 = p_isNeighbourMatching_1_.getBlockState(p_isNeighbourMatching_3_.offset(getFacing(p_isNeighbourMatching_4_)));
        return !iblockstate2.isOpaqueCube() && (p_isNeighbourMatching_4_ != 1 || iblockstate2.getBlock() != Blocks.SNOW_LAYER);
    }
    
    private static boolean isNeighbour(final ConnectedProperties p_isNeighbour_0_, final IBlockAccess p_isNeighbour_1_, final IBlockState p_isNeighbour_2_, final BlockPos p_isNeighbour_3_, final int p_isNeighbour_4_, final TextureAtlasSprite p_isNeighbour_5_, final int p_isNeighbour_6_) {
        final IBlockState iblockstate = p_isNeighbour_1_.getBlockState(p_isNeighbour_3_);
        return isNeighbour(p_isNeighbour_0_, p_isNeighbour_1_, p_isNeighbour_2_, p_isNeighbour_3_, iblockstate, p_isNeighbour_4_, p_isNeighbour_5_, p_isNeighbour_6_);
    }
    
    private static boolean isNeighbour(final ConnectedProperties p_isNeighbour_0_, final IBlockAccess p_isNeighbour_1_, final IBlockState p_isNeighbour_2_, final BlockPos p_isNeighbour_3_, final IBlockState p_isNeighbour_4_, final int p_isNeighbour_5_, final TextureAtlasSprite p_isNeighbour_6_, final int p_isNeighbour_7_) {
        if (p_isNeighbour_2_ == p_isNeighbour_4_) {
            return true;
        }
        if (p_isNeighbour_0_.connect == 2) {
            if (p_isNeighbour_4_ == null) {
                return false;
            }
            if (p_isNeighbour_4_ == ConnectedTextures.AIR_DEFAULT_STATE) {
                return false;
            }
            final TextureAtlasSprite textureatlassprite = getNeighbourIcon(p_isNeighbour_1_, p_isNeighbour_2_, p_isNeighbour_3_, p_isNeighbour_4_, p_isNeighbour_5_);
            return textureatlassprite == p_isNeighbour_6_;
        }
        else {
            if (p_isNeighbour_0_.connect == 3) {
                return p_isNeighbour_4_ != null && p_isNeighbour_4_ != ConnectedTextures.AIR_DEFAULT_STATE && p_isNeighbour_4_.getMaterial() == p_isNeighbour_2_.getMaterial();
            }
            if (!(p_isNeighbour_4_ instanceof BlockStateBase)) {
                return false;
            }
            final BlockStateBase blockstatebase = (BlockStateBase)p_isNeighbour_4_;
            final Block block = blockstatebase.getBlock();
            final int i = blockstatebase.getMetadata();
            return block == p_isNeighbour_2_.getBlock() && i == p_isNeighbour_7_;
        }
    }
    
    private static TextureAtlasSprite getNeighbourIcon(final IBlockAccess p_getNeighbourIcon_0_, final IBlockState p_getNeighbourIcon_1_, final BlockPos p_getNeighbourIcon_2_, IBlockState p_getNeighbourIcon_3_, final int p_getNeighbourIcon_4_) {
        p_getNeighbourIcon_3_ = p_getNeighbourIcon_3_.getBlock().getActualState(p_getNeighbourIcon_3_, p_getNeighbourIcon_0_, p_getNeighbourIcon_2_);
        final IBakedModel ibakedmodel = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelForState(p_getNeighbourIcon_3_);
        if (ibakedmodel == null) {
            return null;
        }
        final EnumFacing enumfacing = getFacing(p_getNeighbourIcon_4_);
        List list = ibakedmodel.getQuads(p_getNeighbourIcon_3_, enumfacing, 0L);
        if (Config.isBetterGrass()) {
            list = BetterGrass.getFaceQuads(p_getNeighbourIcon_0_, p_getNeighbourIcon_3_, p_getNeighbourIcon_2_, enumfacing, list);
        }
        if (list.size() > 0) {
            final BakedQuad bakedquad1 = list.get(0);
            return bakedquad1.getSprite();
        }
        final List list2 = ibakedmodel.getQuads(p_getNeighbourIcon_3_, null, 0L);
        for (int i = 0; i < list2.size(); ++i) {
            final BakedQuad bakedquad2 = list2.get(i);
            if (bakedquad2.getFace() == enumfacing) {
                return bakedquad2.getSprite();
            }
        }
        return null;
    }
    
    private static TextureAtlasSprite getConnectedTextureHorizontal(final ConnectedProperties p_getConnectedTextureHorizontal_0_, final IBlockAccess p_getConnectedTextureHorizontal_1_, final IBlockState p_getConnectedTextureHorizontal_2_, final BlockPos p_getConnectedTextureHorizontal_3_, final int p_getConnectedTextureHorizontal_4_, final int p_getConnectedTextureHorizontal_5_, final TextureAtlasSprite p_getConnectedTextureHorizontal_6_, final int p_getConnectedTextureHorizontal_7_) {
        boolean flag = false;
        boolean flag2 = false;
        Label_0859: {
            switch (p_getConnectedTextureHorizontal_4_) {
                case 0: {
                    switch (p_getConnectedTextureHorizontal_5_) {
                        case 0: {
                            flag = isNeighbour(p_getConnectedTextureHorizontal_0_, p_getConnectedTextureHorizontal_1_, p_getConnectedTextureHorizontal_2_, p_getConnectedTextureHorizontal_3_.west(), p_getConnectedTextureHorizontal_5_, p_getConnectedTextureHorizontal_6_, p_getConnectedTextureHorizontal_7_);
                            flag2 = isNeighbour(p_getConnectedTextureHorizontal_0_, p_getConnectedTextureHorizontal_1_, p_getConnectedTextureHorizontal_2_, p_getConnectedTextureHorizontal_3_.east(), p_getConnectedTextureHorizontal_5_, p_getConnectedTextureHorizontal_6_, p_getConnectedTextureHorizontal_7_);
                            break;
                        }
                        case 1: {
                            flag = isNeighbour(p_getConnectedTextureHorizontal_0_, p_getConnectedTextureHorizontal_1_, p_getConnectedTextureHorizontal_2_, p_getConnectedTextureHorizontal_3_.west(), p_getConnectedTextureHorizontal_5_, p_getConnectedTextureHorizontal_6_, p_getConnectedTextureHorizontal_7_);
                            flag2 = isNeighbour(p_getConnectedTextureHorizontal_0_, p_getConnectedTextureHorizontal_1_, p_getConnectedTextureHorizontal_2_, p_getConnectedTextureHorizontal_3_.east(), p_getConnectedTextureHorizontal_5_, p_getConnectedTextureHorizontal_6_, p_getConnectedTextureHorizontal_7_);
                            break;
                        }
                        case 2: {
                            flag = isNeighbour(p_getConnectedTextureHorizontal_0_, p_getConnectedTextureHorizontal_1_, p_getConnectedTextureHorizontal_2_, p_getConnectedTextureHorizontal_3_.east(), p_getConnectedTextureHorizontal_5_, p_getConnectedTextureHorizontal_6_, p_getConnectedTextureHorizontal_7_);
                            flag2 = isNeighbour(p_getConnectedTextureHorizontal_0_, p_getConnectedTextureHorizontal_1_, p_getConnectedTextureHorizontal_2_, p_getConnectedTextureHorizontal_3_.west(), p_getConnectedTextureHorizontal_5_, p_getConnectedTextureHorizontal_6_, p_getConnectedTextureHorizontal_7_);
                            break;
                        }
                        case 3: {
                            flag = isNeighbour(p_getConnectedTextureHorizontal_0_, p_getConnectedTextureHorizontal_1_, p_getConnectedTextureHorizontal_2_, p_getConnectedTextureHorizontal_3_.west(), p_getConnectedTextureHorizontal_5_, p_getConnectedTextureHorizontal_6_, p_getConnectedTextureHorizontal_7_);
                            flag2 = isNeighbour(p_getConnectedTextureHorizontal_0_, p_getConnectedTextureHorizontal_1_, p_getConnectedTextureHorizontal_2_, p_getConnectedTextureHorizontal_3_.east(), p_getConnectedTextureHorizontal_5_, p_getConnectedTextureHorizontal_6_, p_getConnectedTextureHorizontal_7_);
                            break;
                        }
                        case 4: {
                            flag = isNeighbour(p_getConnectedTextureHorizontal_0_, p_getConnectedTextureHorizontal_1_, p_getConnectedTextureHorizontal_2_, p_getConnectedTextureHorizontal_3_.north(), p_getConnectedTextureHorizontal_5_, p_getConnectedTextureHorizontal_6_, p_getConnectedTextureHorizontal_7_);
                            flag2 = isNeighbour(p_getConnectedTextureHorizontal_0_, p_getConnectedTextureHorizontal_1_, p_getConnectedTextureHorizontal_2_, p_getConnectedTextureHorizontal_3_.south(), p_getConnectedTextureHorizontal_5_, p_getConnectedTextureHorizontal_6_, p_getConnectedTextureHorizontal_7_);
                            break;
                        }
                        case 5: {
                            flag = isNeighbour(p_getConnectedTextureHorizontal_0_, p_getConnectedTextureHorizontal_1_, p_getConnectedTextureHorizontal_2_, p_getConnectedTextureHorizontal_3_.south(), p_getConnectedTextureHorizontal_5_, p_getConnectedTextureHorizontal_6_, p_getConnectedTextureHorizontal_7_);
                            flag2 = isNeighbour(p_getConnectedTextureHorizontal_0_, p_getConnectedTextureHorizontal_1_, p_getConnectedTextureHorizontal_2_, p_getConnectedTextureHorizontal_3_.north(), p_getConnectedTextureHorizontal_5_, p_getConnectedTextureHorizontal_6_, p_getConnectedTextureHorizontal_7_);
                            break;
                        }
                    }
                    break;
                }
                case 1: {
                    switch (p_getConnectedTextureHorizontal_5_) {
                        case 0: {
                            flag = isNeighbour(p_getConnectedTextureHorizontal_0_, p_getConnectedTextureHorizontal_1_, p_getConnectedTextureHorizontal_2_, p_getConnectedTextureHorizontal_3_.east(), p_getConnectedTextureHorizontal_5_, p_getConnectedTextureHorizontal_6_, p_getConnectedTextureHorizontal_7_);
                            flag2 = isNeighbour(p_getConnectedTextureHorizontal_0_, p_getConnectedTextureHorizontal_1_, p_getConnectedTextureHorizontal_2_, p_getConnectedTextureHorizontal_3_.west(), p_getConnectedTextureHorizontal_5_, p_getConnectedTextureHorizontal_6_, p_getConnectedTextureHorizontal_7_);
                            break;
                        }
                        case 1: {
                            flag = isNeighbour(p_getConnectedTextureHorizontal_0_, p_getConnectedTextureHorizontal_1_, p_getConnectedTextureHorizontal_2_, p_getConnectedTextureHorizontal_3_.west(), p_getConnectedTextureHorizontal_5_, p_getConnectedTextureHorizontal_6_, p_getConnectedTextureHorizontal_7_);
                            flag2 = isNeighbour(p_getConnectedTextureHorizontal_0_, p_getConnectedTextureHorizontal_1_, p_getConnectedTextureHorizontal_2_, p_getConnectedTextureHorizontal_3_.east(), p_getConnectedTextureHorizontal_5_, p_getConnectedTextureHorizontal_6_, p_getConnectedTextureHorizontal_7_);
                            break;
                        }
                        case 2: {
                            flag = isNeighbour(p_getConnectedTextureHorizontal_0_, p_getConnectedTextureHorizontal_1_, p_getConnectedTextureHorizontal_2_, p_getConnectedTextureHorizontal_3_.west(), p_getConnectedTextureHorizontal_5_, p_getConnectedTextureHorizontal_6_, p_getConnectedTextureHorizontal_7_);
                            flag2 = isNeighbour(p_getConnectedTextureHorizontal_0_, p_getConnectedTextureHorizontal_1_, p_getConnectedTextureHorizontal_2_, p_getConnectedTextureHorizontal_3_.east(), p_getConnectedTextureHorizontal_5_, p_getConnectedTextureHorizontal_6_, p_getConnectedTextureHorizontal_7_);
                            break;
                        }
                        case 3: {
                            flag = isNeighbour(p_getConnectedTextureHorizontal_0_, p_getConnectedTextureHorizontal_1_, p_getConnectedTextureHorizontal_2_, p_getConnectedTextureHorizontal_3_.west(), p_getConnectedTextureHorizontal_5_, p_getConnectedTextureHorizontal_6_, p_getConnectedTextureHorizontal_7_);
                            flag2 = isNeighbour(p_getConnectedTextureHorizontal_0_, p_getConnectedTextureHorizontal_1_, p_getConnectedTextureHorizontal_2_, p_getConnectedTextureHorizontal_3_.east(), p_getConnectedTextureHorizontal_5_, p_getConnectedTextureHorizontal_6_, p_getConnectedTextureHorizontal_7_);
                            break;
                        }
                        case 4: {
                            flag = isNeighbour(p_getConnectedTextureHorizontal_0_, p_getConnectedTextureHorizontal_1_, p_getConnectedTextureHorizontal_2_, p_getConnectedTextureHorizontal_3_.down(), p_getConnectedTextureHorizontal_5_, p_getConnectedTextureHorizontal_6_, p_getConnectedTextureHorizontal_7_);
                            flag2 = isNeighbour(p_getConnectedTextureHorizontal_0_, p_getConnectedTextureHorizontal_1_, p_getConnectedTextureHorizontal_2_, p_getConnectedTextureHorizontal_3_.up(), p_getConnectedTextureHorizontal_5_, p_getConnectedTextureHorizontal_6_, p_getConnectedTextureHorizontal_7_);
                            break;
                        }
                        case 5: {
                            flag = isNeighbour(p_getConnectedTextureHorizontal_0_, p_getConnectedTextureHorizontal_1_, p_getConnectedTextureHorizontal_2_, p_getConnectedTextureHorizontal_3_.up(), p_getConnectedTextureHorizontal_5_, p_getConnectedTextureHorizontal_6_, p_getConnectedTextureHorizontal_7_);
                            flag2 = isNeighbour(p_getConnectedTextureHorizontal_0_, p_getConnectedTextureHorizontal_1_, p_getConnectedTextureHorizontal_2_, p_getConnectedTextureHorizontal_3_.down(), p_getConnectedTextureHorizontal_5_, p_getConnectedTextureHorizontal_6_, p_getConnectedTextureHorizontal_7_);
                            break;
                        }
                    }
                    break;
                }
                case 2: {
                    switch (p_getConnectedTextureHorizontal_5_) {
                        case 0: {
                            flag = isNeighbour(p_getConnectedTextureHorizontal_0_, p_getConnectedTextureHorizontal_1_, p_getConnectedTextureHorizontal_2_, p_getConnectedTextureHorizontal_3_.south(), p_getConnectedTextureHorizontal_5_, p_getConnectedTextureHorizontal_6_, p_getConnectedTextureHorizontal_7_);
                            flag2 = isNeighbour(p_getConnectedTextureHorizontal_0_, p_getConnectedTextureHorizontal_1_, p_getConnectedTextureHorizontal_2_, p_getConnectedTextureHorizontal_3_.north(), p_getConnectedTextureHorizontal_5_, p_getConnectedTextureHorizontal_6_, p_getConnectedTextureHorizontal_7_);
                            break Label_0859;
                        }
                        case 1: {
                            flag = isNeighbour(p_getConnectedTextureHorizontal_0_, p_getConnectedTextureHorizontal_1_, p_getConnectedTextureHorizontal_2_, p_getConnectedTextureHorizontal_3_.north(), p_getConnectedTextureHorizontal_5_, p_getConnectedTextureHorizontal_6_, p_getConnectedTextureHorizontal_7_);
                            flag2 = isNeighbour(p_getConnectedTextureHorizontal_0_, p_getConnectedTextureHorizontal_1_, p_getConnectedTextureHorizontal_2_, p_getConnectedTextureHorizontal_3_.south(), p_getConnectedTextureHorizontal_5_, p_getConnectedTextureHorizontal_6_, p_getConnectedTextureHorizontal_7_);
                            break Label_0859;
                        }
                        case 2: {
                            flag = isNeighbour(p_getConnectedTextureHorizontal_0_, p_getConnectedTextureHorizontal_1_, p_getConnectedTextureHorizontal_2_, p_getConnectedTextureHorizontal_3_.down(), p_getConnectedTextureHorizontal_5_, p_getConnectedTextureHorizontal_6_, p_getConnectedTextureHorizontal_7_);
                            flag2 = isNeighbour(p_getConnectedTextureHorizontal_0_, p_getConnectedTextureHorizontal_1_, p_getConnectedTextureHorizontal_2_, p_getConnectedTextureHorizontal_3_.up(), p_getConnectedTextureHorizontal_5_, p_getConnectedTextureHorizontal_6_, p_getConnectedTextureHorizontal_7_);
                            break Label_0859;
                        }
                        case 3: {
                            flag = isNeighbour(p_getConnectedTextureHorizontal_0_, p_getConnectedTextureHorizontal_1_, p_getConnectedTextureHorizontal_2_, p_getConnectedTextureHorizontal_3_.up(), p_getConnectedTextureHorizontal_5_, p_getConnectedTextureHorizontal_6_, p_getConnectedTextureHorizontal_7_);
                            flag2 = isNeighbour(p_getConnectedTextureHorizontal_0_, p_getConnectedTextureHorizontal_1_, p_getConnectedTextureHorizontal_2_, p_getConnectedTextureHorizontal_3_.down(), p_getConnectedTextureHorizontal_5_, p_getConnectedTextureHorizontal_6_, p_getConnectedTextureHorizontal_7_);
                            break Label_0859;
                        }
                        case 4: {
                            flag = isNeighbour(p_getConnectedTextureHorizontal_0_, p_getConnectedTextureHorizontal_1_, p_getConnectedTextureHorizontal_2_, p_getConnectedTextureHorizontal_3_.north(), p_getConnectedTextureHorizontal_5_, p_getConnectedTextureHorizontal_6_, p_getConnectedTextureHorizontal_7_);
                            flag2 = isNeighbour(p_getConnectedTextureHorizontal_0_, p_getConnectedTextureHorizontal_1_, p_getConnectedTextureHorizontal_2_, p_getConnectedTextureHorizontal_3_.south(), p_getConnectedTextureHorizontal_5_, p_getConnectedTextureHorizontal_6_, p_getConnectedTextureHorizontal_7_);
                            break Label_0859;
                        }
                        case 5: {
                            flag = isNeighbour(p_getConnectedTextureHorizontal_0_, p_getConnectedTextureHorizontal_1_, p_getConnectedTextureHorizontal_2_, p_getConnectedTextureHorizontal_3_.north(), p_getConnectedTextureHorizontal_5_, p_getConnectedTextureHorizontal_6_, p_getConnectedTextureHorizontal_7_);
                            flag2 = isNeighbour(p_getConnectedTextureHorizontal_0_, p_getConnectedTextureHorizontal_1_, p_getConnectedTextureHorizontal_2_, p_getConnectedTextureHorizontal_3_.south(), p_getConnectedTextureHorizontal_5_, p_getConnectedTextureHorizontal_6_, p_getConnectedTextureHorizontal_7_);
                            break Label_0859;
                        }
                    }
                    break;
                }
            }
        }
        int i = 3;
        if (flag) {
            if (flag2) {
                i = 1;
            }
            else {
                i = 2;
            }
        }
        else if (flag2) {
            i = 0;
        }
        else {
            i = 3;
        }
        return p_getConnectedTextureHorizontal_0_.tileIcons[i];
    }
    
    private static TextureAtlasSprite getConnectedTextureVertical(final ConnectedProperties p_getConnectedTextureVertical_0_, final IBlockAccess p_getConnectedTextureVertical_1_, final IBlockState p_getConnectedTextureVertical_2_, final BlockPos p_getConnectedTextureVertical_3_, final int p_getConnectedTextureVertical_4_, final int p_getConnectedTextureVertical_5_, final TextureAtlasSprite p_getConnectedTextureVertical_6_, final int p_getConnectedTextureVertical_7_) {
        boolean flag = false;
        boolean flag2 = false;
        switch (p_getConnectedTextureVertical_4_) {
            case 0: {
                if (p_getConnectedTextureVertical_5_ == 1) {
                    flag = isNeighbour(p_getConnectedTextureVertical_0_, p_getConnectedTextureVertical_1_, p_getConnectedTextureVertical_2_, p_getConnectedTextureVertical_3_.south(), p_getConnectedTextureVertical_5_, p_getConnectedTextureVertical_6_, p_getConnectedTextureVertical_7_);
                    flag2 = isNeighbour(p_getConnectedTextureVertical_0_, p_getConnectedTextureVertical_1_, p_getConnectedTextureVertical_2_, p_getConnectedTextureVertical_3_.north(), p_getConnectedTextureVertical_5_, p_getConnectedTextureVertical_6_, p_getConnectedTextureVertical_7_);
                    break;
                }
                if (p_getConnectedTextureVertical_5_ == 0) {
                    flag = isNeighbour(p_getConnectedTextureVertical_0_, p_getConnectedTextureVertical_1_, p_getConnectedTextureVertical_2_, p_getConnectedTextureVertical_3_.north(), p_getConnectedTextureVertical_5_, p_getConnectedTextureVertical_6_, p_getConnectedTextureVertical_7_);
                    flag2 = isNeighbour(p_getConnectedTextureVertical_0_, p_getConnectedTextureVertical_1_, p_getConnectedTextureVertical_2_, p_getConnectedTextureVertical_3_.south(), p_getConnectedTextureVertical_5_, p_getConnectedTextureVertical_6_, p_getConnectedTextureVertical_7_);
                    break;
                }
                flag = isNeighbour(p_getConnectedTextureVertical_0_, p_getConnectedTextureVertical_1_, p_getConnectedTextureVertical_2_, p_getConnectedTextureVertical_3_.down(), p_getConnectedTextureVertical_5_, p_getConnectedTextureVertical_6_, p_getConnectedTextureVertical_7_);
                flag2 = isNeighbour(p_getConnectedTextureVertical_0_, p_getConnectedTextureVertical_1_, p_getConnectedTextureVertical_2_, p_getConnectedTextureVertical_3_.up(), p_getConnectedTextureVertical_5_, p_getConnectedTextureVertical_6_, p_getConnectedTextureVertical_7_);
                break;
            }
            case 1: {
                if (p_getConnectedTextureVertical_5_ == 3) {
                    flag = isNeighbour(p_getConnectedTextureVertical_0_, p_getConnectedTextureVertical_1_, p_getConnectedTextureVertical_2_, p_getConnectedTextureVertical_3_.down(), p_getConnectedTextureVertical_5_, p_getConnectedTextureVertical_6_, p_getConnectedTextureVertical_7_);
                    flag2 = isNeighbour(p_getConnectedTextureVertical_0_, p_getConnectedTextureVertical_1_, p_getConnectedTextureVertical_2_, p_getConnectedTextureVertical_3_.up(), p_getConnectedTextureVertical_5_, p_getConnectedTextureVertical_6_, p_getConnectedTextureVertical_7_);
                    break;
                }
                if (p_getConnectedTextureVertical_5_ == 2) {
                    flag = isNeighbour(p_getConnectedTextureVertical_0_, p_getConnectedTextureVertical_1_, p_getConnectedTextureVertical_2_, p_getConnectedTextureVertical_3_.up(), p_getConnectedTextureVertical_5_, p_getConnectedTextureVertical_6_, p_getConnectedTextureVertical_7_);
                    flag2 = isNeighbour(p_getConnectedTextureVertical_0_, p_getConnectedTextureVertical_1_, p_getConnectedTextureVertical_2_, p_getConnectedTextureVertical_3_.down(), p_getConnectedTextureVertical_5_, p_getConnectedTextureVertical_6_, p_getConnectedTextureVertical_7_);
                    break;
                }
                flag = isNeighbour(p_getConnectedTextureVertical_0_, p_getConnectedTextureVertical_1_, p_getConnectedTextureVertical_2_, p_getConnectedTextureVertical_3_.south(), p_getConnectedTextureVertical_5_, p_getConnectedTextureVertical_6_, p_getConnectedTextureVertical_7_);
                flag2 = isNeighbour(p_getConnectedTextureVertical_0_, p_getConnectedTextureVertical_1_, p_getConnectedTextureVertical_2_, p_getConnectedTextureVertical_3_.north(), p_getConnectedTextureVertical_5_, p_getConnectedTextureVertical_6_, p_getConnectedTextureVertical_7_);
                break;
            }
            case 2: {
                if (p_getConnectedTextureVertical_5_ == 5) {
                    flag = isNeighbour(p_getConnectedTextureVertical_0_, p_getConnectedTextureVertical_1_, p_getConnectedTextureVertical_2_, p_getConnectedTextureVertical_3_.up(), p_getConnectedTextureVertical_5_, p_getConnectedTextureVertical_6_, p_getConnectedTextureVertical_7_);
                    flag2 = isNeighbour(p_getConnectedTextureVertical_0_, p_getConnectedTextureVertical_1_, p_getConnectedTextureVertical_2_, p_getConnectedTextureVertical_3_.down(), p_getConnectedTextureVertical_5_, p_getConnectedTextureVertical_6_, p_getConnectedTextureVertical_7_);
                    break;
                }
                if (p_getConnectedTextureVertical_5_ == 4) {
                    flag = isNeighbour(p_getConnectedTextureVertical_0_, p_getConnectedTextureVertical_1_, p_getConnectedTextureVertical_2_, p_getConnectedTextureVertical_3_.down(), p_getConnectedTextureVertical_5_, p_getConnectedTextureVertical_6_, p_getConnectedTextureVertical_7_);
                    flag2 = isNeighbour(p_getConnectedTextureVertical_0_, p_getConnectedTextureVertical_1_, p_getConnectedTextureVertical_2_, p_getConnectedTextureVertical_3_.up(), p_getConnectedTextureVertical_5_, p_getConnectedTextureVertical_6_, p_getConnectedTextureVertical_7_);
                    break;
                }
                flag = isNeighbour(p_getConnectedTextureVertical_0_, p_getConnectedTextureVertical_1_, p_getConnectedTextureVertical_2_, p_getConnectedTextureVertical_3_.west(), p_getConnectedTextureVertical_5_, p_getConnectedTextureVertical_6_, p_getConnectedTextureVertical_7_);
                flag2 = isNeighbour(p_getConnectedTextureVertical_0_, p_getConnectedTextureVertical_1_, p_getConnectedTextureVertical_2_, p_getConnectedTextureVertical_3_.east(), p_getConnectedTextureVertical_5_, p_getConnectedTextureVertical_6_, p_getConnectedTextureVertical_7_);
                break;
            }
        }
        int i = 3;
        if (flag) {
            if (flag2) {
                i = 1;
            }
            else {
                i = 2;
            }
        }
        else if (flag2) {
            i = 0;
        }
        else {
            i = 3;
        }
        return p_getConnectedTextureVertical_0_.tileIcons[i];
    }
    
    private static TextureAtlasSprite getConnectedTextureHorizontalVertical(final ConnectedProperties p_getConnectedTextureHorizontalVertical_0_, final IBlockAccess p_getConnectedTextureHorizontalVertical_1_, final IBlockState p_getConnectedTextureHorizontalVertical_2_, final BlockPos p_getConnectedTextureHorizontalVertical_3_, final int p_getConnectedTextureHorizontalVertical_4_, final int p_getConnectedTextureHorizontalVertical_5_, final TextureAtlasSprite p_getConnectedTextureHorizontalVertical_6_, final int p_getConnectedTextureHorizontalVertical_7_) {
        final TextureAtlasSprite[] atextureatlassprite = p_getConnectedTextureHorizontalVertical_0_.tileIcons;
        final TextureAtlasSprite textureatlassprite = getConnectedTextureHorizontal(p_getConnectedTextureHorizontalVertical_0_, p_getConnectedTextureHorizontalVertical_1_, p_getConnectedTextureHorizontalVertical_2_, p_getConnectedTextureHorizontalVertical_3_, p_getConnectedTextureHorizontalVertical_4_, p_getConnectedTextureHorizontalVertical_5_, p_getConnectedTextureHorizontalVertical_6_, p_getConnectedTextureHorizontalVertical_7_);
        if (textureatlassprite != null && textureatlassprite != p_getConnectedTextureHorizontalVertical_6_ && textureatlassprite != atextureatlassprite[3]) {
            return textureatlassprite;
        }
        final TextureAtlasSprite textureatlassprite2 = getConnectedTextureVertical(p_getConnectedTextureHorizontalVertical_0_, p_getConnectedTextureHorizontalVertical_1_, p_getConnectedTextureHorizontalVertical_2_, p_getConnectedTextureHorizontalVertical_3_, p_getConnectedTextureHorizontalVertical_4_, p_getConnectedTextureHorizontalVertical_5_, p_getConnectedTextureHorizontalVertical_6_, p_getConnectedTextureHorizontalVertical_7_);
        if (textureatlassprite2 == atextureatlassprite[0]) {
            return atextureatlassprite[4];
        }
        if (textureatlassprite2 == atextureatlassprite[1]) {
            return atextureatlassprite[5];
        }
        return (textureatlassprite2 == atextureatlassprite[2]) ? atextureatlassprite[6] : textureatlassprite2;
    }
    
    private static TextureAtlasSprite getConnectedTextureVerticalHorizontal(final ConnectedProperties p_getConnectedTextureVerticalHorizontal_0_, final IBlockAccess p_getConnectedTextureVerticalHorizontal_1_, final IBlockState p_getConnectedTextureVerticalHorizontal_2_, final BlockPos p_getConnectedTextureVerticalHorizontal_3_, final int p_getConnectedTextureVerticalHorizontal_4_, final int p_getConnectedTextureVerticalHorizontal_5_, final TextureAtlasSprite p_getConnectedTextureVerticalHorizontal_6_, final int p_getConnectedTextureVerticalHorizontal_7_) {
        final TextureAtlasSprite[] atextureatlassprite = p_getConnectedTextureVerticalHorizontal_0_.tileIcons;
        final TextureAtlasSprite textureatlassprite = getConnectedTextureVertical(p_getConnectedTextureVerticalHorizontal_0_, p_getConnectedTextureVerticalHorizontal_1_, p_getConnectedTextureVerticalHorizontal_2_, p_getConnectedTextureVerticalHorizontal_3_, p_getConnectedTextureVerticalHorizontal_4_, p_getConnectedTextureVerticalHorizontal_5_, p_getConnectedTextureVerticalHorizontal_6_, p_getConnectedTextureVerticalHorizontal_7_);
        if (textureatlassprite != null && textureatlassprite != p_getConnectedTextureVerticalHorizontal_6_ && textureatlassprite != atextureatlassprite[3]) {
            return textureatlassprite;
        }
        final TextureAtlasSprite textureatlassprite2 = getConnectedTextureHorizontal(p_getConnectedTextureVerticalHorizontal_0_, p_getConnectedTextureVerticalHorizontal_1_, p_getConnectedTextureVerticalHorizontal_2_, p_getConnectedTextureVerticalHorizontal_3_, p_getConnectedTextureVerticalHorizontal_4_, p_getConnectedTextureVerticalHorizontal_5_, p_getConnectedTextureVerticalHorizontal_6_, p_getConnectedTextureVerticalHorizontal_7_);
        if (textureatlassprite2 == atextureatlassprite[0]) {
            return atextureatlassprite[4];
        }
        if (textureatlassprite2 == atextureatlassprite[1]) {
            return atextureatlassprite[5];
        }
        return (textureatlassprite2 == atextureatlassprite[2]) ? atextureatlassprite[6] : textureatlassprite2;
    }
    
    private static TextureAtlasSprite getConnectedTextureTop(final ConnectedProperties p_getConnectedTextureTop_0_, final IBlockAccess p_getConnectedTextureTop_1_, final IBlockState p_getConnectedTextureTop_2_, final BlockPos p_getConnectedTextureTop_3_, final int p_getConnectedTextureTop_4_, final int p_getConnectedTextureTop_5_, final TextureAtlasSprite p_getConnectedTextureTop_6_, final int p_getConnectedTextureTop_7_) {
        boolean flag = false;
        switch (p_getConnectedTextureTop_4_) {
            case 0: {
                if (p_getConnectedTextureTop_5_ == 1 || p_getConnectedTextureTop_5_ == 0) {
                    return null;
                }
                flag = isNeighbour(p_getConnectedTextureTop_0_, p_getConnectedTextureTop_1_, p_getConnectedTextureTop_2_, p_getConnectedTextureTop_3_.up(), p_getConnectedTextureTop_5_, p_getConnectedTextureTop_6_, p_getConnectedTextureTop_7_);
                break;
            }
            case 1: {
                if (p_getConnectedTextureTop_5_ == 3 || p_getConnectedTextureTop_5_ == 2) {
                    return null;
                }
                flag = isNeighbour(p_getConnectedTextureTop_0_, p_getConnectedTextureTop_1_, p_getConnectedTextureTop_2_, p_getConnectedTextureTop_3_.south(), p_getConnectedTextureTop_5_, p_getConnectedTextureTop_6_, p_getConnectedTextureTop_7_);
                break;
            }
            case 2: {
                if (p_getConnectedTextureTop_5_ == 5 || p_getConnectedTextureTop_5_ == 4) {
                    return null;
                }
                flag = isNeighbour(p_getConnectedTextureTop_0_, p_getConnectedTextureTop_1_, p_getConnectedTextureTop_2_, p_getConnectedTextureTop_3_.east(), p_getConnectedTextureTop_5_, p_getConnectedTextureTop_6_, p_getConnectedTextureTop_7_);
                break;
            }
        }
        if (flag) {
            return p_getConnectedTextureTop_0_.tileIcons[0];
        }
        return null;
    }
    
    public static void updateIcons(final TextureMap p_updateIcons_0_) {
        ConnectedTextures.blockProperties = null;
        ConnectedTextures.tileProperties = null;
        ConnectedTextures.spriteQuadMaps = null;
        ConnectedTextures.spriteQuadCompactMaps = null;
        if (Config.isConnectedTextures()) {
            final IResourcePack[] airesourcepack = Config.getResourcePacks();
            for (int i = airesourcepack.length - 1; i >= 0; --i) {
                final IResourcePack iresourcepack = airesourcepack[i];
                updateIcons(p_updateIcons_0_, iresourcepack);
            }
            updateIcons(p_updateIcons_0_, Config.getDefaultResourcePack());
            final ResourceLocation resourcelocation = new ResourceLocation("mcpatcher/ctm/default/empty");
            ConnectedTextures.emptySprite = p_updateIcons_0_.registerSprite(resourcelocation);
            ConnectedTextures.spriteQuadMaps = new Map[p_updateIcons_0_.getCountRegisteredSprites() + 1];
            ConnectedTextures.spriteQuadFullMaps = new Map[p_updateIcons_0_.getCountRegisteredSprites() + 1];
            ConnectedTextures.spriteQuadCompactMaps = new Map[p_updateIcons_0_.getCountRegisteredSprites() + 1][];
            if (ConnectedTextures.blockProperties.length <= 0) {
                ConnectedTextures.blockProperties = null;
            }
            if (ConnectedTextures.tileProperties.length <= 0) {
                ConnectedTextures.tileProperties = null;
            }
        }
    }
    
    private static void updateIconEmpty(final TextureMap p_updateIconEmpty_0_) {
    }
    
    public static void updateIcons(final TextureMap p_updateIcons_0_, final IResourcePack p_updateIcons_1_) {
        final String[] astring = ResUtils.collectFiles(p_updateIcons_1_, "mcpatcher/ctm/", ".properties", getDefaultCtmPaths());
        Arrays.sort(astring);
        final List list = makePropertyList(ConnectedTextures.tileProperties);
        final List list2 = makePropertyList(ConnectedTextures.blockProperties);
        for (int i = 0; i < astring.length; ++i) {
            final String s = astring[i];
            Config.dbg("ConnectedTextures: " + s);
            try {
                final ResourceLocation resourcelocation = new ResourceLocation(s);
                final InputStream inputstream = p_updateIcons_1_.getInputStream(resourcelocation);
                if (inputstream == null) {
                    Config.warn("ConnectedTextures file not found: " + s);
                }
                else {
                    final Properties properties = new Properties();
                    properties.load(inputstream);
                    final ConnectedProperties connectedproperties = new ConnectedProperties(properties, s);
                    if (connectedproperties.isValid(s)) {
                        connectedproperties.updateIcons(p_updateIcons_0_);
                        addToTileList(connectedproperties, list);
                        addToBlockList(connectedproperties, list2);
                    }
                }
            }
            catch (final FileNotFoundException var11) {
                Config.warn("ConnectedTextures file not found: " + s);
            }
            catch (final Exception exception) {
                exception.printStackTrace();
            }
        }
        ConnectedTextures.blockProperties = propertyListToArray(list2);
        ConnectedTextures.tileProperties = propertyListToArray(list);
        ConnectedTextures.multipass = detectMultipass();
        Config.dbg("Multipass connected textures: " + ConnectedTextures.multipass);
    }
    
    private static List makePropertyList(final ConnectedProperties[][] p_makePropertyList_0_) {
        final List list = new ArrayList();
        if (p_makePropertyList_0_ != null) {
            for (int i = 0; i < p_makePropertyList_0_.length; ++i) {
                final ConnectedProperties[] aconnectedproperties = p_makePropertyList_0_[i];
                List list2 = null;
                if (aconnectedproperties != null) {
                    list2 = new ArrayList(Arrays.asList(aconnectedproperties));
                }
                list.add(list2);
            }
        }
        return list;
    }
    
    private static boolean detectMultipass() {
        final List list = new ArrayList();
        for (int i = 0; i < ConnectedTextures.tileProperties.length; ++i) {
            final ConnectedProperties[] aconnectedproperties = ConnectedTextures.tileProperties[i];
            if (aconnectedproperties != null) {
                list.addAll(Arrays.asList(aconnectedproperties));
            }
        }
        for (int k = 0; k < ConnectedTextures.blockProperties.length; ++k) {
            final ConnectedProperties[] aconnectedproperties2 = ConnectedTextures.blockProperties[k];
            if (aconnectedproperties2 != null) {
                list.addAll(Arrays.asList(aconnectedproperties2));
            }
        }
        final ConnectedProperties[] aconnectedproperties3 = list.toArray(new ConnectedProperties[list.size()]);
        final Set set1 = new HashSet();
        final Set set2 = new HashSet();
        for (int j = 0; j < aconnectedproperties3.length; ++j) {
            final ConnectedProperties connectedproperties = aconnectedproperties3[j];
            if (connectedproperties.matchTileIcons != null) {
                set1.addAll(Arrays.asList(connectedproperties.matchTileIcons));
            }
            if (connectedproperties.tileIcons != null) {
                set2.addAll(Arrays.asList(connectedproperties.tileIcons));
            }
        }
        set1.retainAll(set2);
        return !set1.isEmpty();
    }
    
    private static ConnectedProperties[][] propertyListToArray(final List p_propertyListToArray_0_) {
        final ConnectedProperties[][] aconnectedproperties = new ConnectedProperties[p_propertyListToArray_0_.size()][];
        for (int i = 0; i < p_propertyListToArray_0_.size(); ++i) {
            final List list = p_propertyListToArray_0_.get(i);
            if (list != null) {
                final ConnectedProperties[] aconnectedproperties2 = list.toArray(new ConnectedProperties[list.size()]);
                aconnectedproperties[i] = aconnectedproperties2;
            }
        }
        return aconnectedproperties;
    }
    
    private static void addToTileList(final ConnectedProperties p_addToTileList_0_, final List p_addToTileList_1_) {
        if (p_addToTileList_0_.matchTileIcons != null) {
            for (int i = 0; i < p_addToTileList_0_.matchTileIcons.length; ++i) {
                final TextureAtlasSprite textureatlassprite = p_addToTileList_0_.matchTileIcons[i];
                if (!(textureatlassprite instanceof TextureAtlasSprite)) {
                    Config.warn("TextureAtlasSprite is not TextureAtlasSprite: " + textureatlassprite + ", name: " + textureatlassprite.getIconName());
                }
                else {
                    final int j = textureatlassprite.getIndexInMap();
                    if (j < 0) {
                        Config.warn("Invalid tile ID: " + j + ", icon: " + textureatlassprite.getIconName());
                    }
                    else {
                        addToList(p_addToTileList_0_, p_addToTileList_1_, j);
                    }
                }
            }
        }
    }
    
    private static void addToBlockList(final ConnectedProperties p_addToBlockList_0_, final List p_addToBlockList_1_) {
        if (p_addToBlockList_0_.matchBlocks != null) {
            for (int i = 0; i < p_addToBlockList_0_.matchBlocks.length; ++i) {
                final int j = p_addToBlockList_0_.matchBlocks[i].getBlockId();
                if (j < 0) {
                    Config.warn("Invalid block ID: " + j);
                }
                else {
                    addToList(p_addToBlockList_0_, p_addToBlockList_1_, j);
                }
            }
        }
    }
    
    private static void addToList(final ConnectedProperties p_addToList_0_, final List p_addToList_1_, final int p_addToList_2_) {
        while (p_addToList_2_ >= p_addToList_1_.size()) {
            p_addToList_1_.add(null);
        }
        List list = p_addToList_1_.get(p_addToList_2_);
        if (list == null) {
            list = new ArrayList();
            p_addToList_1_.set(p_addToList_2_, list);
        }
        list.add(p_addToList_0_);
    }
    
    private static String[] getDefaultCtmPaths() {
        final List list = new ArrayList();
        final String s = "mcpatcher/ctm/default/";
        if (Config.isFromDefaultResourcePack(new ResourceLocation("textures/blocks/glass.png"))) {
            list.add(String.valueOf(s) + "glass.properties");
            list.add(String.valueOf(s) + "glasspane.properties");
        }
        if (Config.isFromDefaultResourcePack(new ResourceLocation("textures/blocks/bookshelf.png"))) {
            list.add(String.valueOf(s) + "bookshelf.properties");
        }
        if (Config.isFromDefaultResourcePack(new ResourceLocation("textures/blocks/sandstone_normal.png"))) {
            list.add(String.valueOf(s) + "sandstone.properties");
        }
        final String[] astring = { "white", "orange", "magenta", "light_blue", "yellow", "lime", "pink", "gray", "silver", "cyan", "purple", "blue", "brown", "green", "red", "black" };
        for (int i = 0; i < astring.length; ++i) {
            final String s2 = astring[i];
            if (Config.isFromDefaultResourcePack(new ResourceLocation("textures/blocks/glass_" + s2 + ".png"))) {
                list.add(String.valueOf(s) + i + "_glass_" + s2 + "/glass_" + s2 + ".properties");
                list.add(String.valueOf(s) + i + "_glass_" + s2 + "/glass_pane_" + s2 + ".properties");
            }
        }
        final String[] astring2 = list.toArray(new String[list.size()]);
        return astring2;
    }
}
