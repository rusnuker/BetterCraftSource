// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.handler;

import org.cef.CefSettings;
import org.cef.browser.CefFrame;
import org.cef.browser.CefBrowser;

public abstract class CefDisplayHandlerAdapter implements CefDisplayHandler
{
    @Override
    public void onAddressChange(final CefBrowser browser, final CefFrame frame, final String url) {
    }
    
    @Override
    public void onTitleChange(final CefBrowser browser, final String title) {
    }
    
    @Override
    public boolean onTooltip(final CefBrowser browser, final String text) {
        return false;
    }
    
    @Override
    public void onStatusMessage(final CefBrowser browser, final String value) {
    }
    
    @Override
    public boolean onConsoleMessage(final CefBrowser browser, final CefSettings.LogSeverity level, final String message, final String source, final int line) {
        return false;
    }
}
