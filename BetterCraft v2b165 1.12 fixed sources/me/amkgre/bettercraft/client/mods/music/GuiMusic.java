// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.music;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiSlot;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.net.URL;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;
import java.io.IOException;
import net.minecraft.client.resources.I18n;
import javazoom.jl.player.Player;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.GuiButton;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class GuiMusic extends GuiScreen
{
    private Minecraft mc;
    private GuiScreen before;
    private List<String> radioSender;
    private List<String> radioUrl;
    private GuiList radioGuiList;
    private int selectedRadio;
    private String channelName;
    private GuiButton button;
    private GuiTextField stream;
    private GuiTextField name;
    private static int channelId;
    public static String music_link;
    private static String URL;
    private static Player player;
    private static boolean isRunning;
    private static float currentVolume;
    
    static {
        GuiMusic.channelId = 2;
        GuiMusic.URL = "";
        GuiMusic.currentVolume = -40.0f;
    }
    
    public GuiMusic(final GuiScreen before) {
        this.mc = Minecraft.getMinecraft();
        this.selectedRadio = -1;
        this.radioSender = RadioList.radioSender;
        this.radioUrl = RadioList.radioUrl;
        this.before = before;
    }
    
    @Override
    public void initGui() {
        this.buttonList.add(new GuiButton(0, GuiMusic.width - 70, GuiMusic.height - 26, 60, 20, "Back"));
        this.buttonList.add(this.button = new GuiButton(1, GuiMusic.width / 2 - 50, GuiMusic.height - 26, 100, 20, GuiMusic.isRunning ? "§cStop" : "§aStart"));
        this.buttonList.add(this.button = new GuiButton(2, GuiMusic.width / 2 - 100 + 200 - 20, GuiMusic.height - 26, 20, 20, "§a+"));
        this.buttonList.add(this.button = new GuiButton(3, GuiMusic.width / 2 - 100 - 2, GuiMusic.height - 26, 20, 20, "§c-"));
        (this.stream = new GuiTextField(GuiMusic.height, this.mc.fontRendererObj, GuiMusic.width / 2 - 15, 5, 150, 20)).setMaxStringLength(1000);
        this.stream.setText(GuiMusic.URL);
        (this.name = new GuiTextField(GuiMusic.height, this.mc.fontRendererObj, GuiMusic.width / 2 - 133, 5, 100, 20)).setMaxStringLength(1000);
        this.buttonList.add(new GuiButton(4, 5, 5, 60, 20, I18n.format("Add", new Object[0])));
        this.buttonList.add(new GuiButton(5, GuiMusic.width - 65, 5, 60, 20, I18n.format("Remove", new Object[0])));
        (this.radioGuiList = new GuiList(this.mc, GuiMusic.width, GuiMusic.height, 32, GuiMusic.height - 32, 10)).registerScrollButtons(4, 5);
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) {
        final int id = button.id;
        if (id == 0) {
            this.mc.displayGuiScreen(this.before);
        }
        if (id == 1) {
            if (GuiMusic.isRunning) {
                GuiMusic.player.close();
                new Thread(() -> playMusic(GuiMusic.music_link)).stop();
                button.displayString = "§aStart";
                GuiMusic.isRunning = false;
            }
            else {
                this.refreshLink(GuiMusic.channelId);
                new Thread(() -> playMusic(GuiMusic.music_link)).start();
                button.displayString = "§cStop";
                GuiMusic.isRunning = true;
            }
        }
        if (id == 2) {
            GuiMusic.currentVolume += 5.0f;
            if (GuiMusic.currentVolume >= -10.0f) {
                GuiMusic.currentVolume = -10.0f;
            }
            if (GuiMusic.player != null) {
                GuiMusic.player.setGain(GuiMusic.currentVolume);
            }
        }
        if (id == 3) {
            GuiMusic.currentVolume -= 5.0f;
            if (GuiMusic.currentVolume < -60.0f) {
                GuiMusic.currentVolume = -60.0f;
            }
            if (GuiMusic.player != null) {
                GuiMusic.player.setGain(GuiMusic.currentVolume);
            }
        }
        if (button.id == 5) {
            RadioList.removeSender(this.radioSender.get(this.selectedRadio), this.radioUrl.get(this.selectedRadio));
        }
        if (button.id == 4) {
            RadioList.addSender(this.name.getText(), this.stream.getText());
        }
    }
    
    public boolean doesGustreamauseGame() {
        return true;
    }
    
    @Override
    protected void keyTyped(final char character, final int key) throws IOException {
        try {
            super.keyTyped(character, key);
        }
        catch (final IOException e) {
            e.printStackTrace();
        }
        if (character == '\r') {
            this.actionPerformed(this.buttonList.get(0));
        }
        this.stream.textboxKeyTyped(character, key);
        this.name.textboxKeyTyped(character, key);
    }
    
    @Override
    protected void mouseClicked(final int x, final int y, final int button) {
        try {
            super.mouseClicked(x, y, button);
        }
        catch (final IOException e) {
            e.printStackTrace();
        }
        this.stream.mouseClicked(x, y, button);
        this.name.mouseClicked(x, y, button);
    }
    
    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }
    
    @Override
    public void handleMouseInput() throws IOException {
        this.radioGuiList.handleMouseInput();
        super.handleMouseInput();
    }
    
    @Override
    public void updateScreen() {
        this.stream.updateCursorCounter();
        this.name.updateCursorCounter();
        this.refreshLink(GuiMusic.channelId);
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        GlStateManager.scale(4.0f, 4.0f, 1.0f);
        GlStateManager.scale(0.5, 0.5, 1.0);
        GlStateManager.scale(0.5, 0.5, 1.0);
        this.stream.drawTextBox();
        this.name.drawTextBox();
        this.radioGuiList.drawScreen(mouseX, mouseY, partialTicks);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    public static void playMusic(final String url) {
        BufferedInputStream inputStream = null;
        try {
            inputStream = new BufferedInputStream(new URL(url).openStream());
        }
        catch (final Exception ex2) {}
        try {
            GuiMusic.currentVolume = -30.0f;
            (GuiMusic.player = new Player(inputStream)).setGain(GuiMusic.currentVolume);
            GuiMusic.player.play();
        }
        catch (final Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private void refreshLink(final int id) {
        GuiMusic.URL = this.stream.getText().replace("https", "http");
        GuiMusic.music_link = GuiMusic.URL;
    }
    
    static /* synthetic */ void access$1(final GuiMusic guiMusic, final int selectedRadio) {
        guiMusic.selectedRadio = selectedRadio;
    }
    
    class GuiList extends GuiSlot
    {
        public GuiList(final Minecraft mcIn, final int width, final int height, final int topIn, final int bottomIn, final int slotHeightIn) {
            super(mcIn, width, height, topIn, bottomIn, 12);
        }
        
        @Override
        protected int getSize() {
            return GuiMusic.this.radioSender.size();
        }
        
        @Override
        protected void elementClicked(final int slotIndex, final boolean isDoubleClick, final int mouseX, final int mouseY) {
            GuiMusic.access$1(GuiMusic.this, slotIndex);
            GuiMusic.this.stream.setText(GuiMusic.this.radioUrl.get(GuiMusic.this.selectedRadio));
            GuiMusic.this.name.setText(GuiMusic.this.radioSender.get(GuiMusic.this.selectedRadio));
        }
        
        @Override
        protected boolean isSelected(final int slotIndex) {
            return slotIndex == GuiMusic.this.selectedRadio;
        }
        
        @Override
        protected int getContentHeight() {
            return this.getSize() * 12;
        }
        
        @Override
        protected void drawBackground() {
        }
        
        @Override
        public void scrollBy(final int amount) {
            super.scrollBy(amount);
        }
        
        @Override
        protected void func_192637_a(final int p_192637_1_, final int p_192637_2_, final int p_192637_3_, final int p_192637_4_, final int p_192637_5_, final int p_192637_6_, final float p_192637_7_) {
            final String name = GuiMusic.this.radioSender.get(p_192637_1_);
            Gui.drawCenteredString(this.mc.fontRendererObj, name, this.width / 2, p_192637_3_, -1);
        }
    }
}
