// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.addons.resourcepacks24;

import net.labymod.settings.elements.SettingsElement;
import java.util.List;
import net.labymod.addons.resourcepacks24.loader.PackRepositoryLoader;
import net.labymod.addons.resourcepacks24.api.Resourcepacks24Api;
import java.io.File;

public class Resourcepacks24
{
    private static final Resourcepacks24 INSTANCE;
    public final File resourcepacksDir;
    private Resourcepacks24Api rp24Api;
    private PackRepositoryLoader packLoader;
    
    static {
        INSTANCE = new Resourcepacks24();
    }
    
    public static Resourcepacks24 getInstance() {
        return Resourcepacks24.INSTANCE;
    }
    
    public Resourcepacks24() {
        this.resourcepacksDir = new File("resourcepacks");
        this.rp24Api = new Resourcepacks24Api("6b514bb5-cb55-4f68-8c62-3031cf871a72");
        this.packLoader = new PackRepositoryLoader(this.resourcepacksDir);
    }
    
    public void onEnable() {
    }
    
    public void loadConfig() {
    }
    
    protected void fillSettings(final List<SettingsElement> subSettings) {
    }
    
    public Resourcepacks24Api getRp24Api() {
        return this.rp24Api;
    }
    
    public PackRepositoryLoader getPackLoader() {
        return this.packLoader;
    }
}
