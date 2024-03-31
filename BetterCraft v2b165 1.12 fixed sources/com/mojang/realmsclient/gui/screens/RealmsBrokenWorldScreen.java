// 
// Decompiled by Procyon v0.6.0
// 

package com.mojang.realmsclient.gui.screens;

import java.util.Arrays;
import org.apache.logging.log4j.LogManager;
import net.minecraft.realms.RealmsMth;
import org.lwjgl.opengl.GL11;
import com.mojang.realmsclient.util.RealmsTextureManager;
import com.mojang.realmsclient.dto.WorldDownload;
import com.mojang.realmsclient.gui.LongRunningTask;
import com.mojang.realmsclient.util.RealmsTasks;
import java.io.IOException;
import com.mojang.realmsclient.exception.RealmsServiceException;
import com.mojang.realmsclient.client.RealmsClient;
import net.minecraft.realms.Realms;
import net.minecraft.realms.RealmsButton;
import java.util.Iterator;
import com.mojang.realmsclient.dto.RealmsWorldOptions;
import java.util.Map;
import org.lwjgl.input.Keyboard;
import com.mojang.realmsclient.gui.RealmsConstants;
import java.util.ArrayList;
import java.util.List;
import com.mojang.realmsclient.dto.RealmsServer;
import com.mojang.realmsclient.RealmsMainScreen;
import org.apache.logging.log4j.Logger;
import net.minecraft.realms.RealmsScreen;

public class RealmsBrokenWorldScreen extends RealmsScreen
{
    private static final Logger LOGGER;
    private static final String SLOT_FRAME_LOCATION = "realms:textures/gui/realms/slot_frame.png";
    private static final String EMPTY_FRAME_LOCATION = "realms:textures/gui/realms/empty_frame.png";
    private final RealmsScreen lastScreen;
    private final RealmsMainScreen mainScreen;
    private RealmsServer serverData;
    private final long serverId;
    private String title;
    private String message;
    private int left_x;
    private int right_x;
    private final int default_button_width = 80;
    private final int default_button_offset = 5;
    private static final int BUTTON_BACK_ID = 0;
    private static final List<Integer> playButtonIds;
    private static final List<Integer> resetButtonIds;
    private static final List<Integer> downloadButtonIds;
    private static final List<Integer> downloadConfirmationIds;
    private final List<Integer> slotsThatHasBeenDownloaded;
    private static final int SWITCH_SLOT_ID_RESULT = 13;
    private static final int RESET_CONFIRMATION_ID = 14;
    private int animTick;
    
    public RealmsBrokenWorldScreen(final RealmsScreen lastScreen, final RealmsMainScreen mainScreen, final long serverId) {
        this.title = RealmsScreen.getLocalizedString("mco.brokenworld.title");
        this.message = RealmsScreen.getLocalizedString("mco.brokenworld.message.line1") + "\\n" + RealmsScreen.getLocalizedString("mco.brokenworld.message.line2");
        this.slotsThatHasBeenDownloaded = new ArrayList<Integer>();
        this.lastScreen = lastScreen;
        this.mainScreen = mainScreen;
        this.serverId = serverId;
    }
    
    public void setTitle(final String title) {
        this.title = title;
    }
    
    @Override
    public void mouseEvent() {
        super.mouseEvent();
    }
    
    @Override
    public void init() {
        this.buttonsClear();
        this.left_x = this.width() / 2 - 150;
        this.right_x = this.width() / 2 + 190;
        this.buttonsAdd(RealmsScreen.newButton(0, this.right_x - 80 + 8, RealmsConstants.row(13) - 5, 70, 20, RealmsScreen.getLocalizedString("gui.back")));
        if (this.serverData == null) {
            this.fetchServerData(this.serverId);
        }
        else {
            this.addButtons();
        }
        Keyboard.enableRepeatEvents(true);
    }
    
    public void addButtons() {
        for (final Map.Entry<Integer, RealmsWorldOptions> entry : this.serverData.slots.entrySet()) {
            final RealmsWorldOptions slot = entry.getValue();
            final boolean canPlay = entry.getKey() != this.serverData.activeSlot || this.serverData.worldType.equals(RealmsServer.WorldType.MINIGAME);
            final RealmsButton downloadButton = RealmsScreen.newButton(canPlay ? ((int)RealmsBrokenWorldScreen.playButtonIds.get(entry.getKey() - 1)) : ((int)RealmsBrokenWorldScreen.downloadButtonIds.get(entry.getKey() - 1)), this.getFramePositionX(entry.getKey()), RealmsConstants.row(8), 80, 20, RealmsScreen.getLocalizedString(canPlay ? "mco.brokenworld.play" : "mco.brokenworld.download"));
            if (this.slotsThatHasBeenDownloaded.contains(entry.getKey())) {
                downloadButton.active(false);
                downloadButton.msg(RealmsScreen.getLocalizedString("mco.brokenworld.downloaded"));
            }
            this.buttonsAdd(downloadButton);
            this.buttonsAdd(RealmsScreen.newButton(RealmsBrokenWorldScreen.resetButtonIds.get(entry.getKey() - 1), this.getFramePositionX(entry.getKey()), RealmsConstants.row(10), 80, 20, RealmsScreen.getLocalizedString("mco.brokenworld.reset")));
        }
    }
    
    @Override
    public void tick() {
        ++this.animTick;
    }
    
    @Override
    public void render(final int xm, final int ym, final float a) {
        this.renderBackground();
        super.render(xm, ym, a);
        this.drawCenteredString(this.title, this.width() / 2, 17, 16777215);
        final String[] lines = this.message.split("\\\\n");
        for (int i = 0; i < lines.length; ++i) {
            this.drawCenteredString(lines[i], this.width() / 2, RealmsConstants.row(-1) + 3 + i * 12, 10526880);
        }
        if (this.serverData == null) {
            return;
        }
        for (final Map.Entry<Integer, RealmsWorldOptions> entry : this.serverData.slots.entrySet()) {
            if (entry.getValue().templateImage != null && entry.getValue().templateId != -1L) {
                this.drawSlotFrame(this.getFramePositionX(entry.getKey()), RealmsConstants.row(1) + 5, xm, ym, this.serverData.activeSlot == entry.getKey() && !this.isMinigame(), entry.getValue().getSlotName(entry.getKey()), entry.getKey(), entry.getValue().templateId, entry.getValue().templateImage, entry.getValue().empty);
            }
            else {
                this.drawSlotFrame(this.getFramePositionX(entry.getKey()), RealmsConstants.row(1) + 5, xm, ym, this.serverData.activeSlot == entry.getKey() && !this.isMinigame(), entry.getValue().getSlotName(entry.getKey()), entry.getKey(), -1L, null, entry.getValue().empty);
            }
        }
    }
    
    private int getFramePositionX(final int i) {
        return this.left_x + (i - 1) * 110;
    }
    
    @Override
    public void removed() {
        Keyboard.enableRepeatEvents(false);
    }
    
    @Override
    public void buttonClicked(final RealmsButton button) {
        if (!button.active()) {
            return;
        }
        if (RealmsBrokenWorldScreen.playButtonIds.contains(button.id())) {
            final int slot = RealmsBrokenWorldScreen.playButtonIds.indexOf(button.id()) + 1;
            if (this.serverData.slots.get(slot).empty) {
                final RealmsResetWorldScreen resetWorldScreen = new RealmsResetWorldScreen(this, this.serverData, this, RealmsScreen.getLocalizedString("mco.configure.world.switch.slot"), RealmsScreen.getLocalizedString("mco.configure.world.switch.slot.subtitle"), 10526880, RealmsScreen.getLocalizedString("gui.cancel"));
                resetWorldScreen.setSlot(slot);
                resetWorldScreen.setResetTitle(RealmsScreen.getLocalizedString("mco.create.world.reset.title"));
                resetWorldScreen.setConfirmationId(14);
                Realms.setScreen(resetWorldScreen);
            }
            else {
                this.switchSlot(slot);
            }
        }
        else if (RealmsBrokenWorldScreen.resetButtonIds.contains(button.id())) {
            final int slot = RealmsBrokenWorldScreen.resetButtonIds.indexOf(button.id()) + 1;
            final RealmsResetWorldScreen realmsResetWorldScreen = new RealmsResetWorldScreen(this, this.serverData, this);
            if (slot != this.serverData.activeSlot || this.serverData.worldType.equals(RealmsServer.WorldType.MINIGAME)) {
                realmsResetWorldScreen.setSlot(slot);
            }
            realmsResetWorldScreen.setConfirmationId(14);
            Realms.setScreen(realmsResetWorldScreen);
        }
        else if (RealmsBrokenWorldScreen.downloadButtonIds.contains(button.id())) {
            final String line2 = RealmsScreen.getLocalizedString("mco.configure.world.restore.download.question.line1");
            final String line3 = RealmsScreen.getLocalizedString("mco.configure.world.restore.download.question.line2");
            Realms.setScreen(new RealmsLongConfirmationScreen(this, RealmsLongConfirmationScreen.Type.Info, line2, line3, true, button.id()));
        }
        else if (button.id() == 0) {
            this.backButtonClicked();
        }
    }
    
    @Override
    public void keyPressed(final char ch, final int eventKey) {
        if (eventKey == 1) {
            this.backButtonClicked();
        }
    }
    
    private void backButtonClicked() {
        Realms.setScreen(this.lastScreen);
    }
    
    private void fetchServerData(final long worldId) {
        new Thread() {
            @Override
            public void run() {
                final RealmsClient client = RealmsClient.createRealmsClient();
                try {
                    RealmsBrokenWorldScreen.this.serverData = client.getOwnWorld(worldId);
                    RealmsBrokenWorldScreen.this.addButtons();
                }
                catch (final RealmsServiceException e) {
                    RealmsBrokenWorldScreen.LOGGER.error("Couldn't get own world");
                    Realms.setScreen(new RealmsGenericErrorScreen(e.getMessage(), RealmsBrokenWorldScreen.this.lastScreen));
                }
                catch (final IOException ignored) {
                    RealmsBrokenWorldScreen.LOGGER.error("Couldn't parse response getting own world");
                }
            }
        }.start();
    }
    
    @Override
    public void confirmResult(final boolean result, final int id) {
        if (!result) {
            Realms.setScreen(this);
            return;
        }
        if (id == 13 || id == 14) {
            new Thread() {
                @Override
                public void run() {
                    final RealmsClient client = RealmsClient.createRealmsClient();
                    if (RealmsBrokenWorldScreen.this.serverData.state.equals(RealmsServer.State.CLOSED)) {
                        final RealmsTasks.OpenServerTask openServerTask = new RealmsTasks.OpenServerTask(RealmsBrokenWorldScreen.this.serverData, RealmsBrokenWorldScreen.this, RealmsBrokenWorldScreen.this.lastScreen, true);
                        final RealmsLongRunningMcoTaskScreen openWorldLongRunningTaskScreen = new RealmsLongRunningMcoTaskScreen(RealmsBrokenWorldScreen.this, openServerTask);
                        openWorldLongRunningTaskScreen.start();
                        Realms.setScreen(openWorldLongRunningTaskScreen);
                    }
                    else {
                        try {
                            RealmsBrokenWorldScreen.this.mainScreen.newScreen().play(client.getOwnWorld(RealmsBrokenWorldScreen.this.serverId), RealmsBrokenWorldScreen.this);
                        }
                        catch (final RealmsServiceException e) {
                            RealmsBrokenWorldScreen.LOGGER.error("Couldn't get own world");
                            Realms.setScreen(RealmsBrokenWorldScreen.this.lastScreen);
                        }
                        catch (final IOException ignored) {
                            RealmsBrokenWorldScreen.LOGGER.error("Couldn't parse response getting own world");
                            Realms.setScreen(RealmsBrokenWorldScreen.this.lastScreen);
                        }
                    }
                }
            }.start();
        }
        else if (RealmsBrokenWorldScreen.downloadButtonIds.contains(id)) {
            this.downloadWorld(RealmsBrokenWorldScreen.downloadButtonIds.indexOf(id) + 1);
        }
        else if (RealmsBrokenWorldScreen.downloadConfirmationIds.contains(id)) {
            this.slotsThatHasBeenDownloaded.add(RealmsBrokenWorldScreen.downloadConfirmationIds.indexOf(id) + 1);
            this.buttonsClear();
            this.addButtons();
        }
    }
    
    private void downloadWorld(final int slotId) {
        final RealmsClient client = RealmsClient.createRealmsClient();
        try {
            final WorldDownload worldDownload = client.download(this.serverData.id, slotId);
            final RealmsDownloadLatestWorldScreen downloadScreen = new RealmsDownloadLatestWorldScreen(this, worldDownload, this.serverData.name + " (" + this.serverData.slots.get(slotId).getSlotName(slotId) + ")");
            downloadScreen.setConfirmationId(RealmsBrokenWorldScreen.downloadConfirmationIds.get(slotId - 1));
            Realms.setScreen(downloadScreen);
        }
        catch (final RealmsServiceException e) {
            RealmsBrokenWorldScreen.LOGGER.error("Couldn't download world data");
            Realms.setScreen(new RealmsGenericErrorScreen(e, this));
        }
    }
    
    private boolean isMinigame() {
        return this.serverData != null && this.serverData.worldType.equals(RealmsServer.WorldType.MINIGAME);
    }
    
    private void drawSlotFrame(final int x, final int y, final int xm, final int ym, final boolean active, final String text, final int i, final long imageId, final String image, final boolean empty) {
        if (empty) {
            RealmsScreen.bind("realms:textures/gui/realms/empty_frame.png");
        }
        else if (image != null && imageId != -1L) {
            RealmsTextureManager.bindWorldTemplate(String.valueOf(imageId), image);
        }
        else if (i == 1) {
            RealmsScreen.bind("textures/gui/title/background/panorama_0.png");
        }
        else if (i == 2) {
            RealmsScreen.bind("textures/gui/title/background/panorama_2.png");
        }
        else if (i == 3) {
            RealmsScreen.bind("textures/gui/title/background/panorama_3.png");
        }
        else {
            RealmsTextureManager.bindWorldTemplate(String.valueOf(this.serverData.minigameId), this.serverData.minigameImage);
        }
        if (!active) {
            GL11.glColor4f(0.56f, 0.56f, 0.56f, 1.0f);
        }
        else if (active) {
            final float c = 0.9f + 0.1f * RealmsMth.cos(this.animTick * 0.2f);
            GL11.glColor4f(c, c, c, 1.0f);
        }
        RealmsScreen.blit(x + 3, y + 3, 0.0f, 0.0f, 74, 74, 74.0f, 74.0f);
        RealmsScreen.bind("realms:textures/gui/realms/slot_frame.png");
        if (active) {
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        }
        else {
            GL11.glColor4f(0.56f, 0.56f, 0.56f, 1.0f);
        }
        RealmsScreen.blit(x, y, 0.0f, 0.0f, 80, 80, 80.0f, 80.0f);
        this.drawCenteredString(text, x + 40, y + 66, 16777215);
    }
    
    private void switchSlot(final int id) {
        final RealmsTasks.SwitchSlotTask switchSlotTask = new RealmsTasks.SwitchSlotTask(this.serverData.id, id, this, 13);
        final RealmsLongRunningMcoTaskScreen longRunningMcoTaskScreen = new RealmsLongRunningMcoTaskScreen(this.lastScreen, switchSlotTask);
        longRunningMcoTaskScreen.start();
        Realms.setScreen(longRunningMcoTaskScreen);
    }
    
    static {
        LOGGER = LogManager.getLogger();
        playButtonIds = Arrays.asList(1, 2, 3);
        resetButtonIds = Arrays.asList(4, 5, 6);
        downloadButtonIds = Arrays.asList(7, 8, 9);
        downloadConfirmationIds = Arrays.asList(10, 11, 12);
    }
}
