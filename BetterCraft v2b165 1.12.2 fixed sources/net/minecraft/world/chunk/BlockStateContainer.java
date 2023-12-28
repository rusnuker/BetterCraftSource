// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.world.chunk;

import javax.annotation.Nullable;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BitArray;
import net.minecraft.block.state.IBlockState;

public class BlockStateContainer implements IBlockStatePaletteResizer
{
    private static final IBlockStatePalette REGISTRY_BASED_PALETTE;
    protected static final IBlockState AIR_BLOCK_STATE;
    protected BitArray storage;
    protected IBlockStatePalette palette;
    private int bits;
    
    static {
        REGISTRY_BASED_PALETTE = new BlockStatePaletteRegistry();
        AIR_BLOCK_STATE = Blocks.AIR.getDefaultState();
    }
    
    public BlockStateContainer() {
        this.setBits(4);
    }
    
    private static int getIndex(final int x, final int y, final int z) {
        return y << 8 | z << 4 | x;
    }
    
    private void setBits(final int bitsIn) {
        if (bitsIn != this.bits) {
            this.bits = bitsIn;
            if (this.bits <= 4) {
                this.bits = 4;
                this.palette = new BlockStatePaletteLinear(this.bits, this);
            }
            else if (this.bits <= 8) {
                this.palette = new BlockStatePaletteHashMap(this.bits, this);
            }
            else {
                this.palette = BlockStateContainer.REGISTRY_BASED_PALETTE;
                this.bits = MathHelper.log2DeBruijn(Block.BLOCK_STATE_IDS.size());
            }
            this.palette.idFor(BlockStateContainer.AIR_BLOCK_STATE);
            this.storage = new BitArray(this.bits, 4096);
        }
    }
    
    @Override
    public int onResize(final int p_186008_1_, final IBlockState state) {
        final BitArray bitarray = this.storage;
        final IBlockStatePalette iblockstatepalette = this.palette;
        this.setBits(p_186008_1_);
        for (int i = 0; i < bitarray.size(); ++i) {
            final IBlockState iblockstate = iblockstatepalette.getBlockState(bitarray.getAt(i));
            if (iblockstate != null) {
                this.set(i, iblockstate);
            }
        }
        return this.palette.idFor(state);
    }
    
    public void set(final int x, final int y, final int z, final IBlockState state) {
        this.set(getIndex(x, y, z), state);
    }
    
    protected void set(final int index, final IBlockState state) {
        final int i = this.palette.idFor(state);
        this.storage.setAt(index, i);
    }
    
    public IBlockState get(final int x, final int y, final int z) {
        return this.get(getIndex(x, y, z));
    }
    
    protected IBlockState get(final int index) {
        final IBlockState iblockstate = this.palette.getBlockState(this.storage.getAt(index));
        return (iblockstate == null) ? BlockStateContainer.AIR_BLOCK_STATE : iblockstate;
    }
    
    public void read(final PacketBuffer buf) {
        final int i = buf.readByte();
        if (this.bits != i) {
            this.setBits(i);
        }
        this.palette.read(buf);
        buf.readLongArray(this.storage.getBackingLongArray());
    }
    
    public void write(final PacketBuffer buf) {
        buf.writeByte(this.bits);
        this.palette.write(buf);
        buf.writeLongArray(this.storage.getBackingLongArray());
    }
    
    @Nullable
    public NibbleArray getDataForNBT(final byte[] p_186017_1_, final NibbleArray p_186017_2_) {
        NibbleArray nibblearray = null;
        for (int i = 0; i < 4096; ++i) {
            final int j = Block.BLOCK_STATE_IDS.get(this.get(i));
            final int k = i & 0xF;
            final int l = i >> 8 & 0xF;
            final int i2 = i >> 4 & 0xF;
            if ((j >> 12 & 0xF) != 0x0) {
                if (nibblearray == null) {
                    nibblearray = new NibbleArray();
                }
                nibblearray.set(k, l, i2, j >> 12 & 0xF);
            }
            p_186017_1_[i] = (byte)(j >> 4 & 0xFF);
            p_186017_2_.set(k, l, i2, j & 0xF);
        }
        return nibblearray;
    }
    
    public void setDataFromNBT(final byte[] p_186019_1_, final NibbleArray p_186019_2_, @Nullable final NibbleArray p_186019_3_) {
        for (int i = 0; i < 4096; ++i) {
            final int j = i & 0xF;
            final int k = i >> 8 & 0xF;
            final int l = i >> 4 & 0xF;
            final int i2 = (p_186019_3_ == null) ? 0 : p_186019_3_.get(j, k, l);
            final int j2 = i2 << 12 | (p_186019_1_[i] & 0xFF) << 4 | p_186019_2_.get(j, k, l);
            this.set(i, Block.BLOCK_STATE_IDS.getByValue(j2));
        }
    }
    
    public int getSerializedSize() {
        return 1 + this.palette.getSerializedState() + PacketBuffer.getVarIntSize(this.storage.size()) + this.storage.getBackingLongArray().length * 8;
    }
}
