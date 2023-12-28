// 
// Decompiled by Procyon v0.6.0
// 

package optifine;

import java.util.HashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import java.util.Map;

public class PlayerConfigurations
{
    private static Map mapConfigurations;
    private static boolean reloadPlayerItems;
    private static long timeReloadPlayerItemsMs;
    
    static {
        PlayerConfigurations.mapConfigurations = null;
        PlayerConfigurations.reloadPlayerItems = Boolean.getBoolean("player.models.reload");
        PlayerConfigurations.timeReloadPlayerItemsMs = System.currentTimeMillis();
    }
    
    public static void renderPlayerItems(final ModelBiped p_renderPlayerItems_0_, final AbstractClientPlayer p_renderPlayerItems_1_, final float p_renderPlayerItems_2_, final float p_renderPlayerItems_3_) {
        final PlayerConfiguration playerconfiguration = getPlayerConfiguration(p_renderPlayerItems_1_);
        if (playerconfiguration != null) {
            playerconfiguration.renderPlayerItems(p_renderPlayerItems_0_, p_renderPlayerItems_1_, p_renderPlayerItems_2_, p_renderPlayerItems_3_);
        }
    }
    
    public static synchronized PlayerConfiguration getPlayerConfiguration(final AbstractClientPlayer p_getPlayerConfiguration_0_) {
        if (PlayerConfigurations.reloadPlayerItems && System.currentTimeMillis() > PlayerConfigurations.timeReloadPlayerItemsMs + 5000L) {
            final AbstractClientPlayer abstractclientplayer = Minecraft.getMinecraft().player;
            if (abstractclientplayer != null) {
                setPlayerConfiguration(abstractclientplayer.getNameClear(), null);
                PlayerConfigurations.timeReloadPlayerItemsMs = System.currentTimeMillis();
            }
        }
        final String s1 = p_getPlayerConfiguration_0_.getNameClear();
        if (s1 == null) {
            return null;
        }
        PlayerConfiguration playerconfiguration = getMapConfigurations().get(s1);
        if (playerconfiguration == null) {
            playerconfiguration = new PlayerConfiguration();
            getMapConfigurations().put(s1, playerconfiguration);
            final PlayerConfigurationReceiver playerconfigurationreceiver = new PlayerConfigurationReceiver(s1);
            final String s2 = String.valueOf(HttpUtils.getPlayerItemsUrl()) + "/users/" + s1 + ".cfg";
            final FileDownloadThread filedownloadthread = new FileDownloadThread(s2, playerconfigurationreceiver);
            filedownloadthread.start();
        }
        return playerconfiguration;
    }
    
    public static synchronized void setPlayerConfiguration(final String p_setPlayerConfiguration_0_, final PlayerConfiguration p_setPlayerConfiguration_1_) {
        getMapConfigurations().put(p_setPlayerConfiguration_0_, p_setPlayerConfiguration_1_);
    }
    
    private static Map getMapConfigurations() {
        if (PlayerConfigurations.mapConfigurations == null) {
            PlayerConfigurations.mapConfigurations = new HashMap();
        }
        return PlayerConfigurations.mapConfigurations;
    }
}
