// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.item.crafting;

import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemBanner;
import javax.annotation.Nullable;
import net.minecraft.util.NonNullList;
import net.minecraft.tileentity.BannerPattern;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.init.Items;
import net.minecraft.world.World;
import net.minecraft.inventory.InventoryCrafting;

public class RecipesBanners
{
    public static class RecipeAddPattern implements IRecipe
    {
        @Override
        public boolean matches(final InventoryCrafting inv, final World worldIn) {
            boolean flag = false;
            for (int i = 0; i < inv.getSizeInventory(); ++i) {
                final ItemStack itemstack = inv.getStackInSlot(i);
                if (itemstack.getItem() == Items.BANNER) {
                    if (flag) {
                        return false;
                    }
                    if (TileEntityBanner.getPatterns(itemstack) >= 6) {
                        return false;
                    }
                    flag = true;
                }
            }
            return flag && this.func_190933_c(inv) != null;
        }
        
        @Override
        public ItemStack getCraftingResult(final InventoryCrafting inv) {
            ItemStack itemstack = ItemStack.field_190927_a;
            for (int i = 0; i < inv.getSizeInventory(); ++i) {
                final ItemStack itemstack2 = inv.getStackInSlot(i);
                if (!itemstack2.func_190926_b() && itemstack2.getItem() == Items.BANNER) {
                    itemstack = itemstack2.copy();
                    itemstack.func_190920_e(1);
                    break;
                }
            }
            final BannerPattern bannerpattern = this.func_190933_c(inv);
            if (bannerpattern != null) {
                int k = 0;
                for (int j = 0; j < inv.getSizeInventory(); ++j) {
                    final ItemStack itemstack3 = inv.getStackInSlot(j);
                    if (itemstack3.getItem() == Items.DYE) {
                        k = itemstack3.getMetadata();
                        break;
                    }
                }
                final NBTTagCompound nbttagcompound1 = itemstack.func_190925_c("BlockEntityTag");
                NBTTagList nbttaglist;
                if (nbttagcompound1.hasKey("Patterns", 9)) {
                    nbttaglist = nbttagcompound1.getTagList("Patterns", 10);
                }
                else {
                    nbttaglist = new NBTTagList();
                    nbttagcompound1.setTag("Patterns", nbttaglist);
                }
                final NBTTagCompound nbttagcompound2 = new NBTTagCompound();
                nbttagcompound2.setString("Pattern", bannerpattern.func_190993_b());
                nbttagcompound2.setInteger("Color", k);
                nbttaglist.appendTag(nbttagcompound2);
            }
            return itemstack;
        }
        
        @Override
        public ItemStack getRecipeOutput() {
            return ItemStack.field_190927_a;
        }
        
        @Override
        public NonNullList<ItemStack> getRemainingItems(final InventoryCrafting inv) {
            final NonNullList<ItemStack> nonnulllist = NonNullList.func_191197_a(inv.getSizeInventory(), ItemStack.field_190927_a);
            for (int i = 0; i < nonnulllist.size(); ++i) {
                final ItemStack itemstack = inv.getStackInSlot(i);
                if (itemstack.getItem().hasContainerItem()) {
                    nonnulllist.set(i, new ItemStack(itemstack.getItem().getContainerItem()));
                }
            }
            return nonnulllist;
        }
        
        @Nullable
        private BannerPattern func_190933_c(final InventoryCrafting p_190933_1_) {
            BannerPattern[] values;
            for (int length = (values = BannerPattern.values()).length, n = 0; n < length; ++n) {
                final BannerPattern bannerpattern = values[n];
                if (bannerpattern.func_191000_d()) {
                    boolean flag = true;
                    if (bannerpattern.func_190999_e()) {
                        boolean flag2 = false;
                        boolean flag3 = false;
                        for (int i = 0; i < p_190933_1_.getSizeInventory() && flag; ++i) {
                            final ItemStack itemstack = p_190933_1_.getStackInSlot(i);
                            if (!itemstack.func_190926_b() && itemstack.getItem() != Items.BANNER) {
                                if (itemstack.getItem() == Items.DYE) {
                                    if (flag3) {
                                        flag = false;
                                        break;
                                    }
                                    flag3 = true;
                                }
                                else {
                                    if (flag2 || !itemstack.isItemEqual(bannerpattern.func_190998_f())) {
                                        flag = false;
                                        break;
                                    }
                                    flag2 = true;
                                }
                            }
                        }
                        if (!flag2 || !flag3) {
                            flag = false;
                        }
                    }
                    else if (p_190933_1_.getSizeInventory() == bannerpattern.func_190996_c().length * bannerpattern.func_190996_c()[0].length()) {
                        int j = -1;
                        for (int k = 0; k < p_190933_1_.getSizeInventory(); ++k) {
                            if (!flag) {
                                break;
                            }
                            final int l = k / 3;
                            final int i2 = k % 3;
                            final ItemStack itemstack2 = p_190933_1_.getStackInSlot(k);
                            if (!itemstack2.func_190926_b() && itemstack2.getItem() != Items.BANNER) {
                                if (itemstack2.getItem() != Items.DYE) {
                                    flag = false;
                                    break;
                                }
                                if (j != -1 && j != itemstack2.getMetadata()) {
                                    flag = false;
                                    break;
                                }
                                if (bannerpattern.func_190996_c()[l].charAt(i2) == ' ') {
                                    flag = false;
                                    break;
                                }
                                j = itemstack2.getMetadata();
                            }
                            else if (bannerpattern.func_190996_c()[l].charAt(i2) != ' ') {
                                flag = false;
                                break;
                            }
                        }
                    }
                    else {
                        flag = false;
                    }
                    if (flag) {
                        return bannerpattern;
                    }
                }
            }
            return null;
        }
        
        @Override
        public boolean func_192399_d() {
            return true;
        }
        
        @Override
        public boolean func_194133_a(final int p_194133_1_, final int p_194133_2_) {
            return p_194133_1_ >= 3 && p_194133_2_ >= 3;
        }
    }
    
    public static class RecipeDuplicatePattern implements IRecipe
    {
        @Override
        public boolean matches(final InventoryCrafting inv, final World worldIn) {
            ItemStack itemstack = ItemStack.field_190927_a;
            ItemStack itemstack2 = ItemStack.field_190927_a;
            for (int i = 0; i < inv.getSizeInventory(); ++i) {
                final ItemStack itemstack3 = inv.getStackInSlot(i);
                if (!itemstack3.func_190926_b()) {
                    if (itemstack3.getItem() != Items.BANNER) {
                        return false;
                    }
                    if (!itemstack.func_190926_b() && !itemstack2.func_190926_b()) {
                        return false;
                    }
                    final EnumDyeColor enumdyecolor = ItemBanner.getBaseColor(itemstack3);
                    final boolean flag = TileEntityBanner.getPatterns(itemstack3) > 0;
                    if (!itemstack.func_190926_b()) {
                        if (flag) {
                            return false;
                        }
                        if (enumdyecolor != ItemBanner.getBaseColor(itemstack)) {
                            return false;
                        }
                        itemstack2 = itemstack3;
                    }
                    else if (!itemstack2.func_190926_b()) {
                        if (!flag) {
                            return false;
                        }
                        if (enumdyecolor != ItemBanner.getBaseColor(itemstack2)) {
                            return false;
                        }
                        itemstack = itemstack3;
                    }
                    else if (flag) {
                        itemstack = itemstack3;
                    }
                    else {
                        itemstack2 = itemstack3;
                    }
                }
            }
            return !itemstack.func_190926_b() && !itemstack2.func_190926_b();
        }
        
        @Override
        public ItemStack getCraftingResult(final InventoryCrafting inv) {
            for (int i = 0; i < inv.getSizeInventory(); ++i) {
                final ItemStack itemstack = inv.getStackInSlot(i);
                if (!itemstack.func_190926_b() && TileEntityBanner.getPatterns(itemstack) > 0) {
                    final ItemStack itemstack2 = itemstack.copy();
                    itemstack2.func_190920_e(1);
                    return itemstack2;
                }
            }
            return ItemStack.field_190927_a;
        }
        
        @Override
        public ItemStack getRecipeOutput() {
            return ItemStack.field_190927_a;
        }
        
        @Override
        public NonNullList<ItemStack> getRemainingItems(final InventoryCrafting inv) {
            final NonNullList<ItemStack> nonnulllist = NonNullList.func_191197_a(inv.getSizeInventory(), ItemStack.field_190927_a);
            for (int i = 0; i < nonnulllist.size(); ++i) {
                final ItemStack itemstack = inv.getStackInSlot(i);
                if (!itemstack.func_190926_b()) {
                    if (itemstack.getItem().hasContainerItem()) {
                        nonnulllist.set(i, new ItemStack(itemstack.getItem().getContainerItem()));
                    }
                    else if (itemstack.hasTagCompound() && TileEntityBanner.getPatterns(itemstack) > 0) {
                        final ItemStack itemstack2 = itemstack.copy();
                        itemstack2.func_190920_e(1);
                        nonnulllist.set(i, itemstack2);
                    }
                }
            }
            return nonnulllist;
        }
        
        @Override
        public boolean func_192399_d() {
            return true;
        }
        
        @Override
        public boolean func_194133_a(final int p_194133_1_, final int p_194133_2_) {
            return p_194133_1_ * p_194133_2_ >= 2;
        }
    }
}
