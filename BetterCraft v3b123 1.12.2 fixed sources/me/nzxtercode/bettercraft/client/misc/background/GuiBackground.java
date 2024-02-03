// 
// Decompiled by Procyon v0.6.0
// 

package me.nzxtercode.bettercraft.client.misc.background;

import net.minecraft.client.gui.Gui;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiSlot;
import com.google.gson.JsonObject;
import org.lwjgl.opengl.GL11;
import java.io.IOException;
import java.awt.Color;
import java.util.function.Consumer;
import java.awt.Component;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JFileChooser;
import me.nzxtercode.bettercraft.client.Config;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.input.Keyboard;
import net.minecraft.client.gui.GuiScreen;

public class GuiBackground extends GuiScreen
{
    private GuiScreen parent;
    public SlotList list;
    
    public GuiBackground(final GuiScreen parent) {
        this.parent = parent;
    }
    
    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, GuiBackground.width - 107, GuiBackground.height - 27, 100, 20, "Back"));
        this.buttonList.add(new GuiButton(1, 7, GuiBackground.height - 27, 100, 20, String.format("%s", ShaderBackgroundLoader.getLoader().isEnabledShader() ? (String.valueOf(EnumChatFormatting.GREEN.toString()) + "Shader") : (String.valueOf(EnumChatFormatting.RED.toString()) + "Shader"))));
        this.buttonList.add(new GuiButton(2, 5, 5, 75, 20, "Change BG"));
        this.buttonList.add(new GuiButton(3, GuiBackground.width - 80, 5, 75, 20, "Custom BG"));
        this.buttonList.add(new GuiButton(4, 110, GuiBackground.height - 27, 100, 20, String.format("%s", ShaderBackgroundLoader.getLoader().isPEnabledBackground() ? (String.valueOf(EnumChatFormatting.GREEN.toString()) + "Pictures") : (String.valueOf(EnumChatFormatting.RED.toString()) + "Pictures"))));
        this.buttonList.add(new GuiButton(5, GuiBackground.width / 2 - 38, 5, 75, 20, "Colorpicker"));
        this.list = new SlotList(this.mc, GuiBackground.width, GuiBackground.height, 32, GuiBackground.height - 32, 10);
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        this.list.actionPerformed(button);
        switch (button.id) {
            case 0: {
                this.mc.displayGuiScreen(this.parent);
                break;
            }
            case 1: {
                ShaderBackgroundLoader.getLoader().setEnabledShader(!ShaderBackgroundLoader.getLoader().isEnabledShader());
                button.displayString = String.format("%s", ShaderBackgroundLoader.getLoader().isEnabledShader() ? (String.valueOf(EnumChatFormatting.GREEN.toString()) + "Shader") : (String.valueOf(EnumChatFormatting.RED.toString()) + "Shader"));
                break;
            }
            case 2: {
                ShaderBackgroundLoader.getLoader().setCEnabledBackground(false);
                Config.getInstance().editBackground("Background", json -> {
                    final int index = Config.getInstance().getBackground("Background").get("id").getAsInt();
                    json.addProperty("id", (index + 1 >= ShaderBackgroundLoader.getBackgrounds().size()) ? 0 : (index + 1));
                    return;
                });
                break;
            }
            case 3: {
                final JFileChooser fileChooser = new JFileChooser();
                fileChooser.setAcceptAllFileFilterUsed(false);
                fileChooser.setFileFilter(new FileNameExtensionFilter("Images", new String[] { "png", "jpg" }));
                fileChooser.showOpenDialog(null);
                ShaderBackgroundLoader.getLoader().setCEnabledBackground(!ShaderBackgroundLoader.getLoader().isCEnabledBackground());
                Config.getInstance().editBackground("Background", json -> json.addProperty("custom", fileChooser2.getSelectedFile().getPath()));
                ShaderBackgroundLoader.getLoader().getImageCache().clear();
                break;
            }
            case 4: {
                ShaderBackgroundLoader.getLoader().setPEnabledBackground(!ShaderBackgroundLoader.getLoader().isPEnabledBackground());
                button.displayString = String.format("%s", ShaderBackgroundLoader.getLoader().isPEnabledBackground() ? (String.valueOf(EnumChatFormatting.GREEN.toString()) + "Pictures") : (String.valueOf(EnumChatFormatting.RED.toString()) + "Pictures"));
                break;
            }
            case 5: {
                this.mc.displayGuiScreen(new GuiColorPicker(this, null));
                break;
            }
        }
    }
    
    @Override
    public void handleMouseInput() throws IOException {
        this.list.handleMouseInput();
        super.handleMouseInput();
    }
    
    @Override
    public void drawScreen(final int p_drawScreen_1_, final int p_drawScreen_2_, final float p_drawScreen_3_) {
        this.list.drawScreen(p_drawScreen_1_, p_drawScreen_2_, p_drawScreen_3_);
        GL11.glPushMatrix();
        GL11.glScalef(2.0f, 2.0f, 2.0f);
        GL11.glPopMatrix();
        super.drawScreen(p_drawScreen_1_, p_drawScreen_2_, p_drawScreen_3_);
    }
    
    class SlotList extends GuiSlot
    {
        public SlotList(final Minecraft p_i1052_1_, final int p_i1052_2_, final int p_i1052_3_, final int p_i1052_4_, final int p_i1052_5_, final int p_i1052_6_) {
            super(p_i1052_1_, p_i1052_2_, p_i1052_3_, p_i1052_4_, p_i1052_5_, p_i1052_6_);
        }
        
        @Override
        protected int getSize() {
            return ShaderBackgroundLoader.getLoader().getShaderList().size();
        }
        
        @Override
        protected void elementClicked(final int i, final boolean b, final int i1, final int i2) {
            if (ShaderBackgroundLoader.getLoader().isEnabledShader()) {
                ShaderBackgroundLoader.getLoader().setCurrentShaderId(i);
            }
        }
        
        @Override
        protected boolean isSelected(final int i) {
            return false;
        }
        
        @Override
        protected void drawBackground() {
            GuiBackground.this.drawDefaultBackground();
        }
        
        @Override
        protected void drawSlot(final int i, final int i1, final int i2, final int i3, final int i4, final int i5) {
            Gui.drawCenteredString(this.mc.fontRendererObj, String.valueOf(ShaderBackgroundLoader.getLoader().isEnabledShader() ? ((ShaderBackgroundLoader.getLoader().getCurrentShaderId() == i) ? EnumChatFormatting.GREEN.toString() : EnumChatFormatting.RESET.toString()) : EnumChatFormatting.DARK_GRAY.toString()) + (String)ShaderBackgroundLoader.getLoader().getShaderList().get(i).getKey(), this.width / 2, i2 + 2, -1);
        }
    }
}
