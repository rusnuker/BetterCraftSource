// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.util.concurrent;

import java.util.concurrent.atomic.AtomicInteger;

public final class DefaultEventExecutorChooserFactory implements EventExecutorChooserFactory
{
    public static final DefaultEventExecutorChooserFactory INSTANCE;
    
    private DefaultEventExecutorChooserFactory() {
    }
    
    @Override
    public EventExecutorChooser newChooser(final EventExecutor[] executors) {
        if (isPowerOfTwo(executors.length)) {
            return new PowerOfTwoEventExecutorChooser(executors);
        }
        return new GenericEventExecutorChooser(executors);
    }
    
    private static boolean isPowerOfTwo(final int val) {
        return (val & -val) == val;
    }
    
    static {
        INSTANCE = new DefaultEventExecutorChooserFactory();
    }
    
    private static final class PowerOfTwoEventExecutorChooser implements EventExecutorChooser
    {
        private final AtomicInteger idx;
        private final EventExecutor[] executors;
        
        PowerOfTwoEventExecutorChooser(final EventExecutor[] executors) {
            this.idx = new AtomicInteger();
            this.executors = executors;
        }
        
        @Override
        public EventExecutor next() {
            return this.executors[this.idx.getAndIncrement() & this.executors.length - 1];
        }
    }
    
    private static final class GenericEventExecutorChooser implements EventExecutorChooser
    {
        private final AtomicInteger idx;
        private final EventExecutor[] executors;
        
        GenericEventExecutorChooser(final EventExecutor[] executors) {
            this.idx = new AtomicInteger();
            this.executors = executors;
        }
        
        @Override
        public EventExecutor next() {
            return this.executors[Math.abs(this.idx.getAndIncrement() % this.executors.length)];
        }
    }
}
