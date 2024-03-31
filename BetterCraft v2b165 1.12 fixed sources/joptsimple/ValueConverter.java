// 
// Decompiled by Procyon v0.6.0
// 

package joptsimple;

public interface ValueConverter<V>
{
    V convert(final String p0);
    
    Class<? extends V> valueType();
    
    String valuePattern();
}
