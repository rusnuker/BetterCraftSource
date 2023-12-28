// 
// Decompiled by Procyon v0.6.0
// 

package com.mcf.davidee.nbtedit.nbt;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagCompound;
import java.util.Comparator;

public class NBTNodeSorter implements Comparator<Node<NamedNBT>>
{
    @Override
    public int compare(final Node<NamedNBT> a, final Node<NamedNBT> b) {
        final NBTBase n1 = a.getObject().getNBT();
        final NBTBase n2 = b.getObject().getNBT();
        final String s1 = a.getObject().getName();
        final String s2 = b.getObject().getName();
        if (n1 instanceof NBTTagCompound || n1 instanceof NBTTagList) {
            if (n2 instanceof NBTTagCompound || n2 instanceof NBTTagList) {
                final int dif = n1.getId() - n2.getId();
                return (dif == 0) ? s1.compareTo(s2) : dif;
            }
            return 1;
        }
        else {
            if (n2 instanceof NBTTagCompound || n2 instanceof NBTTagList) {
                return -1;
            }
            final int dif = n1.getId() - n2.getId();
            return (dif == 0) ? s1.compareTo(s2) : dif;
        }
    }
}
