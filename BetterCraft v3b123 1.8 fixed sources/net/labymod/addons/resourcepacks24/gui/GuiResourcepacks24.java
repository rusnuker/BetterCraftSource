// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.addons.resourcepacks24.gui;

import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.labymod.utils.DrawUtils;
import net.labymod.main.LabyMod;
import java.util.Iterator;
import net.labymod.addons.resourcepacks24.gui.views.SelectedView;
import net.labymod.addons.resourcepacks24.gui.views.OfflineView;
import net.labymod.addons.resourcepacks24.gui.views.OnlineView;
import java.util.ArrayList;
import net.minecraft.client.gui.GuiButton;
import net.labymod.addons.resourcepacks24.gui.views.View;
import java.util.List;
import net.labymod.addons.resourcepacks24.gui.views.shared.SharedView;
import net.labymod.addons.resourcepacks24.loader.PackRepositoryLoader;
import net.labymod.addons.resourcepacks24.Resourcepacks24;
import net.minecraft.client.gui.GuiScreen;

public class GuiResourcepacks24 extends GuiScreen
{
    private GuiScreen lastScreen;
    private Resourcepacks24 resourcepacks24;
    public PackRepositoryLoader.Repository repository;
    private SharedView sharedView;
    private List<View> views;
    private GuiButton buttonApply;
    
    public GuiResourcepacks24(final GuiScreen lastScreen, final Resourcepacks24 resourcepacks24) {
        this.repository = new PackRepositoryLoader.Repository();
        this.sharedView = new SharedView(this);
        this.views = new ArrayList<View>();
        this.lastScreen = lastScreen;
        this.resourcepacks24 = resourcepacks24;
        this.views.add(new OnlineView(this));
        this.views.add(new OfflineView(this));
        this.views.add(new SelectedView(this));
        this.reloadRepositories();
    }
    
    public void reloadRepositories() {
        for (final View view : this.views) {
            view.loadRepository(this.resourcepacks24);
        }
    }
    
    @Override
    public void initGui() {
        super.initGui();
        this.repository = this.resourcepacks24.getPackLoader().getRepository().clone();
        final double margin = 10.0;
        final double panelWidth = (int)((GuiResourcepacks24.width - 40.0) / 3.0);
        int index = 0;
        for (final View view : this.views) {
            view.init(10.0 + index * (panelWidth + 10.0), 24.0, panelWidth, GuiResourcepacks24.height - 20.0 - 14.0 - ((index != 1) ? ((index == 0) ? 10 : 25) : 0), 10.0);
            ++index;
        }
        final double buttonWidth = panelWidth / 2.0 - 10.0;
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, (int)(GuiResourcepacks24.width - 10.0 - panelWidth), GuiResourcepacks24.height - 30, (int)buttonWidth, 20, "Cancel"));
        this.buttonList.add(this.buttonApply = new GuiButton(1, (int)(GuiResourcepacks24.width - 10.0 - buttonWidth), GuiResourcepacks24.height - 30, (int)buttonWidth, 20, "Apply"));
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        this.sharedView.preRender(mouseX, mouseY);
        for (final View view : this.views) {
            final DrawUtils draw = LabyMod.getInstance().getDrawUtils();
            DrawUtils.startScissor((float)view.x, (float)view.y, (float)(view.x + view.width), (float)(view.y + view.height));
            view.renderPre(mouseX, mouseY);
            DrawUtils.stopScissor();
            view.renderPost(mouseX, mouseY);
        }
        this.sharedView.postRender(mouseX, mouseY);
        this.buttonApply.hovered = this.sharedView.changes;
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        super.actionPerformed(button);
        switch (button.id) {
            case 0: {
                this.resourcepacks24.getRp24Api().getDynamicIconManager().unloadAll();
                this.resourcepacks24.getPackLoader().loadAsync();
                Minecraft.getMinecraft().displayGuiScreen(this.lastScreen);
                break;
            }
            case 1: {
                this.resourcepacks24.getRp24Api().getDynamicIconManager().unloadAll();
                this.resourcepacks24.getPackLoader().getMinecraftRepositoryHandler().saveResourceList();
                this.resourcepacks24.getPackLoader().getMinecraftRepositoryHandler().refreshResources();
                Minecraft.getMinecraft().displayGuiScreen(this.lastScreen);
                break;
            }
        }
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (keyCode == 1) {
            this.resourcepacks24.getRp24Api().getDynamicIconManager().unloadAll();
            this.resourcepacks24.getPackLoader().loadAsync();
            Minecraft.getMinecraft().displayGuiScreen(this.lastScreen);
        }
        else {
            super.keyTyped(typedChar, keyCode);
        }
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.sharedView.mouseClicked(mouseX, mouseY, mouseButton);
        for (final View view : this.views) {
            view.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }
    
    @Override
    protected void mouseClickMove(final int mouseX, final int mouseY, final int clickedMouseButton, final long timeSinceLastClick) {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        this.sharedView.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        for (final View view : this.views) {
            view.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        }
    }
    
    @Override
    protected void mouseReleased(final int mouseX, final int mouseY, final int state) {
        super.mouseReleased(mouseX, mouseY, state);
        this.sharedView.mouseReleased(mouseX, mouseY, state);
        for (final View view : this.views) {
            view.mouseReleased(mouseX, mouseY, state);
        }
    }
    
    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        for (final View view : this.views) {
            view.handleMouseInput();
        }
    }
    
    public GuiScreen getLastScreen() {
        return this.lastScreen;
    }
    
    public Resourcepacks24 getResourcepacks24() {
        return this.resourcepacks24;
    }
    
    public PackRepositoryLoader.Repository getRepository() {
        return this.repository;
    }
    
    public SharedView getSharedView() {
        return this.sharedView;
    }
    
    public List<View> getViews() {
        return this.views;
    }
    
    public GuiButton getButtonApply() {
        return this.buttonApply;
    }
}
