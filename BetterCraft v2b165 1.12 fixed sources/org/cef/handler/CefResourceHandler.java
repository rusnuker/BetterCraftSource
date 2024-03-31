// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.handler;

import org.cef.misc.StringRef;
import org.cef.misc.IntRef;
import org.cef.network.CefResponse;
import org.cef.callback.CefCallback;
import org.cef.network.CefRequest;
import org.cef.callback.CefNative;

public interface CefResourceHandler extends CefNative
{
    boolean processRequest(final CefRequest p0, final CefCallback p1);
    
    void getResponseHeaders(final CefResponse p0, final IntRef p1, final StringRef p2);
    
    boolean readResponse(final byte[] p0, final int p1, final IntRef p2, final CefCallback p3);
    
    void cancel();
}
