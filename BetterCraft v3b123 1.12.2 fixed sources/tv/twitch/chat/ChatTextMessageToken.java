// 
// Decompiled by Procyon v0.6.0
// 

package tv.twitch.chat;

public class ChatTextMessageToken extends ChatMessageToken
{
    public String text;
    
    public ChatTextMessageToken() {
        this.text = null;
        this.type = ChatMessageTokenType.TTV_CHAT_MSGTOKEN_TEXT;
    }
}
