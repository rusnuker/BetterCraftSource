// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.gui.elements;

import net.labymod.labyplay.gui.GuiPlayLayout;
import net.labymod.gui.GuiServerList;
import net.minecraft.client.renderer.GlStateManager;
import net.labymod.labyplay.party.model.PartyMember;
import net.labymod.gui.ModGuiMultiplayer;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.labymod.labyconnect.gui.GuiFriendsLayout;
import net.minecraft.client.Minecraft;
import net.labymod.utils.DrawUtils;
import java.util.Iterator;
import net.labymod.utils.ModColor;
import net.labymod.labyconnect.user.ChatUser;
import net.labymod.main.lang.LanguageManager;
import net.labymod.main.LabyMod;
import net.minecraft.client.gui.GuiButton;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import net.labymod.utils.Consumer;
import java.util.List;
import net.minecraft.client.gui.GuiScreen;
import java.util.Map;

public class Tabs
{
    private static Map<String, Class<? extends GuiScreen>[]> guiMap;
    private static String lastOpenScreen;
    private static int hoverMultiplayerTab;
    private static int lastOpenTab;
    private static List<Consumer<Map<String, Class<? extends GuiScreen>[]>>> tabUpdateListener;
    
    static {
        Tabs.guiMap = new LinkedHashMap<String, Class<? extends GuiScreen>[]>();
        Tabs.lastOpenScreen = null;
        Tabs.hoverMultiplayerTab = -1;
        Tabs.lastOpenTab = -1;
        Tabs.tabUpdateListener = new ArrayList<Consumer<Map<String, Class<? extends GuiScreen>[]>>>();
    }
    
    public static void initGuiScreen(final List<GuiButton> buttonList, final GuiScreen screen) {
        Tabs.guiMap.clear();
        for (final Consumer<Map<String, Class<? extends GuiScreen>[]>> consumer : Tabs.tabUpdateListener) {
            consumer.accept(Tabs.guiMap);
        }
        int positionX = 0;
        int index = 0;
        final DrawUtils draw = LabyMod.getInstance().getDrawUtils();
        for (final Map.Entry<String, Class<? extends GuiScreen>[]> guiEntry : Tabs.guiMap.entrySet()) {
            String displayString = LanguageManager.translate(guiEntry.getKey());
            boolean isSelected = false;
            Class[] array;
            for (int length = (array = guiEntry.getValue()).length, i = 0; i < length; ++i) {
                final Class<? extends GuiScreen> guiClass = array[i];
                if (screen != null) {
                    final Class<? extends GuiScreen> screenClass = screen.getClass();
                    if (screenClass.isAssignableFrom(guiClass)) {
                        isSelected = true;
                        break;
                    }
                }
            }
            if (!isSelected && guiEntry.getKey().equals("tab_chat")) {
                int count = 0;
                for (final ChatUser chatUser : LabyMod.getInstance().getLabyConnect().getFriends()) {
                    count += chatUser.getUnreadMessages();
                }
                if (count != 0) {
                    displayString = String.valueOf(displayString) + ModColor.cl('c') + " (" + count + ")";
                }
            }
            final int nameWidth = draw.getStringWidth(displayString);
            final GuiButton button = new GuiButton(100 + index, 5 + positionX, 5, 10 + nameWidth, 20, "test");
            button.enabled = !isSelected;
            buttonList.add(button);
            if (Tabs.lastOpenScreen != null && Tabs.lastOpenScreen.equals(guiEntry.getKey()) && !isSelected) {
                actionPerformedButton(button);
                break;
            }
            positionX += nameWidth + 12;
            ++index;
        }
        if (!LabyMod.getInstance().getLabyPlay().getPartySystem().hasParty() && screen != null) {
            final String displayString2 = LanguageManager.translate("tab_account");
            final int nameWidth2 = draw.getStringWidth(displayString2);
            final GuiButton button2 = new GuiButton(200, 15, 5, 10 + nameWidth2, 20, "Back");
            buttonList.add(button2);
        }
    }
    
    public static void actionPerformedButton(final GuiButton button) {
        int index = 0;
        for (final Map.Entry<String, Class<? extends GuiScreen>[]> guiEntry : Tabs.guiMap.entrySet()) {
            if (button.id == 100 + index) {
                final GuiScreen screen = getGuiScreenByClass(guiEntry.getValue());
                if (screen == null) {
                    break;
                }
                if (LabyMod.getInstance().isInGame()) {
                    Tabs.lastOpenScreen = guiEntry.getKey();
                }
                else {
                    Tabs.lastOpenScreen = null;
                }
                Minecraft.getMinecraft().displayGuiScreen(screen);
                break;
            }
            else {
                ++index;
            }
        }
        if (button.id == 200) {
            Minecraft.getMinecraft().displayGuiScreen(GuiFriendsLayout.prevScreen);
        }
    }
    
    private static GuiScreen getGuiScreenByClass(final Class<? extends GuiScreen>[] guiScreenClasses) {
        for (final Class<? extends GuiScreen> guiClass : guiScreenClasses) {
            if (GuiMultiplayer.class.isAssignableFrom(guiClass)) {
                return new ModGuiMultiplayer(LabyMod.getInstance().isInGame() ? new GuiIngameMenu() : new GuiMultiplayer(new GuiIngameMenu()));
            }
        }
        try {
            return (GuiScreen)guiScreenClasses[0].newInstance();
        }
        catch (final InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static void initMultiplayerTabs(final int tabIndex) {
        if (Tabs.lastOpenTab != -1 && tabIndex != Tabs.lastOpenTab) {
            openMultiPlayerTab(Tabs.lastOpenTab);
        }
    }
    
    public static void drawParty(final int mouseX, final int mouseY, final int width) {
        final int headSize = 10;
        int posX = width - 10 - 2;
        final int posY = 3;
        PartyMember[] members;
        for (int length = (members = LabyMod.getInstance().getLabyPlay().getPartySystem().getMembers()).length, i = 0; i < length; ++i) {
            final PartyMember member = members[i];
            final boolean hover = mouseX > posX && mouseX < posX + 10 && mouseY > 3 && mouseY < 13;
            LabyMod.getInstance().getDrawUtils().drawPlayerHead(member.getUuid(), posX - (hover ? 1 : 0), 3 - (hover ? 1 : 0), 10 + (hover ? 2 : 0));
            if (hover) {
                LabyMod.getInstance().getDrawUtils().drawRightString(member.getName(), width - 3, 16.0);
            }
            posX -= 12;
        }
    }
    
    public static void drawMultiplayerTabs(final int tabIndex, final int mouseX, final int mouseY, final boolean isScrolled, final boolean isIndex0Selected) {
        final DrawUtils draw = LabyMod.getInstance().getDrawUtils();
        GlStateManager.disableLighting();
        Tabs.hoverMultiplayerTab = -1;
        final int space = 3;
        final int paddingWidth = 6;
        int posX = (draw.getWidth() + 3) / 2;
        final int tabY = 28;
        final int tabHeight = 12;
        int index = 0;
        MultiplayerTabs[] values;
        for (int length = (values = MultiplayerTabs.values()).length, i = 0; i < length; ++i) {
            final MultiplayerTabs tab = values[i];
            if (tab.isVisible()) {
                posX -= (draw.getStringWidth(tab.getDisplayName()) + 3 + 6) / 2;
            }
            ++index;
        }
        index = 0;
        MultiplayerTabs[] values2;
        for (int length2 = (values2 = MultiplayerTabs.values()).length, j = 0; j < length2; ++j) {
            final MultiplayerTabs tab = values2[j];
            if (tab.isVisible()) {
                final String displayString = tab.getDisplayName();
                final int tabWidth = draw.getStringWidth(displayString) + 6;
                final boolean hover = mouseX > posX && mouseX < posX + tabWidth && mouseY > 28 && mouseY < 40;
                draw.drawRectangle(posX, 28, posX + tabWidth, 40, Integer.MIN_VALUE);
                if (tabIndex == index) {
                    draw.drawOverlayBackground(posX, 28, tabWidth, 42 + ((isIndex0Selected || !isScrolled) ? 0 : 3), 32);
                    draw.drawGradientShadowTop(28.0, posX, posX + tabWidth);
                    Tabs.lastOpenTab = index;
                }
                else {
                    draw.drawRectangle(posX, 40, posX + tabWidth, 41, ModColor.toRGB(100, 100, 100, 60));
                }
                draw.drawCenteredString(String.valueOf(ModColor.cl((tabIndex == index) ? "f" : (hover ? "7" : "8"))) + displayString, posX + tabWidth / 2, 30.0);
                if (hover) {
                    Tabs.hoverMultiplayerTab = index;
                }
                posX += tabWidth + 3;
            }
            ++index;
        }
    }
    
    public static void mouseClickedMultiplayerTabs(final int index, final int mouseX, final int mouseY) {
        if (index == Tabs.hoverMultiplayerTab || Tabs.hoverMultiplayerTab == -1) {
            return;
        }
        openMultiPlayerTab(Tabs.hoverMultiplayerTab);
    }
    
    private static void openMultiPlayerTab(final int index) {
        GuiScreen lastScreen = Minecraft.getMinecraft().currentScreen;
        if (lastScreen instanceof ModGuiMultiplayer) {
            lastScreen = ((ModGuiMultiplayer)lastScreen).getParentScreen();
        }
        if (lastScreen instanceof GuiServerList) {
            lastScreen = ((GuiServerList)lastScreen).getParentScreen();
        }
        if (lastScreen instanceof GuiPlayLayout) {
            lastScreen = ((GuiPlayLayout)lastScreen).getParentScreen();
        }
        switch (Tabs.lastOpenTab = index) {
            case 0: {
                Minecraft.getMinecraft().displayGuiScreen(new GuiMultiplayer(lastScreen));
                break;
            }
            case 1: {
                Minecraft.getMinecraft().displayGuiScreen(new GuiServerList(lastScreen));
                break;
            }
            case 2: {
                Minecraft.getMinecraft().displayGuiScreen(new GuiPlayLayout(lastScreen));
                break;
            }
        }
    }
    
    public static Map<String, Class<? extends GuiScreen>[]> getGuiMap() {
        return Tabs.guiMap;
    }
    
    public static List<Consumer<Map<String, Class<? extends GuiScreen>[]>>> getTabUpdateListener() {
        return Tabs.tabUpdateListener;
    }
    
    private enum MultiplayerTabs
    {
        MY_SERVER_LIST("MY_SERVER_LIST", 0, "tab_my_server_list"), 
        PUBLIC_SERVER_LIST("PUBLIC_SERVER_LIST", 1, "tab_public_server_list");
        
        private final String langKey;
        
        private MultiplayerTabs(final String s, final int n, final String langKey) {
            this.langKey = langKey;
        }
        
        public String getDisplayName() {
            return LanguageManager.translate(this.langKey);
        }
        
        public boolean isVisible() {
            switch (this.ordinal()) {
                case 0:
                case 2: {
                    return true;
                }
                case 1: {
                    return LabyMod.getSettings().publicServerList;
                }
                default: {
                    return false;
                }
            }
        }
        
        public String getLangKey() {
            return this.langKey;
        }
    }
}
