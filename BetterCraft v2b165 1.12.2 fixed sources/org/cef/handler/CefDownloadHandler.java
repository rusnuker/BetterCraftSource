// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.handler;

import org.cef.callback.CefDownloadItemCallback;
import org.cef.callback.CefBeforeDownloadCallback;
import org.cef.callback.CefDownloadItem;
import org.cef.browser.CefBrowser;

public interface CefDownloadHandler
{
    void onBeforeDownload(final CefBrowser p0, final CefDownloadItem p1, final String p2, final CefBeforeDownloadCallback p3);
    
    void onDownloadUpdated(final CefBrowser p0, final CefDownloadItem p1, final CefDownloadItemCallback p2);
}
