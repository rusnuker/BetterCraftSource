// 
// Decompiled by Procyon v0.6.0
// 

package wdl.api;

import net.minecraft.util.text.TextFormatting;

public interface IWDLMessageType
{
    TextFormatting getTitleColor();
    
    TextFormatting getTextColor();
    
    String getDisplayName();
    
    String getDescription();
    
    boolean isEnabledByDefault();
}
