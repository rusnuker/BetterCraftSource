// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.core;

public interface GuiExtraChatAdapter
{
    void drawChat(final int p0);
    
    void clearChatMessages();
    
    void setChatLine(final Object p0, final int p1, final int p2, final boolean p3, final boolean p4);
    
    void refreshChat();
    
    void scroll(final int p0);
    
    Object getChatComponent(final int p0, final int p1);
}
