// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.network;

import java.util.Iterator;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;
import org.cef.handler.CefLoadHandler;

public abstract class CefResponse
{
    CefResponse() {
    }
    
    public static final CefResponse create() {
        return CefResponse_N.createNative();
    }
    
    public abstract boolean isReadOnly();
    
    public abstract CefLoadHandler.ErrorCode getError();
    
    public abstract void setError(final CefLoadHandler.ErrorCode p0);
    
    public abstract int getStatus();
    
    public abstract void setStatus(final int p0);
    
    public abstract String getStatusText();
    
    public abstract void setStatusText(final String p0);
    
    public abstract String getMimeType();
    
    public abstract void setMimeType(final String p0);
    
    public abstract String getHeader(final String p0);
    
    public abstract void getHeaderMap(final Map<String, String> p0);
    
    public abstract void setHeaderMap(final Map<String, String> p0);
    
    @Override
    public String toString() {
        String returnValue = "\nHTTP-Response:";
        returnValue = String.valueOf(returnValue) + "\n  error: " + this.getError();
        returnValue = String.valueOf(returnValue) + "\n  readOnly: " + this.isReadOnly();
        returnValue = String.valueOf(returnValue) + "\n    HTTP/1.1 " + this.getStatus() + " " + this.getStatusText();
        returnValue = String.valueOf(returnValue) + "\n    Content-Type: " + this.getMimeType();
        final Map<String, String> headerMap = new HashMap<String, String>();
        this.getHeaderMap(headerMap);
        final Set<Map.Entry<String, String>> entrySet = headerMap.entrySet();
        for (final Map.Entry<String, String> entry : entrySet) {
            returnValue = String.valueOf(returnValue) + "    " + entry.getKey() + "=" + entry.getValue() + "\n";
        }
        return returnValue;
    }
}
