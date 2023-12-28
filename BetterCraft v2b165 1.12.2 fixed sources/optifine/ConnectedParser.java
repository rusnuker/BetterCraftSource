// 
// Decompiled by Procyon v0.6.0
// 

package optifine;

import java.lang.reflect.Array;
import net.minecraft.util.BlockRenderLayer;
import java.util.EnumSet;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.init.Biomes;
import com.google.common.collect.Lists;
import net.minecraft.world.biome.Biome;
import net.minecraft.util.IStringSerializable;
import java.util.Iterator;
import net.minecraft.block.BlockObserver;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.init.Blocks;
import java.util.Map;
import net.minecraft.block.properties.IProperty;
import java.util.HashMap;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import java.util.List;
import java.util.Collection;
import java.util.Arrays;
import java.util.ArrayList;

public class ConnectedParser
{
    private String context;
    
    public ConnectedParser(final String p_i26_1_) {
        this.context = null;
        this.context = p_i26_1_;
    }
    
    public String parseName(final String p_parseName_1_) {
        String s = p_parseName_1_;
        final int i = p_parseName_1_.lastIndexOf(47);
        if (i >= 0) {
            s = p_parseName_1_.substring(i + 1);
        }
        final int j = s.lastIndexOf(46);
        if (j >= 0) {
            s = s.substring(0, j);
        }
        return s;
    }
    
    public String parseBasePath(final String p_parseBasePath_1_) {
        final int i = p_parseBasePath_1_.lastIndexOf(47);
        return (i < 0) ? "" : p_parseBasePath_1_.substring(0, i);
    }
    
    public MatchBlock[] parseMatchBlocks(final String p_parseMatchBlocks_1_) {
        if (p_parseMatchBlocks_1_ == null) {
            return null;
        }
        final List list = new ArrayList();
        final String[] astring = Config.tokenize(p_parseMatchBlocks_1_, " ");
        for (int i = 0; i < astring.length; ++i) {
            final String s = astring[i];
            final MatchBlock[] amatchblock = this.parseMatchBlock(s);
            if (amatchblock != null) {
                list.addAll(Arrays.asList(amatchblock));
            }
        }
        final MatchBlock[] amatchblock2 = list.toArray(new MatchBlock[list.size()]);
        return amatchblock2;
    }
    
    public IBlockState parseBlockState(final String p_parseBlockState_1_, final IBlockState p_parseBlockState_2_) {
        final MatchBlock[] amatchblock = this.parseMatchBlock(p_parseBlockState_1_);
        if (amatchblock == null) {
            return p_parseBlockState_2_;
        }
        if (amatchblock.length != 1) {
            return p_parseBlockState_2_;
        }
        final MatchBlock matchblock = amatchblock[0];
        final int i = matchblock.getBlockId();
        final Block block = Block.getBlockById(i);
        return block.getDefaultState();
    }
    
    public MatchBlock[] parseMatchBlock(String p_parseMatchBlock_1_) {
        if (p_parseMatchBlock_1_ == null) {
            return null;
        }
        p_parseMatchBlock_1_ = p_parseMatchBlock_1_.trim();
        if (p_parseMatchBlock_1_.length() <= 0) {
            return null;
        }
        final String[] astring = Config.tokenize(p_parseMatchBlock_1_, ":");
        String s = "minecraft";
        int i = 0;
        if (astring.length > 1 && this.isFullBlockName(astring)) {
            s = astring[0];
            i = 1;
        }
        else {
            s = "minecraft";
            i = 0;
        }
        final String s2 = astring[i];
        final String[] astring2 = Arrays.copyOfRange(astring, i + 1, astring.length);
        final Block[] ablock = this.parseBlockPart(s, s2);
        if (ablock == null) {
            return null;
        }
        final MatchBlock[] amatchblock = new MatchBlock[ablock.length];
        for (int j = 0; j < ablock.length; ++j) {
            final Block block = ablock[j];
            final int k = Block.getIdFromBlock(block);
            int[] aint = null;
            if (astring2.length > 0) {
                aint = this.parseBlockMetadatas(block, astring2);
                if (aint == null) {
                    return null;
                }
            }
            final MatchBlock matchblock = new MatchBlock(k, aint);
            amatchblock[j] = matchblock;
        }
        return amatchblock;
    }
    
    public boolean isFullBlockName(final String[] p_isFullBlockName_1_) {
        if (p_isFullBlockName_1_.length < 2) {
            return false;
        }
        final String s = p_isFullBlockName_1_[1];
        return s.length() >= 1 && !this.startsWithDigit(s) && !s.contains("=");
    }
    
    public boolean startsWithDigit(final String p_startsWithDigit_1_) {
        if (p_startsWithDigit_1_ == null) {
            return false;
        }
        if (p_startsWithDigit_1_.length() < 1) {
            return false;
        }
        final char c0 = p_startsWithDigit_1_.charAt(0);
        return Character.isDigit(c0);
    }
    
    public Block[] parseBlockPart(final String p_parseBlockPart_1_, final String p_parseBlockPart_2_) {
        if (this.startsWithDigit(p_parseBlockPart_2_)) {
            final int[] aint = this.parseIntList(p_parseBlockPart_2_);
            if (aint == null) {
                return null;
            }
            final Block[] ablock1 = new Block[aint.length];
            for (int j = 0; j < aint.length; ++j) {
                final int i = aint[j];
                final Block block1 = Block.getBlockById(i);
                if (block1 == null) {
                    this.warn("Block not found for id: " + i);
                    return null;
                }
                ablock1[j] = block1;
            }
            return ablock1;
        }
        else {
            final String s = String.valueOf(p_parseBlockPart_1_) + ":" + p_parseBlockPart_2_;
            final Block block2 = Block.getBlockFromName(s);
            if (block2 == null) {
                this.warn("Block not found for name: " + s);
                return null;
            }
            final Block[] ablock2 = { block2 };
            return ablock2;
        }
    }
    
    public int[] parseBlockMetadatas(final Block p_parseBlockMetadatas_1_, final String[] p_parseBlockMetadatas_2_) {
        if (p_parseBlockMetadatas_2_.length <= 0) {
            return null;
        }
        final String s = p_parseBlockMetadatas_2_[0];
        if (this.startsWithDigit(s)) {
            final int[] aint = this.parseIntList(s);
            return aint;
        }
        final IBlockState iblockstate = p_parseBlockMetadatas_1_.getDefaultState();
        final Collection collection = iblockstate.getPropertyNames();
        final Map<IProperty, List<Comparable>> map = new HashMap<IProperty, List<Comparable>>();
        for (int i = 0; i < p_parseBlockMetadatas_2_.length; ++i) {
            final String s2 = p_parseBlockMetadatas_2_[i];
            if (s2.length() > 0) {
                final String[] astring = Config.tokenize(s2, "=");
                if (astring.length != 2) {
                    this.warn("Invalid block property: " + s2);
                    return null;
                }
                final String s3 = astring[0];
                final String s4 = astring[1];
                final IProperty iproperty = ConnectedProperties.getProperty(s3, collection);
                if (iproperty == null) {
                    this.warn("Property not found: " + s3 + ", block: " + p_parseBlockMetadatas_1_);
                    return null;
                }
                List<Comparable> list = map.get(s3);
                if (list == null) {
                    list = new ArrayList<Comparable>();
                    map.put(iproperty, list);
                }
                final String[] astring2 = Config.tokenize(s4, ",");
                for (int j = 0; j < astring2.length; ++j) {
                    final String s5 = astring2[j];
                    final Comparable comparable = parsePropertyValue(iproperty, s5);
                    if (comparable == null) {
                        this.warn("Property value not found: " + s5 + ", property: " + s3 + ", block: " + p_parseBlockMetadatas_1_);
                        return null;
                    }
                    list.add(comparable);
                }
            }
        }
        if (map.isEmpty()) {
            return null;
        }
        final List list2 = new ArrayList();
        for (int k = 0; k < 16; ++k) {
            final int l = k;
            try {
                final IBlockState iblockstate2 = this.getStateFromMeta(p_parseBlockMetadatas_1_, l);
                if (this.matchState(iblockstate2, map)) {
                    list2.add(l);
                }
            }
            catch (final IllegalArgumentException ex) {}
        }
        if (list2.size() == 16) {
            return null;
        }
        final int[] aint2 = new int[list2.size()];
        for (int i2 = 0; i2 < aint2.length; ++i2) {
            aint2[i2] = list2.get(i2);
        }
        return aint2;
    }
    
    private IBlockState getStateFromMeta(final Block p_getStateFromMeta_1_, final int p_getStateFromMeta_2_) {
        try {
            IBlockState iblockstate = p_getStateFromMeta_1_.getStateFromMeta(p_getStateFromMeta_2_);
            if (p_getStateFromMeta_1_ == Blocks.DOUBLE_PLANT && p_getStateFromMeta_2_ > 7) {
                final IBlockState iblockstate2 = p_getStateFromMeta_1_.getStateFromMeta(p_getStateFromMeta_2_ & 0x7);
                iblockstate = iblockstate.withProperty(BlockDoublePlant.VARIANT, (BlockDoublePlant.EnumPlantType)iblockstate2.getValue((IProperty<V>)BlockDoublePlant.VARIANT));
            }
            if (p_getStateFromMeta_1_ == Blocks.field_190976_dk && (p_getStateFromMeta_2_ & 0x8) != 0x0) {
                iblockstate = iblockstate.withProperty((IProperty<Comparable>)BlockObserver.field_190963_a, true);
            }
            return iblockstate;
        }
        catch (final IllegalArgumentException var5) {
            return p_getStateFromMeta_1_.getDefaultState();
        }
    }
    
    public static Comparable parsePropertyValue(final IProperty p_parsePropertyValue_0_, final String p_parsePropertyValue_1_) {
        final Class oclass = p_parsePropertyValue_0_.getValueClass();
        Comparable comparable = parseValue(p_parsePropertyValue_1_, oclass);
        if (comparable == null) {
            final Collection collection = p_parsePropertyValue_0_.getAllowedValues();
            comparable = getPropertyValue(p_parsePropertyValue_1_, collection);
        }
        return comparable;
    }
    
    public static Comparable getPropertyValue(final String p_getPropertyValue_0_, final Collection p_getPropertyValue_1_) {
        for (final Object comparable : p_getPropertyValue_1_) {
            if (getValueName((Comparable)comparable).equals(p_getPropertyValue_0_)) {
                return (Comparable)comparable;
            }
        }
        return null;
    }
    
    private static Object getValueName(final Comparable p_getValueName_0_) {
        if (p_getValueName_0_ instanceof IStringSerializable) {
            final IStringSerializable istringserializable = (IStringSerializable)p_getValueName_0_;
            return istringserializable.getName();
        }
        return p_getValueName_0_.toString();
    }
    
    public static Comparable parseValue(final String p_parseValue_0_, final Class p_parseValue_1_) {
        if (p_parseValue_1_ == String.class) {
            return p_parseValue_0_;
        }
        if (p_parseValue_1_ == Boolean.class) {
            return Boolean.valueOf(p_parseValue_0_);
        }
        if (p_parseValue_1_ == Float.class) {
            return Float.valueOf(p_parseValue_0_);
        }
        if (p_parseValue_1_ == Double.class) {
            return Double.valueOf(p_parseValue_0_);
        }
        if (p_parseValue_1_ == Integer.class) {
            return Integer.valueOf(p_parseValue_0_);
        }
        return (p_parseValue_1_ == Long.class) ? Long.valueOf(p_parseValue_0_) : null;
    }
    
    public boolean matchState(final IBlockState p_matchState_1_, final Map<IProperty, List<Comparable>> p_matchState_2_) {
        for (final IProperty iproperty : p_matchState_2_.keySet()) {
            final List<Comparable> list = p_matchState_2_.get(iproperty);
            final Comparable comparable = p_matchState_1_.getValue((IProperty<Comparable>)iproperty);
            if (comparable == null) {
                return false;
            }
            if (!list.contains(comparable)) {
                return false;
            }
        }
        return true;
    }
    
    public Biome[] parseBiomes(String p_parseBiomes_1_) {
        if (p_parseBiomes_1_ == null) {
            return null;
        }
        p_parseBiomes_1_ = p_parseBiomes_1_.trim();
        boolean flag = false;
        if (p_parseBiomes_1_.startsWith("!")) {
            flag = true;
            p_parseBiomes_1_ = p_parseBiomes_1_.substring(1);
        }
        final String[] astring = Config.tokenize(p_parseBiomes_1_, " ");
        List list = new ArrayList();
        for (int i = 0; i < astring.length; ++i) {
            final String s = astring[i];
            final Biome biome = this.findBiome(s);
            if (biome == null) {
                this.warn("Biome not found: " + s);
            }
            else {
                list.add(biome);
            }
        }
        if (flag) {
            final List<Biome> list2 = (List<Biome>)Lists.newArrayList((Iterator<?>)Biome.REGISTRY.iterator());
            list2.removeAll(list);
            list = list2;
        }
        final Biome[] abiome = list.toArray(new Biome[list.size()]);
        return abiome;
    }
    
    public Biome findBiome(String p_findBiome_1_) {
        p_findBiome_1_ = p_findBiome_1_.toLowerCase();
        if (p_findBiome_1_.equals("nether")) {
            return Biomes.HELL;
        }
        for (final ResourceLocation resourcelocation : Biome.REGISTRY.getKeys()) {
            final Biome biome = Biome.REGISTRY.getObject(resourcelocation);
            if (biome != null) {
                final String s = biome.getBiomeName().replace(" ", "").toLowerCase();
                if (s.equals(p_findBiome_1_)) {
                    return biome;
                }
                continue;
            }
        }
        return null;
    }
    
    public int parseInt(String p_parseInt_1_) {
        if (p_parseInt_1_ == null) {
            return -1;
        }
        p_parseInt_1_ = p_parseInt_1_.trim();
        final int i = Config.parseInt(p_parseInt_1_, -1);
        if (i < 0) {
            this.warn("Invalid number: " + p_parseInt_1_);
        }
        return i;
    }
    
    public int parseInt(String p_parseInt_1_, final int p_parseInt_2_) {
        if (p_parseInt_1_ == null) {
            return p_parseInt_2_;
        }
        p_parseInt_1_ = p_parseInt_1_.trim();
        final int i = Config.parseInt(p_parseInt_1_, -1);
        if (i < 0) {
            this.warn("Invalid number: " + p_parseInt_1_);
            return p_parseInt_2_;
        }
        return i;
    }
    
    public int[] parseIntList(final String p_parseIntList_1_) {
        if (p_parseIntList_1_ == null) {
            return null;
        }
        final List list = new ArrayList();
        final String[] astring = Config.tokenize(p_parseIntList_1_, " ,");
        for (int i = 0; i < astring.length; ++i) {
            final String s = astring[i];
            if (s.contains("-")) {
                final String[] astring2 = Config.tokenize(s, "-");
                if (astring2.length != 2) {
                    this.warn("Invalid interval: " + s + ", when parsing: " + p_parseIntList_1_);
                }
                else {
                    final int k = Config.parseInt(astring2[0], -1);
                    final int l = Config.parseInt(astring2[1], -1);
                    if (k >= 0 && l >= 0 && k <= l) {
                        for (int i2 = k; i2 <= l; ++i2) {
                            list.add(i2);
                        }
                    }
                    else {
                        this.warn("Invalid interval: " + s + ", when parsing: " + p_parseIntList_1_);
                    }
                }
            }
            else {
                final int j = Config.parseInt(s, -1);
                if (j < 0) {
                    this.warn("Invalid number: " + s + ", when parsing: " + p_parseIntList_1_);
                }
                else {
                    list.add(j);
                }
            }
        }
        final int[] aint = new int[list.size()];
        for (int j2 = 0; j2 < aint.length; ++j2) {
            aint[j2] = list.get(j2);
        }
        return aint;
    }
    
    public boolean[] parseFaces(final String p_parseFaces_1_, final boolean[] p_parseFaces_2_) {
        if (p_parseFaces_1_ == null) {
            return p_parseFaces_2_;
        }
        final EnumSet enumset = EnumSet.allOf(EnumFacing.class);
        final String[] astring = Config.tokenize(p_parseFaces_1_, " ,");
        for (int i = 0; i < astring.length; ++i) {
            final String s = astring[i];
            if (s.equals("sides")) {
                enumset.add(EnumFacing.NORTH);
                enumset.add(EnumFacing.SOUTH);
                enumset.add(EnumFacing.WEST);
                enumset.add(EnumFacing.EAST);
            }
            else if (s.equals("all")) {
                enumset.addAll(Arrays.asList(EnumFacing.VALUES));
            }
            else {
                final EnumFacing enumfacing = this.parseFace(s);
                if (enumfacing != null) {
                    enumset.add(enumfacing);
                }
            }
        }
        final boolean[] aboolean = new boolean[EnumFacing.VALUES.length];
        for (int j = 0; j < aboolean.length; ++j) {
            aboolean[j] = enumset.contains(EnumFacing.VALUES[j]);
        }
        return aboolean;
    }
    
    public EnumFacing parseFace(String p_parseFace_1_) {
        p_parseFace_1_ = p_parseFace_1_.toLowerCase();
        if (p_parseFace_1_.equals("bottom") || p_parseFace_1_.equals("down")) {
            return EnumFacing.DOWN;
        }
        if (p_parseFace_1_.equals("top") || p_parseFace_1_.equals("up")) {
            return EnumFacing.UP;
        }
        if (p_parseFace_1_.equals("north")) {
            return EnumFacing.NORTH;
        }
        if (p_parseFace_1_.equals("south")) {
            return EnumFacing.SOUTH;
        }
        if (p_parseFace_1_.equals("east")) {
            return EnumFacing.EAST;
        }
        if (p_parseFace_1_.equals("west")) {
            return EnumFacing.WEST;
        }
        Config.warn("Unknown face: " + p_parseFace_1_);
        return null;
    }
    
    public void dbg(final String p_dbg_1_) {
        Config.dbg(this.context + ": " + p_dbg_1_);
    }
    
    public void warn(final String p_warn_1_) {
        Config.warn(this.context + ": " + p_warn_1_);
    }
    
    public RangeListInt parseRangeListInt(final String p_parseRangeListInt_1_) {
        if (p_parseRangeListInt_1_ == null) {
            return null;
        }
        final RangeListInt rangelistint = new RangeListInt();
        final String[] astring = Config.tokenize(p_parseRangeListInt_1_, " ,");
        for (int i = 0; i < astring.length; ++i) {
            final String s = astring[i];
            final RangeInt rangeint = this.parseRangeInt(s);
            if (rangeint == null) {
                return null;
            }
            rangelistint.addRange(rangeint);
        }
        return rangelistint;
    }
    
    private RangeInt parseRangeInt(final String p_parseRangeInt_1_) {
        if (p_parseRangeInt_1_ == null) {
            return null;
        }
        if (p_parseRangeInt_1_.indexOf(45) >= 0) {
            final String[] astring = Config.tokenize(p_parseRangeInt_1_, "-");
            if (astring.length != 2) {
                this.warn("Invalid range: " + p_parseRangeInt_1_);
                return null;
            }
            final int j = Config.parseInt(astring[0], -1);
            final int k = Config.parseInt(astring[1], -1);
            if (j >= 0 && k >= 0) {
                return new RangeInt(j, k);
            }
            this.warn("Invalid range: " + p_parseRangeInt_1_);
            return null;
        }
        else {
            final int i = Config.parseInt(p_parseRangeInt_1_, -1);
            if (i < 0) {
                this.warn("Invalid integer: " + p_parseRangeInt_1_);
                return null;
            }
            return new RangeInt(i, i);
        }
    }
    
    public static boolean parseBoolean(final String p_parseBoolean_0_) {
        return p_parseBoolean_0_ != null && p_parseBoolean_0_.trim().toLowerCase().equals("true");
    }
    
    public Boolean parseBooleanObject(final String p_parseBooleanObject_1_) {
        if (p_parseBooleanObject_1_ == null) {
            return null;
        }
        final String s = p_parseBooleanObject_1_.toLowerCase().trim();
        if (s.equals("true")) {
            return Boolean.TRUE;
        }
        if (s.equals("false")) {
            return Boolean.FALSE;
        }
        this.warn("Invalid boolean: " + p_parseBooleanObject_1_);
        return null;
    }
    
    public static int parseColor(String p_parseColor_0_, final int p_parseColor_1_) {
        if (p_parseColor_0_ == null) {
            return p_parseColor_1_;
        }
        p_parseColor_0_ = p_parseColor_0_.trim();
        try {
            final int i = Integer.parseInt(p_parseColor_0_, 16) & 0xFFFFFF;
            return i;
        }
        catch (final NumberFormatException var3) {
            return p_parseColor_1_;
        }
    }
    
    public static int parseColor4(String p_parseColor4_0_, final int p_parseColor4_1_) {
        if (p_parseColor4_0_ == null) {
            return p_parseColor4_1_;
        }
        p_parseColor4_0_ = p_parseColor4_0_.trim();
        try {
            final int i = (int)(Long.parseLong(p_parseColor4_0_, 16) & -1L);
            return i;
        }
        catch (final NumberFormatException var3) {
            return p_parseColor4_1_;
        }
    }
    
    public BlockRenderLayer parseBlockRenderLayer(String p_parseBlockRenderLayer_1_, final BlockRenderLayer p_parseBlockRenderLayer_2_) {
        if (p_parseBlockRenderLayer_1_ == null) {
            return p_parseBlockRenderLayer_2_;
        }
        p_parseBlockRenderLayer_1_ = p_parseBlockRenderLayer_1_.toLowerCase().trim();
        final BlockRenderLayer[] ablockrenderlayer = BlockRenderLayer.values();
        for (int i = 0; i < ablockrenderlayer.length; ++i) {
            final BlockRenderLayer blockrenderlayer = ablockrenderlayer[i];
            if (p_parseBlockRenderLayer_1_.equals(blockrenderlayer.name().toLowerCase())) {
                return blockrenderlayer;
            }
        }
        return p_parseBlockRenderLayer_2_;
    }
    
    public Enum parseEnum(final String p_parseEnum_1_, final Enum[] p_parseEnum_2_, final String p_parseEnum_3_) {
        if (p_parseEnum_1_ == null) {
            return null;
        }
        final String s = p_parseEnum_1_.toLowerCase().trim();
        for (int i = 0; i < p_parseEnum_2_.length; ++i) {
            final Enum oenum = p_parseEnum_2_[i];
            if (oenum.name().toLowerCase().equals(s)) {
                return oenum;
            }
        }
        this.warn("Invalid " + p_parseEnum_3_ + ": " + p_parseEnum_1_);
        return null;
    }
    
    public Enum[] parseEnums(String p_parseEnums_1_, final Enum[] p_parseEnums_2_, final String p_parseEnums_3_, final Enum[] p_parseEnums_4_) {
        if (p_parseEnums_1_ == null) {
            return null;
        }
        p_parseEnums_1_ = p_parseEnums_1_.toLowerCase().trim();
        final String[] astring = Config.tokenize(p_parseEnums_1_, " ");
        final Enum[] aenum = (Enum[])Array.newInstance(p_parseEnums_2_.getClass().getComponentType(), astring.length);
        for (int i = 0; i < astring.length; ++i) {
            final String s = astring[i];
            final Enum oenum = this.parseEnum(s, p_parseEnums_2_, p_parseEnums_3_);
            if (oenum == null) {
                return p_parseEnums_4_;
            }
            aenum[i] = oenum;
        }
        return aenum;
    }
}
