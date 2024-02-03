// 
// Decompiled by Procyon v0.6.0
// 

package lumien.chunkanimator;

import lumien.chunkanimator.handler.AnimationHandler;

public class ChunkAnimator
{
    public int mode;
    public int animationDuration;
    public int easingFunction;
    public boolean disableAroundPlayer;
    private static final ChunkAnimator INSTANCE;
    public AnimationHandler animationHandler;
    
    static {
        INSTANCE = new ChunkAnimator();
    }
    
    public ChunkAnimator() {
        this.mode = 0;
        this.animationDuration = 2000;
        this.easingFunction = 10;
        this.disableAroundPlayer = false;
    }
    
    public static ChunkAnimator getInstance() {
        return ChunkAnimator.INSTANCE;
    }
    
    public void init() {
        this.animationHandler = new AnimationHandler();
    }
}
