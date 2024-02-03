// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.libs.gson.stream;

import java.io.IOException;

public final class MalformedJsonException extends IOException
{
    private static final long serialVersionUID = 1L;
    
    public MalformedJsonException(final String msg) {
        super(msg);
    }
    
    public MalformedJsonException(final String msg, final Throwable throwable) {
        super(msg, throwable);
    }
    
    public MalformedJsonException(final Throwable throwable) {
        super(throwable);
    }
}
