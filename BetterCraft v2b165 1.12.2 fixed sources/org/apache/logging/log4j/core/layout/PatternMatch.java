// 
// Decompiled by Procyon v0.6.0
// 

package org.apache.logging.log4j.core.layout;

import java.io.ObjectStreamException;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
import java.io.Serializable;
import org.apache.logging.log4j.core.util.Builder;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "PatternMatch", category = "Core", printObject = true)
public final class PatternMatch
{
    private final String key;
    private final String pattern;
    
    public PatternMatch(final String key, final String pattern) {
        this.key = key;
        this.pattern = pattern;
    }
    
    public String getKey() {
        return this.key;
    }
    
    public String getPattern() {
        return this.pattern;
    }
    
    @Override
    public String toString() {
        return this.key + '=' + this.pattern;
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
        result = 31 * result + ((this.pattern == null) ? 0 : this.pattern.hashCode());
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
        final PatternMatch other = (PatternMatch)obj;
        if (this.key == null) {
            if (other.key != null) {
                return false;
            }
        }
        else if (!this.key.equals(other.key)) {
            return false;
        }
        if (this.pattern == null) {
            if (other.pattern != null) {
                return false;
            }
        }
        else if (!this.pattern.equals(other.pattern)) {
            return false;
        }
        return true;
    }
    
    public static class Builder implements org.apache.logging.log4j.core.util.Builder<PatternMatch>, Serializable
    {
        private static final long serialVersionUID = 1L;
        @PluginBuilderAttribute
        private String key;
        @PluginBuilderAttribute
        private String pattern;
        
        public Builder setKey(final String key) {
            this.key = key;
            return this;
        }
        
        public Builder setPattern(final String pattern) {
            this.pattern = pattern;
            return this;
        }
        
        @Override
        public PatternMatch build() {
            return new PatternMatch(this.key, this.pattern);
        }
        
        protected Object readResolve() throws ObjectStreamException {
            return new PatternMatch(this.key, this.pattern);
        }
    }
}
