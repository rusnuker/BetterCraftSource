// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.world.storage;

import net.minecraft.world.gen.structure.template.TemplateManager;
import java.io.File;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.chunk.storage.IChunkLoader;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.MinecraftException;

public class SaveHandlerMP implements ISaveHandler
{
    @Override
    public WorldInfo loadWorldInfo() {
        return null;
    }
    
    @Override
    public void checkSessionLock() throws MinecraftException {
    }
    
    @Override
    public IChunkLoader getChunkLoader(final WorldProvider provider) {
        return null;
    }
    
    @Override
    public void saveWorldInfoWithPlayer(final WorldInfo worldInformation, final NBTTagCompound tagCompound) {
    }
    
    @Override
    public void saveWorldInfo(final WorldInfo worldInformation) {
    }
    
    @Override
    public IPlayerFileData getPlayerNBTManager() {
        return null;
    }
    
    @Override
    public void flush() {
    }
    
    @Override
    public File getMapFileFromName(final String mapName) {
        return null;
    }
    
    @Override
    public File getWorldDirectory() {
        return null;
    }
    
    @Override
    public TemplateManager getStructureTemplateManager() {
        return null;
    }
}
