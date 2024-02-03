// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.ingamechat.tabs;

import java.io.InputStream;
import java.util.Scanner;
import java.io.DataInputStream;
import net.labymod.settings.elements.SettingsElement;
import net.labymod.main.lang.LanguageManager;
import net.labymod.utils.ModColor;
import net.labymod.core.LabyModCore;
import net.minecraft.client.gui.Gui;
import java.io.IOException;
import org.lwjgl.input.Mouse;
import java.util.HashMap;
import net.labymod.gui.elements.Scrollbar;
import net.labymod.ingamechat.GuiChatCustom;

public class GuiChatSymbols extends GuiChatCustom
{
    private static final char[] SYMBOLS;
    private Scrollbar scrollbar;
    private HashMap<String, Long> pressedAnimation;
    private boolean canScroll;
    
    static {
        SYMBOLS = loadSymbols();
    }
    
    public GuiChatSymbols(final String defaultText) {
        super(defaultText);
        this.scrollbar = new Scrollbar(15);
        this.pressedAnimation = new HashMap<String, Long>();
    }
    
    @Override
    public void initGui() {
        super.initGui();
        this.scrollbar.setPosition(GuiChatSymbols.width - 8, GuiChatSymbols.height - 145, GuiChatSymbols.width - 3, GuiChatSymbols.height - 20);
        this.scrollbar.update(GuiChatSymbols.SYMBOLS.length / 9);
        this.scrollbar.setSpeed(10);
        this.scrollbar.setEntryHeight(10.0);
    }
    
    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        if (this.canScroll) {
            this.scrollbar.mouseInput();
            final int i = Mouse.getEventDWheel();
            if (i != 0) {
                this.mc.ingameGUI.getChatGUI().resetScroll();
            }
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.scrollbar.calc();
        Gui.drawRect(GuiChatSymbols.width - 100, GuiChatSymbols.height - 150, GuiChatSymbols.width - 2, GuiChatSymbols.height - 16, Integer.MIN_VALUE);
        Gui.drawRect(GuiChatSymbols.width - 6, GuiChatSymbols.height - 145, GuiChatSymbols.width - 5, GuiChatSymbols.height - 20, Integer.MIN_VALUE);
        Gui.drawRect(GuiChatSymbols.width - 7, (int)this.scrollbar.getTop(), GuiChatSymbols.width - 4, (int)(this.scrollbar.getTop() + this.scrollbar.getBarLength()), Integer.MAX_VALUE);
        this.canScroll = (mouseX > GuiChatSymbols.width - 100 && mouseX < GuiChatSymbols.width - 2 && mouseY > GuiChatSymbols.height - 150 && mouseY < GuiChatSymbols.height - 16);
        int row = 0;
        int column = 0;
        char[] symbols;
        for (int length = (symbols = GuiChatSymbols.SYMBOLS).length, i = 0; i < length; ++i) {
            final char symbol = symbols[i];
            if (column * 10 + this.scrollbar.getScrollY() > -5.0 && column * 10 + this.scrollbar.getScrollY() < 125.0) {
                final boolean hoverSymbol = mouseX > GuiChatSymbols.width - 93 + row * 10 - 5 && mouseX < GuiChatSymbols.width - 93 + row * 10 + 6 && mouseY > GuiChatSymbols.height - 147 + column * 10 + this.scrollbar.getScrollY() - 5.0 && mouseY < GuiChatSymbols.height - 147 + column * 10 + this.scrollbar.getScrollY() + 6.0;
                Gui.drawCenteredString(LabyModCore.getMinecraft().getFontRenderer(), String.valueOf(symbol), GuiChatSymbols.width - 93 + row * 10, (int)(GuiChatSymbols.height - 147 + column * 10 + this.scrollbar.getScrollY()), hoverSymbol ? -24000 : -1);
            }
            if (++row > 8) {
                row = 0;
                ++column;
            }
        }
        row = 0;
        column = 0;
        String[] color_CODES;
        for (int length2 = (color_CODES = ModColor.COLOR_CODES).length, j = 0; j < length2; ++j) {
            final String code = color_CODES[j];
            final Long longPressed = this.pressedAnimation.get(code);
            final int pressedCode = (int)((longPressed == null) ? 0L : (longPressed - System.currentTimeMillis()));
            final boolean hoverCode = mouseX > GuiChatSymbols.width - 111 - column * 11 && mouseX < GuiChatSymbols.width - 111 + 10 - column * 11 && mouseY > GuiChatSymbols.height - 150 + row * 11 && mouseY < GuiChatSymbols.height - 150 + 10 + row * 11;
            Gui.drawRect(GuiChatSymbols.width - 111 - column * 11, GuiChatSymbols.height - 150 + row * 11, GuiChatSymbols.width - 111 + 10 - column * 11, GuiChatSymbols.height - 150 + 10 + row * 11, (hoverCode || pressedCode != 0) ? ModColor.toRGB(132, 132, 132, (pressedCode == 0) ? 130 : (pressedCode / 4)) : Integer.MIN_VALUE);
            Gui.drawCenteredString(LabyModCore.getMinecraft().getFontRenderer(), String.valueOf(ModColor.cl(code)) + code, GuiChatSymbols.width - 111 - column * 11 + 5, GuiChatSymbols.height - 150 + row * 11 + 1, Integer.MAX_VALUE);
            if (pressedCode < 0) {
                this.pressedAnimation.remove(code);
            }
            if (++row > 11 || code.equals("9") || code.equals("f")) {
                row = 0;
                ++column;
            }
        }
        boolean contains = false;
        String[] color_CODES2;
        for (int length3 = (color_CODES2 = ModColor.COLOR_CODES).length, k = 0; k < length3; ++k) {
            final String code2 = color_CODES2[k];
            if (this.inputField.getText().contains("&" + code2)) {
                contains = true;
                break;
            }
        }
        if (contains) {
            Gui.drawRect(2, GuiChatSymbols.height - 16 - 11, GuiChatSymbols.width - 101, GuiChatSymbols.height - 15, Integer.MIN_VALUE);
            String string;
            for (string = this.inputField.getText().replace("&", ModColor.getCharAsString()); string.contains("  "); string = string.replace("  ", " ")) {}
            this.drawString(LabyModCore.getMinecraft().getFontRenderer(), String.valueOf(ModColor.cl("r")) + string, 4, GuiChatSymbols.height - 16 - 9, Integer.MAX_VALUE);
        }
        this.drawString(LabyModCore.getMinecraft().getFontRenderer(), LanguageManager.translate("ingame_chat_tab_symbols"), GuiChatSymbols.width - 100, GuiChatSymbols.height - 160, -1);
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.CLICKED);
        int row = 0;
        int column = 0;
        char[] symbols;
        for (int length = (symbols = GuiChatSymbols.SYMBOLS).length, i = 0; i < length; ++i) {
            final char symbol = symbols[i];
            if (column * 10 + this.scrollbar.getScrollY() > -5.0 && column * 10 + this.scrollbar.getScrollY() < 125.0) {
                final boolean hoverSymbol = mouseX > GuiChatSymbols.width - 93 + row * 10 - 5 && mouseX < GuiChatSymbols.width - 93 + row * 10 + 6 && mouseY > GuiChatSymbols.height - 147 + column * 10 + this.scrollbar.getScrollY() - 5.0 && mouseY < GuiChatSymbols.height - 147 + column * 10 + this.scrollbar.getScrollY() + 6.0;
                if (hoverSymbol) {
                    this.inputField.textboxKeyTyped(symbol, 0);
                    LabyModCore.getMinecraft().playSound(SettingsElement.BUTTON_PRESS_SOUND, 2.0f);
                    break;
                }
            }
            if (++row > 8) {
                row = 0;
                ++column;
            }
        }
        row = 0;
        column = 0;
        String[] color_CODES;
        for (int length2 = (color_CODES = ModColor.COLOR_CODES).length, j = 0; j < length2; ++j) {
            final String code = color_CODES[j];
            if (mouseX > GuiChatSymbols.width - 111 - column * 11 && mouseX < GuiChatSymbols.width - 111 + 10 - column * 11 && mouseY > GuiChatSymbols.height - 150 + row * 11 && mouseY < GuiChatSymbols.height - 150 + 10 + row * 11) {
                this.inputField.textboxKeyTyped("&".charAt(0), 0);
                this.inputField.textboxKeyTyped(code.charAt(0), 0);
                LabyModCore.getMinecraft().playSound(SettingsElement.BUTTON_PRESS_SOUND, 2.0f);
            }
            if (++row > 11 || code.equals("9") || code.equals("f")) {
                row = 0;
                ++column;
            }
        }
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        final String currentText = this.inputField.getText();
        final int curser = this.inputField.getCursorPosition();
        if (curser > 1 && currentText.length() > 0) {
            final String curserText = String.valueOf(typedChar);
            boolean isColorCode = false;
            String[] color_CODES;
            for (int length = (color_CODES = ModColor.COLOR_CODES).length, i = 0; i < length; ++i) {
                final String code = color_CODES[i];
                if (code.equals(curserText)) {
                    isColorCode = true;
                    break;
                }
            }
            if (curser > 1 && isColorCode && String.valueOf(currentText.charAt(curser - 2)).equals("&")) {
                this.pressedAnimation.put(curserText, System.currentTimeMillis() + 1000L);
            }
        }
    }
    
    @Override
    protected void mouseClickMove(final int mouseX, final int mouseY, final int clickedMouseButton, final long timeSinceLastClick) {
        this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.DRAGGING);
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    }
    
    @Override
    protected void mouseReleased(final int mouseX, final int mouseY, final int state) {
        this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.RELEASED);
        super.mouseReleased(mouseX, mouseY, state);
    }
    
    private static char[] loadSymbols() {
        final InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("assets/minecraft/labymod/data/symbols.txt");
        final DataInputStream dis = new DataInputStream(inputStream);
        String string = "";
        final Scanner scanner = new Scanner(dis, "UTF-8");
        while (scanner.hasNext()) {
            string = String.valueOf(string) + scanner.next();
        }
        scanner.close();
        return string.toCharArray();
    }
}
