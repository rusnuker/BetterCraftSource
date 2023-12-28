// 
// Decompiled by Procyon v0.6.0
// 

package net.montoyo.mcef.example;

import net.montoyo.mcef.utilities.Log;
import net.montoyo.mcef.api.IJSQueryCallback;
import net.montoyo.mcef.api.IBrowser;
import net.minecraft.client.gui.GuiScreen;
import com.darkmagician6.eventapi.EventManager;
import net.montoyo.mcef.api.IScheme;
import net.montoyo.mcef.api.MCEFApi;
import net.montoyo.mcef.api.API;
import net.minecraft.client.Minecraft;
import net.montoyo.mcef.api.IJSQueryHandler;
import net.montoyo.mcef.api.IDisplayHandler;

public class ExampleMod implements IDisplayHandler, IJSQueryHandler
{
    public static ExampleMod INSTANCE;
    public ScreenCfg hudBrowser;
    private Minecraft mc;
    private BrowserScreen backup;
    private API api;
    
    public ExampleMod() {
        this.hudBrowser = null;
        this.mc = Minecraft.getMinecraft();
        this.backup = null;
    }
    
    public API getAPI() {
        return this.api;
    }
    
    public void onPreInit() {
        System.out.println("Fuck");
        this.api = MCEFApi.getAPI();
        if (this.api == null) {
            return;
        }
        this.api.registerScheme("mod", ModScheme.class, true, false, false, true, true, false, false);
    }
    
    public void onInit() {
        ExampleMod.INSTANCE = this;
        System.out.println("-- Registering screen");
        EventManager.register(this);
        if (this.api != null) {
            this.api.registerDisplayHandler(this);
            this.api.registerJSQueryHandler(this);
        }
    }
    
    public void setBackup(final BrowserScreen bu) {
        this.backup = bu;
    }
    
    public boolean hasBackup() {
        return this.backup != null;
    }
    
    public void showScreen(final String url) {
        if (Minecraft.currentScreen instanceof BrowserScreen) {
            ((BrowserScreen)Minecraft.currentScreen).loadURL(url);
        }
        else if (this.hasBackup()) {
            this.mc.displayGuiScreen(this.backup);
            this.backup.loadURL(url);
            this.backup = null;
        }
        else {
            this.mc.displayGuiScreen(new BrowserScreen(url));
        }
    }
    
    public IBrowser getBrowser() {
        if (Minecraft.currentScreen instanceof BrowserScreen) {
            return ((BrowserScreen)Minecraft.currentScreen).browser;
        }
        if (this.backup != null) {
            return this.backup.browser;
        }
        return null;
    }
    
    @Override
    public void onAddressChange(final IBrowser browser, final String url) {
        if (Minecraft.currentScreen instanceof BrowserScreen) {
            ((BrowserScreen)Minecraft.currentScreen).onUrlChanged(browser, url);
        }
        else if (this.hasBackup()) {
            this.backup.onUrlChanged(browser, url);
        }
    }
    
    @Override
    public void onTitleChange(final IBrowser browser, final String title) {
    }
    
    @Override
    public void onTooltip(final IBrowser browser, final String text) {
    }
    
    @Override
    public void onStatusMessage(final IBrowser browser, final String value) {
    }
    
    @Override
    public boolean handleQuery(final IBrowser b, final long queryId, final String query, final boolean persistent, final IJSQueryCallback cb) {
        if (b != null && query.equalsIgnoreCase("username")) {
            if (b.getURL().startsWith("mod://")) {
                this.mc.addScheduledTask(() -> {
                    try {
                        final String name = Minecraft.getSession().getUsername();
                        ijsQueryCallback.success(name);
                    }
                    catch (final Throwable t) {
                        ijsQueryCallback.failure(500, "Internal error.");
                        Log.warning("Could not get username from JavaScript:", new Object[0]);
                        t.printStackTrace();
                    }
                    return;
                });
            }
            else {
                cb.failure(403, "Can't access username from external page");
            }
            return true;
        }
        return false;
    }
    
    @Override
    public void cancelQuery(final IBrowser b, final long queryId) {
    }
}
