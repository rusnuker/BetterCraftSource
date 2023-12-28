// 
// Decompiled by Procyon v0.6.0
// 

package net.montoyo.mcef.example;

import me.amkgre.bettercraft.client.gui.GuiTools;
import org.lwjgl.input.Mouse;
import net.minecraft.client.renderer.GlStateManager;
import net.montoyo.mcef.api.API;
import org.lwjgl.input.Keyboard;
import net.montoyo.mcef.api.MCEFApi;
import net.montoyo.mcef.MCEF;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.GuiButton;
import net.montoyo.mcef.api.IBrowser;
import net.minecraft.client.gui.GuiScreen;

public class BrowserScreen extends GuiScreen
{
    IBrowser browser;
    private GuiButton back;
    private GuiButton fwd;
    private GuiButton go;
    private GuiButton min;
    private GuiButton vidMode;
    private GuiTextField url;
    private String urlToLoad;
    private static final String YT_REGEX1 = "^https?://(?:www\\.)?youtube\\.com/watch\\?v=([a-zA-Z0-9_\\-]+)$";
    private static final String YT_REGEX2 = "^https?://(?:www\\.)?youtu\\.be/([a-zA-Z0-9_\\-]+)$";
    private static final String YT_REGEX3 = "^https?://(?:www\\.)?youtube\\.com/embed/([a-zA-Z0-9_\\-]+)(\\?.+)?$";
    
    public BrowserScreen() {
        this.browser = null;
        this.back = null;
        this.fwd = null;
        this.go = null;
        this.min = null;
        this.vidMode = null;
        this.url = null;
        this.urlToLoad = null;
        this.urlToLoad = MCEF.HOME_PAGE;
    }
    
    public BrowserScreen(final String url) {
        this.browser = null;
        this.back = null;
        this.fwd = null;
        this.go = null;
        this.min = null;
        this.vidMode = null;
        this.url = null;
        this.urlToLoad = null;
        this.urlToLoad = ((url == null) ? MCEF.HOME_PAGE : url);
    }
    
    @Override
    public void initGui() {
        ExampleMod.INSTANCE.hudBrowser = null;
        if (this.browser == null) {
            final API api = MCEFApi.getAPI();
            if (api == null) {
                return;
            }
            this.browser = api.createBrowser((this.urlToLoad == null) ? MCEF.HOME_PAGE : this.urlToLoad, false);
            this.urlToLoad = null;
        }
        if (this.browser != null) {
            this.browser.resize(this.mc.displayWidth, this.mc.displayHeight - this.scaleY(20));
        }
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        if (this.url == null) {
            this.buttonList.add(this.back = new GuiButton(0, 0, 0, 20, 20, "<"));
            this.buttonList.add(this.fwd = new GuiButton(1, 20, 0, 20, 20, ">"));
            this.buttonList.add(this.go = new GuiButton(2, BrowserScreen.width - 60, 0, 20, 20, "Go"));
            this.buttonList.add(this.min = new GuiButton(3, BrowserScreen.width - 20, 0, 20, 20, "_"));
            this.buttonList.add(this.vidMode = new GuiButton(4, BrowserScreen.width - 40, 0, 20, 20, "YT"));
            this.vidMode.enabled = false;
            (this.url = new GuiTextField(5, this.fontRendererObj, 40, 0, BrowserScreen.width - 100, 20)).setMaxStringLength(65535);
        }
        else {
            this.buttonList.add(this.back);
            this.buttonList.add(this.fwd);
            this.buttonList.add(this.go);
            this.buttonList.add(this.min);
            this.buttonList.add(this.vidMode);
            this.vidMode.xPosition = BrowserScreen.width - 40;
            this.go.xPosition = BrowserScreen.width - 60;
            this.min.xPosition = BrowserScreen.width - 20;
            final String old = this.url.getText();
            (this.url = new GuiTextField(5, this.fontRendererObj, 40, 0, BrowserScreen.width - 100, 20)).setMaxStringLength(65535);
            this.url.setText(old);
        }
    }
    
    public int scaleY(final int y) {
        final double sy = y / (double)BrowserScreen.height * this.mc.displayHeight;
        return (int)sy;
    }
    
    public void loadURL(final String url) {
        if (this.browser == null) {
            this.urlToLoad = url;
        }
        else {
            this.browser.loadURL(url);
        }
    }
    
    @Override
    public void updateScreen() {
        if (this.urlToLoad != null && this.browser != null) {
            this.browser.loadURL(this.urlToLoad);
            this.urlToLoad = null;
        }
    }
    
    @Override
    public void drawScreen(final int i1, final int i2, final float f) {
        this.url.drawTextBox();
        super.drawScreen(i1, i2, f);
        if (this.browser != null) {
            GlStateManager.disableDepth();
            GlStateManager.enableTexture2D();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            this.browser.draw(0.0, BrowserScreen.height, BrowserScreen.width, 20.0);
            GlStateManager.enableDepth();
        }
    }
    
    @Override
    public void onGuiClosed() {
        if (!ExampleMod.INSTANCE.hasBackup() && this.browser != null) {
            this.browser.close();
        }
        Keyboard.enableRepeatEvents(false);
    }
    
    @Override
    public void handleInput() {
        while (Keyboard.next()) {
            if (Keyboard.getEventKey() == 1) {
                this.mc.displayGuiScreen(null);
                return;
            }
            final boolean pressed = Keyboard.getEventKeyState();
            final boolean focused = this.url.isFocused();
            final char key = Keyboard.getEventCharacter();
            final int num = Keyboard.getEventKey();
            if (this.browser != null && !focused) {
                if (pressed) {
                    this.browser.injectKeyPressedByKeyCode(num, key, 0);
                }
                else {
                    this.browser.injectKeyReleasedByKeyCode(num, key, 0);
                }
                if (key != '\0') {
                    this.browser.injectKeyTyped(key, 0);
                }
            }
            if (!pressed && focused && num == 28) {
                this.actionPerformed(this.go);
            }
            else {
                if (!pressed) {
                    continue;
                }
                this.url.textboxKeyTyped(key, num);
            }
        }
        while (Mouse.next()) {
            final int btn = Mouse.getEventButton();
            final boolean pressed2 = Mouse.getEventButtonState();
            final int sx = Mouse.getEventX();
            final int sy = Mouse.getEventY();
            final int wheel = Mouse.getEventDWheel();
            if (this.browser != null) {
                final int y = this.mc.displayHeight - sy - this.scaleY(20);
                if (wheel != 0) {
                    this.browser.injectMouseWheel(sx, y, 0, 1, wheel);
                }
                else if (btn == -1) {
                    this.browser.injectMouseMove(sx, y, 0, y < 0);
                }
                else {
                    this.browser.injectMouseButton(sx, y, 0, btn + 1, pressed2, 1);
                }
            }
            if (pressed2) {
                final int x = sx * BrowserScreen.width / this.mc.displayWidth;
                final int y2 = BrowserScreen.height - sy * BrowserScreen.height / this.mc.displayHeight - 1;
                try {
                    this.mouseClicked(x, y2, btn);
                }
                catch (final Throwable t) {
                    t.printStackTrace();
                }
                this.url.mouseClicked(x, y2, btn);
            }
        }
    }
    
    public void onUrlChanged(final IBrowser b, final String nurl) {
        if (b == this.browser && this.url != null) {
            this.url.setText(nurl);
            this.vidMode.enabled = (nurl.matches("^https?://(?:www\\.)?youtube\\.com/watch\\?v=([a-zA-Z0-9_\\-]+)$") || nurl.matches("^https?://(?:www\\.)?youtu\\.be/([a-zA-Z0-9_\\-]+)$") || nurl.matches("^https?://(?:www\\.)?youtube\\.com/embed/([a-zA-Z0-9_\\-]+)(\\?.+)?$"));
        }
    }
    
    @Override
    protected void actionPerformed(final GuiButton src) {
        if (this.browser == null) {
            return;
        }
        if (src.id == 0) {
            this.browser.goBack();
        }
        else if (src.id == 1) {
            this.browser.goForward();
        }
        else if (src.id == 2) {
            final String fixedURL = ExampleMod.INSTANCE.getAPI().punycode(this.url.getText());
            this.browser.loadURL(fixedURL);
        }
        else if (src.id == 3) {
            ExampleMod.INSTANCE.setBackup(this);
            this.mc.displayGuiScreen(new GuiTools(null));
        }
        else if (src.id == 4) {
            final String loc = this.browser.getURL();
            String vId = null;
            boolean redo = false;
            if (loc.matches("^https?://(?:www\\.)?youtube\\.com/watch\\?v=([a-zA-Z0-9_\\-]+)$")) {
                vId = loc.replaceFirst("^https?://(?:www\\.)?youtube\\.com/watch\\?v=([a-zA-Z0-9_\\-]+)$", "$1");
            }
            else if (loc.matches("^https?://(?:www\\.)?youtu\\.be/([a-zA-Z0-9_\\-]+)$")) {
                vId = loc.replaceFirst("^https?://(?:www\\.)?youtu\\.be/([a-zA-Z0-9_\\-]+)$", "$1");
            }
            else if (loc.matches("^https?://(?:www\\.)?youtube\\.com/embed/([a-zA-Z0-9_\\-]+)(\\?.+)?$")) {
                redo = true;
            }
            if (vId != null || redo) {
                ExampleMod.INSTANCE.setBackup(this);
                this.mc.displayGuiScreen(new ScreenCfg(this.browser, vId));
            }
        }
    }
}
