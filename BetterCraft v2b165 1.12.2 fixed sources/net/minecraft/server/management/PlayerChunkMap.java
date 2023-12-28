// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.server.management;

import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldProvider;
import java.util.Collections;
import com.google.common.collect.ComparisonChain;
import java.util.Comparator;
import com.google.common.collect.AbstractIterator;
import net.minecraft.world.chunk.Chunk;
import java.util.Iterator;
import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import com.google.common.collect.Lists;
import javax.annotation.Nullable;
import java.util.Set;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import java.util.List;
import net.minecraft.world.WorldServer;
import net.minecraft.entity.player.EntityPlayerMP;
import com.google.common.base.Predicate;

public class PlayerChunkMap
{
    private static final Predicate<EntityPlayerMP> NOT_SPECTATOR;
    private static final Predicate<EntityPlayerMP> CAN_GENERATE_CHUNKS;
    private final WorldServer theWorldServer;
    private final List<EntityPlayerMP> players;
    private final Long2ObjectMap<PlayerChunkMapEntry> playerInstances;
    private final Set<PlayerChunkMapEntry> playerInstancesToUpdate;
    private final List<PlayerChunkMapEntry> pendingSendToPlayers;
    private final List<PlayerChunkMapEntry> playersNeedingChunks;
    private final List<PlayerChunkMapEntry> playerInstanceList;
    private int playerViewRadius;
    private long previousTotalWorldTime;
    private boolean sortMissingChunks;
    private boolean sortSendToPlayers;
    
    static {
        NOT_SPECTATOR = new Predicate<EntityPlayerMP>() {
            @Override
            public boolean apply(@Nullable final EntityPlayerMP p_apply_1_) {
                return p_apply_1_ != null && !p_apply_1_.isSpectator();
            }
        };
        CAN_GENERATE_CHUNKS = new Predicate<EntityPlayerMP>() {
            @Override
            public boolean apply(@Nullable final EntityPlayerMP p_apply_1_) {
                return p_apply_1_ != null && (!p_apply_1_.isSpectator() || p_apply_1_.getServerWorld().getGameRules().getBoolean("spectatorsGenerateChunks"));
            }
        };
    }
    
    public PlayerChunkMap(final WorldServer serverWorld) {
        this.players = (List<EntityPlayerMP>)Lists.newArrayList();
        this.playerInstances = new Long2ObjectOpenHashMap<PlayerChunkMapEntry>(4096);
        this.playerInstancesToUpdate = (Set<PlayerChunkMapEntry>)Sets.newHashSet();
        this.pendingSendToPlayers = (List<PlayerChunkMapEntry>)Lists.newLinkedList();
        this.playersNeedingChunks = (List<PlayerChunkMapEntry>)Lists.newLinkedList();
        this.playerInstanceList = (List<PlayerChunkMapEntry>)Lists.newArrayList();
        this.sortMissingChunks = true;
        this.sortSendToPlayers = true;
        this.theWorldServer = serverWorld;
        this.setPlayerViewRadius(serverWorld.getMinecraftServer().getPlayerList().getViewDistance());
    }
    
    public WorldServer getWorldServer() {
        return this.theWorldServer;
    }
    
    public Iterator<Chunk> getChunkIterator() {
        final Iterator<PlayerChunkMapEntry> iterator = this.playerInstanceList.iterator();
        return new AbstractIterator<Chunk>() {
            @Override
            protected Chunk computeNext() {
                while (iterator.hasNext()) {
                    final PlayerChunkMapEntry playerchunkmapentry = iterator.next();
                    final Chunk chunk = playerchunkmapentry.getChunk();
                    if (chunk == null) {
                        continue;
                    }
                    if (!chunk.isLightPopulated() && chunk.isTerrainPopulated()) {
                        return chunk;
                    }
                    if (!chunk.isChunkTicked()) {
                        return chunk;
                    }
                    if (!playerchunkmapentry.hasPlayerMatchingInRange(128.0, PlayerChunkMap.NOT_SPECTATOR)) {
                        continue;
                    }
                    return chunk;
                }
                return this.endOfData();
            }
        };
    }
    
    public void tick() {
        final long i = this.theWorldServer.getTotalWorldTime();
        if (i - this.previousTotalWorldTime > 8000L) {
            this.previousTotalWorldTime = i;
            for (int j = 0; j < this.playerInstanceList.size(); ++j) {
                final PlayerChunkMapEntry playerchunkmapentry = this.playerInstanceList.get(j);
                playerchunkmapentry.update();
                playerchunkmapentry.updateChunkInhabitedTime();
            }
        }
        if (!this.playerInstancesToUpdate.isEmpty()) {
            for (final PlayerChunkMapEntry playerchunkmapentry2 : this.playerInstancesToUpdate) {
                playerchunkmapentry2.update();
            }
            this.playerInstancesToUpdate.clear();
        }
        if (this.sortMissingChunks && i % 4L == 0L) {
            this.sortMissingChunks = false;
            Collections.sort(this.playersNeedingChunks, new Comparator<PlayerChunkMapEntry>() {
                @Override
                public int compare(final PlayerChunkMapEntry p_compare_1_, final PlayerChunkMapEntry p_compare_2_) {
                    return ComparisonChain.start().compare(p_compare_1_.getClosestPlayerDistance(), p_compare_2_.getClosestPlayerDistance()).result();
                }
            });
        }
        if (this.sortSendToPlayers && i % 4L == 2L) {
            this.sortSendToPlayers = false;
            Collections.sort(this.pendingSendToPlayers, new Comparator<PlayerChunkMapEntry>() {
                @Override
                public int compare(final PlayerChunkMapEntry p_compare_1_, final PlayerChunkMapEntry p_compare_2_) {
                    return ComparisonChain.start().compare(p_compare_1_.getClosestPlayerDistance(), p_compare_2_.getClosestPlayerDistance()).result();
                }
            });
        }
        if (!this.playersNeedingChunks.isEmpty()) {
            final long l = System.nanoTime() + 50000000L;
            int k = 49;
            final Iterator<PlayerChunkMapEntry> iterator = this.playersNeedingChunks.iterator();
            while (iterator.hasNext()) {
                final PlayerChunkMapEntry playerchunkmapentry3 = iterator.next();
                if (playerchunkmapentry3.getChunk() == null) {
                    final boolean flag = playerchunkmapentry3.hasPlayerMatching(PlayerChunkMap.CAN_GENERATE_CHUNKS);
                    if (!playerchunkmapentry3.providePlayerChunk(flag)) {
                        continue;
                    }
                    iterator.remove();
                    if (playerchunkmapentry3.sendToPlayers()) {
                        this.pendingSendToPlayers.remove(playerchunkmapentry3);
                    }
                    if (--k < 0) {
                        break;
                    }
                    if (System.nanoTime() > l) {
                        break;
                    }
                    continue;
                }
            }
        }
        if (!this.pendingSendToPlayers.isEmpty()) {
            int i2 = 81;
            final Iterator<PlayerChunkMapEntry> iterator2 = this.pendingSendToPlayers.iterator();
            while (iterator2.hasNext()) {
                final PlayerChunkMapEntry playerchunkmapentry4 = iterator2.next();
                if (playerchunkmapentry4.sendToPlayers()) {
                    iterator2.remove();
                    if (--i2 < 0) {
                        break;
                    }
                    continue;
                }
            }
        }
        if (this.players.isEmpty()) {
            final WorldProvider worldprovider = this.theWorldServer.provider;
            if (!worldprovider.canRespawnHere()) {
                this.theWorldServer.getChunkProvider().unloadAllChunks();
            }
        }
    }
    
    public boolean contains(final int chunkX, final int chunkZ) {
        final long i = getIndex(chunkX, chunkZ);
        return this.playerInstances.get(i) != null;
    }
    
    @Nullable
    public PlayerChunkMapEntry getEntry(final int x, final int z) {
        return this.playerInstances.get(getIndex(x, z));
    }
    
    private PlayerChunkMapEntry getOrCreateEntry(final int chunkX, final int chunkZ) {
        final long i = getIndex(chunkX, chunkZ);
        PlayerChunkMapEntry playerchunkmapentry = this.playerInstances.get(i);
        if (playerchunkmapentry == null) {
            playerchunkmapentry = new PlayerChunkMapEntry(this, chunkX, chunkZ);
            this.playerInstances.put(i, playerchunkmapentry);
            this.playerInstanceList.add(playerchunkmapentry);
            if (playerchunkmapentry.getChunk() == null) {
                this.playersNeedingChunks.add(playerchunkmapentry);
            }
            if (!playerchunkmapentry.sendToPlayers()) {
                this.pendingSendToPlayers.add(playerchunkmapentry);
            }
        }
        return playerchunkmapentry;
    }
    
    public void markBlockForUpdate(final BlockPos pos) {
        final int i = pos.getX() >> 4;
        final int j = pos.getZ() >> 4;
        final PlayerChunkMapEntry playerchunkmapentry = this.getEntry(i, j);
        if (playerchunkmapentry != null) {
            playerchunkmapentry.blockChanged(pos.getX() & 0xF, pos.getY(), pos.getZ() & 0xF);
        }
    }
    
    public void addPlayer(final EntityPlayerMP player) {
        final int i = (int)player.posX >> 4;
        final int j = (int)player.posZ >> 4;
        player.managedPosX = player.posX;
        player.managedPosZ = player.posZ;
        for (int k = i - this.playerViewRadius; k <= i + this.playerViewRadius; ++k) {
            for (int l = j - this.playerViewRadius; l <= j + this.playerViewRadius; ++l) {
                this.getOrCreateEntry(k, l).addPlayer(player);
            }
        }
        this.players.add(player);
        this.markSortPending();
    }
    
    public void removePlayer(final EntityPlayerMP player) {
        final int i = (int)player.managedPosX >> 4;
        final int j = (int)player.managedPosZ >> 4;
        for (int k = i - this.playerViewRadius; k <= i + this.playerViewRadius; ++k) {
            for (int l = j - this.playerViewRadius; l <= j + this.playerViewRadius; ++l) {
                final PlayerChunkMapEntry playerchunkmapentry = this.getEntry(k, l);
                if (playerchunkmapentry != null) {
                    playerchunkmapentry.removePlayer(player);
                }
            }
        }
        this.players.remove(player);
        this.markSortPending();
    }
    
    private boolean overlaps(final int x1, final int z1, final int x2, final int z2, final int radius) {
        final int i = x1 - x2;
        final int j = z1 - z2;
        return i >= -radius && i <= radius && (j >= -radius && j <= radius);
    }
    
    public void updateMovingPlayer(final EntityPlayerMP player) {
        final int i = (int)player.posX >> 4;
        final int j = (int)player.posZ >> 4;
        final double d0 = player.managedPosX - player.posX;
        final double d2 = player.managedPosZ - player.posZ;
        final double d3 = d0 * d0 + d2 * d2;
        if (d3 >= 64.0) {
            final int k = (int)player.managedPosX >> 4;
            final int l = (int)player.managedPosZ >> 4;
            final int i2 = this.playerViewRadius;
            final int j2 = i - k;
            final int k2 = j - l;
            if (j2 != 0 || k2 != 0) {
                for (int l2 = i - i2; l2 <= i + i2; ++l2) {
                    for (int i3 = j - i2; i3 <= j + i2; ++i3) {
                        if (!this.overlaps(l2, i3, k, l, i2)) {
                            this.getOrCreateEntry(l2, i3).addPlayer(player);
                        }
                        if (!this.overlaps(l2 - j2, i3 - k2, i, j, i2)) {
                            final PlayerChunkMapEntry playerchunkmapentry = this.getEntry(l2 - j2, i3 - k2);
                            if (playerchunkmapentry != null) {
                                playerchunkmapentry.removePlayer(player);
                            }
                        }
                    }
                }
                player.managedPosX = player.posX;
                player.managedPosZ = player.posZ;
                this.markSortPending();
            }
        }
    }
    
    public boolean isPlayerWatchingChunk(final EntityPlayerMP player, final int chunkX, final int chunkZ) {
        final PlayerChunkMapEntry playerchunkmapentry = this.getEntry(chunkX, chunkZ);
        return playerchunkmapentry != null && playerchunkmapentry.containsPlayer(player) && playerchunkmapentry.isSentToPlayers();
    }
    
    public void setPlayerViewRadius(int radius) {
        radius = MathHelper.clamp(radius, 3, 32);
        if (radius != this.playerViewRadius) {
            final int i = radius - this.playerViewRadius;
            for (final EntityPlayerMP entityplayermp : Lists.newArrayList((Iterable<? extends EntityPlayerMP>)this.players)) {
                final int j = (int)entityplayermp.posX >> 4;
                final int k = (int)entityplayermp.posZ >> 4;
                if (i > 0) {
                    for (int j2 = j - radius; j2 <= j + radius; ++j2) {
                        for (int k2 = k - radius; k2 <= k + radius; ++k2) {
                            final PlayerChunkMapEntry playerchunkmapentry = this.getOrCreateEntry(j2, k2);
                            if (!playerchunkmapentry.containsPlayer(entityplayermp)) {
                                playerchunkmapentry.addPlayer(entityplayermp);
                            }
                        }
                    }
                }
                else {
                    for (int l = j - this.playerViewRadius; l <= j + this.playerViewRadius; ++l) {
                        for (int i2 = k - this.playerViewRadius; i2 <= k + this.playerViewRadius; ++i2) {
                            if (!this.overlaps(l, i2, j, k, radius)) {
                                this.getOrCreateEntry(l, i2).removePlayer(entityplayermp);
                            }
                        }
                    }
                }
            }
            this.playerViewRadius = radius;
            this.markSortPending();
        }
    }
    
    private void markSortPending() {
        this.sortMissingChunks = true;
        this.sortSendToPlayers = true;
    }
    
    public static int getFurthestViewableBlock(final int distance) {
        return distance * 16 - 16;
    }
    
    private static long getIndex(final int p_187307_0_, final int p_187307_1_) {
        return p_187307_0_ + 2147483647L | p_187307_1_ + 2147483647L << 32;
    }
    
    public void addEntry(final PlayerChunkMapEntry entry) {
        this.playerInstancesToUpdate.add(entry);
    }
    
    public void removeEntry(final PlayerChunkMapEntry entry) {
        final ChunkPos chunkpos = entry.getPos();
        final long i = getIndex(chunkpos.chunkXPos, chunkpos.chunkZPos);
        entry.updateChunkInhabitedTime();
        this.playerInstances.remove(i);
        this.playerInstanceList.remove(entry);
        this.playerInstancesToUpdate.remove(entry);
        this.pendingSendToPlayers.remove(entry);
        this.playersNeedingChunks.remove(entry);
        final Chunk chunk = entry.getChunk();
        if (chunk != null) {
            this.getWorldServer().getChunkProvider().unload(chunk);
        }
    }
}
