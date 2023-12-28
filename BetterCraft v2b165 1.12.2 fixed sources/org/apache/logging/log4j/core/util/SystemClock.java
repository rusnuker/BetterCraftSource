// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.util;

public final class SystemClock implements Clock
{
    @Override
    public long currentTimeMillis() {
        return System.currentTimeMillis();
    }
}
