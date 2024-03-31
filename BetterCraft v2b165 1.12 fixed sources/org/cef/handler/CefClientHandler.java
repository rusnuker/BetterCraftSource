// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.handler;

import org.cef.browser.CefBrowser;
import org.cef.browser.CefMessageRouter;
import java.util.Vector;
import java.util.HashMap;
import org.cef.callback.CefNative;

public abstract class CefClientHandler implements CefNative
{
    private HashMap<String, Long> N_CefHandle;
    private Vector<CefMessageRouter> msgRouters;
    
    @Override
    public void setNativeRef(final String identifer, final long nativeRef) {
        synchronized (this.N_CefHandle) {
            this.N_CefHandle.put(identifer, nativeRef);
            monitorexit(this.N_CefHandle);
        }
    }
    
    @Override
    public long getNativeRef(final String identifer) {
        synchronized (this.N_CefHandle) {
            if (this.N_CefHandle.containsKey(identifer)) {
                final long longValue = this.N_CefHandle.get(identifer);
                monitorexit(this.N_CefHandle);
                return longValue;
            }
            monitorexit(this.N_CefHandle);
        }
        return 0L;
    }
    
    public CefClientHandler() {
        this.N_CefHandle = new HashMap<String, Long>();
        this.msgRouters = new Vector<CefMessageRouter>();
        try {
            this.N_CefClientHandler_CTOR();
        }
        catch (final UnsatisfiedLinkError err) {
            err.printStackTrace();
        }
    }
    
    protected void dispose() {
        try {
            for (int i = 0; i < this.msgRouters.size(); ++i) {
                this.msgRouters.get(i).dispose();
            }
            this.msgRouters.clear();
            this.N_CefClientHandler_DTOR();
        }
        catch (final UnsatisfiedLinkError err) {
            err.printStackTrace();
        }
    }
    
    protected abstract CefBrowser getBrowser(final int p0);
    
    protected abstract Object[] getAllBrowser();
    
    protected abstract CefContextMenuHandler getContextMenuHandler();
    
    protected abstract CefDialogHandler getDialogHandler();
    
    protected abstract CefDisplayHandler getDisplayHandler();
    
    protected abstract CefDownloadHandler getDownloadHandler();
    
    protected abstract CefDragHandler getDragHandler();
    
    protected abstract CefFocusHandler getFocusHandler();
    
    protected abstract CefJSDialogHandler getJSDialogHandler();
    
    protected abstract CefKeyboardHandler getKeyboardHandler();
    
    protected abstract CefLifeSpanHandler getLifeSpanHandler();
    
    protected abstract CefLoadHandler getLoadHandler();
    
    protected abstract CefRenderHandler getRenderHandler();
    
    protected abstract CefRequestHandler getRequestHandler();
    
    protected abstract CefWindowHandler getWindowHandler();
    
    protected synchronized void addMessageRouter(final CefMessageRouter h) {
        try {
            this.msgRouters.add(h);
            this.N_addMessageRouter(h);
        }
        catch (final UnsatisfiedLinkError err) {
            err.printStackTrace();
        }
    }
    
    protected void removeContextMenuHandler(final CefContextMenuHandler h) {
        try {
            this.N_removeContextMenuHandler(h);
        }
        catch (final UnsatisfiedLinkError err) {
            err.printStackTrace();
        }
    }
    
    protected void removeDialogHandler(final CefDialogHandler h) {
        try {
            this.N_removeDialogHandler(h);
        }
        catch (final UnsatisfiedLinkError err) {
            err.printStackTrace();
        }
    }
    
    protected void removeDisplayHandler(final CefDisplayHandler h) {
        try {
            this.N_removeDisplayHandler(h);
        }
        catch (final UnsatisfiedLinkError err) {
            err.printStackTrace();
        }
    }
    
    protected void removeDownloadHandler(final CefDisplayHandler h) {
        try {
            this.N_removeDownloadHandler(h);
        }
        catch (final UnsatisfiedLinkError err) {
            err.printStackTrace();
        }
    }
    
    protected void removeDragHandler(final CefDragHandler h) {
        try {
            this.N_removeDragHandler(h);
        }
        catch (final UnsatisfiedLinkError err) {
            err.printStackTrace();
        }
    }
    
    protected void removeFocusHandler(final CefFocusHandler h) {
        try {
            this.N_removeFocusHandler(h);
        }
        catch (final UnsatisfiedLinkError err) {
            err.printStackTrace();
        }
    }
    
    protected void removeJSDialogHandler(final CefJSDialogHandler h) {
        try {
            this.N_removeJSDialogHandler(h);
        }
        catch (final UnsatisfiedLinkError err) {
            err.printStackTrace();
        }
    }
    
    protected void removeKeyboardHandler(final CefKeyboardHandler h) {
        try {
            this.N_removeKeyboardHandler(h);
        }
        catch (final UnsatisfiedLinkError err) {
            err.printStackTrace();
        }
    }
    
    protected void removeLifeSpanHandler(final CefLifeSpanHandler h) {
        try {
            this.N_removeLifeSpanHandler(h);
        }
        catch (final UnsatisfiedLinkError err) {
            err.printStackTrace();
        }
    }
    
    protected void removeLoadHandler(final CefLoadHandler h) {
        try {
            this.N_removeLoadHandler(h);
        }
        catch (final UnsatisfiedLinkError err) {
            err.printStackTrace();
        }
    }
    
    protected synchronized void removeMessageRouter(final CefMessageRouter h) {
        try {
            this.msgRouters.remove(h);
            this.N_removeMessageRouter(h);
        }
        catch (final UnsatisfiedLinkError err) {
            err.printStackTrace();
        }
    }
    
    protected void removeRenderHandler(final CefRenderHandler h) {
        try {
            this.N_removeRenderHandler(h);
        }
        catch (final UnsatisfiedLinkError err) {
            err.printStackTrace();
        }
    }
    
    protected void removeRequestHandler(final CefRequestHandler h) {
        try {
            this.N_removeRequestHandler(h);
        }
        catch (final UnsatisfiedLinkError err) {
            err.printStackTrace();
        }
    }
    
    protected void removeWindowHandler(final CefWindowHandler h) {
        try {
            this.N_removeWindowHandler(h);
        }
        catch (final UnsatisfiedLinkError err) {
            err.printStackTrace();
        }
    }
    
    private final native void N_CefClientHandler_CTOR();
    
    private final native void N_addMessageRouter(final CefMessageRouter p0);
    
    private final native void N_removeContextMenuHandler(final CefContextMenuHandler p0);
    
    private final native void N_removeDialogHandler(final CefDialogHandler p0);
    
    private final native void N_removeDisplayHandler(final CefDisplayHandler p0);
    
    private final native void N_removeDownloadHandler(final CefDisplayHandler p0);
    
    private final native void N_removeDragHandler(final CefDragHandler p0);
    
    private final native void N_removeFocusHandler(final CefFocusHandler p0);
    
    private final native void N_removeJSDialogHandler(final CefJSDialogHandler p0);
    
    private final native void N_removeKeyboardHandler(final CefKeyboardHandler p0);
    
    private final native void N_removeLifeSpanHandler(final CefLifeSpanHandler p0);
    
    private final native void N_removeLoadHandler(final CefLoadHandler p0);
    
    private final native void N_removeMessageRouter(final CefMessageRouter p0);
    
    private final native void N_removeRenderHandler(final CefRenderHandler p0);
    
    private final native void N_removeRequestHandler(final CefRequestHandler p0);
    
    private final native void N_removeWindowHandler(final CefWindowHandler p0);
    
    private final native void N_CefClientHandler_DTOR();
}
