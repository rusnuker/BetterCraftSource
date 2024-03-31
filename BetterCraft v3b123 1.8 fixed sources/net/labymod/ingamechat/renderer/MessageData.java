// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.ingamechat.renderer;

import java.beans.ConstructorProperties;
import net.labymod.ingamechat.tools.filter.Filters;

public class MessageData
{
    private boolean displayInSecondChat;
    private Filters.Filter filter;
    
    public boolean isDisplayInSecondChat() {
        return this.displayInSecondChat;
    }
    
    public Filters.Filter getFilter() {
        return this.filter;
    }
    
    @ConstructorProperties({ "displayInSecondChat", "filter" })
    public MessageData(final boolean displayInSecondChat, final Filters.Filter filter) {
        this.displayInSecondChat = displayInSecondChat;
        this.filter = filter;
    }
}
