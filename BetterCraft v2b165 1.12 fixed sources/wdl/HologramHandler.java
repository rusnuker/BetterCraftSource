// 
// Decompiled by Procyon v0.6.0
// 

package wdl;

import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.Entity;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import wdl.api.IWDLModDescripted;
import wdl.api.ISpecialEntityHandler;

public class HologramHandler implements ISpecialEntityHandler, IWDLModDescripted
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
        return "Hologram support";
    }
    
    @Override
    public Multimap<String, String> getSpecialEntities() {
        final Multimap<String, String> returned = (Multimap<String, String>)HashMultimap.create();
        returned.put("ArmorStand", "Hologram");
        return returned;
    }
    
    @Override
    public String getSpecialEntityName(final Entity entity) {
        if (entity instanceof EntityArmorStand && entity.isInvisible() && entity.hasCustomName()) {
            return "Hologram";
        }
        return null;
    }
    
    @Override
    public String getSpecialEntityCategory(final String name) {
        if (name.equals("Hologram")) {
            return "Other";
        }
        return null;
    }
    
    @Override
    public int getSpecialEntityTrackDistance(final String name) {
        return -1;
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
        return "Provides basic support for disabling holograms.";
    }
}
