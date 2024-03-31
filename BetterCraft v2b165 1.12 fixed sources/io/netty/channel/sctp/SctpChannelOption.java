// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.channel.sctp;

import java.net.SocketAddress;
import com.sun.nio.sctp.SctpStandardSocketOptions;
import io.netty.channel.ChannelOption;

public final class SctpChannelOption<T> extends ChannelOption<T>
{
    public static final ChannelOption<Boolean> SCTP_DISABLE_FRAGMENTS;
    public static final ChannelOption<Boolean> SCTP_EXPLICIT_COMPLETE;
    public static final ChannelOption<Integer> SCTP_FRAGMENT_INTERLEAVE;
    public static final ChannelOption<SctpStandardSocketOptions.InitMaxStreams> SCTP_INIT_MAXSTREAMS;
    public static final ChannelOption<Boolean> SCTP_NODELAY;
    public static final ChannelOption<SocketAddress> SCTP_PRIMARY_ADDR;
    public static final ChannelOption<SocketAddress> SCTP_SET_PEER_PRIMARY_ADDR;
    
    private SctpChannelOption() {
        super(null);
    }
    
    static {
        SCTP_DISABLE_FRAGMENTS = ChannelOption.valueOf(SctpChannelOption.class, "SCTP_DISABLE_FRAGMENTS");
        SCTP_EXPLICIT_COMPLETE = ChannelOption.valueOf(SctpChannelOption.class, "SCTP_EXPLICIT_COMPLETE");
        SCTP_FRAGMENT_INTERLEAVE = ChannelOption.valueOf(SctpChannelOption.class, "SCTP_FRAGMENT_INTERLEAVE");
        SCTP_INIT_MAXSTREAMS = ChannelOption.valueOf(SctpChannelOption.class, "SCTP_INIT_MAXSTREAMS");
        SCTP_NODELAY = ChannelOption.valueOf(SctpChannelOption.class, "SCTP_NODELAY");
        SCTP_PRIMARY_ADDR = ChannelOption.valueOf(SctpChannelOption.class, "SCTP_PRIMARY_ADDR");
        SCTP_SET_PEER_PRIMARY_ADDR = ChannelOption.valueOf(SctpChannelOption.class, "SCTP_SET_PEER_PRIMARY_ADDR");
    }
}
