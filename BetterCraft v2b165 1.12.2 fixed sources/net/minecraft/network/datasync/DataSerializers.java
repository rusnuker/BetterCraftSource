// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.network.datasync;

import javax.annotation.Nullable;
import net.minecraft.block.Block;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.nbt.NBTTagCompound;
import java.util.UUID;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Rotations;
import net.minecraft.block.state.IBlockState;
import com.google.common.base.Optional;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.IntIdentityHashBiMap;

public class DataSerializers
{
    private static final IntIdentityHashBiMap<DataSerializer<?>> REGISTRY;
    public static final DataSerializer<Byte> BYTE;
    public static final DataSerializer<Integer> VARINT;
    public static final DataSerializer<Float> FLOAT;
    public static final DataSerializer<String> STRING;
    public static final DataSerializer<ITextComponent> TEXT_COMPONENT;
    public static final DataSerializer<ItemStack> OPTIONAL_ITEM_STACK;
    public static final DataSerializer<Optional<IBlockState>> OPTIONAL_BLOCK_STATE;
    public static final DataSerializer<Boolean> BOOLEAN;
    public static final DataSerializer<Rotations> ROTATIONS;
    public static final DataSerializer<BlockPos> BLOCK_POS;
    public static final DataSerializer<Optional<BlockPos>> OPTIONAL_BLOCK_POS;
    public static final DataSerializer<EnumFacing> FACING;
    public static final DataSerializer<Optional<UUID>> OPTIONAL_UNIQUE_ID;
    public static final DataSerializer<NBTTagCompound> field_192734_n;
    
    static {
        REGISTRY = new IntIdentityHashBiMap<DataSerializer<?>>(16);
        BYTE = new DataSerializer<Byte>() {
            @Override
            public void write(final PacketBuffer buf, final Byte value) {
                buf.writeByte(value);
            }
            
            @Override
            public Byte read(final PacketBuffer buf) throws IOException {
                return buf.readByte();
            }
            
            @Override
            public DataParameter<Byte> createKey(final int id) {
                return new DataParameter<Byte>(id, this);
            }
            
            @Override
            public Byte func_192717_a(final Byte p_192717_1_) {
                return p_192717_1_;
            }
        };
        VARINT = new DataSerializer<Integer>() {
            @Override
            public void write(final PacketBuffer buf, final Integer value) {
                buf.writeVarIntToBuffer(value);
            }
            
            @Override
            public Integer read(final PacketBuffer buf) throws IOException {
                return buf.readVarIntFromBuffer();
            }
            
            @Override
            public DataParameter<Integer> createKey(final int id) {
                return new DataParameter<Integer>(id, this);
            }
            
            @Override
            public Integer func_192717_a(final Integer p_192717_1_) {
                return p_192717_1_;
            }
        };
        FLOAT = new DataSerializer<Float>() {
            @Override
            public void write(final PacketBuffer buf, final Float value) {
                buf.writeFloat(value);
            }
            
            @Override
            public Float read(final PacketBuffer buf) throws IOException {
                return buf.readFloat();
            }
            
            @Override
            public DataParameter<Float> createKey(final int id) {
                return new DataParameter<Float>(id, this);
            }
            
            @Override
            public Float func_192717_a(final Float p_192717_1_) {
                return p_192717_1_;
            }
        };
        STRING = new DataSerializer<String>() {
            @Override
            public void write(final PacketBuffer buf, final String value) {
                buf.writeString(value);
            }
            
            @Override
            public String read(final PacketBuffer buf) throws IOException {
                return buf.readStringFromBuffer(32767);
            }
            
            @Override
            public DataParameter<String> createKey(final int id) {
                return new DataParameter<String>(id, this);
            }
            
            @Override
            public String func_192717_a(final String p_192717_1_) {
                return p_192717_1_;
            }
        };
        TEXT_COMPONENT = new DataSerializer<ITextComponent>() {
            @Override
            public void write(final PacketBuffer buf, final ITextComponent value) {
                buf.writeTextComponent(value);
            }
            
            @Override
            public ITextComponent read(final PacketBuffer buf) throws IOException {
                return buf.readTextComponent();
            }
            
            @Override
            public DataParameter<ITextComponent> createKey(final int id) {
                return new DataParameter<ITextComponent>(id, this);
            }
            
            @Override
            public ITextComponent func_192717_a(final ITextComponent p_192717_1_) {
                return p_192717_1_.createCopy();
            }
        };
        OPTIONAL_ITEM_STACK = new DataSerializer<ItemStack>() {
            @Override
            public void write(final PacketBuffer buf, final ItemStack value) {
                buf.writeItemStackToBuffer(value);
            }
            
            @Override
            public ItemStack read(final PacketBuffer buf) throws IOException {
                return buf.readItemStackFromBuffer();
            }
            
            @Override
            public DataParameter<ItemStack> createKey(final int id) {
                return new DataParameter<ItemStack>(id, this);
            }
            
            @Override
            public ItemStack func_192717_a(final ItemStack p_192717_1_) {
                return p_192717_1_.copy();
            }
        };
        OPTIONAL_BLOCK_STATE = new DataSerializer<Optional<IBlockState>>() {
            @Override
            public void write(final PacketBuffer buf, final Optional<IBlockState> value) {
                if (value.isPresent()) {
                    buf.writeVarIntToBuffer(Block.getStateId(value.get()));
                }
                else {
                    buf.writeVarIntToBuffer(0);
                }
            }
            
            @Override
            public Optional<IBlockState> read(final PacketBuffer buf) throws IOException {
                final int i = buf.readVarIntFromBuffer();
                return (i == 0) ? Optional.absent() : Optional.of(Block.getStateById(i));
            }
            
            @Override
            public DataParameter<Optional<IBlockState>> createKey(final int id) {
                return new DataParameter<Optional<IBlockState>>(id, this);
            }
            
            @Override
            public Optional<IBlockState> func_192717_a(final Optional<IBlockState> p_192717_1_) {
                return p_192717_1_;
            }
        };
        BOOLEAN = new DataSerializer<Boolean>() {
            @Override
            public void write(final PacketBuffer buf, final Boolean value) {
                buf.writeBoolean(value);
            }
            
            @Override
            public Boolean read(final PacketBuffer buf) throws IOException {
                return buf.readBoolean();
            }
            
            @Override
            public DataParameter<Boolean> createKey(final int id) {
                return new DataParameter<Boolean>(id, this);
            }
            
            @Override
            public Boolean func_192717_a(final Boolean p_192717_1_) {
                return p_192717_1_;
            }
        };
        ROTATIONS = new DataSerializer<Rotations>() {
            @Override
            public void write(final PacketBuffer buf, final Rotations value) {
                buf.writeFloat(value.getX());
                buf.writeFloat(value.getY());
                buf.writeFloat(value.getZ());
            }
            
            @Override
            public Rotations read(final PacketBuffer buf) throws IOException {
                return new Rotations(buf.readFloat(), buf.readFloat(), buf.readFloat());
            }
            
            @Override
            public DataParameter<Rotations> createKey(final int id) {
                return new DataParameter<Rotations>(id, this);
            }
            
            @Override
            public Rotations func_192717_a(final Rotations p_192717_1_) {
                return p_192717_1_;
            }
        };
        BLOCK_POS = new DataSerializer<BlockPos>() {
            @Override
            public void write(final PacketBuffer buf, final BlockPos value) {
                buf.writeBlockPos(value);
            }
            
            @Override
            public BlockPos read(final PacketBuffer buf) throws IOException {
                return buf.readBlockPos();
            }
            
            @Override
            public DataParameter<BlockPos> createKey(final int id) {
                return new DataParameter<BlockPos>(id, this);
            }
            
            @Override
            public BlockPos func_192717_a(final BlockPos p_192717_1_) {
                return p_192717_1_;
            }
        };
        OPTIONAL_BLOCK_POS = new DataSerializer<Optional<BlockPos>>() {
            @Override
            public void write(final PacketBuffer buf, final Optional<BlockPos> value) {
                buf.writeBoolean(value.isPresent());
                if (value.isPresent()) {
                    buf.writeBlockPos(value.get());
                }
            }
            
            @Override
            public Optional<BlockPos> read(final PacketBuffer buf) throws IOException {
                return buf.readBoolean() ? Optional.of(buf.readBlockPos()) : Optional.absent();
            }
            
            @Override
            public DataParameter<Optional<BlockPos>> createKey(final int id) {
                return new DataParameter<Optional<BlockPos>>(id, this);
            }
            
            @Override
            public Optional<BlockPos> func_192717_a(final Optional<BlockPos> p_192717_1_) {
                return p_192717_1_;
            }
        };
        FACING = new DataSerializer<EnumFacing>() {
            @Override
            public void write(final PacketBuffer buf, final EnumFacing value) {
                buf.writeEnumValue(value);
            }
            
            @Override
            public EnumFacing read(final PacketBuffer buf) throws IOException {
                return buf.readEnumValue(EnumFacing.class);
            }
            
            @Override
            public DataParameter<EnumFacing> createKey(final int id) {
                return new DataParameter<EnumFacing>(id, this);
            }
            
            @Override
            public EnumFacing func_192717_a(final EnumFacing p_192717_1_) {
                return p_192717_1_;
            }
        };
        OPTIONAL_UNIQUE_ID = new DataSerializer<Optional<UUID>>() {
            @Override
            public void write(final PacketBuffer buf, final Optional<UUID> value) {
                buf.writeBoolean(value.isPresent());
                if (value.isPresent()) {
                    buf.writeUuid(value.get());
                }
            }
            
            @Override
            public Optional<UUID> read(final PacketBuffer buf) throws IOException {
                return buf.readBoolean() ? Optional.of(buf.readUuid()) : Optional.absent();
            }
            
            @Override
            public DataParameter<Optional<UUID>> createKey(final int id) {
                return new DataParameter<Optional<UUID>>(id, this);
            }
            
            @Override
            public Optional<UUID> func_192717_a(final Optional<UUID> p_192717_1_) {
                return p_192717_1_;
            }
        };
        field_192734_n = new DataSerializer<NBTTagCompound>() {
            @Override
            public void write(final PacketBuffer buf, final NBTTagCompound value) {
                buf.writeNBTTagCompoundToBuffer(value);
            }
            
            @Override
            public NBTTagCompound read(final PacketBuffer buf) throws IOException {
                return buf.readNBTTagCompoundFromBuffer();
            }
            
            @Override
            public DataParameter<NBTTagCompound> createKey(final int id) {
                return new DataParameter<NBTTagCompound>(id, this);
            }
            
            @Override
            public NBTTagCompound func_192717_a(final NBTTagCompound p_192717_1_) {
                return p_192717_1_.copy();
            }
        };
        registerSerializer(DataSerializers.BYTE);
        registerSerializer(DataSerializers.VARINT);
        registerSerializer(DataSerializers.FLOAT);
        registerSerializer(DataSerializers.STRING);
        registerSerializer(DataSerializers.TEXT_COMPONENT);
        registerSerializer(DataSerializers.OPTIONAL_ITEM_STACK);
        registerSerializer(DataSerializers.BOOLEAN);
        registerSerializer(DataSerializers.ROTATIONS);
        registerSerializer(DataSerializers.BLOCK_POS);
        registerSerializer(DataSerializers.OPTIONAL_BLOCK_POS);
        registerSerializer(DataSerializers.FACING);
        registerSerializer(DataSerializers.OPTIONAL_UNIQUE_ID);
        registerSerializer(DataSerializers.OPTIONAL_BLOCK_STATE);
        registerSerializer(DataSerializers.field_192734_n);
    }
    
    public static void registerSerializer(final DataSerializer<?> serializer) {
        DataSerializers.REGISTRY.add(serializer);
    }
    
    @Nullable
    public static DataSerializer<?> getSerializer(final int id) {
        return DataSerializers.REGISTRY.get(id);
    }
    
    public static int getSerializerId(final DataSerializer<?> serializer) {
        return DataSerializers.REGISTRY.getId(serializer);
    }
}
