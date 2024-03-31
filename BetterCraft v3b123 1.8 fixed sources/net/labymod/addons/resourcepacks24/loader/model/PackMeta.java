// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.addons.resourcepacks24.loader.model;

import net.labymod.addons.resourcepacks24.gui.elements.basement.PackElement;
import net.minecraft.util.ResourceLocation;
import java.io.File;

public class PackMeta
{
    public File file;
    public MCPack pack;
    public String displayName;
    public ResourceLocation icon;
    
    public PackMeta(final MCPack pack) {
        this.icon = PackElement.DEFAULT_ICON;
        this.pack = pack;
    }
    
    public PackMeta(final File file, final MCPack pack, final String displayName) {
        this.icon = PackElement.DEFAULT_ICON;
        this.file = file;
        this.pack = pack;
        this.displayName = displayName;
    }
    
    public String getFileName() {
        return this.file.getName();
    }
}
