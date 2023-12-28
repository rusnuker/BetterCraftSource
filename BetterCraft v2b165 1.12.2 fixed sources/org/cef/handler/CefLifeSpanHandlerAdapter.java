// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.handler;

import org.cef.browser.CefFrame;
import org.cef.browser.CefBrowser;

public abstract class CefLifeSpanHandlerAdapter implements CefLifeSpanHandler
{
    @Override
    public boolean onBeforePopup(final CefBrowser browser, final CefFrame frame, final String target_url, final String target_frame_name) {
        return false;
    }
    
    @Override
    public void onAfterCreated(final CefBrowser browser) {
    }
    
    @Override
    public void onAfterParentChanged(final CefBrowser browser) {
    }
    
    @Override
    public boolean doClose(final CefBrowser browser) {
        return false;
    }
    
    @Override
    public void onBeforeClose(final CefBrowser browser) {
    }
}
