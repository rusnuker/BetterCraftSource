// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.channel;

public final class WriteBufferWaterMark
{
    private static final int DEFAULT_LOW_WATER_MARK = 32768;
    private static final int DEFAULT_HIGH_WATER_MARK = 65536;
    public static final WriteBufferWaterMark DEFAULT;
    private final int low;
    private final int high;
    
    public WriteBufferWaterMark(final int low, final int high) {
        this(low, high, true);
    }
    
    WriteBufferWaterMark(final int low, final int high, final boolean validate) {
        if (validate) {
            if (low < 0) {
                throw new IllegalArgumentException("write buffer's low water mark must be >= 0");
            }
            if (high < low) {
                throw new IllegalArgumentException("write buffer's high water mark cannot be less than  low water mark (" + low + "): " + high);
            }
        }
        this.low = low;
        this.high = high;
    }
    
    public int low() {
        return this.low;
    }
    
    public int high() {
        return this.high;
    }
    
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder(55).append("WriteBufferWaterMark(low: ").append(this.low).append(", high: ").append(this.high).append(")");
        return builder.toString();
    }
    
    static {
        DEFAULT = new WriteBufferWaterMark(32768, 65536, false);
    }
}
