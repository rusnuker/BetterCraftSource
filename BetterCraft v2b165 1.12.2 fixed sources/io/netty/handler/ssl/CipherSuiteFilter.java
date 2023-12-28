// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.ssl;

import java.util.Set;
import java.util.List;

public interface CipherSuiteFilter
{
    String[] filterCipherSuites(final Iterable<String> p0, final List<String> p1, final Set<String> p2);
}
