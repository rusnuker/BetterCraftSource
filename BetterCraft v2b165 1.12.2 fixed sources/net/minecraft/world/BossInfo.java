// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.world;

import net.minecraft.util.text.ITextComponent;
import java.util.UUID;

public abstract class BossInfo
{
    private final UUID uniqueId;
    protected ITextComponent name;
    protected float percent;
    protected Color color;
    protected Overlay overlay;
    protected boolean darkenSky;
    protected boolean playEndBossMusic;
    protected boolean createFog;
    
    public BossInfo(final UUID uniqueIdIn, final ITextComponent nameIn, final Color colorIn, final Overlay overlayIn) {
        this.uniqueId = uniqueIdIn;
        this.name = nameIn;
        this.color = colorIn;
        this.overlay = overlayIn;
        this.percent = 1.0f;
    }
    
    public UUID getUniqueId() {
        return this.uniqueId;
    }
    
    public ITextComponent getName() {
        return this.name;
    }
    
    public void setName(final ITextComponent nameIn) {
        this.name = nameIn;
    }
    
    public float getPercent() {
        return this.percent;
    }
    
    public void setPercent(final float percentIn) {
        this.percent = percentIn;
    }
    
    public Color getColor() {
        return this.color;
    }
    
    public void setColor(final Color colorIn) {
        this.color = colorIn;
    }
    
    public Overlay getOverlay() {
        return this.overlay;
    }
    
    public void setOverlay(final Overlay overlayIn) {
        this.overlay = overlayIn;
    }
    
    public boolean shouldDarkenSky() {
        return this.darkenSky;
    }
    
    public BossInfo setDarkenSky(final boolean darkenSkyIn) {
        this.darkenSky = darkenSkyIn;
        return this;
    }
    
    public boolean shouldPlayEndBossMusic() {
        return this.playEndBossMusic;
    }
    
    public BossInfo setPlayEndBossMusic(final boolean playEndBossMusicIn) {
        this.playEndBossMusic = playEndBossMusicIn;
        return this;
    }
    
    public BossInfo setCreateFog(final boolean createFogIn) {
        this.createFog = createFogIn;
        return this;
    }
    
    public boolean shouldCreateFog() {
        return this.createFog;
    }
    
    public enum Color
    {
        PINK("PINK", 0), 
        BLUE("BLUE", 1), 
        RED("RED", 2), 
        GREEN("GREEN", 3), 
        YELLOW("YELLOW", 4), 
        PURPLE("PURPLE", 5), 
        WHITE("WHITE", 6);
        
        private Color(final String s, final int n) {
        }
    }
    
    public enum Overlay
    {
        PROGRESS("PROGRESS", 0), 
        NOTCHED_6("NOTCHED_6", 1), 
        NOTCHED_10("NOTCHED_10", 2), 
        NOTCHED_12("NOTCHED_12", 3), 
        NOTCHED_20("NOTCHED_20", 4);
        
        private Overlay(final String s, final int n) {
        }
    }
}
