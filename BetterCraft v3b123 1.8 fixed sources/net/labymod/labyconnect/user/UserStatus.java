// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.labyconnect.user;

import net.labymod.main.lang.LanguageManager;

public enum UserStatus
{
    ONLINE("ONLINE", 0, (byte)0, "a"), 
    AWAY("AWAY", 1, (byte)1, "b"), 
    BUSY("BUSY", 2, (byte)2, "5"), 
    OFFLINE("OFFLINE", 3, (byte)(-1), "c");
    
    private byte id;
    private String chatColor;
    private String name;
    
    private UserStatus(final String s, final int n, final byte id, final String chatColor) {
        this.id = id;
        this.chatColor = chatColor;
        this.name = LanguageManager.translate("user_status_" + this.name().toLowerCase());
    }
    
    public static UserStatus getById(final int id) {
        UserStatus[] values;
        for (int length = (values = values()).length, i = 0; i < length; ++i) {
            final UserStatus status = values[i];
            if (status.id == id) {
                return status;
            }
        }
        return UserStatus.OFFLINE;
    }
    
    public byte getId() {
        return this.id;
    }
    
    public String getChatColor() {
        return this.chatColor;
    }
    
    public String getName() {
        return this.name;
    }
}
