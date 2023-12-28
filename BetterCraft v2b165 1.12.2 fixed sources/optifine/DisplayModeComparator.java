// 
// Decompiled by Procyon v0.6.0
// 

package optifine;

import org.lwjgl.opengl.DisplayMode;
import java.util.Comparator;

public class DisplayModeComparator implements Comparator
{
    @Override
    public int compare(final Object p_compare_1_, final Object p_compare_2_) {
        final DisplayMode displaymode = (DisplayMode)p_compare_1_;
        final DisplayMode displaymode2 = (DisplayMode)p_compare_2_;
        if (displaymode.getWidth() != displaymode2.getWidth()) {
            return displaymode.getWidth() - displaymode2.getWidth();
        }
        if (displaymode.getHeight() != displaymode2.getHeight()) {
            return displaymode.getHeight() - displaymode2.getHeight();
        }
        if (displaymode.getBitsPerPixel() != displaymode2.getBitsPerPixel()) {
            return displaymode.getBitsPerPixel() - displaymode2.getBitsPerPixel();
        }
        return (displaymode.getFrequency() != displaymode2.getFrequency()) ? (displaymode.getFrequency() - displaymode2.getFrequency()) : 0;
    }
}
