// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.settings;

import net.labymod.utils.ModColor;
import net.labymod.utils.DrawUtils;
import net.labymod.utils.manager.TooltipHelper;
import net.labymod.main.ModTextures;
import java.util.Collections;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.labymod.settings.elements.CategoryModuleEditorElement;
import net.labymod.settings.elements.ColorPickerCheckBoxBulkElement;
import net.labymod.settings.elements.DropDownElement;
import net.minecraft.client.gui.ScaledResolution;
import java.io.IOException;
import java.util.Iterator;
import net.labymod.utils.ModUtils;
import net.minecraft.client.Minecraft;
import net.labymod.core.LabyModCore;
import org.lwjgl.input.Keyboard;
import net.labymod.main.LabyMod;
import java.util.ArrayList;
import net.labymod.main.lang.LanguageManager;
import net.labymod.gui.elements.DropDownMenu;
import net.labymod.settings.elements.ControlElement;
import net.labymod.gui.elements.ModTextField;
import net.minecraft.client.gui.GuiButton;
import net.labymod.settings.elements.BooleanElement;
import net.labymod.settings.elements.SettingsElement;
import java.util.List;
import net.labymod.gui.elements.Scrollbar;
import net.minecraft.client.gui.GuiScreen;

public class LabyModModuleEditorGui extends GuiScreen
{
    public static final String GLOBAL_PROFILE;
    public static final String SINGLEPLAYER_PROFILE;
    public static String selectedProfile;
    private final GuiScreen lastScreen;
    private final Scrollbar scrollbar;
    private final List<SettingsElement> path;
    private final long lightUpSelectedElement;
    private boolean mouseClickSplitScreen;
    private SettingsElement mouseOverElement;
    private BooleanElement masterElement;
    private GuiButton buttonGoBack;
    private ModTextField textFieldSearch;
    private String searchedString;
    private ControlElement selectedControlElement;
    private DropDownMenu<EnumModuleEditorScale> scalingDropdown;
    private DropDownMenu<String> profilesDropdown;
    private boolean hoverResetButton;
    
    static {
        LabyModModuleEditorGui.selectedProfile = null;
        GLOBAL_PROFILE = LanguageManager.translate("global_profile");
        SINGLEPLAYER_PROFILE = LanguageManager.translate("singleplayer_profile");
    }
    
    public LabyModModuleEditorGui(final GuiScreen lastScreen) {
        this.mouseClickSplitScreen = false;
        this.path = new ArrayList<SettingsElement>();
        this.searchedString = "";
        this.lightUpSelectedElement = 0L;
        this.hoverResetButton = false;
        this.lastScreen = lastScreen;
        if (LabyMod.getSettings().moduleEditorZoom == -1.0) {
            LabyMod.getSettings().moduleEditorZoom = 33.333333333333336;
        }
        this.scrollbar = new Scrollbar(24);
    }
    
    @Override
    public void onGuiClosed() {
        PreviewRenderer.getInstance().kill();
        Keyboard.enableRepeatEvents(false);
    }
    
    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        PreviewRenderer.getInstance().init(LabyModModuleEditorGui.class);
        this.initList(true);
    }
    
    private void initList(final boolean scrollUp) {
        this.buttonList.clear();
        this.scrollbar.setPosition(LabyMod.getSettings().moduleEditorSplitX - 8, 0, LabyMod.getSettings().moduleEditorSplitX - 4, LabyModModuleEditorGui.height - 31);
        this.scrollbar.setSpeed(10);
        if (scrollUp) {
            this.scrollbar.setScrollY(0.0);
        }
        this.updateScrollbarValues(4);
        this.scrollbar.init();
        final boolean displayBackButton = this.path.size() != 0;
        (this.textFieldSearch = new ModTextField(0, LabyModCore.getMinecraft().getFontRenderer(), displayBackButton ? 60 : 5, 35, LabyMod.getSettings().moduleEditorSplitX - (displayBackButton ? 69 : 14), 14)).setText(this.searchedString);
        if (!displayBackButton) {
            this.textFieldSearch.setFocused(true);
        }
        this.textFieldSearch.setCursorPositionEnd();
        this.textFieldSearch.setPlaceHolder(LanguageManager.translate("search_textbox_placeholder"));
        this.buttonList.add(new GuiButton(5, 5, LabyModModuleEditorGui.height - 25, LabyMod.getSettings().moduleEditorSplitX - 10, 20, LanguageManager.translate("button_done")));
        (this.scalingDropdown = new DropDownMenu<EnumModuleEditorScale>("", LabyModModuleEditorGui.width - 75, LabyModModuleEditorGui.height - 12 - 5, 70, 12).fill(EnumModuleEditorScale.values())).setSelected(EnumModuleEditorScale.getByZoom(LabyMod.getSettings().moduleEditorZoom));
        this.scalingDropdown.setMaxY(LabyModModuleEditorGui.height);
        this.scalingDropdown.setEntryDrawer(new DropDownMenu.DropDownEntryDrawer() {
            @Override
            public void draw(final Object object, final int x, final int y, final String trimmedEntry) {
                LabyMod.getInstance().getDrawUtils().drawString(((EnumModuleEditorScale)object).getDisplayName(), x, y);
            }
        });
        (this.profilesDropdown = new DropDownMenu<String>("", LabyMod.getSettings().moduleEditorSplitX + 5, LabyModModuleEditorGui.height - 12 - 5, 150, 12)).addOption(LabyModModuleEditorGui.GLOBAL_PROFILE);
        String currentServer = null;
        if (Minecraft.getMinecraft().getCurrentServerData() != null) {
            this.profilesDropdown.addOption(currentServer = ModUtils.getProfileNameByIp(Minecraft.getMinecraft().getCurrentServerData().serverIP));
        }
        if (Minecraft.getMinecraft().isSingleplayer()) {
            this.profilesDropdown.addOption(LabyModModuleEditorGui.SINGLEPLAYER_PROFILE);
        }
        this.profilesDropdown.setSelected((LabyModModuleEditorGui.selectedProfile == null) ? LabyModModuleEditorGui.GLOBAL_PROFILE : (LabyModModuleEditorGui.selectedProfile.equals("singleplayer") ? LabyModModuleEditorGui.SINGLEPLAYER_PROFILE : LabyModModuleEditorGui.selectedProfile));
        this.profilesDropdown.setMaxY(LabyModModuleEditorGui.height);
        this.buttonList.add(this.buttonGoBack = new GuiButton(7, 5, 47, 20, 20, "<"));
        this.checkSplitscreenBorder();
        super.initGui();
        if (this.path.size() != 0) {
            for (final SettingsElement module : this.getCurrentSelectedSubList().getSubSettings().getElements()) {
                module.init();
            }
        }
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (keyCode == 1) {
            this.mc.displayGuiScreen(this.lastScreen);
            return;
        }
        if (this.textFieldSearch.textboxKeyTyped(typedChar, keyCode)) {
            this.searchedString = this.textFieldSearch.getText();
            this.scrollbar.update(this.getFoundModules().size());
        }
        if (this.selectedControlElement != null) {
            this.selectedControlElement.keyTyped(typedChar, keyCode);
        }
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.id == 5) {
            this.mc.displayGuiScreen(this.lastScreen);
        }
        if (button.id == this.buttonGoBack.id) {
            this.searchedString = "";
            if (this.path.size() != 0) {
                this.path.remove(this.path.size() - 1);
            }
            this.initGui();
        }
        super.actionPerformed(button);
    }
    
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        if (this.scalingDropdown.onClick(mouseX, mouseY, mouseButton) && this.scalingDropdown.getSelected() != null && !this.scalingDropdown.isOpen()) {
            final double scaling = this.scalingDropdown.getSelected().getScaling() / 3.0 * 100.0;
            LabyMod.getSettings().moduleEditorZoom = scaling;
            final ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
            LabyModModuleEditorGui.width = scaledResolution.getScaledWidth();
            LabyModModuleEditorGui.height = scaledResolution.getScaledHeight();
            this.initGui();
            return;
        }
        if (this.profilesDropdown.onClick(mouseX, mouseY, mouseButton) && this.profilesDropdown.getSelected() != null && !this.profilesDropdown.isOpen()) {
            LabyModModuleEditorGui.selectedProfile = this.profilesDropdown.getSelected();
            if (LabyModModuleEditorGui.selectedProfile != null && LabyModModuleEditorGui.selectedProfile.equals(LabyModModuleEditorGui.GLOBAL_PROFILE)) {
                LabyModModuleEditorGui.selectedProfile = null;
            }
            Minecraft.getMinecraft().displayGuiScreen(new LabyModModuleEditorGui(this.lastScreen));
            return;
        }
        final List<SettingsElement> foundModules = this.getFoundModules();
        for (final SettingsElement element : foundModules) {
            if (element instanceof DropDownElement && ((DropDownElement)element).onClickDropDown(mouseX, mouseY, mouseButton)) {
                return;
            }
            if (element instanceof ColorPickerCheckBoxBulkElement && ((ColorPickerCheckBoxBulkElement)element).onClickBulkEntry(mouseX, mouseY, mouseButton)) {
                return;
            }
        }
        this.textFieldSearch.mouseClicked(mouseX, mouseY, mouseButton);
        this.unfocusSubListTextfields(mouseX, mouseY, mouseButton);
        this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.CLICKED);
        if (this.mouseOverElement != null) {
            final boolean flag = true;
            if (this.mouseOverElement instanceof ControlElement) {
                final ControlElement controlElement = (ControlElement)this.mouseOverElement;
            }
            if (this.mouseOverElement instanceof CategoryModuleEditorElement) {
                this.path.add(this.mouseOverElement);
            }
            if (flag) {
                this.mouseOverElement.mouseClicked(mouseX, mouseY, mouseButton);
            }
            if (this.mouseOverElement instanceof ControlElement) {
                this.selectedControlElement = (ControlElement)this.mouseOverElement;
            }
            this.updateScrollbarValues(4);
        }
        if (this.mouseOverElement != null && this.mouseOverElement instanceof ControlElement) {
            final ControlElement element2 = (ControlElement)this.mouseOverElement;
            if (element2.hasSubList() && element2.getButtonAdvanced().hovered && element2.getButtonAdvanced().enabled) {
                element2.getButtonAdvanced().playPressSound(this.mc.getSoundHandler());
                this.path.add(element2);
                this.initGui();
            }
        }
        if (mouseButton == 0 && mouseX > LabyMod.getSettings().moduleEditorSplitX - 5 && mouseX < LabyMod.getSettings().moduleEditorSplitX + 2 && mouseY > 45 && mouseY < LabyModModuleEditorGui.height - 30) {
            this.mouseClickSplitScreen = true;
        }
        if (this.hoverResetButton) {
            Minecraft.getMinecraft().displayGuiScreen(new GuiYesNo(new GuiYesNoCallback() {
                @Override
                public void confirmClicked(final boolean flag, final int id) {
                    Minecraft.getMinecraft().displayGuiScreen(new LabyModModuleEditorGui(LabyModModuleEditorGui.this.lastScreen));
                }
            }, LanguageManager.translate((LabyModModuleEditorGui.selectedProfile == null) ? LanguageManager.translate("reset_modules_global") : LanguageManager.translate("reset_modules_profile", LabyModModuleEditorGui.selectedProfile)), LanguageManager.translate("reset_modules_undone"), LanguageManager.translate("button_no"), LanguageManager.translate("button_yes"), 0));
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    private void unfocusSubListTextfields(final int mouseX, final int mouseY, final int mouseButton) {
        if (this.path.size() != 0) {
            for (final SettingsElement moduleElement : this.getCurrentSelectedSubList().getSubSettings().getElements()) {
                moduleElement.unfocus(mouseX, mouseY, mouseButton);
            }
        }
    }
    
    private SettingsElement getCurrentSelectedSubList() {
        return (this.path.size() == 0) ? null : this.path.get(this.path.size() - 1);
    }
    
    @Override
    protected void mouseReleased(final int mouseX, final int mouseY, final int mouseButton) {
        super.mouseReleased(mouseX, mouseY, mouseButton);
        this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.RELEASED);
        if (mouseButton == 0) {
            this.mouseClickSplitScreen = false;
        }
        if (this.mouseOverElement != null) {
            this.mouseOverElement.mouseRelease(mouseX, mouseY, mouseButton);
        }
    }
    
    @Override
    protected void mouseClickMove(final int mouseX, final int mouseY, final int clickedMouseButton, final long timeSinceLastClick) {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.DRAGGING);
        if (this.mouseClickSplitScreen) {
            LabyMod.getSettings().moduleEditorSplitX = mouseX;
            this.checkSplitscreenBorder();
            this.initGui();
            this.textFieldSearch.setFocused(false);
        }
        if (this.mouseOverElement != null) {
            this.mouseOverElement.mouseClickMove(mouseX, mouseY, clickedMouseButton);
        }
    }
    
    private void checkSplitscreenBorder() {
        if (LabyMod.getSettings().moduleEditorSplitX < 180) {
            LabyMod.getSettings().moduleEditorSplitX = 180;
        }
        if (LabyMod.getSettings().moduleEditorSplitX > 300) {
            LabyMod.getSettings().moduleEditorSplitX = 300;
        }
    }
    
    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.scrollbar.mouseInput();
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        final DrawUtils draw = LabyMod.getInstance().getDrawUtils();
        Minecraft.getMinecraft().getFramebuffer().bindFramebuffer(true);
        final int scrollBarListY = 4;
        LabyMod.getInstance().getDrawUtils().drawDimmedOverlayBackground(0, 4, LabyModModuleEditorGui.width, LabyModModuleEditorGui.height - 30);
        final int left = LabyMod.getSettings().moduleEditorSplitX - 1;
        final int top = 30;
        final int right = LabyModModuleEditorGui.width - 1;
        final int bottom = LabyModModuleEditorGui.height - 31;
        final List<SettingsElement> foundModules = this.getFoundModules();
        Collections.reverse(foundModules);
        double totalHeight = 0.0;
        for (final SettingsElement element : foundModules) {
            totalHeight += element.getEntryHeight() + 1;
        }
        final double moduleListStartY = 32 + ((this.path.size() == 0) ? 20 : 0) + this.scrollbar.getScrollY();
        this.scrollbar.draw(mouseX, mouseY);
        this.renderModuleList(foundModules, moduleListStartY, totalHeight, mouseX, mouseY);
        draw.drawOverlayBackground(0, 30);
        draw.drawOverlayBackground(bottom, LabyModModuleEditorGui.height);
        draw.drawOverlayBackground(0, 0, LabyMod.getSettings().moduleEditorSplitX - 4, 29);
        draw.drawGradientShadowTop(30.0, 0.0, LabyMod.getSettings().moduleEditorSplitX - 1);
        draw.drawGradientShadowBottom(bottom, 0.0, LabyMod.getSettings().moduleEditorSplitX - 1);
        final boolean hoverSplitscreen = mouseX > LabyMod.getSettings().moduleEditorSplitX - 5 && mouseX < LabyMod.getSettings().moduleEditorSplitX + 1 && mouseY > 45 && mouseY < LabyModModuleEditorGui.height - 30;
        if (hoverSplitscreen || this.mouseClickSplitScreen) {
            draw.drawRectangle(LabyMod.getSettings().moduleEditorSplitX - 2, 30, LabyMod.getSettings().moduleEditorSplitX - 1, LabyModModuleEditorGui.height - 31, Integer.MAX_VALUE);
            draw.drawCenteredString("|||", mouseX + 1, mouseY - 3);
        }
        draw.drawRightString(LanguageManager.translate("labymod_gui_editor"), LabyModModuleEditorGui.width - 10, 12.0);
        if (this.path.size() != 0) {
            final SettingsElement element2 = this.getCurrentSelectedSubList();
            final int posY = 6;
            final List<String> list = draw.listFormattedStringToWidth(element2.getDisplayName(), LabyMod.getSettings().moduleEditorSplitX - 33 - 5 - 15, 3);
            final int fontHeight = 16 - list.size() * 3;
            double listY = 12.0;
            for (final String line : list) {
                draw.drawString(line, 33.0, listY - list.size() * fontHeight / 2 + fontHeight / 2);
                listY += fontHeight;
            }
            ControlElement.IconData masterIconData = null;
            final boolean b = element2 instanceof ControlElement;
            if (element2 instanceof CategoryModuleEditorElement) {
                masterIconData = ((CategoryModuleEditorElement)element2).getIconData();
            }
            if (masterIconData != null) {
                if (masterIconData.hastextureIcon()) {
                    Minecraft.getMinecraft().getTextureManager().bindTexture(masterIconData.gettextureIcon());
                    LabyMod.getInstance().getDrawUtils().drawTexture(LabyMod.getSettings().moduleEditorSplitX - 22, 9.0, 256.0, 256.0, 16.0, 16.0);
                }
                else if (masterIconData.hasMaterialIcon()) {
                    LabyMod.getInstance().getDrawUtils().drawItem(masterIconData.getMaterialIcon().createItemStack(), LabyMod.getSettings().moduleEditorSplitX - 22, 9.0, null);
                }
            }
            LabyModCore.getMinecraft().setButtonYPosition(this.buttonGoBack, 6);
        }
        else if (this.path.size() == 0) {
            this.textFieldSearch.drawTextBox();
            final int masterY = 4;
            this.masterElement.setHoverable(true).setSelected(this.selectedControlElement == null).draw(5, 4, LabyMod.getSettings().moduleEditorSplitX - 4, 26, mouseX, mouseY);
        }
        this.buttonGoBack.visible = (this.path.size() != 0);
        final int tabX = left + 3;
        final int tabY = 18;
        draw.drawGradientShadowBottom(30.0, 0.0, LabyModModuleEditorGui.width);
        draw.drawGradientShadowTop(bottom, 0.0, LabyModModuleEditorGui.width);
        draw.drawString(LanguageManager.translate("preview_zoom"), LabyModModuleEditorGui.width - 75, LabyModModuleEditorGui.height - 26, 0.75);
        this.scalingDropdown.draw(mouseX, mouseY);
        draw.drawString(LanguageManager.translate("module_profiles"), LabyMod.getSettings().moduleEditorSplitX + 5, LabyModModuleEditorGui.height - 26, 0.75);
        this.profilesDropdown.draw(mouseX, mouseY);
        final int resetX = LabyMod.getSettings().moduleEditorSplitX + 5 + this.profilesDropdown.getWidth() + 5;
        this.hoverResetButton = (mouseX > resetX && mouseX < resetX + 16 && mouseY > LabyModModuleEditorGui.height - 19 && mouseY < LabyModModuleEditorGui.height - 19 + 16);
        draw.bindTexture(ModTextures.BUTTON_RESET);
        draw.drawTexture(resetX, LabyModModuleEditorGui.height - 19, 0.0, this.hoverResetButton ? 127.5 : 0.0, 255.0, 127.5, 16.0, 16.0);
        if (this.hoverResetButton) {
            TooltipHelper.getHelper().pointTooltip(mouseX, mouseY, 0L, (LabyModModuleEditorGui.selectedProfile == null) ? LanguageManager.translate("reset_modules_description") : LanguageManager.translate("delete_profile_description", LabyModModuleEditorGui.selectedProfile));
        }
        if (this.profilesDropdown.isMouseOver(mouseX, mouseY)) {
            TooltipHelper.getHelper().pointTooltip(mouseX, mouseY, LanguageManager.translate("module_profiles_description"));
        }
        this.renderDescriptions(foundModules, moduleListStartY, totalHeight, mouseX, mouseY);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    private void updateScrollbarValues(final int scrollBarListY) {
        final List<SettingsElement> foundModules = this.getFoundModules();
        double totalHeight = 0.0;
        for (final SettingsElement element : foundModules) {
            totalHeight += element.getEntryHeight() + 1;
        }
        this.scrollbar.setEntryHeight(totalHeight / foundModules.size());
        this.scrollbar.update(foundModules.size());
        this.scrollbar.setPosTop(scrollBarListY + 25 + 2 + ((this.path.size() == 0) ? 24 : 0));
        this.scrollbar.setSpeed((int)this.scrollbar.getEntryHeight() / 2);
    }
    
    private void renderModuleList(final List<SettingsElement> foundModules, final double listY, final double totalHeight, final int mouseX, final int mouseY) {
        SettingsElement mouseOverElement = null;
        for (int zLevel = 0; zLevel < 2; ++zLevel) {
            final int x = 5;
            double y = listY + totalHeight;
            final int maxX = LabyMod.getSettings().moduleEditorSplitX - (this.scrollbar.isHidden() ? 5 : 10);
            for (final SettingsElement element : foundModules) {
                final int nextSetting = element.getEntryHeight();
                y -= nextSetting + 1;
                final boolean inYRange = mouseY > listY && mouseY < LabyModModuleEditorGui.height - 30;
                if (((!(element instanceof DropDownElement) || element instanceof ColorPickerCheckBoxBulkElement) && zLevel == 0) || ((element instanceof DropDownElement || element instanceof ColorPickerCheckBoxBulkElement) && zLevel == 1)) {
                    if (element instanceof ControlElement) {
                        final ControlElement controlElement = (ControlElement)element;
                        final boolean selected = this.selectedControlElement != null && this.selectedControlElement == controlElement;
                        controlElement.draw(5, (int)y, maxX, (int)y + nextSetting, mouseX, mouseY);
                        if (this.selectedControlElement != null && this.selectedControlElement.equals(controlElement) && this.lightUpSelectedElement + 1000L > System.currentTimeMillis()) {
                            final int alpha = (int)((System.currentTimeMillis() - this.lightUpSelectedElement) * -1L + 1000L) / 4;
                            LabyMod.getInstance().getDrawUtils().drawRectBorder(5.0, y, maxX, y + nextSetting, ModColor.toRGB(200, 20, 20, alpha), 1.0);
                        }
                    }
                    if (element instanceof CategoryModuleEditorElement) {
                        final CategoryModuleEditorElement categoryElement = (CategoryModuleEditorElement)element;
                        categoryElement.draw(5, (int)y, maxX, (int)y + nextSetting, mouseX, mouseY);
                    }
                }
                if (element.isMouseOver() && inYRange && zLevel == 1) {
                    mouseOverElement = element;
                }
            }
        }
        if (this.masterElement.isMouseOver() && this.path.size() == 0) {
            mouseOverElement = this.masterElement;
        }
        this.mouseOverElement = mouseOverElement;
    }
    
    private void renderDescriptions(final List<SettingsElement> foundModules, final double listY, final double totalHeight, final int mouseX, final int mouseY) {
        for (final SettingsElement element : foundModules) {
            if (element.isMouseOver() && mouseY > listY && mouseY < listY + totalHeight) {
                element.drawDescription(mouseX, mouseY, LabyModModuleEditorGui.width);
            }
        }
    }
    
    @Override
    public void updateScreen() {
        super.updateScreen();
        if (this.path.size() != 0) {
            for (final SettingsElement module : this.getCurrentSelectedSubList().getSubSettings().getElements()) {
                module.updateScreen();
            }
        }
        this.textFieldSearch.updateCursorCounter();
    }
    
    private List<SettingsElement> getFoundModules() {
        final boolean isMasterOpen = this.path.size() != 0 && this.path.get(0) == this.masterElement;
        boolean b = false;
        if (this.textFieldSearch == null || this.textFieldSearch.getText().replaceAll(" ", "").isEmpty()) {
            if (this.path.size() >= 2) {}
        }
        else if (this.path.size() >= 1) {}
        final boolean isSubSettingOpen;
        b = (isSubSettingOpen = true);
        if (this.path.size() != 0) {
            final ArrayList<SettingsElement> list = new ArrayList<SettingsElement>();
            for (final SettingsElement element : this.getCurrentSelectedSubList().getSubSettings().getElements()) {
                final boolean isCurrentTab = isMasterOpen || isSubSettingOpen;
                final boolean b2 = element instanceof ControlElement;
            }
            return list;
        }
        final String searchValue = this.textFieldSearch.getText().toLowerCase();
        searchValue.replace(" ", "").isEmpty();
        final ArrayList<SettingsElement> list2 = new ArrayList<SettingsElement>();
        final boolean isCurrentTab2 = isMasterOpen || isSubSettingOpen;
        return list2;
    }
    
    public GuiScreen getLastScreen() {
        return this.lastScreen;
    }
    
    public enum EnumModuleEditorScale
    {
        SMALL("SMALL", 0, LanguageManager.translate("scaletype_small"), 1), 
        NORMAL("NORMAL", 1, LanguageManager.translate("scaletype_normal"), 2), 
        LARGE("LARGE", 2, LanguageManager.translate("scaletype_large"), 3), 
        AUTO("AUTO", 3, LanguageManager.translate("scaletype_auto"), 4);
        
        private final String displayName;
        private final int scaling;
        
        private EnumModuleEditorScale(final String s, final int n, final String displayName, final int scaling) {
            this.displayName = displayName;
            this.scaling = scaling;
        }
        
        public static EnumModuleEditorScale getByScaling(final int guiScale) {
            EnumModuleEditorScale[] values;
            for (int length = (values = values()).length, i = 0; i < length; ++i) {
                final EnumModuleEditorScale emes = values[i];
                if (emes.getScaling() >= guiScale) {
                    return emes;
                }
            }
            return EnumModuleEditorScale.NORMAL;
        }
        
        public static EnumModuleEditorScale getByZoom(final double zoom) {
            final double customScaleFactor = 1.0 + zoom * 0.03;
            EnumModuleEditorScale[] values;
            for (int length = (values = values()).length, i = 0; i < length; ++i) {
                final EnumModuleEditorScale emes = values[i];
                if (emes.getScaling() == Math.round(customScaleFactor) - 1L) {
                    return emes;
                }
            }
            return EnumModuleEditorScale.NORMAL;
        }
        
        public String getDisplayName() {
            return this.displayName;
        }
        
        public int getScaling() {
            return this.scaling;
        }
    }
}
