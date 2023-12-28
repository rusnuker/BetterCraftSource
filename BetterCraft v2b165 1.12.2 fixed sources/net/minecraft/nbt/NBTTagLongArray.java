// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.nbt;

import java.util.Arrays;
import java.io.DataInput;
import java.io.IOException;
import java.io.DataOutput;
import java.util.List;

public class NBTTagLongArray extends NBTBase
{
    private long[] field_193587_b;
    
    NBTTagLongArray() {
    }
    
    public NBTTagLongArray(final long[] p_i47524_1_) {
        this.field_193587_b = p_i47524_1_;
    }
    
    public NBTTagLongArray(final List<Long> p_i47525_1_) {
        this(func_193586_a(p_i47525_1_));
    }
    
    private static long[] func_193586_a(final List<Long> p_193586_0_) {
        final long[] along = new long[p_193586_0_.size()];
        for (int i = 0; i < p_193586_0_.size(); ++i) {
            final Long olong = p_193586_0_.get(i);
            along[i] = ((olong == null) ? 0L : olong);
        }
        return along;
    }
    
    @Override
    void write(final DataOutput output) throws IOException {
        output.writeInt(this.field_193587_b.length);
        long[] field_193587_b;
        for (int length = (field_193587_b = this.field_193587_b).length, j = 0; j < length; ++j) {
            final long i = field_193587_b[j];
            output.writeLong(i);
        }
    }
    
    @Override
    void read(final DataInput input, final int depth, final NBTSizeTracker sizeTracker) throws IOException {
        sizeTracker.read(192L);
        final int i = input.readInt();
        sizeTracker.read(64 * i);
        this.field_193587_b = new long[i];
        for (int j = 0; j < i; ++j) {
            this.field_193587_b[j] = input.readLong();
        }
    }
    
    @Override
    public byte getId() {
        return 12;
    }
    
    @Override
    public String toString() {
        final StringBuilder stringbuilder = new StringBuilder("[L;");
        for (int i = 0; i < this.field_193587_b.length; ++i) {
            if (i != 0) {
                stringbuilder.append(',');
            }
            stringbuilder.append(this.field_193587_b[i]).append('L');
        }
        return stringbuilder.append(']').toString();
    }
    
    @Override
    public NBTTagLongArray copy() {
        final long[] along = new long[this.field_193587_b.length];
        System.arraycopy(this.field_193587_b, 0, along, 0, this.field_193587_b.length);
        return new NBTTagLongArray(along);
    }
    
    @Override
    public boolean equals(final Object p_equals_1_) {
        return super.equals(p_equals_1_) && Arrays.equals(this.field_193587_b, ((NBTTagLongArray)p_equals_1_).field_193587_b);
    }
    
    @Override
    public int hashCode() {
        return super.hashCode() ^ Arrays.hashCode(this.field_193587_b);
    }
}
