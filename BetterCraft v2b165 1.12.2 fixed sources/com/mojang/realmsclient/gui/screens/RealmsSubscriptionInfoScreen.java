// 
// Decompiled by Procyon v0.6.0
// 

package com.mojang.realmsclient.gui.screens;

import org.apache.logging.log4j.LogManager;
import java.util.Locale;
import java.awt.datatransfer.Clipboard;
import com.mojang.realmsclient.util.RealmsUtil;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.StringSelection;
import java.awt.Toolkit;
import net.minecraft.realms.RealmsButton;
import java.util.Calendar;
import java.text.DateFormat;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.io.IOException;
import com.mojang.realmsclient.exception.RealmsServiceException;
import net.minecraft.realms.Realms;
import com.mojang.realmsclient.client.RealmsClient;
import com.mojang.realmsclient.gui.RealmsConstants;
import org.lwjgl.input.Keyboard;
import com.mojang.realmsclient.dto.Subscription;
import com.mojang.realmsclient.dto.RealmsServer;
import org.apache.logging.log4j.Logger;
import net.minecraft.realms.RealmsScreen;

public class RealmsSubscriptionInfoScreen extends RealmsScreen
{
    private static final Logger LOGGER;
    private final RealmsScreen lastScreen;
    private final RealmsServer serverData;
    private final RealmsScreen mainScreen;
    private final int BUTTON_BACK_ID = 0;
    private final int BUTTON_DELETE_ID = 1;
    private final int BUTTON_SUBSCRIPTION_ID = 2;
    private int daysLeft;
    private String startDate;
    private Subscription.SubscriptionType type;
    private final String PURCHASE_LINK = "https://account.mojang.com/buy/realms";
    
    public RealmsSubscriptionInfoScreen(final RealmsScreen lastScreen, final RealmsServer serverData, final RealmsScreen mainScreen) {
        this.lastScreen = lastScreen;
        this.serverData = serverData;
        this.mainScreen = mainScreen;
    }
    
    @Override
    public void init() {
        this.getSubscription(this.serverData.id);
        Keyboard.enableRepeatEvents(true);
        this.buttonsAdd(RealmsScreen.newButton(2, this.width() / 2 - 100, RealmsConstants.row(6), RealmsScreen.getLocalizedString("mco.configure.world.subscription.extend")));
        this.buttonsAdd(RealmsScreen.newButton(0, this.width() / 2 - 100, RealmsConstants.row(12), RealmsScreen.getLocalizedString("gui.back")));
        if (this.serverData.expired) {
            this.buttonsAdd(RealmsScreen.newButton(1, this.width() / 2 - 100, RealmsConstants.row(10), RealmsScreen.getLocalizedString("mco.configure.world.delete.button")));
        }
    }
    
    private void getSubscription(final long worldId) {
        final RealmsClient client = RealmsClient.createRealmsClient();
        try {
            final Subscription subscription = client.subscriptionFor(worldId);
            this.daysLeft = subscription.daysLeft;
            this.startDate = this.localPresentation(subscription.startDate);
            this.type = subscription.type;
        }
        catch (final RealmsServiceException e) {
            RealmsSubscriptionInfoScreen.LOGGER.error("Couldn't get subscription");
            Realms.setScreen(new RealmsGenericErrorScreen(e, this.lastScreen));
        }
        catch (final IOException ignored) {
            RealmsSubscriptionInfoScreen.LOGGER.error("Couldn't parse response subscribing");
        }
    }
    
    @Override
    public void confirmResult(final boolean result, final int id) {
        if (id == 1 && result) {
            new Thread("Realms-delete-realm") {
                @Override
                public void run() {
                    try {
                        final RealmsClient client = RealmsClient.createRealmsClient();
                        client.deleteWorld(RealmsSubscriptionInfoScreen.this.serverData.id);
                    }
                    catch (final RealmsServiceException e) {
                        RealmsSubscriptionInfoScreen.LOGGER.error("Couldn't delete world");
                        RealmsSubscriptionInfoScreen.LOGGER.error(e);
                    }
                    catch (final IOException e2) {
                        RealmsSubscriptionInfoScreen.LOGGER.error("Couldn't delete world");
                        e2.printStackTrace();
                    }
                    Realms.setScreen(RealmsSubscriptionInfoScreen.this.mainScreen);
                }
            }.start();
        }
        Realms.setScreen(this);
    }
    
    private String localPresentation(final long cetTime) {
        final Calendar cal = new GregorianCalendar(TimeZone.getDefault());
        cal.setTimeInMillis(cetTime);
        return DateFormat.getDateTimeInstance().format(cal.getTime());
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
        switch (button.id()) {
            case 0: {
                Realms.setScreen(this.lastScreen);
                break;
            }
            case 2: {
                final String extensionUrl = "https://account.mojang.com/buy/realms?sid=" + this.serverData.remoteSubscriptionId + "&pid=" + Realms.getUUID();
                final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(new StringSelection(extensionUrl), null);
                RealmsUtil.browseTo(extensionUrl);
                break;
            }
            case 1: {
                final String line2 = RealmsScreen.getLocalizedString("mco.configure.world.delete.question.line1");
                final String line3 = RealmsScreen.getLocalizedString("mco.configure.world.delete.question.line2");
                Realms.setScreen(new RealmsLongConfirmationScreen(this, RealmsLongConfirmationScreen.Type.Warning, line2, line3, true, 1));
                break;
            }
        }
    }
    
    @Override
    public void keyPressed(final char ch, final int eventKey) {
        if (eventKey == 1) {
            Realms.setScreen(this.lastScreen);
        }
    }
    
    @Override
    public void render(final int xm, final int ym, final float a) {
        this.renderBackground();
        final int center = this.width() / 2 - 100;
        this.drawCenteredString(RealmsScreen.getLocalizedString("mco.configure.world.subscription.title"), this.width() / 2, 17, 16777215);
        this.drawString(RealmsScreen.getLocalizedString("mco.configure.world.subscription.start"), center, RealmsConstants.row(0), 10526880);
        this.drawString(this.startDate, center, RealmsConstants.row(1), 16777215);
        if (this.type == Subscription.SubscriptionType.NORMAL) {
            this.drawString(RealmsScreen.getLocalizedString("mco.configure.world.subscription.timeleft"), center, RealmsConstants.row(3), 10526880);
        }
        else if (this.type == Subscription.SubscriptionType.RECURRING) {
            this.drawString(RealmsScreen.getLocalizedString("mco.configure.world.subscription.recurring.daysleft"), center, RealmsConstants.row(3), 10526880);
        }
        this.drawString(this.daysLeftPresentation(this.daysLeft), center, RealmsConstants.row(4), 16777215);
        super.render(xm, ym, a);
    }
    
    private String daysLeftPresentation(final int daysLeft) {
        if (daysLeft == -1 && this.serverData.expired) {
            return RealmsScreen.getLocalizedString("mco.configure.world.subscription.expired");
        }
        if (daysLeft <= 1) {
            return RealmsScreen.getLocalizedString("mco.configure.world.subscription.less_than_a_day");
        }
        final int months = daysLeft / 30;
        final int days = daysLeft % 30;
        final StringBuilder sb = new StringBuilder();
        if (months > 0) {
            sb.append(months).append(" ");
            if (months == 1) {
                sb.append(RealmsScreen.getLocalizedString("mco.configure.world.subscription.month").toLowerCase(Locale.ROOT));
            }
            else {
                sb.append(RealmsScreen.getLocalizedString("mco.configure.world.subscription.months").toLowerCase(Locale.ROOT));
            }
        }
        if (days > 0) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(days).append(" ");
            if (days == 1) {
                sb.append(RealmsScreen.getLocalizedString("mco.configure.world.subscription.day").toLowerCase(Locale.ROOT));
            }
            else {
                sb.append(RealmsScreen.getLocalizedString("mco.configure.world.subscription.days").toLowerCase(Locale.ROOT));
            }
        }
        return sb.toString();
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
