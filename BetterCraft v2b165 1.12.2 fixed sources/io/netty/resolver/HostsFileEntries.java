// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.resolver;

import java.util.Collections;
import java.util.HashMap;
import java.net.Inet6Address;
import java.net.Inet4Address;
import java.util.Map;

public final class HostsFileEntries
{
    static final HostsFileEntries EMPTY;
    private final Map<String, Inet4Address> inet4Entries;
    private final Map<String, Inet6Address> inet6Entries;
    
    public HostsFileEntries(final Map<String, Inet4Address> inet4Entries, final Map<String, Inet6Address> inet6Entries) {
        this.inet4Entries = Collections.unmodifiableMap((Map<? extends String, ? extends Inet4Address>)new HashMap<String, Inet4Address>(inet4Entries));
        this.inet6Entries = Collections.unmodifiableMap((Map<? extends String, ? extends Inet6Address>)new HashMap<String, Inet6Address>(inet6Entries));
    }
    
    public Map<String, Inet4Address> inet4Entries() {
        return this.inet4Entries;
    }
    
    public Map<String, Inet6Address> inet6Entries() {
        return this.inet6Entries;
    }
    
    static {
        EMPTY = new HostsFileEntries(Collections.emptyMap(), Collections.emptyMap());
    }
}
