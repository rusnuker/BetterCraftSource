// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.network;

import org.cef.callback.CefNative;

class CefPostDataElement_N extends CefPostDataElement implements CefNative
{
    private long N_CefHandle;
    
    @Override
    public void setNativeRef(final String identifer, final long nativeRef) {
        this.N_CefHandle = nativeRef;
    }
    
    @Override
    public long getNativeRef(final String identifer) {
        return this.N_CefHandle;
    }
    
    CefPostDataElement_N() {
        this.N_CefHandle = 0L;
    }
    
    public static final CefPostDataElement createNative() {
        final CefPostDataElement_N result = new CefPostDataElement_N();
        try {
            result.N_CefPostDataElement_CTOR();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
        if (result.N_CefHandle == 0L) {
            return null;
        }
        return result;
    }
    
    @Override
    protected void finalize() throws Throwable {
        try {
            this.N_CefPostDataElement_DTOR();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return;
        }
        finally {
            super.finalize();
        }
        super.finalize();
    }
    
    @Override
    public boolean isReadOnly() {
        try {
            return this.N_IsReadOnly();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return false;
        }
    }
    
    @Override
    public void setToEmpty() {
        try {
            this.N_SetToEmpty();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    @Override
    public void setToFile(final String fileName) {
        try {
            this.N_SetToFile(fileName);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    @Override
    public void setToBytes(final int size, final byte[] bytes) {
        try {
            this.N_SetToBytes(size, bytes);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    @Override
    public Type getType() {
        try {
            return this.N_GetType();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return null;
        }
    }
    
    @Override
    public String getFile() {
        try {
            return this.N_GetFile();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return null;
        }
    }
    
    @Override
    public int getBytesCount() {
        try {
            return this.N_GetBytesCount();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return 0;
        }
    }
    
    @Override
    public int getBytes(final int size, final byte[] bytes) {
        try {
            return this.N_GetBytes(size, bytes);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return 0;
        }
    }
    
    private final native void N_CefPostDataElement_CTOR();
    
    private final native boolean N_IsReadOnly();
    
    private final native void N_SetToEmpty();
    
    private final native void N_SetToFile(final String p0);
    
    private final native void N_SetToBytes(final int p0, final byte[] p1);
    
    private final native Type N_GetType();
    
    private final native String N_GetFile();
    
    private final native int N_GetBytesCount();
    
    private final native int N_GetBytes(final int p0, final byte[] p1);
    
    private final native void N_CefPostDataElement_DTOR();
}
