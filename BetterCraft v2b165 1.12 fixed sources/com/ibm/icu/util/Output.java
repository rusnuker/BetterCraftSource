// 
// Decompiled by Procyon v0.6.0
// 

package com.ibm.icu.util;

public class Output<T>
{
    public T value;
    
    @Override
    public String toString() {
        return (this.value == null) ? "null" : this.value.toString();
    }
    
    public Output() {
    }
    
    public Output(final T value) {
        this.value = value;
    }
}
