// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.gui;

import java.beans.ConstructorProperties;
import net.labymod.addon.online.AddonInfoManager;
import java.util.UUID;
import net.labymod.utils.ModUtils;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.IOException;
import net.labymod.main.listeners.AddonRecommendationListener;
import net.labymod.utils.ServerData;
import net.labymod.utils.DrawUtils;
import net.labymod.settings.LabyModAddonsGui;
import net.labymod.main.ModTextures;
import net.minecraft.client.Minecraft;
import net.labymod.addon.online.info.AddonInfo;
import net.labymod.addon.AddonLoader;
import net.labymod.utils.ModColor;
import net.labymod.main.LabyMod;
import net.labymod.addon.online.info.OnlineAddonInfo;
import java.util.Iterator;
import net.labymod.main.lang.LanguageManager;
import net.labymod.gui.elements.CheckBox;
import net.minecraft.client.gui.GuiButton;
import java.util.List;
import net.minecraft.client.gui.GuiScreen;

public class GuiAddonRecommendation extends GuiScreen
{
    private List<RecommendedAddon> addons;
    private GuiButton buttonDone;
    private CheckBox checkBox;
    
    public GuiAddonRecommendation(final List<RecommendedAddon> addons) {
        this.addons = addons;
    }
    
    @Override
    public void initGui() {
        super.initGui();
        boolean hasRequired = false;
        for (final RecommendedAddon addon : this.addons) {
            final OnlineAddonInfo info = addon.getOnlineAddonInfo();
            if (info != null && addon.isRequired()) {
                hasRequired = true;
            }
        }
        if (hasRequired) {
            this.buttonList.add(new GuiButton(1, GuiAddonRecommendation.width / 2 - 100 - 5, GuiAddonRecommendation.height - 50, 100, 20, LanguageManager.translate("button_cancel")));
            this.buttonList.add(this.buttonDone = new GuiButton(0, GuiAddonRecommendation.width / 2 + 5, GuiAddonRecommendation.height - 50, 100, 20, LanguageManager.translate("button_done")));
        }
        else {
            this.buttonList.add(this.buttonDone = new GuiButton(0, GuiAddonRecommendation.width / 2 - 100, GuiAddonRecommendation.height - 50, LanguageManager.translate("button_done")));
        }
        this.checkBox = new CheckBox("", CheckBox.EnumCheckBoxValue.DISABLED, null, 0, GuiAddonRecommendation.height - 80, 20, 20);
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        int amount = 0;
        boolean hasRequired = false;
        boolean allInstalled = true;
        for (final RecommendedAddon addon : this.addons) {
            final OnlineAddonInfo info = addon.getOnlineAddonInfo();
            if (info == null) {
                continue;
            }
            ++amount;
            if (!addon.isRequired()) {
                continue;
            }
            hasRequired = true;
        }
        final DrawUtils draw = LabyMod.getInstance().getDrawUtils();
        final int width = 300;
        final int x = draw.getWidth() / 2 - 150;
        int y = draw.getHeight() / 2 - 42 * amount / 2;
        final String translationKey = hasRequired ? "addon_recommendation_title_required" : "addon_recommendation_title_optional";
        final ServerData serverData = LabyMod.getInstance().getCurrentServerData();
        final String ip = (serverData == null || serverData.getIp() == null) ? "<server>" : serverData.getIp();
        final String title = String.format(String.valueOf(ModColor.cl('a')) + LanguageManager.translate(translationKey), String.valueOf(ModColor.cl('E')) + ip + ModColor.cl('a'));
        draw.drawCenteredString(title, draw.getWidth() / 2, y - 20);
        for (final RecommendedAddon addon2 : this.addons) {
            final OnlineAddonInfo info2 = addon2.getOnlineAddonInfo();
            if (info2 == null) {
                continue;
            }
            info2.getAddonElement().draw(x, y, x + 300, y + 40, mouseX, mouseY, false);
            if (AddonLoader.hasInstalled(addon2.getOnlineAddonInfo())) {
                Minecraft.getMinecraft().getTextureManager().bindTexture(ModTextures.BUTTON_CHECKBOX);
                draw.drawTexture(x - 25, y + 10, 255.0, 255.0, 20.0, 20.0);
            }
            else if (addon2.isRequired()) {
                Minecraft.getMinecraft().getTextureManager().bindTexture(ModTextures.BUTTON_EXCLAMATION);
                draw.drawTexture(x - 20, y + 10, 127.0, 255.0, 10.0, 20.0);
                allInstalled = false;
            }
            else {
                allInstalled = false;
            }
            if (addon2.isRequired()) {
                draw.drawString(String.valueOf(ModColor.cl('c')) + ModColor.cl('l') + LanguageManager.translate("addon_recommendation_required"), x + 300 + 5, y + 15);
            }
            y += 42;
        }
        if (!hasRequired) {
            final String checkBoxLabel = LanguageManager.translate("addon_recommendation_ignore");
            this.checkBox.setX(draw.getWidth() / 2 - draw.getStringWidth(checkBoxLabel) / 2 - 25);
            this.checkBox.drawCheckbox(mouseX, mouseY);
            draw.drawCenteredString(checkBoxLabel, draw.getWidth() / 2, draw.getHeight() - 74);
        }
        this.buttonDone.enabled = (!hasRequired || allInstalled);
        if (hasRequired && LabyModAddonsGui.isRestartRequired()) {
            this.buttonDone.displayString = LanguageManager.translate("button_restart");
            this.buttonDone.enabled = true;
        }
        else {
            this.buttonDone.displayString = LanguageManager.translate("button_done");
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        AddonRecommendationListener.sendMissingStatus(this.addons, true);
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
    }
    
    @Override
    public void updateScreen() {
        super.updateScreen();
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        super.actionPerformed(button);
        boolean hasRequired = false;
        for (final RecommendedAddon addon : this.addons) {
            final OnlineAddonInfo info = addon.getOnlineAddonInfo();
            if (info == null) {
                continue;
            }
            if (!addon.isRequired()) {
                continue;
            }
            hasRequired = true;
        }
        switch (button.id) {
            case 0: {
                final ServerData currentServer = LabyMod.getInstance().getCurrentServerData();
                final boolean trusted = currentServer != null && currentServer.getIp() != null && this.checkBox.getValue() == CheckBox.EnumCheckBoxValue.ENABLED;
                if (trusted) {
                    final List<String> list = new ArrayList<String>(Arrays.asList(LabyMod.getSettings().ignoredAddonRecommendationServers));
                    final String profileAddress = ModUtils.getProfileNameByIp(currentServer.getIp());
                    if (trusted && !list.contains(profileAddress)) {
                        list.add(profileAddress);
                        final String[] array = new String[list.size()];
                        list.toArray(array);
                        LabyMod.getSettings().ignoredAddonRecommendationServers = array;
                        LabyMod.getMainConfig().save();
                    }
                }
                if (hasRequired && LabyModAddonsGui.isRestartRequired()) {
                    Minecraft.getMinecraft().shutdown();
                    break;
                }
                Minecraft.getMinecraft().displayGuiScreen(null);
                break;
            }
            case 1: {
                Minecraft.getMinecraft().displayGuiScreen(null);
                break;
            }
        }
    }
    
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        for (final RecommendedAddon addon : this.addons) {
            final OnlineAddonInfo info = addon.getOnlineAddonInfo();
            if (info == null) {
                continue;
            }
            info.getAddonElement().mouseClicked(mouseX, mouseY, mouseButton);
        }
        boolean hasRequired = false;
        for (final RecommendedAddon addon2 : this.addons) {
            final OnlineAddonInfo info2 = addon2.getOnlineAddonInfo();
            if (info2 == null) {
                continue;
            }
            if (!addon2.isRequired()) {
                continue;
            }
            hasRequired = true;
        }
        if (!hasRequired) {
            this.checkBox.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }
    
    public static class RecommendedAddon
    {
        private UUID uuid;
        private boolean required;
        private OnlineAddonInfo onlineAddonInfo;
        
        public void bindAddon(final AddonInfoManager manager) {
            final AddonInfo addonInfo = manager.getAddonInfoMap().get(this.uuid);
            if (addonInfo instanceof OnlineAddonInfo) {
                this.onlineAddonInfo = (OnlineAddonInfo)addonInfo;
            }
        }
        
        public UUID getUuid() {
            return this.uuid;
        }
        
        public boolean isRequired() {
            return this.required;
        }
        
        public OnlineAddonInfo getOnlineAddonInfo() {
            return this.onlineAddonInfo;
        }
        
        @ConstructorProperties({ "uuid", "required", "onlineAddonInfo" })
        public RecommendedAddon(final UUID uuid, final boolean required, final OnlineAddonInfo onlineAddonInfo) {
            this.uuid = uuid;
            this.required = required;
            this.onlineAddonInfo = onlineAddonInfo;
        }
    }
}
