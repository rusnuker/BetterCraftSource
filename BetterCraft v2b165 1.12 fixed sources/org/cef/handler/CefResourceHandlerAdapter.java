// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.handler;

import org.cef.misc.StringRef;
import org.cef.misc.IntRef;
import org.cef.network.CefResponse;
import org.cef.callback.CefCallback;
import org.cef.network.CefRequest;
import org.cef.callback.CefNativeAdapter;

public abstract class CefResourceHandlerAdapter extends CefNativeAdapter implements CefResourceHandler
{
    @Override
    public boolean processRequest(final CefRequest request, final CefCallback callback) {
        return false;
    }
    
    @Override
    public void getResponseHeaders(final CefResponse response, final IntRef responseLength, final StringRef redirectUrl) {
    }
    
    @Override
    public boolean readResponse(final byte[] dataOut, final int bytesToRead, final IntRef bytesRead, final CefCallback callback) {
        return false;
    }
    
    @Override
    public void cancel() {
    }
}
