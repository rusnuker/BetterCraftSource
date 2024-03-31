// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.shader.browser;

import me.amkgre.bettercraft.client.mods.shader.ShaderRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.gui.Gui;
import java.util.List;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.net.URL;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.texture.DynamicTexture;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import net.minecraft.client.gui.GuiScreen;

public class GuiShaderBrowser extends ShaderSlotRenderUtils<ShaderGlsSandoxShaderBrowser>
{
    private GuiScreen parentScreen;
    private boolean isStartet;
    private static int pageNumber;
    public static boolean shader;
    private ShaderGlsSandboxAPI shaderAPI;
    private HashMap<ShaderGlsSandoxShaderBrowser, BufferedImage> shaderPictures;
    private HashMap<BufferedImage, DynamicTexture> savedTexture;
    
    static {
        GuiShaderBrowser.pageNumber = 0;
        GuiShaderBrowser.shader = false;
    }
    
    public GuiShaderBrowser(final GuiScreen parentScreen) {
        this.isStartet = false;
        this.shaderAPI = new ShaderGlsSandboxAPI();
        this.shaderPictures = new HashMap<ShaderGlsSandoxShaderBrowser, BufferedImage>();
        this.savedTexture = new HashMap<BufferedImage, DynamicTexture>();
        this.parentScreen = parentScreen;
    }
    
    @Override
    public void initGui() {
        this.initSlots();
        this.buttonList.add(new GuiButton(0, GuiShaderBrowser.width - (GuiShaderBrowser.width / (GuiShaderBrowser.width / 2) + GuiShaderBrowser.width / 6) - 6, GuiShaderBrowser.height - 26, GuiShaderBrowser.width / (GuiShaderBrowser.width / 2) + GuiShaderBrowser.width / 6, 20, "Back"));
        final boolean value = GuiShaderBrowser.shader;
        this.buttonList.add(new GuiButton(4, GuiShaderBrowser.width - (GuiShaderBrowser.width / (GuiShaderBrowser.width / 2) + GuiShaderBrowser.width / 6) - 6, GuiShaderBrowser.height - 50, GuiShaderBrowser.width / (GuiShaderBrowser.width / 2) + GuiShaderBrowser.width / 6, 20, "Shader: " + (value ? "§aOn" : "§4Off")));
        this.buttonList.add(new GuiButton(1, GuiShaderBrowser.width / 2 - 102 - 20, GuiShaderBrowser.height - 26, 20, 20, "«"));
        this.buttonList.add(new GuiButton(2, GuiShaderBrowser.width / 2 + 100 + 2, GuiShaderBrowser.height - 26, 20, 20, "»"));
        this.buttonList.add(new GuiButton(3, 6, GuiShaderBrowser.height - 26, GuiShaderBrowser.width / (GuiShaderBrowser.width / 2) + GuiShaderBrowser.width / 6, 20, "Save"));
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000L);
                }
                catch (final InterruptedException ex) {}
                GuiShaderBrowser.this.getSlots().forEach(slotsObj -> {
                    try {
                        if (!GuiShaderBrowser.this.shaderPictures.containsKey(slotsObj.getObject())) {
                            GuiShaderBrowser.this.shaderPictures;
                            final ShaderGlsSandoxShaderBrowser shaderGlsSandoxShaderBrowser = slotsObj.getObject();
                            new URL("http://glslsandbox.com/" + slotsObj.getObject().getPictureID());
                            final URL url;
                            final Object o;
                            ((HashMap<ShaderGlsSandoxShaderBrowser, BufferedImage>)o).put(shaderGlsSandoxShaderBrowser, ImageIO.read(url.openStream()));
                        }
                    }
                    catch (final Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100L);
                    GuiShaderBrowser.access$3(GuiShaderBrowser.this, true);
                }
                catch (final Throwable t) {}
            }
        }).start();
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        switch (button.id) {
            case 0: {
                this.mc.displayGuiScreen(this.parentScreen);
                break;
            }
            case 1: {
                if (GuiShaderBrowser.pageNumber > 0) {
                    --GuiShaderBrowser.pageNumber;
                    this.refresh();
                    break;
                }
                break;
            }
            case 2: {
                ++GuiShaderBrowser.pageNumber;
                this.refresh();
                break;
            }
            case 3: {
                if (this.selectedSlot != null) {
                    this.shaderAPI.saveShader((ShaderGlsSandoxShaderBrowser)this.selectedSlot.getObject());
                    break;
                }
                break;
            }
            case 4: {
                GuiShaderBrowser.shader = !GuiShaderBrowser.shader;
                this.refresh();
                break;
            }
        }
    }
    
    private void refresh() {
        this.buttonList.clear();
        this.getSlots().clear();
        this.initGui();
        this.setInit(false);
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        if (this.isStartet) {
            this.renderSlot(GuiShaderBrowser.width / 2 - 100, 20, 200, 40, this.shaderAPI.getShadersByPage(GuiShaderBrowser.pageNumber), mouseX, mouseY);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    @Override
    public void renderedSlotObj(final int x, final int y, final int width, final int height, final ShaderGlsSandoxShaderBrowser listObj, final ShaderSlot slotObj) {
        Gui.drawRect(x + 64, y + 2, x + width - 2, y + 20, -1439814098);
        Gui.drawCenteredString(this.mc.fontRendererObj, "§7" + listObj.getShaderID().replace("/e#", ""), (x + 64 + (x + width - 2)) / 2, y + 7, -1);
        GlStateManager.color(1.0f, 1.0f, 1.0f);
        if (this.shaderPictures.containsKey(listObj)) {
            final BufferedImage image = this.shaderPictures.get(listObj);
            int textureID = 0;
            if (this.savedTexture.containsKey(image)) {
                textureID = this.savedTexture.get(image).getGlTextureId();
            }
            else {
                final DynamicTexture dynamicTex = new DynamicTexture(this.shaderPictures.get(listObj));
                textureID = dynamicTex.getGlTextureId();
                this.savedTexture.put(image, dynamicTex);
            }
            GlStateManager.bindTexture(textureID);
            Gui.drawModalRectWithCustomSizedTexture(x + 2, y + 2, 0.0f, 0.0f, height + 20, height - 4, (float)(height + 20), (float)(height - 4));
        }
        else {
            Gui.drawRect(x + 2, y + 2, x + height + 20 + 2, y + height - 2, Integer.MIN_VALUE);
            this.mc.fontRendererObj.drawString("§7§oLoading...", x + 10, y + height / 2 - 3, -1);
        }
    }
    
    @Override
    public void clickSlotElement(final ShaderGlsSandoxShaderBrowser listObj) {
        ShaderRenderer.reactiveShader(this.shaderAPI.getShaderByID(listObj.getShaderID()));
    }
    
    static /* synthetic */ void access$3(final GuiShaderBrowser guiShaderBrowser, final boolean isStartet) {
        guiShaderBrowser.isStartet = isStartet;
    }
}
