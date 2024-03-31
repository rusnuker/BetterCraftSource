// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.handler;

import org.cef.misc.BoolRef;
import org.cef.callback.CefJSDialogCallback;
import org.cef.browser.CefBrowser;

public abstract class CefJSDialogHandlerAdapter implements CefJSDialogHandler
{
    @Override
    public boolean onJSDialog(final CefBrowser browser, final String origin_url, final JSDialogType dialog_type, final String message_text, final String default_prompt_text, final CefJSDialogCallback callback, final BoolRef suppress_message) {
        return false;
    }
    
    @Override
    public boolean onBeforeUnloadDialog(final CefBrowser browser, final String message_text, final boolean is_reload, final CefJSDialogCallback callback) {
        return false;
    }
    
    @Override
    public void onResetDialogState(final CefBrowser browser) {
    }
    
    @Override
    public void onDialogClosed(final CefBrowser browser) {
    }
}
