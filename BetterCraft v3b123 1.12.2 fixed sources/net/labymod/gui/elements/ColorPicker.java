// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.gui.elements;

import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import net.labymod.core.LabyModCore;
import net.labymod.settings.PreviewRenderer;
import net.labymod.settings.LabyModModuleEditorGui;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.GuiScreen;
import net.labymod.utils.ModColor;
import net.labymod.utils.manager.TooltipHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.labymod.main.ModTextures;
import net.minecraft.client.Minecraft;
import net.labymod.main.LabyMod;
import net.labymod.utils.Consumer;
import java.awt.Color;
import net.minecraft.client.gui.Gui;

public class ColorPicker extends Gui
{
    private static final int[] ADVANCED_COLORS;
    private String title;
    private int x;
    private int y;
    private int width;
    private int height;
    private Color selectedColor;
    private Color colorForPreview;
    private boolean openedSelector;
    private boolean hoverSlider;
    private boolean hasAdvanced;
    private boolean hoverAdvancedButton;
    private boolean hoverDefaultButton;
    private boolean hasDefault;
    private boolean isDefault;
    private DefaultColorCallback defaultColor;
    private Consumer<Color> updateListener;
    
    static {
        ADVANCED_COLORS = new int[] { -4842468, -7795121, -11922292, -13624430, -15064194, -15841375, -16754788, -16687004, -16757697, -14918112, -13407970, -8292586, -753898, -37120, -1683200, -4246004, -12704222, -14606047, -14208456 };
    }
    
    public ColorPicker(final String title, final Color selectedColor, final DefaultColorCallback defaultColorCallback, final int x, final int y, final int width, final int height) {
        this.hoverSlider = false;
        this.hasAdvanced = false;
        this.hoverAdvancedButton = false;
        this.hasDefault = false;
        this.isDefault = true;
        this.title = title;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.selectedColor = selectedColor;
        this.colorForPreview = selectedColor;
        this.defaultColor = defaultColorCallback;
    }
    
    public void onGuiClosed() {
        if (this.colorForPreview != this.selectedColor && this.updateListener != null) {
            this.updateListener.accept(this.selectedColor);
        }
    }
    
    public void drawColorPicker(final int mouseX, final int mouseY) {
        LabyMod.getInstance().getDrawUtils().drawCenteredString(this.title, this.x + this.width / 2, this.y - 5, 0.5);
        Gui.drawRect(this.x, this.y, this.x + this.width, this.y + this.height, this.openedSelector ? -1 : Integer.MAX_VALUE);
        final int bgColor = (this.colorForPreview == null) ? ((this.defaultColor == null) ? (this.openedSelector ? Integer.MIN_VALUE : Integer.MAX_VALUE) : this.defaultColor.getDefaultColor().getRGB()) : this.colorForPreview.getRGB();
        Gui.drawRect(this.x + 1, this.y + 1, this.x + this.width - 1, this.y + this.height - 1, bgColor);
        if (this.hasDefault && this.selectedColor == null && this.isDefault) {
            Minecraft.getMinecraft().getTextureManager().bindTexture(ModTextures.BUTTON_HOVER_DEFAULT);
            final Color color = new Color(bgColor);
            int luma = (int)(0.2126f * color.getRed() + 0.7152f * color.getGreen() + 0.0722f * color.getBlue());
            luma = 255 - luma;
            if (luma < 80) {
                GlStateManager.color(luma / 255.0f, luma / 255.0f, luma / 255.0f, 1.0f);
                LabyMod.getInstance().getDrawUtils().drawTexture(this.x + 2, this.y + 2, 256.0, 256.0, this.width - 4, this.height - 4, 1.1f);
            }
            else {
                LabyMod.getInstance().getDrawUtils().drawTexture(this.x + 2, this.y + 2, 256.0, 256.0, this.width - 4, this.height - 4);
            }
            if (this.isMouseOver(mouseX, mouseY)) {
                TooltipHelper.getHelper().pointTooltip(mouseX, mouseY, 200L, "Default");
            }
        }
        if (this.openedSelector) {
            this.drawColorsAndButtons(mouseX, mouseY);
        }
    }
    
    private Color getContrastColor(final int r, final int g, final int b) {
        final double y = (299 * r + 587 * g + 114 * b) / 1000;
        return (y >= 128.0) ? Color.black : Color.white;
    }
    
    private void drawColorsAndButtons(final int mouseX, final int mouseY) {
        final int widthPerColor = 13;
        final int sliderHeight = 13;
        int sliderWidth = 0;
        ModColor[] values;
        for (int length = (values = ModColor.values()).length, i = 0; i < length; ++i) {
            final ModColor color = values[i];
            if (color.getColor() != null) {
                sliderWidth += 13;
            }
        }
        int sliderX = this.x - sliderWidth / 2 + this.width / 2;
        int sliderY = this.y + this.height + 4;
        if (this.hasAdvanced) {
            sliderX -= 20;
        }
        final int minX = (this.hasDefault && this.selectedColor != null) ? 20 : 5;
        final int maxX = LabyMod.getInstance().getDrawUtils().getWidth() - 5;
        final int maxY = LabyMod.getInstance().getDrawUtils().getHeight() - 5;
        if (sliderX + sliderWidth > maxX) {
            sliderX -= sliderX + sliderWidth - maxX;
        }
        if (sliderX < minX) {
            sliderX = minX;
        }
        if (sliderY > maxY) {
            sliderY = maxY - 13 - this.height;
        }
        else {
            Gui.drawRect(this.x + this.width / 2 - 1, sliderY - 3, this.x + this.width / 2 + 1, sliderY - 2, Integer.MAX_VALUE);
            Gui.drawRect(this.x + this.width / 2 - 2, sliderY - 2, this.x + this.width / 2 + 2, sliderY - 1, Integer.MAX_VALUE);
        }
        if (!(Minecraft.getMinecraft().currentScreen instanceof AdvancedColorSelectorGui)) {
            this.drawSlider(mouseX, mouseY, sliderX, sliderY, sliderWidth, 13, 13);
            this.drawButtons(mouseX, mouseY, sliderX, sliderY, sliderWidth, 13, 13);
        }
        this.hoverAdvancedButton = (mouseX > sliderX + sliderWidth + 3 - 1 && mouseX < sliderX + sliderWidth + 3 + 13 + 1 && mouseY > sliderY - 1 && mouseY < sliderY + 13 + 1);
        this.hoverSlider = (mouseX > sliderX && mouseX < sliderX + sliderWidth + 13 && mouseY > sliderY && mouseY < sliderY + 13);
        this.hoverDefaultButton = (mouseX > sliderX - 3 - 13 - 1 && mouseX < sliderX - 3 + 1 && mouseY > sliderY - 1 && mouseY < sliderY + 13 + 1);
    }
    
    private void drawSlider(final int mouseX, final int mouseY, final int sliderX, final int sliderY, final int sliderWidth, final int sliderHeight, final int widthPerColor) {
        Gui.drawRect(sliderX - 1, sliderY - 1, sliderX + sliderWidth + 1, sliderY + sliderHeight + 1, Integer.MAX_VALUE);
        int pos = 0;
        int hoverPos = -1;
        int selectedPos = -1;
        ModColor hoverColorType = null;
        ModColor selectedColorType = null;
        ModColor[] values;
        for (int length = (values = ModColor.values()).length, i = 0; i < length; ++i) {
            final ModColor color = values[i];
            if (color.getColor() != null) {
                Gui.drawRect(sliderX + pos, sliderY, sliderX + pos + widthPerColor, sliderY + sliderHeight, color.getColor().getRGB());
                final boolean hoverColor = mouseX > sliderX + pos && mouseX < sliderX + pos + widthPerColor + 1 && mouseY > sliderY && mouseY < sliderY + sliderHeight;
                if (hoverPos == -1 && hoverColorType == null && hoverColor) {
                    hoverPos = pos;
                    hoverColorType = color;
                }
                if (color.getColor() == this.selectedColor) {
                    selectedPos = pos;
                    selectedColorType = color;
                }
                pos += widthPerColor;
            }
        }
        if (hoverColorType != null) {
            Gui.drawRect(sliderX + hoverPos - 1, sliderY - 1, sliderX + hoverPos + widthPerColor + 1, sliderY + sliderHeight + 1, hoverColorType.getColor().getRGB());
            this.colorForPreview = hoverColorType.getColor();
            if (this.updateListener != null) {
                this.updateListener.accept(this.colorForPreview);
            }
        }
        else {
            this.colorForPreview = this.selectedColor;
            if (this.updateListener != null) {
                this.updateListener.accept(this.selectedColor);
            }
        }
        if (selectedColorType != null) {
            Gui.drawRect(sliderX + selectedPos - 1, sliderY - 1, sliderX + selectedPos + widthPerColor + 1, sliderY + sliderHeight + 1, -1);
            Gui.drawRect(sliderX + selectedPos, sliderY, sliderX + selectedPos + widthPerColor, sliderY + sliderHeight, selectedColorType.getColor().getRGB());
        }
    }
    
    private void drawAdvanced(final int mouseX, final int mouseY, final int advancedX, final int advancedY, final int advancedWidth) {
        final int alphaCount = 12;
        final double widthPerColor;
        final double heightPerColor = widthPerColor = advancedWidth / (double)ColorPicker.ADVANCED_COLORS.length;
        Gui.drawRect(advancedX - 1, advancedY - 1, (int)(advancedX + widthPerColor * ColorPicker.ADVANCED_COLORS.length + 1.0), (int)(advancedY + heightPerColor * 12.0 + 1.0), Integer.MAX_VALUE);
        double hoverPosX = -1.0;
        double hoverPosY = -1.0;
        Color hoveredColorType = null;
        double selectedPosX = -1.0;
        double selectedPosY = -1.0;
        Color selectedColorType = null;
        double posX = 0.0;
        int[] advanced_COLORS;
        for (int length = (advanced_COLORS = ColorPicker.ADVANCED_COLORS).length, i = 0; i < length; ++i) {
            final int color = advanced_COLORS[i];
            for (int posY = 0; posY < 12; ++posY) {
                final int rgb = ModColor.changeBrightness(new Color(color), 0.07f * posY).getRGB();
                Gui.drawRect((int)(advancedX + posX), (int)(advancedY + posY * heightPerColor), (int)(advancedX + posX + widthPerColor), (int)(advancedY + posY * heightPerColor + heightPerColor), rgb);
                final boolean hoverColor = mouseX > advancedX + posX && mouseX < advancedX + posX + widthPerColor + 1.0 && mouseY > advancedY + posY * heightPerColor && mouseY < advancedY + posY * heightPerColor + heightPerColor + 1.0;
                if (hoverColor) {
                    hoverPosX = posX;
                    hoverPosY = posY;
                    hoveredColorType = new Color(rgb);
                }
                if (this.selectedColor != null && rgb == this.selectedColor.getRGB()) {
                    selectedPosX = posX;
                    selectedPosY = posY;
                    selectedColorType = this.selectedColor;
                }
            }
            posX += widthPerColor;
        }
        if (hoveredColorType != null) {
            Gui.drawRect((int)(advancedX + hoverPosX - 1.0), (int)(advancedY + hoverPosY * heightPerColor - 1.0), (int)(advancedX + hoverPosX + widthPerColor + 1.0), (int)(advancedY + hoverPosY * heightPerColor + heightPerColor + 1.0), hoveredColorType.getRGB());
            this.colorForPreview = hoveredColorType;
            if (this.updateListener != null) {
                this.updateListener.accept(this.colorForPreview);
            }
        }
        else {
            this.colorForPreview = this.selectedColor;
            if (this.updateListener != null) {
                this.updateListener.accept(this.selectedColor);
            }
        }
        if (selectedColorType != null) {
            Gui.drawRect((int)(advancedX + selectedPosX - 1.0), (int)(advancedY + selectedPosY * heightPerColor - 1.0), (int)(advancedX + selectedPosX + widthPerColor + 1.0), (int)(advancedY + selectedPosY * heightPerColor + heightPerColor + 1.0), -1);
            Gui.drawRect((int)(advancedX + selectedPosX), (int)(advancedY + selectedPosY * heightPerColor), (int)(advancedX + selectedPosX + widthPerColor), (int)(advancedY + selectedPosY * heightPerColor + heightPerColor), selectedColorType.getRGB());
        }
    }
    
    private void drawButtons(final int mouseX, final int mouseY, final int sliderX, final int sliderY, final int sliderWidth, final int sliderHeight, final int widthPerColor) {
        if (this.hasDefault && this.selectedColor != null) {
            Gui.drawRect(sliderX - 3 - widthPerColor - 1, sliderY - 1, sliderX - 3 + 1, sliderY + sliderHeight + 1, this.hoverDefaultButton ? -1 : Integer.MAX_VALUE);
            Gui.drawRect(sliderX - 3 - widthPerColor, sliderY, sliderX - 3, sliderY + sliderHeight, Integer.MIN_VALUE);
            LabyMod.getInstance().getDrawUtils().fontRenderer.drawString("D", (float)(sliderX - 3 - widthPerColor / 2 - 3), (float)(sliderY + sliderHeight / 2 - 3), -1, false);
            if (this.hoverDefaultButton) {
                this.updateListener.accept(null);
                this.colorForPreview = this.defaultColor.getDefaultColor();
            }
        }
        if (this.hasAdvanced) {
            Gui.drawRect(sliderX + sliderWidth + 3 - 1, sliderY - 1, sliderX + sliderWidth + 3 + widthPerColor + 1, sliderY + sliderHeight + 1, this.hoverAdvancedButton ? -1 : Integer.MAX_VALUE);
            Gui.drawRect(sliderX + sliderWidth + 3, sliderY, sliderX + sliderWidth + 3 + widthPerColor, sliderY + sliderHeight, -1);
            final int iconX = sliderX + sliderWidth + 3;
            final int iconY = sliderY;
            double pxlX = iconX;
            int[] advanced_COLORS;
            for (int length = (advanced_COLORS = ColorPicker.ADVANCED_COLORS).length, j = 0; j < length; ++j) {
                final int color = advanced_COLORS[j];
                int pxlY = iconY;
                for (int i = 0; i < 13; ++i) {
                    final Color theColor = new Color(color + i * 2000);
                    final int rgb = ModColor.toRGB(theColor.getRed(), theColor.getGreen(), theColor.getBlue(), 255 - i * 18);
                    Gui.drawRect((int)pxlX, pxlY, (int)pxlX + 1, pxlY + 1, rgb);
                    ++pxlY;
                }
                pxlX += 0.7;
            }
            if (this.hoverAdvancedButton) {
                TooltipHelper.getHelper().pointTooltip(mouseX, mouseY, 0L, "More colors");
            }
        }
    }
    
    public boolean mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        if (this.isMouseOver(mouseX, mouseY)) {
            this.openedSelector = !this.openedSelector;
            return true;
        }
        if (this.openedSelector) {
            if (this.hoverSlider) {
                this.selectedColor = this.colorForPreview;
                if (this.updateListener != null) {
                    this.updateListener.accept(this.selectedColor);
                }
            }
            if (this.hasDefault && this.selectedColor != null && this.hoverDefaultButton) {
                this.selectedColor = null;
                this.colorForPreview = this.defaultColor.getDefaultColor();
                if (this.updateListener != null) {
                    this.updateListener.accept(this.selectedColor);
                }
                return true;
            }
            if (this.hasAdvanced && this.hoverAdvancedButton) {
                Minecraft.getMinecraft().displayGuiScreen(new AdvancedColorSelectorGui(this, Minecraft.getMinecraft().currentScreen, new Consumer<GuiScreen>() {
                    @Override
                    public void accept(final GuiScreen lastScreen) {
                        Minecraft.getMinecraft().displayGuiScreen(lastScreen);
                    }
                }));
                return true;
            }
        }
        final boolean flag = this.openedSelector;
        this.openedSelector = false;
        return flag ^ this.openedSelector;
    }
    
    public boolean mouseDragging(final int mouseX, final int mouseY, final int mouseButton) {
        return false;
    }
    
    public boolean mouseReleased(final int mouseX, final int mouseY, final int mouseButton) {
        return false;
    }
    
    public void setSelectedColor(final Color selectedColor) {
        this.selectedColor = selectedColor;
        this.colorForPreview = selectedColor;
    }
    
    public boolean isHoverAdvancedButton() {
        return this.hoverAdvancedButton;
    }
    
    public boolean isHoverDefaultButton() {
        return this.hoverDefaultButton;
    }
    
    public boolean isHoverSlider() {
        return this.hoverSlider;
    }
    
    public Color getSelectedColor() {
        return this.selectedColor;
    }
    
    public int getX() {
        return this.x;
    }
    
    public void setX(final int x) {
        this.x = x;
    }
    
    public int getY() {
        return this.y;
    }
    
    public void setY(final int y) {
        this.y = y;
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public void setWidth(final int width) {
        this.width = width;
    }
    
    public int getHeight() {
        return this.height;
    }
    
    public void setHeight(final int height) {
        this.height = height;
    }
    
    public boolean isMouseOver(final int mouseX, final int mouseY) {
        return mouseX > this.x && mouseX < this.x + this.width && mouseY > this.y && mouseY < this.y + this.height;
    }
    
    public String getTitle() {
        return this.title;
    }
    
    public Color getColorForPreview() {
        return this.colorForPreview;
    }
    
    public void setHasDefault(final boolean hasDefault) {
        this.hasDefault = hasDefault;
    }
    
    public void setDefault(final boolean aDefault) {
        this.isDefault = aDefault;
    }
    
    public void setHasAdvanced(final boolean hasAdvanced) {
        this.hasAdvanced = hasAdvanced;
    }
    
    public void setUpdateListener(final Consumer<Color> updateListener) {
        this.updateListener = updateListener;
    }
    
    static /* synthetic */ void access$3(final ColorPicker colorPicker, final Color colorForPreview) {
        colorPicker.colorForPreview = colorForPreview;
    }
    
    static /* synthetic */ void access$4(final ColorPicker colorPicker, final Color selectedColor) {
        colorPicker.selectedColor = selectedColor;
    }
    
    public class AdvancedColorSelectorGui extends GuiScreen
    {
        private GuiScreen backgroundScreen;
        private Consumer<GuiScreen> callback;
        private ColorPicker colorPicker;
        private GuiTextField fieldHexColor;
        private Color lastColor;
        private boolean validHex;
        
        public AdvancedColorSelectorGui(final ColorPicker colorPicker, final GuiScreen backgroundScreen, final Consumer<GuiScreen> callback) {
            this.lastColor = null;
            this.validHex = true;
            this.backgroundScreen = backgroundScreen;
            this.callback = callback;
            this.colorPicker = colorPicker;
        }
        
        @Override
        public void initGui() {
            super.initGui();
            GuiScreen.width = AdvancedColorSelectorGui.width;
            GuiScreen.height = AdvancedColorSelectorGui.height;
            if (this.backgroundScreen instanceof LabyModModuleEditorGui) {
                PreviewRenderer.getInstance().init(AdvancedColorSelectorGui.class);
            }
            (this.fieldHexColor = new GuiTextField(0, LabyModCore.getMinecraft().getFontRenderer(), AdvancedColorSelectorGui.width / 2 - 70, AdvancedColorSelectorGui.height / 4 + 115, 100, 16)).setMaxStringLength(7);
            this.lastColor = null;
            this.buttonList.add(new GuiButton(1, AdvancedColorSelectorGui.width / 2 + 40, AdvancedColorSelectorGui.height / 4 + 113, 60, 20, "Done"));
        }
        
        @Override
        public void onGuiClosed() {
            this.backgroundScreen.onGuiClosed();
            super.onGuiClosed();
        }
        
        @Override
        public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
            this.backgroundScreen.drawScreen(mouseX, mouseY, partialTicks);
            Gui.drawRect(0, 0, AdvancedColorSelectorGui.width, AdvancedColorSelectorGui.height, Integer.MIN_VALUE);
            this.colorPicker.drawColorPicker(mouseX, mouseY);
            Gui.drawRect(AdvancedColorSelectorGui.width / 2 - 105, AdvancedColorSelectorGui.height / 4 - 25, AdvancedColorSelectorGui.width / 2 + 105, AdvancedColorSelectorGui.height / 4 + 140, Integer.MIN_VALUE);
            this.colorPicker.drawAdvanced(mouseX, mouseY, AdvancedColorSelectorGui.width / 2 - 100, AdvancedColorSelectorGui.height / 4 - 20, 200);
            Gui.drawRect(LabyModCore.getMinecraft().getXPosition(this.fieldHexColor) - 2, LabyModCore.getMinecraft().getYPosition(this.fieldHexColor) - 2, LabyModCore.getMinecraft().getXPosition(this.fieldHexColor) + 100 + 2, LabyModCore.getMinecraft().getYPosition(this.fieldHexColor) + 16 + 2, this.validHex ? ModColor.toRGB(85, 255, 85, 100) : ModColor.toRGB(255, 85, 85, 100));
            this.fieldHexColor.drawTextBox();
            if (this.colorPicker.colorForPreview == null) {
                ColorPicker.access$3(this.colorPicker, this.colorPicker.defaultColor.getDefaultColor());
            }
            Gui.drawRect(AdvancedColorSelectorGui.width / 2 - 100, AdvancedColorSelectorGui.height / 4 + 113, AdvancedColorSelectorGui.width / 2 - 100 + 20, AdvancedColorSelectorGui.height / 4 + 113 + 20, Integer.MAX_VALUE);
            Gui.drawRect(AdvancedColorSelectorGui.width / 2 - 100 + 1, AdvancedColorSelectorGui.height / 4 + 113 + 1, AdvancedColorSelectorGui.width / 2 - 100 + 20 - 1, AdvancedColorSelectorGui.height / 4 + 113 + 20 - 1, this.colorPicker.colorForPreview.getRGB());
            if (this.lastColor == null || !this.lastColor.equals(this.colorPicker.colorForPreview)) {
                this.lastColor = this.colorPicker.colorForPreview;
                final String hex = String.format("#%02x%02x%02x", this.lastColor.getRed(), this.lastColor.getGreen(), this.lastColor.getBlue());
                this.fieldHexColor.setText(hex);
                this.validHex = true;
            }
            Gui.drawCenteredString(LabyModCore.getMinecraft().getFontRenderer(), "Advanced colors", AdvancedColorSelectorGui.width / 2, AdvancedColorSelectorGui.height / 4 - 35, -1);
            super.drawScreen(mouseX, mouseY, partialTicks);
        }
        
        public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
            super.mouseClicked(mouseX, mouseY, mouseButton);
            ColorPicker.access$4(this.colorPicker, this.colorPicker.colorForPreview);
            if (this.colorPicker.updateListener != null) {
                this.colorPicker.updateListener.accept(this.colorPicker.selectedColor);
            }
            this.fieldHexColor.mouseClicked(mouseX, mouseY, mouseButton);
        }
        
        @Override
        protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
            if (keyCode == 1) {
                Minecraft.getMinecraft().displayGuiScreen(this.backgroundScreen);
            }
            if (this.fieldHexColor.textboxKeyTyped(typedChar, keyCode)) {
                final String hex = this.fieldHexColor.getText();
                if (hex.length() == 7) {
                    try {
                        ColorPicker.access$4(this.colorPicker, new Color(Integer.valueOf(hex.substring(1, 3), 16), Integer.valueOf(hex.substring(3, 5), 16), Integer.valueOf(hex.substring(5, 7), 16)));
                        ColorPicker.access$3(this.colorPicker, this.colorPicker.selectedColor);
                        this.validHex = true;
                    }
                    catch (final Exception error) {
                        this.validHex = false;
                    }
                }
                else {
                    this.validHex = false;
                }
            }
        }
        
        @Override
        public void updateScreen() {
            this.backgroundScreen.updateScreen();
            this.fieldHexColor.updateCursorCounter();
        }
        
        @Override
        protected void actionPerformed(final GuiButton button) throws IOException {
            super.actionPerformed(button);
            if (button.id == 1) {
                Minecraft.getMinecraft().displayGuiScreen(this.backgroundScreen);
            }
        }
        
        public GuiScreen getBackgroundScreen() {
            return this.backgroundScreen;
        }
    }
    
    public interface DefaultColorCallback
    {
        Color getDefaultColor();
    }
}
