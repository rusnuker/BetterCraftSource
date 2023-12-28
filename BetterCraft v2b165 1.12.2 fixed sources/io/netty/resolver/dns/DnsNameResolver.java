// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.resolver.dns;

import io.netty.bootstrap.AbstractBootstrap;
import io.netty.util.ReferenceCountUtil;
import io.netty.handler.codec.dns.DatagramDnsResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.lang.reflect.Method;
import io.netty.util.internal.EmptyArrays;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.net.InetSocketAddress;
import io.netty.handler.codec.dns.DnsResponse;
import io.netty.channel.AddressedEnvelope;
import io.netty.handler.codec.dns.DnsQuestion;
import io.netty.util.internal.StringUtil;
import java.net.IDN;
import java.util.Collections;
import io.netty.util.NetUtil;
import io.netty.handler.codec.dns.DnsRawRecord;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import io.netty.util.concurrent.Promise;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.channel.FixedRecvByteBufAllocator;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.bootstrap.Bootstrap;
import io.netty.util.internal.ObjectUtil;
import io.netty.util.concurrent.EventExecutor;
import io.netty.channel.ChannelFactory;
import io.netty.channel.EventLoop;
import io.netty.resolver.HostsFileEntriesResolver;
import io.netty.util.concurrent.FastThreadLocal;
import io.netty.channel.socket.DatagramChannel;
import io.netty.channel.Channel;
import io.netty.util.concurrent.Future;
import io.netty.handler.codec.dns.DatagramDnsQueryEncoder;
import io.netty.handler.codec.dns.DatagramDnsResponseDecoder;
import io.netty.resolver.ResolvedAddressTypes;
import io.netty.channel.socket.InternetProtocolFamily;
import io.netty.handler.codec.dns.DnsRecordType;
import io.netty.handler.codec.dns.DnsRecord;
import java.net.InetAddress;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.resolver.InetNameResolver;

public class DnsNameResolver extends InetNameResolver
{
    private static final InternalLogger logger;
    private static final String LOCALHOST = "localhost";
    private static final InetAddress LOCALHOST_ADDRESS;
    private static final DnsRecord[] EMPTY_ADDITIONALS;
    private static final DnsRecordType[] IPV4_ONLY_RESOLVED_RECORD_TYPES;
    private static final InternetProtocolFamily[] IPV4_ONLY_RESOLVED_PROTOCOL_FAMILIES;
    private static final DnsRecordType[] IPV4_PREFERRED_RESOLVED_RECORD_TYPES;
    private static final InternetProtocolFamily[] IPV4_PREFERRED_RESOLVED_PROTOCOL_FAMILIES;
    private static final DnsRecordType[] IPV6_ONLY_RESOLVED_RECORD_TYPES;
    private static final InternetProtocolFamily[] IPV6_ONLY_RESOLVED_PROTOCOL_FAMILIES;
    private static final DnsRecordType[] IPV6_PREFERRED_RESOLVED_RECORD_TYPES;
    private static final InternetProtocolFamily[] IPV6_PREFERRED_RESOLVED_PROTOCOL_FAMILIES;
    static final ResolvedAddressTypes DEFAULT_RESOLVE_ADDRESS_TYPES;
    static final String[] DEFAULT_SEARCH_DOMAINS;
    private static final DatagramDnsResponseDecoder DECODER;
    private static final DatagramDnsQueryEncoder ENCODER;
    final DnsServerAddresses nameServerAddresses;
    final Future<Channel> channelFuture;
    final DatagramChannel ch;
    final DnsQueryContextManager queryContextManager;
    private final DnsCache resolveCache;
    private final DnsCache authoritativeDnsServerCache;
    private final FastThreadLocal<DnsServerAddressStream> nameServerAddrStream;
    private final long queryTimeoutMillis;
    private final int maxQueriesPerResolve;
    private final boolean traceEnabled;
    private final ResolvedAddressTypes resolvedAddressTypes;
    private final InternetProtocolFamily[] resolvedInternetProtocolFamilies;
    private final boolean recursionDesired;
    private final int maxPayloadSize;
    private final boolean optResourceEnabled;
    private final HostsFileEntriesResolver hostsFileEntriesResolver;
    private final DnsServerAddressStreamProvider dnsServerAddressStreamProvider;
    private final String[] searchDomains;
    private final int ndots;
    private final boolean supportsAAAARecords;
    private final boolean supportsARecords;
    private final InternetProtocolFamily preferredAddressType;
    private final DnsRecordType[] resolveRecordTypes;
    private final boolean decodeIdn;
    
    public DnsNameResolver(final EventLoop eventLoop, final ChannelFactory<? extends DatagramChannel> channelFactory, final DnsServerAddresses nameServerAddresses, final DnsCache resolveCache, final DnsCache authoritativeDnsServerCache, final long queryTimeoutMillis, final ResolvedAddressTypes resolvedAddressTypes, final boolean recursionDesired, final int maxQueriesPerResolve, final boolean traceEnabled, final int maxPayloadSize, final boolean optResourceEnabled, final HostsFileEntriesResolver hostsFileEntriesResolver, final DnsServerAddressStreamProvider dnsServerAddressStreamProvider, final String[] searchDomains, final int ndots, final boolean decodeIdn) {
        super(eventLoop);
        this.queryContextManager = new DnsQueryContextManager();
        this.nameServerAddrStream = new FastThreadLocal<DnsServerAddressStream>() {
            @Override
            protected DnsServerAddressStream initialValue() throws Exception {
                return DnsNameResolver.this.nameServerAddresses.stream();
            }
        };
        ObjectUtil.checkNotNull(channelFactory, "channelFactory");
        this.nameServerAddresses = ObjectUtil.checkNotNull(nameServerAddresses, "nameServerAddresses");
        this.queryTimeoutMillis = ObjectUtil.checkPositive(queryTimeoutMillis, "queryTimeoutMillis");
        this.resolvedAddressTypes = ((resolvedAddressTypes != null) ? resolvedAddressTypes : DnsNameResolver.DEFAULT_RESOLVE_ADDRESS_TYPES);
        this.recursionDesired = recursionDesired;
        this.maxQueriesPerResolve = ObjectUtil.checkPositive(maxQueriesPerResolve, "maxQueriesPerResolve");
        this.traceEnabled = traceEnabled;
        this.maxPayloadSize = ObjectUtil.checkPositive(maxPayloadSize, "maxPayloadSize");
        this.optResourceEnabled = optResourceEnabled;
        this.hostsFileEntriesResolver = ObjectUtil.checkNotNull(hostsFileEntriesResolver, "hostsFileEntriesResolver");
        this.dnsServerAddressStreamProvider = ObjectUtil.checkNotNull(dnsServerAddressStreamProvider, "dnsServerAddressStreamProvider");
        this.resolveCache = ObjectUtil.checkNotNull(resolveCache, "resolveCache");
        this.authoritativeDnsServerCache = ObjectUtil.checkNotNull(authoritativeDnsServerCache, "authoritativeDnsServerCache");
        this.searchDomains = ObjectUtil.checkNotNull(searchDomains, "searchDomains").clone();
        this.ndots = ObjectUtil.checkPositiveOrZero(ndots, "ndots");
        this.decodeIdn = decodeIdn;
        switch (this.resolvedAddressTypes) {
            case IPV4_ONLY: {
                this.supportsAAAARecords = false;
                this.supportsARecords = true;
                this.resolveRecordTypes = DnsNameResolver.IPV4_ONLY_RESOLVED_RECORD_TYPES;
                this.resolvedInternetProtocolFamilies = DnsNameResolver.IPV4_ONLY_RESOLVED_PROTOCOL_FAMILIES;
                this.preferredAddressType = InternetProtocolFamily.IPv4;
                break;
            }
            case IPV4_PREFERRED: {
                this.supportsAAAARecords = true;
                this.supportsARecords = true;
                this.resolveRecordTypes = DnsNameResolver.IPV4_PREFERRED_RESOLVED_RECORD_TYPES;
                this.resolvedInternetProtocolFamilies = DnsNameResolver.IPV4_PREFERRED_RESOLVED_PROTOCOL_FAMILIES;
                this.preferredAddressType = InternetProtocolFamily.IPv4;
                break;
            }
            case IPV6_ONLY: {
                this.supportsAAAARecords = true;
                this.supportsARecords = false;
                this.resolveRecordTypes = DnsNameResolver.IPV6_ONLY_RESOLVED_RECORD_TYPES;
                this.resolvedInternetProtocolFamilies = DnsNameResolver.IPV6_ONLY_RESOLVED_PROTOCOL_FAMILIES;
                this.preferredAddressType = InternetProtocolFamily.IPv6;
                break;
            }
            case IPV6_PREFERRED: {
                this.supportsAAAARecords = true;
                this.supportsARecords = true;
                this.resolveRecordTypes = DnsNameResolver.IPV6_PREFERRED_RESOLVED_RECORD_TYPES;
                this.resolvedInternetProtocolFamilies = DnsNameResolver.IPV6_PREFERRED_RESOLVED_PROTOCOL_FAMILIES;
                this.preferredAddressType = InternetProtocolFamily.IPv6;
                break;
            }
            default: {
                throw new IllegalArgumentException("Unknown ResolvedAddressTypes " + resolvedAddressTypes);
            }
        }
        final Bootstrap b = new Bootstrap();
        b.group(this.executor());
        b.channelFactory(channelFactory);
        ((AbstractBootstrap<AbstractBootstrap, Channel>)b).option(ChannelOption.DATAGRAM_CHANNEL_ACTIVE_ON_REGISTRATION, true);
        final DnsResponseHandler responseHandler = new DnsResponseHandler(this.executor().newPromise());
        b.handler(new ChannelInitializer<DatagramChannel>() {
            @Override
            protected void initChannel(final DatagramChannel ch) throws Exception {
                ch.pipeline().addLast(DnsNameResolver.DECODER, DnsNameResolver.ENCODER, responseHandler);
            }
        });
        this.channelFuture = responseHandler.channelActivePromise;
        this.ch = (DatagramChannel)b.register().channel();
        this.ch.config().setRecvByteBufAllocator((RecvByteBufAllocator)new FixedRecvByteBufAllocator(maxPayloadSize));
        this.ch.closeFuture().addListener((GenericFutureListener<? extends Future<? super Void>>)new ChannelFutureListener() {
            @Override
            public void operationComplete(final ChannelFuture future) throws Exception {
                resolveCache.clear();
            }
        });
    }
    
    int dnsRedirectPort(final InetAddress server) {
        return 53;
    }
    
    public DnsCache resolveCache() {
        return this.resolveCache;
    }
    
    public DnsCache authoritativeDnsServerCache() {
        return this.authoritativeDnsServerCache;
    }
    
    public long queryTimeoutMillis() {
        return this.queryTimeoutMillis;
    }
    
    public ResolvedAddressTypes resolvedAddressTypes() {
        return this.resolvedAddressTypes;
    }
    
    InternetProtocolFamily[] resolvedInternetProtocolFamiliesUnsafe() {
        return this.resolvedInternetProtocolFamilies;
    }
    
    final String[] searchDomains() {
        return this.searchDomains;
    }
    
    final int ndots() {
        return this.ndots;
    }
    
    final boolean supportsAAAARecords() {
        return this.supportsAAAARecords;
    }
    
    final boolean supportsARecords() {
        return this.supportsARecords;
    }
    
    final InternetProtocolFamily preferredAddressType() {
        return this.preferredAddressType;
    }
    
    final DnsRecordType[] resolveRecordTypes() {
        return this.resolveRecordTypes;
    }
    
    final boolean isDecodeIdn() {
        return this.decodeIdn;
    }
    
    public boolean isRecursionDesired() {
        return this.recursionDesired;
    }
    
    public int maxQueriesPerResolve() {
        return this.maxQueriesPerResolve;
    }
    
    public boolean isTraceEnabled() {
        return this.traceEnabled;
    }
    
    public int maxPayloadSize() {
        return this.maxPayloadSize;
    }
    
    public boolean isOptResourceEnabled() {
        return this.optResourceEnabled;
    }
    
    public HostsFileEntriesResolver hostsFileEntriesResolver() {
        return this.hostsFileEntriesResolver;
    }
    
    @Override
    public void close() {
        if (this.ch.isOpen()) {
            this.ch.close();
        }
    }
    
    @Override
    protected EventLoop executor() {
        return (EventLoop)super.executor();
    }
    
    private InetAddress resolveHostsFileEntry(final String hostname) {
        if (this.hostsFileEntriesResolver == null) {
            return null;
        }
        final InetAddress address = this.hostsFileEntriesResolver.address(hostname, this.resolvedAddressTypes);
        if (address == null && PlatformDependent.isWindows() && "localhost".equalsIgnoreCase(hostname)) {
            return DnsNameResolver.LOCALHOST_ADDRESS;
        }
        return address;
    }
    
    public final Future<InetAddress> resolve(final String inetHost, final Iterable<DnsRecord> additionals) {
        return this.resolve(inetHost, additionals, this.executor().newPromise());
    }
    
    public final Future<InetAddress> resolve(final String inetHost, final Iterable<DnsRecord> additionals, final Promise<InetAddress> promise) {
        ObjectUtil.checkNotNull(promise, "promise");
        final DnsRecord[] additionalsArray = toArray(additionals, true);
        try {
            this.doResolve(inetHost, additionalsArray, promise, this.resolveCache);
            return promise;
        }
        catch (final Exception e) {
            return promise.setFailure(e);
        }
    }
    
    public final Future<List<InetAddress>> resolveAll(final String inetHost, final Iterable<DnsRecord> additionals) {
        return this.resolveAll(inetHost, additionals, this.executor().newPromise());
    }
    
    public final Future<List<InetAddress>> resolveAll(final String inetHost, final Iterable<DnsRecord> additionals, final Promise<List<InetAddress>> promise) {
        ObjectUtil.checkNotNull(promise, "promise");
        final DnsRecord[] additionalsArray = toArray(additionals, true);
        try {
            this.doResolveAll(inetHost, additionalsArray, promise, this.resolveCache);
            return promise;
        }
        catch (final Exception e) {
            return promise.setFailure(e);
        }
    }
    
    @Override
    protected void doResolve(final String inetHost, final Promise<InetAddress> promise) throws Exception {
        this.doResolve(inetHost, DnsNameResolver.EMPTY_ADDITIONALS, promise, this.resolveCache);
    }
    
    private static DnsRecord[] toArray(final Iterable<DnsRecord> additionals, final boolean validateType) {
        ObjectUtil.checkNotNull(additionals, "additionals");
        if (additionals instanceof Collection) {
            final Collection<DnsRecord> records = (Collection)additionals;
            for (final DnsRecord r : additionals) {
                validateAdditional(r, validateType);
            }
            return records.toArray(new DnsRecord[records.size()]);
        }
        final Iterator<DnsRecord> additionalsIt = additionals.iterator();
        if (!additionalsIt.hasNext()) {
            return DnsNameResolver.EMPTY_ADDITIONALS;
        }
        final List<DnsRecord> records2 = new ArrayList<DnsRecord>();
        do {
            final DnsRecord r = additionalsIt.next();
            validateAdditional(r, validateType);
            records2.add(r);
        } while (additionalsIt.hasNext());
        return records2.toArray(new DnsRecord[records2.size()]);
    }
    
    private static void validateAdditional(final DnsRecord record, final boolean validateType) {
        ObjectUtil.checkNotNull(record, "record");
        if (validateType && record instanceof DnsRawRecord) {
            throw new IllegalArgumentException("DnsRawRecord implementations not allowed: " + record);
        }
    }
    
    private InetAddress loopbackAddress() {
        return (this.preferredAddressType() == InternetProtocolFamily.IPv4) ? NetUtil.LOCALHOST4 : NetUtil.LOCALHOST6;
    }
    
    protected void doResolve(final String inetHost, final DnsRecord[] additionals, final Promise<InetAddress> promise, final DnsCache resolveCache) throws Exception {
        if (inetHost == null || inetHost.isEmpty()) {
            promise.setSuccess(this.loopbackAddress());
            return;
        }
        final byte[] bytes = NetUtil.createByteArrayFromIpAddressString(inetHost);
        if (bytes != null) {
            promise.setSuccess(InetAddress.getByAddress(bytes));
            return;
        }
        final String hostname = hostname(inetHost);
        final InetAddress hostsFileEntry = this.resolveHostsFileEntry(hostname);
        if (hostsFileEntry != null) {
            promise.setSuccess(hostsFileEntry);
            return;
        }
        if (!this.doResolveCached(hostname, additionals, promise, resolveCache)) {
            this.doResolveUncached(hostname, additionals, promise, resolveCache);
        }
    }
    
    private boolean doResolveCached(final String hostname, final DnsRecord[] additionals, final Promise<InetAddress> promise, final DnsCache resolveCache) {
        final List<DnsCacheEntry> cachedEntries = resolveCache.get(hostname, additionals);
        if (cachedEntries == null || cachedEntries.isEmpty()) {
            return false;
        }
        InetAddress address = null;
        Throwable cause = null;
        synchronized (cachedEntries) {
            final int numEntries = cachedEntries.size();
            assert numEntries > 0;
            if (cachedEntries.get(0).cause() != null) {
                cause = cachedEntries.get(0).cause();
            }
            else {
                for (final InternetProtocolFamily f : this.resolvedInternetProtocolFamilies) {
                    for (int i = 0; i < numEntries; ++i) {
                        final DnsCacheEntry e = cachedEntries.get(i);
                        if (f.addressType().isInstance(e.address())) {
                            address = e.address();
                            break;
                        }
                    }
                }
            }
        }
        if (address != null) {
            trySuccess(promise, address);
            return true;
        }
        if (cause != null) {
            tryFailure(promise, cause);
            return true;
        }
        return false;
    }
    
    private static <T> void trySuccess(final Promise<T> promise, final T result) {
        if (!promise.trySuccess(result)) {
            DnsNameResolver.logger.warn("Failed to notify success ({}) to a promise: {}", result, promise);
        }
    }
    
    private static void tryFailure(final Promise<?> promise, final Throwable cause) {
        if (!promise.tryFailure(cause)) {
            DnsNameResolver.logger.warn("Failed to notify failure to a promise: {}", promise, cause);
        }
    }
    
    private void doResolveUncached(final String hostname, final DnsRecord[] additionals, final Promise<InetAddress> promise, final DnsCache resolveCache) {
        final DnsServerAddressStream dnsServerAddressStream = this.dnsServerAddressStreamProvider.nameServerAddressStream(hostname);
        final SingleResolverContext ctx = (dnsServerAddressStream == null) ? new SingleResolverContext(this, hostname, additionals, resolveCache, this.nameServerAddresses.stream()) : new SingleResolverContext(this, hostname, additionals, resolveCache, dnsServerAddressStream);
        ctx.resolve(promise);
    }
    
    @Override
    protected void doResolveAll(final String inetHost, final Promise<List<InetAddress>> promise) throws Exception {
        this.doResolveAll(inetHost, DnsNameResolver.EMPTY_ADDITIONALS, promise, this.resolveCache);
    }
    
    protected void doResolveAll(final String inetHost, final DnsRecord[] additionals, final Promise<List<InetAddress>> promise, final DnsCache resolveCache) throws Exception {
        if (inetHost == null || inetHost.isEmpty()) {
            promise.setSuccess(Collections.singletonList(this.loopbackAddress()));
            return;
        }
        final byte[] bytes = NetUtil.createByteArrayFromIpAddressString(inetHost);
        if (bytes != null) {
            promise.setSuccess(Collections.singletonList(InetAddress.getByAddress(bytes)));
            return;
        }
        final String hostname = hostname(inetHost);
        final InetAddress hostsFileEntry = this.resolveHostsFileEntry(hostname);
        if (hostsFileEntry != null) {
            promise.setSuccess(Collections.singletonList(hostsFileEntry));
            return;
        }
        if (!this.doResolveAllCached(hostname, additionals, promise, resolveCache)) {
            this.doResolveAllUncached(hostname, additionals, promise, resolveCache);
        }
    }
    
    private boolean doResolveAllCached(final String hostname, final DnsRecord[] additionals, final Promise<List<InetAddress>> promise, final DnsCache resolveCache) {
        final List<DnsCacheEntry> cachedEntries = resolveCache.get(hostname, additionals);
        if (cachedEntries == null || cachedEntries.isEmpty()) {
            return false;
        }
        List<InetAddress> result = null;
        Throwable cause = null;
        synchronized (cachedEntries) {
            final int numEntries = cachedEntries.size();
            assert numEntries > 0;
            if (cachedEntries.get(0).cause() != null) {
                cause = cachedEntries.get(0).cause();
            }
            else {
                for (final InternetProtocolFamily f : this.resolvedInternetProtocolFamilies) {
                    for (int i = 0; i < numEntries; ++i) {
                        final DnsCacheEntry e = cachedEntries.get(i);
                        if (f.addressType().isInstance(e.address())) {
                            if (result == null) {
                                result = new ArrayList<InetAddress>(numEntries);
                            }
                            result.add(e.address());
                        }
                    }
                }
            }
        }
        if (result != null) {
            trySuccess(promise, result);
            return true;
        }
        if (cause != null) {
            tryFailure(promise, cause);
            return true;
        }
        return false;
    }
    
    private void doResolveAllUncached(final String hostname, final DnsRecord[] additionals, final Promise<List<InetAddress>> promise, final DnsCache resolveCache) {
        final DnsServerAddressStream dnsServerAddressStream = this.dnsServerAddressStreamProvider.nameServerAddressStream(hostname);
        final ListResolverContext ctx = (dnsServerAddressStream == null) ? new ListResolverContext(this, hostname, additionals, resolveCache, this.nameServerAddresses.stream()) : new ListResolverContext(this, hostname, additionals, resolveCache, dnsServerAddressStream);
        ctx.resolve(promise);
    }
    
    private static String hostname(final String inetHost) {
        String hostname = IDN.toASCII(inetHost);
        if (StringUtil.endsWith(inetHost, '.') && !StringUtil.endsWith(hostname, '.')) {
            hostname += ".";
        }
        return hostname;
    }
    
    public Future<AddressedEnvelope<DnsResponse, InetSocketAddress>> query(final DnsQuestion question) {
        return this.query(this.nextNameServerAddress(), question);
    }
    
    public Future<AddressedEnvelope<DnsResponse, InetSocketAddress>> query(final DnsQuestion question, final Iterable<DnsRecord> additionals) {
        return this.query(this.nextNameServerAddress(), question, additionals);
    }
    
    public Future<AddressedEnvelope<DnsResponse, InetSocketAddress>> query(final DnsQuestion question, final Promise<AddressedEnvelope<? extends DnsResponse, InetSocketAddress>> promise) {
        return this.query(this.nextNameServerAddress(), question, (Iterable<DnsRecord>)Collections.emptyList(), promise);
    }
    
    private InetSocketAddress nextNameServerAddress() {
        return this.nameServerAddrStream.get().next();
    }
    
    public Future<AddressedEnvelope<DnsResponse, InetSocketAddress>> query(final InetSocketAddress nameServerAddr, final DnsQuestion question) {
        return this.query0(nameServerAddr, question, DnsNameResolver.EMPTY_ADDITIONALS, this.ch.eventLoop().newPromise());
    }
    
    public Future<AddressedEnvelope<DnsResponse, InetSocketAddress>> query(final InetSocketAddress nameServerAddr, final DnsQuestion question, final Iterable<DnsRecord> additionals) {
        return this.query0(nameServerAddr, question, toArray(additionals, false), this.ch.eventLoop().newPromise());
    }
    
    public Future<AddressedEnvelope<DnsResponse, InetSocketAddress>> query(final InetSocketAddress nameServerAddr, final DnsQuestion question, final Promise<AddressedEnvelope<? extends DnsResponse, InetSocketAddress>> promise) {
        return this.query0(nameServerAddr, question, DnsNameResolver.EMPTY_ADDITIONALS, promise);
    }
    
    public Future<AddressedEnvelope<DnsResponse, InetSocketAddress>> query(final InetSocketAddress nameServerAddr, final DnsQuestion question, final Iterable<DnsRecord> additionals, final Promise<AddressedEnvelope<? extends DnsResponse, InetSocketAddress>> promise) {
        return this.query0(nameServerAddr, question, toArray(additionals, false), promise);
    }
    
    Future<AddressedEnvelope<DnsResponse, InetSocketAddress>> query0(final InetSocketAddress nameServerAddr, final DnsQuestion question, final DnsRecord[] additionals, final Promise<AddressedEnvelope<? extends DnsResponse, InetSocketAddress>> promise) {
        final Promise<AddressedEnvelope<DnsResponse, InetSocketAddress>> castPromise = cast(ObjectUtil.checkNotNull(promise, "promise"));
        try {
            new DnsQueryContext(this, nameServerAddr, question, additionals, castPromise).query();
            return castPromise;
        }
        catch (final Exception e) {
            return castPromise.setFailure(e);
        }
    }
    
    private static Promise<AddressedEnvelope<DnsResponse, InetSocketAddress>> cast(final Promise<?> promise) {
        return (Promise<AddressedEnvelope<DnsResponse, InetSocketAddress>>)promise;
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(DnsNameResolver.class);
        EMPTY_ADDITIONALS = new DnsRecord[0];
        IPV4_ONLY_RESOLVED_RECORD_TYPES = new DnsRecordType[] { DnsRecordType.A };
        IPV4_ONLY_RESOLVED_PROTOCOL_FAMILIES = new InternetProtocolFamily[] { InternetProtocolFamily.IPv4 };
        IPV4_PREFERRED_RESOLVED_RECORD_TYPES = new DnsRecordType[] { DnsRecordType.A, DnsRecordType.AAAA };
        IPV4_PREFERRED_RESOLVED_PROTOCOL_FAMILIES = new InternetProtocolFamily[] { InternetProtocolFamily.IPv4, InternetProtocolFamily.IPv6 };
        IPV6_ONLY_RESOLVED_RECORD_TYPES = new DnsRecordType[] { DnsRecordType.AAAA };
        IPV6_ONLY_RESOLVED_PROTOCOL_FAMILIES = new InternetProtocolFamily[] { InternetProtocolFamily.IPv6 };
        IPV6_PREFERRED_RESOLVED_RECORD_TYPES = new DnsRecordType[] { DnsRecordType.AAAA, DnsRecordType.A };
        IPV6_PREFERRED_RESOLVED_PROTOCOL_FAMILIES = new InternetProtocolFamily[] { InternetProtocolFamily.IPv6, InternetProtocolFamily.IPv4 };
        if (NetUtil.isIpV4StackPreferred()) {
            DEFAULT_RESOLVE_ADDRESS_TYPES = ResolvedAddressTypes.IPV4_ONLY;
            LOCALHOST_ADDRESS = NetUtil.LOCALHOST4;
        }
        else if (NetUtil.isIpV6AddressesPreferred()) {
            DEFAULT_RESOLVE_ADDRESS_TYPES = ResolvedAddressTypes.IPV6_PREFERRED;
            LOCALHOST_ADDRESS = NetUtil.LOCALHOST6;
        }
        else {
            DEFAULT_RESOLVE_ADDRESS_TYPES = ResolvedAddressTypes.IPV4_PREFERRED;
            LOCALHOST_ADDRESS = NetUtil.LOCALHOST4;
        }
        String[] searchDomains;
        try {
            final Class<?> configClass = Class.forName("sun.net.dns.ResolverConfiguration");
            final Method open = configClass.getMethod("open", (Class<?>[])new Class[0]);
            final Method nameservers = configClass.getMethod("searchlist", (Class<?>[])new Class[0]);
            final Object instance = open.invoke(null, new Object[0]);
            final List<String> list = (List<String>)nameservers.invoke(instance, new Object[0]);
            searchDomains = list.toArray(new String[list.size()]);
        }
        catch (final Exception ignore) {
            searchDomains = EmptyArrays.EMPTY_STRINGS;
        }
        DEFAULT_SEARCH_DOMAINS = searchDomains;
        DECODER = new DatagramDnsResponseDecoder();
        ENCODER = new DatagramDnsQueryEncoder();
    }
    
    static final class SingleResolverContext extends DnsNameResolverContext<InetAddress>
    {
        SingleResolverContext(final DnsNameResolver parent, final String hostname, final DnsRecord[] additionals, final DnsCache resolveCache, final DnsServerAddressStream nameServerAddrs) {
            super(parent, hostname, additionals, resolveCache, nameServerAddrs);
        }
        
        @Override
        DnsNameResolverContext<InetAddress> newResolverContext(final DnsNameResolver parent, final String hostname, final DnsRecord[] additionals, final DnsCache resolveCache, final DnsServerAddressStream nameServerAddrs) {
            return new SingleResolverContext(parent, hostname, additionals, resolveCache, nameServerAddrs);
        }
        
        @Override
        boolean finishResolve(final Class<? extends InetAddress> addressType, final List<DnsCacheEntry> resolvedEntries, final Promise<InetAddress> promise) {
            for (int numEntries = resolvedEntries.size(), i = 0; i < numEntries; ++i) {
                final InetAddress a = resolvedEntries.get(i).address();
                if (addressType.isInstance(a)) {
                    trySuccess(promise, a);
                    return true;
                }
            }
            return false;
        }
    }
    
    static final class ListResolverContext extends DnsNameResolverContext<List<InetAddress>>
    {
        ListResolverContext(final DnsNameResolver parent, final String hostname, final DnsRecord[] additionals, final DnsCache resolveCache, final DnsServerAddressStream nameServerAddrs) {
            super(parent, hostname, additionals, resolveCache, nameServerAddrs);
        }
        
        @Override
        DnsNameResolverContext<List<InetAddress>> newResolverContext(final DnsNameResolver parent, final String hostname, final DnsRecord[] additionals, final DnsCache resolveCache, final DnsServerAddressStream nameServerAddrs) {
            return new ListResolverContext(parent, hostname, additionals, resolveCache, nameServerAddrs);
        }
        
        @Override
        boolean finishResolve(final Class<? extends InetAddress> addressType, final List<DnsCacheEntry> resolvedEntries, final Promise<List<InetAddress>> promise) {
            List<InetAddress> result = null;
            for (int numEntries = resolvedEntries.size(), i = 0; i < numEntries; ++i) {
                final InetAddress a = resolvedEntries.get(i).address();
                if (addressType.isInstance(a)) {
                    if (result == null) {
                        result = new ArrayList<InetAddress>(numEntries);
                    }
                    result.add(a);
                }
            }
            if (result != null) {
                promise.trySuccess(result);
                return true;
            }
            return false;
        }
    }
    
    private final class DnsResponseHandler extends ChannelInboundHandlerAdapter
    {
        private final Promise<Channel> channelActivePromise;
        
        DnsResponseHandler(final Promise<Channel> channelActivePromise) {
            this.channelActivePromise = channelActivePromise;
        }
        
        @Override
        public void channelRead(final ChannelHandlerContext ctx, final Object msg) throws Exception {
            try {
                final DatagramDnsResponse res = (DatagramDnsResponse)msg;
                final int queryId = res.id();
                if (DnsNameResolver.logger.isDebugEnabled()) {
                    DnsNameResolver.logger.debug("{} RECEIVED: [{}: {}], {}", DnsNameResolver.this.ch, queryId, res.sender(), res);
                }
                final DnsQueryContext qCtx = DnsNameResolver.this.queryContextManager.get(res.sender(), queryId);
                if (qCtx == null) {
                    DnsNameResolver.logger.warn("{} Received a DNS response with an unknown ID: {}", DnsNameResolver.this.ch, queryId);
                    return;
                }
                qCtx.finish(res);
            }
            finally {
                ReferenceCountUtil.safeRelease(msg);
            }
        }
        
        @Override
        public void channelActive(final ChannelHandlerContext ctx) throws Exception {
            super.channelActive(ctx);
            this.channelActivePromise.setSuccess(ctx.channel());
        }
        
        @Override
        public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) throws Exception {
            DnsNameResolver.logger.warn("{} Unexpected exception: ", DnsNameResolver.this.ch, cause);
        }
    }
}
