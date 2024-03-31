// 
// Decompiled by Procyon v0.6.0
// 

package com.mojang.realmsclient.gui.screens;

import org.apache.logging.log4j.LogManager;
import java.awt.datatransfer.Clipboard;
import com.mojang.realmsclient.util.RealmsUtil;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.StringSelection;
import java.awt.Toolkit;
import com.mojang.realmsclient.exception.RealmsServiceException;
import com.mojang.realmsclient.gui.LongRunningTask;
import com.mojang.realmsclient.util.RealmsTasks;
import java.util.concurrent.locks.ReentrantLock;
import com.mojang.realmsclient.client.RealmsClient;
import net.minecraft.realms.Realms;
import com.mojang.realmsclient.gui.RealmsConstants;
import org.lwjgl.input.Keyboard;
import net.minecraft.realms.RealmsButton;
import com.mojang.realmsclient.dto.RealmsServer;
import com.mojang.realmsclient.RealmsMainScreen;
import org.apache.logging.log4j.Logger;
import net.minecraft.realms.RealmsScreen;

public class RealmsTermsScreen extends RealmsScreen
{
    private static final Logger LOGGER;
    private static final int BUTTON_AGREE_ID = 1;
    private static final int BUTTON_DISAGREE_ID = 2;
    private final RealmsScreen lastScreen;
    private final RealmsMainScreen mainScreen;
    private final RealmsServer realmsServer;
    private RealmsButton agreeButton;
    private boolean onLink;
    private final String realmsToSUrl = "https://minecraft.net/realms/terms";
    
    public RealmsTermsScreen(final RealmsScreen lastScreen, final RealmsMainScreen mainScreen, final RealmsServer realmsServer) {
        this.lastScreen = lastScreen;
        this.mainScreen = mainScreen;
        this.realmsServer = realmsServer;
    }
    
    @Override
    public void init() {
        Keyboard.enableRepeatEvents(true);
        this.buttonsClear();
        final int column1X = this.width() / 4;
        final int columnWidth = this.width() / 4 - 2;
        final int column2X = this.width() / 2 + 4;
        this.buttonsAdd(this.agreeButton = RealmsScreen.newButton(1, column1X, RealmsConstants.row(12), columnWidth, 20, RealmsScreen.getLocalizedString("mco.terms.buttons.agree")));
        this.buttonsAdd(RealmsScreen.newButton(2, column2X, RealmsConstants.row(12), columnWidth, 20, RealmsScreen.getLocalizedString("mco.terms.buttons.disagree")));
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
            case 2: {
                Realms.setScreen(this.lastScreen);
                break;
            }
            case 1: {
                this.agreedToTos();
                break;
            }
            default: {}
        }
    }
    
    @Override
    public void keyPressed(final char eventCharacter, final int eventKey) {
        if (eventKey == 1) {
            Realms.setScreen(this.lastScreen);
        }
    }
    
    private void agreedToTos() {
        final RealmsClient client = RealmsClient.createRealmsClient();
        try {
            client.agreeToTos();
            final RealmsLongRunningMcoTaskScreen longRunningMcoTaskScreen = new RealmsLongRunningMcoTaskScreen(this.lastScreen, new RealmsTasks.RealmsGetServerDetailsTask(this.mainScreen, this.lastScreen, this.realmsServer, new ReentrantLock()));
            longRunningMcoTaskScreen.start();
            Realms.setScreen(longRunningMcoTaskScreen);
        }
        catch (final RealmsServiceException ignored) {
            RealmsTermsScreen.LOGGER.error("Couldn't agree to TOS");
        }
    }
    
    @Override
    public void mouseClicked(final int x, final int y, final int buttonNum) {
        super.mouseClicked(x, y, buttonNum);
        if (this.onLink) {
            final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(new StringSelection("https://minecraft.net/realms/terms"), null);
            RealmsUtil.browseTo("https://minecraft.net/realms/terms");
        }
    }
    
    @Override
    public void render(final int xm, final int ym, final float a) {
        this.renderBackground();
        this.drawCenteredString(RealmsScreen.getLocalizedString("mco.terms.title"), this.width() / 2, 17, 16777215);
        this.drawString(RealmsScreen.getLocalizedString("mco.terms.sentence.1"), this.width() / 2 - 120, RealmsConstants.row(5), 16777215);
        final int firstPartWidth = this.fontWidth(RealmsScreen.getLocalizedString("mco.terms.sentence.1"));
        final int x1 = this.width() / 2 - 121 + firstPartWidth;
        final int y1 = RealmsConstants.row(5);
        final int x2 = x1 + this.fontWidth("mco.terms.sentence.2") + 1;
        final int y2 = y1 + 1 + this.fontLineHeight();
        if (x1 <= xm && xm <= x2 && y1 <= ym && ym <= y2) {
            this.onLink = true;
            this.drawString(" " + RealmsScreen.getLocalizedString("mco.terms.sentence.2"), this.width() / 2 - 120 + firstPartWidth, RealmsConstants.row(5), 7107012);
        }
        else {
            this.onLink = false;
            this.drawString(" " + RealmsScreen.getLocalizedString("mco.terms.sentence.2"), this.width() / 2 - 120 + firstPartWidth, RealmsConstants.row(5), 3368635);
        }
        super.render(xm, ym, a);
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
