// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.gui;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.gui.Gui;
import java.util.Iterator;
import java.util.Map;
import net.labymod.main.LabyMod;
import java.io.IOException;
import net.minecraft.client.gui.GuiYesNo;
import net.labymod.utils.ModColor;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.Minecraft;
import net.labymod.utils.manager.TagManager;
import net.labymod.gui.elements.Tabs;
import net.labymod.main.lang.LanguageManager;
import net.minecraft.client.gui.GuiButton;
import net.labymod.gui.elements.Scrollbar;
import net.minecraft.client.gui.GuiScreen;

public class GuiTags extends GuiScreen
{
    private Scrollbar scrollbar;
    private String selectedTag;
    private String hoveredTag;
    private boolean addTagScreen;
    private GuiButton buttonEdit;
    private GuiButton buttonRemove;
    
    public GuiTags() {
        this.scrollbar = new Scrollbar(29);
        this.selectedTag = null;
        this.hoveredTag = null;
        this.addTagScreen = false;
    }
    
    @Override
    public void initGui() {
        super.initGui();
        this.scrollbar.init();
        this.scrollbar.setPosition(GuiTags.width / 2 + 102, 44, GuiTags.width / 2 + 106, GuiTags.height - 32 - 3);
        this.scrollbar.setSpeed(10);
        this.buttonList.add(this.buttonRemove = new GuiButton(1, GuiTags.width / 2 - 120, GuiTags.height - 26, 75, 20, LanguageManager.translate("button_remove")));
        this.buttonList.add(this.buttonEdit = new GuiButton(2, GuiTags.width / 2 - 37, GuiTags.height - 26, 75, 20, LanguageManager.translate("button_edit")));
        this.buttonList.add(new GuiButton(3, GuiTags.width / 2 + 120 - 75, GuiTags.height - 26, 75, 20, LanguageManager.translate("button_add")));
        Tabs.initGuiScreen(this.buttonList, this);
    }
    
    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        TagManager.save();
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        super.actionPerformed(button);
        switch (button.id) {
            case 1: {
                final GuiScreen lastScreen = Minecraft.getMinecraft().currentScreen;
                Minecraft.getMinecraft().displayGuiScreen(new GuiYesNo(new GuiYesNoCallback() {
                    @Override
                    public void confirmClicked(final boolean result, final int id) {
                        if (result) {
                            TagManager.getConfigManager().getSettings().getTags().remove(GuiTags.this.selectedTag);
                        }
                        Minecraft.getMinecraft().displayGuiScreen(lastScreen);
                    }
                }, LanguageManager.translate("warning_delete"), String.valueOf(ModColor.cl("c")) + this.selectedTag, 1));
                break;
            }
            case 2: {
                Minecraft.getMinecraft().displayGuiScreen(new GuiTagsAdd(this, this.selectedTag));
                break;
            }
            case 3: {
                Minecraft.getMinecraft().displayGuiScreen(new GuiTagsAdd(this, null));
                break;
            }
        }
        Tabs.actionPerformedButton(button);
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        LabyMod.getInstance().getDrawUtils().drawAutoDimmedBackground(this.scrollbar.getScrollY());
        if (this.addTagScreen) {
            return;
        }
        this.hoveredTag = null;
        double yPos = 45.0 + this.scrollbar.getScrollY() + 3.0;
        final Map<String, String> tags = TagManager.getConfigManager().getSettings().getTags();
        for (final String tag : tags.keySet()) {
            this.drawEntry(tag, tags.get(tag), yPos, mouseX, mouseY);
            yPos += 29.0;
        }
        LabyMod.getInstance().getDrawUtils().drawOverlayBackground(0, 41);
        LabyMod.getInstance().getDrawUtils().drawOverlayBackground(GuiTags.height - 32, GuiTags.height);
        LabyMod.getInstance().getDrawUtils().drawGradientShadowTop(41.0, 0.0, GuiTags.width);
        LabyMod.getInstance().getDrawUtils().drawGradientShadowBottom(GuiTags.height - 32, 0.0, GuiTags.width);
        LabyMod.getInstance().getDrawUtils().drawCenteredString(LanguageManager.translate("title_tags"), GuiTags.width / 2, 29.0);
        this.scrollbar.update(tags.size());
        this.scrollbar.draw();
        this.buttonEdit.enabled = (this.selectedTag != null);
        this.buttonRemove.enabled = (this.selectedTag != null);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    private void drawEntry(final String key, final String value, final double y, final int mouseX, final int mouseY) {
        final int x = GuiTags.width / 2 - 100;
        final boolean hovered = mouseX > x && mouseX < x + 200 && mouseY > y && mouseY < y + 24.0 && mouseX > 32 && mouseY < GuiTags.height - 32;
        if (hovered) {
            this.hoveredTag = key;
        }
        final int borderColor = (this.selectedTag == key) ? ModColor.toRGB(240, 240, 240, 240) : Integer.MIN_VALUE;
        final int backgroundColor = hovered ? ModColor.toRGB(50, 50, 50, 120) : ModColor.toRGB(30, 30, 30, 120);
        Gui.drawRect(x - 5, (int)y - 4, x + 200, (int)y + 24, backgroundColor);
        LabyMod.getInstance().getDrawUtils().drawRectBorder(x - 5, (int)y - 4, x + 200, (int)y + 24, borderColor, 1.0);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        final int headSize = 20;
        LabyMod.getInstance().getDrawUtils().drawPlayerHead(key, x, (int)y, 20);
        LabyMod.getInstance().getDrawUtils().drawString(key, x + 20 + 5, y + 1.0);
        String tagValue = value.replaceAll("&", "\u00c2§");
        tagValue = LabyMod.getInstance().getDrawUtils().trimStringToWidth(tagValue, 170);
        LabyMod.getInstance().getDrawUtils().drawString(tagValue, x + 20 + 5, y + 10.0 + 1.0);
        Tabs.drawParty(mouseX, mouseY, GuiTags.width);
    }
    
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.hoveredTag != null) {
            this.selectedTag = this.hoveredTag;
        }
        this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.CLICKED);
    }
    
    @Override
    protected void mouseClickMove(final int mouseX, final int mouseY, final int clickedMouseButton, final long timeSinceLastClick) {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.DRAGGING);
    }
    
    @Override
    protected void mouseReleased(final int mouseX, final int mouseY, final int state) {
        this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.RELEASED);
        super.mouseReleased(mouseX, mouseY, state);
    }
    
    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.scrollbar.mouseInput();
    }
}
