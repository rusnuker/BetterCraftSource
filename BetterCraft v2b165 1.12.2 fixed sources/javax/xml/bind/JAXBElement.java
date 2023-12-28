// 
// Decompiled by Procyon v0.6.0
// 

package javax.xml.bind;

import javax.xml.namespace.QName;
import java.io.Serializable;

public class JAXBElement<T> implements Serializable
{
    protected final QName name;
    protected final Class<T> declaredType;
    protected final Class scope;
    protected T value;
    protected boolean nil;
    private static final long serialVersionUID = 1L;
    
    public JAXBElement(final QName name, final Class<T> declaredType, Class scope, final T value) {
        this.nil = false;
        if (declaredType == null || name == null) {
            throw new IllegalArgumentException();
        }
        this.declaredType = declaredType;
        if (scope == null) {
            scope = GlobalScope.class;
        }
        this.scope = scope;
        this.name = name;
        this.setValue(value);
    }
    
    public JAXBElement(final QName name, final Class<T> declaredType, final T value) {
        this(name, (Class<Object>)declaredType, GlobalScope.class, value);
    }
    
    public Class<T> getDeclaredType() {
        return this.declaredType;
    }
    
    public QName getName() {
        return this.name;
    }
    
    public void setValue(final T t) {
        this.value = t;
    }
    
    public T getValue() {
        return this.value;
    }
    
    public Class getScope() {
        return this.scope;
    }
    
    public boolean isNil() {
        return this.value == null || this.nil;
    }
    
    public void setNil(final boolean value) {
        this.nil = value;
    }
    
    public boolean isGlobalScope() {
        return this.scope == GlobalScope.class;
    }
    
    public boolean isTypeSubstituted() {
        return this.value != null && this.value.getClass() != this.declaredType;
    }
    
    public static final class GlobalScope
    {
    }
}
