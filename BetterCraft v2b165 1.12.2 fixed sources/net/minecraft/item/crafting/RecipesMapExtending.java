// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.item.crafting;

import net.minecraft.nbt.NBTTagCompound;
import java.util.Iterator;
import net.minecraft.world.storage.MapDecoration;
import net.minecraft.world.storage.MapData;
import net.minecraft.world.World;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

public class RecipesMapExtending extends ShapedRecipes
{
    public RecipesMapExtending() {
        super("", 3, 3, NonNullList.func_193580_a(Ingredient.field_193370_a, Ingredient.func_193368_a(Items.PAPER), Ingredient.func_193368_a(Items.PAPER), Ingredient.func_193368_a(Items.PAPER), Ingredient.func_193368_a(Items.PAPER), Ingredient.func_193367_a(Items.FILLED_MAP), Ingredient.func_193368_a(Items.PAPER), Ingredient.func_193368_a(Items.PAPER), Ingredient.func_193368_a(Items.PAPER), Ingredient.func_193368_a(Items.PAPER)), new ItemStack(Items.MAP));
    }
    
    @Override
    public boolean matches(final InventoryCrafting inv, final World worldIn) {
        if (!super.matches(inv, worldIn)) {
            return false;
        }
        ItemStack itemstack = ItemStack.field_190927_a;
        for (int i = 0; i < inv.getSizeInventory() && itemstack.func_190926_b(); ++i) {
            final ItemStack itemstack2 = inv.getStackInSlot(i);
            if (itemstack2.getItem() == Items.FILLED_MAP) {
                itemstack = itemstack2;
            }
        }
        if (itemstack.func_190926_b()) {
            return false;
        }
        final MapData mapdata = Items.FILLED_MAP.getMapData(itemstack, worldIn);
        return mapdata != null && !this.func_190934_a(mapdata) && mapdata.scale < 4;
    }
    
    private boolean func_190934_a(final MapData p_190934_1_) {
        if (p_190934_1_.mapDecorations != null) {
            for (final MapDecoration mapdecoration : p_190934_1_.mapDecorations.values()) {
                if (mapdecoration.func_191179_b() == MapDecoration.Type.MANSION || mapdecoration.func_191179_b() == MapDecoration.Type.MONUMENT) {
                    return true;
                }
            }
        }
        return false;
    }
    
    @Override
    public ItemStack getCraftingResult(final InventoryCrafting inv) {
        ItemStack itemstack = ItemStack.field_190927_a;
        for (int i = 0; i < inv.getSizeInventory() && itemstack.func_190926_b(); ++i) {
            final ItemStack itemstack2 = inv.getStackInSlot(i);
            if (itemstack2.getItem() == Items.FILLED_MAP) {
                itemstack = itemstack2;
            }
        }
        itemstack = itemstack.copy();
        itemstack.func_190920_e(1);
        if (itemstack.getTagCompound() == null) {
            itemstack.setTagCompound(new NBTTagCompound());
        }
        itemstack.getTagCompound().setInteger("map_scale_direction", 1);
        return itemstack;
    }
    
    @Override
    public boolean func_192399_d() {
        return true;
    }
}
