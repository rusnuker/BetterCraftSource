// 
// Decompiled by Procyon v0.6.0
// 

package com.mojang.realmsclient.dto;

import net.minecraft.realms.RealmsScreen;
import com.mojang.realmsclient.util.JsonUtils;
import com.google.gson.JsonObject;

public class RealmsWorldOptions
{
    public Boolean pvp;
    public Boolean spawnAnimals;
    public Boolean spawnMonsters;
    public Boolean spawnNPCs;
    public Integer spawnProtection;
    public Boolean commandBlocks;
    public Boolean forceGameMode;
    public Integer difficulty;
    public Integer gameMode;
    public String slotName;
    public long templateId;
    public String templateImage;
    public boolean adventureMap;
    public boolean empty;
    private static final boolean forceGameModeDefault = false;
    private static final boolean pvpDefault = true;
    private static final boolean spawnAnimalsDefault = true;
    private static final boolean spawnMonstersDefault = true;
    private static final boolean spawnNPCsDefault = true;
    private static final int spawnProtectionDefault = 0;
    private static final boolean commandBlocksDefault = false;
    private static final int difficultyDefault = 2;
    private static final int gameModeDefault = 0;
    private static final String slotNameDefault = "";
    private static final long templateIdDefault = -1L;
    private static final String templateImageDefault;
    private static final boolean adventureMapDefault = false;
    
    public RealmsWorldOptions(final Boolean pvp, final Boolean spawnAnimals, final Boolean spawnMonsters, final Boolean spawnNPCs, final Integer spawnProtection, final Boolean commandBlocks, final Integer difficulty, final Integer gameMode, final Boolean forceGameMode, final String slotName) {
        this.pvp = pvp;
        this.spawnAnimals = spawnAnimals;
        this.spawnMonsters = spawnMonsters;
        this.spawnNPCs = spawnNPCs;
        this.spawnProtection = spawnProtection;
        this.commandBlocks = commandBlocks;
        this.difficulty = difficulty;
        this.gameMode = gameMode;
        this.forceGameMode = forceGameMode;
        this.slotName = slotName;
    }
    
    public static RealmsWorldOptions getDefaults() {
        return new RealmsWorldOptions(true, true, true, true, 0, false, 2, 0, false, "");
    }
    
    public static RealmsWorldOptions getEmptyDefaults() {
        final RealmsWorldOptions options = new RealmsWorldOptions(true, true, true, true, 0, false, 2, 0, false, "");
        options.setEmpty(true);
        return options;
    }
    
    public void setEmpty(final boolean empty) {
        this.empty = empty;
    }
    
    public static RealmsWorldOptions parse(final JsonObject jsonObject) {
        final RealmsWorldOptions newOptions = new RealmsWorldOptions(JsonUtils.getBooleanOr("pvp", jsonObject, true), JsonUtils.getBooleanOr("spawnAnimals", jsonObject, true), JsonUtils.getBooleanOr("spawnMonsters", jsonObject, true), JsonUtils.getBooleanOr("spawnNPCs", jsonObject, true), JsonUtils.getIntOr("spawnProtection", jsonObject, 0), JsonUtils.getBooleanOr("commandBlocks", jsonObject, false), JsonUtils.getIntOr("difficulty", jsonObject, 2), JsonUtils.getIntOr("gameMode", jsonObject, 0), JsonUtils.getBooleanOr("forceGameMode", jsonObject, false), JsonUtils.getStringOr("slotName", jsonObject, ""));
        newOptions.templateId = JsonUtils.getLongOr("worldTemplateId", jsonObject, -1L);
        newOptions.templateImage = JsonUtils.getStringOr("worldTemplateImage", jsonObject, RealmsWorldOptions.templateImageDefault);
        newOptions.adventureMap = JsonUtils.getBooleanOr("adventureMap", jsonObject, false);
        return newOptions;
    }
    
    public String getSlotName(final int i) {
        if (this.slotName != null && !this.slotName.isEmpty()) {
            return this.slotName;
        }
        if (this.empty) {
            return RealmsScreen.getLocalizedString("mco.configure.world.slot.empty");
        }
        return this.getDefaultSlotName(i);
    }
    
    public String getDefaultSlotName(final int i) {
        return RealmsScreen.getLocalizedString("mco.configure.world.slot", i);
    }
    
    public String toJson() {
        final JsonObject jsonObject = new JsonObject();
        if (!this.pvp) {
            jsonObject.addProperty("pvp", this.pvp);
        }
        if (!this.spawnAnimals) {
            jsonObject.addProperty("spawnAnimals", this.spawnAnimals);
        }
        if (!this.spawnMonsters) {
            jsonObject.addProperty("spawnMonsters", this.spawnMonsters);
        }
        if (!this.spawnNPCs) {
            jsonObject.addProperty("spawnNPCs", this.spawnNPCs);
        }
        if (this.spawnProtection != 0) {
            jsonObject.addProperty("spawnProtection", this.spawnProtection);
        }
        if (this.commandBlocks) {
            jsonObject.addProperty("commandBlocks", this.commandBlocks);
        }
        if (this.difficulty != 2) {
            jsonObject.addProperty("difficulty", this.difficulty);
        }
        if (this.gameMode != 0) {
            jsonObject.addProperty("gameMode", this.gameMode);
        }
        if (this.forceGameMode) {
            jsonObject.addProperty("forceGameMode", this.forceGameMode);
        }
        if (this.slotName != null && !this.slotName.equals("")) {
            jsonObject.addProperty("slotName", this.slotName);
        }
        return jsonObject.toString();
    }
    
    public RealmsWorldOptions clone() {
        return new RealmsWorldOptions(this.pvp, this.spawnAnimals, this.spawnMonsters, this.spawnNPCs, this.spawnProtection, this.commandBlocks, this.difficulty, this.gameMode, this.forceGameMode, this.slotName);
    }
    
    static {
        templateImageDefault = null;
    }
}
