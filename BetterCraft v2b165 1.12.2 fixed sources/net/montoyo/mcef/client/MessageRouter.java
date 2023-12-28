// 
// Decompiled by Procyon v0.6.0
// 

package net.montoyo.mcef.client;

import net.montoyo.mcef.api.IJSQueryCallback;
import net.montoyo.mcef.api.IBrowser;
import org.cef.browser.CefBrowserOsr;
import org.cef.callback.CefQueryCallback;
import org.cef.browser.CefFrame;
import org.cef.browser.CefBrowser;
import net.montoyo.mcef.api.IJSQueryHandler;
import org.cef.handler.CefMessageRouterHandlerAdapter;

public class MessageRouter extends CefMessageRouterHandlerAdapter
{
    private IJSQueryHandler handler;
    
    public MessageRouter(final IJSQueryHandler h) {
        this.handler = h;
    }
    
    @Override
    public boolean onQuery(final CefBrowser browser, final CefFrame frame, final long query_id, final String request, final boolean persistent, final CefQueryCallback callback) {
        return this.handler.handleQuery((IBrowser)browser, query_id, request, persistent, new QueryCallback(callback));
    }
    
    @Override
    public void onQueryCanceled(final CefBrowser browser, final CefFrame frame, final long query_id) {
        this.handler.cancelQuery((IBrowser)browser, query_id);
    }
}
