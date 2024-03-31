// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.gui;

import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiYesNoCallback;
import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.labymod.main.ModTextures;
import net.labymod.utils.manager.TooltipHelper;
import net.minecraft.client.gui.Gui;
import net.labymod.utils.DrawUtils;
import net.labymod.utils.ModColor;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.ExecutorService;
import net.labymod.utils.Consumer;
import net.labymod.core.LabyModCore;
import net.labymod.main.LabyMod;
import net.labymod.main.lang.LanguageManager;
import net.labymod.gui.elements.Tabs;
import java.util.Iterator;
import java.util.Set;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import java.util.Scanner;
import net.labymod.main.Source;
import java.net.URL;
import java.net.HttpURLConnection;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.HashMap;
import net.labymod.gui.elements.CheckBox;
import net.minecraft.client.gui.GuiButton;
import net.labymod.core.ServerPingerData;
import net.labymod.gui.elements.Scrollbar;
import net.labymod.utils.ServerData;
import java.util.List;
import net.labymod.utils.manager.ServerInfoRenderer;
import java.util.Map;
import net.minecraft.client.gui.GuiScreen;

public class GuiServerList extends GuiScreen
{
    private static Map<String, ServerInfoRenderer> serverInfoRenderers;
    private static List<ServerInfoRenderer> serverInfoRenderersSorted;
    private static List<ServerData> publicServerListEntrys;
    private static boolean initialized;
    private static boolean partnersOnly;
    private GuiScreen parentScreen;
    private Scrollbar scrollbar;
    private long lastServerDataUpdate;
    private ServerPingerData joinOnServerData;
    private ServerPingerData saveServerData;
    private ServerPingerData hoverServerData;
    private ServerPingerData selectedServerData;
    private int selectedServerId;
    private long lastTimeSelected;
    private GuiButton buttonConnect;
    private GuiButton buttonSaveServer;
    private CheckBox checkBox;
    private int alternativeDragClickY;
    
    static {
        GuiServerList.serverInfoRenderers = new HashMap<String, ServerInfoRenderer>();
        GuiServerList.serverInfoRenderersSorted = new ArrayList<ServerInfoRenderer>();
        GuiServerList.publicServerListEntrys = new ArrayList<ServerData>();
    }
    
    public GuiServerList(final GuiScreen parentScreen) {
        this.alternativeDragClickY = -1;
        this.parentScreen = parentScreen;
        if (GuiServerList.initialized) {
            return;
        }
        GuiServerList.initialized = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                final JsonParser jsonParser = new JsonParser();
                try {
                    final HttpURLConnection connection = (HttpURLConnection)new URL("http://dl.labymod.net/public_servers.json").openConnection();
                    connection.setRequestProperty("User-Agent", Source.getUserAgent());
                    connection.setReadTimeout(5000);
                    connection.setConnectTimeout(2000);
                    connection.connect();
                    int index = 0;
                    final int responseCode = connection.getResponseCode();
                    if (responseCode / 100 == 2) {
                        String jsonString = "";
                        final Scanner scanner = new Scanner(connection.getInputStream());
                        while (scanner.hasNext()) {
                            jsonString = String.valueOf(jsonString) + scanner.next();
                        }
                        scanner.close();
                        final JsonElement jsonElement = jsonParser.parse(jsonString);
                        final JsonObject servers = jsonElement.getAsJsonObject().get("servers").getAsJsonObject();
                        final Set<Map.Entry<String, JsonElement>> entrySet = servers.entrySet();
                        for (final Map.Entry<String, JsonElement> entry : entrySet) {
                            final String address = entry.getKey();
                            final JsonObject data = entry.getValue().getAsJsonObject();
                            final boolean partner = data.has("partner") && data.get("partner").getAsBoolean();
                            final ServerData serverData = new ServerData(address, 25565, partner);
                            serverData.setIndex(index);
                            GuiServerList.publicServerListEntrys.add(serverData);
                            ++index;
                        }
                    }
                }
                catch (final Exception error) {
                    error.printStackTrace();
                }
                GuiServerList.this.refreshServerList();
                GuiServerList.this.sortServerList();
            }
        }).start();
    }
    
    @Override
    public void initGui() {
        Tabs.initMultiplayerTabs(1);
        (this.scrollbar = new Scrollbar(36)).setPosition(GuiServerList.width / 2 + 150 + 4, 41, GuiServerList.width / 2 + 150 + 4 + 6, GuiServerList.height - 40);
        this.scrollbar.setSpeed(20);
        this.scrollbar.setSpaceBelow(5);
        Tabs.initGuiScreen(this.buttonList, this);
        this.refreshServerList();
        this.sortServerList();
        this.lastServerDataUpdate = System.currentTimeMillis() + 5000L;
        this.buttonList.add(this.buttonSaveServer = new GuiButton(6, GuiServerList.width / 2 - 50 - 5 - 100, GuiServerList.height - 30, 100, 20, LanguageManager.translate("button_save_server")));
        this.buttonList.add(this.buttonConnect = new GuiButton(5, GuiServerList.width / 2 - 50, GuiServerList.height - 30, 100, 20, LanguageManager.translate("button_connect")));
        final GuiButton cancelButton = new GuiButton(4, GuiServerList.width / 2 + 50 + 5, GuiServerList.height - 30, 100, 20, LanguageManager.translate("button_cancel"));
        if (LabyMod.getInstance().isInGame()) {
            cancelButton.enabled = false;
        }
        this.buttonList.add(cancelButton);
        this.checkBox = new CheckBox("", GuiServerList.partnersOnly ? CheckBox.EnumCheckBoxValue.ENABLED : CheckBox.EnumCheckBoxValue.DISABLED, null, GuiServerList.width / 2 - 180, GuiServerList.height - 28, 19, 19);
        super.initGui();
    }
    
    private void refreshServerList() {
        final boolean firstInit = GuiServerList.serverInfoRenderers.isEmpty();
        for (final ServerData entry : GuiServerList.publicServerListEntrys) {
            if (firstInit) {
                final ServerPingerData dummyPingerData = new ServerPingerData(entry.getIp(), 0L);
                dummyPingerData.setPingToServer(-1L);
                dummyPingerData.setMotd(LanguageManager.translate("status_pinging_server"));
                dummyPingerData.setPinging(true);
                dummyPingerData.setVersion(Source.ABOUT_MC_PROTOCOL_VERSION);
                final ServerInfoRenderer serverInfoRenderer = new ServerInfoRenderer(entry.getIp(), entry.getIp(), dummyPingerData).setIndex(entry.getIndex());
                serverInfoRenderer.setLabymodServerData(entry);
                GuiServerList.serverInfoRenderers.put(entry.getIp(), serverInfoRenderer);
            }
            final long pingStartTime = System.currentTimeMillis();
            LabyModCore.getServerPinger().pingServer(null, pingStartTime, String.valueOf(entry.getIp()) + ":" + entry.getPort(), new Consumer<ServerPingerData>() {
                @Override
                public void accept(final ServerPingerData accepted) {
                    if (accepted != null && accepted.getTimePinged() != pingStartTime) {
                        return;
                    }
                    final ServerInfoRenderer preInfo = GuiServerList.serverInfoRenderers.get(entry.getIp());
                    if (preInfo != null) {
                        if (accepted == null) {
                            if (!preInfo.canReachServer()) {
                                preInfo.setHidden(true);
                            }
                        }
                        else {
                            preInfo.init(entry.getIp(), entry.getIp(), accepted);
                            preInfo.setHidden(false);
                        }
                    }
                }
            });
        }
        if (firstInit) {
            this.sortServerList();
        }
    }
    
    private void sortServerList() {
        final List<ServerInfoRenderer> list = new ArrayList<ServerInfoRenderer>();
        int count = 0;
        final String versionSplit = Source.ABOUT_MC_VERSION.replaceFirst("\\.", "/");
        final String majorVersion = (versionSplit.contains(".") ? versionSplit.split("\\.")[0] : versionSplit).replaceAll("/", ".");
        for (final ServerInfoRenderer serverInfoRenderer : GuiServerList.serverInfoRenderers.values()) {
            if ((serverInfoRenderer.isClientOutOfDate() || serverInfoRenderer.isServerOutOfDate()) && !serverInfoRenderer.getServerData().getMotd().contains(majorVersion) && !serverInfoRenderer.getServerData().getGameVersion().contains(majorVersion)) {
                continue;
            }
            if (serverInfoRenderer.getServerData() != null && serverInfoRenderer.canReachServer() && !serverInfoRenderer.getServerData().isPinging() && serverInfoRenderer.getServerData().getCurrentPlayers() < 30) {
                continue;
            }
            if (serverInfoRenderer.isHidden()) {
                continue;
            }
            final boolean partner = serverInfoRenderer.getLabymodServerData() != null && serverInfoRenderer.getLabymodServerData().isPartner();
            if (!partner && GuiServerList.partnersOnly) {
                continue;
            }
            list.add(serverInfoRenderer);
            if (++count >= 50) {
                break;
            }
        }
        Collections.sort(list, new Comparator<ServerInfoRenderer>() {
            @Override
            public int compare(final ServerInfoRenderer a, final ServerInfoRenderer b) {
                return a.getIndex() - b.getIndex();
            }
        });
        GuiServerList.serverInfoRenderersSorted = list;
        this.scrollbar.update(GuiServerList.serverInfoRenderersSorted.size());
    }
    
    @Override
    public void updateScreen() {
        if (LabyMod.getSettings().serverlistLiveView && this.lastServerDataUpdate < System.currentTimeMillis()) {
            this.lastServerDataUpdate = System.currentTimeMillis() + 15000L;
            this.sortServerList();
            this.refreshServerList();
        }
        super.updateScreen();
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        final DrawUtils draw = LabyMod.getInstance().getDrawUtils();
        draw.drawAutoDimmedBackground(this.scrollbar.getScrollY());
        this.joinOnServerData = null;
        this.saveServerData = null;
        this.hoverServerData = null;
        this.selectedServerId = -1;
        int id = 1;
        final int midX = GuiServerList.width / 2;
        final int entryWidth = 300;
        final int entryHeight = 36;
        double posY = 45.0 + this.scrollbar.getScrollY();
        for (final ServerInfoRenderer serverInfoRenderer : GuiServerList.serverInfoRenderersSorted) {
            final boolean partner = serverInfoRenderer.getLabymodServerData() != null && serverInfoRenderer.getLabymodServerData().isPartner();
            if (this.selectedServerData != null && this.selectedServerData.getIpAddress().equals(serverInfoRenderer.getServerData().getIpAddress())) {
                DrawUtils.drawRect(midX - 150 - 2, posY - 2.0, midX + 150 + 2, posY + 36.0 - 2.0, ModColor.toRGB(partner ? 155 : 128, partner ? 155 : 128, partner ? 0 : 128, 255));
                DrawUtils.drawRect(midX - 150 - 1, posY - 1.0, midX + 150 + 1, posY + 36.0 - 3.0, ModColor.toRGB(0, 0, 0, 255));
                this.selectedServerId = id;
            }
            else if (partner) {
                final int color = ModColor.toRGB(100, 80, 0, 30);
                final int x = midX - 150 - 1;
                final int y = (int)posY - 1;
                final int x2 = midX + 150 + 2;
                final int y2 = (int)posY + 36 - 3;
                Gui.drawRect(x + 1, y + 1, x2 - 1, y2 - 1, color);
                Gui.drawRect(x, y + 2, x + 1, y2 - 2, color);
                Gui.drawRect(x2 - 1, y + 2, x2, y2 - 2, color);
                Gui.drawRect(x + 2, y, x2 - 2, y + 1, color);
                Gui.drawRect(x + 2, y2 - 1, x2 - 2, y2, color);
            }
            if (mouseY > 41 && mouseY < GuiServerList.height - 40) {
                if (serverInfoRenderer.drawJoinServerButton(midX - 150, (int)posY, 305, 36, mouseX, mouseY)) {
                    this.joinOnServerData = serverInfoRenderer.getServerData();
                }
                if (serverInfoRenderer.drawSaveServerButton(midX - 150, (int)posY, 305, 36, mouseX, mouseY)) {
                    this.saveServerData = serverInfoRenderer.getServerData();
                    TooltipHelper.getHelper().pointTooltip(mouseX, mouseY, 300L, LanguageManager.translate("button_save_in_my_server_list"));
                }
                if (mouseX > midX - 150 && mouseX < midX + 150 + 5 && mouseY > posY && mouseY < posY + 36.0) {
                    this.hoverServerData = serverInfoRenderer.getServerData();
                }
            }
            String number = "#" + id;
            if (partner) {
                final double x3 = midX - 150 - draw.getStringWidth(number) - 12;
                final boolean hover = mouseX > x3 && mouseX < x3 + 10.0 && mouseY > posY && mouseY < posY + 10.0;
                draw.bindTexture(ModTextures.MISC_PARTNER_CROWN);
                draw.drawTexture(x3 - (hover ? 2 : 0), posY - (hover ? 2 : 0), 255.0, 255.0, 10 + (hover ? 4 : 0), 10 + (hover ? 4 : 0), 1.1f);
                if (hover) {
                    TooltipHelper.getHelper().pointTooltip(mouseX, mouseY, 0L, String.valueOf(ModColor.cl('e')) + LanguageManager.translate("partner_server_list"));
                }
                number = String.valueOf(ModColor.cl('e')) + number;
            }
            draw.drawRightString(number, midX - 150 - 3, posY + 2.0, 0.7);
            posY += 36.0;
            ++id;
        }
        this.buttonConnect.enabled = (this.selectedServerData != null);
        this.buttonSaveServer.enabled = (this.selectedServerData != null);
        draw.drawOverlayBackground(0, 41);
        draw.drawGradientShadowTop(41.0, 0.0, GuiServerList.width);
        draw.drawOverlayBackground(GuiServerList.height - 40, GuiServerList.height);
        draw.drawGradientShadowBottom(GuiServerList.height - 40, 0.0, GuiServerList.width);
        if (GuiServerList.serverInfoRenderersSorted.isEmpty()) {
            draw.drawCenteredString(LanguageManager.translate("status_loading_server_list"), GuiServerList.width / 2, GuiServerList.height / 2);
        }
        final int qiX = GuiServerList.width - 12;
        final int qiY = 44;
        final int qiW = 8;
        final int qiH = 12;
        final boolean hover2 = mouseX > qiX && mouseY > 44 && mouseX < qiX + 8 && mouseY < 56;
        Minecraft.getMinecraft().getTextureManager().bindTexture(ModTextures.BUTTON_QUESTION);
        draw.drawTexture(qiX, 44.0, hover2 ? 135.0 : 0.0, 0.0, 122.0, 255.0, 8.0, 12.0);
        if (hover2) {
            final String string = String.valueOf(ModColor.cl("9")) + ModColor.cl("n") + LanguageManager.translate("information") + "\n" + ModColor.cl("r") + LanguageManager.translate("info_public_server_list");
            TooltipHelper.getHelper().pointTooltip(mouseX, mouseY, 0L, (String[])draw.listFormattedStringToWidth(string, GuiServerList.width / 4).toArray());
        }
        this.scrollbar.draw();
        super.drawScreen(mouseX, mouseY, partialTicks);
        final boolean isScrolled = !LabyMod.getInstance().isInGame() && this.scrollbar.getScrollY() == 0.0;
        final boolean isIndex0Selected = !LabyMod.getInstance().isInGame() && this.selectedServerId == 1;
        Tabs.drawMultiplayerTabs(1, mouseX, mouseY, isScrolled, isIndex0Selected);
        Tabs.drawParty(mouseX, mouseY, GuiServerList.width);
        draw.bindTexture(ModTextures.MISC_PARTNER_CROWN);
        draw.drawTexture(GuiServerList.width / 2 - 176, GuiServerList.height - 36, 255.0, 255.0, 10.0, 10.0, 1.1f);
        this.checkBox.drawCheckbox(mouseX, mouseY);
    }
    
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        Tabs.mouseClickedMultiplayerTabs(1, mouseX, mouseY);
        if (this.hoverServerData != null) {
            if (this.selectedServerData != null && this.selectedServerData.getIpAddress().equals(this.hoverServerData.getIpAddress()) && this.lastTimeSelected + 400L > System.currentTimeMillis()) {
                this.joinOnServerData = this.selectedServerData;
            }
            this.selectedServerData = this.hoverServerData;
            this.lastTimeSelected = System.currentTimeMillis();
            this.alternativeDragClickY = (int)(this.scrollbar.getScrollY() - mouseY);
        }
        if (this.joinOnServerData != null) {
            this.joinServer();
        }
        if (this.saveServerData != null) {
            this.saveServer(this.selectedServerData);
        }
        this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.CLICKED);
        if (this.checkBox != null && this.checkBox.mouseClicked(mouseX, mouseY, mouseButton)) {
            GuiServerList.partnersOnly = (this.checkBox.getValue() == CheckBox.EnumCheckBoxValue.ENABLED);
            this.sortServerList();
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    private void joinServer() {
        final ServerPingerData serverPingerData = this.selectedServerData;
        if (serverPingerData != null) {
            if (LabyMod.getInstance().isInGame() && !Minecraft.getMinecraft().isSingleplayer() && LabyMod.getSettings().confirmDisconnect) {
                final GuiScreen lastScreen = Minecraft.getMinecraft().currentScreen;
                Minecraft.getMinecraft().displayGuiScreen(new GuiYesNo(new GuiYesNoCallback() {
                    @Override
                    public void confirmClicked(final boolean result, final int id) {
                        if (result) {
                            LabyMod.getInstance().getLabyConnect().setViaServerList(true);
                            LabyMod.getInstance().connectToServer(serverPingerData.getIpAddress());
                        }
                        else {
                            Minecraft.getMinecraft().displayGuiScreen(lastScreen);
                        }
                    }
                }, LanguageManager.translate("warning_server_disconnect"), String.valueOf(ModColor.cl("c")) + serverPingerData.getIpAddress(), 0));
            }
            else {
                LabyMod.getInstance().getLabyConnect().setViaServerList(true);
                LabyMod.getInstance().connectToServer(serverPingerData.getIpAddress());
            }
        }
    }
    
    @Override
    protected void mouseClickMove(final int mouseX, final int mouseY, final int clickedMouseButton, final long timeSinceLastClick) {
        this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.DRAGGING);
        if (this.alternativeDragClickY != -1) {
            this.scrollbar.setScrollY(this.alternativeDragClickY + mouseY);
            this.scrollbar.checkOutOfBorders();
        }
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    }
    
    @Override
    protected void mouseReleased(final int mouseX, final int mouseY, final int state) {
        super.mouseReleased(mouseX, mouseY, state);
        this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.RELEASED);
        this.alternativeDragClickY = -1;
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        super.actionPerformed(button);
        if (button.id == 4) {
            if (this.parentScreen instanceof ModGuiMultiplayer) {
                Minecraft.getMinecraft().displayGuiScreen(((ModGuiMultiplayer)this.parentScreen).getParentScreen());
            }
            else {
                Minecraft.getMinecraft().displayGuiScreen(this.parentScreen);
            }
        }
        if (button.id == 6) {
            this.saveServer(this.selectedServerData);
        }
        if (button.id == 5 && this.selectedServerData != null) {
            this.joinServer();
        }
        Tabs.actionPerformedButton(button);
    }
    
    private void saveServer(final ServerPingerData selectedServerData) {
        if (this.parentScreen instanceof ModGuiMultiplayer) {
            final ModGuiMultiplayer gmp = (ModGuiMultiplayer)this.parentScreen;
            gmp.getServerList().addServerData(selectedServerData.toMCServerData());
            gmp.getServerList().saveServerList();
            LabyModCore.getMinecraft().updateServerList(gmp.getServerSelector(), gmp.getServerList());
            gmp.getServerSelector().scrollBy(Integer.MAX_VALUE);
            gmp.getServerSelector().setSelectedSlotIndex(gmp.getServerList().countServers() - 1);
            Minecraft.getMinecraft().displayGuiScreen(this.parentScreen);
        }
        else {
            final ServerList serverList = new ServerList(Minecraft.getMinecraft());
            serverList.loadServerList();
            serverList.addServerData(selectedServerData.toMCServerData());
            serverList.saveServerList();
        }
    }
    
    @Override
    public void handleMouseInput() throws IOException {
        this.scrollbar.mouseInput();
        super.handleMouseInput();
    }
    
    public GuiScreen getParentScreen() {
        return this.parentScreen;
    }
}
