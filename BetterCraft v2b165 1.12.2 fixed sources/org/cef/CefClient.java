// 
// Decompiled by Procyon v0.6.0
// 

package org.cef;

import org.cef.callback.CefRequestCallback;
import org.cef.callback.CefAuthCallback;
import org.cef.handler.CefResourceRequestHandler;
import java.nio.ByteBuffer;
import java.awt.Point;
import java.awt.Rectangle;
import org.cef.browser.CefMessageRouter;
import org.cef.network.CefRequest;
import java.util.Iterator;
import java.util.Collection;
import org.cef.misc.BoolRef;
import org.cef.callback.CefJSDialogCallback;
import java.awt.FocusTraversalPolicy;
import org.cef.callback.CefDragData;
import org.cef.callback.CefDownloadItemCallback;
import org.cef.callback.CefBeforeDownloadCallback;
import org.cef.callback.CefDownloadItem;
import org.cef.callback.CefFileDialogCallback;
import java.util.Vector;
import org.cef.callback.CefMenuModel;
import org.cef.callback.CefContextMenuParams;
import org.cef.browser.CefFrame;
import org.cef.browser.CefBrowserFactory;
import org.cef.browser.CefRequestContext;
import java.awt.Container;
import java.awt.KeyboardFocusManager;
import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.cef.browser.CefBrowser;
import java.util.HashMap;
import org.cef.handler.CefWindowHandler;
import org.cef.handler.CefRequestHandler;
import org.cef.handler.CefRenderHandler;
import org.cef.handler.CefLoadHandler;
import org.cef.handler.CefLifeSpanHandler;
import org.cef.handler.CefKeyboardHandler;
import org.cef.handler.CefJSDialogHandler;
import org.cef.handler.CefFocusHandler;
import org.cef.handler.CefDragHandler;
import org.cef.handler.CefDownloadHandler;
import org.cef.handler.CefDisplayHandler;
import org.cef.handler.CefDialogHandler;
import org.cef.handler.CefContextMenuHandler;
import org.cef.handler.CefClientHandler;

public class CefClient extends CefClientHandler implements CefContextMenuHandler, CefDialogHandler, CefDisplayHandler, CefDownloadHandler, CefDragHandler, CefFocusHandler, CefJSDialogHandler, CefKeyboardHandler, CefLifeSpanHandler, CefLoadHandler, CefRenderHandler, CefRequestHandler, CefWindowHandler
{
    private HashMap<Integer, CefBrowser> browser_;
    private CefContextMenuHandler contextMenuHandler_;
    private CefDialogHandler dialogHandler_;
    private CefDisplayHandler displayHandler_;
    private CefDownloadHandler downloadHandler_;
    private CefDragHandler dragHandler_;
    private CefFocusHandler focusHandler_;
    private CefJSDialogHandler jsDialogHandler_;
    private CefKeyboardHandler keyboardHandler_;
    private CefLifeSpanHandler lifeSpanHandler_;
    private CefLoadHandler loadHandler_;
    private CefRequestHandler requestHandler_;
    private boolean isDisposed_;
    private volatile CefBrowser focusedBrowser_;
    private final PropertyChangeListener propertyChangeListener;
    
    CefClient() throws UnsatisfiedLinkError {
        this.browser_ = new HashMap<Integer, CefBrowser>();
        this.contextMenuHandler_ = null;
        this.dialogHandler_ = null;
        this.displayHandler_ = null;
        this.downloadHandler_ = null;
        this.dragHandler_ = null;
        this.focusHandler_ = null;
        this.jsDialogHandler_ = null;
        this.keyboardHandler_ = null;
        this.lifeSpanHandler_ = null;
        this.loadHandler_ = null;
        this.requestHandler_ = null;
        this.isDisposed_ = false;
        this.focusedBrowser_ = null;
        this.propertyChangeListener = new PropertyChangeListener() {
            @Override
            public void propertyChange(final PropertyChangeEvent evt) {
                if (CefClient.this.focusedBrowser_ != null) {
                    final Component browserUI = CefClient.this.focusedBrowser_.getUIComponent();
                    final Object oldUI = evt.getOldValue();
                    if (CefClient.this.isPartOf(oldUI, browserUI)) {
                        CefClient.this.focusedBrowser_.setFocus(false);
                        CefClient.access$2(CefClient.this, null);
                    }
                }
            }
        };
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addPropertyChangeListener(this.propertyChangeListener);
    }
    
    private boolean isPartOf(final Object obj, final Component browserUI) {
        if (obj == browserUI) {
            return true;
        }
        if (obj instanceof Container) {
            final Component[] childs = ((Container)obj).getComponents();
            final Component[] array;
            if ((array = childs).length != 0) {
                final Component child = array[0];
                return this.isPartOf(child, browserUI);
            }
        }
        return false;
    }
    
    public void dispose() {
        this.isDisposed_ = true;
        this.cleanupBrowser(-1);
    }
    
    public CefBrowser createBrowser(final String url, final boolean isOffscreenRendered, final boolean isTransparent) {
        return this.createBrowser(url, isOffscreenRendered, isTransparent, null);
    }
    
    public CefBrowser createBrowser(final String url, final boolean isOffscreenRendered, final boolean isTransparent, final CefRequestContext context) {
        if (this.isDisposed_) {
            throw new IllegalStateException("Can't create browser. CefClient is disposed");
        }
        return CefBrowserFactory.create(this, url, isOffscreenRendered, isTransparent, context);
    }
    
    @Override
    protected CefBrowser getBrowser(final int identifier) {
        synchronized (this.browser_) {
            final CefBrowser cefBrowser = this.browser_.get(new Integer(identifier));
            monitorexit(this.browser_);
            return cefBrowser;
        }
    }
    
    @Override
    protected Object[] getAllBrowser() {
        synchronized (this.browser_) {
            final Object[] array = this.browser_.values().toArray();
            monitorexit(this.browser_);
            return array;
        }
    }
    
    @Override
    protected CefContextMenuHandler getContextMenuHandler() {
        return this;
    }
    
    @Override
    protected CefDialogHandler getDialogHandler() {
        return this;
    }
    
    @Override
    protected CefDisplayHandler getDisplayHandler() {
        return this;
    }
    
    @Override
    protected CefDownloadHandler getDownloadHandler() {
        return this;
    }
    
    @Override
    protected CefDragHandler getDragHandler() {
        return this;
    }
    
    @Override
    protected CefFocusHandler getFocusHandler() {
        return this;
    }
    
    @Override
    protected CefJSDialogHandler getJSDialogHandler() {
        return this;
    }
    
    @Override
    protected CefKeyboardHandler getKeyboardHandler() {
        return this;
    }
    
    @Override
    protected CefLifeSpanHandler getLifeSpanHandler() {
        return this;
    }
    
    @Override
    protected CefLoadHandler getLoadHandler() {
        return this;
    }
    
    @Override
    protected CefRenderHandler getRenderHandler() {
        return this;
    }
    
    @Override
    protected CefRequestHandler getRequestHandler() {
        return this;
    }
    
    @Override
    protected CefWindowHandler getWindowHandler() {
        return this;
    }
    
    public CefClient addContextMenuHandler(final CefContextMenuHandler handler) {
        if (this.contextMenuHandler_ == null) {
            this.contextMenuHandler_ = handler;
        }
        return this;
    }
    
    public void removeContextMenuHandler() {
        this.contextMenuHandler_ = null;
    }
    
    @Override
    public void onBeforeContextMenu(final CefBrowser browser, final CefFrame frame, final CefContextMenuParams params, final CefMenuModel model) {
        if (this.contextMenuHandler_ != null && browser != null) {
            this.contextMenuHandler_.onBeforeContextMenu(browser, frame, params, model);
        }
    }
    
    @Override
    public boolean onContextMenuCommand(final CefBrowser browser, final CefFrame frame, final CefContextMenuParams params, final int commandId, final int eventFlags) {
        return this.contextMenuHandler_ != null && browser != null && this.contextMenuHandler_.onContextMenuCommand(browser, frame, params, commandId, eventFlags);
    }
    
    @Override
    public void onContextMenuDismissed(final CefBrowser browser, final CefFrame frame) {
        if (this.contextMenuHandler_ != null && browser != null) {
            this.contextMenuHandler_.onContextMenuDismissed(browser, frame);
        }
    }
    
    public CefClient addDialogHandler(final CefDialogHandler handler) {
        if (this.dialogHandler_ == null) {
            this.dialogHandler_ = handler;
        }
        return this;
    }
    
    public void removeDialogHandler() {
        this.dialogHandler_ = null;
    }
    
    @Override
    public boolean onFileDialog(final CefBrowser browser, final FileDialogMode mode, final String title, final String defaultFilePath, final Vector<String> acceptFilters, final int selectedAcceptFilter, final CefFileDialogCallback callback) {
        return this.dialogHandler_ != null && browser != null && this.dialogHandler_.onFileDialog(browser, mode, title, defaultFilePath, acceptFilters, selectedAcceptFilter, callback);
    }
    
    public CefClient addDisplayHandler(final CefDisplayHandler handler) {
        if (this.displayHandler_ == null) {
            this.displayHandler_ = handler;
        }
        return this;
    }
    
    public void removeDisplayHandler() {
        this.displayHandler_ = null;
    }
    
    @Override
    public void onAddressChange(final CefBrowser browser, final CefFrame frame, final String url) {
        if (this.displayHandler_ != null && browser != null) {
            this.displayHandler_.onAddressChange(browser, frame, url);
        }
    }
    
    @Override
    public void onTitleChange(final CefBrowser browser, final String title) {
        if (this.displayHandler_ != null && browser != null) {
            this.displayHandler_.onTitleChange(browser, title);
        }
    }
    
    @Override
    public boolean onTooltip(final CefBrowser browser, final String text) {
        return this.displayHandler_ != null && browser != null && this.displayHandler_.onTooltip(browser, text);
    }
    
    @Override
    public void onStatusMessage(final CefBrowser browser, final String value) {
        if (this.displayHandler_ != null && browser != null) {
            this.displayHandler_.onStatusMessage(browser, value);
        }
    }
    
    @Override
    public boolean onConsoleMessage(final CefBrowser browser, final CefSettings.LogSeverity level, final String message, final String source, final int line) {
        return this.displayHandler_ != null && browser != null && this.displayHandler_.onConsoleMessage(browser, level, message, source, line);
    }
    
    public CefClient addDownloadHandler(final CefDownloadHandler handler) {
        if (this.downloadHandler_ == null) {
            this.downloadHandler_ = handler;
        }
        return this;
    }
    
    public void removeDownloadHandler() {
        this.downloadHandler_ = null;
    }
    
    @Override
    public void onBeforeDownload(final CefBrowser browser, final CefDownloadItem downloadItem, final String suggestedName, final CefBeforeDownloadCallback callback) {
        if (this.downloadHandler_ != null && browser != null) {
            this.downloadHandler_.onBeforeDownload(browser, downloadItem, suggestedName, callback);
        }
    }
    
    @Override
    public void onDownloadUpdated(final CefBrowser browser, final CefDownloadItem downloadItem, final CefDownloadItemCallback callback) {
        if (this.downloadHandler_ != null && browser != null) {
            this.downloadHandler_.onDownloadUpdated(browser, downloadItem, callback);
        }
    }
    
    public CefClient addDragHandler(final CefDragHandler handler) {
        if (this.dragHandler_ == null) {
            this.dragHandler_ = handler;
        }
        return this;
    }
    
    public void removeDragHandler() {
        this.dragHandler_ = null;
    }
    
    @Override
    public boolean onDragEnter(final CefBrowser browser, final CefDragData dragData, final int mask) {
        return this.dragHandler_ != null && browser != null && this.dragHandler_.onDragEnter(browser, dragData, mask);
    }
    
    public CefClient addFocusHandler(final CefFocusHandler handler) {
        if (this.focusHandler_ == null) {
            this.focusHandler_ = handler;
        }
        return this;
    }
    
    public void removeFocusHandler() {
        this.focusHandler_ = null;
    }
    
    @Override
    public void onTakeFocus(final CefBrowser browser, final boolean next) {
        if (browser == null) {
            return;
        }
        browser.setFocus(false);
        Container parent = browser.getUIComponent().getParent();
        if (parent != null) {
            FocusTraversalPolicy policy = null;
            while (parent != null) {
                policy = parent.getFocusTraversalPolicy();
                if (policy != null) {
                    break;
                }
                parent = parent.getParent();
            }
            if (policy != null) {
                final Component nextComp = next ? policy.getComponentAfter(parent, browser.getUIComponent()) : policy.getComponentBefore(parent, browser.getUIComponent());
                if (nextComp == null) {
                    policy.getDefaultComponent(parent).requestFocus();
                }
                else {
                    nextComp.requestFocus();
                }
            }
        }
        this.focusedBrowser_ = null;
        if (this.focusHandler_ != null) {
            this.focusHandler_.onTakeFocus(browser, next);
        }
    }
    
    @Override
    public boolean onSetFocus(final CefBrowser browser, final FocusSource source) {
        if (browser == null) {
            return false;
        }
        boolean alreadyHandled = false;
        if (this.focusHandler_ != null) {
            alreadyHandled = this.focusHandler_.onSetFocus(browser, source);
        }
        return alreadyHandled;
    }
    
    @Override
    public void onGotFocus(final CefBrowser browser) {
        if (browser == null) {
            return;
        }
        (this.focusedBrowser_ = browser).setFocus(true);
        if (this.focusHandler_ != null) {
            this.focusHandler_.onGotFocus(browser);
        }
    }
    
    public CefClient addJSDialogHandler(final CefJSDialogHandler handler) {
        if (this.jsDialogHandler_ == null) {
            this.jsDialogHandler_ = handler;
        }
        return this;
    }
    
    public void removeJSDialogHandler() {
        this.jsDialogHandler_ = null;
    }
    
    @Override
    public boolean onJSDialog(final CefBrowser browser, final String origin_url, final JSDialogType dialog_type, final String message_text, final String default_prompt_text, final CefJSDialogCallback callback, final BoolRef suppress_message) {
        return this.jsDialogHandler_ != null && browser != null && this.jsDialogHandler_.onJSDialog(browser, origin_url, dialog_type, message_text, default_prompt_text, callback, suppress_message);
    }
    
    @Override
    public boolean onBeforeUnloadDialog(final CefBrowser browser, final String message_text, final boolean is_reload, final CefJSDialogCallback callback) {
        return this.jsDialogHandler_ != null && browser != null && this.jsDialogHandler_.onBeforeUnloadDialog(browser, message_text, is_reload, callback);
    }
    
    @Override
    public void onResetDialogState(final CefBrowser browser) {
        if (this.jsDialogHandler_ != null && browser != null) {
            this.jsDialogHandler_.onResetDialogState(browser);
        }
    }
    
    @Override
    public void onDialogClosed(final CefBrowser browser) {
        if (this.jsDialogHandler_ != null && browser != null) {
            this.jsDialogHandler_.onDialogClosed(browser);
        }
    }
    
    public CefClient addKeyboardHandler(final CefKeyboardHandler handler) {
        if (this.keyboardHandler_ == null) {
            this.keyboardHandler_ = handler;
        }
        return this;
    }
    
    public void removeKeyboardHandler() {
        this.keyboardHandler_ = null;
    }
    
    @Override
    public boolean onPreKeyEvent(final CefBrowser browser, final CefKeyEvent event, final BoolRef is_keyboard_shortcut) {
        return this.keyboardHandler_ != null && browser != null && this.keyboardHandler_.onPreKeyEvent(browser, event, is_keyboard_shortcut);
    }
    
    @Override
    public boolean onKeyEvent(final CefBrowser browser, final CefKeyEvent event) {
        return this.keyboardHandler_ != null && browser != null && this.keyboardHandler_.onKeyEvent(browser, event);
    }
    
    public CefClient addLifeSpanHandler(final CefLifeSpanHandler handler) {
        if (this.lifeSpanHandler_ == null) {
            this.lifeSpanHandler_ = handler;
        }
        return this;
    }
    
    public void removeLifeSpanHandler() {
        this.lifeSpanHandler_ = null;
    }
    
    @Override
    public boolean onBeforePopup(final CefBrowser browser, final CefFrame frame, final String target_url, final String target_frame_name) {
        return this.isDisposed_ || (this.lifeSpanHandler_ != null && browser != null && this.lifeSpanHandler_.onBeforePopup(browser, frame, target_url, target_frame_name));
    }
    
    @Override
    public void onAfterCreated(final CefBrowser browser) {
        if (browser == null) {
            return;
        }
        final Integer identifier = browser.getIdentifier();
        synchronized (this.browser_) {
            this.browser_.put(identifier, browser);
            monitorexit(this.browser_);
        }
        if (this.lifeSpanHandler_ != null) {
            this.lifeSpanHandler_.onAfterCreated(browser);
        }
    }
    
    @Override
    public void onAfterParentChanged(final CefBrowser browser) {
        if (browser == null) {
            return;
        }
        if (this.lifeSpanHandler_ != null) {
            this.lifeSpanHandler_.onAfterParentChanged(browser);
        }
    }
    
    @Override
    public boolean doClose(final CefBrowser browser) {
        if (browser == null) {
            return false;
        }
        if (this.lifeSpanHandler_ != null) {
            return this.lifeSpanHandler_.doClose(browser);
        }
        return browser.doClose();
    }
    
    @Override
    public void onBeforeClose(final CefBrowser browser) {
        if (browser == null) {
            return;
        }
        if (this.lifeSpanHandler_ != null) {
            this.lifeSpanHandler_.onBeforeClose(browser);
        }
        browser.onBeforeClose();
        this.cleanupBrowser(browser.getIdentifier());
    }
    
    private void cleanupBrowser(final int identifier) {
        synchronized (this.browser_) {
            if (identifier >= 0) {
                this.browser_.remove(identifier);
            }
            else if (!this.browser_.isEmpty()) {
                final Collection<CefBrowser> browserList = this.browser_.values();
                for (final CefBrowser browser : browserList) {
                    browser.close(true);
                }
                monitorexit(this.browser_);
                return;
            }
            if (this.browser_.isEmpty() && this.isDisposed_) {
                KeyboardFocusManager.getCurrentKeyboardFocusManager().removePropertyChangeListener(this.propertyChangeListener);
                this.removeContextMenuHandler(this);
                this.removeDialogHandler(this);
                this.removeDisplayHandler(this);
                this.removeDownloadHandler(this);
                this.removeDragHandler(this);
                this.removeFocusHandler(this);
                this.removeJSDialogHandler(this);
                this.removeKeyboardHandler(this);
                this.removeLifeSpanHandler(this);
                this.removeLoadHandler(this);
                this.removeRenderHandler(this);
                this.removeRequestHandler(this);
                this.removeWindowHandler(this);
                super.dispose();
                CefApp.getInstance().clientWasDisposed(this);
            }
            monitorexit(this.browser_);
        }
    }
    
    public CefClient addLoadHandler(final CefLoadHandler handler) {
        if (this.loadHandler_ == null) {
            this.loadHandler_ = handler;
        }
        return this;
    }
    
    public void removeLoadHandler() {
        this.loadHandler_ = null;
    }
    
    @Override
    public void onLoadingStateChange(final CefBrowser browser, final boolean isLoading, final boolean canGoBack, final boolean canGoForward) {
        if (this.loadHandler_ != null && browser != null) {
            this.loadHandler_.onLoadingStateChange(browser, isLoading, canGoBack, canGoForward);
        }
    }
    
    @Override
    public void onLoadStart(final CefBrowser browser, final CefFrame frame, final CefRequest.TransitionType transitionType) {
        if (this.loadHandler_ != null && browser != null) {
            this.loadHandler_.onLoadStart(browser, frame, transitionType);
        }
    }
    
    @Override
    public void onLoadEnd(final CefBrowser browser, final CefFrame frame, final int httpStatusCode) {
        if (this.loadHandler_ != null && browser != null) {
            this.loadHandler_.onLoadEnd(browser, frame, httpStatusCode);
        }
    }
    
    @Override
    public void onLoadError(final CefBrowser browser, final CefFrame frame, final ErrorCode errorCode, final String errorText, final String failedUrl) {
        if (this.loadHandler_ != null && browser != null) {
            this.loadHandler_.onLoadError(browser, frame, errorCode, errorText, failedUrl);
        }
    }
    
    public synchronized void addMessageRouter(final CefMessageRouter messageRouter) {
        super.addMessageRouter(messageRouter);
    }
    
    public synchronized void removeMessageRouter(final CefMessageRouter messageRouter) {
        super.removeMessageRouter(messageRouter);
    }
    
    @Override
    public Rectangle getViewRect(final CefBrowser browser) {
        if (browser == null) {
            return new Rectangle(0, 0, 0, 0);
        }
        final CefRenderHandler realHandler = browser.getRenderHandler();
        if (realHandler != null) {
            return realHandler.getViewRect(browser);
        }
        return new Rectangle(0, 0, 0, 0);
    }
    
    @Override
    public Point getScreenPoint(final CefBrowser browser, final Point viewPoint) {
        if (browser == null) {
            return new Point(0, 0);
        }
        final CefRenderHandler realHandler = browser.getRenderHandler();
        if (realHandler != null) {
            return realHandler.getScreenPoint(browser, viewPoint);
        }
        return new Point(0, 0);
    }
    
    @Override
    public void onPopupShow(final CefBrowser browser, final boolean show) {
        if (browser == null) {
            return;
        }
        final CefRenderHandler realHandler = browser.getRenderHandler();
        if (realHandler != null) {
            realHandler.onPopupShow(browser, show);
        }
    }
    
    @Override
    public void onPopupSize(final CefBrowser browser, final Rectangle size) {
        if (browser == null) {
            return;
        }
        final CefRenderHandler realHandler = browser.getRenderHandler();
        if (realHandler != null) {
            realHandler.onPopupSize(browser, size);
        }
    }
    
    @Override
    public void onPaint(final CefBrowser browser, final boolean popup, final Rectangle[] dirtyRects, final ByteBuffer buffer, final int width, final int height) {
        if (browser == null) {
            return;
        }
        final CefRenderHandler realHandler = browser.getRenderHandler();
        if (realHandler != null) {
            realHandler.onPaint(browser, popup, dirtyRects, buffer, width, height);
        }
    }
    
    @Override
    public void onCursorChange(final CefBrowser browser, final int cursorType) {
        if (browser == null) {
            return;
        }
        final CefRenderHandler realHandler = browser.getRenderHandler();
        if (realHandler != null) {
            realHandler.onCursorChange(browser, cursorType);
        }
    }
    
    @Override
    public boolean startDragging(final CefBrowser browser, final CefDragData dragData, final int mask, final int x, final int y) {
        if (browser == null) {
            return false;
        }
        final CefRenderHandler realHandler = browser.getRenderHandler();
        return realHandler != null && realHandler.startDragging(browser, dragData, mask, x, y);
    }
    
    @Override
    public void updateDragCursor(final CefBrowser browser, final int operation) {
        if (browser == null) {
            return;
        }
        final CefRenderHandler realHandler = browser.getRenderHandler();
        if (realHandler != null) {
            realHandler.updateDragCursor(browser, operation);
        }
    }
    
    public CefClient addRequestHandler(final CefRequestHandler handler) {
        if (this.requestHandler_ == null) {
            this.requestHandler_ = handler;
        }
        return this;
    }
    
    public void removeRequestHandler() {
        this.requestHandler_ = null;
    }
    
    @Override
    public boolean onBeforeBrowse(final CefBrowser browser, final CefFrame frame, final CefRequest request, final boolean user_gesture, final boolean is_redirect) {
        return this.requestHandler_ != null && browser != null && this.requestHandler_.onBeforeBrowse(browser, frame, request, user_gesture, is_redirect);
    }
    
    @Override
    public CefResourceRequestHandler getResourceRequestHandler(final CefBrowser browser, final CefFrame frame, final CefRequest request, final boolean isNavigation, final boolean isDownload, final String requestInitiator, final BoolRef disableDefaultHandling) {
        if (this.requestHandler_ != null && browser != null) {
            return this.requestHandler_.getResourceRequestHandler(browser, frame, request, isNavigation, isDownload, requestInitiator, disableDefaultHandling);
        }
        return null;
    }
    
    @Override
    public boolean getAuthCredentials(final CefBrowser browser, final CefFrame frame, final boolean isProxy, final String host, final int port, final String realm, final String scheme, final CefAuthCallback callback) {
        return this.requestHandler_ != null && browser != null && this.requestHandler_.getAuthCredentials(browser, frame, isProxy, host, port, realm, scheme, callback);
    }
    
    @Override
    public boolean onQuotaRequest(final CefBrowser browser, final String origin_url, final long new_size, final CefRequestCallback callback) {
        return this.requestHandler_ != null && browser != null && this.requestHandler_.onQuotaRequest(browser, origin_url, new_size, callback);
    }
    
    @Override
    public boolean onCertificateError(final CefBrowser browser, final ErrorCode cert_error, final String request_url, final CefRequestCallback callback) {
        return this.requestHandler_ != null && this.requestHandler_.onCertificateError(browser, cert_error, request_url, callback);
    }
    
    @Override
    public void onPluginCrashed(final CefBrowser browser, final String pluginPath) {
        if (this.requestHandler_ != null) {
            this.requestHandler_.onPluginCrashed(browser, pluginPath);
        }
    }
    
    @Override
    public void onRenderProcessTerminated(final CefBrowser browser, final TerminationStatus status) {
        if (this.requestHandler_ != null) {
            this.requestHandler_.onRenderProcessTerminated(browser, status);
        }
    }
    
    @Override
    public Rectangle getRect(final CefBrowser browser) {
        if (browser == null) {
            return new Rectangle(0, 0, 0, 0);
        }
        final CefWindowHandler realHandler = browser.getWindowHandler();
        if (realHandler != null) {
            return realHandler.getRect(browser);
        }
        return new Rectangle(0, 0, 0, 0);
    }
    
    @Override
    public void onMouseEvent(final CefBrowser browser, final int event, final int screenX, final int screenY, final int modifier, final int button) {
        if (browser == null) {
            return;
        }
        final CefWindowHandler realHandler = browser.getWindowHandler();
        if (realHandler != null) {
            realHandler.onMouseEvent(browser, event, screenX, screenY, modifier, button);
        }
    }
    
    static /* synthetic */ void access$2(final CefClient cefClient, final CefBrowser focusedBrowser_) {
        cefClient.focusedBrowser_ = focusedBrowser_;
    }
}
