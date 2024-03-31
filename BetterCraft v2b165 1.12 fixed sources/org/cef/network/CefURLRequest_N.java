// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.network;

import org.cef.handler.CefLoadHandler;
import org.cef.callback.CefURLRequestClient;
import org.cef.callback.CefNative;

class CefURLRequest_N extends CefURLRequest implements CefNative
{
    private long N_CefHandle;
    private final CefRequest request_;
    private final CefURLRequestClient client_;
    
    @Override
    public void setNativeRef(final String identifer, final long nativeRef) {
        this.N_CefHandle = nativeRef;
    }
    
    @Override
    public long getNativeRef(final String identifer) {
        return this.N_CefHandle;
    }
    
    CefURLRequest_N(final CefRequest request, final CefURLRequestClient client) {
        this.N_CefHandle = 0L;
        this.request_ = request;
        this.client_ = client;
    }
    
    public static final CefURLRequest createNative(final CefRequest request, final CefURLRequestClient client) {
        final CefURLRequest_N result = new CefURLRequest_N(request, client);
        try {
            result.N_CefURLRequest_CTOR(request, client);
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
    public void finalize() {
        try {
            this.N_CefURLRequest_DTOR();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    @Override
    public CefRequest getRequest() {
        return this.request_;
    }
    
    @Override
    public CefURLRequestClient getClient() {
        return this.client_;
    }
    
    @Override
    public Status getRequestStatus() {
        try {
            return this.N_GetRequestStatus();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return null;
        }
    }
    
    @Override
    public CefLoadHandler.ErrorCode getRequestError() {
        try {
            return this.N_GetRequestError();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return null;
        }
    }
    
    @Override
    public CefResponse getResponse() {
        try {
            return this.N_GetResponse();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return null;
        }
    }
    
    @Override
    public void cancel() {
        try {
            this.N_Cancel();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    private final native void N_CefURLRequest_CTOR(final CefRequest p0, final CefURLRequestClient p1);
    
    private final native Status N_GetRequestStatus();
    
    private final native CefLoadHandler.ErrorCode N_GetRequestError();
    
    private final native CefResponse N_GetResponse();
    
    private final native void N_Cancel();
    
    private final native void N_CefURLRequest_DTOR();
}
