// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.resolver.dns;

import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.List;
import io.netty.util.internal.SocketUtils;
import io.netty.util.NetUtil;
import io.netty.util.internal.StringUtil;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.io.IOException;
import java.io.File;
import java.util.Map;
import io.netty.util.internal.logging.InternalLogger;

public final class UnixResolverDnsServerAddressStreamProvider implements DnsServerAddressStreamProvider
{
    private static final InternalLogger logger;
    private static final String NAMESERVER_ROW_LABEL = "nameserver";
    private static final String SORTLIST_ROW_LABEL = "sortlist";
    private static final String DOMAIN_ROW_LABEL = "domain";
    private static final String PORT_ROW_LABEL = "port";
    private final DnsServerAddresses defaultNameServerAddresses;
    private final Map<String, DnsServerAddresses> domainToNameServerStreamMap;
    
    public static DnsServerAddressStreamProvider parseSilently() {
        try {
            final UnixResolverDnsServerAddressStreamProvider nameServerCache = new UnixResolverDnsServerAddressStreamProvider("/etc/resolv.conf", "/etc/resolver");
            return nameServerCache.mayOverrideNameServers() ? nameServerCache : NoopDnsServerAddressStreamProvider.INSTANCE;
        }
        catch (final Exception e) {
            UnixResolverDnsServerAddressStreamProvider.logger.debug("failed to parse /etc/resolv.conf and/or /etc/resolver", e);
            return NoopDnsServerAddressStreamProvider.INSTANCE;
        }
    }
    
    public UnixResolverDnsServerAddressStreamProvider(final File etcResolvConf, final File... etcResolverFiles) throws IOException {
        if (etcResolvConf == null && (etcResolverFiles == null || etcResolverFiles.length == 0)) {
            throw new IllegalArgumentException("no files to parse");
        }
        if (etcResolverFiles != null) {
            this.domainToNameServerStreamMap = parse(etcResolverFiles);
            if (etcResolvConf != null) {
                final Map<String, DnsServerAddresses> etcResolvConfMap = parse(etcResolvConf);
                this.defaultNameServerAddresses = etcResolvConfMap.remove(etcResolvConf.getName());
                this.domainToNameServerStreamMap.putAll(etcResolvConfMap);
            }
            else {
                this.defaultNameServerAddresses = null;
            }
        }
        else {
            this.domainToNameServerStreamMap = parse(etcResolvConf);
            this.defaultNameServerAddresses = this.domainToNameServerStreamMap.remove(etcResolvConf.getName());
        }
    }
    
    public UnixResolverDnsServerAddressStreamProvider(final String etcResolvConf, final String etcResolverDir) throws IOException {
        this((etcResolvConf == null) ? null : new File(etcResolvConf), (File[])((etcResolverDir == null) ? null : new File(etcResolverDir).listFiles()));
    }
    
    @Override
    public DnsServerAddressStream nameServerAddressStream(String hostname) {
        while (true) {
            final int i = hostname.indexOf(46, 1);
            if (i < 0 || i == hostname.length() - 1) {
                return (this.defaultNameServerAddresses != null) ? this.defaultNameServerAddresses.stream() : null;
            }
            final DnsServerAddresses addresses = this.domainToNameServerStreamMap.get(hostname);
            if (addresses != null) {
                return addresses.stream();
            }
            hostname = hostname.substring(i + 1);
        }
    }
    
    boolean mayOverrideNameServers() {
        return !this.domainToNameServerStreamMap.isEmpty() || (this.defaultNameServerAddresses != null && this.defaultNameServerAddresses.stream().next() != null);
    }
    
    private static Map<String, DnsServerAddresses> parse(final File... etcResolverFiles) throws IOException {
        final Map<String, DnsServerAddresses> domainToNameServerStreamMap = new HashMap<String, DnsServerAddresses>(etcResolverFiles.length << 1);
        for (final File etcResolverFile : etcResolverFiles) {
            if (etcResolverFile.isFile()) {
                final FileReader fr = new FileReader(etcResolverFile);
                BufferedReader br = null;
                try {
                    br = new BufferedReader(fr);
                    List<InetSocketAddress> addresses = new ArrayList<InetSocketAddress>(2);
                    String domainName = etcResolverFile.getName();
                    int port = 53;
                    String line;
                    while ((line = br.readLine()) != null) {
                        line = line.trim();
                        final char c;
                        if (!line.isEmpty() && (c = line.charAt(0)) != '#') {
                            if (c == ';') {
                                continue;
                            }
                            if (line.startsWith("nameserver")) {
                                int i = StringUtil.indexOfNonWhiteSpace(line, "nameserver".length());
                                if (i < 0) {
                                    throw new IllegalArgumentException("error parsing label nameserver in file " + etcResolverFile + ". value: " + line);
                                }
                                String maybeIP = line.substring(i);
                                if (!NetUtil.isValidIpV4Address(maybeIP) && !NetUtil.isValidIpV6Address(maybeIP)) {
                                    i = maybeIP.lastIndexOf(46);
                                    if (i + 1 >= maybeIP.length()) {
                                        throw new IllegalArgumentException("error parsing label nameserver in file " + etcResolverFile + ". invalid IP value: " + line);
                                    }
                                    port = Integer.parseInt(maybeIP.substring(i + 1));
                                    maybeIP = maybeIP.substring(0, i);
                                }
                                addresses.add(new InetSocketAddress(SocketUtils.addressByName(maybeIP), port));
                            }
                            else if (line.startsWith("domain")) {
                                final int i = StringUtil.indexOfNonWhiteSpace(line, "domain".length());
                                if (i < 0) {
                                    throw new IllegalArgumentException("error parsing label domain in file " + etcResolverFile + " value: " + line);
                                }
                                domainName = line.substring(i);
                                if (addresses != null && !addresses.isEmpty()) {
                                    putIfAbsent(domainToNameServerStreamMap, domainName, addresses);
                                }
                                addresses = new ArrayList<InetSocketAddress>(2);
                            }
                            else if (line.startsWith("port")) {
                                final int i = StringUtil.indexOfNonWhiteSpace(line, "port".length());
                                if (i < 0) {
                                    throw new IllegalArgumentException("error parsing label port in file " + etcResolverFile + " value: " + line);
                                }
                                port = Integer.parseInt(line.substring(i));
                            }
                            else {
                                if (!line.startsWith("sortlist")) {
                                    continue;
                                }
                                UnixResolverDnsServerAddressStreamProvider.logger.info("row type {} not supported. ignoring line: {}", "sortlist", line);
                            }
                        }
                    }
                    if (addresses != null && !addresses.isEmpty()) {
                        putIfAbsent(domainToNameServerStreamMap, domainName, addresses);
                    }
                }
                finally {
                    if (br == null) {
                        fr.close();
                    }
                    else {
                        br.close();
                    }
                }
            }
        }
        return domainToNameServerStreamMap;
    }
    
    private static void putIfAbsent(final Map<String, DnsServerAddresses> domainToNameServerStreamMap, final String domainName, final List<InetSocketAddress> addresses) {
        putIfAbsent(domainToNameServerStreamMap, domainName, DnsServerAddresses.shuffled(addresses));
    }
    
    private static void putIfAbsent(final Map<String, DnsServerAddresses> domainToNameServerStreamMap, final String domainName, final DnsServerAddresses addresses) {
        final DnsServerAddresses existingAddresses = domainToNameServerStreamMap.put(domainName, addresses);
        if (existingAddresses != null) {
            domainToNameServerStreamMap.put(domainName, existingAddresses);
            UnixResolverDnsServerAddressStreamProvider.logger.debug("Domain name {} already maps to addresses {} so new addresses {} will be discarded", domainName, existingAddresses, addresses);
        }
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(UnixResolverDnsServerAddressStreamProvider.class);
    }
}
