// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.gui.chat;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ChatType;
import net.minecraft.client.Minecraft;

public class OverlayChatListener implements IChatListener
{
    private final Minecraft field_192577_a;
    
    public OverlayChatListener(final Minecraft p_i47394_1_) {
        this.field_192577_a = p_i47394_1_;
    }
    
    @Override
    public void func_192576_a(final ChatType p_192576_1_, final ITextComponent p_192576_2_) {
        this.field_192577_a.ingameGUI.setRecordPlaying(p_192576_2_, false);
    }
}
