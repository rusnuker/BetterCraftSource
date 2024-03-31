// 
// Decompiled by Procyon v0.6.0
// 

package me.nzxtercode.bettercraft.client.mods;

import java.io.File;
import me.nzxtercode.bettercraft.client.Config;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonObject;
import java.awt.Color;
import me.nzxtercode.bettercraft.client.hud.ScreenPosition;
import me.nzxtercode.bettercraft.client.hud.IRender;

public abstract class ModRender extends Mod implements IRender
{
    protected ScreenPosition screenpos;
    private boolean isEnabled;
    private boolean showPrefix;
    private int r;
    private int g;
    private int b;
    private int alpha;
    private boolean isRGBEnabled;
    private Color color;
    private int rBackground;
    private int gBackground;
    private int bBackground;
    private int alphaBackground;
    private Color colorBackground;
    
    public ModRender() {
        this.setType(ModType.RENDER);
        this.screenpos = this.initPosition();
        this.isEnabled = this.isEnabled();
    }
    
    @Override
    public ScreenPosition load() {
        return this.screenpos;
    }
    
    @Override
    public void save(final ScreenPosition screenpos) {
        this.screenpos = screenpos;
        this.savePosition();
    }
    
    public ScreenPosition initPosition() {
        return this.hasPosition() ? this.getPosition() : ScreenPosition.fromAbsolute(0, 0);
    }
    
    private boolean hasPosition() {
        return this.getModJson().has("positions");
    }
    
    private ScreenPosition getPosition() {
        final JsonObject positions = this.getModJson().get("positions").getAsJsonObject();
        return new ScreenPosition(positions.get("x").getAsInt(), positions.get("y").getAsInt());
    }
    
    private void savePosition() {
        final JsonObject json = this.getJson();
        final String modName = this.getClass().getSimpleName();
        final JsonObject modJson = json.has(modName) ? json.get(modName).getAsJsonObject() : new JsonObject();
        final JsonObject positions = new JsonObject();
        positions.add("x", new JsonPrimitive(this.screenpos.getAbsoluteX()));
        positions.add("y", new JsonPrimitive(this.screenpos.getAbsoluteY()));
        modJson.add("positions", positions);
        Config.getInstance();
        Config.getInstance();
        Config.write(new File(Config.ROOT_DIR, "mods.json"), json);
    }
    
    private void saveEnabledState() {
        final JsonObject json = this.getJson();
        final String modName = this.getClass().getSimpleName();
        final JsonObject modJson = json.has(modName) ? json.get(modName).getAsJsonObject() : new JsonObject();
        modJson.add("enabled", new JsonPrimitive(this.isEnabled));
        Config.getInstance();
        Config.getInstance();
        Config.write(new File(Config.ROOT_DIR, "mods.json"), json);
    }
    
    public void setEnabledState(final boolean state) {
        this.isEnabled = state;
        this.saveEnabledState();
    }
    
    @Override
    public boolean isEnabled() {
        return !this.getModJson().has("enabled") || this.getModJson().get("enabled").getAsBoolean();
    }
    
    private final JsonObject getJson() {
        Config.getInstance();
        final File file = new File(Config.ROOT_DIR, "mods.json");
        if (!file.exists()) {
            try {
                file.createNewFile();
            }
            catch (final Throwable throwable) {
                throwable.printStackTrace();
            }
        }
        Config.getInstance();
        final JsonElement jsonElement = Config.read(file);
        return jsonElement.isJsonNull() ? new JsonObject() : jsonElement.getAsJsonObject();
    }
    
    private JsonObject getModJson() {
        final JsonObject json = this.getJson();
        final String modName = this.getClass().getSimpleName();
        final JsonObject modJson = json.has(modName) ? json.get(modName).getAsJsonObject() : new JsonObject();
        if (!json.has(modName)) {
            json.add(modName, modJson);
            Config.getInstance();
            Config.getInstance();
            Config.write(new File(Config.ROOT_DIR, "mods.json"), json);
        }
        return modJson;
    }
    
    public final int getLineOffset(final ScreenPosition screenpos, final int lineNumber) {
        return screenpos.getAbsoluteY() + this.getLineOffset(lineNumber);
    }
    
    private int getLineOffset(final int lineNumber) {
        return (this.font.FONT_HEIGHT + 3) * lineNumber;
    }
    
    public void setColor(final int r, final int g, final int b, final int alpha) {
        this.color = new Color(r, g, b, alpha);
    }
    
    public void setColor(final Color color) {
        this.color = color;
    }
    
    public void setColorBackground(final Color color) {
        this.colorBackground = color;
    }
    
    public void setColorBackground(final int r, final int g, final int b, final int alpha) {
        this.colorBackground = new Color(r, g, b, alpha);
    }
    
    public Color getColor() {
        return this.color;
    }
    
    public Color getBackgroundColor() {
        return this.colorBackground;
    }
    
    public boolean isRGBEnabled() {
        return this.isRGBEnabled;
    }
    
    public void setRGBEnabled(final boolean isRGB) {
        this.isRGBEnabled = isRGB;
    }
    
    public boolean ShowPrefix() {
        return this.showPrefix;
    }
    
    public void setShowPrefix(final boolean showPrefix) {
        this.showPrefix = showPrefix;
    }
}
