// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.settings;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import java.util.ArrayList;

public class HotbarSnapshot extends ArrayList<ItemStack>
{
    public static final int field_192835_a;
    
    static {
        field_192835_a = InventoryPlayer.getHotbarSize();
    }
    
    public HotbarSnapshot() {
        this.ensureCapacity(HotbarSnapshot.field_192835_a);
        for (int i = 0; i < HotbarSnapshot.field_192835_a; ++i) {
            this.add(ItemStack.field_190927_a);
        }
    }
    
    public NBTTagList func_192834_a() {
        final NBTTagList nbttaglist = new NBTTagList();
        for (int i = 0; i < HotbarSnapshot.field_192835_a; ++i) {
            nbttaglist.appendTag(this.get(i).writeToNBT(new NBTTagCompound()));
        }
        return nbttaglist;
    }
    
    public void func_192833_a(final NBTTagList p_192833_1_) {
        for (int i = 0; i < HotbarSnapshot.field_192835_a; ++i) {
            this.set(i, new ItemStack(p_192833_1_.getCompoundTagAt(i)));
        }
    }
    
    @Override
    public boolean isEmpty() {
        for (int i = 0; i < HotbarSnapshot.field_192835_a; ++i) {
            if (!this.get(i).func_190926_b()) {
                return false;
            }
        }
        return true;
    }
}
