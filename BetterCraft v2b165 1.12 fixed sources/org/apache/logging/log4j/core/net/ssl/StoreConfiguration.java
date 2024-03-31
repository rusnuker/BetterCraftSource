// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.net.ssl;

import org.apache.logging.log4j.status.StatusLogger;

public class StoreConfiguration<T>
{
    protected static final StatusLogger LOGGER;
    private String location;
    private String password;
    
    public StoreConfiguration(final String location, final String password) {
        this.location = location;
        this.password = password;
    }
    
    public String getLocation() {
        return this.location;
    }
    
    public void setLocation(final String location) {
        this.location = location;
    }
    
    public String getPassword() {
        return this.password;
    }
    
    public char[] getPasswordAsCharArray() {
        return (char[])((this.password == null) ? null : this.password.toCharArray());
    }
    
    public void setPassword(final String password) {
        this.password = password;
    }
    
    protected T load() throws StoreConfigurationException {
        return null;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + ((this.location == null) ? 0 : this.location.hashCode());
        result = 31 * result + ((this.password == null) ? 0 : this.password.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof StoreConfiguration)) {
            return false;
        }
        final StoreConfiguration<?> other = (StoreConfiguration<?>)obj;
        if (this.location == null) {
            if (other.location != null) {
                return false;
            }
        }
        else if (!this.location.equals(other.location)) {
            return false;
        }
        if (this.password == null) {
            if (other.password != null) {
                return false;
            }
        }
        else if (!this.password.equals(other.password)) {
            return false;
        }
        return true;
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
    }
}
