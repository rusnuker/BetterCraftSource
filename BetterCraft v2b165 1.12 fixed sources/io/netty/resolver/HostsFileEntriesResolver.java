// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.resolver;

import java.net.InetAddress;

public interface HostsFileEntriesResolver
{
    public static final HostsFileEntriesResolver DEFAULT = new DefaultHostsFileEntriesResolver();
    
    InetAddress address(final String p0, final ResolvedAddressTypes p1);
}
