// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.callback;

class CefRequestCallback_N extends CefNativeAdapter implements CefRequestCallback
{
    @Override
    protected void finalize() throws Throwable {
        this.Cancel();
        super.finalize();
    }
    
    @Override
    public void Continue(final boolean allow) {
        try {
            this.N_Continue(this.getNativeRef(null), allow);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    @Override
    public void Cancel() {
        try {
            this.N_Cancel(this.getNativeRef(null));
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    private final native void N_Continue(final long p0, final boolean p1);
    
    private final native void N_Cancel(final long p0);
}
