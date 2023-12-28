// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.ssl;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.Set;
import java.util.List;

public final class IdentityCipherSuiteFilter implements CipherSuiteFilter
{
    public static final IdentityCipherSuiteFilter INSTANCE;
    
    private IdentityCipherSuiteFilter() {
    }
    
    @Override
    public String[] filterCipherSuites(final Iterable<String> ciphers, final List<String> defaultCiphers, final Set<String> supportedCiphers) {
        if (ciphers == null) {
            return defaultCiphers.toArray(new String[defaultCiphers.size()]);
        }
        final List<String> newCiphers = new ArrayList<String>(supportedCiphers.size());
        for (final String c : ciphers) {
            if (c == null) {
                break;
            }
            newCiphers.add(c);
        }
        return newCiphers.toArray(new String[newCiphers.size()]);
    }
    
    static {
        INSTANCE = new IdentityCipherSuiteFilter();
    }
}
