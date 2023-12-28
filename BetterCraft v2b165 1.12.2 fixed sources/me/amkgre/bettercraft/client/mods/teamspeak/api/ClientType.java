// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.teamspeak.api;

public enum ClientType
{
    NORMAL("NORMAL", 0, 0), 
    BOT("BOT", 1, 1);
    
    private int id;
    
    private ClientType(final String s, final int n, final int id) {
        this.id = id;
    }
    
    public int getId() {
        return this.id;
    }
    
    public static ClientType byId(final int id) {
        ClientType[] values;
        for (int length = (values = values()).length, i = 0; i < length; ++i) {
            final ClientType clientType = values[i];
            if (clientType.getId() == id) {
                return clientType;
            }
        }
        return ClientType.NORMAL;
    }
}
