// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.world.chunk.storage;

import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.util.Iterator;
import java.io.IOException;
import com.google.common.collect.Maps;
import java.io.File;
import java.util.Map;

public class RegionFileCache
{
    private static final Map<File, RegionFile> REGIONS_BY_FILE;
    
    static {
        REGIONS_BY_FILE = Maps.newHashMap();
    }
    
    public static synchronized RegionFile createOrLoadRegionFile(final File worldDir, final int chunkX, final int chunkZ) {
        final File file1 = new File(worldDir, "region");
        final File file2 = new File(file1, "r." + (chunkX >> 5) + "." + (chunkZ >> 5) + ".mca");
        final RegionFile regionfile = RegionFileCache.REGIONS_BY_FILE.get(file2);
        if (regionfile != null) {
            return regionfile;
        }
        if (!file1.exists()) {
            file1.mkdirs();
        }
        if (RegionFileCache.REGIONS_BY_FILE.size() >= 256) {
            clearRegionFileReferences();
        }
        final RegionFile regionfile2 = new RegionFile(file2);
        RegionFileCache.REGIONS_BY_FILE.put(file2, regionfile2);
        return regionfile2;
    }
    
    public static synchronized RegionFile func_191065_b(final File p_191065_0_, final int p_191065_1_, final int p_191065_2_) {
        final File file1 = new File(p_191065_0_, "region");
        final File file2 = new File(file1, "r." + (p_191065_1_ >> 5) + "." + (p_191065_2_ >> 5) + ".mca");
        final RegionFile regionfile = RegionFileCache.REGIONS_BY_FILE.get(file2);
        if (regionfile != null) {
            return regionfile;
        }
        if (file1.exists() && file2.exists()) {
            if (RegionFileCache.REGIONS_BY_FILE.size() >= 256) {
                clearRegionFileReferences();
            }
            final RegionFile regionfile2 = new RegionFile(file2);
            RegionFileCache.REGIONS_BY_FILE.put(file2, regionfile2);
            return regionfile2;
        }
        return null;
    }
    
    public static synchronized void clearRegionFileReferences() {
        for (final RegionFile regionfile : RegionFileCache.REGIONS_BY_FILE.values()) {
            try {
                if (regionfile == null) {
                    continue;
                }
                regionfile.close();
            }
            catch (final IOException ioexception) {
                ioexception.printStackTrace();
            }
        }
        RegionFileCache.REGIONS_BY_FILE.clear();
    }
    
    public static DataInputStream getChunkInputStream(final File worldDir, final int chunkX, final int chunkZ) {
        final RegionFile regionfile = createOrLoadRegionFile(worldDir, chunkX, chunkZ);
        return regionfile.getChunkDataInputStream(chunkX & 0x1F, chunkZ & 0x1F);
    }
    
    public static DataOutputStream getChunkOutputStream(final File worldDir, final int chunkX, final int chunkZ) {
        final RegionFile regionfile = createOrLoadRegionFile(worldDir, chunkX, chunkZ);
        return regionfile.getChunkDataOutputStream(chunkX & 0x1F, chunkZ & 0x1F);
    }
    
    public static boolean func_191064_f(final File p_191064_0_, final int p_191064_1_, final int p_191064_2_) {
        final RegionFile regionfile = func_191065_b(p_191064_0_, p_191064_1_, p_191064_2_);
        return regionfile != null && regionfile.isChunkSaved(p_191064_1_ & 0x1F, p_191064_2_ & 0x1F);
    }
}
