// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.cosmetics;

import java.io.IOException;
import org.lwjgl.input.Keyboard;
import me.amkgre.bettercraft.client.utils.ClientSettingsUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiCosmetics extends GuiScreen
{
    public GuiScreen before;
    public static boolean bcCapeCosmetic1;
    public static boolean bcCapeCosmetic2;
    public static boolean dragonWingsCosmetic;
    public static boolean crystalWingsCosmetic;
    public static boolean batWingsCosmetic;
    public static boolean vexWingsCosmetic;
    public static boolean beeWingsCosmetic;
    public static boolean devilWingsCosmetic;
    public static boolean witherCosmetic;
    public static boolean creeperCosmetic;
    public static boolean enchantCosmetic;
    public static boolean blazeCosmetic;
    public static boolean enderCrystalCosmetic;
    public static boolean guardianSpikesCosmetic;
    public static boolean slimeGelCosmetic;
    public static boolean susannoCosmetic;
    public static boolean sixPathCosmetic;
    public static boolean galaxySkinCosmetic;
    public static boolean skinDerpCosmetic;
    public static boolean tophatCosmetic;
    public static boolean witchhatCosmetic;
    public static boolean headsetCosmetic;
    public static boolean crownKingCosmetic;
    public static boolean devilHornsCosmetic;
    public static boolean haloCosmetic;
    public static boolean capCosmetic;
    public static boolean snoxhEyesCosmetic;
    public static boolean villagerNoseCosmetic;
    public static boolean nerdGlassesCosmetic;
    public static boolean bandanaCosmetic;
    public static boolean capeCosmetic;
    private GuiButton button;
    
    static {
        GuiCosmetics.bcCapeCosmetic1 = ClientSettingsUtils.bcCapeCosmetic1;
        GuiCosmetics.bcCapeCosmetic2 = ClientSettingsUtils.bcCapeCosmetic2;
        GuiCosmetics.dragonWingsCosmetic = ClientSettingsUtils.dragonWingsCosmetic;
        GuiCosmetics.crystalWingsCosmetic = ClientSettingsUtils.crystalWingsCosmetic;
        GuiCosmetics.batWingsCosmetic = ClientSettingsUtils.batWingsCosmetic;
        GuiCosmetics.vexWingsCosmetic = ClientSettingsUtils.vexWingsCosmetic;
        GuiCosmetics.beeWingsCosmetic = ClientSettingsUtils.beeWingsCosmetic;
        GuiCosmetics.devilWingsCosmetic = ClientSettingsUtils.devilWingsCosmetic;
        GuiCosmetics.witherCosmetic = ClientSettingsUtils.witherCosmetic;
        GuiCosmetics.creeperCosmetic = ClientSettingsUtils.creeperCosmetic;
        GuiCosmetics.enchantCosmetic = ClientSettingsUtils.enchantCosmetic;
        GuiCosmetics.blazeCosmetic = ClientSettingsUtils.blazeCosmetic;
        GuiCosmetics.enderCrystalCosmetic = ClientSettingsUtils.enderCrystalCosmetic;
        GuiCosmetics.guardianSpikesCosmetic = ClientSettingsUtils.guardianSpikesCosmetic;
        GuiCosmetics.slimeGelCosmetic = ClientSettingsUtils.slimeGelCosmetic;
        GuiCosmetics.susannoCosmetic = ClientSettingsUtils.susannoCosmetic;
        GuiCosmetics.sixPathCosmetic = ClientSettingsUtils.sixPathCosmetic;
        GuiCosmetics.galaxySkinCosmetic = ClientSettingsUtils.galaxySkinCosmetic;
        GuiCosmetics.skinDerpCosmetic = ClientSettingsUtils.skinDerpCosmetic;
        GuiCosmetics.tophatCosmetic = ClientSettingsUtils.tophatCosmetic;
        GuiCosmetics.witchhatCosmetic = ClientSettingsUtils.witchhatCosmetic;
        GuiCosmetics.headsetCosmetic = ClientSettingsUtils.headsetCosmetic;
        GuiCosmetics.crownKingCosmetic = ClientSettingsUtils.crownKingCosmetic;
        GuiCosmetics.devilHornsCosmetic = ClientSettingsUtils.devilHornsCosmetic;
        GuiCosmetics.haloCosmetic = ClientSettingsUtils.haloCosmetic;
        GuiCosmetics.capCosmetic = ClientSettingsUtils.capCosmetic;
        GuiCosmetics.snoxhEyesCosmetic = ClientSettingsUtils.snoxhEyesCosmetic;
        GuiCosmetics.villagerNoseCosmetic = ClientSettingsUtils.villagerNoseCosmetic;
        GuiCosmetics.nerdGlassesCosmetic = ClientSettingsUtils.nerdGlassesCosmetic;
        GuiCosmetics.bandanaCosmetic = ClientSettingsUtils.bandanaCosmetic;
        GuiCosmetics.capeCosmetic = ClientSettingsUtils.capeCosmetic;
    }
    
    public GuiCosmetics(final GuiScreen screen) {
        this.before = screen;
    }
    
    @Override
    public void updateScreen() {
    }
    
    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, GuiCosmetics.width - 70, GuiCosmetics.height - 30, 60, 20, "Back"));
        this.buttonList.add(this.button = new GuiButton(13, GuiCosmetics.width / 2 - 30, 20, 60, 20, GuiCosmetics.snoxhEyesCosmetic ? "§aSnoxh" : "§cSnoxh"));
        this.buttonList.add(this.button = new GuiButton(21, GuiCosmetics.width / 2 - 30, 45, 60, 20, GuiCosmetics.villagerNoseCosmetic ? "§aVillager" : "§cVillager"));
        this.buttonList.add(this.button = new GuiButton(26, GuiCosmetics.width / 2 - 30, 70, 60, 20, GuiCosmetics.nerdGlassesCosmetic ? "§aGlasses" : "§cGlasses"));
        this.buttonList.add(this.button = new GuiButton(31, GuiCosmetics.width / 2 - 30, 95, 60, 20, GuiCosmetics.bandanaCosmetic ? "§aBandana" : "§cBandana"));
        this.buttonList.add(this.button = new GuiButton(10, GuiCosmetics.width - 144 + 74, 20, 60, 20, GuiCosmetics.dragonWingsCosmetic ? "§aDragon" : "§cDragon"));
        this.buttonList.add(this.button = new GuiButton(12, GuiCosmetics.width - 144 + 74, 45, 60, 20, GuiCosmetics.crystalWingsCosmetic ? "§aCrystal" : "§cCrystal"));
        this.buttonList.add(this.button = new GuiButton(15, GuiCosmetics.width - 144 + 10, 45, 60, 20, GuiCosmetics.batWingsCosmetic ? "§aBat" : "§cBat"));
        this.buttonList.add(this.button = new GuiButton(16, GuiCosmetics.width - 144 + 74, 70, 60, 20, GuiCosmetics.vexWingsCosmetic ? "§aVex" : "§cVex"));
        this.buttonList.add(this.button = new GuiButton(23, GuiCosmetics.width - 144 + 10, 70, 60, 20, GuiCosmetics.beeWingsCosmetic ? "§aBee" : "§cBee"));
        this.buttonList.add(this.button = new GuiButton(24, GuiCosmetics.width - 144 + 10, 20, 60, 20, GuiCosmetics.devilWingsCosmetic ? "§aDevil" : "§cDevil"));
        this.buttonList.add(this.button = new GuiButton(4, 10, GuiCosmetics.height - 30, 60, 20, GuiCosmetics.bcCapeCosmetic1 ? "§aCape 1" : "§cCape 1"));
        this.buttonList.add(this.button = new GuiButton(9, 75, GuiCosmetics.height - 30, 60, 20, GuiCosmetics.bcCapeCosmetic2 ? "§aCape 2" : "§cCape 2"));
        this.buttonList.add(this.button = new GuiButton(22, 140, GuiCosmetics.height - 30, 60, 20, GuiCosmetics.capeCosmetic ? "§aOwnCape" : "§cOwn Cape"));
        this.buttonList.add(this.button = new GuiButton(6, GuiCosmetics.width - 144 + 10, 110, 60, 20, GuiCosmetics.tophatCosmetic ? "§aTophat" : "§cTophat"));
        this.buttonList.add(this.button = new GuiButton(7, GuiCosmetics.width - 144 + 74, 110, 60, 20, GuiCosmetics.witchhatCosmetic ? "§aWitchhat" : "§cWitchhat"));
        this.buttonList.add(this.button = new GuiButton(11, GuiCosmetics.width - 144 + 74, 135, 60, 20, GuiCosmetics.headsetCosmetic ? "§aHeadset" : "§cHeadset"));
        this.buttonList.add(this.button = new GuiButton(27, GuiCosmetics.width - 144 + 10, 135, 60, 20, GuiCosmetics.crownKingCosmetic ? "§aCrownking" : "§cCrownking"));
        this.buttonList.add(this.button = new GuiButton(28, GuiCosmetics.width - 144 + 74, 160, 60, 20, GuiCosmetics.devilHornsCosmetic ? "§aHorns" : "§cHorns"));
        this.buttonList.add(this.button = new GuiButton(29, GuiCosmetics.width - 144 + 10, 160, 60, 20, GuiCosmetics.haloCosmetic ? "§aHalo" : "§cHalo"));
        this.buttonList.add(this.button = new GuiButton(32, GuiCosmetics.width - 144 + 10, 185, 60, 20, GuiCosmetics.capCosmetic ? "§aCapy" : "§cCapy"));
        this.buttonList.add(this.button = new GuiButton(1, 10, 20, 60, 20, GuiCosmetics.witherCosmetic ? "§aWither" : "§cWither"));
        this.buttonList.add(this.button = new GuiButton(2, 10, 45, 60, 20, GuiCosmetics.blazeCosmetic ? "§aBlaze" : "§cBlaze"));
        this.buttonList.add(this.button = new GuiButton(5, 74, 45, 60, 20, GuiCosmetics.creeperCosmetic ? "§aCreeper" : "§cCreeper"));
        this.buttonList.add(this.button = new GuiButton(8, 74, 20, 60, 20, GuiCosmetics.enchantCosmetic ? "§aEnchant" : "§cEnchant"));
        this.buttonList.add(this.button = new GuiButton(17, 10, 70, 60, 20, GuiCosmetics.enderCrystalCosmetic ? "§aCrystal" : "§cCrystal"));
        this.buttonList.add(this.button = new GuiButton(18, 74, 70, 60, 20, GuiCosmetics.guardianSpikesCosmetic ? "§aSpikes" : "§cSpikes"));
        this.buttonList.add(this.button = new GuiButton(19, 10, 95, 60, 20, GuiCosmetics.slimeGelCosmetic ? "§aSlime" : "§cSlime"));
        this.buttonList.add(this.button = new GuiButton(20, 74, 95, 60, 20, GuiCosmetics.susannoCosmetic ? "§aSusanno" : "§cSusanno"));
        this.buttonList.add(this.button = new GuiButton(25, 10, 120, 60, 20, GuiCosmetics.sixPathCosmetic ? "§aSixpath" : "§cSixpath"));
        this.buttonList.add(this.button = new GuiButton(30, 74, 120, 60, 20, GuiCosmetics.galaxySkinCosmetic ? "§aGalaxy" : "§cGalaxy"));
        this.buttonList.add(this.button = new GuiButton(14, 10, 145, 60, 20, GuiCosmetics.skinDerpCosmetic ? "§aDerp" : "§cDerp"));
    }
    
    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        final int id = button.id;
        if (id == 0) {
            this.mc.displayGuiScreen(this.before);
        }
        if (id == 1) {
            if (!GuiCosmetics.witherCosmetic) {
                button.displayString = "§aWither";
                GuiCosmetics.witherCosmetic = (ClientSettingsUtils.witherCosmetic = true);
            }
            else {
                button.displayString = "§cWither";
                GuiCosmetics.witherCosmetic = (ClientSettingsUtils.witherCosmetic = false);
            }
        }
        if (id == 2) {
            if (!GuiCosmetics.blazeCosmetic) {
                button.displayString = "§aBlaze";
                GuiCosmetics.blazeCosmetic = (ClientSettingsUtils.blazeCosmetic = true);
            }
            else {
                button.displayString = "§cBlaze";
                GuiCosmetics.blazeCosmetic = (ClientSettingsUtils.blazeCosmetic = false);
            }
        }
        if (id == 4) {
            if (!GuiCosmetics.bcCapeCosmetic1) {
                button.displayString = "§aCape 1";
                GuiCosmetics.bcCapeCosmetic1 = (ClientSettingsUtils.bcCapeCosmetic1 = true);
            }
            else {
                button.displayString = "§cCape 2";
                GuiCosmetics.bcCapeCosmetic1 = (ClientSettingsUtils.bcCapeCosmetic1 = false);
            }
        }
        if (id == 5) {
            if (!GuiCosmetics.creeperCosmetic) {
                button.displayString = "§aCreeper";
                GuiCosmetics.creeperCosmetic = (ClientSettingsUtils.creeperCosmetic = true);
            }
            else {
                button.displayString = "§cCreeper";
                GuiCosmetics.creeperCosmetic = (ClientSettingsUtils.creeperCosmetic = false);
            }
        }
        if (id == 6) {
            if (!GuiCosmetics.tophatCosmetic) {
                button.displayString = "§aTophat";
                GuiCosmetics.tophatCosmetic = (ClientSettingsUtils.tophatCosmetic = true);
            }
            else {
                button.displayString = "§cTophat";
                GuiCosmetics.tophatCosmetic = (ClientSettingsUtils.tophatCosmetic = false);
            }
        }
        if (id == 7) {
            if (!GuiCosmetics.witchhatCosmetic) {
                button.displayString = "§aWitchhat";
                GuiCosmetics.witchhatCosmetic = (ClientSettingsUtils.witchhatCosmetic = true);
            }
            else {
                button.displayString = "§cWitchhat";
                GuiCosmetics.witchhatCosmetic = (ClientSettingsUtils.witchhatCosmetic = false);
            }
        }
        if (id == 8) {
            if (!GuiCosmetics.enchantCosmetic) {
                button.displayString = "§aEnchant";
                GuiCosmetics.enchantCosmetic = (ClientSettingsUtils.enchantCosmetic = true);
            }
            else {
                button.displayString = "§cEnchant";
                GuiCosmetics.enchantCosmetic = (ClientSettingsUtils.enchantCosmetic = false);
            }
        }
        if (id == 9) {
            if (!GuiCosmetics.bcCapeCosmetic2) {
                button.displayString = "§aCape 1";
                GuiCosmetics.bcCapeCosmetic2 = (ClientSettingsUtils.bcCapeCosmetic2 = true);
            }
            else {
                button.displayString = "§cCape 2";
                GuiCosmetics.bcCapeCosmetic2 = (ClientSettingsUtils.bcCapeCosmetic2 = false);
            }
        }
        if (id == 10) {
            if (!GuiCosmetics.dragonWingsCosmetic) {
                button.displayString = "§aDragon";
                GuiCosmetics.dragonWingsCosmetic = (ClientSettingsUtils.dragonWingsCosmetic = true);
            }
            else {
                button.displayString = "§cDragon";
                GuiCosmetics.dragonWingsCosmetic = (ClientSettingsUtils.dragonWingsCosmetic = false);
            }
        }
        if (id == 11) {
            if (!GuiCosmetics.headsetCosmetic) {
                button.displayString = "§aHeadset";
                GuiCosmetics.headsetCosmetic = (ClientSettingsUtils.headsetCosmetic = true);
            }
            else {
                button.displayString = "§cHeadset";
                GuiCosmetics.headsetCosmetic = (ClientSettingsUtils.headsetCosmetic = false);
            }
        }
        if (id == 12) {
            if (!GuiCosmetics.crystalWingsCosmetic) {
                button.displayString = "§aCrystal";
                GuiCosmetics.crystalWingsCosmetic = (ClientSettingsUtils.crystalWingsCosmetic = true);
            }
            else {
                button.displayString = "§cCrystal";
                GuiCosmetics.crystalWingsCosmetic = (ClientSettingsUtils.crystalWingsCosmetic = false);
            }
        }
        if (id == 13) {
            if (!GuiCosmetics.snoxhEyesCosmetic) {
                button.displayString = "§aSnoxh";
                GuiCosmetics.snoxhEyesCosmetic = (ClientSettingsUtils.snoxhEyesCosmetic = true);
            }
            else {
                button.displayString = "§cSnoxh";
                GuiCosmetics.snoxhEyesCosmetic = (ClientSettingsUtils.snoxhEyesCosmetic = false);
            }
        }
        if (id == 14) {
            if (!GuiCosmetics.skinDerpCosmetic) {
                button.displayString = "§aDerp";
                GuiCosmetics.skinDerpCosmetic = (ClientSettingsUtils.skinDerpCosmetic = true);
            }
            else {
                button.displayString = "§cDerp";
                GuiCosmetics.skinDerpCosmetic = (ClientSettingsUtils.skinDerpCosmetic = false);
            }
        }
        if (id == 15) {
            if (!GuiCosmetics.batWingsCosmetic) {
                button.displayString = "§aBat";
                GuiCosmetics.batWingsCosmetic = (ClientSettingsUtils.batWingsCosmetic = true);
            }
            else {
                button.displayString = "§cBat";
                GuiCosmetics.batWingsCosmetic = (ClientSettingsUtils.batWingsCosmetic = false);
            }
        }
        if (id == 16) {
            if (!GuiCosmetics.vexWingsCosmetic) {
                button.displayString = "§aVex";
                GuiCosmetics.vexWingsCosmetic = (ClientSettingsUtils.vexWingsCosmetic = true);
            }
            else {
                button.displayString = "§cVex";
                GuiCosmetics.vexWingsCosmetic = (ClientSettingsUtils.vexWingsCosmetic = false);
            }
        }
        if (id == 17) {
            if (!GuiCosmetics.enderCrystalCosmetic) {
                button.displayString = "§aCrystal";
                GuiCosmetics.enderCrystalCosmetic = (ClientSettingsUtils.enderCrystalCosmetic = true);
            }
            else {
                button.displayString = "§cCrystal";
                GuiCosmetics.enderCrystalCosmetic = (ClientSettingsUtils.enderCrystalCosmetic = false);
            }
        }
        if (id == 18) {
            if (!GuiCosmetics.guardianSpikesCosmetic) {
                button.displayString = "§aSpikes";
                GuiCosmetics.guardianSpikesCosmetic = (ClientSettingsUtils.guardianSpikesCosmetic = true);
            }
            else {
                button.displayString = "§cSpikes";
                GuiCosmetics.guardianSpikesCosmetic = (ClientSettingsUtils.guardianSpikesCosmetic = false);
            }
        }
        if (id == 19) {
            if (!GuiCosmetics.slimeGelCosmetic) {
                button.displayString = "§aSlime";
                GuiCosmetics.slimeGelCosmetic = (ClientSettingsUtils.slimeGelCosmetic = true);
            }
            else {
                button.displayString = "§cSlime";
                GuiCosmetics.slimeGelCosmetic = (ClientSettingsUtils.slimeGelCosmetic = false);
            }
        }
        if (id == 20) {
            if (!GuiCosmetics.susannoCosmetic) {
                button.displayString = "§aSusanno";
                GuiCosmetics.susannoCosmetic = (ClientSettingsUtils.susannoCosmetic = true);
            }
            else {
                button.displayString = "§cSusanno";
                GuiCosmetics.susannoCosmetic = (ClientSettingsUtils.susannoCosmetic = false);
            }
        }
        if (id == 21) {
            if (!GuiCosmetics.villagerNoseCosmetic) {
                button.displayString = "§aVillager";
                GuiCosmetics.villagerNoseCosmetic = (ClientSettingsUtils.villagerNoseCosmetic = true);
            }
            else {
                button.displayString = "§cVillager";
                GuiCosmetics.villagerNoseCosmetic = (ClientSettingsUtils.villagerNoseCosmetic = false);
            }
        }
        if (id == 22) {
            if (!GuiCosmetics.capeCosmetic) {
                button.displayString = "§aOwn Cape";
                GuiCosmetics.capeCosmetic = (ClientSettingsUtils.capeCosmetic = true);
            }
            else {
                button.displayString = "§cOwn Cape";
                GuiCosmetics.capeCosmetic = (ClientSettingsUtils.capeCosmetic = false);
            }
        }
        if (id == 23) {
            if (!GuiCosmetics.beeWingsCosmetic) {
                button.displayString = "§aBee";
                GuiCosmetics.beeWingsCosmetic = (ClientSettingsUtils.beeWingsCosmetic = true);
            }
            else {
                button.displayString = "§cBee";
                GuiCosmetics.beeWingsCosmetic = (ClientSettingsUtils.beeWingsCosmetic = false);
            }
        }
        if (id == 24) {
            if (!GuiCosmetics.devilWingsCosmetic) {
                button.displayString = "§aDevil";
                GuiCosmetics.devilWingsCosmetic = (ClientSettingsUtils.devilWingsCosmetic = true);
            }
            else {
                button.displayString = "§cDevil";
                GuiCosmetics.devilWingsCosmetic = (ClientSettingsUtils.devilWingsCosmetic = false);
            }
        }
        if (id == 25) {
            if (!GuiCosmetics.sixPathCosmetic) {
                button.displayString = "§aSixpath";
                GuiCosmetics.sixPathCosmetic = (ClientSettingsUtils.sixPathCosmetic = true);
            }
            else {
                button.displayString = "§cSixpath";
                GuiCosmetics.sixPathCosmetic = (ClientSettingsUtils.sixPathCosmetic = false);
            }
        }
        if (id == 26) {
            if (!GuiCosmetics.nerdGlassesCosmetic) {
                button.displayString = "§aGlasses";
                GuiCosmetics.nerdGlassesCosmetic = (ClientSettingsUtils.nerdGlassesCosmetic = true);
            }
            else {
                button.displayString = "§cGlasses";
                GuiCosmetics.nerdGlassesCosmetic = (ClientSettingsUtils.nerdGlassesCosmetic = false);
            }
        }
        if (id == 27) {
            if (!GuiCosmetics.crownKingCosmetic) {
                button.displayString = "§aCrownking";
                GuiCosmetics.crownKingCosmetic = (ClientSettingsUtils.crownKingCosmetic = true);
            }
            else {
                button.displayString = "§cCrownking";
                GuiCosmetics.crownKingCosmetic = (ClientSettingsUtils.crownKingCosmetic = false);
            }
        }
        if (id == 28) {
            if (!GuiCosmetics.devilHornsCosmetic) {
                button.displayString = "§aHorns";
                GuiCosmetics.devilHornsCosmetic = (ClientSettingsUtils.devilHornsCosmetic = true);
            }
            else {
                button.displayString = "§cHorns";
                GuiCosmetics.devilHornsCosmetic = (ClientSettingsUtils.devilHornsCosmetic = false);
            }
        }
        if (id == 29) {
            if (!GuiCosmetics.haloCosmetic) {
                button.displayString = "§aHalo";
                GuiCosmetics.haloCosmetic = (ClientSettingsUtils.haloCosmetic = true);
            }
            else {
                button.displayString = "§cHalo";
                GuiCosmetics.haloCosmetic = (ClientSettingsUtils.haloCosmetic = false);
            }
        }
        if (id == 30) {
            if (!GuiCosmetics.galaxySkinCosmetic) {
                button.displayString = "§aGalaxy";
                GuiCosmetics.galaxySkinCosmetic = (ClientSettingsUtils.galaxySkinCosmetic = true);
            }
            else {
                button.displayString = "§cGalaxy";
                GuiCosmetics.galaxySkinCosmetic = (ClientSettingsUtils.galaxySkinCosmetic = false);
            }
        }
        if (id == 31) {
            if (!GuiCosmetics.bandanaCosmetic) {
                button.displayString = "§aBandana";
                GuiCosmetics.bandanaCosmetic = (ClientSettingsUtils.bandanaCosmetic = true);
            }
            else {
                button.displayString = "§cBandana";
                GuiCosmetics.bandanaCosmetic = (ClientSettingsUtils.bandanaCosmetic = false);
            }
        }
        if (id == 32) {
            if (!GuiCosmetics.capCosmetic) {
                button.displayString = "§aCapy";
                GuiCosmetics.capCosmetic = (ClientSettingsUtils.capCosmetic = true);
            }
            else {
                button.displayString = "§cCapy";
                GuiCosmetics.capCosmetic = (ClientSettingsUtils.capCosmetic = false);
            }
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        this.mc.fontRendererObj.drawString("§7Body", 57, 7, -1);
        this.mc.fontRendererObj.drawString("§7Wings", GuiCosmetics.width - 85, 7, -1);
        this.mc.fontRendererObj.drawString("§7Hats", GuiCosmetics.width - 82, 98, -1);
        this.mc.fontRendererObj.drawString("§7Capes", 90, GuiCosmetics.height - 42, -1);
        this.mc.fontRendererObj.drawString("§7Face", GuiCosmetics.width / 2 - 12, 7, -1);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
