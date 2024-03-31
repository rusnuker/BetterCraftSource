// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.browser;

import org.cef.callback.CefNativeAdapter;

class CefFrame_N extends CefNativeAdapter implements CefFrame
{
    @Override
    protected void finalize() throws Throwable {
        try {
            this.N_CefFrame_DTOR();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return;
        }
        finally {
            super.finalize();
        }
        super.finalize();
    }
    
    @Override
    public long getIdentifier() {
        try {
            return this.N_GetIdentifier();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return -1L;
        }
    }
    
    @Override
    public String getURL() {
        try {
            return this.N_GetURL();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return null;
        }
    }
    
    @Override
    public String getName() {
        try {
            return this.N_GetName();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return null;
        }
    }
    
    @Override
    public boolean isMain() {
        try {
            return this.N_IsMain();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean isValid() {
        try {
            return this.N_IsValid();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean isFocused() {
        try {
            return this.N_IsFocused();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return false;
        }
    }
    
    @Override
    public CefFrame getParent() {
        try {
            return this.N_GetParent();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return null;
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
    
    private final native void N_CefFrame_DTOR();
    
    private final native int N_GetIdentifier();
    
    private final native String N_GetURL();
    
    private final native String N_GetName();
    
    private final native boolean N_IsMain();
    
    private final native boolean N_IsValid();
    
    private final native boolean N_IsFocused();
    
    private final native CefFrame N_GetParent();
    
    private final native void N_ExecuteJavaScript(final String p0, final String p1, final int p2);
}
