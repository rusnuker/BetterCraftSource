// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.util;

import java.util.Iterator;
import java.util.Set;
import java.util.Collections;
import java.util.LinkedHashMap;
import io.netty.util.internal.ObjectUtil;
import java.util.Map;

public final class DomainNameMappingBuilder<V>
{
    private final V defaultValue;
    private final Map<String, V> map;
    
    public DomainNameMappingBuilder(final V defaultValue) {
        this(4, defaultValue);
    }
    
    public DomainNameMappingBuilder(final int initialCapacity, final V defaultValue) {
        this.defaultValue = ObjectUtil.checkNotNull(defaultValue, "defaultValue");
        this.map = new LinkedHashMap<String, V>(initialCapacity);
    }
    
    public DomainNameMappingBuilder<V> add(final String hostname, final V output) {
        this.map.put(ObjectUtil.checkNotNull(hostname, "hostname"), ObjectUtil.checkNotNull(output, "output"));
        return this;
    }
    
    public DomainNameMapping<V> build() {
        return new ImmutableDomainNameMapping<V>((Object)this.defaultValue, (Map)this.map);
    }
    
    private static final class ImmutableDomainNameMapping<V> extends DomainNameMapping<V>
    {
        private static final String REPR_HEADER = "ImmutableDomainNameMapping(default: ";
        private static final String REPR_MAP_OPENING = ", map: {";
        private static final String REPR_MAP_CLOSING = "})";
        private static final int REPR_CONST_PART_LENGTH;
        private final String[] domainNamePatterns;
        private final V[] values;
        private final Map<String, V> map;
        
        private ImmutableDomainNameMapping(final V defaultValue, final Map<String, V> map) {
            super(null, defaultValue);
            final Set<Map.Entry<String, V>> mappings = map.entrySet();
            final int numberOfMappings = mappings.size();
            this.domainNamePatterns = new String[numberOfMappings];
            this.values = (V[])new Object[numberOfMappings];
            final Map<String, V> mapCopy = new LinkedHashMap<String, V>(map.size());
            int index = 0;
            for (final Map.Entry<String, V> mapping : mappings) {
                final String hostname = DomainNameMapping.normalizeHostname(mapping.getKey());
                final V value = mapping.getValue();
                mapCopy.put(this.domainNamePatterns[index] = hostname, this.values[index] = value);
                ++index;
            }
            this.map = Collections.unmodifiableMap((Map<? extends String, ? extends V>)mapCopy);
        }
        
        @Deprecated
        @Override
        public DomainNameMapping<V> add(final String hostname, final V output) {
            throw new UnsupportedOperationException("Immutable DomainNameMapping does not support modification after initial creation");
        }
        
        @Override
        public V map(String hostname) {
            if (hostname != null) {
                hostname = DomainNameMapping.normalizeHostname(hostname);
                for (int length = this.domainNamePatterns.length, index = 0; index < length; ++index) {
                    if (DomainNameMapping.matches(this.domainNamePatterns[index], hostname)) {
                        return this.values[index];
                    }
                }
            }
            return this.defaultValue;
        }
        
        @Override
        public Map<String, V> asMap() {
            return this.map;
        }
        
        @Override
        public String toString() {
            final String defaultValueStr = this.defaultValue.toString();
            final int numberOfMappings = this.domainNamePatterns.length;
            if (numberOfMappings == 0) {
                return "ImmutableDomainNameMapping(default: " + defaultValueStr + ", map: {" + "})";
            }
            final String pattern0 = this.domainNamePatterns[0];
            final String value0 = this.values[0].toString();
            final int oneMappingLength = pattern0.length() + value0.length() + 3;
            final int estimatedBufferSize = estimateBufferSize(defaultValueStr.length(), numberOfMappings, oneMappingLength);
            final StringBuilder sb = new StringBuilder(estimatedBufferSize).append("ImmutableDomainNameMapping(default: ").append(defaultValueStr).append(", map: {");
            appendMapping(sb, pattern0, value0);
            for (int index = 1; index < numberOfMappings; ++index) {
                sb.append(", ");
                this.appendMapping(sb, index);
            }
            return sb.append("})").toString();
        }
        
        private static int estimateBufferSize(final int defaultValueLength, final int numberOfMappings, final int estimatedMappingLength) {
            return ImmutableDomainNameMapping.REPR_CONST_PART_LENGTH + defaultValueLength + (int)(estimatedMappingLength * numberOfMappings * 1.1);
        }
        
        private StringBuilder appendMapping(final StringBuilder sb, final int mappingIndex) {
            return appendMapping(sb, this.domainNamePatterns[mappingIndex], this.values[mappingIndex].toString());
        }
        
        private static StringBuilder appendMapping(final StringBuilder sb, final String domainNamePattern, final String value) {
            return sb.append(domainNamePattern).append('=').append(value);
        }
        
        static {
            REPR_CONST_PART_LENGTH = "ImmutableDomainNameMapping(default: ".length() + ", map: {".length() + "})".length();
        }
    }
}
