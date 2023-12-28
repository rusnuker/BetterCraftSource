// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.handler;

import org.cef.callback.CefDownloadItemCallback;
import org.cef.callback.CefBeforeDownloadCallback;
import org.cef.callback.CefDownloadItem;
import org.cef.browser.CefBrowser;

public abstract class CefDownloadHandlerAdapter implements CefDownloadHandler
{
    @Override
    public void onBeforeDownload(final CefBrowser browser, final CefDownloadItem downloadItem, final String suggestedName, final CefBeforeDownloadCallback callback) {
    }
    
    @Override
    public void onDownloadUpdated(final CefBrowser browser, final CefDownloadItem downloadItem, final CefDownloadItemCallback callback) {
    }
}
