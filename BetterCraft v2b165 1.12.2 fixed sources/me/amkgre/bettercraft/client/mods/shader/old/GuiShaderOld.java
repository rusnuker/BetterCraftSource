// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.shader.old;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.gui.Gui;
import org.lwjgl.opengl.GL11;
import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import java.util.ArrayList;
import net.minecraft.client.gui.GuiScreen;

public class GuiShaderOld extends GuiScreen
{
    public GuiList list;
    public static boolean shader;
    private int selectedShader;
    private GuiScreen before;
    public static ArrayList<ShaderGlsSandoxShaderOld> shaders;
    public static ShaderGlsSandoxShaderOld current;
    
    static {
        GuiShaderOld.shader = false;
        GuiShaderOld.shaders = new ArrayList<ShaderGlsSandoxShaderOld>() {
            {
                this.add(new ShaderGlsSandoxShaderOld("City", ShaderList1.City));
                this.add(new ShaderGlsSandoxShaderOld("Acora", ShaderList1.Acora));
                this.add(new ShaderGlsSandoxShaderOld("BlueBalls", ShaderList1.BlueBalls));
                this.add(new ShaderGlsSandoxShaderOld("Ball", ShaderList1.Ball));
                this.add(new ShaderGlsSandoxShaderOld("RainbowSpiral", ShaderList1.RainbowSpiral));
                this.add(new ShaderGlsSandoxShaderOld("Galaxy", ShaderList1.Galaxy));
                this.add(new ShaderGlsSandoxShaderOld("Green", ShaderList1.Green));
                this.add(new ShaderGlsSandoxShaderOld("Loading", ShaderList1.Loading));
                this.add(new ShaderGlsSandoxShaderOld("LoadingSnow", ShaderList1.LoadingSnow));
                this.add(new ShaderGlsSandoxShaderOld("LoadingSnowNo", ShaderList1.LoadingSnowNo));
                this.add(new ShaderGlsSandoxShaderOld("LoadingSnowCircle", ShaderList1.LoadingSnowCircle));
                this.add(new ShaderGlsSandoxShaderOld("RainBridge", ShaderList1.RainBridge));
                this.add(new ShaderGlsSandoxShaderOld("Mario", ShaderList1.Mario));
                this.add(new ShaderGlsSandoxShaderOld("Minecraft", ShaderList1.Minecraft));
                this.add(new ShaderGlsSandoxShaderOld("RedShape", ShaderList1.RedShape));
                this.add(new ShaderGlsSandoxShaderOld("Nordlicht", ShaderList1.Nordlicht));
                this.add(new ShaderGlsSandoxShaderOld("HellWhite", ShaderList1.HellWhite));
                this.add(new ShaderGlsSandoxShaderOld("RainbowColor", ShaderList1.RainbowColor));
                this.add(new ShaderGlsSandoxShaderOld("RedNight", ShaderList1.RedNight));
                this.add(new ShaderGlsSandoxShaderOld("Rings", ShaderList1.Rings));
                this.add(new ShaderGlsSandoxShaderOld("RainbowLine", ShaderList1.RainbowLine));
                this.add(new ShaderGlsSandoxShaderOld("Hexagon", ShaderList1.Hexagon));
                this.add(new ShaderGlsSandoxShaderOld("Thunder", ShaderList1.Thunder));
                this.add(new ShaderGlsSandoxShaderOld("Universes", ShaderList1.Universes));
                this.add(new ShaderGlsSandoxShaderOld("Wassersaeule", ShaderList1.Wassersaeule));
                this.add(new ShaderGlsSandoxShaderOld("Balls", ShaderList1.Balls));
                this.add(new ShaderGlsSandoxShaderOld("Boxes", ShaderList1.Boxes));
                this.add(new ShaderGlsSandoxShaderOld("Crosshair", ShaderList1.Crosshair));
                this.add(new ShaderGlsSandoxShaderOld("EaZy", ShaderList1.EaZy));
                this.add(new ShaderGlsSandoxShaderOld("Deep", ShaderList1.Deep));
                this.add(new ShaderGlsSandoxShaderOld("Face", ShaderList1.Face));
                this.add(new ShaderGlsSandoxShaderOld("Flames", ShaderList1.Flames));
                this.add(new ShaderGlsSandoxShaderOld("LSD", ShaderList1.LSD));
                this.add(new ShaderGlsSandoxShaderOld("Mauer", ShaderList1.Mauer));
                this.add(new ShaderGlsSandoxShaderOld("Nordlich", ShaderList1.Nordlich));
                this.add(new ShaderGlsSandoxShaderOld("Rainy", ShaderList1.Rainy));
                this.add(new ShaderGlsSandoxShaderOld("Sandbox", ShaderList1.Sandbox));
                this.add(new ShaderGlsSandoxShaderOld("Schall", ShaderList1.Schall));
                this.add(new ShaderGlsSandoxShaderOld("Smiley", ShaderList1.Smiley));
                this.add(new ShaderGlsSandoxShaderOld("StaticGod", ShaderList1.staticGod));
                this.add(new ShaderGlsSandoxShaderOld("ThunderClouds", ShaderList1.ThunderClouds));
                this.add(new ShaderGlsSandoxShaderOld("Corona", ShaderList2.Corona));
                this.add(new ShaderGlsSandoxShaderOld("Lichttunnel", ShaderList2.Lichttunnel));
                this.add(new ShaderGlsSandoxShaderOld("Mars", ShaderList2.Mars));
                this.add(new ShaderGlsSandoxShaderOld("Insel", ShaderList2.Insel));
                this.add(new ShaderGlsSandoxShaderOld("Berge", ShaderList2.Berge));
                this.add(new ShaderGlsSandoxShaderOld("Wasserwirbel", ShaderList2.Wasserwirbel));
                this.add(new ShaderGlsSandoxShaderOld("Whitekugel", ShaderList2.Whitekugel));
                this.add(new ShaderGlsSandoxShaderOld("Planet", ShaderList2.Planet));
                this.add(new ShaderGlsSandoxShaderOld("Radar", ShaderList2.Radar));
                this.add(new ShaderGlsSandoxShaderOld("Wuerfellego", ShaderList2.Wuerfellego));
                this.add(new ShaderGlsSandoxShaderOld("Spin", ShaderList2.Spin));
                this.add(new ShaderGlsSandoxShaderOld("Glasstown", ShaderList2.Glasstown));
                this.add(new ShaderGlsSandoxShaderOld("Blue", ShaderList2.Blue));
                this.add(new ShaderGlsSandoxShaderOld("Wolken", ShaderList2.Wolken));
                this.add(new ShaderGlsSandoxShaderOld("Floor", ShaderList2.Floor));
                this.add(new ShaderGlsSandoxShaderOld("Crack", ShaderList2.Crack));
                this.add(new ShaderGlsSandoxShaderOld("Line", ShaderList2.Line));
                this.add(new ShaderGlsSandoxShaderOld("Street", ShaderList2.Street));
                this.add(new ShaderGlsSandoxShaderOld("Sonne", ShaderList2.Sonne));
                this.add(new ShaderGlsSandoxShaderOld("Darkblue", ShaderList2.Darkblue));
                this.add(new ShaderGlsSandoxShaderOld("Greentown", ShaderList2.Greentown));
                this.add(new ShaderGlsSandoxShaderOld("Whitewuerfel", ShaderList2.Whitewuerfel));
                this.add(new ShaderGlsSandoxShaderOld("GalaxyGurtel", ShaderList2.GalaxyGurtel));
                this.add(new ShaderGlsSandoxShaderOld("Zauberwuerfel", ShaderList2.Zauberwuerfel));
                this.add(new ShaderGlsSandoxShaderOld("Blackandwhite", ShaderList2.Blackandwhite));
                this.add(new ShaderGlsSandoxShaderOld("Roetem", ShaderList2.Roetem));
                this.add(new ShaderGlsSandoxShaderOld("Sheep", ShaderList2.Sheep));
                this.add(new ShaderGlsSandoxShaderOld("Rakete", ShaderList2.Rakete));
                this.add(new ShaderGlsSandoxShaderOld("Leuchtring", ShaderList2.Leuchtring));
                this.add(new ShaderGlsSandoxShaderOld("Ecasty", ShaderList2.Ecasty));
                this.add(new ShaderGlsSandoxShaderOld("Pyramide", ShaderList2.Pyramide));
                this.add(new ShaderGlsSandoxShaderOld("RainBowDash", ShaderList2.RainbowDash));
                this.add(new ShaderGlsSandoxShaderOld("OtherRainbow", ShaderList2.OtherRainbow));
                this.add(new ShaderGlsSandoxShaderOld("Rainbow", ShaderList2.Rainbow));
                this.add(new ShaderGlsSandoxShaderOld("Auroras", ShaderList2.Auroras));
                this.add(new ShaderGlsSandoxShaderOld("Nastrovje", ShaderList2.Nastrovje));
                this.add(new ShaderGlsSandoxShaderOld("PreLifeRecode", ShaderList2.PreLifeRecode));
            }
        };
        GuiShaderOld.current = GuiShaderOld.shaders.get(0);
    }
    
    public GuiShaderOld(final GuiScreen before) {
        this.selectedShader = -1;
        this.before = before;
    }
    
    @Override
    public void initGui() {
        final boolean value = GuiShaderOld.shader;
        this.buttonList.add(new GuiButton(0, 6, GuiShaderOld.height - 26, GuiShaderOld.width / (GuiShaderOld.width / 2) + GuiShaderOld.width / 6, 20, "Shader: " + (value ? "§aOn" : "§4Off")));
        this.buttonList.add(new GuiButton(1, GuiShaderOld.width - (GuiShaderOld.width / (GuiShaderOld.width / 2) + GuiShaderOld.width / 6) - 6, GuiShaderOld.height - 26, GuiShaderOld.width / (GuiShaderOld.width / 2) + GuiShaderOld.width / 6, 20, "Back"));
        (this.list = new GuiList(this.mc, GuiShaderOld.width, GuiShaderOld.height, 32, GuiShaderOld.height - 32, 10)).registerScrollButtons(4, 5);
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.id == 0) {
            GuiShaderOld.shader = !GuiShaderOld.shader;
            this.mc.displayGuiScreen(this);
        }
        if (button.id == 1) {
            this.mc.displayGuiScreen(this.before);
        }
    }
    
    @Override
    public void handleMouseInput() throws IOException {
        this.list.handleMouseInput();
        super.handleMouseInput();
    }
    
    @Override
    public void drawScreen(final int p_drawScreen_1_, final int p_drawScreen_2_, final float p_drawScreen_3_) {
        this.drawDefaultBackground();
        this.list.drawScreen(p_drawScreen_1_, p_drawScreen_2_, p_drawScreen_3_);
        GL11.glPushMatrix();
        GL11.glScalef(2.0f, 2.0f, 2.0f);
        Gui.drawCenteredString(this.fontRendererObj, "§dShader", GuiShaderOld.width / 4, 6, 16777215);
        GL11.glPopMatrix();
        super.drawScreen(p_drawScreen_1_, p_drawScreen_2_, p_drawScreen_3_);
    }
    
    static /* synthetic */ void access$0(final GuiShaderOld guiShaderOld, final int selectedShader) {
        guiShaderOld.selectedShader = selectedShader;
    }
    
    class GuiList extends GuiSlot
    {
        public GuiList(final Minecraft mcIn, final int width, final int height, final int topIn, final int bottomIn, final int slotHeightIn) {
            super(mcIn, width, height, topIn, bottomIn, 12);
        }
        
        @Override
        protected int getSize() {
            return GuiShaderOld.shaders.size();
        }
        
        @Override
        protected void elementClicked(final int slotIndex, final boolean isDoubleClick, final int mouseX, final int mouseY) {
            GuiShaderOld.access$0(GuiShaderOld.this, slotIndex);
            GuiShaderOld.current = GuiShaderOld.shaders.get(GuiShaderOld.this.selectedShader);
        }
        
        @Override
        protected boolean isSelected(final int slotIndex) {
            return slotIndex == GuiShaderOld.this.selectedShader;
        }
        
        @Override
        protected int getContentHeight() {
            return this.getSize() * 12;
        }
        
        @Override
        protected void drawBackground() {
        }
        
        @Override
        public void scrollBy(final int amount) {
            super.scrollBy(amount);
        }
        
        @Override
        protected void func_192637_a(final int p_192637_1_, final int p_192637_2_, final int p_192637_3_, final int p_192637_4_, final int p_192637_5_, final int p_192637_6_, final float p_192637_7_) {
            final ShaderGlsSandoxShaderOld shader = GuiShaderOld.shaders.get(p_192637_1_);
            Gui.drawCenteredString(this.mc.fontRendererObj, new StringBuilder().append(shader.getName()).toString(), this.width / 2, p_192637_3_, -1);
        }
    }
}
