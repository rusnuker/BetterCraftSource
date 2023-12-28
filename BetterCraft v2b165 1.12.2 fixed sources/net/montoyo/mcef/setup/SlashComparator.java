// 
// Decompiled by Procyon v0.6.0
// 

package net.montoyo.mcef.setup;

import java.util.Comparator;

final class SlashComparator implements Comparator<String>
{
    private Comparator<String> fallback;
    
    SlashComparator(final Comparator<String> fb) {
        this.fallback = fb;
    }
    
    @Override
    public int compare(final String a, final String b) {
        int slashA = 0;
        int slashB = 0;
        for (int i = 0; i < a.length(); ++i) {
            if (a.charAt(i) == '/' || a.charAt(i) == '\\') {
                ++slashA;
            }
        }
        for (int i = 0; i < b.length(); ++i) {
            if (b.charAt(i) == '/' || b.charAt(i) == '\\') {
                ++slashB;
            }
        }
        final int ret = slashB - slashA;
        if (ret == 0 && this.fallback != null) {
            return this.fallback.compare(a, b);
        }
        return ret;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof SlashComparator)) {
            return false;
        }
        final SlashComparator other = (SlashComparator)obj;
        if (this.fallback == null) {
            return other.fallback == null;
        }
        return other.fallback != null && this.fallback.equals(other.fallback);
    }
}
