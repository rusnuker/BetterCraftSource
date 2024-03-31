// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.channel;

public final class ChannelMetadata
{
    private final boolean hasDisconnect;
    private final int defaultMaxMessagesPerRead;
    
    public ChannelMetadata(final boolean hasDisconnect) {
        this(hasDisconnect, 1);
    }
    
    public ChannelMetadata(final boolean hasDisconnect, final int defaultMaxMessagesPerRead) {
        if (defaultMaxMessagesPerRead <= 0) {
            throw new IllegalArgumentException("defaultMaxMessagesPerRead: " + defaultMaxMessagesPerRead + " (expected > 0)");
        }
        this.hasDisconnect = hasDisconnect;
        this.defaultMaxMessagesPerRead = defaultMaxMessagesPerRead;
    }
    
    public boolean hasDisconnect() {
        return this.hasDisconnect;
    }
    
    public int defaultMaxMessagesPerRead() {
        return this.defaultMaxMessagesPerRead;
    }
}
