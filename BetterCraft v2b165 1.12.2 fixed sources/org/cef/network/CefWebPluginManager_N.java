// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.network;

import org.cef.callback.CefWebPluginUnstableCallback;
import org.cef.callback.CefWebPluginInfoVisitor;
import org.cef.callback.CefNative;

class CefWebPluginManager_N extends CefWebPluginManager implements CefNative
{
    private long N_CefHandle;
    private static CefWebPluginManager_N instance;
    
    static {
        CefWebPluginManager_N.instance = null;
    }
    
    @Override
    public void setNativeRef(final String identifer, final long nativeRef) {
        this.N_CefHandle = nativeRef;
    }
    
    @Override
    public long getNativeRef(final String identifer) {
        return this.N_CefHandle;
    }
    
    CefWebPluginManager_N() {
        this.N_CefHandle = 0L;
    }
    
    public static synchronized CefWebPluginManager_N getInstance() {
        if (CefWebPluginManager_N.instance == null) {
            CefWebPluginManager_N.instance = new CefWebPluginManager_N();
        }
        return CefWebPluginManager_N.instance;
    }
    
    @Override
    public void visitPlugins(final CefWebPluginInfoVisitor visitor) {
        try {
            this.N_VisitPlugins(visitor);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    @Override
    public void refreshPlugins() {
        try {
            this.N_RefreshPlugins();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    @Override
    public void unregisterInternalPlugin(final String path) {
        try {
            this.N_UnregisterInternalPlugin(path);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    @Override
    public void registerPluginCrash(final String path) {
        try {
            this.N_RegisterPluginCrash(path);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    @Override
    public void isWebPluginUnstable(final String path, final CefWebPluginUnstableCallback callback) {
        try {
            this.N_IsWebPluginUnstable(path, callback);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    private final native void N_VisitPlugins(final CefWebPluginInfoVisitor p0);
    
    private final native void N_RefreshPlugins();
    
    private final native void N_UnregisterInternalPlugin(final String p0);
    
    private final native void N_RegisterPluginCrash(final String p0);
    
    private final native void N_IsWebPluginUnstable(final String p0, final CefWebPluginUnstableCallback p1);
}
