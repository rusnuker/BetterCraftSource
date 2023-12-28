// 
// Decompiled by Procyon v0.6.0
// 

package com.mojang.realmsclient.gui.screens;

import org.apache.logging.log4j.LogManager;
import com.mojang.realmsclient.gui.LongRunningTask;
import com.mojang.realmsclient.util.RealmsTasks;
import org.lwjgl.opengl.GL11;
import com.mojang.realmsclient.util.RealmsTextureManager;
import net.minecraft.realms.RealmsButton;
import net.minecraft.realms.Realms;
import org.lwjgl.input.Keyboard;
import com.mojang.realmsclient.exception.RealmsServiceException;
import com.mojang.realmsclient.client.RealmsClient;
import com.mojang.realmsclient.gui.RealmsConstants;
import com.mojang.realmsclient.dto.WorldTemplatePaginatedList;
import com.mojang.realmsclient.dto.RealmsServer;
import net.minecraft.realms.RealmsScreen;
import org.apache.logging.log4j.Logger;
import com.mojang.realmsclient.dto.WorldTemplate;

public class RealmsResetWorldScreen extends RealmsScreenWithCallback<WorldTemplate>
{
    private static final Logger LOGGER;
    private final RealmsScreen lastScreen;
    private final RealmsServer serverData;
    private final RealmsScreen returnScreen;
    private String title;
    private String subtitle;
    private String buttonTitle;
    private int subtitleColor;
    private static final String SLOT_FRAME_LOCATION = "realms:textures/gui/realms/slot_frame.png";
    private static final String UPLOAD_LOCATION = "realms:textures/gui/realms/upload.png";
    private static final String ADVENTURE_MAP_LOCATION = "realms:textures/gui/realms/adventure.png";
    private static final String SURVIVAL_SPAWN_LOCATION = "realms:textures/gui/realms/survival_spawn.png";
    private static final String NEW_WORLD_LOCATION = "realms:textures/gui/realms/new_world.png";
    private static final String EXPERIENCE_LOCATION = "realms:textures/gui/realms/experience.png";
    private static final String INSPIRATION_LOCATION = "realms:textures/gui/realms/inspiration.png";
    private final int BUTTON_CANCEL_ID = 0;
    private final WorldTemplatePaginatedList templates;
    private final WorldTemplatePaginatedList adventuremaps;
    private final WorldTemplatePaginatedList experiences;
    private final WorldTemplatePaginatedList inspirations;
    private ResetType selectedType;
    public int slot;
    private ResetType typeToReset;
    private ResetWorldInfo worldInfoToReset;
    private WorldTemplate worldTemplateToReset;
    private String resetTitle;
    private int confirmationId;
    
    public RealmsResetWorldScreen(final RealmsScreen lastScreen, final RealmsServer serverData, final RealmsScreen returnScreen) {
        this.title = RealmsScreen.getLocalizedString("mco.reset.world.title");
        this.subtitle = RealmsScreen.getLocalizedString("mco.reset.world.warning");
        this.buttonTitle = RealmsScreen.getLocalizedString("gui.cancel");
        this.subtitleColor = 16711680;
        this.templates = new WorldTemplatePaginatedList();
        this.adventuremaps = new WorldTemplatePaginatedList();
        this.experiences = new WorldTemplatePaginatedList();
        this.inspirations = new WorldTemplatePaginatedList();
        this.selectedType = ResetType.NONE;
        this.slot = -1;
        this.typeToReset = ResetType.NONE;
        this.worldInfoToReset = null;
        this.worldTemplateToReset = null;
        this.resetTitle = null;
        this.confirmationId = -1;
        this.lastScreen = lastScreen;
        this.serverData = serverData;
        this.returnScreen = returnScreen;
    }
    
    public RealmsResetWorldScreen(final RealmsScreen lastScreen, final RealmsServer serverData, final RealmsScreen returnScreen, final String title, final String subtitle, final int subtitleColor, final String buttonTitle) {
        this(lastScreen, serverData, returnScreen);
        this.title = title;
        this.subtitle = subtitle;
        this.subtitleColor = subtitleColor;
        this.buttonTitle = buttonTitle;
    }
    
    public void setConfirmationId(final int confirmationId) {
        this.confirmationId = confirmationId;
    }
    
    public void setSlot(final int slot) {
        this.slot = slot;
    }
    
    public void setResetTitle(final String title) {
        this.resetTitle = title;
    }
    
    @Override
    public void init() {
        this.buttonsClear();
        this.buttonsAdd(RealmsScreen.newButton(0, this.width() / 2 - 40, RealmsConstants.row(14) - 10, 80, 20, this.buttonTitle));
        new Thread("Realms-reset-world-fetcher") {
            @Override
            public void run() {
                final RealmsClient client = RealmsClient.createRealmsClient();
                try {
                    RealmsResetWorldScreen.this.templates.set(client.fetchWorldTemplates(1, 10, RealmsServer.WorldType.NORMAL));
                    RealmsResetWorldScreen.this.adventuremaps.set(client.fetchWorldTemplates(1, 10, RealmsServer.WorldType.ADVENTUREMAP));
                    RealmsResetWorldScreen.this.experiences.set(client.fetchWorldTemplates(1, 10, RealmsServer.WorldType.EXPERIENCE));
                    RealmsResetWorldScreen.this.inspirations.set(client.fetchWorldTemplates(1, 10, RealmsServer.WorldType.INSPIRATION));
                }
                catch (final RealmsServiceException e) {
                    RealmsResetWorldScreen.LOGGER.error("Couldn't fetch templates in reset world", e);
                }
            }
        }.start();
    }
    
    @Override
    public void removed() {
        Keyboard.enableRepeatEvents(false);
    }
    
    @Override
    public void keyPressed(final char ch, final int eventKey) {
        if (eventKey == 1) {
            Realms.setScreen(this.lastScreen);
        }
    }
    
    @Override
    public void buttonClicked(final RealmsButton button) {
        if (!button.active()) {
            return;
        }
        if (button.id() == 0) {
            Realms.setScreen(this.lastScreen);
        }
    }
    
    @Override
    public void mouseClicked(final int x, final int y, final int buttonNum) {
        switch (this.selectedType) {
            case NONE: {
                break;
            }
            case GENERATE: {
                Realms.setScreen(new RealmsResetNormalWorldScreen(this, this.title));
                break;
            }
            case UPLOAD: {
                Realms.setScreen(new RealmsSelectFileToUploadScreen(this.serverData.id, (this.slot != -1) ? this.slot : this.serverData.activeSlot, this));
                break;
            }
            case ADVENTURE: {
                final RealmsSelectWorldTemplateScreen screen = new RealmsSelectWorldTemplateScreen(this, null, RealmsServer.WorldType.ADVENTUREMAP, new WorldTemplatePaginatedList(this.adventuremaps));
                screen.setTitle(RealmsScreen.getLocalizedString("mco.reset.world.adventure"));
                Realms.setScreen(screen);
                break;
            }
            case SURVIVAL_SPAWN: {
                final RealmsSelectWorldTemplateScreen templateScreen = new RealmsSelectWorldTemplateScreen(this, null, RealmsServer.WorldType.NORMAL, new WorldTemplatePaginatedList(this.templates));
                templateScreen.setTitle(RealmsScreen.getLocalizedString("mco.reset.world.template"));
                Realms.setScreen(templateScreen);
                break;
            }
            case EXPERIENCE: {
                final RealmsSelectWorldTemplateScreen experienceScreen = new RealmsSelectWorldTemplateScreen(this, null, RealmsServer.WorldType.EXPERIENCE, new WorldTemplatePaginatedList(this.experiences));
                experienceScreen.setTitle(RealmsScreen.getLocalizedString("mco.reset.world.experience"));
                Realms.setScreen(experienceScreen);
                break;
            }
            case INSPIRATION: {
                final RealmsSelectWorldTemplateScreen inspirationScreen = new RealmsSelectWorldTemplateScreen(this, null, RealmsServer.WorldType.INSPIRATION, new WorldTemplatePaginatedList(this.inspirations));
                inspirationScreen.setTitle(RealmsScreen.getLocalizedString("mco.reset.world.inspiration"));
                Realms.setScreen(inspirationScreen);
                break;
            }
            default: {}
        }
    }
    
    private int frame(final int i) {
        return this.width() / 2 - 130 + (i - 1) * 100;
    }
    
    @Override
    public void render(final int xm, final int ym, final float a) {
        this.selectedType = ResetType.NONE;
        this.renderBackground();
        this.drawCenteredString(this.title, this.width() / 2, 7, 16777215);
        this.drawCenteredString(this.subtitle, this.width() / 2, 22, this.subtitleColor);
        this.drawFrame(this.frame(1), RealmsConstants.row(0) + 10, xm, ym, RealmsScreen.getLocalizedString("mco.reset.world.generate"), -1L, "realms:textures/gui/realms/new_world.png", ResetType.GENERATE);
        this.drawFrame(this.frame(2), RealmsConstants.row(0) + 10, xm, ym, RealmsScreen.getLocalizedString("mco.reset.world.upload"), -1L, "realms:textures/gui/realms/upload.png", ResetType.UPLOAD);
        this.drawFrame(this.frame(3), RealmsConstants.row(0) + 10, xm, ym, RealmsScreen.getLocalizedString("mco.reset.world.template"), -1L, "realms:textures/gui/realms/survival_spawn.png", ResetType.SURVIVAL_SPAWN);
        this.drawFrame(this.frame(1), RealmsConstants.row(6) + 20, xm, ym, RealmsScreen.getLocalizedString("mco.reset.world.adventure"), -1L, "realms:textures/gui/realms/adventure.png", ResetType.ADVENTURE);
        this.drawFrame(this.frame(2), RealmsConstants.row(6) + 20, xm, ym, RealmsScreen.getLocalizedString("mco.reset.world.experience"), -1L, "realms:textures/gui/realms/experience.png", ResetType.EXPERIENCE);
        this.drawFrame(this.frame(3), RealmsConstants.row(6) + 20, xm, ym, RealmsScreen.getLocalizedString("mco.reset.world.inspiration"), -1L, "realms:textures/gui/realms/inspiration.png", ResetType.INSPIRATION);
        super.render(xm, ym, a);
    }
    
    private void drawFrame(final int x, final int y, final int xm, final int ym, final String text, final long imageId, final String image, final ResetType resetType) {
        boolean hovered = false;
        if (xm >= x && xm <= x + 60 && ym >= y - 12 && ym <= y + 60) {
            hovered = true;
            this.selectedType = resetType;
        }
        if (imageId == -1L) {
            RealmsScreen.bind(image);
        }
        else {
            RealmsTextureManager.bindWorldTemplate(String.valueOf(imageId), image);
        }
        if (hovered) {
            GL11.glColor4f(0.56f, 0.56f, 0.56f, 1.0f);
        }
        else {
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        }
        RealmsScreen.blit(x + 2, y + 2, 0.0f, 0.0f, 56, 56, 56.0f, 56.0f);
        RealmsScreen.bind("realms:textures/gui/realms/slot_frame.png");
        if (hovered) {
            GL11.glColor4f(0.56f, 0.56f, 0.56f, 1.0f);
        }
        else {
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        }
        RealmsScreen.blit(x, y, 0.0f, 0.0f, 60, 60, 60.0f, 60.0f);
        this.drawCenteredString(text, x + 30, y - 12, hovered ? 10526880 : 16777215);
    }
    
    @Override
    void callback(final WorldTemplate worldTemplate) {
        if (worldTemplate != null) {
            if (this.slot == -1) {
                this.resetWorldWithTemplate(worldTemplate);
            }
            else {
                switch (worldTemplate.type) {
                    case WORLD_TEMPLATE: {
                        this.typeToReset = ResetType.SURVIVAL_SPAWN;
                        break;
                    }
                    case ADVENTUREMAP: {
                        this.typeToReset = ResetType.ADVENTURE;
                        break;
                    }
                    case EXPERIENCE: {
                        this.typeToReset = ResetType.EXPERIENCE;
                        break;
                    }
                    case INSPIRATION: {
                        this.typeToReset = ResetType.INSPIRATION;
                        break;
                    }
                }
                this.worldTemplateToReset = worldTemplate;
                this.switchSlot();
            }
        }
    }
    
    private void switchSlot() {
        this.switchSlot(this);
    }
    
    public void switchSlot(final RealmsScreen screen) {
        final RealmsTasks.SwitchSlotTask switchSlotTask = new RealmsTasks.SwitchSlotTask(this.serverData.id, this.slot, screen, 100);
        final RealmsLongRunningMcoTaskScreen longRunningMcoTaskScreen = new RealmsLongRunningMcoTaskScreen(this.lastScreen, switchSlotTask);
        longRunningMcoTaskScreen.start();
        Realms.setScreen(longRunningMcoTaskScreen);
    }
    
    @Override
    public void confirmResult(final boolean result, final int id) {
        if (id == 100 && result) {
            switch (this.typeToReset) {
                case ADVENTURE:
                case SURVIVAL_SPAWN:
                case EXPERIENCE:
                case INSPIRATION: {
                    if (this.worldTemplateToReset != null) {
                        this.resetWorldWithTemplate(this.worldTemplateToReset);
                        break;
                    }
                    break;
                }
                case GENERATE: {
                    if (this.worldInfoToReset != null) {
                        this.triggerResetWorld(this.worldInfoToReset);
                        break;
                    }
                    break;
                }
                default: {}
            }
            return;
        }
        if (result) {
            Realms.setScreen(this.returnScreen);
            if (this.confirmationId != -1) {
                this.returnScreen.confirmResult(true, this.confirmationId);
            }
        }
    }
    
    public void resetWorldWithTemplate(final WorldTemplate template) {
        final RealmsTasks.ResettingWorldTask resettingWorldTask = new RealmsTasks.ResettingWorldTask(this.serverData.id, this.returnScreen, template);
        if (this.resetTitle != null) {
            resettingWorldTask.setResetTitle(this.resetTitle);
        }
        if (this.confirmationId != -1) {
            resettingWorldTask.setConfirmationId(this.confirmationId);
        }
        final RealmsLongRunningMcoTaskScreen longRunningMcoTaskScreen = new RealmsLongRunningMcoTaskScreen(this.lastScreen, resettingWorldTask);
        longRunningMcoTaskScreen.start();
        Realms.setScreen(longRunningMcoTaskScreen);
    }
    
    public void resetWorld(final ResetWorldInfo resetWorldInfo) {
        if (this.slot == -1) {
            this.triggerResetWorld(resetWorldInfo);
        }
        else {
            this.typeToReset = ResetType.GENERATE;
            this.worldInfoToReset = resetWorldInfo;
            this.switchSlot();
        }
    }
    
    private void triggerResetWorld(final ResetWorldInfo resetWorldInfo) {
        final RealmsTasks.ResettingWorldTask resettingWorldTask = new RealmsTasks.ResettingWorldTask(this.serverData.id, this.returnScreen, resetWorldInfo.seed, resetWorldInfo.levelType, resetWorldInfo.generateStructures);
        if (this.resetTitle != null) {
            resettingWorldTask.setResetTitle(this.resetTitle);
        }
        if (this.confirmationId != -1) {
            resettingWorldTask.setConfirmationId(this.confirmationId);
        }
        final RealmsLongRunningMcoTaskScreen longRunningMcoTaskScreen = new RealmsLongRunningMcoTaskScreen(this.lastScreen, resettingWorldTask);
        longRunningMcoTaskScreen.start();
        Realms.setScreen(longRunningMcoTaskScreen);
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    enum ResetType
    {
        NONE, 
        GENERATE, 
        UPLOAD, 
        ADVENTURE, 
        SURVIVAL_SPAWN, 
        EXPERIENCE, 
        INSPIRATION;
    }
    
    public static class ResetWorldInfo
    {
        String seed;
        int levelType;
        boolean generateStructures;
        
        public ResetWorldInfo(final String seed, final int levelType, final boolean generateStructures) {
            this.seed = seed;
            this.levelType = levelType;
            this.generateStructures = generateStructures;
        }
    }
}
