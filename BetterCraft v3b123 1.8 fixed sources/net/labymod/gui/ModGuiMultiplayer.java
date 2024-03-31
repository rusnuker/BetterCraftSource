// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.gui;

import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiYesNo;
import net.labymod.utils.ModColor;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.gui.ServerListEntryNormal;
import net.minecraft.client.Minecraft;
import java.io.IOException;
import java.util.Iterator;
import net.labymod.utils.ReflectionHelper;
import net.labymod.main.LabyMod;
import net.minecraft.client.gui.GuiButton;
import net.labymod.gui.elements.Tabs;
import net.labymod.core.LabyModCore;
import net.minecraft.client.gui.ServerSelectionList;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiMultiplayer;

public class ModGuiMultiplayer extends GuiMultiplayer
{
    private static final String[] serverListSelectorMappings;
    private static final String[] savedServerListMappings;
    private final GuiScreen parentScreen;
    private long lastRefreshed;
    private ServerSelectionList serverSelector;
    private boolean inited;
    private Object serverListSelectorField;
    
    static {
        serverListSelectorMappings = LabyModCore.getMappingAdapter().getServerListSelectorMappings();
        savedServerListMappings = LabyModCore.getMappingAdapter().getSavedServerListMappings();
    }
    
    public ModGuiMultiplayer(final GuiScreen parentScreen) {
        super(parentScreen);
        this.parentScreen = parentScreen;
        this.lastRefreshed = System.currentTimeMillis();
    }
    
    @Override
    public void initGui() {
        super.initGui();
        Tabs.initMultiplayerTabs(0);
        this.lastRefreshed = System.currentTimeMillis();
        this.inited = true;
        this.serverSelector = this.getServerSelectionList();
        Tabs.initGuiScreen(this.buttonList, this);
        if (LabyModCore.getMinecraft().getPlayer() != null) {
            for (final GuiButton button : this.buttonList) {
                if (button.id == 0) {
                    button.enabled = false;
                }
            }
        }
        if (LabyMod.getSettings().publicServerList) {
            final int addSpace = 9;
            this.serverSelector.setDimensions(ModGuiMultiplayer.width, ModGuiMultiplayer.height, 41, ModGuiMultiplayer.height - 64);
        }
        try {
            this.serverListSelectorField = ReflectionHelper.findField(GuiMultiplayer.class, ModGuiMultiplayer.serverListSelectorMappings).get(this);
        }
        catch (final IllegalAccessException e) {
            e.printStackTrace();
            this.serverListSelectorField = null;
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        if (this.serverListSelectorField == null) {
            return;
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
        final boolean isScrolled = !LabyMod.getInstance().isInGame() && this.serverSelector != null && this.serverSelector.getAmountScrolled() == 0;
        final boolean isIndex0Selected = !LabyMod.getInstance().isInGame() && this.serverSelector != null && LabyModCore.getMinecraft().isSelected(this.serverSelector, 0);
        Tabs.drawMultiplayerTabs(0, mouseX, mouseY, isScrolled, isIndex0Selected);
        Tabs.drawParty(mouseX, mouseY, ModGuiMultiplayer.width);
    }
    
    @Override
    public void updateScreen() {
        if (this.inited) {
            try {
                super.updateScreen();
            }
            catch (final Exception error) {
                error.printStackTrace();
            }
        }
        if (LabyMod.getSettings().serverlistLiveView && System.currentTimeMillis() - this.lastRefreshed >= LabyMod.getSettings().serverlistLiveViewInterval * 1000) {
            this.refreshServerListSilently();
            this.lastRefreshed = System.currentTimeMillis();
        }
    }
    
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        Tabs.mouseClickedMultiplayerTabs(0, mouseX, mouseY);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.id == 4) {
            LabyMod.getInstance().getLabyConnect().setViaServerList(false);
        }
        if (button.id != 8) {
            super.actionPerformed(button);
        }
        else {
            this.refreshServerList();
        }
        Tabs.actionPerformedButton(button);
    }
    
    @Override
    public void handleMouseInput() throws IOException {
        if (this.inited) {
            super.handleMouseInput();
        }
    }
    
    @Override
    public void connectToSelected() {
        if (LabyMod.getInstance().isInGame() && !Minecraft.getMinecraft().isSingleplayer() && LabyMod.getSettings().confirmDisconnect) {
            String ip = "";
            final GuiListExtended.IGuiListEntry guilistextended$iguilistentry = (LabyModCore.getMinecraft().getSelectedServerInSelectionList(this.getServerSelector()) < 0) ? null : this.getServerSelector().getListEntry(LabyModCore.getMinecraft().getSelectedServerInSelectionList(this.getServerSelector()));
            if (guilistextended$iguilistentry instanceof ServerListEntryNormal) {
                ip = ((ServerListEntryNormal)guilistextended$iguilistentry).getServerData().serverIP;
            }
            final GuiScreen lastScreen = Minecraft.getMinecraft().currentScreen;
            Minecraft.getMinecraft().displayGuiScreen(new GuiYesNo(new GuiYesNoCallback() {
                @Override
                public void confirmClicked(final boolean result, final int id) {
                    if (result) {
                        ModGuiMultiplayer.this.joinToServer();
                    }
                    else {
                        Minecraft.getMinecraft().displayGuiScreen(lastScreen);
                    }
                }
            }, "Are you sure you want to leave the current server and join this one?", String.valueOf(ModColor.cl("c")) + ip, 0));
        }
        else {
            this.joinToServer();
        }
    }
    
    private void joinToServer() {
        LabyMod.getInstance().getLabyConnect().setViaServerList(false);
        if (LabyModCore.getMinecraft().getWorld() != null) {
            LabyModCore.getMinecraft().getWorld().sendQuittingDisconnectingPacket();
            this.mc.loadWorld(null);
        }
        if (LabyMod.getInstance().isInGame()) {
            LabyMod.getInstance().onQuit();
        }
        super.connectToSelected();
    }
    
    @Override
    public void confirmClicked(final boolean result, final int id) {
        if (id == 5 && result && LabyModCore.getMinecraft().getWorld() != null) {
            LabyModCore.getMinecraft().getWorld().sendQuittingDisconnectingPacket();
            this.mc.loadWorld(null);
        }
        super.confirmClicked(result, id);
    }
    
    public GuiScreen getParentScreen() {
        return this.parentScreen;
    }
    
    private void refreshServerList() {
        try {
            ServerList serverList = null;
            ReflectionHelper.findField(GuiMultiplayer.class, ModGuiMultiplayer.savedServerListMappings).set(this, serverList = new ServerList(this.mc));
            LabyModCore.getMinecraft().updateOnlineServers(this.serverSelector, serverList);
        }
        catch (final Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private void refreshServerListSilently() {
    }
    
    private ServerSelectionList getServerSelectionList() {
        try {
            return (ServerSelectionList)ReflectionHelper.findField(GuiMultiplayer.class, ModGuiMultiplayer.serverListSelectorMappings).get(this);
        }
        catch (final IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public ServerSelectionList getServerSelector() {
        return this.serverSelector;
    }
}
