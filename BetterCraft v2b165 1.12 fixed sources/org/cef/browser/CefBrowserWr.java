// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.browser;

import org.cef.handler.CefClientHandler;
import java.awt.Container;
import java.awt.Window;
import org.cef.handler.CefWindowHandler;
import java.awt.event.HierarchyListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyBoundsListener;
import javax.swing.MenuSelectionManager;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.LayoutManager;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.ToolTipManager;
import javax.swing.JPopupMenu;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.AWTEvent;
import java.awt.event.MouseWheelEvent;
import java.util.Date;
import java.awt.MouseInfo;
import javax.swing.SwingUtilities;
import org.cef.OS;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Point;
import org.cef.CefClient;
import org.cef.handler.CefWindowHandlerAdapter;
import javax.swing.Timer;
import java.awt.Rectangle;
import java.awt.Component;
import java.awt.Canvas;

class CefBrowserWr extends CefBrowser_N
{
    private Canvas canvas_;
    private Component component_;
    private Rectangle content_rect_;
    private long window_handle_;
    private boolean justCreated_;
    private Timer delayedUpdate_;
    private CefWindowHandlerAdapter win_handler_;
    
    CefBrowserWr(final CefClient client, final String url, final CefRequestContext context) {
        this(client, url, context, null, null);
    }
    
    private CefBrowserWr(final CefClient client, final String url, final CefRequestContext context, final CefBrowserWr parent, final Point inspectAt) {
        super(client, url, context, parent, inspectAt);
        this.canvas_ = null;
        this.component_ = null;
        this.content_rect_ = new Rectangle(0, 0, 0, 0);
        this.window_handle_ = 0L;
        this.justCreated_ = false;
        this.delayedUpdate_ = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        if (CefBrowserWr.this.isClosed()) {
                            return;
                        }
                        final boolean hasCreatedUI = CefBrowserWr.this.createBrowserIfRequired(true);
                        if (hasCreatedUI) {
                            CefBrowserWr.this.delayedUpdate_.restart();
                        }
                        else if (OS.isMacintosh() || OS.isLinux()) {
                            CefBrowserWr.this.doUpdate();
                        }
                    }
                });
            }
        });
        this.win_handler_ = new CefWindowHandlerAdapter() {
            private Point lastPos = new Point(-1, -1);
            private long[] nextClick = new long[MouseInfo.getNumberOfButtons()];
            private int[] clickCnt = new int[MouseInfo.getNumberOfButtons()];
            
            @Override
            public Rectangle getRect(final CefBrowser browser) {
                synchronized (CefBrowserWr.this.content_rect_) {
                    final Rectangle access$3 = CefBrowserWr.this.content_rect_;
                    monitorexit(CefBrowserWr.this.content_rect_);
                    return access$3;
                }
            }
            
            @Override
            public void onMouseEvent(final CefBrowser browser, int event, final int screenX, final int screenY, final int modifier, final int button) {
                final Point pt = new Point(screenX, screenY);
                if (event == 503) {
                    if (pt.equals(this.lastPos)) {
                        return;
                    }
                    this.lastPos = pt;
                    if ((modifier & 0x400) != 0x0) {
                        event = 506;
                    }
                }
                final int finalEvent = event;
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        final Component parent = SwingUtilities.getRoot(CefBrowserWr.this.component_);
                        if (parent == null) {
                            return;
                        }
                        SwingUtilities.convertPointFromScreen(pt, parent);
                        int clickCnt = 0;
                        final long now = new Date().getTime();
                        if (finalEvent == 507) {
                            final int scrollType = 0;
                            final int rotation = (button > 0) ? 1 : -1;
                            CefBrowserWr.this.component_.dispatchEvent(new MouseWheelEvent(parent, finalEvent, now, modifier, pt.x, pt.y, 0, false, scrollType, 3, rotation));
                        }
                        else {
                            clickCnt = CefWindowHandlerAdapter.this.getClickCount(finalEvent, button);
                            CefBrowserWr.this.component_.dispatchEvent(new MouseEvent(parent, finalEvent, now, modifier, pt.x, pt.y, screenX, screenY, clickCnt, false, button));
                        }
                        if (finalEvent == 502) {
                            CefBrowserWr.this.component_.dispatchEvent(new MouseEvent(parent, 500, now, modifier, pt.x, pt.y, screenX, screenY, clickCnt, false, button));
                        }
                    }
                });
            }
            
            public int getClickCount(final int event, final int button) {
                final int idx = button % this.nextClick.length;
                switch (event) {
                    case 501: {
                        final long currTime = new Date().getTime();
                        if (currTime > this.nextClick[idx]) {
                            this.nextClick[idx] = currTime + (int)Toolkit.getDefaultToolkit().getDesktopProperty("awt.multiClickInterval");
                            this.clickCnt[idx] = 1;
                            return this.clickCnt[idx];
                        }
                        final int[] clickCnt = this.clickCnt;
                        final int n = idx;
                        ++clickCnt[n];
                        return this.clickCnt[idx];
                    }
                    case 502: {
                        return this.clickCnt[idx];
                    }
                    default: {
                        return 0;
                    }
                }
            }
        };
        this.delayedUpdate_.setRepeats(false);
        JPopupMenu.setDefaultLightWeightPopupEnabled(false);
        ToolTipManager.sharedInstance().setLightWeightPopupEnabled(false);
        this.component_ = new JPanel(new BorderLayout()) {
            private boolean removed_ = true;
            
            @Override
            public void setBounds(final int x, final int y, final int width, final int height) {
                super.setBounds(x, y, width, height);
                CefBrowserWr.this.wasResized(width, height);
            }
            
            @Override
            public void setBounds(final Rectangle r) {
                this.setBounds(r.x, r.y, r.width, r.height);
            }
            
            @Override
            public void setSize(final int width, final int height) {
                super.setSize(width, height);
                CefBrowserWr.this.wasResized(width, height);
            }
            
            @Override
            public void setSize(final Dimension d) {
                this.setSize(d.width, d.height);
            }
            
            @Override
            public void paint(final Graphics g) {
                CefBrowserWr.this.doUpdate();
                CefBrowserWr.this.delayedUpdate_.restart();
            }
            
            @Override
            public void addNotify() {
                super.addNotify();
                if (this.removed_) {
                    CefBrowserWr.this.setParent(getWindowHandle(this), CefBrowserWr.this.canvas_);
                    this.removed_ = false;
                }
            }
            
            @Override
            public void removeNotify() {
                if (!this.removed_) {
                    if (!CefBrowserWr.this.isClosed()) {
                        CefBrowserWr.this.setParent(0L, null);
                    }
                    this.removed_ = true;
                }
                super.removeNotify();
            }
        };
        if (OS.isWindows() || OS.isLinux()) {
            this.canvas_ = new Canvas();
            ((JPanel)this.component_).add(this.canvas_, "Center");
        }
        this.component_.setMinimumSize(new Dimension(0, 0));
        this.component_.setFocusable(true);
        this.component_.addFocusListener(new FocusListener() {
            @Override
            public void focusLost(final FocusEvent e) {
                CefBrowserWr.this.setFocus(false);
            }
            
            @Override
            public void focusGained(final FocusEvent e) {
                MenuSelectionManager.defaultManager().clearSelectedPath();
                CefBrowserWr.this.setFocus(true);
            }
        });
        this.component_.addHierarchyBoundsListener(new HierarchyBoundsListener() {
            @Override
            public void ancestorResized(final HierarchyEvent e) {
                CefBrowserWr.this.doUpdate();
            }
            
            @Override
            public void ancestorMoved(final HierarchyEvent e) {
                CefBrowserWr.this.doUpdate();
                CefBrowserWr.this.notifyMoveOrResizeStarted();
            }
        });
        this.component_.addHierarchyListener(new HierarchyListener() {
            @Override
            public void hierarchyChanged(final HierarchyEvent e) {
                if ((e.getChangeFlags() & 0x4L) != 0x0L) {
                    CefBrowserWr.this.setWindowVisibility(e.getChanged().isVisible());
                }
            }
        });
    }
    
    @Override
    public void createImmediately() {
        this.justCreated_ = true;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                CefBrowserWr.this.createBrowserIfRequired(false);
            }
        });
    }
    
    @Override
    public Component getUIComponent() {
        return this.component_;
    }
    
    @Override
    public CefWindowHandler getWindowHandler() {
        return this.win_handler_;
    }
    
    @Override
    protected CefBrowser_N createDevToolsBrowser(final CefClient client, final String url, final CefRequestContext context, final CefBrowser_N parent, final Point inspectAt) {
        return new CefBrowserWr(client, url, context, this, inspectAt);
    }
    
    private synchronized long getWindowHandle() {
        if (this.window_handle_ == 0L && OS.isMacintosh()) {
            this.window_handle_ = getWindowHandle(this.component_);
        }
        return this.window_handle_;
    }
    
    private static long getWindowHandle(final Component component) {
        if (OS.isMacintosh()) {
            try {
                final Class<?> cls = Class.forName("org.cef.browser.mac.CefBrowserWindowMac");
                final CefBrowserWindow browserWindow = (CefBrowserWindow)cls.newInstance();
                if (browserWindow != null) {
                    return browserWindow.getWindowHandle(component);
                }
            }
            catch (final ClassNotFoundException e) {
                e.printStackTrace();
            }
            catch (final InstantiationException e2) {
                e2.printStackTrace();
            }
            catch (final IllegalAccessException e3) {
                e3.printStackTrace();
            }
        }
        return 0L;
    }
    
    private void doUpdate() {
        if (this.isClosed()) {
            return;
        }
        final Rectangle clipping = ((JPanel)this.component_).getVisibleRect();
        if (OS.isMacintosh()) {
            Container parent = this.component_.getParent();
            final Point contentPos = this.component_.getLocation();
            while (parent != null) {
                final Container next = parent.getParent();
                if (next != null && next instanceof Window) {
                    break;
                }
                final Point parentPos = parent.getLocation();
                contentPos.translate(parentPos.x, parentPos.y);
                parent = next;
            }
            contentPos.translate(clipping.x, clipping.y);
            final Point location;
            final Point browserPos = location = clipping.getLocation();
            location.x *= -1;
            final Point point = browserPos;
            point.y *= -1;
            synchronized (this.content_rect_) {
                this.content_rect_ = new Rectangle(contentPos, clipping.getSize());
                final Rectangle browserRect = new Rectangle(browserPos, this.component_.getSize());
                this.updateUI(this.content_rect_, browserRect);
                monitorexit(this.content_rect_);
                return;
            }
        }
        synchronized (this.content_rect_) {
            this.updateUI(clipping, this.content_rect_ = this.component_.getBounds());
            monitorexit(this.content_rect_);
        }
    }
    
    private boolean createBrowserIfRequired(final boolean hasParent) {
        if (this.isClosed()) {
            return false;
        }
        long windowHandle = 0L;
        Component canvas = null;
        if (hasParent) {
            windowHandle = this.getWindowHandle();
            canvas = ((OS.isWindows() || OS.isLinux()) ? this.canvas_ : this.component_);
        }
        if (this.getNativeRef("CefBrowser") != 0L) {
            if (hasParent && this.justCreated_) {
                this.setParent(windowHandle, canvas);
                this.setFocus(true);
                this.justCreated_ = false;
            }
            return false;
        }
        if (this.getParentBrowser() != null) {
            this.createDevTools(this.getParentBrowser(), this.getClient(), windowHandle, false, false, canvas, this.getInspectAt());
            return true;
        }
        this.createBrowser(this.getClient(), windowHandle, this.getUrl(), false, false, canvas, this.getRequestContext());
        return true;
    }
}
