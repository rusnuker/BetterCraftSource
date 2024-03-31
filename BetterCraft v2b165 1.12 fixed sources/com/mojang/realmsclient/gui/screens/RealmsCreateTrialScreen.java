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
import net.minecraft.realms.RealmsScreen;

public class RealmsCreateTrialScreen extends RealmsScreen
{
    private final RealmsMainScreen lastScreen;
    private RealmsEditBox nameBox;
    private RealmsEditBox descriptionBox;
    private static final int CREATE_BUTTON = 0;
    private static final int CANCEL_BUTTON = 1;
    private static final int NAME_BOX_ID = 3;
    private static final int DESCRIPTION_BOX_ID = 4;
    private boolean initialized;
    private RealmsButton createButton;
    
    public RealmsCreateTrialScreen(final RealmsMainScreen lastScreen) {
        this.lastScreen = lastScreen;
    }
    
    @Override
    public void tick() {
        if (this.nameBox != null) {
            this.nameBox.tick();
            this.createButton.active(this.valid());
        }
        if (this.descriptionBox != null) {
            this.descriptionBox.tick();
        }
    }
    
    @Override
    public void init() {
        Keyboard.enableRepeatEvents(true);
        this.buttonsClear();
        if (!this.initialized) {
            this.initialized = true;
            (this.nameBox = this.newEditBox(3, this.width() / 2 - 100, 65, 200, 20)).setFocus(true);
            this.descriptionBox = this.newEditBox(4, this.width() / 2 - 100, 115, 200, 20);
        }
        this.buttonsAdd(this.createButton = RealmsScreen.newButton(0, this.width() / 2 - 100, this.height() / 4 + 120 + 17, 97, 20, RealmsScreen.getLocalizedString("mco.create.world")));
        this.buttonsAdd(RealmsScreen.newButton(1, this.width() / 2 + 5, this.height() / 4 + 120 + 17, 95, 20, RealmsScreen.getLocalizedString("gui.cancel")));
        this.createButton.active(this.valid());
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
            final RealmsTasks.TrialCreationTask trialCreationTask = new RealmsTasks.TrialCreationTask(this.nameBox.getValue(), this.descriptionBox.getValue(), this.lastScreen);
            final RealmsLongRunningMcoTaskScreen longRunningMcoTaskScreen = new RealmsLongRunningMcoTaskScreen(this.lastScreen, trialCreationTask);
            longRunningMcoTaskScreen.start();
            Realms.setScreen(longRunningMcoTaskScreen);
        }
    }
    
    private boolean valid() {
        return this.nameBox != null && this.nameBox.getValue() != null && !this.nameBox.getValue().trim().isEmpty();
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
        this.drawCenteredString(RealmsScreen.getLocalizedString("mco.trial.title"), this.width() / 2, 11, 16777215);
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
