// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.gson;

import java.util.Arrays;
import java.util.Collection;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Objects;
import java.lang.reflect.Field;

public final class FieldAttributes
{
    private final Field field;
    
    public FieldAttributes(final Field f) {
        this.field = Objects.requireNonNull(f);
    }
    
    public Class<?> getDeclaringClass() {
        return this.field.getDeclaringClass();
    }
    
    public String getName() {
        return this.field.getName();
    }
    
    public Type getDeclaredType() {
        return this.field.getGenericType();
    }
    
    public Class<?> getDeclaredClass() {
        return this.field.getType();
    }
    
    public <T extends Annotation> T getAnnotation(final Class<T> annotation) {
        return this.field.getAnnotation(annotation);
    }
    
    public Collection<Annotation> getAnnotations() {
        return Arrays.asList(this.field.getAnnotations());
    }
    
    public boolean hasModifier(final int modifier) {
        return (this.field.getModifiers() & modifier) != 0x0;
    }
    
    @Override
    public String toString() {
        return this.field.toString();
    }
}
