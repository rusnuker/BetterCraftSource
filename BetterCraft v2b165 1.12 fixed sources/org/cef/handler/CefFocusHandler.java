// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.handler;

import org.cef.browser.CefBrowser;

public interface CefFocusHandler
{
    void onTakeFocus(final CefBrowser p0, final boolean p1);
    
    boolean onSetFocus(final CefBrowser p0, final FocusSource p1);
    
    void onGotFocus(final CefBrowser p0);
    
    public enum FocusSource
    {
        FOCUS_SOURCE_NAVIGATION("FOCUS_SOURCE_NAVIGATION", 0), 
        FOCUS_SOURCE_SYSTEM("FOCUS_SOURCE_SYSTEM", 1);
        
        private FocusSource(final String s, final int n) {
        }
    }
}
