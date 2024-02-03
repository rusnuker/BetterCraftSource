// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.settings;

import net.labymod.settings.elements.KeyElement;
import java.io.IOException;
import net.labymod.settings.elements.ColorPickerCheckBoxBulkElement;
import net.labymod.settings.elements.DropDownElement;
import net.labymod.utils.manager.TooltipHelper;
import net.labymod.main.ModTextures;
import net.labymod.utils.DrawUtils;
import net.labymod.settings.elements.ControlElement;
import net.labymod.settings.elements.HeaderElement;
import java.util.Iterator;
import java.util.Collection;
import net.labymod.main.DefinedSettings;
import net.labymod.main.LabyMod;
import org.lwjgl.input.Keyboard;
import net.labymod.main.lang.LanguageManager;
import net.labymod.core.LabyModCore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.labymod.gui.elements.ModTextField;
import net.labymod.support.DashboardConnector;
import java.util.ArrayList;
import net.labymod.settings.elements.SettingsElement;
import net.labymod.settings.elements.CategorySettingsElement;
import java.util.List;
import net.labymod.gui.elements.Scrollbar;
import net.minecraft.client.gui.GuiScreen;

public class LabyModSettingsGui extends GuiScreen
{
    private final Scrollbar scrollbar;
    private final GuiScreen lastScreen;
    private final List<CategorySettingsElement> buttonCategoryElements;
    private final List<SettingsElement> tempElementsStored;
    private final ArrayList<SettingsElement> path;
    private final DashboardConnector dashboardConnector;
    private ModTextField searchField;
    private SettingsElement mouseOverElement;
    private List<SettingsElement> listedElementsStored;
    private GuiButton buttonGoBack;
    private double preScrollPos;
    private boolean hoverUpdateButton;
    private boolean closed;
    private boolean skipDrawDescription;
    
    public LabyModSettingsGui(final GuiScreen lastScreen) {
        this.scrollbar = new Scrollbar(1);
        this.buttonCategoryElements = new ArrayList<CategorySettingsElement>();
        this.listedElementsStored = new ArrayList<SettingsElement>();
        this.tempElementsStored = new ArrayList<SettingsElement>();
        this.path = new ArrayList<SettingsElement>();
        this.preScrollPos = 0.0;
        this.closed = false;
        this.skipDrawDescription = false;
        this.dashboardConnector = new DashboardConnector();
        this.lastScreen = lastScreen;
        this.buttonCategoryElements.add(new CategorySettingsElement(new SettingsCategory("settings_category_ingame_gui").setIcon("ingame_gui.png"), new CategorySettingsElement.ClickedCallback() {
            @Override
            public void clicked(final SettingsCategory category) {
                Minecraft.getMinecraft().displayGuiScreen(new LabyModModuleEditorGui(LabyModSettingsGui.this));
            }
        }));
        this.buttonCategoryElements.add(new CategorySettingsElement(new SettingsCategory("settings_category_addons").setIcon("addons.png"), new CategorySettingsElement.ClickedCallback() {
            @Override
            public void clicked(final SettingsCategory category) {
                Minecraft.getMinecraft().displayGuiScreen(new LabyModAddonsGui(LabyModSettingsGui.this));
            }
        }));
    }
    
    @Override
    public void initGui() {
        super.initGui();
        this.doQuery(null);
        this.buttonList.clear();
        (this.searchField = new ModTextField(0, LabyModCore.getMinecraft().getFontRenderer(), LabyModSettingsGui.width / 2 - 126, 50, 252, 16)).setPlaceHolder(LanguageManager.translate("search_textbox_placeholder"));
        this.scrollbar.setPosition(LabyModSettingsGui.width / 2 + 122, 80, LabyModSettingsGui.width / 2 + 122 + 4, LabyModSettingsGui.height - 35);
        this.scrollbar.setSpeed(20);
        this.scrollbar.update(this.listedElementsStored.size());
        this.scrollbar.init();
        this.buttonList.add(new GuiButton(5, LabyModSettingsGui.width / 2 - 100, LabyModSettingsGui.height - 25, LanguageManager.translate("button_save_changes")));
        this.buttonList.add(this.buttonGoBack = new GuiButton(6, LabyModSettingsGui.width / 2 - 120, 48, 20, 20, "<"));
        this.buttonGoBack.visible = !this.path.isEmpty();
        Keyboard.enableRepeatEvents(true);
    }
    
    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        Keyboard.enableRepeatEvents(false);
        LabyMod.getMainConfig().save();
    }
    
    private void doQuery(final String query) {
        this.tempElementsStored.clear();
        if (this.path.isEmpty()) {
            for (final SettingsCategory settingsCategory : DefinedSettings.getCategories()) {
                this.queryCategory(settingsCategory, query);
            }
        }
        else {
            final SettingsElement currentOpenElement = this.path.get(this.path.size() - 1);
            this.tempElementsStored.addAll(currentOpenElement.getSubSettings().getElements());
        }
        this.listedElementsStored = this.tempElementsStored;
    }
    
    private void queryCategory(final SettingsCategory settingsCategory, final String query) {
        final List<SettingsElement> elementsToAdd = new ArrayList<SettingsElement>();
        for (final SettingsElement element : settingsCategory.getSettings().getElements()) {
            if (query == null || settingsCategory.getTitle().toLowerCase().contains(query) || this.isSettingElement(query, element)) {
                elementsToAdd.add(element);
            }
            if (query != null && !query.isEmpty() && element.getSubSettings() != null && !element.getSubSettings().getElements().isEmpty()) {
                for (final SettingsElement subElement : element.getSubSettings().getElements()) {
                    if (this.isSettingElement(query, subElement)) {
                        elementsToAdd.add(subElement);
                    }
                }
            }
        }
        if (!elementsToAdd.isEmpty()) {
            this.tempElementsStored.add(new HeaderElement(settingsCategory.getTitle()));
        }
        for (final SettingsElement element : elementsToAdd) {
            this.tempElementsStored.add(element);
        }
        for (final SettingsCategory subCategory : settingsCategory.getSubList()) {
            this.queryCategory(subCategory, query);
        }
    }
    
    private boolean isSettingElement(final String query, final SettingsElement settingsElement) {
        return settingsElement.getDisplayName().toLowerCase().contains(query) || (settingsElement.getDescriptionText() != null && settingsElement.getDescriptionText().toLowerCase().contains(query));
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        final DrawUtils draw = LabyMod.getInstance().getDrawUtils();
        draw.drawAutoDimmedBackground(this.scrollbar.getScrollY());
        this.drawSettingsList(mouseX, mouseY, LabyModSettingsGui.height - 30);
        draw.drawOverlayBackground(0, 75);
        draw.drawGradientShadowTop(75.0, 0.0, LabyModSettingsGui.width);
        draw.drawOverlayBackground(LabyModSettingsGui.height - 30, LabyModSettingsGui.height);
        draw.drawGradientShadowBottom(LabyModSettingsGui.height - 30, 0.0, LabyModSettingsGui.width);
        this.drawHeadButtons(mouseX, mouseY);
        this.scrollbar.draw(mouseX, mouseY);
        if (!this.path.isEmpty()) {
            final SettingsElement currentOpenElement = this.path.get(this.path.size() - 1);
            draw.drawString(currentOpenElement.getDisplayName(), LabyModSettingsGui.width / 2 - 95, 55.0);
            if (currentOpenElement instanceof ControlElement) {
                final ControlElement control = (ControlElement)currentOpenElement;
                final ControlElement.IconData iconData = control.getIconData();
                if (iconData.hastextureIcon()) {
                    Minecraft.getMinecraft().getTextureManager().bindTexture(iconData.gettextureIcon());
                    LabyMod.getInstance().getDrawUtils().drawTexture(LabyModSettingsGui.width / 2 + 100, 50.0, 256.0, 256.0, 16.0, 16.0);
                }
                else if (iconData.hasMaterialIcon()) {
                    LabyMod.getInstance().getDrawUtils().drawItem(iconData.getMaterialIcon().createItemStack(), LabyModSettingsGui.width / 2 + 100, 50.0, null);
                }
            }
        }
        else {
            this.searchField.drawTextBox();
        }
        if (!this.skipDrawDescription) {
            this.drawDescriptions(mouseX, mouseY, 75, LabyModSettingsGui.height - 30);
        }
        this.drawUpdateNotification(mouseX, mouseY);
        this.dashboardConnector.renderIcon(LabyModSettingsGui.width - 15, 15, mouseX, mouseY);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    private void drawUpdateNotification(final int mouseX, final int mouseY) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(ModTextures.BUTTON_UPDATE);
        final DrawUtils draw = LabyMod.getInstance().getDrawUtils();
        this.hoverUpdateButton = (mouseX > 5 && mouseX < 20 && mouseY > LabyModSettingsGui.height - 20 && mouseY < LabyModSettingsGui.height - 20 + 15);
        final int add = this.hoverUpdateButton ? 1 : 0;
        draw.drawTexture(5 - add, LabyModSettingsGui.height - 20 - add, 255.0, 255.0, 15 + add * 2, 15 + add * 2);
        final boolean updateAvailable = LabyMod.getInstance().getUpdater().isUpdateAvailable();
        if (updateAvailable) {
            draw.drawString(LanguageManager.translate("update_available"), 25.0, LabyModSettingsGui.height - 20 + 4);
            if (this.hoverUpdateButton) {
                TooltipHelper.getHelper().pointTooltip(mouseX, mouseY, 0L, LanguageManager.translate("reinstall_version", LabyMod.getInstance().getUpdater().getLatestVersionString()));
            }
        }
        else if (this.hoverUpdateButton) {
            TooltipHelper.getHelper().pointTooltip(mouseX, mouseY, 0L, LanguageManager.translate("reinstall"));
        }
    }
    
    private void drawHeadButtons(final int mouseX, final int mouseY) {
        final int midX = LabyModSettingsGui.width / 2;
        int buttonLength = LabyModSettingsGui.width / (this.buttonCategoryElements.size() + 1);
        final int buttonHeight = 25;
        int spaceLength = LabyModSettingsGui.width / (this.buttonCategoryElements.size() + 2) / 20;
        if (buttonLength > 123) {
            buttonLength = 123;
        }
        if (buttonLength < 70) {
            buttonLength = 70;
        }
        if (spaceLength > 50) {
            spaceLength = 50;
        }
        final int totalLength = (buttonLength + spaceLength) * this.buttonCategoryElements.size() - spaceLength;
        int posX = midX - totalLength / 2;
        final int posY = 15;
        for (final CategorySettingsElement element : this.buttonCategoryElements) {
            element.draw(posX, 15, posX + buttonLength, 40, mouseX, mouseY);
            posX += buttonLength + spaceLength;
        }
    }
    
    private void drawSettingsList(final int mouseX, final int mouseY, final int maxY) {
        this.mouseOverElement = null;
        this.skipDrawDescription = false;
        double totalEntryHeight = 0.0;
        for (int zLevel = 0; zLevel < 2; ++zLevel) {
            double posY = 80.0 + this.scrollbar.getScrollY();
            final int midX = LabyModSettingsGui.width / 2;
            final int elementLength = 120;
            totalEntryHeight = 0.0;
            for (final SettingsElement element : this.listedElementsStored) {
                if (((!(element instanceof DropDownElement) || element instanceof ColorPickerCheckBoxBulkElement) && zLevel == 0) || ((element instanceof DropDownElement || element instanceof ColorPickerCheckBoxBulkElement) && zLevel == 1)) {
                    if (element instanceof DropDownElement) {
                        ((DropDownElement)element).getDropDownMenu().setMaxY(maxY);
                        if (((DropDownElement)element).getDropDownMenu().isOpen()) {
                            this.skipDrawDescription = true;
                        }
                    }
                    element.draw(midX - 120, (int)posY, midX + 120, (int)posY + element.getEntryHeight(), mouseX, mouseY);
                    if (element.isMouseOver()) {
                        this.mouseOverElement = element;
                    }
                }
                posY += element.getEntryHeight() + 1;
                totalEntryHeight += element.getEntryHeight() + 1;
            }
        }
        this.scrollbar.setEntryHeight(totalEntryHeight / this.listedElementsStored.size());
        this.scrollbar.update(this.listedElementsStored.size());
    }
    
    private void drawDescriptions(final int mouseX, final int mouseY, final int minY, final int maxY) {
        for (final SettingsElement element : this.listedElementsStored) {
            if (element.isMouseOver() && mouseY > minY && mouseY < maxY) {
                element.drawDescription(mouseX, mouseY, LabyModSettingsGui.width);
            }
        }
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        super.actionPerformed(button);
        if (button.id == 5) {
            this.closed = true;
            Minecraft.getMinecraft().displayGuiScreen(this.lastScreen);
        }
        if (button.id == 6 && !this.path.isEmpty()) {
            this.path.remove(this.path.size() - 1);
            this.initGui();
            this.scrollbar.setScrollY(this.preScrollPos);
            this.preScrollPos = 0.0;
        }
    }
    
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.closed) {
            return;
        }
        if (this.hoverUpdateButton) {
            try {
                LabyMod.getInstance().getUpdater().downloadUpdaterFile();
            }
            catch (final Exception e) {
                e.printStackTrace();
            }
            LabyMod.getInstance().getUpdater().addUpdaterHook();
            Minecraft.getMinecraft().shutdown();
            return;
        }
        for (final SettingsElement element : this.listedElementsStored) {
            if (element instanceof DropDownElement && ((DropDownElement)element).onClickDropDown(mouseX, mouseY, mouseButton)) {
                return;
            }
        }
        for (final CategorySettingsElement element2 : this.buttonCategoryElements) {
            element2.mouseClicked(mouseX, mouseY, mouseButton);
        }
        for (final SettingsElement element : this.listedElementsStored) {
            element.unfocus(mouseX, mouseY, mouseButton);
            if (element.isMouseOver()) {
                element.mouseClicked(mouseX, mouseY, mouseButton);
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
        this.searchField.mouseClicked(mouseX, mouseY, mouseButton);
        this.dashboardConnector.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    protected void mouseReleased(final int mouseX, final int mouseY, final int state) {
        super.mouseReleased(mouseX, mouseY, state);
        for (final SettingsElement element : this.listedElementsStored) {
            element.mouseRelease(mouseX, mouseY, state);
        }
        this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.RELEASED);
    }
    
    @Override
    protected void mouseClickMove(final int mouseX, final int mouseY, final int clickedMouseButton, final long timeSinceLastClick) {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        for (final SettingsElement element : this.listedElementsStored) {
            element.mouseClickMove(mouseX, mouseY, clickedMouseButton);
        }
        this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.DRAGGING);
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        for (final SettingsElement element : this.listedElementsStored) {
            final boolean cancel = element instanceof KeyElement && ((KeyElement)element).getTextField().isFocused();
            element.keyTyped(typedChar, keyCode);
            if (cancel) {
                return;
            }
        }
        if (keyCode == 1) {
            this.mc.displayGuiScreen(this.lastScreen);
            return;
        }
        super.keyTyped(typedChar, keyCode);
        if (this.path.isEmpty() && this.searchField.textboxKeyTyped(typedChar, keyCode)) {
            this.doQuery(this.searchField.getText().toLowerCase());
        }
    }
    
    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.scrollbar.mouseInput();
    }
    
    @Override
    public void updateScreen() {
        super.updateScreen();
        this.searchField.updateCursorCounter();
    }
}
