// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.addons.resourcepacks24.gui.elements;

import net.labymod.addons.resourcepacks24.loader.PackRepositoryLoader;
import net.labymod.utils.DrawUtils;
import net.minecraft.client.Minecraft;
import net.labymod.main.LabyMod;
import net.labymod.addons.resourcepacks24.gui.GuiResourcepacks24;
import net.labymod.addons.resourcepacks24.loader.model.PackMeta;
import net.minecraft.util.ResourceLocation;
import net.labymod.addons.resourcepacks24.gui.elements.basement.PackElement;

public class LocalPackElement extends PackElement
{
    private static final ResourceLocation RESOURCE_PACKS_TEXTURE;
    private PackMeta packMeta;
    private boolean hoverChoose;
    private int hoverSwap;
    
    static {
        RESOURCE_PACKS_TEXTURE = new ResourceLocation("textures/gui/resource_packs.png");
    }
    
    public LocalPackElement(final PackMeta packMeta) {
        super(true);
        this.packMeta = packMeta;
    }
    
    @Override
    public String getDisplayName() {
        return this.packMeta.displayName;
    }
    
    @Override
    public ResourceLocation getIcon() {
        return this.packMeta.icon;
    }
    
    @Override
    public String getDescription() {
        return this.packMeta.pack.description;
    }
    
    @Override
    public void drawControls(final double x, final double y, final double width, final double height, final boolean isFirstEntry, final boolean isLastEntry, final boolean isSelected, final int mouseX, final int mouseY, final GuiResourcepacks24 gui) {
        final DrawUtils draw = LabyMod.getInstance().getDrawUtils();
        if (gui.getSharedView().draggingElement == null) {
            Minecraft.getMinecraft().renderEngine.bindTexture(LocalPackElement.RESOURCE_PACKS_TEXTURE);
            this.hoverChoose = (mouseX < x + height / (isSelected ? 2 : 1));
            draw.drawTexture(x, y, isSelected ? 32.0 : 0.0, this.hoverChoose ? 32.0 : 0.0, 32.0, 32.0, height, height);
            if (isSelected) {
                final boolean hoverRightSide = mouseX > x + height / 2.0 && mouseX < x + height;
                this.hoverSwap = (hoverRightSide ? ((mouseY < y + height / 2.0) ? (isFirstEntry ? 0 : 1) : (isLastEntry ? 0 : -1)) : 0);
                if (!isFirstEntry) {
                    draw.drawTexture(x, y, 96.0, (this.hoverSwap == 1) ? 32.0 : 0.0, 32.0, 32.0, height, height);
                }
                if (!isLastEntry) {
                    draw.drawTexture(x, y, 64.0, (this.hoverSwap == -1) ? 32.0 : 0.0, 32.0, 32.0, height, height);
                }
            }
        }
        super.drawControls(x, y, width, height, isFirstEntry, isLastEntry, isSelected, mouseX, mouseY, gui);
    }
    
    @Override
    public void mouseReleased(final int mouseX, final int mouseY, final int mouseButton, final GuiResourcepacks24 gui) {
        super.mouseReleased(mouseX, mouseY, mouseButton, gui);
        final PackRepositoryLoader packLoader = gui.getResourcepacks24().getPackLoader();
        if (this.hoverChoose) {
            if (packLoader.isSelected(this)) {
                packLoader.unselectPack(this);
            }
            else {
                packLoader.selectPack(this);
            }
        }
        else if (this.hoverSwap != 0) {
            packLoader.swap(this, this.hoverSwap);
        }
        gui.initGui();
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof LocalPackElement) {
            return ((LocalPackElement)obj).getPackMeta().equals(this.packMeta);
        }
        return super.equals(obj);
    }
    
    @Override
    public int hashCode() {
        return this.packMeta.hashCode();
    }
    
    public PackMeta getPackMeta() {
        return this.packMeta;
    }
    
    public boolean isHoverChoose() {
        return this.hoverChoose;
    }
    
    public int getHoverSwap() {
        return this.hoverSwap;
    }
}
