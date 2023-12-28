// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.handler;

import org.cef.callback.CefMenuModel;
import org.cef.callback.CefContextMenuParams;
import org.cef.browser.CefFrame;
import org.cef.browser.CefBrowser;

public abstract class CefContextMenuHandlerAdapter implements CefContextMenuHandler
{
    @Override
    public void onBeforeContextMenu(final CefBrowser browser, final CefFrame frame, final CefContextMenuParams params, final CefMenuModel model) {
    }
    
    @Override
    public boolean onContextMenuCommand(final CefBrowser browser, final CefFrame frame, final CefContextMenuParams params, final int commandId, final int eventFlags) {
        return false;
    }
    
    @Override
    public void onContextMenuDismissed(final CefBrowser browser, final CefFrame frame) {
    }
}
