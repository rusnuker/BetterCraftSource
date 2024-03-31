// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.tileentity;

import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import java.util.Iterator;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ContainerShulkerBox;
import net.minecraft.inventory.Container;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.SoundCategory;
import net.minecraft.init.SoundEvents;
import net.minecraft.entity.player.EntityPlayer;
import java.util.List;
import net.minecraft.entity.MoverType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.entity.Entity;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.block.state.IBlockState;
import javax.annotation.Nullable;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.util.ITickable;

public class TileEntityShulkerBox extends TileEntityLockableLoot implements ITickable, ISidedInventory
{
    private static final int[] field_190595_a;
    private NonNullList<ItemStack> field_190596_f;
    private boolean field_190597_g;
    private int field_190598_h;
    private AnimationStatus field_190599_i;
    private float field_190600_j;
    private float field_190601_k;
    private EnumDyeColor field_190602_l;
    private boolean field_190594_p;
    
    static {
        field_190595_a = new int[27];
        for (int i = 0; i < TileEntityShulkerBox.field_190595_a.length; TileEntityShulkerBox.field_190595_a[i] = i++) {}
    }
    
    public TileEntityShulkerBox() {
        this(null);
    }
    
    public TileEntityShulkerBox(@Nullable final EnumDyeColor p_i47242_1_) {
        this.field_190596_f = NonNullList.func_191197_a(27, ItemStack.field_190927_a);
        this.field_190599_i = AnimationStatus.CLOSED;
        this.field_190602_l = p_i47242_1_;
    }
    
    @Override
    public void update() {
        this.func_190583_o();
        if (this.field_190599_i == AnimationStatus.OPENING || this.field_190599_i == AnimationStatus.CLOSING) {
            this.func_190589_G();
        }
    }
    
    protected void func_190583_o() {
        this.field_190601_k = this.field_190600_j;
        switch (this.field_190599_i) {
            case CLOSED: {
                this.field_190600_j = 0.0f;
                break;
            }
            case OPENING: {
                this.field_190600_j += 0.1f;
                if (this.field_190600_j >= 1.0f) {
                    this.func_190589_G();
                    this.field_190599_i = AnimationStatus.OPENED;
                    this.field_190600_j = 1.0f;
                    break;
                }
                break;
            }
            case CLOSING: {
                this.field_190600_j -= 0.1f;
                if (this.field_190600_j <= 0.0f) {
                    this.field_190599_i = AnimationStatus.CLOSED;
                    this.field_190600_j = 0.0f;
                    break;
                }
                break;
            }
            case OPENED: {
                this.field_190600_j = 1.0f;
                break;
            }
        }
    }
    
    public AnimationStatus func_190591_p() {
        return this.field_190599_i;
    }
    
    public AxisAlignedBB func_190584_a(final IBlockState p_190584_1_) {
        return this.func_190587_b(p_190584_1_.getValue(BlockShulkerBox.field_190957_a));
    }
    
    public AxisAlignedBB func_190587_b(final EnumFacing p_190587_1_) {
        return Block.FULL_BLOCK_AABB.addCoord(0.5f * this.func_190585_a(1.0f) * p_190587_1_.getFrontOffsetX(), 0.5f * this.func_190585_a(1.0f) * p_190587_1_.getFrontOffsetY(), 0.5f * this.func_190585_a(1.0f) * p_190587_1_.getFrontOffsetZ());
    }
    
    private AxisAlignedBB func_190588_c(final EnumFacing p_190588_1_) {
        final EnumFacing enumfacing = p_190588_1_.getOpposite();
        return this.func_190587_b(p_190588_1_).func_191195_a(enumfacing.getFrontOffsetX(), enumfacing.getFrontOffsetY(), enumfacing.getFrontOffsetZ());
    }
    
    private void func_190589_G() {
        final IBlockState iblockstate = this.world.getBlockState(this.getPos());
        if (iblockstate.getBlock() instanceof BlockShulkerBox) {
            final EnumFacing enumfacing = iblockstate.getValue(BlockShulkerBox.field_190957_a);
            final AxisAlignedBB axisalignedbb = this.func_190588_c(enumfacing).offset(this.pos);
            final List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(null, axisalignedbb);
            if (!list.isEmpty()) {
                for (int i = 0; i < list.size(); ++i) {
                    final Entity entity = list.get(i);
                    if (entity.getPushReaction() != EnumPushReaction.IGNORE) {
                        double d0 = 0.0;
                        double d2 = 0.0;
                        double d3 = 0.0;
                        final AxisAlignedBB axisalignedbb2 = entity.getEntityBoundingBox();
                        switch (enumfacing.getAxis()) {
                            case X: {
                                if (enumfacing.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE) {
                                    d0 = axisalignedbb.maxX - axisalignedbb2.minX;
                                }
                                else {
                                    d0 = axisalignedbb2.maxX - axisalignedbb.minX;
                                }
                                d0 += 0.01;
                                break;
                            }
                            case Y: {
                                if (enumfacing.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE) {
                                    d2 = axisalignedbb.maxY - axisalignedbb2.minY;
                                }
                                else {
                                    d2 = axisalignedbb2.maxY - axisalignedbb.minY;
                                }
                                d2 += 0.01;
                                break;
                            }
                            case Z: {
                                if (enumfacing.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE) {
                                    d3 = axisalignedbb.maxZ - axisalignedbb2.minZ;
                                }
                                else {
                                    d3 = axisalignedbb2.maxZ - axisalignedbb.minZ;
                                }
                                d3 += 0.01;
                                break;
                            }
                        }
                        entity.moveEntity(MoverType.SHULKER_BOX, d0 * enumfacing.getFrontOffsetX(), d2 * enumfacing.getFrontOffsetY(), d3 * enumfacing.getFrontOffsetZ());
                    }
                }
            }
        }
    }
    
    @Override
    public int getSizeInventory() {
        return this.field_190596_f.size();
    }
    
    @Override
    public int getInventoryStackLimit() {
        return 64;
    }
    
    @Override
    public boolean receiveClientEvent(final int id, final int type) {
        if (id == 1) {
            if ((this.field_190598_h = type) == 0) {
                this.field_190599_i = AnimationStatus.CLOSING;
            }
            if (type == 1) {
                this.field_190599_i = AnimationStatus.OPENING;
            }
            return true;
        }
        return super.receiveClientEvent(id, type);
    }
    
    @Override
    public void openInventory(final EntityPlayer player) {
        if (!player.isSpectator()) {
            if (this.field_190598_h < 0) {
                this.field_190598_h = 0;
            }
            ++this.field_190598_h;
            this.world.addBlockEvent(this.pos, this.getBlockType(), 1, this.field_190598_h);
            if (this.field_190598_h == 1) {
                this.world.playSound(null, this.pos, SoundEvents.field_191262_fB, SoundCategory.BLOCKS, 0.5f, this.world.rand.nextFloat() * 0.1f + 0.9f);
            }
        }
    }
    
    @Override
    public void closeInventory(final EntityPlayer player) {
        if (!player.isSpectator()) {
            --this.field_190598_h;
            this.world.addBlockEvent(this.pos, this.getBlockType(), 1, this.field_190598_h);
            if (this.field_190598_h <= 0) {
                this.world.playSound(null, this.pos, SoundEvents.field_191261_fA, SoundCategory.BLOCKS, 0.5f, this.world.rand.nextFloat() * 0.1f + 0.9f);
            }
        }
    }
    
    @Override
    public Container createContainer(final InventoryPlayer playerInventory, final EntityPlayer playerIn) {
        return new ContainerShulkerBox(playerInventory, this, playerIn);
    }
    
    @Override
    public String getGuiID() {
        return "minecraft:shulker_box";
    }
    
    @Override
    public String getName() {
        return this.hasCustomName() ? this.field_190577_o : "container.shulkerBox";
    }
    
    public static void func_190593_a(final DataFixer p_190593_0_) {
        p_190593_0_.registerWalker(FixTypes.BLOCK_ENTITY, new ItemStackDataLists(TileEntityShulkerBox.class, new String[] { "Items" }));
    }
    
    @Override
    public void readFromNBT(final NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.func_190586_e(compound);
    }
    
    @Override
    public NBTTagCompound writeToNBT(final NBTTagCompound compound) {
        super.writeToNBT(compound);
        return this.func_190580_f(compound);
    }
    
    public void func_190586_e(final NBTTagCompound p_190586_1_) {
        this.field_190596_f = NonNullList.func_191197_a(this.getSizeInventory(), ItemStack.field_190927_a);
        if (!this.checkLootAndRead(p_190586_1_) && p_190586_1_.hasKey("Items", 9)) {
            ItemStackHelper.func_191283_b(p_190586_1_, this.field_190596_f);
        }
        if (p_190586_1_.hasKey("CustomName", 8)) {
            this.field_190577_o = p_190586_1_.getString("CustomName");
        }
    }
    
    public NBTTagCompound func_190580_f(final NBTTagCompound p_190580_1_) {
        if (!this.checkLootAndWrite(p_190580_1_)) {
            ItemStackHelper.func_191281_a(p_190580_1_, this.field_190596_f, false);
        }
        if (this.hasCustomName()) {
            p_190580_1_.setString("CustomName", this.field_190577_o);
        }
        if (!p_190580_1_.hasKey("Lock") && this.isLocked()) {
            this.getLockCode().toNBT(p_190580_1_);
        }
        return p_190580_1_;
    }
    
    @Override
    protected NonNullList<ItemStack> func_190576_q() {
        return this.field_190596_f;
    }
    
    @Override
    public boolean func_191420_l() {
        for (final ItemStack itemstack : this.field_190596_f) {
            if (!itemstack.func_190926_b()) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public int[] getSlotsForFace(final EnumFacing side) {
        return TileEntityShulkerBox.field_190595_a;
    }
    
    @Override
    public boolean canInsertItem(final int index, final ItemStack itemStackIn, final EnumFacing direction) {
        return !(Block.getBlockFromItem(itemStackIn.getItem()) instanceof BlockShulkerBox);
    }
    
    @Override
    public boolean canExtractItem(final int index, final ItemStack stack, final EnumFacing direction) {
        return true;
    }
    
    @Override
    public void clear() {
        this.field_190597_g = true;
        super.clear();
    }
    
    public boolean func_190590_r() {
        return this.field_190597_g;
    }
    
    public float func_190585_a(final float p_190585_1_) {
        return this.field_190601_k + (this.field_190600_j - this.field_190601_k) * p_190585_1_;
    }
    
    public EnumDyeColor func_190592_s() {
        if (this.field_190602_l == null) {
            this.field_190602_l = BlockShulkerBox.func_190954_c(this.getBlockType());
        }
        return this.field_190602_l;
    }
    
    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos, 10, this.getUpdateTag());
    }
    
    public boolean func_190581_E() {
        return this.field_190594_p;
    }
    
    public void func_190579_a(final boolean p_190579_1_) {
        this.field_190594_p = p_190579_1_;
    }
    
    public boolean func_190582_F() {
        return !this.func_190581_E() || !this.func_191420_l() || this.hasCustomName() || this.lootTable != null;
    }
    
    public enum AnimationStatus
    {
        CLOSED("CLOSED", 0), 
        OPENING("OPENING", 1), 
        OPENED("OPENED", 2), 
        CLOSING("CLOSING", 3);
        
        private AnimationStatus(final String s, final int n) {
        }
    }
}
