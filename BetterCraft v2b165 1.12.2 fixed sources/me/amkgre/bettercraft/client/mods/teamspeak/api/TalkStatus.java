// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.teamspeak.api;

public enum TalkStatus
{
    NOT_TALKING("NOT_TALKING", 0, 0), 
    TALKING("TALKING", 1, 1), 
    TALKING_BUT_MUTED("TALKING_BUT_MUTED", 2, 2);
    
    private int id;
    
    private TalkStatus(final String s, final int n, final int id) {
        this.id = id;
    }
    
    public int getId() {
        return this.id;
    }
    
    public static TalkStatus byId(final int id) {
        TalkStatus[] values;
        for (int length = (values = values()).length, i = 0; i < length; ++i) {
            final TalkStatus talkStatus = values[i];
            if (talkStatus.getId() == id) {
                return talkStatus;
            }
        }
        throw new IllegalArgumentException("ID " + id + " could not be matched with any talk status!");
    }
}
