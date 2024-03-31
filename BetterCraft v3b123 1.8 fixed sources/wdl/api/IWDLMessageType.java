// 
// Decompiled by Procyon v0.6.0
// 

package wdl.api;

import net.minecraft.util.EnumChatFormatting;

public interface IWDLMessageType
{
    EnumChatFormatting getTitleColor();
    
    EnumChatFormatting getTextColor();
    
    String getDisplayName();
    
    String getDescription();
    
    boolean isEnabledByDefault();
}
