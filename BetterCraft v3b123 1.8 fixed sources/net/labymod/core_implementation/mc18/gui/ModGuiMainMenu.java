// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.core_implementation.mc18.gui;

import net.labymod.support.util.Debug;
import net.minecraft.client.gui.GuiConfirmOpenLink;
import net.labymod.utils.ModColor;
import net.labymod.main.Source;
import net.minecraft.client.gui.Gui;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.util.glu.Project;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import java.net.URI;
import net.minecraft.realms.RealmsBridge;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.world.demo.DemoWorldServer;
import net.labymod.gui.ModGuiMultiplayer;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.gui.GuiLanguage;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.world.storage.WorldInfo;
import net.minecraft.world.storage.ISaveFormat;
import net.labymod.main.LabyMod;
import net.labymod.core.LabyModCore;
import net.minecraft.client.gui.GuiButtonLanguage;
import net.labymod.splash.splashdates.SplashDate;
import net.labymod.splash.SplashLoader;
import net.labymod.utils.Consumer;
import java.util.Date;
import java.util.Calendar;
import java.util.List;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.renderer.OpenGlHelper;
import org.lwjgl.opengl.GLContext;
import java.io.IOException;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.apache.commons.io.Charsets;
import net.minecraft.client.Minecraft;
import com.google.common.collect.Lists;
import net.labymod.main.LabyModForge;
import net.minecraft.util.EnumChatFormatting;
import org.apache.logging.log4j.LogManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import java.util.Random;
import org.apache.logging.log4j.Logger;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.gui.GuiScreen;

public class ModGuiMainMenu extends GuiScreen implements GuiYesNoCallback
{
    public static final String field_96138_a;
    private static final Logger logger;
    private static final Random RANDOM;
    private static final ResourceLocation splashTexts;
    private static final ResourceLocation minecraftTitleTextures;
    private static final ResourceLocation[] titlePanoramaPaths;
    private final Object threadLock;
    private final float updateCounter;
    private final boolean vanillaForge;
    private String splashText;
    private GuiButton buttonResetDemo;
    private int panoramaTimer;
    private DynamicTexture viewportTexture;
    private String openGLWarning1;
    private String openGLWarning2;
    private String openGLWarningLink;
    private int field_92024_r;
    private int field_92023_s;
    private int field_92022_t;
    private int field_92021_u;
    private int field_92020_v;
    private int field_92019_w;
    private ResourceLocation backgroundTexture;
    private long startDebugClicking;
    
    static {
        logger = LogManager.getLogger();
        RANDOM = new Random();
        splashTexts = new ResourceLocation("texts/splashes.txt");
        minecraftTitleTextures = new ResourceLocation("textures/gui/title/minecraft.png");
        titlePanoramaPaths = new ResourceLocation[] { new ResourceLocation("textures/gui/title/background/panorama_0.png"), new ResourceLocation("textures/gui/title/background/panorama_1.png"), new ResourceLocation("textures/gui/title/background/panorama_2.png"), new ResourceLocation("textures/gui/title/background/panorama_3.png"), new ResourceLocation("textures/gui/title/background/panorama_4.png"), new ResourceLocation("textures/gui/title/background/panorama_5.png") };
        field_96138_a = "Please click " + EnumChatFormatting.UNDERLINE + "here" + EnumChatFormatting.RESET + " for more information.";
    }
    
    public ModGuiMainMenu() {
        this.threadLock = new Object();
        this.startDebugClicking = -1L;
        this.vanillaForge = !LabyModForge.isForge();
        this.openGLWarning2 = ModGuiMainMenu.field_96138_a;
        this.splashText = "missingno";
        BufferedReader bufferedreader = null;
        try {
            final List<String> list = (List<String>)Lists.newArrayList();
            bufferedreader = new BufferedReader(new InputStreamReader(Minecraft.getMinecraft().getResourceManager().getResource(ModGuiMainMenu.splashTexts).getInputStream(), Charsets.UTF_8));
            String s;
            while ((s = bufferedreader.readLine()) != null) {
                s = s.trim();
                if (!s.isEmpty()) {
                    list.add(s);
                }
            }
            if (!list.isEmpty()) {
                do {
                    this.splashText = list.get(ModGuiMainMenu.RANDOM.nextInt(list.size()));
                } while (this.splashText.hashCode() == 125780783);
            }
        }
        catch (final IOException ex) {}
        finally {
            if (bufferedreader != null) {
                try {
                    bufferedreader.close();
                }
                catch (final IOException ex2) {}
            }
        }
        if (bufferedreader != null) {
            try {
                bufferedreader.close();
            }
            catch (final IOException ex3) {}
        }
        this.updateCounter = ModGuiMainMenu.RANDOM.nextFloat();
        this.openGLWarning1 = "";
        if (!GLContext.getCapabilities().OpenGL20 && !OpenGlHelper.areShadersSupported()) {
            this.openGLWarning1 = I18n.format("title.oldgl1", new Object[0]);
            this.openGLWarning2 = I18n.format("title.oldgl2", new Object[0]);
            this.openGLWarningLink = "https://help.mojang.com/customer/portal/articles/325948?ref=game";
        }
    }
    
    @Override
    public void updateScreen() {
        ++this.panoramaTimer;
    }
    
    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
    }
    
    @Override
    public void initGui() {
        this.buttonList.clear();
        this.viewportTexture = new DynamicTexture(256, 256);
        this.backgroundTexture = this.mc.getTextureManager().getDynamicTextureLocation("background", this.viewportTexture);
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        if (calendar.get(2) + 1 == 12 && calendar.get(5) == 24) {
            this.splashText = "Merry X-mas!";
        }
        else if (calendar.get(2) + 1 == 1 && calendar.get(5) == 1) {
            this.splashText = "Happy new year!";
        }
        else if (calendar.get(2) + 1 == 10 && calendar.get(5) == 31) {
            this.splashText = "OOoooOOOoooo! Spooky!";
        }
        final Consumer<SplashLoader> consumer = new Consumer<SplashLoader>() {
            @Override
            public void accept(final SplashLoader accepted) {
                SplashDate[] splashDates;
                for (int length = (splashDates = SplashLoader.getLoader().getEntries().getSplashDates()).length, i = 0; i < length; ++i) {
                    final SplashDate date = splashDates[i];
                    if (calendar.get(2) + 1 == date.getMonth() && calendar.get(5) == date.getDay()) {
                        ModGuiMainMenu.access$0(ModGuiMainMenu.this, date.getDisplayString());
                    }
                }
            }
        };
        final SplashLoader loader = SplashLoader.getLoader();
        if (loader.getEntries() == null) {
            loader.setLoadListener(consumer);
        }
        else {
            consumer.accept(loader);
        }
        final int j = ModGuiMainMenu.height / 4 + 48;
        if (this.mc.isDemo()) {
            this.addDemoButtons(j, 24);
        }
        else {
            this.addSingleplayerMultiplayerButtons(j, 24);
        }
        this.buttonList.add(new GuiButton(0, ModGuiMainMenu.width / 2 - 100, j + 72 + 12, 98, 20, I18n.format("menu.options", new Object[0])));
        this.buttonList.add(new GuiButton(4, ModGuiMainMenu.width / 2 + 2, j + 72 + 12, 98, 20, I18n.format("menu.quit", new Object[0])));
        this.buttonList.add(new GuiButtonLanguage(5, ModGuiMainMenu.width / 2 - 124, j + 72 + 12));
        synchronized (this.threadLock) {
            this.field_92023_s = LabyModCore.getMinecraft().getFontRenderer().getStringWidth(this.openGLWarning1);
            this.field_92024_r = LabyModCore.getMinecraft().getFontRenderer().getStringWidth(this.openGLWarning2);
            final int k = Math.max(this.field_92023_s, this.field_92024_r);
            this.field_92022_t = (ModGuiMainMenu.width - k) / 2;
            this.field_92021_u = this.buttonList.get(0).yPosition - 24;
            this.field_92020_v = this.field_92022_t + k;
            this.field_92019_w = this.field_92021_u + 24;
            monitorexit(this.threadLock);
        }
        this.mc.setConnectedToRealms(false);
    }
    
    private void addSingleplayerMultiplayerButtons(final int p_73969_1_, final int p_73969_2_) {
        this.buttonList.add(new GuiButton(1, ModGuiMainMenu.width / 2 - 100, p_73969_1_, I18n.format("menu.singleplayer", new Object[0])));
        this.buttonList.add(new GuiButton(2, ModGuiMainMenu.width / 2 - 100, p_73969_1_ + p_73969_2_ * 1, I18n.format("menu.multiplayer", new Object[0])));
        String quickPlay = this.mc.gameSettings.lastServer;
        final String realms = "Minecraft Realms";
        final boolean quickPlayEnabled = LabyMod.getSettings().quickPlay && quickPlay != null && !quickPlay.isEmpty();
        if (quickPlayEnabled && quickPlay.length() > 16) {
            quickPlay = quickPlay.substring(0, 16);
        }
        if (quickPlayEnabled && !this.vanillaForge) {
            this.buttonList.add(new GuiButton(6, ModGuiMainMenu.width / 2 + 2, p_73969_1_ + p_73969_2_ * 2, 98, 20, quickPlay));
        }
        else if (!quickPlayEnabled && !this.vanillaForge) {
            this.buttonList.add(new GuiButton(14, ModGuiMainMenu.width / 2 + 2, p_73969_1_ + p_73969_2_ * 2, 98, 20, I18n.format("Minecraft Realms", new Object[0])));
        }
        else if (quickPlayEnabled) {
            this.buttonList.add(new GuiButton(6, ModGuiMainMenu.width / 2 + 2, p_73969_1_ + p_73969_2_ * 2, 98, 20, quickPlay));
            this.buttonList.add(new GuiButton(14, ModGuiMainMenu.width / 2 - 100, p_73969_1_ + p_73969_2_ * 2, 98, 20, I18n.format("Minecraft Realms", new Object[0])));
        }
        else {
            this.buttonList.add(new GuiButton(14, ModGuiMainMenu.width / 2 - 100, p_73969_1_ + p_73969_2_ * 2, I18n.format("Minecraft Realms", new Object[0])));
        }
        if (!this.vanillaForge) {
            this.buttonList.add(new GuiButton(125, ModGuiMainMenu.width / 2 - 100, p_73969_1_ + p_73969_2_ * 2, 98, 20, I18n.format("fml.menu.mods", new Object[0])));
        }
    }
    
    private void addDemoButtons(final int p_73972_1_, final int p_73972_2_) {
        this.buttonList.add(new GuiButton(11, ModGuiMainMenu.width / 2 - 100, p_73972_1_, I18n.format("menu.playdemo", new Object[0])));
        this.buttonList.add(this.buttonResetDemo = new GuiButton(12, ModGuiMainMenu.width / 2 - 100, p_73972_1_ + p_73972_2_ * 1, I18n.format("menu.resetdemo", new Object[0])));
        final ISaveFormat isaveformat = this.mc.getSaveLoader();
        final WorldInfo worldinfo = isaveformat.getWorldInfo("Demo_World");
        if (worldinfo == null) {
            this.buttonResetDemo.enabled = false;
        }
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.id == 0) {
            this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
        }
        if (button.id == 5) {
            this.mc.displayGuiScreen(new GuiLanguage(this, this.mc.gameSettings, this.mc.getLanguageManager()));
        }
        if (button.id == 1) {
            this.mc.displayGuiScreen(new GuiSelectWorld(this));
        }
        if (button.id == 2) {
            this.mc.displayGuiScreen(new ModGuiMultiplayer(this));
        }
        if (button.id == 14) {
            this.switchToRealms();
        }
        if (button.id == 4) {
            this.mc.shutdown();
        }
        if (button.id == 11) {
            this.mc.launchIntegratedServer("Demo_World", "Demo_World", DemoWorldServer.demoWorldSettings);
        }
        if (button.id == 12) {
            final ISaveFormat isaveformat = this.mc.getSaveLoader();
            final WorldInfo worldinfo = isaveformat.getWorldInfo("Demo_World");
            if (worldinfo != null) {
                final GuiYesNo guiyesno = GuiSelectWorld.makeDeleteWorldYesNo(this, worldinfo.getWorldName(), 12);
                this.mc.displayGuiScreen(guiyesno);
            }
        }
        if (button.id == 125 && !this.vanillaForge) {
            try {
                this.mc.displayGuiScreen((GuiScreen)Class.forName("net.minecraftforge.fml.client.GuiModList").getConstructor(GuiScreen.class).newInstance(this));
            }
            catch (final Exception e) {
                e.printStackTrace();
            }
        }
        if (button.id == 6) {
            LabyMod.getInstance().connectToServer(this.mc.gameSettings.lastServer);
        }
    }
    
    private void switchToRealms() {
        final RealmsBridge realmsbridge = new RealmsBridge();
        realmsbridge.switchToRealms(this);
    }
    
    @Override
    public void confirmClicked(final boolean result, final int id) {
        if (result && id == 12) {
            final ISaveFormat isaveformat = this.mc.getSaveLoader();
            isaveformat.flushCache();
            isaveformat.deleteWorldDirectory("Demo_World");
            this.mc.displayGuiScreen(this);
        }
        else if (id == 13) {
            if (result) {
                try {
                    final Class<?> oclass = Class.forName("java.awt.Desktop");
                    final Object object = oclass.getMethod("getDesktop", (Class<?>[])new Class[0]).invoke(null, new Object[0]);
                    oclass.getMethod("browse", URI.class).invoke(object, new URI(this.openGLWarningLink));
                }
                catch (final Throwable throwable) {
                    ModGuiMainMenu.logger.error("Couldn't open link", throwable);
                }
            }
            this.mc.displayGuiScreen(this);
        }
    }
    
    private void drawPanorama(final int p_73970_1_, final int p_73970_2_, final float p_73970_3_) {
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.matrixMode(5889);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        Project.gluPerspective(120.0f, 1.0f, 0.05f, 10.0f);
        GlStateManager.matrixMode(5888);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.rotate(180.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(90.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.disableCull();
        GlStateManager.depthMask(false);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        for (int i = 8, j = 0; j < i * i; ++j) {
            GlStateManager.pushMatrix();
            final float f = (j % i / (float)i - 0.5f) / 64.0f;
            final float f2 = (j / i / (float)i - 0.5f) / 64.0f;
            final float f3 = 0.0f;
            GlStateManager.translate(f, f2, 0.0f);
            GlStateManager.rotate(LabyModCore.getMath().sin((this.panoramaTimer + p_73970_3_) / 400.0f) * 25.0f + 20.0f, 1.0f, 0.0f, 0.0f);
            GlStateManager.rotate(-(this.panoramaTimer + p_73970_3_) * 0.1f, 0.0f, 1.0f, 0.0f);
            for (int k = 0; k < 6; ++k) {
                GlStateManager.pushMatrix();
                if (k == 1) {
                    GlStateManager.rotate(90.0f, 0.0f, 1.0f, 0.0f);
                }
                if (k == 2) {
                    GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f);
                }
                if (k == 3) {
                    GlStateManager.rotate(-90.0f, 0.0f, 1.0f, 0.0f);
                }
                if (k == 4) {
                    GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
                }
                if (k == 5) {
                    GlStateManager.rotate(-90.0f, 1.0f, 0.0f, 0.0f);
                }
                this.mc.getTextureManager().bindTexture(ModGuiMainMenu.titlePanoramaPaths[k]);
                worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                final int l = 255 / (j + 1);
                worldrenderer.pos(-1.0, -1.0, 1.0).tex(0.0, 0.0).color(255, 255, 255, l).endVertex();
                worldrenderer.pos(1.0, -1.0, 1.0).tex(1.0, 0.0).color(255, 255, 255, l).endVertex();
                worldrenderer.pos(1.0, 1.0, 1.0).tex(1.0, 1.0).color(255, 255, 255, l).endVertex();
                worldrenderer.pos(-1.0, 1.0, 1.0).tex(0.0, 1.0).color(255, 255, 255, l).endVertex();
                tessellator.draw();
                GlStateManager.popMatrix();
            }
            GlStateManager.popMatrix();
            GlStateManager.colorMask(true, true, true, false);
        }
        worldrenderer.setTranslation(0.0, 0.0, 0.0);
        GlStateManager.colorMask(true, true, true, true);
        GlStateManager.matrixMode(5889);
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(5888);
        GlStateManager.popMatrix();
        GlStateManager.depthMask(true);
        GlStateManager.enableCull();
        GlStateManager.enableDepth();
    }
    
    private void rotateAndBlurSkybox(final float p_73968_1_) {
        this.mc.getTextureManager().bindTexture(this.backgroundTexture);
        GL11.glTexParameteri(3553, 10241, 9729);
        GL11.glTexParameteri(3553, 10240, 9729);
        GL11.glCopyTexSubImage2D(3553, 0, 0, 0, 0, 0, 256, 256);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.colorMask(true, true, true, false);
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        GlStateManager.disableAlpha();
        for (int i = 3, j = 0; j < i; ++j) {
            final float f = 1.0f / (j + 1);
            final int k = ModGuiMainMenu.width;
            final int l = ModGuiMainMenu.height;
            final float f2 = (j - i / 2) / 256.0f;
            worldrenderer.pos(k, l, this.zLevel).tex(0.0f + f2, 1.0).color(1.0f, 1.0f, 1.0f, f).endVertex();
            worldrenderer.pos(k, 0.0, this.zLevel).tex(1.0f + f2, 1.0).color(1.0f, 1.0f, 1.0f, f).endVertex();
            worldrenderer.pos(0.0, 0.0, this.zLevel).tex(1.0f + f2, 0.0).color(1.0f, 1.0f, 1.0f, f).endVertex();
            worldrenderer.pos(0.0, l, this.zLevel).tex(0.0f + f2, 0.0).color(1.0f, 1.0f, 1.0f, f).endVertex();
        }
        tessellator.draw();
        GlStateManager.enableAlpha();
        GlStateManager.colorMask(true, true, true, true);
    }
    
    private void renderSkybox(final int p_73971_1_, final int p_73971_2_, final float p_73971_3_) {
        this.mc.getFramebuffer().unbindFramebuffer();
        GlStateManager.viewport(0, 0, 256, 256);
        this.drawPanorama(p_73971_1_, p_73971_2_, p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.rotateAndBlurSkybox(p_73971_3_);
        this.mc.getFramebuffer().bindFramebuffer(true);
        GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
        final float f = (ModGuiMainMenu.width > ModGuiMainMenu.height) ? (120.0f / ModGuiMainMenu.width) : (120.0f / ModGuiMainMenu.height);
        final float f2 = ModGuiMainMenu.height * f / 256.0f;
        final float f3 = ModGuiMainMenu.width * f / 256.0f;
        final int i = ModGuiMainMenu.width;
        final int j = ModGuiMainMenu.height;
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        worldrenderer.pos(0.0, j, this.zLevel).tex(0.5f - f2, 0.5f + f3).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
        worldrenderer.pos(i, j, this.zLevel).tex(0.5f - f2, 0.5f - f3).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
        worldrenderer.pos(i, 0.0, this.zLevel).tex(0.5f + f2, 0.5f - f3).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
        worldrenderer.pos(0.0, 0.0, this.zLevel).tex(0.5f + f2, 0.5f + f3).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
        tessellator.draw();
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        GlStateManager.disableAlpha();
        this.renderSkybox(mouseX, mouseY, partialTicks);
        GlStateManager.enableAlpha();
        final int i = 274;
        final int j = ModGuiMainMenu.width / 2 - 137;
        final int k = 30;
        this.drawGradientRect(0, 0, ModGuiMainMenu.width, ModGuiMainMenu.height, -2130706433, 16777215);
        this.drawGradientRect(0, 0, ModGuiMainMenu.width, ModGuiMainMenu.height, 0, Integer.MIN_VALUE);
        this.mc.getTextureManager().bindTexture(ModGuiMainMenu.minecraftTitleTextures);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        if (this.updateCounter < 1.0E-4) {
            this.drawTexturedModalRect(j + 0, 30, 0, 0, 99, 44);
            this.drawTexturedModalRect(j + 99, 30, 129, 0, 27, 44);
            this.drawTexturedModalRect(j + 99 + 26, 30, 126, 0, 3, 44);
            this.drawTexturedModalRect(j + 99 + 26 + 3, 30, 99, 0, 26, 44);
            this.drawTexturedModalRect(j + 155, 30, 0, 45, 155, 44);
        }
        else {
            this.drawTexturedModalRect(j + 0, 30, 0, 0, 155, 44);
            this.drawTexturedModalRect(j + 155, 30, 0, 45, 155, 44);
        }
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)(ModGuiMainMenu.width / 2 + 90), 70.0f, 0.0f);
        GlStateManager.rotate(-20.0f, 0.0f, 0.0f, 1.0f);
        float f = 1.8f - LabyModCore.getMath().abs(LabyModCore.getMath().sin(Minecraft.getSystemTime() % 1000L / 1000.0f * 3.1415927f * 2.0f) * 0.1f);
        f = f * 100.0f / (LabyModCore.getMinecraft().getFontRenderer().getStringWidth(this.splashText) + 32);
        GlStateManager.scale(f, f, f);
        Gui.drawCenteredString(LabyModCore.getMinecraft().getFontRenderer(), this.splashText, 0, -8, -256);
        GlStateManager.popMatrix();
        String s = "Minecraft " + Source.ABOUT_MC_VERSION;
        if (this.mc.isDemo()) {
            s = String.valueOf(s) + " Demo";
        }
        final String dc = (this.startDebugClicking != -1L && this.startDebugClicking + 1000L < System.currentTimeMillis()) ? ModColor.cl("e") : "";
        this.drawString(LabyModCore.getMinecraft().getFontRenderer(), String.valueOf(dc) + "LabyMod " + "3.6.6" + " ", 2, ModGuiMainMenu.height - 10, -1);
        this.drawString(LabyModCore.getMinecraft().getFontRenderer(), s, 2, ModGuiMainMenu.height - 20, -1);
        final String s2 = "Copyright Mojang AB. Do not distribute!";
        this.drawString(LabyModCore.getMinecraft().getFontRenderer(), "Copyright Mojang AB. Do not distribute!", ModGuiMainMenu.width - LabyModCore.getMinecraft().getFontRenderer().getStringWidth("Copyright Mojang AB. Do not distribute!") - 2, ModGuiMainMenu.height - 10, -1);
        if (this.openGLWarning1 != null && this.openGLWarning1.length() > 0) {
            Gui.drawRect(this.field_92022_t - 2, this.field_92021_u - 2, this.field_92020_v + 2, this.field_92019_w - 1, 1428160512);
            this.drawString(LabyModCore.getMinecraft().getFontRenderer(), this.openGLWarning1, this.field_92022_t, this.field_92021_u, -1);
            this.drawString(LabyModCore.getMinecraft().getFontRenderer(), this.openGLWarning2, (ModGuiMainMenu.width - this.field_92024_r) / 2, this.buttonList.get(0).yPosition - 12, -1);
        }
        SplashLoader.getLoader().render(mouseX, mouseY);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        SplashLoader.getLoader().onClick(mouseX, mouseY);
        if (mouseX < 1500 && mouseY > LabyMod.getInstance().getDrawUtils().getHeight() - 20 && mouseButton == 0) {
            this.startDebugClicking = System.currentTimeMillis();
        }
        else {
            this.startDebugClicking = -1L;
        }
        synchronized (this.threadLock) {
            if (this.openGLWarning1.length() > 0 && mouseX >= this.field_92022_t && mouseX <= this.field_92020_v && mouseY >= this.field_92021_u && mouseY <= this.field_92019_w) {
                final GuiConfirmOpenLink guiconfirmopenlink = new GuiConfirmOpenLink(this, this.openGLWarningLink, 13, true);
                guiconfirmopenlink.disableSecurityWarning();
                this.mc.displayGuiScreen(guiconfirmopenlink);
            }
            monitorexit(this.threadLock);
        }
    }
    
    @Override
    protected void mouseReleased(final int mouseX, final int mouseY, final int state) {
        super.mouseReleased(mouseX, mouseY, state);
        if (mouseX < 1500 && mouseY > LabyMod.getInstance().getDrawUtils().getHeight() - 20 && state == 0 && this.startDebugClicking != -1L && this.startDebugClicking + 1000L < System.currentTimeMillis()) {
            Debug.openDebugConsole();
        }
        this.startDebugClicking = -1L;
    }
    
    static /* synthetic */ void access$0(final ModGuiMainMenu modGuiMainMenu, final String splashText) {
        modGuiMainMenu.splashText = splashText;
    }
}
