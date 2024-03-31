// 
// Decompiled by Procyon v0.6.0
// 

package org.cef;

import java.io.FilenameFilter;
import java.io.File;
import javax.swing.SwingUtilities;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.cef.callback.CefSchemeHandlerFactory;
import java.util.Iterator;
import java.util.Collection;
import java.util.HashSet;
import javax.swing.Timer;
import org.cef.handler.CefAppHandler;
import org.cef.handler.CefAppHandlerAdapter;

public class CefApp extends CefAppHandlerAdapter
{
    private static CefApp self;
    private static CefAppHandler appHandler_;
    private static CefAppState state_;
    private Timer workTimer_;
    private HashSet<CefClient> clients_;
    private CefSettings settings_;
    
    static {
        CefApp.self = null;
        CefApp.appHandler_ = null;
        CefApp.state_ = CefAppState.NONE;
    }
    
    private CefApp(final String[] args, final CefSettings settings) throws UnsatisfiedLinkError {
        super(args);
        this.workTimer_ = null;
        this.clients_ = new HashSet<CefClient>();
        this.settings_ = null;
        if (settings != null) {
            this.settings_ = settings.clone();
        }
        if (CefApp.appHandler_ == null) {
            CefApp.appHandler_ = this;
        }
        try {
            final Runnable r = new Runnable() {
                @Override
                public void run() {
                    if (!CefApp.this.N_PreInitialize()) {
                        throw new IllegalStateException("Failed to pre-initialize native code");
                    }
                }
            };
            r.run();
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void addAppHandler(final CefAppHandler appHandler) throws IllegalStateException {
        if (getState().compareTo(CefAppState.NEW) > 0) {
            throw new IllegalStateException("Must be called before CefApp is initialized");
        }
        CefApp.appHandler_ = appHandler;
    }
    
    public static synchronized CefApp getInstance() throws UnsatisfiedLinkError {
        return getInstance(null, null);
    }
    
    public static synchronized CefApp getInstance(final String[] args) throws UnsatisfiedLinkError {
        return getInstance(args, null);
    }
    
    public static synchronized CefApp getInstance(final CefSettings settings) throws UnsatisfiedLinkError {
        return getInstance(null, settings);
    }
    
    public static synchronized CefApp getInstance(final String[] args, final CefSettings settings) throws UnsatisfiedLinkError {
        if (settings != null && getState() != CefAppState.NONE && getState() != CefAppState.NEW) {
            throw new IllegalStateException("Settings can only be passed to CEF before createClient is called the first time.");
        }
        if (CefApp.self == null) {
            if (getState() == CefAppState.TERMINATED) {
                throw new IllegalStateException("CefApp was terminated");
            }
            CefApp.self = new CefApp(args, settings);
            setState(CefAppState.NEW);
        }
        return CefApp.self;
    }
    
    public final void setSettings(final CefSettings settings) throws IllegalStateException {
        if (getState() != CefAppState.NONE && getState() != CefAppState.NEW) {
            throw new IllegalStateException("Settings can only be passed to CEF before createClient is called the first time.");
        }
        this.settings_ = settings.clone();
    }
    
    public final CefVersion getVersion() {
        try {
            return this.N_GetVersion();
        }
        catch (final UnsatisfiedLinkError ule) {
            ule.printStackTrace();
            return null;
        }
    }
    
    public static final CefAppState getState() {
        synchronized (CefApp.state_) {
            final CefAppState state_ = CefApp.state_;
            monitorexit(CefApp.state_);
            return state_;
        }
    }
    
    private static final void setState(final CefAppState state) {
        synchronized (CefApp.state_) {
            monitorexit(CefApp.state_ = state);
        }
        if (CefApp.appHandler_ != null) {
            CefApp.appHandler_.stateHasChanged(state);
        }
    }
    
    public static final void forceShutdownState() {
        synchronized (CefApp.state_) {
            monitorexit(CefApp.state_ = CefAppState.SHUTTING_DOWN);
        }
    }
    
    public final synchronized void dispose() {
        switch (getState()) {
            case NEW: {
                setState(CefAppState.TERMINATED);
                break;
            }
            case INITIALIZING:
            case INITIALIZED: {
                setState(CefAppState.SHUTTING_DOWN);
                if (this.clients_.isEmpty()) {
                    this.shutdown();
                    break;
                }
                final HashSet<CefClient> clients = new HashSet<CefClient>(this.clients_);
                for (final CefClient c : clients) {
                    c.dispose();
                }
                break;
            }
        }
    }
    
    public synchronized CefClient createClient() {
        switch (getState()) {
            case NEW: {
                setState(CefAppState.INITIALIZING);
                this.initialize();
            }
            case INITIALIZING:
            case INITIALIZED: {
                final CefClient client = new CefClient();
                this.clients_.add(client);
                return client;
            }
            default: {
                throw new IllegalStateException("Can't crate client in state " + CefApp.state_);
            }
        }
    }
    
    public boolean registerSchemeHandlerFactory(final String schemeName, final String domainName, final CefSchemeHandlerFactory factory) {
        try {
            return this.N_RegisterSchemeHandlerFactory(schemeName, domainName, factory);
        }
        catch (final Exception err) {
            err.printStackTrace();
            return false;
        }
    }
    
    public boolean clearSchemeHandlerFactories() {
        try {
            return this.N_ClearSchemeHandlerFactories();
        }
        catch (final Exception err) {
            err.printStackTrace();
            return false;
        }
    }
    
    protected final synchronized void clientWasDisposed(final CefClient client) {
        this.clients_.remove(client);
        if (this.clients_.isEmpty() && getState().compareTo(CefAppState.SHUTTING_DOWN) >= 0) {
            this.shutdown();
        }
    }
    
    private final void initialize() {
        try {
            final Runnable r = new Runnable() {
                @Override
                public void run() {
                    final String library_path = CefApp.this.getJcefLibPath();
                    System.out.println("initialize on " + Thread.currentThread() + " with library path " + library_path);
                    final CefSettings settings = (CefApp.this.settings_ != null) ? CefApp.this.settings_ : new CefSettings();
                    if (OS.isMacintosh()) {
                        if (settings.browser_subprocess_path == null) {
                            final Path path = Paths.get(library_path, "../Frameworks/jcef Helper.app/Contents/MacOS/jcef Helper");
                            settings.browser_subprocess_path = path.normalize().toAbsolutePath().toString();
                        }
                    }
                    else if (OS.isWindows()) {
                        if (settings.browser_subprocess_path == null) {
                            settings.browser_subprocess_path = String.valueOf(library_path) + "\\jcef_helper.exe";
                        }
                    }
                    else if (OS.isLinux()) {
                        if (settings.browser_subprocess_path == null) {
                            settings.browser_subprocess_path = String.valueOf(library_path) + "/jcef_helper";
                        }
                        if (settings.resources_dir_path == null) {
                            settings.resources_dir_path = library_path;
                        }
                        if (settings.locales_dir_path == null) {
                            settings.locales_dir_path = String.valueOf(library_path) + "/locales";
                        }
                    }
                    if (CefApp.this.N_Initialize(library_path, CefApp.appHandler_, settings)) {
                        setState(CefAppState.INITIALIZED);
                    }
                }
            };
            r.run();
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
    
    protected final void handleBeforeTerminate() {
        System.out.println("Cmd+Q termination request.");
        final CefAppHandler handler = (CefApp.appHandler_ == null) ? this : CefApp.appHandler_;
        if (!handler.onBeforeTerminate()) {
            this.dispose();
        }
    }
    
    private final void shutdown() {
        System.out.println("shutdown on " + Thread.currentThread());
        setState(CefAppState.TERMINATED);
        CefApp.self = null;
    }
    
    public final void doMessageLoopWork(final long delay_ms) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (CefApp.getState() == CefAppState.TERMINATED) {
                    return;
                }
                final long kMaxTimerDelay = 33L;
                if (CefApp.this.workTimer_ != null) {
                    CefApp.this.workTimer_.stop();
                    CefApp.access$8(CefApp.this, null);
                }
                if (delay_ms <= 0L) {
                    CefApp.this.N_DoMessageLoopWork();
                    CefApp.this.doMessageLoopWork(33L);
                }
                else {
                    long timer_delay_ms = delay_ms;
                    if (timer_delay_ms > 33L) {
                        timer_delay_ms = 33L;
                    }
                    CefApp.access$8(CefApp.this, new Timer((int)timer_delay_ms, new ActionListener() {
                        @Override
                        public void actionPerformed(final ActionEvent evt) {
                            CefApp.this.workTimer_.stop();
                            CefApp.access$8(CefApp.this, null);
                            CefApp.this.N_DoMessageLoopWork();
                            CefApp.this.doMessageLoopWork(33L);
                        }
                    }));
                    CefApp.this.workTimer_.start();
                }
            }
        });
    }
    
    public static final boolean startup() {
        return (!OS.isLinux() && !OS.isMacintosh()) || N_Startup();
    }
    
    private final String getJcefLibPath() {
        final String library_path = System.getProperty("java.library.path");
        final String[] paths = library_path.split(System.getProperty("path.separator"));
        String[] array;
        for (int length = (array = paths).length, i = 0; i < length; ++i) {
            final String path = array[i];
            final File dir = new File(path);
            final String[] found = dir.list(new FilenameFilter() {
                @Override
                public boolean accept(final File dir, final String name) {
                    return name.equalsIgnoreCase("libjcef.dylib") || name.equalsIgnoreCase("libjcef.so") || name.equalsIgnoreCase("jcef.dll");
                }
            });
            if (found != null && found.length != 0) {
                return path;
            }
        }
        return library_path;
    }
    
    private static final native boolean N_Startup();
    
    private final native boolean N_PreInitialize();
    
    private final native boolean N_Initialize(final String p0, final CefAppHandler p1, final CefSettings p2);
    
    public final native void N_Shutdown();
    
    public final native void N_DoMessageLoopWork();
    
    private final native CefVersion N_GetVersion();
    
    private final native boolean N_RegisterSchemeHandlerFactory(final String p0, final String p1, final CefSchemeHandlerFactory p2);
    
    private final native boolean N_ClearSchemeHandlerFactories();
    
    static /* synthetic */ void access$8(final CefApp cefApp, final Timer workTimer_) {
        cefApp.workTimer_ = workTimer_;
    }
    
    public enum CefAppState
    {
        NONE("NONE", 0), 
        NEW("NEW", 1), 
        INITIALIZING("INITIALIZING", 2), 
        INITIALIZED("INITIALIZED", 3), 
        SHUTTING_DOWN("SHUTTING_DOWN", 4), 
        TERMINATED("TERMINATED", 5);
        
        private CefAppState(final String s, final int n) {
        }
    }
    
    public final class CefVersion
    {
        public final int JCEF_COMMIT_NUMBER;
        public final int CEF_VERSION_MAJOR;
        public final int CEF_VERSION_MINOR;
        public final int CEF_VERSION_PATCH;
        public final int CEF_COMMIT_NUMBER;
        public final int CHROME_VERSION_MAJOR;
        public final int CHROME_VERSION_MINOR;
        public final int CHROME_VERSION_BUILD;
        public final int CHROME_VERSION_PATCH;
        
        private CefVersion(final int jcefCommitNo, final int cefMajor, final int cefMinor, final int cefPatch, final int cefCommitNo, final int chrMajor, final int chrMin, final int chrBuild, final int chrPatch) {
            this.JCEF_COMMIT_NUMBER = jcefCommitNo;
            this.CEF_VERSION_MAJOR = cefMajor;
            this.CEF_VERSION_MINOR = cefMinor;
            this.CEF_VERSION_PATCH = cefPatch;
            this.CEF_COMMIT_NUMBER = cefCommitNo;
            this.CHROME_VERSION_MAJOR = chrMajor;
            this.CHROME_VERSION_MINOR = chrMin;
            this.CHROME_VERSION_BUILD = chrBuild;
            this.CHROME_VERSION_PATCH = chrPatch;
        }
        
        public String getJcefVersion() {
            return String.valueOf(this.CEF_VERSION_MAJOR) + "." + this.CEF_VERSION_MINOR + "." + this.CEF_VERSION_PATCH + "." + this.JCEF_COMMIT_NUMBER;
        }
        
        public String getCefVersion() {
            return String.valueOf(this.CEF_VERSION_MAJOR) + "." + this.CEF_VERSION_MINOR + "." + this.CEF_VERSION_PATCH;
        }
        
        public String getChromeVersion() {
            return String.valueOf(this.CHROME_VERSION_MAJOR) + "." + this.CHROME_VERSION_MINOR + "." + this.CHROME_VERSION_BUILD + "." + this.CHROME_VERSION_PATCH;
        }
        
        @Override
        public String toString() {
            return "JCEF Version = " + this.getJcefVersion() + "\n" + "CEF Version = " + this.getCefVersion() + "\n" + "Chromium Version = " + this.getChromeVersion();
        }
    }
}
