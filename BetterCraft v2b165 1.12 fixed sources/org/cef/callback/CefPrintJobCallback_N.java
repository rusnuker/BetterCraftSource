// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.callback;

class CefPrintJobCallback_N extends CefNativeAdapter implements CefPrintJobCallback
{
    @Override
    public void Continue() {
        try {
            this.N_Continue();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    private final native void N_Continue();
}
