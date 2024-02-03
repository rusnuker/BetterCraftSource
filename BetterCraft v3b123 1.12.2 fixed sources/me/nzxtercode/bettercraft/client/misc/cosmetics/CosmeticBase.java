// 
// Decompiled by Procyon v0.6.0
// 

package me.nzxtercode.bettercraft.client.misc.cosmetics;

import net.minecraft.entity.EntityLivingBase;
import java.io.File;
import me.nzxtercode.bettercraft.client.Config;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonObject;
import java.util.List;
import net.minecraft.client.Minecraft;
import me.nzxtercode.bettercraft.client.misc.irc.IRC;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;

public abstract class CosmeticBase implements LayerRenderer<AbstractClientPlayer>
{
    private boolean isEnabled;
    protected final RenderPlayer playerRenderer;
    
    public CosmeticBase(final RenderPlayer playerRenderer) {
        this.playerRenderer = playerRenderer;
    }
    
    @Override
    public void doRenderLayer(final AbstractClientPlayer player, final float limbSwing, final float limbSwingAmount, final float partialTicks, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        if (player.hasPlayerInfo() && !player.isInvisible() && IRC.getInstance().isUserConnected(player.getGameProfile().getName())) {
            if (player == Minecraft.getMinecraft().thePlayer && this.isEnabled()) {
                this.render(player, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
                return;
            }
            if (CosmeticInstance.USER_COSMETICS.containsKey(player.getGameProfile().getName()) && CosmeticInstance.USER_COSMETICS.get(player.getGameProfile().getName()).stream().anyMatch(cos -> this.getId() == cos.getId())) {
                this.render(player, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
            }
        }
    }
    
    public abstract int getId();
    
    public void render(final AbstractClientPlayer player, final float limbSwing, final float limbSwingAmount, final float partialTicks, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
    }
    
    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
    
    private void saveEnabledState() {
        final JsonObject json = this.getJson();
        final String CosmeticName = this.getClass().getSimpleName();
        final JsonObject CosmeticJson = json.has(CosmeticName) ? json.get(CosmeticName).getAsJsonObject() : new JsonObject();
        CosmeticJson.add("enabled", new JsonPrimitive(this.isEnabled));
        Config.getInstance();
        Config.getInstance();
        Config.write(new File(Config.ROOT_DIR, "cosmetics.json"), json);
    }
    
    public void setEnabledState(final boolean state) {
        this.isEnabled = state;
        this.saveEnabledState();
    }
    
    public boolean isEnabled() {
        return !this.getCosmeticJson().has("enabled") || this.getCosmeticJson().get("enabled").getAsBoolean();
    }
    
    private final JsonObject getJson() {
        Config.getInstance();
        final File file = new File(Config.ROOT_DIR, "cosmetics.json");
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
    
    private JsonObject getCosmeticJson() {
        final JsonObject json = this.getJson();
        final String CosmeticName = this.getClass().getSimpleName();
        final JsonObject CosmeticJson = json.has(CosmeticName) ? json.get(CosmeticName).getAsJsonObject() : new JsonObject();
        if (!json.has(CosmeticName)) {
            json.add(CosmeticName, CosmeticJson);
            Config.getInstance();
            Config.getInstance();
            Config.write(new File(Config.ROOT_DIR, "cosmetics.json"), json);
        }
        return CosmeticJson;
    }
}
