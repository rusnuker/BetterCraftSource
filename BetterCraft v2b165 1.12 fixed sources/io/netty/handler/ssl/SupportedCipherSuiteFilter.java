// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.ssl;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.Set;
import java.util.List;

public final class SupportedCipherSuiteFilter implements CipherSuiteFilter
{
    public static final SupportedCipherSuiteFilter INSTANCE;
    
    private SupportedCipherSuiteFilter() {
    }
    
    @Override
    public String[] filterCipherSuites(Iterable<String> ciphers, final List<String> defaultCiphers, final Set<String> supportedCiphers) {
        if (defaultCiphers == null) {
            throw new NullPointerException("defaultCiphers");
        }
        if (supportedCiphers == null) {
            throw new NullPointerException("supportedCiphers");
        }
        List<String> newCiphers;
        if (ciphers == null) {
            newCiphers = new ArrayList<String>(defaultCiphers.size());
            ciphers = defaultCiphers;
        }
        else {
            newCiphers = new ArrayList<String>(supportedCiphers.size());
        }
        for (final String c : ciphers) {
            if (c == null) {
                break;
            }
            if (!supportedCiphers.contains(c)) {
                continue;
            }
            newCiphers.add(c);
        }
        return newCiphers.toArray(new String[newCiphers.size()]);
    }
    
    static {
        INSTANCE = new SupportedCipherSuiteFilter();
    }
}
