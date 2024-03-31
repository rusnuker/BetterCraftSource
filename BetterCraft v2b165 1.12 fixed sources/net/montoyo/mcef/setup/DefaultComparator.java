// 
// Decompiled by Procyon v0.6.0
// 

package net.montoyo.mcef.setup;

import java.util.Comparator;

final class DefaultComparator implements Comparator<String>
{
    @Override
    public int compare(final String a, final String b) {
        return a.compareTo(b);
    }
    
    @Override
    public boolean equals(final Object o) {
        return o != null && o instanceof DefaultComparator;
    }
}
