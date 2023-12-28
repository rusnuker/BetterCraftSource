// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.inventory;

import net.minecraft.util.SoundCategory;
import net.minecraft.init.SoundEvents;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.stats.StatList;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.entity.player.EntityPlayer;
import java.util.List;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.InventoryPlayer;
import java.util.Random;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ContainerEnchantment extends Container
{
    public IInventory tableInventory;
    private final World worldPointer;
    private final BlockPos position;
    private final Random rand;
    public int xpSeed;
    public int[] enchantLevels;
    public int[] enchantClue;
    public int[] worldClue;
    
    public ContainerEnchantment(final InventoryPlayer playerInv, final World worldIn) {
        this(playerInv, worldIn, BlockPos.ORIGIN);
    }
    
    public ContainerEnchantment(final InventoryPlayer playerInv, final World worldIn, final BlockPos pos) {
        this.tableInventory = new InventoryBasic("Enchant", true, 2) {
            @Override
            public int getInventoryStackLimit() {
                return 64;
            }
            
            @Override
            public void markDirty() {
                super.markDirty();
                ContainerEnchantment.this.onCraftMatrixChanged(this);
            }
        };
        this.rand = new Random();
        this.enchantLevels = new int[3];
        this.enchantClue = new int[] { -1, -1, -1 };
        this.worldClue = new int[] { -1, -1, -1 };
        this.worldPointer = worldIn;
        this.position = pos;
        this.xpSeed = playerInv.player.getXPSeed();
        this.addSlotToContainer(new Slot(this.tableInventory, 0, 15, 47) {
            @Override
            public boolean isItemValid(final ItemStack stack) {
                return true;
            }
            
            @Override
            public int getSlotStackLimit() {
                return 1;
            }
        });
        this.addSlotToContainer(new Slot(this.tableInventory, 1, 35, 47) {
            @Override
            public boolean isItemValid(final ItemStack stack) {
                return stack.getItem() == Items.DYE && EnumDyeColor.byDyeDamage(stack.getMetadata()) == EnumDyeColor.BLUE;
            }
        });
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (int k = 0; k < 9; ++k) {
            this.addSlotToContainer(new Slot(playerInv, k, 8 + k * 18, 142));
        }
    }
    
    protected void broadcastData(final IContainerListener crafting) {
        crafting.sendProgressBarUpdate(this, 0, this.enchantLevels[0]);
        crafting.sendProgressBarUpdate(this, 1, this.enchantLevels[1]);
        crafting.sendProgressBarUpdate(this, 2, this.enchantLevels[2]);
        crafting.sendProgressBarUpdate(this, 3, this.xpSeed & 0xFFFFFFF0);
        crafting.sendProgressBarUpdate(this, 4, this.enchantClue[0]);
        crafting.sendProgressBarUpdate(this, 5, this.enchantClue[1]);
        crafting.sendProgressBarUpdate(this, 6, this.enchantClue[2]);
        crafting.sendProgressBarUpdate(this, 7, this.worldClue[0]);
        crafting.sendProgressBarUpdate(this, 8, this.worldClue[1]);
        crafting.sendProgressBarUpdate(this, 9, this.worldClue[2]);
    }
    
    @Override
    public void addListener(final IContainerListener listener) {
        super.addListener(listener);
        this.broadcastData(listener);
    }
    
    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        for (int i = 0; i < this.listeners.size(); ++i) {
            final IContainerListener icontainerlistener = this.listeners.get(i);
            this.broadcastData(icontainerlistener);
        }
    }
    
    @Override
    public void updateProgressBar(final int id, final int data) {
        if (id >= 0 && id <= 2) {
            this.enchantLevels[id] = data;
        }
        else if (id == 3) {
            this.xpSeed = data;
        }
        else if (id >= 4 && id <= 6) {
            this.enchantClue[id - 4] = data;
        }
        else if (id >= 7 && id <= 9) {
            this.worldClue[id - 7] = data;
        }
        else {
            super.updateProgressBar(id, data);
        }
    }
    
    @Override
    public void onCraftMatrixChanged(final IInventory inventoryIn) {
        if (inventoryIn == this.tableInventory) {
            final ItemStack itemstack = inventoryIn.getStackInSlot(0);
            if (!itemstack.func_190926_b() && itemstack.isItemEnchantable()) {
                if (!this.worldPointer.isRemote) {
                    int l = 0;
                    for (int j = -1; j <= 1; ++j) {
                        for (int k = -1; k <= 1; ++k) {
                            if ((j != 0 || k != 0) && this.worldPointer.isAirBlock(this.position.add(k, 0, j)) && this.worldPointer.isAirBlock(this.position.add(k, 1, j))) {
                                if (this.worldPointer.getBlockState(this.position.add(k * 2, 0, j * 2)).getBlock() == Blocks.BOOKSHELF) {
                                    ++l;
                                }
                                if (this.worldPointer.getBlockState(this.position.add(k * 2, 1, j * 2)).getBlock() == Blocks.BOOKSHELF) {
                                    ++l;
                                }
                                if (k != 0 && j != 0) {
                                    if (this.worldPointer.getBlockState(this.position.add(k * 2, 0, j)).getBlock() == Blocks.BOOKSHELF) {
                                        ++l;
                                    }
                                    if (this.worldPointer.getBlockState(this.position.add(k * 2, 1, j)).getBlock() == Blocks.BOOKSHELF) {
                                        ++l;
                                    }
                                    if (this.worldPointer.getBlockState(this.position.add(k, 0, j * 2)).getBlock() == Blocks.BOOKSHELF) {
                                        ++l;
                                    }
                                    if (this.worldPointer.getBlockState(this.position.add(k, 1, j * 2)).getBlock() == Blocks.BOOKSHELF) {
                                        ++l;
                                    }
                                }
                            }
                        }
                    }
                    this.rand.setSeed(this.xpSeed);
                    for (int i1 = 0; i1 < 3; ++i1) {
                        this.enchantLevels[i1] = EnchantmentHelper.calcItemStackEnchantability(this.rand, i1, l, itemstack);
                        this.enchantClue[i1] = -1;
                        this.worldClue[i1] = -1;
                        if (this.enchantLevels[i1] < i1 + 1) {
                            this.enchantLevels[i1] = 0;
                        }
                    }
                    for (int j2 = 0; j2 < 3; ++j2) {
                        if (this.enchantLevels[j2] > 0) {
                            final List<EnchantmentData> list = this.getEnchantmentList(itemstack, j2, this.enchantLevels[j2]);
                            if (list != null && !list.isEmpty()) {
                                final EnchantmentData enchantmentdata = list.get(this.rand.nextInt(list.size()));
                                this.enchantClue[j2] = Enchantment.getEnchantmentID(enchantmentdata.enchantmentobj);
                                this.worldClue[j2] = enchantmentdata.enchantmentLevel;
                            }
                        }
                    }
                    this.detectAndSendChanges();
                }
            }
            else {
                for (int m = 0; m < 3; ++m) {
                    this.enchantLevels[m] = 0;
                    this.enchantClue[m] = -1;
                    this.worldClue[m] = -1;
                }
            }
        }
    }
    
    @Override
    public boolean enchantItem(final EntityPlayer playerIn, final int id) {
        ItemStack itemstack = this.tableInventory.getStackInSlot(0);
        final ItemStack itemstack2 = this.tableInventory.getStackInSlot(1);
        final int i = id + 1;
        if ((itemstack2.func_190926_b() || itemstack2.func_190916_E() < i) && !playerIn.capabilities.isCreativeMode) {
            return false;
        }
        if (this.enchantLevels[id] > 0 && !itemstack.func_190926_b() && ((playerIn.experienceLevel >= i && playerIn.experienceLevel >= this.enchantLevels[id]) || playerIn.capabilities.isCreativeMode)) {
            if (!this.worldPointer.isRemote) {
                final List<EnchantmentData> list = this.getEnchantmentList(itemstack, id, this.enchantLevels[id]);
                if (!list.isEmpty()) {
                    playerIn.func_192024_a(itemstack, i);
                    final boolean flag = itemstack.getItem() == Items.BOOK;
                    if (flag) {
                        itemstack = new ItemStack(Items.ENCHANTED_BOOK);
                        this.tableInventory.setInventorySlotContents(0, itemstack);
                    }
                    for (int j = 0; j < list.size(); ++j) {
                        final EnchantmentData enchantmentdata = list.get(j);
                        if (flag) {
                            ItemEnchantedBook.addEnchantment(itemstack, enchantmentdata);
                        }
                        else {
                            itemstack.addEnchantment(enchantmentdata.enchantmentobj, enchantmentdata.enchantmentLevel);
                        }
                    }
                    if (!playerIn.capabilities.isCreativeMode) {
                        itemstack2.func_190918_g(i);
                        if (itemstack2.func_190926_b()) {
                            this.tableInventory.setInventorySlotContents(1, ItemStack.field_190927_a);
                        }
                    }
                    playerIn.addStat(StatList.ITEM_ENCHANTED);
                    if (playerIn instanceof EntityPlayerMP) {
                        CriteriaTriggers.field_192129_i.func_192190_a((EntityPlayerMP)playerIn, itemstack, i);
                    }
                    this.tableInventory.markDirty();
                    this.xpSeed = playerIn.getXPSeed();
                    this.onCraftMatrixChanged(this.tableInventory);
                    this.worldPointer.playSound(null, this.position, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1.0f, this.worldPointer.rand.nextFloat() * 0.1f + 0.9f);
                }
            }
            return true;
        }
        return false;
    }
    
    private List<EnchantmentData> getEnchantmentList(final ItemStack stack, final int p_178148_2_, final int p_178148_3_) {
        this.rand.setSeed(this.xpSeed + p_178148_2_);
        final List<EnchantmentData> list = EnchantmentHelper.buildEnchantmentList(this.rand, stack, p_178148_3_, false);
        if (stack.getItem() == Items.BOOK && list.size() > 1) {
            list.remove(this.rand.nextInt(list.size()));
        }
        return list;
    }
    
    public int getLapisAmount() {
        final ItemStack itemstack = this.tableInventory.getStackInSlot(1);
        return itemstack.func_190926_b() ? 0 : itemstack.func_190916_E();
    }
    
    @Override
    public void onContainerClosed(final EntityPlayer playerIn) {
        super.onContainerClosed(playerIn);
        if (!this.worldPointer.isRemote) {
            this.func_193327_a(playerIn, playerIn.world, this.tableInventory);
        }
    }
    
    @Override
    public boolean canInteractWith(final EntityPlayer playerIn) {
        return this.worldPointer.getBlockState(this.position).getBlock() == Blocks.ENCHANTING_TABLE && playerIn.getDistanceSq(this.position.getX() + 0.5, this.position.getY() + 0.5, this.position.getZ() + 0.5) <= 64.0;
    }
    
    @Override
    public ItemStack transferStackInSlot(final EntityPlayer playerIn, final int index) {
        ItemStack itemstack = ItemStack.field_190927_a;
        final Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            final ItemStack itemstack2 = slot.getStack();
            itemstack = itemstack2.copy();
            if (index == 0) {
                if (!this.mergeItemStack(itemstack2, 2, 38, true)) {
                    return ItemStack.field_190927_a;
                }
            }
            else if (index == 1) {
                if (!this.mergeItemStack(itemstack2, 2, 38, true)) {
                    return ItemStack.field_190927_a;
                }
            }
            else if (itemstack2.getItem() == Items.DYE && EnumDyeColor.byDyeDamage(itemstack2.getMetadata()) == EnumDyeColor.BLUE) {
                if (!this.mergeItemStack(itemstack2, 1, 2, true)) {
                    return ItemStack.field_190927_a;
                }
            }
            else {
                if (this.inventorySlots.get(0).getHasStack() || !this.inventorySlots.get(0).isItemValid(itemstack2)) {
                    return ItemStack.field_190927_a;
                }
                if (itemstack2.hasTagCompound() && itemstack2.func_190916_E() == 1) {
                    this.inventorySlots.get(0).putStack(itemstack2.copy());
                    itemstack2.func_190920_e(0);
                }
                else if (!itemstack2.func_190926_b()) {
                    this.inventorySlots.get(0).putStack(new ItemStack(itemstack2.getItem(), 1, itemstack2.getMetadata()));
                    itemstack2.func_190918_g(1);
                }
            }
            if (itemstack2.func_190926_b()) {
                slot.putStack(ItemStack.field_190927_a);
            }
            else {
                slot.onSlotChanged();
            }
            if (itemstack2.func_190916_E() == itemstack.func_190916_E()) {
                return ItemStack.field_190927_a;
            }
            slot.func_190901_a(playerIn, itemstack2);
        }
        return itemstack;
    }
}
