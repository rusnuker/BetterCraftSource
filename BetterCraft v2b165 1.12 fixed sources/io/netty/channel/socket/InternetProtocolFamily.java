// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.channel.socket;

import java.net.Inet6Address;
import io.netty.util.NetUtil;
import java.net.Inet4Address;
import java.net.InetAddress;

public enum InternetProtocolFamily
{
    IPv4((Class<? extends InetAddress>)Inet4Address.class), 
    IPv6((Class<? extends InetAddress>)Inet6Address.class);
    
    private final Class<? extends InetAddress> addressType;
    private final int addressNumber;
    private final InetAddress localHost;
    
    private InternetProtocolFamily(final Class<? extends InetAddress> addressType) {
        this.addressType = addressType;
        this.addressNumber = addressNumber(addressType);
        this.localHost = localhost(addressType);
    }
    
    public Class<? extends InetAddress> addressType() {
        return this.addressType;
    }
    
    public int addressNumber() {
        return this.addressNumber;
    }
    
    public InetAddress localhost() {
        return this.localHost;
    }
    
    private static InetAddress localhost(final Class<? extends InetAddress> addressType) {
        if (addressType.isAssignableFrom(Inet4Address.class)) {
            return NetUtil.LOCALHOST4;
        }
        if (addressType.isAssignableFrom(Inet6Address.class)) {
            return NetUtil.LOCALHOST6;
        }
        throw new Error();
    }
    
    private static int addressNumber(final Class<? extends InetAddress> addressType) {
        if (addressType.isAssignableFrom(Inet4Address.class)) {
            return 1;
        }
        if (addressType.isAssignableFrom(Inet6Address.class)) {
            return 2;
        }
        throw new IllegalArgumentException("addressType " + addressType + " not supported");
    }
    
    public static InternetProtocolFamily of(final InetAddress address) {
        if (address instanceof Inet4Address) {
            return InternetProtocolFamily.IPv4;
        }
        if (address instanceof Inet6Address) {
            return InternetProtocolFamily.IPv6;
        }
        throw new IllegalArgumentException("address " + address + " not supported");
    }
}
