// 
// Decompiled by Procyon v0.6.0
// 

package com.mojang.realmsclient;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.StringSelection;
import java.awt.Toolkit;
import com.mojang.realmsclient.util.RealmsTextureManager;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.realms.RealmsSharedConstants;
import org.lwjgl.input.Mouse;
import net.minecraft.realms.RealmsDefaultVertexFormat;
import net.minecraft.realms.Tezzelator;
import com.mojang.realmsclient.gui.screens.RealmsCreateRealmScreen;
import net.minecraft.realms.RealmsClickableScrolledSelectionList;
import org.apache.logging.log4j.LogManager;
import com.mojang.realmsclient.gui.LongRunningTask;
import com.mojang.realmsclient.gui.screens.RealmsLongRunningMcoTaskScreen;
import com.mojang.realmsclient.util.RealmsTasks;
import java.util.concurrent.TimeUnit;
import net.minecraft.realms.RealmsMth;
import com.mojang.realmsclient.util.RealmsPersistence;
import com.mojang.realmsclient.gui.screens.RealmsPendingInvitesScreen;
import org.lwjgl.opengl.GL11;
import com.mojang.realmsclient.gui.screens.RealmsLongConfirmationScreen;
import com.mojang.realmsclient.gui.screens.RealmsConfigureWorldScreen;
import com.mojang.realmsclient.gui.screens.RealmsParentalConsentScreen;
import java.io.IOException;
import com.mojang.realmsclient.exception.RealmsServiceException;
import com.mojang.realmsclient.gui.screens.RealmsGenericErrorScreen;
import com.mojang.realmsclient.gui.screens.RealmsClientOutdatedScreen;
import com.mojang.realmsclient.gui.screens.RealmsCreateTrialScreen;
import com.mojang.realmsclient.util.RealmsUtil;
import java.util.ArrayList;
import com.mojang.realmsclient.dto.RegionPingResult;
import com.mojang.realmsclient.dto.PingResult;
import com.mojang.realmsclient.client.RealmsClient;
import com.mojang.realmsclient.client.Ping;
import com.mojang.realmsclient.dto.RealmsServerPlayerLists;
import com.mojang.realmsclient.dto.RealmsServerPlayerList;
import org.lwjgl.input.Keyboard;
import java.util.Iterator;
import net.minecraft.realms.Realms;
import com.google.common.collect.Lists;
import java.util.concurrent.locks.ReentrantLock;
import com.mojang.realmsclient.dto.RealmsServer;
import java.util.List;
import net.minecraft.realms.RealmsButton;
import com.mojang.realmsclient.gui.RealmsDataFetcher;
import org.apache.logging.log4j.Logger;
import net.minecraft.realms.RealmsScreen;

public class RealmsMainScreen extends RealmsScreen
{
    private static final Logger LOGGER;
    private static boolean overrideConfigure;
    private boolean dontSetConnectedToRealms;
    protected static final int BUTTON_BACK_ID = 0;
    protected static final int BUTTON_PLAY_ID = 1;
    private static final int LEAVE_ID = 2;
    private static final int BUTTON_BUY_ID = 3;
    private static final int BUTTON_TRY_ID = 4;
    private static final String ON_ICON_LOCATION = "realms:textures/gui/realms/on_icon.png";
    private static final String OFF_ICON_LOCATION = "realms:textures/gui/realms/off_icon.png";
    private static final String EXPIRED_ICON_LOCATION = "realms:textures/gui/realms/expired_icon.png";
    private static final String EXPIRES_SOON_ICON_LOCATION = "realms:textures/gui/realms/expires_soon_icon.png";
    private static final String LEAVE_ICON_LOCATION = "realms:textures/gui/realms/leave_icon.png";
    private static final String INVITATION_ICONS_LOCATION = "realms:textures/gui/realms/invitation_icons.png";
    private static final String INVITE_ICON_LOCATION = "realms:textures/gui/realms/invite_icon.png";
    private static final String WORLDICON_LOCATION = "realms:textures/gui/realms/world_icon.png";
    private static final String LOGO_LOCATION = "realms:textures/gui/title/realms.png";
    private static final String CONFIGURE_LOCATION = "realms:textures/gui/realms/configure_icon.png";
    private static final String QUESTIONMARK_LOCATION = "realms:textures/gui/realms/questionmark.png";
    private static final String NEWS_LOCATION = "realms:textures/gui/realms/news_icon.png";
    private static final String POPUP_LOCATION = "realms:textures/gui/realms/popup.png";
    private static final String DARKEN_LOCATION = "realms:textures/gui/realms/darken.png";
    private static final String CROSS_ICON_LOCATION = "realms:textures/gui/realms/cross_icon.png";
    private static final String TRIAL_ICON_LOCATION = "realms:textures/gui/realms/trial_icon.png";
    private static final String BUTTON_LOCATION = "minecraft:textures/gui/widgets.png";
    private static final String[] IMAGES_LOCATION;
    private static final RealmsDataFetcher realmsDataFetcher;
    private static int lastScrollYPosition;
    private final RealmsScreen lastScreen;
    private volatile ServerSelectionList serverSelectionList;
    private long selectedServerId;
    private RealmsButton playButton;
    private RealmsButton backButton;
    private String toolTip;
    private List<RealmsServer> realmsServers;
    private volatile int numberOfPendingInvites;
    private int animTick;
    private static volatile boolean hasParentalConsent;
    private static volatile boolean checkedParentalConsent;
    private static volatile boolean checkedClientCompatability;
    private boolean hasFetchedServers;
    private boolean popupOpenedByUser;
    private boolean justClosedPopup;
    private volatile boolean trialsAvailable;
    private volatile boolean createdTrial;
    private volatile boolean showingPopup;
    private volatile boolean hasUnreadNews;
    private volatile String newsLink;
    private int carouselIndex;
    private int carouselTick;
    boolean hasSwitchedCarouselImage;
    private static RealmsScreen realmsGenericErrorScreen;
    private static boolean regionsPinged;
    private int mindex;
    private final char[] mchars;
    private int sindex;
    private final char[] schars;
    private int clicks;
    private int lindex;
    private final char[] lchars;
    private ReentrantLock connectLock;
    private boolean expiredHover;
    
    public RealmsMainScreen(final RealmsScreen lastScreen) {
        this.selectedServerId = -1L;
        this.realmsServers = (List<RealmsServer>)Lists.newArrayList();
        this.mchars = new char[] { '3', '2', '1', '4', '5', '6' };
        this.schars = new char[] { '9', '8', '7', '1', '2', '3' };
        this.lchars = new char[] { '9', '8', '7', '4', '5', '6' };
        this.connectLock = new ReentrantLock();
        this.lastScreen = lastScreen;
    }
    
    @Override
    public void mouseEvent() {
        super.mouseEvent();
        if (!this.shouldShowPopup()) {
            this.serverSelectionList.mouseEvent();
        }
    }
    
    public boolean shouldShowMessageInList() {
        if (!this.hasParentalConsent() || !this.hasFetchedServers) {
            return false;
        }
        if (this.trialsAvailable && !this.createdTrial) {
            return true;
        }
        for (final RealmsServer realmsServer : this.realmsServers) {
            if (realmsServer.ownerUUID.equals(Realms.getUUID())) {
                return false;
            }
        }
        return true;
    }
    
    public boolean shouldShowPopup() {
        return this.hasParentalConsent() && this.hasFetchedServers && (this.popupOpenedByUser || (this.trialsAvailable && !this.createdTrial && this.realmsServers.isEmpty()) || this.realmsServers.isEmpty());
    }
    
    @Override
    public void init() {
        if (RealmsMainScreen.realmsGenericErrorScreen != null) {
            Realms.setScreen(RealmsMainScreen.realmsGenericErrorScreen);
            return;
        }
        this.connectLock = new ReentrantLock();
        if (RealmsMainScreen.checkedClientCompatability && !this.hasParentalConsent()) {
            this.checkParentalConsent();
        }
        this.checkClientCompatability();
        this.checkUnreadNews();
        if (!this.dontSetConnectedToRealms) {
            Realms.setConnectedToRealms(false);
        }
        Keyboard.enableRepeatEvents(true);
        this.buttonsClear();
        if (this.hasParentalConsent()) {
            RealmsMainScreen.realmsDataFetcher.forceUpdate();
        }
        this.showingPopup = false;
        this.postInit();
    }
    
    private boolean hasParentalConsent() {
        return RealmsMainScreen.checkedParentalConsent && RealmsMainScreen.hasParentalConsent;
    }
    
    public void addButtons() {
        this.buttonsAdd(this.playButton = RealmsScreen.newButton(1, this.width() / 2 - 98, this.height() - 32, 98, 20, RealmsScreen.getLocalizedString("mco.selectServer.play")));
        this.buttonsAdd(this.backButton = RealmsScreen.newButton(0, this.width() / 2 + 6, this.height() - 32, 98, 20, RealmsScreen.getLocalizedString("gui.back")));
        final RealmsServer server = this.findServer(this.selectedServerId);
        this.playButton.active(this.shouldPlayButtonBeActive(server));
    }
    
    private boolean shouldPlayButtonBeActive(final RealmsServer server) {
        return server != null && !server.expired && server.state == RealmsServer.State.OPEN;
    }
    
    public void postInit() {
        if (this.hasParentalConsent() && this.hasFetchedServers) {
            this.addButtons();
        }
        (this.serverSelectionList = new ServerSelectionList()).setLeftPos(-15);
        if (RealmsMainScreen.lastScrollYPosition != -1) {
            this.serverSelectionList.scroll(RealmsMainScreen.lastScrollYPosition);
        }
    }
    
    @Override
    public void tick() {
        this.justClosedPopup = false;
        ++this.animTick;
        --this.clicks;
        if (this.clicks < 0) {
            this.clicks = 0;
        }
        if (this.hasParentalConsent()) {
            RealmsMainScreen.realmsDataFetcher.init();
            if (RealmsMainScreen.realmsDataFetcher.isFetchedSinceLastTry(RealmsDataFetcher.Task.SERVER_LIST)) {
                final List<RealmsServer> newServers = RealmsMainScreen.realmsDataFetcher.getServers();
                if (newServers != null) {
                    boolean ownsNonExpiredRealmServer = false;
                    for (final RealmsServer retrievedServer : newServers) {
                        if (this.isSelfOwnedNonExpiredServer(retrievedServer)) {
                            ownsNonExpiredRealmServer = true;
                        }
                    }
                    this.realmsServers = newServers;
                    if (!RealmsMainScreen.regionsPinged && ownsNonExpiredRealmServer) {
                        RealmsMainScreen.regionsPinged = true;
                        this.pingRegions();
                    }
                }
                if (!this.hasFetchedServers) {
                    this.hasFetchedServers = true;
                    this.addButtons();
                }
            }
            if (RealmsMainScreen.realmsDataFetcher.isFetchedSinceLastTry(RealmsDataFetcher.Task.PENDING_INVITE)) {
                this.numberOfPendingInvites = RealmsMainScreen.realmsDataFetcher.getPendingInvitesCount();
            }
            if (RealmsMainScreen.realmsDataFetcher.isFetchedSinceLastTry(RealmsDataFetcher.Task.TRIAL_AVAILABLE) && !this.createdTrial) {
                final boolean newStatus = RealmsMainScreen.realmsDataFetcher.isTrialAvailable();
                if (newStatus != this.trialsAvailable && this.shouldShowPopup()) {
                    this.trialsAvailable = newStatus;
                    this.showingPopup = false;
                }
                else {
                    this.trialsAvailable = newStatus;
                }
            }
            if (RealmsMainScreen.realmsDataFetcher.isFetchedSinceLastTry(RealmsDataFetcher.Task.LIVE_STATS)) {
                final RealmsServerPlayerLists playerLists = RealmsMainScreen.realmsDataFetcher.getLivestats();
                for (final RealmsServerPlayerList playerList : playerLists.servers) {
                    for (final RealmsServer server : this.realmsServers) {
                        if (server.id == playerList.serverId) {
                            server.updateServerPing(playerList);
                            break;
                        }
                    }
                }
            }
            if (RealmsMainScreen.realmsDataFetcher.isFetchedSinceLastTry(RealmsDataFetcher.Task.UNREAD_NEWS)) {
                this.hasUnreadNews = RealmsMainScreen.realmsDataFetcher.hasUnreadNews();
                this.newsLink = RealmsMainScreen.realmsDataFetcher.newsLink();
            }
            RealmsMainScreen.realmsDataFetcher.markClean();
            if (this.shouldShowPopup()) {
                ++this.carouselTick;
            }
        }
    }
    
    private void pingRegions() {
        new Thread() {
            @Override
            public void run() {
                final List<RegionPingResult> regionPingResultList = Ping.pingAllRegions();
                final RealmsClient client = RealmsClient.createRealmsClient();
                final PingResult pingResult = new PingResult();
                pingResult.pingResults = regionPingResultList;
                pingResult.worldIds = RealmsMainScreen.this.getOwnedNonExpiredWorldIds();
                try {
                    client.sendPingResults(pingResult);
                }
                catch (final Throwable t) {
                    RealmsMainScreen.LOGGER.warn("Could not send ping result to Realms: ", t);
                }
            }
        }.start();
    }
    
    private List<Long> getOwnedNonExpiredWorldIds() {
        final List<Long> ids = new ArrayList<Long>();
        for (final RealmsServer server : this.realmsServers) {
            if (this.isSelfOwnedNonExpiredServer(server)) {
                ids.add(server.id);
            }
        }
        return ids;
    }
    
    @Override
    public void removed() {
        Keyboard.enableRepeatEvents(false);
        this.stopRealmsFetcher();
    }
    
    public void setCreatedTrial(final boolean createdTrial) {
        this.createdTrial = createdTrial;
    }
    
    @Override
    public void buttonClicked(final RealmsButton button) {
        if (!button.active()) {
            return;
        }
        switch (button.id()) {
            case 1: {
                final RealmsServer server = this.findServer(this.selectedServerId);
                if (server == null) {
                    return;
                }
                this.play(server, this);
                break;
            }
            case 0: {
                if (!this.justClosedPopup) {
                    Realms.setScreen(this.lastScreen);
                    break;
                }
                break;
            }
            case 4: {
                this.createTrial();
                break;
            }
            case 3: {
                RealmsUtil.browseTo("https://minecraft.net/realms");
                break;
            }
            default: {}
        }
    }
    
    private void createTrial() {
        if (!this.trialsAvailable || this.createdTrial) {
            return;
        }
        Realms.setScreen(new RealmsCreateTrialScreen(this));
    }
    
    private void checkClientCompatability() {
        if (!RealmsMainScreen.checkedClientCompatability) {
            RealmsMainScreen.checkedClientCompatability = true;
            new Thread("MCO Compatability Checker #1") {
                @Override
                public void run() {
                    final RealmsClient client = RealmsClient.createRealmsClient();
                    try {
                        final RealmsClient.CompatibleVersionResponse versionResponse = client.clientCompatible();
                        if (versionResponse.equals(RealmsClient.CompatibleVersionResponse.OUTDATED)) {
                            Realms.setScreen(RealmsMainScreen.realmsGenericErrorScreen = new RealmsClientOutdatedScreen(RealmsMainScreen.this.lastScreen, true));
                            return;
                        }
                        if (versionResponse.equals(RealmsClient.CompatibleVersionResponse.OTHER)) {
                            Realms.setScreen(RealmsMainScreen.realmsGenericErrorScreen = new RealmsClientOutdatedScreen(RealmsMainScreen.this.lastScreen, false));
                            return;
                        }
                        RealmsMainScreen.this.checkParentalConsent();
                    }
                    catch (final RealmsServiceException e) {
                        RealmsMainScreen.checkedClientCompatability = false;
                        RealmsMainScreen.LOGGER.error("Couldn't connect to realms: ", e.toString());
                        if (e.httpResultCode == 401) {
                            Realms.setScreen(RealmsMainScreen.realmsGenericErrorScreen = new RealmsGenericErrorScreen(RealmsScreen.getLocalizedString("mco.error.invalid.session.title"), RealmsScreen.getLocalizedString("mco.error.invalid.session.message"), RealmsMainScreen.this.lastScreen));
                            return;
                        }
                        Realms.setScreen(new RealmsGenericErrorScreen(e, RealmsMainScreen.this.lastScreen));
                    }
                    catch (final IOException e2) {
                        RealmsMainScreen.checkedClientCompatability = false;
                        RealmsMainScreen.LOGGER.error("Couldn't connect to realms: ", e2.getMessage());
                        Realms.setScreen(new RealmsGenericErrorScreen(e2.getMessage(), RealmsMainScreen.this.lastScreen));
                    }
                }
            }.start();
        }
    }
    
    private void checkUnreadNews() {
    }
    
    private void checkParentalConsent() {
        new Thread("MCO Compatability Checker #1") {
            @Override
            public void run() {
                final RealmsClient client = RealmsClient.createRealmsClient();
                try {
                    final Boolean result = client.mcoEnabled();
                    if (result) {
                        RealmsMainScreen.LOGGER.info("Realms is available for this user");
                        RealmsMainScreen.hasParentalConsent = true;
                    }
                    else {
                        RealmsMainScreen.LOGGER.info("Realms is not available for this user");
                        RealmsMainScreen.hasParentalConsent = false;
                        Realms.setScreen(new RealmsParentalConsentScreen(RealmsMainScreen.this.lastScreen));
                    }
                    RealmsMainScreen.checkedParentalConsent = true;
                }
                catch (final RealmsServiceException e) {
                    RealmsMainScreen.LOGGER.error("Couldn't connect to realms: ", e.toString());
                    Realms.setScreen(new RealmsGenericErrorScreen(e, RealmsMainScreen.this.lastScreen));
                }
                catch (final IOException e2) {
                    RealmsMainScreen.LOGGER.error("Couldn't connect to realms: ", e2.getMessage());
                    Realms.setScreen(new RealmsGenericErrorScreen(e2.getMessage(), RealmsMainScreen.this.lastScreen));
                }
            }
        }.start();
    }
    
    private void switchToStage() {
        if (!RealmsClient.currentEnvironment.equals(RealmsClient.Environment.STAGE)) {
            new Thread("MCO Stage Availability Checker #1") {
                @Override
                public void run() {
                    final RealmsClient client = RealmsClient.createRealmsClient();
                    try {
                        final Boolean result = client.stageAvailable();
                        if (result) {
                            RealmsClient.switchToStage();
                            RealmsMainScreen.LOGGER.info("Switched to stage");
                            RealmsMainScreen.realmsDataFetcher.forceUpdate();
                        }
                    }
                    catch (final RealmsServiceException e) {
                        RealmsMainScreen.LOGGER.error("Couldn't connect to Realms: " + e);
                    }
                    catch (final IOException e2) {
                        RealmsMainScreen.LOGGER.error("Couldn't parse response connecting to Realms: " + e2.getMessage());
                    }
                }
            }.start();
        }
    }
    
    private void switchToLocal() {
        if (!RealmsClient.currentEnvironment.equals(RealmsClient.Environment.LOCAL)) {
            new Thread("MCO Local Availability Checker #1") {
                @Override
                public void run() {
                    final RealmsClient client = RealmsClient.createRealmsClient();
                    try {
                        final Boolean result = client.stageAvailable();
                        if (result) {
                            RealmsClient.switchToLocal();
                            RealmsMainScreen.LOGGER.info("Switched to local");
                            RealmsMainScreen.realmsDataFetcher.forceUpdate();
                        }
                    }
                    catch (final RealmsServiceException e) {
                        RealmsMainScreen.LOGGER.error("Couldn't connect to Realms: " + e);
                    }
                    catch (final IOException e2) {
                        RealmsMainScreen.LOGGER.error("Couldn't parse response connecting to Realms: " + e2.getMessage());
                    }
                }
            }.start();
        }
    }
    
    private void switchToProd() {
        RealmsClient.switchToProd();
        RealmsMainScreen.realmsDataFetcher.forceUpdate();
    }
    
    private void stopRealmsFetcher() {
        RealmsMainScreen.realmsDataFetcher.stop();
    }
    
    private void configureClicked(final RealmsServer selectedServer) {
        if (Realms.getUUID().equals(selectedServer.ownerUUID) || RealmsMainScreen.overrideConfigure) {
            this.saveListScrollPosition();
            Realms.setScreen(new RealmsConfigureWorldScreen(this, selectedServer.id));
        }
    }
    
    private void leaveClicked(final RealmsServer selectedServer) {
        if (!Realms.getUUID().equals(selectedServer.ownerUUID)) {
            this.saveListScrollPosition();
            final String line2 = RealmsScreen.getLocalizedString("mco.configure.world.leave.question.line1");
            final String line3 = RealmsScreen.getLocalizedString("mco.configure.world.leave.question.line2");
            Realms.setScreen(new RealmsLongConfirmationScreen(this, RealmsLongConfirmationScreen.Type.Info, line2, line3, true, 2));
        }
    }
    
    private void saveListScrollPosition() {
        RealmsMainScreen.lastScrollYPosition = this.serverSelectionList.getScroll();
    }
    
    private RealmsServer findServer(final long id) {
        for (final RealmsServer server : this.realmsServers) {
            if (server.id == id) {
                return server;
            }
        }
        return null;
    }
    
    private int findIndex(final long serverId) {
        for (int i = 0; i < this.realmsServers.size(); ++i) {
            if (this.realmsServers.get(i).id == serverId) {
                return i;
            }
        }
        return -1;
    }
    
    @Override
    public void confirmResult(final boolean result, final int id) {
        if (id == 2) {
            if (result) {
                new Thread("Realms-leave-server") {
                    @Override
                    public void run() {
                        try {
                            final RealmsServer server = RealmsMainScreen.this.findServer(RealmsMainScreen.this.selectedServerId);
                            if (server != null) {
                                final RealmsClient client = RealmsClient.createRealmsClient();
                                client.uninviteMyselfFrom(server.id);
                                RealmsMainScreen.realmsDataFetcher.removeItem(server);
                                RealmsMainScreen.this.realmsServers.remove(server);
                                RealmsMainScreen.this.selectedServerId = -1L;
                                RealmsMainScreen.this.playButton.active(false);
                            }
                        }
                        catch (final RealmsServiceException e) {
                            RealmsMainScreen.LOGGER.error("Couldn't configure world");
                            Realms.setScreen(new RealmsGenericErrorScreen(e, RealmsMainScreen.this));
                        }
                    }
                }.start();
            }
            Realms.setScreen(this);
        }
    }
    
    public void removeSelection() {
        this.selectedServerId = -1L;
    }
    
    @Override
    public void keyPressed(final char ch, final int eventKey) {
        switch (eventKey) {
            case 28:
            case 156: {
                this.mindex = 0;
                this.sindex = 0;
                this.lindex = 0;
                if (this.shouldShowPopup()) {
                    return;
                }
                if (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54)) {
                    final RealmsServer server = this.findServer(this.selectedServerId);
                    if (server != null) {
                        this.configureClicked(server);
                    }
                    break;
                }
                this.buttonClicked(this.playButton);
                break;
            }
            case 1: {
                this.mindex = 0;
                this.sindex = 0;
                this.lindex = 0;
                if (this.shouldShowPopup() && this.popupOpenedByUser) {
                    this.popupOpenedByUser = false;
                    break;
                }
                Realms.setScreen(this.lastScreen);
                break;
            }
            case 200: {
                if (this.selectedServerId != -1L && !this.shouldShowPopup()) {
                    final RealmsServer server = this.findServer(this.selectedServerId);
                    final int theIndex = this.realmsServers.indexOf(server);
                    if (theIndex == 0) {
                        this.serverSelectionList.scroll(0 - this.serverSelectionList.getScroll());
                        return;
                    }
                    if (server != null && theIndex > 0) {
                        final int newIndex = theIndex - 1;
                        final RealmsServer newServer = this.realmsServers.get(newIndex);
                        if (newServer != null) {
                            this.selectedServerId = newServer.id;
                            final int maxScroll = Math.max(0, this.serverSelectionList.getMaxPosition() - (this.height() - 40 - 32 - 4));
                            final int maxItemsInView = (int)Math.floor((this.height() - 40 - 32) / 36);
                            final int scroll = this.serverSelectionList.getScroll();
                            final int hiddenItems = (int)Math.ceil(scroll / 36.0f);
                            final int scrollPerItem = maxScroll / this.realmsServers.size();
                            final int positionNeeded = scrollPerItem * newIndex;
                            final int proposedScroll = positionNeeded - this.serverSelectionList.getScroll();
                            if (newIndex < hiddenItems || newIndex > hiddenItems + maxItemsInView) {
                                this.serverSelectionList.scroll(proposedScroll);
                            }
                            return;
                        }
                    }
                }
                if (!this.shouldShowPopup() && !this.realmsServers.isEmpty()) {
                    this.selectedServerId = this.realmsServers.get(0).id;
                    this.serverSelectionList.scroll(0 - this.serverSelectionList.getScroll());
                    break;
                }
                break;
            }
            case 208: {
                if (this.selectedServerId != -1L && !this.shouldShowPopup()) {
                    final RealmsServer server = this.findServer(this.selectedServerId);
                    final int theIndex = this.realmsServers.indexOf(server);
                    final int maxScroll2 = Math.max(0, this.serverSelectionList.getMaxPosition() - (this.height() - 40 - 32));
                    if (theIndex == this.realmsServers.size() - 1) {
                        this.serverSelectionList.scroll(maxScroll2 - this.serverSelectionList.getScroll() + 36);
                        return;
                    }
                    if (server != null && theIndex > -1 && theIndex < this.realmsServers.size() - 1) {
                        final int newIndex2 = theIndex + 1;
                        final RealmsServer newServer2 = this.realmsServers.get(newIndex2);
                        if (newIndex2 == this.realmsServers.size() - 1) {
                            this.selectedServerId = newServer2.id;
                            this.serverSelectionList.scroll(maxScroll2 - this.serverSelectionList.getScroll() + 36);
                            return;
                        }
                        if (newServer2 != null) {
                            this.selectedServerId = newServer2.id;
                            final int maxItemsInView = (int)Math.floor((this.height() - 40 - 32) / 36);
                            final int scroll = this.serverSelectionList.getScroll();
                            final int hiddenItems = (int)Math.ceil(scroll / 36.0f);
                            final int scrollPerItem = maxScroll2 / this.realmsServers.size();
                            final int positionNeeded = scrollPerItem * newIndex2;
                            int proposedScroll = positionNeeded - this.serverSelectionList.getScroll();
                            if (proposedScroll > 0) {
                                proposedScroll += scrollPerItem;
                            }
                            if (newIndex2 < hiddenItems || newIndex2 >= hiddenItems + maxItemsInView) {
                                this.serverSelectionList.scroll(proposedScroll);
                            }
                            return;
                        }
                    }
                }
                if (!this.shouldShowPopup() && !this.realmsServers.isEmpty()) {
                    this.selectedServerId = this.realmsServers.get(0).id;
                    this.serverSelectionList.scroll(-(this.serverSelectionList.getItemCount() * 36));
                    break;
                }
                break;
            }
            default: {
                if (this.mchars[this.mindex] == ch) {
                    ++this.mindex;
                    if (this.mindex == this.mchars.length) {
                        this.mindex = 0;
                        RealmsMainScreen.overrideConfigure = !RealmsMainScreen.overrideConfigure;
                    }
                }
                else {
                    this.mindex = 0;
                }
                if (this.schars[this.sindex] == ch) {
                    ++this.sindex;
                    if (this.sindex == this.schars.length) {
                        this.sindex = 0;
                        if (RealmsClient.currentEnvironment.equals(RealmsClient.Environment.STAGE)) {
                            this.switchToProd();
                        }
                        else {
                            this.switchToStage();
                        }
                    }
                }
                else {
                    this.sindex = 0;
                }
                if (this.lchars[this.lindex] != ch) {
                    this.lindex = 0;
                    break;
                }
                ++this.lindex;
                if (this.lindex != this.lchars.length) {
                    break;
                }
                this.lindex = 0;
                if (RealmsClient.currentEnvironment.equals(RealmsClient.Environment.LOCAL)) {
                    this.switchToProd();
                    break;
                }
                this.switchToLocal();
                break;
            }
        }
    }
    
    @Override
    public void render(final int xm, final int ym, final float a) {
        this.expiredHover = false;
        this.toolTip = null;
        this.renderBackground();
        this.serverSelectionList.render(xm, ym, a);
        this.drawRealmsLogo(this.width() / 2 - 50, 7);
        if ((!this.shouldShowPopup() || this.popupOpenedByUser) && this.hasParentalConsent() && this.hasFetchedServers) {
            this.renderMoreInfo(xm, ym);
        }
        this.renderNews(xm, ym, this.hasUnreadNews);
        this.drawInvitationPendingIcon(xm, ym);
        if (RealmsClient.currentEnvironment.equals(RealmsClient.Environment.STAGE)) {
            this.renderStage();
        }
        if (RealmsClient.currentEnvironment.equals(RealmsClient.Environment.LOCAL)) {
            this.renderLocal();
        }
        if (this.shouldShowPopup()) {
            this.drawPopup(xm, ym);
        }
        else {
            if (this.showingPopup) {
                this.buttonsClear();
                this.buttonsAdd(this.playButton);
                this.buttonsAdd(this.backButton);
                final RealmsServer server = this.findServer(this.selectedServerId);
                this.playButton.active(this.shouldPlayButtonBeActive(server));
            }
            this.showingPopup = false;
        }
        if (this.toolTip != null) {
            this.renderMousehoverTooltip(this.toolTip, xm, ym);
        }
        super.render(xm, ym, a);
        if (this.trialsAvailable && !this.createdTrial && this.shouldShowPopup()) {
            RealmsScreen.bind("realms:textures/gui/realms/trial_icon.png");
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glPushMatrix();
            int ySprite = 0;
            if ((System.currentTimeMillis() / 800L & 0x1L) == 0x1L) {
                ySprite = 8;
            }
            final int yo = this.height() / 2 - 83 - 3;
            final int buttonHeight = yo + 147 - 20;
            RealmsScreen.blit(this.width() / 2 + 52 + 83, buttonHeight - 4, 0.0f, (float)ySprite, 8, 8, 8.0f, 16.0f);
            GL11.glPopMatrix();
        }
    }
    
    private void drawRealmsLogo(final int x, final int y) {
        RealmsScreen.bind("realms:textures/gui/title/realms.png");
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPushMatrix();
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        RealmsScreen.blit(x * 2, y * 2 - 5, 0.0f, 0.0f, 200, 50, 200.0f, 50.0f);
        GL11.glPopMatrix();
    }
    
    @Override
    public void mouseClicked(final int x, final int y, final int buttonNum) {
        if (this.inPendingInvitationArea(x, y)) {
            final RealmsPendingInvitesScreen pendingInvitationScreen = new RealmsPendingInvitesScreen(this.lastScreen);
            Realms.setScreen(pendingInvitationScreen);
        }
        else if (this.toolTip != null && this.toolTip.equals(RealmsScreen.getLocalizedString("mco.selectServer.info"))) {
            this.popupOpenedByUser = !this.popupOpenedByUser;
        }
        else if (this.toolTip != null && this.toolTip.equals(RealmsScreen.getLocalizedString("mco.selectServer.close"))) {
            this.popupOpenedByUser = false;
        }
        else if (this.toolTip != null && this.toolTip.equals(RealmsScreen.getLocalizedString("mco.news"))) {
            if (this.newsLink == null) {
                return;
            }
            RealmsUtil.browseTo(this.newsLink);
            if (this.hasUnreadNews) {
                final RealmsPersistence.RealmsPersistenceData data = RealmsPersistence.readFile();
                data.hasUnreadNews = false;
                this.hasUnreadNews = false;
                RealmsPersistence.writeFile(data);
            }
        }
        else if (this.isOutsidePopup(x, y) && this.popupOpenedByUser) {
            this.popupOpenedByUser = false;
            this.justClosedPopup = true;
        }
    }
    
    private boolean isOutsidePopup(final int xm, final int ym) {
        final int xo = (this.width() - 310) / 2;
        final int yo = this.height() / 2 - 83 - 3;
        return xm < xo - 5 || xm > xo + 315 || ym < yo - 5 || ym > yo + 171;
    }
    
    private void drawPopup(final int xm, final int ym) {
        final int xo = (this.width() - 310) / 2;
        final int yo = this.height() / 2 - 83 - 3;
        int buttonHeight = yo + 147 - 20;
        if (!this.showingPopup) {
            this.carouselIndex = 0;
            this.carouselTick = 0;
            this.hasSwitchedCarouselImage = true;
            if (this.hasFetchedServers && this.realmsServers.isEmpty()) {
                this.buttonsClear();
                this.buttonsAdd(RealmsScreen.newButton(0, this.width() / 2 - 49, this.height() - 32, 98, 20, RealmsScreen.getLocalizedString("gui.back")));
            }
            if (this.trialsAvailable && !this.createdTrial) {
                buttonHeight -= 10;
                this.buttonsAdd(RealmsScreen.newButton(4, this.width() / 2 + 52, buttonHeight, 98, 20, RealmsScreen.getLocalizedString("mco.selectServer.trial")));
                buttonHeight = yo + 170 - 20 - 10;
            }
            this.buttonsAdd(RealmsScreen.newButton(3, this.width() / 2 + 52, buttonHeight, 98, 20, RealmsScreen.getLocalizedString("mco.selectServer.buy")));
            this.playButton.active(false);
        }
        if (this.hasFetchedServers) {
            this.showingPopup = true;
        }
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.7f);
        GL11.glEnable(3042);
        RealmsScreen.bind("realms:textures/gui/realms/darken.png");
        GL11.glPushMatrix();
        final int otherxo = 0;
        final int otheryo = 32;
        RealmsScreen.blit(0, 32, 0.0f, 0.0f, this.width(), this.height() - 40 - 32, 310.0f, 166.0f);
        GL11.glPopMatrix();
        GL11.glDisable(3042);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        RealmsScreen.bind("realms:textures/gui/realms/popup.png");
        GL11.glPushMatrix();
        RealmsScreen.blit(xo, yo, 0.0f, 0.0f, 310, 166, 310.0f, 166.0f);
        GL11.glPopMatrix();
        RealmsScreen.bind(RealmsMainScreen.IMAGES_LOCATION[this.carouselIndex]);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPushMatrix();
        RealmsScreen.blit(xo + 7, yo + 7, 0.0f, 0.0f, 195, 152, 195.0f, 152.0f);
        GL11.glPopMatrix();
        if (this.carouselTick % 100 < 5) {
            if (!this.hasSwitchedCarouselImage) {
                if (this.carouselIndex == RealmsMainScreen.IMAGES_LOCATION.length - 1) {
                    this.carouselIndex = 0;
                }
                else {
                    ++this.carouselIndex;
                }
                this.hasSwitchedCarouselImage = true;
            }
        }
        else {
            this.hasSwitchedCarouselImage = false;
        }
        if (this.popupOpenedByUser) {
            boolean crossHovered = false;
            final int bx = xo + 4;
            final int by = yo + 4;
            if (xm >= bx && xm <= bx + 12 && ym >= by && ym <= by + 12) {
                this.toolTip = RealmsScreen.getLocalizedString("mco.selectServer.close");
                crossHovered = true;
            }
            RealmsScreen.bind("realms:textures/gui/realms/cross_icon.png");
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glPushMatrix();
            RealmsScreen.blit(bx, by, 0.0f, crossHovered ? 12.0f : 0.0f, 12, 12, 12.0f, 24.0f);
            GL11.glPopMatrix();
            if (crossHovered) {
                this.toolTip = RealmsScreen.getLocalizedString("mco.selectServer.close");
            }
        }
        final List<String> strings = this.fontSplit(RealmsScreen.getLocalizedString("mco.selectServer.popup"), 100);
        int index = 0;
        for (final String s : strings) {
            this.drawString(s, this.width() / 2 + 52, yo + 10 * ++index - 3, 5000268, false);
        }
    }
    
    private void drawInvitationPendingIcon(final int xm, final int ym) {
        final int pendingInvitesCount = this.numberOfPendingInvites;
        final boolean hovering = this.inPendingInvitationArea(xm, ym);
        final int baseX = this.width() / 2 + 50;
        final int baseY = 8;
        if (pendingInvitesCount != 0) {
            final float scale = 0.25f + (1.0f + RealmsMth.sin(this.animTick * 0.5f)) * 0.25f;
            int color = 0xFF000000 | (int)(scale * 64.0f) << 16 | (int)(scale * 64.0f) << 8 | (int)(scale * 64.0f) << 0;
            this.fillGradient(baseX - 2, 6, baseX + 18, 26, color, color);
            color = (0xFF000000 | (int)(scale * 255.0f) << 16 | (int)(scale * 255.0f) << 8 | (int)(scale * 255.0f) << 0);
            this.fillGradient(baseX - 2, 6, baseX + 18, 7, color, color);
            this.fillGradient(baseX - 2, 6, baseX - 1, 26, color, color);
            this.fillGradient(baseX + 17, 6, baseX + 18, 26, color, color);
            this.fillGradient(baseX - 2, 25, baseX + 18, 26, color, color);
        }
        RealmsScreen.bind("realms:textures/gui/realms/invite_icon.png");
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPushMatrix();
        RealmsScreen.blit(baseX, 2, hovering ? 16.0f : 0.0f, 0.0f, 15, 25, 31.0f, 25.0f);
        GL11.glPopMatrix();
        if (pendingInvitesCount != 0) {
            final int spritePos = (Math.min(pendingInvitesCount, 6) - 1) * 8;
            final int yOff = (int)(Math.max(0.0f, Math.max(RealmsMth.sin((10 + this.animTick) * 0.57f), RealmsMth.cos(this.animTick * 0.35f))) * -6.0f);
            RealmsScreen.bind("realms:textures/gui/realms/invitation_icons.png");
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glPushMatrix();
            RealmsScreen.blit(baseX + 4, 12 + yOff, (float)spritePos, hovering ? 8.0f : 0.0f, 8, 8, 48.0f, 16.0f);
            GL11.glPopMatrix();
        }
        if (hovering) {
            final int rx = xm + 12;
            final int ry = ym;
            final String message = RealmsScreen.getLocalizedString((pendingInvitesCount == 0) ? "mco.invites.nopending" : "mco.invites.pending");
            final int width = this.fontWidth(message);
            this.fillGradient(rx - 3, ry - 3, rx + width + 3, ry + 8 + 3, -1073741824, -1073741824);
            this.fontDrawShadow(message, rx, ry, -1);
        }
    }
    
    private boolean inPendingInvitationArea(final int xm, final int ym) {
        int x1 = this.width() / 2 + 50;
        int x2 = this.width() / 2 + 66;
        int y1 = 11;
        int y2 = 23;
        if (this.numberOfPendingInvites != 0) {
            x1 -= 3;
            x2 += 3;
            y1 -= 5;
            y2 += 5;
        }
        return x1 <= xm && xm <= x2 && y1 <= ym && ym <= y2;
    }
    
    public void play(final RealmsServer server, final RealmsScreen cancelScreen) {
        if (server != null) {
            try {
                if (!this.connectLock.tryLock(1L, TimeUnit.SECONDS)) {
                    return;
                }
                if (this.connectLock.getHoldCount() > 1) {
                    return;
                }
            }
            catch (final InterruptedException ignored) {
                return;
            }
            this.dontSetConnectedToRealms = true;
            this.connectToServer(server, cancelScreen);
        }
    }
    
    private void connectToServer(final RealmsServer server, final RealmsScreen cancelScreen) {
        final RealmsLongRunningMcoTaskScreen longRunningMcoTaskScreen = new RealmsLongRunningMcoTaskScreen(cancelScreen, new RealmsTasks.RealmsGetServerDetailsTask(this, cancelScreen, server, this.connectLock));
        longRunningMcoTaskScreen.start();
        Realms.setScreen(longRunningMcoTaskScreen);
    }
    
    private boolean isSelfOwnedServer(final RealmsServer serverData) {
        return serverData.ownerUUID != null && serverData.ownerUUID.equals(Realms.getUUID());
    }
    
    private boolean isSelfOwnedNonExpiredServer(final RealmsServer serverData) {
        return serverData.ownerUUID != null && serverData.ownerUUID.equals(Realms.getUUID()) && !serverData.expired;
    }
    
    private void drawExpired(final int x, final int y, final int xm, final int ym) {
        RealmsScreen.bind("realms:textures/gui/realms/expired_icon.png");
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPushMatrix();
        RealmsScreen.blit(x, y, 0.0f, 0.0f, 10, 28, 10.0f, 28.0f);
        GL11.glPopMatrix();
        if (xm >= x && xm <= x + 9 && ym >= y && ym <= y + 27 && ym < this.height() - 40 && ym > 32 && !this.shouldShowPopup()) {
            this.toolTip = RealmsScreen.getLocalizedString("mco.selectServer.expired");
        }
    }
    
    private void drawExpiring(final int x, final int y, final int xm, final int ym, final int daysLeft) {
        RealmsScreen.bind("realms:textures/gui/realms/expires_soon_icon.png");
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPushMatrix();
        if (this.animTick % 20 < 10) {
            RealmsScreen.blit(x, y, 0.0f, 0.0f, 10, 28, 20.0f, 28.0f);
        }
        else {
            RealmsScreen.blit(x, y, 10.0f, 0.0f, 10, 28, 20.0f, 28.0f);
        }
        GL11.glPopMatrix();
        if (xm >= x && xm <= x + 9 && ym >= y && ym <= y + 27 && ym < this.height() - 40 && ym > 32 && !this.shouldShowPopup()) {
            if (daysLeft <= 0) {
                this.toolTip = RealmsScreen.getLocalizedString("mco.selectServer.expires.soon");
            }
            else if (daysLeft == 1) {
                this.toolTip = RealmsScreen.getLocalizedString("mco.selectServer.expires.day");
            }
            else {
                this.toolTip = RealmsScreen.getLocalizedString("mco.selectServer.expires.days", daysLeft);
            }
        }
    }
    
    private void drawOpen(final int x, final int y, final int xm, final int ym) {
        RealmsScreen.bind("realms:textures/gui/realms/on_icon.png");
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPushMatrix();
        RealmsScreen.blit(x, y, 0.0f, 0.0f, 10, 28, 10.0f, 28.0f);
        GL11.glPopMatrix();
        if (xm >= x && xm <= x + 9 && ym >= y && ym <= y + 27 && ym < this.height() - 40 && ym > 32 && !this.shouldShowPopup()) {
            this.toolTip = RealmsScreen.getLocalizedString("mco.selectServer.open");
        }
    }
    
    private void drawClose(final int x, final int y, final int xm, final int ym) {
        RealmsScreen.bind("realms:textures/gui/realms/off_icon.png");
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPushMatrix();
        RealmsScreen.blit(x, y, 0.0f, 0.0f, 10, 28, 10.0f, 28.0f);
        GL11.glPopMatrix();
        if (xm >= x && xm <= x + 9 && ym >= y && ym <= y + 27 && ym < this.height() - 40 && ym > 32 && !this.shouldShowPopup()) {
            this.toolTip = RealmsScreen.getLocalizedString("mco.selectServer.closed");
        }
    }
    
    private void drawLeave(final int x, final int y, final int xm, final int ym) {
        boolean hovered = false;
        if (xm >= x && xm <= x + 28 && ym >= y && ym <= y + 28 && ym < this.height() - 40 && ym > 32 && !this.shouldShowPopup()) {
            hovered = true;
        }
        RealmsScreen.bind("realms:textures/gui/realms/leave_icon.png");
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPushMatrix();
        RealmsScreen.blit(x, y, hovered ? 28.0f : 0.0f, 0.0f, 28, 28, 56.0f, 28.0f);
        GL11.glPopMatrix();
        if (hovered) {
            this.toolTip = RealmsScreen.getLocalizedString("mco.selectServer.leave");
        }
    }
    
    private void drawConfigure(final int x, final int y, final int xm, final int ym) {
        boolean hovered = false;
        if (xm >= x && xm <= x + 28 && ym >= y && ym <= y + 28 && ym < this.height() - 40 && ym > 32 && !this.shouldShowPopup()) {
            hovered = true;
        }
        RealmsScreen.bind("realms:textures/gui/realms/configure_icon.png");
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPushMatrix();
        RealmsScreen.blit(x, y, hovered ? 28.0f : 0.0f, 0.0f, 28, 28, 56.0f, 28.0f);
        GL11.glPopMatrix();
        if (hovered) {
            this.toolTip = RealmsScreen.getLocalizedString("mco.selectServer.configure");
        }
    }
    
    protected void renderMousehoverTooltip(final String msg, final int x, final int y) {
        if (msg == null) {
            return;
        }
        int index = 0;
        int width = 0;
        for (final String s : msg.split("\n")) {
            final int theWidth = this.fontWidth(s);
            if (theWidth > width) {
                width = theWidth;
            }
        }
        int rx = x - width - 5;
        final int ry = y;
        if (rx < 0) {
            rx = x + 12;
        }
        for (final String s2 : msg.split("\n")) {
            this.fillGradient(rx - 3, ry - ((index == 0) ? 3 : 0) + index, rx + width + 3, ry + 8 + 3 + index, -1073741824, -1073741824);
            this.fontDrawShadow(s2, rx, ry + index, 16777215);
            index += 10;
        }
    }
    
    private void renderMoreInfo(final int xm, final int ym) {
        final int x = this.width() - 17 - 20;
        final int y = 6;
        boolean hovered = false;
        if (xm >= x && xm <= x + 20 && ym >= 6 && ym <= 26) {
            hovered = true;
        }
        RealmsScreen.bind("realms:textures/gui/realms/questionmark.png");
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPushMatrix();
        RealmsScreen.blit(x, 6, hovered ? 20.0f : 0.0f, 0.0f, 20, 20, 40.0f, 20.0f);
        GL11.glPopMatrix();
        if (hovered) {
            this.toolTip = RealmsScreen.getLocalizedString("mco.selectServer.info");
        }
    }
    
    private void renderNews(final int xm, final int ym, final boolean unread) {
        final int x = this.width() - 17 - 20 - 25;
        final int y = 6;
        boolean hovered = false;
        if (xm >= x && xm <= x + 20 && ym >= 6 && ym <= 26) {
            hovered = true;
        }
        RealmsScreen.bind("realms:textures/gui/realms/news_icon.png");
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPushMatrix();
        RealmsScreen.blit(x, 6, hovered ? 20.0f : 0.0f, 0.0f, 20, 20, 40.0f, 20.0f);
        GL11.glPopMatrix();
        if (hovered) {
            this.toolTip = RealmsScreen.getLocalizedString("mco.news");
        }
        if (unread) {
            final int yOff = hovered ? 0 : ((int)(Math.max(0.0f, Math.max(RealmsMth.sin((10 + this.animTick) * 0.57f), RealmsMth.cos(this.animTick * 0.35f))) * -6.0f));
            RealmsScreen.bind("realms:textures/gui/realms/invitation_icons.png");
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glPushMatrix();
            RealmsScreen.blit(x + 10, 8 + yOff, 40.0f, 0.0f, 8, 8, 48.0f, 16.0f);
            GL11.glPopMatrix();
        }
    }
    
    private void renderLocal() {
        final String text = "LOCAL!";
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPushMatrix();
        GL11.glTranslatef((float)(this.width() / 2 - 25), 20.0f, 0.0f);
        GL11.glRotatef(-20.0f, 0.0f, 0.0f, 1.0f);
        GL11.glScalef(1.5f, 1.5f, 1.5f);
        this.drawString("LOCAL!", 0, 0, 8388479);
        GL11.glPopMatrix();
    }
    
    private void renderStage() {
        final String text = "STAGE!";
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPushMatrix();
        GL11.glTranslatef((float)(this.width() / 2 - 25), 20.0f, 0.0f);
        GL11.glRotatef(-20.0f, 0.0f, 0.0f, 1.0f);
        GL11.glScalef(1.5f, 1.5f, 1.5f);
        this.drawString("STAGE!", 0, 0, -256);
        GL11.glPopMatrix();
    }
    
    public RealmsMainScreen newScreen() {
        return new RealmsMainScreen(this.lastScreen);
    }
    
    public void closePopup() {
        if (this.shouldShowPopup() && this.popupOpenedByUser) {
            this.popupOpenedByUser = false;
        }
    }
    
    static {
        LOGGER = LogManager.getLogger();
        IMAGES_LOCATION = new String[] { "realms:textures/gui/realms/images/flower_mountain.png", "realms:textures/gui/realms/images/dornenstein_estate.png", "realms:textures/gui/realms/images/desert.png", "realms:textures/gui/realms/images/gray.png", "realms:textures/gui/realms/images/imperium.png", "realms:textures/gui/realms/images/ludo.png", "realms:textures/gui/realms/images/makersspleef.png", "realms:textures/gui/realms/images/negentropy.png", "realms:textures/gui/realms/images/pumpkin_party.png", "realms:textures/gui/realms/images/sparrenhout.png", "realms:textures/gui/realms/images/spindlewood.png" };
        realmsDataFetcher = new RealmsDataFetcher();
        RealmsMainScreen.lastScrollYPosition = -1;
        final String version = RealmsVersion.getVersion();
        if (version != null) {
            RealmsMainScreen.LOGGER.info("Realms library version == " + version);
        }
    }
    
    private class ServerSelectionList extends RealmsClickableScrolledSelectionList
    {
        public ServerSelectionList() {
            super(RealmsMainScreen.this.width() + 15, RealmsMainScreen.this.height(), 32, RealmsMainScreen.this.height() - 40, 36);
        }
        
        @Override
        public int getItemCount() {
            if (RealmsMainScreen.this.shouldShowMessageInList()) {
                return RealmsMainScreen.this.realmsServers.size() + 1;
            }
            return RealmsMainScreen.this.realmsServers.size();
        }
        
        @Override
        public void selectItem(int item, final boolean doubleClick, final int xMouse, final int yMouse) {
            if (RealmsMainScreen.this.shouldShowMessageInList()) {
                if (item == 0) {
                    RealmsMainScreen.this.popupOpenedByUser = true;
                    return;
                }
                --item;
            }
            if (item >= RealmsMainScreen.this.realmsServers.size()) {
                return;
            }
            final RealmsServer server = RealmsMainScreen.this.realmsServers.get(item);
            if (server.state == RealmsServer.State.UNINITIALIZED) {
                RealmsMainScreen.this.selectedServerId = -1L;
                Realms.setScreen(new RealmsCreateRealmScreen(server, RealmsMainScreen.this));
            }
            else {
                RealmsMainScreen.this.selectedServerId = server.id;
            }
            RealmsMainScreen.this.playButton.active(RealmsMainScreen.this.shouldPlayButtonBeActive(server));
            if (doubleClick && RealmsMainScreen.this.playButton.active()) {
                RealmsMainScreen.this.play(RealmsMainScreen.this.findServer(RealmsMainScreen.this.selectedServerId), RealmsMainScreen.this);
            }
        }
        
        @Override
        public boolean isSelectedItem(int item) {
            if (RealmsMainScreen.this.shouldShowMessageInList()) {
                if (item == 0) {
                    return false;
                }
                --item;
            }
            return item == RealmsMainScreen.this.findIndex(RealmsMainScreen.this.selectedServerId);
        }
        
        @Override
        public int getMaxPosition() {
            return this.getItemCount() * 36;
        }
        
        @Override
        public int getScrollbarPosition() {
            return super.getScrollbarPosition() + 15;
        }
        
        @Override
        protected void renderItem(int i, final int x, final int y, final int h, final Tezzelator t, final int mouseX, final int mouseY) {
            if (RealmsMainScreen.this.shouldShowMessageInList()) {
                if (i == 0) {
                    this.renderTrialItem(0, x, y);
                    return;
                }
                --i;
            }
            if (i < RealmsMainScreen.this.realmsServers.size()) {
                this.renderMcoServerItem(i, x, y);
            }
        }
        
        private void renderTrialItem(final int i, final int x, final int y) {
            final int ry = y + 8;
            int index = 0;
            final String msg = RealmsScreen.getLocalizedString("mco.trial.message.line1") + "\\n" + RealmsScreen.getLocalizedString("mco.trial.message.line2");
            boolean hovered = false;
            if (x <= this.xm() && this.xm() <= this.getScrollbarPosition() && y <= this.ym() && this.ym() <= y + 32) {
                hovered = true;
            }
            int textColor = 8388479;
            if (hovered && !RealmsMainScreen.this.shouldShowPopup()) {
                textColor = 6077788;
            }
            for (final String s : msg.split("\\\\n")) {
                RealmsMainScreen.this.drawCenteredString(s, RealmsMainScreen.this.width() / 2, ry + index, textColor);
                index += 10;
            }
        }
        
        @Override
        public void renderSelected(final int width, final int y, final int h, final Tezzelator t) {
            final int x0 = this.getScrollbarPosition() - 300;
            final int x2 = this.getScrollbarPosition() - 5;
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glDisable(3553);
            t.begin(7, RealmsDefaultVertexFormat.POSITION_TEX_COLOR);
            t.vertex(x0, y + h + 2, 0.0).tex(0.0, 1.0).color(128, 128, 128, 255).endVertex();
            t.vertex(x2, y + h + 2, 0.0).tex(1.0, 1.0).color(128, 128, 128, 255).endVertex();
            t.vertex(x2, y - 2, 0.0).tex(1.0, 0.0).color(128, 128, 128, 255).endVertex();
            t.vertex(x0, y - 2, 0.0).tex(0.0, 0.0).color(128, 128, 128, 255).endVertex();
            t.vertex(x0 + 1, y + h + 1, 0.0).tex(0.0, 1.0).color(0, 0, 0, 255).endVertex();
            t.vertex(x2 - 1, y + h + 1, 0.0).tex(1.0, 1.0).color(0, 0, 0, 255).endVertex();
            t.vertex(x2 - 1, y - 1, 0.0).tex(1.0, 0.0).color(0, 0, 0, 255).endVertex();
            t.vertex(x0 + 1, y - 1, 0.0).tex(0.0, 0.0).color(0, 0, 0, 255).endVertex();
            t.end();
            GL11.glEnable(3553);
        }
        
        @Override
        public void itemClicked(final int clickSlotPos, int slot, final int xm, final int ym, final int width) {
            if (RealmsMainScreen.this.shouldShowMessageInList()) {
                if (slot == 0) {
                    RealmsMainScreen.this.popupOpenedByUser = true;
                    return;
                }
                --slot;
            }
            if (slot >= RealmsMainScreen.this.realmsServers.size()) {
                return;
            }
            final RealmsServer server = RealmsMainScreen.this.realmsServers.get(slot);
            if (server == null) {
                return;
            }
            if (RealmsMainScreen.this.toolTip != null && RealmsMainScreen.this.toolTip.equals(RealmsScreen.getLocalizedString("mco.selectServer.configure"))) {
                RealmsMainScreen.this.selectedServerId = server.id;
                RealmsMainScreen.this.configureClicked(server);
            }
            else if (RealmsMainScreen.this.toolTip != null && RealmsMainScreen.this.toolTip.equals(RealmsScreen.getLocalizedString("mco.selectServer.leave"))) {
                RealmsMainScreen.this.selectedServerId = server.id;
                RealmsMainScreen.this.leaveClicked(server);
            }
            else if (RealmsMainScreen.this.isSelfOwnedServer(server) && server.expired && RealmsMainScreen.this.expiredHover) {
                final String extensionUrl = "https://account.mojang.com/buy/realms?sid=" + server.remoteSubscriptionId + "&pid=" + Realms.getUUID() + "&ref=" + (server.expiredTrial ? "expiredTrial" : "expiredRealm");
                this.browseURL(extensionUrl);
            }
        }
        
        @Override
        public void customMouseEvent(final int y0, final int y1, final int headerHeight, final float yo, final int itemHeight) {
            if (Mouse.isButtonDown(0) && this.ym() >= y0 && this.ym() <= y1) {
                final int x0 = this.width() / 2 - 160;
                final int x2 = this.getScrollbarPosition();
                final int clickSlotPos = this.ym() - y0 - headerHeight + (int)yo - 4;
                final int slot = clickSlotPos / itemHeight;
                if (this.xm() >= x0 && this.xm() <= x2 && slot >= 0 && clickSlotPos >= 0 && slot < this.getItemCount()) {
                    this.itemClicked(clickSlotPos, slot, this.xm(), this.ym(), this.width());
                    RealmsMainScreen.this.clicks += RealmsSharedConstants.TICKS_PER_SECOND / 3 + 1;
                    this.selectItem(slot, RealmsMainScreen.this.clicks >= RealmsSharedConstants.TICKS_PER_SECOND / 2, this.xm(), this.ym());
                }
            }
        }
        
        private void renderMcoServerItem(final int i, final int x, final int y) {
            final RealmsServer serverData = RealmsMainScreen.this.realmsServers.get(i);
            if (serverData.state == RealmsServer.State.UNINITIALIZED) {
                RealmsScreen.bind("realms:textures/gui/realms/world_icon.png");
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                GL11.glEnable(3008);
                GL11.glPushMatrix();
                RealmsScreen.blit(x + 10, y + 6, 0.0f, 0.0f, 40, 20, 40.0f, 20.0f);
                GL11.glPopMatrix();
                final float scale = 0.5f + (1.0f + RealmsMth.sin(RealmsMainScreen.this.animTick * 0.25f)) * 0.25f;
                final int textColor = 0xFF000000 | (int)(127.0f * scale) << 16 | (int)(255.0f * scale) << 8 | (int)(127.0f * scale);
                RealmsMainScreen.this.drawCenteredString(RealmsScreen.getLocalizedString("mco.selectServer.uninitialized"), x + 10 + 40 + 75, y + 12, textColor);
                return;
            }
            final int dx = 225;
            final int dy = 2;
            if (serverData.expired) {
                RealmsMainScreen.this.drawExpired(x + 225 - 14, y + 2, this.xm(), this.ym());
            }
            else if (serverData.state == RealmsServer.State.CLOSED) {
                RealmsMainScreen.this.drawClose(x + 225 - 14, y + 2, this.xm(), this.ym());
            }
            else if (RealmsMainScreen.this.isSelfOwnedServer(serverData) && serverData.daysLeft < 7) {
                RealmsMainScreen.this.drawExpiring(x + 225 - 14, y + 2, this.xm(), this.ym(), serverData.daysLeft);
            }
            else if (serverData.state == RealmsServer.State.OPEN) {
                RealmsMainScreen.this.drawOpen(x + 225 - 14, y + 2, this.xm(), this.ym());
            }
            if (!RealmsMainScreen.this.isSelfOwnedServer(serverData) && !RealmsMainScreen.overrideConfigure) {
                RealmsMainScreen.this.drawLeave(x + 225, y + 2, this.xm(), this.ym());
            }
            else {
                RealmsMainScreen.this.drawConfigure(x + 225, y + 2, this.xm(), this.ym());
            }
            if (!"0".equals(serverData.serverPing.nrOfPlayers)) {
                final String coloredNumPlayers = ChatFormatting.GRAY + "" + serverData.serverPing.nrOfPlayers;
                RealmsMainScreen.this.drawString(coloredNumPlayers, x + 207 - RealmsMainScreen.this.fontWidth(coloredNumPlayers), y + 3, 8421504);
                if (this.xm() >= x + 207 - RealmsMainScreen.this.fontWidth(coloredNumPlayers) && this.xm() <= x + 207 && this.ym() >= y + 1 && this.ym() <= y + 10 && this.ym() < RealmsMainScreen.this.height() - 40 && this.ym() > 32 && !RealmsMainScreen.this.shouldShowPopup()) {
                    RealmsMainScreen.this.toolTip = serverData.serverPing.playerList;
                }
            }
            if (RealmsMainScreen.this.isSelfOwnedServer(serverData) && serverData.expired) {
                boolean hovered = false;
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                GL11.glEnable(3042);
                RealmsScreen.bind("minecraft:textures/gui/widgets.png");
                GL11.glPushMatrix();
                GL11.glBlendFunc(770, 771);
                String expirationText = RealmsScreen.getLocalizedString("mco.selectServer.expiredList");
                String expirationButtonText = RealmsScreen.getLocalizedString("mco.selectServer.expiredRenew");
                if (serverData.expiredTrial) {
                    expirationText = RealmsScreen.getLocalizedString("mco.selectServer.expiredTrial");
                    expirationButtonText = RealmsScreen.getLocalizedString("mco.selectServer.expiredSubscribe");
                }
                final int buttonWidth = RealmsMainScreen.this.fontWidth(expirationButtonText) + 20;
                final int buttonHeight = 16;
                final int buttonX = x + RealmsMainScreen.this.fontWidth(expirationText) + 8;
                final int buttonY = y + 13;
                if (this.xm() >= buttonX && this.xm() < buttonX + buttonWidth && this.ym() > buttonY && (this.ym() <= buttonY + 16 & this.ym() < RealmsMainScreen.this.height() - 40) && this.ym() > 32 && !RealmsMainScreen.this.shouldShowPopup()) {
                    hovered = true;
                    RealmsMainScreen.this.expiredHover = true;
                }
                final int yImage = hovered ? 2 : 1;
                RealmsScreen.blit(buttonX, buttonY, 0.0f, (float)(46 + yImage * 20), buttonWidth / 2, 8, 256.0f, 256.0f);
                RealmsScreen.blit(buttonX + buttonWidth / 2, buttonY, (float)(200 - buttonWidth / 2), (float)(46 + yImage * 20), buttonWidth / 2, 8, 256.0f, 256.0f);
                RealmsScreen.blit(buttonX, buttonY + 8, 0.0f, (float)(46 + yImage * 20 + 12), buttonWidth / 2, 8, 256.0f, 256.0f);
                RealmsScreen.blit(buttonX + buttonWidth / 2, buttonY + 8, (float)(200 - buttonWidth / 2), (float)(46 + yImage * 20 + 12), buttonWidth / 2, 8, 256.0f, 256.0f);
                GL11.glPopMatrix();
                GL11.glDisable(3042);
                final int textHeight = y + 11 + 5;
                final int buttonTextColor = hovered ? 16777120 : 16777215;
                RealmsMainScreen.this.drawString(expirationText, x + 2, textHeight + 1, 15553363);
                RealmsMainScreen.this.drawCenteredString(expirationButtonText, buttonX + buttonWidth / 2, textHeight + 1, buttonTextColor);
            }
            else {
                if (serverData.worldType.equals(RealmsServer.WorldType.MINIGAME)) {
                    final int motdColor = 13413468;
                    final String miniGameStr = RealmsScreen.getLocalizedString("mco.selectServer.minigame") + " ";
                    final int mgWidth = RealmsMainScreen.this.fontWidth(miniGameStr);
                    RealmsMainScreen.this.drawString(miniGameStr, x + 2, y + 12, 13413468);
                    RealmsMainScreen.this.drawString(serverData.getMinigameName(), x + 2 + mgWidth, y + 12, 7105644);
                }
                else {
                    RealmsMainScreen.this.drawString(serverData.getDescription(), x + 2, y + 12, 7105644);
                }
                if (!RealmsMainScreen.this.isSelfOwnedServer(serverData)) {
                    RealmsMainScreen.this.drawString(serverData.owner, x + 2, y + 12 + 11, 5000268);
                }
            }
            RealmsMainScreen.this.drawString(serverData.getName(), x + 2, y + 1, 16777215);
            RealmsTextureManager.bindFace(serverData.ownerUUID);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            RealmsScreen.blit(x - 36, y, 8.0f, 8.0f, 8, 8, 32, 32, 64.0f, 64.0f);
            RealmsScreen.blit(x - 36, y, 40.0f, 8.0f, 8, 8, 32, 32, 64.0f, 64.0f);
        }
        
        private boolean renderRealmNote(final int i, final int x, final int y, String text, final boolean hover) {
            String label = RealmsScreen.getLocalizedString("mco.selectServer.note") + " ";
            final int labelWidth = RealmsMainScreen.this.fontWidth(label);
            final int textWidth = RealmsMainScreen.this.fontWidth(text);
            final int noteWidth = labelWidth + textWidth;
            final int offsetX = x + 2;
            final int offsetY = y + 12 + 11;
            final boolean noteIsHovered = this.xm() >= offsetX && this.xm() < offsetX + noteWidth && this.ym() > offsetY && this.ym() <= offsetY + RealmsMainScreen.this.fontLineHeight() && hover;
            int labelColor = 15553363;
            int textColor = 16777215;
            if (noteIsHovered) {
                labelColor = 12535109;
                textColor = 10526880;
                label = "§n" + label;
                text = "§n" + text;
            }
            RealmsMainScreen.this.drawString(label, offsetX, offsetY, labelColor, true);
            RealmsMainScreen.this.drawString(text, offsetX + labelWidth, offsetY, textColor, true);
            return noteIsHovered;
        }
        
        private void browseURL(final String url) {
            final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(new StringSelection(url), null);
            RealmsUtil.browseTo(url);
        }
    }
}
