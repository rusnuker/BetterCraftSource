// 
// Decompiled by Procyon v0.6.0
// 

package net.montoyo.mcef.client;

import net.montoyo.mcef.api.IBrowser;
import org.cef.browser.CefBrowserOsr;
import java.util.Iterator;
import org.cef.CefSettings;
import org.cef.browser.CefFrame;
import org.cef.browser.CefBrowser;
import net.montoyo.mcef.api.IDisplayHandler;
import java.util.ArrayList;
import org.cef.handler.CefDisplayHandler;

public class DisplayHandler implements CefDisplayHandler
{
    private final ArrayList<IDisplayHandler> list;
    private final ArrayList<EventData> queue;
    
    public DisplayHandler() {
        this.list = new ArrayList<IDisplayHandler>();
        this.queue = new ArrayList<EventData>();
    }
    
    @Override
    public void onAddressChange(final CefBrowser browser, final CefFrame frame, final String url) {
        synchronized (this.queue) {
            this.queue.add(new EventData(browser, url, EventType.ADDRESS_CHANGE, null));
            monitorexit(this.queue);
        }
    }
    
    @Override
    public void onTitleChange(final CefBrowser browser, final String title) {
        synchronized (this.queue) {
            this.queue.add(new EventData(browser, title, EventType.TITLE_CHANGE, null));
            monitorexit(this.queue);
        }
    }
    
    @Override
    public boolean onTooltip(final CefBrowser browser, final String text) {
        synchronized (this.queue) {
            this.queue.add(new EventData(browser, text, EventType.TOOLTIP, null));
            monitorexit(this.queue);
        }
        return false;
    }
    
    @Override
    public void onStatusMessage(final CefBrowser browser, final String value) {
        synchronized (this.queue) {
            this.queue.add(new EventData(browser, value, EventType.STATUS_MESSAGE, null));
            monitorexit(this.queue);
        }
    }
    
    @Override
    public boolean onConsoleMessage(final CefBrowser browser, final CefSettings.LogSeverity level, final String message, final String source, final int line) {
        return false;
    }
    
    public void addHandler(final IDisplayHandler h) {
        this.list.add(h);
    }
    
    public void update() {
        synchronized (this.queue) {
            while (!this.queue.isEmpty()) {
                final EventData ed = this.queue.remove(0);
                for (final IDisplayHandler idh : this.list) {
                    ed.execute(idh);
                }
            }
            monitorexit(this.queue);
        }
    }
    
    private enum EventType
    {
        ADDRESS_CHANGE("ADDRESS_CHANGE", 0), 
        TITLE_CHANGE("TITLE_CHANGE", 1), 
        TOOLTIP("TOOLTIP", 2), 
        STATUS_MESSAGE("STATUS_MESSAGE", 3);
        
        private EventType(final String s, final int n) {
        }
    }
    
    private static final class EventData
    {
        private final CefBrowser browser;
        private final String data;
        private final EventType type;
        
        private EventData(final CefBrowser b, final String d, final EventType t) {
            this.browser = b;
            this.data = d;
            this.type = t;
        }
        
        private void execute(final IDisplayHandler idh) {
            switch (this.type) {
                case ADDRESS_CHANGE: {
                    idh.onAddressChange((IBrowser)this.browser, this.data);
                    break;
                }
                case TITLE_CHANGE: {
                    idh.onTitleChange((IBrowser)this.browser, this.data);
                    break;
                }
                case TOOLTIP: {
                    idh.onTooltip((IBrowser)this.browser, this.data);
                    break;
                }
                case STATUS_MESSAGE: {
                    idh.onStatusMessage((IBrowser)this.browser, this.data);
                    break;
                }
            }
        }
    }
}
