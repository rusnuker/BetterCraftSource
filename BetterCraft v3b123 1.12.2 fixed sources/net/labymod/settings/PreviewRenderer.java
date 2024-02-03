// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.settings;

import net.minecraft.client.gui.GuiMainMenu;
import java.io.IOException;
import net.labymod.utils.DrawUtils;
import net.labymod.utils.ModColor;
import net.labymod.settings.elements.BooleanElement;
import java.lang.reflect.Field;
import net.labymod.utils.ReflectionHelper;
import net.minecraft.client.settings.GameSettings;
import java.util.Iterator;
import java.util.Map;
import net.labymod.main.lang.LanguageManager;
import net.minecraft.client.gui.GuiScreen;
import java.util.List;
import net.minecraft.client.gui.GuiButton;
import java.util.ArrayList;
import net.labymod.gui.elements.Tabs;
import net.minecraft.client.resources.I18n;
import net.labymod.utils.manager.TooltipHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.labymod.core.WorldRendererAdapter;
import net.labymod.main.ModTextures;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.labymod.core.LabyModCore;
import net.minecraft.client.renderer.Tessellator;
import net.labymod.main.LabyMod;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;
import net.labymod.utils.Material;
import net.labymod.main.Source;
import net.minecraft.client.shader.Framebuffer;
import net.labymod.gui.elements.CustomGuiButton;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.gui.Gui;

public class PreviewRenderer extends Gui
{
    private static final boolean MC18;
    private static final ResourceLocation widgetsTexPath;
    private static final ItemStack previewItemStackSword;
    private static final ItemStack previewItemStackFish;
    private static final CustomGuiButton dummyButton;
    private static PreviewRenderer instance;
    private final Framebuffer framebuffer;
    private Class<?> bindedClass;
    private boolean created;
    private boolean fastRenderConflict;
    
    static {
        MC18 = Source.ABOUT_MC_VERSION.startsWith("1.8");
        widgetsTexPath = new ResourceLocation("textures/gui/widgets.png");
        previewItemStackSword = Material.IRON_SWORD.createItemStack();
        previewItemStackFish = Material.RAW_FISH.createItemStack(1, 3);
        dummyButton = new CustomGuiButton(0, 0, 0, null);
    }
    
    public PreviewRenderer() {
        this.framebuffer = new Framebuffer(Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight, true);
        this.created = false;
        PreviewRenderer.instance = this;
    }
    
    public static PreviewRenderer getInstance() {
        if (PreviewRenderer.instance == null) {
            PreviewRenderer.instance = new PreviewRenderer();
        }
        return PreviewRenderer.instance;
    }
    
    public void init(final Class<?> theClass) {
        this.bindedClass = theClass;
        if (!this.fastRenderConflict) {
            this.handleOFFastRender();
        }
        final int width = Minecraft.getMinecraft().displayWidth;
        final int height = Minecraft.getMinecraft().displayHeight;
        if (this.framebuffer.framebufferWidth != width || (this.framebuffer.framebufferHeight != height && !this.fastRenderConflict)) {
            this.framebuffer.createFramebuffer(Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
            this.created = false;
        }
    }
    
    public void createFrame() {
        if (this.framebuffer == null || Minecraft.getMinecraft().currentScreen == null || !Minecraft.getMinecraft().currentScreen.getClass().equals(this.bindedClass)) {
            return;
        }
        if (!this.fastRenderConflict) {
            this.framebuffer.bindFramebuffer(true);
        }
    }
    
    private void setFirstFrameInBuffer() {
        if (this.created) {
            return;
        }
        this.created = true;
        GlStateManager.pushMatrix();
        this.framebuffer.bindFramebuffer(true);
        this.renderImage(true, 0, 0, LabyMod.getInstance().getDrawUtils().getScaledResolution().getScaledWidth(), LabyMod.getInstance().getDrawUtils().getScaledResolution().getScaledHeight());
        Minecraft.getMinecraft().getFramebuffer().bindFramebuffer(true);
        GlStateManager.popMatrix();
    }
    
    public void kill() {
        if (!this.fastRenderConflict && this.framebuffer != null) {
            this.framebuffer.unbindFramebuffer();
        }
    }
    
    private void renderImage(final boolean forceDefaultImage, final int left, final int top, final int right, final int bottom) {
        final int minWidth = Math.min(left, right);
        final int minHeight = Math.min(top, bottom);
        final int maxWidth = Math.max(left, right);
        final int maxHeight = Math.max(top, bottom);
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRendererAdapter vertexBuffer = LabyModCore.getWorldRenderer();
        GlStateManager.disableTexture2D();
        GlStateManager.disableBlend();
        final float[] color = { 0.0f, 0.0f, 0.0f, 0.5f };
        vertexBuffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        vertexBuffer.pos(minWidth - 1, maxHeight + 1, -90.0).color(color[0], color[1], color[2], color[3]).endVertex();
        vertexBuffer.pos(maxWidth + 1, maxHeight + 1, -90.0).color(color[0], color[1], color[2], color[3]).endVertex();
        vertexBuffer.pos(maxWidth + 1, minHeight - 1, -90.0).color(color[0], color[1], color[2], color[3]).endVertex();
        vertexBuffer.pos(minWidth - 1, minHeight - 1, -90.0).color(color[0], color[1], color[2], color[3]).endVertex();
        tessellator.draw();
        if (LabyMod.getInstance().isInGame() && !forceDefaultImage) {
            this.framebuffer.bindFramebufferTexture();
            GlStateManager.enableTexture2D();
            vertexBuffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            vertexBuffer.pos(minWidth, maxHeight, -90.0).tex(0.0, 0.0).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
            vertexBuffer.pos(maxWidth, maxHeight, -90.0).tex(1.0, 0.0).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
            vertexBuffer.pos(maxWidth, minHeight, -90.0).tex(1.0, 1.0).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
            vertexBuffer.pos(minWidth, minHeight, -90.0).tex(0.0, 1.0).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
            tessellator.draw();
        }
        else {
            Minecraft.getMinecraft().getTextureManager().bindTexture(ModTextures.SETTINGS_MODULE_EDITOR_BG);
            GlStateManager.enableTexture2D();
            LabyMod.getInstance().getDrawUtils().drawTexture(left, top, 0.0, 0.0, 255.0, 255.0, right - left, bottom - top);
        }
    }
    
    private void renderHotbar(final int left, final int top, final int right, final int bottom, final float partialTicks, final int mouseX, final int mouseY) {
        EntityPlayer entityplayer = (EntityPlayer)Minecraft.getMinecraft().getRenderViewEntity();
        if (!(entityplayer instanceof EntityPlayer)) {
            entityplayer = null;
        }
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        Minecraft.getMinecraft().getTextureManager().bindTexture(PreviewRenderer.widgetsTexPath);
        final int i = (right - left) / 2;
        final float f = this.zLevel;
        this.zLevel = -90.0f;
        this.drawTexturedModalRect(left + i - 91, bottom - 22, 0, 0, 182, 22);
        this.drawTexturedModalRect(left + i - 91 - 1 + ((entityplayer == null) ? 0 : entityplayer.inventory.currentItem) * 20, bottom - 22 - 1, 0, 22, 24, 22);
        this.zLevel = f;
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        RenderHelper.enableGUIStandardItemLighting();
        for (int j = 0; j < 9; ++j) {
            final int k = (right - left) / 2 - 90 + j * 20 + 2;
            final int l = bottom - 16 - 3;
            this.renderHotbarItem(j, left + k, l, partialTicks, entityplayer, mouseX, mouseY);
        }
        GlStateManager.disableDepth();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableBlend();
    }
    
    private void renderHotbarItem(final int index, final int xPos, final int yPos, final float partialTicks, final EntityPlayer entity, final int mouseX, final int mouseY) {
        ItemStack itemstack = null;
        if (entity == null) {
            itemstack = ((index == 0) ? PreviewRenderer.previewItemStackSword : ((index == 8) ? PreviewRenderer.previewItemStackFish : null));
        }
        else {
            itemstack = LabyModCore.getMinecraft().getItem(entity.inventory, index);
        }
        if (itemstack != null) {
            final float f = LabyModCore.getMinecraft().getAnimationsToGo(itemstack) - partialTicks;
            if (f > 0.0f) {
                GlStateManager.pushMatrix();
                final float f2 = 1.0f + f / 5.0f;
                GlStateManager.translate((float)(xPos + 8), (float)(yPos + 12), 0.0f);
                GlStateManager.scale(1.0f / f2, (f2 + 1.0f) / 2.0f, 1.0f);
                GlStateManager.translate((float)(-(xPos + 8)), (float)(-(yPos + 12)), 0.0f);
            }
            GlStateManager.enableDepth();
            Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(itemstack, xPos, yPos);
            GlStateManager.disableDepth();
            if (f > 0.0f) {
                GlStateManager.popMatrix();
            }
            Minecraft.getMinecraft().getRenderItem().renderItemOverlays(LabyModCore.getMinecraft().getFontRenderer(), itemstack, xPos, yPos);
            if (index == 8 && entity == null && mouseX > xPos && mouseX < xPos + 16 && mouseY > yPos && mouseY < yPos + 16) {
                TooltipHelper.getHelper().pointTooltip(mouseX, mouseY, 0L, "Puffi");
            }
        }
    }
    
    private void renderPlayerStatsNew(final int left, final int top, final int right, final int bottom) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(LabyModCore.getRenderImplementation().getIcons());
        GlStateManager.enableAlpha();
        float healthUpdateCounter = 0.0f;
        int lastPlayerHealth = 0;
        float lastSystemTime = 0.0f;
        float playerHealth = 0.0f;
        final float updateCounter = 0.0f;
        final int i = 20;
        final boolean flag = healthUpdateCounter > 0.0f && (healthUpdateCounter - 0.0f) / 3.0f % 2.0f == 1.0f;
        lastSystemTime = (float)Minecraft.getSystemTime();
        healthUpdateCounter = 10.0f;
        if (Minecraft.getSystemTime() - lastSystemTime > 1000.0f) {
            playerHealth = 20.0f;
            lastPlayerHealth = 20;
            lastSystemTime = (float)Minecraft.getSystemTime();
        }
        playerHealth = 20.0f;
        final int j = lastPlayerHealth;
        final boolean flag2 = false;
        final int k = 20;
        final int l = 20;
        final float saturation = 0.0f;
        final int i2 = left + (right - left) / 2 - 91;
        final int j2 = left + (right - left) / 2 + 91;
        final int k2 = top + bottom - top - 39;
        final float f = 20.0f;
        final float f2 = 0.0f;
        final int l2 = LabyModCore.getMath().ceiling_float_int(1.0f);
        final int i3 = Math.max(10 - (l2 - 2), 3);
        final int j3 = k2 - (l2 - 1) * i3 - 10;
        float f3 = 0.0f;
        final int k3 = 20;
        final int l3 = -1;
        for (int i4 = 0; i4 < 10; ++i4) {
            final int j4 = i2 + i4 * 8;
            if (i4 * 2 + 1 < 20) {
                this.drawTexturedModalRect(j4, j3, 34, 9, 9, 9);
            }
            if (i4 * 2 + 1 == 20) {
                this.drawTexturedModalRect(j4, j3, 25, 9, 9, 9);
            }
            if (i4 * 2 + 1 > 20) {
                this.drawTexturedModalRect(j4, j3, 16, 9, 9, 9);
            }
        }
        for (int i5 = LabyModCore.getMath().ceiling_float_int(10.0f) - 1; i5 >= 0; --i5) {
            final int j5 = 16;
            int k4 = 0;
            if (flag) {
                k4 = 1;
            }
            final int l4 = LabyModCore.getMath().ceiling_float_int((i5 + 1) / 10.0f) - 1;
            final int i6 = i2 + i5 % 10 * 8;
            int j6 = k2 - l4 * i3;
            if (i5 == -1) {
                j6 -= 2;
            }
            final int k5 = 0;
            this.drawTexturedModalRect(i6, j6, 16 + k4 * 9, 0, 9, 9);
            if (flag) {
                if (i5 * 2 + 1 < j) {
                    this.drawTexturedModalRect(i6, j6, 70, 0, 9, 9);
                }
                if (i5 * 2 + 1 == j) {
                    this.drawTexturedModalRect(i6, j6, 79, 0, 9, 9);
                }
            }
            if (f3 > 0.0f) {
                this.drawTexturedModalRect(i6, j6, 160, 0, 9, 9);
                f3 -= 2.0f;
            }
            else {
                if (i5 * 2 + 1 < 20) {
                    this.drawTexturedModalRect(i6, j6, 52, 0, 9, 9);
                }
                if (i5 * 2 + 1 == 20) {
                    this.drawTexturedModalRect(i6, j6, 61, 0, 9, 9);
                }
            }
        }
        for (int k6 = 0; k6 < 10; ++k6) {
            final int i7 = k2;
            final int l5 = 16;
            final int j7 = 0;
            final int i8 = j2 - k6 * 8 - 9;
            this.drawTexturedModalRect(i8, i7, 16 + j7 * 9, 27, 9, 9);
            if (k6 * 2 + 1 < 20) {
                this.drawTexturedModalRect(i8, i7, 52, 27, 9, 9);
            }
            if (k6 * 2 + 1 == 20) {
                this.drawTexturedModalRect(i8, i7, 61, 27, 9, 9);
            }
        }
        final int l6 = 300;
        for (int k7 = LabyModCore.getMath().ceiling_double_int(9.933333333333334), i9 = LabyModCore.getMath().ceiling_double_int(10.0) - k7, l7 = 0; l7 < k7 + i9; ++l7) {
            if (l7 < k7) {
                this.drawTexturedModalRect(j2 - l7 * 8 - 9, j3, 16, 18, 9, 9);
            }
            else {
                this.drawTexturedModalRect(j2 - l7 * 8 - 9, j3, 25, 18, 9, 9);
            }
        }
    }
    
    public void renderExpBar(final int left, final int top, final int right, final int bottom) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(LabyModCore.getRenderImplementation().getIcons());
        final int i = 1;
        final float experience = 0.7f;
        final int experienceLevel = 42;
        final int j = 182;
        final int k = 128;
        final int l = bottom - 32 + 3;
        this.drawTexturedModalRect(left + (right - left) / 2 - 91, l, 0, 64, 182, 5);
        this.drawTexturedModalRect(left + (right - left) / 2 - 91, l, 0, 69, 128, 5);
        final int k2 = 8453920;
        final String s = "42";
        final int l2 = left + (right - left - LabyModCore.getMinecraft().getFontRenderer().getStringWidth("42")) / 2;
        final int i2 = bottom - 31 - 4;
        LabyModCore.getMinecraft().getFontRenderer().drawString("42", l2 + 1, i2, 0);
        LabyModCore.getMinecraft().getFontRenderer().drawString("42", l2 - 1, i2, 0);
        LabyModCore.getMinecraft().getFontRenderer().drawString("42", l2, i2 + 1, 0);
        LabyModCore.getMinecraft().getFontRenderer().drawString("42", l2, i2 - 1, 0);
        LabyModCore.getMinecraft().getFontRenderer().drawString("42", l2, i2, 8453920);
    }
    
    public void renderEscapeScreen(final int left, final int top, final int right, final int bottom, final int mouseX, final int mouseY) {
        final int width = right - left;
        final int height = bottom - top;
        final int i = -16;
        final Minecraft mc = Minecraft.getMinecraft();
        final String disconnectString = I18n.format(mc.isIntegratedServerRunning() ? "menu.disconnect" : "menu.returnToMenu", new Object[0]);
        final boolean shareToLanEnabled = mc.isSingleplayer() && !mc.getIntegratedServer().getPublic();
        this.renderDummyButton(disconnectString, left + width / 2 - 100, top + height / 4 + 120 - 16, 200, true, mouseX, mouseY);
        this.renderDummyButton(I18n.format("menu.returnToGame", new Object[0]), left + width / 2 - 100, top + height / 4 + 24 - 16, 200, true, mouseX, mouseY);
        this.renderDummyButton(I18n.format("menu.options", new Object[0]), left + width / 2 - 100, top + height / 4 + 96 - 16, 98, true, mouseX, mouseY);
        this.renderDummyButton(I18n.format("menu.shareToLan", new Object[0]), left + width / 2 + 2, top + height / 4 + 96 - 16, 98, shareToLanEnabled, mouseX, mouseY);
        this.renderDummyButton(PreviewRenderer.MC18 ? I18n.format("gui.achievements", new Object[0]) : "Achievements", left + width / 2 - 100, top + height / 4 + 48 - 16, 98, true, mouseX, mouseY);
        this.renderDummyButton(I18n.format("gui.stats", new Object[0]), left + width / 2 + 2, top + height / 4 + 48 - 16, 98, true, mouseX, mouseY);
        Gui.drawCenteredString(LabyModCore.getMinecraft().getFontRenderer(), I18n.format("menu.game", new Object[0]), left + width / 2, 40 + top, 16777215);
        final Map<String, Class<? extends GuiScreen>[]> tabs = Tabs.getGuiMap();
        if (tabs.isEmpty()) {
            Tabs.initGuiScreen(new ArrayList<GuiButton>(), null);
        }
        int tabX = 0;
        for (String buttonName : tabs.keySet()) {
            buttonName = LanguageManager.translate(buttonName);
            final int nameWidth = LabyMod.getInstance().getDrawUtils().getStringWidth(buttonName);
            this.renderDummyButton(buttonName, left + tabX + 5, top + 5, 10 + nameWidth, true, mouseX, mouseY);
            tabX += nameWidth + 12;
        }
    }
    
    public void renderDummyButton(final String displayText, final int x, final int y, final int width, final boolean enabled, final int mouseX, final int mouseY) {
        PreviewRenderer.dummyButton.displayString = displayText;
        PreviewRenderer.dummyButton.setPosition(x, y, x + width, y + 20);
        PreviewRenderer.dummyButton.setEnabled(enabled);
        LabyModCore.getMinecraft().drawButton(PreviewRenderer.dummyButton, mouseX, mouseY);
    }
    
    private void handleOFFastRender() {
        try {
            final Field fieldOfFastRender = ReflectionHelper.findField(GameSettings.class, "ofFastRender");
            try {
                this.fastRenderConflict = fieldOfFastRender.getBoolean(Minecraft.getMinecraft().gameSettings);
                if (this.fastRenderConflict) {
                    Minecraft.getMinecraft().displayGuiScreen(new FastRenderConflict());
                }
            }
            catch (final Exception e) {
                e.printStackTrace();
            }
        }
        catch (final Exception ex) {}
    }
    
    public static class FastRenderConflict extends GuiScreen
    {
        private final GuiScreen lastScreen;
        private BooleanElement ofFastRenderElement;
        private GuiButton buttonRestartGame;
        
        public FastRenderConflict() {
            this.lastScreen = Minecraft.getMinecraft().currentScreen;
        }
        
        @Override
        public void initGui() {
            this.buttonList.clear();
            final int divY = FastRenderConflict.height / 2 - 40 + 50;
            this.buttonList.add(new GuiButton(0, FastRenderConflict.width / 2 - 100, divY + 30, 90, 20, LanguageManager.translate("button_cancel")));
            this.buttonList.add(this.buttonRestartGame = new GuiButton(1, FastRenderConflict.width / 2 + 10, divY + 30, 90, 20, LanguageManager.translate("button_restart")));
            final String buttonString = String.valueOf(LanguageManager.translate("button_continue_anyway")) + " >>";
            this.buttonList.add(new GuiButton(2, FastRenderConflict.width - LabyMod.getInstance().getDrawUtils().getStringWidth(buttonString) - 10, FastRenderConflict.height - 25, 120, 20, buttonString));
            super.initGui();
        }
        
        @Override
        public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
            this.drawBackground(0);
            final DrawUtils draw = LabyMod.getInstance().getDrawUtils();
            if (this.ofFastRenderElement == null) {
                draw.drawCenteredString(String.valueOf(ModColor.cl("c")) + LanguageManager.translate("optifine_fast_render_manual_title"), FastRenderConflict.width / 2, FastRenderConflict.height / 2 - 50);
                draw.drawCenteredString(LanguageManager.translate("optifine_fast_render_manual_explanation"), FastRenderConflict.width / 2, FastRenderConflict.height / 2 - 50 + 10);
                draw.drawCenteredString(LanguageManager.translate("optifine_fast_render_manual_path"), FastRenderConflict.width / 2, FastRenderConflict.height / 2 - 50 + 30);
            }
            else {
                final int divY = FastRenderConflict.height / 2 - 40;
                draw.drawCenteredString(String.valueOf(ModColor.cl("c")) + LanguageManager.translate("optifine_fast_render_auto_tile"), FastRenderConflict.width / 2, divY);
                draw.drawCenteredString(LanguageManager.translate("optifine_fast_render_auto_explanation"), FastRenderConflict.width / 2, divY + 10);
                draw.drawCenteredString(LanguageManager.translate("optifine_fast_render_auto_request"), FastRenderConflict.width / 2, divY + 30);
                final int centerPosX = FastRenderConflict.width / 2;
                final int centerPosY = divY + 55;
                final int elementWidth = 200;
                final int elementHeight = 22;
                this.ofFastRenderElement.draw(centerPosX - 100, centerPosY - 11, centerPosX + 100, centerPosY + 11, mouseX, mouseY);
            }
            super.drawScreen(mouseX, mouseY, partialTicks);
        }
        
        public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
            if (this.ofFastRenderElement != null) {
                this.ofFastRenderElement.mouseClicked(mouseX, mouseY, mouseButton);
            }
            super.mouseClicked(mouseX, mouseY, mouseButton);
        }
        
        @Override
        protected void actionPerformed(final GuiButton button) throws IOException {
            super.actionPerformed(button);
            switch (button.id) {
                case 0: {
                    if (this.lastScreen instanceof LabyModModuleEditorGui) {
                        final LabyModModuleEditorGui lastScreen = (LabyModModuleEditorGui)this.lastScreen;
                        Minecraft.getMinecraft().displayGuiScreen(lastScreen.getLastScreen());
                        break;
                    }
                    Minecraft.getMinecraft().displayGuiScreen(LabyMod.getInstance().isInGame() ? null : new GuiMainMenu());
                    break;
                }
                case 1: {
                    Minecraft.getMinecraft().shutdown();
                    break;
                }
                case 2: {
                    Minecraft.getMinecraft().displayGuiScreen(this.lastScreen);
                    break;
                }
            }
        }
    }
}
