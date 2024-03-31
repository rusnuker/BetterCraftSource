// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.block.state;

import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import optifine.BlockModelUtils;
import net.minecraft.block.BlockFlower;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.block.material.MapColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.entity.Entity;
import net.minecraft.block.material.Material;
import com.google.common.collect.Table;
import com.google.common.collect.HashBasedTable;
import java.util.Collections;
import com.google.common.collect.ImmutableTable;
import optifine.Reflector;
import com.google.common.collect.Iterables;
import com.google.common.base.MoreObjects;
import com.google.common.collect.UnmodifiableIterator;
import com.google.common.collect.ImmutableCollection;
import java.util.Iterator;
import java.util.Collection;
import net.minecraft.util.MapPopulator;
import java.util.List;
import net.minecraft.util.math.Cartesian;
import com.google.common.collect.Lists;
import java.util.Map;
import com.google.common.collect.Maps;
import java.util.Optional;
import net.minecraftforge.common.property.IUnlistedProperty;
import com.google.common.collect.ImmutableMap;
import javax.annotation.Nullable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSortedMap;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import com.google.common.base.Function;
import java.util.regex.Pattern;

public class BlockStateContainer
{
    private static final Pattern NAME_PATTERN;
    private static final Function<IProperty<?>, String> GET_NAME_FUNC;
    private final Block block;
    private final ImmutableSortedMap<String, IProperty<?>> properties;
    private final ImmutableList<IBlockState> validStates;
    
    static {
        NAME_PATTERN = Pattern.compile("^[a-z0-9_]+$");
        GET_NAME_FUNC = new Function<IProperty<?>, String>() {
            @Nullable
            @Override
            public String apply(@Nullable final IProperty<?> p_apply_1_) {
                return (p_apply_1_ == null) ? "<NULL>" : p_apply_1_.getName();
            }
        };
    }
    
    public BlockStateContainer(final Block blockIn, final IProperty<?>... properties) {
        this(blockIn, properties, null);
    }
    
    protected StateImplementation createState(final Block p_createState_1_, final ImmutableMap<IProperty<?>, Comparable<?>> p_createState_2_, @Nullable final ImmutableMap<IUnlistedProperty<?>, Optional<?>> p_createState_3_) {
        return new StateImplementation(p_createState_1_, p_createState_2_, (StateImplementation)null);
    }
    
    protected BlockStateContainer(final Block p_i9_1_, final IProperty<?>[] p_i9_2_, final ImmutableMap<IUnlistedProperty<?>, Optional<?>> p_i9_3_) {
        this.block = p_i9_1_;
        final Map<String, IProperty<?>> map = (Map<String, IProperty<?>>)Maps.newHashMap();
        for (final IProperty<?> iproperty : p_i9_2_) {
            validateProperty(p_i9_1_, iproperty);
            map.put(iproperty.getName(), iproperty);
        }
        this.properties = ImmutableSortedMap.copyOf((Map<? extends String, ? extends IProperty<?>>)map);
        final Map<Map<IProperty<?>, Comparable<?>>, StateImplementation> map2 = (Map<Map<IProperty<?>, Comparable<?>>, StateImplementation>)Maps.newLinkedHashMap();
        final List<StateImplementation> list = (List<StateImplementation>)Lists.newArrayList();
        for (final List<Comparable<?>> list2 : Cartesian.cartesianProduct((Iterable<? extends Iterable<?>>)this.getAllowedValues())) {
            final Map<IProperty<?>, Comparable<?>> map3 = MapPopulator.createMap(this.properties.values(), list2);
            final StateImplementation blockstatecontainer$stateimplementation = this.createState(p_i9_1_, ImmutableMap.copyOf((Map<? extends IProperty<?>, ? extends Comparable<?>>)map3), p_i9_3_);
            map2.put(map3, blockstatecontainer$stateimplementation);
            list.add(blockstatecontainer$stateimplementation);
        }
        for (final StateImplementation blockstatecontainer$stateimplementation2 : list) {
            blockstatecontainer$stateimplementation2.buildPropertyValueTable(map2);
        }
        this.validStates = ImmutableList.copyOf((Collection<? extends IBlockState>)list);
    }
    
    public static <T extends Comparable<T>> String validateProperty(final Block block, final IProperty<T> property) {
        final String s = property.getName();
        if (!BlockStateContainer.NAME_PATTERN.matcher(s).matches()) {
            throw new IllegalArgumentException("Block: " + block.getClass() + " has invalidly named property: " + s);
        }
        for (final T t : property.getAllowedValues()) {
            final String s2 = property.getName(t);
            if (!BlockStateContainer.NAME_PATTERN.matcher(s2).matches()) {
                throw new IllegalArgumentException("Block: " + block.getClass() + " has property: " + s + " with invalidly named value: " + s2);
            }
        }
        return s;
    }
    
    public ImmutableList<IBlockState> getValidStates() {
        return this.validStates;
    }
    
    private List<Iterable<Comparable<?>>> getAllowedValues() {
        final List<Iterable<Comparable<?>>> list = (List<Iterable<Comparable<?>>>)Lists.newArrayList();
        final ImmutableCollection<IProperty<?>> immutablecollection = this.properties.values();
        for (final IProperty<?> iproperty : immutablecollection) {
            list.add((Iterable<Comparable<?>>)iproperty.getAllowedValues());
        }
        return list;
    }
    
    public IBlockState getBaseState() {
        return this.validStates.get(0);
    }
    
    public Block getBlock() {
        return this.block;
    }
    
    public Collection<IProperty<?>> getProperties() {
        return this.properties.values();
    }
    
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("block", Block.REGISTRY.getNameForObject(this.block)).add("properties", Iterables.transform((Iterable<IProperty<?>>)this.properties.values(), (Function<? super IProperty<?>, ?>)BlockStateContainer.GET_NAME_FUNC)).toString();
    }
    
    @Nullable
    public IProperty<?> getProperty(final String propertyName) {
        return this.properties.get(propertyName);
    }
    
    public static class Builder
    {
        private final Block block;
        private final List<IProperty<?>> listed;
        private final List<IUnlistedProperty<?>> unlisted;
        
        public Builder(final Block p_i11_1_) {
            this.listed = (List<IProperty<?>>)Lists.newArrayList();
            this.unlisted = (List<IUnlistedProperty<?>>)Lists.newArrayList();
            this.block = p_i11_1_;
        }
        
        public Builder add(final IProperty<?>... p_add_1_) {
            for (final IProperty<?> iproperty : p_add_1_) {
                this.listed.add(iproperty);
            }
            return this;
        }
        
        public Builder add(final IUnlistedProperty<?>... p_add_1_) {
            for (final IUnlistedProperty<?> iunlistedproperty : p_add_1_) {
                this.unlisted.add(iunlistedproperty);
            }
            return this;
        }
        
        public BlockStateContainer build() {
            IProperty[] iproperty = new IProperty[this.listed.size()];
            iproperty = this.listed.toArray(iproperty);
            if (this.unlisted.size() == 0) {
                return new BlockStateContainer(this.block, (IProperty<?>[])iproperty);
            }
            IUnlistedProperty[] iunlistedproperty = new IUnlistedProperty[this.unlisted.size()];
            iunlistedproperty = this.unlisted.toArray(iunlistedproperty);
            return (BlockStateContainer)Reflector.newInstance(Reflector.ExtendedBlockState_Constructor, this.block, iproperty, iunlistedproperty);
        }
    }
    
    static class StateImplementation extends BlockStateBase
    {
        private final Block block;
        private final ImmutableMap<IProperty<?>, Comparable<?>> properties;
        private ImmutableTable<IProperty<?>, Comparable<?>, IBlockState> propertyValueTable;
        
        private StateImplementation(final Block blockIn, final ImmutableMap<IProperty<?>, Comparable<?>> propertiesIn) {
            this.block = blockIn;
            this.properties = propertiesIn;
        }
        
        protected StateImplementation(final Block p_i8_1_, final ImmutableMap<IProperty<?>, Comparable<?>> p_i8_2_, final ImmutableTable<IProperty<?>, Comparable<?>, IBlockState> p_i8_3_) {
            this.block = p_i8_1_;
            this.properties = p_i8_2_;
            this.propertyValueTable = p_i8_3_;
        }
        
        @Override
        public Collection<IProperty<?>> getPropertyNames() {
            return Collections.unmodifiableCollection((Collection<? extends IProperty<?>>)this.properties.keySet());
        }
        
        @Override
        public <T extends Comparable<T>> T getValue(final IProperty<T> property) {
            final Comparable<?> comparable = this.properties.get(property);
            if (comparable == null) {
                throw new IllegalArgumentException("Cannot get property " + property + " as it does not exist in " + this.block.getBlockState());
            }
            return property.getValueClass().cast(comparable);
        }
        
        @Override
        public <T extends Comparable<T>, V extends T> IBlockState withProperty(final IProperty<T> property, final V value) {
            final Comparable<?> comparable = this.properties.get(property);
            if (comparable == null) {
                throw new IllegalArgumentException("Cannot set property " + property + " as it does not exist in " + this.block.getBlockState());
            }
            if (comparable == value) {
                return this;
            }
            final IBlockState iblockstate = (IBlockState)this.propertyValueTable.get(property, value);
            if (iblockstate == null) {
                throw new IllegalArgumentException("Cannot set property " + property + " to " + value + " on block " + Block.REGISTRY.getNameForObject(this.block) + ", it is not an allowed value");
            }
            return iblockstate;
        }
        
        @Override
        public ImmutableMap<IProperty<?>, Comparable<?>> getProperties() {
            return this.properties;
        }
        
        @Override
        public Block getBlock() {
            return this.block;
        }
        
        @Override
        public boolean equals(final Object p_equals_1_) {
            return this == p_equals_1_;
        }
        
        @Override
        public int hashCode() {
            return this.properties.hashCode();
        }
        
        public void buildPropertyValueTable(final Map<Map<IProperty<?>, Comparable<?>>, StateImplementation> map) {
            if (this.propertyValueTable != null) {
                throw new IllegalStateException();
            }
            final Table<IProperty<?>, Comparable<?>, IBlockState> table = (Table<IProperty<?>, Comparable<?>, IBlockState>)HashBasedTable.create();
            for (final Map.Entry<IProperty<?>, Comparable<?>> entry : this.properties.entrySet()) {
                final IProperty<?> iproperty = entry.getKey();
                for (final Comparable<?> comparable : iproperty.getAllowedValues()) {
                    if (comparable != entry.getValue()) {
                        table.put(iproperty, comparable, map.get(this.getPropertiesWithValue(iproperty, comparable)));
                    }
                }
            }
            this.propertyValueTable = ImmutableTable.copyOf((Table<? extends IProperty<?>, ? extends Comparable<?>, ? extends IBlockState>)table);
        }
        
        private Map<IProperty<?>, Comparable<?>> getPropertiesWithValue(final IProperty<?> property, final Comparable<?> value) {
            final Map<IProperty<?>, Comparable<?>> map = (Map<IProperty<?>, Comparable<?>>)Maps.newHashMap((Map<?, ?>)this.properties);
            map.put(property, value);
            return map;
        }
        
        @Override
        public Material getMaterial() {
            return this.block.getMaterial(this);
        }
        
        @Override
        public boolean isFullBlock() {
            return this.block.isFullBlock(this);
        }
        
        @Override
        public boolean canEntitySpawn(final Entity entityIn) {
            return this.block.canEntitySpawn(this, entityIn);
        }
        
        @Override
        public int getLightOpacity() {
            return this.block.getLightOpacity(this);
        }
        
        @Override
        public int getLightValue() {
            return this.block.getLightValue(this);
        }
        
        @Override
        public boolean isTranslucent() {
            return this.block.isTranslucent(this);
        }
        
        @Override
        public boolean useNeighborBrightness() {
            return this.block.getUseNeighborBrightness(this);
        }
        
        @Override
        public MapColor getMapColor(final IBlockAccess p_185909_1_, final BlockPos p_185909_2_) {
            return this.block.getMapColor(this, p_185909_1_, p_185909_2_);
        }
        
        @Override
        public IBlockState withRotation(final Rotation rot) {
            return this.block.withRotation(this, rot);
        }
        
        @Override
        public IBlockState withMirror(final Mirror mirrorIn) {
            return this.block.withMirror(this, mirrorIn);
        }
        
        @Override
        public boolean isFullCube() {
            return this.block.isFullCube(this);
        }
        
        @Override
        public boolean func_191057_i() {
            return this.block.func_190946_v(this);
        }
        
        @Override
        public EnumBlockRenderType getRenderType() {
            return this.block.getRenderType(this);
        }
        
        @Override
        public int getPackedLightmapCoords(final IBlockAccess source, final BlockPos pos) {
            return this.block.getPackedLightmapCoords(this, source, pos);
        }
        
        @Override
        public float getAmbientOcclusionLightValue() {
            return this.block.getAmbientOcclusionLightValue(this);
        }
        
        @Override
        public boolean isBlockNormalCube() {
            return this.block.isBlockNormalCube(this);
        }
        
        @Override
        public boolean isNormalCube() {
            return this.block.isNormalCube(this);
        }
        
        @Override
        public boolean canProvidePower() {
            return this.block.canProvidePower(this);
        }
        
        @Override
        public int getWeakPower(final IBlockAccess blockAccess, final BlockPos pos, final EnumFacing side) {
            return this.block.getWeakPower(this, blockAccess, pos, side);
        }
        
        @Override
        public boolean hasComparatorInputOverride() {
            return this.block.hasComparatorInputOverride(this);
        }
        
        @Override
        public int getComparatorInputOverride(final World worldIn, final BlockPos pos) {
            return this.block.getComparatorInputOverride(this, worldIn, pos);
        }
        
        @Override
        public float getBlockHardness(final World worldIn, final BlockPos pos) {
            return this.block.getBlockHardness(this, worldIn, pos);
        }
        
        @Override
        public float getPlayerRelativeBlockHardness(final EntityPlayer player, final World worldIn, final BlockPos pos) {
            return this.block.getPlayerRelativeBlockHardness(this, player, worldIn, pos);
        }
        
        @Override
        public int getStrongPower(final IBlockAccess blockAccess, final BlockPos pos, final EnumFacing side) {
            return this.block.getStrongPower(this, blockAccess, pos, side);
        }
        
        @Override
        public EnumPushReaction getMobilityFlag() {
            return this.block.getMobilityFlag(this);
        }
        
        @Override
        public IBlockState getActualState(final IBlockAccess blockAccess, final BlockPos pos) {
            return this.block.getActualState(this, blockAccess, pos);
        }
        
        @Override
        public AxisAlignedBB getSelectedBoundingBox(final World worldIn, final BlockPos pos) {
            return this.block.getSelectedBoundingBox(this, worldIn, pos);
        }
        
        @Override
        public boolean shouldSideBeRendered(final IBlockAccess blockAccess, final BlockPos pos, final EnumFacing facing) {
            return this.block.shouldSideBeRendered(this, blockAccess, pos, facing);
        }
        
        @Override
        public boolean isOpaqueCube() {
            return this.block.isOpaqueCube(this);
        }
        
        @Nullable
        @Override
        public AxisAlignedBB getCollisionBoundingBox(final IBlockAccess worldIn, final BlockPos pos) {
            return this.block.getCollisionBoundingBox(this, worldIn, pos);
        }
        
        @Override
        public void addCollisionBoxToList(final World worldIn, final BlockPos pos, final AxisAlignedBB p_185908_3_, final List<AxisAlignedBB> p_185908_4_, @Nullable final Entity p_185908_5_, final boolean p_185908_6_) {
            this.block.addCollisionBoxToList(this, worldIn, pos, p_185908_3_, p_185908_4_, p_185908_5_, p_185908_6_);
        }
        
        @Override
        public AxisAlignedBB getBoundingBox(final IBlockAccess blockAccess, final BlockPos pos) {
            final Block.EnumOffsetType block$enumoffsettype = this.block.getOffsetType();
            if (block$enumoffsettype != Block.EnumOffsetType.NONE && !(this.block instanceof BlockFlower)) {
                AxisAlignedBB axisalignedbb = this.block.getBoundingBox(this, blockAccess, pos);
                axisalignedbb = BlockModelUtils.getOffsetBoundingBox(axisalignedbb, block$enumoffsettype, pos);
                return axisalignedbb;
            }
            return this.block.getBoundingBox(this, blockAccess, pos);
        }
        
        @Override
        public RayTraceResult collisionRayTrace(final World worldIn, final BlockPos pos, final Vec3d start, final Vec3d end) {
            return this.block.collisionRayTrace(this, worldIn, pos, start, end);
        }
        
        @Override
        public boolean isFullyOpaque() {
            return this.block.isFullyOpaque(this);
        }
        
        @Override
        public Vec3d func_191059_e(final IBlockAccess p_191059_1_, final BlockPos p_191059_2_) {
            return this.block.func_190949_e(this, p_191059_1_, p_191059_2_);
        }
        
        @Override
        public boolean onBlockEventReceived(final World worldIn, final BlockPos pos, final int id, final int param) {
            return this.block.eventReceived(this, worldIn, pos, id, param);
        }
        
        @Override
        public void neighborChanged(final World worldIn, final BlockPos pos, final Block blockIn, final BlockPos p_189546_4_) {
            this.block.neighborChanged(this, worldIn, pos, blockIn, p_189546_4_);
        }
        
        @Override
        public boolean func_191058_s() {
            return this.block.causesSuffocation(this);
        }
        
        @Override
        public ImmutableTable<IProperty<?>, Comparable<?>, IBlockState> getPropertyValueTable() {
            return this.propertyValueTable;
        }
        
        public int getLightOpacity(final IBlockAccess p_getLightOpacity_1_, final BlockPos p_getLightOpacity_2_) {
            return Reflector.callInt(this.block, Reflector.ForgeBlock_getLightOpacity, this, p_getLightOpacity_1_, p_getLightOpacity_2_);
        }
        
        public int getLightValue(final IBlockAccess p_getLightValue_1_, final BlockPos p_getLightValue_2_) {
            return Reflector.callInt(this.block, Reflector.ForgeBlock_getLightValue, this, p_getLightValue_1_, p_getLightValue_2_);
        }
        
        public boolean isSideSolid(final IBlockAccess p_isSideSolid_1_, final BlockPos p_isSideSolid_2_, final EnumFacing p_isSideSolid_3_) {
            return Reflector.callBoolean(this.block, Reflector.ForgeBlock_isSideSolid, this, p_isSideSolid_1_, p_isSideSolid_2_, p_isSideSolid_3_);
        }
        
        public boolean doesSideBlockRendering(final IBlockAccess p_doesSideBlockRendering_1_, final BlockPos p_doesSideBlockRendering_2_, final EnumFacing p_doesSideBlockRendering_3_) {
            return Reflector.callBoolean(this.block, Reflector.ForgeBlock_doesSideBlockRendering, this, p_doesSideBlockRendering_1_, p_doesSideBlockRendering_2_, p_doesSideBlockRendering_3_);
        }
        
        @Override
        public BlockFaceShape func_193401_d(final IBlockAccess p_193401_1_, final BlockPos p_193401_2_, final EnumFacing p_193401_3_) {
            return this.block.func_193383_a(p_193401_1_, this, p_193401_2_, p_193401_3_);
        }
    }
}
