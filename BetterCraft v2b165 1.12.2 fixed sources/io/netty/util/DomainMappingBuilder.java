// 
// Decompiled by Procyon v0.6.0
// 

package io.netty.util;

@Deprecated
public final class DomainMappingBuilder<V>
{
    private final DomainNameMappingBuilder<V> builder;
    
    public DomainMappingBuilder(final V defaultValue) {
        this.builder = new DomainNameMappingBuilder<V>(defaultValue);
    }
    
    public DomainMappingBuilder(final int initialCapacity, final V defaultValue) {
        this.builder = new DomainNameMappingBuilder<V>(initialCapacity, defaultValue);
    }
    
    public DomainMappingBuilder<V> add(final String hostname, final V output) {
        this.builder.add(hostname, output);
        return this;
    }
    
    public DomainNameMapping<V> build() {
        return this.builder.build();
    }
}
