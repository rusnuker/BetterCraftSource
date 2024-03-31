// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.network;

import org.cef.callback.CefWebPluginUnstableCallback;
import org.cef.callback.CefWebPluginInfoVisitor;

public abstract class CefWebPluginManager
{
    CefWebPluginManager() {
    }
    
    public static final CefWebPluginManager getGlobalManager() {
        return CefWebPluginManager_N.getInstance();
    }
    
    public abstract void visitPlugins(final CefWebPluginInfoVisitor p0);
    
    public abstract void refreshPlugins();
    
    public abstract void unregisterInternalPlugin(final String p0);
    
    public abstract void registerPluginCrash(final String p0);
    
    public abstract void isWebPluginUnstable(final String p0, final CefWebPluginUnstableCallback p1);
}
