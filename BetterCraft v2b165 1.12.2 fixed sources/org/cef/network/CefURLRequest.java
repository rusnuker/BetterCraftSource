// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.network;

import org.cef.handler.CefLoadHandler;
import org.cef.callback.CefURLRequestClient;

public abstract class CefURLRequest
{
    CefURLRequest() {
    }
    
    public static final CefURLRequest create(final CefRequest request, final CefURLRequestClient client) {
        return CefURLRequest_N.createNative(request, client);
    }
    
    public abstract void finalize();
    
    public abstract CefRequest getRequest();
    
    public abstract CefURLRequestClient getClient();
    
    public abstract Status getRequestStatus();
    
    public abstract CefLoadHandler.ErrorCode getRequestError();
    
    public abstract CefResponse getResponse();
    
    public abstract void cancel();
    
    public enum Status
    {
        UR_UNKNOWN("UR_UNKNOWN", 0), 
        UR_SUCCESS("UR_SUCCESS", 1), 
        UR_IO_PENDING("UR_IO_PENDING", 2), 
        UR_CANCELED("UR_CANCELED", 3), 
        UR_FAILED("UR_FAILED", 4);
        
        private Status(final String s, final int n) {
        }
    }
}
