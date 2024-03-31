// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.ingamechat.tools.playermenu;

import java.beans.ConstructorProperties;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;
import net.labymod.main.lang.LanguageManager;
import java.util.List;

public class PlayerMenu
{
    private List<PlayerMenuEntry> playerMenuEntries;
    
    public PlayerMenu() {
        this.playerMenuEntries = new ArrayList<PlayerMenuEntry>(Arrays.asList(new PlayerMenuEntry(LanguageManager.translate("playermenu_entry_partyinvite"), "/party invite {name}", true)));
    }
    
    public List<PlayerMenuEntry> getPlayerMenuEntries() {
        return this.playerMenuEntries;
    }
    
    public static class PlayerMenuEntry
    {
        private String displayName;
        private String command;
        private boolean sendInstantly;
        
        public String getDisplayName() {
            return this.displayName;
        }
        
        public String getCommand() {
            return this.command;
        }
        
        public boolean isSendInstantly() {
            return this.sendInstantly;
        }
        
        public void setDisplayName(final String displayName) {
            this.displayName = displayName;
        }
        
        public void setCommand(final String command) {
            this.command = command;
        }
        
        public void setSendInstantly(final boolean sendInstantly) {
            this.sendInstantly = sendInstantly;
        }
        
        @ConstructorProperties({ "displayName", "command", "sendInstantly" })
        public PlayerMenuEntry(final String displayName, final String command, final boolean sendInstantly) {
            this.displayName = displayName;
            this.command = command;
            this.sendInstantly = sendInstantly;
        }
    }
}
