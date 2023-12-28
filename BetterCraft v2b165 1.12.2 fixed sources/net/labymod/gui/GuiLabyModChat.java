// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.gui;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import me.amkgre.bettercraft.client.utils.RenderUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import java.util.HashMap;
import net.minecraft.client.gui.GuiSlot;
import java.util.Iterator;
import net.labymod.labyconnect.log.ChatlogManager;
import net.minecraft.client.gui.Gui;
import java.io.IOException;
import net.labymod.labyconnect.log.SingleChat;
import net.labymod.labyconnect.log.MessageChatComponent;
import me.amkgre.bettercraft.client.Client;
import org.lwjgl.input.Keyboard;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.GuiButton;
import net.labymod.labyconnect.user.ChatUser;
import java.util.List;
import net.minecraft.client.gui.GuiScreen;

public class GuiLabyModChat extends GuiScreen
{
    private final List<ChatUser> friends;
    private GuiButton guiButton;
    private FriendsListSlot friendlistSlot;
    private GuiTextField textField;
    public GuiScreen before;
    int i;
    
    public GuiLabyModChat(final List<ChatUser> friends) {
        this.friends = friends;
    }
    
    @Override
    public void initGui() {
        final int width = GuiLabyModChat.width / 2;
        (this.textField = new GuiTextField(0, this.mc.fontRendererObj, width / 2, GuiLabyModChat.height - 40, width, 20)).setMaxStringLength(Integer.MAX_VALUE);
        this.buttonList.add(new GuiButton(1, GuiLabyModChat.width - 80, GuiLabyModChat.height - 26, 75, 20, "Back"));
        (this.friendlistSlot = new FriendsListSlot(this.mc, this, this.friends)).registerScrollButtons(4, 5);
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        Keyboard.enableRepeatEvents(true);
        this.textField.textboxKeyTyped(typedChar, keyCode);
        switch (keyCode) {
            case 28: {
                if (!this.textField.getText().isEmpty()) {
                    final SingleChat chat = Client.getInstance().getLabyMod().getLabyConnect().getChatlogManager().getChat(this.friendlistSlot.user);
                    chat.addMessage(new MessageChatComponent(Client.getInstance().getLabyMod().getGameProfile().getName(), System.currentTimeMillis(), this.textField.getText()));
                }
                this.textField.setText("");
                break;
            }
        }
        super.keyTyped(typedChar, keyCode);
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        this.textField.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        this.textField.drawTextBox();
        this.friendlistSlot.drawScreen(mouseX, mouseY, partialTicks);
        if (this.friendlistSlot.user != null) {
            final ChatlogManager chatlogmanager = Client.getInstance().getLabyMod().getLabyConnect().getChatlogManager();
            final String name = this.friendlistSlot.user.getGameProfile().getName();
            final String currentServer = this.friendlistSlot.user.getCurrentServerInfo().getDisplayAddress();
            final String onStatus = this.friendlistSlot.user.isOnline() ? "§aOnline" : "§cOffline";
            Gui.drawString(this.fontRendererObj, String.valueOf(onStatus) + "§7: §7" + name, GuiLabyModChat.width / 2, GuiLabyModChat.height / 2 - 100, -1);
            Gui.drawString(this.fontRendererObj, "§fLast Server: §d" + currentServer, GuiLabyModChat.width / 2, GuiLabyModChat.height / 2 - 90, -1);
            int count = 10;
            for (final MessageChatComponent bbb : chatlogmanager.getChat(this.friendlistSlot.user).getMessages()) {
                Gui.drawString(this.fontRendererObj, "§7" + bbb.getSender() + ": §d" + bbb.getMessage(), GuiLabyModChat.width / 2, GuiLabyModChat.height / 2 - 80 + count, -1);
                count += 10;
            }
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    @Override
    public void handleMouseInput() throws IOException {
        this.friendlistSlot.handleMouseInput();
        super.handleMouseInput();
    }
    
    public static String[] substringargs(final String[] arr) {
        final int newLen = arr.length - 1;
        final String[] out = new String[newLen];
        for (int i = 0; i < newLen; ++i) {
            out[i] = arr[i + 1];
        }
        return out;
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.id == 2) {
            if (Client.getInstance().getLabyMod().getLabyConnect().isOnline()) {
                Client.getInstance().disconnectLabyMod();
            }
            else {
                Client.getInstance().connectLabyMod();
            }
        }
        if (button.id == 1) {
            this.mc.displayGuiScreen(this.before);
        }
    }
    
    class FriendsListSlot extends GuiSlot
    {
        private HashMap<String, Integer> helmetTexture;
        public ChatUser user;
        private GuiScreen guiScreen;
        private int currentChat;
        private List<ChatUser> friendList;
        private FontRenderer fontRendererObj;
        
        public FriendsListSlot(final Minecraft mcIn, final GuiScreen gui, final List<ChatUser> friendList) {
            super(mcIn, GuiScreen.width, GuiScreen.height, 32, GuiScreen.height - 64, 36);
            this.helmetTexture = new HashMap<String, Integer>();
            this.currentChat = -1;
            this.fontRendererObj = Minecraft.getMinecraft().fontRendererObj;
            this.guiScreen = gui;
            this.friendList = friendList;
            this.renderClicked = false;
        }
        
        @Override
        protected int getSize() {
            return this.friendList.size();
        }
        
        @Override
        protected void elementClicked(final int slotIndex, final boolean isDoubleClick, final int mouseX, final int mouseY) {
            this.currentChat = slotIndex;
            this.user = this.friendList.get(slotIndex);
        }
        
        @Override
        protected boolean isSelected(final int slotIndex) {
            return slotIndex == this.currentChat;
        }
        
        @Override
        protected int getContentHeight() {
            return this.getSize() * 36;
        }
        
        @Override
        protected void drawBackground() {
        }
        
        @Override
        public void scrollBy(final int amount) {
            super.scrollBy(amount);
        }
        
        @Override
        protected void func_192637_a(final int entryID, int x, final int y, final int p_192637_4_, final int p_192637_5_, final int p_192637_6_, final float p_192637_7_) {
            final ChatUser labyname = this.friendList.get(entryID);
            final String clientName;
            final String name = clientName = labyname.getGameProfile().getName().split(" ")[0];
            x /= 4;
            Gui.drawString(this.fontRendererObj, String.valueOf(labyname.isOnline() ? ("§a" + name) : ("§c" + name)), x, y + 12, 8421504);
            try {
                if (this.helmetTexture.containsKey(clientName)) {
                    GlStateManager.bindTexture(this.helmetTexture.get(clientName));
                }
                else {
                    RenderUtils.downloadSkin(clientName);
                    if (RenderUtils.dynamicTexture != null) {
                        this.helmetTexture.put(clientName, RenderUtils.dynamicTexture.getGlTextureId());
                    }
                }
            }
            catch (final Exception ex) {}
            final int img_w = 32;
            final int img_h = 32;
            final Tessellator tessellator = Tessellator.getInstance();
            final BufferBuilder worldrenderer = tessellator.getBuffer();
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
            worldrenderer.pos(x - 40, y, 0.0).tex(0.0, 0.0).endVertex();
            worldrenderer.pos(x - 40, y + img_w, 0.0).tex(0.0, 1.0).endVertex();
            worldrenderer.pos(x + img_h - 40, y + img_w, 0.0).tex(1.0, 1.0).endVertex();
            worldrenderer.pos(x + img_h - 40, y, 0.0).tex(1.0, 0.0).endVertex();
            tessellator.draw();
        }
    }
}
