// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.labyplay.party.model;

public final class PartyActionTypes
{
    public enum Client
    {
        INVITE_PLAYER("INVITE_PLAYER", 0), 
        INVITE_PLAYER_RESPONSE("INVITE_PLAYER_RESPONSE", 1), 
        CHAT("CHAT", 2), 
        LEAVE_PARTY("LEAVE_PARTY", 3), 
        KICK_PLAYER("KICK_PLAYER", 4), 
        CHANGE_OWNER("CHANGE_OWNER", 5);
        
        private Client(final String s, final int n) {
        }
        
        public String getKey() {
            return this.name().toLowerCase();
        }
        
        public static Client getByKey(final String key) {
            Client[] values;
            for (int length = (values = values()).length, i = 0; i < length; ++i) {
                final Client type = values[i];
                if (type.getKey().equalsIgnoreCase(key)) {
                    return type;
                }
            }
            return null;
        }
    }
    
    public enum Message
    {
        UNKNOWN("UNKNOWN", 0), 
        PARTY_FULL("PARTY_FULL", 1), 
        PLAYER_NOT_FOUND("PLAYER_NOT_FOUND", 2), 
        PLAYER_IN_OTHER_PARTY("PLAYER_IN_OTHER_PARTY", 3), 
        PLAYER_INVITED("PLAYER_INVITED", 4), 
        PLAYER_LEFT("PLAYER_LEFT", 5), 
        PLAYER_JOINED("PLAYER_JOINED", 6), 
        PLAYER_INVITE_REJECTED("PLAYER_INVITE_REJECTED", 7), 
        PLAYER_KICKED("PLAYER_KICKED", 8), 
        NEW_OWNER_NOT_IN_PARTY("NEW_OWNER_NOT_IN_PARTY", 9), 
        YOU_ARE_NOT_THE_OWNER("YOU_ARE_NOT_THE_OWNER", 10), 
        PARTY_DOES_NOT_EXIST("PARTY_DOES_NOT_EXIST", 11), 
        NO_INVITE("NO_INVITE", 12);
        
        private Message(final String s, final int n) {
        }
        
        public String getKey() {
            return this.name().toLowerCase();
        }
        
        public static Message getByKey(final String key) {
            Message[] values;
            for (int length = (values = values()).length, i = 0; i < length; ++i) {
                final Message type = values[i];
                if (type.getKey().equalsIgnoreCase(key)) {
                    return type;
                }
            }
            return Message.UNKNOWN;
        }
    }
    
    public enum Server
    {
        INVITED_PLAYER("INVITED_PLAYER", 0), 
        INVITE_SUCCESS("INVITE_SUCCESS", 1), 
        CHAT("CHAT", 2), 
        SYSTEM_MESSAGE("SYSTEM_MESSAGE", 3), 
        YOU_LEFT("YOU_LEFT", 4), 
        MEMBER_LIST("MEMBER_LIST", 5);
        
        private Server(final String s, final int n) {
        }
        
        public String getKey() {
            return this.name().toLowerCase();
        }
        
        public static Server getByKey(final String key) {
            Server[] values;
            for (int length = (values = values()).length, i = 0; i < length; ++i) {
                final Server type = values[i];
                if (type.getKey().equalsIgnoreCase(key)) {
                    return type;
                }
            }
            return null;
        }
    }
}
