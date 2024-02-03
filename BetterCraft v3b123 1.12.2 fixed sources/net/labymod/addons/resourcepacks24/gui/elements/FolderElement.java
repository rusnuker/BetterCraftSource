// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.addons.resourcepacks24.gui.elements;

import net.labymod.addons.resourcepacks24.gui.GuiResourcepacks24;
import net.minecraft.client.Minecraft;
import net.labymod.utils.DrawUtils;
import net.labymod.utils.ModColor;
import net.labymod.main.LabyMod;
import net.labymod.gui.elements.GuiImageButton;
import java.io.File;
import net.minecraft.util.ResourceLocation;
import net.labymod.addons.resourcepacks24.gui.elements.basement.DeletableElement;

public class FolderElement extends DeletableElement
{
    public static final ResourceLocation TEXTURE_FOLDER;
    private File directory;
    private boolean expanded;
    private GuiImageButton buttonFolder;
    private GuiImageButton buttonArrow;
    
    static {
        TEXTURE_FOLDER = new ResourceLocation("resourcepacks24/textures/folder.png");
    }
    
    public FolderElement(final File directory) {
        super(true);
        this.directory = directory;
        this.buttonFolder = new GuiImageButton(FolderElement.TEXTURE_FOLDER, 0, 0, 85, 127);
        this.buttonArrow = new GuiImageButton(FolderElement.TEXTURE_FOLDER, 85, 0, 85, 127, 85, 127) {
            @Override
            public boolean isMouseOver(final int mouseX, final int mouseY) {
                return FolderElement.this.expanded;
            }
        };
    }
    
    @Override
    protected String getElementName() {
        return this.directory.getName();
    }
    
    @Override
    public boolean draw(final double x, final double y, final double width, final double height, final int mouseX, final int mouseY) {
        final boolean mouseOver = super.draw(x, y, width, height, mouseX, mouseY);
        final DrawUtils draw = LabyMod.getInstance().getDrawUtils();
        final int padding = 3;
        if (mouseOver) {
            DrawUtils.drawRect(x, y - 1.0, x + width, y + height + 1.0, ModColor.toRGB(100, 100, 100, 35));
        }
        Minecraft.getMinecraft().renderEngine.bindTexture(FolderElement.TEXTURE_FOLDER);
        this.buttonArrow.draw(x + 3.0, y, height, mouseX, mouseY);
        this.buttonFolder.draw(x + 6.0 + height, y, height, mouseX, mouseY);
        draw.drawString(this.directory.getName(), x + height + 9.0 + height, y + 1.0);
        return mouseOver;
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton, final GuiResourcepacks24 gui) {
        super.mouseClicked(mouseX, mouseY, mouseButton, gui);
        if (!this.buttonDelete.isMouseOver(mouseX, mouseY)) {
            this.expanded = !this.expanded;
        }
    }
    
    @Override
    public boolean equals(final Object obj) {
        return ((FolderElement)obj).getDirectory().equals(this.directory);
    }
    
    public boolean contains(final LocalPackElement meta) {
        return meta.getPackMeta().file.getParentFile().equals(this.directory);
    }
    
    public File getDirectory() {
        return this.directory;
    }
    
    public boolean isExpanded() {
        return this.expanded;
    }
    
    public GuiImageButton getButtonFolder() {
        return this.buttonFolder;
    }
    
    public GuiImageButton getButtonArrow() {
        return this.buttonArrow;
    }
    
    public void setExpanded(final boolean expanded) {
        this.expanded = expanded;
    }
}
