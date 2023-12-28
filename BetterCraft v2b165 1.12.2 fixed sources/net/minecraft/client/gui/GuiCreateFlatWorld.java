// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.gui;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.world.gen.FlatLayerInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import java.io.IOException;
import net.minecraft.client.resources.I18n;
import net.minecraft.world.gen.FlatGeneratorInfo;

public class GuiCreateFlatWorld extends GuiScreen
{
    private final GuiCreateWorld createWorldGui;
    private FlatGeneratorInfo theFlatGeneratorInfo;
    private String flatWorldTitle;
    private String materialText;
    private String heightText;
    private Details createFlatWorldListSlotGui;
    private GuiButton addLayerButton;
    private GuiButton editLayerButton;
    private GuiButton removeLayerButton;
    
    public GuiCreateFlatWorld(final GuiCreateWorld createWorldGuiIn, final String preset) {
        this.theFlatGeneratorInfo = FlatGeneratorInfo.getDefaultFlatGenerator();
        this.createWorldGui = createWorldGuiIn;
        this.setPreset(preset);
    }
    
    public String getPreset() {
        return this.theFlatGeneratorInfo.toString();
    }
    
    public void setPreset(final String preset) {
        this.theFlatGeneratorInfo = FlatGeneratorInfo.createFlatGeneratorFromString(preset);
    }
    
    @Override
    public void initGui() {
        this.buttonList.clear();
        this.flatWorldTitle = I18n.format("createWorld.customize.flat.title", new Object[0]);
        this.materialText = I18n.format("createWorld.customize.flat.tile", new Object[0]);
        this.heightText = I18n.format("createWorld.customize.flat.height", new Object[0]);
        this.createFlatWorldListSlotGui = new Details();
        this.addLayerButton = this.addButton(new GuiButton(2, GuiCreateFlatWorld.width / 2 - 154, GuiCreateFlatWorld.height - 52, 100, 20, String.valueOf(I18n.format("createWorld.customize.flat.addLayer", new Object[0])) + " (NYI)"));
        this.editLayerButton = this.addButton(new GuiButton(3, GuiCreateFlatWorld.width / 2 - 50, GuiCreateFlatWorld.height - 52, 100, 20, String.valueOf(I18n.format("createWorld.customize.flat.editLayer", new Object[0])) + " (NYI)"));
        this.removeLayerButton = this.addButton(new GuiButton(4, GuiCreateFlatWorld.width / 2 - 155, GuiCreateFlatWorld.height - 52, 150, 20, I18n.format("createWorld.customize.flat.removeLayer", new Object[0])));
        this.buttonList.add(new GuiButton(0, GuiCreateFlatWorld.width / 2 - 155, GuiCreateFlatWorld.height - 28, 150, 20, I18n.format("gui.done", new Object[0])));
        this.buttonList.add(new GuiButton(5, GuiCreateFlatWorld.width / 2 + 5, GuiCreateFlatWorld.height - 52, 150, 20, I18n.format("createWorld.customize.presets", new Object[0])));
        this.buttonList.add(new GuiButton(1, GuiCreateFlatWorld.width / 2 + 5, GuiCreateFlatWorld.height - 28, 150, 20, I18n.format("gui.cancel", new Object[0])));
        this.addLayerButton.visible = false;
        this.editLayerButton.visible = false;
        this.theFlatGeneratorInfo.updateLayers();
        this.onLayersChanged();
    }
    
    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.createFlatWorldListSlotGui.handleMouseInput();
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        final int i = this.theFlatGeneratorInfo.getFlatLayers().size() - this.createFlatWorldListSlotGui.selectedLayer - 1;
        if (button.id == 1) {
            this.mc.displayGuiScreen(this.createWorldGui);
        }
        else if (button.id == 0) {
            this.createWorldGui.chunkProviderSettingsJson = this.getPreset();
            this.mc.displayGuiScreen(this.createWorldGui);
        }
        else if (button.id == 5) {
            this.mc.displayGuiScreen(new GuiFlatPresets(this));
        }
        else if (button.id == 4 && this.hasSelectedLayer()) {
            this.theFlatGeneratorInfo.getFlatLayers().remove(i);
            this.createFlatWorldListSlotGui.selectedLayer = Math.min(this.createFlatWorldListSlotGui.selectedLayer, this.theFlatGeneratorInfo.getFlatLayers().size() - 1);
        }
        this.theFlatGeneratorInfo.updateLayers();
        this.onLayersChanged();
    }
    
    public void onLayersChanged() {
        final boolean flag = this.hasSelectedLayer();
        this.removeLayerButton.enabled = flag;
        this.editLayerButton.enabled = flag;
        this.editLayerButton.enabled = false;
        this.addLayerButton.enabled = false;
    }
    
    private boolean hasSelectedLayer() {
        return this.createFlatWorldListSlotGui.selectedLayer > -1 && this.createFlatWorldListSlotGui.selectedLayer < this.theFlatGeneratorInfo.getFlatLayers().size();
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        this.createFlatWorldListSlotGui.drawScreen(mouseX, mouseY, partialTicks);
        Gui.drawCenteredString(this.fontRendererObj, this.flatWorldTitle, GuiCreateFlatWorld.width / 2, 8, 16777215);
        final int i = GuiCreateFlatWorld.width / 2 - 92 - 16;
        Gui.drawString(this.fontRendererObj, this.materialText, i, 32, 16777215);
        Gui.drawString(this.fontRendererObj, this.heightText, i + 2 + 213 - this.fontRendererObj.getStringWidth(this.heightText), 32, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    class Details extends GuiSlot
    {
        public int selectedLayer;
        
        public Details() {
            super(GuiCreateFlatWorld.this.mc, GuiCreateFlatWorld.width, GuiCreateFlatWorld.height, 43, GuiCreateFlatWorld.height - 60, 24);
            this.selectedLayer = -1;
        }
        
        private void drawItem(final int x, final int z, final ItemStack itemToDraw) {
            this.drawItemBackground(x + 1, z + 1);
            GlStateManager.enableRescaleNormal();
            if (!itemToDraw.func_190926_b()) {
                RenderHelper.enableGUIStandardItemLighting();
                GuiCreateFlatWorld.this.itemRender.renderItemIntoGUI(itemToDraw, x + 2, z + 2);
                RenderHelper.disableStandardItemLighting();
            }
            GlStateManager.disableRescaleNormal();
        }
        
        private void drawItemBackground(final int x, final int y) {
            this.drawItemBackground(x, y, 0, 0);
        }
        
        private void drawItemBackground(final int x, final int z, final int textureX, final int textureY) {
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            this.mc.getTextureManager().bindTexture(Gui.STAT_ICONS);
            final float f = 0.0078125f;
            final float f2 = 0.0078125f;
            final int i = 18;
            final int j = 18;
            final Tessellator tessellator = Tessellator.getInstance();
            final BufferBuilder bufferbuilder = tessellator.getBuffer();
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
            bufferbuilder.pos(x + 0, z + 18, GuiCreateFlatWorld.zLevel).tex((textureX + 0) * 0.0078125f, (textureY + 18) * 0.0078125f).endVertex();
            bufferbuilder.pos(x + 18, z + 18, GuiCreateFlatWorld.zLevel).tex((textureX + 18) * 0.0078125f, (textureY + 18) * 0.0078125f).endVertex();
            bufferbuilder.pos(x + 18, z + 0, GuiCreateFlatWorld.zLevel).tex((textureX + 18) * 0.0078125f, (textureY + 0) * 0.0078125f).endVertex();
            bufferbuilder.pos(x + 0, z + 0, GuiCreateFlatWorld.zLevel).tex((textureX + 0) * 0.0078125f, (textureY + 0) * 0.0078125f).endVertex();
            tessellator.draw();
        }
        
        @Override
        protected int getSize() {
            return GuiCreateFlatWorld.this.theFlatGeneratorInfo.getFlatLayers().size();
        }
        
        @Override
        protected void elementClicked(final int slotIndex, final boolean isDoubleClick, final int mouseX, final int mouseY) {
            this.selectedLayer = slotIndex;
            GuiCreateFlatWorld.this.onLayersChanged();
        }
        
        @Override
        protected boolean isSelected(final int slotIndex) {
            return slotIndex == this.selectedLayer;
        }
        
        @Override
        protected void drawBackground() {
        }
        
        @Override
        protected void func_192637_a(final int p_192637_1_, final int p_192637_2_, final int p_192637_3_, final int p_192637_4_, final int p_192637_5_, final int p_192637_6_, final float p_192637_7_) {
            final FlatLayerInfo flatlayerinfo = GuiCreateFlatWorld.this.theFlatGeneratorInfo.getFlatLayers().get(GuiCreateFlatWorld.this.theFlatGeneratorInfo.getFlatLayers().size() - p_192637_1_ - 1);
            final IBlockState iblockstate = flatlayerinfo.getLayerMaterial();
            final Block block = iblockstate.getBlock();
            Item item = Item.getItemFromBlock(block);
            if (item == Items.field_190931_a) {
                if (block != Blocks.WATER && block != Blocks.FLOWING_WATER) {
                    if (block == Blocks.LAVA || block == Blocks.FLOWING_LAVA) {
                        item = Items.LAVA_BUCKET;
                    }
                }
                else {
                    item = Items.WATER_BUCKET;
                }
            }
            final ItemStack itemstack = new ItemStack(item, 1, item.getHasSubtypes() ? block.getMetaFromState(iblockstate) : 0);
            final String s = item.getItemStackDisplayName(itemstack);
            this.drawItem(p_192637_2_, p_192637_3_, itemstack);
            GuiCreateFlatWorld.this.fontRendererObj.drawString(s, p_192637_2_ + 18 + 5, p_192637_3_ + 3, 16777215);
            String s2;
            if (p_192637_1_ == 0) {
                s2 = I18n.format("createWorld.customize.flat.layer.top", flatlayerinfo.getLayerCount());
            }
            else if (p_192637_1_ == GuiCreateFlatWorld.this.theFlatGeneratorInfo.getFlatLayers().size() - 1) {
                s2 = I18n.format("createWorld.customize.flat.layer.bottom", flatlayerinfo.getLayerCount());
            }
            else {
                s2 = I18n.format("createWorld.customize.flat.layer", flatlayerinfo.getLayerCount());
            }
            GuiCreateFlatWorld.this.fontRendererObj.drawString(s2, p_192637_2_ + 2 + 213 - GuiCreateFlatWorld.this.fontRendererObj.getStringWidth(s2), p_192637_3_ + 3, 16777215);
        }
        
        @Override
        protected int getScrollBarX() {
            return this.width - 70;
        }
    }
}
