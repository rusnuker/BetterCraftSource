// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client;

import java.util.stream.Stream;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.client.util.ISearchTree;
import java.util.concurrent.Executors;
import com.google.common.util.concurrent.ListenableFutureTask;
import com.google.common.util.concurrent.Futures;
import org.apache.commons.lang3.Validate;
import java.util.concurrent.Callable;
import net.minecraft.client.gui.ScreenChatOptions;
import net.minecraft.util.ScreenShotHelper;
import net.minecraft.client.gui.GuiControls;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.WorldProviderHell;
import net.minecraft.client.gui.GuiWinGame;
import com.mojang.authlib.properties.Property;
import com.google.common.collect.Multimap;
import java.nio.IntBuffer;
import com.mojang.authlib.GameProfile;
import org.lwjgl.opengl.ContextCapabilities;
import org.apache.commons.io.Charsets;
import com.google.common.hash.Hashing;
import org.lwjgl.opengl.GLContext;
import java.nio.ByteOrder;
import com.google.common.util.concurrent.ListenableFuture;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.block.Block;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.EntityLeashKnot;
import net.minecraft.init.Items;
import net.minecraft.entity.item.EntityPainting;
import com.mojang.authlib.AuthenticationService;
import net.minecraft.client.network.NetHandlerPlayClient;
import com.TominoCZ.FBP.FBP;
import net.minecraft.util.MovementInputFromOptions;
import net.minecraft.stats.RecipeBook;
import net.minecraft.stats.StatisticsManager;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.gui.chat.NarratorChatListener;
import java.net.SocketAddress;
import com.mojang.authlib.GameProfileRepository;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.network.login.client.CPacketLoginStart;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.INetHandler;
import net.minecraft.client.network.NetHandlerLoginClient;
import net.minecraft.client.gui.GuiScreenWorking;
import net.minecraft.client.resources.I18n;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.world.storage.WorldInfo;
import net.minecraft.world.WorldSettings;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.network.Packet;
import net.minecraft.util.EnumFacing;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.client.gui.advancements.GuiScreenAdvancements;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.util.text.TextComponentTranslation;
import com.mcf.davidee.nbtedit.gui.GuiEditNBTTree;
import me.amkgre.bettercraft.client.gui.GuiRShift;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.client.gui.GuiSleepMP;
import me.amkgre.bettercraft.client.mods.cosmetics.impl.CosmeticSkinDerp;
import me.amkgre.bettercraft.client.mods.cosmetics.GuiCosmetics;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumHand;
import net.minecraft.block.material.Material;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiIngameMenuSinglePlayer;
import java.text.DecimalFormat;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.renderer.chunk.RenderChunk;
import com.darkmagician6.eventapi.events.Event;
import com.darkmagician6.eventapi.EventManager;
import me.amkgre.bettercraft.client.events.GameLoopEvent;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.gui.ScaledResolution;
import java.util.Set;
import com.google.common.collect.Sets;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import java.nio.ByteBuffer;
import org.lwjgl.opengl.PixelFormat;
import net.minecraft.client.resources.data.LanguageMetadataSection;
import net.minecraft.client.resources.data.LanguageMetadataSectionSerializer;
import net.minecraft.client.resources.data.PackMetadataSection;
import net.minecraft.client.resources.data.PackMetadataSectionSerializer;
import net.minecraft.client.resources.data.AnimationMetadataSection;
import net.minecraft.client.resources.data.AnimationMetadataSectionSerializer;
import net.minecraft.client.resources.data.FontMetadataSection;
import net.minecraft.client.resources.data.FontMetadataSectionSerializer;
import net.minecraft.client.resources.data.IMetadataSectionSerializer;
import net.minecraft.client.resources.data.TextureMetadataSection;
import net.minecraft.client.resources.data.TextureMetadataSectionSerializer;
import java.util.Iterator;
import net.minecraft.client.util.RecipeBookClient;
import net.minecraft.client.gui.recipebook.RecipeList;
import java.util.function.Consumer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.NonNullList;
import net.minecraft.item.ItemStack;
import net.minecraft.client.util.SearchTree;
import java.util.Collections;
import net.minecraft.item.Item;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Function;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.util.ITooltipFlag;
import java.awt.AWTException;
import org.newdawn.slick.SlickException;
import java.io.IOException;
import org.lwjgl.LWJGLException;
import me.amkgre.bettercraft.client.mods.discord.rpc.DiscordRPRegistry;
import me.amkgre.bettercraft.client.utils.DisplayTrayUtils;
import java.awt.TrayIcon;
import org.lwjgl.opengl.OpenGLException;
import me.amkgre.bettercraft.client.gui.GuiFirstTime;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.world.World;
import net.minecraft.client.renderer.texture.ITickableTextureObject;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.FoliageColorReloadListener;
import net.minecraft.client.resources.GrassColorReloadListener;
import net.minecraft.world.chunk.storage.AnvilSaveConverter;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.client.renderer.OpenGlHelper;
import org.lwjgl.Sys;
import me.amkgre.bettercraft.client.Client;
import me.amkgre.bettercraft.client.utils.SplashProgressUtils;
import me.amkgre.bettercraft.client.mods.music.GuiMusic;
import me.amkgre.bettercraft.client.mods.discord.rpc.DiscordMain;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.util.ReportedException;
import net.minecraft.util.MinecraftError;
import org.lwjgl.opengl.Display;
import net.minecraft.client.gui.GuiMemoryErrorScreen;
import net.minecraft.util.datafix.DataFixesManager;
import net.minecraft.util.text.TextComponentKeybind;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Bootstrap;
import java.util.Locale;
import javax.imageio.ImageIO;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import java.util.UUID;
import com.google.common.collect.Queues;
import net.minecraft.server.MinecraftServer;
import net.minecraft.client.main.GameConfiguration;
import com.google.common.collect.Lists;
import net.minecraft.util.Util;
import org.apache.logging.log4j.LogManager;
import net.minecraft.client.tutorial.Tutorial;
import net.minecraft.client.gui.toasts.GuiToast;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.block.model.ModelManager;
import java.util.concurrent.FutureTask;
import java.util.Queue;
import net.minecraft.client.resources.SkinManager;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import net.minecraft.client.audio.MusicTicker;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.resources.LanguageManager;
import net.minecraft.client.resources.ResourcePackRepository;
import net.minecraft.client.resources.DefaultResourcePack;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.data.MetadataSerializer;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.profiler.Profiler;
import net.minecraft.network.NetworkManager;
import net.minecraft.util.FrameTimer;
import com.mojang.text2speech.Narrator;
import net.minecraft.world.storage.ISaveFormat;
import java.net.Proxy;
import net.minecraft.util.MouseHelper;
import net.minecraft.client.settings.CreativeSettings;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.client.renderer.debug.DebugRenderer;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.Session;
import net.minecraft.client.util.SearchTreeManager;
import net.minecraft.client.particle.ParticleManager;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.profiler.Snooper;
import net.minecraft.util.Timer;
import net.minecraft.crash.CrashReport;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.multiplayer.ServerData;
import com.mojang.authlib.properties.PropertyMap;
import java.io.File;
import org.lwjgl.opengl.DisplayMode;
import java.util.List;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Logger;
import net.minecraft.profiler.ISnooperInfo;
import net.minecraft.util.IThreadListener;

public class Minecraft implements IThreadListener, ISnooperInfo
{
    private static final Logger LOGGER;
    private static final ResourceLocation LOCATION_MOJANG_PNG;
    public static final boolean IS_RUNNING_ON_MAC;
    public static byte[] memoryReserve;
    private static final List<DisplayMode> MAC_DISPLAY_MODES;
    private final File fileResourcepacks;
    private final PropertyMap twitchDetails;
    private final PropertyMap profileProperties;
    private ServerData currentServerData;
    public TextureManager renderEngine;
    private static Minecraft theMinecraft;
    private final DataFixer dataFixer;
    public PlayerControllerMP playerController;
    private boolean fullscreen;
    private final boolean enableGLErrorChecking = true;
    private boolean hasCrashed;
    private CrashReport crashReporter;
    public int displayWidth;
    public int displayHeight;
    private boolean connectedToRealms;
    public final Timer timer;
    private final Snooper usageSnooper;
    public WorldClient world;
    public RenderGlobal renderGlobal;
    private RenderManager renderManager;
    private RenderItem renderItem;
    private ItemRenderer itemRenderer;
    public EntityPlayerSP player;
    @Nullable
    private Entity renderViewEntity;
    public Entity pointedEntity;
    public ParticleManager effectRenderer;
    private SearchTreeManager field_193995_ae;
    public static Session session;
    private boolean isGamePaused;
    private float field_193996_ah;
    public FontRenderer fontRendererObj;
    public FontRenderer mcfontRendererObj;
    public FontRenderer standardGalacticFontRenderer;
    @Nullable
    public static GuiScreen currentScreen;
    public LoadingScreenRenderer loadingScreen;
    public EntityRenderer entityRenderer;
    public DebugRenderer debugRenderer;
    private int leftClickCounter;
    private final int tempDisplayWidth;
    private final int tempDisplayHeight;
    @Nullable
    private IntegratedServer theIntegratedServer;
    public GuiIngame ingameGUI;
    public boolean skipRenderWorld;
    public RayTraceResult objectMouseOver;
    public GameSettings gameSettings;
    public CreativeSettings field_191950_u;
    public MouseHelper mouseHelper;
    public final File mcDataDir;
    private final File fileAssets;
    private final String launchedVersion;
    private final String versionType;
    private final Proxy proxy;
    private ISaveFormat saveLoader;
    public static int debugFPS;
    private int rightClickDelayTimer;
    private String serverName;
    private int serverPort;
    public boolean inGameHasFocus;
    long systemTime;
    private int joinPlayerCounter;
    public Narrator narrator;
    public final FrameTimer frameTimer;
    long startNanoTime;
    private final boolean jvm64bit;
    private final boolean isDemo;
    @Nullable
    private NetworkManager myNetworkManager;
    private boolean integratedServerIsRunning;
    public final Profiler mcProfiler;
    private long debugCrashKeyPressTime;
    private IReloadableResourceManager mcResourceManager;
    private final MetadataSerializer metadataSerializer_;
    private final List<IResourcePack> defaultResourcePacks;
    private final DefaultResourcePack mcDefaultResourcePack;
    private ResourcePackRepository mcResourcePackRepository;
    private LanguageManager mcLanguageManager;
    private BlockColors blockColors;
    private ItemColors itemColors;
    public Framebuffer framebufferMc;
    private TextureMap textureMapBlocks;
    private SoundHandler mcSoundHandler;
    private MusicTicker mcMusicTicker;
    private ResourceLocation mojangLogo;
    private final MinecraftSessionService sessionService;
    private SkinManager skinManager;
    private final Queue<FutureTask<?>> scheduledTasks;
    private final Thread mcThread;
    private ModelManager modelManager;
    private BlockRendererDispatcher blockRenderDispatcher;
    private final GuiToast field_193034_aS;
    volatile boolean running;
    public String debug;
    public boolean renderChunksMany;
    private long debugUpdateTime;
    private int fpsCounter;
    private boolean actionKeyF3;
    private final Tutorial field_193035_aW;
    long prevFrameTime;
    private String debugProfilerName;
    private boolean field_181541_X;
    
    static {
        LOGGER = LogManager.getLogger();
        LOCATION_MOJANG_PNG = new ResourceLocation("textures/gui/title/mojang.png");
        IS_RUNNING_ON_MAC = (Util.getOSType() == Util.EnumOS.OSX);
        Minecraft.memoryReserve = new byte[10485760];
        MAC_DISPLAY_MODES = Lists.newArrayList(new DisplayMode(2560, 1600), new DisplayMode(2880, 1800));
    }
    
    public Minecraft(final GameConfiguration gameConfig) {
        this.timer = new Timer(20.0f);
        this.usageSnooper = new Snooper("client", this, MinecraftServer.getCurrentTimeMillis());
        this.field_193995_ae = new SearchTreeManager();
        this.systemTime = getSystemTime();
        this.narrator = Narrator.getNarrator();
        this.frameTimer = new FrameTimer();
        this.startNanoTime = System.nanoTime();
        this.mcProfiler = new Profiler();
        this.debugCrashKeyPressTime = -1L;
        this.metadataSerializer_ = new MetadataSerializer();
        this.defaultResourcePacks = (List<IResourcePack>)Lists.newArrayList();
        this.scheduledTasks = (Queue<FutureTask<?>>)Queues.newArrayDeque();
        this.mcThread = Thread.currentThread();
        this.running = true;
        this.debug = "";
        this.renderChunksMany = true;
        this.debugUpdateTime = getSystemTime();
        this.prevFrameTime = -1L;
        this.debugProfilerName = "root";
        this.field_181541_X = false;
        Minecraft.theMinecraft = this;
        this.mcDataDir = gameConfig.folderInfo.mcDataDir;
        this.fileAssets = gameConfig.folderInfo.assetsDir;
        this.fileResourcepacks = gameConfig.folderInfo.resourcePacksDir;
        this.launchedVersion = gameConfig.gameInfo.version;
        this.versionType = gameConfig.gameInfo.versionType;
        this.twitchDetails = gameConfig.userInfo.userProperties;
        this.profileProperties = gameConfig.userInfo.profileProperties;
        this.mcDefaultResourcePack = new DefaultResourcePack(gameConfig.folderInfo.getAssetsIndex());
        this.proxy = ((gameConfig.userInfo.proxy == null) ? Proxy.NO_PROXY : gameConfig.userInfo.proxy);
        this.sessionService = new YggdrasilAuthenticationService(this.proxy, UUID.randomUUID().toString()).createMinecraftSessionService();
        Minecraft.session = gameConfig.userInfo.session;
        Minecraft.LOGGER.info("Setting user: {}", Minecraft.session.getUsername());
        Minecraft.LOGGER.debug("(Session ID is <censored>)", Minecraft.session.getSessionID());
        this.isDemo = gameConfig.gameInfo.isDemo;
        this.displayWidth = ((gameConfig.displayInfo.width > 0) ? gameConfig.displayInfo.width : 1);
        this.displayHeight = ((gameConfig.displayInfo.height > 0) ? gameConfig.displayInfo.height : 1);
        this.tempDisplayWidth = gameConfig.displayInfo.width;
        this.tempDisplayHeight = gameConfig.displayInfo.height;
        this.fullscreen = gameConfig.displayInfo.fullscreen;
        this.jvm64bit = isJvm64bit();
        this.theIntegratedServer = null;
        if (gameConfig.serverInfo.serverName != null) {
            this.serverName = gameConfig.serverInfo.serverName;
            this.serverPort = gameConfig.serverInfo.serverPort;
        }
        ImageIO.setUseCache(false);
        Locale.setDefault(Locale.ROOT);
        Bootstrap.register();
        TextComponentKeybind.field_193637_b = KeyBinding::func_193626_b;
        this.dataFixer = DataFixesManager.createFixer();
        this.field_193034_aS = new GuiToast(this);
        this.field_193035_aW = new Tutorial(this);
    }
    
    public void run() {
        this.running = true;
        while (this.running) {
            Label_0042: {
                try {
                    this.startGame();
                    break Label_0042;
                }
                catch (final Throwable var3) {
                    final CrashReport var4 = CrashReport.makeCrashReport(var3, "Initializing game");
                    var4.makeCategory("Initialization");
                    this.displayCrashReport(this.addGraphicsAndWorldToCrashReport(var4));
                    return;
                }
                try {
                    do {
                        try {
                            this.runGameLoop();
                        }
                        catch (final OutOfMemoryError var5) {
                            this.freeMemory();
                            this.displayGuiScreen(new GuiMemoryErrorScreen());
                            System.gc();
                        }
                        catch (final Throwable t) {}
                    } while (this.running && !Display.isCloseRequested());
                    this.shutdownMinecraftApplet();
                }
                catch (final MinecraftError minecraftError) {}
                catch (final ReportedException var6) {
                    this.addGraphicsAndWorldToCrashReport(var6.getCrashReport());
                    this.freeMemory();
                    Minecraft.LOGGER.fatal("Reported exception thrown!", var6);
                    this.displayCrashReport(var6.getCrashReport());
                }
                catch (final Throwable var7) {
                    final CrashReport var4 = this.addGraphicsAndWorldToCrashReport(new CrashReport("Unexpected error", var7));
                    this.freeMemory();
                    Minecraft.LOGGER.fatal("Unreported exception thrown!", var7);
                }
            }
        }
        this.displayGuiScreen(new GuiMainMenu());
    }
    
    private void startGame() throws LWJGLException, IOException, SlickException, AWTException {
        DiscordMain.getInstance().getDiscordRP().update("Startup...", "");
        new Thread(() -> GuiMusic.playMusic("http://www.mboxdrive.com/startup.mp3")).start();
        SplashProgressUtils.setProgress(1, "BetterCraft - Startup");
        Client.getInstance().onEnable();
        this.gameSettings = new GameSettings(this, this.mcDataDir);
        this.field_191950_u = new CreativeSettings(this, this.mcDataDir);
        this.defaultResourcePacks.add(this.mcDefaultResourcePack);
        this.startTimerHackThread();
        if (this.gameSettings.overrideHeight > 0 && this.gameSettings.overrideWidth > 0) {
            this.displayWidth = this.gameSettings.overrideWidth;
            this.displayHeight = this.gameSettings.overrideHeight;
        }
        Minecraft.LOGGER.info("LWJGL Version: {}", Sys.getVersion());
        this.setWindowIcon();
        this.setInitialDisplayMode();
        this.createDisplay();
        this.narrator.clear();
        this.narrator.say("Thanks for using BetterCraft");
        OpenGlHelper.initializeTextures();
        (this.framebufferMc = new Framebuffer(this.displayWidth, this.displayHeight, true)).setFramebufferColor(0.0f, 0.0f, 0.0f, 0.0f);
        this.registerMetadataSerializers();
        this.mcResourcePackRepository = new ResourcePackRepository(this.fileResourcepacks, new File(this.mcDataDir, "server-resource-packs"), this.mcDefaultResourcePack, this.metadataSerializer_, this.gameSettings);
        this.mcResourceManager = new SimpleReloadableResourceManager(this.metadataSerializer_);
        this.mcLanguageManager = new LanguageManager(this.metadataSerializer_, this.gameSettings.language);
        this.mcResourceManager.registerReloadListener(this.mcLanguageManager);
        this.refreshResources();
        this.renderEngine = new TextureManager(this.mcResourceManager);
        this.mcResourceManager.registerReloadListener(this.renderEngine);
        SplashProgressUtils.drawSplash(this.getTextureManager());
        this.skinManager = new SkinManager(this.renderEngine, new File(this.fileAssets, "skins"), this.sessionService);
        this.saveLoader = new AnvilSaveConverter(new File(this.mcDataDir, "saves"), this.dataFixer);
        this.mcSoundHandler = new SoundHandler(this.mcResourceManager, this.gameSettings);
        this.mcResourceManager.registerReloadListener(this.mcSoundHandler);
        this.mcMusicTicker = new MusicTicker(this);
        this.fontRendererObj = new FontRenderer(this.gameSettings, new ResourceLocation("textures/font/ascii.png"), this.renderEngine, false);
        if (this.gameSettings.language != null) {
            this.fontRendererObj.setUnicodeFlag(this.isUnicode());
            this.fontRendererObj.setBidiFlag(this.mcLanguageManager.isCurrentLanguageBidirectional());
        }
        this.standardGalacticFontRenderer = new FontRenderer(this.gameSettings, new ResourceLocation("textures/font/ascii_sga.png"), this.renderEngine, false);
        this.mcResourceManager.registerReloadListener(this.fontRendererObj);
        this.mcResourceManager.registerReloadListener(this.standardGalacticFontRenderer);
        this.mcResourceManager.registerReloadListener(new GrassColorReloadListener());
        this.mcResourceManager.registerReloadListener(new FoliageColorReloadListener());
        this.mouseHelper = new MouseHelper();
        SplashProgressUtils.setProgress(2, "Minecraft - Prestartup");
        this.checkGLError("Pre startup");
        GlStateManager.enableTexture2D();
        GlStateManager.shadeModel(7425);
        GlStateManager.clearDepth(1.0);
        GlStateManager.enableDepth();
        GlStateManager.depthFunc(515);
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.1f);
        GlStateManager.cullFace(GlStateManager.CullFace.BACK);
        GlStateManager.matrixMode(5889);
        GlStateManager.loadIdentity();
        GlStateManager.matrixMode(5888);
        this.checkGLError("Startup");
        SplashProgressUtils.setProgress(3, "Minecraft - Startup");
        (this.textureMapBlocks = new TextureMap("textures")).setMipmapLevels(this.gameSettings.mipmapLevels);
        this.renderEngine.loadTickableTexture(TextureMap.LOCATION_BLOCKS_TEXTURE, this.textureMapBlocks);
        this.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        this.textureMapBlocks.setBlurMipmapDirect(false, this.gameSettings.mipmapLevels > 0);
        SplashProgressUtils.setProgress(4, "Minecraft - ModelManager");
        this.modelManager = new ModelManager(this.textureMapBlocks);
        this.mcResourceManager.registerReloadListener(this.modelManager);
        this.blockColors = BlockColors.init();
        this.itemColors = ItemColors.init(this.blockColors);
        this.renderItem = new RenderItem(this.renderEngine, this.modelManager, this.itemColors);
        SplashProgressUtils.setProgress(5, "Minecraft - RenderItem");
        this.renderManager = new RenderManager(this.renderEngine, this.renderItem);
        SplashProgressUtils.setProgress(6, "Minecraft - RenderManager");
        this.itemRenderer = new ItemRenderer(this);
        SplashProgressUtils.setProgress(7, "Minecraft - ItemRender");
        this.mcResourceManager.registerReloadListener(this.renderItem);
        SplashProgressUtils.setProgress(8, "Minecraft - EntityRender");
        this.entityRenderer = new EntityRenderer(this, this.mcResourceManager);
        this.mcResourceManager.registerReloadListener(this.entityRenderer);
        this.blockRenderDispatcher = new BlockRendererDispatcher(this.modelManager.getBlockModelShapes(), this.blockColors);
        SplashProgressUtils.setProgress(9, "Minecraft - BlockRenderDispatcher");
        this.mcResourceManager.registerReloadListener(this.blockRenderDispatcher);
        this.renderGlobal = new RenderGlobal(this);
        SplashProgressUtils.setProgress(10, "Minecraft - RenderGlobal");
        this.mcResourceManager.registerReloadListener(this.renderGlobal);
        this.func_193986_ar();
        this.mcResourceManager.registerReloadListener(this.field_193995_ae);
        GlStateManager.viewport(0, 0, this.displayWidth, this.displayHeight);
        this.effectRenderer = new ParticleManager(this.world, this.renderEngine);
        this.checkGLError("Post startup");
        this.ingameGUI = new GuiIngame(this);
        if (this.serverName != null) {
            this.displayGuiScreen(new GuiConnecting(new GuiMainMenu(), this, this.serverName, this.serverPort));
        }
        else if (!GuiFirstTime.firstTime) {
            this.displayGuiScreen(new GuiFirstTime(null));
        }
        else {
            this.displayGuiScreen(new GuiMainMenu());
        }
        this.renderEngine.deleteTexture(this.mojangLogo);
        this.mojangLogo = null;
        this.loadingScreen = new LoadingScreenRenderer(this);
        this.debugRenderer = new DebugRenderer(this);
        if (this.gameSettings.fullScreen && !this.fullscreen) {
            this.toggleFullscreen();
        }
        try {
            Display.setVSyncEnabled(this.gameSettings.enableVsync);
        }
        catch (final OpenGLException var2) {
            this.gameSettings.enableVsync = false;
            this.gameSettings.saveOptions();
        }
        this.renderGlobal.makeEntityOutlineShader();
        SplashProgressUtils.setProgress(11, "BetterCraft - Loaded");
        final DiscordRPRegistry discordRP = DiscordMain.getInstance().getDiscordRP();
        getMinecraft();
        discordRP.update(getSession().username, "");
        Client.getInstance();
        final StringBuilder append = new StringBuilder(String.valueOf(Client.clientName)).append(" ");
        Client.getInstance();
        Display.setTitle(append.append(Client.clientVersion).append(" | Minecraft 1.12.2").toString());
        DisplayTrayUtils.displayTray(String.valueOf(Client.clientName) + " " + Client.clientVersion + " by " + Client.clientAuthor, "Have fun with this awesome client", TrayIcon.MessageType.INFO);
    }
    
    private void func_193986_ar() {
        final SearchTree<ItemStack> searchtree = new SearchTree<ItemStack>(p_193988_0_ -> p_193988_0_.getTooltip(null, ITooltipFlag.TooltipFlags.NORMAL).stream().map((Function<? super Object, ?>)TextFormatting::getTextWithoutFormattingCodes).map((Function<? super Object, ?>)String::trim).filter(p_193984_0_ -> !p_193984_0_.isEmpty()).collect((Collector<? super Object, ?, List<? super Object>>)Collectors.toList()), p_193985_0_ -> Collections.singleton(Item.REGISTRY.getNameForObject(p_193985_0_.getItem())));
        final NonNullList<ItemStack> nonnulllist = NonNullList.func_191196_a();
        for (final Item item : Item.REGISTRY) {
            item.getSubItems(CreativeTabs.SEARCH, nonnulllist);
        }
        nonnulllist.forEach(searchtree::func_194043_a);
        final SearchTree<RecipeList> searchtree2 = new SearchTree<RecipeList>(p_193990_0_ -> p_193990_0_.func_192711_b().stream().flatMap(p_193993_0_ -> p_193993_0_.getRecipeOutput().getTooltip(null, ITooltipFlag.TooltipFlags.NORMAL).stream()).map((Function<? super Object, ?>)TextFormatting::getTextWithoutFormattingCodes).map((Function<? super Object, ?>)String::trim).filter(p_193994_0_ -> !p_193994_0_.isEmpty()).collect((Collector<? super Object, ?, List<? super Object>>)Collectors.toList()), p_193991_0_ -> p_193991_0_.func_192711_b().stream().map(p_193992_0_ -> Item.REGISTRY.getNameForObject(p_193992_0_.getRecipeOutput().getItem())).collect((Collector<? super Object, ?, List<? super Object>>)Collectors.toList()));
        RecipeBookClient.field_194087_f.forEach(searchtree2::func_194043_a);
        this.field_193995_ae.func_194009_a(SearchTreeManager.field_194011_a, searchtree);
        this.field_193995_ae.func_194009_a(SearchTreeManager.field_194012_b, searchtree2);
    }
    
    private void registerMetadataSerializers() {
        this.metadataSerializer_.registerMetadataSectionType(new TextureMetadataSectionSerializer(), TextureMetadataSection.class);
        this.metadataSerializer_.registerMetadataSectionType(new FontMetadataSectionSerializer(), FontMetadataSection.class);
        this.metadataSerializer_.registerMetadataSectionType(new AnimationMetadataSectionSerializer(), AnimationMetadataSection.class);
        this.metadataSerializer_.registerMetadataSectionType(new PackMetadataSectionSerializer(), PackMetadataSection.class);
        this.metadataSerializer_.registerMetadataSectionType(new LanguageMetadataSectionSerializer(), LanguageMetadataSection.class);
    }
    
    private void createDisplay() throws LWJGLException {
        Display.setResizable(true);
        Display.setTitle("Loading...");
        try {
            Display.create(new PixelFormat().withDepthBits(24));
        }
        catch (final LWJGLException lwjglexception) {
            Minecraft.LOGGER.error("Couldn't set pixel format", lwjglexception);
            try {
                Thread.sleep(1000L);
            }
            catch (final InterruptedException ex) {}
            if (this.fullscreen) {
                this.updateDisplayMode();
            }
            Display.create();
        }
    }
    
    private void setInitialDisplayMode() throws LWJGLException {
        if (this.fullscreen) {
            Display.setFullscreen(true);
            final DisplayMode displaymode = Display.getDisplayMode();
            this.displayWidth = Math.max(1, displaymode.getWidth());
            this.displayHeight = Math.max(1, displaymode.getHeight());
        }
        else {
            Display.setDisplayMode(new DisplayMode(this.displayWidth, this.displayHeight));
        }
    }
    
    private void setWindowIcon() {
        if (Util.getOSType() != Util.EnumOS.OSX) {
            setIcon("textures/icons/icon_16x16.png", "textures/icons/icon_32x32.png");
        }
    }
    
    public static void setIcon(final String icon_16, final String icon_32) {
        InputStream inputstream = null;
        InputStream inputstream2 = null;
        try {
            inputstream = getMinecraft().mcDefaultResourcePack.getInputStream(new ResourceLocation(icon_16));
            inputstream2 = getMinecraft().mcDefaultResourcePack.getInputStream(new ResourceLocation(icon_32));
            if (inputstream != null && inputstream2 != null) {
                Display.setIcon(new ByteBuffer[] { getMinecraft().readImageToBuffer(inputstream), getMinecraft().readImageToBuffer(inputstream2) });
            }
        }
        catch (final IOException ioexception) {
            Minecraft.LOGGER.error("Couldn't set icon", ioexception);
            return;
        }
        finally {
            IOUtils.closeQuietly(inputstream);
            IOUtils.closeQuietly(inputstream2);
        }
        IOUtils.closeQuietly(inputstream);
        IOUtils.closeQuietly(inputstream2);
    }
    
    private static boolean isJvm64bit() {
        final String[] astring = { "sun.arch.data.model", "com.ibm.vm.bitmode", "os.arch" };
        String[] array;
        for (int length = (array = astring).length, i = 0; i < length; ++i) {
            final String s = array[i];
            final String s2 = System.getProperty(s);
            if (s2 != null && s2.contains("64")) {
                return true;
            }
        }
        return false;
    }
    
    public Framebuffer getFramebuffer() {
        return this.framebufferMc;
    }
    
    public String getVersion() {
        return this.launchedVersion;
    }
    
    public String getVersionType() {
        return this.versionType;
    }
    
    private void startTimerHackThread() {
        final Thread thread = new Thread("Timer hack thread") {
            @Override
            public void run() {
                while (Minecraft.this.running) {
                    try {
                        Thread.sleep(2147483647L);
                    }
                    catch (final InterruptedException ex) {}
                }
            }
        };
        thread.setDaemon(true);
        thread.start();
    }
    
    public void crashed(final CrashReport crash) {
        this.hasCrashed = true;
        this.crashReporter = crash;
    }
    
    public void displayCrashReport(final CrashReport crashReportIn) {
        final File file1 = new File(getMinecraft().mcDataDir, "crash-reports");
        final File file2 = new File(file1, "crash-" + new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss").format(new Date()) + "-client.txt");
        Bootstrap.printToSYSOUT(crashReportIn.getCompleteReport());
        if (crashReportIn.getFile() != null) {
            Bootstrap.printToSYSOUT("#@!@# Game crashed! Crash report saved to: #@!@# " + crashReportIn.getFile());
            System.exit(-1);
        }
        else if (crashReportIn.saveToFile(file2)) {
            Bootstrap.printToSYSOUT("#@!@# Game crashed! Crash report saved to: #@!@# " + file2.getAbsolutePath());
            System.exit(-1);
        }
        else {
            Bootstrap.printToSYSOUT("#@?@# Game crashed! Crash report could not be saved. #@?@#");
            System.exit(-2);
        }
    }
    
    public boolean isUnicode() {
        return this.mcLanguageManager.isCurrentLocaleUnicode() || this.gameSettings.forceUnicodeFont;
    }
    
    public void refreshResources() {
        final List<IResourcePack> list = (List<IResourcePack>)Lists.newArrayList((Iterable<?>)this.defaultResourcePacks);
        if (this.theIntegratedServer != null) {
            this.theIntegratedServer.func_193031_aM();
        }
        for (final ResourcePackRepository.Entry resourcepackrepository$entry : this.mcResourcePackRepository.getRepositoryEntries()) {
            list.add(resourcepackrepository$entry.getResourcePack());
        }
        if (this.mcResourcePackRepository.getResourcePackInstance() != null) {
            list.add(this.mcResourcePackRepository.getResourcePackInstance());
        }
        try {
            this.mcResourceManager.reloadResources(list);
        }
        catch (final RuntimeException runtimeexception) {
            Minecraft.LOGGER.info("Caught error stitching, removing all assigned resourcepacks", runtimeexception);
            list.clear();
            list.addAll(this.defaultResourcePacks);
            this.mcResourcePackRepository.setRepositories(Collections.emptyList());
            this.mcResourceManager.reloadResources(list);
            this.gameSettings.resourcePacks.clear();
            this.gameSettings.incompatibleResourcePacks.clear();
            this.gameSettings.saveOptions();
        }
        this.mcLanguageManager.parseLanguageMetadata(list);
        if (this.renderGlobal != null) {
            this.renderGlobal.loadRenderers();
        }
    }
    
    private ByteBuffer readImageToBuffer(final InputStream imageStream) throws IOException {
        final BufferedImage bufferedimage = ImageIO.read(imageStream);
        final int[] aint = bufferedimage.getRGB(0, 0, bufferedimage.getWidth(), bufferedimage.getHeight(), null, 0, bufferedimage.getWidth());
        final ByteBuffer bytebuffer = ByteBuffer.allocate(4 * aint.length);
        int[] array;
        for (int length = (array = aint).length, j = 0; j < length; ++j) {
            final int i = array[j];
            bytebuffer.putInt(i << 8 | (i >> 24 & 0xFF));
        }
        bytebuffer.flip();
        return bytebuffer;
    }
    
    private void updateDisplayMode() throws LWJGLException {
        final Set<DisplayMode> set = (Set<DisplayMode>)Sets.newHashSet();
        Collections.addAll(set, Display.getAvailableDisplayModes());
        DisplayMode displaymode = Display.getDesktopDisplayMode();
        if (!set.contains(displaymode) && Util.getOSType() == Util.EnumOS.OSX) {
            for (final DisplayMode displaymode2 : Minecraft.MAC_DISPLAY_MODES) {
                boolean flag = true;
                for (final DisplayMode displaymode3 : set) {
                    if (displaymode3.getBitsPerPixel() == 32 && displaymode3.getWidth() == displaymode2.getWidth() && displaymode3.getHeight() == displaymode2.getHeight()) {
                        flag = false;
                        break;
                    }
                }
                if (!flag) {
                    for (final DisplayMode displaymode4 : set) {
                        if (displaymode4.getBitsPerPixel() == 32 && displaymode4.getWidth() == displaymode2.getWidth() / 2 && displaymode4.getHeight() == displaymode2.getHeight() / 2) {
                            displaymode = displaymode4;
                            break;
                        }
                    }
                }
            }
        }
        Display.setDisplayMode(displaymode);
        this.displayWidth = displaymode.getWidth();
        this.displayHeight = displaymode.getHeight();
    }
    
    private void drawSplashScreen(final TextureManager textureManagerInstance) throws LWJGLException {
        final ScaledResolution scaledresolution = new ScaledResolution(this);
        final int i = scaledresolution.getScaleFactor();
        final Framebuffer framebuffer = new Framebuffer(ScaledResolution.getScaledWidth() * i, ScaledResolution.getScaledHeight() * i, true);
        framebuffer.bindFramebuffer(false);
        GlStateManager.matrixMode(5889);
        GlStateManager.loadIdentity();
        GlStateManager.ortho(0.0, ScaledResolution.getScaledWidth(), ScaledResolution.getScaledHeight(), 0.0, 1000.0, 3000.0);
        GlStateManager.matrixMode(5888);
        GlStateManager.loadIdentity();
        GlStateManager.translate(0.0f, 0.0f, -2000.0f);
        GlStateManager.disableLighting();
        GlStateManager.disableFog();
        GlStateManager.disableDepth();
        GlStateManager.enableTexture2D();
        InputStream inputstream = null;
        Label_0187: {
            try {
                inputstream = this.mcDefaultResourcePack.getInputStream(Minecraft.LOCATION_MOJANG_PNG);
                textureManagerInstance.bindTexture(this.mojangLogo = textureManagerInstance.getDynamicTextureLocation("logo", new DynamicTexture(ImageIO.read(inputstream))));
            }
            catch (final IOException ioexception) {
                Minecraft.LOGGER.error("Unable to load logo: {}", Minecraft.LOCATION_MOJANG_PNG, ioexception);
                break Label_0187;
            }
            finally {
                IOUtils.closeQuietly(inputstream);
            }
            IOUtils.closeQuietly(inputstream);
        }
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        bufferbuilder.pos(0.0, this.displayHeight, 0.0).tex(0.0, 0.0).color(255, 255, 255, 255).endVertex();
        bufferbuilder.pos(this.displayWidth, this.displayHeight, 0.0).tex(0.0, 0.0).color(255, 255, 255, 255).endVertex();
        bufferbuilder.pos(this.displayWidth, 0.0, 0.0).tex(0.0, 0.0).color(255, 255, 255, 255).endVertex();
        bufferbuilder.pos(0.0, 0.0, 0.0).tex(0.0, 0.0).color(255, 255, 255, 255).endVertex();
        tessellator.draw();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        final int j = 256;
        final int k = 256;
        this.draw((ScaledResolution.getScaledWidth() - 256) / 2, (ScaledResolution.getScaledHeight() - 256) / 2, 0, 0, 256, 256, 255, 255, 255, 255);
        GlStateManager.disableLighting();
        GlStateManager.disableFog();
        framebuffer.unbindFramebuffer();
        framebuffer.framebufferRender(ScaledResolution.getScaledWidth() * i, ScaledResolution.getScaledHeight() * i);
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.1f);
        this.updateDisplay();
    }
    
    public void draw(final int posX, final int posY, final int texU, final int texV, final int width, final int height, final int red, final int green, final int blue, final int alpha) {
        final BufferBuilder bufferbuilder = Tessellator.getInstance().getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        final float f = 0.00390625f;
        final float f2 = 0.00390625f;
        bufferbuilder.pos(posX, posY + height, 0.0).tex(texU * 0.00390625f, (texV + height) * 0.00390625f).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(posX + width, posY + height, 0.0).tex((texU + width) * 0.00390625f, (texV + height) * 0.00390625f).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(posX + width, posY, 0.0).tex((texU + width) * 0.00390625f, texV * 0.00390625f).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(posX, posY, 0.0).tex(texU * 0.00390625f, texV * 0.00390625f).color(red, green, blue, alpha).endVertex();
        Tessellator.getInstance().draw();
    }
    
    public ISaveFormat getSaveLoader() {
        return this.saveLoader;
    }
    
    public void displayGuiScreen(@Nullable GuiScreen guiScreenIn) {
        if (Minecraft.currentScreen != null) {
            Minecraft.currentScreen.onGuiClosed();
        }
        if (guiScreenIn == null && this.world == null) {
            guiScreenIn = new GuiMainMenu();
        }
        else if (guiScreenIn == null && this.player.getHealth() <= 0.0f) {
            guiScreenIn = new GuiGameOver(null);
        }
        if (guiScreenIn instanceof GuiMainMenu || guiScreenIn instanceof GuiMultiplayer) {
            this.gameSettings.showDebugInfo = false;
            this.ingameGUI.getChatGUI().clearChatMessages(true);
        }
        if ((Minecraft.currentScreen = guiScreenIn) != null) {
            this.setIngameNotInFocus();
            KeyBinding.unPressAllKeys();
            while (Mouse.next()) {}
            while (Keyboard.next()) {}
            final ScaledResolution scaledresolution = new ScaledResolution(this);
            final int i = ScaledResolution.getScaledWidth();
            final int j = ScaledResolution.getScaledHeight();
            guiScreenIn.setWorldAndResolution(this, i, j);
            this.skipRenderWorld = false;
        }
        else {
            this.mcSoundHandler.resumeSounds();
            this.setIngameFocus();
        }
    }
    
    private void checkGLError(final String message) {
        final int i = GlStateManager.glGetError();
        if (i != 0) {
            final String s = GLU.gluErrorString(i);
            Minecraft.LOGGER.error("########## GL ERROR ##########");
            Minecraft.LOGGER.error("@ {}", message);
            Minecraft.LOGGER.error("{}: {}", (Object)i, s);
        }
    }
    
    public void shutdownMinecraftApplet() {
        Client.getInstance().onDisable();
        try {
            Minecraft.LOGGER.info("Stopping!");
            try {
                this.loadWorld(null);
            }
            catch (final Throwable t) {}
            this.mcSoundHandler.unloadSounds();
        }
        finally {
            Display.destroy();
            if (!this.hasCrashed) {
                System.exit(0);
            }
        }
        Display.destroy();
        if (!this.hasCrashed) {
            System.exit(0);
        }
        System.gc();
    }
    
    private void runGameLoop() throws IOException {
        EventManager.call(new GameLoopEvent());
        final long i = System.nanoTime();
        this.mcProfiler.startSection("root");
        if (Display.isCreated() && Display.isCloseRequested()) {
            this.shutdown();
        }
        this.timer.updateTimer();
        this.mcProfiler.startSection("scheduledExecutables");
        synchronized (this.scheduledTasks) {
            while (!this.scheduledTasks.isEmpty()) {
                Util.runTask(this.scheduledTasks.poll(), Minecraft.LOGGER);
            }
            monitorexit(this.scheduledTasks);
        }
        this.mcProfiler.endSection();
        final long l = System.nanoTime();
        this.mcProfiler.startSection("tick");
        for (int j = 0; j < Math.min(10, this.timer.elapsedTicks); ++j) {
            this.runTick();
        }
        this.mcProfiler.endStartSection("preRenderErrors");
        final long i2 = System.nanoTime() - l;
        this.checkGLError("Pre render");
        this.mcProfiler.endStartSection("sound");
        this.mcSoundHandler.setListener(this.player, this.timer.field_194147_b);
        this.mcProfiler.endSection();
        this.mcProfiler.startSection("render");
        GlStateManager.pushMatrix();
        GlStateManager.clear(16640);
        this.framebufferMc.bindFramebuffer(true);
        this.mcProfiler.startSection("display");
        GlStateManager.enableTexture2D();
        this.mcProfiler.endSection();
        if (!this.skipRenderWorld) {
            this.mcProfiler.endStartSection("gameRenderer");
            this.entityRenderer.updateCameraAndRender(this.isGamePaused ? this.field_193996_ah : this.timer.field_194147_b, i);
            this.mcProfiler.endStartSection("toasts");
            this.field_193034_aS.func_191783_a(new ScaledResolution(this));
            this.mcProfiler.endSection();
        }
        this.mcProfiler.endSection();
        if (this.gameSettings.showDebugInfo && this.gameSettings.showDebugProfilerChart && !this.gameSettings.hideGUI) {
            if (!this.mcProfiler.profilingEnabled) {
                this.mcProfiler.clearProfiling();
            }
            this.mcProfiler.profilingEnabled = true;
            this.displayDebugInfo(i2);
        }
        else {
            this.mcProfiler.profilingEnabled = false;
            this.prevFrameTime = System.nanoTime();
        }
        this.framebufferMc.unbindFramebuffer();
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        this.framebufferMc.framebufferRender(this.displayWidth, this.displayHeight);
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        this.entityRenderer.renderStreamIndicator(this.timer.field_194147_b);
        GlStateManager.popMatrix();
        this.mcProfiler.startSection("root");
        this.updateDisplay();
        Thread.yield();
        this.checkGLError("Post render");
        ++this.fpsCounter;
        final boolean flag = this.isSingleplayer() && Minecraft.currentScreen != null && Minecraft.currentScreen.doesGuiPauseGame() && !this.theIntegratedServer.getPublic();
        if (this.isGamePaused != flag) {
            if (this.isGamePaused) {
                this.field_193996_ah = this.timer.field_194147_b;
            }
            else {
                this.timer.field_194147_b = this.field_193996_ah;
            }
            this.isGamePaused = flag;
        }
        final long k = System.nanoTime();
        this.frameTimer.addFrame(k - this.startNanoTime);
        this.startNanoTime = k;
        while (getSystemTime() >= this.debugUpdateTime + 1000L) {
            Minecraft.debugFPS = this.fpsCounter;
            this.debug = String.format("%d fps (%d chunk update%s) T: %s%s%s%s%s", Minecraft.debugFPS, RenderChunk.renderChunksUpdated, (RenderChunk.renderChunksUpdated == 1) ? "" : "s", (this.gameSettings.limitFramerate == GameSettings.Options.FRAMERATE_LIMIT.getValueMax()) ? "inf" : Integer.valueOf(this.gameSettings.limitFramerate), this.gameSettings.enableVsync ? " vsync" : "", this.gameSettings.fancyGraphics ? "" : " fast", (this.gameSettings.clouds == 0) ? "" : ((this.gameSettings.clouds == 1) ? " fast-clouds" : " fancy-clouds"), OpenGlHelper.useVbo() ? " vbo" : "");
            RenderChunk.renderChunksUpdated = 0;
            this.debugUpdateTime += 1000L;
            this.fpsCounter = 0;
            this.usageSnooper.addMemoryStatsToSnooper();
            if (!this.usageSnooper.isSnooperRunning()) {
                this.usageSnooper.startSnooper();
            }
        }
        if (this.isFramerateLimitBelowMax()) {
            this.mcProfiler.startSection("fpslimit_wait");
            Display.sync(this.getLimitFramerate());
            this.mcProfiler.endSection();
        }
        this.mcProfiler.endSection();
    }
    
    public void updateDisplay() {
        this.mcProfiler.startSection("display_update");
        Display.update();
        this.mcProfiler.endSection();
        this.checkWindowResize();
    }
    
    protected void checkWindowResize() {
        if (!this.fullscreen && Display.wasResized()) {
            final int i = this.displayWidth;
            final int j = this.displayHeight;
            this.displayWidth = Display.getWidth();
            this.displayHeight = Display.getHeight();
            if (this.displayWidth != i || this.displayHeight != j) {
                if (this.displayWidth <= 0) {
                    this.displayWidth = 1;
                }
                if (this.displayHeight <= 0) {
                    this.displayHeight = 1;
                }
                this.resize(this.displayWidth, this.displayHeight);
            }
        }
    }
    
    public int getLimitFramerate() {
        return (this.world == null && Minecraft.currentScreen != null) ? 30 : this.gameSettings.limitFramerate;
    }
    
    public boolean isFramerateLimitBelowMax() {
        return this.getLimitFramerate() < GameSettings.Options.FRAMERATE_LIMIT.getValueMax();
    }
    
    public void freeMemory() {
        try {
            Minecraft.memoryReserve = new byte[0];
            this.renderGlobal.deleteAllDisplayLists();
        }
        catch (final Throwable t) {}
        try {
            System.gc();
            this.loadWorld(null);
        }
        catch (final Throwable t2) {}
        System.gc();
    }
    
    private void updateDebugProfilerName(int keyCount) {
        final List<Profiler.Result> list = this.mcProfiler.getProfilingData(this.debugProfilerName);
        if (!list.isEmpty()) {
            final Profiler.Result profiler$result = list.remove(0);
            if (keyCount == 0) {
                if (!profiler$result.profilerName.isEmpty()) {
                    final int i = this.debugProfilerName.lastIndexOf(46);
                    if (i >= 0) {
                        this.debugProfilerName = this.debugProfilerName.substring(0, i);
                    }
                }
            }
            else if (--keyCount < list.size() && !"unspecified".equals(list.get(keyCount).profilerName)) {
                if (!this.debugProfilerName.isEmpty()) {
                    this.debugProfilerName = String.valueOf(this.debugProfilerName) + ".";
                }
                this.debugProfilerName = String.valueOf(this.debugProfilerName) + list.get(keyCount).profilerName;
            }
        }
    }
    
    private void displayDebugInfo(final long elapsedTicksTime) {
        if (this.mcProfiler.profilingEnabled) {
            final List<Profiler.Result> list = this.mcProfiler.getProfilingData(this.debugProfilerName);
            final Profiler.Result profiler$result = list.remove(0);
            GlStateManager.clear(256);
            GlStateManager.matrixMode(5889);
            GlStateManager.enableColorMaterial();
            GlStateManager.loadIdentity();
            GlStateManager.ortho(0.0, this.displayWidth, this.displayHeight, 0.0, 1000.0, 3000.0);
            GlStateManager.matrixMode(5888);
            GlStateManager.loadIdentity();
            GlStateManager.translate(0.0f, 0.0f, -2000.0f);
            GlStateManager.glLineWidth(1.0f);
            GlStateManager.disableTexture2D();
            final Tessellator tessellator = Tessellator.getInstance();
            final BufferBuilder bufferbuilder = tessellator.getBuffer();
            final int i = 160;
            final int j = this.displayWidth - 160 - 10;
            final int k = this.displayHeight - 320;
            GlStateManager.enableBlend();
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
            bufferbuilder.pos(j - 176.0f, k - 96.0f - 16.0f, 0.0).color(200, 0, 0, 0).endVertex();
            bufferbuilder.pos(j - 176.0f, k + 320, 0.0).color(200, 0, 0, 0).endVertex();
            bufferbuilder.pos(j + 176.0f, k + 320, 0.0).color(200, 0, 0, 0).endVertex();
            bufferbuilder.pos(j + 176.0f, k - 96.0f - 16.0f, 0.0).color(200, 0, 0, 0).endVertex();
            tessellator.draw();
            GlStateManager.disableBlend();
            double d0 = 0.0;
            for (int l = 0; l < list.size(); ++l) {
                final Profiler.Result profiler$result2 = list.get(l);
                final int i2 = MathHelper.floor(profiler$result2.usePercentage / 4.0) + 1;
                bufferbuilder.begin(6, DefaultVertexFormats.POSITION_COLOR);
                final int j2 = profiler$result2.getColor();
                final int k2 = j2 >> 16 & 0xFF;
                final int l2 = j2 >> 8 & 0xFF;
                final int i3 = j2 & 0xFF;
                bufferbuilder.pos(j, k, 0.0).color(k2, l2, i3, 255).endVertex();
                for (int j3 = i2; j3 >= 0; --j3) {
                    final float f = (float)((d0 + profiler$result2.usePercentage * j3 / i2) * 6.283185307179586 / 100.0);
                    final float f2 = MathHelper.sin(f) * 160.0f;
                    final float f3 = MathHelper.cos(f) * 160.0f * 0.5f;
                    bufferbuilder.pos(j + f2, k - f3, 0.0).color(k2, l2, i3, 255).endVertex();
                }
                tessellator.draw();
                bufferbuilder.begin(5, DefaultVertexFormats.POSITION_COLOR);
                for (int i4 = i2; i4 >= 0; --i4) {
                    final float f4 = (float)((d0 + profiler$result2.usePercentage * i4 / i2) * 6.283185307179586 / 100.0);
                    final float f5 = MathHelper.sin(f4) * 160.0f;
                    final float f6 = MathHelper.cos(f4) * 160.0f * 0.5f;
                    bufferbuilder.pos(j + f5, k - f6, 0.0).color(k2 >> 1, l2 >> 1, i3 >> 1, 255).endVertex();
                    bufferbuilder.pos(j + f5, k - f6 + 10.0f, 0.0).color(k2 >> 1, l2 >> 1, i3 >> 1, 255).endVertex();
                }
                tessellator.draw();
                d0 += profiler$result2.usePercentage;
            }
            final DecimalFormat decimalformat = new DecimalFormat("##0.00");
            GlStateManager.enableTexture2D();
            String s = "";
            if (!"unspecified".equals(profiler$result.profilerName)) {
                s = String.valueOf(s) + "[0] ";
            }
            if (profiler$result.profilerName.isEmpty()) {
                s = String.valueOf(s) + "ROOT ";
            }
            else {
                s = String.valueOf(s) + profiler$result.profilerName + ' ';
            }
            final int l3 = 16777215;
            this.fontRendererObj.drawStringWithShadow(s, (float)(j - 160), (float)(k - 80 - 16), 16777215);
            s = String.valueOf(decimalformat.format(profiler$result.totalUsePercentage)) + "%";
            this.fontRendererObj.drawStringWithShadow(s, (float)(j + 160 - this.fontRendererObj.getStringWidth(s)), (float)(k - 80 - 16), 16777215);
            for (int k3 = 0; k3 < list.size(); ++k3) {
                final Profiler.Result profiler$result3 = list.get(k3);
                final StringBuilder stringbuilder = new StringBuilder();
                if ("unspecified".equals(profiler$result3.profilerName)) {
                    stringbuilder.append("[?] ");
                }
                else {
                    stringbuilder.append("[").append(k3 + 1).append("] ");
                }
                String s2 = stringbuilder.append(profiler$result3.profilerName).toString();
                this.fontRendererObj.drawStringWithShadow(s2, (float)(j - 160), (float)(k + 80 + k3 * 8 + 20), profiler$result3.getColor());
                s2 = String.valueOf(decimalformat.format(profiler$result3.usePercentage)) + "%";
                this.fontRendererObj.drawStringWithShadow(s2, (float)(j + 160 - 50 - this.fontRendererObj.getStringWidth(s2)), (float)(k + 80 + k3 * 8 + 20), profiler$result3.getColor());
                s2 = String.valueOf(decimalformat.format(profiler$result3.totalUsePercentage)) + "%";
                this.fontRendererObj.drawStringWithShadow(s2, (float)(j + 160 - this.fontRendererObj.getStringWidth(s2)), (float)(k + 80 + k3 * 8 + 20), profiler$result3.getColor());
            }
        }
    }
    
    public void shutdown() {
        this.running = false;
    }
    
    public void setIngameFocus() {
        if (Display.isActive() && !this.inGameHasFocus) {
            if (!Minecraft.IS_RUNNING_ON_MAC) {
                KeyBinding.updateKeyBindState();
            }
            this.inGameHasFocus = true;
            this.mouseHelper.grabMouseCursor();
            this.displayGuiScreen(null);
            this.leftClickCounter = 10000;
        }
    }
    
    public void setIngameNotInFocus() {
        if (this.inGameHasFocus) {
            this.inGameHasFocus = false;
            this.mouseHelper.ungrabMouseCursor();
        }
    }
    
    public void displayInGameMenu() {
        if (Minecraft.currentScreen == null) {
            if (this.isSingleplayer()) {
                this.displayGuiScreen(new GuiIngameMenuSinglePlayer());
            }
            else {
                this.displayGuiScreen(new GuiIngameMenu());
            }
            if (this.isSingleplayer() && !this.theIntegratedServer.getPublic()) {
                this.mcSoundHandler.pauseSounds();
            }
        }
    }
    
    private void sendClickBlockToController(final boolean leftClick) {
        if (!leftClick) {
            this.leftClickCounter = 0;
        }
        if (this.leftClickCounter <= 0 && !this.player.isHandActive()) {
            if (leftClick && this.objectMouseOver != null && this.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK) {
                final BlockPos blockpos = this.objectMouseOver.getBlockPos();
                if (this.world.getBlockState(blockpos).getMaterial() != Material.AIR && this.playerController.onPlayerDamageBlock(blockpos, this.objectMouseOver.sideHit)) {
                    this.effectRenderer.addBlockHitEffects(blockpos, this.objectMouseOver.sideHit);
                    this.player.swingArm(EnumHand.MAIN_HAND);
                }
            }
            else {
                this.playerController.resetBlockRemoving();
            }
        }
    }
    
    private void clickMouse() {
        if (this.leftClickCounter <= 0) {
            if (this.objectMouseOver == null) {
                Minecraft.LOGGER.error("Null returned as 'hitResult', this shouldn't happen!");
                if (this.playerController.isNotCreative()) {
                    this.leftClickCounter = 10;
                }
            }
            else if (!this.player.isRowingBoat()) {
                switch (this.objectMouseOver.typeOfHit) {
                    case ENTITY: {
                        this.playerController.attackEntity(this.player, this.objectMouseOver.entityHit);
                        break;
                    }
                    case BLOCK: {
                        final BlockPos blockpos = this.objectMouseOver.getBlockPos();
                        if (this.world.getBlockState(blockpos).getMaterial() != Material.AIR) {
                            this.playerController.clickBlock(blockpos, this.objectMouseOver.sideHit);
                            break;
                        }
                    }
                    case MISS: {
                        if (this.playerController.isNotCreative()) {
                            this.leftClickCounter = 10;
                        }
                        this.player.resetCooldown();
                        break;
                    }
                }
                this.player.swingArm(EnumHand.MAIN_HAND);
            }
        }
    }
    
    private void rightClickMouse() {
        if (!this.playerController.getIsHittingBlock()) {
            this.rightClickDelayTimer = 4;
            if (!this.player.isRowingBoat()) {
                if (this.objectMouseOver == null) {
                    Minecraft.LOGGER.warn("Null returned as 'hitResult', this shouldn't happen!");
                }
                EnumHand[] values;
                for (int length = (values = EnumHand.values()).length, j = 0; j < length; ++j) {
                    final EnumHand enumhand = values[j];
                    final ItemStack itemstack = this.player.getHeldItem(enumhand);
                    if (this.objectMouseOver != null) {
                        switch (this.objectMouseOver.typeOfHit) {
                            case ENTITY: {
                                if (this.playerController.interactWithEntity(this.player, this.objectMouseOver.entityHit, this.objectMouseOver, enumhand) == EnumActionResult.SUCCESS) {
                                    return;
                                }
                                if (this.playerController.interactWithEntity(this.player, this.objectMouseOver.entityHit, enumhand) == EnumActionResult.SUCCESS) {
                                    return;
                                }
                                break;
                            }
                            case BLOCK: {
                                final BlockPos blockpos = this.objectMouseOver.getBlockPos();
                                if (this.world.getBlockState(blockpos).getMaterial() == Material.AIR) {
                                    break;
                                }
                                final int i = itemstack.func_190916_E();
                                final EnumActionResult enumactionresult = this.playerController.processRightClickBlock(this.player, this.world, blockpos, this.objectMouseOver.sideHit, this.objectMouseOver.hitVec, enumhand);
                                if (enumactionresult == EnumActionResult.SUCCESS) {
                                    this.player.swingArm(enumhand);
                                    if (!itemstack.func_190926_b() && (itemstack.func_190916_E() != i || this.playerController.isInCreativeMode())) {
                                        this.entityRenderer.itemRenderer.resetEquippedProgress(enumhand);
                                    }
                                    return;
                                }
                                break;
                            }
                        }
                    }
                    if (!itemstack.func_190926_b() && this.playerController.processRightClick(this.player, this.world, enumhand) == EnumActionResult.SUCCESS) {
                        this.entityRenderer.itemRenderer.resetEquippedProgress(enumhand);
                        return;
                    }
                }
            }
        }
    }
    
    public void toggleFullscreen() {
        try {
            this.fullscreen = !this.fullscreen;
            this.gameSettings.fullScreen = this.fullscreen;
            if (this.fullscreen) {
                this.updateDisplayMode();
                this.displayWidth = Display.getDisplayMode().getWidth();
                this.displayHeight = Display.getDisplayMode().getHeight();
                if (this.displayWidth <= 0) {
                    this.displayWidth = 1;
                }
                if (this.displayHeight <= 0) {
                    this.displayHeight = 1;
                }
            }
            else {
                Display.setDisplayMode(new DisplayMode(this.tempDisplayWidth, this.tempDisplayHeight));
                this.displayWidth = this.tempDisplayWidth;
                this.displayHeight = this.tempDisplayHeight;
                if (this.displayWidth <= 0) {
                    this.displayWidth = 1;
                }
                if (this.displayHeight <= 0) {
                    this.displayHeight = 1;
                }
            }
            if (Minecraft.currentScreen != null) {
                this.resize(this.displayWidth, this.displayHeight);
            }
            else {
                this.updateFramebufferSize();
            }
            Display.setFullscreen(this.fullscreen);
            Display.setVSyncEnabled(this.gameSettings.enableVsync);
            this.updateDisplay();
        }
        catch (final Exception exception) {
            Minecraft.LOGGER.error("Couldn't toggle fullscreen", exception);
        }
    }
    
    private void resize(final int width, final int height) {
        this.displayWidth = Math.max(1, width);
        this.displayHeight = Math.max(1, height);
        if (Minecraft.currentScreen != null) {
            final ScaledResolution scaledresolution = new ScaledResolution(this);
            Minecraft.currentScreen.onResize(this, ScaledResolution.getScaledWidth(), ScaledResolution.getScaledHeight());
        }
        this.loadingScreen = new LoadingScreenRenderer(this);
        this.updateFramebufferSize();
    }
    
    private void updateFramebufferSize() {
        this.framebufferMc.createBindFramebuffer(this.displayWidth, this.displayHeight);
        if (this.entityRenderer != null) {
            this.entityRenderer.updateShaderGroupSize(this.displayWidth, this.displayHeight);
        }
    }
    
    public MusicTicker getMusicTicker() {
        return this.mcMusicTicker;
    }
    
    public void runTick() throws IOException {
        if (GuiCosmetics.skinDerpCosmetic) {
            CosmeticSkinDerp.tick();
        }
        if (this.rightClickDelayTimer > 0) {
            --this.rightClickDelayTimer;
        }
        this.mcProfiler.startSection("gui");
        if (!this.isGamePaused) {
            this.ingameGUI.updateTick();
        }
        this.mcProfiler.endSection();
        this.entityRenderer.getMouseOver(1.0f);
        this.field_193035_aW.func_193297_a(this.world, this.objectMouseOver);
        this.mcProfiler.startSection("gameMode");
        if (!this.isGamePaused && this.world != null) {
            this.playerController.updateController();
        }
        this.mcProfiler.endStartSection("textures");
        if (this.world != null) {
            this.renderEngine.tick();
        }
        if (Minecraft.currentScreen == null && this.player != null) {
            if (this.player.getHealth() <= 0.0f && !(Minecraft.currentScreen instanceof GuiGameOver)) {
                this.displayGuiScreen(null);
            }
            else if (this.player.isPlayerSleeping() && this.world != null) {
                this.displayGuiScreen(new GuiSleepMP());
            }
        }
        else if (Minecraft.currentScreen != null && Minecraft.currentScreen instanceof GuiSleepMP && !this.player.isPlayerSleeping()) {
            this.displayGuiScreen(null);
        }
        if (Minecraft.currentScreen != null) {
            this.leftClickCounter = 10000;
        }
        if (Minecraft.currentScreen != null) {
            try {
                Minecraft.currentScreen.handleInput();
            }
            catch (final Throwable throwable1) {
                final CrashReport crashreport = CrashReport.makeCrashReport(throwable1, "Updating screen events");
                final CrashReportCategory crashreportcategory = crashreport.makeCategory("Affected screen");
                crashreportcategory.setDetail("Screen name", new ICrashReportDetail<String>() {
                    @Override
                    public String call() throws Exception {
                        return Minecraft.currentScreen.getClass().getCanonicalName();
                    }
                });
                throw new ReportedException(crashreport);
            }
            if (Minecraft.currentScreen != null) {
                try {
                    Minecraft.currentScreen.updateScreen();
                }
                catch (final Throwable throwable2) {
                    final CrashReport crashreport2 = CrashReport.makeCrashReport(throwable2, "Ticking screen");
                    final CrashReportCategory crashreportcategory2 = crashreport2.makeCategory("Affected screen");
                    crashreportcategory2.setDetail("Screen name", new ICrashReportDetail<String>() {
                        @Override
                        public String call() throws Exception {
                            return Minecraft.currentScreen.getClass().getCanonicalName();
                        }
                    });
                    throw new ReportedException(crashreport2);
                }
            }
        }
        if (Minecraft.currentScreen == null || Minecraft.currentScreen.allowUserInput) {
            this.mcProfiler.endStartSection("mouse");
            this.runTickMouse();
            if (this.leftClickCounter > 0) {
                --this.leftClickCounter;
            }
            this.mcProfiler.endStartSection("keyboard");
            this.runTickKeyboard();
        }
        if (this.world != null) {
            if (this.player != null) {
                ++this.joinPlayerCounter;
                if (this.joinPlayerCounter == 30) {
                    this.joinPlayerCounter = 0;
                    this.world.joinEntityInSurroundings(this.player);
                }
            }
            this.mcProfiler.endStartSection("gameRenderer");
            if (!this.isGamePaused) {
                this.entityRenderer.updateRenderer();
            }
            this.mcProfiler.endStartSection("levelRenderer");
            if (!this.isGamePaused) {
                this.renderGlobal.updateClouds();
            }
            this.mcProfiler.endStartSection("level");
            if (!this.isGamePaused) {
                if (this.world.getLastLightningBolt() > 0) {
                    this.world.setLastLightningBolt(this.world.getLastLightningBolt() - 1);
                }
                this.world.updateEntities();
            }
        }
        else if (this.entityRenderer.isShaderActive()) {
            this.entityRenderer.stopUseShader();
        }
        if (!this.isGamePaused) {
            this.mcMusicTicker.update();
            this.mcSoundHandler.update();
        }
        if (this.world != null) {
            if (!this.isGamePaused) {
                this.world.setAllowedSpawnTypes(this.world.getDifficulty() != EnumDifficulty.PEACEFUL, true);
                this.field_193035_aW.func_193303_d();
                try {
                    this.world.tick();
                }
                catch (final Throwable throwable3) {
                    final CrashReport crashreport3 = CrashReport.makeCrashReport(throwable3, "Exception in world tick");
                    if (this.world == null) {
                        final CrashReportCategory crashreportcategory3 = crashreport3.makeCategory("Affected level");
                        crashreportcategory3.addCrashSection("Problem", "Level is null!");
                    }
                    else {
                        this.world.addWorldInfoToCrashReport(crashreport3);
                    }
                    throw new ReportedException(crashreport3);
                }
            }
            this.mcProfiler.endStartSection("animateTick");
            if (!this.isGamePaused && this.world != null) {
                this.world.doVoidFogParticles(MathHelper.floor(this.player.posX), MathHelper.floor(this.player.posY), MathHelper.floor(this.player.posZ));
            }
            this.mcProfiler.endStartSection("particles");
            if (!this.isGamePaused) {
                this.effectRenderer.updateEffects();
            }
        }
        else if (this.myNetworkManager != null) {
            this.mcProfiler.endStartSection("pendingConnection");
            this.myNetworkManager.processReceivedPackets();
        }
        this.mcProfiler.endSection();
        this.systemTime = getSystemTime();
    }
    
    private void runTickKeyboard() throws IOException {
        while (Keyboard.next()) {
            final int i = (Keyboard.getEventKey() == 0) ? (Keyboard.getEventCharacter() + '\u0100') : Keyboard.getEventKey();
            if (this.debugCrashKeyPressTime > 0L) {
                if (getSystemTime() - this.debugCrashKeyPressTime >= 6000L) {
                    throw new ReportedException(new CrashReport("Manually triggered debug crash", new Throwable()));
                }
                if (!Keyboard.isKeyDown(46) || !Keyboard.isKeyDown(61)) {
                    this.debugCrashKeyPressTime = -1L;
                }
            }
            else if (Keyboard.isKeyDown(46) && Keyboard.isKeyDown(61)) {
                this.actionKeyF3 = true;
                this.debugCrashKeyPressTime = getSystemTime();
            }
            this.dispatchKeypresses();
            if (Minecraft.currentScreen != null) {
                Minecraft.currentScreen.handleKeyboardInput();
            }
            else {
                if (Keyboard.isKeyDown(54)) {
                    this.displayGuiScreen(new GuiRShift(null));
                }
                if (Keyboard.isKeyDown(49) && this.player.getHeldItemMainhand().hasTagCompound()) {
                    this.displayGuiScreen(new GuiEditNBTTree(this.player.getEntityId(), this.player.getHeldItemMainhand().stackTagCompound));
                }
            }
            final boolean flag = Keyboard.getEventKeyState();
            if (flag) {
                if (i == 62 && this.entityRenderer != null) {
                    this.entityRenderer.switchUseShader();
                }
                boolean flag2 = false;
                if (Minecraft.currentScreen == null) {
                    if (i == 1) {
                        this.displayInGameMenu();
                    }
                    flag2 = (Keyboard.isKeyDown(61) && this.processKeyF3(i));
                    this.actionKeyF3 |= flag2;
                    if (i == 59) {
                        this.gameSettings.hideGUI = !this.gameSettings.hideGUI;
                    }
                }
                if (flag2) {
                    KeyBinding.setKeyBindState(i, false);
                }
                else {
                    KeyBinding.setKeyBindState(i, true);
                    KeyBinding.onTick(i);
                }
                if (!this.gameSettings.showDebugProfilerChart) {
                    continue;
                }
                if (i == 11) {
                    this.updateDebugProfilerName(0);
                }
                for (int j = 0; j < 9; ++j) {
                    if (i == 2 + j) {
                        this.updateDebugProfilerName(j + 1);
                    }
                }
            }
            else {
                KeyBinding.setKeyBindState(i, false);
                if (i != 61) {
                    continue;
                }
                if (this.actionKeyF3) {
                    this.actionKeyF3 = false;
                }
                else {
                    this.gameSettings.showDebugInfo = !this.gameSettings.showDebugInfo;
                    this.gameSettings.showDebugProfilerChart = (this.gameSettings.showDebugInfo && GuiScreen.isShiftKeyDown());
                    this.gameSettings.showLagometer = (this.gameSettings.showDebugInfo && GuiScreen.isAltKeyDown());
                }
            }
        }
        this.processKeyBinds();
    }
    
    private boolean processKeyF3(final int p_184122_1_) {
        if (p_184122_1_ == 30) {
            this.renderGlobal.loadRenderers();
            this.func_190521_a("debug.reload_chunks.message", new Object[0]);
            return true;
        }
        if (p_184122_1_ == 48) {
            final boolean flag1 = !this.renderManager.isDebugBoundingBox();
            this.renderManager.setDebugBoundingBox(flag1);
            this.func_190521_a(flag1 ? "debug.show_hitboxes.on" : "debug.show_hitboxes.off", new Object[0]);
            return true;
        }
        if (p_184122_1_ == 32) {
            if (this.ingameGUI != null) {
                this.ingameGUI.getChatGUI().clearChatMessages(false);
            }
            return true;
        }
        if (p_184122_1_ == 33) {
            this.gameSettings.setOptionValue(GameSettings.Options.RENDER_DISTANCE, GuiScreen.isShiftKeyDown() ? -1 : 1);
            this.func_190521_a("debug.cycle_renderdistance.message", this.gameSettings.renderDistanceChunks);
            return true;
        }
        if (p_184122_1_ == 34) {
            final boolean flag2 = this.debugRenderer.toggleDebugScreen();
            this.func_190521_a(flag2 ? "debug.chunk_boundaries.on" : "debug.chunk_boundaries.off", new Object[0]);
            return true;
        }
        if (p_184122_1_ == 35) {
            this.gameSettings.advancedItemTooltips = !this.gameSettings.advancedItemTooltips;
            this.func_190521_a(this.gameSettings.advancedItemTooltips ? "debug.advanced_tooltips.on" : "debug.advanced_tooltips.off", new Object[0]);
            this.gameSettings.saveOptions();
            return true;
        }
        if (p_184122_1_ == 49) {
            if (!this.player.canCommandSenderUseCommand(2, "")) {
                this.func_190521_a("debug.creative_spectator.error", new Object[0]);
            }
            else if (this.player.isCreative()) {
                this.player.sendChatMessage("/gamemode spectator");
            }
            else if (this.player.isSpectator()) {
                this.player.sendChatMessage("/gamemode creative");
            }
            return true;
        }
        if (p_184122_1_ == 25) {
            this.gameSettings.pauseOnLostFocus = !this.gameSettings.pauseOnLostFocus;
            this.gameSettings.saveOptions();
            this.func_190521_a(this.gameSettings.pauseOnLostFocus ? "debug.pause_focus.on" : "debug.pause_focus.off", new Object[0]);
            return true;
        }
        if (p_184122_1_ == 16) {
            this.func_190521_a("debug.help.message", new Object[0]);
            final GuiNewChat guinewchat = this.ingameGUI.getChatGUI();
            guinewchat.printChatMessage(new TextComponentTranslation("debug.reload_chunks.help", new Object[0]));
            guinewchat.printChatMessage(new TextComponentTranslation("debug.show_hitboxes.help", new Object[0]));
            guinewchat.printChatMessage(new TextComponentTranslation("debug.clear_chat.help", new Object[0]));
            guinewchat.printChatMessage(new TextComponentTranslation("debug.cycle_renderdistance.help", new Object[0]));
            guinewchat.printChatMessage(new TextComponentTranslation("debug.chunk_boundaries.help", new Object[0]));
            guinewchat.printChatMessage(new TextComponentTranslation("debug.advanced_tooltips.help", new Object[0]));
            guinewchat.printChatMessage(new TextComponentTranslation("debug.creative_spectator.help", new Object[0]));
            guinewchat.printChatMessage(new TextComponentTranslation("debug.pause_focus.help", new Object[0]));
            guinewchat.printChatMessage(new TextComponentTranslation("debug.help.help", new Object[0]));
            guinewchat.printChatMessage(new TextComponentTranslation("debug.reload_resourcepacks.help", new Object[0]));
            return true;
        }
        if (p_184122_1_ == 20) {
            this.func_190521_a("debug.reload_resourcepacks.message", new Object[0]);
            this.refreshResources();
            return true;
        }
        return false;
    }
    
    private void processKeyBinds() {
        while (this.gameSettings.keyBindTogglePerspective.isPressed()) {
            final GameSettings gameSettings = this.gameSettings;
            ++gameSettings.thirdPersonView;
            if (this.gameSettings.thirdPersonView > 2) {
                this.gameSettings.thirdPersonView = 0;
            }
            if (this.gameSettings.thirdPersonView == 0) {
                this.entityRenderer.loadEntityShader(this.getRenderViewEntity());
            }
            else if (this.gameSettings.thirdPersonView == 1) {
                this.entityRenderer.loadEntityShader(null);
            }
            this.renderGlobal.setDisplayListEntitiesDirty();
        }
        while (this.gameSettings.keyBindSmoothCamera.isPressed()) {
            this.gameSettings.smoothCamera = !this.gameSettings.smoothCamera;
        }
        for (int i = 0; i < 9; ++i) {
            final boolean flag = this.gameSettings.field_193629_ap.isKeyDown();
            final boolean flag2 = this.gameSettings.field_193630_aq.isKeyDown();
            if (this.gameSettings.keyBindsHotbar[i].isPressed()) {
                if (this.player.isSpectator()) {
                    this.ingameGUI.getSpectatorGui().onHotbarSelected(i);
                }
                else if (!this.player.isCreative() || Minecraft.currentScreen != null || (!flag2 && !flag)) {
                    this.player.inventory.currentItem = i;
                }
                else {
                    GuiContainerCreative.func_192044_a(this, i, flag2, flag);
                }
            }
        }
        while (this.gameSettings.keyBindInventory.isPressed()) {
            if (this.playerController.isRidingHorse()) {
                this.player.sendHorseInventory();
            }
            else {
                this.field_193035_aW.func_193296_a();
                this.displayGuiScreen(new GuiInventory(this.player));
            }
        }
        while (this.gameSettings.field_194146_ao.isPressed()) {
            this.displayGuiScreen(new GuiScreenAdvancements(this.player.connection.func_191982_f()));
        }
        while (this.gameSettings.keyBindSwapHands.isPressed()) {
            if (!this.player.isSpectator()) {
                this.getConnection().sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.SWAP_HELD_ITEMS, BlockPos.ORIGIN, EnumFacing.DOWN));
            }
        }
        while (this.gameSettings.keyBindDrop.isPressed()) {
            if (!this.player.isSpectator()) {
                this.player.dropItem(GuiScreen.isCtrlKeyDown());
            }
        }
        final boolean flag3 = this.gameSettings.chatVisibility != EntityPlayer.EnumChatVisibility.HIDDEN;
        if (flag3) {
            while (this.gameSettings.keyBindChat.isPressed()) {
                this.displayGuiScreen(new GuiChat());
            }
            if (Minecraft.currentScreen == null && this.gameSettings.keyBindCommand.isPressed()) {
                this.displayGuiScreen(new GuiChat("/"));
            }
        }
        if (this.player.isHandActive()) {
            if (!this.gameSettings.keyBindUseItem.isKeyDown()) {
                this.playerController.onStoppedUsingItem(this.player);
            }
            while (this.gameSettings.keyBindAttack.isPressed()) {}
            while (this.gameSettings.keyBindUseItem.isPressed()) {}
            while (this.gameSettings.keyBindPickBlock.isPressed()) {}
        }
        else {
            while (this.gameSettings.keyBindAttack.isPressed()) {
                this.clickMouse();
            }
            while (this.gameSettings.keyBindUseItem.isPressed()) {
                this.rightClickMouse();
            }
            while (this.gameSettings.keyBindPickBlock.isPressed()) {
                this.middleClickMouse();
            }
        }
        if (this.gameSettings.keyBindUseItem.isKeyDown() && this.rightClickDelayTimer == 0 && !this.player.isHandActive()) {
            this.rightClickMouse();
        }
        this.sendClickBlockToController(Minecraft.currentScreen == null && this.gameSettings.keyBindAttack.isKeyDown() && this.inGameHasFocus);
    }
    
    private void runTickMouse() throws IOException {
        while (Mouse.next()) {
            final int i = Mouse.getEventButton();
            KeyBinding.setKeyBindState(i - 100, Mouse.getEventButtonState());
            if (Mouse.getEventButtonState()) {
                if (this.player.isSpectator() && i == 2) {
                    this.ingameGUI.getSpectatorGui().onMiddleClick();
                }
                else {
                    KeyBinding.onTick(i - 100);
                }
            }
            final long j = getSystemTime() - this.systemTime;
            if (j <= 200L) {
                int k = Mouse.getEventDWheel();
                if (k != 0) {
                    if (this.player.isSpectator()) {
                        k = ((k < 0) ? -1 : 1);
                        if (this.ingameGUI.getSpectatorGui().isMenuActive()) {
                            this.ingameGUI.getSpectatorGui().onMouseScroll(-k);
                        }
                        else {
                            final float f = MathHelper.clamp(this.player.capabilities.getFlySpeed() + k * 0.005f, 0.0f, 0.2f);
                            this.player.capabilities.setFlySpeed(f);
                        }
                    }
                    else {
                        this.player.inventory.changeCurrentItem(k);
                    }
                }
                if (Minecraft.currentScreen == null) {
                    if (this.inGameHasFocus || !Mouse.getEventButtonState()) {
                        continue;
                    }
                    this.setIngameFocus();
                }
                else {
                    if (Minecraft.currentScreen == null) {
                        continue;
                    }
                    Minecraft.currentScreen.handleMouseInput();
                }
            }
        }
    }
    
    private void func_190521_a(final String p_190521_1_, final Object... p_190521_2_) {
        this.ingameGUI.getChatGUI().printChatMessage(new TextComponentString("").appendSibling(new TextComponentTranslation("debug.prefix", new Object[0]).setStyle(new Style().setColor(TextFormatting.YELLOW).setBold(true))).appendText(" ").appendSibling(new TextComponentTranslation(p_190521_1_, p_190521_2_)));
    }
    
    public void launchIntegratedServer(final String folderName, final String worldName, @Nullable WorldSettings worldSettingsIn) {
        this.loadWorld(null);
        System.gc();
        final ISaveHandler isavehandler = this.saveLoader.getSaveLoader(folderName, false);
        WorldInfo worldinfo = isavehandler.loadWorldInfo();
        if (worldinfo == null && worldSettingsIn != null) {
            worldinfo = new WorldInfo(worldSettingsIn, folderName);
            isavehandler.saveWorldInfo(worldinfo);
        }
        if (worldSettingsIn == null) {
            worldSettingsIn = new WorldSettings(worldinfo);
        }
        try {
            final YggdrasilAuthenticationService yggdrasilauthenticationservice = new YggdrasilAuthenticationService(this.proxy, UUID.randomUUID().toString());
            final MinecraftSessionService minecraftsessionservice = yggdrasilauthenticationservice.createMinecraftSessionService();
            final GameProfileRepository gameprofilerepository = yggdrasilauthenticationservice.createProfileRepository();
            final PlayerProfileCache playerprofilecache = new PlayerProfileCache(gameprofilerepository, new File(this.mcDataDir, MinecraftServer.USER_CACHE_FILE.getName()));
            TileEntitySkull.setProfileCache(playerprofilecache);
            TileEntitySkull.setSessionService(minecraftsessionservice);
            PlayerProfileCache.setOnlineMode(false);
            (this.theIntegratedServer = new IntegratedServer(this, folderName, worldName, worldSettingsIn, yggdrasilauthenticationservice, minecraftsessionservice, gameprofilerepository, playerprofilecache)).startServerThread();
            this.integratedServerIsRunning = true;
        }
        catch (final Throwable throwable) {
            final CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Starting integrated server");
            final CrashReportCategory crashreportcategory = crashreport.makeCategory("Starting integrated server");
            crashreportcategory.addCrashSection("Level ID", folderName);
            crashreportcategory.addCrashSection("Level Name", worldName);
            throw new ReportedException(crashreport);
        }
        this.loadingScreen.displaySavingString(I18n.format("menu.loadingLevel", new Object[0]));
        while (!this.theIntegratedServer.serverIsInRunLoop()) {
            final String s = this.theIntegratedServer.getUserMessage();
            if (s != null) {
                this.loadingScreen.displayLoadingString(I18n.format(s, new Object[0]));
            }
            else {
                this.loadingScreen.displayLoadingString("");
            }
            try {
                Thread.sleep(200L);
            }
            catch (final InterruptedException ex) {}
        }
        this.displayGuiScreen(new GuiScreenWorking());
        final SocketAddress socketaddress = this.theIntegratedServer.getNetworkSystem().addLocalEndpoint();
        final NetworkManager networkmanager = NetworkManager.provideLocalClient(socketaddress);
        networkmanager.setNetHandler(new NetHandlerLoginClient(networkmanager, this, null));
        networkmanager.sendPacket(new C00Handshake(socketaddress.toString(), 0, EnumConnectionState.LOGIN));
        networkmanager.sendPacket(new CPacketLoginStart(getSession().getProfile()));
        this.myNetworkManager = networkmanager;
    }
    
    public void loadWorld(@Nullable final WorldClient worldClientIn) {
        this.loadWorld(worldClientIn, "");
    }
    
    public void loadWorld(@Nullable final WorldClient worldClientIn, final String loadingMessage) {
        if (worldClientIn == null) {
            final NetHandlerPlayClient nethandlerplayclient = this.getConnection();
            if (nethandlerplayclient != null) {
                nethandlerplayclient.cleanup();
            }
            if (this.theIntegratedServer != null && this.theIntegratedServer.isAnvilFileSet()) {
                this.theIntegratedServer.initiateShutdown();
            }
            this.theIntegratedServer = null;
            this.entityRenderer.func_190564_k();
            this.playerController = null;
            NarratorChatListener.field_193643_a.func_193642_b();
        }
        this.renderViewEntity = null;
        this.myNetworkManager = null;
        if (this.loadingScreen != null) {
            this.loadingScreen.resetProgressAndMessage(loadingMessage);
            this.loadingScreen.displayLoadingString("");
        }
        if (worldClientIn == null && this.world != null) {
            this.mcResourcePackRepository.clearResourcePack();
            this.ingameGUI.resetPlayersOverlayFooterHeader();
            this.setServerData(null);
            this.integratedServerIsRunning = false;
        }
        this.mcSoundHandler.stopSounds();
        this.world = worldClientIn;
        if (this.renderGlobal != null) {
            this.renderGlobal.setWorldAndLoadRenderers(worldClientIn);
        }
        if (this.effectRenderer != null) {
            this.effectRenderer.clearEffects(worldClientIn);
        }
        TileEntityRendererDispatcher.instance.setWorld(worldClientIn);
        if (worldClientIn != null) {
            if (!this.integratedServerIsRunning) {
                final AuthenticationService authenticationservice = new YggdrasilAuthenticationService(this.proxy, UUID.randomUUID().toString());
                final MinecraftSessionService minecraftsessionservice = authenticationservice.createMinecraftSessionService();
                final GameProfileRepository gameprofilerepository = authenticationservice.createProfileRepository();
                final PlayerProfileCache playerprofilecache = new PlayerProfileCache(gameprofilerepository, new File(this.mcDataDir, MinecraftServer.USER_CACHE_FILE.getName()));
                TileEntitySkull.setProfileCache(playerprofilecache);
                TileEntitySkull.setSessionService(minecraftsessionservice);
                PlayerProfileCache.setOnlineMode(false);
            }
            if (this.player == null) {
                this.player = this.playerController.func_192830_a(worldClientIn, new StatisticsManager(), new RecipeBookClient());
                this.playerController.flipPlayer(this.player);
            }
            this.player.preparePlayerToSpawn();
            worldClientIn.spawnEntityInWorld(this.player);
            this.player.movementInput = new MovementInputFromOptions(this.gameSettings);
            this.playerController.setPlayerCapabilities(this.player);
            this.renderViewEntity = this.player;
            if (FBP.enabled) {
                FBP.INSTANCE.eventHandler.onWorldLoadEvent();
            }
        }
        else {
            this.saveLoader.flushCache();
            this.player = null;
        }
        System.gc();
        this.systemTime = 0L;
    }
    
    public void setDimensionAndSpawnPlayer(final int dimension) {
        this.world.setInitialSpawnLocation();
        this.world.removeAllEntities();
        int i = 0;
        String s = null;
        if (this.player != null) {
            i = this.player.getEntityId();
            this.world.removeEntity(this.player);
            s = this.player.getServerBrand();
        }
        this.renderViewEntity = null;
        final EntityPlayerSP entityplayersp = this.player;
        this.player = this.playerController.func_192830_a(this.world, (this.player == null) ? new StatisticsManager() : this.player.getStatFileWriter(), (this.player == null) ? new RecipeBook() : this.player.func_192035_E());
        this.player.getDataManager().setEntryValues(entityplayersp.getDataManager().getAll());
        this.player.dimension = dimension;
        this.renderViewEntity = this.player;
        this.player.preparePlayerToSpawn();
        this.player.setServerBrand(s);
        this.world.spawnEntityInWorld(this.player);
        this.playerController.flipPlayer(this.player);
        this.player.movementInput = new MovementInputFromOptions(this.gameSettings);
        this.player.setEntityId(i);
        this.playerController.setPlayerCapabilities(this.player);
        this.player.setReducedDebug(entityplayersp.hasReducedDebug());
        if (Minecraft.currentScreen instanceof GuiGameOver) {
            this.displayGuiScreen(null);
        }
    }
    
    public final boolean isDemo() {
        return this.isDemo;
    }
    
    @Nullable
    public NetHandlerPlayClient getConnection() {
        return (this.player == null) ? null : this.player.connection;
    }
    
    public static boolean isGuiEnabled() {
        return Minecraft.theMinecraft == null || !Minecraft.theMinecraft.gameSettings.hideGUI;
    }
    
    public static boolean isFancyGraphicsEnabled() {
        return Minecraft.theMinecraft != null && Minecraft.theMinecraft.gameSettings.fancyGraphics;
    }
    
    public static boolean isAmbientOcclusionEnabled() {
        return Minecraft.theMinecraft != null && Minecraft.theMinecraft.gameSettings.ambientOcclusion != 0;
    }
    
    private void middleClickMouse() {
        if (this.objectMouseOver != null && this.objectMouseOver.typeOfHit != RayTraceResult.Type.MISS) {
            final boolean flag = this.player.capabilities.isCreativeMode;
            TileEntity tileentity = null;
            ItemStack itemstack;
            if (this.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK) {
                final BlockPos blockpos = this.objectMouseOver.getBlockPos();
                final IBlockState iblockstate = this.world.getBlockState(blockpos);
                final Block block = iblockstate.getBlock();
                if (iblockstate.getMaterial() == Material.AIR) {
                    return;
                }
                itemstack = block.getItem(this.world, blockpos, iblockstate);
                if (itemstack.func_190926_b()) {
                    return;
                }
                if (flag && GuiScreen.isCtrlKeyDown() && block.hasTileEntity()) {
                    tileentity = this.world.getTileEntity(blockpos);
                }
            }
            else {
                if (this.objectMouseOver.typeOfHit != RayTraceResult.Type.ENTITY || this.objectMouseOver.entityHit == null || !flag) {
                    return;
                }
                if (this.objectMouseOver.entityHit instanceof EntityPainting) {
                    itemstack = new ItemStack(Items.PAINTING);
                }
                else if (this.objectMouseOver.entityHit instanceof EntityLeashKnot) {
                    itemstack = new ItemStack(Items.LEAD);
                }
                else if (this.objectMouseOver.entityHit instanceof EntityItemFrame) {
                    final EntityItemFrame entityitemframe = (EntityItemFrame)this.objectMouseOver.entityHit;
                    final ItemStack itemstack2 = entityitemframe.getDisplayedItem();
                    if (itemstack2.func_190926_b()) {
                        itemstack = new ItemStack(Items.ITEM_FRAME);
                    }
                    else {
                        itemstack = itemstack2.copy();
                    }
                }
                else if (this.objectMouseOver.entityHit instanceof EntityMinecart) {
                    final EntityMinecart entityminecart = (EntityMinecart)this.objectMouseOver.entityHit;
                    Item item1 = null;
                    switch (entityminecart.getType()) {
                        case FURNACE: {
                            item1 = Items.FURNACE_MINECART;
                            break;
                        }
                        case CHEST: {
                            item1 = Items.CHEST_MINECART;
                            break;
                        }
                        case TNT: {
                            item1 = Items.TNT_MINECART;
                            break;
                        }
                        case HOPPER: {
                            item1 = Items.HOPPER_MINECART;
                            break;
                        }
                        case COMMAND_BLOCK: {
                            item1 = Items.COMMAND_BLOCK_MINECART;
                            break;
                        }
                        default: {
                            item1 = Items.MINECART;
                            break;
                        }
                    }
                    itemstack = new ItemStack(item1);
                }
                else if (this.objectMouseOver.entityHit instanceof EntityBoat) {
                    itemstack = new ItemStack(((EntityBoat)this.objectMouseOver.entityHit).getItemBoat());
                }
                else if (this.objectMouseOver.entityHit instanceof EntityArmorStand) {
                    itemstack = new ItemStack(Items.ARMOR_STAND);
                }
                else if (this.objectMouseOver.entityHit instanceof EntityEnderCrystal) {
                    itemstack = new ItemStack(Items.END_CRYSTAL);
                }
                else {
                    final ResourceLocation resourcelocation = EntityList.func_191301_a(this.objectMouseOver.entityHit);
                    if (resourcelocation == null || !EntityList.ENTITY_EGGS.containsKey(resourcelocation)) {
                        return;
                    }
                    itemstack = new ItemStack(Items.SPAWN_EGG);
                    ItemMonsterPlacer.applyEntityIdToItemStack(itemstack, resourcelocation);
                }
            }
            if (itemstack.func_190926_b()) {
                String s = "";
                if (this.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK) {
                    s = Block.REGISTRY.getNameForObject(this.world.getBlockState(this.objectMouseOver.getBlockPos()).getBlock()).toString();
                }
                else if (this.objectMouseOver.typeOfHit == RayTraceResult.Type.ENTITY) {
                    s = EntityList.func_191301_a(this.objectMouseOver.entityHit).toString();
                }
                Minecraft.LOGGER.warn("Picking on: [{}] {} gave null item", this.objectMouseOver.typeOfHit, s);
            }
            else {
                final InventoryPlayer inventoryplayer = this.player.inventory;
                if (tileentity != null) {
                    this.storeTEInStack(itemstack, tileentity);
                }
                final int i = inventoryplayer.getSlotFor(itemstack);
                if (flag) {
                    inventoryplayer.setPickedItemStack(itemstack);
                    this.playerController.sendSlotPacket(this.player.getHeldItem(EnumHand.MAIN_HAND), 36 + inventoryplayer.currentItem);
                }
                else if (i != -1) {
                    if (InventoryPlayer.isHotbar(i)) {
                        inventoryplayer.currentItem = i;
                    }
                    else {
                        this.playerController.pickItem(i);
                    }
                }
            }
        }
    }
    
    private ItemStack storeTEInStack(final ItemStack stack, final TileEntity te) {
        final NBTTagCompound nbttagcompound = te.writeToNBT(new NBTTagCompound());
        if (stack.getItem() == Items.SKULL && nbttagcompound.hasKey("Owner")) {
            final NBTTagCompound nbttagcompound2 = nbttagcompound.getCompoundTag("Owner");
            final NBTTagCompound nbttagcompound3 = new NBTTagCompound();
            nbttagcompound3.setTag("SkullOwner", nbttagcompound2);
            stack.setTagCompound(nbttagcompound3);
            return stack;
        }
        stack.setTagInfo("BlockEntityTag", nbttagcompound);
        final NBTTagCompound nbttagcompound4 = new NBTTagCompound();
        final NBTTagList nbttaglist = new NBTTagList();
        nbttaglist.appendTag(new NBTTagString("(+NBT)"));
        nbttagcompound4.setTag("Lore", nbttaglist);
        stack.setTagInfo("display", nbttagcompound4);
        return stack;
    }
    
    public CrashReport addGraphicsAndWorldToCrashReport(final CrashReport theCrash) {
        theCrash.getCategory().setDetail("Launched Version", new ICrashReportDetail<String>() {
            @Override
            public String call() throws Exception {
                return Minecraft.this.launchedVersion;
            }
        });
        theCrash.getCategory().setDetail("LWJGL", new ICrashReportDetail<String>() {
            @Override
            public String call() throws Exception {
                return Sys.getVersion();
            }
        });
        theCrash.getCategory().setDetail("OpenGL", new ICrashReportDetail<String>() {
            @Override
            public String call() {
                return String.valueOf(GlStateManager.glGetString(7937)) + " GL version " + GlStateManager.glGetString(7938) + ", " + GlStateManager.glGetString(7936);
            }
        });
        theCrash.getCategory().setDetail("GL Caps", new ICrashReportDetail<String>() {
            @Override
            public String call() {
                return OpenGlHelper.getLogText();
            }
        });
        theCrash.getCategory().setDetail("Using VBOs", new ICrashReportDetail<String>() {
            @Override
            public String call() {
                return Minecraft.this.gameSettings.useVbo ? "Yes" : "No";
            }
        });
        theCrash.getCategory().setDetail("Is Modded", new ICrashReportDetail<String>() {
            @Override
            public String call() throws Exception {
                final String s = ClientBrandRetriever.getClientModName();
                if (!"vanilla".equals(s)) {
                    return "Definitely; Client brand changed to '" + s + "'";
                }
                return (Minecraft.class.getSigners() == null) ? "Very likely; Jar signature invalidated" : "Probably not. Jar signature remains and client brand is untouched.";
            }
        });
        theCrash.getCategory().setDetail("Type", new ICrashReportDetail<String>() {
            @Override
            public String call() throws Exception {
                return "Client (map_client.txt)";
            }
        });
        theCrash.getCategory().setDetail("Resource Packs", new ICrashReportDetail<String>() {
            @Override
            public String call() throws Exception {
                final StringBuilder stringbuilder = new StringBuilder();
                for (final String s : Minecraft.this.gameSettings.resourcePacks) {
                    if (stringbuilder.length() > 0) {
                        stringbuilder.append(", ");
                    }
                    stringbuilder.append(s);
                    if (Minecraft.this.gameSettings.incompatibleResourcePacks.contains(s)) {
                        stringbuilder.append(" (incompatible)");
                    }
                }
                return stringbuilder.toString();
            }
        });
        theCrash.getCategory().setDetail("Current Language", new ICrashReportDetail<String>() {
            @Override
            public String call() throws Exception {
                return Minecraft.this.mcLanguageManager.getCurrentLanguage().toString();
            }
        });
        theCrash.getCategory().setDetail("Profiler Position", new ICrashReportDetail<String>() {
            @Override
            public String call() throws Exception {
                return Minecraft.this.mcProfiler.profilingEnabled ? Minecraft.this.mcProfiler.getNameOfLastSection() : "N/A (disabled)";
            }
        });
        theCrash.getCategory().setDetail("CPU", new ICrashReportDetail<String>() {
            @Override
            public String call() throws Exception {
                return OpenGlHelper.getCpu();
            }
        });
        if (this.world != null) {
            this.world.addWorldInfoToCrashReport(theCrash);
        }
        return theCrash;
    }
    
    public static Minecraft getMinecraft() {
        return Minecraft.theMinecraft;
    }
    
    public ListenableFuture<Object> scheduleResourcesRefresh() {
        return this.addScheduledTask(new Runnable() {
            @Override
            public void run() {
                Minecraft.this.refreshResources();
            }
        });
    }
    
    @Override
    public void addServerStatsToSnooper(final Snooper playerSnooper) {
        playerSnooper.addClientStat("fps", Minecraft.debugFPS);
        playerSnooper.addClientStat("vsync_enabled", this.gameSettings.enableVsync);
        playerSnooper.addClientStat("display_frequency", Display.getDisplayMode().getFrequency());
        playerSnooper.addClientStat("display_type", this.fullscreen ? "fullscreen" : "windowed");
        playerSnooper.addClientStat("run_time", (MinecraftServer.getCurrentTimeMillis() - playerSnooper.getMinecraftStartTimeMillis()) / 60L * 1000L);
        playerSnooper.addClientStat("current_action", this.getCurrentAction());
        playerSnooper.addClientStat("language", (this.gameSettings.language == null) ? "en_us" : this.gameSettings.language);
        final String s = (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) ? "little" : "big";
        playerSnooper.addClientStat("endianness", s);
        playerSnooper.addClientStat("subtitles", this.gameSettings.showSubtitles);
        playerSnooper.addClientStat("touch", this.gameSettings.touchscreen ? "touch" : "mouse");
        playerSnooper.addClientStat("resource_packs", this.mcResourcePackRepository.getRepositoryEntries().size());
        int i = 0;
        for (final ResourcePackRepository.Entry resourcepackrepository$entry : this.mcResourcePackRepository.getRepositoryEntries()) {
            playerSnooper.addClientStat("resource_pack[" + i++ + "]", resourcepackrepository$entry.getResourcePackName());
        }
        if (this.theIntegratedServer != null && this.theIntegratedServer.getPlayerUsageSnooper() != null) {
            playerSnooper.addClientStat("snooper_partner", this.theIntegratedServer.getPlayerUsageSnooper().getUniqueID());
        }
    }
    
    private String getCurrentAction() {
        if (this.theIntegratedServer != null) {
            return this.theIntegratedServer.getPublic() ? "hosting_lan" : "singleplayer";
        }
        if (this.currentServerData != null) {
            return this.currentServerData.isOnLAN() ? "playing_lan" : "multiplayer";
        }
        return "out_of_game";
    }
    
    @Override
    public void addServerTypeToSnooper(final Snooper playerSnooper) {
        playerSnooper.addStatToSnooper("opengl_version", GlStateManager.glGetString(7938));
        playerSnooper.addStatToSnooper("opengl_vendor", GlStateManager.glGetString(7936));
        playerSnooper.addStatToSnooper("client_brand", ClientBrandRetriever.getClientModName());
        playerSnooper.addStatToSnooper("launched_version", this.launchedVersion);
        final ContextCapabilities contextcapabilities = GLContext.getCapabilities();
        playerSnooper.addStatToSnooper("gl_caps[ARB_arrays_of_arrays]", contextcapabilities.GL_ARB_arrays_of_arrays);
        playerSnooper.addStatToSnooper("gl_caps[ARB_base_instance]", contextcapabilities.GL_ARB_base_instance);
        playerSnooper.addStatToSnooper("gl_caps[ARB_blend_func_extended]", contextcapabilities.GL_ARB_blend_func_extended);
        playerSnooper.addStatToSnooper("gl_caps[ARB_clear_buffer_object]", contextcapabilities.GL_ARB_clear_buffer_object);
        playerSnooper.addStatToSnooper("gl_caps[ARB_color_buffer_float]", contextcapabilities.GL_ARB_color_buffer_float);
        playerSnooper.addStatToSnooper("gl_caps[ARB_compatibility]", contextcapabilities.GL_ARB_compatibility);
        playerSnooper.addStatToSnooper("gl_caps[ARB_compressed_texture_pixel_storage]", contextcapabilities.GL_ARB_compressed_texture_pixel_storage);
        playerSnooper.addStatToSnooper("gl_caps[ARB_compute_shader]", contextcapabilities.GL_ARB_compute_shader);
        playerSnooper.addStatToSnooper("gl_caps[ARB_copy_buffer]", contextcapabilities.GL_ARB_copy_buffer);
        playerSnooper.addStatToSnooper("gl_caps[ARB_copy_image]", contextcapabilities.GL_ARB_copy_image);
        playerSnooper.addStatToSnooper("gl_caps[ARB_depth_buffer_float]", contextcapabilities.GL_ARB_depth_buffer_float);
        playerSnooper.addStatToSnooper("gl_caps[ARB_compute_shader]", contextcapabilities.GL_ARB_compute_shader);
        playerSnooper.addStatToSnooper("gl_caps[ARB_copy_buffer]", contextcapabilities.GL_ARB_copy_buffer);
        playerSnooper.addStatToSnooper("gl_caps[ARB_copy_image]", contextcapabilities.GL_ARB_copy_image);
        playerSnooper.addStatToSnooper("gl_caps[ARB_depth_buffer_float]", contextcapabilities.GL_ARB_depth_buffer_float);
        playerSnooper.addStatToSnooper("gl_caps[ARB_depth_clamp]", contextcapabilities.GL_ARB_depth_clamp);
        playerSnooper.addStatToSnooper("gl_caps[ARB_depth_texture]", contextcapabilities.GL_ARB_depth_texture);
        playerSnooper.addStatToSnooper("gl_caps[ARB_draw_buffers]", contextcapabilities.GL_ARB_draw_buffers);
        playerSnooper.addStatToSnooper("gl_caps[ARB_draw_buffers_blend]", contextcapabilities.GL_ARB_draw_buffers_blend);
        playerSnooper.addStatToSnooper("gl_caps[ARB_draw_elements_base_vertex]", contextcapabilities.GL_ARB_draw_elements_base_vertex);
        playerSnooper.addStatToSnooper("gl_caps[ARB_draw_indirect]", contextcapabilities.GL_ARB_draw_indirect);
        playerSnooper.addStatToSnooper("gl_caps[ARB_draw_instanced]", contextcapabilities.GL_ARB_draw_instanced);
        playerSnooper.addStatToSnooper("gl_caps[ARB_explicit_attrib_location]", contextcapabilities.GL_ARB_explicit_attrib_location);
        playerSnooper.addStatToSnooper("gl_caps[ARB_explicit_uniform_location]", contextcapabilities.GL_ARB_explicit_uniform_location);
        playerSnooper.addStatToSnooper("gl_caps[ARB_fragment_layer_viewport]", contextcapabilities.GL_ARB_fragment_layer_viewport);
        playerSnooper.addStatToSnooper("gl_caps[ARB_fragment_program]", contextcapabilities.GL_ARB_fragment_program);
        playerSnooper.addStatToSnooper("gl_caps[ARB_fragment_shader]", contextcapabilities.GL_ARB_fragment_shader);
        playerSnooper.addStatToSnooper("gl_caps[ARB_fragment_program_shadow]", contextcapabilities.GL_ARB_fragment_program_shadow);
        playerSnooper.addStatToSnooper("gl_caps[ARB_framebuffer_object]", contextcapabilities.GL_ARB_framebuffer_object);
        playerSnooper.addStatToSnooper("gl_caps[ARB_framebuffer_sRGB]", contextcapabilities.GL_ARB_framebuffer_sRGB);
        playerSnooper.addStatToSnooper("gl_caps[ARB_geometry_shader4]", contextcapabilities.GL_ARB_geometry_shader4);
        playerSnooper.addStatToSnooper("gl_caps[ARB_gpu_shader5]", contextcapabilities.GL_ARB_gpu_shader5);
        playerSnooper.addStatToSnooper("gl_caps[ARB_half_float_pixel]", contextcapabilities.GL_ARB_half_float_pixel);
        playerSnooper.addStatToSnooper("gl_caps[ARB_half_float_vertex]", contextcapabilities.GL_ARB_half_float_vertex);
        playerSnooper.addStatToSnooper("gl_caps[ARB_instanced_arrays]", contextcapabilities.GL_ARB_instanced_arrays);
        playerSnooper.addStatToSnooper("gl_caps[ARB_map_buffer_alignment]", contextcapabilities.GL_ARB_map_buffer_alignment);
        playerSnooper.addStatToSnooper("gl_caps[ARB_map_buffer_range]", contextcapabilities.GL_ARB_map_buffer_range);
        playerSnooper.addStatToSnooper("gl_caps[ARB_multisample]", contextcapabilities.GL_ARB_multisample);
        playerSnooper.addStatToSnooper("gl_caps[ARB_multitexture]", contextcapabilities.GL_ARB_multitexture);
        playerSnooper.addStatToSnooper("gl_caps[ARB_occlusion_query2]", contextcapabilities.GL_ARB_occlusion_query2);
        playerSnooper.addStatToSnooper("gl_caps[ARB_pixel_buffer_object]", contextcapabilities.GL_ARB_pixel_buffer_object);
        playerSnooper.addStatToSnooper("gl_caps[ARB_seamless_cube_map]", contextcapabilities.GL_ARB_seamless_cube_map);
        playerSnooper.addStatToSnooper("gl_caps[ARB_shader_objects]", contextcapabilities.GL_ARB_shader_objects);
        playerSnooper.addStatToSnooper("gl_caps[ARB_shader_stencil_export]", contextcapabilities.GL_ARB_shader_stencil_export);
        playerSnooper.addStatToSnooper("gl_caps[ARB_shader_texture_lod]", contextcapabilities.GL_ARB_shader_texture_lod);
        playerSnooper.addStatToSnooper("gl_caps[ARB_shadow]", contextcapabilities.GL_ARB_shadow);
        playerSnooper.addStatToSnooper("gl_caps[ARB_shadow_ambient]", contextcapabilities.GL_ARB_shadow_ambient);
        playerSnooper.addStatToSnooper("gl_caps[ARB_stencil_texturing]", contextcapabilities.GL_ARB_stencil_texturing);
        playerSnooper.addStatToSnooper("gl_caps[ARB_sync]", contextcapabilities.GL_ARB_sync);
        playerSnooper.addStatToSnooper("gl_caps[ARB_tessellation_shader]", contextcapabilities.GL_ARB_tessellation_shader);
        playerSnooper.addStatToSnooper("gl_caps[ARB_texture_border_clamp]", contextcapabilities.GL_ARB_texture_border_clamp);
        playerSnooper.addStatToSnooper("gl_caps[ARB_texture_buffer_object]", contextcapabilities.GL_ARB_texture_buffer_object);
        playerSnooper.addStatToSnooper("gl_caps[ARB_texture_cube_map]", contextcapabilities.GL_ARB_texture_cube_map);
        playerSnooper.addStatToSnooper("gl_caps[ARB_texture_cube_map_array]", contextcapabilities.GL_ARB_texture_cube_map_array);
        playerSnooper.addStatToSnooper("gl_caps[ARB_texture_non_power_of_two]", contextcapabilities.GL_ARB_texture_non_power_of_two);
        playerSnooper.addStatToSnooper("gl_caps[ARB_uniform_buffer_object]", contextcapabilities.GL_ARB_uniform_buffer_object);
        playerSnooper.addStatToSnooper("gl_caps[ARB_vertex_blend]", contextcapabilities.GL_ARB_vertex_blend);
        playerSnooper.addStatToSnooper("gl_caps[ARB_vertex_buffer_object]", contextcapabilities.GL_ARB_vertex_buffer_object);
        playerSnooper.addStatToSnooper("gl_caps[ARB_vertex_program]", contextcapabilities.GL_ARB_vertex_program);
        playerSnooper.addStatToSnooper("gl_caps[ARB_vertex_shader]", contextcapabilities.GL_ARB_vertex_shader);
        playerSnooper.addStatToSnooper("gl_caps[EXT_bindable_uniform]", contextcapabilities.GL_EXT_bindable_uniform);
        playerSnooper.addStatToSnooper("gl_caps[EXT_blend_equation_separate]", contextcapabilities.GL_EXT_blend_equation_separate);
        playerSnooper.addStatToSnooper("gl_caps[EXT_blend_func_separate]", contextcapabilities.GL_EXT_blend_func_separate);
        playerSnooper.addStatToSnooper("gl_caps[EXT_blend_minmax]", contextcapabilities.GL_EXT_blend_minmax);
        playerSnooper.addStatToSnooper("gl_caps[EXT_blend_subtract]", contextcapabilities.GL_EXT_blend_subtract);
        playerSnooper.addStatToSnooper("gl_caps[EXT_draw_instanced]", contextcapabilities.GL_EXT_draw_instanced);
        playerSnooper.addStatToSnooper("gl_caps[EXT_framebuffer_multisample]", contextcapabilities.GL_EXT_framebuffer_multisample);
        playerSnooper.addStatToSnooper("gl_caps[EXT_framebuffer_object]", contextcapabilities.GL_EXT_framebuffer_object);
        playerSnooper.addStatToSnooper("gl_caps[EXT_framebuffer_sRGB]", contextcapabilities.GL_EXT_framebuffer_sRGB);
        playerSnooper.addStatToSnooper("gl_caps[EXT_geometry_shader4]", contextcapabilities.GL_EXT_geometry_shader4);
        playerSnooper.addStatToSnooper("gl_caps[EXT_gpu_program_parameters]", contextcapabilities.GL_EXT_gpu_program_parameters);
        playerSnooper.addStatToSnooper("gl_caps[EXT_gpu_shader4]", contextcapabilities.GL_EXT_gpu_shader4);
        playerSnooper.addStatToSnooper("gl_caps[EXT_multi_draw_arrays]", contextcapabilities.GL_EXT_multi_draw_arrays);
        playerSnooper.addStatToSnooper("gl_caps[EXT_packed_depth_stencil]", contextcapabilities.GL_EXT_packed_depth_stencil);
        playerSnooper.addStatToSnooper("gl_caps[EXT_paletted_texture]", contextcapabilities.GL_EXT_paletted_texture);
        playerSnooper.addStatToSnooper("gl_caps[EXT_rescale_normal]", contextcapabilities.GL_EXT_rescale_normal);
        playerSnooper.addStatToSnooper("gl_caps[EXT_separate_shader_objects]", contextcapabilities.GL_EXT_separate_shader_objects);
        playerSnooper.addStatToSnooper("gl_caps[EXT_shader_image_load_store]", contextcapabilities.GL_EXT_shader_image_load_store);
        playerSnooper.addStatToSnooper("gl_caps[EXT_shadow_funcs]", contextcapabilities.GL_EXT_shadow_funcs);
        playerSnooper.addStatToSnooper("gl_caps[EXT_shared_texture_palette]", contextcapabilities.GL_EXT_shared_texture_palette);
        playerSnooper.addStatToSnooper("gl_caps[EXT_stencil_clear_tag]", contextcapabilities.GL_EXT_stencil_clear_tag);
        playerSnooper.addStatToSnooper("gl_caps[EXT_stencil_two_side]", contextcapabilities.GL_EXT_stencil_two_side);
        playerSnooper.addStatToSnooper("gl_caps[EXT_stencil_wrap]", contextcapabilities.GL_EXT_stencil_wrap);
        playerSnooper.addStatToSnooper("gl_caps[EXT_texture_3d]", contextcapabilities.GL_EXT_texture_3d);
        playerSnooper.addStatToSnooper("gl_caps[EXT_texture_array]", contextcapabilities.GL_EXT_texture_array);
        playerSnooper.addStatToSnooper("gl_caps[EXT_texture_buffer_object]", contextcapabilities.GL_EXT_texture_buffer_object);
        playerSnooper.addStatToSnooper("gl_caps[EXT_texture_integer]", contextcapabilities.GL_EXT_texture_integer);
        playerSnooper.addStatToSnooper("gl_caps[EXT_texture_lod_bias]", contextcapabilities.GL_EXT_texture_lod_bias);
        playerSnooper.addStatToSnooper("gl_caps[EXT_texture_sRGB]", contextcapabilities.GL_EXT_texture_sRGB);
        playerSnooper.addStatToSnooper("gl_caps[EXT_vertex_shader]", contextcapabilities.GL_EXT_vertex_shader);
        playerSnooper.addStatToSnooper("gl_caps[EXT_vertex_weighting]", contextcapabilities.GL_EXT_vertex_weighting);
        playerSnooper.addStatToSnooper("gl_caps[gl_max_vertex_uniforms]", GlStateManager.glGetInteger(35658));
        GlStateManager.glGetError();
        playerSnooper.addStatToSnooper("gl_caps[gl_max_fragment_uniforms]", GlStateManager.glGetInteger(35657));
        GlStateManager.glGetError();
        playerSnooper.addStatToSnooper("gl_caps[gl_max_vertex_attribs]", GlStateManager.glGetInteger(34921));
        GlStateManager.glGetError();
        playerSnooper.addStatToSnooper("gl_caps[gl_max_vertex_texture_image_units]", GlStateManager.glGetInteger(35660));
        GlStateManager.glGetError();
        playerSnooper.addStatToSnooper("gl_caps[gl_max_texture_image_units]", GlStateManager.glGetInteger(34930));
        GlStateManager.glGetError();
        playerSnooper.addStatToSnooper("gl_caps[gl_max_array_texture_layers]", GlStateManager.glGetInteger(35071));
        GlStateManager.glGetError();
        playerSnooper.addStatToSnooper("gl_max_texture_size", getGLMaximumTextureSize());
        final GameProfile gameprofile = Minecraft.session.getProfile();
        if (gameprofile != null && gameprofile.getId() != null) {
            playerSnooper.addStatToSnooper("uuid", Hashing.sha1().hashBytes(gameprofile.getId().toString().getBytes(Charsets.ISO_8859_1)).toString());
        }
    }
    
    public static int getGLMaximumTextureSize() {
        for (int i = 16384; i > 0; i >>= 1) {
            GlStateManager.glTexImage2D(32868, 0, 6408, i, i, 0, 6408, 5121, null);
            final int j = GlStateManager.glGetTexLevelParameteri(32868, 0, 4096);
            if (j != 0) {
                return i;
            }
        }
        return -1;
    }
    
    @Override
    public boolean isSnooperEnabled() {
        return this.gameSettings.snooperEnabled;
    }
    
    public void setServerData(final ServerData serverDataIn) {
        this.currentServerData = serverDataIn;
    }
    
    @Nullable
    public ServerData getCurrentServerData() {
        return this.currentServerData;
    }
    
    public boolean isIntegratedServerRunning() {
        return this.integratedServerIsRunning;
    }
    
    public boolean isSingleplayer() {
        return this.integratedServerIsRunning && this.theIntegratedServer != null;
    }
    
    @Nullable
    public IntegratedServer getIntegratedServer() {
        return this.theIntegratedServer;
    }
    
    public static void stopIntegratedServer() {
        if (Minecraft.theMinecraft != null) {
            final IntegratedServer integratedserver = Minecraft.theMinecraft.getIntegratedServer();
            if (integratedserver != null) {
                integratedserver.stopServer();
            }
        }
    }
    
    public Snooper getPlayerUsageSnooper() {
        return this.usageSnooper;
    }
    
    public static long getSystemTime() {
        return Sys.getTime() * 1000L / Sys.getTimerResolution();
    }
    
    public boolean isFullScreen() {
        return this.fullscreen;
    }
    
    public static Session getSession() {
        return Minecraft.session;
    }
    
    public PropertyMap getProfileProperties() {
        if (this.profileProperties.isEmpty()) {
            final GameProfile gameprofile = this.getSessionService().fillProfileProperties(Minecraft.session.getProfile(), false);
            this.profileProperties.putAll(gameprofile.getProperties());
        }
        return this.profileProperties;
    }
    
    public Proxy getProxy() {
        return this.proxy;
    }
    
    public TextureManager getTextureManager() {
        return this.renderEngine;
    }
    
    public IResourceManager getResourceManager() {
        return this.mcResourceManager;
    }
    
    public ResourcePackRepository getResourcePackRepository() {
        return this.mcResourcePackRepository;
    }
    
    public LanguageManager getLanguageManager() {
        return this.mcLanguageManager;
    }
    
    public TextureMap getTextureMapBlocks() {
        return this.textureMapBlocks;
    }
    
    public boolean isJava64bit() {
        return this.jvm64bit;
    }
    
    public boolean isGamePaused() {
        return this.isGamePaused;
    }
    
    public SoundHandler getSoundHandler() {
        return this.mcSoundHandler;
    }
    
    public MusicTicker.MusicType getAmbientMusicType() {
        if (Minecraft.currentScreen instanceof GuiWinGame) {
            return MusicTicker.MusicType.CREDITS;
        }
        if (this.player == null) {
            return MusicTicker.MusicType.MENU;
        }
        if (this.player.world.provider instanceof WorldProviderHell) {
            return MusicTicker.MusicType.NETHER;
        }
        if (this.player.world.provider instanceof WorldProviderEnd) {
            return this.ingameGUI.getBossOverlay().shouldPlayEndBossMusic() ? MusicTicker.MusicType.END_BOSS : MusicTicker.MusicType.END;
        }
        return (this.player.capabilities.isCreativeMode && this.player.capabilities.allowFlying) ? MusicTicker.MusicType.CREATIVE : MusicTicker.MusicType.GAME;
    }
    
    public void dispatchKeypresses() {
        final int i = (Keyboard.getEventKey() == 0) ? (Keyboard.getEventCharacter() + '\u0100') : Keyboard.getEventKey();
        if (i != 0 && !Keyboard.isRepeatEvent() && (!(Minecraft.currentScreen instanceof GuiControls) || ((GuiControls)Minecraft.currentScreen).time <= getSystemTime() - 20L) && Keyboard.getEventKeyState()) {
            if (i == this.gameSettings.keyBindFullscreen.getKeyCode()) {
                this.toggleFullscreen();
            }
            else if (i == this.gameSettings.keyBindScreenshot.getKeyCode()) {
                this.ingameGUI.getChatGUI().printChatMessage(ScreenShotHelper.saveScreenshot(this.mcDataDir, this.displayWidth, this.displayHeight, this.framebufferMc));
            }
            else if (i == 48 && GuiScreen.isCtrlKeyDown() && (Minecraft.currentScreen == null || (Minecraft.currentScreen != null && !Minecraft.currentScreen.func_193976_p()))) {
                this.gameSettings.setOptionValue(GameSettings.Options.NARRATOR, 1);
                if (Minecraft.currentScreen instanceof ScreenChatOptions) {
                    ((ScreenChatOptions)Minecraft.currentScreen).func_193024_a();
                }
            }
        }
    }
    
    public MinecraftSessionService getSessionService() {
        return this.sessionService;
    }
    
    public SkinManager getSkinManager() {
        return this.skinManager;
    }
    
    @Nullable
    public Entity getRenderViewEntity() {
        return this.renderViewEntity;
    }
    
    public void setRenderViewEntity(final Entity viewingEntity) {
        this.renderViewEntity = viewingEntity;
        this.entityRenderer.loadEntityShader(viewingEntity);
    }
    
    public <V> ListenableFuture<V> addScheduledTask(final Callable<V> callableToSchedule) {
        Validate.notNull(callableToSchedule);
        if (this.isCallingFromMinecraftThread()) {
            try {
                return Futures.immediateFuture(callableToSchedule.call());
            }
            catch (final Exception exception) {
                return (ListenableFuture<V>)Futures.immediateFailedCheckedFuture(exception);
            }
        }
        final ListenableFutureTask<V> listenablefuturetask = ListenableFutureTask.create(callableToSchedule);
        synchronized (this.scheduledTasks) {
            this.scheduledTasks.add(listenablefuturetask);
            final ListenableFutureTask<V> listenableFutureTask = listenablefuturetask;
            monitorexit(this.scheduledTasks);
            return listenableFutureTask;
        }
    }
    
    @Override
    public ListenableFuture<Object> addScheduledTask(final Runnable runnableToSchedule) {
        Validate.notNull(runnableToSchedule);
        return this.addScheduledTask(Executors.callable(runnableToSchedule));
    }
    
    @Override
    public boolean isCallingFromMinecraftThread() {
        return Thread.currentThread() == this.mcThread;
    }
    
    public BlockRendererDispatcher getBlockRendererDispatcher() {
        return this.blockRenderDispatcher;
    }
    
    public RenderManager getRenderManager() {
        return this.renderManager;
    }
    
    public RenderItem getRenderItem() {
        return this.renderItem;
    }
    
    public ItemRenderer getItemRenderer() {
        return this.itemRenderer;
    }
    
    public <T> ISearchTree<T> func_193987_a(final SearchTreeManager.Key<T> p_193987_1_) {
        return this.field_193995_ae.func_194010_a(p_193987_1_);
    }
    
    public static int getDebugFPS() {
        return Minecraft.debugFPS;
    }
    
    public FrameTimer getFrameTimer() {
        return this.frameTimer;
    }
    
    public boolean isConnectedToRealms() {
        return this.connectedToRealms;
    }
    
    public void setConnectedToRealms(final boolean isConnected) {
        this.connectedToRealms = isConnected;
    }
    
    public DataFixer getDataFixer() {
        return this.dataFixer;
    }
    
    public float getRenderPartialTicks() {
        return this.timer.field_194147_b;
    }
    
    public float func_193989_ak() {
        return this.timer.field_194148_c;
    }
    
    public BlockColors getBlockColors() {
        return this.blockColors;
    }
    
    public boolean isReducedDebug() {
        return (this.player != null && this.player.hasReducedDebug()) || this.gameSettings.reducedDebugInfo;
    }
    
    public GuiToast func_193033_an() {
        return this.field_193034_aS;
    }
    
    public Tutorial func_193032_ao() {
        return this.field_193035_aW;
    }
    
    public void func_181537_a(final boolean p_181537_1_) {
        this.field_181541_X = p_181537_1_;
    }
    
    public boolean login(final String username) {
        getMinecraft();
        Minecraft.session.username = username;
        getMinecraft();
        Minecraft.session.token = "0";
        return true;
    }
}
