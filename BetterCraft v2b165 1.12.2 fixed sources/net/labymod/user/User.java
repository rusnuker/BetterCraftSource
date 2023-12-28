// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.user;

import java.util.ArrayList;
import java.util.HashMap;
import net.labymod.main.LabyMod;
import net.labymod.user.util.EnumUserRank;
import java.util.List;
import net.labymod.user.util.CosmeticData;
import java.util.Map;
import java.util.UUID;

public class User
{
    private UUID uuid;
    private Map<Integer, CosmeticData> cosmetics;
    private List<Short> emotes;
    private EnumUserRank rank;
    private boolean rankVisible;
    private String subTitle;
    private double subTitleSize;
    private boolean mojangCapeModified;
    private boolean mojangCapeVisible;
    private long nextPriorityCheck;
    private float maxNameTagHeight;
    private UserManager userManager;
    private LabyMod labyMod;
    
    public User(final UUID uuid, final LabyMod labyMod) {
        this.cosmetics = new HashMap<Integer, CosmeticData>();
        this.emotes = new ArrayList<Short>();
        this.rank = EnumUserRank.USER;
        this.rankVisible = false;
        this.mojangCapeModified = false;
        this.mojangCapeVisible = true;
        this.nextPriorityCheck = -1L;
        this.uuid = uuid;
        this.labyMod = labyMod;
        this.userManager = labyMod.getUserManager();
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
    }
    
    public boolean canRenderMojangCape() {
        return false;
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
    
    public EnumUserRank getRank() {
        return this.rank;
    }
    
    public boolean isRankVisible() {
        return this.rankVisible;
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
    
    public void setRank(final EnumUserRank rank) {
        this.rank = rank;
    }
    
    public void setRankVisible(final boolean rankVisible) {
        this.rankVisible = rankVisible;
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
