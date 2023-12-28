// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.network;

import java.util.Iterator;
import java.util.Vector;

public abstract class CefPostData
{
    CefPostData() {
    }
    
    public static final CefPostData create() {
        return CefPostData_N.createNative();
    }
    
    public abstract boolean isReadOnly();
    
    public abstract int getElementCount();
    
    public abstract void getElements(final Vector<CefPostDataElement> p0);
    
    public abstract boolean removeElement(final CefPostDataElement p0);
    
    public abstract boolean addElement(final CefPostDataElement p0);
    
    public abstract void removeElements();
    
    @Override
    public String toString() {
        return this.toString(null);
    }
    
    public String toString(final String mimeType) {
        final Vector<CefPostDataElement> elements = new Vector<CefPostDataElement>();
        this.getElements(elements);
        String returnValue = "";
        for (final CefPostDataElement el : elements) {
            returnValue = String.valueOf(returnValue) + el.toString(mimeType) + "\n";
        }
        return returnValue;
    }
}
