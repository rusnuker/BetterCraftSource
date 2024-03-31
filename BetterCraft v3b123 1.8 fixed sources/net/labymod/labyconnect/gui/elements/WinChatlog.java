// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.labyconnect.gui.elements;

import java.util.regex.Matcher;
import net.labymod.utils.DrawUtils;
import net.labymod.utils.ModColor;
import java.util.Iterator;
import net.labymod.labyconnect.log.SingleChat;
import java.util.Collection;
import net.labymod.labyconnect.log.MessageChatComponent;
import java.util.ArrayList;
import net.labymod.main.LabyMod;
import net.minecraft.client.gui.GuiButton;
import java.util.List;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import net.labymod.labyconnect.user.ChatUser;
import net.labymod.gui.elements.Scrollbar;
import java.util.regex.Pattern;
import net.labymod.labyconnect.gui.GuiFriendsLayout;
import net.labymod.gui.layout.WindowElement;

public class WinChatlog extends WindowElement<GuiFriendsLayout>
{
    private static final Pattern URL_PATTERNS;
    private Scrollbar scrollbar;
    private ChatUser clientUser;
    private long prevMessageTime;
    private String lastRenderHoveringMessage;
    private DateFormat timeDateFormat;
    
    static {
        URL_PATTERNS = Pattern.compile("(?:^|[\\W])((ht|f)tp(s?):\\/\\/|www\\.)(([\\w\\-]+\\.){1,}?([\\w\\-.~]+\\/?)*[\\p{Alnum}.,%_=?&#\\-+()\\[\\]\\*$~@!:/{};']*)", 42);
    }
    
    public WinChatlog(final GuiFriendsLayout chatLayout, final ChatUser clientUser) {
        super(chatLayout);
        this.prevMessageTime = 0L;
        this.lastRenderHoveringMessage = null;
        this.timeDateFormat = new SimpleDateFormat("HH:mm");
        this.clientUser = clientUser;
    }
    
    @Override
    protected void init(final List<GuiButton> buttonlist, final int left, final int top, final int right, final int bottom) {
        (this.scrollbar = new Scrollbar(0)).init();
        this.scrollbar.setSpeed(15);
        this.scrollbar.setPosition(this.right - 4, this.top + 4, this.right, this.bottom - 4);
        this.updateGuiElements();
    }
    
    private void updateGuiElements() {
        if (GuiFriendsLayout.selectedUser == null) {
            return;
        }
        double totalEntryHeight = 0.0;
        final SingleChat singleChat = LabyMod.getInstance().getLabyConnect().getChatlogManager().getChat(GuiFriendsLayout.selectedUser);
        final List<MessageChatComponent> messages = new ArrayList<MessageChatComponent>();
        messages.addAll(singleChat.getMessages());
        if (messages.size() != 0) {
            for (final MessageChatComponent messageChatComponent : messages) {
                totalEntryHeight += this.drawMessageEntry(messageChatComponent, totalEntryHeight, false, 0, 0);
            }
            totalEntryHeight = totalEntryHeight / messages.size() + 2.0;
        }
        final long sentTime = (singleChat.getMessages().size() == 0) ? 0L : singleChat.getMessages().get(singleChat.getMessages().size() - 1).getSentTime();
        if (this.prevMessageTime != sentTime) {
            this.prevMessageTime = sentTime;
            this.scrollbar.requestBottom();
        }
        this.scrollbar.setEntryHeight(totalEntryHeight);
        this.scrollbar.update(messages.size());
    }
    
    @Override
    public void draw(final int mouseX, final int mouseY) {
        super.draw(mouseX, mouseY);
        if (GuiFriendsLayout.selectedUser == null) {
            return;
        }
        this.updateGuiElements();
        this.lastRenderHoveringMessage = null;
        final SingleChat singleChat = LabyMod.getInstance().getLabyConnect().getChatlogManager().getChat(GuiFriendsLayout.selectedUser);
        final List<MessageChatComponent> messages = singleChat.getMessages();
        try {
            double entryY = this.top + this.scrollbar.getScrollY() + 4.0;
            for (final MessageChatComponent messageChatComponent : messages) {
                entryY += this.drawMessageEntry(messageChatComponent, entryY, true, mouseX, mouseY);
                entryY += 2.0;
            }
        }
        catch (final Exception error) {
            error.printStackTrace();
        }
        this.scrollbar.draw();
    }
    
    @Override
    public void actionPerformed(final GuiButton button) {
    }
    
    private int drawMessageEntry(final MessageChatComponent messageChatComponent, final double entryY, final boolean shouldDraw, final int mouseX, final int mouseY) {
        final DrawUtils drawUtils = LabyMod.getInstance().getDrawUtils();
        final String meName = LabyMod.getInstance().getPlayerName();
        final boolean isMe = messageChatComponent.getSender().equals(meName);
        final String message = messageChatComponent.getMessage();
        final int margineWidth = 20;
        final int margineBorder = 30;
        final int paddingWidth = 11;
        final int paddingHeight = 2;
        final int spaceBetweenText = 10;
        final int headSize = 12;
        final double maxWidth = this.right - this.left - (this.right - this.left) / 5.0 - 20.0 - 5.5 - 20.0;
        final List<String> messageList = drawUtils.listFormattedStringToWidth(message, (int)maxWidth);
        final int entryHeight = messageList.size() * 10 + 4;
        final int firstMessageWidth = drawUtils.getStringWidth(message);
        if (!shouldDraw) {
            return entryHeight;
        }
        if (isMe) {
            double entryLeft = this.right - 30 - maxWidth - 20.0 - 5.5;
            final double entryRight = this.right - 30 - 20;
            if (messageList.size() == 1) {
                entryLeft = this.right - 30 - firstMessageWidth - 20 - 11;
            }
            final double entryTop = entryY;
            final double entryBottom = entryY + entryHeight;
            DrawUtils.drawRect(entryLeft, entryTop, entryRight, entryBottom, ModColor.toRGB(80, 80, 80, 60));
            double lineYAdd = entryTop + 2.0 + 1.0;
            for (String messageLine : messageList) {
                if (mouseX > entryLeft && mouseX < entryRight && mouseY > lineYAdd && mouseY < lineYAdd + 10.0) {
                    this.lastRenderHoveringMessage = messageLine;
                    if (this.containsUrl(messageLine)) {
                        messageLine = String.valueOf(ModColor.cl('9')) + messageLine + ModColor.cl('f');
                    }
                }
                drawUtils.drawString(messageLine, entryLeft + 5.5, lineYAdd);
                lineYAdd += 10.0;
            }
            if (GuiFriendsLayout.selectedUser.isParty()) {
                LabyMod.getInstance().getDrawUtils().drawPlayerHead(messageChatComponent.getSender(), (int)(entryRight + 10.0 - 6.0), (int)(entryTop + (entryBottom - entryTop) / 2.0) - 6, 12);
            }
            else {
                drawUtils.drawPlayerHead(this.clientUser.getGameProfile(), (int)(entryRight + 10.0 - 6.0), (int)(entryTop + (entryBottom - entryTop) / 2.0) - 6, 12);
            }
            final String displayTime = this.timeDateFormat.format(messageChatComponent.getSentTime());
            drawUtils.drawRightString(displayTime, entryLeft - 2.0, entryTop + 2.0, 0.5);
        }
        else {
            final double entryLeft = this.left + 30 + 20;
            double entryRight = this.left + 30 + maxWidth + 20.0 + 5.5;
            if (messageList.size() == 1) {
                entryRight = this.left + 30 + firstMessageWidth + 20 + 11;
            }
            final double entryTop = entryY;
            final double entryBottom = entryY + entryHeight;
            DrawUtils.drawRect(entryLeft, entryTop, entryRight, entryBottom, ModColor.toRGB(80, 80, 80, 60));
            double lineYAdd = entryTop + 2.0 + 1.0;
            for (String messageLine : messageList) {
                if (mouseX > entryLeft && mouseX < entryRight && mouseY > lineYAdd && mouseY < lineYAdd + 10.0) {
                    this.lastRenderHoveringMessage = messageLine;
                    if (this.containsUrl(messageLine)) {
                        messageLine = String.valueOf(ModColor.cl('9')) + messageLine + ModColor.cl('f');
                    }
                }
                drawUtils.drawString(messageLine, entryLeft + 5.0, lineYAdd);
                lineYAdd += 10.0;
            }
            if (GuiFriendsLayout.selectedUser.isParty()) {
                LabyMod.getInstance().getDrawUtils().drawPlayerHead(messageChatComponent.getSender(), (int)(entryLeft - 10.0 - 6.0), (int)(entryTop + (entryBottom - entryTop) / 2.0 - 6.0), 12);
            }
            else {
                drawUtils.drawPlayerHead(GuiFriendsLayout.selectedUser.getGameProfile(), (int)(entryLeft - 10.0 - 6.0), (int)(entryTop + (entryBottom - entryTop) / 2.0 - 6.0), 12);
            }
            final String displayTime = this.timeDateFormat.format(messageChatComponent.getSentTime());
            drawUtils.drawString(displayTime, entryRight + 2.0, entryTop + 2.0, 0.5);
        }
        return entryHeight;
    }
    
    private boolean containsUrl(final String message) {
        return message.contains("http://") || message.contains("https://");
    }
    
    public void handleMouseInput() {
        if (this.isMouseOver()) {
            this.scrollbar.mouseInput();
        }
    }
    
    @Override
    public boolean mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.CLICKED);
        if (this.lastRenderHoveringMessage != null) {
            final Matcher matcher = WinChatlog.URL_PATTERNS.matcher(this.lastRenderHoveringMessage);
            if (matcher.find()) {
                final int matchStart = matcher.start(1);
                final int matchEnd = matcher.end();
                final String website = this.lastRenderHoveringMessage.substring(matchStart, matchEnd);
                LabyMod.getInstance().openWebpage(website, true);
            }
        }
        return false;
    }
    
    @Override
    public void keyTyped(final char typedChar, final int keyCode) {
    }
    
    @Override
    public void mouseClickMove(final int mouseX, final int mouseY) {
        this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.DRAGGING);
    }
    
    @Override
    public void mouseReleased(final int mouseX, final int mouseY, final int mouseButton) {
    }
    
    @Override
    public void mouseInput() {
    }
    
    public boolean isScrolledToTop() {
        return this.scrollbar.getScrollY() == 0.0;
    }
    
    @Override
    public void updateScreen() {
    }
}
