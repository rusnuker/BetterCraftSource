// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.codec.http2;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Arrays;
import java.util.List;

public final class Http2SecurityUtil
{
    public static final List<String> CIPHERS;
    private static final List<String> CIPHERS_JAVA_MOZILLA_INCREASED_SECURITY;
    private static final List<String> CIPHERS_JAVA_NO_MOZILLA_INCREASED_SECURITY;
    
    private Http2SecurityUtil() {
    }
    
    static {
        CIPHERS_JAVA_MOZILLA_INCREASED_SECURITY = Collections.unmodifiableList((List<? extends String>)Arrays.asList("TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384", "SSL_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384", "TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256", "SSL_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256", "TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384", "SSL_ECDHE_RSA_WITH_AES_256_GCM_SHA384", "TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256", "SSL_ECDHE_RSA_WITH_AES_128_GCM_SHA256", "TLS_DHE_RSA_WITH_AES_128_GCM_SHA256", "SSL_DHE_RSA_WITH_AES_128_GCM_SHA256", "TLS_DHE_DSS_WITH_AES_128_GCM_SHA256", "SSL_DHE_DSS_WITH_AES_128_GCM_SHA256"));
        CIPHERS_JAVA_NO_MOZILLA_INCREASED_SECURITY = Collections.unmodifiableList((List<? extends String>)Arrays.asList("TLS_DHE_RSA_WITH_AES_256_GCM_SHA384", "SSL_DHE_RSA_WITH_AES_256_GCM_SHA384", "TLS_DHE_DSS_WITH_AES_256_GCM_SHA384", "SSL_DHE_DSS_WITH_AES_256_GCM_SHA384"));
        final List<String> ciphers = new ArrayList<String>(Http2SecurityUtil.CIPHERS_JAVA_MOZILLA_INCREASED_SECURITY.size() + Http2SecurityUtil.CIPHERS_JAVA_NO_MOZILLA_INCREASED_SECURITY.size());
        ciphers.addAll(Http2SecurityUtil.CIPHERS_JAVA_MOZILLA_INCREASED_SECURITY);
        ciphers.addAll(Http2SecurityUtil.CIPHERS_JAVA_NO_MOZILLA_INCREASED_SECURITY);
        CIPHERS = Collections.unmodifiableList((List<? extends String>)ciphers);
    }
}
