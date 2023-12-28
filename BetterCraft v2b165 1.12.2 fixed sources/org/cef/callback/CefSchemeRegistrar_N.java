// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.callback;

class CefSchemeRegistrar_N extends CefNativeAdapter implements CefSchemeRegistrar
{
    @Override
    public boolean addCustomScheme(final String schemeName, final boolean isStandard, final boolean isLocal, final boolean isDisplayIsolated, final boolean isSecure, final boolean isCorsEnabled, final boolean isCspBypassing, final boolean isFetchEnabled) {
        try {
            return this.N_AddCustomScheme(schemeName, isStandard, isLocal, isDisplayIsolated, isSecure, isCorsEnabled, isCspBypassing, isFetchEnabled);
        }
        catch (final UnsatisfiedLinkError err) {
            err.printStackTrace();
            return false;
        }
    }
    
    private final native boolean N_AddCustomScheme(final String p0, final boolean p1, final boolean p2, final boolean p3, final boolean p4, final boolean p5, final boolean p6, final boolean p7);
}
