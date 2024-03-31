// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.callback;

public class CefNativeAdapter implements CefNative
{
    private long N_CefHandle;
    
    public CefNativeAdapter() {
        this.N_CefHandle = 0L;
    }
    
    @Override
    public void setNativeRef(final String identifer, final long nativeRef) {
        this.N_CefHandle = nativeRef;
    }
    
    @Override
    public long getNativeRef(final String identifer) {
        return this.N_CefHandle;
    }
}
