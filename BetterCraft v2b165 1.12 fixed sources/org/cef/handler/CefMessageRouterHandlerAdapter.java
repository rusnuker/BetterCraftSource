// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.handler;

import org.cef.callback.CefQueryCallback;
import org.cef.browser.CefFrame;
import org.cef.browser.CefBrowser;
import org.cef.callback.CefNativeAdapter;

public abstract class CefMessageRouterHandlerAdapter extends CefNativeAdapter implements CefMessageRouterHandler
{
    @Override
    public boolean onQuery(final CefBrowser browser, final CefFrame frame, final long query_id, final String request, final boolean persistent, final CefQueryCallback callback) {
        return false;
    }
    
    @Override
    public void onQueryCanceled(final CefBrowser browser, final CefFrame frame, final long query_id) {
    }
}
