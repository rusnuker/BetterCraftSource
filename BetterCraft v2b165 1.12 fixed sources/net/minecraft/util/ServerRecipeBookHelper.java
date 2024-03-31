// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.util;

import java.util.Iterator;
import net.minecraft.item.crafting.ShapedRecipes;
import it.unimi.dsi.fastutil.ints.IntListIterator;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketPlaceGhostRecipe;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.ContainerWorkbench;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import net.minecraft.inventory.Slot;
import java.util.List;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.client.util.RecipeItemHelper;
import org.apache.logging.log4j.Logger;

public class ServerRecipeBookHelper
{
    private final Logger field_194330_a;
    private final RecipeItemHelper field_194331_b;
    private EntityPlayerMP field_194332_c;
    private IRecipe field_194333_d;
    private boolean field_194334_e;
    private InventoryCraftResult field_194335_f;
    private InventoryCrafting field_194336_g;
    private List<Slot> field_194337_h;
    
    public ServerRecipeBookHelper() {
        this.field_194330_a = LogManager.getLogger();
        this.field_194331_b = new RecipeItemHelper();
    }
    
    public void func_194327_a(final EntityPlayerMP p_194327_1_, @Nullable final IRecipe p_194327_2_, final boolean p_194327_3_) {
        if (p_194327_2_ != null && p_194327_1_.func_192037_E().func_193830_f(p_194327_2_)) {
            this.field_194332_c = p_194327_1_;
            this.field_194333_d = p_194327_2_;
            this.field_194334_e = p_194327_3_;
            this.field_194337_h = p_194327_1_.openContainer.inventorySlots;
            final Container container = p_194327_1_.openContainer;
            this.field_194335_f = null;
            this.field_194336_g = null;
            if (container instanceof ContainerWorkbench) {
                this.field_194335_f = ((ContainerWorkbench)container).craftResult;
                this.field_194336_g = ((ContainerWorkbench)container).craftMatrix;
            }
            else if (container instanceof ContainerPlayer) {
                this.field_194335_f = ((ContainerPlayer)container).craftResult;
                this.field_194336_g = ((ContainerPlayer)container).craftMatrix;
            }
            if (this.field_194335_f != null && this.field_194336_g != null && (this.func_194328_c() || p_194327_1_.isCreative())) {
                this.field_194331_b.func_194119_a();
                p_194327_1_.inventory.func_194016_a(this.field_194331_b, false);
                this.field_194336_g.func_194018_a(this.field_194331_b);
                if (this.field_194331_b.func_194116_a(p_194327_2_, null)) {
                    this.func_194329_b();
                }
                else {
                    this.func_194326_a();
                    p_194327_1_.connection.sendPacket(new SPacketPlaceGhostRecipe(p_194327_1_.openContainer.windowId, p_194327_2_));
                }
                p_194327_1_.inventory.markDirty();
            }
        }
    }
    
    private void func_194326_a() {
        final InventoryPlayer inventoryplayer = this.field_194332_c.inventory;
        for (int i = 0; i < this.field_194336_g.getSizeInventory(); ++i) {
            final ItemStack itemstack = this.field_194336_g.getStackInSlot(i);
            if (!itemstack.func_190926_b()) {
                while (itemstack.func_190916_E() > 0) {
                    int j = inventoryplayer.storeItemStack(itemstack);
                    if (j == -1) {
                        j = inventoryplayer.getFirstEmptyStack();
                    }
                    final ItemStack itemstack2 = itemstack.copy();
                    itemstack2.func_190920_e(1);
                    inventoryplayer.func_191971_c(j, itemstack2);
                    this.field_194336_g.decrStackSize(i, 1);
                }
            }
        }
        this.field_194336_g.clear();
        this.field_194335_f.clear();
    }
    
    private void func_194329_b() {
        final boolean flag = this.field_194333_d.matches(this.field_194336_g, this.field_194332_c.world);
        final int i = this.field_194331_b.func_194114_b(this.field_194333_d, null);
        if (flag) {
            boolean flag2 = true;
            for (int j = 0; j < this.field_194336_g.getSizeInventory(); ++j) {
                final ItemStack itemstack = this.field_194336_g.getStackInSlot(j);
                if (!itemstack.func_190926_b() && Math.min(i, itemstack.getMaxStackSize()) > itemstack.func_190916_E()) {
                    flag2 = false;
                }
            }
            if (flag2) {
                return;
            }
        }
        final int i2 = this.func_194324_a(i, flag);
        final IntList intlist = new IntArrayList();
        if (this.field_194331_b.func_194118_a(this.field_194333_d, intlist, i2)) {
            int j2 = i2;
            for (final int k : intlist) {
                final int l = RecipeItemHelper.func_194115_b(k).getMaxStackSize();
                if (l < j2) {
                    j2 = l;
                }
            }
            if (this.field_194331_b.func_194118_a(this.field_194333_d, intlist, j2)) {
                this.func_194326_a();
                this.func_194323_a(j2, intlist);
            }
        }
    }
    
    private int func_194324_a(final int p_194324_1_, final boolean p_194324_2_) {
        int i = 1;
        if (this.field_194334_e) {
            i = p_194324_1_;
        }
        else if (p_194324_2_) {
            i = 64;
            for (int j = 0; j < this.field_194336_g.getSizeInventory(); ++j) {
                final ItemStack itemstack = this.field_194336_g.getStackInSlot(j);
                if (!itemstack.func_190926_b() && i > itemstack.func_190916_E()) {
                    i = itemstack.func_190916_E();
                }
            }
            if (i < 64) {
                ++i;
            }
        }
        return i;
    }
    
    private void func_194323_a(final int p_194323_1_, final IntList p_194323_2_) {
        int i = this.field_194336_g.getWidth();
        int j = this.field_194336_g.getHeight();
        if (this.field_194333_d instanceof ShapedRecipes) {
            final ShapedRecipes shapedrecipes = (ShapedRecipes)this.field_194333_d;
            i = shapedrecipes.func_192403_f();
            j = shapedrecipes.func_192404_g();
        }
        int j2 = 1;
        final Iterator<Integer> iterator = p_194323_2_.iterator();
        for (int k = 0; k < this.field_194336_g.getWidth() && j != k; ++k) {
            for (int l = 0; l < this.field_194336_g.getHeight(); ++l) {
                if (i == l || !iterator.hasNext()) {
                    j2 += this.field_194336_g.getWidth() - l;
                    break;
                }
                final Slot slot = this.field_194337_h.get(j2);
                final ItemStack itemstack = RecipeItemHelper.func_194115_b(iterator.next());
                if (itemstack.func_190926_b()) {
                    ++j2;
                }
                else {
                    for (int i2 = 0; i2 < p_194323_1_; ++i2) {
                        this.func_194325_a(slot, itemstack);
                    }
                    ++j2;
                }
            }
            if (!iterator.hasNext()) {
                break;
            }
        }
    }
    
    private void func_194325_a(final Slot p_194325_1_, final ItemStack p_194325_2_) {
        final InventoryPlayer inventoryplayer = this.field_194332_c.inventory;
        final int i = inventoryplayer.func_194014_c(p_194325_2_);
        if (i != -1) {
            final ItemStack itemstack = inventoryplayer.getStackInSlot(i).copy();
            if (!itemstack.func_190926_b()) {
                if (itemstack.func_190916_E() > 1) {
                    inventoryplayer.decrStackSize(i, 1);
                }
                else {
                    inventoryplayer.removeStackFromSlot(i);
                }
                itemstack.func_190920_e(1);
                if (p_194325_1_.getStack().func_190926_b()) {
                    p_194325_1_.putStack(itemstack);
                }
                else {
                    p_194325_1_.getStack().func_190917_f(1);
                }
            }
        }
    }
    
    private boolean func_194328_c() {
        final InventoryPlayer inventoryplayer = this.field_194332_c.inventory;
        for (int i = 0; i < this.field_194336_g.getSizeInventory(); ++i) {
            final ItemStack itemstack = this.field_194336_g.getStackInSlot(i);
            if (!itemstack.func_190926_b()) {
                int j = inventoryplayer.storeItemStack(itemstack);
                if (j == -1) {
                    j = inventoryplayer.getFirstEmptyStack();
                }
                if (j == -1) {
                    return false;
                }
            }
        }
        return true;
    }
}
