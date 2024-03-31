// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.misc;

public class StringRef
{
    private String value_;
    
    public StringRef() {
    }
    
    public StringRef(final String value) {
        this.value_ = value;
    }
    
    public void set(final String value) {
        this.value_ = value;
    }
    
    public String get() {
        return this.value_;
    }
}
