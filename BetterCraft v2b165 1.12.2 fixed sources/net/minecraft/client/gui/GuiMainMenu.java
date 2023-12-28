// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.gui;

import com.google.common.util.concurrent.Runnables;
import net.minecraft.util.StringUtils;
import net.minecraft.util.Session;
import me.amkgre.bettercraft.client.mods.altmanager.GuiAltManager;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import optifine.CustomPanoramaProperties;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.MathHelper;
import optifine.CustomPanorama;
import org.lwjgl.util.glu.Project;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import java.net.URI;
import net.minecraft.world.WorldServerDemo;
import me.amkgre.bettercraft.client.gui.GuiMods;
import me.amkgre.bettercraft.client.gui.GuiBackground;
import me.amkgre.bettercraft.client.mods.status.GuiStatus;
import me.amkgre.bettercraft.client.gui.GuiTools;
import net.minecraft.world.storage.WorldInfo;
import net.minecraft.world.storage.ISaveFormat;
import optifine.Reflector;
import net.minecraft.realms.RealmsBridge;
import java.util.Date;
import java.util.Calendar;
import me.amkgre.bettercraft.client.utils.RenderUtils;
import net.minecraft.client.settings.GameSettings;
import java.util.List;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.renderer.OpenGlHelper;
import org.lwjgl.opengl.GLContext;
import java.io.Closeable;
import org.apache.commons.io.IOUtils;
import java.io.IOException;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import net.minecraft.client.Minecraft;
import com.google.common.collect.Lists;
import net.minecraft.util.text.TextFormatting;
import org.apache.logging.log4j.LogManager;
import me.amkgre.bettercraft.client.utils.ParticleUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.renderer.texture.DynamicTexture;
import java.util.Random;
import org.apache.logging.log4j.Logger;

public class GuiMainMenu extends GuiScreen
{
    private static final Logger LOGGER;
    private static final Random RANDOM;
    private final float updateCounter;
    private String splashText;
    private GuiButton buttonResetDemo;
    private float panoramaTimer;
    private DynamicTexture viewportTexture;
    private final Object threadLock;
    public static final String MORE_INFO_TEXT;
    private int openGLWarning2Width;
    private int openGLWarning1Width;
    private int openGLWarningX1;
    private int openGLWarningY1;
    private int openGLWarningX2;
    private int openGLWarningY2;
    private String openGLWarning1;
    private String openGLWarning2;
    private String openGLWarningLink;
    private static final ResourceLocation SPLASH_TEXTS;
    private static final ResourceLocation MINECRAFT_TITLE_TEXTURES;
    private static final ResourceLocation field_194400_H;
    private static final ResourceLocation[] TITLE_PANORAMA_PATHS;
    private ResourceLocation backgroundTexture;
    private GuiButton realmsButton;
    private boolean hasCheckedForRealmsNotification;
    private GuiScreen realmsNotification;
    private int field_193978_M;
    private int field_193979_N;
    private GuiButton modButton;
    private GuiScreen modUpdateNotification;
    private ParticleUtils particles;
    private boolean accountInfo;
    private boolean accountInfoOverride;
    
    static {
        LOGGER = LogManager.getLogger();
        RANDOM = new Random();
        MORE_INFO_TEXT = "Please click " + TextFormatting.UNDERLINE + "here" + TextFormatting.RESET + " for more information.";
        SPLASH_TEXTS = new ResourceLocation("texts/splashes.txt");
        MINECRAFT_TITLE_TEXTURES = new ResourceLocation("textures/gui/title/minecraft.png");
        field_194400_H = new ResourceLocation("textures/gui/title/edition.png");
        TITLE_PANORAMA_PATHS = new ResourceLocation[] { new ResourceLocation("textures/gui/title/background/panorama_0.png"), new ResourceLocation("textures/gui/title/background/panorama_1.png"), new ResourceLocation("textures/gui/title/background/panorama_2.png"), new ResourceLocation("textures/gui/title/background/panorama_3.png"), new ResourceLocation("textures/gui/title/background/panorama_4.png"), new ResourceLocation("textures/gui/title/background/panorama_5.png") };
    }
    
    public GuiMainMenu() {
        this.threadLock = new Object();
        this.openGLWarning2 = GuiMainMenu.MORE_INFO_TEXT;
        this.splashText = "missingno";
        IResource iresource = null;
        Label_0180: {
            try {
                final List<String> list = (List<String>)Lists.newArrayList();
                iresource = Minecraft.getMinecraft().getResourceManager().getResource(GuiMainMenu.SPLASH_TEXTS);
                final BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(iresource.getInputStream(), StandardCharsets.UTF_8));
                String s;
                while ((s = bufferedreader.readLine()) != null) {
                    s = s.trim();
                    if (!s.isEmpty()) {
                        list.add(s);
                    }
                }
                if (!list.isEmpty()) {
                    do {
                        this.splashText = list.get(GuiMainMenu.RANDOM.nextInt(list.size()));
                    } while (this.splashText.hashCode() == 125780783);
                }
            }
            catch (final IOException ex) {
                break Label_0180;
            }
            finally {
                IOUtils.closeQuietly(iresource);
            }
            IOUtils.closeQuietly(iresource);
        }
        this.updateCounter = GuiMainMenu.RANDOM.nextFloat();
        this.openGLWarning1 = "";
        if (!GLContext.getCapabilities().OpenGL20 && !OpenGlHelper.areShadersSupported()) {
            this.openGLWarning1 = I18n.format("title.oldgl1", new Object[0]);
            this.openGLWarning2 = I18n.format("title.oldgl2", new Object[0]);
            this.openGLWarningLink = "https://help.mojang.com/customer/portal/articles/325948?ref=game";
        }
    }
    
    private boolean areRealmsNotificationsEnabled() {
        return Minecraft.getMinecraft().gameSettings.getOptionOrdinalValue(GameSettings.Options.REALMS_NOTIFICATIONS) && this.realmsNotification != null;
    }
    
    @Override
    public void updateScreen() {
        if (this.areRealmsNotificationsEnabled()) {
            this.realmsNotification.updateScreen();
        }
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
        Minecraft.getMinecraft();
        RenderUtils.downloadSkin(Minecraft.getSession().getUsername());
        this.particles = new ParticleUtils(GuiMainMenu.width, GuiMainMenu.height);
        this.viewportTexture = new DynamicTexture(256, 256);
        this.backgroundTexture = this.mc.getTextureManager().getDynamicTextureLocation("background", this.viewportTexture);
        this.field_193978_M = this.fontRendererObj.getStringWidth("Copyright Mojang AB. Do not distribute!");
        this.field_193979_N = GuiMainMenu.width - this.field_193978_M - 2;
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
        final int i = 24;
        final int j = GuiMainMenu.height / 4 + 48;
        if (this.mc.isDemo()) {
            this.addDemoButtons(j, 24);
        }
        else {
            this.addSingleplayerMultiplayerButtons(j, 24);
        }
        this.buttonList.add(new GuiButton(0, GuiMainMenu.width / 2 - 100, j + 72 + 12, 98, 20, I18n.format("menu.options", new Object[0])));
        this.buttonList.add(new GuiButton(4, GuiMainMenu.width / 2 + 2, j + 72 + 12, 98, 20, I18n.format("menu.quit", new Object[0])));
        synchronized (this.threadLock) {
            this.openGLWarning1Width = this.fontRendererObj.getStringWidth(this.openGLWarning1);
            this.openGLWarning2Width = this.fontRendererObj.getStringWidth(this.openGLWarning2);
            final int k = Math.max(this.openGLWarning1Width, this.openGLWarning2Width);
            this.openGLWarningX1 = (GuiMainMenu.width - k) / 2;
            this.openGLWarningY1 = this.buttonList.get(0).yPosition - 24;
            this.openGLWarningX2 = this.openGLWarningX1 + k;
            this.openGLWarningY2 = this.openGLWarningY1 + 24;
            monitorexit(this.threadLock);
        }
        this.mc.setConnectedToRealms(false);
        if (Minecraft.getMinecraft().gameSettings.getOptionOrdinalValue(GameSettings.Options.REALMS_NOTIFICATIONS) && !this.hasCheckedForRealmsNotification) {
            final RealmsBridge realmsbridge = new RealmsBridge();
            this.realmsNotification = realmsbridge.getNotificationScreen(this);
            this.hasCheckedForRealmsNotification = true;
        }
        if (this.areRealmsNotificationsEnabled()) {
            this.realmsNotification.setGuiSize(GuiMainMenu.width, GuiMainMenu.height);
            this.realmsNotification.initGui();
        }
        if (Reflector.NotificationModUpdateScreen_init.exists()) {
            this.modUpdateNotification = (GuiScreen)Reflector.call(Reflector.NotificationModUpdateScreen_init, this, this.modButton);
        }
    }
    
    private void addSingleplayerMultiplayerButtons(final int p_73969_1_, final int p_73969_2_) {
        this.buttonList.add(new GuiButton(1, GuiMainMenu.width / 2 - 100, p_73969_1_ + p_73969_2_ - 42, 98, 20, I18n.format("menu.singleplayer", new Object[0])));
        this.buttonList.add(new GuiButton(2, GuiMainMenu.width / 2 + 2, p_73969_1_ + p_73969_2_ - 42, 98, 20, I18n.format("menu.multiplayer", new Object[0])));
        this.buttonList.add(new GuiButton(14, GuiMainMenu.width / 2 - 100, p_73969_1_ + p_73969_2_ * 2 - 15, 98, 20, I18n.format("Server Status", new Object[0])));
        this.buttonList.add(new GuiButton(15, GuiMainMenu.width / 2 + 2, p_73969_1_ + p_73969_2_ * 2 - 15, 98, 20, I18n.format("Background", new Object[0])));
        this.buttonList.add(new GuiButton(16, GuiMainMenu.width / 2 - 100, p_73969_1_ + p_73969_2_ * 2 + 10, I18n.format("Tools", new Object[0])));
        this.buttonList.add(new GuiButton(17, GuiMainMenu.width / 2 - 100, p_73969_1_ + p_73969_2_ * 2 - 40, I18n.format("Mods", new Object[0])));
        if (Reflector.GuiModList_Constructor.exists()) {
            this.buttonList.add(this.modButton = new GuiButton(6, GuiMainMenu.width / 2 - 100, p_73969_1_ + p_73969_2_ * 2, 98, 20, I18n.format("fml.menu.mods", new Object[0])));
        }
    }
    
    private void addDemoButtons(final int p_73972_1_, final int p_73972_2_) {
        this.buttonList.add(new GuiButton(11, GuiMainMenu.width / 2 - 100, p_73972_1_, I18n.format("menu.playdemo", new Object[0])));
        this.buttonResetDemo = this.addButton(new GuiButton(12, GuiMainMenu.width / 2 - 100, p_73972_1_ + p_73972_2_ * 1, I18n.format("menu.resetdemo", new Object[0])));
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
        if (button.id == 100) {
            this.mc.displayGuiScreen(new GuiTools(null));
        }
        if (button.id == 1) {
            this.mc.displayGuiScreen(new GuiWorldSelection(this));
        }
        if (button.id == 2) {
            this.mc.displayGuiScreen(new GuiMultiplayer(this));
        }
        if (button.id == 14) {
            this.mc.displayGuiScreen(new GuiStatus(this));
        }
        if (button.id == 15) {
            this.mc.displayGuiScreen(new GuiBackground(this));
        }
        if (button.id == 16) {
            this.mc.displayGuiScreen(new GuiTools(this));
        }
        if (button.id == 17) {
            this.mc.displayGuiScreen(new GuiMods(this));
        }
        if (button.id == 4) {
            this.mc.shutdown();
        }
        if (button.id == 6 && Reflector.GuiModList_Constructor.exists()) {
            this.mc.displayGuiScreen((GuiScreen)Reflector.newInstance(Reflector.GuiModList_Constructor, this));
        }
        if (button.id == 11) {
            this.mc.launchIntegratedServer("Demo_World", "Demo_World", WorldServerDemo.DEMO_WORLD_SETTINGS);
        }
        if (button.id == 12) {
            final ISaveFormat isaveformat = this.mc.getSaveLoader();
            final WorldInfo worldinfo = isaveformat.getWorldInfo("Demo_World");
            if (worldinfo != null) {
                this.mc.displayGuiScreen(new GuiYesNo(this, I18n.format("selectWorld.deleteQuestion", new Object[0]), "'" + worldinfo.getWorldName() + "' " + I18n.format("selectWorld.deleteWarning", new Object[0]), I18n.format("selectWorld.deleteButton", new Object[0]), I18n.format("gui.cancel", new Object[0]), 12));
            }
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
        else if (id == 12) {
            this.mc.displayGuiScreen(this);
        }
        else if (id == 13) {
            if (result) {
                try {
                    final Class<?> oclass = Class.forName("java.awt.Desktop");
                    final Object object = oclass.getMethod("getDesktop", (Class<?>[])new Class[0]).invoke(null, new Object[0]);
                    oclass.getMethod("browse", URI.class).invoke(object, new URI(this.openGLWarningLink));
                }
                catch (final Throwable throwable1) {
                    GuiMainMenu.LOGGER.error("Couldn't open link", throwable1);
                }
            }
            this.mc.displayGuiScreen(this);
        }
    }
    
    private void drawPanorama(final int mouseX, final int mouseY, final float partialTicks) {
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
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
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        final int i = 8;
        int j = 64;
        final CustomPanoramaProperties custompanoramaproperties = CustomPanorama.getCustomPanoramaProperties();
        if (custompanoramaproperties != null) {
            j = custompanoramaproperties.getBlur1();
        }
        for (int k = 0; k < j; ++k) {
            GlStateManager.pushMatrix();
            final float f = (k % 8 / 8.0f - 0.5f) / 64.0f;
            final float f2 = (k / 8 / 8.0f - 0.5f) / 64.0f;
            final float f3 = 0.0f;
            GlStateManager.translate(f, f2, 0.0f);
            GlStateManager.rotate(MathHelper.sin(this.panoramaTimer / 400.0f) * 25.0f + 20.0f, 1.0f, 0.0f, 0.0f);
            GlStateManager.rotate(-this.panoramaTimer * 0.1f, 0.0f, 1.0f, 0.0f);
            for (int l = 0; l < 6; ++l) {
                GlStateManager.pushMatrix();
                if (l == 1) {
                    GlStateManager.rotate(90.0f, 0.0f, 1.0f, 0.0f);
                }
                if (l == 2) {
                    GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f);
                }
                if (l == 3) {
                    GlStateManager.rotate(-90.0f, 0.0f, 1.0f, 0.0f);
                }
                if (l == 4) {
                    GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
                }
                if (l == 5) {
                    GlStateManager.rotate(-90.0f, 1.0f, 0.0f, 0.0f);
                }
                ResourceLocation[] aresourcelocation = GuiMainMenu.TITLE_PANORAMA_PATHS;
                if (custompanoramaproperties != null) {
                    aresourcelocation = custompanoramaproperties.getPanoramaLocations();
                }
                this.mc.getTextureManager().bindTexture(aresourcelocation[l]);
                bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                final int i2 = 255 / (k + 1);
                final float f4 = 0.0f;
                bufferbuilder.pos(-1.0, -1.0, 1.0).tex(0.0, 0.0).color(255, 255, 255, i2).endVertex();
                bufferbuilder.pos(1.0, -1.0, 1.0).tex(1.0, 0.0).color(255, 255, 255, i2).endVertex();
                bufferbuilder.pos(1.0, 1.0, 1.0).tex(1.0, 1.0).color(255, 255, 255, i2).endVertex();
                bufferbuilder.pos(-1.0, 1.0, 1.0).tex(0.0, 1.0).color(255, 255, 255, i2).endVertex();
                tessellator.draw();
                GlStateManager.popMatrix();
            }
            GlStateManager.popMatrix();
            GlStateManager.colorMask(true, true, true, false);
        }
        bufferbuilder.setTranslation(0.0, 0.0, 0.0);
        GlStateManager.colorMask(true, true, true, true);
        GlStateManager.matrixMode(5889);
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(5888);
        GlStateManager.popMatrix();
        GlStateManager.depthMask(true);
        GlStateManager.enableCull();
        GlStateManager.enableDepth();
    }
    
    private void rotateAndBlurSkybox() {
        this.mc.getTextureManager().bindTexture(this.backgroundTexture);
        GlStateManager.glTexParameteri(3553, 10241, 9729);
        GlStateManager.glTexParameteri(3553, 10240, 9729);
        GlStateManager.glCopyTexSubImage2D(3553, 0, 0, 0, 0, 0, 256, 256);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.colorMask(true, true, true, false);
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        GlStateManager.disableAlpha();
        final int i = 3;
        int j = 3;
        final CustomPanoramaProperties custompanoramaproperties = CustomPanorama.getCustomPanoramaProperties();
        if (custompanoramaproperties != null) {
            j = custompanoramaproperties.getBlur2();
        }
        for (int k = 0; k < j; ++k) {
            final float f = 1.0f / (k + 1);
            final int l = GuiMainMenu.width;
            final int i2 = GuiMainMenu.height;
            final float f2 = (k - 1) / 256.0f;
            bufferbuilder.pos(l, i2, GuiMainMenu.zLevel).tex(0.0f + f2, 1.0).color(1.0f, 1.0f, 1.0f, f).endVertex();
            bufferbuilder.pos(l, 0.0, GuiMainMenu.zLevel).tex(1.0f + f2, 1.0).color(1.0f, 1.0f, 1.0f, f).endVertex();
            bufferbuilder.pos(0.0, 0.0, GuiMainMenu.zLevel).tex(1.0f + f2, 0.0).color(1.0f, 1.0f, 1.0f, f).endVertex();
            bufferbuilder.pos(0.0, i2, GuiMainMenu.zLevel).tex(0.0f + f2, 0.0).color(1.0f, 1.0f, 1.0f, f).endVertex();
        }
        tessellator.draw();
        GlStateManager.enableAlpha();
        GlStateManager.colorMask(true, true, true, true);
    }
    
    private void renderSkybox(final int mouseX, final int mouseY, final float partialTicks) {
        this.mc.getFramebuffer().unbindFramebuffer();
        GlStateManager.viewport(0, 0, 256, 256);
        this.drawPanorama(mouseX, mouseY, partialTicks);
        this.rotateAndBlurSkybox();
        int i = 3;
        final CustomPanoramaProperties custompanoramaproperties = CustomPanorama.getCustomPanoramaProperties();
        if (custompanoramaproperties != null) {
            i = custompanoramaproperties.getBlur3();
        }
        for (int j = 0; j < i; ++j) {
            this.rotateAndBlurSkybox();
            this.rotateAndBlurSkybox();
        }
        this.mc.getFramebuffer().bindFramebuffer(true);
        GlStateManager.viewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
        final float f2 = 120.0f / ((GuiMainMenu.width > GuiMainMenu.height) ? GuiMainMenu.width : GuiMainMenu.height);
        final float f3 = GuiMainMenu.height * f2 / 256.0f;
        final float f4 = GuiMainMenu.width * f2 / 256.0f;
        final int k = GuiMainMenu.width;
        final int l = GuiMainMenu.height;
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        bufferbuilder.pos(0.0, l, GuiMainMenu.zLevel).tex(0.5f - f3, 0.5f + f4).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
        bufferbuilder.pos(k, l, GuiMainMenu.zLevel).tex(0.5f - f3, 0.5f - f4).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
        bufferbuilder.pos(k, 0.0, GuiMainMenu.zLevel).tex(0.5f + f3, 0.5f - f4).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
        bufferbuilder.pos(0.0, 0.0, GuiMainMenu.zLevel).tex(0.5f + f3, 0.5f + f4).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
        tessellator.draw();
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        GL11.glEnable(3042);
        this.particles.drawParticles();
        Gui.drawString(this.fontRendererObj, Minecraft.session.getUsername(), 5, 59, -1);
        try {
            final int x = 2;
            final int y = 2;
            final int scale = 55;
            final String name = Minecraft.session.getUsername();
            if (RenderUtils.dynamicTexture != null) {
                GlStateManager.bindTexture(RenderUtils.dynamicTexture.getGlTextureId());
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                Gui.drawScaledCustomSizeModalRect(x, y, 0.0f, 0.0f, scale, scale, scale, scale, (float)scale, (float)scale);
            }
            if (mouseX > 4 && mouseX < 56 && mouseY > 5 && mouseY < 57) {
                Gui.drawRect(5, 5, 55, 55, Mouse.isButtonDown(0) ? 1342177280 : 1073741824);
                this.accountInfo = true;
                if (Mouse.isButtonDown(0)) {
                    this.mc.displayGuiScreen(new GuiAltManager(null));
                    Mouse.destroy();
                    Mouse.create();
                }
            }
            else {
                this.accountInfo = false;
            }
            GL11.glDisable(3042);
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        if (this.accountInfo || this.accountInfoOverride) {
            if (Minecraft.session.getSessionType() == Session.Type.LEGACY || Minecraft.session.getUsername().isEmpty()) {
                Gui.drawString(this.fontRendererObj, "§cNot Migrated", 5, 70, -1);
            }
            else {
                Gui.drawString(this.fontRendererObj, "§6Migrated (Mojang Account)", 5, 70, -1);
            }
        }
        GlStateManager.pushMatrix();
        GlStateManager.scale(1.5, 1.5, 0.0);
        Gui.drawString(this.fontRendererObj, " §dAccount Manager", 38, 3, -1);
        GlStateManager.popMatrix();
        Gui.drawString(this.fontRendererObj, "§7 (Click the head)", 58, 19, -1);
        Gui.drawString(this.fontRendererObj, "§9Discord: §7NzxterDC#6813", GuiMainMenu.width - 130, 5, -1);
        Gui.drawString(this.fontRendererObj, "§aMineCraft: §7NzxterMC", GuiMainMenu.width - 130, 15, -1);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        synchronized (this.threadLock) {
            if (!this.openGLWarning1.isEmpty() && !StringUtils.isNullOrEmpty(this.openGLWarningLink) && mouseX >= this.openGLWarningX1 && mouseX <= this.openGLWarningX2 && mouseY >= this.openGLWarningY1 && mouseY <= this.openGLWarningY2) {
                final GuiConfirmOpenLink guiconfirmopenlink = new GuiConfirmOpenLink(this, this.openGLWarningLink, 13, true);
                guiconfirmopenlink.disableSecurityWarning();
                this.mc.displayGuiScreen(guiconfirmopenlink);
            }
            monitorexit(this.threadLock);
        }
        if (this.areRealmsNotificationsEnabled()) {
            this.realmsNotification.mouseClicked(mouseX, mouseY, mouseButton);
        }
        if (mouseX > this.field_193979_N && mouseX < this.field_193979_N + this.field_193978_M && mouseY > GuiMainMenu.height - 10 && mouseY < GuiMainMenu.height) {
            this.mc.displayGuiScreen(new GuiWinGame(false, Runnables.doNothing()));
        }
    }
    
    @Override
    public void onGuiClosed() {
        if (this.realmsNotification != null) {
            this.realmsNotification.onGuiClosed();
        }
    }
}
