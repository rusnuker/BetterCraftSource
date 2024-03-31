// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.handler;

import java.awt.Rectangle;
import org.cef.browser.CefBrowser;

public abstract class CefWindowHandlerAdapter implements CefWindowHandler
{
    @Override
    public Rectangle getRect(final CefBrowser browser) {
        return new Rectangle(0, 0, 0, 0);
    }
    
    @Override
    public void onMouseEvent(final CefBrowser browser, final int event, final int screenX, final int screenY, final int modifier, final int button) {
    }
}
