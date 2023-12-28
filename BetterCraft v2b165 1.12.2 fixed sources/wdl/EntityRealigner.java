// 
// Decompiled by Procyon v0.6.0
// 

package wdl;

import net.minecraft.entity.Entity;
import wdl.api.IWDLModDescripted;
import wdl.api.IEntityEditor;

public class EntityRealigner implements IEntityEditor, IWDLModDescripted
{
    @Override
    public boolean isValidEnvironment(final String version) {
        return true;
    }
    
    @Override
    public String getEnvironmentErrorMessage(final String version) {
        return null;
    }
    
    @Override
    public String getDisplayName() {
        return "Entity realigner";
    }
    
    @Override
    public String getMainAuthor() {
        return "Pokechu22";
    }
    
    @Override
    public String[] getAuthors() {
        return null;
    }
    
    @Override
    public String getURL() {
        return null;
    }
    
    @Override
    public String getDescription() {
        return "Realigns entities to their serverside position to deal with entities that drift clientside (for example, boats).";
    }
    
    @Override
    public boolean shouldEdit(final Entity e) {
        return e.serverPosX != 0L || e.serverPosY != 0L || e.serverPosZ != 0L;
    }
    
    @Override
    public void editEntity(final Entity e) {
        e.posX = convertServerPos(e.serverPosX);
        e.posY = convertServerPos(e.serverPosY);
        e.posZ = convertServerPos(e.serverPosZ);
    }
    
    private static double convertServerPos(final long serverPos) {
        return serverPos / 4096.0;
    }
}
