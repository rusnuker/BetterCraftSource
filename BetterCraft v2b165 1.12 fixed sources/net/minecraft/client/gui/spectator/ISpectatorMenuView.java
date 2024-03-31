// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.gui.spectator;

import net.minecraft.util.text.ITextComponent;
import java.util.List;

public interface ISpectatorMenuView
{
    List<ISpectatorMenuObject> getItems();
    
    ITextComponent getPrompt();
}
