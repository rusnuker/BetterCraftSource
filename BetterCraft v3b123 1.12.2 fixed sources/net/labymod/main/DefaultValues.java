// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.main;

import net.labymod.utils.ModColor;
import java.awt.Color;

public class DefaultValues
{
    public static final boolean GRID_ENABLED = false;
    public static final int PREFIX_COLOR;
    public static final int BRACKETS_COLOR;
    public static final int VALUE_COLOR;
    public static final int BACKGROUND_COLOR;
    public static final int BOLD_FORMATTING = 0;
    public static final int ITALIC_FORMATTING = 0;
    public static final int UNDERLINE_FORMATTING = 0;
    public static final int POTION_NAME_COLOR;
    public static final int POTION_AMPLIFIER_COLOR;
    public static final int POTION_DURATION_COLOR;
    
    static {
        PREFIX_COLOR = new Color(255, 170, 0).getRGB();
        BRACKETS_COLOR = new Color(170, 170, 170).getRGB();
        VALUE_COLOR = new Color(255, 255, 255).getRGB();
        BACKGROUND_COLOR = new Color(0, 0, 0).getRGB();
        POTION_NAME_COLOR = ModColor.YELLOW.getColor().getRGB();
        POTION_AMPLIFIER_COLOR = ModColor.YELLOW.getColor().getRGB();
        POTION_DURATION_COLOR = new Color(255, 255, 255).getRGB();
    }
}
