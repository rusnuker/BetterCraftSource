// 
// Decompiled by Procyon v0.6.0
// 

package com.mojang.realmsclient.gui.screens;

import java.util.Arrays;
import org.lwjgl.opengl.GL11;
import java.io.IOException;
import com.mojang.realmsclient.exception.RealmsServiceException;
import com.mojang.realmsclient.client.RealmsClient;
import net.minecraft.realms.Realms;
import org.lwjgl.input.Keyboard;
import java.util.List;
import com.mojang.realmsclient.gui.RealmsDataFetcher;
import net.minecraft.realms.RealmsScreen;

public class RealmsNotificationsScreen extends RealmsScreen
{
    private static final String INVITE_ICON_LOCATION = "realms:textures/gui/realms/invite_icon.png";
    private static final String TRIAL_ICON_LOCATION = "realms:textures/gui/realms/trial_icon.png";
    private static final String NEWS_ICON_LOCATION = "realms:textures/gui/realms/news_notification_mainscreen.png";
    private static final RealmsDataFetcher realmsDataFetcher;
    private volatile int numberOfPendingInvites;
    private static boolean checkedMcoAvailability;
    private static boolean trialAvailable;
    private static boolean validClient;
    private static boolean hasUnreadNews;
    private static final List<RealmsDataFetcher.Task> tasks;
    
    public RealmsNotificationsScreen(final RealmsScreen lastScreen) {
    }
    
    @Override
    public void init() {
        this.checkIfMcoEnabled();
        Keyboard.enableRepeatEvents(true);
        this.buttonsClear();
    }
    
    @Override
    public void tick() {
        if ((!Realms.getRealmsNotificationsEnabled() || !Realms.inTitleScreen() || !RealmsNotificationsScreen.validClient) && !RealmsNotificationsScreen.realmsDataFetcher.isStopped()) {
            RealmsNotificationsScreen.realmsDataFetcher.stop();
            return;
        }
        if (RealmsNotificationsScreen.validClient && Realms.getRealmsNotificationsEnabled()) {
            RealmsNotificationsScreen.realmsDataFetcher.initWithSpecificTaskList(RealmsNotificationsScreen.tasks);
            if (RealmsNotificationsScreen.realmsDataFetcher.isFetchedSinceLastTry(RealmsDataFetcher.Task.PENDING_INVITE)) {
                this.numberOfPendingInvites = RealmsNotificationsScreen.realmsDataFetcher.getPendingInvitesCount();
            }
            if (RealmsNotificationsScreen.realmsDataFetcher.isFetchedSinceLastTry(RealmsDataFetcher.Task.TRIAL_AVAILABLE)) {
                RealmsNotificationsScreen.trialAvailable = RealmsNotificationsScreen.realmsDataFetcher.isTrialAvailable();
            }
            if (RealmsNotificationsScreen.realmsDataFetcher.isFetchedSinceLastTry(RealmsDataFetcher.Task.UNREAD_NEWS)) {
                RealmsNotificationsScreen.hasUnreadNews = RealmsNotificationsScreen.realmsDataFetcher.hasUnreadNews();
            }
            RealmsNotificationsScreen.realmsDataFetcher.markClean();
        }
    }
    
    private void checkIfMcoEnabled() {
        if (!RealmsNotificationsScreen.checkedMcoAvailability) {
            RealmsNotificationsScreen.checkedMcoAvailability = true;
            new Thread("Realms Notification Availability checker #1") {
                @Override
                public void run() {
                    final RealmsClient client = RealmsClient.createRealmsClient();
                    try {
                        final RealmsClient.CompatibleVersionResponse versionResponse = client.clientCompatible();
                        if (!versionResponse.equals(RealmsClient.CompatibleVersionResponse.COMPATIBLE)) {
                            return;
                        }
                    }
                    catch (final RealmsServiceException e) {
                        if (e.httpResultCode != 401) {
                            RealmsNotificationsScreen.checkedMcoAvailability = false;
                        }
                        return;
                    }
                    catch (final IOException ignored) {
                        RealmsNotificationsScreen.checkedMcoAvailability = false;
                        return;
                    }
                    RealmsNotificationsScreen.validClient = true;
                }
            }.start();
        }
    }
    
    @Override
    public void render(final int xm, final int ym, final float a) {
        if (RealmsNotificationsScreen.validClient) {
            this.drawIcons(xm, ym);
        }
        super.render(xm, ym, a);
    }
    
    @Override
    public void mouseClicked(final int xm, final int ym, final int button) {
    }
    
    private void drawIcons(final int xm, final int ym) {
        final int pendingInvitesCount = this.numberOfPendingInvites;
        final int spacing = 24;
        final int topPos = this.height() / 4 + 48;
        final int baseX = this.width() / 2 + 80;
        final int baseY = topPos + 48 + 2;
        int iconOffset = 0;
        if (RealmsNotificationsScreen.hasUnreadNews) {
            RealmsScreen.bind("realms:textures/gui/realms/news_notification_mainscreen.png");
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glPushMatrix();
            GL11.glScalef(0.4f, 0.4f, 0.4f);
            RealmsScreen.blit((int)((baseX + 2 - iconOffset) * 2.5), (int)(baseY * 2.5), 0.0f, 0.0f, 40, 40, 40.0f, 40.0f);
            GL11.glPopMatrix();
            iconOffset += 14;
        }
        if (pendingInvitesCount != 0) {
            RealmsScreen.bind("realms:textures/gui/realms/invite_icon.png");
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glPushMatrix();
            RealmsScreen.blit(baseX - iconOffset, baseY - 6, 0.0f, 0.0f, 15, 25, 31.0f, 25.0f);
            GL11.glPopMatrix();
            iconOffset += 16;
        }
        if (RealmsNotificationsScreen.trialAvailable) {
            RealmsScreen.bind("realms:textures/gui/realms/trial_icon.png");
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glPushMatrix();
            int ySprite = 0;
            if ((System.currentTimeMillis() / 800L & 0x1L) == 0x1L) {
                ySprite = 8;
            }
            RealmsScreen.blit(baseX + 4 - iconOffset, baseY + 4, 0.0f, (float)ySprite, 8, 8, 8.0f, 16.0f);
            GL11.glPopMatrix();
        }
    }
    
    @Override
    public void removed() {
        RealmsNotificationsScreen.realmsDataFetcher.stop();
    }
    
    static {
        realmsDataFetcher = new RealmsDataFetcher();
        tasks = Arrays.asList(RealmsDataFetcher.Task.PENDING_INVITE, RealmsDataFetcher.Task.TRIAL_AVAILABLE, RealmsDataFetcher.Task.UNREAD_NEWS);
    }
}
