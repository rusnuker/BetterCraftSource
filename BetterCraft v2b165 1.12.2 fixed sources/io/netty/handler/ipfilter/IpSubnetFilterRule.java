// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.ipfilter;

import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.net.Inet6Address;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import io.netty.util.internal.SocketUtils;

public final class IpSubnetFilterRule implements IpFilterRule
{
    private final IpFilterRule filterRule;
    
    public IpSubnetFilterRule(final String ipAddress, final int cidrPrefix, final IpFilterRuleType ruleType) {
        try {
            this.filterRule = selectFilterRule(SocketUtils.addressByName(ipAddress), cidrPrefix, ruleType);
        }
        catch (final UnknownHostException e) {
            throw new IllegalArgumentException("ipAddress", e);
        }
    }
    
    public IpSubnetFilterRule(final InetAddress ipAddress, final int cidrPrefix, final IpFilterRuleType ruleType) {
        this.filterRule = selectFilterRule(ipAddress, cidrPrefix, ruleType);
    }
    
    private static IpFilterRule selectFilterRule(final InetAddress ipAddress, final int cidrPrefix, final IpFilterRuleType ruleType) {
        if (ipAddress == null) {
            throw new NullPointerException("ipAddress");
        }
        if (ruleType == null) {
            throw new NullPointerException("ruleType");
        }
        if (ipAddress instanceof Inet4Address) {
            return new Ip4SubnetFilterRule((Inet4Address)ipAddress, cidrPrefix, ruleType);
        }
        if (ipAddress instanceof Inet6Address) {
            return new Ip6SubnetFilterRule((Inet6Address)ipAddress, cidrPrefix, ruleType);
        }
        throw new IllegalArgumentException("Only IPv4 and IPv6 addresses are supported");
    }
    
    @Override
    public boolean matches(final InetSocketAddress remoteAddress) {
        return this.filterRule.matches(remoteAddress);
    }
    
    @Override
    public IpFilterRuleType ruleType() {
        return this.filterRule.ruleType();
    }
    
    private static final class Ip4SubnetFilterRule implements IpFilterRule
    {
        private final int networkAddress;
        private final int subnetMask;
        private final IpFilterRuleType ruleType;
        
        private Ip4SubnetFilterRule(final Inet4Address ipAddress, final int cidrPrefix, final IpFilterRuleType ruleType) {
            if (cidrPrefix < 0 || cidrPrefix > 32) {
                throw new IllegalArgumentException(String.format("IPv4 requires the subnet prefix to be in range of [0,32]. The prefix was: %d", cidrPrefix));
            }
            this.subnetMask = prefixToSubnetMask(cidrPrefix);
            this.networkAddress = (ipToInt(ipAddress) & this.subnetMask);
            this.ruleType = ruleType;
        }
        
        @Override
        public boolean matches(final InetSocketAddress remoteAddress) {
            final int ipAddress = ipToInt((Inet4Address)remoteAddress.getAddress());
            return (ipAddress & this.subnetMask) == this.networkAddress;
        }
        
        @Override
        public IpFilterRuleType ruleType() {
            return this.ruleType;
        }
        
        private static int ipToInt(final Inet4Address ipAddress) {
            final byte[] octets = ipAddress.getAddress();
            assert octets.length == 4;
            return (octets[0] & 0xFF) << 24 | (octets[1] & 0xFF) << 16 | (octets[2] & 0xFF) << 8 | (octets[3] & 0xFF);
        }
        
        private static int prefixToSubnetMask(final int cidrPrefix) {
            return (int)(-1L << 32 - cidrPrefix & -1L);
        }
    }
    
    private static final class Ip6SubnetFilterRule implements IpFilterRule
    {
        private static final BigInteger MINUS_ONE;
        private final BigInteger networkAddress;
        private final BigInteger subnetMask;
        private final IpFilterRuleType ruleType;
        
        private Ip6SubnetFilterRule(final Inet6Address ipAddress, final int cidrPrefix, final IpFilterRuleType ruleType) {
            if (cidrPrefix < 0 || cidrPrefix > 128) {
                throw new IllegalArgumentException(String.format("IPv6 requires the subnet prefix to be in range of [0,128]. The prefix was: %d", cidrPrefix));
            }
            this.subnetMask = prefixToSubnetMask(cidrPrefix);
            this.networkAddress = ipToInt(ipAddress).and(this.subnetMask);
            this.ruleType = ruleType;
        }
        
        @Override
        public boolean matches(final InetSocketAddress remoteAddress) {
            final BigInteger ipAddress = ipToInt((Inet6Address)remoteAddress.getAddress());
            return ipAddress.and(this.subnetMask).equals(this.networkAddress);
        }
        
        @Override
        public IpFilterRuleType ruleType() {
            return this.ruleType;
        }
        
        private static BigInteger ipToInt(final Inet6Address ipAddress) {
            final byte[] octets = ipAddress.getAddress();
            assert octets.length == 16;
            return new BigInteger(octets);
        }
        
        private static BigInteger prefixToSubnetMask(final int cidrPrefix) {
            return Ip6SubnetFilterRule.MINUS_ONE.shiftLeft(128 - cidrPrefix);
        }
        
        static {
            MINUS_ONE = BigInteger.valueOf(-1L);
        }
    }
}
