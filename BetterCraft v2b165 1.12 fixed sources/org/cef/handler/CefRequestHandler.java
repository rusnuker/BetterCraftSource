// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.handler;

import org.cef.callback.CefRequestCallback;
import org.cef.callback.CefAuthCallback;
import org.cef.misc.BoolRef;
import org.cef.network.CefRequest;
import org.cef.browser.CefFrame;
import org.cef.browser.CefBrowser;

public interface CefRequestHandler
{
    boolean onBeforeBrowse(final CefBrowser p0, final CefFrame p1, final CefRequest p2, final boolean p3, final boolean p4);
    
    CefResourceRequestHandler getResourceRequestHandler(final CefBrowser p0, final CefFrame p1, final CefRequest p2, final boolean p3, final boolean p4, final String p5, final BoolRef p6);
    
    boolean getAuthCredentials(final CefBrowser p0, final CefFrame p1, final boolean p2, final String p3, final int p4, final String p5, final String p6, final CefAuthCallback p7);
    
    boolean onQuotaRequest(final CefBrowser p0, final String p1, final long p2, final CefRequestCallback p3);
    
    boolean onCertificateError(final CefBrowser p0, final CefLoadHandler.ErrorCode p1, final String p2, final CefRequestCallback p3);
    
    void onPluginCrashed(final CefBrowser p0, final String p1);
    
    void onRenderProcessTerminated(final CefBrowser p0, final TerminationStatus p1);
    
    public enum TerminationStatus
    {
        TS_ABNORMAL_TERMINATION("TS_ABNORMAL_TERMINATION", 0), 
        TS_PROCESS_WAS_KILLED("TS_PROCESS_WAS_KILLED", 1), 
        TS_PROCESS_CRASHED("TS_PROCESS_CRASHED", 2), 
        TS_PROCESS_OOM("TS_PROCESS_OOM", 3);
        
        private TerminationStatus(final String s, final int n) {
        }
    }
}
