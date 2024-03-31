// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.channel.udt;

import io.netty.channel.ChannelOption;

@Deprecated
public final class UdtChannelOption<T> extends ChannelOption<T>
{
    public static final ChannelOption<Integer> PROTOCOL_RECEIVE_BUFFER_SIZE;
    public static final ChannelOption<Integer> PROTOCOL_SEND_BUFFER_SIZE;
    public static final ChannelOption<Integer> SYSTEM_RECEIVE_BUFFER_SIZE;
    public static final ChannelOption<Integer> SYSTEM_SEND_BUFFER_SIZE;
    
    private UdtChannelOption() {
        super(null);
    }
    
    static {
        PROTOCOL_RECEIVE_BUFFER_SIZE = ChannelOption.valueOf(UdtChannelOption.class, "PROTOCOL_RECEIVE_BUFFER_SIZE");
        PROTOCOL_SEND_BUFFER_SIZE = ChannelOption.valueOf(UdtChannelOption.class, "PROTOCOL_SEND_BUFFER_SIZE");
        SYSTEM_RECEIVE_BUFFER_SIZE = ChannelOption.valueOf(UdtChannelOption.class, "SYSTEM_RECEIVE_BUFFER_SIZE");
        SYSTEM_SEND_BUFFER_SIZE = ChannelOption.valueOf(UdtChannelOption.class, "SYSTEM_SEND_BUFFER_SIZE");
    }
}
