// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.gui;

import me.nzxtercode.bettercraft.client.BetterCraft;
import java.io.IOException;
import me.nzxtercode.bettercraft.client.gui.GuiMusic;
import me.nzxtercode.bettercraft.client.misc.background.GuiBackground;
import net.labymod.addons.resourcepacks24.gui.GuiResourcepacks24;
import net.labymod.addons.resourcepacks24.Resourcepacks24;
import me.nzxtercode.bettercraft.client.gui.GuiStatus;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.client.audio.SoundEventAccessorComposite;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundCategory;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;

public class GuiOptions extends GuiScreen implements GuiYesNoCallback
{
    private static final GameSettings.Options[] field_146440_f;
    private final GuiScreen field_146441_g;
    private final GameSettings game_settings_1;
    private GuiButton field_175357_i;
    private GuiLockIconButton field_175356_r;
    protected String field_146442_a;
    
    static {
        field_146440_f = new GameSettings.Options[] { GameSettings.Options.FOV };
    }
    
    public GuiOptions(final GuiScreen p_i1046_1_, final GameSettings p_i1046_2_) {
        this.field_146442_a = "Options";
        this.field_146441_g = p_i1046_1_;
        this.game_settings_1 = p_i1046_2_;
    }
    
    @Override
    public void initGui() {
        int i = 0;
        this.field_146442_a = I18n.format("options.title", new Object[0]);
        GameSettings.Options[] field_146440_f;
        for (int length = (field_146440_f = GuiOptions.field_146440_f).length, j = 0; j < length; ++j) {
            final GameSettings.Options gamesettings$options = field_146440_f[j];
            if (gamesettings$options.getEnumFloat()) {
                this.buttonList.add(new GuiOptionSlider(gamesettings$options.returnEnumOrdinal(), GuiOptions.width / 2 - 155 + i % 2 * 160, GuiOptions.height / 6 - 12 + 24 * (i >> 1), gamesettings$options));
            }
            else {
                final GuiOptionButton guioptionbutton = new GuiOptionButton(gamesettings$options.returnEnumOrdinal(), GuiOptions.width / 2 - 155 + i % 2 * 160, GuiOptions.height / 6 - 12 + 24 * (i >> 1), gamesettings$options, this.game_settings_1.getKeyBinding(gamesettings$options));
                this.buttonList.add(guioptionbutton);
            }
            ++i;
        }
        if (this.mc.theWorld != null) {
            final EnumDifficulty enumdifficulty = this.mc.theWorld.getDifficulty();
            this.field_175357_i = new GuiButton(108, GuiOptions.width / 2 - 155 + i % 2 * 160, GuiOptions.height / 6 - 12 + 24 * (i >> 1), 150, 20, this.func_175355_a(enumdifficulty));
            this.buttonList.add(this.field_175357_i);
            if (this.mc.isSingleplayer() && !this.mc.theWorld.getWorldInfo().isHardcoreModeEnabled()) {
                this.field_175357_i.setWidth(this.field_175357_i.getButtonWidth() - 20);
                this.field_175356_r = new GuiLockIconButton(109, this.field_175357_i.xPosition + this.field_175357_i.getButtonWidth(), this.field_175357_i.yPosition);
                this.buttonList.add(this.field_175356_r);
                this.field_175356_r.func_175229_b(this.mc.theWorld.getWorldInfo().isDifficultyLocked());
                this.field_175356_r.enabled = !this.field_175356_r.func_175230_c();
                this.field_175357_i.enabled = !this.field_175356_r.func_175230_c();
            }
            else {
                this.field_175357_i.enabled = false;
            }
        }
        else {
            this.buttonList.add(new GuiButton(123, GuiOptions.width / 2 + 5, GuiOptions.height / 6 - 12, 150, 20, "Background"));
        }
        this.buttonList.add(new GuiButton(110, GuiOptions.width / 2 - 155, GuiOptions.height / 6 + 48 - 6, 150, 20, I18n.format("options.skinCustomisation", new Object[0])));
        this.buttonList.add(new GuiButton(8675309, GuiOptions.width / 2 + 5, GuiOptions.height / 6 + 72 - 6, 150, 20, "Super Secret Settings...") {
            @Override
            public void playPressSound(final SoundHandler soundHandlerIn) {
                final SoundEventAccessorComposite soundeventaccessorcomposite = soundHandlerIn.getRandomSoundFromCategories(SoundCategory.ANIMALS, SoundCategory.BLOCKS, SoundCategory.MOBS, SoundCategory.PLAYERS, SoundCategory.WEATHER);
                if (soundeventaccessorcomposite != null) {
                    soundHandlerIn.playSound(PositionedSoundRecord.create(soundeventaccessorcomposite.getSoundEventLocation(), 0.5f));
                }
            }
        });
        this.buttonList.add(new GuiButton(106, GuiOptions.width / 2 - 155, GuiOptions.height / 6 + 72 - 6, 150, 20, I18n.format("options.sounds", new Object[0])));
        this.buttonList.add(new GuiButton(107, GuiOptions.width / 2 + 5, GuiOptions.height / 6 + 48 - 6, 150, 20, "Music Stream"));
        this.buttonList.add(new GuiButton(101, GuiOptions.width / 2 - 155, GuiOptions.height / 6 + 96 - 6, 150, 20, I18n.format("options.video", new Object[0])));
        this.buttonList.add(new GuiButton(100, GuiOptions.width / 2 + 5, GuiOptions.height / 6 + 96 - 6, 150, 20, I18n.format("options.controls", new Object[0])));
        this.buttonList.add(new GuiButton(102, GuiOptions.width / 2 - 155, GuiOptions.height / 6 + 120 - 6, 150, 20, I18n.format("options.language", new Object[0])));
        this.buttonList.add(new GuiButton(103, GuiOptions.width / 2 + 5, GuiOptions.height / 6 + 120 - 6, 150, 20, I18n.format("options.chat.title", new Object[0])));
        this.buttonList.add(new GuiButton(105, GuiOptions.width / 2 - 155, GuiOptions.height / 6 + 144 - 6, 150, 20, I18n.format("options.resourcepack", new Object[0])));
        this.buttonList.add(new GuiButton(104, GuiOptions.width / 2 + 5, GuiOptions.height / 6 + 144 - 6, 150, 20, "Server Status"));
        this.buttonList.add(new GuiButton(200, GuiOptions.width / 2 - 100, GuiOptions.height / 6 + 168, I18n.format("gui.done", new Object[0])));
    }
    
    public String func_175355_a(final EnumDifficulty p_175355_1_) {
        final IChatComponent ichatcomponent = new ChatComponentText("");
        ichatcomponent.appendSibling(new ChatComponentTranslation("options.difficulty", new Object[0]));
        ichatcomponent.appendText(": ");
        ichatcomponent.appendSibling(new ChatComponentTranslation(p_175355_1_.getDifficultyResourceKey(), new Object[0]));
        return ichatcomponent.getFormattedText();
    }
    
    @Override
    public void confirmClicked(final boolean result, final int id) {
        this.mc.displayGuiScreen(this);
        if (id == 109 && result && this.mc.theWorld != null) {
            this.mc.theWorld.getWorldInfo().setDifficultyLocked(true);
            this.field_175356_r.func_175229_b(true);
            this.field_175356_r.enabled = false;
            this.field_175357_i.enabled = false;
        }
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.enabled) {
            if (button.id < 100 && button instanceof GuiOptionButton) {
                final GameSettings.Options gamesettings$options = ((GuiOptionButton)button).returnEnumOptions();
                this.game_settings_1.setOptionValue(gamesettings$options, 1);
                button.displayString = this.game_settings_1.getKeyBinding(GameSettings.Options.getEnumOptions(button.id));
            }
            if (button.id == 108) {
                this.mc.theWorld.getWorldInfo().setDifficulty(EnumDifficulty.getDifficultyEnum(this.mc.theWorld.getDifficulty().getDifficultyId() + 1));
                this.field_175357_i.displayString = this.func_175355_a(this.mc.theWorld.getDifficulty());
            }
            if (button.id == 109) {
                this.mc.displayGuiScreen(new GuiYesNo(this, new ChatComponentTranslation("difficulty.lock.title", new Object[0]).getFormattedText(), new ChatComponentTranslation("difficulty.lock.question", new Object[] { new ChatComponentTranslation(this.mc.theWorld.getWorldInfo().getDifficulty().getDifficultyResourceKey(), new Object[0]) }).getFormattedText(), 109));
            }
            if (button.id == 110) {
                this.mc.gameSettings.saveOptions();
                this.mc.displayGuiScreen(new GuiCustomizeSkin(this));
            }
            if (button.id == 8675309) {
                this.mc.entityRenderer.activateNextShader();
            }
            if (button.id == 101) {
                this.mc.gameSettings.saveOptions();
                this.mc.displayGuiScreen(new GuiVideoSettings(this, this.game_settings_1));
            }
            if (button.id == 100) {
                this.mc.gameSettings.saveOptions();
                this.mc.displayGuiScreen(new GuiControls(this, this.game_settings_1));
            }
            if (button.id == 102) {
                this.mc.gameSettings.saveOptions();
                this.mc.displayGuiScreen(new GuiLanguage(this, this.game_settings_1, this.mc.getLanguageManager()));
            }
            if (button.id == 103) {
                this.mc.gameSettings.saveOptions();
                this.mc.displayGuiScreen(new ScreenChatOptions(this, this.game_settings_1));
            }
            if (button.id == 104) {
                this.mc.gameSettings.saveOptions();
                this.mc.displayGuiScreen(new GuiStatus(this));
            }
            if (button.id == 200) {
                this.mc.gameSettings.saveOptions();
                this.mc.displayGuiScreen(this.field_146441_g);
            }
            if (button.id == 105) {
                this.mc.gameSettings.saveOptions();
                this.mc.displayGuiScreen(new GuiResourcepacks24(this, Resourcepacks24.getInstance()));
            }
            if (button.id == 106) {
                this.mc.gameSettings.saveOptions();
                this.mc.displayGuiScreen(new GuiScreenOptionsSounds(this, this.game_settings_1));
            }
            if (button.id == 123) {
                this.mc.displayGuiScreen(new GuiBackground(this));
            }
            if (button.id == 107) {
                this.mc.displayGuiScreen(new GuiMusic(this));
            }
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        Gui.drawCenteredString(this.fontRendererObj, this.field_146442_a, GuiOptions.width / 2, 15, 16777215);
        this.drawString(this.fontRendererObj, "https://discord.gg/h44RrPT4yP", 2, GuiOptions.height - 10, -1);
        this.drawString(this.fontRendererObj, "https://nzxter.de.cool/", 2, GuiOptions.height - 20, -1);
        final FontRenderer fontRendererObj = this.fontRendererObj;
        BetterCraft.getInstance();
        final String clientAuthor = BetterCraft.clientAuthor;
        final int width = GuiOptions.width;
        final FontRenderer fontRendererObj2 = this.fontRendererObj;
        BetterCraft.getInstance();
        this.drawString(fontRendererObj, clientAuthor, width - fontRendererObj2.getStringWidth(BetterCraft.clientAuthor) - 2, GuiOptions.height - 10, -1);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
