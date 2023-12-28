// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.callback;

class CefJSDialogCallback_N extends CefNativeAdapter implements CefJSDialogCallback
{
    @Override
    public void Continue(final boolean success, final String user_input) {
        try {
            this.N_Continue(success, user_input);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    private final native void N_Continue(final boolean p0, final String p1);
}
