// 
// Decompiled by Procyon v0.6.0
// 

package com.mojang.realmsclient.util;

import com.mojang.realmsclient.dto.WorldDownload;
import com.mojang.realmsclient.gui.screens.RealmsDownloadLatestWorldScreen;
import com.mojang.realmsclient.dto.Backup;
import com.mojang.realmsclient.gui.screens.RealmsResetWorldScreen;
import java.io.UnsupportedEncodingException;
import net.minecraft.realms.RealmsConnect;
import com.mojang.realmsclient.dto.RealmsServerAddress;
import com.mojang.realmsclient.gui.screens.RealmsLongRunningMcoTaskScreen;
import com.mojang.realmsclient.gui.screens.RealmsLongConfirmationScreen;
import com.mojang.realmsclient.gui.screens.RealmsResourcePackScreen;
import com.mojang.realmsclient.gui.screens.RealmsGenericErrorScreen;
import com.mojang.realmsclient.gui.screens.RealmsBrokenWorldScreen;
import com.mojang.realmsclient.gui.screens.RealmsTermsScreen;
import java.io.IOException;
import com.mojang.realmsclient.exception.RealmsServiceException;
import java.util.concurrent.locks.ReentrantLock;
import com.mojang.realmsclient.dto.WorldTemplate;
import com.mojang.realmsclient.exception.RetryCallException;
import net.minecraft.realms.Realms;
import com.mojang.realmsclient.RealmsMainScreen;
import com.mojang.realmsclient.gui.screens.RealmsConfigureWorldScreen;
import com.mojang.realmsclient.client.RealmsClient;
import net.minecraft.realms.RealmsScreen;
import com.mojang.realmsclient.dto.RealmsServer;
import com.mojang.realmsclient.gui.LongRunningTask;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RealmsTasks
{
    private static final Logger LOGGER;
    private static final int NUMBER_OF_RETRIES = 25;
    
    private static void pause(final int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        }
        catch (final InterruptedException e) {
            RealmsTasks.LOGGER.error("", e);
        }
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    public static class OpenServerTask extends LongRunningTask
    {
        private final RealmsServer serverData;
        private final RealmsScreen returnScreen;
        private final boolean join;
        private final RealmsScreen mainScreen;
        
        public OpenServerTask(final RealmsServer realmsServer, final RealmsScreen returnScreen, final RealmsScreen mainScreen, final boolean join) {
            this.serverData = realmsServer;
            this.returnScreen = returnScreen;
            this.join = join;
            this.mainScreen = mainScreen;
        }
        
        @Override
        public void run() {
            this.setTitle(RealmsScreen.getLocalizedString("mco.configure.world.opening"));
            final RealmsClient client = RealmsClient.createRealmsClient();
            for (int i = 0; i < 25; ++i) {
                if (this.aborted()) {
                    return;
                }
                try {
                    final boolean openResult = client.open(this.serverData.id);
                    if (openResult) {
                        if (this.returnScreen instanceof RealmsConfigureWorldScreen) {
                            ((RealmsConfigureWorldScreen)this.returnScreen).stateChanged();
                        }
                        this.serverData.state = RealmsServer.State.OPEN;
                        if (this.join) {
                            ((RealmsMainScreen)this.mainScreen).play(this.serverData, this.returnScreen);
                            break;
                        }
                        Realms.setScreen(this.returnScreen);
                        break;
                    }
                }
                catch (final RetryCallException e) {
                    if (this.aborted()) {
                        return;
                    }
                    pause(e.delaySeconds);
                }
                catch (final Exception e2) {
                    if (this.aborted()) {
                        return;
                    }
                    RealmsTasks.LOGGER.error("Failed to open server", e2);
                    this.error("Failed to open the server");
                }
            }
        }
    }
    
    public static class CloseServerTask extends LongRunningTask
    {
        private final RealmsServer serverData;
        private final RealmsConfigureWorldScreen configureScreen;
        
        public CloseServerTask(final RealmsServer realmsServer, final RealmsConfigureWorldScreen configureWorldScreen) {
            this.serverData = realmsServer;
            this.configureScreen = configureWorldScreen;
        }
        
        @Override
        public void run() {
            this.setTitle(RealmsScreen.getLocalizedString("mco.configure.world.closing"));
            final RealmsClient client = RealmsClient.createRealmsClient();
            for (int i = 0; i < 25; ++i) {
                if (this.aborted()) {
                    return;
                }
                try {
                    final boolean closeResult = client.close(this.serverData.id);
                    if (closeResult) {
                        this.configureScreen.stateChanged();
                        this.serverData.state = RealmsServer.State.CLOSED;
                        Realms.setScreen(this.configureScreen);
                        break;
                    }
                }
                catch (final RetryCallException e) {
                    if (this.aborted()) {
                        return;
                    }
                    pause(e.delaySeconds);
                }
                catch (final Exception e2) {
                    if (this.aborted()) {
                        return;
                    }
                    RealmsTasks.LOGGER.error("Failed to close server", e2);
                    this.error("Failed to close the server");
                }
            }
        }
    }
    
    public static class SwitchSlotTask extends LongRunningTask
    {
        private final long worldId;
        private final int slot;
        private final RealmsScreen lastScreen;
        private final int confirmId;
        
        public SwitchSlotTask(final long worldId, final int slot, final RealmsScreen lastScreen, final int confirmId) {
            this.worldId = worldId;
            this.slot = slot;
            this.lastScreen = lastScreen;
            this.confirmId = confirmId;
        }
        
        @Override
        public void run() {
            final RealmsClient client = RealmsClient.createRealmsClient();
            final String title = RealmsScreen.getLocalizedString("mco.minigame.world.slot.screen.title");
            this.setTitle(title);
            for (int i = 0; i < 25; ++i) {
                try {
                    if (this.aborted()) {
                        return;
                    }
                    if (client.switchSlot(this.worldId, this.slot)) {
                        this.lastScreen.confirmResult(true, this.confirmId);
                        break;
                    }
                }
                catch (final RetryCallException e) {
                    if (this.aborted()) {
                        return;
                    }
                    pause(e.delaySeconds);
                }
                catch (final Exception e2) {
                    if (this.aborted()) {
                        return;
                    }
                    RealmsTasks.LOGGER.error("Couldn't switch world!");
                    this.error(e2.toString());
                }
            }
        }
    }
    
    public static class SwitchMinigameTask extends LongRunningTask
    {
        private final long worldId;
        private final WorldTemplate worldTemplate;
        private final RealmsConfigureWorldScreen lastScreen;
        
        public SwitchMinigameTask(final long worldId, final WorldTemplate worldTemplate, final RealmsConfigureWorldScreen lastScreen) {
            this.worldId = worldId;
            this.worldTemplate = worldTemplate;
            this.lastScreen = lastScreen;
        }
        
        @Override
        public void run() {
            final RealmsClient client = RealmsClient.createRealmsClient();
            final String title = RealmsScreen.getLocalizedString("mco.minigame.world.starting.screen.title");
            this.setTitle(title);
            for (int i = 0; i < 25; ++i) {
                try {
                    if (this.aborted()) {
                        return;
                    }
                    if (client.putIntoMinigameMode(this.worldId, this.worldTemplate.id)) {
                        Realms.setScreen(this.lastScreen);
                        break;
                    }
                }
                catch (final RetryCallException e) {
                    if (this.aborted()) {
                        return;
                    }
                    pause(e.delaySeconds);
                }
                catch (final Exception e2) {
                    if (this.aborted()) {
                        return;
                    }
                    RealmsTasks.LOGGER.error("Couldn't start mini game!");
                    this.error(e2.toString());
                }
            }
        }
    }
    
    public static class ResettingWorldTask extends LongRunningTask
    {
        private final String seed;
        private final WorldTemplate worldTemplate;
        private final int levelType;
        private final boolean generateStructures;
        private final long serverId;
        private final RealmsScreen lastScreen;
        private int confirmationId;
        private String title;
        
        public ResettingWorldTask(final long serverId, final RealmsScreen lastScreen, final WorldTemplate worldTemplate) {
            this.confirmationId = -1;
            this.title = RealmsScreen.getLocalizedString("mco.reset.world.resetting.screen.title");
            this.seed = null;
            this.worldTemplate = worldTemplate;
            this.levelType = -1;
            this.generateStructures = true;
            this.serverId = serverId;
            this.lastScreen = lastScreen;
        }
        
        public ResettingWorldTask(final long serverId, final RealmsScreen lastScreen, final String seed, final int levelType, final boolean generateStructures) {
            this.confirmationId = -1;
            this.title = RealmsScreen.getLocalizedString("mco.reset.world.resetting.screen.title");
            this.seed = seed;
            this.worldTemplate = null;
            this.levelType = levelType;
            this.generateStructures = generateStructures;
            this.serverId = serverId;
            this.lastScreen = lastScreen;
        }
        
        public void setConfirmationId(final int confirmationId) {
            this.confirmationId = confirmationId;
        }
        
        public void setResetTitle(final String title) {
            this.title = title;
        }
        
        @Override
        public void run() {
            final RealmsClient client = RealmsClient.createRealmsClient();
            this.setTitle(this.title);
            for (int i = 0; i < 25; ++i) {
                try {
                    if (this.aborted()) {
                        return;
                    }
                    if (this.worldTemplate != null) {
                        client.resetWorldWithTemplate(this.serverId, this.worldTemplate.id);
                    }
                    else {
                        client.resetWorldWithSeed(this.serverId, this.seed, this.levelType, this.generateStructures);
                    }
                    if (this.aborted()) {
                        return;
                    }
                    if (this.confirmationId == -1) {
                        Realms.setScreen(this.lastScreen);
                    }
                    else {
                        this.lastScreen.confirmResult(true, this.confirmationId);
                    }
                    return;
                }
                catch (final RetryCallException e) {
                    if (this.aborted()) {
                        return;
                    }
                    pause(e.delaySeconds);
                }
                catch (final Exception e2) {
                    if (this.aborted()) {
                        return;
                    }
                    RealmsTasks.LOGGER.error("Couldn't reset world");
                    this.error(e2.toString());
                    return;
                }
            }
        }
    }
    
    public static class RealmsGetServerDetailsTask extends LongRunningTask
    {
        private final RealmsServer server;
        private final RealmsScreen lastScreen;
        private final RealmsMainScreen mainScreen;
        private final ReentrantLock connectLock;
        
        public RealmsGetServerDetailsTask(final RealmsMainScreen mainScreen, final RealmsScreen lastScreen, final RealmsServer server, final ReentrantLock connectLock) {
            this.lastScreen = lastScreen;
            this.mainScreen = mainScreen;
            this.server = server;
            this.connectLock = connectLock;
        }
        
        @Override
        public void run() {
            this.setTitle(RealmsScreen.getLocalizedString("mco.connect.connecting"));
            final RealmsClient client = RealmsClient.createRealmsClient();
            boolean addressRetrieved = false;
            boolean hasError = false;
            int sleepTime = 5;
            RealmsServerAddress address = null;
            boolean tosNotAccepted = false;
            boolean brokenWorld = false;
            for (int i = 0; i < 40; ++i) {
                if (this.aborted()) {
                    break;
                }
                try {
                    address = client.join(this.server.id);
                    addressRetrieved = true;
                }
                catch (final RetryCallException e) {
                    sleepTime = e.delaySeconds;
                }
                catch (final RealmsServiceException e2) {
                    if (e2.errorCode == 6002) {
                        tosNotAccepted = true;
                        break;
                    }
                    if (e2.errorCode == 6006) {
                        brokenWorld = true;
                        break;
                    }
                    hasError = true;
                    this.error(e2.toString());
                    RealmsTasks.LOGGER.error("Couldn't connect to world", e2);
                    break;
                }
                catch (final IOException e3) {
                    RealmsTasks.LOGGER.error("Couldn't parse response connecting to world", e3);
                }
                catch (final Exception e4) {
                    hasError = true;
                    RealmsTasks.LOGGER.error("Couldn't connect to world", e4);
                    this.error(e4.getLocalizedMessage());
                    break;
                }
                if (addressRetrieved) {
                    break;
                }
                this.sleep(sleepTime);
            }
            if (tosNotAccepted) {
                Realms.setScreen(new RealmsTermsScreen(this.lastScreen, this.mainScreen, this.server));
            }
            else if (brokenWorld) {
                if (this.server.ownerUUID.equals(Realms.getUUID())) {
                    final RealmsBrokenWorldScreen brokenWorldScreen = new RealmsBrokenWorldScreen(this.lastScreen, this.mainScreen, this.server.id);
                    if (this.server.worldType.equals(RealmsServer.WorldType.MINIGAME)) {
                        brokenWorldScreen.setTitle(RealmsScreen.getLocalizedString("mco.brokenworld.minigame.title"));
                    }
                    Realms.setScreen(brokenWorldScreen);
                }
                else {
                    Realms.setScreen(new RealmsGenericErrorScreen(RealmsScreen.getLocalizedString("mco.brokenworld.nonowner.title"), RealmsScreen.getLocalizedString("mco.brokenworld.nonowner.error"), this.lastScreen));
                }
            }
            else if (!this.aborted() && !hasError) {
                if (addressRetrieved) {
                    if (address.resourcePackUrl != null && address.resourcePackHash != null) {
                        final String line2 = RealmsScreen.getLocalizedString("mco.configure.world.resourcepack.question.line1");
                        final String line3 = RealmsScreen.getLocalizedString("mco.configure.world.resourcepack.question.line2");
                        Realms.setScreen(new RealmsLongConfirmationScreen(new RealmsResourcePackScreen(this.lastScreen, address, this.connectLock), RealmsLongConfirmationScreen.Type.Info, line2, line3, true, 100));
                    }
                    else {
                        final RealmsLongRunningMcoTaskScreen longRunningMcoTaskScreen = new RealmsLongRunningMcoTaskScreen(this.lastScreen, new RealmsConnectTask(this.lastScreen, address));
                        longRunningMcoTaskScreen.start();
                        Realms.setScreen(longRunningMcoTaskScreen);
                    }
                }
                else {
                    this.error(RealmsScreen.getLocalizedString("mco.errorMessage.connectionFailure"));
                }
            }
        }
        
        private void sleep(final int sleepTimeSeconds) {
            try {
                Thread.sleep(sleepTimeSeconds * 1000);
            }
            catch (final InterruptedException e1) {
                RealmsTasks.LOGGER.warn(e1.getLocalizedMessage());
            }
        }
    }
    
    public static class RealmsConnectTask extends LongRunningTask
    {
        private final RealmsConnect realmsConnect;
        private final RealmsServerAddress a;
        
        public RealmsConnectTask(final RealmsScreen lastScreen, final RealmsServerAddress address) {
            this.a = address;
            this.realmsConnect = new RealmsConnect(lastScreen);
        }
        
        @Override
        public void run() {
            this.setTitle(RealmsScreen.getLocalizedString("mco.connect.connecting"));
            final net.minecraft.realms.RealmsServerAddress address = net.minecraft.realms.RealmsServerAddress.parseString(this.a.address);
            this.realmsConnect.connect(address.getHost(), address.getPort());
        }
        
        @Override
        public void abortTask() {
            this.realmsConnect.abort();
            Realms.clearResourcePack();
        }
        
        @Override
        public void tick() {
            this.realmsConnect.tick();
        }
    }
    
    public static class WorldCreationTask extends LongRunningTask
    {
        private final String name;
        private final String motd;
        private final long worldId;
        private final RealmsScreen lastScreen;
        
        public WorldCreationTask(final long worldId, final String name, final String motd, final RealmsScreen lastScreen) {
            this.worldId = worldId;
            this.name = name;
            this.motd = motd;
            this.lastScreen = lastScreen;
        }
        
        @Override
        public void run() {
            final String title = RealmsScreen.getLocalizedString("mco.create.world.wait");
            this.setTitle(title);
            final RealmsClient client = RealmsClient.createRealmsClient();
            try {
                client.initializeWorld(this.worldId, this.name, this.motd);
                Realms.setScreen(this.lastScreen);
            }
            catch (final RealmsServiceException e) {
                RealmsTasks.LOGGER.error("Couldn't create world");
                this.error(e.toString());
            }
            catch (final UnsupportedEncodingException e2) {
                RealmsTasks.LOGGER.error("Couldn't create world");
                this.error(e2.getLocalizedMessage());
            }
            catch (final IOException e3) {
                RealmsTasks.LOGGER.error("Could not parse response creating world");
                this.error(e3.getLocalizedMessage());
            }
            catch (final Exception e4) {
                RealmsTasks.LOGGER.error("Could not create world");
                this.error(e4.getLocalizedMessage());
            }
        }
    }
    
    public static class TrialCreationTask extends LongRunningTask
    {
        private final String name;
        private final String motd;
        private final RealmsMainScreen lastScreen;
        
        public TrialCreationTask(final String name, final String motd, final RealmsMainScreen lastScreen) {
            this.name = name;
            this.motd = motd;
            this.lastScreen = lastScreen;
        }
        
        @Override
        public void run() {
            final String title = RealmsScreen.getLocalizedString("mco.create.world.wait");
            this.setTitle(title);
            final RealmsClient client = RealmsClient.createRealmsClient();
            try {
                final RealmsServer server = client.createTrial(this.name, this.motd);
                if (server != null) {
                    this.lastScreen.setCreatedTrial(true);
                    this.lastScreen.closePopup();
                    final RealmsResetWorldScreen resetWorldScreen = new RealmsResetWorldScreen(this.lastScreen, server, this.lastScreen.newScreen(), RealmsScreen.getLocalizedString("mco.selectServer.create"), RealmsScreen.getLocalizedString("mco.create.world.subtitle"), 10526880, RealmsScreen.getLocalizedString("mco.create.world.skip"));
                    resetWorldScreen.setResetTitle(RealmsScreen.getLocalizedString("mco.create.world.reset.title"));
                    Realms.setScreen(resetWorldScreen);
                }
                else {
                    this.error(RealmsScreen.getLocalizedString("mco.trial.unavailable"));
                }
            }
            catch (final RealmsServiceException e) {
                RealmsTasks.LOGGER.error("Couldn't create trial");
                this.error(e.toString());
            }
            catch (final UnsupportedEncodingException e2) {
                RealmsTasks.LOGGER.error("Couldn't create trial");
                this.error(e2.getLocalizedMessage());
            }
            catch (final IOException e3) {
                RealmsTasks.LOGGER.error("Could not parse response creating trial");
                this.error(e3.getLocalizedMessage());
            }
            catch (final Exception e4) {
                RealmsTasks.LOGGER.error("Could not create trial");
                this.error(e4.getLocalizedMessage());
            }
        }
    }
    
    public static class RestoreTask extends LongRunningTask
    {
        private final Backup backup;
        private final long worldId;
        private final RealmsConfigureWorldScreen lastScreen;
        
        public RestoreTask(final Backup backup, final long worldId, final RealmsConfigureWorldScreen lastScreen) {
            this.backup = backup;
            this.worldId = worldId;
            this.lastScreen = lastScreen;
        }
        
        @Override
        public void run() {
            this.setTitle(RealmsScreen.getLocalizedString("mco.backup.restoring"));
            final RealmsClient client = RealmsClient.createRealmsClient();
            for (int i = 0; i < 25; ++i) {
                try {
                    if (this.aborted()) {
                        return;
                    }
                    client.restoreWorld(this.worldId, this.backup.backupId);
                    pause(1);
                    if (this.aborted()) {
                        return;
                    }
                    Realms.setScreen(this.lastScreen.getNewScreen());
                    return;
                }
                catch (final RetryCallException e) {
                    if (this.aborted()) {
                        return;
                    }
                    pause(e.delaySeconds);
                }
                catch (final RealmsServiceException e2) {
                    if (this.aborted()) {
                        return;
                    }
                    RealmsTasks.LOGGER.error("Couldn't restore backup", e2);
                    Realms.setScreen(new RealmsGenericErrorScreen(e2, this.lastScreen));
                    return;
                }
                catch (final Exception e3) {
                    if (this.aborted()) {
                        return;
                    }
                    RealmsTasks.LOGGER.error("Couldn't restore backup", e3);
                    this.error(e3.getLocalizedMessage());
                    return;
                }
            }
        }
    }
    
    public static class DownloadTask extends LongRunningTask
    {
        private final long worldId;
        private final int slot;
        private final RealmsScreen lastScreen;
        private final String downloadName;
        
        public DownloadTask(final long worldId, final int slot, final String downloadName, final RealmsScreen lastScreen) {
            this.worldId = worldId;
            this.slot = slot;
            this.lastScreen = lastScreen;
            this.downloadName = downloadName;
        }
        
        @Override
        public void run() {
            this.setTitle(RealmsScreen.getLocalizedString("mco.download.preparing"));
            final RealmsClient client = RealmsClient.createRealmsClient();
            for (int i = 0; i < 25; ++i) {
                try {
                    if (this.aborted()) {
                        return;
                    }
                    final WorldDownload worldDownload = client.download(this.worldId, this.slot);
                    pause(1);
                    if (this.aborted()) {
                        return;
                    }
                    Realms.setScreen(new RealmsDownloadLatestWorldScreen(this.lastScreen, worldDownload, this.downloadName));
                    return;
                }
                catch (final RetryCallException e) {
                    if (this.aborted()) {
                        return;
                    }
                    pause(e.delaySeconds);
                }
                catch (final RealmsServiceException e2) {
                    if (this.aborted()) {
                        return;
                    }
                    RealmsTasks.LOGGER.error("Couldn't download world data");
                    Realms.setScreen(new RealmsGenericErrorScreen(e2, this.lastScreen));
                    return;
                }
                catch (final Exception e3) {
                    if (this.aborted()) {
                        return;
                    }
                    RealmsTasks.LOGGER.error("Couldn't download world data", e3);
                    this.error(e3.getLocalizedMessage());
                    return;
                }
            }
        }
    }
}
