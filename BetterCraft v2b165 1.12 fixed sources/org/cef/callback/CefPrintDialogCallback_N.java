// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.callback;

import org.cef.misc.CefPrintSettings;

class CefPrintDialogCallback_N extends CefNativeAdapter implements CefPrintDialogCallback
{
    @Override
    public void Continue(final CefPrintSettings settings) {
        try {
            this.N_Continue(settings);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    @Override
    public void cancel() {
        try {
            this.N_Cancel();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    private final native void N_Continue(final CefPrintSettings p0);
    
    private final native void N_Cancel();
}
