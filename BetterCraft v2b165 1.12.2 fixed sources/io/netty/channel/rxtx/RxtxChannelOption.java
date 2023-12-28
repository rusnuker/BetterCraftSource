// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.channel.rxtx;

import io.netty.channel.ChannelOption;

public final class RxtxChannelOption<T> extends ChannelOption<T>
{
    public static final ChannelOption<Integer> BAUD_RATE;
    public static final ChannelOption<Boolean> DTR;
    public static final ChannelOption<Boolean> RTS;
    public static final ChannelOption<RxtxChannelConfig.Stopbits> STOP_BITS;
    public static final ChannelOption<RxtxChannelConfig.Databits> DATA_BITS;
    public static final ChannelOption<RxtxChannelConfig.Paritybit> PARITY_BIT;
    public static final ChannelOption<Integer> WAIT_TIME;
    public static final ChannelOption<Integer> READ_TIMEOUT;
    
    private RxtxChannelOption() {
        super(null);
    }
    
    static {
        BAUD_RATE = ChannelOption.valueOf(RxtxChannelOption.class, "BAUD_RATE");
        DTR = ChannelOption.valueOf(RxtxChannelOption.class, "DTR");
        RTS = ChannelOption.valueOf(RxtxChannelOption.class, "RTS");
        STOP_BITS = ChannelOption.valueOf(RxtxChannelOption.class, "STOP_BITS");
        DATA_BITS = ChannelOption.valueOf(RxtxChannelOption.class, "DATA_BITS");
        PARITY_BIT = ChannelOption.valueOf(RxtxChannelOption.class, "PARITY_BIT");
        WAIT_TIME = ChannelOption.valueOf(RxtxChannelOption.class, "WAIT_TIME");
        READ_TIMEOUT = ChannelOption.valueOf(RxtxChannelOption.class, "READ_TIMEOUT");
    }
}
