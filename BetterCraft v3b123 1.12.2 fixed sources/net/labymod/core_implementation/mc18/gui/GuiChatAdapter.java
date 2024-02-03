// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.core_implementation.mc18.gui;

import java.beans.ConstructorProperties;
import net.minecraft.util.ChatComponentText;
import java.util.LinkedList;
import net.labymod.ingamechat.renderer.ChatLine;
import net.labymod.ingamechat.tools.filter.Filters;
import net.labymod.ingamechat.renderer.MessageData;
import net.labymod.servermanager.ChatDisplayAction;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.labymod.utils.ModColor;
import net.labymod.utils.manager.TagManager;
import net.labymod.core.LabyModCore;
import java.util.Iterator;
import net.labymod.api.events.MessageReceiveEvent;
import net.minecraft.util.IChatComponent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.renderer.GlStateManager;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import net.labymod.main.LabyMod;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.LogManager;
import java.util.Queue;
import net.labymod.ingamechat.renderer.ChatRenderer;
import net.labymod.ingamechat.renderer.types.ChatRendererSecond;
import net.labymod.ingamechat.renderer.types.ChatRendererMain;
import net.labymod.ingamechat.IngameChatManager;
import org.apache.logging.log4j.Logger;
import net.minecraft.client.gui.GuiNewChat;

public class GuiChatAdapter extends GuiNewChat
{
    private static final Logger logger;
    private final IngameChatManager manager;
    private final ChatRendererMain chatMain;
    private final ChatRendererSecond chatSecond;
    private final ChatRenderer[] chatRenderers;
    private final Queue<QueuedMessage> queuedMessages;
    
    static {
        logger = LogManager.getLogger();
    }
    
    public GuiChatAdapter(final Minecraft mcIn) {
        super(mcIn);
        this.manager = LabyMod.getInstance().getIngameChatManager();
        this.chatMain = this.manager.getMain();
        this.chatSecond = this.manager.getSecond();
        this.chatRenderers = this.manager.getChatRenderers();
        this.queuedMessages = new ConcurrentLinkedQueue<QueuedMessage>();
    }
    
    @Override
    public void addToSentMessages(final String message) {
        this.manager.addToSentMessages(message);
    }
    
    @Override
    public List<String> getSentMessages() {
        return this.manager.getSentMessages();
    }
    
    @Override
    public void drawChat(final int updateCounter) {
        final int offset = LabyMod.getInstance().getDrawUtils().getHeight() - 48;
        GlStateManager.translate(0.0f, (float)(-offset), 0.0f);
        if (Minecraft.getMinecraft().gameSettings.chatVisibility == EntityPlayer.EnumChatVisibility.HIDDEN) {
            return;
        }
        ChatRenderer[] chatRenderers;
        for (int length = (chatRenderers = this.chatRenderers).length, i = 0; i < length; ++i) {
            final ChatRenderer chatRenderer = chatRenderers[i];
            chatRenderer.renderChat(updateCounter);
        }
        if (this.queuedMessages.size() != 0) {
            QueuedMessage queuedMessage = null;
            while ((queuedMessage = this.queuedMessages.poll()) != null) {
                this.setChatLine(queuedMessage.getComponent(), queuedMessage.getChatLineId(), Minecraft.getMinecraft().ingameGUI.getUpdateCounter(), false, false, "Global", null);
            }
        }
    }
    
    @Override
    public void deleteChatLine(final int id) {
    }
    
    public void deleteChatLine(final ChatRenderer chatRenderer, final int id) {
        chatRenderer.deleteChatLine(id);
    }
    
    @Override
    public void clearChatMessages() {
        ChatRenderer[] chatRenderers;
        for (int length = (chatRenderers = this.chatRenderers).length, i = 0; i < length; ++i) {
            final ChatRenderer chatRenderer = chatRenderers[i];
            chatRenderer.clearChatMessages(true);
        }
    }
    
    @Override
    public void scroll(int amount) {
        if (amount != 1 && amount != -1) {
            amount = (int)(amount / 7.0 * LabyMod.getSettings().chatScrollSpeed);
        }
        ChatRenderer[] chatRenderers;
        for (int length = (chatRenderers = this.chatRenderers).length, i = 0; i < length; ++i) {
            final ChatRenderer chatRenderer = chatRenderers[i];
            if (chatRenderer.isMouseOver()) {
                chatRenderer.scroll(amount);
            }
        }
    }
    
    @Override
    public void printChatMessageWithOptionalDeletion(final IChatComponent component, final int chatLineId) {
        boolean cancel = false;
        for (final MessageReceiveEvent event : LabyMod.getInstance().getEventManager().getMessageReceive()) {
            if (event.onReceive(component.getFormattedText(), component.getUnformattedText())) {
                cancel = true;
            }
        }
        if (!cancel) {
            this.queuedMessages.add(new QueuedMessage(component, chatLineId));
        }
    }
    
    private void setChatLine(IChatComponent component, final int chatLineId, final int updateCounter, final boolean refresh, boolean secondChat, String room, Integer highlightColor) {
        if (component == null) {
            return;
        }
        ChatRenderer target = null;
        if (!refresh) {
            final ChatDisplayAction serverDisplayActionResponse = LabyMod.getInstance().getServerManager().handleChatMessage(component.getUnformattedText(), component.getFormattedText());
            final MessageData messageData = this.manager.handleSwap(serverDisplayActionResponse, LabyModCore.getMinecraft().getChatComponent(component));
            if (messageData == null) {
                return;
            }
            secondChat = messageData.isDisplayInSecondChat();
            component = (IChatComponent)TagManager.tagComponent(component);
            final Filters.Filter filter = messageData.getFilter();
            if (filter != null) {
                room = filter.getRoom();
                if (filter.isHighlightMessage()) {
                    highlightColor = ModColor.toRGB(filter.getHighlightColorR(), filter.getHighlightColorG(), filter.getHighlightColorB(), 120);
                }
            }
        }
        target = (secondChat ? this.chatSecond : this.chatMain);
        if (chatLineId != 0) {
            this.deleteChatLine(target, chatLineId);
        }
        final int width = target.getVisualWidth();
        final List<IChatComponent> list = GuiUtilRenderComponents.splitText(component, width, LabyModCore.getMinecraft().getFontRenderer(), false, false);
        for (final IChatComponent ichatcomponent : list) {
            target.addChatLine(ichatcomponent.getFormattedText(), secondChat, room, ichatcomponent, updateCounter, chatLineId, highlightColor, refresh);
        }
        target.checkLimit();
        if (!refresh) {
            target.handleBackendLines(component.getFormattedText(), secondChat, room, component, chatLineId, updateCounter, highlightColor);
            this.manager.handleUnread(room);
            GuiChatAdapter.logger.info("[" + target.getLogPrefix() + "] " + component.getUnformattedText());
        }
    }
    
    @Override
    public void refreshChat() {
        ChatRenderer[] chatRenderers;
        for (int length = (chatRenderers = this.chatRenderers).length, j = 0; j < length; ++j) {
            final ChatRenderer chatRenderer = chatRenderers[j];
            chatRenderer.clearChatMessages(false);
            for (int i = chatRenderer.getBackendComponents().size() - 1; i >= 0; --i) {
                final ChatLine chatLine = chatRenderer.getBackendComponents().get(i);
                this.setChatLine((IChatComponent)chatLine.getComponent(), chatLine.getChatLineId(), chatLine.getUpdateCounter(), true, chatLine.isSecondChat(), chatLine.getRoom(), chatLine.getHighlightColor());
            }
        }
    }
    
    @Override
    public IChatComponent getChatComponent(final int mouseX, final int mouseY) {
        ChatRenderer[] chatRenderers;
        for (int length = (chatRenderers = this.chatRenderers).length, i = 0; i < length; ++i) {
            final ChatRenderer chatRenderer = chatRenderers[i];
            final Object component = this.getHoveringComponent(chatRenderer);
            if (component != null) {
                return (IChatComponent)component;
            }
        }
        return null;
    }
    
    private Object getHoveringComponent(final ChatRenderer chatRenderer) {
        if (!chatRenderer.isChatOpen() || !chatRenderer.isMouseOver()) {
            return null;
        }
        float mouseX = chatRenderer.isRightBound() ? (chatRenderer.lastMouseX - chatRenderer.getChatPositionX() + chatRenderer.getChatWidth() + 3.0f) : (-(chatRenderer.getChatPositionX() - chatRenderer.lastMouseX));
        float mouseY = -chatRenderer.lastMouseY + chatRenderer.getChatPositionY();
        mouseX /= this.getChatScale();
        mouseY /= this.getChatScale();
        final List<ChatLine> list = new LinkedList<ChatLine>();
        for (final ChatLine chatline : chatRenderer.getChatLines()) {
            if (chatline != null) {
                if (!chatline.getRoom().equals(this.manager.getSelectedRoom())) {
                    continue;
                }
                list.add(chatline);
            }
        }
        final int hoveredLine = (int)mouseY / Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + chatRenderer.getScrollPos();
        if (hoveredLine < 0 || hoveredLine >= list.size()) {
            return null;
        }
        int x = 0;
        final ChatLine chatline2 = list.get(hoveredLine);
        for (final IChatComponent ichatcomponent : (IChatComponent)chatline2.getComponent()) {
            if (!(ichatcomponent instanceof ChatComponentText)) {
                continue;
            }
            x += Minecraft.getMinecraft().fontRendererObj.getStringWidth(GuiUtilRenderComponents.func_178909_a(((ChatComponentText)ichatcomponent).getChatComponentText_TextValue(), false));
            if (x > mouseX) {
                return ichatcomponent;
            }
        }
        return null;
    }
    
    private class QueuedMessage
    {
        private final IChatComponent component;
        private final int chatLineId;
        
        @ConstructorProperties({ "component", "chatLineId" })
        public QueuedMessage(final IChatComponent component, final int chatLineId) {
            this.component = component;
            this.chatLineId = chatLineId;
        }
        
        public IChatComponent getComponent() {
            return this.component;
        }
        
        public int getChatLineId() {
            return this.chatLineId;
        }
    }
}
