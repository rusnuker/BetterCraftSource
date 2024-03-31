// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.callback;

import java.util.Vector;

class CefFileDialogCallback_N extends CefNativeAdapter implements CefFileDialogCallback
{
    @Override
    public void Continue(final int selectedAcceptFilter, final Vector<String> filePaths) {
        try {
            this.N_Continue(selectedAcceptFilter, filePaths);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    @Override
    public void Cancel() {
        try {
            this.N_Cancel();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    private final native void N_Continue(final int p0, final Vector<String> p1);
    
    private final native void N_Cancel();
}
