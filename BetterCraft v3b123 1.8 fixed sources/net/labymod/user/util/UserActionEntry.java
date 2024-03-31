// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.user.util;

import java.awt.datatransfer.Clipboard;
import net.minecraft.client.gui.GuiScreen;
import net.labymod.ingamechat.GuiChatCustom;
import net.minecraft.client.Minecraft;
import net.labymod.main.LabyMod;
import net.labymod.core.LabyModCore;
import net.labymod.api.permissions.Permissions;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.labymod.user.User;
import net.labymod.utils.ModColor;

public class UserActionEntry
{
    private String displayName;
    private EnumActionType type;
    private String value;
    private ActionExecutor executor;
    
    public UserActionEntry(final String displayName, final EnumActionType type, final String value, final ActionExecutor executor) {
        this.displayName = ModColor.createColors(displayName);
        this.type = type;
        this.value = value;
        this.executor = executor;
    }
    
    public void execute(final User user, final EntityPlayer entityPlayer, final NetworkPlayerInfo networkPlayerInfo) {
        if (this.value != null && entityPlayer != null && networkPlayerInfo != null) {
            String string = this.value.replace("{name}", entityPlayer.getName()).replace("{uuid}", entityPlayer.getUniqueID().toString());
            switch (this.type) {
                case CLIPBOARD: {
                    final StringSelection stringSelection = new StringSelection(string);
                    final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                    clipboard.setContents(stringSelection, null);
                    break;
                }
                case RUN_COMMAND: {
                    if (!string.startsWith("/")) {
                        string = "/" + string;
                    }
                    if (Permissions.isAllowed(Permissions.Permission.CHAT)) {
                        LabyModCore.getMinecraft().getPlayer().sendChatMessage(string);
                        break;
                    }
                    LabyMod.getInstance().displayMessageInChat(String.valueOf(ModColor.cl('c')) + "This feature is not allowed on this server!");
                    Minecraft.getMinecraft().displayGuiScreen(new GuiChatCustom(string));
                    break;
                }
                case SUGGEST_COMMAND: {
                    if (!string.startsWith("/")) {
                        string = "/" + string;
                    }
                    Minecraft.getMinecraft().displayGuiScreen(new GuiChatCustom(string));
                    break;
                }
                case OPEN_BROWSER: {
                    LabyMod.getInstance().openWebpage(string, true);
                    break;
                }
            }
        }
        if (this.executor != null) {
            this.executor.execute(user, entityPlayer, networkPlayerInfo);
        }
    }
    
    public String getDisplayName() {
        return this.displayName;
    }
    
    public void setDisplayName(final String displayName) {
        this.displayName = displayName;
    }
    
    public EnumActionType getType() {
        return this.type;
    }
    
    public void setType(final EnumActionType type) {
        this.type = type;
    }
    
    public String getValue() {
        return this.value;
    }
    
    public void setValue(final String value) {
        this.value = value;
    }
    
    public ActionExecutor getExecutor() {
        return this.executor;
    }
    
    public void setExecutor(final ActionExecutor executor) {
        this.executor = executor;
    }
    
    public enum EnumActionType
    {
        NONE("NONE", 0), 
        CLIPBOARD("CLIPBOARD", 1), 
        RUN_COMMAND("RUN_COMMAND", 2), 
        SUGGEST_COMMAND("SUGGEST_COMMAND", 3), 
        OPEN_BROWSER("OPEN_BROWSER", 4);
        
        private EnumActionType(final String s, final int n) {
        }
    }
    
    public interface ActionExecutor
    {
        void execute(final User p0, final EntityPlayer p1, final NetworkPlayerInfo p2);
        
        boolean canAppear(final User p0, final EntityPlayer p1, final NetworkPlayerInfo p2);
    }
}
