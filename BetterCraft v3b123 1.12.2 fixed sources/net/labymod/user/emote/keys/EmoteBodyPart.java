// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.user.emote.keys;

public class EmoteBodyPart
{
    private int id;
    private long animationStart;
    private long animationDuration;
    private long prevOffset;
    private float x;
    private float y;
    private float z;
    private float rootX;
    private float rootY;
    private float rootZ;
    private float targetX;
    private float targetY;
    private float targetZ;
    private boolean interpolate;
    
    public EmoteBodyPart(final int id) {
        this.animationStart = 0L;
        this.animationDuration = 0L;
        this.prevOffset = 0L;
        this.x = 0.0f;
        this.y = 0.0f;
        this.z = 0.0f;
        this.rootX = 0.0f;
        this.rootY = 0.0f;
        this.rootZ = 0.0f;
        this.targetX = 0.0f;
        this.targetY = 0.0f;
        this.targetZ = 0.0f;
        this.id = id;
    }
    
    public void applyPose(final EmotePose emoteKeyFrame, final long duration) {
        this.rootX = this.targetX;
        this.rootY = this.targetY;
        this.rootZ = this.targetZ;
        this.targetX = emoteKeyFrame.getX() / 57.295776f;
        this.targetY = emoteKeyFrame.getY() / 57.295776f;
        this.targetZ = emoteKeyFrame.getZ() / 57.295776f;
        this.animationStart = System.currentTimeMillis();
        this.animationDuration = duration - this.prevOffset;
        this.prevOffset = duration;
        this.interpolate = emoteKeyFrame.isInterpolate();
    }
    
    public void cancel() {
        this.rootX = this.targetX;
        this.rootY = this.targetY;
        this.rootZ = this.targetZ;
        this.targetX = this.x;
        this.targetY = this.y;
        this.targetZ = this.z;
        this.animationStart = System.currentTimeMillis();
        this.animationDuration = 0L;
        this.prevOffset = 0L;
    }
    
    public void animateOnTime() {
        final long progress = System.currentTimeMillis() - this.animationStart;
        this.x = process(this.rootX, this.targetX, progress, this.animationDuration, this.interpolate);
        this.y = process(this.rootY, this.targetY, progress, this.animationDuration, this.interpolate);
        this.z = process(this.rootZ, this.targetZ, progress, this.animationDuration, this.interpolate);
    }
    
    public static float process(final float root, final float target, final long progress, final long animationDuration, final boolean interpolate) {
        if (root == target || animationDuration == 0L || progress > animationDuration) {
            return target;
        }
        if (interpolate) {
            return interpolate(root, target, (float)progress, (float)animationDuration);
        }
        return linear(root, target, progress, animationDuration);
    }
    
    public static float interpolate(final float startY, final float endY, final float currentTime, final float endTime) {
        if (startY == endY || endTime == 0.0f || currentTime > endTime) {
            return endY;
        }
        return startY + sigmoid(currentTime / endTime * 4.0f) * (endY - startY);
    }
    
    public static float linear(final float root, final float target, final long progress, final long animationDuration) {
        if (root == target || animationDuration == 0L || progress > animationDuration) {
            return target;
        }
        final float difference = root - target;
        return root - difference / animationDuration * progress;
    }
    
    private static float sigmoid(final float input) {
        return (float)(1.0 / (1.0 + Math.exp(-input * 2.0f + 4.0f)));
    }
    
    public int getId() {
        return this.id;
    }
    
    public long getAnimationStart() {
        return this.animationStart;
    }
    
    public long getAnimationDuration() {
        return this.animationDuration;
    }
    
    public long getPrevOffset() {
        return this.prevOffset;
    }
    
    public float getX() {
        return this.x;
    }
    
    public float getY() {
        return this.y;
    }
    
    public float getZ() {
        return this.z;
    }
    
    public float getRootX() {
        return this.rootX;
    }
    
    public float getRootY() {
        return this.rootY;
    }
    
    public float getRootZ() {
        return this.rootZ;
    }
    
    public float getTargetX() {
        return this.targetX;
    }
    
    public float getTargetY() {
        return this.targetY;
    }
    
    public float getTargetZ() {
        return this.targetZ;
    }
    
    public boolean isInterpolate() {
        return this.interpolate;
    }
}
