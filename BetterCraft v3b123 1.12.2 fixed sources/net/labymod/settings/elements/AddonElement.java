// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.settings.elements;

import net.labymod.addon.AddonLoader;
import net.minecraft.util.ResourceLocation;
import java.util.Iterator;
import net.labymod.utils.manager.TooltipHelper;
import net.labymod.main.ModTextures;
import net.minecraft.client.Minecraft;
import net.labymod.addon.online.info.OnlineAddonInfo;
import net.labymod.utils.DrawUtils;
import net.labymod.utils.ModColor;
import net.labymod.main.LabyMod;
import java.util.ArrayList;
import net.labymod.utils.Consumer;
import net.labymod.api.LabyModAddon;
import net.labymod.addon.online.info.AddonInfo;
import java.util.List;

public class AddonElement
{
    private List<SettingsElement> subSettings;
    private AddonInfo addonInfo;
    private LabyModAddon installedAddon;
    private Consumer<AddonInfo.AddonActionState> callbackAction;
    private AddonInfo.AddonActionState lastActionState;
    private boolean mouseOver;
    private int hoverButtonId;
    private boolean canHover;
    private double installProgress;
    
    public AddonElement(final AddonInfo addonInfo, final LabyModAddon installedAddon, final Consumer<AddonInfo.AddonActionState> callbackAction) {
        this.subSettings = new ArrayList<SettingsElement>();
        this.mouseOver = false;
        this.hoverButtonId = -1;
        this.canHover = false;
        this.addonInfo = addonInfo;
        this.installedAddon = installedAddon;
        this.callbackAction = callbackAction;
        this.lastActionState = ((this.installedAddon == null) ? AddonInfo.AddonActionState.INSTALL_REVOKE : AddonInfo.AddonActionState.UNINSTALL_REVOKE);
    }
    
    public void draw(final int x, final int y, final int maxX, final int maxY, final int mouseX, final int mouseY) {
        this.draw(x, y, maxX, maxY, mouseX, mouseY, true);
    }
    
    public void draw(final int x, final int y, final int maxX, final int maxY, final int mouseX, final int mouseY, final boolean showSettingsButton) {
        this.mouseOver = (mouseX > x && mouseX < maxX && mouseY > y && mouseY < maxY && this.canHover);
        final DrawUtils draw = LabyMod.getInstance().getDrawUtils();
        final int iconWidth = this.getEntryHeight();
        int textLineY = y + 3;
        if (this.lastActionState == AddonInfo.AddonActionState.ERROR) {
            draw.drawRectangle(x, y, maxX, maxY, ModColor.toRGB(100, 50, 50, this.isMouseOver() ? 90 : 70));
        }
        else if (this.lastActionState == AddonInfo.AddonActionState.UNINSTALL || (this.lastActionState == AddonInfo.AddonActionState.INSTALL && this.installProgress >= 100.0)) {
            draw.drawRectangle(x, y, maxX, maxY, ModColor.toRGB(50, 50, 100, this.isMouseOver() ? 90 : 70));
        }
        else if (this.lastActionState == AddonInfo.AddonActionState.INSTALL) {
            final double totalWidth = maxX - x - iconWidth;
            final double progressbarWidth = totalWidth / 100.0 * this.installProgress;
            DrawUtils.drawRect(x + iconWidth, y, x + iconWidth + progressbarWidth, maxY, ModColor.toRGB(50, 100, 50, this.isMouseOver() ? 90 : 70));
            DrawUtils.drawRect(x + iconWidth + totalWidth, y, x + iconWidth + progressbarWidth, maxY, ModColor.toRGB(50, this.isAddonInstalled() ? 100 : 50, 50, this.isMouseOver() ? 90 : 70));
        }
        else {
            draw.drawRectangle(x, y, maxX, maxY, ModColor.toRGB(50, this.isAddonInstalled() ? 100 : 50, 50, this.isMouseOver() ? 90 : 70));
        }
        this.drawIcon(x, y, iconWidth, this.getEntryHeight());
        final boolean verified = this.addonInfo instanceof OnlineAddonInfo && ((OnlineAddonInfo)this.addonInfo).isVerified();
        final String titleString = String.valueOf(ModColor.cl(this.isAddonInstalled() ? "a" : (verified ? "e" : "f"))) + ModColor.cl("l") + ModColor.createColors(this.addonInfo.getName());
        draw.drawString(titleString, x + iconWidth + 5, textLineY);
        textLineY += 15;
        final int vX = x + iconWidth + 5 + draw.getStringWidth(titleString) + 3;
        final int vY = y + 3;
        final boolean hoverVerified = mouseX > vX && mouseX < vX + 8 && mouseY > vY && mouseY < vY + 8;
        if (verified) {
            Minecraft.getMinecraft().getTextureManager().bindTexture(ModTextures.MISC_FEATURED);
            draw.drawTexture(vX, vY, 0.0, 0.0, 255.0, 255.0, 8.0, 8.0);
            if (hoverVerified) {
                TooltipHelper.getHelper().pointTooltip(mouseX, mouseY, 0L, String.valueOf(ModColor.cl("e")) + "Featured");
            }
        }
        draw.drawString(String.valueOf(ModColor.cl("a")) + "by " + this.addonInfo.getAuthor(), x + iconWidth + 5, y + 12, 0.5);
        final List<String> descriptionLines = draw.listFormattedStringToWidth(ModColor.createColors(this.addonInfo.getDescription()), (int)((maxX - x - iconWidth - 60) / 0.7));
        int lineCount = 0;
        for (String descriptionLine : descriptionLines) {
            if (lineCount >= 3) {
                descriptionLine = String.valueOf(descriptionLine) + "...";
            }
            draw.drawString(String.valueOf(ModColor.cl("7")) + descriptionLine, x + iconWidth + 5, textLineY, 0.7);
            textLineY += 7;
            if (++lineCount >= 3) {
                break;
            }
        }
        this.hoverButtonId = -1;
        if (this.lastActionState == AddonInfo.AddonActionState.INSTALL_REVOKE) {
            final int marginX = 10;
            final int marginY = (maxY - y - 14) / 2;
            if (this.drawButton(ModTextures.BUTTON_DOWNLOAD, y, 14, 2, 10, marginY, maxX, maxY, mouseX, mouseY)) {
                this.hoverButtonId = 0;
            }
        }
        if (this.lastActionState == AddonInfo.AddonActionState.UNINSTALL_REVOKE) {
            int marginX = 10;
            final int marginY = (maxY - y - 14) / 2;
            if (this.drawButton(ModTextures.BUTTON_TRASH, y, 14, 6, marginX, marginY, maxX, maxY, mouseX, mouseY)) {
                this.hoverButtonId = 1;
            }
            marginX = 30;
            if (showSettingsButton && !this.subSettings.isEmpty() && this.drawButton(ModTextures.BUTTON_ADDON_SETTINGS, y, 14, 6, marginX, marginY, maxX, maxY, mouseX, mouseY)) {
                this.hoverButtonId = 2;
            }
        }
        if (this.lastActionState == AddonInfo.AddonActionState.UNINSTALL) {
            final int marginX = 10;
            final int marginY = (maxY - y - 14) / 2;
            if (this.drawButton(ModTextures.BUTTON_UNDO, y, 14, 2, 10, marginY, maxX, maxY, mouseX, mouseY)) {
                this.hoverButtonId = 3;
            }
            draw.drawRightString(String.valueOf(ModColor.cl("4")) + "Restart required to uninstall", maxX - 2, y + 2, 0.75);
        }
        if (this.lastActionState == AddonInfo.AddonActionState.INSTALL) {
            if (this.installProgress >= 100.0) {
                draw.drawRightString(String.valueOf(ModColor.cl("4")) + "Restart required to install", maxX - 2, y + 2, 0.75);
                final int marginX = 10;
                final int marginY = (maxY - y - 14) / 2;
                if (this.drawButton(ModTextures.BUTTON_UNDO, y, 14, 2, 10, marginY, maxX, maxY, mouseX, mouseY)) {
                    this.hoverButtonId = 4;
                }
            }
            else {
                draw.drawRightString(String.valueOf((int)(this.installProgress * 10.0) / 10.0) + "%", maxX - 10, y + (maxY - y - 8) / 2);
            }
        }
        if (this.isMouseOver() && !hoverVerified && this.hoverButtonId == -1) {
            final String[] array = new String[descriptionLines.size()];
            descriptionLines.toArray(array);
            TooltipHelper.getHelper().pointTooltip(mouseX, mouseY, array);
        }
        if (this.lastActionState == AddonInfo.AddonActionState.ERROR) {
            draw.drawRightString(String.valueOf(ModColor.cl("4")) + "ERROR", maxX - 10, y + (maxY - y - 8) / 2);
        }
        if (this.isMouseOver()) {
            switch (this.hoverButtonId) {
                case 0: {
                    TooltipHelper.getHelper().pointTooltip(mouseX, mouseY, 0L, "Install");
                    break;
                }
                case 1: {
                    TooltipHelper.getHelper().pointTooltip(mouseX, mouseY, 0L, "Uninstall");
                    break;
                }
                case 2: {
                    TooltipHelper.getHelper().pointTooltip(mouseX, mouseY, 0L, "Settings");
                    break;
                }
                case 3: {
                    TooltipHelper.getHelper().pointTooltip(mouseX, mouseY, 0L, "Enable again");
                    break;
                }
                case 4: {
                    TooltipHelper.getHelper().pointTooltip(mouseX, mouseY, 0L, "Delete again");
                    break;
                }
            }
        }
    }
    
    private boolean drawButton(final ResourceLocation resourceLocation, final int y, final int buttonSize, final int buttonPadding, int marginX, final int marginY, final int maxX, final int maxY, final int mouseX, final int mouseY) {
        final DrawUtils draw = LabyMod.getInstance().getDrawUtils();
        final boolean hover = mouseX > maxX - buttonSize - marginX + 1 && mouseX < maxX - buttonSize + buttonSize - marginX + 1 && mouseY > y + marginY + 1 && mouseY < y + buttonSize + marginY + 1;
        marginX += (hover ? 1 : 0);
        final int colorA = hover ? ModColor.toRGB(10, 10, 10, 255) : ModColor.toRGB(220, 220, 220, 255);
        final int colorB = hover ? ModColor.toRGB(150, 150, 150, 255) : ModColor.toRGB(0, 0, 0, 255);
        final int colorC = hover ? ModColor.toRGB(150, 150, 150, 255) : ModColor.toRGB(180, 180, 180, 255);
        draw.drawRectangle(maxX - buttonSize - marginX, y + marginY, maxX - buttonSize + buttonSize - marginX, y + buttonSize + marginY, colorA);
        draw.drawRectangle(maxX - buttonSize - marginX + 1, y + marginY + 1, maxX - buttonSize + buttonSize - marginX + 1, y + buttonSize + marginY + 1, colorB);
        draw.drawRectangle(maxX - buttonSize - marginX + 1, y + marginY + 1, maxX - buttonSize + buttonSize - marginX, y + buttonSize + marginY, colorC);
        Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation);
        draw.drawTexture(maxX - buttonSize - marginX + buttonPadding / 2 + (hover ? 1 : 0), y + marginY + buttonPadding / 2 + (hover ? 1 : 0), 256.0, 256.0, buttonSize - buttonPadding, buttonSize - buttonPadding, 0.8f);
        return hover;
    }
    
    public void drawIcon(final int x, final int y, final int width, final int height) {
        if (this.addonInfo.getImageURL() == null) {
            Minecraft.getMinecraft().getTextureManager().bindTexture(ModTextures.MISC_HEAD_QUESTION);
            LabyMod.getInstance().getDrawUtils().drawTexture(x, y, 256.0, 256.0, width, height);
        }
        else {
            LabyMod.getInstance().getDrawUtils().drawImageUrl(this.addonInfo.getImageURL(), x, y, 256.0, 256.0, width, height);
        }
    }
    
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        switch (this.hoverButtonId) {
            case 0: {
                this.callbackAction.accept(AddonInfo.AddonActionState.INSTALL);
                break;
            }
            case 1: {
                this.callbackAction.accept(AddonInfo.AddonActionState.UNINSTALL);
            }
            case 3: {
                this.callbackAction.accept(AddonInfo.AddonActionState.UNINSTALL_REVOKE);
                break;
            }
            case 4: {
                this.callbackAction.accept(AddonInfo.AddonActionState.INSTALL_REVOKE);
                break;
            }
        }
    }
    
    public int getEntryHeight() {
        return 40;
    }
    
    public void mouseRelease(final int mouseX, final int mouseY, final int mouseButton) {
    }
    
    public boolean isHoverSubSettingsButton() {
        return this.hoverButtonId == 2;
    }
    
    public boolean isAddonInstalled() {
        return this.installedAddon != null;
    }
    
    public AddonElement canHover(final boolean flag) {
        this.canHover = flag;
        return this;
    }
    
    public void onMouseClickedPreview(final int mouseX, final int mouseY, final int mouseButton) {
        if (this.addonInfo != null) {
            final LabyModAddon loadedAddon = AddonLoader.getInstalledAddonByInfo(this.addonInfo);
            if (loadedAddon != null) {
                loadedAddon.onMouseClickedPreview(mouseX, mouseY, mouseButton);
            }
        }
    }
    
    public void onRenderPreview(final int mouseX, final int mouseY, final float partialTicks) {
        if (this.addonInfo != null) {
            final LabyModAddon loadedAddon = AddonLoader.getInstalledAddonByInfo(this.addonInfo);
            if (loadedAddon != null) {
                loadedAddon.onRenderPreview(mouseX, mouseY, partialTicks);
            }
        }
    }
    
    public LabyModAddon getInstalledAddon() {
        return this.installedAddon;
    }
    
    public Consumer<AddonInfo.AddonActionState> getCallbackAction() {
        return this.callbackAction;
    }
    
    public AddonInfo.AddonActionState getLastActionState() {
        return this.lastActionState;
    }
    
    public boolean isMouseOver() {
        return this.mouseOver;
    }
    
    public int getHoverButtonId() {
        return this.hoverButtonId;
    }
    
    public boolean isCanHover() {
        return this.canHover;
    }
    
    public double getInstallProgress() {
        return this.installProgress;
    }
    
    public List<SettingsElement> getSubSettings() {
        return this.subSettings;
    }
    
    public AddonInfo getAddonInfo() {
        return this.addonInfo;
    }
    
    public void setInstalledAddon(final LabyModAddon installedAddon) {
        this.installedAddon = installedAddon;
    }
    
    public void setLastActionState(final AddonInfo.AddonActionState lastActionState) {
        this.lastActionState = lastActionState;
    }
    
    public void setCanHover(final boolean canHover) {
        this.canHover = canHover;
    }
    
    public void setInstallProgress(final double installProgress) {
        this.installProgress = installProgress;
    }
}
