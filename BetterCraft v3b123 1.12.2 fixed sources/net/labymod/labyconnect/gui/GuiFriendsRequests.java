// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.labyconnect.gui;

import java.util.Iterator;
import net.minecraft.client.renderer.GlStateManager;
import net.labymod.utils.DrawUtils;
import net.labymod.utils.ModColor;
import java.util.Collection;
import java.util.ArrayList;
import net.labymod.labyconnect.LabyConnect;
import net.labymod.labyconnect.packets.PacketPlayDenyFriendRequest;
import net.labymod.labyconnect.packets.Packet;
import net.labymod.labyconnect.packets.PacketPlayRequestAddFriend;
import net.labymod.main.LabyMod;
import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import net.labymod.main.lang.LanguageManager;
import org.lwjgl.input.Keyboard;
import net.labymod.labyconnect.user.ChatRequest;
import net.labymod.gui.elements.Scrollbar;
import net.minecraft.client.gui.GuiScreen;

public class GuiFriendsRequests extends GuiScreen
{
    public static String response;
    private GuiScreen lastScreen;
    private Scrollbar scrollbar;
    private ChatRequest hoverAccept;
    private ChatRequest hoverDeny;
    
    static {
        GuiFriendsRequests.response = null;
    }
    
    public GuiFriendsRequests(final GuiScreen lastScreen) {
        this.scrollbar = new Scrollbar(10);
        this.hoverAccept = null;
        this.hoverDeny = null;
        this.lastScreen = lastScreen;
    }
    
    @Override
    public void initGui() {
        super.initGui();
        Keyboard.enableRepeatEvents(true);
        this.buttonList.add(new GuiButton(1, GuiFriendsRequests.width / 2 - 101, GuiFriendsRequests.height - 37, 98, 20, LanguageManager.translate("button_done")));
        this.scrollbar.init();
        this.scrollbar.setPosition(GuiFriendsRequests.width / 2 + 100, 32, GuiFriendsRequests.width / 2 + 104, GuiFriendsRequests.height - 40 - 2);
        this.scrollbar.setSpeed(10);
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
        final LabyConnect chatClient = LabyMod.getInstance().getLabyConnect();
        if (this.hoverAccept != null) {
            chatClient.getClientConnection().sendPacket(new PacketPlayRequestAddFriend(this.hoverAccept.getGameProfile().getName()));
            chatClient.getRequests().remove(this.hoverAccept);
        }
        else if (this.hoverDeny != null) {
            chatClient.getClientConnection().sendPacket(new PacketPlayDenyFriendRequest(this.hoverDeny));
            chatClient.getRequests().remove(this.hoverDeny);
        }
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
        final DrawUtils draw = LabyMod.getInstance().getDrawUtils();
        this.drawBackground(0);
        draw.drawDimmedOverlayBackground(GuiFriendsRequests.width / 2 - 100, 30, GuiFriendsRequests.width / 2 + 100, GuiFriendsRequests.height - 40);
        this.hoverAccept = null;
        this.hoverDeny = null;
        double listY = 32.0 + this.scrollbar.getScrollY() + 1.0;
        final int entryHeight = 10;
        final ArrayList<ChatRequest> list = new ArrayList<ChatRequest>(LabyMod.getInstance().getLabyConnect().getRequests());
        for (final ChatRequest requester : list) {
            final boolean hoverEntry = mouseX > GuiFriendsRequests.width / 2 - 100 && mouseX < GuiFriendsRequests.width / 2 + 100 && mouseY > listY && mouseY < listY + 10.0 + 1.0;
            final int hoverColor = hoverEntry ? 200 : 100;
            DrawUtils.drawRect(GuiFriendsRequests.width / 2 - 100, listY, GuiFriendsRequests.width / 2 + 100, listY + 10.0, ModColor.toRGB(hoverColor, hoverColor, hoverColor, 30));
            GlStateManager.color(1.0f, 1.0f, 1.0f);
            draw.drawPlayerHead(requester.getGameProfile(), GuiFriendsRequests.width / 2 - 97, (int)listY, 10);
            draw.drawString(requester.getGameProfile().getName(), GuiFriendsRequests.width / 2 - 85, listY + 1.0);
            if (hoverEntry) {
                final int buttonWidth = 10;
                final boolean hoverAccept = mouseX > GuiFriendsRequests.width / 2 + 60 - 10 && mouseX < GuiFriendsRequests.width / 2 + 60 + 10;
                if (hoverAccept) {
                    DrawUtils.drawRect(GuiFriendsRequests.width / 2 + 60 - 10, listY, GuiFriendsRequests.width / 2 + 60 + 10, listY + 10.0, ModColor.toRGB(255, 255, 255, 50));
                    this.hoverAccept = requester;
                }
                final boolean hoverDeny = mouseX > GuiFriendsRequests.width / 2 + 80 - 10 && mouseX < GuiFriendsRequests.width / 2 + 80 + 10;
                if (hoverDeny) {
                    DrawUtils.drawRect(GuiFriendsRequests.width / 2 + 80 - 10, listY, GuiFriendsRequests.width / 2 + 80 + 10, listY + 10.0, ModColor.toRGB(255, 255, 255, 50));
                    this.hoverDeny = requester;
                }
                draw.drawCenteredString(ModColor.GREEN + "\u2714", GuiFriendsRequests.width / 2 + 60, listY + 1.0);
                draw.drawCenteredString(ModColor.RED + "\u2716", GuiFriendsRequests.width / 2 + 80, listY + 1.0);
            }
            listY += 11.0;
        }
        draw.drawGradientShadowTop(30.0, GuiFriendsRequests.width / 2 - 100, GuiFriendsRequests.width / 2 + 100);
        draw.drawGradientShadowBottom(GuiFriendsRequests.height - 40, GuiFriendsRequests.width / 2 - 100, GuiFriendsRequests.width / 2 + 100);
        draw.drawString(String.valueOf(LanguageManager.translate("button_requests")) + ":", GuiFriendsRequests.width / 2 - 100, 20.0);
        final int size = LabyMod.getInstance().getLabyConnect().getRequests().size();
        draw.drawRightString(String.valueOf(size) + " " + LanguageManager.translate((size == 1) ? "button_request" : "button_requests"), GuiFriendsRequests.width / 2 + 99, GuiFriendsRequests.height - 35);
        this.scrollbar.update(list.size());
        this.scrollbar.setEntryHeight(11.0);
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
