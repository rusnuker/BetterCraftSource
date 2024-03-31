// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.gui;

import net.minecraft.network.play.server.SPacketUpdateBossInfo;
import java.util.Iterator;
import net.minecraft.world.BossInfo;
import net.minecraft.client.renderer.GlStateManager;
import com.google.common.collect.Maps;
import java.util.UUID;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class GuiBossOverlay extends Gui
{
    private static final ResourceLocation GUI_BARS_TEXTURES;
    private final Minecraft client;
    private final Map<UUID, BossInfoClient> mapBossInfos;
    
    static {
        GUI_BARS_TEXTURES = new ResourceLocation("textures/gui/bars.png");
    }
    
    public GuiBossOverlay(final Minecraft clientIn) {
        this.mapBossInfos = (Map<UUID, BossInfoClient>)Maps.newLinkedHashMap();
        this.client = clientIn;
    }
    
    public void renderBossHealth() {
        if (!this.mapBossInfos.isEmpty()) {
            final ScaledResolution scaledresolution = new ScaledResolution(this.client);
            final int i = ScaledResolution.getScaledWidth();
            int j = 12;
            for (final BossInfoClient bossinfoclient : this.mapBossInfos.values()) {
                final int k = i / 2 - 91;
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                this.client.getTextureManager().bindTexture(GuiBossOverlay.GUI_BARS_TEXTURES);
                this.render(k, j, bossinfoclient);
                final String s = bossinfoclient.getName().getFormattedText();
                this.client.fontRendererObj.drawStringWithShadow(s, (float)(i / 2 - this.client.fontRendererObj.getStringWidth(s) / 2), (float)(j - 9), 16777215);
                j += 10 + this.client.fontRendererObj.FONT_HEIGHT;
                if (j >= ScaledResolution.getScaledHeight() / 3) {
                    break;
                }
            }
        }
    }
    
    private void render(final int x, final int y, final BossInfo info) {
        this.drawTexturedModalRect(x, y, 0, info.getColor().ordinal() * 5 * 2, 182, 5);
        if (info.getOverlay() != BossInfo.Overlay.PROGRESS) {
            this.drawTexturedModalRect(x, y, 0, 80 + (info.getOverlay().ordinal() - 1) * 5 * 2, 182, 5);
        }
        final int i = (int)(info.getPercent() * 183.0f);
        if (i > 0) {
            this.drawTexturedModalRect(x, y, 0, info.getColor().ordinal() * 5 * 2 + 5, i, 5);
            if (info.getOverlay() != BossInfo.Overlay.PROGRESS) {
                this.drawTexturedModalRect(x, y, 0, 80 + (info.getOverlay().ordinal() - 1) * 5 * 2 + 5, i, 5);
            }
        }
    }
    
    public void read(final SPacketUpdateBossInfo packetIn) {
        if (packetIn.getOperation() == SPacketUpdateBossInfo.Operation.ADD) {
            this.mapBossInfos.put(packetIn.getUniqueId(), new BossInfoClient(packetIn));
        }
        else if (packetIn.getOperation() == SPacketUpdateBossInfo.Operation.REMOVE) {
            this.mapBossInfos.remove(packetIn.getUniqueId());
        }
        else {
            this.mapBossInfos.get(packetIn.getUniqueId()).updateFromPacket(packetIn);
        }
    }
    
    public void clearBossInfos() {
        this.mapBossInfos.clear();
    }
    
    public boolean shouldPlayEndBossMusic() {
        if (!this.mapBossInfos.isEmpty()) {
            for (final BossInfo bossinfo : this.mapBossInfos.values()) {
                if (bossinfo.shouldPlayEndBossMusic()) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean shouldDarkenSky() {
        if (!this.mapBossInfos.isEmpty()) {
            for (final BossInfo bossinfo : this.mapBossInfos.values()) {
                if (bossinfo.shouldDarkenSky()) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean shouldCreateFog() {
        if (!this.mapBossInfos.isEmpty()) {
            for (final BossInfo bossinfo : this.mapBossInfos.values()) {
                if (bossinfo.shouldCreateFog()) {
                    return true;
                }
            }
        }
        return false;
    }
}
