// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.settings;

import net.labymod.settings.elements.KeyElement;
import net.minecraft.client.Minecraft;
import java.util.Collections;
import java.util.Comparator;
import java.io.IOException;
import net.labymod.settings.elements.ControlElement;
import net.labymod.addon.online.info.OnlineAddonInfo;
import net.labymod.settings.elements.ColorPickerCheckBoxBulkElement;
import net.labymod.settings.elements.DropDownElement;
import com.google.common.collect.Lists;
import java.util.Collection;
import net.labymod.gui.elements.CheckBox;
import net.labymod.utils.DrawUtils;
import net.labymod.utils.ModColor;
import net.labymod.main.Source;
import net.labymod.main.LabyMod;
import org.lwjgl.input.Keyboard;
import net.labymod.core.LabyModCore;
import net.labymod.main.lang.LanguageManager;
import java.util.Iterator;
import net.labymod.api.LabyModAddon;
import net.labymod.addon.AddonLoader;
import net.labymod.addon.online.AddonInfoManager;
import net.labymod.addon.online.info.AddonInfo;
import java.util.ArrayList;
import java.util.List;
import net.labymod.settings.elements.SettingsElement;
import net.labymod.settings.elements.AddonElement;
import net.labymod.gui.elements.ModTextField;
import net.minecraft.client.gui.GuiButton;
import net.labymod.gui.elements.Scrollbar;
import net.minecraft.client.gui.GuiScreen;

public class LabyModAddonsGui extends GuiScreen
{
    private final GuiScreen lastScreen;
    private final Scrollbar scrollbar;
    private GuiButton buttonDone;
    private GuiButton buttonBack;
    private GuiButton buttonWarningExitGame;
    private GuiButton buttonWarningRestartLater;
    private ModTextField fieldSearch;
    private boolean displayRestartWarning;
    private AddonElement openedAddonSettings;
    private AddonElement mouseOverAddonEntry;
    private SettingsElement mouseOverElement;
    private List<SettingsElement> listedElementsStored;
    private final List<SettingsElement> tempElementsStored;
    private final ArrayList<SettingsElement> path;
    private double preScrollPos;
    private EnumSortingState selectedSortingState;
    private EnumSortingState hoveredSortingState;
    private List<AddonInfo> sortedAddonInfoList;
    private int[] enabledFilter;
    
    public LabyModAddonsGui(final GuiScreen lastScreen) {
        this.scrollbar = new Scrollbar(1);
        this.displayRestartWarning = false;
        this.openedAddonSettings = null;
        this.listedElementsStored = new ArrayList<SettingsElement>();
        this.tempElementsStored = new ArrayList<SettingsElement>();
        this.path = new ArrayList<SettingsElement>();
        this.preScrollPos = 0.0;
        this.selectedSortingState = EnumSortingState.TRENDING;
        this.hoveredSortingState = null;
        this.sortedAddonInfoList = AddonInfoManager.getInstance().getAddonInfoList();
        this.enabledFilter = null;
        this.lastScreen = lastScreen;
        AddonInfoManager.getInstance().init();
        if (!AddonLoader.getAddons().isEmpty()) {
            this.selectedSortingState = EnumSortingState.INSTALLED;
        }
        if (AddonInfoManager.getInstance().isLoaded()) {
            this.handleCheckBox(false, 0, 0, 0);
        }
    }
    
    public static boolean isRestartRequired() {
        for (final LabyModAddon labyModAddon : AddonLoader.getAddons()) {
            if (labyModAddon.about != null && labyModAddon.about.deleted) {
                return true;
            }
        }
        for (final AddonInfo addonInfo : AddonInfoManager.getInstance().getAddonInfoList()) {
            final AddonElement addonElement = addonInfo.getAddonElement();
            if (addonElement.getLastActionState() == AddonInfo.AddonActionState.INSTALL) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.clear();
        this.doQuery();
        this.sortAddonInfoList();
        this.scrollbar.setPosition(LabyModAddonsGui.width / 2 + 152, 80, LabyModAddonsGui.width / 2 + 152 + 4, LabyModAddonsGui.height - 35);
        this.scrollbar.setSpeed(20);
        this.scrollbar.init();
        this.buttonList.add(this.buttonDone = new GuiButton(0, LabyModAddonsGui.width / 2 + 50, LabyModAddonsGui.height - 25, 100, 20, LanguageManager.translate("button_done")));
        this.buttonList.add(this.buttonBack = new GuiButton(1, LabyModAddonsGui.width / 2 - 100, 50, 22, 20, "<"));
        this.buttonList.add(this.buttonWarningRestartLater = new GuiButton(2, LabyModAddonsGui.width / 2 - 95, LabyModAddonsGui.height / 2 + 20, 90, 20, LanguageManager.translate("button_restart_later")));
        this.buttonList.add(this.buttonWarningExitGame = new GuiButton(3, LabyModAddonsGui.width / 2 + 5, LabyModAddonsGui.height / 2 + 20, 90, 20, LanguageManager.translate("button_exit_game")));
        (this.fieldSearch = new ModTextField(0, LabyModCore.getMinecraft().getFontRenderer(), LabyModAddonsGui.width / 2 - 150, 35, 300, 14)).setBlackBox(false);
        this.fieldSearch.setPlaceHolder(LanguageManager.translate("search_textbox_placeholder"));
        Keyboard.enableRepeatEvents(true);
        if (this.isInSubSettings()) {
            for (final SettingsElement settingsElement : this.listedElementsStored) {
                settingsElement.init();
            }
        }
    }
    
    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        Keyboard.enableRepeatEvents(false);
        for (final LabyModAddon addon : AddonLoader.getAddons()) {
            addon.saveConfig();
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        final DrawUtils draw = LabyMod.getInstance().getDrawUtils();
        draw.drawAutoDimmedBackground(this.scrollbar.getScrollY());
        this.drawElementList(mouseX, mouseY);
        if (!AddonInfoManager.getInstance().isLoaded() || AddonInfoManager.getInstance().getAddonInfoList().isEmpty()) {
            draw.drawCenteredString(LanguageManager.translate("button_no_addons_available", Source.ABOUT_MC_VERSION), LabyModAddonsGui.width / 2, LabyModAddonsGui.height / 2);
        }
        draw.drawOverlayBackground(0, 75);
        draw.drawGradientShadowTop(75.0, 0.0, LabyModAddonsGui.width);
        draw.drawOverlayBackground(LabyModAddonsGui.height - 30, LabyModAddonsGui.height);
        draw.drawGradientShadowBottom(LabyModAddonsGui.height - 30, 0.0, LabyModAddonsGui.width);
        this.scrollbar.draw(mouseX, mouseY);
        this.buttonDone.enabled = !this.displayRestartWarning;
        this.buttonBack.visible = (this.openedAddonSettings != null);
        this.buttonBack.enabled = !this.displayRestartWarning;
        this.buttonWarningExitGame.visible = this.displayRestartWarning;
        this.buttonWarningRestartLater.visible = this.displayRestartWarning;
        draw.drawCenteredString(LanguageManager.translate("button_labymod_addons"), LabyModAddonsGui.width / 2, 20.0);
        if (AddonInfoManager.getInstance().isLoaded()) {
            if (!this.isInSubSettings()) {
                this.drawSortingTabs(mouseX, mouseY);
            }
            else {
                draw.drawString(this.openedAddonSettings.getAddonInfo().getName(), LabyModAddonsGui.width / 2 - 100 + 30, 55.0);
                this.openedAddonSettings.drawIcon(LabyModAddonsGui.width / 2 + 100 - 20, 50, 20, 20);
            }
        }
        if (AddonInfoManager.getInstance().isLoaded()) {
            this.drawCategoryFilterMenu(mouseX, mouseY);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
        if (this.displayRestartWarning) {
            draw.drawIngameBackground();
            draw.drawRectangle(LabyModAddonsGui.width / 2 - 100 - 2, LabyModAddonsGui.height / 2 - 50 - 2, LabyModAddonsGui.width / 2 + 100 + 2, LabyModAddonsGui.height / 2 + 50 + 2, Integer.MIN_VALUE);
            draw.drawRectangle(LabyModAddonsGui.width / 2 - 100, LabyModAddonsGui.height / 2 - 50, LabyModAddonsGui.width / 2 + 100, LabyModAddonsGui.height / 2 + 50, ModColor.toRGB(20, 20, 20, 144));
            LabyModCore.getMinecraft().drawButton(this.buttonWarningRestartLater, mouseX, mouseY);
            LabyModCore.getMinecraft().drawButton(this.buttonWarningExitGame, mouseX, mouseY);
            draw.drawCenteredString(LanguageManager.translate("warning_title"), LabyModAddonsGui.width / 2, LabyModAddonsGui.height / 2 - 44);
            final List<String> list = draw.listFormattedStringToWidth(LanguageManager.translate("warning_content"), 190);
            int lineY = 0;
            for (final String line : list) {
                draw.drawCenteredString(String.valueOf(ModColor.cl("4")) + line, LabyModAddonsGui.width / 2, LabyModAddonsGui.height / 2 - 30 + lineY);
                lineY += 10;
            }
        }
        if (this.fieldSearch != null && !this.isInSubSettings()) {
            this.fieldSearch.drawTextBox();
        }
        if (this.openedAddonSettings != null) {
            this.openedAddonSettings.onRenderPreview(mouseX, mouseY, partialTicks);
        }
    }
    
    private void drawCategoryFilterMenu(final int mouseX, final int mouseY) {
        int x = LabyModAddonsGui.width / 2 - 145;
        final int y = LabyModAddonsGui.height - 20;
        CheckBox[] categorieCheckboxList;
        for (int length = (categorieCheckboxList = AddonInfoManager.getInstance().getCategorieCheckboxList()).length, i = 0; i < length; ++i) {
            final CheckBox checkBox = categorieCheckboxList[i];
            checkBox.setX(x);
            checkBox.setY(y);
            checkBox.drawCheckbox(mouseX, mouseY);
            x += 25;
        }
    }
    
    private void doQuery() {
        this.tempElementsStored.clear();
        if (this.openedAddonSettings != null) {
            if (this.path.isEmpty()) {
                this.tempElementsStored.addAll(this.openedAddonSettings.getSubSettings());
            }
            else {
                final SettingsElement currentOpenElement = this.path.get(this.path.size() - 1);
                this.tempElementsStored.addAll(currentOpenElement.getSubSettings().getElements());
            }
        }
        this.listedElementsStored = this.tempElementsStored;
    }
    
    private void drawSortingTabs(final int mouseX, final int mouseY) {
        final DrawUtils draw = LabyMod.getInstance().getDrawUtils();
        int tabX = LabyModAddonsGui.width / 2 - 150;
        final int tabY = 63;
        this.hoveredSortingState = null;
        if (AddonInfoManager.getInstance().isLoaded()) {
            EnumSortingState[] values;
            for (int length = (values = EnumSortingState.values()).length, i = 0; i < length; ++i) {
                final EnumSortingState sortingState = values[i];
                final boolean selected = this.selectedSortingState == sortingState;
                final int tabLen = draw.getStringWidth(sortingState.getDisplayName()) + 6 + (selected ? 6 : 0);
                String prefix = "";
                if (sortingState == EnumSortingState.INSTALLED) {
                    tabX = LabyModAddonsGui.width / 2 + 150 - tabLen;
                    prefix = ModColor.cl(selected ? "6" : "8");
                }
                this.drawSingleTab(sortingState, prefix, tabX, 63, tabLen, selected, mouseX, mouseY);
                tabX += tabLen + 1;
            }
        }
    }
    
    private void drawSingleTab(final EnumSortingState sortingState, final String prefix, final int tabX, final int tabY, final int length, final boolean selected, final int mouseX, final int mouseY) {
        final DrawUtils draw = LabyMod.getInstance().getDrawUtils();
        final int tabHeight = 12;
        final boolean hovered = mouseX > tabX && mouseX < tabX + length && mouseY > tabY && mouseY < tabY + 12;
        final int animate = (selected || hovered) ? (selected ? 2 : 1) : 0;
        final int tabMid = tabX + length / 2;
        draw.drawRectangle(tabX, tabY - animate * 2, tabX + length, tabY + 12, ModColor.toRGB(5, 5, 5, 140));
        draw.drawRectBorder(tabX, tabY - animate * 2, tabX + length, tabY + 12, ModColor.toRGB(5, 5, 5, 140), 1.0);
        if (selected) {
            draw.drawRectangle(tabX + 1, tabY - animate * 2 + 1, tabX + length - 1, tabY + 12, ModColor.toRGB(55, 55, 55, 65));
        }
        draw.drawCenteredString(String.valueOf((selected || hovered) ? ModColor.cl("f") : ModColor.cl("8")) + prefix + sortingState.getDisplayName(), tabMid, tabY + 2 - animate);
        if (hovered) {
            this.hoveredSortingState = sortingState;
        }
    }
    
    private void drawElementList(final int mouseX, final int mouseY) {
        double totalEntryHeight = 0.0;
        this.mouseOverAddonEntry = null;
        this.mouseOverElement = null;
        boolean canRenderDescription = true;
        int count = 0;
        if (this.isInSubSettings()) {
            for (int zLevel = 0; zLevel < 2; ++zLevel) {
                totalEntryHeight = 0.0;
                double listY = 80.0 + this.scrollbar.getScrollY();
                this.listedElementsStored = Lists.reverse(this.listedElementsStored);
                for (final SettingsElement settingsElement : this.listedElementsStored) {
                    listY += settingsElement.getEntryHeight() + 2;
                    totalEntryHeight += settingsElement.getEntryHeight() + 2;
                }
                for (final SettingsElement settingsElement : this.listedElementsStored) {
                    listY -= settingsElement.getEntryHeight() + 2;
                    totalEntryHeight -= settingsElement.getEntryHeight() + 2;
                    if (((!(settingsElement instanceof DropDownElement) || settingsElement instanceof ColorPickerCheckBoxBulkElement) && zLevel == 0) || ((settingsElement instanceof DropDownElement || settingsElement instanceof ColorPickerCheckBoxBulkElement) && zLevel == 1)) {
                        settingsElement.draw(LabyModAddonsGui.width / 2 - 100, (int)listY, LabyModAddonsGui.width / 2 + 100, (int)(listY + settingsElement.getEntryHeight()), mouseX, mouseY);
                        if (canRenderDescription && settingsElement instanceof DropDownElement) {
                            canRenderDescription = !((DropDownElement)settingsElement).getDropDownMenu().isOpen();
                        }
                        if (!settingsElement.isMouseOver()) {
                            continue;
                        }
                        this.mouseOverElement = settingsElement;
                    }
                }
                for (final SettingsElement settingsElement : this.listedElementsStored) {
                    listY += settingsElement.getEntryHeight() + 2;
                    totalEntryHeight += settingsElement.getEntryHeight() + 2;
                }
                this.listedElementsStored = Lists.reverse(this.listedElementsStored);
            }
            count = this.listedElementsStored.size();
        }
        else {
            final boolean ranked = this.selectedSortingState.isRanked();
            int rank = 1;
            double listY2 = 80.0 + this.scrollbar.getScrollY();
            for (final AddonInfo addonInfo : this.sortedAddonInfoList) {
                boolean visible = this.enabledFilter == null;
                if (!visible) {
                    int[] enabledFilter;
                    for (int length = (enabledFilter = this.enabledFilter).length, j = 0; j < length; ++j) {
                        final int i = enabledFilter[j];
                        if (i == addonInfo.getCategory()) {
                            visible = true;
                        }
                    }
                }
                if (!visible) {
                    continue;
                }
                final boolean hasInstalled = AddonLoader.hasInstalled(addonInfo);
                if (this.selectedSortingState == EnumSortingState.VERIFIED && (this.fieldSearch == null || this.fieldSearch.getText().isEmpty())) {
                    if (!(addonInfo instanceof OnlineAddonInfo)) {
                        continue;
                    }
                    if (!((OnlineAddonInfo)addonInfo).isVerified()) {
                        continue;
                    }
                }
                if (this.selectedSortingState == EnumSortingState.INSTALLED) {
                    if (!hasInstalled) {
                        continue;
                    }
                }
                else if (!(addonInfo instanceof OnlineAddonInfo)) {
                    if (this.fieldSearch == null) {
                        continue;
                    }
                    if (this.fieldSearch.getText().isEmpty()) {
                        continue;
                    }
                }
                final AddonElement addonElement = addonInfo.getAddonElement();
                addonElement.canHover(mouseY >= 75 && mouseY <= LabyModAddonsGui.height - 30).draw(LabyModAddonsGui.width / 2 - 150, (int)listY2, LabyModAddonsGui.width / 2 + 150, (int)(listY2 + addonElement.getEntryHeight()), mouseX, mouseY);
                if (ranked) {
                    String prefix = "8";
                    switch (rank) {
                        case 1: {
                            prefix = "e";
                            break;
                        }
                        case 2: {
                            prefix = "7";
                            break;
                        }
                        case 3: {
                            prefix = "6";
                            break;
                        }
                    }
                    LabyMod.getInstance().getDrawUtils().drawRightString(String.valueOf(ModColor.cl(prefix)) + rank + ".", LabyModAddonsGui.width / 2 - 152, listY2 + 3.0, 0.7);
                }
                if (addonElement.isMouseOver()) {
                    this.mouseOverAddonEntry = addonInfo.getAddonElement();
                }
                listY2 += addonElement.getEntryHeight() + 2;
                totalEntryHeight += addonElement.getEntryHeight() + 2;
                ++rank;
            }
            count = AddonInfoManager.getInstance().getAddonInfoList().size();
        }
        if (canRenderDescription) {
            this.drawDescriptions(mouseX, mouseY, 80, LabyModAddonsGui.height - 30);
        }
        this.scrollbar.setEntryHeight(totalEntryHeight / count);
        this.scrollbar.update(count);
    }
    
    private void drawDescriptions(final int mouseX, final int mouseY, final int minY, final int maxY) {
        for (final SettingsElement element : this.listedElementsStored) {
            if (element.isMouseOver() && mouseY > minY && mouseY < maxY) {
                element.drawDescription(mouseX, mouseY, LabyModAddonsGui.width);
            }
        }
    }
    
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.displayRestartWarning) {
            return;
        }
        if (this.hoveredSortingState != null) {
            this.selectedSortingState = this.hoveredSortingState;
            this.sortAddonInfoList();
        }
        this.handleCheckBox(true, mouseX, mouseY, mouseButton);
        if (this.fieldSearch != null) {
            this.fieldSearch.mouseClicked(mouseX, mouseY, mouseButton);
        }
        if (this.openedAddonSettings != null) {
            this.openedAddonSettings.onMouseClickedPreview(mouseX, mouseY, mouseButton);
        }
        if (mouseY < 75 || mouseY > LabyModAddonsGui.height - 30) {
            return;
        }
        if (this.isInSubSettings()) {
            this.unfocusSubListTextfields(mouseX, mouseY, mouseButton);
            for (final SettingsElement element : this.listedElementsStored) {
                if (element instanceof DropDownElement && ((DropDownElement)element).onClickDropDown(mouseX, mouseY, mouseButton)) {
                    return;
                }
                if (element instanceof ColorPickerCheckBoxBulkElement && ((ColorPickerCheckBoxBulkElement)element).onClickBulkEntry(mouseX, mouseY, mouseButton)) {
                    return;
                }
            }
            if (this.mouseOverElement != null) {
                final boolean flag = true;
                if (this.mouseOverElement instanceof ControlElement) {
                    final ControlElement controlElement = (ControlElement)this.mouseOverElement;
                }
                if (flag) {
                    this.mouseOverElement.mouseClicked(mouseX, mouseY, mouseButton);
                }
            }
        }
        else {
            for (final AddonInfo addonInfo : AddonInfoManager.getInstance().getAddonInfoList()) {
                final AddonElement element2 = addonInfo.getAddonElement();
                element2.mouseClicked(mouseX, mouseY, mouseButton);
                if (element2.isHoverSubSettingsButton()) {
                    this.openedAddonSettings = this.mouseOverAddonEntry;
                    this.initGui();
                }
            }
        }
        if (this.mouseOverElement != null && this.mouseOverElement instanceof ControlElement) {
            final ControlElement element3 = (ControlElement)this.mouseOverElement;
            if (element3.hasSubList() && element3.getButtonAdvanced().hovered && element3.getButtonAdvanced().enabled) {
                element3.getButtonAdvanced().playPressSound(this.mc.getSoundHandler());
                this.path.add(element3);
                this.preScrollPos = this.scrollbar.getScrollY();
                this.scrollbar.setScrollY(0.0);
                this.initGui();
            }
        }
        this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.CLICKED);
    }
    
    private void unfocusSubListTextfields(final int mouseX, final int mouseY, final int mouseButton) {
        for (final SettingsElement moduleElement : this.listedElementsStored) {
            moduleElement.unfocus(mouseX, mouseY, mouseButton);
        }
    }
    
    private void sortAddonInfoList() {
        String query = (this.fieldSearch == null) ? null : this.fieldSearch.getText().toLowerCase().replaceAll(" ", "");
        if (query != null && query.isEmpty()) {
            query = null;
        }
        final List<AddonInfo> sortedAddonInfoList = new ArrayList<AddonInfo>();
        for (final AddonInfo addonInfo : AddonInfoManager.getInstance().getAddonInfoList()) {
            if (query == null || this.match(addonInfo.getName(), query) || this.match(addonInfo.getDescription(), query) || this.match(addonInfo.getAuthor(), query)) {
                sortedAddonInfoList.add(addonInfo);
            }
        }
        final int index = this.selectedSortingState.ordinal();
        Collections.sort(sortedAddonInfoList, new Comparator<AddonInfo>() {
            @Override
            public int compare(final AddonInfo a, final AddonInfo b) {
                final int aId = (a instanceof OnlineAddonInfo) ? ((OnlineAddonInfo)a).getSorting()[index] : 0;
                final int bId = (b instanceof OnlineAddonInfo) ? ((OnlineAddonInfo)b).getSorting()[index] : 0;
                return aId - bId;
            }
        });
        this.sortedAddonInfoList = sortedAddonInfoList;
    }
    
    private boolean match(final String s1, final String s2) {
        return s1.toLowerCase().replaceAll(" ", "").contains(s2);
    }
    
    private void handleCheckBox(final boolean handleMouseClick, final int mouseX, final int mouseY, final int mouseButton) {
        if (!AddonInfoManager.getInstance().isLoaded()) {
            return;
        }
        final CheckBox[] checkBoxList = AddonInfoManager.getInstance().getCategorieCheckboxList();
        this.enabledFilter = new int[checkBoxList.length];
        int index = 0;
        CheckBox[] array;
        for (int length = (array = checkBoxList).length, i = 0; i < length; ++i) {
            final CheckBox checkBox = array[i];
            if (handleMouseClick) {
                checkBox.mouseClicked(mouseX, mouseY, mouseButton);
            }
            this.enabledFilter[index] = ((checkBox.getValue() == CheckBox.EnumCheckBoxValue.ENABLED) ? (index + 1) : 0);
            ++index;
        }
    }
    
    @Override
    protected void mouseReleased(final int mouseX, final int mouseY, final int state) {
        super.mouseReleased(mouseX, mouseY, state);
        if (this.isInSubSettings()) {
            for (final SettingsElement settingsElement : this.listedElementsStored) {
                settingsElement.mouseRelease(mouseX, mouseY, state);
            }
        }
        this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.RELEASED);
    }
    
    @Override
    protected void mouseClickMove(final int mouseX, final int mouseY, final int clickedMouseButton, final long timeSinceLastClick) {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        if (this.isInSubSettings()) {
            for (final SettingsElement settingsElement : this.listedElementsStored) {
                settingsElement.mouseClickMove(mouseX, mouseY, clickedMouseButton);
            }
        }
        this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.DRAGGING);
    }
    
    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.scrollbar.mouseInput();
        if (this.isInSubSettings()) {
            for (final SettingsElement settingsElement : this.listedElementsStored) {
                if (settingsElement instanceof DropDownElement) {
                    ((DropDownElement)settingsElement).onScrollDropDown();
                }
            }
        }
    }
    
    @Override
    public void updateScreen() {
        super.updateScreen();
        if (this.fieldSearch != null) {
            this.fieldSearch.updateCursorCounter();
        }
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        super.actionPerformed(button);
        if (button == this.buttonDone && !this.displayRestartWarning) {
            if (isRestartRequired()) {
                this.displayRestartWarning = true;
            }
            else {
                Minecraft.getMinecraft().displayGuiScreen(this.lastScreen);
            }
        }
        if (this.path.isEmpty()) {
            if (button == this.buttonBack && !this.displayRestartWarning) {
                this.openedAddonSettings = null;
            }
        }
        else {
            this.path.remove(this.path.size() - 1);
            this.initGui();
            this.scrollbar.setScrollY(this.preScrollPos);
            this.preScrollPos = 0.0;
        }
        if (button == this.buttonWarningExitGame && this.displayRestartWarning) {
            this.mc.shutdown();
        }
        if (button == this.buttonWarningRestartLater && this.displayRestartWarning) {
            Minecraft.getMinecraft().displayGuiScreen(this.lastScreen);
        }
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (this.isInSubSettings()) {
            for (final SettingsElement settingsElement : this.listedElementsStored) {
                final boolean cancel = settingsElement instanceof KeyElement && ((KeyElement)settingsElement).getTextField().isFocused();
                settingsElement.keyTyped(typedChar, keyCode);
                if (cancel) {
                    return;
                }
            }
            super.keyTyped(typedChar, keyCode);
        }
        if (this.fieldSearch != null && !this.isInSubSettings()) {
            this.fieldSearch.textboxKeyTyped(typedChar, keyCode);
            this.sortAddonInfoList();
        }
        if (keyCode == 1) {
            if (!this.displayRestartWarning && isRestartRequired()) {
                this.displayRestartWarning = true;
            }
            else {
                Minecraft.getMinecraft().displayGuiScreen(this.lastScreen);
            }
        }
    }
    
    private boolean isInSubSettings() {
        return this.openedAddonSettings != null;
    }
    
    public enum EnumSortingState
    {
        TRENDING("TRENDING", 0, LanguageManager.translate("sortingtab_trending"), true), 
        TOP("TOP", 1, LanguageManager.translate("sortingtab_top"), true), 
        LATEST("LATEST", 2, LanguageManager.translate("sortingtab_latest"), false), 
        VERIFIED("VERIFIED", 3, LanguageManager.translate("sortingtab_featured"), false), 
        INSTALLED("INSTALLED", 4, LanguageManager.translate("sortingtab_installed"), false);
        
        private final String displayName;
        private final boolean ranked;
        
        private EnumSortingState(final String s, final int n, final String displayName, final boolean ranked) {
            this.displayName = displayName;
            this.ranked = ranked;
        }
        
        public String getDisplayName() {
            return this.displayName;
        }
        
        public boolean isRanked() {
            return this.ranked;
        }
    }
}
