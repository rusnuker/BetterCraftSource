// 
// Decompiled by Procyon v0.6.0
// 

package com.google.common.base;

import com.google.common.annotations.GwtCompatible;

@GwtCompatible
abstract class CommonMatcher
{
    abstract boolean matches();
    
    abstract boolean find();
    
    abstract boolean find(final int p0);
    
    abstract String replaceAll(final String p0);
    
    abstract int end();
    
    abstract int start();
}
