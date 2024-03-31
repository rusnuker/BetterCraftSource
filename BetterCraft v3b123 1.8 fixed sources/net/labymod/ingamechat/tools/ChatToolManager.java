// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.ingamechat.tools;

import java.util.List;
import net.labymod.main.LabyMod;
import net.lenni0451.eventapi.manager.EventManager;
import net.labymod.ingamechat.tools.autotext.AutoTextListener;
import net.labymod.ingamechat.tools.playermenu.PlayerMenu;
import net.labymod.ingamechat.tools.shortcuts.Shortcuts;
import net.labymod.ingamechat.tools.filter.Filters;
import net.labymod.ingamechat.tools.autotext.AutoTextKeyBinds;
import net.labymod.utils.manager.ConfigManager;
import java.io.File;

public class ChatToolManager
{
    private final File fileAutoText;
    private final File fileFilters;
    private final File fileShortcuts;
    private final File filePlayerMenu;
    private ConfigManager<AutoTextKeyBinds> configAutoText;
    private ConfigManager<Filters> configFilters;
    private ConfigManager<Shortcuts> configShortcuts;
    private ConfigManager<PlayerMenu> configPlayerMenu;
    
    public ChatToolManager() {
        this.fileAutoText = new File("LabyMod/", "autotext.json");
        this.fileFilters = new File("LabyMod/", "filters.json");
        this.fileShortcuts = new File("LabyMod/", "shortcuts.json");
        this.filePlayerMenu = new File("LabyMod/", "playermenu.json");
    }
    
    public void initTools() {
        this.configAutoText = new ConfigManager<AutoTextKeyBinds>(this.fileAutoText, AutoTextKeyBinds.class);
        this.configFilters = new ConfigManager<Filters>(this.fileFilters, Filters.class);
        this.configShortcuts = new ConfigManager<Shortcuts>(this.fileShortcuts, Shortcuts.class);
        this.configPlayerMenu = new ConfigManager<PlayerMenu>(this.filePlayerMenu, PlayerMenu.class);
        EventManager.register((Object)new AutoTextListener());
        LabyMod.getInstance().getIngameChatManager().updateRooms();
    }
    
    public void saveTools() {
        this.configAutoText.save();
        this.configFilters.save();
        this.configShortcuts.save();
        this.configPlayerMenu.save();
        LabyMod.getInstance().getIngameChatManager().updateRooms();
    }
    
    public List<AutoTextKeyBinds.AutoText> getAutoTextKeyBinds() {
        return this.configAutoText.getSettings().getAutoTextKeyBinds();
    }
    
    public List<Filters.Filter> getFilters() {
        return this.configFilters.getSettings().getFilters();
    }
    
    public List<Shortcuts.Shortcut> getShortcuts() {
        return this.configShortcuts.getSettings().getShortcuts();
    }
    
    public List<PlayerMenu.PlayerMenuEntry> getPlayerMenu() {
        return this.configPlayerMenu.getSettings().getPlayerMenuEntries();
    }
}
