// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.world.storage;

public class WorldSavedDataCallableSave implements Runnable
{
    private final WorldSavedData data;
    
    public WorldSavedDataCallableSave(final WorldSavedData dataIn) {
        this.data = dataIn;
    }
    
    @Override
    public void run() {
        this.data.markDirty();
    }
}
