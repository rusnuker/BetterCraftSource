// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.misc;

import java.util.Vector;
import java.awt.Rectangle;
import java.awt.Dimension;
import org.cef.callback.CefNative;

class CefPrintSettings_N extends CefPrintSettings implements CefNative
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
    
    CefPrintSettings_N() {
        this.N_CefHandle = 0L;
    }
    
    public static final CefPrintSettings createNative() {
        final CefPrintSettings_N result = new CefPrintSettings_N();
        try {
            result.N_CefPrintSettings_CTOR();
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
            this.N_CefPrintSettings_DTOR();
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
    public boolean isValid() {
        try {
            return this.N_IsValid();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return false;
        }
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
    public CefPrintSettings copy() {
        try {
            return this.N_Copy();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return null;
        }
    }
    
    @Override
    public void setOrientation(final boolean landscape) {
        try {
            this.N_SetOrientation(landscape);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    @Override
    public boolean isLandscape() {
        try {
            return this.N_IsLandscape();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return false;
        }
    }
    
    @Override
    public void setPrinterPrintableArea(final Dimension physical_size_device_units, final Rectangle printable_area_device_units, final boolean landscape_needs_flip) {
        try {
            this.N_SetPrinterPrintableArea(physical_size_device_units, printable_area_device_units, landscape_needs_flip);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    @Override
    public void setDeviceName(final String name) {
        try {
            this.N_SetDeviceName(name);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    @Override
    public String getDeviceName() {
        try {
            return this.N_GetDeviceName();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return null;
        }
    }
    
    @Override
    public void setDPI(final int dpi) {
        try {
            this.N_SetDPI(dpi);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    @Override
    public int getDPI() {
        try {
            return this.N_GetDPI();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return 0;
        }
    }
    
    @Override
    public void setPageRanges(final Vector<CefPageRange> ranges) {
        try {
            this.N_SetPageRanges(ranges);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    @Override
    public int getPageRangesCount() {
        try {
            return this.N_GetPageRangesCount();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return 0;
        }
    }
    
    @Override
    public void getPageRanges(final Vector<CefPageRange> ranges) {
        try {
            this.N_GetPageRanges(ranges);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    @Override
    public void setSelectionOnly(final boolean selection_only) {
        try {
            this.N_SetSelectionOnly(selection_only);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    @Override
    public boolean isSelectionOnly() {
        try {
            return this.N_IsSelectionOnly();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return false;
        }
    }
    
    @Override
    public void setCollate(final boolean collate) {
        try {
            this.N_SetCollate(collate);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    @Override
    public boolean willCollate() {
        try {
            return this.N_WillCollate();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return false;
        }
    }
    
    @Override
    public void setColorModel(final ColorModel model) {
        try {
            this.N_SetColorModel(model);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    @Override
    public ColorModel getColorModel() {
        try {
            return this.N_GetColorModel();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return null;
        }
    }
    
    @Override
    public void setCopies(final int copies) {
        try {
            this.N_SetCopies(copies);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    @Override
    public int getCopies() {
        try {
            return this.N_GetCopies();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return 0;
        }
    }
    
    @Override
    public void setDuplexMode(final DuplexMode mode) {
        try {
            this.N_SetDuplexMode(mode);
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
        }
    }
    
    @Override
    public DuplexMode getDuplexMode() {
        try {
            return this.N_GetDuplexMode();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return null;
        }
    }
    
    private final native void N_CefPrintSettings_CTOR();
    
    private final native boolean N_IsValid();
    
    private final native boolean N_IsReadOnly();
    
    private final native CefPrintSettings N_Copy();
    
    private final native void N_SetOrientation(final boolean p0);
    
    private final native boolean N_IsLandscape();
    
    private final native void N_SetPrinterPrintableArea(final Dimension p0, final Rectangle p1, final boolean p2);
    
    private final native void N_SetDeviceName(final String p0);
    
    private final native String N_GetDeviceName();
    
    private final native void N_SetDPI(final int p0);
    
    private final native int N_GetDPI();
    
    private final native void N_SetPageRanges(final Vector<CefPageRange> p0);
    
    private final native int N_GetPageRangesCount();
    
    private final native void N_GetPageRanges(final Vector<CefPageRange> p0);
    
    private final native void N_SetSelectionOnly(final boolean p0);
    
    private final native boolean N_IsSelectionOnly();
    
    private final native void N_SetCollate(final boolean p0);
    
    private final native boolean N_WillCollate();
    
    private final native void N_SetColorModel(final ColorModel p0);
    
    private final native ColorModel N_GetColorModel();
    
    private final native void N_SetCopies(final int p0);
    
    private final native int N_GetCopies();
    
    private final native void N_SetDuplexMode(final DuplexMode p0);
    
    private final native DuplexMode N_GetDuplexMode();
    
    private final native void N_CefPrintSettings_DTOR();
}
