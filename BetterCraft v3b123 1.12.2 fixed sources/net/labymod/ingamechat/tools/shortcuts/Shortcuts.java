// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.ingamechat.tools.shortcuts;

import java.beans.ConstructorProperties;
import java.util.ArrayList;
import java.util.List;

public class Shortcuts
{
    private List<Shortcut> shortcuts;
    
    public Shortcuts() {
        this.shortcuts = new ArrayList<Shortcut>();
    }
    
    public List<Shortcut> getShortcuts() {
        return this.shortcuts;
    }
    
    public static class Shortcut
    {
        private String shortcut;
        private String replacement;
        
        public String getShortcut() {
            return this.shortcut;
        }
        
        public String getReplacement() {
            return this.replacement;
        }
        
        public void setShortcut(final String shortcut) {
            this.shortcut = shortcut;
        }
        
        public void setReplacement(final String replacement) {
            this.replacement = replacement;
        }
        
        @ConstructorProperties({ "shortcut", "replacement" })
        public Shortcut(final String shortcut, final String replacement) {
            this.shortcut = shortcut;
            this.replacement = replacement;
        }
    }
}
