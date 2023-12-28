// 
// Decompiled by Procyon v0.6.0
// 

package optifine;

import java.util.Hashtable;
import java.util.Collection;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.texture.ITextureObject;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockPartFace;
import net.minecraft.util.EnumFacing;
import net.minecraft.client.renderer.block.model.BlockPart;
import net.minecraft.client.renderer.block.model.SimpleBakedModel;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.client.renderer.block.model.ModelBlock;
import net.minecraft.client.renderer.block.model.ItemModelGenerator;
import java.util.HashMap;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Items;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;
import net.minecraft.item.Item;
import java.util.TreeSet;
import java.util.Properties;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import java.util.Map;

public class CustomItemProperties
{
    public String name;
    public String basePath;
    public int type;
    public int[] items;
    public String texture;
    public Map<String, String> mapTextures;
    public String model;
    public Map<String, String> mapModels;
    public RangeListInt damage;
    public boolean damagePercent;
    public int damageMask;
    public RangeListInt stackSize;
    public RangeListInt enchantmentIds;
    public RangeListInt enchantmentLevels;
    public NbtTagValue[] nbtTagValues;
    public int hand;
    public int blend;
    public float speed;
    public float rotation;
    public int layer;
    public float duration;
    public int weight;
    public ResourceLocation textureLocation;
    public Map mapTextureLocations;
    public TextureAtlasSprite sprite;
    public Map mapSprites;
    public IBakedModel bakedModelTexture;
    public Map<String, IBakedModel> mapBakedModelsTexture;
    public IBakedModel bakedModelFull;
    public Map<String, IBakedModel> mapBakedModelsFull;
    private int textureWidth;
    private int textureHeight;
    public static final int TYPE_UNKNOWN = 0;
    public static final int TYPE_ITEM = 1;
    public static final int TYPE_ENCHANTMENT = 2;
    public static final int TYPE_ARMOR = 3;
    public static final int TYPE_ELYTRA = 4;
    public static final int HAND_ANY = 0;
    public static final int HAND_MAIN = 1;
    public static final int HAND_OFF = 2;
    public static final String INVENTORY = "inventory";
    
    public CustomItemProperties(final Properties p_i33_1_, final String p_i33_2_) {
        this.name = null;
        this.basePath = null;
        this.type = 1;
        this.items = null;
        this.texture = null;
        this.mapTextures = null;
        this.model = null;
        this.mapModels = null;
        this.damage = null;
        this.damagePercent = false;
        this.damageMask = 0;
        this.stackSize = null;
        this.enchantmentIds = null;
        this.enchantmentLevels = null;
        this.nbtTagValues = null;
        this.hand = 0;
        this.blend = 1;
        this.speed = 0.0f;
        this.rotation = 0.0f;
        this.layer = 0;
        this.duration = 1.0f;
        this.weight = 0;
        this.textureLocation = null;
        this.mapTextureLocations = null;
        this.sprite = null;
        this.mapSprites = null;
        this.bakedModelTexture = null;
        this.mapBakedModelsTexture = null;
        this.bakedModelFull = null;
        this.mapBakedModelsFull = null;
        this.textureWidth = 0;
        this.textureHeight = 0;
        this.name = parseName(p_i33_2_);
        this.basePath = parseBasePath(p_i33_2_);
        this.type = this.parseType(p_i33_1_.getProperty("type"));
        this.items = this.parseItems(p_i33_1_.getProperty("items"), p_i33_1_.getProperty("matchItems"));
        this.mapModels = parseModels(p_i33_1_, this.basePath);
        this.model = parseModel(p_i33_1_.getProperty("model"), p_i33_2_, this.basePath, this.type, this.mapModels);
        this.mapTextures = parseTextures(p_i33_1_, this.basePath);
        final boolean flag = this.mapModels == null && this.model == null;
        this.texture = parseTexture(p_i33_1_.getProperty("texture"), p_i33_1_.getProperty("tile"), p_i33_1_.getProperty("source"), p_i33_2_, this.basePath, this.type, this.mapTextures, flag);
        String s = p_i33_1_.getProperty("damage");
        if (s != null) {
            this.damagePercent = s.contains("%");
            s = s.replace("%", "");
            this.damage = this.parseRangeListInt(s);
            this.damageMask = this.parseInt(p_i33_1_.getProperty("damageMask"), 0);
        }
        this.stackSize = this.parseRangeListInt(p_i33_1_.getProperty("stackSize"));
        this.enchantmentIds = this.parseRangeListInt(p_i33_1_.getProperty("enchantmentIDs"), new ParserEnchantmentId());
        this.enchantmentLevels = this.parseRangeListInt(p_i33_1_.getProperty("enchantmentLevels"));
        this.nbtTagValues = this.parseNbtTagValues(p_i33_1_);
        this.hand = this.parseHand(p_i33_1_.getProperty("hand"));
        this.blend = Blender.parseBlend(p_i33_1_.getProperty("blend"));
        this.speed = this.parseFloat(p_i33_1_.getProperty("speed"), 0.0f);
        this.rotation = this.parseFloat(p_i33_1_.getProperty("rotation"), 0.0f);
        this.layer = this.parseInt(p_i33_1_.getProperty("layer"), 0);
        this.weight = this.parseInt(p_i33_1_.getProperty("weight"), 0);
        this.duration = this.parseFloat(p_i33_1_.getProperty("duration"), 1.0f);
    }
    
    private static String parseName(final String p_parseName_0_) {
        String s = p_parseName_0_;
        final int i = p_parseName_0_.lastIndexOf(47);
        if (i >= 0) {
            s = p_parseName_0_.substring(i + 1);
        }
        final int j = s.lastIndexOf(46);
        if (j >= 0) {
            s = s.substring(0, j);
        }
        return s;
    }
    
    private static String parseBasePath(final String p_parseBasePath_0_) {
        final int i = p_parseBasePath_0_.lastIndexOf(47);
        return (i < 0) ? "" : p_parseBasePath_0_.substring(0, i);
    }
    
    private int parseType(final String p_parseType_1_) {
        if (p_parseType_1_ == null) {
            return 1;
        }
        if (p_parseType_1_.equals("item")) {
            return 1;
        }
        if (p_parseType_1_.equals("enchantment")) {
            return 2;
        }
        if (p_parseType_1_.equals("armor")) {
            return 3;
        }
        if (p_parseType_1_.equals("elytra")) {
            return 4;
        }
        Config.warn("Unknown method: " + p_parseType_1_);
        return 0;
    }
    
    private int[] parseItems(String p_parseItems_1_, final String p_parseItems_2_) {
        if (p_parseItems_1_ == null) {
            p_parseItems_1_ = p_parseItems_2_;
        }
        if (p_parseItems_1_ == null) {
            return null;
        }
        p_parseItems_1_ = p_parseItems_1_.trim();
        final Set set = new TreeSet();
        final String[] astring = Config.tokenize(p_parseItems_1_, " ");
        for (int i = 0; i < astring.length; ++i) {
            final String s = astring[i];
            final int j = Config.parseInt(s, -1);
            if (j >= 0) {
                set.add(new Integer(j));
            }
            else {
                if (s.contains("-")) {
                    final String[] astring2 = Config.tokenize(s, "-");
                    if (astring2.length == 2) {
                        final int k = Config.parseInt(astring2[0], -1);
                        final int l = Config.parseInt(astring2[1], -1);
                        if (k >= 0 && l >= 0) {
                            final int i2 = Math.min(k, l);
                            for (int j2 = Math.max(k, l), k2 = i2; k2 <= j2; ++k2) {
                                set.add(new Integer(k2));
                            }
                            continue;
                        }
                    }
                }
                final Item item = Item.getByNameOrId(s);
                if (item == null) {
                    Config.warn("Item not found: " + s);
                }
                else {
                    final int i3 = Item.getIdFromItem(item);
                    if (i3 <= 0) {
                        Config.warn("Item not found: " + s);
                    }
                    else {
                        set.add(new Integer(i3));
                    }
                }
            }
        }
        final Integer[] ainteger = set.toArray(new Integer[set.size()]);
        final int[] aint = new int[ainteger.length];
        for (int l2 = 0; l2 < aint.length; ++l2) {
            aint[l2] = ainteger[l2];
        }
        return aint;
    }
    
    private static String parseTexture(String p_parseTexture_0_, final String p_parseTexture_1_, final String p_parseTexture_2_, final String p_parseTexture_3_, final String p_parseTexture_4_, final int p_parseTexture_5_, final Map<String, String> p_parseTexture_6_, final boolean p_parseTexture_7_) {
        if (p_parseTexture_0_ == null) {
            p_parseTexture_0_ = p_parseTexture_1_;
        }
        if (p_parseTexture_0_ == null) {
            p_parseTexture_0_ = p_parseTexture_2_;
        }
        if (p_parseTexture_0_ != null) {
            final String s2 = ".png";
            if (p_parseTexture_0_.endsWith(s2)) {
                p_parseTexture_0_ = p_parseTexture_0_.substring(0, p_parseTexture_0_.length() - s2.length());
            }
            p_parseTexture_0_ = fixTextureName(p_parseTexture_0_, p_parseTexture_4_);
            return p_parseTexture_0_;
        }
        if (p_parseTexture_5_ == 3) {
            return null;
        }
        if (p_parseTexture_6_ != null) {
            final String s3 = p_parseTexture_6_.get("texture.bow_standby");
            if (s3 != null) {
                return s3;
            }
        }
        if (!p_parseTexture_7_) {
            return null;
        }
        String s4 = p_parseTexture_3_;
        final int i = p_parseTexture_3_.lastIndexOf(47);
        if (i >= 0) {
            s4 = p_parseTexture_3_.substring(i + 1);
        }
        final int j = s4.lastIndexOf(46);
        if (j >= 0) {
            s4 = s4.substring(0, j);
        }
        s4 = fixTextureName(s4, p_parseTexture_4_);
        return s4;
    }
    
    private static Map parseTextures(final Properties p_parseTextures_0_, final String p_parseTextures_1_) {
        final String s = "texture.";
        final Map map = getMatchingProperties(p_parseTextures_0_, s);
        if (map.size() <= 0) {
            return null;
        }
        final Set set = map.keySet();
        final Map map2 = new LinkedHashMap();
        for (final Object s2 : set) {
            String s3 = map.get(s2);
            s3 = fixTextureName(s3, p_parseTextures_1_);
            map2.put(s2, s3);
        }
        return map2;
    }
    
    private static String fixTextureName(String p_fixTextureName_0_, final String p_fixTextureName_1_) {
        p_fixTextureName_0_ = TextureUtils.fixResourcePath(p_fixTextureName_0_, p_fixTextureName_1_);
        if (!p_fixTextureName_0_.startsWith(p_fixTextureName_1_) && !p_fixTextureName_0_.startsWith("textures/") && !p_fixTextureName_0_.startsWith("mcpatcher/")) {
            p_fixTextureName_0_ = String.valueOf(p_fixTextureName_1_) + "/" + p_fixTextureName_0_;
        }
        if (p_fixTextureName_0_.endsWith(".png")) {
            p_fixTextureName_0_ = p_fixTextureName_0_.substring(0, p_fixTextureName_0_.length() - 4);
        }
        if (p_fixTextureName_0_.startsWith("/")) {
            p_fixTextureName_0_ = p_fixTextureName_0_.substring(1);
        }
        return p_fixTextureName_0_;
    }
    
    private static String parseModel(String p_parseModel_0_, final String p_parseModel_1_, final String p_parseModel_2_, final int p_parseModel_3_, final Map<String, String> p_parseModel_4_) {
        if (p_parseModel_0_ != null) {
            final String s1 = ".json";
            if (p_parseModel_0_.endsWith(s1)) {
                p_parseModel_0_ = p_parseModel_0_.substring(0, p_parseModel_0_.length() - s1.length());
            }
            p_parseModel_0_ = fixModelName(p_parseModel_0_, p_parseModel_2_);
            return p_parseModel_0_;
        }
        if (p_parseModel_3_ == 3) {
            return null;
        }
        if (p_parseModel_4_ != null) {
            final String s2 = p_parseModel_4_.get("model.bow_standby");
            if (s2 != null) {
                return s2;
            }
        }
        return p_parseModel_0_;
    }
    
    private static Map parseModels(final Properties p_parseModels_0_, final String p_parseModels_1_) {
        final String s = "model.";
        final Map map = getMatchingProperties(p_parseModels_0_, s);
        if (map.size() <= 0) {
            return null;
        }
        final Set set = map.keySet();
        final Map map2 = new LinkedHashMap();
        for (final Object s2 : set) {
            String s3 = map.get(s2);
            s3 = fixModelName(s3, p_parseModels_1_);
            map2.put(s2, s3);
        }
        return map2;
    }
    
    private static String fixModelName(String p_fixModelName_0_, final String p_fixModelName_1_) {
        p_fixModelName_0_ = TextureUtils.fixResourcePath(p_fixModelName_0_, p_fixModelName_1_);
        final boolean flag = p_fixModelName_0_.startsWith("block/") || p_fixModelName_0_.startsWith("item/");
        if (!p_fixModelName_0_.startsWith(p_fixModelName_1_) && !flag && !p_fixModelName_0_.startsWith("mcpatcher/")) {
            p_fixModelName_0_ = String.valueOf(p_fixModelName_1_) + "/" + p_fixModelName_0_;
        }
        final String s = ".json";
        if (p_fixModelName_0_.endsWith(s)) {
            p_fixModelName_0_ = p_fixModelName_0_.substring(0, p_fixModelName_0_.length() - s.length());
        }
        if (p_fixModelName_0_.startsWith("/")) {
            p_fixModelName_0_ = p_fixModelName_0_.substring(1);
        }
        return p_fixModelName_0_;
    }
    
    private int parseInt(String p_parseInt_1_, final int p_parseInt_2_) {
        if (p_parseInt_1_ == null) {
            return p_parseInt_2_;
        }
        p_parseInt_1_ = p_parseInt_1_.trim();
        final int i = Config.parseInt(p_parseInt_1_, Integer.MIN_VALUE);
        if (i == Integer.MIN_VALUE) {
            Config.warn("Invalid integer: " + p_parseInt_1_);
            return p_parseInt_2_;
        }
        return i;
    }
    
    private float parseFloat(String p_parseFloat_1_, final float p_parseFloat_2_) {
        if (p_parseFloat_1_ == null) {
            return p_parseFloat_2_;
        }
        p_parseFloat_1_ = p_parseFloat_1_.trim();
        final float f = Config.parseFloat(p_parseFloat_1_, Float.MIN_VALUE);
        if (f == Float.MIN_VALUE) {
            Config.warn("Invalid float: " + p_parseFloat_1_);
            return p_parseFloat_2_;
        }
        return f;
    }
    
    private RangeListInt parseRangeListInt(final String p_parseRangeListInt_1_) {
        return this.parseRangeListInt(p_parseRangeListInt_1_, null);
    }
    
    private RangeListInt parseRangeListInt(final String p_parseRangeListInt_1_, final IParserInt p_parseRangeListInt_2_) {
        if (p_parseRangeListInt_1_ == null) {
            return null;
        }
        final String[] astring = Config.tokenize(p_parseRangeListInt_1_, " ");
        final RangeListInt rangelistint = new RangeListInt();
        for (int i = 0; i < astring.length; ++i) {
            final String s = astring[i];
            if (p_parseRangeListInt_2_ != null) {
                final int j = p_parseRangeListInt_2_.parse(s, Integer.MIN_VALUE);
                if (j != Integer.MIN_VALUE) {
                    rangelistint.addRange(new RangeInt(j, j));
                    continue;
                }
            }
            final RangeInt rangeint = this.parseRangeInt(s);
            if (rangeint == null) {
                Config.warn("Invalid range list: " + p_parseRangeListInt_1_);
                return null;
            }
            rangelistint.addRange(rangeint);
        }
        return rangelistint;
    }
    
    private RangeInt parseRangeInt(String p_parseRangeInt_1_) {
        if (p_parseRangeInt_1_ == null) {
            return null;
        }
        p_parseRangeInt_1_ = p_parseRangeInt_1_.trim();
        final int i = p_parseRangeInt_1_.length() - p_parseRangeInt_1_.replace("-", "").length();
        if (i > 1) {
            Config.warn("Invalid range: " + p_parseRangeInt_1_);
            return null;
        }
        final String[] astring = Config.tokenize(p_parseRangeInt_1_, "- ");
        final int[] aint = new int[astring.length];
        for (int j = 0; j < astring.length; ++j) {
            final String s = astring[j];
            final int k = Config.parseInt(s, -1);
            if (k < 0) {
                Config.warn("Invalid range: " + p_parseRangeInt_1_);
                return null;
            }
            aint[j] = k;
        }
        if (aint.length == 1) {
            final int i2 = aint[0];
            if (p_parseRangeInt_1_.startsWith("-")) {
                return new RangeInt(0, i2);
            }
            if (p_parseRangeInt_1_.endsWith("-")) {
                return new RangeInt(i2, 65535);
            }
            return new RangeInt(i2, i2);
        }
        else {
            if (aint.length == 2) {
                final int l = Math.min(aint[0], aint[1]);
                final int j2 = Math.max(aint[0], aint[1]);
                return new RangeInt(l, j2);
            }
            Config.warn("Invalid range: " + p_parseRangeInt_1_);
            return null;
        }
    }
    
    private NbtTagValue[] parseNbtTagValues(final Properties p_parseNbtTagValues_1_) {
        final String s = "nbt.";
        final Map map = getMatchingProperties(p_parseNbtTagValues_1_, s);
        if (map.size() <= 0) {
            return null;
        }
        final List list = new ArrayList();
        for (final Object s2 : map.keySet()) {
            final String s3 = map.get(s2);
            final String s4 = ((String)s2).substring(s.length());
            final NbtTagValue nbttagvalue = new NbtTagValue(s4, s3);
            list.add(nbttagvalue);
        }
        final NbtTagValue[] anbttagvalue = list.toArray(new NbtTagValue[list.size()]);
        return anbttagvalue;
    }
    
    private static Map getMatchingProperties(final Properties p_getMatchingProperties_0_, final String p_getMatchingProperties_1_) {
        final Map map = new LinkedHashMap();
        for (final Object s : ((Hashtable<Object, V>)p_getMatchingProperties_0_).keySet()) {
            final String s2 = p_getMatchingProperties_0_.getProperty((String)s);
            if (((String)s).startsWith(p_getMatchingProperties_1_)) {
                map.put(s, s2);
            }
        }
        return map;
    }
    
    private int parseHand(String p_parseHand_1_) {
        if (p_parseHand_1_ == null) {
            return 0;
        }
        p_parseHand_1_ = p_parseHand_1_.toLowerCase();
        if (p_parseHand_1_.equals("any")) {
            return 0;
        }
        if (p_parseHand_1_.equals("main")) {
            return 1;
        }
        if (p_parseHand_1_.equals("off")) {
            return 2;
        }
        Config.warn("Invalid hand: " + p_parseHand_1_);
        return 0;
    }
    
    public boolean isValid(final String p_isValid_1_) {
        if (this.name == null || this.name.length() <= 0) {
            Config.warn("No name found: " + p_isValid_1_);
            return false;
        }
        if (this.basePath == null) {
            Config.warn("No base path found: " + p_isValid_1_);
            return false;
        }
        if (this.type == 0) {
            Config.warn("No type defined: " + p_isValid_1_);
            return false;
        }
        if (this.type == 4 && this.items == null) {
            this.items = new int[] { Item.getIdFromItem(Items.ELYTRA) };
        }
        if (this.type == 1 || this.type == 3 || this.type == 4) {
            if (this.items == null) {
                this.items = this.detectItems();
            }
            if (this.items == null) {
                Config.warn("No items defined: " + p_isValid_1_);
                return false;
            }
        }
        if (this.texture == null && this.mapTextures == null && this.model == null && this.mapModels == null) {
            Config.warn("No texture or model specified: " + p_isValid_1_);
            return false;
        }
        if (this.type == 2 && this.enchantmentIds == null) {
            Config.warn("No enchantmentIDs specified: " + p_isValid_1_);
            return false;
        }
        return true;
    }
    
    private int[] detectItems() {
        final Item item = Item.getByNameOrId(this.name);
        if (item == null) {
            return null;
        }
        final int i = Item.getIdFromItem(item);
        return (int[])((i <= 0) ? null : new int[] { i });
    }
    
    public void updateIcons(final TextureMap p_updateIcons_1_) {
        if (this.texture != null) {
            this.textureLocation = this.getTextureLocation(this.texture);
            if (this.type == 1) {
                final ResourceLocation resourcelocation = this.getSpriteLocation(this.textureLocation);
                this.sprite = p_updateIcons_1_.registerSprite(resourcelocation);
            }
        }
        if (this.mapTextures != null) {
            this.mapTextureLocations = new HashMap();
            this.mapSprites = new HashMap();
            for (final String s : this.mapTextures.keySet()) {
                final String s2 = this.mapTextures.get(s);
                final ResourceLocation resourcelocation2 = this.getTextureLocation(s2);
                this.mapTextureLocations.put(s, resourcelocation2);
                if (this.type == 1) {
                    final ResourceLocation resourcelocation3 = this.getSpriteLocation(resourcelocation2);
                    final TextureAtlasSprite textureatlassprite = p_updateIcons_1_.registerSprite(resourcelocation3);
                    this.mapSprites.put(s, textureatlassprite);
                }
            }
        }
    }
    
    private ResourceLocation getTextureLocation(final String p_getTextureLocation_1_) {
        if (p_getTextureLocation_1_ == null) {
            return null;
        }
        final ResourceLocation resourcelocation = new ResourceLocation(p_getTextureLocation_1_);
        final String s = resourcelocation.getResourceDomain();
        String s2 = resourcelocation.getResourcePath();
        if (!s2.contains("/")) {
            s2 = "textures/items/" + s2;
        }
        final String s3 = String.valueOf(s2) + ".png";
        final ResourceLocation resourcelocation2 = new ResourceLocation(s, s3);
        final boolean flag = Config.hasResource(resourcelocation2);
        if (!flag) {
            Config.warn("File not found: " + s3);
        }
        return resourcelocation2;
    }
    
    private ResourceLocation getSpriteLocation(final ResourceLocation p_getSpriteLocation_1_) {
        String s = p_getSpriteLocation_1_.getResourcePath();
        s = StrUtils.removePrefix(s, "textures/");
        s = StrUtils.removeSuffix(s, ".png");
        final ResourceLocation resourcelocation = new ResourceLocation(p_getSpriteLocation_1_.getResourceDomain(), s);
        return resourcelocation;
    }
    
    public void updateModelTexture(final TextureMap p_updateModelTexture_1_, final ItemModelGenerator p_updateModelTexture_2_) {
        if (this.texture != null || this.mapTextures != null) {
            final String[] astring = this.getModelTextures();
            final boolean flag = this.isUseTint();
            this.bakedModelTexture = makeBakedModel(p_updateModelTexture_1_, p_updateModelTexture_2_, astring, flag);
            if (this.type == 1 && this.mapTextures != null) {
                for (final String s : this.mapTextures.keySet()) {
                    final String s2 = this.mapTextures.get(s);
                    final String s3 = StrUtils.removePrefix(s, "texture.");
                    if (s3.startsWith("bow") || s3.startsWith("fishing_rod")) {
                        final String[] astring2 = { s2 };
                        final IBakedModel ibakedmodel = makeBakedModel(p_updateModelTexture_1_, p_updateModelTexture_2_, astring2, flag);
                        if (this.mapBakedModelsTexture == null) {
                            this.mapBakedModelsTexture = new HashMap<String, IBakedModel>();
                        }
                        final String s4 = "item/" + s3;
                        this.mapBakedModelsTexture.put(s4, ibakedmodel);
                    }
                }
            }
        }
    }
    
    private boolean isUseTint() {
        return true;
    }
    
    private static IBakedModel makeBakedModel(final TextureMap p_makeBakedModel_0_, final ItemModelGenerator p_makeBakedModel_1_, final String[] p_makeBakedModel_2_, final boolean p_makeBakedModel_3_) {
        final String[] astring = new String[p_makeBakedModel_2_.length];
        for (int i = 0; i < astring.length; ++i) {
            final String s = p_makeBakedModel_2_[i];
            astring[i] = StrUtils.removePrefix(s, "textures/");
        }
        final ModelBlock modelblock = makeModelBlock(astring);
        final ModelBlock modelblock2 = p_makeBakedModel_1_.makeItemModel(p_makeBakedModel_0_, modelblock);
        final IBakedModel ibakedmodel = bakeModel(p_makeBakedModel_0_, modelblock2, p_makeBakedModel_3_);
        return ibakedmodel;
    }
    
    private String[] getModelTextures() {
        if (this.type == 1 && this.items.length == 1) {
            final Item item = Item.getItemById(this.items[0]);
            final boolean flag = item == Items.POTIONITEM || item == Items.SPLASH_POTION || item == Items.LINGERING_POTION;
            if (flag && this.damage != null && this.damage.getCountRanges() > 0) {
                final RangeInt rangeint = this.damage.getRange(0);
                final int i = rangeint.getMin();
                final boolean flag2 = (i & 0x4000) != 0x0;
                final String s5 = this.getMapTexture(this.mapTextures, "texture.potion_overlay", "items/potion_overlay");
                String s6 = null;
                if (flag2) {
                    s6 = this.getMapTexture(this.mapTextures, "texture.potion_bottle_splash", "items/potion_bottle_splash");
                }
                else {
                    s6 = this.getMapTexture(this.mapTextures, "texture.potion_bottle_drinkable", "items/potion_bottle_drinkable");
                }
                return new String[] { s5, s6 };
            }
            if (item instanceof ItemArmor) {
                final ItemArmor itemarmor = (ItemArmor)item;
                if (itemarmor.getArmorMaterial() == ItemArmor.ArmorMaterial.LEATHER) {
                    final String s7 = "leather";
                    String s8 = "helmet";
                    if (itemarmor.armorType == EntityEquipmentSlot.HEAD) {
                        s8 = "helmet";
                    }
                    if (itemarmor.armorType == EntityEquipmentSlot.CHEST) {
                        s8 = "chestplate";
                    }
                    if (itemarmor.armorType == EntityEquipmentSlot.LEGS) {
                        s8 = "leggings";
                    }
                    if (itemarmor.armorType == EntityEquipmentSlot.FEET) {
                        s8 = "boots";
                    }
                    final String s9 = String.valueOf(s7) + "_" + s8;
                    final String s10 = this.getMapTexture(this.mapTextures, "texture." + s9, "items/" + s9);
                    final String s11 = this.getMapTexture(this.mapTextures, "texture." + s9 + "_overlay", "items/" + s9 + "_overlay");
                    return new String[] { s10, s11 };
                }
            }
        }
        return new String[] { this.texture };
    }
    
    private String getMapTexture(final Map<String, String> p_getMapTexture_1_, final String p_getMapTexture_2_, final String p_getMapTexture_3_) {
        if (p_getMapTexture_1_ == null) {
            return p_getMapTexture_3_;
        }
        final String s = p_getMapTexture_1_.get(p_getMapTexture_2_);
        return (s == null) ? p_getMapTexture_3_ : s;
    }
    
    private static ModelBlock makeModelBlock(final String[] p_makeModelBlock_0_) {
        final StringBuffer stringbuffer = new StringBuffer();
        stringbuffer.append("{\"parent\": \"builtin/generated\",\"textures\": {");
        for (int i = 0; i < p_makeModelBlock_0_.length; ++i) {
            final String s = p_makeModelBlock_0_[i];
            if (i > 0) {
                stringbuffer.append(", ");
            }
            stringbuffer.append("\"layer" + i + "\": \"" + s + "\"");
        }
        stringbuffer.append("}}");
        final String s2 = stringbuffer.toString();
        final ModelBlock modelblock = ModelBlock.deserialize(s2);
        return modelblock;
    }
    
    private static IBakedModel bakeModel(final TextureMap p_bakeModel_0_, final ModelBlock p_bakeModel_1_, final boolean p_bakeModel_2_) {
        final ModelRotation modelrotation = ModelRotation.X0_Y0;
        final boolean flag = false;
        final String s = p_bakeModel_1_.resolveTextureName("particle");
        final TextureAtlasSprite textureatlassprite = p_bakeModel_0_.getAtlasSprite(new ResourceLocation(s).toString());
        final SimpleBakedModel.Builder simplebakedmodel$builder = new SimpleBakedModel.Builder(p_bakeModel_1_, p_bakeModel_1_.createOverrides()).setTexture(textureatlassprite);
        for (final BlockPart blockpart : p_bakeModel_1_.getElements()) {
            for (final EnumFacing enumfacing : blockpart.mapFaces.keySet()) {
                BlockPartFace blockpartface = blockpart.mapFaces.get(enumfacing);
                if (!p_bakeModel_2_) {
                    blockpartface = new BlockPartFace(blockpartface.cullFace, -1, blockpartface.texture, blockpartface.blockFaceUV);
                }
                final String s2 = p_bakeModel_1_.resolveTextureName(blockpartface.texture);
                final TextureAtlasSprite textureatlassprite2 = p_bakeModel_0_.getAtlasSprite(new ResourceLocation(s2).toString());
                final BakedQuad bakedquad = makeBakedQuad(blockpart, blockpartface, textureatlassprite2, enumfacing, modelrotation, flag);
                if (blockpartface.cullFace == null) {
                    simplebakedmodel$builder.addGeneralQuad(bakedquad);
                }
                else {
                    simplebakedmodel$builder.addFaceQuad(modelrotation.rotateFace(blockpartface.cullFace), bakedquad);
                }
            }
        }
        return simplebakedmodel$builder.makeBakedModel();
    }
    
    private static BakedQuad makeBakedQuad(final BlockPart p_makeBakedQuad_0_, final BlockPartFace p_makeBakedQuad_1_, final TextureAtlasSprite p_makeBakedQuad_2_, final EnumFacing p_makeBakedQuad_3_, final ModelRotation p_makeBakedQuad_4_, final boolean p_makeBakedQuad_5_) {
        final FaceBakery facebakery = new FaceBakery();
        return facebakery.makeBakedQuad(p_makeBakedQuad_0_.positionFrom, p_makeBakedQuad_0_.positionTo, p_makeBakedQuad_1_, p_makeBakedQuad_2_, p_makeBakedQuad_3_, p_makeBakedQuad_4_, p_makeBakedQuad_0_.partRotation, p_makeBakedQuad_5_, p_makeBakedQuad_0_.shade);
    }
    
    @Override
    public String toString() {
        return this.basePath + "/" + this.name + ", type: " + this.type + ", items: [" + Config.arrayToString(this.items) + "], textture: " + this.texture;
    }
    
    public float getTextureWidth(final TextureManager p_getTextureWidth_1_) {
        if (this.textureWidth <= 0) {
            if (this.textureLocation != null) {
                final ITextureObject itextureobject = p_getTextureWidth_1_.getTexture(this.textureLocation);
                final int i = itextureobject.getGlTextureId();
                final int j = GlStateManager.getBoundTexture();
                GlStateManager.bindTexture(i);
                this.textureWidth = GL11.glGetTexLevelParameteri(3553, 0, 4096);
                GlStateManager.bindTexture(j);
            }
            if (this.textureWidth <= 0) {
                this.textureWidth = 16;
            }
        }
        return (float)this.textureWidth;
    }
    
    public float getTextureHeight(final TextureManager p_getTextureHeight_1_) {
        if (this.textureHeight <= 0) {
            if (this.textureLocation != null) {
                final ITextureObject itextureobject = p_getTextureHeight_1_.getTexture(this.textureLocation);
                final int i = itextureobject.getGlTextureId();
                final int j = GlStateManager.getBoundTexture();
                GlStateManager.bindTexture(i);
                this.textureHeight = GL11.glGetTexLevelParameteri(3553, 0, 4097);
                GlStateManager.bindTexture(j);
            }
            if (this.textureHeight <= 0) {
                this.textureHeight = 16;
            }
        }
        return (float)this.textureHeight;
    }
    
    public IBakedModel getBakedModel(final ResourceLocation p_getBakedModel_1_, final boolean p_getBakedModel_2_) {
        IBakedModel ibakedmodel;
        Map<String, IBakedModel> map;
        if (p_getBakedModel_2_) {
            ibakedmodel = this.bakedModelFull;
            map = this.mapBakedModelsFull;
        }
        else {
            ibakedmodel = this.bakedModelTexture;
            map = this.mapBakedModelsTexture;
        }
        if (p_getBakedModel_1_ != null && map != null) {
            final String s = p_getBakedModel_1_.getResourcePath();
            final IBakedModel ibakedmodel2 = map.get(s);
            if (ibakedmodel2 != null) {
                return ibakedmodel2;
            }
        }
        return ibakedmodel;
    }
    
    public void loadModels(final ModelBakery p_loadModels_1_) {
        if (this.model != null) {
            loadItemModel(p_loadModels_1_, this.model);
        }
        if (this.type == 1 && this.mapModels != null) {
            for (final String s : this.mapModels.keySet()) {
                final String s2 = this.mapModels.get(s);
                final String s3 = StrUtils.removePrefix(s, "model.");
                if (s3.startsWith("bow") || s3.startsWith("fishing_rod")) {
                    loadItemModel(p_loadModels_1_, s2);
                }
            }
        }
    }
    
    public void updateModelsFull() {
        final ModelManager modelmanager = Config.getModelManager();
        final IBakedModel ibakedmodel = modelmanager.getMissingModel();
        if (this.model != null) {
            final ResourceLocation resourcelocation = getModelLocation(this.model);
            final ModelResourceLocation modelresourcelocation = new ModelResourceLocation(resourcelocation, "inventory");
            this.bakedModelFull = modelmanager.getModel(modelresourcelocation);
            if (this.bakedModelFull == ibakedmodel) {
                Config.warn("Custom Items: Model not found " + modelresourcelocation.getResourcePath());
                this.bakedModelFull = null;
            }
        }
        if (this.type == 1 && this.mapModels != null) {
            for (final String s : this.mapModels.keySet()) {
                final String s2 = this.mapModels.get(s);
                final String s3 = StrUtils.removePrefix(s, "model.");
                if (s3.startsWith("bow") || s3.startsWith("fishing_rod")) {
                    final ResourceLocation resourcelocation2 = getModelLocation(s2);
                    final ModelResourceLocation modelresourcelocation2 = new ModelResourceLocation(resourcelocation2, "inventory");
                    final IBakedModel ibakedmodel2 = modelmanager.getModel(modelresourcelocation2);
                    if (ibakedmodel2 == ibakedmodel) {
                        Config.warn("Custom Items: Model not found " + modelresourcelocation2.getResourcePath());
                    }
                    else {
                        if (this.mapBakedModelsFull == null) {
                            this.mapBakedModelsFull = new HashMap<String, IBakedModel>();
                        }
                        final String s4 = "item/" + s3;
                        this.mapBakedModelsFull.put(s4, ibakedmodel2);
                    }
                }
            }
        }
    }
    
    private static void loadItemModel(final ModelBakery p_loadItemModel_0_, final String p_loadItemModel_1_) {
        final ResourceLocation resourcelocation = getModelLocation(p_loadItemModel_1_);
        final ModelResourceLocation modelresourcelocation = new ModelResourceLocation(resourcelocation, "inventory");
        if (Reflector.ModelLoader.exists()) {
            try {
                final Object object = Reflector.ModelLoader_VanillaLoader_INSTANCE.getValue();
                checkNull(object, "vanillaLoader is null");
                final Object object2 = Reflector.call(object, Reflector.ModelLoader_VanillaLoader_loadModel, modelresourcelocation);
                checkNull(object2, "iModel is null");
                final Map map = (Map)Reflector.getFieldValue(p_loadItemModel_0_, Reflector.ModelLoader_stateModels);
                checkNull(map, "stateModels is null");
                map.put(modelresourcelocation, object2);
                final Set set = (Set)Reflector.ModelLoaderRegistry_textures.getValue();
                checkNull(set, "registryTextures is null");
                final Collection collection = (Collection)Reflector.call(object2, Reflector.IModel_getTextures, new Object[0]);
                checkNull(collection, "modelTextures is null");
                set.addAll(collection);
            }
            catch (final Exception exception) {
                Config.warn("Error registering model: " + modelresourcelocation + ", " + exception.getClass().getName() + ": " + exception.getMessage());
            }
        }
        else {
            p_loadItemModel_0_.loadItemModel(resourcelocation.toString(), modelresourcelocation, resourcelocation);
        }
    }
    
    private static void checkNull(final Object p_checkNull_0_, final String p_checkNull_1_) throws NullPointerException {
        if (p_checkNull_0_ == null) {
            throw new NullPointerException(p_checkNull_1_);
        }
    }
    
    private static ResourceLocation getModelLocation(final String p_getModelLocation_0_) {
        return (Reflector.ModelLoader.exists() && !p_getModelLocation_0_.startsWith("mcpatcher/") && !p_getModelLocation_0_.startsWith("optifine/")) ? new ResourceLocation("models/" + p_getModelLocation_0_) : new ResourceLocation(p_getModelLocation_0_);
    }
}
