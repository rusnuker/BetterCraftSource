// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.handler;

import org.cef.callback.CefDragData;
import java.nio.ByteBuffer;
import java.awt.Point;
import java.awt.Rectangle;
import org.cef.browser.CefBrowser;

public interface CefRenderHandler
{
    Rectangle getViewRect(final CefBrowser p0);
    
    Point getScreenPoint(final CefBrowser p0, final Point p1);
    
    void onPopupShow(final CefBrowser p0, final boolean p1);
    
    void onPopupSize(final CefBrowser p0, final Rectangle p1);
    
    void onPaint(final CefBrowser p0, final boolean p1, final Rectangle[] p2, final ByteBuffer p3, final int p4, final int p5);
    
    void onCursorChange(final CefBrowser p0, final int p1);
    
    boolean startDragging(final CefBrowser p0, final CefDragData p1, final int p2, final int p3, final int p4);
    
    void updateDragCursor(final CefBrowser p0, final int p1);
}
