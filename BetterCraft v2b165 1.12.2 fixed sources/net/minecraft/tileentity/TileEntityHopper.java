// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.tileentity;

import net.minecraft.inventory.ContainerHopper;
import net.minecraft.inventory.Container;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.block.BlockChest;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import com.google.common.base.Predicate;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import javax.annotation.Nullable;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.util.EnumFacing;
import net.minecraft.inventory.IInventory;
import java.util.Iterator;
import net.minecraft.block.BlockHopper;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.datafix.walkers.ItemStackDataLists;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ITickable;

public class TileEntityHopper extends TileEntityLockableLoot implements IHopper, ITickable
{
    private NonNullList<ItemStack> inventory;
    private int transferCooldown;
    private long field_190578_g;
    
    public TileEntityHopper() {
        this.inventory = NonNullList.func_191197_a(5, ItemStack.field_190927_a);
        this.transferCooldown = -1;
    }
    
    public static void registerFixesHopper(final DataFixer fixer) {
        fixer.registerWalker(FixTypes.BLOCK_ENTITY, new ItemStackDataLists(TileEntityHopper.class, new String[] { "Items" }));
    }
    
    @Override
    public void readFromNBT(final NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.inventory = NonNullList.func_191197_a(this.getSizeInventory(), ItemStack.field_190927_a);
        if (!this.checkLootAndRead(compound)) {
            ItemStackHelper.func_191283_b(compound, this.inventory);
        }
        if (compound.hasKey("CustomName", 8)) {
            this.field_190577_o = compound.getString("CustomName");
        }
        this.transferCooldown = compound.getInteger("TransferCooldown");
    }
    
    @Override
    public NBTTagCompound writeToNBT(final NBTTagCompound compound) {
        super.writeToNBT(compound);
        if (!this.checkLootAndWrite(compound)) {
            ItemStackHelper.func_191282_a(compound, this.inventory);
        }
        compound.setInteger("TransferCooldown", this.transferCooldown);
        if (this.hasCustomName()) {
            compound.setString("CustomName", this.field_190577_o);
        }
        return compound;
    }
    
    @Override
    public int getSizeInventory() {
        return this.inventory.size();
    }
    
    @Override
    public ItemStack decrStackSize(final int index, final int count) {
        this.fillWithLoot(null);
        final ItemStack itemstack = ItemStackHelper.getAndSplit(this.func_190576_q(), index, count);
        return itemstack;
    }
    
    @Override
    public void setInventorySlotContents(final int index, final ItemStack stack) {
        this.fillWithLoot(null);
        this.func_190576_q().set(index, stack);
        if (stack.func_190916_E() > this.getInventoryStackLimit()) {
            stack.func_190920_e(this.getInventoryStackLimit());
        }
    }
    
    @Override
    public String getName() {
        return this.hasCustomName() ? this.field_190577_o : "container.hopper";
    }
    
    @Override
    public int getInventoryStackLimit() {
        return 64;
    }
    
    @Override
    public void update() {
        if (this.world != null && !this.world.isRemote) {
            --this.transferCooldown;
            this.field_190578_g = this.world.getTotalWorldTime();
            if (!this.isOnTransferCooldown()) {
                this.setTransferCooldown(0);
                this.updateHopper();
            }
        }
    }
    
    private boolean updateHopper() {
        if (this.world != null && !this.world.isRemote) {
            if (!this.isOnTransferCooldown() && BlockHopper.isEnabled(this.getBlockMetadata())) {
                boolean flag = false;
                if (!this.isEmpty()) {
                    flag = this.transferItemsOut();
                }
                if (!this.isFull()) {
                    flag = (captureDroppedItems(this) || flag);
                }
                if (flag) {
                    this.setTransferCooldown(8);
                    this.markDirty();
                    return true;
                }
            }
            return false;
        }
        return false;
    }
    
    private boolean isEmpty() {
        for (final ItemStack itemstack : this.inventory) {
            if (!itemstack.func_190926_b()) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean func_191420_l() {
        return this.isEmpty();
    }
    
    private boolean isFull() {
        for (final ItemStack itemstack : this.inventory) {
            if (itemstack.func_190926_b() || itemstack.func_190916_E() != itemstack.getMaxStackSize()) {
                return false;
            }
        }
        return true;
    }
    
    private boolean transferItemsOut() {
        final IInventory iinventory = this.getInventoryForHopperTransfer();
        if (iinventory == null) {
            return false;
        }
        final EnumFacing enumfacing = BlockHopper.getFacing(this.getBlockMetadata()).getOpposite();
        if (this.isInventoryFull(iinventory, enumfacing)) {
            return false;
        }
        for (int i = 0; i < this.getSizeInventory(); ++i) {
            if (!this.getStackInSlot(i).func_190926_b()) {
                final ItemStack itemstack = this.getStackInSlot(i).copy();
                final ItemStack itemstack2 = putStackInInventoryAllSlots(this, iinventory, this.decrStackSize(i, 1), enumfacing);
                if (itemstack2.func_190926_b()) {
                    iinventory.markDirty();
                    return true;
                }
                this.setInventorySlotContents(i, itemstack);
            }
        }
        return false;
    }
    
    private boolean isInventoryFull(final IInventory inventoryIn, final EnumFacing side) {
        if (inventoryIn instanceof ISidedInventory) {
            final ISidedInventory isidedinventory = (ISidedInventory)inventoryIn;
            final int[] aint = isidedinventory.getSlotsForFace(side);
            int[] array;
            for (int length = (array = aint).length, l = 0; l < length; ++l) {
                final int k = array[l];
                final ItemStack itemstack1 = isidedinventory.getStackInSlot(k);
                if (itemstack1.func_190926_b() || itemstack1.func_190916_E() != itemstack1.getMaxStackSize()) {
                    return false;
                }
            }
        }
        else {
            for (int i = inventoryIn.getSizeInventory(), j = 0; j < i; ++j) {
                final ItemStack itemstack2 = inventoryIn.getStackInSlot(j);
                if (itemstack2.func_190926_b() || itemstack2.func_190916_E() != itemstack2.getMaxStackSize()) {
                    return false;
                }
            }
        }
        return true;
    }
    
    private static boolean isInventoryEmpty(final IInventory inventoryIn, final EnumFacing side) {
        if (inventoryIn instanceof ISidedInventory) {
            final ISidedInventory isidedinventory = (ISidedInventory)inventoryIn;
            final int[] aint = isidedinventory.getSlotsForFace(side);
            int[] array;
            for (int length = (array = aint).length, l = 0; l < length; ++l) {
                final int i = array[l];
                if (!isidedinventory.getStackInSlot(i).func_190926_b()) {
                    return false;
                }
            }
        }
        else {
            for (int j = inventoryIn.getSizeInventory(), k = 0; k < j; ++k) {
                if (!inventoryIn.getStackInSlot(k).func_190926_b()) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public static boolean captureDroppedItems(final IHopper hopper) {
        final IInventory iinventory = getHopperInventory(hopper);
        if (iinventory != null) {
            final EnumFacing enumfacing = EnumFacing.DOWN;
            if (isInventoryEmpty(iinventory, enumfacing)) {
                return false;
            }
            if (iinventory instanceof ISidedInventory) {
                final ISidedInventory isidedinventory = (ISidedInventory)iinventory;
                final int[] aint = isidedinventory.getSlotsForFace(enumfacing);
                int[] array;
                for (int length = (array = aint).length, l = 0; l < length; ++l) {
                    final int i = array[l];
                    if (pullItemFromSlot(hopper, iinventory, i, enumfacing)) {
                        return true;
                    }
                }
            }
            else {
                for (int j = iinventory.getSizeInventory(), k = 0; k < j; ++k) {
                    if (pullItemFromSlot(hopper, iinventory, k, enumfacing)) {
                        return true;
                    }
                }
            }
        }
        else {
            for (final EntityItem entityitem : getCaptureItems(hopper.getWorld(), hopper.getXPos(), hopper.getYPos(), hopper.getZPos())) {
                if (putDropInInventoryAllSlots(null, hopper, entityitem)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private static boolean pullItemFromSlot(final IHopper hopper, final IInventory inventoryIn, final int index, final EnumFacing direction) {
        final ItemStack itemstack = inventoryIn.getStackInSlot(index);
        if (!itemstack.func_190926_b() && canExtractItemFromSlot(inventoryIn, itemstack, index, direction)) {
            final ItemStack itemstack2 = itemstack.copy();
            final ItemStack itemstack3 = putStackInInventoryAllSlots(inventoryIn, hopper, inventoryIn.decrStackSize(index, 1), null);
            if (itemstack3.func_190926_b()) {
                inventoryIn.markDirty();
                return true;
            }
            inventoryIn.setInventorySlotContents(index, itemstack2);
        }
        return false;
    }
    
    public static boolean putDropInInventoryAllSlots(final IInventory p_145898_0_, final IInventory itemIn, final EntityItem p_145898_2_) {
        boolean flag = false;
        if (p_145898_2_ == null) {
            return false;
        }
        final ItemStack itemstack = p_145898_2_.getEntityItem().copy();
        final ItemStack itemstack2 = putStackInInventoryAllSlots(p_145898_0_, itemIn, itemstack, null);
        if (itemstack2.func_190926_b()) {
            flag = true;
            p_145898_2_.setDead();
        }
        else {
            p_145898_2_.setEntityItemStack(itemstack2);
        }
        return flag;
    }
    
    public static ItemStack putStackInInventoryAllSlots(final IInventory inventoryIn, final IInventory stack, ItemStack side, @Nullable final EnumFacing p_174918_3_) {
        if (stack instanceof ISidedInventory && p_174918_3_ != null) {
            final ISidedInventory isidedinventory = (ISidedInventory)stack;
            final int[] aint = isidedinventory.getSlotsForFace(p_174918_3_);
            for (int k = 0; k < aint.length; ++k) {
                if (side.func_190926_b()) {
                    break;
                }
                side = insertStack(inventoryIn, stack, side, aint[k], p_174918_3_);
            }
        }
        else {
            for (int i = stack.getSizeInventory(), j = 0; j < i && !side.func_190926_b(); side = insertStack(inventoryIn, stack, side, j, p_174918_3_), ++j) {}
        }
        return side;
    }
    
    private static boolean canInsertItemInSlot(final IInventory inventoryIn, final ItemStack stack, final int index, final EnumFacing side) {
        return inventoryIn.isItemValidForSlot(index, stack) && (!(inventoryIn instanceof ISidedInventory) || ((ISidedInventory)inventoryIn).canInsertItem(index, stack, side));
    }
    
    private static boolean canExtractItemFromSlot(final IInventory inventoryIn, final ItemStack stack, final int index, final EnumFacing side) {
        return !(inventoryIn instanceof ISidedInventory) || ((ISidedInventory)inventoryIn).canExtractItem(index, stack, side);
    }
    
    private static ItemStack insertStack(final IInventory inventoryIn, final IInventory stack, ItemStack index, final int side, final EnumFacing p_174916_4_) {
        final ItemStack itemstack = stack.getStackInSlot(side);
        if (canInsertItemInSlot(stack, index, side, p_174916_4_)) {
            boolean flag = false;
            final boolean flag2 = stack.func_191420_l();
            if (itemstack.func_190926_b()) {
                stack.setInventorySlotContents(side, index);
                index = ItemStack.field_190927_a;
                flag = true;
            }
            else if (canCombine(itemstack, index)) {
                final int i = index.getMaxStackSize() - itemstack.func_190916_E();
                final int j = Math.min(index.func_190916_E(), i);
                index.func_190918_g(j);
                itemstack.func_190917_f(j);
                flag = (j > 0);
            }
            if (flag) {
                if (flag2 && stack instanceof TileEntityHopper) {
                    final TileEntityHopper tileentityhopper1 = (TileEntityHopper)stack;
                    if (!tileentityhopper1.mayTransfer()) {
                        int k = 0;
                        if (inventoryIn != null && inventoryIn instanceof TileEntityHopper) {
                            final TileEntityHopper tileentityhopper2 = (TileEntityHopper)inventoryIn;
                            if (tileentityhopper1.field_190578_g >= tileentityhopper2.field_190578_g) {
                                k = 1;
                            }
                        }
                        tileentityhopper1.setTransferCooldown(8 - k);
                    }
                }
                stack.markDirty();
            }
        }
        return index;
    }
    
    private IInventory getInventoryForHopperTransfer() {
        final EnumFacing enumfacing = BlockHopper.getFacing(this.getBlockMetadata());
        return getInventoryAtPosition(this.getWorld(), this.getXPos() + enumfacing.getFrontOffsetX(), this.getYPos() + enumfacing.getFrontOffsetY(), this.getZPos() + enumfacing.getFrontOffsetZ());
    }
    
    public static IInventory getHopperInventory(final IHopper hopper) {
        return getInventoryAtPosition(hopper.getWorld(), hopper.getXPos(), hopper.getYPos() + 1.0, hopper.getZPos());
    }
    
    public static List<EntityItem> getCaptureItems(final World worldIn, final double p_184292_1_, final double p_184292_3_, final double p_184292_5_) {
        return worldIn.getEntitiesWithinAABB((Class<? extends EntityItem>)EntityItem.class, new AxisAlignedBB(p_184292_1_ - 0.5, p_184292_3_, p_184292_5_ - 0.5, p_184292_1_ + 0.5, p_184292_3_ + 1.5, p_184292_5_ + 0.5), (Predicate<? super EntityItem>)EntitySelectors.IS_ALIVE);
    }
    
    public static IInventory getInventoryAtPosition(final World worldIn, final double x, final double y, final double z) {
        IInventory iinventory = null;
        final int i = MathHelper.floor(x);
        final int j = MathHelper.floor(y);
        final int k = MathHelper.floor(z);
        final BlockPos blockpos = new BlockPos(i, j, k);
        final Block block = worldIn.getBlockState(blockpos).getBlock();
        if (block.hasTileEntity()) {
            final TileEntity tileentity = worldIn.getTileEntity(blockpos);
            if (tileentity instanceof IInventory) {
                iinventory = (IInventory)tileentity;
                if (iinventory instanceof TileEntityChest && block instanceof BlockChest) {
                    iinventory = ((BlockChest)block).getContainer(worldIn, blockpos, true);
                }
            }
        }
        if (iinventory == null) {
            final List<Entity> list = worldIn.getEntitiesInAABBexcluding(null, new AxisAlignedBB(x - 0.5, y - 0.5, z - 0.5, x + 0.5, y + 0.5, z + 0.5), EntitySelectors.HAS_INVENTORY);
            if (!list.isEmpty()) {
                iinventory = (IInventory)list.get(worldIn.rand.nextInt(list.size()));
            }
        }
        return iinventory;
    }
    
    private static boolean canCombine(final ItemStack stack1, final ItemStack stack2) {
        return stack1.getItem() == stack2.getItem() && stack1.getMetadata() == stack2.getMetadata() && stack1.func_190916_E() <= stack1.getMaxStackSize() && ItemStack.areItemStackTagsEqual(stack1, stack2);
    }
    
    @Override
    public double getXPos() {
        return this.pos.getX() + 0.5;
    }
    
    @Override
    public double getYPos() {
        return this.pos.getY() + 0.5;
    }
    
    @Override
    public double getZPos() {
        return this.pos.getZ() + 0.5;
    }
    
    private void setTransferCooldown(final int ticks) {
        this.transferCooldown = ticks;
    }
    
    private boolean isOnTransferCooldown() {
        return this.transferCooldown > 0;
    }
    
    private boolean mayTransfer() {
        return this.transferCooldown > 8;
    }
    
    @Override
    public String getGuiID() {
        return "minecraft:hopper";
    }
    
    @Override
    public Container createContainer(final InventoryPlayer playerInventory, final EntityPlayer playerIn) {
        this.fillWithLoot(playerIn);
        return new ContainerHopper(playerInventory, this, playerIn);
    }
    
    @Override
    protected NonNullList<ItemStack> func_190576_q() {
        return this.inventory;
    }
}
