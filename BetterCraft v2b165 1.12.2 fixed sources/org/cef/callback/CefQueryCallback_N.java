// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.callback;

class CefQueryCallback_N extends CefNativeAdapter implements CefQueryCallback
{
    @Override
    public void success(final String response) {
        try {
            this.N_Success(response);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    @Override
    public void failure(final int error_code, final String error_message) {
        try {
            this.N_Failure(error_code, error_message);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    private final native void N_Success(final String p0);
    
    private final native void N_Failure(final int p0, final String p1);
}
