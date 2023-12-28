// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.util.datafix.walkers;

import net.minecraft.util.datafix.DataFixesManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IDataFixer;

public class ItemStackDataLists extends Filtered
{
    private final String[] matchingTags;
    
    public ItemStackDataLists(final Class<?> p_i47310_1_, final String... p_i47310_2_) {
        super(p_i47310_1_);
        this.matchingTags = p_i47310_2_;
    }
    
    @Override
    NBTTagCompound filteredProcess(final IDataFixer fixer, NBTTagCompound compound, final int versionIn) {
        String[] matchingTags;
        for (int length = (matchingTags = this.matchingTags).length, i = 0; i < length; ++i) {
            final String s = matchingTags[i];
            compound = DataFixesManager.processInventory(fixer, compound, versionIn, s);
        }
        return compound;
    }
}
