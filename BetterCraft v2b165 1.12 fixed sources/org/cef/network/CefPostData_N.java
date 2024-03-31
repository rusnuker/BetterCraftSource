// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.network;

import java.util.Vector;
import org.cef.callback.CefNative;

class CefPostData_N extends CefPostData implements CefNative
{
    private long N_CefHandle;
    
    @Override
    public void setNativeRef(final String identifer, final long nativeRef) {
        this.N_CefHandle = nativeRef;
    }
    
    @Override
    public long getNativeRef(final String identifer) {
        return this.N_CefHandle;
    }
    
    CefPostData_N() {
        this.N_CefHandle = 0L;
    }
    
    public static final CefPostData createNative() {
        final CefPostData_N result = new CefPostData_N();
        try {
            result.N_CefPostData_CTOR();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
        if (result.N_CefHandle == 0L) {
            return null;
        }
        return result;
    }
    
    @Override
    protected void finalize() throws Throwable {
        try {
            this.N_CefPostData_DTOR();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return;
        }
        finally {
            super.finalize();
        }
        super.finalize();
    }
    
    @Override
    public boolean isReadOnly() {
        try {
            return this.N_IsReadOnly();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return false;
        }
    }
    
    @Override
    public int getElementCount() {
        try {
            return this.N_GetElementCount();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return 0;
        }
    }
    
    @Override
    public void getElements(final Vector<CefPostDataElement> elements) {
        try {
            this.N_GetElements(elements);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    @Override
    public boolean removeElement(final CefPostDataElement element) {
        try {
            return this.N_RemoveElement(element);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean addElement(final CefPostDataElement element) {
        try {
            return this.N_AddElement(element);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return false;
        }
    }
    
    @Override
    public void removeElements() {
        try {
            this.N_RemoveElements();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    private final native void N_CefPostData_CTOR();
    
    private final native boolean N_IsReadOnly();
    
    private final native int N_GetElementCount();
    
    private final native void N_GetElements(final Vector<CefPostDataElement> p0);
    
    private final native boolean N_RemoveElement(final CefPostDataElement p0);
    
    private final native boolean N_AddElement(final CefPostDataElement p0);
    
    private final native void N_RemoveElements();
    
    private final native void N_CefPostData_DTOR();
}
