// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.util.internal;

import java.util.concurrent.atomic.LongAdder;

final class LongAdderCounter extends LongAdder implements LongCounter
{
    @Override
    public long value() {
        return this.longValue();
    }
}
