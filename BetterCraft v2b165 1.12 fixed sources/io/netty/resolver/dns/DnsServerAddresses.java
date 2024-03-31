// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.resolver.dns;

import java.lang.reflect.Method;
import java.util.Collections;
import io.netty.util.internal.SocketUtils;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collection;
import java.net.InetSocketAddress;
import java.util.List;
import io.netty.util.internal.logging.InternalLogger;

public abstract class DnsServerAddresses
{
    private static final InternalLogger logger;
    private static final List<InetSocketAddress> DEFAULT_NAME_SERVER_LIST;
    private static final InetSocketAddress[] DEFAULT_NAME_SERVER_ARRAY;
    private static final DnsServerAddresses DEFAULT_NAME_SERVERS;
    static final int DNS_PORT = 53;
    
    public static List<InetSocketAddress> defaultAddressList() {
        return DnsServerAddresses.DEFAULT_NAME_SERVER_LIST;
    }
    
    public static DnsServerAddresses defaultAddresses() {
        return DnsServerAddresses.DEFAULT_NAME_SERVERS;
    }
    
    public static DnsServerAddresses sequential(final Iterable<? extends InetSocketAddress> addresses) {
        return sequential0(sanitize(addresses));
    }
    
    public static DnsServerAddresses sequential(final InetSocketAddress... addresses) {
        return sequential0(sanitize(addresses));
    }
    
    private static DnsServerAddresses sequential0(final InetSocketAddress... addresses) {
        if (addresses.length == 1) {
            return singleton(addresses[0]);
        }
        return new DefaultDnsServerAddresses("sequential", addresses) {
            @Override
            public DnsServerAddressStream stream() {
                return new SequentialDnsServerAddressStream(this.addresses, 0);
            }
        };
    }
    
    public static DnsServerAddresses shuffled(final Iterable<? extends InetSocketAddress> addresses) {
        return shuffled0(sanitize(addresses));
    }
    
    public static DnsServerAddresses shuffled(final InetSocketAddress... addresses) {
        return shuffled0(sanitize(addresses));
    }
    
    private static DnsServerAddresses shuffled0(final InetSocketAddress[] addresses) {
        if (addresses.length == 1) {
            return singleton(addresses[0]);
        }
        return new DefaultDnsServerAddresses("shuffled", addresses) {
            @Override
            public DnsServerAddressStream stream() {
                return new ShuffledDnsServerAddressStream(this.addresses);
            }
        };
    }
    
    public static DnsServerAddresses rotational(final Iterable<? extends InetSocketAddress> addresses) {
        return rotational0(sanitize(addresses));
    }
    
    public static DnsServerAddresses rotational(final InetSocketAddress... addresses) {
        return rotational0(sanitize(addresses));
    }
    
    private static DnsServerAddresses rotational0(final InetSocketAddress[] addresses) {
        if (addresses.length == 1) {
            return singleton(addresses[0]);
        }
        return new RotationalDnsServerAddresses(addresses);
    }
    
    public static DnsServerAddresses singleton(final InetSocketAddress address) {
        if (address == null) {
            throw new NullPointerException("address");
        }
        if (address.isUnresolved()) {
            throw new IllegalArgumentException("cannot use an unresolved DNS server address: " + address);
        }
        return new SingletonDnsServerAddresses(address);
    }
    
    private static InetSocketAddress[] sanitize(final Iterable<? extends InetSocketAddress> addresses) {
        if (addresses == null) {
            throw new NullPointerException("addresses");
        }
        List<InetSocketAddress> list;
        if (addresses instanceof Collection) {
            list = new ArrayList<InetSocketAddress>(((Collection)addresses).size());
        }
        else {
            list = new ArrayList<InetSocketAddress>(4);
        }
        for (final InetSocketAddress a : addresses) {
            if (a == null) {
                break;
            }
            if (a.isUnresolved()) {
                throw new IllegalArgumentException("cannot use an unresolved DNS server address: " + a);
            }
            list.add(a);
        }
        if (list.isEmpty()) {
            throw new IllegalArgumentException("empty addresses");
        }
        return list.toArray(new InetSocketAddress[list.size()]);
    }
    
    private static InetSocketAddress[] sanitize(final InetSocketAddress[] addresses) {
        if (addresses == null) {
            throw new NullPointerException("addresses");
        }
        final List<InetSocketAddress> list = new ArrayList<InetSocketAddress>(addresses.length);
        for (final InetSocketAddress a : addresses) {
            if (a == null) {
                break;
            }
            if (a.isUnresolved()) {
                throw new IllegalArgumentException("cannot use an unresolved DNS server address: " + a);
            }
            list.add(a);
        }
        if (list.isEmpty()) {
            return DnsServerAddresses.DEFAULT_NAME_SERVER_ARRAY;
        }
        return list.toArray(new InetSocketAddress[list.size()]);
    }
    
    public abstract DnsServerAddressStream stream();
    
    static {
        logger = InternalLoggerFactory.getInstance(DnsServerAddresses.class);
        final List<InetSocketAddress> defaultNameServers = new ArrayList<InetSocketAddress>(2);
        try {
            final Class<?> configClass = Class.forName("sun.net.dns.ResolverConfiguration");
            final Method open = configClass.getMethod("open", (Class<?>[])new Class[0]);
            final Method nameservers = configClass.getMethod("nameservers", (Class<?>[])new Class[0]);
            final Object instance = open.invoke(null, new Object[0]);
            final List<String> list = (List<String>)nameservers.invoke(instance, new Object[0]);
            for (final String a : list) {
                if (a != null) {
                    defaultNameServers.add(new InetSocketAddress(SocketUtils.addressByName(a), 53));
                }
            }
        }
        catch (final Exception ex) {}
        if (!defaultNameServers.isEmpty()) {
            if (DnsServerAddresses.logger.isDebugEnabled()) {
                DnsServerAddresses.logger.debug("Default DNS servers: {} (sun.net.dns.ResolverConfiguration)", defaultNameServers);
            }
        }
        else {
            Collections.addAll(defaultNameServers, new InetSocketAddress[] { SocketUtils.socketAddress("8.8.8.8", 53), SocketUtils.socketAddress("8.8.4.4", 53) });
            if (DnsServerAddresses.logger.isWarnEnabled()) {
                DnsServerAddresses.logger.warn("Default DNS servers: {} (Google Public DNS as a fallback)", defaultNameServers);
            }
        }
        DEFAULT_NAME_SERVER_LIST = Collections.unmodifiableList((List<? extends InetSocketAddress>)defaultNameServers);
        DEFAULT_NAME_SERVER_ARRAY = defaultNameServers.toArray(new InetSocketAddress[defaultNameServers.size()]);
        DEFAULT_NAME_SERVERS = sequential(DnsServerAddresses.DEFAULT_NAME_SERVER_ARRAY);
    }
}
