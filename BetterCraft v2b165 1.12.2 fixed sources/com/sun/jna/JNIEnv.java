// 
// Decompiled by Procyon v0.6.0
// 

package com.sun.jna;

public final class JNIEnv
{
    public static final JNIEnv CURRENT;
    
    private JNIEnv() {
    }
    
    static {
        CURRENT = new JNIEnv();
    }
}
