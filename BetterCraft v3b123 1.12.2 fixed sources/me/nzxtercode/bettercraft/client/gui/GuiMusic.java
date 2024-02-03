// 
// Decompiled by Procyon v0.6.0
// 

package me.nzxtercode.bettercraft.client.gui;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiSlot;
import com.google.gson.JsonObject;
import java.util.Objects;
import java.io.InputStream;
import java.net.URL;
import java.io.BufferedInputStream;
import java.util.Map;
import java.io.IOException;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import me.nzxtercode.bettercraft.client.Config;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.input.Keyboard;
import javazoom.jl.player.Player;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.GuiScreen;

public class GuiMusic extends GuiScreen
{
    private GuiScreen parent;
    private static Thread thread;
    private GuiList radioList;
    private GuiTextField stream;
    private GuiTextField name;
    private int selectedRadio;
    private Player player;
    private float volume;
    
    public GuiMusic(final GuiScreen parent) {
        this.volume = -40.0f;
        this.parent = parent;
    }
    
    @Override
    public void updateScreen() {
        this.stream.updateCursorCounter();
        this.name.updateCursorCounter();
    }
    
    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, GuiMusic.width - 70, GuiMusic.height - 26, 60, 20, "Back"));
        this.buttonList.add(new GuiButton(1, GuiMusic.width / 2 - 50, GuiMusic.height - 26, 100, 20, String.format("%s", isEnabled() ? (String.valueOf(EnumChatFormatting.GREEN.toString()) + "On") : (String.valueOf(EnumChatFormatting.RED.toString()) + "Off"))));
        this.buttonList.add(new GuiButton(2, GuiMusic.width / 2 - 100 + 200 - 20, GuiMusic.height - 26, 20, 20, "+"));
        this.buttonList.add(new GuiButton(3, GuiMusic.width / 2 - 100 - 2, GuiMusic.height - 26, 20, 20, "-"));
        this.buttonList.add(new GuiButton(4, 5, 5, 60, 20, "Add"));
        this.buttonList.add(new GuiButton(5, GuiMusic.width - 65, 5, 60, 20, "Remove"));
        (this.stream = new GuiTextField(GuiMusic.height, this.fontRendererObj, GuiMusic.width / 2 - 15, 5, 150, 20)).setMaxStringLength(Integer.MAX_VALUE);
        this.stream.setText(this.getStreamUrl());
        (this.name = new GuiTextField(GuiMusic.height, this.fontRendererObj, GuiMusic.width / 2 - 133, 5, 100, 20)).setMaxStringLength(Integer.MAX_VALUE);
        this.radioList = new GuiList(this.mc, GuiMusic.width, GuiMusic.height, 32, GuiMusic.height - 32, 10);
        super.initGui();
    }
    
    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
        Config.getInstance().editMusic("Music", json -> json.add("id", new JsonPrimitive(this.selectedRadio)));
    }
    
    @Override
    public void handleMouseInput() throws IOException {
        this.radioList.handleMouseInput();
        super.handleMouseInput();
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        switch (button.id) {
            case 0: {
                this.mc.displayGuiScreen(this.parent);
                break;
            }
            case 1: {
                this.startStream();
                button.displayString = String.format("%s", isEnabled() ? (String.valueOf(EnumChatFormatting.GREEN.toString()) + "On") : (String.valueOf(EnumChatFormatting.RED.toString()) + "Off"));
                break;
            }
            case 2: {
                this.volume += 5.0f;
                if (this.volume >= -10.0f) {
                    this.volume = -10.0f;
                }
                if (this.player != null) {
                    this.player.setGain(this.volume);
                    break;
                }
                break;
            }
            case 3: {
                this.volume -= 5.0f;
                if (this.volume < -60.0f) {
                    this.volume = -60.0f;
                }
                if (this.player != null) {
                    this.player.setGain(this.volume);
                    break;
                }
                break;
            }
            case 4: {
                Config.getInstance().editMusic("RadioList", json -> {
                    if ((!this.name.getText().isEmpty() || !this.stream.getText().isEmpty()) && !json.has(this.name.getText())) {
                        json.add(this.name.getText(), new JsonPrimitive(this.stream.getText()));
                    }
                    this.initGui();
                    return;
                });
                break;
            }
            case 5: {
                Config.getInstance().editMusic("RadioList", json -> {
                    final Map.Entry entry = (Map.Entry)Config.getInstance().getMusic("RadioList").getAsJsonObject().entrySet().toArray()[this.selectedRadio];
                    json.remove((String)entry.getKey());
                    this.initGui();
                    return;
                });
                this.selectedRadio = 0;
                break;
            }
        }
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (this.stream.textboxKeyTyped(typedChar, keyCode)) {
            this.buttonList.get(1).enabled = (this.stream.getText().length() > 0 && this.stream.getText().split(":").length > 0);
        }
        this.name.textboxKeyTyped(typedChar, keyCode);
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.stream.mouseClicked(mouseX, mouseY, mouseButton);
        this.name.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        this.radioList.drawScreen(mouseX, mouseY, partialTicks);
        this.drawString(this.fontRendererObj, "Volume: " + EnumChatFormatting.RED.toString() + String.valueOf(this.volume + 65.0f), 2, GuiMusic.height - 10, -1);
        this.stream.drawTextBox();
        this.name.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    public void startStream() {
        if (!isEnabled()) {
            (GuiMusic.thread = new Thread(() -> {
                try {
                    new(javazoom.jl.player.Player.class)();
                    new BufferedInputStream(new URL(this.getStreamUrl()).openStream());
                    final BufferedInputStream stream;
                    new Player(stream);
                    final Player player;
                    (this.player = player).setGain(this.volume);
                    this.player.play();
                }
                catch (final Exception ex) {}
            })).start();
        }
        else {
            if (Objects.nonNull(this.player)) {
                this.player.close();
            }
            if (Objects.nonNull(GuiMusic.thread)) {
                GuiMusic.thread.stop();
                GuiMusic.thread = null;
            }
        }
    }
    
    public static final boolean isEnabled() {
        return Objects.nonNull(GuiMusic.thread);
    }
    
    public final String getStreamUrl() {
        return ((Map.Entry)Config.getInstance().getMusic("RadioList").getAsJsonObject().entrySet().toArray()[this.selectedRadio]).getValue().getAsString().replace("https", "http");
    }
    
    static /* synthetic */ void access$2(final GuiMusic guiMusic, final int selectedRadio) {
        guiMusic.selectedRadio = selectedRadio;
    }
    
    class GuiList extends GuiSlot
    {
        public GuiList(final Minecraft mcIn, final int width, final int height, final int topIn, final int bottomIn, final int slotHeightIn) {
            super(mcIn, width, height, topIn, bottomIn, 12);
        }
        
        @Override
        protected int getSize() {
            return Config.getInstance().getMusic("RadioList").getAsJsonObject().entrySet().size();
        }
        
        @Override
        protected void elementClicked(final int slotIndex, final boolean isDoubleClick, final int mouseX, final int mouseY) {
            final Map.Entry<String, JsonElement> entry = (Map.Entry<String, JsonElement>)Config.getInstance().getMusic("RadioList").getAsJsonObject().entrySet().toArray()[slotIndex];
            final String radioName = entry.getKey();
            final String radioUrl = entry.getValue().getAsString();
            GuiMusic.this.name.setText(radioName);
            GuiMusic.this.stream.setText(radioUrl);
            GuiMusic.access$2(GuiMusic.this, slotIndex);
            GuiMusic.this.startStream();
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
        protected void drawSlot(final int p_192637_1_, final int p_192637_2_, final int p_192637_3_, final int p_192637_4_, final int p_192637_5_, final int p_192637_6) {
            final Map.Entry<String, JsonElement> entry = (Map.Entry<String, JsonElement>)Config.getInstance().getMusic("RadioList").getAsJsonObject().entrySet().toArray()[p_192637_1_];
            final String name = entry.getKey();
            Gui.drawCenteredString(this.mc.fontRendererObj, name, this.width / 2, p_192637_3_, -1);
        }
    }
}
