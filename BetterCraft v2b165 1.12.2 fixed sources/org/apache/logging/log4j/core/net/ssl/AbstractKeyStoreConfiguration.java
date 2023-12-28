// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.net.ssl;

import java.io.FileNotFoundException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;

public class AbstractKeyStoreConfiguration extends StoreConfiguration<KeyStore>
{
    private final KeyStore keyStore;
    private final String keyStoreType;
    
    public AbstractKeyStoreConfiguration(final String location, final String password, final String keyStoreType) throws StoreConfigurationException {
        super(location, password);
        this.keyStoreType = ((keyStoreType == null) ? "JKS" : keyStoreType);
        this.keyStore = this.load();
    }
    
    @Override
    protected KeyStore load() throws StoreConfigurationException {
        AbstractKeyStoreConfiguration.LOGGER.debug("Loading keystore from file with params(location={})", this.getLocation());
        try {
            if (this.getLocation() == null) {
                throw new IOException("The location is null");
            }
            try (final FileInputStream fin = new FileInputStream(this.getLocation())) {
                final KeyStore ks = KeyStore.getInstance(this.keyStoreType);
                ks.load(fin, this.getPasswordAsCharArray());
                AbstractKeyStoreConfiguration.LOGGER.debug("Keystore successfully loaded with params(location={})", this.getLocation());
                return ks;
            }
        }
        catch (final CertificateException e) {
            AbstractKeyStoreConfiguration.LOGGER.error("No Provider supports a KeyStoreSpi implementation for the specified type" + this.keyStoreType, e);
            throw new StoreConfigurationException(e);
        }
        catch (final NoSuchAlgorithmException e2) {
            AbstractKeyStoreConfiguration.LOGGER.error("The algorithm used to check the integrity of the keystore cannot be found", e2);
            throw new StoreConfigurationException(e2);
        }
        catch (final KeyStoreException e3) {
            AbstractKeyStoreConfiguration.LOGGER.error(e3);
            throw new StoreConfigurationException(e3);
        }
        catch (final FileNotFoundException e4) {
            AbstractKeyStoreConfiguration.LOGGER.error("The keystore file(" + this.getLocation() + ") is not found", e4);
            throw new StoreConfigurationException(e4);
        }
        catch (final IOException e5) {
            AbstractKeyStoreConfiguration.LOGGER.error("Something is wrong with the format of the keystore or the given password", e5);
            throw new StoreConfigurationException(e5);
        }
    }
    
    public KeyStore getKeyStore() {
        return this.keyStore;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = 31 * result + ((this.keyStore == null) ? 0 : this.keyStore.hashCode());
        result = 31 * result + ((this.keyStoreType == null) ? 0 : this.keyStoreType.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final AbstractKeyStoreConfiguration other = (AbstractKeyStoreConfiguration)obj;
        if (this.keyStore == null) {
            if (other.keyStore != null) {
                return false;
            }
        }
        else if (!this.keyStore.equals(other.keyStore)) {
            return false;
        }
        if (this.keyStoreType == null) {
            if (other.keyStoreType != null) {
                return false;
            }
        }
        else if (!this.keyStoreType.equals(other.keyStoreType)) {
            return false;
        }
        return true;
    }
}
