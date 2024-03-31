// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.handler;

import org.cef.callback.CefMenuModel;
import org.cef.callback.CefContextMenuParams;
import org.cef.browser.CefFrame;
import org.cef.browser.CefBrowser;

public interface CefContextMenuHandler
{
    void onBeforeContextMenu(final CefBrowser p0, final CefFrame p1, final CefContextMenuParams p2, final CefMenuModel p3);
    
    boolean onContextMenuCommand(final CefBrowser p0, final CefFrame p1, final CefContextMenuParams p2, final int p3, final int p4);
    
    void onContextMenuDismissed(final CefBrowser p0, final CefFrame p1);
}
