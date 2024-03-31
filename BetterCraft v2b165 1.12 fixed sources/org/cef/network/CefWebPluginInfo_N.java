// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.network;

import org.cef.callback.CefNativeAdapter;

class CefWebPluginInfo_N extends CefNativeAdapter implements CefWebPluginInfo
{
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
    public String getPath() {
        try {
            return this.N_GetPath();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return null;
        }
    }
    
    @Override
    public String getVersion() {
        try {
            return this.N_GetVersion();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return null;
        }
    }
    
    @Override
    public String getDescription() {
        try {
            return this.N_GetDescription();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return null;
        }
    }
    
    private final native String N_GetName();
    
    private final native String N_GetPath();
    
    private final native String N_GetVersion();
    
    private final native String N_GetDescription();
}
