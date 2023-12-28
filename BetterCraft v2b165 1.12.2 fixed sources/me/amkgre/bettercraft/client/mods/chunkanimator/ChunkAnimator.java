// 
// Decompiled by Procyon v0.6.0
// 

package me.amkgre.bettercraft.client.mods.chunkanimator;

import me.amkgre.bettercraft.client.mods.chunkanimator.handler.AnimationHandler;

public class ChunkAnimator
{
    public static ChunkAnimator INSTANCE;
    public AnimationHandler animationHandler;
    public int mode;
    public int animationDuration;
    public int easingFunction;
    public boolean disableAroundPlayer;
    
    public ChunkAnimator() {
        this.mode = 0;
        this.animationDuration = 2000;
        this.easingFunction = 10;
        this.disableAroundPlayer = true;
        ChunkAnimator.INSTANCE = this;
    }
    
    public void onStart() {
        this.animationHandler = new AnimationHandler();
    }
}
