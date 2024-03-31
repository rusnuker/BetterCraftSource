// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.handler;

import org.cef.callback.CefDragData;
import java.nio.ByteBuffer;
import java.awt.Point;
import java.awt.Rectangle;
import org.cef.browser.CefBrowser;

public abstract class CefRenderHandlerAdapter implements CefRenderHandler
{
    @Override
    public void onCursorChange(final CefBrowser browser, final int cursorIdentifer) {
    }
    
    @Override
    public Rectangle getViewRect(final CefBrowser browser) {
        return new Rectangle(0, 0, 0, 0);
    }
    
    @Override
    public Point getScreenPoint(final CefBrowser browser, final Point viewPoint) {
        return new Point(0, 0);
    }
    
    @Override
    public void onPopupShow(final CefBrowser browser, final boolean show) {
    }
    
    @Override
    public void onPopupSize(final CefBrowser browser, final Rectangle size) {
    }
    
    @Override
    public void onPaint(final CefBrowser browser, final boolean popup, final Rectangle[] dirtyRects, final ByteBuffer buffer, final int width, final int height) {
    }
    
    @Override
    public boolean startDragging(final CefBrowser browser, final CefDragData dragData, final int mask, final int x, final int y) {
        return false;
    }
    
    @Override
    public void updateDragCursor(final CefBrowser browser, final int operation) {
    }
}
