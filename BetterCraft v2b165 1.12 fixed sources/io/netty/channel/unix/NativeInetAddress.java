// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.channel.unix;

import java.net.UnknownHostException;
import java.net.InetSocketAddress;
import java.net.Inet6Address;
import java.net.InetAddress;

public final class NativeInetAddress
{
    private static final byte[] IPV4_MAPPED_IPV6_PREFIX;
    final byte[] address;
    final int scopeId;
    
    public static NativeInetAddress newInstance(final InetAddress addr) {
        final byte[] bytes = addr.getAddress();
        if (addr instanceof Inet6Address) {
            return new NativeInetAddress(bytes, ((Inet6Address)addr).getScopeId());
        }
        return new NativeInetAddress(ipv4MappedIpv6Address(bytes));
    }
    
    public NativeInetAddress(final byte[] address, final int scopeId) {
        this.address = address;
        this.scopeId = scopeId;
    }
    
    public NativeInetAddress(final byte[] address) {
        this(address, 0);
    }
    
    public byte[] address() {
        return this.address;
    }
    
    public int scopeId() {
        return this.scopeId;
    }
    
    public static byte[] ipv4MappedIpv6Address(final byte[] ipv4) {
        final byte[] address = new byte[16];
        System.arraycopy(NativeInetAddress.IPV4_MAPPED_IPV6_PREFIX, 0, address, 0, NativeInetAddress.IPV4_MAPPED_IPV6_PREFIX.length);
        System.arraycopy(ipv4, 0, address, 12, ipv4.length);
        return address;
    }
    
    public static InetSocketAddress address(final byte[] addr, final int offset, final int len) {
        final int port = decodeInt(addr, offset + len - 4);
        try {
            InetAddress address = null;
            switch (len) {
                case 8: {
                    final byte[] ipv4 = new byte[4];
                    System.arraycopy(addr, offset, ipv4, 0, 4);
                    address = InetAddress.getByAddress(ipv4);
                    break;
                }
                case 24: {
                    final byte[] ipv5 = new byte[16];
                    System.arraycopy(addr, offset, ipv5, 0, 16);
                    final int scopeId = decodeInt(addr, offset + len - 8);
                    address = Inet6Address.getByAddress(null, ipv5, scopeId);
                    break;
                }
                default: {
                    throw new Error();
                }
            }
            return new InetSocketAddress(address, port);
        }
        catch (final UnknownHostException e) {
            throw new Error("Should never happen", e);
        }
    }
    
    static int decodeInt(final byte[] addr, final int index) {
        return (addr[index] & 0xFF) << 24 | (addr[index + 1] & 0xFF) << 16 | (addr[index + 2] & 0xFF) << 8 | (addr[index + 3] & 0xFF);
    }
    
    static {
        IPV4_MAPPED_IPV6_PREFIX = new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, -1 };
    }
}
