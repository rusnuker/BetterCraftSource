// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.misc;

public class BoolRef
{
    private boolean value_;
    
    public BoolRef() {
    }
    
    public BoolRef(final boolean value) {
        this.value_ = value;
    }
    
    public void set(final boolean value) {
        this.value_ = value;
    }
    
    public boolean get() {
        return this.value_;
    }
}
