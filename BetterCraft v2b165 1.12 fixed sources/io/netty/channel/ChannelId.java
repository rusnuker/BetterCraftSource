// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.channel;

import java.io.Serializable;

public interface ChannelId extends Serializable, Comparable<ChannelId>
{
    String asShortText();
    
    String asLongText();
}
