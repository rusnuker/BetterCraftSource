// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.string;

import io.netty.util.internal.StringUtil;
import io.netty.buffer.ByteBufUtil;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.ObjectUtil;

public final class LineSeparator
{
    public static final LineSeparator DEFAULT;
    public static final LineSeparator UNIX;
    public static final LineSeparator WINDOWS;
    private final String value;
    
    public LineSeparator(final String lineSeparator) {
        this.value = ObjectUtil.checkNotNull(lineSeparator, "lineSeparator");
    }
    
    public String value() {
        return this.value;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LineSeparator)) {
            return false;
        }
        final LineSeparator that = (LineSeparator)o;
        return (this.value != null) ? this.value.equals(that.value) : (that.value == null);
    }
    
    @Override
    public int hashCode() {
        return (this.value != null) ? this.value.hashCode() : 0;
    }
    
    @Override
    public String toString() {
        return ByteBufUtil.hexDump(this.value.getBytes(CharsetUtil.UTF_8));
    }
    
    static {
        DEFAULT = new LineSeparator(StringUtil.NEWLINE);
        UNIX = new LineSeparator("\n");
        WINDOWS = new LineSeparator("\r\n");
    }
}
