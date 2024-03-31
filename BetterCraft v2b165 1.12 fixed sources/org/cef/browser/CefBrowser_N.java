// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.browser;

import java.awt.Rectangle;
import org.cef.callback.CefDragData;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;
import org.cef.callback.CefPdfPrintCallback;
import org.cef.misc.CefPdfPrintSettings;
import org.cef.callback.CefRunFileDialogCallback;
import org.cef.handler.CefDialogHandler;
import org.cef.network.CefRequest;
import org.cef.callback.CefStringVisitor;
import java.util.Vector;
import org.cef.handler.CefClientHandler;
import java.awt.Component;
import java.awt.AWTEvent;
import java.awt.event.WindowEvent;
import java.awt.Window;
import javax.swing.SwingUtilities;
import org.cef.handler.CefWindowHandler;
import org.cef.handler.CefRenderHandler;
import java.awt.Point;
import org.cef.CefClient;
import org.cef.callback.CefNativeAdapter;

abstract class CefBrowser_N extends CefNativeAdapter implements CefBrowser
{
    private boolean isPending_;
    private CefClient client_;
    private String url_;
    private CefRequestContext request_context_;
    private CefBrowser_N parent_;
    private Point inspectAt_;
    private CefBrowser_N devTools_;
    private boolean closeAllowed_;
    private boolean isClosed_;
    
    protected CefBrowser_N(final CefClient client, final String url, final CefRequestContext context, final CefBrowser_N parent, final Point inspectAt) {
        this.isPending_ = false;
        this.parent_ = null;
        this.inspectAt_ = null;
        this.devTools_ = null;
        this.closeAllowed_ = false;
        this.isClosed_ = false;
        this.client_ = client;
        this.url_ = url;
        this.request_context_ = context;
        this.parent_ = parent;
        this.inspectAt_ = inspectAt;
    }
    
    protected String getUrl() {
        return this.url_;
    }
    
    protected CefRequestContext getRequestContext() {
        return this.request_context_;
    }
    
    protected CefBrowser_N getParentBrowser() {
        return this.parent_;
    }
    
    protected Point getInspectAt() {
        return this.inspectAt_;
    }
    
    protected boolean isClosed() {
        return this.isClosed_;
    }
    
    @Override
    public CefClient getClient() {
        return this.client_;
    }
    
    @Override
    public CefRenderHandler getRenderHandler() {
        return null;
    }
    
    @Override
    public CefWindowHandler getWindowHandler() {
        return null;
    }
    
    @Override
    public synchronized void setCloseAllowed() {
        this.closeAllowed_ = true;
    }
    
    @Override
    public synchronized boolean doClose() {
        if (this.closeAllowed_) {
            return false;
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                final Component parent = SwingUtilities.getRoot(CefBrowser_N.this.getUIComponent());
                if (parent != null) {
                    parent.dispatchEvent(new WindowEvent((Window)parent, 201));
                }
            }
        });
        return true;
    }
    
    @Override
    public synchronized void onBeforeClose() {
        this.isClosed_ = true;
        if (this.request_context_ != null) {
            this.request_context_.dispose();
        }
        if (this.parent_ != null) {
            this.parent_.closeDevTools();
            this.parent_.devTools_ = null;
            this.parent_ = null;
        }
    }
    
    @Override
    public CefBrowser getDevTools() {
        return this.getDevTools(null);
    }
    
    @Override
    public synchronized CefBrowser getDevTools(final Point inspectAt) {
        if (this.devTools_ == null) {
            this.devTools_ = this.createDevToolsBrowser(this.client_, this.url_, this.request_context_, this, inspectAt);
        }
        return this.devTools_;
    }
    
    protected abstract CefBrowser_N createDevToolsBrowser(final CefClient p0, final String p1, final CefRequestContext p2, final CefBrowser_N p3, final Point p4);
    
    protected void createBrowser(final CefClientHandler clientHandler, final long windowHandle, final String url, final boolean osr, final boolean transparent, final Component canvas, final CefRequestContext context) {
        if (this.getNativeRef("CefBrowser") == 0L && !this.isPending_) {
            try {
                this.isPending_ = this.N_CreateBrowser(clientHandler, windowHandle, url, osr, transparent, canvas, context);
            }
            catch (final UnsatisfiedLinkError err) {
                err.printStackTrace();
            }
        }
    }
    
    protected final void createDevTools(final CefBrowser_N parent, final CefClientHandler clientHandler, final long windowHandle, final boolean osr, final boolean transparent, final Component canvas, final Point inspectAt) {
        if (this.getNativeRef("CefBrowser") == 0L && !this.isPending_) {
            try {
                this.isPending_ = this.N_CreateDevTools(parent, clientHandler, windowHandle, osr, transparent, canvas, inspectAt);
            }
            catch (final UnsatisfiedLinkError err) {
                err.printStackTrace();
            }
        }
    }
    
    protected final long getWindowHandle(final long surfaceHandle) {
        try {
            return this.N_GetWindowHandle(surfaceHandle);
        }
        catch (final UnsatisfiedLinkError err) {
            err.printStackTrace();
            return 0L;
        }
    }
    
    @Override
    protected void finalize() throws Throwable {
        this.close(true);
        super.finalize();
    }
    
    @Override
    public boolean canGoBack() {
        try {
            return this.N_CanGoBack();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return false;
        }
    }
    
    @Override
    public void goBack() {
        try {
            this.N_GoBack();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    @Override
    public boolean canGoForward() {
        try {
            return this.N_CanGoForward();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return false;
        }
    }
    
    @Override
    public void goForward() {
        try {
            this.N_GoForward();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    @Override
    public boolean isLoading() {
        try {
            return this.N_IsLoading();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return false;
        }
    }
    
    @Override
    public void reload() {
        try {
            this.N_Reload();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    @Override
    public void reloadIgnoreCache() {
        try {
            this.N_ReloadIgnoreCache();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    @Override
    public void stopLoad() {
        try {
            this.N_StopLoad();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    @Override
    public int getIdentifier() {
        try {
            return this.N_GetIdentifier();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return -1;
        }
    }
    
    @Override
    public CefFrame getMainFrame() {
        try {
            return this.N_GetMainFrame();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return null;
        }
    }
    
    @Override
    public CefFrame getFocusedFrame() {
        try {
            return this.N_GetFocusedFrame();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return null;
        }
    }
    
    @Override
    public CefFrame getFrame(final long identifier) {
        try {
            return this.N_GetFrame(identifier);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return null;
        }
    }
    
    @Override
    public CefFrame getFrame(final String name) {
        try {
            return this.N_GetFrame2(name);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return null;
        }
    }
    
    @Override
    public Vector<Long> getFrameIdentifiers() {
        try {
            return this.N_GetFrameIdentifiers();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return null;
        }
    }
    
    @Override
    public Vector<String> getFrameNames() {
        try {
            return this.N_GetFrameNames();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return null;
        }
    }
    
    @Override
    public int getFrameCount() {
        try {
            return this.N_GetFrameCount();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return -1;
        }
    }
    
    @Override
    public boolean isPopup() {
        try {
            return this.N_IsPopup();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean hasDocument() {
        try {
            return this.N_HasDocument();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return false;
        }
    }
    
    @Override
    public void viewSource() {
        try {
            this.N_ViewSource();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    @Override
    public void getSource(final CefStringVisitor visitor) {
        try {
            this.N_GetSource(visitor);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    @Override
    public void getText(final CefStringVisitor visitor) {
        try {
            this.N_GetText(visitor);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    @Override
    public void loadRequest(final CefRequest request) {
        try {
            this.N_LoadRequest(request);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    @Override
    public void loadURL(final String url) {
        try {
            this.N_LoadURL(url);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    @Override
    public void loadString(final String val, final String url) {
        try {
            this.N_LoadString(val, url);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    @Override
    public void executeJavaScript(final String code, final String url, final int line) {
        try {
            this.N_ExecuteJavaScript(code, url, line);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    @Override
    public String getURL() {
        try {
            return this.N_GetURL();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return "";
        }
    }
    
    @Override
    public void close(final boolean force) {
        try {
            this.N_Close(force);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    @Override
    public void setFocus(final boolean enable) {
        try {
            this.N_SetFocus(enable);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    @Override
    public void setWindowVisibility(final boolean visible) {
        try {
            this.N_SetWindowVisibility(visible);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    @Override
    public double getZoomLevel() {
        try {
            return this.N_GetZoomLevel();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return 0.0;
        }
    }
    
    @Override
    public void setZoomLevel(final double zoomLevel) {
        try {
            this.N_SetZoomLevel(zoomLevel);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    @Override
    public void runFileDialog(final CefDialogHandler.FileDialogMode mode, final String title, final String defaultFilePath, final Vector<String> acceptFilters, final int selectedAcceptFilter, final CefRunFileDialogCallback callback) {
        try {
            this.N_RunFileDialog(mode, title, defaultFilePath, acceptFilters, selectedAcceptFilter, callback);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    @Override
    public void startDownload(final String url) {
        try {
            this.N_StartDownload(url);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    @Override
    public void print() {
        try {
            this.N_Print();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    @Override
    public void printToPDF(final String path, final CefPdfPrintSettings settings, final CefPdfPrintCallback callback) {
        if (path == null || path.isEmpty()) {
            throw new IllegalArgumentException("path was null or empty");
        }
        try {
            this.N_PrintToPDF(path, settings, callback);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    @Override
    public void find(final int identifier, final String searchText, final boolean forward, final boolean matchCase, final boolean findNext) {
        try {
            this.N_Find(identifier, searchText, forward, matchCase, findNext);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    @Override
    public void stopFinding(final boolean clearSelection) {
        try {
            this.N_StopFinding(clearSelection);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    protected final void closeDevTools() {
        try {
            this.N_CloseDevTools();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    @Override
    public void replaceMisspelling(final String word) {
        try {
            this.N_ReplaceMisspelling(word);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    protected final void wasResized(final int width, final int height) {
        try {
            this.N_WasResized(width, height);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    protected final void invalidate() {
        try {
            this.N_Invalidate();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    protected final void sendKeyEvent(final KeyEvent e) {
        try {
            this.N_SendKeyEvent(e);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    protected final void sendMouseEvent(final MouseEvent e) {
        try {
            this.N_SendMouseEvent(e);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    protected final void sendMouseWheelEvent(final MouseWheelEvent e) {
        try {
            this.N_SendMouseWheelEvent(e);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    protected final void dragTargetDragEnter(final CefDragData dragData, final Point pos, final int modifiers, final int allowedOps) {
        try {
            this.N_DragTargetDragEnter(dragData, pos, modifiers, allowedOps);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    protected final void dragTargetDragOver(final Point pos, final int modifiers, final int allowedOps) {
        try {
            this.N_DragTargetDragOver(pos, modifiers, allowedOps);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    protected final void dragTargetDragLeave() {
        try {
            this.N_DragTargetDragLeave();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    protected final void dragTargetDrop(final Point pos, final int modifiers) {
        try {
            this.N_DragTargetDrop(pos, modifiers);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    protected final void dragSourceEndedAt(final Point pos, final int operation) {
        try {
            this.N_DragSourceEndedAt(pos, operation);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    protected final void dragSourceSystemDragEnded() {
        try {
            this.N_DragSourceSystemDragEnded();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    protected final void updateUI(final Rectangle contentRect, final Rectangle browserRect) {
        try {
            this.N_UpdateUI(contentRect, browserRect);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    protected final void setParent(final long windowHandle, final Component canvas) {
        try {
            this.N_SetParent(windowHandle, canvas);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    protected final void notifyMoveOrResizeStarted() {
        try {
            this.N_NotifyMoveOrResizeStarted();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    private final native boolean N_CreateBrowser(final CefClientHandler p0, final long p1, final String p2, final boolean p3, final boolean p4, final Component p5, final CefRequestContext p6);
    
    private final native boolean N_CreateDevTools(final CefBrowser p0, final CefClientHandler p1, final long p2, final boolean p3, final boolean p4, final Component p5, final Point p6);
    
    private final native long N_GetWindowHandle(final long p0);
    
    private final native boolean N_CanGoBack();
    
    private final native void N_GoBack();
    
    private final native boolean N_CanGoForward();
    
    private final native void N_GoForward();
    
    private final native boolean N_IsLoading();
    
    private final native void N_Reload();
    
    private final native void N_ReloadIgnoreCache();
    
    private final native void N_StopLoad();
    
    private final native int N_GetIdentifier();
    
    private final native CefFrame N_GetMainFrame();
    
    private final native CefFrame N_GetFocusedFrame();
    
    private final native CefFrame N_GetFrame(final long p0);
    
    private final native CefFrame N_GetFrame2(final String p0);
    
    private final native Vector<Long> N_GetFrameIdentifiers();
    
    private final native Vector<String> N_GetFrameNames();
    
    private final native int N_GetFrameCount();
    
    private final native boolean N_IsPopup();
    
    private final native boolean N_HasDocument();
    
    private final native void N_ViewSource();
    
    private final native void N_GetSource(final CefStringVisitor p0);
    
    private final native void N_GetText(final CefStringVisitor p0);
    
    private final native void N_LoadRequest(final CefRequest p0);
    
    private final native void N_LoadURL(final String p0);
    
    private final native void N_LoadString(final String p0, final String p1);
    
    private final native void N_ExecuteJavaScript(final String p0, final String p1, final int p2);
    
    private final native String N_GetURL();
    
    private final native void N_Close(final boolean p0);
    
    private final native void N_SetFocus(final boolean p0);
    
    private final native void N_SetWindowVisibility(final boolean p0);
    
    private final native double N_GetZoomLevel();
    
    private final native void N_SetZoomLevel(final double p0);
    
    private final native void N_RunFileDialog(final CefDialogHandler.FileDialogMode p0, final String p1, final String p2, final Vector<String> p3, final int p4, final CefRunFileDialogCallback p5);
    
    private final native void N_StartDownload(final String p0);
    
    private final native void N_Print();
    
    private final native void N_PrintToPDF(final String p0, final CefPdfPrintSettings p1, final CefPdfPrintCallback p2);
    
    private final native void N_Find(final int p0, final String p1, final boolean p2, final boolean p3, final boolean p4);
    
    private final native void N_StopFinding(final boolean p0);
    
    private final native void N_CloseDevTools();
    
    private final native void N_ReplaceMisspelling(final String p0);
    
    private final native void N_WasResized(final int p0, final int p1);
    
    private final native void N_Invalidate();
    
    private final native void N_SendKeyEvent(final KeyEvent p0);
    
    private final native void N_SendMouseEvent(final MouseEvent p0);
    
    private final native void N_SendMouseWheelEvent(final MouseWheelEvent p0);
    
    private final native void N_DragTargetDragEnter(final CefDragData p0, final Point p1, final int p2, final int p3);
    
    private final native void N_DragTargetDragOver(final Point p0, final int p1, final int p2);
    
    private final native void N_DragTargetDragLeave();
    
    private final native void N_DragTargetDrop(final Point p0, final int p1);
    
    private final native void N_DragSourceEndedAt(final Point p0, final int p1);
    
    private final native void N_DragSourceSystemDragEnded();
    
    private final native void N_UpdateUI(final Rectangle p0, final Rectangle p1);
    
    private final native void N_SetParent(final long p0, final Component p1);
    
    private final native void N_NotifyMoveOrResizeStarted();
}
