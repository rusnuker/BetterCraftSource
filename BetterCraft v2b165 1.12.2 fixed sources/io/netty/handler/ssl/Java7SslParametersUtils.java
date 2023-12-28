// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.ssl;

import java.security.AlgorithmConstraints;
import javax.net.ssl.SSLParameters;

final class Java7SslParametersUtils
{
    private Java7SslParametersUtils() {
    }
    
    static void setAlgorithmConstraints(final SSLParameters sslParameters, final Object algorithmConstraints) {
        sslParameters.setAlgorithmConstraints((AlgorithmConstraints)algorithmConstraints);
    }
}
