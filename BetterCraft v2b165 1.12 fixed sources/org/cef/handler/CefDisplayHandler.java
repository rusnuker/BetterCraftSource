// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.handler;

import org.cef.CefSettings;
import org.cef.browser.CefFrame;
import org.cef.browser.CefBrowser;

public interface CefDisplayHandler
{
    void onAddressChange(final CefBrowser p0, final CefFrame p1, final String p2);
    
    void onTitleChange(final CefBrowser p0, final String p1);
    
    boolean onTooltip(final CefBrowser p0, final String p1);
    
    void onStatusMessage(final CefBrowser p0, final String p1);
    
    boolean onConsoleMessage(final CefBrowser p0, final CefSettings.LogSeverity p1, final String p2, final String p3, final int p4);
}
