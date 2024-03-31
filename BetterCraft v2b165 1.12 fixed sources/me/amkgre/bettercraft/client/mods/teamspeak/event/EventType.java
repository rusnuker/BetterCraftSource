// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.teamspeak.event;

public enum EventType
{
    ANY("ANY", 0, "any"), 
    TALK_STATUS_CHANGE("TALK_STATUS_CHANGE", 1, "notifytalkstatuschange"), 
    MESSAGE("MESSAGE", 2, "notifymessage"), 
    MESSAGE_LIST("MESSAGE_LIST", 3, "notifymessagelist"), 
    COMPLAIN_LIST("COMPLAIN_LIST", 4, "notifycomplainlist"), 
    BAN_LIST("BAN_LIST", 5, "notifybanlist"), 
    CLIENT_MOVED("CLIENT_MOVED", 6, "notifyclientmoved"), 
    CLIENT_LEFT_VIEW("CLIENT_LEFT_VIEW", 7, "notifyclientleftview"), 
    CLIENT_ENTERED_VIEW("CLIENT_ENTERED_VIEW", 8, "notifycliententerview"), 
    CLIENT_POKE("CLIENT_POKE", 9, "notifyclientpoke"), 
    CLIENT_CHAT_CLOSED("CLIENT_CHAT_CLOSED", 10, "notifyclientchatclosed"), 
    CLIENT_CHAT_COMPOSING("CLIENT_CHAT_COMPOSING", 11, "notifyclientchatcomposing"), 
    CLIENT_UPDATED("CLIENT_UPDATED", 12, "notifyclientupdated"), 
    CLIENT_IDS("CLIENT_IDS", 13, "notifyclientids"), 
    CLIENT_DB_ID_FROM_UID("CLIENT_DB_ID_FROM_UID", 14, "notifyclientdbidfromuid"), 
    CLIENT_NAME_FROM_UID("CLIENT_NAME_FROM_UID", 15, "notifyclientnamefromuid"), 
    CLIENT_NAME_FROM_DB_ID("CLIENT_NAME_FROM_DB_ID", 16, "notifyclientnamefromdbid"), 
    CLIENT_UID_FROM_CLID("CLIENT_UID_FROM_CLID", 17, "notifyclientuidfromclid"), 
    CONNECTION_INFO("CONNECTION_INFO", 18, "notifyconnectioninfo"), 
    CHANNEL_CREATED("CHANNEL_CREATED", 19, "notifychannelcreated"), 
    CHANNEL_EDITED("CHANNEL_EDITED", 20, "notifychanneledited"), 
    CHANNEL_DELETED("CHANNEL_DELETED", 21, "notifychanneldeleted"), 
    CHANNEL_DESCRIPTION_CHANGED("CHANNEL_DESCRIPTION_CHANGED", 22, "notifychanneldescriptionchanged"), 
    CHANNEL_MOVED("CHANNEL_MOVED", 23, "notifychannelmoved"), 
    SERVER_EDITED("SERVER_EDITED", 24, "notifyserveredited"), 
    SERVER_UPDATED("SERVER_UPDATED", 25, "notifyserverupdated"), 
    CHANNEL_LIST("CHANNEL_LIST", 26, "channellist"), 
    CHANNEL_LIST_FINISHED("CHANNEL_LIST_FINISHED", 27, "channellistfinished"), 
    TEXT_MESSAGE("TEXT_MESSAGE", 28, "notifytextmessage"), 
    CURRENT_SERVER_CONNECTION_CHANGED("CURRENT_SERVER_CONNECTION_CHANGED", 29, "notifycurrentserverconnectionchanged"), 
    CONNECT_STATUS_CHANGE("CONNECT_STATUS_CHANGE", 30, "notifyconnectstatuschange"), 
    CHANNEL_GROUP_CHANNGED("CHANNEL_GROUP_CHANNGED", 31, "notifyclientchannelgroupchanged"), 
    CLIENT_NEEDED_PERMISSIONS("CLIENT_NEEDED_PERMISSIONS", 32, "notifyclientneededpermissions"), 
    SERVER_GROUP_CLIENT_ADDED("SERVER_GROUP_CLIENT_ADDED", 33, "notifyservergroupclientadded"), 
    SERVER_GROUP_CLIENT_REMOVED("SERVER_GROUP_CLIENT_REMOVED", 34, "notifyservergroupclientdeleted"), 
    CHANNEL_PASSWORD_CHANGED("CHANNEL_PASSWORD_CHANGED", 35, "notifychannelpasswordchanged"), 
    CHANNEL_SUBSCRIBED("CHANNEL_SUBSCRIBED", 36, "notifychannelsubscribed"), 
    START_DOWNLOAD("START_DOWNLOAD", 37, "notifystartdownload"), 
    STATUS_FILE_TRANSFER("STATUS_FILE_TRANSFER", 38, "notifystatusfiletransfer"), 
    SERVER_GROUP_LIST("SERVER_GROUP_LIST", 39, "notifyservergrouplist"), 
    CHANNEL_GROUP_LIST("CHANNEL_GROUP_LIST", 40, "notifychannelgrouplist");
    
    private String name;
    
    private EventType(final String s, final int n, final String name) {
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }
    
    public static EventType byName(final String name) {
        EventType[] values;
        for (int length = (values = values()).length, i = 0; i < length; ++i) {
            final EventType eventType = values[i];
            if (eventType.getName().equals(name)) {
                return eventType;
            }
        }
        return null;
    }
}
