// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.handler;

import org.cef.browser.CefBrowser;

public abstract class CefFocusHandlerAdapter implements CefFocusHandler
{
    @Override
    public void onTakeFocus(final CefBrowser browser, final boolean next) {
    }
    
    @Override
    public boolean onSetFocus(final CefBrowser browser, final FocusSource source) {
        return false;
    }
    
    @Override
    public void onGotFocus(final CefBrowser browser) {
    }
}
