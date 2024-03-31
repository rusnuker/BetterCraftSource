// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.api.permissions;

import net.labymod.main.LabyMod;

public class Permissions
{
    private static PermissionNotifyRenderer permissionNotifyRenderer;
    
    static {
        Permissions.permissionNotifyRenderer = new PermissionNotifyRenderer();
    }
    
    public static boolean isAllowed(final Permission permission) {
        if (LabyMod.getInstance() == null || LabyMod.getInstance().getServerManager() == null) {
            return permission.isDefaultEnabled();
        }
        return LabyMod.getInstance().getServerManager().isAllowed(permission);
    }
    
    public static PermissionNotifyRenderer getPermissionNotifyRenderer() {
        return Permissions.permissionNotifyRenderer;
    }
    
    public enum Permission
    {
        IMPROVED_LAVA("IMPROVED_LAVA", 0, "Improved Lava", false), 
        CROSSHAIR_SYNC("CROSSHAIR_SYNC", 1, "Crosshair sync", false), 
        REFILL_FIX("REFILL_FIX", 2, "Refill fix", false), 
        GUI_ALL("GUI_ALL", 3, "LabyMod GUI", true), 
        GUI_POTION_EFFECTS("GUI_POTION_EFFECTS", 4, "Potion Effects", true), 
        GUI_ARMOR_HUD("GUI_ARMOR_HUD", 5, "Armor HUD", true), 
        GUI_ITEM_HUD("GUI_ITEM_HUD", 6, "Item HUD", true), 
        BLOCKBUILD("BLOCKBUILD", 7, "Blockbuild", true), 
        TAGS("TAGS", 8, "Tags", true), 
        CHAT("CHAT", 9, "Chat features", true), 
        ANIMATIONS("ANIMATIONS", 10, "Animations", true), 
        SATURATION_BAR("SATURATION_BAR", 11, "Saturation bar", true);
        
        private String displayName;
        private boolean defaultEnabled;
        
        public static Permission getPermissionByName(final String name) {
            Permission[] values;
            for (int length = (values = values()).length, i = 0; i < length; ++i) {
                final Permission permission = values[i];
                if (permission.name().equals(name)) {
                    return permission;
                }
            }
            return null;
        }
        
        private Permission(final String s, final int n, final String displayName, final boolean defaultEnabled) {
            this.displayName = displayName;
            this.defaultEnabled = defaultEnabled;
        }
        
        public String getDisplayName() {
            return this.displayName;
        }
        
        public boolean isDefaultEnabled() {
            return this.defaultEnabled;
        }
    }
}
