// 
// Decompiled by Procyon v0.6.0
// 

package com.mojang.realmsclient.gui.screens;

import com.mojang.realmsclient.gui.LongRunningTask;
import com.mojang.realmsclient.util.RealmsTasks;
import net.minecraft.realms.Realms;
import org.lwjgl.input.Keyboard;
import net.minecraft.realms.RealmsButton;
import net.minecraft.realms.RealmsEditBox;
import com.mojang.realmsclient.RealmsMainScreen;
import com.mojang.realmsclient.dto.RealmsServer;
import net.minecraft.realms.RealmsScreen;

public class RealmsCreateRealmScreen extends RealmsScreen
{
    private final RealmsServer server;
    private final RealmsMainScreen lastScreen;
    private RealmsEditBox nameBox;
    private RealmsEditBox descriptionBox;
    private static final int CREATE_BUTTON = 0;
    private static final int CANCEL_BUTTON = 1;
    private static final int NAME_BOX_ID = 3;
    private static final int DESCRIPTION_BOX_ID = 4;
    private RealmsButton createButton;
    
    public RealmsCreateRealmScreen(final RealmsServer server, final RealmsMainScreen lastScreen) {
        this.server = server;
        this.lastScreen = lastScreen;
    }
    
    @Override
    public void tick() {
        if (this.nameBox != null) {
            this.nameBox.tick();
        }
        if (this.descriptionBox != null) {
            this.descriptionBox.tick();
        }
    }
    
    @Override
    public void init() {
        Keyboard.enableRepeatEvents(true);
        this.buttonsClear();
        this.buttonsAdd(this.createButton = RealmsScreen.newButton(0, this.width() / 2 - 100, this.height() / 4 + 120 + 17, 97, 20, RealmsScreen.getLocalizedString("mco.create.world")));
        this.buttonsAdd(RealmsScreen.newButton(1, this.width() / 2 + 5, this.height() / 4 + 120 + 17, 95, 20, RealmsScreen.getLocalizedString("gui.cancel")));
        this.createButton.active(false);
        (this.nameBox = this.newEditBox(3, this.width() / 2 - 100, 65, 200, 20)).setFocus(true);
        this.descriptionBox = this.newEditBox(4, this.width() / 2 - 100, 115, 200, 20);
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
        if (button.id() == 1) {
            Realms.setScreen(this.lastScreen);
        }
        else if (button.id() == 0) {
            this.createWorld();
        }
    }
    
    @Override
    public void keyPressed(final char ch, final int eventKey) {
        if (this.nameBox != null) {
            this.nameBox.keyPressed(ch, eventKey);
        }
        if (this.descriptionBox != null) {
            this.descriptionBox.keyPressed(ch, eventKey);
        }
        this.createButton.active(this.valid());
        switch (eventKey) {
            case 15: {
                if (this.nameBox != null) {
                    this.nameBox.setFocus(!this.nameBox.isFocused());
                }
                if (this.descriptionBox != null) {
                    this.descriptionBox.setFocus(!this.descriptionBox.isFocused());
                    break;
                }
                break;
            }
            case 28:
            case 156: {
                this.buttonClicked(this.createButton);
                break;
            }
            case 1: {
                Realms.setScreen(this.lastScreen);
                break;
            }
        }
    }
    
    private void createWorld() {
        if (this.valid()) {
            final RealmsResetWorldScreen resetWorldScreen = new RealmsResetWorldScreen(this.lastScreen, this.server, this.lastScreen.newScreen(), RealmsScreen.getLocalizedString("mco.selectServer.create"), RealmsScreen.getLocalizedString("mco.create.world.subtitle"), 10526880, RealmsScreen.getLocalizedString("mco.create.world.skip"));
            resetWorldScreen.setResetTitle(RealmsScreen.getLocalizedString("mco.create.world.reset.title"));
            final RealmsTasks.WorldCreationTask worldCreationTask = new RealmsTasks.WorldCreationTask(this.server.id, this.nameBox.getValue(), this.descriptionBox.getValue(), resetWorldScreen);
            final RealmsLongRunningMcoTaskScreen longRunningMcoTaskScreen = new RealmsLongRunningMcoTaskScreen(this.lastScreen, worldCreationTask);
            longRunningMcoTaskScreen.start();
            Realms.setScreen(longRunningMcoTaskScreen);
        }
    }
    
    private boolean valid() {
        return this.nameBox.getValue() != null && !this.nameBox.getValue().trim().isEmpty();
    }
    
    @Override
    public void mouseClicked(final int x, final int y, final int buttonNum) {
        if (this.nameBox != null) {
            this.nameBox.mouseClicked(x, y, buttonNum);
        }
        if (this.descriptionBox != null) {
            this.descriptionBox.mouseClicked(x, y, buttonNum);
        }
    }
    
    @Override
    public void render(final int xm, final int ym, final float a) {
        this.renderBackground();
        this.drawCenteredString(RealmsScreen.getLocalizedString("mco.selectServer.create"), this.width() / 2, 11, 16777215);
        this.drawString(RealmsScreen.getLocalizedString("mco.configure.world.name"), this.width() / 2 - 100, 52, 10526880);
        this.drawString(RealmsScreen.getLocalizedString("mco.configure.world.description"), this.width() / 2 - 100, 102, 10526880);
        if (this.nameBox != null) {
            this.nameBox.render();
        }
        if (this.descriptionBox != null) {
            this.descriptionBox.render();
        }
        super.render(xm, ym, a);
    }
}
