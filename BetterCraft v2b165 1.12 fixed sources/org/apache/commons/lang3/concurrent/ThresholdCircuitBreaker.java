// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.commons.lang3.concurrent;

import java.util.concurrent.atomic.AtomicLong;

public class ThresholdCircuitBreaker extends AbstractCircuitBreaker<Long>
{
    private static final long INITIAL_COUNT = 0L;
    private final long threshold;
    private final AtomicLong used;
    
    public ThresholdCircuitBreaker(final long threshold) {
        this.used = new AtomicLong(0L);
        this.threshold = threshold;
    }
    
    public long getThreshold() {
        return this.threshold;
    }
    
    @Override
    public boolean checkState() throws CircuitBreakingException {
        return this.isOpen();
    }
    
    @Override
    public void close() {
        super.close();
        this.used.set(0L);
    }
    
    @Override
    public boolean incrementAndCheckState(final Long increment) throws CircuitBreakingException {
        if (this.threshold == 0L) {
            this.open();
        }
        final long used = this.used.addAndGet(increment);
        if (used > this.threshold) {
            this.open();
        }
        return this.checkState();
    }
}
