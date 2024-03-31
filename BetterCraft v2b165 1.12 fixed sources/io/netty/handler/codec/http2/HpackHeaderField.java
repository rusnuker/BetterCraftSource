// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http2;

import io.netty.util.internal.ObjectUtil;

class HpackHeaderField
{
    static final int HEADER_ENTRY_OVERHEAD = 32;
    final CharSequence name;
    final CharSequence value;
    
    static long sizeOf(final CharSequence name, final CharSequence value) {
        return name.length() + value.length() + 32;
    }
    
    HpackHeaderField(final CharSequence name, final CharSequence value) {
        this.name = ObjectUtil.checkNotNull(name, "name");
        this.value = ObjectUtil.checkNotNull(value, "value");
    }
    
    final int size() {
        return this.name.length() + this.value.length() + 32;
    }
    
    @Override
    public final int hashCode() {
        return super.hashCode();
    }
    
    @Override
    public final boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof HpackHeaderField)) {
            return false;
        }
        final HpackHeaderField other = (HpackHeaderField)obj;
        return (HpackUtil.equalsConstantTime(this.name, other.name) & HpackUtil.equalsConstantTime(this.value, other.value)) != 0x0;
    }
    
    @Override
    public String toString() {
        return (Object)this.name + ": " + (Object)this.value;
    }
}
