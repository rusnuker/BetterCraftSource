// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.buffer;

import io.netty.util.internal.StringUtil;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.ResourceLeakTracker;
import io.netty.util.ResourceLeakDetector;

public abstract class AbstractByteBufAllocator implements ByteBufAllocator
{
    static final int DEFAULT_INITIAL_CAPACITY = 256;
    static final int DEFAULT_MAX_CAPACITY = Integer.MAX_VALUE;
    static final int DEFAULT_MAX_COMPONENTS = 16;
    static final int CALCULATE_THRESHOLD = 4194304;
    private final boolean directByDefault;
    private final ByteBuf emptyBuf;
    
    protected static ByteBuf toLeakAwareBuffer(ByteBuf buf) {
        switch (ResourceLeakDetector.getLevel()) {
            case SIMPLE: {
                final ResourceLeakTracker<ByteBuf> leak = AbstractByteBuf.leakDetector.track(buf);
                if (leak != null) {
                    buf = new SimpleLeakAwareByteBuf(buf, leak);
                    break;
                }
                break;
            }
            case ADVANCED:
            case PARANOID: {
                final ResourceLeakTracker<ByteBuf> leak = AbstractByteBuf.leakDetector.track(buf);
                if (leak != null) {
                    buf = new AdvancedLeakAwareByteBuf(buf, leak);
                    break;
                }
                break;
            }
        }
        return buf;
    }
    
    protected static CompositeByteBuf toLeakAwareBuffer(CompositeByteBuf buf) {
        switch (ResourceLeakDetector.getLevel()) {
            case SIMPLE: {
                final ResourceLeakTracker<ByteBuf> leak = (ResourceLeakTracker<ByteBuf>)AbstractByteBuf.leakDetector.track(buf);
                if (leak != null) {
                    buf = new SimpleLeakAwareCompositeByteBuf(buf, leak);
                    break;
                }
                break;
            }
            case ADVANCED:
            case PARANOID: {
                final ResourceLeakTracker<ByteBuf> leak = (ResourceLeakTracker<ByteBuf>)AbstractByteBuf.leakDetector.track(buf);
                if (leak != null) {
                    buf = new AdvancedLeakAwareCompositeByteBuf(buf, leak);
                    break;
                }
                break;
            }
        }
        return buf;
    }
    
    protected AbstractByteBufAllocator() {
        this(false);
    }
    
    protected AbstractByteBufAllocator(final boolean preferDirect) {
        this.directByDefault = (preferDirect && PlatformDependent.hasUnsafe());
        this.emptyBuf = new EmptyByteBuf(this);
    }
    
    @Override
    public ByteBuf buffer() {
        if (this.directByDefault) {
            return this.directBuffer();
        }
        return this.heapBuffer();
    }
    
    @Override
    public ByteBuf buffer(final int initialCapacity) {
        if (this.directByDefault) {
            return this.directBuffer(initialCapacity);
        }
        return this.heapBuffer(initialCapacity);
    }
    
    @Override
    public ByteBuf buffer(final int initialCapacity, final int maxCapacity) {
        if (this.directByDefault) {
            return this.directBuffer(initialCapacity, maxCapacity);
        }
        return this.heapBuffer(initialCapacity, maxCapacity);
    }
    
    @Override
    public ByteBuf ioBuffer() {
        if (PlatformDependent.hasUnsafe()) {
            return this.directBuffer(256);
        }
        return this.heapBuffer(256);
    }
    
    @Override
    public ByteBuf ioBuffer(final int initialCapacity) {
        if (PlatformDependent.hasUnsafe()) {
            return this.directBuffer(initialCapacity);
        }
        return this.heapBuffer(initialCapacity);
    }
    
    @Override
    public ByteBuf ioBuffer(final int initialCapacity, final int maxCapacity) {
        if (PlatformDependent.hasUnsafe()) {
            return this.directBuffer(initialCapacity, maxCapacity);
        }
        return this.heapBuffer(initialCapacity, maxCapacity);
    }
    
    @Override
    public ByteBuf heapBuffer() {
        return this.heapBuffer(256, Integer.MAX_VALUE);
    }
    
    @Override
    public ByteBuf heapBuffer(final int initialCapacity) {
        return this.heapBuffer(initialCapacity, Integer.MAX_VALUE);
    }
    
    @Override
    public ByteBuf heapBuffer(final int initialCapacity, final int maxCapacity) {
        if (initialCapacity == 0 && maxCapacity == 0) {
            return this.emptyBuf;
        }
        validate(initialCapacity, maxCapacity);
        return this.newHeapBuffer(initialCapacity, maxCapacity);
    }
    
    @Override
    public ByteBuf directBuffer() {
        return this.directBuffer(256, Integer.MAX_VALUE);
    }
    
    @Override
    public ByteBuf directBuffer(final int initialCapacity) {
        return this.directBuffer(initialCapacity, Integer.MAX_VALUE);
    }
    
    @Override
    public ByteBuf directBuffer(final int initialCapacity, final int maxCapacity) {
        if (initialCapacity == 0 && maxCapacity == 0) {
            return this.emptyBuf;
        }
        validate(initialCapacity, maxCapacity);
        return this.newDirectBuffer(initialCapacity, maxCapacity);
    }
    
    @Override
    public CompositeByteBuf compositeBuffer() {
        if (this.directByDefault) {
            return this.compositeDirectBuffer();
        }
        return this.compositeHeapBuffer();
    }
    
    @Override
    public CompositeByteBuf compositeBuffer(final int maxNumComponents) {
        if (this.directByDefault) {
            return this.compositeDirectBuffer(maxNumComponents);
        }
        return this.compositeHeapBuffer(maxNumComponents);
    }
    
    @Override
    public CompositeByteBuf compositeHeapBuffer() {
        return this.compositeHeapBuffer(16);
    }
    
    @Override
    public CompositeByteBuf compositeHeapBuffer(final int maxNumComponents) {
        return toLeakAwareBuffer(new CompositeByteBuf(this, false, maxNumComponents));
    }
    
    @Override
    public CompositeByteBuf compositeDirectBuffer() {
        return this.compositeDirectBuffer(16);
    }
    
    @Override
    public CompositeByteBuf compositeDirectBuffer(final int maxNumComponents) {
        return toLeakAwareBuffer(new CompositeByteBuf(this, true, maxNumComponents));
    }
    
    private static void validate(final int initialCapacity, final int maxCapacity) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("initialCapacity: " + initialCapacity + " (expectd: 0+)");
        }
        if (initialCapacity > maxCapacity) {
            throw new IllegalArgumentException(String.format("initialCapacity: %d (expected: not greater than maxCapacity(%d)", initialCapacity, maxCapacity));
        }
    }
    
    protected abstract ByteBuf newHeapBuffer(final int p0, final int p1);
    
    protected abstract ByteBuf newDirectBuffer(final int p0, final int p1);
    
    @Override
    public String toString() {
        return StringUtil.simpleClassName(this) + "(directByDefault: " + this.directByDefault + ')';
    }
    
    @Override
    public int calculateNewCapacity(final int minNewCapacity, final int maxCapacity) {
        if (minNewCapacity < 0) {
            throw new IllegalArgumentException("minNewCapacity: " + minNewCapacity + " (expectd: 0+)");
        }
        if (minNewCapacity > maxCapacity) {
            throw new IllegalArgumentException(String.format("minNewCapacity: %d (expected: not greater than maxCapacity(%d)", minNewCapacity, maxCapacity));
        }
        final int threshold = 4194304;
        if (minNewCapacity == 4194304) {
            return 4194304;
        }
        if (minNewCapacity > 4194304) {
            int newCapacity = minNewCapacity / 4194304 * 4194304;
            if (newCapacity > maxCapacity - 4194304) {
                newCapacity = maxCapacity;
            }
            else {
                newCapacity += 4194304;
            }
            return newCapacity;
        }
        int newCapacity;
        for (newCapacity = 64; newCapacity < minNewCapacity; newCapacity <<= 1) {}
        return Math.min(newCapacity, maxCapacity);
    }
}
