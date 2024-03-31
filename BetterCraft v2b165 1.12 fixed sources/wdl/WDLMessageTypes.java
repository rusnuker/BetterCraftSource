// 
// Decompiled by Procyon v0.6.0
// 

package wdl;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;
import wdl.api.IWDLMessageType;

public enum WDLMessageTypes implements IWDLMessageType
{
    INFO("INFO", 0, "wdl.messages.message.info", TextFormatting.RED, TextFormatting.GOLD, true, MessageTypeCategory.CORE_RECOMMENDED), 
    ERROR("ERROR", 1, "wdl.messages.message.error", TextFormatting.DARK_GREEN, TextFormatting.DARK_RED, true, MessageTypeCategory.CORE_RECOMMENDED), 
    UPDATES("UPDATES", 2, "wdl.messages.message.updates", TextFormatting.RED, TextFormatting.GOLD, true, MessageTypeCategory.CORE_RECOMMENDED), 
    LOAD_TILE_ENTITY("LOAD_TILE_ENTITY", 3, "wdl.messages.message.loadingTileEntity", false), 
    ON_WORLD_LOAD("ON_WORLD_LOAD", 4, "wdl.messages.message.onWorldLoad", false), 
    ON_BLOCK_EVENT("ON_BLOCK_EVENT", 5, "wdl.messages.message.blockEvent", true), 
    ON_MAP_SAVED("ON_MAP_SAVED", 6, "wdl.messages.message.mapDataSaved", false), 
    ON_CHUNK_NO_LONGER_NEEDED("ON_CHUNK_NO_LONGER_NEEDED", 7, "wdl.messages.message.chunkUnloaded", false), 
    ON_GUI_CLOSED_INFO("ON_GUI_CLOSED_INFO", 8, "wdl.messages.message.guiClosedInfo", true), 
    ON_GUI_CLOSED_WARNING("ON_GUI_CLOSED_WARNING", 9, "wdl.messages.message.guiClosedWarning", true), 
    SAVING("SAVING", 10, "wdl.messages.message.saving", true), 
    REMOVE_ENTITY("REMOVE_ENTITY", 11, "wdl.messages.message.removeEntity", false), 
    PLUGIN_CHANNEL_MESSAGE("PLUGIN_CHANNEL_MESSAGE", 12, "wdl.messages.message.pluginChannel", false), 
    UPDATE_DEBUG("UPDATE_DEBUG", 13, "wdl.messages.message.updateDebug", false);
    
    private final String displayTextKey;
    private final TextFormatting titleColor;
    private final TextFormatting textColor;
    private final String descriptionKey;
    private final boolean enabledByDefault;
    
    private WDLMessageTypes(final String s, final int n, final String i18nKey, final boolean enabledByDefault) {
        this(s, n, i18nKey, TextFormatting.DARK_GREEN, TextFormatting.GOLD, enabledByDefault, MessageTypeCategory.CORE_DEBUG);
    }
    
    private WDLMessageTypes(final String s, final int n, final String i18nKey, final TextFormatting titleColor, final TextFormatting textColor, final boolean enabledByDefault, final MessageTypeCategory category) {
        this.displayTextKey = String.valueOf(i18nKey) + ".text";
        this.titleColor = titleColor;
        this.textColor = textColor;
        this.descriptionKey = String.valueOf(i18nKey) + ".description";
        this.enabledByDefault = enabledByDefault;
        WDLMessages.registerMessage(this.name(), this, category);
    }
    
    @Override
    public String getDisplayName() {
        return I18n.format(this.displayTextKey, new Object[0]);
    }
    
    @Override
    public TextFormatting getTitleColor() {
        return this.titleColor;
    }
    
    @Override
    public TextFormatting getTextColor() {
        return this.textColor;
    }
    
    @Override
    public String getDescription() {
        return I18n.format(this.descriptionKey, new Object[0]);
    }
    
    @Override
    public boolean isEnabledByDefault() {
        return this.enabledByDefault;
    }
}
