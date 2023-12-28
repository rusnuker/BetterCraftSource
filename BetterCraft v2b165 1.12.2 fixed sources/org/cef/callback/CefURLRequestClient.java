// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.callback;

import org.cef.network.CefURLRequest;

public interface CefURLRequestClient extends CefNative
{
    void onRequestComplete(final CefURLRequest p0);
    
    void onUploadProgress(final CefURLRequest p0, final int p1, final int p2);
    
    void onDownloadProgress(final CefURLRequest p0, final int p1, final int p2);
    
    void onDownloadData(final CefURLRequest p0, final byte[] p1, final int p2);
    
    boolean getAuthCredentials(final boolean p0, final String p1, final int p2, final String p3, final String p4, final CefAuthCallback p5);
}
