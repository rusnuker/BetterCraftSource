// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.misc;

public class IntRef
{
    private int value_;
    
    public IntRef() {
    }
    
    public IntRef(final int value) {
        this.value_ = value;
    }
    
    public void set(final int value) {
        this.value_ = value;
    }
    
    public int get() {
        return this.value_;
    }
}
