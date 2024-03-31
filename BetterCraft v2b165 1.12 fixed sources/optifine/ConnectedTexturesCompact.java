// 
// Decompiled by Procyon v0.6.0
// 

package optifine;

import java.util.IdentityHashMap;
import java.util.Map;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.block.model.BakedQuad;

public class ConnectedTexturesCompact
{
    private static final int COMPACT_NONE = 0;
    private static final int COMPACT_ALL = 1;
    private static final int COMPACT_V = 2;
    private static final int COMPACT_H = 3;
    private static final int COMPACT_HV = 4;
    
    public static BakedQuad[] getConnectedTextureCtmCompact(final int p_getConnectedTextureCtmCompact_0_, final ConnectedProperties p_getConnectedTextureCtmCompact_1_, final int p_getConnectedTextureCtmCompact_2_, final BakedQuad p_getConnectedTextureCtmCompact_3_, final RenderEnv p_getConnectedTextureCtmCompact_4_) {
        if (p_getConnectedTextureCtmCompact_1_.ctmTileIndexes != null && p_getConnectedTextureCtmCompact_0_ >= 0 && p_getConnectedTextureCtmCompact_0_ < p_getConnectedTextureCtmCompact_1_.ctmTileIndexes.length) {
            final int i = p_getConnectedTextureCtmCompact_1_.ctmTileIndexes[p_getConnectedTextureCtmCompact_0_];
            if (i >= 0 && i <= p_getConnectedTextureCtmCompact_1_.tileIcons.length) {
                return getQuadsCompact(i, p_getConnectedTextureCtmCompact_1_.tileIcons, p_getConnectedTextureCtmCompact_3_, p_getConnectedTextureCtmCompact_4_);
            }
        }
        switch (p_getConnectedTextureCtmCompact_0_) {
            case 1: {
                return getQuadsCompactH(0, 3, p_getConnectedTextureCtmCompact_1_.tileIcons, p_getConnectedTextureCtmCompact_2_, p_getConnectedTextureCtmCompact_3_, p_getConnectedTextureCtmCompact_4_);
            }
            case 2: {
                return getQuadsCompact(3, p_getConnectedTextureCtmCompact_1_.tileIcons, p_getConnectedTextureCtmCompact_3_, p_getConnectedTextureCtmCompact_4_);
            }
            case 3: {
                return getQuadsCompactH(3, 0, p_getConnectedTextureCtmCompact_1_.tileIcons, p_getConnectedTextureCtmCompact_2_, p_getConnectedTextureCtmCompact_3_, p_getConnectedTextureCtmCompact_4_);
            }
            case 4: {
                return getQuadsCompact4(0, 3, 2, 4, p_getConnectedTextureCtmCompact_1_.tileIcons, p_getConnectedTextureCtmCompact_2_, p_getConnectedTextureCtmCompact_3_, p_getConnectedTextureCtmCompact_4_);
            }
            case 5: {
                return getQuadsCompact4(3, 0, 4, 2, p_getConnectedTextureCtmCompact_1_.tileIcons, p_getConnectedTextureCtmCompact_2_, p_getConnectedTextureCtmCompact_3_, p_getConnectedTextureCtmCompact_4_);
            }
            case 6: {
                return getQuadsCompact4(2, 4, 2, 4, p_getConnectedTextureCtmCompact_1_.tileIcons, p_getConnectedTextureCtmCompact_2_, p_getConnectedTextureCtmCompact_3_, p_getConnectedTextureCtmCompact_4_);
            }
            case 7: {
                return getQuadsCompact4(3, 3, 4, 4, p_getConnectedTextureCtmCompact_1_.tileIcons, p_getConnectedTextureCtmCompact_2_, p_getConnectedTextureCtmCompact_3_, p_getConnectedTextureCtmCompact_4_);
            }
            case 8: {
                return getQuadsCompact4(4, 1, 4, 4, p_getConnectedTextureCtmCompact_1_.tileIcons, p_getConnectedTextureCtmCompact_2_, p_getConnectedTextureCtmCompact_3_, p_getConnectedTextureCtmCompact_4_);
            }
            case 9: {
                return getQuadsCompact4(4, 4, 4, 1, p_getConnectedTextureCtmCompact_1_.tileIcons, p_getConnectedTextureCtmCompact_2_, p_getConnectedTextureCtmCompact_3_, p_getConnectedTextureCtmCompact_4_);
            }
            case 10: {
                return getQuadsCompact4(1, 4, 1, 4, p_getConnectedTextureCtmCompact_1_.tileIcons, p_getConnectedTextureCtmCompact_2_, p_getConnectedTextureCtmCompact_3_, p_getConnectedTextureCtmCompact_4_);
            }
            case 11: {
                return getQuadsCompact4(1, 1, 4, 4, p_getConnectedTextureCtmCompact_1_.tileIcons, p_getConnectedTextureCtmCompact_2_, p_getConnectedTextureCtmCompact_3_, p_getConnectedTextureCtmCompact_4_);
            }
            case 12: {
                return getQuadsCompactV(0, 2, p_getConnectedTextureCtmCompact_1_.tileIcons, p_getConnectedTextureCtmCompact_2_, p_getConnectedTextureCtmCompact_3_, p_getConnectedTextureCtmCompact_4_);
            }
            case 13: {
                return getQuadsCompact4(0, 3, 2, 1, p_getConnectedTextureCtmCompact_1_.tileIcons, p_getConnectedTextureCtmCompact_2_, p_getConnectedTextureCtmCompact_3_, p_getConnectedTextureCtmCompact_4_);
            }
            case 14: {
                return getQuadsCompactV(3, 1, p_getConnectedTextureCtmCompact_1_.tileIcons, p_getConnectedTextureCtmCompact_2_, p_getConnectedTextureCtmCompact_3_, p_getConnectedTextureCtmCompact_4_);
            }
            case 15: {
                return getQuadsCompact4(3, 0, 1, 2, p_getConnectedTextureCtmCompact_1_.tileIcons, p_getConnectedTextureCtmCompact_2_, p_getConnectedTextureCtmCompact_3_, p_getConnectedTextureCtmCompact_4_);
            }
            case 16: {
                return getQuadsCompact4(2, 4, 0, 3, p_getConnectedTextureCtmCompact_1_.tileIcons, p_getConnectedTextureCtmCompact_2_, p_getConnectedTextureCtmCompact_3_, p_getConnectedTextureCtmCompact_4_);
            }
            case 17: {
                return getQuadsCompact4(4, 2, 3, 0, p_getConnectedTextureCtmCompact_1_.tileIcons, p_getConnectedTextureCtmCompact_2_, p_getConnectedTextureCtmCompact_3_, p_getConnectedTextureCtmCompact_4_);
            }
            case 18: {
                return getQuadsCompact4(4, 4, 3, 3, p_getConnectedTextureCtmCompact_1_.tileIcons, p_getConnectedTextureCtmCompact_2_, p_getConnectedTextureCtmCompact_3_, p_getConnectedTextureCtmCompact_4_);
            }
            case 19: {
                return getQuadsCompact4(4, 2, 4, 2, p_getConnectedTextureCtmCompact_1_.tileIcons, p_getConnectedTextureCtmCompact_2_, p_getConnectedTextureCtmCompact_3_, p_getConnectedTextureCtmCompact_4_);
            }
            case 20: {
                return getQuadsCompact4(1, 4, 4, 4, p_getConnectedTextureCtmCompact_1_.tileIcons, p_getConnectedTextureCtmCompact_2_, p_getConnectedTextureCtmCompact_3_, p_getConnectedTextureCtmCompact_4_);
            }
            case 21: {
                return getQuadsCompact4(4, 4, 1, 4, p_getConnectedTextureCtmCompact_1_.tileIcons, p_getConnectedTextureCtmCompact_2_, p_getConnectedTextureCtmCompact_3_, p_getConnectedTextureCtmCompact_4_);
            }
            case 22: {
                return getQuadsCompact4(4, 4, 1, 1, p_getConnectedTextureCtmCompact_1_.tileIcons, p_getConnectedTextureCtmCompact_2_, p_getConnectedTextureCtmCompact_3_, p_getConnectedTextureCtmCompact_4_);
            }
            case 23: {
                return getQuadsCompact4(4, 1, 4, 1, p_getConnectedTextureCtmCompact_1_.tileIcons, p_getConnectedTextureCtmCompact_2_, p_getConnectedTextureCtmCompact_3_, p_getConnectedTextureCtmCompact_4_);
            }
            case 24: {
                return getQuadsCompact(2, p_getConnectedTextureCtmCompact_1_.tileIcons, p_getConnectedTextureCtmCompact_3_, p_getConnectedTextureCtmCompact_4_);
            }
            case 25: {
                return getQuadsCompactH(2, 1, p_getConnectedTextureCtmCompact_1_.tileIcons, p_getConnectedTextureCtmCompact_2_, p_getConnectedTextureCtmCompact_3_, p_getConnectedTextureCtmCompact_4_);
            }
            case 26: {
                return getQuadsCompact(1, p_getConnectedTextureCtmCompact_1_.tileIcons, p_getConnectedTextureCtmCompact_3_, p_getConnectedTextureCtmCompact_4_);
            }
            case 27: {
                return getQuadsCompactH(1, 2, p_getConnectedTextureCtmCompact_1_.tileIcons, p_getConnectedTextureCtmCompact_2_, p_getConnectedTextureCtmCompact_3_, p_getConnectedTextureCtmCompact_4_);
            }
            case 28: {
                return getQuadsCompact4(2, 4, 2, 1, p_getConnectedTextureCtmCompact_1_.tileIcons, p_getConnectedTextureCtmCompact_2_, p_getConnectedTextureCtmCompact_3_, p_getConnectedTextureCtmCompact_4_);
            }
            case 29: {
                return getQuadsCompact4(3, 3, 1, 4, p_getConnectedTextureCtmCompact_1_.tileIcons, p_getConnectedTextureCtmCompact_2_, p_getConnectedTextureCtmCompact_3_, p_getConnectedTextureCtmCompact_4_);
            }
            case 30: {
                return getQuadsCompact4(2, 1, 2, 4, p_getConnectedTextureCtmCompact_1_.tileIcons, p_getConnectedTextureCtmCompact_2_, p_getConnectedTextureCtmCompact_3_, p_getConnectedTextureCtmCompact_4_);
            }
            case 31: {
                return getQuadsCompact4(3, 3, 4, 1, p_getConnectedTextureCtmCompact_1_.tileIcons, p_getConnectedTextureCtmCompact_2_, p_getConnectedTextureCtmCompact_3_, p_getConnectedTextureCtmCompact_4_);
            }
            case 32: {
                return getQuadsCompact4(1, 1, 1, 4, p_getConnectedTextureCtmCompact_1_.tileIcons, p_getConnectedTextureCtmCompact_2_, p_getConnectedTextureCtmCompact_3_, p_getConnectedTextureCtmCompact_4_);
            }
            case 33: {
                return getQuadsCompact4(1, 1, 4, 1, p_getConnectedTextureCtmCompact_1_.tileIcons, p_getConnectedTextureCtmCompact_2_, p_getConnectedTextureCtmCompact_3_, p_getConnectedTextureCtmCompact_4_);
            }
            case 34: {
                return getQuadsCompact4(4, 1, 1, 4, p_getConnectedTextureCtmCompact_1_.tileIcons, p_getConnectedTextureCtmCompact_2_, p_getConnectedTextureCtmCompact_3_, p_getConnectedTextureCtmCompact_4_);
            }
            case 35: {
                return getQuadsCompact4(1, 4, 4, 1, p_getConnectedTextureCtmCompact_1_.tileIcons, p_getConnectedTextureCtmCompact_2_, p_getConnectedTextureCtmCompact_3_, p_getConnectedTextureCtmCompact_4_);
            }
            case 36: {
                return getQuadsCompactV(2, 0, p_getConnectedTextureCtmCompact_1_.tileIcons, p_getConnectedTextureCtmCompact_2_, p_getConnectedTextureCtmCompact_3_, p_getConnectedTextureCtmCompact_4_);
            }
            case 37: {
                return getQuadsCompact4(2, 1, 0, 3, p_getConnectedTextureCtmCompact_1_.tileIcons, p_getConnectedTextureCtmCompact_2_, p_getConnectedTextureCtmCompact_3_, p_getConnectedTextureCtmCompact_4_);
            }
            case 38: {
                return getQuadsCompactV(1, 3, p_getConnectedTextureCtmCompact_1_.tileIcons, p_getConnectedTextureCtmCompact_2_, p_getConnectedTextureCtmCompact_3_, p_getConnectedTextureCtmCompact_4_);
            }
            case 39: {
                return getQuadsCompact4(1, 2, 3, 0, p_getConnectedTextureCtmCompact_1_.tileIcons, p_getConnectedTextureCtmCompact_2_, p_getConnectedTextureCtmCompact_3_, p_getConnectedTextureCtmCompact_4_);
            }
            case 40: {
                return getQuadsCompact4(4, 1, 3, 3, p_getConnectedTextureCtmCompact_1_.tileIcons, p_getConnectedTextureCtmCompact_2_, p_getConnectedTextureCtmCompact_3_, p_getConnectedTextureCtmCompact_4_);
            }
            case 41: {
                return getQuadsCompact4(1, 2, 4, 2, p_getConnectedTextureCtmCompact_1_.tileIcons, p_getConnectedTextureCtmCompact_2_, p_getConnectedTextureCtmCompact_3_, p_getConnectedTextureCtmCompact_4_);
            }
            case 42: {
                return getQuadsCompact4(1, 4, 3, 3, p_getConnectedTextureCtmCompact_1_.tileIcons, p_getConnectedTextureCtmCompact_2_, p_getConnectedTextureCtmCompact_3_, p_getConnectedTextureCtmCompact_4_);
            }
            case 43: {
                return getQuadsCompact4(4, 2, 1, 2, p_getConnectedTextureCtmCompact_1_.tileIcons, p_getConnectedTextureCtmCompact_2_, p_getConnectedTextureCtmCompact_3_, p_getConnectedTextureCtmCompact_4_);
            }
            case 44: {
                return getQuadsCompact4(1, 4, 1, 1, p_getConnectedTextureCtmCompact_1_.tileIcons, p_getConnectedTextureCtmCompact_2_, p_getConnectedTextureCtmCompact_3_, p_getConnectedTextureCtmCompact_4_);
            }
            case 45: {
                return getQuadsCompact4(4, 1, 1, 1, p_getConnectedTextureCtmCompact_1_.tileIcons, p_getConnectedTextureCtmCompact_2_, p_getConnectedTextureCtmCompact_3_, p_getConnectedTextureCtmCompact_4_);
            }
            case 46: {
                return getQuadsCompact(4, p_getConnectedTextureCtmCompact_1_.tileIcons, p_getConnectedTextureCtmCompact_3_, p_getConnectedTextureCtmCompact_4_);
            }
            default: {
                return getQuadsCompact(0, p_getConnectedTextureCtmCompact_1_.tileIcons, p_getConnectedTextureCtmCompact_3_, p_getConnectedTextureCtmCompact_4_);
            }
        }
    }
    
    private static BakedQuad[] getQuadsCompactH(final int p_getQuadsCompactH_0_, final int p_getQuadsCompactH_1_, final TextureAtlasSprite[] p_getQuadsCompactH_2_, final int p_getQuadsCompactH_3_, final BakedQuad p_getQuadsCompactH_4_, final RenderEnv p_getQuadsCompactH_5_) {
        return getQuadsCompact(Dir.LEFT, p_getQuadsCompactH_0_, Dir.RIGHT, p_getQuadsCompactH_1_, p_getQuadsCompactH_2_, p_getQuadsCompactH_3_, p_getQuadsCompactH_4_, p_getQuadsCompactH_5_);
    }
    
    private static BakedQuad[] getQuadsCompactV(final int p_getQuadsCompactV_0_, final int p_getQuadsCompactV_1_, final TextureAtlasSprite[] p_getQuadsCompactV_2_, final int p_getQuadsCompactV_3_, final BakedQuad p_getQuadsCompactV_4_, final RenderEnv p_getQuadsCompactV_5_) {
        return getQuadsCompact(Dir.UP, p_getQuadsCompactV_0_, Dir.DOWN, p_getQuadsCompactV_1_, p_getQuadsCompactV_2_, p_getQuadsCompactV_3_, p_getQuadsCompactV_4_, p_getQuadsCompactV_5_);
    }
    
    private static BakedQuad[] getQuadsCompact4(final int p_getQuadsCompact4_0_, final int p_getQuadsCompact4_1_, final int p_getQuadsCompact4_2_, final int p_getQuadsCompact4_3_, final TextureAtlasSprite[] p_getQuadsCompact4_4_, final int p_getQuadsCompact4_5_, final BakedQuad p_getQuadsCompact4_6_, final RenderEnv p_getQuadsCompact4_7_) {
        if (p_getQuadsCompact4_0_ == p_getQuadsCompact4_1_) {
            return (p_getQuadsCompact4_2_ == p_getQuadsCompact4_3_) ? getQuadsCompact(Dir.UP, p_getQuadsCompact4_0_, Dir.DOWN, p_getQuadsCompact4_2_, p_getQuadsCompact4_4_, p_getQuadsCompact4_5_, p_getQuadsCompact4_6_, p_getQuadsCompact4_7_) : getQuadsCompact(Dir.UP, p_getQuadsCompact4_0_, Dir.DOWN_LEFT, p_getQuadsCompact4_2_, Dir.DOWN_RIGHT, p_getQuadsCompact4_3_, p_getQuadsCompact4_4_, p_getQuadsCompact4_5_, p_getQuadsCompact4_6_, p_getQuadsCompact4_7_);
        }
        if (p_getQuadsCompact4_2_ == p_getQuadsCompact4_3_) {
            return getQuadsCompact(Dir.UP_LEFT, p_getQuadsCompact4_0_, Dir.UP_RIGHT, p_getQuadsCompact4_1_, Dir.DOWN, p_getQuadsCompact4_2_, p_getQuadsCompact4_4_, p_getQuadsCompact4_5_, p_getQuadsCompact4_6_, p_getQuadsCompact4_7_);
        }
        if (p_getQuadsCompact4_0_ == p_getQuadsCompact4_2_) {
            return (p_getQuadsCompact4_1_ == p_getQuadsCompact4_3_) ? getQuadsCompact(Dir.LEFT, p_getQuadsCompact4_0_, Dir.RIGHT, p_getQuadsCompact4_1_, p_getQuadsCompact4_4_, p_getQuadsCompact4_5_, p_getQuadsCompact4_6_, p_getQuadsCompact4_7_) : getQuadsCompact(Dir.LEFT, p_getQuadsCompact4_0_, Dir.UP_RIGHT, p_getQuadsCompact4_1_, Dir.DOWN_RIGHT, p_getQuadsCompact4_3_, p_getQuadsCompact4_4_, p_getQuadsCompact4_5_, p_getQuadsCompact4_6_, p_getQuadsCompact4_7_);
        }
        return (p_getQuadsCompact4_1_ == p_getQuadsCompact4_3_) ? getQuadsCompact(Dir.UP_LEFT, p_getQuadsCompact4_0_, Dir.DOWN_LEFT, p_getQuadsCompact4_2_, Dir.RIGHT, p_getQuadsCompact4_1_, p_getQuadsCompact4_4_, p_getQuadsCompact4_5_, p_getQuadsCompact4_6_, p_getQuadsCompact4_7_) : getQuadsCompact(Dir.UP_LEFT, p_getQuadsCompact4_0_, Dir.UP_RIGHT, p_getQuadsCompact4_1_, Dir.DOWN_LEFT, p_getQuadsCompact4_2_, Dir.DOWN_RIGHT, p_getQuadsCompact4_3_, p_getQuadsCompact4_4_, p_getQuadsCompact4_5_, p_getQuadsCompact4_6_, p_getQuadsCompact4_7_);
    }
    
    private static BakedQuad[] getQuadsCompact(final int p_getQuadsCompact_0_, final TextureAtlasSprite[] p_getQuadsCompact_1_, final BakedQuad p_getQuadsCompact_2_, final RenderEnv p_getQuadsCompact_3_) {
        final TextureAtlasSprite textureatlassprite = p_getQuadsCompact_1_[p_getQuadsCompact_0_];
        return ConnectedTextures.getQuads(textureatlassprite, p_getQuadsCompact_2_, p_getQuadsCompact_3_);
    }
    
    private static BakedQuad[] getQuadsCompact(final Dir p_getQuadsCompact_0_, final int p_getQuadsCompact_1_, final Dir p_getQuadsCompact_2_, final int p_getQuadsCompact_3_, final TextureAtlasSprite[] p_getQuadsCompact_4_, final int p_getQuadsCompact_5_, final BakedQuad p_getQuadsCompact_6_, final RenderEnv p_getQuadsCompact_7_) {
        final BakedQuad bakedquad = getQuadCompact(p_getQuadsCompact_4_[p_getQuadsCompact_1_], p_getQuadsCompact_0_, p_getQuadsCompact_5_, p_getQuadsCompact_6_, p_getQuadsCompact_7_);
        final BakedQuad bakedquad2 = getQuadCompact(p_getQuadsCompact_4_[p_getQuadsCompact_3_], p_getQuadsCompact_2_, p_getQuadsCompact_5_, p_getQuadsCompact_6_, p_getQuadsCompact_7_);
        return p_getQuadsCompact_7_.getArrayQuadsCtm(bakedquad, bakedquad2);
    }
    
    private static BakedQuad[] getQuadsCompact(final Dir p_getQuadsCompact_0_, final int p_getQuadsCompact_1_, final Dir p_getQuadsCompact_2_, final int p_getQuadsCompact_3_, final Dir p_getQuadsCompact_4_, final int p_getQuadsCompact_5_, final TextureAtlasSprite[] p_getQuadsCompact_6_, final int p_getQuadsCompact_7_, final BakedQuad p_getQuadsCompact_8_, final RenderEnv p_getQuadsCompact_9_) {
        final BakedQuad bakedquad = getQuadCompact(p_getQuadsCompact_6_[p_getQuadsCompact_1_], p_getQuadsCompact_0_, p_getQuadsCompact_7_, p_getQuadsCompact_8_, p_getQuadsCompact_9_);
        final BakedQuad bakedquad2 = getQuadCompact(p_getQuadsCompact_6_[p_getQuadsCompact_3_], p_getQuadsCompact_2_, p_getQuadsCompact_7_, p_getQuadsCompact_8_, p_getQuadsCompact_9_);
        final BakedQuad bakedquad3 = getQuadCompact(p_getQuadsCompact_6_[p_getQuadsCompact_5_], p_getQuadsCompact_4_, p_getQuadsCompact_7_, p_getQuadsCompact_8_, p_getQuadsCompact_9_);
        return p_getQuadsCompact_9_.getArrayQuadsCtm(bakedquad, bakedquad2, bakedquad3);
    }
    
    private static BakedQuad[] getQuadsCompact(final Dir p_getQuadsCompact_0_, final int p_getQuadsCompact_1_, final Dir p_getQuadsCompact_2_, final int p_getQuadsCompact_3_, final Dir p_getQuadsCompact_4_, final int p_getQuadsCompact_5_, final Dir p_getQuadsCompact_6_, final int p_getQuadsCompact_7_, final TextureAtlasSprite[] p_getQuadsCompact_8_, final int p_getQuadsCompact_9_, final BakedQuad p_getQuadsCompact_10_, final RenderEnv p_getQuadsCompact_11_) {
        final BakedQuad bakedquad = getQuadCompact(p_getQuadsCompact_8_[p_getQuadsCompact_1_], p_getQuadsCompact_0_, p_getQuadsCompact_9_, p_getQuadsCompact_10_, p_getQuadsCompact_11_);
        final BakedQuad bakedquad2 = getQuadCompact(p_getQuadsCompact_8_[p_getQuadsCompact_3_], p_getQuadsCompact_2_, p_getQuadsCompact_9_, p_getQuadsCompact_10_, p_getQuadsCompact_11_);
        final BakedQuad bakedquad3 = getQuadCompact(p_getQuadsCompact_8_[p_getQuadsCompact_5_], p_getQuadsCompact_4_, p_getQuadsCompact_9_, p_getQuadsCompact_10_, p_getQuadsCompact_11_);
        final BakedQuad bakedquad4 = getQuadCompact(p_getQuadsCompact_8_[p_getQuadsCompact_7_], p_getQuadsCompact_6_, p_getQuadsCompact_9_, p_getQuadsCompact_10_, p_getQuadsCompact_11_);
        return p_getQuadsCompact_11_.getArrayQuadsCtm(bakedquad, bakedquad2, bakedquad3, bakedquad4);
    }
    
    private static BakedQuad getQuadCompact(final TextureAtlasSprite p_getQuadCompact_0_, final Dir p_getQuadCompact_1_, final int p_getQuadCompact_2_, final BakedQuad p_getQuadCompact_3_, final RenderEnv p_getQuadCompact_4_) {
        switch (p_getQuadCompact_1_) {
            case UP: {
                return getQuadCompact(p_getQuadCompact_0_, p_getQuadCompact_1_, 0, 0, 16, 8, p_getQuadCompact_2_, p_getQuadCompact_3_, p_getQuadCompact_4_);
            }
            case UP_RIGHT: {
                return getQuadCompact(p_getQuadCompact_0_, p_getQuadCompact_1_, 8, 0, 16, 8, p_getQuadCompact_2_, p_getQuadCompact_3_, p_getQuadCompact_4_);
            }
            case RIGHT: {
                return getQuadCompact(p_getQuadCompact_0_, p_getQuadCompact_1_, 8, 0, 16, 16, p_getQuadCompact_2_, p_getQuadCompact_3_, p_getQuadCompact_4_);
            }
            case DOWN_RIGHT: {
                return getQuadCompact(p_getQuadCompact_0_, p_getQuadCompact_1_, 8, 8, 16, 16, p_getQuadCompact_2_, p_getQuadCompact_3_, p_getQuadCompact_4_);
            }
            case DOWN: {
                return getQuadCompact(p_getQuadCompact_0_, p_getQuadCompact_1_, 0, 8, 16, 16, p_getQuadCompact_2_, p_getQuadCompact_3_, p_getQuadCompact_4_);
            }
            case DOWN_LEFT: {
                return getQuadCompact(p_getQuadCompact_0_, p_getQuadCompact_1_, 0, 8, 8, 16, p_getQuadCompact_2_, p_getQuadCompact_3_, p_getQuadCompact_4_);
            }
            case LEFT: {
                return getQuadCompact(p_getQuadCompact_0_, p_getQuadCompact_1_, 0, 0, 8, 16, p_getQuadCompact_2_, p_getQuadCompact_3_, p_getQuadCompact_4_);
            }
            case UP_LEFT: {
                return getQuadCompact(p_getQuadCompact_0_, p_getQuadCompact_1_, 0, 0, 8, 8, p_getQuadCompact_2_, p_getQuadCompact_3_, p_getQuadCompact_4_);
            }
            default: {
                return p_getQuadCompact_3_;
            }
        }
    }
    
    private static BakedQuad getQuadCompact(final TextureAtlasSprite p_getQuadCompact_0_, final Dir p_getQuadCompact_1_, final int p_getQuadCompact_2_, final int p_getQuadCompact_3_, final int p_getQuadCompact_4_, final int p_getQuadCompact_5_, final int p_getQuadCompact_6_, final BakedQuad p_getQuadCompact_7_, final RenderEnv p_getQuadCompact_8_) {
        final Map[][] amap = ConnectedTextures.getSpriteQuadCompactMaps();
        if (amap == null) {
            return p_getQuadCompact_7_;
        }
        final int i = p_getQuadCompact_0_.getIndexInMap();
        if (i >= 0 && i < amap.length) {
            Map[] amap2 = amap[i];
            if (amap2 == null) {
                amap2 = new Map[Dir.VALUES.length];
                amap[i] = amap2;
            }
            Map<BakedQuad, BakedQuad> map = amap2[p_getQuadCompact_1_.ordinal()];
            if (map == null) {
                map = new IdentityHashMap<BakedQuad, BakedQuad>(1);
                amap2[p_getQuadCompact_1_.ordinal()] = map;
            }
            BakedQuad bakedquad = map.get(p_getQuadCompact_7_);
            if (bakedquad == null) {
                bakedquad = makeSpriteQuadCompact(p_getQuadCompact_7_, p_getQuadCompact_0_, p_getQuadCompact_6_, p_getQuadCompact_2_, p_getQuadCompact_3_, p_getQuadCompact_4_, p_getQuadCompact_5_);
                map.put(p_getQuadCompact_7_, bakedquad);
            }
            return bakedquad;
        }
        return p_getQuadCompact_7_;
    }
    
    private static BakedQuad makeSpriteQuadCompact(final BakedQuad p_makeSpriteQuadCompact_0_, final TextureAtlasSprite p_makeSpriteQuadCompact_1_, final int p_makeSpriteQuadCompact_2_, final int p_makeSpriteQuadCompact_3_, final int p_makeSpriteQuadCompact_4_, final int p_makeSpriteQuadCompact_5_, final int p_makeSpriteQuadCompact_6_) {
        final int[] aint = p_makeSpriteQuadCompact_0_.getVertexData().clone();
        final TextureAtlasSprite textureatlassprite = p_makeSpriteQuadCompact_0_.getSprite();
        for (int i = 0; i < 4; ++i) {
            fixVertexCompact(aint, i, textureatlassprite, p_makeSpriteQuadCompact_1_, p_makeSpriteQuadCompact_2_, p_makeSpriteQuadCompact_3_, p_makeSpriteQuadCompact_4_, p_makeSpriteQuadCompact_5_, p_makeSpriteQuadCompact_6_);
        }
        final BakedQuad bakedquad = new BakedQuad(aint, p_makeSpriteQuadCompact_0_.getTintIndex(), p_makeSpriteQuadCompact_0_.getFace(), p_makeSpriteQuadCompact_1_);
        return bakedquad;
    }
    
    private static void fixVertexCompact(final int[] p_fixVertexCompact_0_, final int p_fixVertexCompact_1_, final TextureAtlasSprite p_fixVertexCompact_2_, final TextureAtlasSprite p_fixVertexCompact_3_, final int p_fixVertexCompact_4_, final int p_fixVertexCompact_5_, final int p_fixVertexCompact_6_, final int p_fixVertexCompact_7_, final int p_fixVertexCompact_8_) {
        final int i = p_fixVertexCompact_0_.length / 4;
        final int j = i * p_fixVertexCompact_1_;
        final float f = Float.intBitsToFloat(p_fixVertexCompact_0_[j + 4]);
        final float f2 = Float.intBitsToFloat(p_fixVertexCompact_0_[j + 4 + 1]);
        double d0 = p_fixVertexCompact_2_.getSpriteU16(f);
        double d2 = p_fixVertexCompact_2_.getSpriteV16(f2);
        float f3 = Float.intBitsToFloat(p_fixVertexCompact_0_[j + 0]);
        float f4 = Float.intBitsToFloat(p_fixVertexCompact_0_[j + 1]);
        float f5 = Float.intBitsToFloat(p_fixVertexCompact_0_[j + 2]);
        float f6 = 0.0f;
        float f7 = 0.0f;
        switch (p_fixVertexCompact_4_) {
            case 0: {
                f6 = f3;
                f7 = 1.0f - f5;
                break;
            }
            case 1: {
                f6 = f3;
                f7 = f5;
                break;
            }
            case 2: {
                f6 = 1.0f - f3;
                f7 = 1.0f - f4;
                break;
            }
            case 3: {
                f6 = f3;
                f7 = 1.0f - f4;
                break;
            }
            case 4: {
                f6 = f5;
                f7 = 1.0f - f4;
                break;
            }
            case 5: {
                f6 = 1.0f - f5;
                f7 = 1.0f - f4;
                break;
            }
            default: {
                return;
            }
        }
        final float f8 = 15.968f;
        final float f9 = 15.968f;
        if (d0 < p_fixVertexCompact_5_) {
            f6 += (float)((p_fixVertexCompact_5_ - d0) / f8);
            d0 = p_fixVertexCompact_5_;
        }
        if (d0 > p_fixVertexCompact_7_) {
            f6 -= (float)((d0 - p_fixVertexCompact_7_) / f8);
            d0 = p_fixVertexCompact_7_;
        }
        if (d2 < p_fixVertexCompact_6_) {
            f7 += (float)((p_fixVertexCompact_6_ - d2) / f9);
            d2 = p_fixVertexCompact_6_;
        }
        if (d2 > p_fixVertexCompact_8_) {
            f7 -= (float)((d2 - p_fixVertexCompact_8_) / f9);
            d2 = p_fixVertexCompact_8_;
        }
        switch (p_fixVertexCompact_4_) {
            case 0: {
                f3 = f6;
                f5 = 1.0f - f7;
                break;
            }
            case 1: {
                f3 = f6;
                f5 = f7;
                break;
            }
            case 2: {
                f3 = 1.0f - f6;
                f4 = 1.0f - f7;
                break;
            }
            case 3: {
                f3 = f6;
                f4 = 1.0f - f7;
                break;
            }
            case 4: {
                f5 = f6;
                f4 = 1.0f - f7;
                break;
            }
            case 5: {
                f5 = 1.0f - f6;
                f4 = 1.0f - f7;
                break;
            }
            default: {
                return;
            }
        }
        p_fixVertexCompact_0_[j + 4] = Float.floatToRawIntBits(p_fixVertexCompact_3_.getInterpolatedU(d0));
        p_fixVertexCompact_0_[j + 4 + 1] = Float.floatToRawIntBits(p_fixVertexCompact_3_.getInterpolatedV(d2));
        p_fixVertexCompact_0_[j + 0] = Float.floatToRawIntBits(f3);
        p_fixVertexCompact_0_[j + 1] = Float.floatToRawIntBits(f4);
        p_fixVertexCompact_0_[j + 2] = Float.floatToRawIntBits(f5);
    }
    
    private enum Dir
    {
        UP("UP", 0), 
        UP_RIGHT("UP_RIGHT", 1), 
        RIGHT("RIGHT", 2), 
        DOWN_RIGHT("DOWN_RIGHT", 3), 
        DOWN("DOWN", 4), 
        DOWN_LEFT("DOWN_LEFT", 5), 
        LEFT("LEFT", 6), 
        UP_LEFT("UP_LEFT", 7);
        
        public static final Dir[] VALUES;
        
        static {
            VALUES = values();
        }
        
        private Dir(final String s, final int n) {
        }
    }
}
