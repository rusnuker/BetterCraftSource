// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.callback;

class CefAuthCallback_N extends CefNativeAdapter implements CefAuthCallback
{
    @Override
    protected void finalize() throws Throwable {
        this.cancel();
        super.finalize();
    }
    
    @Override
    public void Continue(final String username, final String password) {
        try {
            this.N_Continue(this.getNativeRef(null), username, password);
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
    
    private final native void N_Continue(final long p0, final String p1, final String p2);
    
    private final native void N_Cancel(final long p0);
}
