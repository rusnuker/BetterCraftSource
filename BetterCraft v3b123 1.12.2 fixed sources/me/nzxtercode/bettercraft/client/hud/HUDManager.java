// 
// Decompiled by Procyon v0.6.0
// 

package me.nzxtercode.bettercraft.client.hud;

import java.util.Iterator;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.GuiScreen;
import net.lenni0451.eventapi.manager.EventManager;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import java.util.List;

public class HUDManager
{
    private static HUDManager instance;
    private static boolean isPaused;
    private List<IRender> registeredRenderers;
    private Minecraft mc;
    
    static {
        HUDManager.instance = null;
    }
    
    public HUDManager() {
        this.registeredRenderers = new ArrayList<IRender>();
        this.mc = Minecraft.getMinecraft();
    }
    
    public static HUDManager getInstance() {
        if (HUDManager.instance != null) {
            return HUDManager.instance;
        }
        EventManager.register((Object)(HUDManager.instance = new HUDManager()));
        return HUDManager.instance;
    }
    
    public void register(final IRender... renderers) {
        for (final IRender irender : renderers) {
            this.registeredRenderers.add(irender);
        }
    }
    
    public void unregister(final IRender... renderers) {
        for (final IRender irender : renderers) {
            this.registeredRenderers.remove(irender);
        }
    }
    
    public List<IRender> getRegisteredRenderers() {
        return this.registeredRenderers;
    }
    
    public void openConfigScreen() {
        HUDManager.isPaused = false;
        this.mc.displayGuiScreen(new HUDConfigScreen(this, true));
    }
    
    public void openConfigScreenPaused() {
        HUDManager.isPaused = true;
        this.mc.displayGuiScreen(new HUDConfigScreen(this, false));
    }
    
    public void render() {
        if ((this.mc.currentScreen == null || this.mc.currentScreen instanceof GuiContainer) && !this.mc.gameSettings.showDebugInfo) {
            for (final IRender irender : this.registeredRenderers) {
                this.callRenderer(irender);
            }
        }
    }
    
    private void callRenderer(final IRender renderer) {
        if (renderer.isEnabled()) {
            ScreenPosition screenposition = renderer.load();
            if (screenposition == null) {
                screenposition = ScreenPosition.fromAbsolute(0, 0);
            }
            renderer.render(screenposition);
        }
    }
    
    public static boolean isPaused() {
        return HUDManager.isPaused;
    }
    
    public static void setPaused(final boolean isPaused) {
        HUDManager.isPaused = isPaused;
    }
}
