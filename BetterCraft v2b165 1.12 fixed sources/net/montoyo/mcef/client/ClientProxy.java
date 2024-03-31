// 
// Decompiled by Procyon v0.6.0
// 

package net.montoyo.mcef.client;

import java.util.regex.Matcher;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Pattern;
import org.cef.browser.CefRenderer;
import com.darkmagician6.eventapi.EventTarget;
import me.amkgre.bettercraft.client.events.GameLoopEvent;
import net.montoyo.mcef.api.IScheme;
import org.cef.handler.CefMessageRouterHandler;
import net.montoyo.mcef.api.IJSQueryHandler;
import net.montoyo.mcef.api.IDisplayHandler;
import net.montoyo.mcef.virtual.VirtualBrowser;
import net.montoyo.mcef.api.IBrowser;
import java.util.Iterator;
import java.nio.file.Path;
import java.nio.file.FileSystem;
import java.lang.reflect.Field;
import net.montoyo.mcef.utilities.IProgressListener;
import org.cef.handler.CefLifeSpanHandler;
import org.cef.browser.CefBrowser;
import org.cef.handler.CefLifeSpanHandlerAdapter;
import org.cef.handler.CefDisplayHandler;
import org.cef.handler.CefAppHandler;
import org.cef.CefSettings;
import java.io.IOException;
import java.awt.Component;
import javax.swing.JOptionPane;
import java.util.Arrays;
import net.montoyo.mcef.utilities.Util;
import java.nio.file.FileSystems;
import org.cef.OS;
import net.montoyo.mcef.utilities.Log;
import net.montoyo.mcef.remote.RemoteConfig;
import java.io.File;
import net.montoyo.mcef.MCEF;
import com.darkmagician6.eventapi.EventManager;
import net.montoyo.mcef.example.ExampleMod;
import java.util.HashMap;
import net.minecraft.client.Minecraft;
import org.cef.browser.CefBrowserOsr;
import java.util.ArrayList;
import org.cef.browser.CefMessageRouter;
import org.cef.CefClient;
import org.cef.CefApp;
import net.montoyo.mcef.BaseProxy;

public class ClientProxy extends BaseProxy
{
    public static String ROOT;
    public static boolean VIRTUAL;
    private CefApp cefApp;
    private CefClient cefClient;
    private CefMessageRouter cefRouter;
    private final ArrayList<CefBrowserOsr> browsers;
    private String updateStr;
    private final Minecraft mc;
    private final DisplayHandler displayHandler;
    private final HashMap<String, String> mimeTypeMap;
    private final AppHandler appHandler;
    private ExampleMod exampleMod;
    public static final String LINUX_WIKI = "https://montoyo.net/wdwiki/Linux";
    
    static {
        ClientProxy.ROOT = ".";
        ClientProxy.VIRTUAL = false;
    }
    
    public ClientProxy() {
        this.browsers = new ArrayList<CefBrowserOsr>();
        this.mc = Minecraft.getMinecraft();
        this.displayHandler = new DisplayHandler();
        this.mimeTypeMap = new HashMap<String, String>();
        this.appHandler = new AppHandler();
    }
    
    @Override
    public void onPreInit() {
        (this.exampleMod = new ExampleMod()).onPreInit();
    }
    
    @Override
    public void onInit() {
        System.out.println("Client init!! 1");
        EventManager.register(this);
        this.appHandler.setArgs(MCEF.CEF_ARGS);
        ClientProxy.ROOT = this.mc.mcDataDir.getAbsolutePath().replaceAll("\\\\", "/");
        if (ClientProxy.ROOT.endsWith(".")) {
            ClientProxy.ROOT = ClientProxy.ROOT.substring(0, ClientProxy.ROOT.length() - 1);
        }
        if (ClientProxy.ROOT.endsWith("/")) {
            ClientProxy.ROOT = ClientProxy.ROOT.substring(0, ClientProxy.ROOT.length() - 1);
        }
        final File fileListing = new File(new File(ClientProxy.ROOT), "config");
        final RemoteConfig cfg = new RemoteConfig();
        System.out.println("Client init!! 2");
        final IProgressListener ipl = new UpdateFrame();
        cfg.load();
        final File[] resourceArray = cfg.getResourceArray();
        if (!cfg.updateFileListing(fileListing, false)) {
            Log.warning("There was a problem while establishing file list. Uninstall may not delete all files.", new Object[0]);
        }
        if (!cfg.downloadMissing(ipl)) {
            Log.warning("Going in virtual mode; couldn't download resources.", new Object[0]);
            ClientProxy.VIRTUAL = true;
            return;
        }
        System.out.println("Client init!! 3");
        if (!cfg.updateFileListing(fileListing, true)) {
            Log.warning("There was a problem while updating file list. Uninstall may not delete all files.", new Object[0]);
        }
        this.updateStr = cfg.getUpdateString();
        ipl.onProgressEnd();
        if (ClientProxy.VIRTUAL) {
            return;
        }
        Log.info("Now adding \"%s\" to java.library.path", ClientProxy.ROOT);
        try {
            final Field pathsField = ClassLoader.class.getDeclaredField("usr_paths");
            pathsField.setAccessible(true);
            final String[] paths = (String[])pathsField.get(null);
            final String[] newList = new String[paths.length + 1];
            System.arraycopy(paths, 0, newList, 1, paths.length);
            newList[0] = ClientProxy.ROOT.replace('/', File.separatorChar);
            pathsField.set(null, newList);
        }
        catch (final Exception e) {
            Log.error("Failed to do it! Entering virtual mode...", new Object[0]);
            e.printStackTrace();
            ClientProxy.VIRTUAL = true;
            return;
        }
        System.out.println("Client init!! 4");
        Log.info("Done without errors.", new Object[0]);
        if (OS.isLinux()) {
            final FileSystem fs = FileSystems.getDefault();
            final Path here = fs.getPath(this.mc.mcDataDir.getPath(), new String[0]);
            final String[] libPath = Util.getenv("LD_LIBRARY_PATH").split(":");
            if (Arrays.stream(libPath).filter(s -> !s.isEmpty()).map( arg0 -> fileSystem.getPath( arg0, new String[0])).noneMatch(p -> Util.isSameFile(p, p2))) {
                Log.error("On Linux, you *HAVE* to add the .minecraft folder to LD_LIBRARY_PATH in order for MCEF to work.", new Object[0]);
                Log.error("You can do this by running the following command and then starting Minecraft within the same terminal:", new Object[0]);
                Log.error("export \"LD_LIBRARY_PATH=$LD_LIBRARY_PATH:%s\"", ClientProxy.ROOT);
                Log.error("", new Object[0]);
                Log.error("Since this has not been done yet, MCEF will now enter virtual mode and WILL NOT WORK.", new Object[0]);
                Log.error("For more info, please read %s", "https://montoyo.net/wdwiki/Linux");
                Log.error("Please don't post a GitHub issue for this.", new Object[0]);
                final int ans = JOptionPane.showConfirmDialog(null, "A bug on Linux requires you to add the Minecraft folder to LD_LIBRARY_PATH.\nThis has not been done, so MCEF will not work for now.\nWould you like to open the wiki page?", "MCEF Linux", 0);
                if (ans == 0) {
                    try {
                        Runtime.getRuntime().exec("xdg-open https://montoyo.net/wdwiki/Linux");
                    }
                    catch (final IOException ex) {
                        Log.errorEx("Could not open wiki page", ex, new Object[0]);
                        JOptionPane.showMessageDialog(null, "Couldn't automatically open the wiki page. The link is:\nhttps://montoyo.net/wdwiki/Linux", "MCEF Linux", 0);
                    }
                }
                ClientProxy.VIRTUAL = true;
                return;
            }
        }
        System.out.println("Client init!! 5");
        String exeSuffix;
        if (OS.isWindows()) {
            exeSuffix = ".exe";
        }
        else {
            exeSuffix = "";
        }
        final File subproc = new File(ClientProxy.ROOT, "jcef_helper" + exeSuffix);
        if (OS.isLinux() && !subproc.canExecute()) {
            try {
                final int retCode = Runtime.getRuntime().exec(new String[] { "/usr/bin/chmod", "+x", subproc.getAbsolutePath() }).waitFor();
                if (retCode != 0) {
                    throw new RuntimeException("chmod exited with code " + retCode);
                }
            }
            catch (final Throwable t) {
                Log.errorEx("Error while giving execution rights to jcef_helper. MCEF will probably enter virtual mode. You can fix this by chmoding jcef_helper manually.", t, new Object[0]);
            }
        }
        final CefSettings settings = new CefSettings();
        settings.windowless_rendering_enabled = true;
        settings.background_color = settings.new ColorType(0, 255, 255, 255);
        settings.locales_dir_path = new File(ClientProxy.ROOT, "MCEFLocales").getAbsolutePath();
        settings.cache_path = new File(ClientProxy.ROOT, "MCEFCache").getAbsolutePath();
        settings.browser_subprocess_path = subproc.getAbsolutePath();
        try {
            final ArrayList<String> libs = new ArrayList<String>();
            if (OS.isWindows()) {
                libs.add("d3dcompiler_47.dll");
                libs.add("libGLESv2.dll");
                libs.add("libEGL.dll");
                libs.add("chrome_elf.dll");
                libs.add("libcef.dll");
                libs.add("jcef.dll");
            }
            else {
                libs.add("libcef.so");
                libs.add("libjcef.so");
            }
            for (final String lib : libs) {
                File f = new File(ClientProxy.ROOT, lib);
                try {
                    f = f.getCanonicalFile();
                }
                catch (final IOException ex2) {
                    f = f.getAbsoluteFile();
                }
                System.load(f.getPath());
            }
            System.out.println("Client init!! 6");
            CefApp.startup();
            this.cefApp = CefApp.getInstance(settings);
            this.loadMimeTypeMapping();
            CefApp.addAppHandler(this.appHandler);
            this.cefClient = this.cefApp.createClient();
        }
        catch (final Throwable t2) {
            Log.error("Going in virtual mode; couldn't initialize CEF.", new Object[0]);
            t2.printStackTrace();
            ClientProxy.VIRTUAL = true;
            return;
        }
        Log.info(this.cefApp.getVersion().toString(), new Object[0]);
        this.cefRouter = CefMessageRouter.create(new CefMessageRouter.CefMessageRouterConfig("mcefQuery", "mcefCancel"));
        this.cefClient.addMessageRouter(this.cefRouter);
        this.cefClient.addDisplayHandler(this.displayHandler);
        this.cefClient.addLifeSpanHandler(new CefLifeSpanHandlerAdapter() {
            @Override
            public boolean doClose(final CefBrowser browser) {
                browser.close(true);
                return false;
            }
        });
        System.out.println("Client init!! 7");
        if (MCEF.ENABLE_EXAMPLE) {
            this.exampleMod.onInit();
        }
        Log.info("MCEF loaded successfuly.", new Object[0]);
    }
    
    public CefApp getCefApp() {
        return this.cefApp;
    }
    
    @Override
    public IBrowser createBrowser(final String url, final boolean transp) {
        if (ClientProxy.VIRTUAL) {
            return new VirtualBrowser();
        }
        final CefBrowserOsr ret = (CefBrowserOsr)this.cefClient.createBrowser(url, true, transp);
        ret.setCloseAllowed();
        ret.createImmediately();
        this.browsers.add(ret);
        return ret;
    }
    
    @Override
    public void registerDisplayHandler(final IDisplayHandler idh) {
        this.displayHandler.addHandler(idh);
    }
    
    @Override
    public boolean isVirtual() {
        return ClientProxy.VIRTUAL;
    }
    
    @Override
    public void openExampleBrowser(final String url) {
        if (MCEF.ENABLE_EXAMPLE) {
            this.exampleMod.showScreen(url);
        }
    }
    
    @Override
    public void registerJSQueryHandler(final IJSQueryHandler iqh) {
        if (!ClientProxy.VIRTUAL) {
            this.cefRouter.addHandler(new MessageRouter(iqh), false);
        }
    }
    
    @Override
    public void registerScheme(final String name, final Class<? extends IScheme> schemeClass, final boolean std, final boolean local, final boolean displayIsolated, final boolean secure, final boolean corsEnabled, final boolean cspBypassing, final boolean fetchEnabled) {
        this.appHandler.registerScheme(name, schemeClass, std, local, displayIsolated, secure, corsEnabled, cspBypassing, fetchEnabled);
    }
    
    @Override
    public boolean isSchemeRegistered(final String name) {
        return this.appHandler.isSchemeRegistered(name);
    }
    
    @EventTarget
    public void onTick(final GameLoopEvent ev) {
        this.mc.mcProfiler.startSection("MCEF");
        if (this.cefApp != null) {
            this.cefApp.N_DoMessageLoopWork();
        }
        for (final CefBrowserOsr b : this.browsers) {
            b.mcefUpdate();
        }
        this.displayHandler.update();
        this.mc.mcProfiler.endSection();
    }
    
    public void removeBrowser(final CefBrowserOsr b) {
        this.browsers.remove(b);
    }
    
    @Override
    public IBrowser createBrowser(final String url) {
        return this.createBrowser(url, false);
    }
    
    private void runMessageLoopFor(final long ms) {
        final long start = System.currentTimeMillis();
        do {
            this.cefApp.N_DoMessageLoopWork();
        } while (System.currentTimeMillis() - start < ms);
    }
    
    @Override
    public void onShutdown() {
        if (ClientProxy.VIRTUAL) {
            return;
        }
        Log.info("Shutting down JCEF...", new Object[0]);
        CefBrowserOsr.CLEANUP = false;
        for (final CefBrowserOsr b : this.browsers) {
            b.close();
        }
        this.browsers.clear();
        if (MCEF.CHECK_VRAM_LEAK) {
            CefRenderer.dumpVRAMLeak();
        }
        this.runMessageLoopFor(100L);
        CefApp.forceShutdownState();
        this.cefClient.dispose();
        if (MCEF.SHUTDOWN_JCEF) {
            this.cefApp.N_Shutdown();
        }
    }
    
    public void loadMimeTypeMapping() {
        final Pattern p = Pattern.compile("^(\\S+)\\s+(\\S+)\\s*(\\S*)\\s*(\\S*)$");
        String line = "";
        int cLine = 0;
        this.mimeTypeMap.clear();
        try {
            final BufferedReader br = new BufferedReader(new InputStreamReader(ClientProxy.class.getResourceAsStream("/assets/mcef/mime.types")));
            while (true) {
                ++cLine;
                line = br.readLine();
                if (line == null) {
                    break;
                }
                line = line.trim();
                if (line.startsWith("#")) {
                    continue;
                }
                final Matcher m = p.matcher(line);
                if (!m.matches()) {
                    continue;
                }
                this.mimeTypeMap.put(m.group(2), m.group(1));
                if (m.groupCount() < 4 || m.group(3).isEmpty()) {
                    continue;
                }
                this.mimeTypeMap.put(m.group(3), m.group(1));
                if (m.groupCount() < 5 || m.group(4).isEmpty()) {
                    continue;
                }
                this.mimeTypeMap.put(m.group(4), m.group(1));
            }
            Util.close(br);
        }
        catch (final Throwable e) {
            Log.error("[Mime Types] Error while parsing \"%s\" at line %d:", line, cLine);
            e.printStackTrace();
        }
        Log.info("Loaded %d mime types", this.mimeTypeMap.size());
    }
    
    @Override
    public String mimeTypeFromExtension(String ext) {
        ext = ext.toLowerCase();
        final String ret = this.mimeTypeMap.get(ext);
        if (ret != null) {
            return ret;
        }
        final String s;
        switch ((s = ext).hashCode()) {
            case 3401: {
                if (!s.equals("js")) {
                    return null;
                }
                return "text/javascript";
            }
            case 98819: {
                if (!s.equals("css")) {
                    return null;
                }
                return "text/css";
            }
            case 102340: {
                if (!s.equals("gif")) {
                    return null;
                }
                return "image/gif";
            }
            case 103649: {
                if (!s.equals("htm")) {
                    return null;
                }
                break;
            }
            case 105441: {
                if (!s.equals("jpg")) {
                    return null;
                }
                return "image/jpeg";
            }
            case 111145: {
                if (!s.equals("png")) {
                    return null;
                }
                return "image/png";
            }
            case 114276: {
                if (!s.equals("svg")) {
                    return null;
                }
                return "image/svg+xml";
            }
            case 115312: {
                if (!s.equals("txt")) {
                    return null;
                }
                return "text/plain";
            }
            case 118807: {
                if (!s.equals("xml")) {
                    return null;
                }
                return "text/xml";
            }
            case 3213227: {
                if (!s.equals("html")) {
                    return null;
                }
                break;
            }
            case 3268712: {
                if (!s.equals("jpeg")) {
                    return null;
                }
                return "image/jpeg";
            }
        }
        return "text/html";
    }
}
