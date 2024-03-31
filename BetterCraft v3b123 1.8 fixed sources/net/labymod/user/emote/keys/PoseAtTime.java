// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.user.emote.keys;

import java.beans.ConstructorProperties;

public class PoseAtTime
{
    private EmotePose pose;
    private long offset;
    private boolean animate;
    
    public void setPose(final EmotePose pose) {
        this.pose = pose;
    }
    
    public void setOffset(final long offset) {
        this.offset = offset;
    }
    
    public void setAnimate(final boolean animate) {
        this.animate = animate;
    }
    
    public EmotePose getPose() {
        return this.pose;
    }
    
    public long getOffset() {
        return this.offset;
    }
    
    public boolean isAnimate() {
        return this.animate;
    }
    
    @ConstructorProperties({ "pose", "offset", "animate" })
    public PoseAtTime(final EmotePose pose, final long offset, final boolean animate) {
        this.pose = pose;
        this.offset = offset;
        this.animate = animate;
    }
}
