// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.callback;

class CefBeforeDownloadCallback_N extends CefNativeAdapter implements CefBeforeDownloadCallback
{
    @Override
    public void Continue(final String downloadPath, final boolean showDialog) {
        try {
            this.N_Continue(downloadPath, showDialog);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    private final native void N_Continue(final String p0, final boolean p1);
}
