// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.callback;

class CefDownloadItemCallback_N extends CefNativeAdapter implements CefDownloadItemCallback
{
    @Override
    public void cancel() {
        try {
            this.N_Cancel();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    private final native void N_Cancel();
}
