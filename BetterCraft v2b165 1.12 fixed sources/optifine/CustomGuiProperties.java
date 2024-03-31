// 
// Decompiled by Procyon v0.6.0
// 

package optifine;

import java.util.Hashtable;
import net.minecraft.entity.passive.EntityMule;
import net.minecraft.entity.passive.EntityDonkey;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityLlama;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.tileentity.TileEntityDropper;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.block.BlockChest;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IWorldNameable;
import net.minecraft.world.IBlockAccess;
import net.minecraft.util.math.BlockPos;
import java.util.Iterator;
import java.util.HashMap;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.ints.IntArraySet;
import java.util.List;
import java.util.ArrayList;
import java.util.Properties;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.world.biome.Biome;
import net.minecraft.util.ResourceLocation;
import java.util.Map;

public class CustomGuiProperties
{
    private String fileName;
    private String basePath;
    private EnumContainer container;
    private Map<ResourceLocation, ResourceLocation> textureLocations;
    private NbtTagValue nbtName;
    private Biome[] biomes;
    private RangeListInt heights;
    private Boolean large;
    private Boolean trapped;
    private Boolean christmas;
    private Boolean ender;
    private RangeListInt levels;
    private VillagerProfession[] professions;
    private EnumVariant[] variants;
    private EnumDyeColor[] colors;
    private static final VillagerProfession[] PROFESSIONS_INVALID;
    private static final EnumVariant[] VARIANTS_HORSE;
    private static final EnumVariant[] VARIANTS_DISPENSER;
    private static final EnumVariant[] VARIANTS_INVALID;
    private static final EnumDyeColor[] COLORS_INVALID;
    private static final ResourceLocation ANVIL_GUI_TEXTURE;
    private static final ResourceLocation BEACON_GUI_TEXTURE;
    private static final ResourceLocation BREWING_STAND_GUI_TEXTURE;
    private static final ResourceLocation CHEST_GUI_TEXTURE;
    private static final ResourceLocation CRAFTING_TABLE_GUI_TEXTURE;
    private static final ResourceLocation HORSE_GUI_TEXTURE;
    private static final ResourceLocation DISPENSER_GUI_TEXTURE;
    private static final ResourceLocation ENCHANTMENT_TABLE_GUI_TEXTURE;
    private static final ResourceLocation FURNACE_GUI_TEXTURE;
    private static final ResourceLocation HOPPER_GUI_TEXTURE;
    private static final ResourceLocation INVENTORY_GUI_TEXTURE;
    private static final ResourceLocation SHULKER_BOX_GUI_TEXTURE;
    private static final ResourceLocation VILLAGER_GUI_TEXTURE;
    
    static {
        PROFESSIONS_INVALID = new VillagerProfession[0];
        VARIANTS_HORSE = new EnumVariant[] { EnumVariant.HORSE, EnumVariant.DONKEY, EnumVariant.MULE, EnumVariant.LLAMA };
        VARIANTS_DISPENSER = new EnumVariant[] { EnumVariant.DISPENSER, EnumVariant.DROPPER };
        VARIANTS_INVALID = new EnumVariant[0];
        COLORS_INVALID = new EnumDyeColor[0];
        ANVIL_GUI_TEXTURE = new ResourceLocation("textures/gui/container/anvil.png");
        BEACON_GUI_TEXTURE = new ResourceLocation("textures/gui/container/beacon.png");
        BREWING_STAND_GUI_TEXTURE = new ResourceLocation("textures/gui/container/brewing_stand.png");
        CHEST_GUI_TEXTURE = new ResourceLocation("textures/gui/container/generic_54.png");
        CRAFTING_TABLE_GUI_TEXTURE = new ResourceLocation("textures/gui/container/crafting_table.png");
        HORSE_GUI_TEXTURE = new ResourceLocation("textures/gui/container/horse.png");
        DISPENSER_GUI_TEXTURE = new ResourceLocation("textures/gui/container/dispenser.png");
        ENCHANTMENT_TABLE_GUI_TEXTURE = new ResourceLocation("textures/gui/container/enchanting_table.png");
        FURNACE_GUI_TEXTURE = new ResourceLocation("textures/gui/container/furnace.png");
        HOPPER_GUI_TEXTURE = new ResourceLocation("textures/gui/container/hopper.png");
        INVENTORY_GUI_TEXTURE = new ResourceLocation("textures/gui/container/inventory.png");
        SHULKER_BOX_GUI_TEXTURE = new ResourceLocation("textures/gui/container/shulker_box.png");
        VILLAGER_GUI_TEXTURE = new ResourceLocation("textures/gui/container/villager.png");
    }
    
    public CustomGuiProperties(final Properties p_i32_1_, final String p_i32_2_) {
        this.fileName = null;
        this.basePath = null;
        this.container = null;
        this.textureLocations = null;
        this.nbtName = null;
        this.biomes = null;
        this.heights = null;
        this.large = null;
        this.trapped = null;
        this.christmas = null;
        this.ender = null;
        this.levels = null;
        this.professions = null;
        this.variants = null;
        this.colors = null;
        final ConnectedParser connectedparser = new ConnectedParser("CustomGuis");
        this.fileName = connectedparser.parseName(p_i32_2_);
        this.basePath = connectedparser.parseBasePath(p_i32_2_);
        this.container = (EnumContainer)connectedparser.parseEnum(p_i32_1_.getProperty("container"), EnumContainer.values(), "container");
        this.textureLocations = parseTextureLocations(p_i32_1_, "texture", this.container, "textures/gui/", this.basePath);
        this.nbtName = parseNbtTagValue("name", p_i32_1_.getProperty("name"));
        this.biomes = connectedparser.parseBiomes(p_i32_1_.getProperty("biomes"));
        this.heights = connectedparser.parseRangeListInt(p_i32_1_.getProperty("heights"));
        this.large = connectedparser.parseBooleanObject(p_i32_1_.getProperty("large"));
        this.trapped = connectedparser.parseBooleanObject(p_i32_1_.getProperty("trapped"));
        this.christmas = connectedparser.parseBooleanObject(p_i32_1_.getProperty("christmas"));
        this.ender = connectedparser.parseBooleanObject(p_i32_1_.getProperty("ender"));
        this.levels = connectedparser.parseRangeListInt(p_i32_1_.getProperty("levels"));
        this.professions = parseProfessions(p_i32_1_.getProperty("professions"));
        final EnumVariant[] acustomguiproperties$enumvariant = getContainerVariants(this.container);
        this.variants = (EnumVariant[])connectedparser.parseEnums(p_i32_1_.getProperty("variants"), acustomguiproperties$enumvariant, "variants", CustomGuiProperties.VARIANTS_INVALID);
        this.colors = parseEnumDyeColors(p_i32_1_.getProperty("colors"));
    }
    
    private static EnumVariant[] getContainerVariants(final EnumContainer p_getContainerVariants_0_) {
        if (p_getContainerVariants_0_ == EnumContainer.HORSE) {
            return CustomGuiProperties.VARIANTS_HORSE;
        }
        return (p_getContainerVariants_0_ == EnumContainer.DISPENSER) ? CustomGuiProperties.VARIANTS_DISPENSER : new EnumVariant[0];
    }
    
    private static EnumDyeColor[] parseEnumDyeColors(String p_parseEnumDyeColors_0_) {
        if (p_parseEnumDyeColors_0_ == null) {
            return null;
        }
        p_parseEnumDyeColors_0_ = p_parseEnumDyeColors_0_.toLowerCase();
        final String[] astring = Config.tokenize(p_parseEnumDyeColors_0_, " ");
        final EnumDyeColor[] aenumdyecolor = new EnumDyeColor[astring.length];
        for (int i = 0; i < astring.length; ++i) {
            final String s = astring[i];
            final EnumDyeColor enumdyecolor = parseEnumDyeColor(s);
            if (enumdyecolor == null) {
                warn("Invalid color: " + s);
                return CustomGuiProperties.COLORS_INVALID;
            }
            aenumdyecolor[i] = enumdyecolor;
        }
        return aenumdyecolor;
    }
    
    private static EnumDyeColor parseEnumDyeColor(final String p_parseEnumDyeColor_0_) {
        if (p_parseEnumDyeColor_0_ == null) {
            return null;
        }
        final EnumDyeColor[] aenumdyecolor = EnumDyeColor.values();
        for (int i = 0; i < aenumdyecolor.length; ++i) {
            final EnumDyeColor enumdyecolor = aenumdyecolor[i];
            if (enumdyecolor.getName().equals(p_parseEnumDyeColor_0_)) {
                return enumdyecolor;
            }
            if (enumdyecolor.getUnlocalizedName().equals(p_parseEnumDyeColor_0_)) {
                return enumdyecolor;
            }
        }
        return null;
    }
    
    private static NbtTagValue parseNbtTagValue(final String p_parseNbtTagValue_0_, final String p_parseNbtTagValue_1_) {
        return (p_parseNbtTagValue_0_ != null && p_parseNbtTagValue_1_ != null) ? new NbtTagValue(p_parseNbtTagValue_0_, p_parseNbtTagValue_1_) : null;
    }
    
    private static VillagerProfession[] parseProfessions(final String p_parseProfessions_0_) {
        if (p_parseProfessions_0_ == null) {
            return null;
        }
        final List<VillagerProfession> list = new ArrayList<VillagerProfession>();
        final String[] astring = Config.tokenize(p_parseProfessions_0_, " ");
        for (int i = 0; i < astring.length; ++i) {
            final String s = astring[i];
            final VillagerProfession villagerprofession = parseProfession(s);
            if (villagerprofession == null) {
                warn("Invalid profession: " + s);
                return CustomGuiProperties.PROFESSIONS_INVALID;
            }
            list.add(villagerprofession);
        }
        if (list.isEmpty()) {
            return null;
        }
        final VillagerProfession[] avillagerprofession = list.toArray(new VillagerProfession[list.size()]);
        return avillagerprofession;
    }
    
    private static VillagerProfession parseProfession(String p_parseProfession_0_) {
        p_parseProfession_0_ = p_parseProfession_0_.toLowerCase();
        final String[] astring = Config.tokenize(p_parseProfession_0_, ":");
        if (astring.length > 2) {
            return null;
        }
        final String s = astring[0];
        String s2 = null;
        if (astring.length > 1) {
            s2 = astring[1];
        }
        final int i = parseProfessionId(s);
        if (i < 0) {
            return null;
        }
        int[] aint = null;
        if (s2 != null) {
            aint = parseCareerIds(i, s2);
            if (aint == null) {
                return null;
            }
        }
        return new VillagerProfession(i, aint);
    }
    
    private static int parseProfessionId(final String p_parseProfessionId_0_) {
        final int i = Config.parseInt(p_parseProfessionId_0_, -1);
        if (i >= 0) {
            return i;
        }
        if (p_parseProfessionId_0_.equals("farmer")) {
            return 0;
        }
        if (p_parseProfessionId_0_.equals("librarian")) {
            return 1;
        }
        if (p_parseProfessionId_0_.equals("priest")) {
            return 2;
        }
        if (p_parseProfessionId_0_.equals("blacksmith")) {
            return 3;
        }
        if (p_parseProfessionId_0_.equals("butcher")) {
            return 4;
        }
        return p_parseProfessionId_0_.equals("nitwit") ? 5 : -1;
    }
    
    private static int[] parseCareerIds(final int p_parseCareerIds_0_, final String p_parseCareerIds_1_) {
        final IntSet intset = new IntArraySet();
        final String[] astring = Config.tokenize(p_parseCareerIds_1_, ",");
        for (int i = 0; i < astring.length; ++i) {
            final String s = astring[i];
            final int j = parseCareerId(p_parseCareerIds_0_, s);
            if (j < 0) {
                return null;
            }
            intset.add(j);
        }
        final int[] aint = intset.toIntArray();
        return aint;
    }
    
    private static int parseCareerId(final int p_parseCareerId_0_, final String p_parseCareerId_1_) {
        final int i = Config.parseInt(p_parseCareerId_1_, -1);
        if (i >= 0) {
            return i;
        }
        if (p_parseCareerId_0_ == 0) {
            if (p_parseCareerId_1_.equals("farmer")) {
                return 1;
            }
            if (p_parseCareerId_1_.equals("fisherman")) {
                return 2;
            }
            if (p_parseCareerId_1_.equals("shepherd")) {
                return 3;
            }
            if (p_parseCareerId_1_.equals("fletcher")) {
                return 4;
            }
        }
        if (p_parseCareerId_0_ == 1) {
            if (p_parseCareerId_1_.equals("librarian")) {
                return 1;
            }
            if (p_parseCareerId_1_.equals("cartographer")) {
                return 2;
            }
        }
        if (p_parseCareerId_0_ == 2 && p_parseCareerId_1_.equals("cleric")) {
            return 1;
        }
        if (p_parseCareerId_0_ == 3) {
            if (p_parseCareerId_1_.equals("armor")) {
                return 1;
            }
            if (p_parseCareerId_1_.equals("weapon")) {
                return 2;
            }
            if (p_parseCareerId_1_.equals("tool")) {
                return 3;
            }
        }
        if (p_parseCareerId_0_ == 4) {
            if (p_parseCareerId_1_.equals("butcher")) {
                return 1;
            }
            if (p_parseCareerId_1_.equals("leather")) {
                return 2;
            }
        }
        return (p_parseCareerId_0_ == 5 && p_parseCareerId_1_.equals("nitwit")) ? 1 : -1;
    }
    
    private static ResourceLocation parseTextureLocation(String p_parseTextureLocation_0_, final String p_parseTextureLocation_1_) {
        if (p_parseTextureLocation_0_ == null) {
            return null;
        }
        p_parseTextureLocation_0_ = p_parseTextureLocation_0_.trim();
        String s = TextureUtils.fixResourcePath(p_parseTextureLocation_0_, p_parseTextureLocation_1_);
        if (!s.endsWith(".png")) {
            s = String.valueOf(s) + ".png";
        }
        return new ResourceLocation(String.valueOf(p_parseTextureLocation_1_) + "/" + s);
    }
    
    private static Map<ResourceLocation, ResourceLocation> parseTextureLocations(final Properties p_parseTextureLocations_0_, final String p_parseTextureLocations_1_, final EnumContainer p_parseTextureLocations_2_, final String p_parseTextureLocations_3_, final String p_parseTextureLocations_4_) {
        final Map<ResourceLocation, ResourceLocation> map = new HashMap<ResourceLocation, ResourceLocation>();
        final String s = p_parseTextureLocations_0_.getProperty(p_parseTextureLocations_1_);
        if (s != null) {
            final ResourceLocation resourcelocation = getGuiTextureLocation(p_parseTextureLocations_2_);
            final ResourceLocation resourcelocation2 = parseTextureLocation(s, p_parseTextureLocations_4_);
            if (resourcelocation != null && resourcelocation2 != null) {
                map.put(resourcelocation, resourcelocation2);
            }
        }
        final String s2 = String.valueOf(p_parseTextureLocations_1_) + ".";
        for (final Object s3 : ((Hashtable<Object, V>)p_parseTextureLocations_0_).keySet()) {
            if (((String)s3).startsWith(s2)) {
                String s4 = ((String)s3).substring(s2.length());
                s4 = s4.replace('\\', '/');
                s4 = StrUtils.removePrefixSuffix(s4, "/", ".png");
                final String s5 = String.valueOf(p_parseTextureLocations_3_) + s4 + ".png";
                final String s6 = p_parseTextureLocations_0_.getProperty((String)s3);
                final ResourceLocation resourcelocation3 = new ResourceLocation(s5);
                final ResourceLocation resourcelocation4 = parseTextureLocation(s6, p_parseTextureLocations_4_);
                map.put(resourcelocation3, resourcelocation4);
            }
        }
        return map;
    }
    
    private static ResourceLocation getGuiTextureLocation(final EnumContainer p_getGuiTextureLocation_0_) {
        switch (p_getGuiTextureLocation_0_) {
            case ANVIL: {
                return CustomGuiProperties.ANVIL_GUI_TEXTURE;
            }
            case BEACON: {
                return CustomGuiProperties.BEACON_GUI_TEXTURE;
            }
            case BREWING_STAND: {
                return CustomGuiProperties.BREWING_STAND_GUI_TEXTURE;
            }
            case CHEST: {
                return CustomGuiProperties.CHEST_GUI_TEXTURE;
            }
            case CRAFTING: {
                return CustomGuiProperties.CRAFTING_TABLE_GUI_TEXTURE;
            }
            case CREATIVE: {
                return null;
            }
            case DISPENSER: {
                return CustomGuiProperties.DISPENSER_GUI_TEXTURE;
            }
            case ENCHANTMENT: {
                return CustomGuiProperties.ENCHANTMENT_TABLE_GUI_TEXTURE;
            }
            case FURNACE: {
                return CustomGuiProperties.FURNACE_GUI_TEXTURE;
            }
            case HOPPER: {
                return CustomGuiProperties.HOPPER_GUI_TEXTURE;
            }
            case HORSE: {
                return CustomGuiProperties.HORSE_GUI_TEXTURE;
            }
            case INVENTORY: {
                return CustomGuiProperties.INVENTORY_GUI_TEXTURE;
            }
            case SHULKER_BOX: {
                return CustomGuiProperties.SHULKER_BOX_GUI_TEXTURE;
            }
            case VILLAGER: {
                return CustomGuiProperties.VILLAGER_GUI_TEXTURE;
            }
            default: {
                return null;
            }
        }
    }
    
    public boolean isValid(final String p_isValid_1_) {
        if (this.fileName == null || this.fileName.length() <= 0) {
            warn("No name found: " + p_isValid_1_);
            return false;
        }
        if (this.basePath == null) {
            warn("No base path found: " + p_isValid_1_);
            return false;
        }
        if (this.container == null) {
            warn("No container found: " + p_isValid_1_);
            return false;
        }
        if (this.textureLocations.isEmpty()) {
            warn("No texture found: " + p_isValid_1_);
            return false;
        }
        if (this.professions == CustomGuiProperties.PROFESSIONS_INVALID) {
            warn("Invalid professions or careers: " + p_isValid_1_);
            return false;
        }
        if (this.variants == CustomGuiProperties.VARIANTS_INVALID) {
            warn("Invalid variants: " + p_isValid_1_);
            return false;
        }
        if (this.colors == CustomGuiProperties.COLORS_INVALID) {
            warn("Invalid colors: " + p_isValid_1_);
            return false;
        }
        return true;
    }
    
    private static void warn(final String p_warn_0_) {
        Config.warn("[CustomGuis] " + p_warn_0_);
    }
    
    private boolean matchesGeneral(final EnumContainer p_matchesGeneral_1_, final BlockPos p_matchesGeneral_2_, final IBlockAccess p_matchesGeneral_3_) {
        if (this.container != p_matchesGeneral_1_) {
            return false;
        }
        if (this.biomes != null) {
            final Biome biome = p_matchesGeneral_3_.getBiome(p_matchesGeneral_2_);
            if (!Matches.biome(biome, this.biomes)) {
                return false;
            }
        }
        return this.heights == null || this.heights.isInRange(p_matchesGeneral_2_.getY());
    }
    
    public boolean matchesPos(final EnumContainer p_matchesPos_1_, final BlockPos p_matchesPos_2_, final IBlockAccess p_matchesPos_3_) {
        if (!this.matchesGeneral(p_matchesPos_1_, p_matchesPos_2_, p_matchesPos_3_)) {
            return false;
        }
        switch (p_matchesPos_1_) {
            case BEACON: {
                return this.matchesBeacon(p_matchesPos_2_, p_matchesPos_3_);
            }
            case BREWING_STAND: {
                return this.matchesNameable(p_matchesPos_2_, p_matchesPos_3_);
            }
            case CHEST: {
                return this.matchesChest(p_matchesPos_2_, p_matchesPos_3_);
            }
            default: {
                return true;
            }
            case DISPENSER: {
                return this.matchesDispenser(p_matchesPos_2_, p_matchesPos_3_);
            }
            case ENCHANTMENT: {
                return this.matchesNameable(p_matchesPos_2_, p_matchesPos_3_);
            }
            case FURNACE: {
                return this.matchesNameable(p_matchesPos_2_, p_matchesPos_3_);
            }
            case HOPPER: {
                return this.matchesNameable(p_matchesPos_2_, p_matchesPos_3_);
            }
            case SHULKER_BOX: {
                return this.matchesShulker(p_matchesPos_2_, p_matchesPos_3_);
            }
        }
    }
    
    private boolean matchesNameable(final BlockPos p_matchesNameable_1_, final IBlockAccess p_matchesNameable_2_) {
        final TileEntity tileentity = p_matchesNameable_2_.getTileEntity(p_matchesNameable_1_);
        if (!(tileentity instanceof IWorldNameable)) {
            return false;
        }
        final IWorldNameable iworldnameable = (IWorldNameable)tileentity;
        if (this.nbtName != null) {
            final String s = iworldnameable.getName();
            if (!this.nbtName.matchesValue(s)) {
                return false;
            }
        }
        return true;
    }
    
    private boolean matchesBeacon(final BlockPos p_matchesBeacon_1_, final IBlockAccess p_matchesBeacon_2_) {
        final TileEntity tileentity = p_matchesBeacon_2_.getTileEntity(p_matchesBeacon_1_);
        if (!(tileentity instanceof TileEntityBeacon)) {
            return false;
        }
        final TileEntityBeacon tileentitybeacon = (TileEntityBeacon)tileentity;
        if (this.levels != null) {
            final int i = tileentitybeacon.func_191979_s();
            if (!this.levels.isInRange(i)) {
                return false;
            }
        }
        if (this.nbtName != null) {
            final String s = tileentitybeacon.getName();
            if (!this.nbtName.matchesValue(s)) {
                return false;
            }
        }
        return true;
    }
    
    private boolean matchesChest(final BlockPos p_matchesChest_1_, final IBlockAccess p_matchesChest_2_) {
        final TileEntity tileentity = p_matchesChest_2_.getTileEntity(p_matchesChest_1_);
        if (tileentity instanceof TileEntityChest) {
            final TileEntityChest tileentitychest = (TileEntityChest)tileentity;
            return this.matchesChest(tileentitychest, p_matchesChest_1_, p_matchesChest_2_);
        }
        if (tileentity instanceof TileEntityEnderChest) {
            final TileEntityEnderChest tileentityenderchest = (TileEntityEnderChest)tileentity;
            return this.matchesEnderChest(tileentityenderchest, p_matchesChest_1_, p_matchesChest_2_);
        }
        return false;
    }
    
    private boolean matchesChest(final TileEntityChest p_matchesChest_1_, final BlockPos p_matchesChest_2_, final IBlockAccess p_matchesChest_3_) {
        final boolean flag = p_matchesChest_1_.adjacentChestXNeg != null || p_matchesChest_1_.adjacentChestXPos != null || p_matchesChest_1_.adjacentChestZNeg != null || p_matchesChest_1_.adjacentChestZPos != null;
        final boolean flag2 = p_matchesChest_1_.getChestType() == BlockChest.Type.TRAP;
        final boolean flag3 = CustomGuis.isChristmas;
        final boolean flag4 = false;
        final String s = p_matchesChest_1_.getName();
        return this.matchesChest(flag, flag2, flag3, flag4, s);
    }
    
    private boolean matchesEnderChest(final TileEntityEnderChest p_matchesEnderChest_1_, final BlockPos p_matchesEnderChest_2_, final IBlockAccess p_matchesEnderChest_3_) {
        return this.matchesChest(false, false, false, true, null);
    }
    
    private boolean matchesChest(final boolean p_matchesChest_1_, final boolean p_matchesChest_2_, final boolean p_matchesChest_3_, final boolean p_matchesChest_4_, final String p_matchesChest_5_) {
        return (this.large == null || this.large == p_matchesChest_1_) && (this.trapped == null || this.trapped == p_matchesChest_2_) && (this.christmas == null || this.christmas == p_matchesChest_3_) && (this.ender == null || this.ender == p_matchesChest_4_) && (this.nbtName == null || this.nbtName.matchesValue(p_matchesChest_5_));
    }
    
    private boolean matchesDispenser(final BlockPos p_matchesDispenser_1_, final IBlockAccess p_matchesDispenser_2_) {
        final TileEntity tileentity = p_matchesDispenser_2_.getTileEntity(p_matchesDispenser_1_);
        if (!(tileentity instanceof TileEntityDispenser)) {
            return false;
        }
        final TileEntityDispenser tileentitydispenser = (TileEntityDispenser)tileentity;
        if (this.nbtName != null) {
            final String s = tileentitydispenser.getName();
            if (!this.nbtName.matchesValue(s)) {
                return false;
            }
        }
        if (this.variants != null) {
            final EnumVariant customguiproperties$enumvariant = this.getDispenserVariant(tileentitydispenser);
            if (!Config.equalsOne(customguiproperties$enumvariant, this.variants)) {
                return false;
            }
        }
        return true;
    }
    
    private EnumVariant getDispenserVariant(final TileEntityDispenser p_getDispenserVariant_1_) {
        return (p_getDispenserVariant_1_ instanceof TileEntityDropper) ? EnumVariant.DROPPER : EnumVariant.DISPENSER;
    }
    
    private boolean matchesShulker(final BlockPos p_matchesShulker_1_, final IBlockAccess p_matchesShulker_2_) {
        final TileEntity tileentity = p_matchesShulker_2_.getTileEntity(p_matchesShulker_1_);
        if (!(tileentity instanceof TileEntityShulkerBox)) {
            return false;
        }
        final TileEntityShulkerBox tileentityshulkerbox = (TileEntityShulkerBox)tileentity;
        if (this.nbtName != null) {
            final String s = tileentityshulkerbox.getName();
            if (!this.nbtName.matchesValue(s)) {
                return false;
            }
        }
        if (this.colors != null) {
            final EnumDyeColor enumdyecolor = tileentityshulkerbox.func_190592_s();
            if (!Config.equalsOne(enumdyecolor, this.colors)) {
                return false;
            }
        }
        return true;
    }
    
    public boolean matchesEntity(final EnumContainer p_matchesEntity_1_, final Entity p_matchesEntity_2_, final IBlockAccess p_matchesEntity_3_) {
        if (!this.matchesGeneral(p_matchesEntity_1_, p_matchesEntity_2_.getPosition(), p_matchesEntity_3_)) {
            return false;
        }
        if (this.nbtName != null) {
            final String s = p_matchesEntity_2_.getName();
            if (!this.nbtName.matchesValue(s)) {
                return false;
            }
        }
        switch (p_matchesEntity_1_) {
            case HORSE: {
                return this.matchesHorse(p_matchesEntity_2_, p_matchesEntity_3_);
            }
            case VILLAGER: {
                return this.matchesVillager(p_matchesEntity_2_, p_matchesEntity_3_);
            }
            default: {
                return true;
            }
        }
    }
    
    private boolean matchesVillager(final Entity p_matchesVillager_1_, final IBlockAccess p_matchesVillager_2_) {
        if (!(p_matchesVillager_1_ instanceof EntityVillager)) {
            return false;
        }
        final EntityVillager entityvillager = (EntityVillager)p_matchesVillager_1_;
        final NBTTagCompound nbttagcompound = new NBTTagCompound();
        entityvillager.writeToNBT(nbttagcompound);
        final Integer integer = nbttagcompound.getInteger("Profession");
        final Integer integer2 = nbttagcompound.getInteger("Career");
        if (integer != null && integer2 != null) {
            if (this.professions != null) {
                boolean flag = false;
                for (int i = 0; i < this.professions.length; ++i) {
                    final VillagerProfession villagerprofession = this.professions[i];
                    if (villagerprofession.matches(integer, integer2)) {
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    
    private boolean matchesHorse(final Entity p_matchesHorse_1_, final IBlockAccess p_matchesHorse_2_) {
        if (!(p_matchesHorse_1_ instanceof AbstractHorse)) {
            return false;
        }
        final AbstractHorse abstracthorse = (AbstractHorse)p_matchesHorse_1_;
        if (this.variants != null) {
            final EnumVariant customguiproperties$enumvariant = this.getHorseVariant(abstracthorse);
            if (!Config.equalsOne(customguiproperties$enumvariant, this.variants)) {
                return false;
            }
        }
        if (this.colors != null && abstracthorse instanceof EntityLlama) {
            final EntityLlama entityllama = (EntityLlama)abstracthorse;
            final EnumDyeColor enumdyecolor = entityllama.func_190704_dO();
            if (!Config.equalsOne(enumdyecolor, this.colors)) {
                return false;
            }
        }
        return true;
    }
    
    private EnumVariant getHorseVariant(final AbstractHorse p_getHorseVariant_1_) {
        if (p_getHorseVariant_1_ instanceof EntityHorse) {
            return EnumVariant.HORSE;
        }
        if (p_getHorseVariant_1_ instanceof EntityDonkey) {
            return EnumVariant.DONKEY;
        }
        if (p_getHorseVariant_1_ instanceof EntityMule) {
            return EnumVariant.MULE;
        }
        return (p_getHorseVariant_1_ instanceof EntityLlama) ? EnumVariant.LLAMA : null;
    }
    
    public EnumContainer getContainer() {
        return this.container;
    }
    
    public ResourceLocation getTextureLocation(final ResourceLocation p_getTextureLocation_1_) {
        final ResourceLocation resourcelocation = this.textureLocations.get(p_getTextureLocation_1_);
        return (resourcelocation == null) ? p_getTextureLocation_1_ : resourcelocation;
    }
    
    @Override
    public String toString() {
        return "name: " + this.fileName + ", container: " + this.container + ", textures: " + this.textureLocations;
    }
    
    public enum EnumContainer
    {
        ANVIL("ANVIL", 0), 
        BEACON("BEACON", 1), 
        BREWING_STAND("BREWING_STAND", 2), 
        CHEST("CHEST", 3), 
        CRAFTING("CRAFTING", 4), 
        DISPENSER("DISPENSER", 5), 
        ENCHANTMENT("ENCHANTMENT", 6), 
        FURNACE("FURNACE", 7), 
        HOPPER("HOPPER", 8), 
        HORSE("HORSE", 9), 
        VILLAGER("VILLAGER", 10), 
        SHULKER_BOX("SHULKER_BOX", 11), 
        CREATIVE("CREATIVE", 12), 
        INVENTORY("INVENTORY", 13);
        
        private EnumContainer(final String s, final int n) {
        }
    }
    
    private enum EnumVariant
    {
        HORSE("HORSE", 0), 
        DONKEY("DONKEY", 1), 
        MULE("MULE", 2), 
        LLAMA("LLAMA", 3), 
        DISPENSER("DISPENSER", 4), 
        DROPPER("DROPPER", 5);
        
        private EnumVariant(final String s, final int n) {
        }
    }
}
