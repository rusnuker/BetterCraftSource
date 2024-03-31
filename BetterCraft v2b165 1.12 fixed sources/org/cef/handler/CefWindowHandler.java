// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.handler;

import java.awt.Rectangle;
import org.cef.browser.CefBrowser;

public interface CefWindowHandler
{
    Rectangle getRect(final CefBrowser p0);
    
    void onMouseEvent(final CefBrowser p0, final int p1, final int p2, final int p3, final int p4, final int p5);
}
