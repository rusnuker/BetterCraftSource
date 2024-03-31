// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.browser;

import org.cef.callback.CefPdfPrintCallback;
import org.cef.misc.CefPdfPrintSettings;
import org.cef.network.CefRequest;
import org.cef.callback.CefRunFileDialogCallback;
import java.util.Vector;
import org.cef.handler.CefDialogHandler;
import org.cef.handler.CefWindowHandler;
import org.cef.callback.CefStringVisitor;
import net.montoyo.mcef.client.StringVisitor;
import net.montoyo.mcef.api.IStringVisitor;
import java.awt.event.MouseWheelEvent;
import java.awt.event.KeyEvent;
import net.montoyo.mcef.MCEF;
import org.cef.handler.CefClientHandler;
import org.cef.callback.CefDragData;
import org.lwjgl.BufferUtils;
import net.montoyo.mcef.utilities.Log;
import java.nio.ByteBuffer;
import java.awt.Component;
import org.cef.CefClient;
import java.util.HashMap;
import java.awt.event.MouseEvent;
import org.cef.DummyComponent;
import java.awt.Point;
import java.awt.Rectangle;
import net.montoyo.mcef.api.IBrowser;
import org.cef.handler.CefRenderHandler;

public class CefBrowserOsr extends CefBrowser_N implements CefRenderHandler, IBrowser
{
    private CefRenderer renderer_;
    private Rectangle browser_rect_;
    private Point screenPoint_;
    private boolean isTransparent_;
    private final DummyComponent dc_;
    private MouseEvent lastMouseEvent;
    public static boolean CLEANUP;
    private final PaintData paintData;
    private static final HashMap<Integer, Character> WORST_HACK;
    
    static {
        CefBrowserOsr.CLEANUP = true;
        WORST_HACK = new HashMap<Integer, Character>();
    }
    
    CefBrowserOsr(final CefClient client, final String url, final boolean transparent, final CefRequestContext context) {
        this(client, url, transparent, context, null, null);
    }
    
    private CefBrowserOsr(final CefClient client, final String url, final boolean transparent, final CefRequestContext context, final CefBrowserOsr parent, final Point inspectAt) {
        super(client, url, context, parent, inspectAt);
        this.browser_rect_ = new Rectangle(0, 0, 1, 1);
        this.screenPoint_ = new Point(0, 0);
        this.dc_ = new DummyComponent();
        this.lastMouseEvent = new MouseEvent(this.dc_, 503, 0L, 0, 0, 0, 0, false);
        this.paintData = new PaintData(null);
        this.isTransparent_ = transparent;
        this.renderer_ = new CefRenderer(transparent);
    }
    
    @Override
    public void createImmediately() {
        this.createBrowserIfRequired(false);
    }
    
    @Override
    public Component getUIComponent() {
        return this.dc_;
    }
    
    @Override
    public CefRenderHandler getRenderHandler() {
        return this;
    }
    
    @Override
    protected CefBrowser_N createDevToolsBrowser(final CefClient client, final String url, final CefRequestContext context, final CefBrowser_N parent, final Point inspectAt) {
        return new CefBrowserOsr(client, url, this.isTransparent_, context, this, inspectAt);
    }
    
    @Override
    public Rectangle getViewRect(final CefBrowser browser) {
        return this.browser_rect_;
    }
    
    @Override
    public Point getScreenPoint(final CefBrowser browser, final Point viewPoint) {
        final Point screenPoint = new Point(this.screenPoint_);
        screenPoint.translate(viewPoint.x, viewPoint.y);
        return screenPoint;
    }
    
    @Override
    public void onPopupShow(final CefBrowser browser, final boolean show) {
        if (!show) {
            this.renderer_.clearPopupRects();
            this.invalidate();
        }
    }
    
    @Override
    public void onPopupSize(final CefBrowser browser, final Rectangle size) {
        this.renderer_.onPopupSize(size);
    }
    
    @Override
    public void onPaint(final CefBrowser browser, final boolean popup, final Rectangle[] dirtyRects, final ByteBuffer buffer, final int width, final int height) {
        if (popup) {
            return;
        }
        final int size = width * height << 2;
        synchronized (this.paintData) {
            if (buffer.limit() > size) {
                Log.warning("Skipping MCEF browser frame, data is too heavy", new Object[0]);
            }
            else {
                if (this.paintData.hasFrame) {
                    PaintData.access$2(this.paintData, true);
                }
                if (this.paintData.buffer == null || size != this.paintData.buffer.capacity()) {
                    PaintData.access$4(this.paintData, BufferUtils.createByteBuffer(size));
                }
                this.paintData.buffer.position(0);
                this.paintData.buffer.limit(buffer.limit());
                buffer.position(0);
                this.paintData.buffer.put(buffer);
                this.paintData.buffer.position(0);
                PaintData.access$5(this.paintData, width);
                PaintData.access$6(this.paintData, height);
                PaintData.access$7(this.paintData, dirtyRects);
                PaintData.access$8(this.paintData, true);
            }
            monitorexit(this.paintData);
        }
    }
    
    public void mcefUpdate() {
        synchronized (this.paintData) {
            if (this.paintData.hasFrame) {
                this.renderer_.onPaint(false, this.paintData.dirtyRects, this.paintData.buffer, this.paintData.width, this.paintData.height, this.paintData.fullReRender);
                PaintData.access$8(this.paintData, false);
                PaintData.access$2(this.paintData, false);
            }
            monitorexit(this.paintData);
        }
        this.sendMouseEvent(this.lastMouseEvent);
    }
    
    @Override
    public void onCursorChange(final CefBrowser browser, final int cursorType) {
    }
    
    @Override
    public boolean startDragging(final CefBrowser browser, final CefDragData dragData, final int mask, final int x, final int y) {
        return false;
    }
    
    @Override
    public void updateDragCursor(final CefBrowser browser, final int operation) {
    }
    
    private void createBrowserIfRequired(final boolean hasParent) {
        if (this.getNativeRef("CefBrowser") == 0L) {
            if (this.getParentBrowser() != null) {
                this.createDevTools(this.getParentBrowser(), this.getClient(), 0L, true, this.isTransparent_, null, this.getInspectAt());
            }
            else {
                this.createBrowser(this.getClient(), 0L, this.getUrl(), true, this.isTransparent_, null, this.getRequestContext());
            }
        }
        else {
            this.setFocus(true);
        }
    }
    
    @Override
    public void close() {
        if (CefBrowserOsr.CLEANUP) {
            MCEF.PROXY_CLIENT.removeBrowser(this);
            this.renderer_.cleanup();
        }
        super.close(true);
    }
    
    @Override
    public void resize(final int width, final int height) {
        this.browser_rect_.setBounds(0, 0, width, height);
        this.dc_.setBounds(this.browser_rect_);
        this.dc_.setVisible(true);
        this.wasResized(width, height);
    }
    
    @Override
    public void draw(final double x1, final double y1, final double x2, final double y2) {
        this.renderer_.render(x1, y1, x2, y2);
    }
    
    @Override
    public int getTextureID() {
        return this.renderer_.texture_id_[0];
    }
    
    @Override
    public void injectMouseMove(final int x, final int y, final int mods, final boolean left) {
        final MouseEvent ev = new MouseEvent(this.dc_, 503, 0L, mods, x, y, 0, false);
        this.sendMouseEvent(this.lastMouseEvent = ev);
    }
    
    @Override
    public void injectMouseButton(final int x, final int y, final int mods, final int btn, final boolean pressed, final int ccnt) {
        final MouseEvent ev = new MouseEvent(this.dc_, pressed ? 501 : 502, 0L, mods, x, y, ccnt, false, btn);
        this.sendMouseEvent(ev);
    }
    
    @Override
    public void injectKeyTyped(final char c, final int mods) {
        final KeyEvent ev = new KeyEvent(this.dc_, 400, 0L, mods, 0, c);
        this.sendKeyEvent(ev);
    }
    
    public static int remapKeycode(final int kc, final char c) {
        switch (kc) {
            case 14: {
                return 8;
            }
            case 211: {
                return 46;
            }
            case 208: {
                return 40;
            }
            case 28: {
                return 13;
            }
            case 1: {
                return 27;
            }
            case 203: {
                return 37;
            }
            case 205: {
                return 39;
            }
            case 15: {
                return 9;
            }
            case 200: {
                return 38;
            }
            case 201: {
                return 33;
            }
            case 209: {
                return 34;
            }
            case 207: {
                return 35;
            }
            case 199: {
                return 36;
            }
            default: {
                return c;
            }
        }
    }
    
    @Override
    public void injectKeyPressedByKeyCode(final int keyCode, final char c, final int mods) {
        if (c != '\0') {
            synchronized (CefBrowserOsr.WORST_HACK) {
                CefBrowserOsr.WORST_HACK.put(keyCode, c);
                monitorexit(CefBrowserOsr.WORST_HACK);
            }
        }
        final KeyEvent ev = new KeyEvent(this.dc_, 401, 0L, mods, remapKeycode(keyCode, c), c);
        this.sendKeyEvent(ev);
    }
    
    @Override
    public void injectKeyReleasedByKeyCode(final int keyCode, char c, final int mods) {
        if (c == '\0') {
            synchronized (CefBrowserOsr.WORST_HACK) {
                c = CefBrowserOsr.WORST_HACK.getOrDefault(keyCode, '\0');
                monitorexit(CefBrowserOsr.WORST_HACK);
            }
        }
        final KeyEvent ev = new KeyEvent(this.dc_, 402, 0L, mods, remapKeycode(keyCode, c), c);
        this.sendKeyEvent(ev);
    }
    
    @Override
    public void injectMouseWheel(final int x, final int y, final int mods, final int amount, final int rot) {
        final MouseWheelEvent ev = new MouseWheelEvent(this.dc_, 507, 0L, mods, x, y, 0, false, 0, amount, rot);
        this.sendMouseWheelEvent(ev);
    }
    
    @Override
    public void runJS(final String script, final String frame) {
        this.executeJavaScript(script, frame, 0);
    }
    
    @Override
    public void visitSource(final IStringVisitor isv) {
        this.getSource(new StringVisitor(isv));
    }
    
    @Override
    public boolean isPageLoading() {
        return this.isLoading();
    }
    
    private static class PaintData
    {
        private ByteBuffer buffer;
        private int width;
        private int height;
        private Rectangle[] dirtyRects;
        private boolean hasFrame;
        private boolean fullReRender;
        
        static /* synthetic */ void access$2(final PaintData paintData, final boolean fullReRender) {
            paintData.fullReRender = fullReRender;
        }
        
        static /* synthetic */ void access$4(final PaintData paintData, final ByteBuffer buffer) {
            paintData.buffer = buffer;
        }
        
        static /* synthetic */ void access$5(final PaintData paintData, final int width) {
            paintData.width = width;
        }
        
        static /* synthetic */ void access$6(final PaintData paintData, final int height) {
            paintData.height = height;
        }
        
        static /* synthetic */ void access$7(final PaintData paintData, final Rectangle[] dirtyRects) {
            paintData.dirtyRects = dirtyRects;
        }
        
        static /* synthetic */ void access$8(final PaintData paintData, final boolean hasFrame) {
            paintData.hasFrame = hasFrame;
        }
    }
}
