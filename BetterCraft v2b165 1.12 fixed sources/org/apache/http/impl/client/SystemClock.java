// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.http.impl.client;

class SystemClock implements Clock
{
    public long getCurrentTime() {
        return System.currentTimeMillis();
    }
}
