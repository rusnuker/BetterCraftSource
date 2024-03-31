// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.user;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.labymod.main.LabyMod;
import net.labymod.user.group.GroupManager;
import java.util.ArrayList;
import java.util.HashMap;
import net.labymod.user.cosmetic.custom.UserTextureContainer;
import net.labymod.user.group.LabyGroup;
import java.util.List;
import net.labymod.user.cosmetic.util.CosmeticData;
import java.util.Map;
import java.util.UUID;

public class User
{
    private UUID uuid;
    private Map<Integer, CosmeticData> cosmetics;
    private List<Short> emotes;
    private List<Short> stickerPacks;
    protected short playingSticker;
    protected long stickerStartedPlaying;
    private LabyGroup group;
    private boolean dailyEmoteFlat;
    private String subTitle;
    private double subTitleSize;
    private boolean mojangCapeModified;
    private boolean mojangCapeVisible;
    private long nextPriorityCheck;
    private final UserTextureContainer cloakContainer;
    private final UserTextureContainer bandanaContainer;
    private final UserTextureContainer shoesContainer;
    private final UserTextureContainer kawaiiMaskContainer;
    private final UserTextureContainer coverMaskContainer;
    private final UserTextureContainer watchContainer;
    private final UserTextureContainer angelWingsContainer;
    private float maxNameTagHeight;
    private UserManager userManager;
    
    public User(final UUID uuid) {
        this.cosmetics = new HashMap<Integer, CosmeticData>();
        this.emotes = new ArrayList<Short>();
        this.stickerPacks = new ArrayList<Short>();
        this.playingSticker = -1;
        this.group = GroupManager.DEFAULT_GROUP;
        this.mojangCapeModified = false;
        this.mojangCapeVisible = true;
        this.nextPriorityCheck = -1L;
        this.uuid = uuid;
        this.cloakContainer = new UserTextureContainer("../capes", this.uuid);
        this.bandanaContainer = new UserTextureContainer("bandanas", this.uuid);
        this.shoesContainer = new UserTextureContainer("shoes");
        this.kawaiiMaskContainer = new UserTextureContainer("kawaiimasks");
        this.coverMaskContainer = new UserTextureContainer("covermasks");
        this.watchContainer = new UserTextureContainer("watches");
        this.angelWingsContainer = new UserTextureContainer("angelwings");
        this.userManager = LabyMod.getInstance().getUserManager();
    }
    
    public boolean hasCosmeticById(final int id) {
        return this.cosmetics.containsKey(id);
    }
    
    public void resetMaxNameTagHeight() {
        this.maxNameTagHeight = 0.0f;
    }
    
    public void applyNameTagHeight(final float nameTagHeight) {
        if (nameTagHeight > this.maxNameTagHeight) {
            this.maxNameTagHeight = nameTagHeight;
        }
    }
    
    public void unloadCosmeticTextures() {
        this.cloakContainer.unload();
        this.bandanaContainer.unload();
        this.shoesContainer.unload();
        this.kawaiiMaskContainer.unload();
        this.coverMaskContainer.unload();
        this.watchContainer.unload();
        this.angelWingsContainer.unload();
    }
    
    public boolean canRenderMojangCape(final AbstractClientPlayer entitylivingbaseIn) {
        if (this.nextPriorityCheck > System.currentTimeMillis()) {
            return this.mojangCapeVisible;
        }
        this.nextPriorityCheck = System.currentTimeMillis() + 500L;
        return this.mojangCapeVisible = this.userManager.getCosmeticImageManager().getCloakImageHandler().canRenderMojangCape(this, entitylivingbaseIn);
    }
    
    public boolean isFamiliar() {
        return this.userManager.getFamiliarManager().isFamiliar(this.uuid);
    }
    
    public boolean isStickerVisible() {
        if (this.playingSticker == -1) {
            return false;
        }
        final long timePassed = System.currentTimeMillis() - this.stickerStartedPlaying;
        return timePassed < 4000L;
    }
    
    public void setGroup(final LabyGroup group) {
        this.group = ((group == null) ? GroupManager.DEFAULT_GROUP : group);
    }
    
    public UUID getUuid() {
        return this.uuid;
    }
    
    public Map<Integer, CosmeticData> getCosmetics() {
        return this.cosmetics;
    }
    
    public List<Short> getEmotes() {
        return this.emotes;
    }
    
    public List<Short> getStickerPacks() {
        return this.stickerPacks;
    }
    
    public short getPlayingSticker() {
        return this.playingSticker;
    }
    
    public long getStickerStartedPlaying() {
        return this.stickerStartedPlaying;
    }
    
    public LabyGroup getGroup() {
        return this.group;
    }
    
    public boolean isDailyEmoteFlat() {
        return this.dailyEmoteFlat;
    }
    
    public String getSubTitle() {
        return this.subTitle;
    }
    
    public double getSubTitleSize() {
        return this.subTitleSize;
    }
    
    public boolean isMojangCapeModified() {
        return this.mojangCapeModified;
    }
    
    public boolean isMojangCapeVisible() {
        return this.mojangCapeVisible;
    }
    
    public long getNextPriorityCheck() {
        return this.nextPriorityCheck;
    }
    
    public UserTextureContainer getCloakContainer() {
        return this.cloakContainer;
    }
    
    public UserTextureContainer getBandanaContainer() {
        return this.bandanaContainer;
    }
    
    public UserTextureContainer getShoesContainer() {
        return this.shoesContainer;
    }
    
    public UserTextureContainer getKawaiiMaskContainer() {
        return this.kawaiiMaskContainer;
    }
    
    public UserTextureContainer getCoverMaskContainer() {
        return this.coverMaskContainer;
    }
    
    public UserTextureContainer getWatchContainer() {
        return this.watchContainer;
    }
    
    public UserTextureContainer getAngelWingsContainer() {
        return this.angelWingsContainer;
    }
    
    public float getMaxNameTagHeight() {
        return this.maxNameTagHeight;
    }
    
    public UserManager getUserManager() {
        return this.userManager;
    }
    
    public void setCosmetics(final Map<Integer, CosmeticData> cosmetics) {
        this.cosmetics = cosmetics;
    }
    
    public void setEmotes(final List<Short> emotes) {
        this.emotes = emotes;
    }
    
    public void setStickerPacks(final List<Short> stickerPacks) {
        this.stickerPacks = stickerPacks;
    }
    
    public void setPlayingSticker(final short playingSticker) {
        this.playingSticker = playingSticker;
    }
    
    public void setStickerStartedPlaying(final long stickerStartedPlaying) {
        this.stickerStartedPlaying = stickerStartedPlaying;
    }
    
    public void setDailyEmoteFlat(final boolean dailyEmoteFlat) {
        this.dailyEmoteFlat = dailyEmoteFlat;
    }
    
    public void setSubTitle(final String subTitle) {
        this.subTitle = subTitle;
    }
    
    public void setSubTitleSize(final double subTitleSize) {
        this.subTitleSize = subTitleSize;
    }
    
    public void setMojangCapeModified(final boolean mojangCapeModified) {
        this.mojangCapeModified = mojangCapeModified;
    }
}
