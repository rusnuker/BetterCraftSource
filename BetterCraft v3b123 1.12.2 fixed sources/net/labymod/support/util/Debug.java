// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.support.util;

import net.labymod.utils.Consumer;
import org.apache.logging.log4j.LogManager;
import net.labymod.support.DebugConsoleGui;
import org.apache.logging.log4j.Logger;
import java.io.File;

public class Debug
{
    public static final File DEBUG_FILE;
    private static boolean active;
    private static final Logger logger;
    private static DebugConsoleGui debugConsoleGui;
    
    static {
        DEBUG_FILE = new File("LabyMod/", ".debug");
        Debug.active = false;
        logger = LogManager.getLogger();
    }
    
    public static void init() {
        if (Debug.DEBUG_FILE.exists()) {
            Debug.DEBUG_FILE.delete();
            openDebugConsole();
        }
        else {
            final String debugMode = System.getProperty("debugMode");
            if (debugMode != null) {
                if (debugMode.equals("true")) {
                    Debug.active = true;
                    Debug.logger.info("[Debug] Started debug logging");
                }
                else if (debugMode.equals("GUI")) {
                    openDebugConsole();
                }
                else if (!debugMode.equals("false")) {
                    Debug.logger.info("[Debug] Invalid debug mode: " + debugMode);
                }
            }
        }
    }
    
    public static boolean isActive() {
        return Debug.active;
    }
    
    public static void openDebugConsole() {
        if (Debug.debugConsoleGui != null) {
            Debug.debugConsoleGui.toFront();
            return;
        }
        Debug.debugConsoleGui = new DebugConsoleGui(new Consumer<Boolean>() {
            @Override
            public void accept(final Boolean accepted) {
                Debug.access$1(null);
            }
        });
        Debug.active = true;
        Debug.logger.info("[Debug] Started debug GUI");
    }
    
    public static void log(final EnumDebugMode debugMode, final String message) {
        if (!Debug.active) {
            return;
        }
        Debug.logger.info("{}", "[Debug] [" + debugMode.name() + "] " + message);
    }
    
    public static DebugConsoleGui getDebugConsoleGui() {
        return Debug.debugConsoleGui;
    }
    
    static /* synthetic */ void access$1(final DebugConsoleGui debugConsoleGui) {
        Debug.debugConsoleGui = debugConsoleGui;
    }
    
    public enum EnumDebugMode
    {
        ADDON("ADDON", 0), 
        API("API", 1), 
        UPDATER("UPDATER", 2), 
        COSMETIC_IMAGE_MANAGER("COSMETIC_IMAGE_MANAGER", 3), 
        USER_MANAGER("USER_MANAGER", 4), 
        MINECRAFT("MINECRAFT", 5), 
        TEAMSPEAK("TEAMSPEAK", 6), 
        LABYMOD_CHAT("LABYMOD_CHAT", 7), 
        ACCOUNT_MANAGER("ACCOUNT_MANAGER", 8), 
        CONFIG_MANAGER("CONFIG_MANAGER", 9), 
        ASM("ASM", 10), 
        PLUGINMESSAGE("PLUGINMESSAGE", 11), 
        GENERAL("GENERAL", 12), 
        LANGUAGE("LANGUAGE", 13), 
        CCP("CCP", 14), 
        EMOTE("EMOTE", 15), 
        STICKER("STICKER", 16), 
        DISCORD("DISCORD", 17), 
        LABY_PLAY("LABY_PLAY", 18);
        
        private EnumDebugMode(final String s, final int n) {
        }
    }
}
