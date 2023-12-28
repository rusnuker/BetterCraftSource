// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.ssl;

import javax.net.ssl.SSLEngine;
import java.security.Principal;
import javax.security.auth.x500.X500Principal;
import javax.net.ssl.X509KeyManager;
import javax.net.ssl.X509ExtendedKeyManager;

final class OpenSslExtendedKeyMaterialManager extends OpenSslKeyMaterialManager
{
    private final X509ExtendedKeyManager keyManager;
    
    OpenSslExtendedKeyMaterialManager(final X509ExtendedKeyManager keyManager, final String password) {
        super(keyManager, password);
        this.keyManager = keyManager;
    }
    
    @Override
    protected String chooseClientAlias(final ReferenceCountedOpenSslEngine engine, final String[] keyTypes, final X500Principal[] issuer) {
        return this.keyManager.chooseEngineClientAlias(keyTypes, issuer, engine);
    }
    
    @Override
    protected String chooseServerAlias(final ReferenceCountedOpenSslEngine engine, final String type) {
        return this.keyManager.chooseEngineServerAlias(type, null, engine);
    }
}
