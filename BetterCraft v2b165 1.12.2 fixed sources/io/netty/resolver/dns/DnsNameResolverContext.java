// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.resolver.dns;

import io.netty.handler.codec.dns.DefaultDnsQuestion;
import io.netty.handler.codec.CorruptedFrameException;
import io.netty.handler.codec.dns.DefaultDnsRecordDecoder;
import java.util.Iterator;
import java.net.Inet6Address;
import java.net.Inet4Address;
import java.util.HashMap;
import io.netty.buffer.ByteBuf;
import java.net.IDN;
import io.netty.buffer.ByteBufHolder;
import io.netty.handler.codec.dns.DnsRawRecord;
import java.util.Locale;
import java.util.ArrayList;
import io.netty.util.ReferenceCountUtil;
import io.netty.handler.codec.dns.DnsSection;
import io.netty.handler.codec.dns.DnsResponseCode;
import io.netty.handler.codec.dns.DnsQuestion;
import java.net.InetAddress;
import io.netty.handler.codec.dns.DnsRecordType;
import java.net.UnknownHostException;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.internal.StringUtil;
import io.netty.util.concurrent.Promise;
import java.util.Map;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import io.netty.util.concurrent.Future;
import java.util.Set;
import io.netty.handler.codec.dns.DnsRecord;
import io.netty.channel.socket.InternetProtocolFamily;
import java.net.InetSocketAddress;
import io.netty.handler.codec.dns.DnsResponse;
import io.netty.channel.AddressedEnvelope;
import io.netty.util.concurrent.FutureListener;

abstract class DnsNameResolverContext<T>
{
    private static final int INADDRSZ4 = 4;
    private static final int INADDRSZ6 = 16;
    private static final FutureListener<AddressedEnvelope<DnsResponse, InetSocketAddress>> RELEASE_RESPONSE;
    private final DnsNameResolver parent;
    private final DnsServerAddressStream nameServerAddrs;
    private final String hostname;
    protected String pristineHostname;
    private final DnsCache resolveCache;
    private final boolean traceEnabled;
    private final int maxAllowedQueries;
    private final InternetProtocolFamily[] resolvedInternetProtocolFamilies;
    private final DnsRecord[] additionals;
    private final Set<Future<AddressedEnvelope<DnsResponse, InetSocketAddress>>> queriesInProgress;
    private List<DnsCacheEntry> resolvedEntries;
    private StringBuilder trace;
    private int allowedQueries;
    private boolean triedCNAME;
    
    protected DnsNameResolverContext(final DnsNameResolver parent, final String hostname, final DnsRecord[] additionals, final DnsCache resolveCache, final DnsServerAddressStream nameServerAddrs) {
        this.queriesInProgress = Collections.newSetFromMap(new IdentityHashMap<Future<AddressedEnvelope<DnsResponse, InetSocketAddress>>, Boolean>());
        this.parent = parent;
        this.hostname = hostname;
        this.additionals = additionals;
        this.resolveCache = resolveCache;
        this.nameServerAddrs = nameServerAddrs;
        this.maxAllowedQueries = parent.maxQueriesPerResolve();
        this.resolvedInternetProtocolFamilies = parent.resolvedInternetProtocolFamiliesUnsafe();
        this.traceEnabled = parent.isTraceEnabled();
        this.allowedQueries = this.maxAllowedQueries;
    }
    
    void resolve(Promise<T> promise) {
        final boolean directSearch = this.parent.searchDomains().length == 0 || StringUtil.endsWith(this.hostname, '.');
        if (directSearch) {
            this.internalResolve(promise);
        }
        else {
            final Promise<T> original = promise;
            promise = this.parent.executor().newPromise();
            promise.addListener((GenericFutureListener<? extends Future<? super T>>)new FutureListener<T>() {
                int count;
                
                @Override
                public void operationComplete(final Future<T> future) throws Exception {
                    if (future.isSuccess()) {
                        original.trySuccess(future.getNow());
                    }
                    else if (this.count < DnsNameResolverContext.this.parent.searchDomains().length) {
                        final String searchDomain = DnsNameResolverContext.this.parent.searchDomains()[this.count++];
                        final Promise<T> nextPromise = DnsNameResolverContext.this.parent.executor().newPromise();
                        final String nextHostname = DnsNameResolverContext.this.hostname + '.' + searchDomain;
                        final DnsNameResolverContext<T> nextContext = DnsNameResolverContext.this.newResolverContext(DnsNameResolverContext.this.parent, nextHostname, DnsNameResolverContext.this.additionals, DnsNameResolverContext.this.resolveCache, DnsNameResolverContext.this.nameServerAddrs);
                        nextContext.pristineHostname = DnsNameResolverContext.this.hostname;
                        nextContext.internalResolve(nextPromise);
                        nextPromise.addListener((GenericFutureListener<? extends Future<? super T>>)this);
                    }
                    else {
                        original.tryFailure(future.cause());
                    }
                }
            });
            if (this.parent.ndots() == 0) {
                this.internalResolve(promise);
            }
            else {
                int dots = 0;
                for (int idx = this.hostname.length() - 1; idx >= 0; --idx) {
                    if (this.hostname.charAt(idx) == '.' && ++dots >= this.parent.ndots()) {
                        this.internalResolve(promise);
                        return;
                    }
                }
                promise.tryFailure(new UnknownHostException(this.hostname));
            }
        }
    }
    
    private void internalResolve(final Promise<T> promise) {
        final DnsServerAddressStream nameServerAddressStream = this.getNameServers(this.hostname);
        for (final DnsRecordType type : this.parent.resolveRecordTypes()) {
            if (!this.query(this.hostname, type, nameServerAddressStream, promise)) {
                return;
            }
        }
    }
    
    private void addNameServerToCache(final AuthoritativeNameServer name, final InetAddress resolved, final long ttl) {
        if (!name.isRootServer()) {
            this.parent.authoritativeDnsServerCache().cache(name.domainName(), this.additionals, resolved, ttl, this.parent.ch.eventLoop());
        }
    }
    
    private DnsServerAddressStream getNameServersFromCache(String hostname) {
        final int len = hostname.length();
        if (len == 0) {
            return null;
        }
        if (hostname.charAt(len - 1) != '.') {
            hostname += ".";
        }
        int idx = hostname.indexOf(46);
        if (idx == hostname.length() - 1) {
            return null;
        }
        while (true) {
            hostname = hostname.substring(idx + 1);
            final int idx2 = hostname.indexOf(46);
            if (idx2 <= 0 || idx2 == hostname.length() - 1) {
                return null;
            }
            idx = idx2;
            final List<DnsCacheEntry> entries = this.parent.authoritativeDnsServerCache().get(hostname, this.additionals);
            if (entries != null && !entries.isEmpty()) {
                return DnsServerAddresses.shuffled(new DnsCacheIterable(entries)).stream();
            }
        }
    }
    
    private void query(final DnsServerAddressStream nameServerAddrStream, final DnsQuestion question, final Promise<T> promise) {
        if (this.allowedQueries == 0 || promise.isCancelled()) {
            this.tryToFinishResolve(promise);
            return;
        }
        --this.allowedQueries;
        final Future<AddressedEnvelope<DnsResponse, InetSocketAddress>> f = this.parent.query0(nameServerAddrStream.next(), question, this.additionals, this.parent.ch.eventLoop().newPromise());
        this.queriesInProgress.add(f);
        f.addListener(new FutureListener<AddressedEnvelope<DnsResponse, InetSocketAddress>>() {
            @Override
            public void operationComplete(final Future<AddressedEnvelope<DnsResponse, InetSocketAddress>> future) {
                DnsNameResolverContext.this.queriesInProgress.remove(future);
                if (promise.isDone() || future.isCancelled()) {
                    return;
                }
                try {
                    if (future.isSuccess()) {
                        DnsNameResolverContext.this.onResponse(nameServerAddrStream, question, future.getNow(), promise);
                    }
                    else {
                        if (DnsNameResolverContext.this.traceEnabled) {
                            DnsNameResolverContext.this.addTrace(future.cause());
                        }
                        DnsNameResolverContext.this.query(nameServerAddrStream, question, promise);
                    }
                }
                finally {
                    DnsNameResolverContext.this.tryToFinishResolve(promise);
                }
            }
        });
    }
    
    void onResponse(final DnsServerAddressStream nameServerAddrStream, final DnsQuestion question, final AddressedEnvelope<DnsResponse, InetSocketAddress> envelope, final Promise<T> promise) {
        try {
            final DnsResponse res = envelope.content();
            final DnsResponseCode code = res.code();
            if (code == DnsResponseCode.NOERROR) {
                if (this.handleRedirect(question, envelope, promise)) {
                    return;
                }
                final DnsRecordType type = question.type();
                if (type == DnsRecordType.A || type == DnsRecordType.AAAA) {
                    this.onResponseAorAAAA(type, question, envelope, promise);
                }
                else if (type == DnsRecordType.CNAME) {
                    this.onResponseCNAME(question, envelope, promise);
                }
            }
            else {
                if (this.traceEnabled) {
                    this.addTrace(envelope.sender(), "response code: " + code + " with " + res.count(DnsSection.ANSWER) + " answer(s) and " + res.count(DnsSection.AUTHORITY) + " authority resource(s)");
                }
                if (code != DnsResponseCode.NXDOMAIN) {
                    this.query(nameServerAddrStream, question, promise);
                }
            }
        }
        finally {
            ReferenceCountUtil.safeRelease(envelope);
        }
    }
    
    private boolean handleRedirect(final DnsQuestion question, final AddressedEnvelope<DnsResponse, InetSocketAddress> envelope, final Promise<T> promise) {
        final DnsResponse res = envelope.content();
        if (res.count(DnsSection.ANSWER) == 0) {
            final AuthoritativeNameServerList serverNames = extractAuthoritativeNameServers(question.name(), res);
            if (serverNames != null) {
                final List<InetSocketAddress> nameServers = new ArrayList<InetSocketAddress>(serverNames.size());
                for (int additionalCount = res.count(DnsSection.ADDITIONAL), i = 0; i < additionalCount; ++i) {
                    final DnsRecord r = res.recordAt(DnsSection.ADDITIONAL, i);
                    if (r.type() != DnsRecordType.A || this.parent.supportsARecords()) {
                        if (r.type() != DnsRecordType.AAAA || this.parent.supportsAAAARecords()) {
                            final String recordName = r.name();
                            final AuthoritativeNameServer authoritativeNameServer = serverNames.remove(recordName);
                            if (authoritativeNameServer != null) {
                                final InetAddress resolved = this.parseAddress(r, recordName);
                                if (resolved != null) {
                                    nameServers.add(new InetSocketAddress(resolved, this.parent.dnsRedirectPort(resolved)));
                                    this.addNameServerToCache(authoritativeNameServer, resolved, r.timeToLive());
                                }
                            }
                        }
                    }
                }
                if (nameServers.isEmpty()) {
                    promise.tryFailure(new UnknownHostException("Unable to find correct name server for " + this.hostname));
                }
                else {
                    this.query(DnsServerAddresses.shuffled(nameServers).stream(), question, promise);
                }
                return true;
            }
        }
        return false;
    }
    
    private static AuthoritativeNameServerList extractAuthoritativeNameServers(final String questionName, final DnsResponse res) {
        final int authorityCount = res.count(DnsSection.AUTHORITY);
        if (authorityCount == 0) {
            return null;
        }
        final AuthoritativeNameServerList serverNames = new AuthoritativeNameServerList(questionName);
        for (int i = 0; i < authorityCount; ++i) {
            serverNames.add(res.recordAt(DnsSection.AUTHORITY, i));
        }
        return serverNames;
    }
    
    private void onResponseAorAAAA(final DnsRecordType qType, final DnsQuestion question, final AddressedEnvelope<DnsResponse, InetSocketAddress> envelope, final Promise<T> promise) {
        final DnsResponse response = envelope.content();
        final Map<String, String> cnames = buildAliasMap(response);
        final int answerCount = response.count(DnsSection.ANSWER);
        boolean found = false;
        for (int i = 0; i < answerCount; ++i) {
            final DnsRecord r = response.recordAt(DnsSection.ANSWER, i);
            final DnsRecordType type = r.type();
            if (type == DnsRecordType.A || type == DnsRecordType.AAAA) {
                final String questionName = question.name().toLowerCase(Locale.US);
                final String recordName = r.name().toLowerCase(Locale.US);
                if (!recordName.equals(questionName)) {
                    String resolved = questionName;
                    do {
                        resolved = cnames.get(resolved);
                        if (recordName.equals(resolved)) {
                            break;
                        }
                    } while (resolved != null);
                    if (resolved == null) {
                        continue;
                    }
                }
                final InetAddress resolved2 = this.parseAddress(r, this.hostname);
                if (resolved2 != null) {
                    if (this.resolvedEntries == null) {
                        this.resolvedEntries = new ArrayList<DnsCacheEntry>(8);
                    }
                    final DnsCacheEntry e = new DnsCacheEntry(this.hostname, resolved2);
                    this.resolveCache.cache(this.hostname, this.additionals, resolved2, r.timeToLive(), this.parent.ch.eventLoop());
                    this.resolvedEntries.add(e);
                    found = true;
                }
            }
        }
        if (found) {
            return;
        }
        if (this.traceEnabled) {
            this.addTrace(envelope.sender(), "no matching " + qType + " record found");
        }
        if (!cnames.isEmpty()) {
            this.onResponseCNAME(question, envelope, cnames, false, promise);
        }
    }
    
    private InetAddress parseAddress(final DnsRecord r, final String name) {
        if (!(r instanceof DnsRawRecord)) {
            return null;
        }
        final ByteBuf content = ((ByteBufHolder)r).content();
        final int contentLen = content.readableBytes();
        if (contentLen != 4 && contentLen != 16) {
            return null;
        }
        final byte[] addrBytes = new byte[contentLen];
        content.getBytes(content.readerIndex(), addrBytes);
        try {
            return InetAddress.getByAddress(this.parent.isDecodeIdn() ? IDN.toUnicode(name) : name, addrBytes);
        }
        catch (final UnknownHostException e) {
            throw new Error(e);
        }
    }
    
    private void onResponseCNAME(final DnsQuestion question, final AddressedEnvelope<DnsResponse, InetSocketAddress> envelope, final Promise<T> promise) {
        this.onResponseCNAME(question, envelope, buildAliasMap(envelope.content()), true, promise);
    }
    
    private void onResponseCNAME(final DnsQuestion question, final AddressedEnvelope<DnsResponse, InetSocketAddress> response, final Map<String, String> cnames, final boolean trace, final Promise<T> promise) {
        String resolved;
        final String name = resolved = question.name().toLowerCase(Locale.US);
        boolean found = false;
        while (!cnames.isEmpty()) {
            final String next = cnames.remove(resolved);
            if (next == null) {
                break;
            }
            found = true;
            resolved = next;
        }
        if (found) {
            this.followCname(response.sender(), name, resolved, promise);
        }
        else if (trace && this.traceEnabled) {
            this.addTrace(response.sender(), "no matching CNAME record found");
        }
    }
    
    private static Map<String, String> buildAliasMap(final DnsResponse response) {
        final int answerCount = response.count(DnsSection.ANSWER);
        Map<String, String> cnames = null;
        for (int i = 0; i < answerCount; ++i) {
            final DnsRecord r = response.recordAt(DnsSection.ANSWER, i);
            final DnsRecordType type = r.type();
            if (type == DnsRecordType.CNAME) {
                if (r instanceof DnsRawRecord) {
                    final ByteBuf recordContent = ((ByteBufHolder)r).content();
                    final String domainName = decodeDomainName(recordContent);
                    if (domainName != null) {
                        if (cnames == null) {
                            cnames = new HashMap<String, String>();
                        }
                        cnames.put(r.name().toLowerCase(Locale.US), domainName.toLowerCase(Locale.US));
                    }
                }
            }
        }
        return (cnames != null) ? cnames : Collections.emptyMap();
    }
    
    void tryToFinishResolve(final Promise<T> promise) {
        if (!this.queriesInProgress.isEmpty()) {
            if (this.gotPreferredAddress()) {
                this.finishResolve(promise);
            }
            return;
        }
        if (this.resolvedEntries == null && !this.triedCNAME) {
            this.triedCNAME = true;
            this.query(this.hostname, DnsRecordType.CNAME, this.getNameServers(this.hostname), promise);
            return;
        }
        this.finishResolve(promise);
    }
    
    private boolean gotPreferredAddress() {
        if (this.resolvedEntries == null) {
            return false;
        }
        final int size = this.resolvedEntries.size();
        switch (this.parent.preferredAddressType()) {
            case IPv4: {
                for (int i = 0; i < size; ++i) {
                    if (this.resolvedEntries.get(i).address() instanceof Inet4Address) {
                        return true;
                    }
                }
                break;
            }
            case IPv6: {
                for (int i = 0; i < size; ++i) {
                    if (this.resolvedEntries.get(i).address() instanceof Inet6Address) {
                        return true;
                    }
                }
                break;
            }
            default: {
                throw new Error();
            }
        }
        return false;
    }
    
    private void finishResolve(final Promise<T> promise) {
        if (!this.queriesInProgress.isEmpty()) {
            final Iterator<Future<AddressedEnvelope<DnsResponse, InetSocketAddress>>> i = this.queriesInProgress.iterator();
            while (i.hasNext()) {
                final Future<AddressedEnvelope<DnsResponse, InetSocketAddress>> f = i.next();
                i.remove();
                if (!f.cancel(false)) {
                    f.addListener(DnsNameResolverContext.RELEASE_RESPONSE);
                }
            }
        }
        if (this.resolvedEntries != null) {
            for (final InternetProtocolFamily f2 : this.resolvedInternetProtocolFamilies) {
                if (this.finishResolve(f2.addressType(), this.resolvedEntries, promise)) {
                    return;
                }
            }
        }
        final int tries = this.maxAllowedQueries - this.allowedQueries;
        final StringBuilder buf = new StringBuilder(64);
        buf.append("failed to resolve '");
        if (this.pristineHostname != null) {
            buf.append(this.pristineHostname);
        }
        else {
            buf.append(this.hostname);
        }
        buf.append('\'');
        if (tries > 1) {
            if (tries < this.maxAllowedQueries) {
                buf.append(" after ").append(tries).append(" queries ");
            }
            else {
                buf.append(". Exceeded max queries per resolve ").append(this.maxAllowedQueries).append(' ');
            }
        }
        if (this.trace != null) {
            buf.append(':').append((CharSequence)this.trace);
        }
        final UnknownHostException cause = new UnknownHostException(buf.toString());
        this.resolveCache.cache(this.hostname, this.additionals, cause, this.parent.ch.eventLoop());
        promise.tryFailure(cause);
    }
    
    abstract boolean finishResolve(final Class<? extends InetAddress> p0, final List<DnsCacheEntry> p1, final Promise<T> p2);
    
    abstract DnsNameResolverContext<T> newResolverContext(final DnsNameResolver p0, final String p1, final DnsRecord[] p2, final DnsCache p3, final DnsServerAddressStream p4);
    
    static String decodeDomainName(final ByteBuf in) {
        in.markReaderIndex();
        try {
            return DefaultDnsRecordDecoder.decodeName(in);
        }
        catch (final CorruptedFrameException e) {
            return null;
        }
        finally {
            in.resetReaderIndex();
        }
    }
    
    private DnsServerAddressStream getNameServers(final String hostame) {
        final DnsServerAddressStream stream = this.getNameServersFromCache(hostame);
        return (stream == null) ? this.nameServerAddrs : stream;
    }
    
    private void followCname(final InetSocketAddress nameServerAddr, final String name, final String cname, final Promise<T> promise) {
        if (this.traceEnabled) {
            if (this.trace == null) {
                this.trace = new StringBuilder(128);
            }
            this.trace.append(StringUtil.NEWLINE);
            this.trace.append("\tfrom ");
            this.trace.append(nameServerAddr);
            this.trace.append(": ");
            this.trace.append(name);
            this.trace.append(" CNAME ");
            this.trace.append(cname);
        }
        final DnsServerAddressStream stream = DnsServerAddresses.singleton(this.getNameServers(cname).next()).stream();
        if (this.parent.supportsARecords() && !this.query(this.hostname, DnsRecordType.A, stream, promise)) {
            return;
        }
        if (this.parent.supportsAAAARecords()) {
            this.query(this.hostname, DnsRecordType.AAAA, stream, promise);
        }
    }
    
    private boolean query(final String hostname, final DnsRecordType type, final DnsServerAddressStream nextAddr, final Promise<T> promise) {
        DnsQuestion question;
        try {
            question = new DefaultDnsQuestion(hostname, type);
        }
        catch (final IllegalArgumentException e) {
            promise.tryFailure(e);
            return false;
        }
        this.query(nextAddr, question, promise);
        return true;
    }
    
    private void addTrace(final InetSocketAddress nameServerAddr, final String msg) {
        assert this.traceEnabled;
        if (this.trace == null) {
            this.trace = new StringBuilder(128);
        }
        this.trace.append(StringUtil.NEWLINE);
        this.trace.append("\tfrom ");
        this.trace.append(nameServerAddr);
        this.trace.append(": ");
        this.trace.append(msg);
    }
    
    private void addTrace(final Throwable cause) {
        assert this.traceEnabled;
        if (this.trace == null) {
            this.trace = new StringBuilder(128);
        }
        this.trace.append(StringUtil.NEWLINE);
        this.trace.append("Caused by: ");
        this.trace.append(cause);
    }
    
    static {
        RELEASE_RESPONSE = new FutureListener<AddressedEnvelope<DnsResponse, InetSocketAddress>>() {
            @Override
            public void operationComplete(final Future<AddressedEnvelope<DnsResponse, InetSocketAddress>> future) {
                if (future.isSuccess()) {
                    future.getNow().release();
                }
            }
        };
    }
    
    private final class DnsCacheIterable implements Iterable<InetSocketAddress>
    {
        private final List<DnsCacheEntry> entries;
        
        DnsCacheIterable(final List<DnsCacheEntry> entries) {
            this.entries = entries;
        }
        
        @Override
        public Iterator<InetSocketAddress> iterator() {
            return new Iterator<InetSocketAddress>() {
                Iterator<DnsCacheEntry> entryIterator = DnsCacheIterable.this.entries.iterator();
                
                @Override
                public boolean hasNext() {
                    return this.entryIterator.hasNext();
                }
                
                @Override
                public InetSocketAddress next() {
                    final InetAddress address = this.entryIterator.next().address();
                    return new InetSocketAddress(address, DnsNameResolverContext.this.parent.dnsRedirectPort(address));
                }
                
                @Override
                public void remove() {
                    this.entryIterator.remove();
                }
            };
        }
    }
    
    private static final class AuthoritativeNameServerList
    {
        private final String questionName;
        private AuthoritativeNameServer head;
        private int count;
        
        AuthoritativeNameServerList(final String questionName) {
            this.questionName = questionName.toLowerCase(Locale.US);
        }
        
        void add(final DnsRecord r) {
            if (r.type() != DnsRecordType.NS || !(r instanceof DnsRawRecord)) {
                return;
            }
            if (this.questionName.length() < r.name().length()) {
                return;
            }
            final String recordName = r.name().toLowerCase(Locale.US);
            int dots = 0;
            for (int a = recordName.length() - 1, b = this.questionName.length() - 1; a >= 0; --a, --b) {
                final char c = recordName.charAt(a);
                if (this.questionName.charAt(b) != c) {
                    return;
                }
                if (c == '.') {
                    ++dots;
                }
            }
            if (this.head != null && this.head.dots > dots) {
                return;
            }
            final ByteBuf recordContent = ((ByteBufHolder)r).content();
            final String domainName = DnsNameResolverContext.decodeDomainName(recordContent);
            if (domainName == null) {
                return;
            }
            if (this.head == null || this.head.dots < dots) {
                this.count = 1;
                this.head = new AuthoritativeNameServer(dots, recordName, domainName);
            }
            else if (this.head.dots == dots) {
                AuthoritativeNameServer serverName;
                for (serverName = this.head; serverName.next != null; serverName = serverName.next) {}
                serverName.next = new AuthoritativeNameServer(dots, recordName, domainName);
                ++this.count;
            }
        }
        
        AuthoritativeNameServer remove(final String nsName) {
            for (AuthoritativeNameServer serverName = this.head; serverName != null; serverName = serverName.next) {
                if (!serverName.removed && serverName.nsName.equalsIgnoreCase(nsName)) {
                    serverName.removed = true;
                    return serverName;
                }
            }
            return null;
        }
        
        int size() {
            return this.count;
        }
    }
    
    static final class AuthoritativeNameServer
    {
        final int dots;
        final String nsName;
        final String domainName;
        AuthoritativeNameServer next;
        boolean removed;
        
        AuthoritativeNameServer(final int dots, final String domainName, final String nsName) {
            this.dots = dots;
            this.nsName = nsName;
            this.domainName = domainName;
        }
        
        boolean isRootServer() {
            return this.dots == 1;
        }
        
        String domainName() {
            return this.domainName;
        }
    }
}
