// 
// Decompiled by Procyon v0.6.0
// 

package joptsimple.internal;

import java.lang.reflect.Constructor;
import joptsimple.ValueConverter;

class ConstructorInvokingValueConverter<V> implements ValueConverter<V>
{
    private final Constructor<V> ctor;
    
    ConstructorInvokingValueConverter(final Constructor<V> ctor) {
        this.ctor = ctor;
    }
    
    @Override
    public V convert(final String value) {
        return Reflection.instantiate(this.ctor, value);
    }
    
    @Override
    public Class<V> valueType() {
        return this.ctor.getDeclaringClass();
    }
    
    @Override
    public String valuePattern() {
        return null;
    }
}
