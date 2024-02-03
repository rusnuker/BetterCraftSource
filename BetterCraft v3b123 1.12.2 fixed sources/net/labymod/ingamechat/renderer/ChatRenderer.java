// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.ingamechat.renderer;

import net.minecraft.client.gui.GuiChat;
import java.util.Iterator;
import net.labymod.main.lang.LanguageManager;
import net.labymod.utils.ModColor;
import java.awt.Color;
import net.labymod.utils.DrawUtils;
import net.minecraft.client.gui.Gui;
import net.labymod.core.LabyModCore;
import net.labymod.main.LabyMod;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.gui.GuiScreen;
import net.labymod.ingamechat.GuiChatCustom;
import net.minecraft.client.Minecraft;
import java.util.ArrayList;
import java.util.LinkedList;
import net.labymod.ingamechat.IngameChatManager;
import java.util.List;

public abstract class ChatRenderer
{
    private List<ChatLine> backendComponents;
    private List<ChatLine> chatLines;
    private int scrollPos;
    public int lastMouseX;
    public int lastMouseY;
    protected int lastRenderedLinesCount;
    private final IngameChatManager manager;
    private final boolean rightBound;
    private final boolean tabMenu;
    public int resizeDragging;
    public boolean moving;
    public double movingClickedX;
    public double movingClickedY;
    private String hoveringRoom;
    private long animationShift;
    
    public ChatRenderer(final IngameChatManager manager, final boolean tabMenu, final boolean rightBound) {
        this.backendComponents = new LinkedList<ChatLine>();
        this.chatLines = new ArrayList<ChatLine>();
        this.lastRenderedLinesCount = 0;
        this.resizeDragging = 0;
        this.moving = false;
        this.hoveringRoom = null;
        this.animationShift = 0L;
        this.manager = manager;
        this.tabMenu = tabMenu;
        this.rightBound = rightBound;
    }
    
    public boolean isVisible() {
        final GuiScreen currentScreen = Minecraft.getMinecraft().currentScreen;
        return !this.rightBound || currentScreen == null || GuiChatCustom.activeTab == -1;
    }
    
    public void renderChat(final int updateCounter) {
        if (this.chatLines.size() == 0 || !this.isVisible()) {
            return;
        }
        GlStateManager.pushMatrix();
        final DrawUtils draw = LabyMod.getInstance().getDrawUtils();
        final int fontHeight = LabyModCore.getMinecraft().getFontRenderer().FONT_HEIGHT;
        final float scale = this.getChatScale();
        final int chatLineCount = this.getLineCount();
        final boolean chatOpen = this.isChatOpen();
        final float opacity = this.getChatOpacity() * 0.9f + 0.1f;
        final int width = this.getVisualWidth() + 1;
        int visibleMessages = 0;
        double totalMessages = 0.0;
        final double animationSpeed = 20.0;
        final float lineHeight = 10.0f * scale;
        double shift = 0.0;
        if (LabyMod.getSettings().chatAnimation) {
            shift = (System.currentTimeMillis() - lineHeight * 20.0 - this.animationShift) / 20.0;
            if (shift > 0.0) {
                shift = 0.0;
            }
        }
        final double posX = this.getChatPositionX() - (this.rightBound ? width : 0) * scale;
        final double posY = this.getChatPositionY() - shift;
        GlStateManager.translate(posX, posY, 0.0);
        GlStateManager.scale(scale, scale, 1.0f);
        if (!this.isChatOpen()) {
            this.scrollPos = 0;
        }
        int i = -this.scrollPos;
        for (final ChatLine chatline : this.chatLines) {
            if (chatline == null) {
                continue;
            }
            if (!chatline.getRoom().equals(this.manager.getSelectedRoom())) {
                continue;
            }
            final boolean firstLine = i == -this.scrollPos;
            final boolean lastLine = i == chatLineCount;
            ++i;
            ++totalMessages;
            if (!lastLine || shift == 0.0) {
                if (i > chatLineCount) {
                    continue;
                }
                if (i <= 0) {
                    continue;
                }
            }
            final int updateCounterDifference = Minecraft.getMinecraft().ingameGUI.getUpdateCounter() - chatline.getUpdateCounter();
            if (updateCounterDifference >= 200 && !chatOpen) {
                continue;
            }
            ++visibleMessages;
            int alpha = 255;
            if (!chatOpen) {
                double percent = updateCounterDifference / 200.0;
                percent = 1.0 - percent;
                percent *= 10.0;
                percent = LabyModCore.getMath().clamp_double(percent, 0.0, 1.0);
                percent *= percent;
                alpha = (int)(255.0 * percent);
            }
            if (shift != 0.0 && firstLine) {
                final double fadeIn = 25.5 * -shift;
                alpha = (int)(255.0 - fadeIn);
            }
            if (shift != 0.0 && lastLine) {
                final double fadeIn = 25.5 * -shift;
                alpha = (int)fadeIn;
            }
            alpha *= (int)opacity;
            if (alpha <= 3) {
                continue;
            }
            final int x = 0;
            final int y = (i - 1) * -9;
            if (!LabyMod.getSettings().fastChat || chatline.getHighlightColor() != null) {
                Gui.drawRect(0, y - fontHeight, 0 + width, y, (chatline.getHighlightColor() != null) ? ((int)chatline.getHighlightColor()) : (alpha / 2 << 24));
            }
            GlStateManager.enableBlend();
            draw.drawStringWithShadow(chatline.getMessage(), 1.0, y - 8, 16777215 + (alpha << 24));
            GlStateManager.disableAlpha();
            GlStateManager.disableBlend();
            this.lastRenderedLinesCount = visibleMessages;
        }
        if (chatOpen) {
            final double totalHeight = totalMessages * fontHeight;
            final double visibleHeight = visibleMessages * fontHeight;
            final double yStart = this.scrollPos * visibleHeight / totalMessages;
            final double yEnd = visibleHeight * visibleHeight / totalHeight;
            final double xStart = this.rightBound ? width : -1.0;
            final double xEnd = this.rightBound ? (width + 1) : 0.0;
            if (totalHeight != visibleHeight) {
                DrawUtils.drawRect(xStart, -yStart, xEnd, -yStart - yEnd, -1);
            }
            if (this.moving) {
                final double midY = yStart - visibleHeight / 2.0;
                final double x2 = -this.getChatPositionX() / scale;
                final double y2 = -this.getChatPositionY() / scale;
                final float percentX = this.getChatPercentX();
                final float percentY = this.getChatPercentY();
                if (this.isRightBound()) {
                    if (percentX < 98.0f) {
                        DrawUtils.drawRect(this.getVisualWidth(), midY, x2 + draw.getWidth() / scale + width, midY + 1.0, Color.YELLOW.getRGB());
                        draw.drawRightString(String.valueOf(ModColor.cl('e')) + (int)(100.0f - percentX) + "%", x2 + (draw.getWidth() - 1) / scale + width, midY - 10.0);
                    }
                }
                else if (percentX > 2.0f) {
                    DrawUtils.drawRect(x2, midY, 0.0, midY + 1.0, Color.YELLOW.getRGB());
                    draw.drawString(String.valueOf(ModColor.cl('e')) + (int)percentX + "%", x2 + 1.0, midY - 10.0);
                }
                if (percentY > 50.0f) {
                    if (percentY < 98.0f) {
                        DrawUtils.drawRect(width / 2.0, 0.0, width / 2.0 + 1.0, y2 + (draw.getHeight() - 28) / scale, Color.YELLOW.getRGB());
                        draw.drawString(String.valueOf(ModColor.cl('e')) + (int)(100.0f - percentY) + "%", width / 2.0 + 4.0, y2 + (draw.getHeight() - 28) / scale - 7.0);
                    }
                }
                else if (percentY > 2.0f) {
                    DrawUtils.drawRect(width / 2.0, y2, width / 2.0 + 1.0, -visibleHeight, Color.YELLOW.getRGB());
                    draw.drawString(String.valueOf(ModColor.cl('e')) + (int)percentY + "%", width / 2.0 + 4.0, y2 + 2.0);
                }
            }
        }
        GlStateManager.popMatrix();
        if (LabyMod.getSettings().chatFilter && this.tabMenu && LabyMod.getInstance().getIngameChatManager().getVisibleRooms().size() > 1) {
            this.hoveringRoom = null;
            double roomX = chatOpen ? 2.0 : 1.0;
            final double roomY = chatOpen ? (draw.getHeight() - 27) : ((double)(draw.getHeight() - 9));
            for (String roomName : LabyMod.getInstance().getIngameChatManager().getVisibleRooms()) {
                if (roomName == null) {
                    continue;
                }
                Integer unread = LabyMod.getInstance().getIngameChatManager().getRoomsUnread().get(roomName);
                if (unread == null) {
                    unread = 0;
                }
                if (!chatOpen && unread <= 0) {
                    continue;
                }
                final int notificationColor = Integer.MIN_VALUE;
                final boolean selected = roomName.equals(this.manager.getSelectedRoom());
                if (roomName.equals("Global")) {
                    roomName = LanguageManager.translate("ingame_chat_room_global");
                }
                final String string = (unread > 0) ? (String.valueOf(roomName) + ModColor.cl("a") + " [" + unread + "]") : roomName;
                final double roomWidth = draw.getStringWidth(string) * 0.7 + 2.0;
                final double roomHeight = 8.0;
                final boolean hover = this.lastMouseX - 2 >= roomX && this.lastMouseX - 2 < roomX + roomWidth && posY - this.lastMouseY < 0.0 && posY - this.lastMouseY > -8.0;
                if (!LabyMod.getSettings().fastChat) {
                    DrawUtils.drawRect(roomX, roomY - (selected ? 1 : -1), roomX + roomWidth, roomY + 8.0, Integer.MIN_VALUE);
                }
                draw.drawString(String.valueOf(ModColor.cl(hover ? "e" : (selected ? "f" : "7"))) + string, roomX + 1.0, roomY + 2.0, 0.7);
                roomX += roomWidth + 2.0;
                if (!hover) {
                    continue;
                }
                this.hoveringRoom = roomName;
            }
        }
    }
    
    public void deleteChatLine(final int id) {
        Iterator<ChatLine> iterator = this.chatLines.iterator();
        while (iterator.hasNext()) {
            final ChatLine chatline = iterator.next();
            if (chatline.getChatLineId() == id) {
                iterator.remove();
            }
        }
        iterator = this.chatLines.iterator();
        while (iterator.hasNext()) {
            final ChatLine chatline2 = iterator.next();
            if (chatline2.getChatLineId() == id) {
                iterator.remove();
                break;
            }
        }
    }
    
    public void addChatLine(final String message, final boolean secondChat, final String room, final Object component, final int updateCounter, final int chatLineId, final Integer highlightColor, final boolean refresh) {
        this.chatLines.add(0, new ChatLine(message, secondChat, room, component, updateCounter, chatLineId, highlightColor));
        if (!refresh) {
            this.animationShift = System.currentTimeMillis();
        }
    }
    
    public void checkLimit() {
        this.reduce(this.chatLines);
    }
    
    public void handleBackendLines(final String message, final boolean secondChat, final String room, final Object component, final int chatLineId, final int updateCounter, final Integer highlightColor) {
        this.backendComponents.add(0, new ChatLine(message, secondChat, room, component, updateCounter, chatLineId, highlightColor));
        this.reduce(this.backendComponents);
    }
    
    private void reduce(final List<ChatLine> list) {
        final int lineLimit = LabyMod.getSettings().chatLineLimit;
        if (list.size() > lineLimit) {
            for (final String roomName : LabyMod.getInstance().getIngameChatManager().getVisibleRooms()) {
                final Iterator<ChatLine> it = list.iterator();
                int count = 0;
                while (it.hasNext()) {
                    final ChatLine chatLine = it.next();
                    if (chatLine.getRoom() != null && chatLine.getRoom().equals(roomName)) {
                        ++count;
                    }
                    if (count > lineLimit) {
                        it.remove();
                    }
                }
            }
        }
    }
    
    public void clearChatMessages(final boolean clearBackend) {
        this.chatLines.clear();
        this.scrollPos = 0;
        if (clearBackend) {
            this.backendComponents.clear();
        }
        if (clearBackend) {
            this.manager.getRoomsUnread().clear();
            this.manager.getVisibleRooms().clear();
            this.manager.getVisibleRooms().add("Global");
            this.manager.setSelectedRoom("Global");
        }
    }
    
    public void scroll(final int amount) {
        this.scrollPos += amount;
        int totalMessages = 0;
        for (final ChatLine chatline : this.chatLines) {
            if (chatline.getRoom().equals(this.manager.getSelectedRoom())) {
                ++totalMessages;
            }
        }
        if (this.scrollPos > totalMessages - this.getLineCount()) {
            this.scrollPos = totalMessages - this.getLineCount();
        }
        if (this.scrollPos <= 0) {
            this.scrollPos = 0;
        }
    }
    
    public boolean isChatOpen() {
        return Minecraft.getMinecraft().currentScreen instanceof GuiChat;
    }
    
    public int getVisualWidth() {
        int width = LabyModCore.getMath().ceiling_float_int(this.getChatWidth() / this.getChatScale());
        if (width <= 0) {
            width = 100;
        }
        return width + 4;
    }
    
    public int getVisualHeight() {
        return LabyModCore.getMath().ceiling_float_int(this.getChatHeight() / this.getChatScale());
    }
    
    public int getLineCount() {
        return this.getChatHeight() / 9;
    }
    
    public boolean isMouseOver() {
        final float x = this.getChatPositionX();
        final float y = this.getChatPositionY();
        final float scale = this.getChatScale();
        final float width = this.getVisualWidth() * scale;
        final boolean hoverY = this.lastMouseY < y && this.lastMouseY > y - this.getChatHeight() * scale;
        if (this.rightBound) {
            return this.lastMouseX < x && this.lastMouseX > x - width && hoverY;
        }
        return this.lastMouseX > x && this.lastMouseX < x + width && hoverY;
    }
    
    public boolean renderHoveringResizeX(final boolean forceRender) {
        if (this.chatLines.size() == 0 || !LabyMod.getSettings().scalableChat) {
            return false;
        }
        final float x = this.getChatPositionX();
        final float y = this.getChatPositionY();
        final float scale = this.getChatScale();
        final float width = this.getVisualWidth() * scale;
        final float height = this.lastRenderedLinesCount * LabyModCore.getMinecraft().getFontRenderer().FONT_HEIGHT * scale;
        final float thickness = 2.0f;
        final boolean hoverY = this.lastMouseY < y && this.lastMouseY > y - height;
        boolean hover = false;
        if (this.rightBound) {
            hover = (this.lastMouseX < x - width + 2.0f && this.lastMouseX > x - width - 2.0f && hoverY);
        }
        else {
            hover = (this.lastMouseX > x + width - 2.0f && this.lastMouseX < x + width + 2.0f && hoverY);
        }
        if (hover || forceRender) {
            final DrawUtils draw = LabyMod.getInstance().getDrawUtils();
            draw.drawString("|||", this.lastMouseX - 2, this.lastMouseY - 2);
            if (this.rightBound) {
                LabyMod.getInstance().getDrawUtils();
                DrawUtils.drawRect(x - width - 1.0f, y, x - width, y - height, Integer.MAX_VALUE);
            }
            else {
                LabyMod.getInstance().getDrawUtils();
                DrawUtils.drawRect(x + width, y, x + width + 1.0f, y - height, Integer.MAX_VALUE);
            }
        }
        return hover;
    }
    
    public boolean renderHoveringResizeY(final boolean forceRender) {
        if (this.chatLines.size() == 0 || !LabyMod.getSettings().scalableChat) {
            return false;
        }
        final float x = this.getChatPositionX();
        final float y = this.getChatPositionY();
        final float scale = this.getChatScale();
        final float width = this.getVisualWidth() * scale;
        float height = this.lastRenderedLinesCount * LabyModCore.getMinecraft().getFontRenderer().FONT_HEIGHT * scale;
        final float thickness = 2.0f;
        final boolean hoverY = this.lastMouseY > y - height - 2.0f && this.lastMouseY < y - height;
        boolean hover = false;
        if (this.rightBound) {
            hover = (this.lastMouseX < x && this.lastMouseX > x - width && hoverY);
        }
        else {
            hover = (this.lastMouseX > x && this.lastMouseX < x + width && hoverY);
        }
        if (forceRender) {
            height = this.getLineCount() * LabyModCore.getMinecraft().getFontRenderer().FONT_HEIGHT * scale;
        }
        if (hover || forceRender) {
            final DrawUtils draw = LabyMod.getInstance().getDrawUtils();
            draw.drawString("==", this.lastMouseX - 5, this.lastMouseY - 3);
            if (this.rightBound) {
                LabyMod.getInstance().getDrawUtils();
                DrawUtils.drawRect(x - width, y - height - 1.0f, x, y - height, Integer.MAX_VALUE);
            }
            else {
                LabyMod.getInstance().getDrawUtils();
                DrawUtils.drawRect(x, y - height - 1.0f, x + width, y - height, Integer.MAX_VALUE);
            }
        }
        return hover;
    }
    
    public String selectHoveredTab() {
        if (this.hoveringRoom != null) {
            this.manager.setSelectedRoom(this.hoveringRoom);
            this.scrollPos = 0;
        }
        return this.manager.getSelectedRoom();
    }
    
    public void updateMouse(final int mouseX, final int mouseY) {
        this.lastMouseX = mouseX;
        this.lastMouseY = mouseY;
    }
    
    public abstract float getChatOpacity();
    
    public abstract float getChatScale();
    
    public abstract float getChatWidth();
    
    public abstract int getChatHeight();
    
    public abstract float getChatPositionX();
    
    public abstract float getChatPositionY();
    
    public abstract float getChatPercentX();
    
    public abstract float getChatPercentY();
    
    public abstract String getLogPrefix();
    
    public abstract void updateChatSetting(final ChatSettingType p0, final float p1);
    
    public abstract void save();
    
    public List<ChatLine> getBackendComponents() {
        return this.backendComponents;
    }
    
    public List<ChatLine> getChatLines() {
        return this.chatLines;
    }
    
    public int getScrollPos() {
        return this.scrollPos;
    }
    
    public boolean isRightBound() {
        return this.rightBound;
    }
    
    public enum ChatSettingType
    {
        WIDTH("WIDTH", 0), 
        HEIGHT("HEIGHT", 1), 
        X("X", 2), 
        Y("Y", 3);
        
        private ChatSettingType(final String s, final int n) {
        }
    }
}
