// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.labyconnect.gui;

import java.util.Iterator;
import net.minecraft.client.renderer.GlStateManager;
import net.labymod.utils.DrawUtils;
import java.io.IOException;
import net.labymod.labyconnect.user.UserStatus;
import net.labymod.utils.ModColor;
import net.labymod.main.LabyMod;
import java.util.TimeZone;
import java.util.Calendar;
import net.minecraft.client.gui.GuiButton;
import net.labymod.main.lang.LanguageManager;
import org.lwjgl.input.Keyboard;
import java.util.HashMap;
import java.text.SimpleDateFormat;
import net.labymod.labyconnect.user.ChatUser;
import java.util.Map;
import java.text.DateFormat;
import net.labymod.gui.elements.Scrollbar;
import net.minecraft.client.gui.GuiScreen;

public class GuiFriendsAbout extends GuiScreen
{
    private GuiScreen lastScreen;
    private Scrollbar scrollbar;
    private DateFormat formatTime;
    private DateFormat formatDate;
    private Map<String, String> infoList;
    private ChatUser player;
    
    public GuiFriendsAbout(final GuiScreen lastScreen, final ChatUser player) {
        this.scrollbar = new Scrollbar(10);
        this.formatTime = new SimpleDateFormat("HH:mm");
        this.formatDate = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        this.infoList = new HashMap<String, String>();
        this.lastScreen = lastScreen;
        this.player = player;
    }
    
    @Override
    public void initGui() {
        super.initGui();
        Keyboard.enableRepeatEvents(true);
        this.buttonList.add(new GuiButton(1, GuiFriendsAbout.width / 2 - 100, GuiFriendsAbout.height - 37, 200, 20, LanguageManager.translate("button_done")));
        this.scrollbar.init();
        this.scrollbar.setPosition(GuiFriendsAbout.width / 2 + 130, 40, GuiFriendsAbout.width / 2 + 134, GuiFriendsAbout.height - 40);
        this.scrollbar.setSpeed(10);
        this.infoList.clear();
        this.formatTime.setCalendar(Calendar.getInstance());
        this.formatTime.setTimeZone(TimeZone.getTimeZone(this.player.getTimeZone()));
        this.infoList.put(LanguageManager.translate("profile_timezone"), LabyMod.getInstance().getDrawUtils().trimStringToWidth(this.formatTime.getTimeZone().getDisplayName(), 120));
        this.infoList.put(LanguageManager.translate("profile_time"), this.formatTime.format(this.formatTime.getCalendar().getTime()));
        if (this.player.getFirstJoined() != 0L) {
            this.infoList.put(LanguageManager.translate("profile_first_joined"), this.formatDate.format(this.player.getFirstJoined()));
        }
        if (this.player.getLastOnline() != 0L) {
            this.infoList.put(LanguageManager.translate("profile_last_online"), this.formatDate.format(this.player.isOnline() ? System.currentTimeMillis() : this.player.getLastOnline()));
        }
        if (this.player.getContactAmount() != 0) {
            this.infoList.put(LanguageManager.translate("profile_friends"), new StringBuilder().append(this.player.getContactAmount()).toString());
        }
        if (!this.player.getStatusMessage().isEmpty()) {
            this.infoList.put(LanguageManager.translate("profile_status_message"), LabyMod.getInstance().getDrawUtils().trimStringToWidth(this.player.getStatusMessage(), 100));
        }
        this.infoList.put(LanguageManager.translate("profile_status"), String.valueOf(ModColor.cl(this.player.getStatus().getChatColor())) + this.player.getStatus().getName());
        if (this.player.getStatus() != UserStatus.OFFLINE) {
            this.infoList.put("Playing", (this.player.getCurrentServerInfo() != null && this.player.getCurrentServerInfo().isServerAvailable()) ? this.player.getCurrentServerInfo().getDisplayAddress() : "No");
        }
    }
    
    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (keyCode == 1) {
            this.mc.displayGuiScreen(this.lastScreen);
            return;
        }
        super.keyTyped(typedChar, keyCode);
    }
    
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.CLICKED);
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        super.actionPerformed(button);
        if (button.id == 1) {
            this.mc.displayGuiScreen(this.lastScreen);
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawBackground(0);
        double listY = 42.0 + this.scrollbar.getScrollY();
        final int entryHeight = 10;
        for (final Map.Entry<String, String> entry : this.infoList.entrySet()) {
            final String key = entry.getKey();
            final String value = entry.getValue();
            final boolean hoverEntry = mouseX > GuiFriendsAbout.width / 2 - 130 && mouseX < GuiFriendsAbout.width / 2 + 130 && mouseY > listY && mouseY < listY + 10.0 + 1.0;
            final int hoverColor = hoverEntry ? 200 : 100;
            LabyMod.getInstance().getDrawUtils();
            DrawUtils.drawRect(GuiFriendsAbout.width / 2 - 130, listY, GuiFriendsAbout.width / 2 + 130, listY + 10.0, ModColor.toRGB(hoverColor, hoverColor, hoverColor, 30));
            LabyMod.getInstance().getDrawUtils().drawString(String.valueOf(ModColor.cl("e")) + key + ":", GuiFriendsAbout.width / 2 - 120, listY + 1.0);
            LabyMod.getInstance().getDrawUtils().drawRightString(value, GuiFriendsAbout.width / 2 + 120, listY + 1.0);
            listY += 11.0;
        }
        LabyMod.getInstance().getDrawUtils().drawString(LanguageManager.translate("profile_title", String.valueOf(ModColor.cl(this.player.getStatus().getChatColor())) + this.player.getGameProfile().getName()), GuiFriendsAbout.width / 2 - 109, 30.0, 1.0);
        GlStateManager.color(1.0f, 1.0f, 1.0f);
        LabyMod.getInstance().getDrawUtils().drawPlayerHead(this.player.getGameProfile(), GuiFriendsAbout.width / 2 - 130, 22, 17);
        this.scrollbar.update(this.infoList.size());
        this.scrollbar.draw();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    @Override
    protected void mouseClickMove(final int mouseX, final int mouseY, final int clickedMouseButton, final long timeSinceLastClick) {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.DRAGGING);
    }
    
    @Override
    protected void mouseReleased(final int mouseX, final int mouseY, final int state) {
        this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.RELEASED);
        super.mouseReleased(mouseX, mouseY, state);
    }
    
    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.scrollbar.mouseInput();
    }
}
