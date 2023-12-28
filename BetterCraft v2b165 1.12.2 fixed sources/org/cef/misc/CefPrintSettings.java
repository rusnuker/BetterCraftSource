// 
// Decompiled by Procyon v0.6.0
// 

package org.cef.misc;

import java.util.Vector;
import java.awt.Rectangle;
import java.awt.Dimension;

public abstract class CefPrintSettings
{
    CefPrintSettings() {
    }
    
    public static final CefPrintSettings create() {
        return CefPrintSettings_N.createNative();
    }
    
    public abstract boolean isValid();
    
    public abstract boolean isReadOnly();
    
    public abstract CefPrintSettings copy();
    
    public abstract void setOrientation(final boolean p0);
    
    public abstract boolean isLandscape();
    
    public abstract void setPrinterPrintableArea(final Dimension p0, final Rectangle p1, final boolean p2);
    
    public abstract void setDeviceName(final String p0);
    
    public abstract String getDeviceName();
    
    public abstract void setDPI(final int p0);
    
    public abstract int getDPI();
    
    public abstract void setPageRanges(final Vector<CefPageRange> p0);
    
    public abstract int getPageRangesCount();
    
    public abstract void getPageRanges(final Vector<CefPageRange> p0);
    
    public abstract void setSelectionOnly(final boolean p0);
    
    public abstract boolean isSelectionOnly();
    
    public abstract void setCollate(final boolean p0);
    
    public abstract boolean willCollate();
    
    public abstract void setColorModel(final ColorModel p0);
    
    public abstract ColorModel getColorModel();
    
    public abstract void setCopies(final int p0);
    
    public abstract int getCopies();
    
    public abstract void setDuplexMode(final DuplexMode p0);
    
    public abstract DuplexMode getDuplexMode();
    
    public enum ColorModel
    {
        COLOR_MODEL_UNKNOWN("COLOR_MODEL_UNKNOWN", 0), 
        COLOR_MODEL_GRAY("COLOR_MODEL_GRAY", 1), 
        COLOR_MODEL_COLOR("COLOR_MODEL_COLOR", 2), 
        COLOR_MODEL_CMYK("COLOR_MODEL_CMYK", 3), 
        COLOR_MODEL_CMY("COLOR_MODEL_CMY", 4), 
        COLOR_MODEL_KCMY("COLOR_MODEL_KCMY", 5), 
        COLOR_MODEL_CMY_K("COLOR_MODEL_CMY_K", 6), 
        COLOR_MODEL_BLACK("COLOR_MODEL_BLACK", 7), 
        COLOR_MODEL_GRAYSCALE("COLOR_MODEL_GRAYSCALE", 8), 
        COLOR_MODEL_RGB("COLOR_MODEL_RGB", 9), 
        COLOR_MODEL_RGB16("COLOR_MODEL_RGB16", 10), 
        COLOR_MODEL_RGBA("COLOR_MODEL_RGBA", 11), 
        COLOR_MODEL_COLORMODE_COLOR("COLOR_MODEL_COLORMODE_COLOR", 12), 
        COLOR_MODEL_COLORMODE_MONOCHROME("COLOR_MODEL_COLORMODE_MONOCHROME", 13), 
        COLOR_MODEL_HP_COLOR_COLOR("COLOR_MODEL_HP_COLOR_COLOR", 14), 
        COLOR_MODEL_HP_COLOR_BLACK("COLOR_MODEL_HP_COLOR_BLACK", 15), 
        COLOR_MODEL_PRINTOUTMODE_NORMAL("COLOR_MODEL_PRINTOUTMODE_NORMAL", 16), 
        COLOR_MODEL_PRINTOUTMODE_NORMAL_GRAY("COLOR_MODEL_PRINTOUTMODE_NORMAL_GRAY", 17), 
        COLOR_MODEL_PROCESSCOLORMODEL_CMYK("COLOR_MODEL_PROCESSCOLORMODEL_CMYK", 18), 
        COLOR_MODEL_PROCESSCOLORMODEL_GREYSCALE("COLOR_MODEL_PROCESSCOLORMODEL_GREYSCALE", 19), 
        COLOR_MODEL_PROCESSCOLORMODEL_RGB("COLOR_MODEL_PROCESSCOLORMODEL_RGB", 20);
        
        private ColorModel(final String s, final int n) {
        }
    }
    
    public enum DuplexMode
    {
        DUPLEX_MODE_UNKNOWN("DUPLEX_MODE_UNKNOWN", 0), 
        DUPLEX_MODE_SIMPLEX("DUPLEX_MODE_SIMPLEX", 1), 
        DUPLEX_MODE_LONG_EDGE("DUPLEX_MODE_LONG_EDGE", 2), 
        DUPLEX_MODE_SHORT_EDGE("DUPLEX_MODE_SHORT_EDGE", 3);
        
        private DuplexMode(final String s, final int n) {
        }
    }
}
