// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.gui;

import net.minecraft.util.text.TextComponentString;
import org.lwjgl.input.Mouse;
import java.util.Iterator;
import com.darkmagician6.eventapi.events.Event;
import com.darkmagician6.eventapi.EventManager;
import me.amkgre.bettercraft.client.events.ChatMessageSendEvent;
import me.amkgre.bettercraft.client.Client;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.ITextComponent;
import me.amkgre.bettercraft.client.gui.GuiClientUI;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.player.EntityPlayer;
import com.google.common.collect.Lists;
import org.apache.logging.log4j.LogManager;
import java.util.List;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.Logger;

public class GuiNewChat extends Gui
{
    private static final Logger LOGGER;
    private final Minecraft mc;
    private final List<String> sentMessages;
    private final List<ChatLine> chatLines;
    private final List<ChatLine> drawnChatLines;
    private int scrollPos;
    private boolean isScrolled;
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    public GuiNewChat(final Minecraft mcIn) {
        this.sentMessages = (List<String>)Lists.newArrayList();
        this.chatLines = (List<ChatLine>)Lists.newArrayList();
        this.drawnChatLines = (List<ChatLine>)Lists.newArrayList();
        this.mc = mcIn;
    }
    
    public void drawChat(final int updateCounter) {
        if (this.mc.gameSettings.chatVisibility != EntityPlayer.EnumChatVisibility.HIDDEN) {
            final int i = this.getLineCount();
            final int j = this.drawnChatLines.size();
            final float f = this.mc.gameSettings.chatOpacity * 0.9f + 0.1f;
            if (j > 0) {
                boolean flag = false;
                if (this.getChatOpen()) {
                    flag = true;
                }
                final float f2 = this.getChatScale();
                final int k = MathHelper.ceil(this.getChatWidth() / f2);
                GlStateManager.pushMatrix();
                GlStateManager.translate(2.0f, 8.0f, 0.0f);
                GlStateManager.scale(f2, f2, 1.0f);
                int l = 0;
                for (int i2 = 0; i2 + this.scrollPos < this.drawnChatLines.size() && i2 < i; ++i2) {
                    final ChatLine chatline = this.drawnChatLines.get(i2 + this.scrollPos);
                    if (chatline != null) {
                        final int j2;
                        if ((j2 = updateCounter - chatline.getUpdatedCounter()) < 200 || flag) {
                            double d0 = j2 / 200.0;
                            d0 = 1.0 - d0;
                            d0 *= 10.0;
                            d0 = MathHelper.clamp(d0, 0.0, 1.0);
                            d0 *= d0;
                            int l2 = (int)(255.0 * d0);
                            if (flag) {
                                l2 = 255;
                            }
                            l2 *= (int)f;
                            ++l;
                            if (l2 > 3) {
                                final boolean i3 = false;
                                final int j3 = -i2 * 9 + 7;
                                if (!GuiClientUI.chatBackground) {
                                    Gui.drawRect(-2, j3 - 9, 0 + k + 10, j3, l2 / 2 << 24);
                                }
                                final String s2 = chatline.getChatComponent().getFormattedText();
                                GlStateManager.enableBlend();
                                if (chatline.slide < 0) {
                                    final ChatLine chatLine = chatline;
                                    chatLine.slide += 5;
                                }
                                this.mc.fontRendererObj.drawStringWithShadow(s2, (float)chatline.slide, (float)(j3 - 8), 16777215);
                                GlStateManager.disableAlpha();
                                GlStateManager.disableBlend();
                            }
                        }
                    }
                }
                if (flag) {
                    final int k2 = this.mc.fontRendererObj.FONT_HEIGHT;
                    GlStateManager.translate(-3.0f, 0.0f, 0.0f);
                    final int l3 = j * k2 + j;
                    final int i4 = l * k2 + l;
                    final int j4 = this.scrollPos * i4 / j;
                    final int n = i4 * i4 / l3;
                }
                GlStateManager.popMatrix();
            }
        }
    }
    
    public void clearChatMessages(final boolean p_146231_1_) {
        this.drawnChatLines.clear();
        this.chatLines.clear();
        if (p_146231_1_) {
            this.sentMessages.clear();
        }
    }
    
    public void printChatMessage(final ITextComponent chatComponent) {
        if (TextFormatting.getTextWithoutFormattingCodes(chatComponent.getUnformattedText()).replaceAll("\n", "").trim().isEmpty()) {
            return;
        }
        this.printChatMessageWithOptionalDeletion(chatComponent, 0);
    }
    
    public void printChatMessageWithOptionalDeletion(final ITextComponent chatComponent, final int chatLineId) {
        this.setChatLine(chatComponent, chatLineId, this.mc.ingameGUI.getUpdateCounter(), false);
        final String unformattedText = chatComponent.getUnformattedText();
        Client.getInstance();
        if (!unformattedText.contains(Client.clientPrefix)) {
            final ChatMessageSendEvent chatMessageSendEvent = new ChatMessageSendEvent(chatComponent.getUnformattedText());
            EventManager.call(chatMessageSendEvent);
        }
        GuiNewChat.LOGGER.info("[CHAT] {}", chatComponent.getUnformattedText().replaceAll("\r", "\\\\r").replaceAll("\n", "\\\\n"));
    }
    
    private void setChatLine(final ITextComponent chatComponent, final int chatLineId, final int updateCounter, final boolean displayOnly) {
        if (chatLineId != 0) {
            this.deleteChatLine(chatLineId);
        }
        final int i = MathHelper.floor(this.getChatWidth() / this.getChatScale());
        final List<ITextComponent> list2 = GuiUtilRenderComponents.splitText(chatComponent, i, this.mc.fontRendererObj, false, false);
        final boolean flag = this.getChatOpen();
        for (final ITextComponent itextcomponent : list2) {
            if (flag && this.scrollPos > 0) {
                this.isScrolled = true;
                this.scroll(1);
            }
            this.drawnChatLines.add(0, new ChatLine(updateCounter, itextcomponent, chatLineId));
        }
        while (this.drawnChatLines.size() > 100) {
            this.drawnChatLines.remove(this.drawnChatLines.size() - 1);
        }
        if (!displayOnly) {
            this.chatLines.add(0, new ChatLine(updateCounter, chatComponent, chatLineId));
            while (this.chatLines.size() > 100) {
                this.chatLines.remove(this.chatLines.size() - 1);
            }
        }
    }
    
    public void refreshChat() {
        this.drawnChatLines.clear();
        this.resetScroll();
        for (int i = this.chatLines.size() - 1; i >= 0; --i) {
            final ChatLine chatline = this.chatLines.get(i);
            this.setChatLine(chatline.getChatComponent(), chatline.getChatLineID(), chatline.getUpdatedCounter(), true);
        }
    }
    
    public List<String> getSentMessages() {
        return this.sentMessages;
    }
    
    public void addToSentMessages(final String message) {
        if (this.sentMessages.isEmpty() || !this.sentMessages.get(this.sentMessages.size() - 1).equals(message)) {
            this.sentMessages.add(message);
        }
    }
    
    public void resetScroll() {
        this.scrollPos = 0;
        this.isScrolled = false;
    }
    
    public void scroll(final int amount) {
        this.scrollPos += amount;
        final int i = this.drawnChatLines.size();
        if (this.scrollPos > i - this.getLineCount()) {
            this.scrollPos = i - this.getLineCount();
        }
        if (this.scrollPos <= 0) {
            this.scrollPos = 0;
            this.isScrolled = false;
        }
    }
    
    public ITextComponent getChatComponent(final int mouseX, final int mouseY) {
        if (!this.getChatOpen()) {
            return null;
        }
        final ScaledResolution scaledresolution = new ScaledResolution(this.mc);
        final int i = scaledresolution.getScaleFactor();
        final float f = this.getChatScale();
        int j = mouseX / i - 2;
        int k = mouseY / i - 33;
        j = MathHelper.floor(j / f);
        k = MathHelper.floor(k / f);
        if (j < 0 || k < 0) {
            return null;
        }
        final int l = Math.min(this.getLineCount(), this.drawnChatLines.size());
        if (j <= MathHelper.floor(this.getChatWidth() / this.getChatScale()) && k < this.mc.fontRendererObj.FONT_HEIGHT * l + l) {
            final int i2 = k / this.mc.fontRendererObj.FONT_HEIGHT + this.scrollPos;
            if (i2 >= 0 && i2 < this.drawnChatLines.size()) {
                final ChatLine chatline = this.drawnChatLines.get(i2);
                if (Mouse.isButtonDown(1)) {
                    return chatline.getChatComponent();
                }
                int j2 = 0;
                for (final ITextComponent itextcomponent : chatline.getChatComponent()) {
                    if (itextcomponent instanceof TextComponentString) {
                        j2 += this.mc.fontRendererObj.getStringWidth(GuiUtilRenderComponents.removeTextColorsIfConfigured(((TextComponentString)itextcomponent).getText(), false));
                        if (j2 > j) {
                            return itextcomponent;
                        }
                        continue;
                    }
                }
            }
            return null;
        }
        return null;
    }
    
    public boolean getChatOpen() {
        return Minecraft.currentScreen instanceof GuiChat;
    }
    
    public void deleteChatLine(final int id) {
        Iterator<ChatLine> iterator2 = this.drawnChatLines.iterator();
        while (iterator2.hasNext()) {
            final ChatLine chatline = iterator2.next();
            if (chatline.getChatLineID() != id) {
                continue;
            }
            iterator2.remove();
        }
        iterator2 = this.chatLines.iterator();
        while (iterator2.hasNext()) {
            final ChatLine chatline2 = iterator2.next();
            if (chatline2.getChatLineID() != id) {
                continue;
            }
            iterator2.remove();
            break;
        }
    }
    
    public int getChatWidth() {
        return calculateChatboxWidth(this.mc.gameSettings.chatWidth);
    }
    
    public int getChatHeight() {
        return calculateChatboxHeight(this.getChatOpen() ? this.mc.gameSettings.chatHeightFocused : this.mc.gameSettings.chatHeightUnfocused);
    }
    
    public float getChatScale() {
        return this.mc.gameSettings.chatScale;
    }
    
    public static int calculateChatboxWidth(final float scale) {
        final int i = 320;
        final int j = 40;
        return MathHelper.floor(scale * 280.0f + 40.0f);
    }
    
    public static int calculateChatboxHeight(final float scale) {
        final int i = 180;
        final int j = 20;
        return MathHelper.floor(scale * 160.0f + 20.0f);
    }
    
    public int getLineCount() {
        return this.getChatHeight() / 9;
    }
}
