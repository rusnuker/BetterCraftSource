// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.ingamechat;

import java.util.Iterator;
import net.labymod.utils.DrawUtils;
import net.labymod.ingamechat.renderer.EnumMouseAction;
import net.labymod.ingamechat.tools.filter.Filters;
import net.labymod.main.LabyMod;
import net.minecraft.util.ResourceLocation;
import net.labymod.core.LabyModCore;
import net.labymod.ingamechat.tools.filter.FilterChatManager;
import net.labymod.ingamechat.renderer.MessageData;
import net.labymod.core.ChatComponent;
import net.labymod.servermanager.ChatDisplayAction;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.LinkedList;
import net.minecraft.client.Minecraft;
import java.util.Map;
import java.util.List;
import net.minecraft.client.settings.GameSettings;
import net.labymod.ingamechat.renderer.ChatRenderer;
import net.labymod.ingamechat.renderer.types.ChatRendererSecond;
import net.labymod.ingamechat.renderer.types.ChatRendererMain;

public class IngameChatManager
{
    public static final String GLOBAL = "Global";
    private ChatRendererMain main;
    private ChatRendererSecond second;
    private ChatRenderer[] chatRenderers;
    private GameSettings gameSettings;
    private List<String> sentMessages;
    private List<String> backendRooms;
    private List<String> visibleRooms;
    private Map<String, Integer> roomsUnread;
    private String selectedRoom;
    
    public IngameChatManager() {
        this.main = new ChatRendererMain(this);
        this.second = new ChatRendererSecond(this);
        this.chatRenderers = new ChatRenderer[] { this.main, this.second };
        this.gameSettings = Minecraft.getMinecraft().gameSettings;
        this.sentMessages = new LinkedList<String>();
        this.backendRooms = new ArrayList<String>();
        this.visibleRooms = new ArrayList<String>();
        this.roomsUnread = new HashMap<String, Integer>();
        this.selectedRoom = "Global";
    }
    
    public MessageData handleSwap(ChatDisplayAction chatDisplayAction, final ChatComponent chatComponent) {
        final Filters.Filter filter = FilterChatManager.getFilterComponent(chatComponent);
        if (chatDisplayAction == ChatDisplayAction.NORMAL && filter != null && filter.isDisplayInSecondChat()) {
            chatDisplayAction = ChatDisplayAction.SWAP;
        }
        if (filter != null && filter.isPlaySound()) {
            LabyModCore.getMinecraft().playSound(new ResourceLocation(filter.getSoundPath()), 1.0f);
        }
        if (chatDisplayAction == ChatDisplayAction.HIDE || (filter != null && filter.isHideMessage())) {
            return null;
        }
        boolean displayInSecondChat = chatDisplayAction == ChatDisplayAction.SWAP;
        if (LabyMod.getSettings().chatPositionRight) {
            displayInSecondChat = !displayInSecondChat;
        }
        return new MessageData(displayInSecondChat, filter);
    }
    
    public void addToSentMessages(final String message) {
        if (this.sentMessages.isEmpty() || !this.sentMessages.get(this.sentMessages.size() - 1).equals(message)) {
            this.sentMessages.add(message);
        }
    }
    
    public void handleMouse(final int mouseX, final int mouseY, final int mouseButton, final EnumMouseAction action) {
        ChatRenderer[] chatRenderers;
        for (int length = (chatRenderers = this.chatRenderers).length, i = 0; i < length; ++i) {
            final ChatRenderer chatRenderer = chatRenderers[i];
            if (chatRenderer.isVisible()) {
                chatRenderer.updateMouse(mouseX, mouseY);
                if (action == EnumMouseAction.CLICKED || action == EnumMouseAction.RENDER) {
                    final boolean isDragging = chatRenderer.resizeDragging != 0;
                    if (chatRenderer.renderHoveringResizeX(chatRenderer.resizeDragging == 1) && !isDragging && action == EnumMouseAction.CLICKED) {
                        chatRenderer.resizeDragging = 1;
                    }
                    else if (chatRenderer.renderHoveringResizeY(chatRenderer.resizeDragging == 2) && !isDragging && action == EnumMouseAction.CLICKED) {
                        chatRenderer.resizeDragging = 2;
                    }
                }
                if (action == EnumMouseAction.RELEASED && chatRenderer.resizeDragging != 0) {
                    chatRenderer.resizeDragging = 0;
                    chatRenderer.save();
                }
                if ((action == EnumMouseAction.DRAGGING || action == EnumMouseAction.CLICKED || action == EnumMouseAction.RENDER) && chatRenderer.resizeDragging != 0) {
                    final boolean horizontal = chatRenderer.resizeDragging == 1;
                    final int min = horizontal ? 40 : 20;
                    final int max = horizontal ? 320 : 180;
                    final float valueX = (chatRenderer.isRightBound() ? (chatRenderer.getChatPositionX() - mouseX) : (mouseX - chatRenderer.getChatPositionX())) - 4.0f;
                    float value = horizontal ? valueX : (chatRenderer.getChatPositionY() - mouseY);
                    if (!horizontal) {
                        value /= chatRenderer.getChatScale();
                    }
                    if (value > max) {
                        value = (float)max;
                    }
                    if (value < min) {
                        value = (float)min;
                    }
                    value -= min;
                    value /= max - min;
                    chatRenderer.updateChatSetting(horizontal ? ChatRenderer.ChatSettingType.WIDTH : ChatRenderer.ChatSettingType.HEIGHT, value);
                    Minecraft.getMinecraft().ingameGUI.getChatGUI().refreshChat();
                }
                if (chatRenderer.isMouseOver() && action == EnumMouseAction.CLICKED && mouseButton == 2 && LabyMod.getSettings().scalableChat) {
                    chatRenderer.moving = true;
                    final DrawUtils draw = LabyMod.getInstance().getDrawUtils();
                    final double screenWidth = draw.getWidth();
                    final double screenHeight = draw.getHeight();
                    chatRenderer.movingClickedX = -(screenWidth / 100.0 * chatRenderer.getChatPercentX()) + mouseX;
                    chatRenderer.movingClickedY = -(screenHeight / 100.0 * chatRenderer.getChatPercentY()) + mouseY;
                }
                if (chatRenderer.moving && (action == EnumMouseAction.DRAGGING || action == EnumMouseAction.RENDER)) {
                    final DrawUtils draw = LabyMod.getInstance().getDrawUtils();
                    final double screenWidth = draw.getWidth();
                    double percentX = 100.0 / screenWidth * (-chatRenderer.movingClickedX + mouseX);
                    final double screenHeight2 = draw.getHeight();
                    double percentY = 100.0 / screenHeight2 * (-chatRenderer.movingClickedY + mouseY);
                    if (percentX > 100.0) {
                        percentX = 100.0;
                    }
                    if (percentX < 0.0) {
                        percentX = 0.0;
                    }
                    if (percentY > 100.0) {
                        percentY = 100.0;
                    }
                    if (percentY < 0.0) {
                        percentY = 0.0;
                    }
                    chatRenderer.updateChatSetting(ChatRenderer.ChatSettingType.X, (float)percentX);
                    chatRenderer.updateChatSetting(ChatRenderer.ChatSettingType.Y, (float)percentY);
                }
                if (action == EnumMouseAction.RELEASED) {
                    chatRenderer.moving = false;
                }
                if (action == EnumMouseAction.CLICKED) {
                    final String selected = chatRenderer.selectHoveredTab();
                    if (selected != null) {
                        this.roomsUnread.put(selected, 0);
                    }
                }
            }
        }
    }
    
    public void updateRooms() {
        final List<String> rooms = new ArrayList<String>();
        for (final Filters.Filter filterComponent : LabyMod.getInstance().getChatToolManager().getFilters()) {
            if (filterComponent.getRoom() != null && !rooms.contains(filterComponent.getRoom())) {
                rooms.add(filterComponent.getRoom());
            }
        }
        if (!rooms.isEmpty()) {
            rooms.add(0, "Global");
        }
        this.backendRooms = rooms;
        this.visibleRooms.clear();
        this.roomsUnread.clear();
        this.visibleRooms.add("Global");
        this.selectedRoom = "Global";
    }
    
    public void handleUnread(final String room) {
        Integer unread = this.roomsUnread.get(room);
        if (unread == null) {
            unread = 0;
        }
        if (!this.selectedRoom.equals(room)) {
            ++unread;
        }
        this.roomsUnread.put(room, unread);
        if (room != null && !this.visibleRooms.contains(room)) {
            this.visibleRooms.add(room);
        }
    }
    
    public ChatRendererMain getMain() {
        return this.main;
    }
    
    public ChatRendererSecond getSecond() {
        return this.second;
    }
    
    public ChatRenderer[] getChatRenderers() {
        return this.chatRenderers;
    }
    
    public GameSettings getGameSettings() {
        return this.gameSettings;
    }
    
    public List<String> getSentMessages() {
        return this.sentMessages;
    }
    
    public List<String> getBackendRooms() {
        return this.backendRooms;
    }
    
    public List<String> getVisibleRooms() {
        return this.visibleRooms;
    }
    
    public Map<String, Integer> getRoomsUnread() {
        return this.roomsUnread;
    }
    
    public void setSelectedRoom(final String selectedRoom) {
        this.selectedRoom = selectedRoom;
    }
    
    public String getSelectedRoom() {
        return this.selectedRoom;
    }
}
