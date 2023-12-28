// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.renderer.texture;

import net.minecraft.client.renderer.GlStateManager;
import java.awt.image.BufferedImage;
import java.util.Set;
import java.awt.Dimension;
import java.io.InputStream;
import java.util.TreeSet;
import java.util.HashMap;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.client.resources.IResource;
import java.util.Iterator;
import net.minecraft.util.ReportedException;
import net.minecraft.crash.CrashReport;
import net.minecraft.client.renderer.StitcherException;
import net.minecraft.util.math.MathHelper;
import java.io.Closeable;
import org.apache.commons.io.IOUtils;
import optifine.ReflectorForge;
import optifine.SpriteDependencies;
import java.util.Collection;
import java.util.ArrayList;
import optifine.TextureUtils;
import optifine.BetterGrass;
import optifine.CustomItems;
import optifine.ConnectedTextures;
import optifine.Config;
import optifine.Reflector;
import java.io.IOException;
import shadersmod.client.ShadersTex;
import net.minecraft.client.resources.IResourceManager;
import com.google.common.collect.Maps;
import com.google.common.collect.Lists;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import java.util.Map;
import java.util.List;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Logger;

public class TextureMap extends AbstractTexture implements ITickableTextureObject
{
    private static final Logger LOGGER;
    public static final ResourceLocation LOCATION_MISSING_TEXTURE;
    public static final ResourceLocation LOCATION_BLOCKS_TEXTURE;
    private final List<TextureAtlasSprite> listAnimatedSprites;
    private final Map<String, TextureAtlasSprite> mapRegisteredSprites;
    private final Map<String, TextureAtlasSprite> mapUploadedSprites;
    private final String basePath;
    private final ITextureMapPopulator iconCreator;
    private int mipmapLevels;
    private final TextureAtlasSprite missingImage;
    private TextureAtlasSprite[] iconGrid;
    private int iconGridSize;
    private int iconGridCountX;
    private int iconGridCountY;
    private double iconGridSizeU;
    private double iconGridSizeV;
    private int counterIndexInMap;
    public int atlasWidth;
    public int atlasHeight;
    
    static {
        LOGGER = LogManager.getLogger();
        LOCATION_MISSING_TEXTURE = new ResourceLocation("missingno");
        LOCATION_BLOCKS_TEXTURE = new ResourceLocation("textures/atlas/blocks.png");
    }
    
    public TextureMap(final String basePathIn) {
        this(basePathIn, null);
    }
    
    public TextureMap(final String p_i3_1_, final boolean p_i3_2_) {
        this(p_i3_1_, null, p_i3_2_);
    }
    
    public TextureMap(final String basePathIn, @Nullable final ITextureMapPopulator iconCreatorIn) {
        this(basePathIn, iconCreatorIn, false);
    }
    
    public TextureMap(final String p_i4_1_, final ITextureMapPopulator p_i4_2_, final boolean p_i4_3_) {
        this.iconGrid = null;
        this.iconGridSize = -1;
        this.iconGridCountX = -1;
        this.iconGridCountY = -1;
        this.iconGridSizeU = -1.0;
        this.iconGridSizeV = -1.0;
        this.counterIndexInMap = 0;
        this.atlasWidth = 0;
        this.atlasHeight = 0;
        this.listAnimatedSprites = (List<TextureAtlasSprite>)Lists.newArrayList();
        this.mapRegisteredSprites = (Map<String, TextureAtlasSprite>)Maps.newHashMap();
        this.mapUploadedSprites = (Map<String, TextureAtlasSprite>)Maps.newHashMap();
        this.missingImage = new TextureAtlasSprite("missingno");
        this.basePath = p_i4_1_;
        this.iconCreator = p_i4_2_;
    }
    
    private void initMissingImage() {
        final int i = this.getMinSpriteSize();
        final int[] aint = this.getMissingImageData(i);
        this.missingImage.setIconWidth(i);
        this.missingImage.setIconHeight(i);
        final int[][] aint2 = new int[this.mipmapLevels + 1][];
        aint2[0] = aint;
        this.missingImage.setFramesTextureData(Lists.newArrayList(new int[][][] { aint2 }));
        this.missingImage.setIndexInMap(this.counterIndexInMap++);
    }
    
    @Override
    public void loadTexture(final IResourceManager resourceManager) throws IOException {
        ShadersTex.resManager = resourceManager;
        if (this.iconCreator != null) {
            this.loadSprites(resourceManager, this.iconCreator);
        }
    }
    
    public void loadSprites(final IResourceManager resourceManager, final ITextureMapPopulator iconCreatorIn) {
        this.mapRegisteredSprites.clear();
        this.counterIndexInMap = 0;
        Reflector.callVoid(Reflector.ForgeHooksClient_onTextureStitchedPre, this);
        iconCreatorIn.registerSprites(this);
        if (this.mipmapLevels >= 4) {
            this.mipmapLevels = this.detectMaxMipmapLevel(this.mapRegisteredSprites, resourceManager);
            Config.log("Mipmap levels: " + this.mipmapLevels);
        }
        this.initMissingImage();
        this.deleteGlTexture();
        this.loadTextureAtlas(resourceManager);
    }
    
    public void loadTextureAtlas(final IResourceManager resourceManager) {
        ShadersTex.resManager = resourceManager;
        Config.dbg("Multitexture: " + Config.isMultiTexture());
        if (Config.isMultiTexture()) {
            for (final TextureAtlasSprite textureatlassprite : this.mapUploadedSprites.values()) {
                textureatlassprite.deleteSpriteTexture();
            }
        }
        ConnectedTextures.updateIcons(this);
        CustomItems.updateIcons(this);
        BetterGrass.updateIcons(this);
        final int k1 = TextureUtils.getGLMaximumTextureSize();
        final Stitcher stitcher = new Stitcher(k1, k1, 0, this.mipmapLevels);
        this.mapUploadedSprites.clear();
        this.listAnimatedSprites.clear();
        int i = Integer.MAX_VALUE;
        final int j = this.getMinSpriteSize();
        this.iconGridSize = j;
        int l = 1 << this.mipmapLevels;
        final List<TextureAtlasSprite> list = new ArrayList<TextureAtlasSprite>(this.mapRegisteredSprites.values());
        for (int m = 0; m < list.size(); ++m) {
            final TextureAtlasSprite textureatlassprite2 = SpriteDependencies.resolveDependencies(list, m, this);
            final ResourceLocation resourcelocation = this.getResourceLocation(textureatlassprite2);
            IResource iresource = null;
            if (textureatlassprite2.getIndexInMap() < 0) {
                textureatlassprite2.setIndexInMap(this.counterIndexInMap++);
            }
            if (textureatlassprite2.hasCustomLoader(resourceManager, resourcelocation)) {
                if (textureatlassprite2.load(resourceManager, resourcelocation, p_lambda$loadTextureAtlas$0_1_ -> this.mapRegisteredSprites.get(p_lambda$loadTextureAtlas$0_1_.toString()))) {
                    Config.dbg("Custom loader (skipped): " + textureatlassprite2);
                    continue;
                }
                Config.dbg("Custom loader: " + textureatlassprite2);
            }
            else {
                try {
                    final PngSizeInfo pngsizeinfo = PngSizeInfo.makeFromResource(resourceManager.getResource(resourcelocation));
                    if (Config.isShaders()) {
                        iresource = ShadersTex.loadResource(resourceManager, resourcelocation);
                    }
                    else {
                        iresource = resourceManager.getResource(resourcelocation);
                    }
                    final boolean flag = iresource.getMetadata("animation") != null;
                    textureatlassprite2.loadSprite(pngsizeinfo, flag);
                }
                catch (final RuntimeException runtimeexception) {
                    TextureMap.LOGGER.error("Unable to parse metadata from {}", resourcelocation, runtimeexception);
                    ReflectorForge.FMLClientHandler_trackBrokenTexture(resourcelocation, runtimeexception.getMessage());
                }
                catch (final IOException ioexception) {
                    TextureMap.LOGGER.error("Using missing texture, unable to load " + resourcelocation + ", " + ioexception.getClass().getName());
                    ReflectorForge.FMLClientHandler_trackMissingTexture(resourcelocation);
                }
                finally {
                    IOUtils.closeQuietly(iresource);
                }
                IOUtils.closeQuietly(iresource);
            }
            final int k2 = textureatlassprite2.getIconWidth();
            final int i2 = textureatlassprite2.getIconHeight();
            if (k2 >= 1 && i2 >= 1) {
                if (k2 < j || this.mipmapLevels > 0) {
                    final int i3 = (this.mipmapLevels > 0) ? TextureUtils.scaleToPowerOfTwo(k2, j) : TextureUtils.scaleMinTo(k2, j);
                    if (i3 != k2) {
                        if (!TextureUtils.isPowerOfTwo(k2)) {
                            Config.log("Scaled non power of 2: " + textureatlassprite2.getIconName() + ", " + k2 + " -> " + i3);
                        }
                        else {
                            Config.log("Scaled too small texture: " + textureatlassprite2.getIconName() + ", " + k2 + " -> " + i3);
                        }
                        final int j2 = i2 * i3 / k2;
                        textureatlassprite2.setIconWidth(i3);
                        textureatlassprite2.setIconHeight(j2);
                    }
                }
                i = Math.min(i, Math.min(textureatlassprite2.getIconWidth(), textureatlassprite2.getIconHeight()));
                final int j3 = Math.min(Integer.lowestOneBit(textureatlassprite2.getIconWidth()), Integer.lowestOneBit(textureatlassprite2.getIconHeight()));
                if (j3 < l) {
                    TextureMap.LOGGER.warn("Texture {} with size {}x{} limits mip level from {} to {}", resourcelocation, textureatlassprite2.getIconWidth(), textureatlassprite2.getIconHeight(), MathHelper.log2(l), MathHelper.log2(j3));
                    l = j3;
                }
                if (this.generateMipmaps(resourceManager, textureatlassprite2)) {
                    stitcher.addSprite(textureatlassprite2);
                }
            }
            else {
                Config.warn("Invalid sprite size: " + textureatlassprite2);
            }
        }
        final int l2 = Math.min(i, l);
        int i4 = MathHelper.log2(l2);
        if (i4 < 0) {
            i4 = 0;
        }
        if (i4 < this.mipmapLevels) {
            TextureMap.LOGGER.warn("{}: dropping miplevel from {} to {}, because of minimum power of two: {}", this.basePath, this.mipmapLevels, i4, l2);
            this.mipmapLevels = i4;
        }
        this.missingImage.generateMipmaps(this.mipmapLevels);
        stitcher.addSprite(this.missingImage);
        try {
            stitcher.doStitch();
        }
        catch (final StitcherException stitcherexception) {
            throw stitcherexception;
        }
        TextureMap.LOGGER.info("Created: {}x{} {}-atlas", (Object)stitcher.getCurrentWidth(), stitcher.getCurrentHeight(), this.basePath);
        if (Config.isShaders()) {
            ShadersTex.allocateTextureMap(this.getGlTextureId(), this.mipmapLevels, stitcher.getCurrentWidth(), stitcher.getCurrentHeight(), stitcher, this);
        }
        else {
            TextureUtil.allocateTextureImpl(this.getGlTextureId(), this.mipmapLevels, stitcher.getCurrentWidth(), stitcher.getCurrentHeight());
        }
        final Map<String, TextureAtlasSprite> map = (Map<String, TextureAtlasSprite>)Maps.newHashMap((Map<?, ?>)this.mapRegisteredSprites);
        for (final TextureAtlasSprite textureatlassprite3 : stitcher.getStichSlots()) {
            if (Config.isShaders()) {
                ShadersTex.setIconName(ShadersTex.setSprite(textureatlassprite3).getIconName());
            }
            final String s = textureatlassprite3.getIconName();
            map.remove(s);
            this.mapUploadedSprites.put(s, textureatlassprite3);
            try {
                if (Config.isShaders()) {
                    ShadersTex.uploadTexSubForLoadAtlas(textureatlassprite3.getFrameTextureData(0), textureatlassprite3.getIconWidth(), textureatlassprite3.getIconHeight(), textureatlassprite3.getOriginX(), textureatlassprite3.getOriginY(), false, false);
                }
                else {
                    TextureUtil.uploadTextureMipmap(textureatlassprite3.getFrameTextureData(0), textureatlassprite3.getIconWidth(), textureatlassprite3.getIconHeight(), textureatlassprite3.getOriginX(), textureatlassprite3.getOriginY(), false, false);
                }
            }
            catch (final Throwable throwable) {
                final CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Stitching texture atlas");
                final CrashReportCategory crashreportcategory = crashreport.makeCategory("Texture being stitched together");
                crashreportcategory.addCrashSection("Atlas path", this.basePath);
                crashreportcategory.addCrashSection("Sprite", textureatlassprite3);
                throw new ReportedException(crashreport);
            }
            if (textureatlassprite3.hasAnimationMetadata()) {
                this.listAnimatedSprites.add(textureatlassprite3);
            }
        }
        for (final TextureAtlasSprite textureatlassprite4 : map.values()) {
            textureatlassprite4.copyFrom(this.missingImage);
        }
        if (Config.isMultiTexture()) {
            final int j4 = stitcher.getCurrentWidth();
            final int l3 = stitcher.getCurrentHeight();
            for (final TextureAtlasSprite textureatlassprite5 : stitcher.getStichSlots()) {
                textureatlassprite5.sheetWidth = j4;
                textureatlassprite5.sheetHeight = l3;
                textureatlassprite5.mipmapLevels = this.mipmapLevels;
                final TextureAtlasSprite textureatlassprite6 = textureatlassprite5.spriteSingle;
                if (textureatlassprite6 != null) {
                    if (textureatlassprite6.getIconWidth() <= 0) {
                        textureatlassprite6.setIconWidth(textureatlassprite5.getIconWidth());
                        textureatlassprite6.setIconHeight(textureatlassprite5.getIconHeight());
                        textureatlassprite6.initSprite(textureatlassprite5.getIconWidth(), textureatlassprite5.getIconHeight(), 0, 0, false);
                        textureatlassprite6.clearFramesTextureData();
                        final List<int[][]> list2 = textureatlassprite5.getFramesTextureData();
                        textureatlassprite6.setFramesTextureData(list2);
                        textureatlassprite6.setAnimationMetadata(textureatlassprite5.getAnimationMetadata());
                    }
                    textureatlassprite6.sheetWidth = j4;
                    textureatlassprite6.sheetHeight = l3;
                    textureatlassprite6.mipmapLevels = this.mipmapLevels;
                    textureatlassprite5.bindSpriteTexture();
                    final boolean flag2 = false;
                    final boolean flag3 = true;
                    try {
                        TextureUtil.uploadTextureMipmap(textureatlassprite6.getFrameTextureData(0), textureatlassprite6.getIconWidth(), textureatlassprite6.getIconHeight(), textureatlassprite6.getOriginX(), textureatlassprite6.getOriginY(), flag2, flag3);
                    }
                    catch (final Exception exception) {
                        Config.dbg("Error uploading sprite single: " + textureatlassprite6 + ", parent: " + textureatlassprite5);
                        exception.printStackTrace();
                    }
                }
            }
            Config.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        }
        Reflector.callVoid(Reflector.ForgeHooksClient_onTextureStitchedPost, this);
        this.updateIconGrid(stitcher.getCurrentWidth(), stitcher.getCurrentHeight());
        if (Config.equals(System.getProperty("saveTextureMap"), "true")) {
            Config.dbg("Exporting texture map: " + this.basePath);
            TextureUtils.saveGlTexture("debug/" + this.basePath.replaceAll("/", "_"), this.getGlTextureId(), this.mipmapLevels, stitcher.getCurrentWidth(), stitcher.getCurrentHeight());
        }
    }
    
    public boolean generateMipmaps(final IResourceManager resourceManager, final TextureAtlasSprite texture) {
        final ResourceLocation resourcelocation1 = this.getResourceLocation(texture);
        IResource iresource1 = null;
        if (!texture.hasCustomLoader(resourceManager, resourcelocation1)) {
            boolean flag4 = false;
            try {
                iresource1 = resourceManager.getResource(resourcelocation1);
                texture.loadSpriteFrames(iresource1, this.mipmapLevels + 1);
            }
            catch (final RuntimeException runtimeexception1) {
                TextureMap.LOGGER.error("Unable to parse metadata from {}", resourcelocation1, runtimeexception1);
                flag4 = false;
            }
            catch (final IOException ioexception1) {
                TextureMap.LOGGER.error("Using missing texture, unable to load {}", resourcelocation1, ioexception1);
                final boolean crashreportcategory;
                flag4 = (crashreportcategory = false);
                return crashreportcategory;
            }
            finally {
                IOUtils.closeQuietly(iresource1);
            }
            return flag4;
        }
        TextureUtils.generateCustomMipmaps(texture, this.mipmapLevels);
        try {
            texture.generateMipmaps(this.mipmapLevels);
            return true;
        }
        catch (final Throwable throwable1) {
            final CrashReport crashreport1 = CrashReport.makeCrashReport(throwable1, "Applying mipmap");
            final CrashReportCategory crashreportcategory2 = crashreport1.makeCategory("Sprite being mipmapped");
            crashreportcategory2.setDetail("Sprite name", new ICrashReportDetail<String>() {
                @Override
                public String call() throws Exception {
                    return texture.getIconName();
                }
            });
            crashreportcategory2.setDetail("Sprite size", new ICrashReportDetail<String>() {
                @Override
                public String call() throws Exception {
                    return String.valueOf(texture.getIconWidth()) + " x " + texture.getIconHeight();
                }
            });
            crashreportcategory2.setDetail("Sprite frames", new ICrashReportDetail<String>() {
                @Override
                public String call() throws Exception {
                    return String.valueOf(texture.getFrameCount()) + " frames";
                }
            });
            crashreportcategory2.addCrashSection("Mipmap levels", this.mipmapLevels);
            throw new ReportedException(crashreport1);
        }
    }
    
    public ResourceLocation getResourceLocation(final TextureAtlasSprite p_184396_1_) {
        final ResourceLocation resourcelocation1 = new ResourceLocation(p_184396_1_.getIconName());
        return this.completeResourceLocation(resourcelocation1);
    }
    
    public ResourceLocation completeResourceLocation(final ResourceLocation p_completeResourceLocation_1_) {
        return this.isAbsoluteLocation(p_completeResourceLocation_1_) ? new ResourceLocation(p_completeResourceLocation_1_.getResourceDomain(), String.valueOf(p_completeResourceLocation_1_.getResourcePath()) + ".png") : new ResourceLocation(p_completeResourceLocation_1_.getResourceDomain(), String.format("%s/%s%s", this.basePath, p_completeResourceLocation_1_.getResourcePath(), ".png"));
    }
    
    public TextureAtlasSprite getAtlasSprite(final String iconName) {
        TextureAtlasSprite textureatlassprite6 = this.mapUploadedSprites.get(iconName);
        if (textureatlassprite6 == null) {
            textureatlassprite6 = this.missingImage;
        }
        return textureatlassprite6;
    }
    
    public void updateAnimations() {
        if (Config.isShaders()) {
            ShadersTex.updatingTex = this.getMultiTexID();
        }
        boolean flag3 = false;
        boolean flag4 = false;
        TextureUtil.bindTexture(this.getGlTextureId());
        for (final TextureAtlasSprite textureatlassprite6 : this.listAnimatedSprites) {
            if (this.isTerrainAnimationActive(textureatlassprite6)) {
                textureatlassprite6.updateAnimation();
                if (textureatlassprite6.spriteNormal != null) {
                    flag3 = true;
                }
                if (textureatlassprite6.spriteSpecular == null) {
                    continue;
                }
                flag4 = true;
            }
        }
        if (Config.isMultiTexture()) {
            for (final TextureAtlasSprite textureatlassprite7 : this.listAnimatedSprites) {
                if (this.isTerrainAnimationActive(textureatlassprite7)) {
                    final TextureAtlasSprite textureatlassprite8 = textureatlassprite7.spriteSingle;
                    if (textureatlassprite8 == null) {
                        continue;
                    }
                    if (textureatlassprite7 == TextureUtils.iconClock || textureatlassprite7 == TextureUtils.iconCompass) {
                        textureatlassprite8.frameCounter = textureatlassprite7.frameCounter;
                    }
                    textureatlassprite7.bindSpriteTexture();
                    textureatlassprite8.updateAnimation();
                }
            }
            TextureUtil.bindTexture(this.getGlTextureId());
        }
        if (Config.isShaders()) {
            if (flag3) {
                TextureUtil.bindTexture(this.getMultiTexID().norm);
                for (final TextureAtlasSprite textureatlassprite9 : this.listAnimatedSprites) {
                    if (textureatlassprite9.spriteNormal != null && this.isTerrainAnimationActive(textureatlassprite9)) {
                        if (textureatlassprite9 == TextureUtils.iconClock || textureatlassprite9 == TextureUtils.iconCompass) {
                            textureatlassprite9.spriteNormal.frameCounter = textureatlassprite9.frameCounter;
                        }
                        textureatlassprite9.spriteNormal.updateAnimation();
                    }
                }
            }
            if (flag4) {
                TextureUtil.bindTexture(this.getMultiTexID().spec);
                for (final TextureAtlasSprite textureatlassprite10 : this.listAnimatedSprites) {
                    if (textureatlassprite10.spriteSpecular != null && this.isTerrainAnimationActive(textureatlassprite10)) {
                        if (textureatlassprite10 == TextureUtils.iconClock || textureatlassprite10 == TextureUtils.iconCompass) {
                            textureatlassprite10.spriteNormal.frameCounter = textureatlassprite10.frameCounter;
                        }
                        textureatlassprite10.spriteSpecular.updateAnimation();
                    }
                }
            }
            if (flag3 || flag4) {
                TextureUtil.bindTexture(this.getGlTextureId());
            }
        }
        if (Config.isShaders()) {
            ShadersTex.updatingTex = null;
        }
    }
    
    public TextureAtlasSprite registerSprite(final ResourceLocation location) {
        if (location == null) {
            throw new IllegalArgumentException("Location cannot be null!");
        }
        TextureAtlasSprite textureatlassprite6 = this.mapRegisteredSprites.get(location.toString());
        if (textureatlassprite6 == null) {
            textureatlassprite6 = TextureAtlasSprite.makeAtlasSprite(location);
            this.mapRegisteredSprites.put(location.toString(), textureatlassprite6);
            if (textureatlassprite6.getIndexInMap() < 0) {
                textureatlassprite6.setIndexInMap(this.counterIndexInMap++);
            }
        }
        return textureatlassprite6;
    }
    
    @Override
    public void tick() {
        this.updateAnimations();
    }
    
    public void setMipmapLevels(final int mipmapLevelsIn) {
        this.mipmapLevels = mipmapLevelsIn;
    }
    
    public TextureAtlasSprite getMissingSprite() {
        return this.missingImage;
    }
    
    @Nullable
    public TextureAtlasSprite getTextureExtry(final String p_getTextureExtry_1_) {
        return this.mapRegisteredSprites.get(p_getTextureExtry_1_);
    }
    
    public boolean setTextureEntry(final TextureAtlasSprite p_setTextureEntry_1_) {
        final String s1 = p_setTextureEntry_1_.getIconName();
        if (!this.mapRegisteredSprites.containsKey(s1)) {
            this.mapRegisteredSprites.put(s1, p_setTextureEntry_1_);
            if (p_setTextureEntry_1_.getIndexInMap() < 0) {
                p_setTextureEntry_1_.setIndexInMap(this.counterIndexInMap++);
            }
            return true;
        }
        return false;
    }
    
    public String getBasePath() {
        return this.basePath;
    }
    
    public int getMipmapLevels() {
        return this.mipmapLevels;
    }
    
    private boolean isAbsoluteLocation(final ResourceLocation p_isAbsoluteLocation_1_) {
        final String s1 = p_isAbsoluteLocation_1_.getResourcePath();
        return this.isAbsoluteLocationPath(s1);
    }
    
    private boolean isAbsoluteLocationPath(final String p_isAbsoluteLocationPath_1_) {
        final String s1 = p_isAbsoluteLocationPath_1_.toLowerCase();
        return s1.startsWith("mcpatcher/") || s1.startsWith("optifine/");
    }
    
    public TextureAtlasSprite getSpriteSafe(final String p_getSpriteSafe_1_) {
        final ResourceLocation resourcelocation1 = new ResourceLocation(p_getSpriteSafe_1_);
        return this.mapRegisteredSprites.get(resourcelocation1.toString());
    }
    
    public TextureAtlasSprite getRegisteredSprite(final ResourceLocation p_getRegisteredSprite_1_) {
        return this.mapRegisteredSprites.get(p_getRegisteredSprite_1_.toString());
    }
    
    private boolean isTerrainAnimationActive(final TextureAtlasSprite p_isTerrainAnimationActive_1_) {
        if (p_isTerrainAnimationActive_1_ == TextureUtils.iconWaterStill || p_isTerrainAnimationActive_1_ == TextureUtils.iconWaterFlow) {
            return Config.isAnimatedWater();
        }
        if (p_isTerrainAnimationActive_1_ == TextureUtils.iconLavaStill || p_isTerrainAnimationActive_1_ == TextureUtils.iconLavaFlow) {
            return Config.isAnimatedLava();
        }
        if (p_isTerrainAnimationActive_1_ == TextureUtils.iconFireLayer0 || p_isTerrainAnimationActive_1_ == TextureUtils.iconFireLayer1) {
            return Config.isAnimatedFire();
        }
        if (p_isTerrainAnimationActive_1_ == TextureUtils.iconPortal) {
            return Config.isAnimatedPortal();
        }
        return p_isTerrainAnimationActive_1_ == TextureUtils.iconClock || p_isTerrainAnimationActive_1_ == TextureUtils.iconCompass || Config.isAnimatedTerrain();
    }
    
    public int getCountRegisteredSprites() {
        return this.counterIndexInMap;
    }
    
    private int detectMaxMipmapLevel(final Map p_detectMaxMipmapLevel_1_, final IResourceManager p_detectMaxMipmapLevel_2_) {
        int k3 = this.detectMinimumSpriteSize(p_detectMaxMipmapLevel_1_, p_detectMaxMipmapLevel_2_, 20);
        if (k3 < 16) {
            k3 = 16;
        }
        k3 = MathHelper.smallestEncompassingPowerOfTwo(k3);
        if (k3 > 16) {
            Config.log("Sprite size: " + k3);
        }
        int l3 = MathHelper.log2(k3);
        if (l3 < 4) {
            l3 = 4;
        }
        return l3;
    }
    
    private int detectMinimumSpriteSize(final Map p_detectMinimumSpriteSize_1_, final IResourceManager p_detectMinimumSpriteSize_2_, final int p_detectMinimumSpriteSize_3_) {
        final Map map1 = new HashMap();
        for (final Object entry : p_detectMinimumSpriteSize_1_.entrySet()) {
            final TextureAtlasSprite textureatlassprite6 = ((Map.Entry)entry).getValue();
            final ResourceLocation resourcelocation1 = new ResourceLocation(textureatlassprite6.getIconName());
            final ResourceLocation resourcelocation2 = this.completeResourceLocation(resourcelocation1);
            if (!textureatlassprite6.hasCustomLoader(p_detectMinimumSpriteSize_2_, resourcelocation1)) {
                try {
                    final IResource iresource1 = p_detectMinimumSpriteSize_2_.getResource(resourcelocation2);
                    if (iresource1 == null) {
                        continue;
                    }
                    final InputStream inputstream = iresource1.getInputStream();
                    if (inputstream == null) {
                        continue;
                    }
                    final Dimension dimension = TextureUtils.getImageSize(inputstream, "png");
                    if (dimension == null) {
                        continue;
                    }
                    final int k3 = dimension.width;
                    final int l3 = MathHelper.smallestEncompassingPowerOfTwo(k3);
                    if (!map1.containsKey(l3)) {
                        map1.put(l3, 1);
                    }
                    else {
                        final int i4 = map1.get(l3);
                        map1.put(l3, i4 + 1);
                    }
                }
                catch (final Exception ex) {}
            }
        }
        int j4 = 0;
        final Set set = map1.keySet();
        final Set set2 = new TreeSet(set);
        for (final int l4 : set2) {
            final int j5 = map1.get(l4);
            j4 += j5;
        }
        int k4 = 16;
        int i5 = 0;
        final int j5 = j4 * p_detectMinimumSpriteSize_3_ / 100;
        for (final int k5 : set2) {
            final int l5 = map1.get(k5);
            i5 += l5;
            if (k5 > k4) {
                k4 = k5;
            }
            if (i5 > j5) {
                return k4;
            }
        }
        return k4;
    }
    
    private int getMinSpriteSize() {
        int k3 = 1 << this.mipmapLevels;
        if (k3 < 8) {
            k3 = 8;
        }
        return k3;
    }
    
    private int[] getMissingImageData(final int p_getMissingImageData_1_) {
        final BufferedImage bufferedimage = new BufferedImage(16, 16, 2);
        bufferedimage.setRGB(0, 0, 16, 16, TextureUtil.MISSING_TEXTURE_DATA, 0, 16);
        final BufferedImage bufferedimage2 = TextureUtils.scaleToPowerOfTwo(bufferedimage, p_getMissingImageData_1_);
        final int[] aint = new int[p_getMissingImageData_1_ * p_getMissingImageData_1_];
        bufferedimage2.getRGB(0, 0, p_getMissingImageData_1_, p_getMissingImageData_1_, aint, 0, p_getMissingImageData_1_);
        return aint;
    }
    
    public boolean isTextureBound() {
        final int k3 = GlStateManager.getBoundTexture();
        final int l3 = this.getGlTextureId();
        return k3 == l3;
    }
    
    private void updateIconGrid(final int p_updateIconGrid_1_, final int p_updateIconGrid_2_) {
        this.iconGridCountX = -1;
        this.iconGridCountY = -1;
        this.iconGrid = null;
        if (this.iconGridSize > 0) {
            this.iconGridCountX = p_updateIconGrid_1_ / this.iconGridSize;
            this.iconGridCountY = p_updateIconGrid_2_ / this.iconGridSize;
            this.iconGrid = new TextureAtlasSprite[this.iconGridCountX * this.iconGridCountY];
            this.iconGridSizeU = 1.0 / this.iconGridCountX;
            this.iconGridSizeV = 1.0 / this.iconGridCountY;
            for (final TextureAtlasSprite textureatlassprite6 : this.mapUploadedSprites.values()) {
                final double d0 = 0.5 / p_updateIconGrid_1_;
                final double d2 = 0.5 / p_updateIconGrid_2_;
                final double d3 = Math.min(textureatlassprite6.getMinU(), textureatlassprite6.getMaxU()) + d0;
                final double d4 = Math.min(textureatlassprite6.getMinV(), textureatlassprite6.getMaxV()) + d2;
                final double d5 = Math.max(textureatlassprite6.getMinU(), textureatlassprite6.getMaxU()) - d0;
                final double d6 = Math.max(textureatlassprite6.getMinV(), textureatlassprite6.getMaxV()) - d2;
                final int k3 = (int)(d3 / this.iconGridSizeU);
                final int l3 = (int)(d4 / this.iconGridSizeV);
                final int i4 = (int)(d5 / this.iconGridSizeU);
                final int j4 = (int)(d6 / this.iconGridSizeV);
                for (int k4 = k3; k4 <= i4; ++k4) {
                    if (k4 >= 0 && k4 < this.iconGridCountX) {
                        for (int l4 = l3; l4 <= j4; ++l4) {
                            if (l4 >= 0 && l4 < this.iconGridCountX) {
                                final int i5 = l4 * this.iconGridCountX + k4;
                                this.iconGrid[i5] = textureatlassprite6;
                            }
                            else {
                                Config.warn("Invalid grid V: " + l4 + ", icon: " + textureatlassprite6.getIconName());
                            }
                        }
                    }
                    else {
                        Config.warn("Invalid grid U: " + k4 + ", icon: " + textureatlassprite6.getIconName());
                    }
                }
            }
        }
    }
    
    public TextureAtlasSprite getIconByUV(final double p_getIconByUV_1_, final double p_getIconByUV_3_) {
        if (this.iconGrid == null) {
            return null;
        }
        final int k3 = (int)(p_getIconByUV_1_ / this.iconGridSizeU);
        final int l3 = (int)(p_getIconByUV_3_ / this.iconGridSizeV);
        final int i4 = l3 * this.iconGridCountX + k3;
        return (i4 >= 0 && i4 <= this.iconGrid.length) ? this.iconGrid[i4] : null;
    }
}
