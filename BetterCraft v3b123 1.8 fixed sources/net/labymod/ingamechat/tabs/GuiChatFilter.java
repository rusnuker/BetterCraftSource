// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.ingamechat.tabs;

import java.awt.Color;
import net.labymod.ingamechat.tools.filter.FilterChatManager;
import net.labymod.main.lang.LanguageManager;
import net.labymod.utils.ModColor;
import net.minecraft.client.gui.Gui;
import java.io.IOException;
import org.lwjgl.input.Mouse;
import net.labymod.main.LabyMod;
import java.util.Iterator;
import java.lang.reflect.Field;
import net.minecraft.util.ResourceLocation;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundRegistry;
import net.labymod.utils.ReflectionHelper;
import net.labymod.core.LabyModCore;
import net.minecraft.client.audio.SoundHandler;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiTextField;
import net.labymod.ingamechat.tools.filter.Filters;
import net.labymod.gui.elements.Scrollbar;
import net.labymod.ingamechat.GuiChatCustom;

public class GuiChatFilter extends GuiChatCustom
{
    private Scrollbar scrollbar;
    private Filters.Filter selectedFilter;
    private GuiTextField textFieldFilterName;
    private GuiTextField textFieldFilterContains;
    private GuiTextField textFieldFilterContainsNot;
    private GuiTextField textFieldFilterSoundfile;
    private GuiTextField textFieldFilterRoom;
    private int sliderDrag;
    private boolean markFilterNameRed;
    private boolean markContainsRed;
    private boolean markSoundNameRed;
    private String editStartName;
    private boolean canScroll;
    private static List<String> soundNames;
    
    static {
        GuiChatFilter.soundNames = new ArrayList<String>();
        try {
            final Field soundRegistryInSoundHandlerField = ReflectionHelper.findField(SoundHandler.class, LabyModCore.getMappingAdapter().getSoundRegistryInSoundHandlerMappings());
            final Field soundRegistryInSoundRegistryField = ReflectionHelper.findField(SoundRegistry.class, LabyModCore.getMappingAdapter().getSoundRegistryInSoundRegistryMappings());
            final SoundRegistry soundRegistry = (SoundRegistry)soundRegistryInSoundHandlerField.get(Minecraft.getMinecraft().getSoundHandler());
            final Map sounds = (Map)soundRegistryInSoundRegistryField.get(soundRegistry);
            for (final Object resourceObject : sounds.keySet()) {
                GuiChatFilter.soundNames.add(((ResourceLocation)resourceObject).getResourcePath());
            }
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
    
    public GuiChatFilter(final String defaultText) {
        super(defaultText);
        this.scrollbar = new Scrollbar(15);
        this.sliderDrag = -1;
        this.markFilterNameRed = false;
        this.markContainsRed = false;
        this.markSoundNameRed = false;
        this.editStartName = "";
    }
    
    @Override
    public void initGui() {
        super.initGui();
        this.scrollbar.setPosition(GuiChatFilter.width - 6, GuiChatFilter.height - 196, GuiChatFilter.width - 5, GuiChatFilter.height - 20);
        this.scrollbar.update(LabyMod.getInstance().getChatToolManager().getFilters().size());
        this.scrollbar.setSpeed(10);
        this.scrollbar.setEntryHeight(10.0);
        this.textFieldFilterName = new GuiTextField(0, LabyModCore.getMinecraft().getFontRenderer(), 0, 0, 110, 10);
        this.textFieldFilterContains = new GuiTextField(0, LabyModCore.getMinecraft().getFontRenderer(), 0, 0, 110, 10);
        this.textFieldFilterContainsNot = new GuiTextField(0, LabyModCore.getMinecraft().getFontRenderer(), 0, 0, 110, 10);
        this.textFieldFilterSoundfile = new GuiTextField(0, LabyModCore.getMinecraft().getFontRenderer(), 0, 0, 110, 10);
        this.textFieldFilterRoom = new GuiTextField(0, LabyModCore.getMinecraft().getFontRenderer(), 0, 0, 110, 10);
        this.markContainsRed = false;
        this.markFilterNameRed = false;
        this.markSoundNameRed = false;
        this.selectedFilter = null;
    }
    
    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        if (this.canScroll) {
            this.scrollbar.mouseInput();
            final int i = Mouse.getEventDWheel();
            if (i != 0) {
                this.mc.ingameGUI.getChatGUI().resetScroll();
            }
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.scrollbar.calc();
        Gui.drawRect(GuiChatFilter.width - 150, GuiChatFilter.height - 220, GuiChatFilter.width - 2, GuiChatFilter.height - 16, Integer.MIN_VALUE);
        this.canScroll = (mouseX > GuiChatFilter.width - 150 && mouseX < GuiChatFilter.width - 2 && mouseY > GuiChatFilter.height - 150 && mouseY < GuiChatFilter.height - 16);
        int row = 0;
        for (final Filters.Filter component : LabyMod.getInstance().getChatToolManager().getFilters()) {
            final double posY = GuiChatFilter.height - 215 + row * 10 + this.scrollbar.getScrollY();
            ++row;
            if (posY >= GuiChatFilter.height - 220) {
                if (posY > GuiChatFilter.height - 25) {
                    continue;
                }
                final boolean hover = this.selectedFilter == null && mouseX > GuiChatFilter.width - 150 + 1 && mouseX < GuiChatFilter.width - 2 - 1 && mouseY > posY - 1.0 && mouseY < posY + 9.0;
                if (hover || (this.selectedFilter != null && (this.selectedFilter.getFilterName().equalsIgnoreCase(component.getFilterName()) || component.getFilterName().equalsIgnoreCase(this.editStartName)))) {
                    Gui.drawRect(GuiChatFilter.width - 150 + 1, (int)posY - 1, GuiChatFilter.width - 2 - 1, (int)posY + 9, hover ? ModColor.toRGB(100, 200, 200, 100) : Integer.MAX_VALUE);
                }
                this.drawString(LabyModCore.getMinecraft().getFontRenderer(), LabyMod.getInstance().getDrawUtils().trimStringToWidth(component.getFilterName(), 130), GuiChatFilter.width - 145, (int)posY, Integer.MAX_VALUE);
                if (this.selectedFilter != null) {
                    continue;
                }
                final boolean hoverX = mouseX > GuiChatFilter.width - 12 - 1 && mouseX < GuiChatFilter.width - 12 + 7 && mouseY > posY && mouseY < posY + 8.0;
                this.drawString(LabyModCore.getMinecraft().getFontRenderer(), String.valueOf(ModColor.cl(hoverX ? "c" : "4")) + "\u2718", GuiChatFilter.width - 12, (int)posY, Integer.MAX_VALUE);
            }
        }
        if (!this.scrollbar.isHidden()) {
            Gui.drawRect(GuiChatFilter.width - 6, GuiChatFilter.height - 145, GuiChatFilter.width - 5, GuiChatFilter.height - 20, Integer.MIN_VALUE);
            Gui.drawRect(GuiChatFilter.width - 7, (int)this.scrollbar.getTop(), GuiChatFilter.width - 4, (int)(this.scrollbar.getTop() + this.scrollbar.getBarLength()), Integer.MAX_VALUE);
        }
        if (this.selectedFilter == null) {
            final boolean hover2 = mouseX > GuiChatFilter.width - 165 && mouseX < GuiChatFilter.width - 152 && mouseY > GuiChatFilter.height - 220 && mouseY < GuiChatFilter.height - 220 + 13;
            Gui.drawRect(GuiChatFilter.width - 165, GuiChatFilter.height - 220, GuiChatFilter.width - 152, GuiChatFilter.height - 220 + 13, hover2 ? Integer.MAX_VALUE : Integer.MIN_VALUE);
            Gui.drawCenteredString(LabyModCore.getMinecraft().getFontRenderer(), "+", GuiChatFilter.width - 158, GuiChatFilter.height - 217, hover2 ? ModColor.toRGB(50, 220, 120, 210) : Integer.MAX_VALUE);
        }
        else {
            int relYAtSoundHint = 0;
            int relYAtRoomHint = 0;
            Gui.drawRect(GuiChatFilter.width - 270, GuiChatFilter.height - 220, GuiChatFilter.width - 152, GuiChatFilter.height - 16, Integer.MIN_VALUE);
            final int relX = GuiChatFilter.width - 270;
            int relY = GuiChatFilter.height - 220;
            this.drawElementTextField("name", this.textFieldFilterName, relX, relY, mouseX, mouseY);
            this.drawElementTextField("contains", this.textFieldFilterContains, relX, relY + 23, mouseX, mouseY);
            this.drawElementTextField("contains_not", this.textFieldFilterContainsNot, relX, relY + 46, mouseX, mouseY);
            this.drawElementTextField("room", this.textFieldFilterRoom, relX, relY + 69, mouseX, mouseY);
            relYAtRoomHint = relY + 69;
            relY += 23;
            this.drawElementCheckBox("play_sound", this.selectedFilter.isPlaySound(), relX, relY + 69, mouseX, mouseY);
            if (this.selectedFilter.isPlaySound()) {
                this.drawElementTextField("", this.textFieldFilterSoundfile, relX, relY + 69, mouseX, mouseY);
                relYAtSoundHint = relY + 69;
            }
            else {
                relY -= 10;
            }
            this.drawElementCheckBox("highlight", this.selectedFilter.isHighlightMessage(), relX, relY + 92, mouseX, mouseY);
            if (this.selectedFilter.isHighlightMessage() && this.selectedFilter.getHighlightColor() != null) {
                this.drawElementSlider(this.selectedFilter.getHighlightColor().getRed(), relX, relY + 92 + 15, 0, mouseX, mouseY);
                this.drawElementSlider(this.selectedFilter.getHighlightColor().getGreen(), relX, relY + 92 + 15 + 9, 1, mouseX, mouseY);
                this.drawElementSlider(this.selectedFilter.getHighlightColor().getBlue(), relX, relY + 92 + 15 + 18, 2, mouseX, mouseY);
                Gui.drawRect(relX + 85, relY + 92 + 1, relX + 85 + 9, relY + 92 + 1 + 9, this.selectedFilter.getHighlightColor().getRGB());
            }
            else {
                relY -= 26;
            }
            this.drawElementCheckBox("hide", this.selectedFilter.isHideMessage(), relX, relY + 115 + 15, mouseX, mouseY);
            this.drawElementCheckBox("second_chat", this.selectedFilter.isDisplayInSecondChat(), relX, relY + 115 + 15 + 12, mouseX, mouseY);
            this.drawElementCheckBox("tooltip", this.selectedFilter.isFilterTooltips(), relX, relY + 115 + 15 + 24, mouseX, mouseY);
            final boolean hoverCancel = mouseX > GuiChatFilter.width - 268 && mouseX < GuiChatFilter.width - 213 && mouseY > GuiChatFilter.height - 30 && mouseY < GuiChatFilter.height - 18;
            final boolean hoverSave = mouseX > GuiChatFilter.width - 210 && mouseX < GuiChatFilter.width - 154 && mouseY > GuiChatFilter.height - 30 && mouseY < GuiChatFilter.height - 18;
            Gui.drawRect(GuiChatFilter.width - 268, GuiChatFilter.height - 30, GuiChatFilter.width - 213, GuiChatFilter.height - 18, hoverCancel ? ModColor.toRGB(200, 100, 100, 200) : Integer.MAX_VALUE);
            Gui.drawRect(GuiChatFilter.width - 210, GuiChatFilter.height - 30, GuiChatFilter.width - 154, GuiChatFilter.height - 18, hoverSave ? ModColor.toRGB(100, 200, 100, 200) : Integer.MAX_VALUE);
            Gui.drawCenteredString(LabyModCore.getMinecraft().getFontRenderer(), LanguageManager.translate("button_cancel"), GuiChatFilter.width - 262 + 22, GuiChatFilter.height - 30 + 2, Integer.MAX_VALUE);
            Gui.drawCenteredString(LabyModCore.getMinecraft().getFontRenderer(), LanguageManager.translate("button_save"), GuiChatFilter.width - 205 + 23, GuiChatFilter.height - 30 + 2, Integer.MAX_VALUE);
            this.textFieldFilterName.drawTextBox();
            this.textFieldFilterContains.drawTextBox();
            this.textFieldFilterContainsNot.drawTextBox();
            this.textFieldFilterRoom.drawTextBox();
            if (this.selectedFilter.isPlaySound()) {
                this.textFieldFilterSoundfile.drawTextBox();
            }
            if (this.textFieldFilterSoundfile.isFocused() && this.selectedFilter.isPlaySound() && this.textFieldFilterSoundfile != null && !this.textFieldFilterSoundfile.getText().isEmpty()) {
                int count = 0;
                String hint = "";
                for (final String path : GuiChatFilter.soundNames) {
                    final String lowerCase = this.textFieldFilterSoundfile.getText().toLowerCase();
                    if (path.startsWith(lowerCase) && !path.equals(lowerCase)) {
                        hint = String.valueOf(hint) + path + "\n";
                        if (++count > 5) {
                            break;
                        }
                        continue;
                    }
                }
                if (count == 0) {
                    for (final String path : GuiChatFilter.soundNames) {
                        final String lowerCase = this.textFieldFilterSoundfile.getText().toLowerCase();
                        if (path.contains(lowerCase) && !path.equals(lowerCase)) {
                            hint = String.valueOf(hint) + path + "\n";
                            if (++count > 5) {
                                break;
                            }
                            continue;
                        }
                    }
                }
                if (count != 0) {
                    LabyMod.getInstance().getDrawUtils().drawHoveringText(relX, relYAtSoundHint + 40, hint.split("\n"));
                }
            }
            if (this.textFieldFilterRoom.isFocused()) {
                int count = 0;
                String hint = "";
                if (this.textFieldFilterRoom.getText().isEmpty() || "Global".contains(this.textFieldFilterRoom.getText().toUpperCase())) {
                    ++count;
                    hint = String.valueOf(hint) + "Global";
                }
                for (final Filters.Filter filterComponent : LabyMod.getInstance().getChatToolManager().getFilters()) {
                    if (filterComponent.getRoom() != null && filterComponent.getRoom().toLowerCase().contains(this.textFieldFilterRoom.getText().toLowerCase()) && !hint.contains(filterComponent.getRoom())) {
                        hint = String.valueOf(hint) + filterComponent.getRoom() + "\n";
                        ++count;
                    }
                }
                if (count != 0) {
                    LabyMod.getInstance().getDrawUtils().drawHoveringText(relX, relYAtRoomHint + 40, hint.split("\n"));
                }
            }
        }
        this.drawString(LabyModCore.getMinecraft().getFontRenderer(), LanguageManager.translate("ingame_chat_tab_filter"), GuiChatFilter.width - 150, GuiChatFilter.height - 230, -1);
        if (this.sliderDrag != -1) {
            this.mouseClickMove(mouseX, mouseY, 0, 0L);
        }
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.CLICKED);
        if (this.selectedFilter == null && mouseX > GuiChatFilter.width - 165 && mouseX < GuiChatFilter.width - 152 && mouseY > GuiChatFilter.height - 220 && mouseY < GuiChatFilter.height - 220 + 13) {
            this.loadFilter(new Filters.Filter("", new String[0], new String[0], false, "note.harp", true, (short)200, (short)200, (short)50, false, false, false, "Global"));
        }
        if (this.selectedFilter == null) {
            int row = 0;
            Filters.Filter todoDelete = null;
            for (final Filters.Filter component : LabyMod.getInstance().getChatToolManager().getFilters()) {
                final double posY = GuiChatFilter.height - 215 + row * 10 + this.scrollbar.getScrollY();
                ++row;
                if (posY >= GuiChatFilter.height - 220) {
                    if (posY > GuiChatFilter.height - 25) {
                        continue;
                    }
                    if (mouseX > GuiChatFilter.width - 12 - 1 && mouseX < GuiChatFilter.width - 12 + 7 && mouseY > posY && mouseY < posY + 8.0) {
                        todoDelete = component;
                    }
                    else {
                        if (mouseX <= GuiChatFilter.width - 150 + 1 || mouseX >= GuiChatFilter.width - 2 - 1 || mouseY <= posY - 1.0) {
                            continue;
                        }
                        if (mouseY >= posY + 9.0) {
                            continue;
                        }
                        this.loadFilter(new Filters.Filter(component));
                        this.editStartName = component.getFilterName();
                    }
                }
            }
            if (todoDelete != null) {
                LabyMod.getInstance().getChatToolManager().getFilters().remove(todoDelete);
                FilterChatManager.removeFilterComponent(todoDelete);
                LabyMod.getInstance().getChatToolManager().saveTools();
            }
        }
        else {
            if (this.textFieldFilterRoom.getText().contains(" ") || this.selectedFilter.getRoom() == null || this.selectedFilter.getRoom().contains(" ")) {
                this.textFieldFilterRoom.setText(this.textFieldFilterRoom.getText().replaceAll(" ", ""));
                this.selectedFilter.setRoom(this.textFieldFilterRoom.getText());
            }
            if (this.textFieldFilterRoom.getText().isEmpty() || this.selectedFilter.getRoom() == null || this.selectedFilter.getRoom().isEmpty()) {
                this.textFieldFilterRoom.setText("Global");
                this.selectedFilter.setRoom(this.textFieldFilterRoom.getText());
            }
            final int relX = GuiChatFilter.width - 270;
            int relY = GuiChatFilter.height - 220;
            relY += 23;
            if (this.isHoverElementCheckbox("play_sound", this.selectedFilter.isPlaySound(), relX, relY + 69, mouseX, mouseY)) {
                this.selectedFilter.setPlaySound(!this.selectedFilter.isPlaySound());
            }
            if (!this.selectedFilter.isPlaySound()) {
                relY -= 10;
            }
            if (this.isHoverElementCheckbox("highlight", this.selectedFilter.isHighlightMessage(), relX, relY + 92, mouseX, mouseY)) {
                this.selectedFilter.setHighlightMessage(!this.selectedFilter.isHighlightMessage());
            }
            if (this.selectedFilter.isHighlightMessage() && this.selectedFilter.getHighlightColor() != null) {
                this.dragElementSlider(relX, relY + 92 + 15, 0, mouseX, mouseY);
                this.dragElementSlider(relX, relY + 92 + 15 + 9, 1, mouseX, mouseY);
                this.dragElementSlider(relX, relY + 92 + 15 + 18, 2, mouseX, mouseY);
            }
            else {
                relY -= 26;
            }
            if (this.isHoverElementCheckbox("hide", this.selectedFilter.isHideMessage(), relX, relY + 115 + 15, mouseX, mouseY)) {
                this.selectedFilter.setHideMessage(!this.selectedFilter.isHideMessage());
            }
            if (this.isHoverElementCheckbox("second_chat", this.selectedFilter.isDisplayInSecondChat(), relX, relY + 115 + 15 + 12, mouseX, mouseY)) {
                this.selectedFilter.setDisplayInSecondChat(!this.selectedFilter.isDisplayInSecondChat());
            }
            if (this.isHoverElementCheckbox("tooltip", this.selectedFilter.isFilterTooltips(), relX, relY + 115 + 15 + 24, mouseX, mouseY)) {
                this.selectedFilter.setFilterTooltips(!this.selectedFilter.isFilterTooltips());
            }
            final boolean hoverCancel = mouseX > GuiChatFilter.width - 268 && mouseX < GuiChatFilter.width - 213 && mouseY > GuiChatFilter.height - 30 && mouseY < GuiChatFilter.height - 18;
            final boolean hoverSave = mouseX > GuiChatFilter.width - 210 && mouseX < GuiChatFilter.width - 154 && mouseY > GuiChatFilter.height - 30 && mouseY < GuiChatFilter.height - 18;
            if (hoverCancel) {
                this.selectedFilter = null;
            }
            if (hoverSave) {
                if (this.selectedFilter.getFilterName().replaceAll(" ", "").isEmpty()) {
                    this.markFilterNameRed = true;
                }
                if (this.selectedFilter.getWordsContains().length == 0) {
                    this.markContainsRed = true;
                }
                else {
                    this.markContainsRed = false;
                }
                if (this.selectedFilter.isPlaySound() && !GuiChatFilter.soundNames.contains(this.textFieldFilterSoundfile.getText().toLowerCase())) {
                    this.markSoundNameRed = true;
                }
                else {
                    this.markSoundNameRed = false;
                }
                if (!this.markFilterNameRed && !this.markSoundNameRed && !this.markContainsRed) {
                    final Iterator<Filters.Filter> it = LabyMod.getInstance().getChatToolManager().getFilters().iterator();
                    while (it.hasNext()) {
                        final Filters.Filter next = it.next();
                        if (this.editStartName == null) {
                            if (!next.getFilterName().equalsIgnoreCase(this.selectedFilter.getFilterName())) {
                                continue;
                            }
                            it.remove();
                        }
                        else {
                            if (!next.getFilterName().equalsIgnoreCase(this.editStartName)) {
                                continue;
                            }
                            it.remove();
                        }
                    }
                    if (!LabyMod.getInstance().getChatToolManager().getFilters().contains(this.selectedFilter)) {
                        LabyMod.getInstance().getChatToolManager().getFilters().add(this.selectedFilter);
                    }
                    LabyMod.getInstance().getChatToolManager().saveTools();
                    FilterChatManager.getFilterResults().clear();
                    Minecraft.getMinecraft().ingameGUI.getChatGUI().refreshChat();
                    this.selectedFilter = null;
                    this.initGui();
                }
            }
        }
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (this.selectedFilter != null && (this.textFieldFilterName.isFocused() || this.textFieldFilterContains.isFocused() || this.textFieldFilterContainsNot.isFocused() || this.textFieldFilterRoom.isFocused() || this.textFieldFilterSoundfile.isFocused())) {
            if (keyCode == 15) {
                if (this.textFieldFilterName.isFocused()) {
                    this.textFieldFilterName.setFocused(false);
                    this.textFieldFilterContains.setFocused(true);
                    return;
                }
                if (this.textFieldFilterContains.isFocused()) {
                    this.textFieldFilterContains.setFocused(false);
                    this.textFieldFilterContainsNot.setFocused(true);
                    return;
                }
                if (this.textFieldFilterContainsNot.isFocused()) {
                    this.textFieldFilterContainsNot.setFocused(false);
                    this.textFieldFilterRoom.setFocused(true);
                    return;
                }
                if (this.textFieldFilterRoom.isFocused()) {
                    this.textFieldFilterRoom.setFocused(false);
                    if (this.selectedFilter.isPlaySound()) {
                        this.textFieldFilterSoundfile.setFocused(true);
                    }
                    else {
                        this.textFieldFilterName.setFocused(true);
                    }
                    return;
                }
                if (this.textFieldFilterSoundfile.isFocused()) {
                    this.textFieldFilterSoundfile.setFocused(false);
                    this.textFieldFilterName.setFocused(true);
                    return;
                }
            }
            if (this.textFieldFilterName.textboxKeyTyped(typedChar, keyCode)) {
                final String newText = this.textFieldFilterName.getText().replaceAll(" ", "");
                boolean equals = false;
                for (final Filters.Filter filter : LabyMod.getInstance().getChatToolManager().getFilters()) {
                    if (filter.getFilterName().equalsIgnoreCase(newText) && !filter.getFilterName().equalsIgnoreCase(this.editStartName)) {
                        equals = true;
                        break;
                    }
                }
                if (equals) {
                    this.markFilterNameRed = true;
                    return;
                }
                this.selectedFilter.setFilterName(newText);
                this.markFilterNameRed = false;
            }
            if (this.textFieldFilterContains.textboxKeyTyped(typedChar, keyCode)) {
                this.selectedFilter.setWordsContains(this.splitWords(this.textFieldFilterContains.getText()));
                this.markContainsRed = false;
            }
            if (this.textFieldFilterContainsNot.textboxKeyTyped(typedChar, keyCode)) {
                this.selectedFilter.setWordsContainsNot(this.splitWords(this.textFieldFilterContainsNot.getText()));
            }
            if (this.textFieldFilterRoom.textboxKeyTyped(typedChar, keyCode)) {
                this.selectedFilter.setRoom(this.textFieldFilterRoom.getText());
            }
            if (this.textFieldFilterSoundfile.textboxKeyTyped(typedChar, keyCode)) {
                this.selectedFilter.setSoundPath(this.textFieldFilterSoundfile.getText().toLowerCase());
                this.markSoundNameRed = false;
            }
        }
        else {
            super.keyTyped(typedChar, keyCode);
        }
    }
    
    @Override
    protected void mouseClickMove(final int mouseX, final int mouseY, final int clickedMouseButton, final long timeSinceLastClick) {
        this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.DRAGGING);
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        if (this.selectedFilter != null) {
            final int relX = GuiChatFilter.width - 270;
            int relY = GuiChatFilter.height - 200;
            if (!this.selectedFilter.isPlaySound()) {
                relY -= 10;
            }
            if (this.selectedFilter.isHighlightMessage() && this.selectedFilter.getHighlightColor() != null) {
                if (this.sliderDrag == 0) {
                    this.dragElementSlider(relX, relY + 92 + 15, 0, mouseX, mouseY);
                }
                if (this.sliderDrag == 1) {
                    this.dragElementSlider(relX, relY + 92 + 15 + 9, 1, mouseX, mouseY);
                }
                if (this.sliderDrag == 2) {
                    this.dragElementSlider(relX, relY + 92 + 15 + 18, 2, mouseX, mouseY);
                }
            }
        }
    }
    
    @Override
    protected void mouseReleased(final int mouseX, final int mouseY, final int mouseButton) {
        super.mouseReleased(mouseX, mouseY, mouseButton);
        this.sliderDrag = -1;
        this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.RELEASED);
        this.textFieldFilterName.mouseClicked(mouseX, mouseY, mouseButton);
        this.textFieldFilterContains.mouseClicked(mouseX, mouseY, mouseButton);
        this.textFieldFilterContainsNot.mouseClicked(mouseX, mouseY, mouseButton);
        this.textFieldFilterRoom.mouseClicked(mouseX, mouseY, mouseButton);
        this.textFieldFilterSoundfile.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    public void updateScreen() {
        super.updateScreen();
        this.textFieldFilterName.updateCursorCounter();
        this.textFieldFilterContains.updateCursorCounter();
        this.textFieldFilterContainsNot.updateCursorCounter();
        this.textFieldFilterRoom.updateCursorCounter();
        this.textFieldFilterSoundfile.updateCursorCounter();
    }
    
    private void drawElementTextField(String prefix, final GuiTextField textField, final int x, final int y, final int mouseX, final int mouseY) {
        if (!prefix.isEmpty()) {
            prefix = String.valueOf(LanguageManager.translate(new StringBuilder("chat_filter_").append(prefix).toString())) + ":";
        }
        this.drawString(LabyModCore.getMinecraft().getFontRenderer(), prefix, x + 2, y + 2, Integer.MAX_VALUE);
        Gui.drawRect(x + 2, y + 12, x + 2 + 114, y + 12 + 10, ((this.markContainsRed && textField != null && textField.equals(this.textFieldFilterContains)) || (this.markFilterNameRed && textField != null && textField.equals(this.textFieldFilterName)) || (this.markSoundNameRed && textField != null && textField.equals(this.textFieldFilterSoundfile))) ? ModColor.toRGB(200, 100, 100, 200) : Integer.MAX_VALUE);
        if (textField == null) {
            return;
        }
        LabyModCore.getMinecraft().setTextFieldXPosition(textField, x + 2);
        LabyModCore.getMinecraft().setTextFieldYPosition(textField, y + 13);
        textField.setEnableBackgroundDrawing(false);
        textField.setMaxStringLength(120);
    }
    
    private void drawElementCheckBox(String text, final boolean check, int x, final int y, final int mouseX, final int mouseY) {
        final boolean hover = this.isHoverElementCheckbox(text, check, x, y, mouseX, mouseY);
        text = LanguageManager.translate("chat_filter_" + text);
        this.drawString(LabyModCore.getMinecraft().getFontRenderer(), text, x + 2, y + 2, Integer.MAX_VALUE);
        x += LabyModCore.getMinecraft().getFontRenderer().getStringWidth(text) + 2;
        Gui.drawRect(x + 3, y + 1, x + 12, y + 10, hover ? 2147483547 : Integer.MAX_VALUE);
        if (!check) {
            return;
        }
        Gui.drawCenteredString(LabyModCore.getMinecraft().getFontRenderer(), String.valueOf(ModColor.cl("a")) + "\u2714", x + 8, y + 1, Integer.MAX_VALUE);
    }
    
    private void drawElementSlider(final int value, final int x, final int y, final int id, final int mouseX, final int mouseY) {
        Gui.drawRect(x + 2, y, x + 2 + 94, y + 1, Integer.MAX_VALUE);
        final double colorValue = value;
        final double percent = colorValue / 255.0 * 94.0;
        final int pos = (int)percent;
        Gui.drawRect(x + 2 + pos, y - 3, x + 2 + pos + 2, y + 5, ModColor.toRGB((id == 0) ? value : 0, (id == 1) ? value : 0, (id == 2) ? value : 0, 255));
    }
    
    private boolean isHoverElementCheckbox(String text, final boolean check, int x, final int y, final int mouseX, final int mouseY) {
        text = LanguageManager.translate("chat_filter_" + text);
        x += LabyModCore.getMinecraft().getFontRenderer().getStringWidth(text) + 2;
        return mouseX > x + 3 && mouseX < x + 12 && mouseY > y + 1 && mouseY < y + 10;
    }
    
    private void dragElementSlider(final int x, final int y, final int id, int mouseX, final int mouseY) {
        if (mouseX <= x || mouseX >= x + 94 || ((mouseY <= y - 5 || mouseY >= y + 5) && this.sliderDrag != id)) {
            return;
        }
        if (mouseX < x) {
            mouseX = x;
        }
        if (mouseX > x + 94) {
            mouseX = x + 94;
        }
        final double pos = mouseX - x;
        final double value = pos * 255.0 / 94.0;
        final int colorValue = (int)value;
        final Color highlightColor = this.selectedFilter.getHighlightColor();
        int r = highlightColor.getRed();
        int g = highlightColor.getGreen();
        int b = highlightColor.getBlue();
        switch (id) {
            case 0: {
                r = colorValue;
                break;
            }
            case 1: {
                g = colorValue;
                break;
            }
            case 2: {
                b = colorValue;
                break;
            }
        }
        this.selectedFilter.setHighlightColorR((short)r);
        this.selectedFilter.setHighlightColorG((short)g);
        this.selectedFilter.setHighlightColorB((short)b);
        this.sliderDrag = id;
    }
    
    private void loadFilter(final Filters.Filter filter) {
        this.editStartName = null;
        this.selectedFilter = filter;
        this.markFilterNameRed = false;
        this.textFieldFilterName.setText(filter.getFilterName());
        this.textFieldFilterContains.setText(this.wordsToString(filter.getWordsContains()));
        this.textFieldFilterContainsNot.setText(this.wordsToString(filter.getWordsContainsNot()));
        this.textFieldFilterRoom.setText((filter.getRoom() == null || filter.getRoom().isEmpty()) ? "Global" : filter.getRoom());
        this.textFieldFilterSoundfile.setText(filter.getSoundPath());
    }
    
    private String[] splitWords(final String text) {
        return text.contains(",") ? text.split(",") : (text.isEmpty() ? new String[0] : new String[] { text });
    }
    
    private String wordsToString(final String[] words) {
        String output = "";
        for (final String word : words) {
            if (!output.isEmpty()) {
                output = String.valueOf(output) + ",";
            }
            output = String.valueOf(output) + word;
        }
        return output;
    }
}
