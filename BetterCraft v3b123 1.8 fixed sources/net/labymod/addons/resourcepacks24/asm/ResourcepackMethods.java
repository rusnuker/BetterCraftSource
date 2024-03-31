// 
// Decompiled by Procyon v0.6.0
// 

package net.labymod.addons.resourcepacks24.asm;

import java.util.ArrayList;
import java.util.List;
import net.labymod.addons.resourcepacks24.Resourcepacks24;
import java.io.File;

public class ResourcepackMethods
{
    public static String modifyName(final String nameIn, final File file) {
        if (!file.getParentFile().equals(Resourcepacks24.getInstance().resourcepacksDir)) {
            return String.valueOf(file.getParentFile().getName()) + "/" + nameIn;
        }
        return nameIn;
    }
    
    public static List<File> getFiles(final Object screen) {
        final List<File> files = new ArrayList<File>();
        File[] listFiles;
        for (int length = (listFiles = Resourcepacks24.getInstance().resourcepacksDir.listFiles()).length, i = 0; i < length; ++i) {
            final File file = listFiles[i];
            if (isValidPack(file)) {
                files.add(file);
            }
            else if (file.isDirectory()) {
                File[] listFiles2;
                for (int length2 = (listFiles2 = file.listFiles()).length, j = 0; j < length2; ++j) {
                    final File subFile = listFiles2[j];
                    if (isValidPack(subFile)) {
                        files.add(subFile);
                    }
                }
            }
        }
        return files;
    }
    
    private static boolean isValidPack(final File file) {
        final boolean flag = file.isFile() && file.getName().endsWith(".zip");
        final boolean flag2 = file.isDirectory() && new File(file, "pack.mcmeta").isFile();
        return flag || flag2;
    }
}
