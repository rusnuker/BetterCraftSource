// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.teamspeak.api;

public enum MessageTargetMode
{
    CLIENT("CLIENT", 0, 1), 
    CHANNEL("CHANNEL", 1, 2), 
    SERVER("SERVER", 2, 3), 
    POKE("POKE", 3, -1);
    
    private int id;
    
    private MessageTargetMode(final String s, final int n, final int id) {
        this.id = id;
    }
    
    public int getId() {
        return this.id;
    }
    
    public static MessageTargetMode byId(final int id) {
        MessageTargetMode[] values;
        for (int length = (values = values()).length, i = 0; i < length; ++i) {
            final MessageTargetMode targetMode = values[i];
            if (targetMode.getId() == id) {
                return targetMode;
            }
        }
        throw new IllegalArgumentException("ID " + id + " could not be matched with any message target status!");
    }
}
