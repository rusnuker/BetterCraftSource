// 
// Decompiled by Procyon v0.6.0
// 

package wdl;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.NetworkPlayerInfo;
import java.util.Iterator;
import net.minecraft.client.entity.AbstractClientPlayer;
import java.util.Collection;
import java.util.List;
import java.util.HashSet;
import java.util.HashMap;
import org.apache.logging.log4j.LogManager;
import net.minecraft.entity.player.EntityPlayer;
import java.util.Set;
import net.minecraft.util.ResourceLocation;
import java.util.UUID;
import java.util.Map;
import org.apache.logging.log4j.Logger;

public class CapeHandler
{
    private static final Logger logger;
    private static final Map<UUID, ResourceLocation> capes;
    private static final Set<EntityPlayer> handledPlayers;
    private static final Map<EntityPlayer, Integer> playerFailures;
    private static int totalFailures;
    private static final int MAX_PLAYER_FAILURES = 40;
    private static final int MAX_TOTAL_FAILURES = 40;
    
    static {
        logger = LogManager.getLogger();
        capes = new HashMap<UUID, ResourceLocation>();
        handledPlayers = new HashSet<EntityPlayer>();
        playerFailures = new HashMap<EntityPlayer, Integer>();
        CapeHandler.totalFailures = 0;
        CapeHandler.capes.put(UUID.fromString("6c8976e3-99a9-4d8b-a98e-d4c0c09b305b"), new ResourceLocation("wdl", "textures/cape_dev.png"));
        CapeHandler.capes.put(UUID.fromString("f6c068f1-0738-4b41-bdb2-69d81d2b0f1c"), new ResourceLocation("wdl", "textures/cape_dev.png"));
    }
    
    public static void onWorldTick(final List<EntityPlayer> players) {
        if (CapeHandler.totalFailures > 40) {
            return;
        }
        try {
            CapeHandler.handledPlayers.retainAll(players);
            for (final EntityPlayer player : players) {
                if (CapeHandler.handledPlayers.contains(player)) {
                    continue;
                }
                if (!(player instanceof AbstractClientPlayer)) {
                    continue;
                }
                setupPlayer((AbstractClientPlayer)player);
            }
        }
        catch (final Exception e) {
            CapeHandler.logger.warn("[WDL] Failed to tick cape setup", e);
            ++CapeHandler.totalFailures;
            if (CapeHandler.totalFailures > 40) {
                CapeHandler.logger.warn("[WDL] Disabling cape system (too many failures)");
            }
        }
    }
    
    private static void setupPlayer(final AbstractClientPlayer player) {
        try {
            final NetworkPlayerInfo info = ReflectionUtils.stealAndGetField(player, AbstractClientPlayer.class, NetworkPlayerInfo.class);
            if (info == null) {
                incrementFailure(player);
                return;
            }
            final GameProfile profile = info.getGameProfile();
            if (CapeHandler.capes.containsKey(profile.getId())) {
                setPlayerCape(info, CapeHandler.capes.get(profile.getId()));
            }
            CapeHandler.handledPlayers.add(player);
        }
        catch (final Exception e) {
            CapeHandler.logger.warn("[WDL] Failed to perform cape set up for " + player, e);
            incrementFailure(player);
        }
    }
    
    private static void setPlayerCape(final NetworkPlayerInfo info, final ResourceLocation cape) throws Exception {
        final Map<MinecraftProfileTexture.Type, ResourceLocation> map = ReflectionUtils.stealAndGetField(info, Map.class);
        if (!map.containsKey(MinecraftProfileTexture.Type.CAPE)) {
            map.put(MinecraftProfileTexture.Type.CAPE, cape);
        }
    }
    
    private static void incrementFailure(final EntityPlayer player) {
        if (CapeHandler.playerFailures.containsKey(player)) {
            final int numFailures = CapeHandler.playerFailures.get(player) + 1;
            CapeHandler.playerFailures.put(player, numFailures);
            if (numFailures > 40) {
                CapeHandler.handledPlayers.add(player);
                CapeHandler.playerFailures.remove(player);
                CapeHandler.logger.warn("[WDL] Failed to set up cape for " + player + " too many times (" + numFailures + "); skipping them");
            }
        }
        else {
            CapeHandler.playerFailures.put(player, 1);
        }
    }
}
