// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.util;

import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "KeyValuePair", category = "Core", printObject = true)
public final class KeyValuePair
{
    private final String key;
    private final String value;
    
    public KeyValuePair(final String key, final String value) {
        this.key = key;
        this.value = value;
    }
    
    public String getKey() {
        return this.key;
    }
    
    public String getValue() {
        return this.value;
    }
    
    @Override
    public String toString() {
        return this.key + '=' + this.value;
    }
    
    @PluginBuilderFactory
    public static Builder newBuilder() {
        return new Builder();
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + ((this.key == null) ? 0 : this.key.hashCode());
        result = 31 * result + ((this.value == null) ? 0 : this.value.hashCode());
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
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final KeyValuePair other = (KeyValuePair)obj;
        if (this.key == null) {
            if (other.key != null) {
                return false;
            }
        }
        else if (!this.key.equals(other.key)) {
            return false;
        }
        if (this.value == null) {
            if (other.value != null) {
                return false;
            }
        }
        else if (!this.value.equals(other.value)) {
            return false;
        }
        return true;
    }
    
    public static class Builder implements org.apache.logging.log4j.core.util.Builder<KeyValuePair>
    {
        @PluginBuilderAttribute
        private String key;
        @PluginBuilderAttribute
        private String value;
        
        public Builder setKey(final String aKey) {
            this.key = aKey;
            return this;
        }
        
        public Builder setValue(final String aValue) {
            this.value = aValue;
            return this;
        }
        
        @Override
        public KeyValuePair build() {
            return new KeyValuePair(this.key, this.value);
        }
    }
}
