// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.utils.manager;

import net.labymod.utils.ModColor;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.io.DataInputStream;
import net.minecraft.client.renderer.texture.DynamicTexture;
import javax.imageio.ImageIO;
import net.labymod.main.ModTextures;
import net.minecraft.client.resources.DefaultResourcePack;
import net.labymod.core.asm.LabyModTransformer;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.gui.FontRenderer;
import net.labymod.utils.DrawUtils;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.Minecraft;

public class CustomLoadingScreen
{
    private static CustomLoadingScreen instance;
    private Minecraft mc;
    private ResourceLocation resourceMojangLogo;
    private ResourceLocation resourceLabymodBanner;
    private TextureManager textureManager;
    private DrawUtils drawUtils;
    private int renderedFrames;
    private String[] steps;
    
    public static void renderInstance() {
        if (CustomLoadingScreen.instance == null) {
            CustomLoadingScreen.instance = new CustomLoadingScreen(Minecraft.getMinecraft(), Minecraft.getMinecraft().getTextureManager());
        }
        CustomLoadingScreen.instance.drawSplashScreen();
    }
    
    public CustomLoadingScreen(final Minecraft mc, final TextureManager textureManager) {
        this.renderedFrames = 0;
        this.mc = mc;
        this.textureManager = textureManager;
        this.drawUtils = new DrawUtils();
        final FontRenderer fontRenderer = new FontRenderer(mc.gameSettings, new ResourceLocation("textures/font/ascii.png"), textureManager, false);
        fontRenderer.setUnicodeFlag(mc.isUnicode());
        fontRenderer.setBidiFlag(mc.getLanguageManager().isCurrentLanguageBidirectional());
        ((IReloadableResourceManager)mc.getResourceManager()).registerReloadListener(fontRenderer);
        this.drawUtils.setFontRenderer(fontRenderer);
        this.loadResource();
    }
    
    private void loadResource() {
        try {
            final Class<?> clazz = this.mc.getClass();
            final Field field = clazz.getDeclaredField(LabyModTransformer.getMappingImplementation().getMCDefaultResourcePack());
            field.setAccessible(true);
            final DefaultResourcePack mcDefaultResourcePack = (DefaultResourcePack)field.get(this.mc);
            final InputStream inputstreamMojang = mcDefaultResourcePack.getInputStream(ModTextures.TITLE_MOJANG_BANNER);
            this.resourceMojangLogo = this.textureManager.getDynamicTextureLocation("logo", new DynamicTexture(ImageIO.read(inputstreamMojang)));
            final InputStream inputstreamLabyModBanner = mcDefaultResourcePack.getInputStream(ModTextures.TITLE_LABYMOD_BANNER);
            this.resourceLabymodBanner = this.textureManager.getDynamicTextureLocation("logo_lm_banner", new DynamicTexture(ImageIO.read(inputstreamLabyModBanner)));
            final InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("assets/minecraft/labymod/data/steps.titles");
            final DataInputStream dis = new DataInputStream(inputStream);
            this.steps = new String[dis.readInt()];
            for (int i = 0; i < this.steps.length; ++i) {
                final byte[] bytes = new byte[dis.readInt()];
                dis.read(bytes);
                this.steps[i] = new String(bytes);
            }
        }
        catch (final Exception error) {
            error.printStackTrace();
        }
    }
    
    public void drawSplashScreen() {
        ++this.renderedFrames;
        if (this.steps == null) {
            return;
        }
        final int width = this.drawUtils.getWidth();
        final int height = this.drawUtils.getHeight();
        GlStateManager.matrixMode(5889);
        GlStateManager.loadIdentity();
        GlStateManager.ortho(0.0, width, height, 0.0, 1000.0, 3000.0);
        GlStateManager.matrixMode(5888);
        GlStateManager.loadIdentity();
        GlStateManager.translate(0.0f, 0.0f, -2000.0f);
        GlStateManager.disableLighting();
        GlStateManager.disableFog();
        GlStateManager.disableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        this.drawUtils.drawRectangle(0, 0, width, height, -1);
        this.textureManager.bindTexture(this.resourceMojangLogo);
        Gui.drawModalRectWithCustomSizedTexture((width - 256) / 2, (height - 256) / 2, 0.0f, 0.0f, 256, 256, 256.0f, 256.0f);
        final double percent = 100.0 / this.steps.length * this.renderedFrames;
        this.drawProgressbar(width / 2.0 - 100.0, height / 2 + 30, 200.0, 12.0, percent);
        this.textureManager.bindTexture(this.resourceLabymodBanner);
        final int bannerLength = 140;
        Gui.drawModalRectWithCustomSizedTexture(width / 2 - 70, height / 2 + 45 + 10, 0.0f, 0.0f, 140, 30, 140.0f, 30.0f);
        final String text = (this.renderedFrames < this.steps.length) ? this.steps[this.renderedFrames] : "";
        this.drawUtils.getFontRenderer().drawString(text, (width - this.drawUtils.getStringWidth(text)) / 2, height / 2 + 45, ModColor.toRGB(0, 0, 0, 255));
        try {
            this.mc.updateDisplay();
        }
        catch (final NullPointerException error) {
            error.printStackTrace();
        }
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();
    }
    
    private void drawProgressbar(final double x, final double y, final double width, final double height, double percent) {
        if (percent > 100.0) {
            percent = 100.0;
        }
        this.drawRect(x - 1.0, y - 1.0, x + width + 1.0, y + height + 1.0, ModColor.toRGB(0, 0, 0, 255));
        this.drawRect(x, y, x + width, y + height, ModColor.toRGB(200, 200, 200, 255));
        this.drawRect(x, y, (int)(x + width / 100.0 * percent), y + height, ModColor.toRGB(0, 143, 232, 255));
        this.drawUtils.drawCenteredString(String.valueOf(ModColor.cl("f")) + (int)percent + "%", x + width / 2.0, y + height / 2.0 - 4.0);
    }
    
    private void drawRect(final double left, final double top, final double right, final double bottom, final int color) {
        Gui.drawRect((int)left, (int)top, (int)right, (int)bottom, color);
    }
}
