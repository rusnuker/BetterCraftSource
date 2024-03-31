// 
// Decompiled by Procyon v0.6.0
// 

package net.montoyo.mcef.client;

import org.cef.handler.CefResourceHandler;
import org.cef.network.CefRequest;
import org.cef.browser.CefFrame;
import org.cef.browser.CefBrowser;
import org.cef.CefApp;
import org.cef.callback.CefSchemeHandlerFactory;
import net.montoyo.mcef.MCEF;
import java.util.Iterator;
import net.montoyo.mcef.utilities.Log;
import java.util.Map;
import org.cef.callback.CefSchemeRegistrar;
import net.montoyo.mcef.api.IScheme;
import java.util.HashMap;
import org.cef.handler.CefAppHandlerAdapter;

public class AppHandler extends CefAppHandlerAdapter
{
    private final HashMap<String, SchemeData> schemeMap;
    
    public AppHandler() {
        super(new String[0]);
        this.schemeMap = new HashMap<String, SchemeData>();
    }
    
    public void registerScheme(final String name, final Class<? extends IScheme> cls, final boolean std, final boolean local, final boolean dispIsolated, final boolean secure, final boolean corsEnabled, final boolean cspBypassing, final boolean fetchEnabled) {
        this.schemeMap.put(name, new SchemeData(cls, std, local, dispIsolated, secure, corsEnabled, cspBypassing, fetchEnabled, null));
    }
    
    public boolean isSchemeRegistered(final String name) {
        return this.schemeMap.containsKey(name);
    }
    
    @Override
    public void onRegisterCustomSchemes(final CefSchemeRegistrar reg) {
        int cnt = 0;
        for (final Map.Entry<String, SchemeData> entry : this.schemeMap.entrySet()) {
            final SchemeData v = entry.getValue();
            if (reg.addCustomScheme(entry.getKey(), v.std, v.local, v.dispIsolated, v.secure, v.corsEnabled, v.cspBypassing, v.fetchEnabled)) {
                ++cnt;
            }
            else {
                Log.error("Could not register scheme %s", entry.getKey());
            }
        }
        Log.info("%d schemes registered", cnt);
    }
    
    @Override
    public void onContextInitialized() {
        final CefApp app = MCEF.PROXY_CLIENT.getCefApp();
        for (final Map.Entry<String, SchemeData> entry : this.schemeMap.entrySet()) {
            app.registerSchemeHandlerFactory(entry.getKey(), "", new SchemeHandlerFactory(entry.getValue().cls, null));
        }
    }
    
    private static class SchemeData
    {
        private Class<? extends IScheme> cls;
        private boolean std;
        private boolean local;
        private boolean dispIsolated;
        private boolean secure;
        private boolean corsEnabled;
        private boolean cspBypassing;
        private boolean fetchEnabled;
        
        private SchemeData(final Class<? extends IScheme> cls, final boolean std, final boolean local, final boolean dispIsolated, final boolean secure, final boolean corsEnabled, final boolean cspBypassing, final boolean fetchEnabled) {
            this.cls = cls;
            this.std = std;
            this.local = local;
            this.dispIsolated = dispIsolated;
            this.secure = secure;
            this.corsEnabled = corsEnabled;
            this.cspBypassing = cspBypassing;
            this.fetchEnabled = fetchEnabled;
        }
    }
    
    private static class SchemeHandlerFactory implements CefSchemeHandlerFactory
    {
        private Class<? extends IScheme> cls;
        
        private SchemeHandlerFactory(final Class<? extends IScheme> cls) {
            this.cls = cls;
        }
        
        @Override
        public CefResourceHandler create(final CefBrowser browser, final CefFrame frame, final String schemeName, final CefRequest request) {
            try {
                return new SchemeResourceHandler((IScheme)this.cls.newInstance());
            }
            catch (final Throwable t) {
                t.printStackTrace();
                return null;
            }
        }
    }
}
