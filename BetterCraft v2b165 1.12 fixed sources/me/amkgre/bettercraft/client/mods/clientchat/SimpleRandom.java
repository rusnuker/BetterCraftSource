// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.clientchat;

public class SimpleRandom
{
    long RandomSeed;
    final long AND_VALUE = 281474976710655L;
    
    public void setSeed(final long seed) {
        this.RandomSeed = seed;
    }
    
    public int next() {
        this.RandomSeed = (this.RandomSeed * 25214903917L + 11L & 0xFFFFFFFFFFFFL);
        return (int)(this.RandomSeed >>> 17);
    }
    
    public int next(final int limit) {
        final int a = this.next() % limit;
        if (a < 0) {
            return -a;
        }
        return a;
    }
    
    public int next(final int start, final int end) {
        final int a = start + this.next() % (start - end);
        if (a < 0) {
            return -a;
        }
        return a;
    }
    
    public long nextLong() {
        return ((long)this.next() & 0xFFFFFFFFL) << 32 | ((long)this.next() & 0xFFFFFFFFL);
    }
}
