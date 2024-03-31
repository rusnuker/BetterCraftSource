// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.main;

import net.minecraft.realms.RealmsSharedConstants;
import java.io.File;

public class Source
{
    public static final String ABOUT_VERSION_TYPE = "";
    public static final String ABOUT_MODID = "labymod";
    public static final String ABOUT_VERSION = "3.6.6";
    public static final String ABOUT_MC_VERSION;
    public static final int ABOUT_MC_PROTOCOL_VERSION;
    public static final String PROFILE_VERSION_NAME;
    public static File RUNNING_JAR;
    public static final String FILE_STEPS_TITLES = "assets/minecraft/labymod/data/steps.titles";
    public static final String FILE_SYMBOLS = "assets/minecraft/labymod/data/symbols.txt";
    public static final String FILE_ICON_16 = "assets/minecraft/labymod/data/icons/icon_16x16.png";
    public static final String FILE_ICON_32 = "assets/minecraft/labymod/data/icons/icon_32x32.png";
    public static final String FILE_ICON_NOTIFY_16 = "assets/minecraft/labymod/data/icons/icon_notify_16x16.png";
    public static final String FILE_ICON_NOTIFY_32 = "assets/minecraft/labymod/data/icons/icon_notify_32x32.png";
    public static final String FILE_LABYMOD_FOLDER = "LabyMod/";
    public static final String FILE_MODS_FOLDER = "mods/";
    public static final String FILE_LANGUAGE_FOLDER = "/assets/minecraft/labymod/lang/";
    public static final String FILE_CCP_FOLDER = "LabyMod/ccp/";
    public static final String FILE_CONFIG = "LabyMod/LabyMod-3.json";
    public static final String URL_USERDATA = "http://dl.labymod.net/userdata/%s.json";
    public static final String URL_USER_WHITELIST = "http://dl.labymod.net/whitelist.bin";
    public static final String URL_EMOTEDATA = "http://dl.labymod.net/emotes/emotedata";
    public static final String URL_FAMILIAR_USERS = "http://dl.labymod.net/familiar.csv";
    public static final String URL_GROUPS = "http://dl.labymod.net/groups.json";
    public static final String URL_STICKERS = "http://dl.labymod.net/stickers.json";
    public static final String URL_CUSTOM_TEXTURES = "http://dl.labymod.net/textures/%s";
    public static final String URL_CAPE_REPORT = "http://api.labymod.net/capes/capeReport.php";
    public static final String URL_HASTEBIN = "https://paste.labymod.net/";
    public static final String URL_HASTEBIN_API = "https://paste.labymod.net/documents";
    public static final String URL_SERVER_LIST = "http://dl.labymod.net/public_servers.json";
    public static final String URL_SERVER_LIST_JOIN = "http://api.labymod.net/serverlist/join.php?server=%s";
    public static final String URL_ADVERTISEMENT_ENTRIES = "http://dl.labymod.net/advertisement/entries.json";
    public static final String URL_ADVERTISEMENT_ICONS = "http://dl.labymod.net/advertisement/icons/%s.png";
    public static final String URL_VERSIONS = "http://dl.labymod.net/versions.json";
    public static final String URL_UPDATER = "http://dl.labymod.net/latest/install/updater.jar";
    public static final String URL_OFHANDLER = "http://dl.labymod.net/latest/install/ofhandler.jar";
    public static final String FILE_UPDATER = "LabyMod/Updater.jar";
    public static final String FILE_OFHANDLER_FOLDER = "LabyMod/ofhandler/";
    public static final String URL_ADDONS = "http://dl.labymod.net/addons.json";
    public static final String URL_ADDON_DOWNLOAD = "http://dl.labymod.net/latest/?file=%s&a=1";
    public static final String URL_ADDON_TEXTURE = "http://dl.labymod.net/latest/addons/%s/icon.png";
    public static final String URL_DISABLED_ADDONS = "http://dl.labymod.net/disabled_addons.json";
    @Deprecated
    public static final String URL_MINOTAR = "https://minotar.net/helm/%s/16.png";
    public static final String URL_DASHBOARD_LOGIN = "http://www.labymod.net/key/?id=%s&pin=%s";
    public static final String URL_DASHBOARD = "http://www.labymod.net/dashboard";
    public static final String FILE_CHATLOG = "LabyMod/chatlog/%s.log";
    public static final String CHATSERVER_IP = "mod.labymod.net";
    public static final int CHATSERVER_PORT = 30336;
    public static final int CHATSERVER_MAX_MESSAGES = 300;
    public static final short CHATSERVER_PACKET_VERSION = 23;
    public static final String URL_SUBSCRIBER_API = "http://dl.labymod.net/subcounter.json";
    public static final String SOCIALBLADE_API_CHANNEL_PAGE = "https://socialblade.com/youtube/";
    public static final String URL_DISCORD_LIBRARY = "http://dl.labymod.net/latest/install/discord/%s.%s";
    public static final String URL_DISCORD_IMAGE = "https://cdn.discordapp.com/avatars/%s/%s.png";
    
    static {
        ABOUT_MC_VERSION = RealmsSharedConstants.VERSION_STRING;
        ABOUT_MC_PROTOCOL_VERSION = RealmsSharedConstants.NETWORK_PROTOCOL_VERSION;
        PROFILE_VERSION_NAME = "LabyMod-3-" + Source.ABOUT_MC_VERSION;
        Source.RUNNING_JAR = null;
    }
    
    public static String getUserAgent() {
        return "LabyMod v3.6.6" + ("".isEmpty() ? "" : " ") + " on mc" + Source.ABOUT_MC_VERSION;
    }
    
    public static String getMajorVersion() {
        final String versionSplit = Source.ABOUT_MC_VERSION.replaceFirst("\\.", "");
        return versionSplit.contains(".") ? versionSplit.split("\\.")[0] : versionSplit;
    }
}
