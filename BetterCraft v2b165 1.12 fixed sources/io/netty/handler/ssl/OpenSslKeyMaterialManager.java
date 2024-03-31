// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.handler.ssl;

import java.util.HashMap;
import java.net.Socket;
import java.security.Principal;
import io.netty.buffer.ByteBufAllocator;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import io.netty.internal.tcnative.CertificateRequestedCallback;
import javax.security.auth.x500.X500Principal;
import javax.net.ssl.SSLException;
import java.util.Set;
import java.util.HashSet;
import io.netty.internal.tcnative.SSL;
import javax.net.ssl.X509KeyManager;
import java.util.Map;

class OpenSslKeyMaterialManager
{
    static final String KEY_TYPE_RSA = "RSA";
    static final String KEY_TYPE_DH_RSA = "DH_RSA";
    static final String KEY_TYPE_EC = "EC";
    static final String KEY_TYPE_EC_EC = "EC_EC";
    static final String KEY_TYPE_EC_RSA = "EC_RSA";
    private static final Map<String, String> KEY_TYPES;
    private final X509KeyManager keyManager;
    private final String password;
    
    OpenSslKeyMaterialManager(final X509KeyManager keyManager, final String password) {
        this.keyManager = keyManager;
        this.password = password;
    }
    
    void setKeyMaterial(final ReferenceCountedOpenSslEngine engine) throws SSLException {
        final long ssl = engine.sslPointer();
        final String[] authMethods = SSL.authenticationMethods(ssl);
        final Set<String> aliases = new HashSet<String>(authMethods.length);
        for (final String authMethod : authMethods) {
            final String type = OpenSslKeyMaterialManager.KEY_TYPES.get(authMethod);
            if (type != null) {
                final String alias = this.chooseServerAlias(engine, type);
                if (alias != null && aliases.add(alias)) {
                    this.setKeyMaterial(ssl, alias);
                }
            }
        }
    }
    
    CertificateRequestedCallback.KeyMaterial keyMaterial(final ReferenceCountedOpenSslEngine engine, final String[] keyTypes, final X500Principal[] issuer) throws SSLException {
        final String alias = this.chooseClientAlias(engine, keyTypes, issuer);
        long keyBio = 0L;
        long keyCertChainBio = 0L;
        long pkey = 0L;
        long certChain = 0L;
        try {
            final X509Certificate[] certificates = this.keyManager.getCertificateChain(alias);
            if (certificates == null || certificates.length == 0) {
                return null;
            }
            final PrivateKey key = this.keyManager.getPrivateKey(alias);
            keyCertChainBio = ReferenceCountedOpenSslContext.toBIO(certificates);
            certChain = SSL.parseX509Chain(keyCertChainBio);
            if (key != null) {
                keyBio = ReferenceCountedOpenSslContext.toBIO(key);
                pkey = SSL.parsePrivateKey(keyBio, this.password);
            }
            final CertificateRequestedCallback.KeyMaterial material = new CertificateRequestedCallback.KeyMaterial(certChain, pkey);
            pkey = (certChain = 0L);
            return material;
        }
        catch (final SSLException e) {
            throw e;
        }
        catch (final Exception e2) {
            throw new SSLException(e2);
        }
        finally {
            ReferenceCountedOpenSslContext.freeBio(keyBio);
            ReferenceCountedOpenSslContext.freeBio(keyCertChainBio);
            SSL.freePrivateKey(pkey);
            SSL.freeX509Chain(certChain);
        }
    }
    
    private void setKeyMaterial(final long ssl, final String alias) throws SSLException {
        long keyBio = 0L;
        long keyCertChainBio = 0L;
        long keyCertChainBio2 = 0L;
        try {
            final X509Certificate[] certificates = this.keyManager.getCertificateChain(alias);
            if (certificates == null || certificates.length == 0) {
                return;
            }
            final PrivateKey key = this.keyManager.getPrivateKey(alias);
            final PemEncoded encoded = PemX509Certificate.toPEM(ByteBufAllocator.DEFAULT, true, certificates);
            try {
                keyCertChainBio = ReferenceCountedOpenSslContext.toBIO(ByteBufAllocator.DEFAULT, encoded.retain());
                keyCertChainBio2 = ReferenceCountedOpenSslContext.toBIO(ByteBufAllocator.DEFAULT, encoded.retain());
                if (key != null) {
                    keyBio = ReferenceCountedOpenSslContext.toBIO(key);
                }
                SSL.setCertificateBio(ssl, keyCertChainBio, keyBio, this.password);
                SSL.setCertificateChainBio(ssl, keyCertChainBio2, true);
            }
            finally {
                encoded.release();
            }
        }
        catch (final SSLException e) {
            throw e;
        }
        catch (final Exception e2) {
            throw new SSLException(e2);
        }
        finally {
            ReferenceCountedOpenSslContext.freeBio(keyBio);
            ReferenceCountedOpenSslContext.freeBio(keyCertChainBio);
            ReferenceCountedOpenSslContext.freeBio(keyCertChainBio2);
        }
    }
    
    protected String chooseClientAlias(final ReferenceCountedOpenSslEngine engine, final String[] keyTypes, final X500Principal[] issuer) {
        return this.keyManager.chooseClientAlias(keyTypes, issuer, null);
    }
    
    protected String chooseServerAlias(final ReferenceCountedOpenSslEngine engine, final String type) {
        return this.keyManager.chooseServerAlias(type, null, null);
    }
    
    static {
        (KEY_TYPES = new HashMap<String, String>()).put("RSA", "RSA");
        OpenSslKeyMaterialManager.KEY_TYPES.put("DHE_RSA", "RSA");
        OpenSslKeyMaterialManager.KEY_TYPES.put("ECDHE_RSA", "RSA");
        OpenSslKeyMaterialManager.KEY_TYPES.put("ECDHE_ECDSA", "EC");
        OpenSslKeyMaterialManager.KEY_TYPES.put("ECDH_RSA", "EC_RSA");
        OpenSslKeyMaterialManager.KEY_TYPES.put("ECDH_ECDSA", "EC_EC");
        OpenSslKeyMaterialManager.KEY_TYPES.put("DH_RSA", "DH_RSA");
    }
}
