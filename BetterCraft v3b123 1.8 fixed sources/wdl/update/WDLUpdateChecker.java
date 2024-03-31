// 
// Decompiled by Procyon v0.6.0
// 

package wdl.update;

import java.util.Map;
import java.util.Arrays;
import java.util.HashMap;
import net.minecraft.util.IChatComponent;
import wdl.api.IWDLMessageType;
import wdl.WDLMessages;
import wdl.WDLMessageTypes;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ChatComponentTranslation;
import java.util.Iterator;
import wdl.WDL;
import java.util.List;

public class WDLUpdateChecker extends Thread
{
    private static volatile boolean started;
    private static volatile boolean finished;
    private static volatile boolean failed;
    private static volatile String failReason;
    private static volatile List<Release> releases;
    private static volatile Release runningRelease;
    private static final String FORUMS_THREAD_USAGE_LINK = "http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/2520465#Usage";
    private static final String WIKI_LINK = "https://github.com/pokechu22/WorldDownloader/wiki";
    private static final String GITHUB_LINK = "https://github.com/pokechu22/WorldDownloader";
    private static final String REDISTRIBUTION_LINK = "http://pokechu22.github.io/WorldDownloader/redistribution";
    private static final String SMR_LINK = "http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/mods-discussion/2314237";
    
    static {
        WDLUpdateChecker.started = false;
        WDLUpdateChecker.finished = false;
        WDLUpdateChecker.failed = false;
        WDLUpdateChecker.failReason = null;
    }
    
    public static List<Release> getReleases() {
        return WDLUpdateChecker.releases;
    }
    
    public static Release getRunningRelease() {
        return WDLUpdateChecker.runningRelease;
    }
    
    public static Release getRecomendedRelease() {
        if (WDLUpdateChecker.releases == null) {
            return null;
        }
        if (WDLUpdateChecker.runningRelease == null) {
            return null;
        }
        final String mcVersion = WDL.getMinecraftVersion();
        final boolean usePrereleases = WDL.globalProps.getProperty("UpdateAllowBetas").equals("true");
        final boolean versionMustBeExact = WDL.globalProps.getProperty("UpdateMinecraftVersion").equals("client");
        final boolean versionMustBeCompatible = WDL.globalProps.getProperty("UpdateMinecraftVersion").equals("server");
        for (final Release release : WDLUpdateChecker.releases) {
            if (release.hiddenInfo != null) {
                if (release.prerelease && !usePrereleases) {
                    continue;
                }
                if (versionMustBeExact) {
                    if (!release.hiddenInfo.mainMinecraftVersion.equals(mcVersion)) {
                        continue;
                    }
                }
                else if (versionMustBeCompatible) {
                    boolean foundCompatible = false;
                    String[] supportedMinecraftVersions;
                    for (int length = (supportedMinecraftVersions = release.hiddenInfo.supportedMinecraftVersions).length, i = 0; i < length; ++i) {
                        final String version = supportedMinecraftVersions[i];
                        if (version.equals(mcVersion)) {
                            foundCompatible = true;
                            break;
                        }
                    }
                    if (!foundCompatible) {
                        continue;
                    }
                }
                if (WDLUpdateChecker.releases.indexOf(release) > WDLUpdateChecker.releases.indexOf(WDLUpdateChecker.runningRelease)) {
                    continue;
                }
                return release;
            }
        }
        return null;
    }
    
    public static boolean hasNewVersion() {
        if (WDLUpdateChecker.runningRelease == null) {
            return false;
        }
        final Release recomendedRelease = getRecomendedRelease();
        return recomendedRelease != null && WDLUpdateChecker.runningRelease != recomendedRelease;
    }
    
    public static void startIfNeeded() {
        if (!WDLUpdateChecker.started) {
            WDLUpdateChecker.started = true;
            new WDLUpdateChecker().start();
        }
    }
    
    public static boolean hasFinishedUpdateCheck() {
        return WDLUpdateChecker.finished;
    }
    
    public static boolean hasUpdateCheckFailed() {
        return WDLUpdateChecker.failed;
    }
    
    public static String getUpdateCheckFailReason() {
        return WDLUpdateChecker.failReason;
    }
    
    private WDLUpdateChecker() {
        super("WorldDownloader update check thread");
    }
    
    @Override
    public void run() {
        try {
            if (!WDL.globalProps.getProperty("TutorialShown").equals("true")) {
                Thread.sleep(5000L);
                final ChatComponentTranslation success = new ChatComponentTranslation("wdl.intro.success", new Object[0]);
                final ChatComponentTranslation mcfThread = new ChatComponentTranslation("wdl.intro.forumsLink", new Object[0]);
                mcfThread.getChatStyle().setColor(EnumChatFormatting.BLUE).setUnderlined(true).setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/2520465#Usage"));
                final ChatComponentTranslation wikiLink = new ChatComponentTranslation("wdl.intro.wikiLink", new Object[0]);
                wikiLink.getChatStyle().setColor(EnumChatFormatting.BLUE).setUnderlined(true).setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/pokechu22/WorldDownloader/wiki"));
                final ChatComponentTranslation usage = new ChatComponentTranslation("wdl.intro.usage", new Object[] { mcfThread, wikiLink });
                final ChatComponentTranslation githubRepo = new ChatComponentTranslation("wdl.intro.githubRepo", new Object[0]);
                githubRepo.getChatStyle().setColor(EnumChatFormatting.BLUE).setUnderlined(true).setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/pokechu22/WorldDownloader"));
                final ChatComponentTranslation contribute = new ChatComponentTranslation("wdl.intro.contribute", new Object[] { githubRepo });
                final ChatComponentTranslation redistributionList = new ChatComponentTranslation("wdl.intro.redistributionList", new Object[0]);
                redistributionList.getChatStyle().setColor(EnumChatFormatting.BLUE).setUnderlined(true).setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "http://pokechu22.github.io/WorldDownloader/redistribution"));
                final ChatComponentTranslation warning = new ChatComponentTranslation("wdl.intro.warning", new Object[0]);
                warning.getChatStyle().setColor(EnumChatFormatting.DARK_RED).setBold(true);
                final ChatComponentTranslation illegally = new ChatComponentTranslation("wdl.intro.illegally", new Object[0]);
                illegally.getChatStyle().setColor(EnumChatFormatting.DARK_RED).setBold(true);
                final ChatComponentTranslation stolen = new ChatComponentTranslation("wdl.intro.stolen", new Object[] { warning, redistributionList, illegally });
                final ChatComponentTranslation smr = new ChatComponentTranslation("wdl.intro.stopModReposts", new Object[0]);
                smr.getChatStyle().setColor(EnumChatFormatting.BLUE).setUnderlined(true).setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/mods-discussion/2314237"));
                final ChatComponentTranslation stolenBeware = new ChatComponentTranslation("wdl.intro.stolenBeware", new Object[] { smr });
                WDLMessages.chatMessage(WDLMessageTypes.UPDATES, success);
                WDLMessages.chatMessage(WDLMessageTypes.UPDATES, usage);
                WDLMessages.chatMessage(WDLMessageTypes.UPDATES, contribute);
                WDLMessages.chatMessage(WDLMessageTypes.UPDATES, stolen);
                WDLMessages.chatMessage(WDLMessageTypes.UPDATES, stolenBeware);
                WDL.globalProps.setProperty("TutorialShown", "true");
                WDL.saveGlobalProps();
            }
            Thread.sleep(5000L);
            WDLUpdateChecker.releases = GithubInfoGrabber.getReleases();
            WDLMessages.chatMessageTranslated(WDLMessageTypes.UPDATE_DEBUG, "wdl.messages.updates.releaseCount", WDLUpdateChecker.releases.size());
            if (WDLUpdateChecker.releases.isEmpty()) {
                WDLUpdateChecker.failed = true;
                WDLUpdateChecker.failReason = "No releases found.";
                return;
            }
            for (int i = 0; i < WDLUpdateChecker.releases.size(); ++i) {
                final Release release = WDLUpdateChecker.releases.get(i);
                if (release.tag.equalsIgnoreCase("1.8.9a-beta2")) {
                    WDLUpdateChecker.runningRelease = release;
                }
            }
            if (WDLUpdateChecker.runningRelease == null) {
                WDLMessages.chatMessageTranslated(WDLMessageTypes.UPDATE_DEBUG, "wdl.messages.updates.failedToFindMatchingRelease", "1.8.9a-beta2");
                return;
            }
            if (hasNewVersion()) {
                final Release recomendedRelease = getRecomendedRelease();
                final ChatComponentTranslation updateLink = new ChatComponentTranslation("wdl.messages.updates.newRelease.updateLink", new Object[0]);
                updateLink.getChatStyle().setColor(EnumChatFormatting.BLUE).setUnderlined(true).setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, recomendedRelease.URL));
                WDLMessages.chatMessageTranslated(WDLMessageTypes.UPDATES, "wdl.messages.updates.newRelease", WDLUpdateChecker.runningRelease.tag, recomendedRelease.tag, updateLink);
            }
            if (WDLUpdateChecker.runningRelease.hiddenInfo == null) {
                WDLMessages.chatMessageTranslated(WDLMessageTypes.UPDATE_DEBUG, "wdl.messages.updates.failedToFindMetadata", "1.8.9a-beta2");
                return;
            }
            final Map<Release.HashData, Object> failed = new HashMap<Release.HashData, Object>();
            Release.HashData[] hashes;
        Label_0963:
            for (int length = (hashes = WDLUpdateChecker.runningRelease.hiddenInfo.hashes).length, j = 0; j < length; ++j) {
                final Release.HashData data = hashes[j];
                try {
                    final String hash = ClassHasher.hash(data.relativeTo, data.file);
                    String[] validHashes;
                    for (int length2 = (validHashes = data.validHashes).length, k = 0; k < length2; ++k) {
                        final String validHash = validHashes[k];
                        if (validHash.equalsIgnoreCase(hash)) {
                            continue Label_0963;
                        }
                    }
                    WDLMessages.chatMessageTranslated(WDLMessageTypes.UPDATE_DEBUG, "wdl.messages.updates.incorrectHash", data.file, data.relativeTo, Arrays.toString(data.validHashes), hash);
                    failed.put(data, hash);
                }
                catch (final Exception e) {
                    WDLMessages.chatMessageTranslated(WDLMessageTypes.UPDATE_DEBUG, "wdl.messages.updates.hashException", data.file, data.relativeTo, Arrays.toString(data.validHashes), e);
                    failed.put(data, e);
                }
            }
            if (failed.size() > 0) {
                final ChatComponentTranslation mcfThread = new ChatComponentTranslation("wdl.intro.forumsLink", new Object[0]);
                mcfThread.getChatStyle().setColor(EnumChatFormatting.BLUE).setUnderlined(true).setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/2520465#Usage"));
                WDLMessages.chatMessageTranslated(WDLMessageTypes.UPDATES, "wdl.messages.updates.badHashesFound", mcfThread);
            }
        }
        catch (final Exception e2) {
            WDLMessages.chatMessageTranslated(WDLMessageTypes.UPDATE_DEBUG, "wdl.messages.updates.updateCheckError", e2);
            WDLUpdateChecker.failed = true;
            WDLUpdateChecker.failReason = e2.toString();
            return;
        }
        finally {
            WDLUpdateChecker.finished = true;
        }
        WDLUpdateChecker.finished = true;
    }
}
