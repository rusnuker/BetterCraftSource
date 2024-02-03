// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.gui.layout;

import java.io.IOException;
import java.util.Iterator;
import net.labymod.main.LabyMod;
import net.minecraft.client.gui.GuiButton;
import net.labymod.main.lang.LanguageManager;
import org.lwjgl.input.Keyboard;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiScreen;

public abstract class WindowLayout extends GuiScreen
{
    protected List<WindowElement<?>> windowElements;
    
    public WindowLayout() {
        this.windowElements = new ArrayList<WindowElement<?>>();
    }
    
    @Override
    public void initGui() {
        this.buttonList.clear();
        super.initGui();
        Keyboard.enableRepeatEvents(true);
        this.buttonList.add(new GuiButton(20, WindowLayout.width - 55, 5, 50, 20, LanguageManager.translate("account")));
        this.initLayout();
        LabyMod.getInstance().getLabyConnect().setViaServerList(false);
    }
    
    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }
    
    public void initLayout() {
        final List<WindowElement<?>> windowElements = new ArrayList<WindowElement<?>>();
        this.initLayout(windowElements);
        this.windowElements = windowElements;
    }
    
    protected abstract void initLayout(final List<WindowElement<?>> p0);
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        for (final WindowElement<?> element : this.windowElements) {
            element.draw(mouseX, mouseY);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        for (final WindowElement<?> element : this.windowElements) {
            element.mouseClicked(mouseX, mouseY, mouseButton);
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        for (final WindowElement<?> element : this.windowElements) {
            element.actionPerformed(button);
        }
        super.actionPerformed(button);
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        for (final WindowElement<?> element : this.windowElements) {
            element.keyTyped(typedChar, keyCode);
        }
        super.keyTyped(typedChar, keyCode);
    }
    
    @Override
    protected void mouseClickMove(final int mouseX, final int mouseY, final int clickedMouseButton, final long timeSinceLastClick) {
        for (final WindowElement<?> element : this.windowElements) {
            element.mouseClickMove(mouseX, mouseY);
        }
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    }
    
    @Override
    protected void mouseReleased(final int mouseX, final int mouseY, final int state) {
        for (final WindowElement<?> element : this.windowElements) {
            element.mouseReleased(mouseX, mouseY, state);
        }
        super.mouseReleased(mouseX, mouseY, state);
    }
    
    @Override
    public void handleMouseInput() throws IOException {
        for (final WindowElement<?> element : this.windowElements) {
            element.mouseInput();
        }
        super.handleMouseInput();
    }
    
    @Override
    public void updateScreen() {
        for (final WindowElement<?> element : this.windowElements) {
            element.updateScreen();
        }
        super.updateScreen();
    }
    
    public List<GuiButton> getButtonList() {
        return this.buttonList;
    }
}
