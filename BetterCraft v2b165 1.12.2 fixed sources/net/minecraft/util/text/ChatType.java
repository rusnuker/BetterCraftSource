// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.util.text;

public enum ChatType
{
    CHAT("CHAT", 0, (byte)0), 
    SYSTEM("SYSTEM", 1, (byte)1), 
    GAME_INFO("GAME_INFO", 2, (byte)2);
    
    private final byte field_192588_d;
    
    private ChatType(final String s, final int n, final byte p_i47429_3_) {
        this.field_192588_d = p_i47429_3_;
    }
    
    public byte func_192583_a() {
        return this.field_192588_d;
    }
    
    public static ChatType func_192582_a(final byte p_192582_0_) {
        ChatType[] values;
        for (int length = (values = values()).length, i = 0; i < length; ++i) {
            final ChatType chattype = values[i];
            if (p_192582_0_ == chattype.field_192588_d) {
                return chattype;
            }
        }
        return ChatType.CHAT;
    }
}
