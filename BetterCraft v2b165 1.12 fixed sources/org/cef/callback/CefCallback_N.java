// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.callback;

class CefCallback_N extends CefNativeAdapter implements CefCallback
{
    @Override
    protected void finalize() throws Throwable {
        this.cancel();
        super.finalize();
    }
    
    @Override
    public void Continue() {
        try {
            this.N_Continue(this.getNativeRef(null));
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    @Override
    public void cancel() {
        try {
            this.N_Cancel(this.getNativeRef(null));
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    private final native void N_Continue(final long p0);
    
    private final native void N_Cancel(final long p0);
}
