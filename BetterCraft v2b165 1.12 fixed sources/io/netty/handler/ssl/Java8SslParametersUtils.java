// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.ssl;

import java.util.Iterator;
import javax.net.ssl.SNIHostName;
import javax.net.ssl.SNIServerName;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.net.ssl.SSLParameters;

final class Java8SslParametersUtils
{
    private Java8SslParametersUtils() {
    }
    
    static List<String> getSniHostNames(final SSLParameters sslParameters) {
        final List<SNIServerName> names = sslParameters.getServerNames();
        if (names == null || names.isEmpty()) {
            return Collections.emptyList();
        }
        final List<String> strings = new ArrayList<String>(names.size());
        for (final SNIServerName serverName : names) {
            if (!(serverName instanceof SNIHostName)) {
                throw new IllegalArgumentException("Only " + SNIHostName.class.getName() + " instances are supported, but found: " + serverName);
            }
            strings.add(((SNIHostName)serverName).getAsciiName());
        }
        return strings;
    }
    
    static void setSniHostNames(final SSLParameters sslParameters, final List<String> names) {
        final List<SNIServerName> sniServerNames = new ArrayList<SNIServerName>(names.size());
        for (final String name : names) {
            sniServerNames.add(new SNIHostName(name));
        }
        sslParameters.setServerNames(sniServerNames);
    }
    
    static boolean getUseCipherSuitesOrder(final SSLParameters sslParameters) {
        return sslParameters.getUseCipherSuitesOrder();
    }
    
    static void setUseCipherSuitesOrder(final SSLParameters sslParameters, final boolean useOrder) {
        sslParameters.setUseCipherSuitesOrder(useOrder);
    }
}
