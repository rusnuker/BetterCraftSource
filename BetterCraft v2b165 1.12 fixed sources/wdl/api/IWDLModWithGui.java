// 
// Decompiled by Procyon v0.6.0
// 

package wdl.api;

import net.minecraft.client.gui.GuiScreen;

public interface IWDLModWithGui extends IWDLMod
{
    String getButtonName();
    
    void openGui(final GuiScreen p0);
}
