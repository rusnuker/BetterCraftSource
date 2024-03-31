// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.network;

import java.util.Map;
import org.cef.handler.CefLoadHandler;
import org.cef.callback.CefNative;

class CefResponse_N extends CefResponse implements CefNative
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
    
    CefResponse_N() {
        this.N_CefHandle = 0L;
    }
    
    public static final CefResponse createNative() {
        final CefResponse_N result = new CefResponse_N();
        try {
            result.N_CefResponse_CTOR();
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
            this.N_CefResponse_DTOR();
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
    public CefLoadHandler.ErrorCode getError() {
        try {
            return this.N_GetError();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return null;
        }
    }
    
    @Override
    public void setError(final CefLoadHandler.ErrorCode errorCode) {
        try {
            this.N_SetError(errorCode);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    @Override
    public int getStatus() {
        try {
            return this.N_GetStatus();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return 0;
        }
    }
    
    @Override
    public void setStatus(final int status) {
        try {
            this.N_SetStatus(status);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    @Override
    public String getStatusText() {
        try {
            return this.N_GetStatusText();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return null;
        }
    }
    
    @Override
    public void setStatusText(final String statusText) {
        try {
            this.N_SetStatusText(statusText);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    @Override
    public String getMimeType() {
        try {
            return this.N_GetMimeType();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return null;
        }
    }
    
    @Override
    public void setMimeType(final String mimeType) {
        try {
            this.N_SetMimeType(mimeType);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    @Override
    public String getHeader(final String name) {
        try {
            return this.N_GetHeader(name);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return null;
        }
    }
    
    @Override
    public void getHeaderMap(final Map<String, String> headerMap) {
        try {
            this.N_GetHeaderMap(headerMap);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    @Override
    public void setHeaderMap(final Map<String, String> headerMap) {
        try {
            this.N_SetHeaderMap(headerMap);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    private final native void N_CefResponse_CTOR();
    
    private final native boolean N_IsReadOnly();
    
    private final native CefLoadHandler.ErrorCode N_GetError();
    
    private final native void N_SetError(final CefLoadHandler.ErrorCode p0);
    
    private final native int N_GetStatus();
    
    private final native void N_SetStatus(final int p0);
    
    private final native String N_GetStatusText();
    
    private final native void N_SetStatusText(final String p0);
    
    private final native String N_GetMimeType();
    
    private final native void N_SetMimeType(final String p0);
    
    private final native String N_GetHeader(final String p0);
    
    private final native void N_GetHeaderMap(final Map<String, String> p0);
    
    private final native void N_SetHeaderMap(final Map<String, String> p0);
    
    private final native void N_CefResponse_DTOR();
}
