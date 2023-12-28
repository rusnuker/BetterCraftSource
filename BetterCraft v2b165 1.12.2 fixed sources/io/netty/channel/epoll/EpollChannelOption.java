// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.channel.epoll;

import java.net.InetAddress;
import java.util.Map;
import io.netty.channel.unix.DomainSocketReadMode;
import io.netty.channel.ChannelOption;

public final class EpollChannelOption<T> extends ChannelOption<T>
{
    public static final ChannelOption<Boolean> TCP_CORK;
    public static final ChannelOption<Boolean> SO_REUSEPORT;
    public static final ChannelOption<Long> TCP_NOTSENT_LOWAT;
    public static final ChannelOption<Integer> TCP_KEEPIDLE;
    public static final ChannelOption<Integer> TCP_KEEPINTVL;
    public static final ChannelOption<Integer> TCP_KEEPCNT;
    public static final ChannelOption<Integer> TCP_USER_TIMEOUT;
    public static final ChannelOption<Boolean> IP_FREEBIND;
    public static final ChannelOption<Integer> TCP_FASTOPEN;
    public static final ChannelOption<Integer> TCP_DEFER_ACCEPT;
    public static final ChannelOption<Boolean> TCP_QUICKACK;
    public static final ChannelOption<DomainSocketReadMode> DOMAIN_SOCKET_READ_MODE;
    public static final ChannelOption<EpollMode> EPOLL_MODE;
    public static final ChannelOption<Map<InetAddress, byte[]>> TCP_MD5SIG;
    
    private EpollChannelOption() {
        super(null);
    }
    
    static {
        TCP_CORK = ChannelOption.valueOf(EpollChannelOption.class, "TCP_CORK");
        SO_REUSEPORT = ChannelOption.valueOf(EpollChannelOption.class, "SO_REUSEPORT");
        TCP_NOTSENT_LOWAT = ChannelOption.valueOf(EpollChannelOption.class, "TCP_NOTSENT_LOWAT");
        TCP_KEEPIDLE = ChannelOption.valueOf(EpollChannelOption.class, "TCP_KEEPIDLE");
        TCP_KEEPINTVL = ChannelOption.valueOf(EpollChannelOption.class, "TCP_KEEPINTVL");
        TCP_KEEPCNT = ChannelOption.valueOf(EpollChannelOption.class, "TCP_KEEPCNT");
        TCP_USER_TIMEOUT = ChannelOption.valueOf(EpollChannelOption.class, "TCP_USER_TIMEOUT");
        IP_FREEBIND = ChannelOption.valueOf("IP_FREEBIND");
        TCP_FASTOPEN = ChannelOption.valueOf(EpollChannelOption.class, "TCP_FASTOPEN");
        TCP_DEFER_ACCEPT = ChannelOption.valueOf(EpollChannelOption.class, "TCP_DEFER_ACCEPT");
        TCP_QUICKACK = ChannelOption.valueOf(EpollChannelOption.class, "TCP_QUICKACK");
        DOMAIN_SOCKET_READ_MODE = ChannelOption.valueOf(EpollChannelOption.class, "DOMAIN_SOCKET_READ_MODE");
        EPOLL_MODE = ChannelOption.valueOf(EpollChannelOption.class, "EPOLL_MODE");
        TCP_MD5SIG = ChannelOption.valueOf("TCP_MD5SIG");
    }
}
