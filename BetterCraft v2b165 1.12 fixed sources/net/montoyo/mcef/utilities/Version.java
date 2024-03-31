// 
// Decompiled by Procyon v0.6.0
// 

package net.montoyo.mcef.utilities;

public class Version
{
    private int[] numbers;
    
    public Version(final String v) {
        final String[] ray = v.trim().split("\\.");
        this.numbers = new int[ray.length];
        for (int i = 0; i < ray.length; ++i) {
            try {
                this.numbers[i] = Integer.parseInt(ray[i]);
            }
            catch (final NumberFormatException e) {
                Log.error("Couldn't parse %s. Number %d will be zero.", v, i);
                e.printStackTrace();
                this.numbers[i] = 0;
            }
        }
        int end;
        for (end = this.numbers.length - 1; end >= 0 && this.numbers[end] == 0; --end) {}
        if (++end != this.numbers.length) {
            final int[] na = new int[end];
            System.arraycopy(this.numbers, 0, na, 0, end);
            this.numbers = na;
        }
    }
    
    public boolean isBiggerThan(final Version v) {
        for (int len = Math.min(this.numbers.length, v.numbers.length), i = 0; i < len; ++i) {
            if (this.numbers[i] > v.numbers[i]) {
                return true;
            }
        }
        return this.numbers.length > v.numbers.length;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof Version)) {
            return false;
        }
        final Version v = (Version)o;
        if (v == this) {
            return true;
        }
        if (this.numbers.length != v.numbers.length) {
            return false;
        }
        for (int i = 0; i < this.numbers.length; ++i) {
            if (this.numbers[i] != v.numbers[i]) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public String toString() {
        String ret = "";
        for (int i = 0; i < this.numbers.length; ++i) {
            if (i > 0) {
                ret = String.valueOf(ret) + '.';
            }
            ret = String.valueOf(ret) + this.numbers[i];
        }
        return ret;
    }
}
