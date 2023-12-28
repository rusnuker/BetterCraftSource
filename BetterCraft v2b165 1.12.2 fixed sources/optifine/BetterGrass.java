// 
// Decompiled by Procyon v0.6.0
// 

package optifine;

import net.minecraft.block.properties.IProperty;
import net.minecraft.init.Blocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockGrassPath;
import net.minecraft.block.BlockMycelium;
import java.util.List;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.IBlockAccess;
import java.io.InputStream;
import java.io.IOException;
import java.util.Properties;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public class BetterGrass
{
    private static boolean betterGrass;
    private static boolean betterGrassPath;
    private static boolean betterMycelium;
    private static boolean betterPodzol;
    private static boolean betterGrassSnow;
    private static boolean betterMyceliumSnow;
    private static boolean betterPodzolSnow;
    private static boolean grassMultilayer;
    private static TextureAtlasSprite spriteGrass;
    private static TextureAtlasSprite spriteGrassSide;
    private static TextureAtlasSprite spriteGrassPath;
    private static TextureAtlasSprite spriteMycelium;
    private static TextureAtlasSprite spritePodzol;
    private static TextureAtlasSprite spriteSnow;
    private static boolean spritesLoaded;
    private static IBakedModel modelCubeGrass;
    private static IBakedModel modelGrassPath;
    private static IBakedModel modelCubeGrassPath;
    private static IBakedModel modelCubeMycelium;
    private static IBakedModel modelCubePodzol;
    private static IBakedModel modelCubeSnow;
    private static boolean modelsLoaded;
    private static final String TEXTURE_GRASS_DEFAULT = "blocks/grass_top";
    private static final String TEXTURE_GRASS_SIDE_DEFAULT = "blocks/grass_side";
    private static final String TEXTURE_GRASS_PATH_DEFAULT = "blocks/grass_path_top";
    private static final String TEXTURE_MYCELIUM_DEFAULT = "blocks/mycelium_top";
    private static final String TEXTURE_PODZOL_DEFAULT = "blocks/dirt_podzol_top";
    private static final String TEXTURE_SNOW_DEFAULT = "blocks/snow";
    
    static {
        BetterGrass.betterGrass = true;
        BetterGrass.betterGrassPath = true;
        BetterGrass.betterMycelium = true;
        BetterGrass.betterPodzol = true;
        BetterGrass.betterGrassSnow = true;
        BetterGrass.betterMyceliumSnow = true;
        BetterGrass.betterPodzolSnow = true;
        BetterGrass.grassMultilayer = false;
        BetterGrass.spriteGrass = null;
        BetterGrass.spriteGrassSide = null;
        BetterGrass.spriteGrassPath = null;
        BetterGrass.spriteMycelium = null;
        BetterGrass.spritePodzol = null;
        BetterGrass.spriteSnow = null;
        BetterGrass.spritesLoaded = false;
        BetterGrass.modelCubeGrass = null;
        BetterGrass.modelGrassPath = null;
        BetterGrass.modelCubeGrassPath = null;
        BetterGrass.modelCubeMycelium = null;
        BetterGrass.modelCubePodzol = null;
        BetterGrass.modelCubeSnow = null;
        BetterGrass.modelsLoaded = false;
    }
    
    public static void updateIcons(final TextureMap p_updateIcons_0_) {
        BetterGrass.spritesLoaded = false;
        BetterGrass.modelsLoaded = false;
        loadProperties(p_updateIcons_0_);
    }
    
    public static void update() {
        if (BetterGrass.spritesLoaded) {
            BetterGrass.modelCubeGrass = BlockModelUtils.makeModelCube(BetterGrass.spriteGrass, 0);
            if (BetterGrass.grassMultilayer) {
                final IBakedModel ibakedmodel = BlockModelUtils.makeModelCube(BetterGrass.spriteGrassSide, -1);
                BetterGrass.modelCubeGrass = BlockModelUtils.joinModelsCube(ibakedmodel, BetterGrass.modelCubeGrass);
            }
            final TextureAtlasSprite textureatlassprite = Config.getTextureMap().registerSprite(new ResourceLocation("blocks/grass_path_side"));
            BetterGrass.modelGrassPath = BlockModelUtils.makeModel("grass_path", textureatlassprite, BetterGrass.spriteGrassPath);
            BetterGrass.modelCubeGrassPath = BlockModelUtils.makeModelCube(BetterGrass.spriteGrassPath, -1);
            BetterGrass.modelCubeMycelium = BlockModelUtils.makeModelCube(BetterGrass.spriteMycelium, -1);
            BetterGrass.modelCubePodzol = BlockModelUtils.makeModelCube(BetterGrass.spritePodzol, 0);
            BetterGrass.modelCubeSnow = BlockModelUtils.makeModelCube(BetterGrass.spriteSnow, -1);
            BetterGrass.modelsLoaded = true;
        }
    }
    
    private static void loadProperties(final TextureMap p_loadProperties_0_) {
        BetterGrass.betterGrass = true;
        BetterGrass.betterGrassPath = true;
        BetterGrass.betterMycelium = true;
        BetterGrass.betterPodzol = true;
        BetterGrass.betterGrassSnow = true;
        BetterGrass.betterMyceliumSnow = true;
        BetterGrass.betterPodzolSnow = true;
        BetterGrass.spriteGrass = p_loadProperties_0_.registerSprite(new ResourceLocation("blocks/grass_top"));
        BetterGrass.spriteGrassSide = p_loadProperties_0_.registerSprite(new ResourceLocation("blocks/grass_side"));
        BetterGrass.spriteGrassPath = p_loadProperties_0_.registerSprite(new ResourceLocation("blocks/grass_path_top"));
        BetterGrass.spriteMycelium = p_loadProperties_0_.registerSprite(new ResourceLocation("blocks/mycelium_top"));
        BetterGrass.spritePodzol = p_loadProperties_0_.registerSprite(new ResourceLocation("blocks/dirt_podzol_top"));
        BetterGrass.spriteSnow = p_loadProperties_0_.registerSprite(new ResourceLocation("blocks/snow"));
        BetterGrass.spritesLoaded = true;
        final String s = "optifine/bettergrass.properties";
        try {
            final ResourceLocation resourcelocation = new ResourceLocation(s);
            if (!Config.hasResource(resourcelocation)) {
                return;
            }
            final InputStream inputstream = Config.getResourceStream(resourcelocation);
            if (inputstream == null) {
                return;
            }
            final boolean flag = Config.isFromDefaultResourcePack(resourcelocation);
            if (flag) {
                Config.dbg("BetterGrass: Parsing default configuration " + s);
            }
            else {
                Config.dbg("BetterGrass: Parsing configuration " + s);
            }
            final Properties properties = new Properties();
            properties.load(inputstream);
            BetterGrass.betterGrass = getBoolean(properties, "grass", true);
            BetterGrass.betterGrassPath = getBoolean(properties, "grass_path", true);
            BetterGrass.betterMycelium = getBoolean(properties, "mycelium", true);
            BetterGrass.betterPodzol = getBoolean(properties, "podzol", true);
            BetterGrass.betterGrassSnow = getBoolean(properties, "grass.snow", true);
            BetterGrass.betterMyceliumSnow = getBoolean(properties, "mycelium.snow", true);
            BetterGrass.betterPodzolSnow = getBoolean(properties, "podzol.snow", true);
            BetterGrass.grassMultilayer = getBoolean(properties, "grass.multilayer", false);
            BetterGrass.spriteGrass = registerSprite(properties, "texture.grass", "blocks/grass_top", p_loadProperties_0_);
            BetterGrass.spriteGrassSide = registerSprite(properties, "texture.grass_side", "blocks/grass_side", p_loadProperties_0_);
            BetterGrass.spriteGrassPath = registerSprite(properties, "texture.grass_path", "blocks/grass_path_top", p_loadProperties_0_);
            BetterGrass.spriteMycelium = registerSprite(properties, "texture.mycelium", "blocks/mycelium_top", p_loadProperties_0_);
            BetterGrass.spritePodzol = registerSprite(properties, "texture.podzol", "blocks/dirt_podzol_top", p_loadProperties_0_);
            BetterGrass.spriteSnow = registerSprite(properties, "texture.snow", "blocks/snow", p_loadProperties_0_);
        }
        catch (final IOException ioexception) {
            Config.warn("Error reading: " + s + ", " + ioexception.getClass().getName() + ": " + ioexception.getMessage());
        }
    }
    
    private static TextureAtlasSprite registerSprite(final Properties p_registerSprite_0_, final String p_registerSprite_1_, final String p_registerSprite_2_, final TextureMap p_registerSprite_3_) {
        String s = p_registerSprite_0_.getProperty(p_registerSprite_1_);
        if (s == null) {
            s = p_registerSprite_2_;
        }
        final ResourceLocation resourcelocation = new ResourceLocation("textures/" + s + ".png");
        if (!Config.hasResource(resourcelocation)) {
            Config.warn("BetterGrass texture not found: " + resourcelocation);
            s = p_registerSprite_2_;
        }
        final ResourceLocation resourcelocation2 = new ResourceLocation(s);
        final TextureAtlasSprite textureatlassprite = p_registerSprite_3_.registerSprite(resourcelocation2);
        return textureatlassprite;
    }
    
    public static List getFaceQuads(final IBlockAccess p_getFaceQuads_0_, final IBlockState p_getFaceQuads_1_, final BlockPos p_getFaceQuads_2_, final EnumFacing p_getFaceQuads_3_, final List p_getFaceQuads_4_) {
        if (p_getFaceQuads_3_ == EnumFacing.UP || p_getFaceQuads_3_ == EnumFacing.DOWN) {
            return p_getFaceQuads_4_;
        }
        if (!BetterGrass.modelsLoaded) {
            return p_getFaceQuads_4_;
        }
        final Block block = p_getFaceQuads_1_.getBlock();
        if (block instanceof BlockMycelium) {
            return getFaceQuadsMycelium(p_getFaceQuads_0_, p_getFaceQuads_1_, p_getFaceQuads_2_, p_getFaceQuads_3_, p_getFaceQuads_4_);
        }
        if (block instanceof BlockGrassPath) {
            return getFaceQuadsGrassPath(p_getFaceQuads_0_, p_getFaceQuads_1_, p_getFaceQuads_2_, p_getFaceQuads_3_, p_getFaceQuads_4_);
        }
        if (block instanceof BlockDirt) {
            return getFaceQuadsDirt(p_getFaceQuads_0_, p_getFaceQuads_1_, p_getFaceQuads_2_, p_getFaceQuads_3_, p_getFaceQuads_4_);
        }
        return (block instanceof BlockGrass) ? getFaceQuadsGrass(p_getFaceQuads_0_, p_getFaceQuads_1_, p_getFaceQuads_2_, p_getFaceQuads_3_, p_getFaceQuads_4_) : p_getFaceQuads_4_;
    }
    
    private static List getFaceQuadsMycelium(final IBlockAccess p_getFaceQuadsMycelium_0_, final IBlockState p_getFaceQuadsMycelium_1_, final BlockPos p_getFaceQuadsMycelium_2_, final EnumFacing p_getFaceQuadsMycelium_3_, final List p_getFaceQuadsMycelium_4_) {
        final Block block = p_getFaceQuadsMycelium_0_.getBlockState(p_getFaceQuadsMycelium_2_.up()).getBlock();
        final boolean flag = block == Blocks.SNOW || block == Blocks.SNOW_LAYER;
        if (Config.isBetterGrassFancy()) {
            if (flag) {
                if (BetterGrass.betterMyceliumSnow && getBlockAt(p_getFaceQuadsMycelium_2_, p_getFaceQuadsMycelium_3_, p_getFaceQuadsMycelium_0_) == Blocks.SNOW_LAYER) {
                    return BetterGrass.modelCubeSnow.getQuads(p_getFaceQuadsMycelium_1_, p_getFaceQuadsMycelium_3_, 0L);
                }
            }
            else if (BetterGrass.betterMycelium && getBlockAt(p_getFaceQuadsMycelium_2_.down(), p_getFaceQuadsMycelium_3_, p_getFaceQuadsMycelium_0_) == Blocks.MYCELIUM) {
                return BetterGrass.modelCubeMycelium.getQuads(p_getFaceQuadsMycelium_1_, p_getFaceQuadsMycelium_3_, 0L);
            }
        }
        else if (flag) {
            if (BetterGrass.betterMyceliumSnow) {
                return BetterGrass.modelCubeSnow.getQuads(p_getFaceQuadsMycelium_1_, p_getFaceQuadsMycelium_3_, 0L);
            }
        }
        else if (BetterGrass.betterMycelium) {
            return BetterGrass.modelCubeMycelium.getQuads(p_getFaceQuadsMycelium_1_, p_getFaceQuadsMycelium_3_, 0L);
        }
        return p_getFaceQuadsMycelium_4_;
    }
    
    private static List getFaceQuadsGrassPath(final IBlockAccess p_getFaceQuadsGrassPath_0_, final IBlockState p_getFaceQuadsGrassPath_1_, final BlockPos p_getFaceQuadsGrassPath_2_, final EnumFacing p_getFaceQuadsGrassPath_3_, final List p_getFaceQuadsGrassPath_4_) {
        if (!BetterGrass.betterGrassPath) {
            return p_getFaceQuadsGrassPath_4_;
        }
        if (Config.isBetterGrassFancy()) {
            return (getBlockAt(p_getFaceQuadsGrassPath_2_.down(), p_getFaceQuadsGrassPath_3_, p_getFaceQuadsGrassPath_0_) == Blocks.GRASS_PATH) ? BetterGrass.modelGrassPath.getQuads(p_getFaceQuadsGrassPath_1_, p_getFaceQuadsGrassPath_3_, 0L) : p_getFaceQuadsGrassPath_4_;
        }
        return BetterGrass.modelGrassPath.getQuads(p_getFaceQuadsGrassPath_1_, p_getFaceQuadsGrassPath_3_, 0L);
    }
    
    private static List getFaceQuadsDirt(final IBlockAccess p_getFaceQuadsDirt_0_, final IBlockState p_getFaceQuadsDirt_1_, final BlockPos p_getFaceQuadsDirt_2_, final EnumFacing p_getFaceQuadsDirt_3_, final List p_getFaceQuadsDirt_4_) {
        final Block block = getBlockAt(p_getFaceQuadsDirt_2_, EnumFacing.UP, p_getFaceQuadsDirt_0_);
        if (p_getFaceQuadsDirt_1_.getValue(BlockDirt.VARIANT) == BlockDirt.DirtType.PODZOL) {
            final boolean flag = block == Blocks.SNOW || block == Blocks.SNOW_LAYER;
            if (Config.isBetterGrassFancy()) {
                if (flag) {
                    if (BetterGrass.betterPodzolSnow && getBlockAt(p_getFaceQuadsDirt_2_, p_getFaceQuadsDirt_3_, p_getFaceQuadsDirt_0_) == Blocks.SNOW_LAYER) {
                        return BetterGrass.modelCubeSnow.getQuads(p_getFaceQuadsDirt_1_, p_getFaceQuadsDirt_3_, 0L);
                    }
                }
                else if (BetterGrass.betterPodzol) {
                    final BlockPos blockpos = p_getFaceQuadsDirt_2_.down().offset(p_getFaceQuadsDirt_3_);
                    final IBlockState iblockstate = p_getFaceQuadsDirt_0_.getBlockState(blockpos);
                    if (iblockstate.getBlock() == Blocks.DIRT && iblockstate.getValue(BlockDirt.VARIANT) == BlockDirt.DirtType.PODZOL) {
                        return BetterGrass.modelCubePodzol.getQuads(p_getFaceQuadsDirt_1_, p_getFaceQuadsDirt_3_, 0L);
                    }
                }
            }
            else if (flag) {
                if (BetterGrass.betterPodzolSnow) {
                    return BetterGrass.modelCubeSnow.getQuads(p_getFaceQuadsDirt_1_, p_getFaceQuadsDirt_3_, 0L);
                }
            }
            else if (BetterGrass.betterPodzol) {
                return BetterGrass.modelCubePodzol.getQuads(p_getFaceQuadsDirt_1_, p_getFaceQuadsDirt_3_, 0L);
            }
            return p_getFaceQuadsDirt_4_;
        }
        if (block == Blocks.GRASS_PATH) {
            return (BetterGrass.betterGrassPath && getBlockAt(p_getFaceQuadsDirt_2_, p_getFaceQuadsDirt_3_, p_getFaceQuadsDirt_0_) == Blocks.GRASS_PATH) ? BetterGrass.modelCubeGrassPath.getQuads(p_getFaceQuadsDirt_1_, p_getFaceQuadsDirt_3_, 0L) : p_getFaceQuadsDirt_4_;
        }
        return p_getFaceQuadsDirt_4_;
    }
    
    private static List getFaceQuadsGrass(final IBlockAccess p_getFaceQuadsGrass_0_, final IBlockState p_getFaceQuadsGrass_1_, final BlockPos p_getFaceQuadsGrass_2_, final EnumFacing p_getFaceQuadsGrass_3_, final List p_getFaceQuadsGrass_4_) {
        final Block block = p_getFaceQuadsGrass_0_.getBlockState(p_getFaceQuadsGrass_2_.up()).getBlock();
        final boolean flag = block == Blocks.SNOW || block == Blocks.SNOW_LAYER;
        if (Config.isBetterGrassFancy()) {
            if (flag) {
                if (BetterGrass.betterGrassSnow && getBlockAt(p_getFaceQuadsGrass_2_, p_getFaceQuadsGrass_3_, p_getFaceQuadsGrass_0_) == Blocks.SNOW_LAYER) {
                    return BetterGrass.modelCubeSnow.getQuads(p_getFaceQuadsGrass_1_, p_getFaceQuadsGrass_3_, 0L);
                }
            }
            else if (BetterGrass.betterGrass && getBlockAt(p_getFaceQuadsGrass_2_.down(), p_getFaceQuadsGrass_3_, p_getFaceQuadsGrass_0_) == Blocks.GRASS) {
                return BetterGrass.modelCubeGrass.getQuads(p_getFaceQuadsGrass_1_, p_getFaceQuadsGrass_3_, 0L);
            }
        }
        else if (flag) {
            if (BetterGrass.betterGrassSnow) {
                return BetterGrass.modelCubeSnow.getQuads(p_getFaceQuadsGrass_1_, p_getFaceQuadsGrass_3_, 0L);
            }
        }
        else if (BetterGrass.betterGrass) {
            return BetterGrass.modelCubeGrass.getQuads(p_getFaceQuadsGrass_1_, p_getFaceQuadsGrass_3_, 0L);
        }
        return p_getFaceQuadsGrass_4_;
    }
    
    private static Block getBlockAt(final BlockPos p_getBlockAt_0_, final EnumFacing p_getBlockAt_1_, final IBlockAccess p_getBlockAt_2_) {
        final BlockPos blockpos = p_getBlockAt_0_.offset(p_getBlockAt_1_);
        final Block block = p_getBlockAt_2_.getBlockState(blockpos).getBlock();
        return block;
    }
    
    private static boolean getBoolean(final Properties p_getBoolean_0_, final String p_getBoolean_1_, final boolean p_getBoolean_2_) {
        final String s = p_getBoolean_0_.getProperty(p_getBoolean_1_);
        return (s == null) ? p_getBoolean_2_ : Boolean.parseBoolean(s);
    }
}
