// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.gui;

import net.labymod.utils.Consumer;
import java.util.Collection;
import java.util.ArrayList;
import java.io.IOException;
import net.labymod.utils.manager.TooltipHelper;
import net.labymod.main.ModTextures;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import java.util.Iterator;
import java.util.List;
import net.labymod.utils.DrawUtils;
import net.labymod.utils.ModColor;
import net.labymod.main.lang.LanguageManager;
import net.labymod.main.LabyMod;
import net.labymod.gui.elements.Scrollbar;
import net.labymod.account.LauncherProfile;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiAccountManager extends GuiScreen
{
    private final GuiScreen lastScreen;
    private GuiButton buttonSwitch;
    private GuiButton buttonRemove;
    private LauncherProfile selectedProfile;
    private LauncherProfile hoverProfile;
    private Scrollbar scrollbar;
    private long lastLoginClick;
    private boolean displayError;
    
    public GuiAccountManager(final GuiScreen lastScreen) {
        this.lastLoginClick = 0L;
        this.displayError = false;
        this.lastScreen = lastScreen;
        LabyMod.getInstance().getAccountManager().loadLauncherProfiles();
    }
    
    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, GuiAccountManager.width / 2 - 180, GuiAccountManager.height - 25, 60, 20, LanguageManager.translate("button_add")));
        this.buttonList.add(this.buttonRemove = new GuiButton(1, GuiAccountManager.width / 2 - 110, GuiAccountManager.height - 25, 60, 20, LanguageManager.translate("button_remove")));
        this.buttonList.add(this.buttonSwitch = new GuiButton(2, GuiAccountManager.width / 2 - 40, GuiAccountManager.height - 25, 60, 20, LanguageManager.translate("button_switch")));
        this.buttonList.add(new GuiButton(3, GuiAccountManager.width / 2 + 30, GuiAccountManager.height - 25, 80, 20, LanguageManager.translate("button_direct_login")));
        this.buttonList.add(new GuiButton(4, GuiAccountManager.width / 2 + 120, GuiAccountManager.height - 25, 60, 20, LanguageManager.translate("button_cancel")));
        (this.scrollbar = new Scrollbar(21)).setSpeed(15);
        this.scrollbar.setPosition(GuiAccountManager.width / 2 + 100, 35, GuiAccountManager.width / 2 + 104, GuiAccountManager.height - 35);
        this.scrollbar.update(LabyMod.getInstance().getAccountManager().getLauncherProfiles().size());
        this.displayError = false;
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        final DrawUtils draw = LabyMod.getInstance().getDrawUtils();
        this.drawBackground(0);
        draw.drawOverlayBackground(0, 30, GuiAccountManager.width, GuiAccountManager.height - 30, 32);
        final List<LauncherProfile> launcherProfiles = LabyMod.getInstance().getAccountManager().getLauncherProfiles();
        this.hoverProfile = null;
        final int entryWidth = 132;
        double y = 35.0 + this.scrollbar.getScrollY();
        final int x = GuiAccountManager.width / 2 - 66;
        for (final LauncherProfile launcherProfile : launcherProfiles) {
            this.drawProfile(launcherProfile, x, y, mouseX, mouseY);
            y += 21.0;
        }
        draw.drawOverlayBackground(0, 30);
        draw.drawOverlayBackground(GuiAccountManager.height - 30, GuiAccountManager.height);
        draw.drawGradientShadowTop(30.0, 0.0, GuiAccountManager.width);
        draw.drawGradientShadowBottom(GuiAccountManager.height - 30, 0.0, GuiAccountManager.width);
        draw.drawCenteredString(String.valueOf(LanguageManager.translate("title_account_manager")) + " (" + launcherProfiles.size() + " " + LanguageManager.translate((launcherProfiles.size() == 1) ? "account" : "accounts") + ")", GuiAccountManager.width / 2, 12.0);
        if (this.displayError) {
            draw.drawCenteredString(String.valueOf(ModColor.cl("4")) + LabyMod.getInstance().getAccountManager().getLastErrorMessage(), GuiAccountManager.width / 2, GuiAccountManager.height - 30 - 12);
        }
        this.scrollbar.draw();
        this.buttonSwitch.enabled = (this.selectedProfile != null);
        this.buttonRemove.enabled = (this.selectedProfile != null);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    private void drawProfile(final LauncherProfile launcherProfile, final int x, final double y, final int mouseX, final int mouseY) {
        final DrawUtils draw = LabyMod.getInstance().getDrawUtils();
        final int entryWidth = 132;
        final int entryHeight = 20;
        final boolean loggedIn = launcherProfile.getGameProfile().getId().equals(LabyMod.getInstance().getPlayerUUID());
        final boolean hover = mouseX > x && mouseX < x + 132 && mouseY > y && mouseY < y + 20.0;
        final boolean selected = launcherProfile == this.selectedProfile;
        final boolean offline = launcherProfile.getAccessToken().isEmpty();
        double selectAnimation = (System.currentTimeMillis() - this.lastLoginClick) / 2.0;
        if (selectAnimation > 100.0) {
            selectAnimation = 100.0;
        }
        if (selected) {
            draw.drawRectBorder(x, y, x + 132, y + 20.0, Integer.MAX_VALUE, 1.0);
        }
        else if (hover) {
            draw.drawRectangle(x, (int)y, x + 132, (int)(y + 20.0), ModColor.toRGB(120, 120, 120, 100));
        }
        if (selected) {
            draw.drawRectangle(x, (int)y, x + 132, (int)(y + 20.0), ModColor.toRGB(120, 120, 120, 100 - (int)selectAnimation));
        }
        final float headBrightness = (selected || (hover | loggedIn)) ? 1.0f : 0.7f;
        GlStateManager.color(headBrightness, headBrightness, headBrightness);
        LabyMod.getInstance().getDrawUtils().drawPlayerHead(launcherProfile.getGameProfile(), x + 2, (int)y + 2, 16);
        final String namePrefix = offline ? ModColor.cl("c") : (loggedIn ? ModColor.cl("a") : "");
        final String nameSuffix = offline ? (String.valueOf(ModColor.cl("4")) + " (" + LanguageManager.translate("offline") + ")") : "";
        draw.drawString(String.valueOf(namePrefix) + launcherProfile.getGameProfile().getName() + nameSuffix, x + 2 + 16 + 4, y + 3.0);
        draw.drawString(String.valueOf(ModColor.cl("7")) + launcherProfile.getGameProfile().getId().toString(), x + 2 + 16 + 4, y + 3.0 + 10.0, 0.5);
        if (loggedIn) {
            Minecraft.getMinecraft().getTextureManager().bindTexture(ModTextures.BUTTON_CHECKBOX);
            draw.drawTexture(x - 15, y + 5.0, 255.0, 255.0, 10.0, 10.0);
        }
        if (hover) {
            this.hoverProfile = launcherProfile;
            TooltipHelper.getHelper().pointTooltip(mouseX, mouseY, launcherProfile.getMojangUsername());
        }
    }
    
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (mouseY < GuiAccountManager.height - 30) {
            this.selectedProfile = this.hoverProfile;
        }
        if (this.selectedProfile != null && this.hoverProfile != null) {
            if (this.lastLoginClick + 200L > System.currentTimeMillis()) {
                this.actionPerformed(this.buttonSwitch);
            }
            this.lastLoginClick = System.currentTimeMillis();
        }
        this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.CLICKED);
    }
    
    @Override
    protected void mouseReleased(final int mouseX, final int mouseY, final int state) {
        super.mouseReleased(mouseX, mouseY, state);
        this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.RELEASED);
    }
    
    @Override
    protected void mouseClickMove(final int mouseX, final int mouseY, final int clickedMouseButton, final long timeSinceLastClick) {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.DRAGGING);
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        super.actionPerformed(button);
        switch (button.id) {
            case 0: {
                Minecraft.getMinecraft().displayGuiScreen(new GuiAccountDirectLogin(this, true));
                break;
            }
            case 1: {
                final List<LauncherProfile> list = new ArrayList<LauncherProfile>(LabyMod.getInstance().getAccountManager().getLauncherProfiles());
                list.remove(this.selectedProfile);
                LabyMod.getInstance().getAccountManager().setLauncherProfiles(list);
                this.selectedProfile = null;
                LabyMod.getInstance().getAccountManager().saveLauncherProfiles(new Consumer<Boolean>() {
                    @Override
                    public void accept(final Boolean accepted) {
                        if (!accepted) {
                            GuiAccountManager.access$0(GuiAccountManager.this, true);
                        }
                    }
                });
                break;
            }
            case 2: {
                this.selectedProfile.login(new Consumer<Boolean>() {
                    @Override
                    public void accept(final Boolean response) {
                        if (response) {
                            Minecraft.getMinecraft().displayGuiScreen(GuiAccountManager.this.lastScreen);
                        }
                        else {
                            GuiAccountManager.access$0(GuiAccountManager.this, true);
                        }
                    }
                });
                break;
            }
            case 3: {
                Minecraft.getMinecraft().displayGuiScreen(new GuiAccountDirectLogin(this, false));
                break;
            }
            case 4: {
                Minecraft.getMinecraft().displayGuiScreen(this.lastScreen);
                break;
            }
        }
    }
    
    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.scrollbar.mouseInput();
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (keyCode == 1) {
            Minecraft.getMinecraft().displayGuiScreen(this.lastScreen);
            return;
        }
        super.keyTyped(typedChar, keyCode);
    }
    
    static /* synthetic */ void access$0(final GuiAccountManager guiAccountManager, final boolean displayError) {
        guiAccountManager.displayError = displayError;
    }
}
